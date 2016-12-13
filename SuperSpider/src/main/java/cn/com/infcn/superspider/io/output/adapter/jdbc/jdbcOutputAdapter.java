package cn.com.infcn.superspider.io.output.adapter.jdbc;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;

import cn.com.infcn.superspider.common.BasicDao;
import cn.com.infcn.superspider.common.Constant;
import cn.com.infcn.superspider.io.mq.disk.DiskBlockingQueue;
import cn.com.infcn.superspider.io.output.DbModel;
import cn.com.infcn.superspider.io.output.Doc;
import cn.com.infcn.superspider.io.output.OutputDispatcherFactory;
import cn.com.infcn.superspider.io.output.OutputAdapter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.justme.superspider.xml.Site;
/**
 * 
 * jdbc输出
 * @author WChao
 *
 */
public class jdbcOutputAdapter extends OutputAdapter {

	Logger logger = Logger.getLogger(jdbcOutputAdapter.class);
	private BasicDao basicDao = null;
	public jdbcOutputAdapter(){}
	
	public jdbcOutputAdapter(JSONObject obj, Site site) throws Throwable {
		this.site = site;
		init(obj);
	}
	
	public void init(JSONObject obj){
		//1)初始化磁盘队列;
		this.support = OutputDispatcherFactory.pool.getDiskQueue("jdbcQueue_"+site.getId());
		//2) decorate to a blocking queue
		queue = new DiskBlockingQueue(this.support);
		dbModel = new DbModel(obj.getString(Constant.HOST), Integer.parseInt(obj.getString(Constant.PORT)),obj.getString(Constant.TYPE), obj.getString(Constant.DBNAME), obj.getString(Constant.USERNAME), obj.getString(Constant.PASSWORD), obj.getString(Constant.TABLE_NAME));
		basicDao = new BasicDao(dbModel.getDriverClassName(),dbModel.getJdbcurl(),dbModel.getUsername(),dbModel.getPassword());
        this.thread = obj.getString(Constant.THREAD) == null ? 1 : obj.getInteger(Constant.THREAD);
        JSONObject tableStruct = JSONObject.parseObject(obj.getString(Constant.TABLE_STRUCT));
        this.dbModel.outputOption = obj;
        this.dbModel.tableStruct = tableStruct;
        String is_clear = tableStruct.getString(Constant.IS_CLEAR);
        if("1".equals(is_clear))//清除持久化队列;
        queue.clear();
        CreateTable(tableStruct);
	}
	
