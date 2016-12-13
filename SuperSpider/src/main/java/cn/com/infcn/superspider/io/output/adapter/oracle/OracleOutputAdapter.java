/**
 * 
 */
package cn.com.infcn.superspider.io.output.adapter.oracle;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.justme.superspider.spider.SpiderListener;
import com.justme.superspider.xml.Site;

import cn.com.infcn.superspider.common.BasicDao;
import cn.com.infcn.superspider.common.Constant;
import cn.com.infcn.superspider.io.mq.disk.DiskBlockingQueue;
import cn.com.infcn.superspider.io.output.DbModel;
import cn.com.infcn.superspider.io.output.Doc;
import cn.com.infcn.superspider.io.output.OutputDispatcherFactory;
import cn.com.infcn.superspider.io.output.OutputAdapter;
import cn.com.infcn.superspider.io.output.adapter.jdbc.jdbcOutputAdapter;

/**
 * @description 
 * @author WChao
 * @date   2016年4月12日 	上午11:56:30
 */
public class OracleOutputAdapter extends OutputAdapter{

	Logger logger = Logger.getLogger(jdbcOutputAdapter.class);
	private BasicDao basicDao = null;
	
	public OracleOutputAdapter(){}
	public OracleOutputAdapter(JSONObject obj, Site site) throws Throwable {
		this.site = site;
		init(obj);
	}
	public OracleOutputAdapter(JSONObject obj, Site site ,SpiderListener listener) throws Throwable {
		this(obj,site);
		this.listener = listener;
	}
	@Override
	public void init(JSONObject obj) {
		//1)初始化磁盘队列;
		this.support = OutputDispatcherFactory.pool.getDiskQueue("jdbcQueue_"+site.getId());
		//2) decorate to a blocking queue
		queue = new DiskBlockingQueue(this.support);
		dbModel = new DbModel(obj.getString(Constant.HOST), Integer.parseInt(obj.getString(Constant.PORT)),obj.getString(Constant.TYPE), obj.getString(Constant.DBNAME), obj.getString(Constant.USERNAME), obj.getString(Constant.PASSWORD), obj.getString(Constant.TABLE_NAME).toUpperCase());
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
	public void save(Doc doc) throws Exception {
		//同步信号量,在插入数据库时进行同步控制;
		JSONObject targetFields = this.dbModel.tableStruct.getJSONObject(Constant.TARGET_FIELDS);
		//增量标识;
		String increment = this.dbModel.tableStruct.getString(Constant.INCREMENT);
		//增量主键字段;
		String primaryField = this.dbModel.tableStruct.getString(Constant.INCREMENT_PRIMARY_FIELD);
		if(targetFields == null || targetFields.size() == 0){
			logger.info("["+site.getName()+"]没有选择要同步的字段...");
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
			JSONObject jsonObject = doc.getResult().getJSONObject(i);
			jsonObject.put(".table","\""+dbModel.getTablename()+"\"");
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
				String delInfo = "["+site.getName()+"]删除"+deleteResult.size()+"条,耗时"+(end-start)+"毫秒";
				logger.info(delInfo);
				listener.onInfo(Thread.currentThread(),null, delInfo);
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
				String updateInfo = "["+site.getName()+"]更新"+updateResult.size()+"条,耗时"+(end-start)+"毫秒";
				logger.info(updateInfo);
				listener.onInfo(Thread.currentThread(),null,updateInfo);
			}
		}
		if(basicDao != null)
		{
			long start = System.currentTimeMillis();
			basicDao.saveAll(insertResult);
			long end = System.currentTimeMillis();
			String saveInfo = "["+site.getName()+"]保存"+insertResult.size()+"条,耗时"+(end-start)+"毫秒";
			logger.info(saveInfo);
			listener.onInfo(Thread.currentThread(),null,saveInfo);
		}
		
	}
	
	@SuppressWarnings("unused")
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
				StringBuffer sqlStr = new StringBuffer();
				sqlStr.append("select count(*) as table_num from user_tables where table_name = '"+dbModel.getTablename()+"'");
				Record record = basicDao.selectOne(sqlStr.toString());
				int table_num = record.getInt("table_num");
				if(table_num > 0){
					String deleteSql = "drop table \""+dbModel.getTablename()+"\"";
					basicDao.executeSql(deleteSql);
				}
			}
			StringBuffer sqlStr = new StringBuffer();
			sqlStr.append("select count(*) as table_num from user_tables where table_name = '"+dbModel.getTablename()+"'");
			Record record = basicDao.selectOne(sqlStr.toString());
			int table_num = record.getInt("table_num");
			if(table_num == 0)
			{
				sqlStr.setLength(0);
				sqlStr.append("CREATE TABLE \"$table\"(");
			}else{
				return;
			}
			for(Object obj : targetFields.keySet())
			{
				sqlStr.append("\""+obj.toString().toUpperCase()+"\" "+targetFields.get(obj));
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
			String errorInfo = "["+site.getName()+"]在CreateTable中执行Sql出现异常..."+e.getMessage();
			logger.error(errorInfo,e);
			listener.onError(Thread.currentThread(), null, errorInfo, e);
		}finally{
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
			logger.error("["+this.site.getName()+"]"+Thread.currentThread()+"OracleOutputAdapter.name->"+site.getName()+".ThreadPool shutdown failed.",e);
		}finally{
			if(queue != null)
			{
				 OutputDispatcherFactory.pool.releaseDiskQueue(this.support);//关闭磁盘队列;
			}
		}
		
	}

}