	@Override
	public void save(Doc doc)throws Exception{
		//同步信号量,在插入数据库时进行同步控制;
		JSONObject targetFields = this.dbModel.tableStruct.getJSONObject(Constant.TARGET_FIELDS);
		//增量标识;
		String increment = this.dbModel.tableStruct.getString(Constant.INCREMENT);
		//增量主键字段;
		String primaryField = this.dbModel.tableStruct.getString(Constant.INCREMENT_PRIMARY_FIELD);
		if(targetFields == null || targetFields.size() == 0){
			logger.info("没有选择要同步的字段...");
			return;
		}
		if(doc.getResult() == null || doc.getResult().size() == 0)
			return;
		JSONArray insertResult = new JSONArray();//新增数据;
		JSONArray updateResult = new JSONArray();//修改数据;
		JSONArray deleteResult = new JSONArray();//删除数据;
		
		if (dbModel.getTablename() == null) {
			throw new Exception("not set tableName! in " + dbModel.toString().replace("\n", " "));
		}
		for(int i=0 ;i<doc.getResult().size();i++)
		{
			if(site.isStop)
				break;
			JSONObject jsonObject = doc.getResult().getJSONObject(i);
			if(Constant.ORACLE.equals(dbModel.getType())||Constant.SQLSERVER.equals(dbModel.getType())||Constant.DB2.equals(dbModel.getType()))
			{
				jsonObject.put(".table","\""+dbModel.getTablename()+"\"");
			}else{
				jsonObject.put(".table","`"+dbModel.getTablename()+"`");
			}
			jsonObject.remove("$target");//移除爬虫内置参数
			processByteData(jsonObject);//处理blob大字段转化byte[]后的数据;
			processIncrementData(insertResult,updateResult,deleteResult,jsonObject);//处理增量爬取;
		}
		if(!"1".equals(increment)){
			insertResult = doc.getResult();
		}else{//增量爬取;
			if(deleteResult.size() > 0){//删除操作;
				List<Object> ids = new ArrayList<Object>();
				for(Object deleteObj : deleteResult){
					JSONObject deleteJsonObj = (JSONObject)deleteObj;
					Object id = deleteJsonObj.get(primaryField);
					ids.add(id);
				}
				long start = System.currentTimeMillis();
				basicDao.deleteByIds(dbModel.getTablename(),primaryField,ids);
				long end = System.currentTimeMillis();
				logger.info("删除"+deleteResult.size()+"条,耗时"+(end-start)+"毫秒");
			}
			if(updateResult.size() > 0){//更新操作;
				long start = System.currentTimeMillis();
				for(Object updateObj : updateResult){
					JSONObject updateJsonObj = (JSONObject)updateObj;
					if(targetFields.getString(primaryField) == null)
					updateJsonObj.remove(primaryField);
					basicDao.update(dbModel.getTablename(),updateJsonObj, primaryField);
				}
				long end = System.currentTimeMillis();
				logger.info("更新"+updateResult.size()+"条,耗时"+(end-start)+"毫秒");
			}
		}
		if(basicDao != null)
		{
			long start = System.currentTimeMillis();
			basicDao.saveAll(insertResult);
			long end = System.currentTimeMillis();
			logger.info("保存"+insertResult.size()+"条,耗时"+(end-start)+"毫秒");
		}
	}
	/**
	 * 处理byte的blob字段数据;
	 * @param jsonObj
	 */
	public void processByteData(JSONObject jsonObj)
	{
		for(String key : jsonObj.keySet())
		{
			Object value = jsonObj.get(key);
			if(value instanceof byte[])
			{
				jsonObj.put(key,new ByteArrayInputStream((byte[])value));
			}
		}
	}
	/**
	 * 处理增量数据;
	 * @param jsonObj
	 */
	public void processIncrementData(JSONArray insertResult,JSONArray updateResult,JSONArray deleteResult,JSONObject jsonObj){
		//同步信号量,在插入数据库时进行同步控制;
		JSONObject targetFields = this.dbModel.tableStruct.getJSONObject(Constant.TARGET_FIELDS);
		//增量标识;
		String increment = this.dbModel.tableStruct.getString(Constant.INCREMENT);
		//增量主键字段;
		String primaryField = this.dbModel.tableStruct.getString(Constant.INCREMENT_PRIMARY_FIELD) == null ? "" : this.dbModel.tableStruct.getString(Constant.INCREMENT_PRIMARY_FIELD).toString();
		if(!"1".equals(increment))
			return;
		if(jsonObj.containsKey(Constant.TRIGGER_STATUS)){
			String status = jsonObj.getString(Constant.TRIGGER_STATUS);
			if("insert".equals(status)){
				insertResult.add(jsonObj);
				if(targetFields.getString(primaryField) == null)
				jsonObj.remove(primaryField);
			}else if("update".equals(status)){
				updateResult.add(jsonObj);
			}else if("delete".equals(status)){
				deleteResult.add(jsonObj);
			}
			jsonObj.remove(Constant.TRIGGER_STATUS);
		}else{
			insertResult.add(jsonObj);
			if(targetFields.getString(primaryField) == null)
			jsonObj.remove(primaryField);
		}
	}
	
	public void CreateTable(JSONObject tableStruct)
	{
		JSONObject targetFields = tableStruct.getJSONObject(Constant.TARGET_FIELDS);
		String is_clear = tableStruct.getString(Constant.IS_CLEAR);
		//增量标识;
		//String increment = this.dbModel.tableStruct.getString(Constant.INCREMENT);
		boolean create_success = true;
		//String schema = this.dbModel.outputOption.getString(Constant.SCHEMA).equals("")?"":this.dbModel.outputOption.getString(Constant.SCHEMA)+".";
		if(targetFields == null || targetFields.size() == 0)
		return;
		try {
			if("1".equals(is_clear))
			{
				String tableName = dbModel.getTablename();
				if(Constant.ORACLE.equals(dbModel.getType())||Constant.SQLSERVER.equals(dbModel.getType())||Constant.DB2.equals(dbModel.getType()))
				{
					tableName = "\""+dbModel.getTablename()+"\"";
				}else{
					tableName = "`"+dbModel.getTablename()+"`";
				}
				String deleteSql = "drop table "+tableName;
				basicDao.executeSql(deleteSql);
			}
			StringBuffer sqlStr = new StringBuffer();
			if(Constant.ORACLE.equals(dbModel.getType()))
			{
				sqlStr.append("select count(*) as table_num from user_tables where table_name = '"+dbModel.getTablename().toUpperCase()+"'");
				Record record = basicDao.selectOne(sqlStr.toString());
				int table_num = record.getInt("table_num");
				if(table_num == 0)
				{
					sqlStr.setLength(0);
					sqlStr.append("CREATE TABLE \"$table\"(");
				}else{
					return;
				}
			}else if(Constant.SQLSERVER.equals(dbModel.getType())){
				sqlStr.append("if not exists ( select * from  sysobjects where name = '"+dbModel.getTablename()+"' and type = 'U')");
				sqlStr.append("CREATE TABLE \"$table\"(");
			}else if(Constant.DB2.equals(dbModel.getType())){
				sqlStr.append("select 1 from syscat.tables where tabname = upper('"+dbModel.getTablename().toUpperCase()+"')");
				Record record = basicDao.selectOne(sqlStr.toString());
				if(record == null)
				{
					sqlStr.setLength(0);
					sqlStr.append("CREATE TABLE \"$table\"(");
				}else{
					return;
				}
			}else{
				sqlStr = new StringBuffer("CREATE TABLE IF NOT EXISTS `$table`(");
			}
			for(Object obj : targetFields.keySet())
			{
				sqlStr.append(obj+" "+targetFields.get(obj));
				sqlStr.append(",");
			}
			String sqlString = sqlStr.toString();
			sqlString = sqlString.substring(0,sqlString.lastIndexOf(",")) + ")";
			Sql sql =Sqls.create(sqlString);
			sql.vars().set("table",dbModel.getTablename());
			if(basicDao != null)
			basicDao.executeSql(sql);
		} catch (Exception e) {
			create_success = false;
			logger.error("在CreateTable中执行Sql出现异常..."+e.getMessage(),e);
		}finally{
			if("1".equals(is_clear) && create_success)
			{
				
			}
		}
	}
	
	@Override
	public void shutdown(boolean isShutdownNow) {
		try {
			if(this.pool != null)
			{
				if (isShutdownNow)
					this.pool.shutdownNow();
				else
					this.pool.shutdown();
			}
			if(basicDao != null)
			{
				basicDao.getDs().close();
				basicDao = null;
			}
		}catch(Throwable e){
			logger.error(Thread.currentThread()+"OutputAdapter.name->"+site.getName()+".ThreadPool shutdown failed.",e);
		}finally{
			if(queue != null)
			{
				 OutputDispatcherFactory.pool.releaseDiskQueue(this.support);//关闭磁盘队列;
			}
		}
	}

	public BasicDao getBasicDao() {
		return basicDao;
	}

	public void setBasicDao(BasicDao basicDao) {
		this.basicDao = basicDao;
	}
}
