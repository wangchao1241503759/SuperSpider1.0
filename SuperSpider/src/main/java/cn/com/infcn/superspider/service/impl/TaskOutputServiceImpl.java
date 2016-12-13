/**
 * 
 */
package cn.com.infcn.superspider.service.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import opennlp.tools.util.StringUtil;

import org.apache.log4j.Logger;
import org.bson.BsonDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.infcn.ade.common.dao.BaseDaoI;
import cn.com.infcn.ade.common.persistence.HibernateDao;
import cn.com.infcn.ade.common.service.BaseService;
import cn.com.infcn.superspider.common.Constant;
import cn.com.infcn.superspider.common.MongoDao;
import cn.com.infcn.superspider.dao.TaskOutputDao;
import cn.com.infcn.superspider.model.TaskOutput;
import cn.com.infcn.superspider.service.TaskOutputServiceI;
import cn.com.infcn.superspider.utils.ConnectionUtil;
import cn.com.infcn.superspider.utils.DbUtil;
import cn.com.infcn.superspider.utils.HttpApiUtil;

import com.alibaba.fastjson.JSONArray;
import com.justme.superspider.plugin.db.adapter.DbProtocol;
import com.mongodb.BasicDBObject;

/**
 * @description 
 * @author WChao
 * @date   2015年12月21日 	下午6:29:54
 */
@Service(value="taskOutputService")
@Transactional(readOnly=true)
public class TaskOutputServiceImpl extends BaseService<TaskOutput, String> implements TaskOutputServiceI{
	
	Logger logger = Logger.getLogger(TaskOutputServiceImpl.class);
	
	@Autowired
	private TaskOutputDao topDao;
	
	@Override
	public HibernateDao<TaskOutput, String> getEntityDao() {
		
		return topDao;
	}
	
	@Autowired
	private BaseDaoI<TaskOutput> taskOutputDao;

	@Override
	public Map<String,String> getOutputAllTables(TaskOutput taskOutput) throws Exception {
		Map<String,String> tables = new HashMap<String,String>();
		tables.put("status","0");
		if(DbProtocol.MYSQL.equals(taskOutput.getTopType()) || DbProtocol.SQLSERVER.equals(taskOutput.getTopType()) || DbProtocol.ORACLE.equals(taskOutput.getTopType()) || DbProtocol.DB2.equals(taskOutput.getTopType())){
			Connection conn = null;
	        PreparedStatement pstmt = null;
	        ResultSet tableRet = null;
	        try{
				conn = ConnectionUtil.getConnection(taskOutput.getTopType(),taskOutput.getTopHost(),taskOutput.getTopPort(),taskOutput.getTopDbName(),taskOutput.getTopUserName(), taskOutput.getTopPassWord());
				if(conn != null){
					tables.put("status","1");
					String sql = "";
			        if(DbProtocol.ORACLE.equals(taskOutput.getTopType()))//Oracle数据库;有USER_TABLES、ALL_TABLES、DBA_TABLES三种区分，我们需要的是USER_TABLES;
			        {
			        	sql = "SELECT TABLE_NAME as name FROM USER_TABLES";
			        	pstmt = conn.prepareStatement(sql);
			        	tableRet = pstmt.executeQuery();
				        while(tableRet.next()){
				        	tables.put(tableRet.getString("name"),tableRet.getString("name"));
				        }
			        }else{//其他关系型数据库;
			        	DatabaseMetaData m_DBMetaData = conn.getMetaData();
			        	String schema = "%";
			        	if(taskOutput.getTopSchema()!= null && !"".equals(taskOutput.getTopSchema()))
			        	schema = taskOutput.getTopSchema();
			            tableRet = m_DBMetaData.getTables(null, schema,"%",new String[]{"TABLE"});
				        while(tableRet.next())
				        {
				        	tables.put(tableRet.getString("TABLE_NAME"),tableRet.getString("TABLE_NAME"));
						}
			        }
				} 
			} catch (SQLException e) {
				e.printStackTrace();
				return tables;
			}finally{
				if(conn != null)
				ConnectionUtil.closeConnection(conn, pstmt, tableRet);
			}
		}else if(DbProtocol.MONGO.equals(taskOutput.getTopType())){
			MongoDao mongoDao = null;
			try {
				mongoDao = new MongoDao(taskOutput.getTopHost(), taskOutput.getTopPort(), taskOutput.getTopDbName());
				Set<String> collections = mongoDao.getDB(taskOutput.getTopDbName()).getCollectionNames();
				if (collections != null)
	            {
					 tables.put("status","1");
					 for(String name : collections)
					 {
						 if(!(name.contains("$")||name.contains(Constant.INDEX_TABLENAME)||name.contains(Constant.PROFILE_TABLENAME)))
						 {
							 tables.put(name,name);
						 }
					 }
	             }
			} catch (Exception e) {
				e.printStackTrace();
				return tables;
			}finally{
				if(mongoDao != null)
				mongoDao.getClient().close();
			}
		}else if(Constant.MSS.equals(taskOutput.getTopType()))
		{
			/*MssServer mssServer = new MssServer(taskOutput.getTopHost(),taskOutput.getTopPort(),taskOutput.getTopDbName(),taskOutput.getTopUserName(), taskOutput.getTopPassWord(),taskOutput.getTopMssType(),null);
			MssStorage mssStorage = new MssStorage(mssServer);
			List<MssMetadbInfo> metadbs = mssStorage.getMetadbInfo();
			if(metadbs != null && metadbs.size() > 0){
				tables.put("status","1");
				for(MssMetadbInfo metadb : metadbs)
				{
					tables.put(metadb.getDbcode(),metadb.getId());
				}
			}*/
		}
		else if (Constant.MSS_HTTP.equals(taskOutput.getTopType()))
		{
//			MssServer mssServer = new MssServer(taskOutput.getTopHost(), taskOutput.getTopPort(), taskOutput.getTopDbName(), taskOutput.getTopUserName(), taskOutput.getTopPassWord(),
//			        taskOutput.getTopMssType(), null);
//			MssStorage mssStorage = new MssStorage(mssServer);
//			List<MssMetadbInfo> metadbs = mssStorage.getMetadbInfo();
//			if (metadbs != null && metadbs.size() > 0)
//			{
//				tables.put("status", "1");
//				for (MssMetadbInfo metadb : metadbs)
//				{
//					tables.put(metadb.getDbcode(), metadb.getId());
//				}
//			}
			
			JSONArray jsonArray = HttpApiUtil.getMssTables(taskOutput.getTopHost(), taskOutput.getTopPort());
			if(null!=jsonArray && jsonArray.size()>0)
			{
				tables.put("status", "1");
				for(int i=0;i<jsonArray.size();i++)
				{
					String tableName = jsonArray.getString(i);
					tables.put(tableName, tableName);
				}
			}
		}
		return tables;
	}

	@SuppressWarnings({ "static-access" })
	@Override
	public Map<String,List<String>> getAllColumns(TaskOutput taskOutput){
		Map<String,List<String>> columns = new HashMap<String, List<String>>();
		if(Constant.MYSQL.equals(taskOutput.getTopType()) || Constant.SQLSERVER.equals(taskOutput.getTopType()) || Constant.ORACLE.equals(taskOutput.getTopType()) || Constant.DB2.equals(taskOutput.getTopType())){
			Connection conn = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
	        try{
	        	Integer port = taskOutput.getTopPort() == null ? 0 : taskOutput.getTopPort();
		        conn = ConnectionUtil.getConnection(taskOutput.getTopType(),taskOutput.getTopHost(),port,taskOutput.getTopDbName(),taskOutput.getTopUserName(), taskOutput.getTopPassWord());
		        if(conn != null)
		        {
		        	DatabaseMetaData m_DBMetaData = conn.getMetaData();
		        	String schema = "%";
		        	if(taskOutput.getTopSchema() != null && !"".equals(taskOutput.getTopSchema()))
		        	schema = taskOutput.getTopSchema();
		        	ResultSet colRet = m_DBMetaData.getColumns(null,schema,taskOutput.getTopTableName(),"%");
		        	while(colRet.next()) {
			        	if(columns.get("name") == null){
			        		columns.put("name",new ArrayList<String>());
			        	}
			        	if(columns.get("type") == null){
			        		columns.put("type",new ArrayList<String>());
			        	}
			        	if(!DbUtil.isSpecialColumnTypeChar(colRet.getString("TYPE_NAME")))
			        	{
			        		columns.get("type").add(colRet.getString("TYPE_NAME"));//如果是大字段去掉类型大小限制;
			        	}else{
			        		columns.get("type").add(colRet.getString("TYPE_NAME")+"("+colRet.getInt("COLUMN_SIZE")+")");
			        	}
			        	columns.get("name").add(colRet.getString("COLUMN_NAME"));
		        	}
		        }
	        } catch (SQLException e) {
				e.printStackTrace();
				return columns;
			}finally{
				if(conn != null)
				ConnectionUtil.closeConnection(conn, pstmt, rs);
			}
		}else if(Constant.MONGO.equals(taskOutput.getTopType())){
			MongoDao mongoDao = null;
			try {
				mongoDao = new MongoDao(taskOutput.getTopHost(),taskOutput.getTopPort(),taskOutput.getTopDbName());
				BasicDBObject basicDbObject = (BasicDBObject)mongoDao.findOne(taskOutput.getTopTableName());
				BsonDocument bsonDocument = basicDbObject.toBsonDocument(null,mongoDao.getClient().getDefaultCodecRegistry());
				for(String key :basicDbObject.keySet())
				{
		        	if(columns.get("name") == null){
		        		columns.put("name",new ArrayList<String>());
		        	}
		        	if(columns.get("type") == null){
		        		columns.put("type",new ArrayList<String>());
		        	}
		        	columns.get("type").add(bsonDocument.get(key).getBsonType().toString());
		        	columns.get("name").add(key);
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				return columns;
			}finally{
				if(mongoDao != null)
				mongoDao.getClient().close();
			}
		}else if(Constant.MSS.equals(taskOutput.getTopType()))
		{
			/*try {
				MssServer mssServer = new MssServer(taskOutput.getTopHost(),taskOutput.getTopPort(),taskOutput.getTopDbName(),taskOutput.getTopUserName(), taskOutput.getTopPassWord(),taskOutput.getTopMssType(),null);
				MssStorage mssStorage = new MssStorage(mssServer);
				List<MssMetadbStruct>  metadbStructs = mssStorage.getMetadbStruct(taskOutput.getTopTableName());
				if(metadbStructs != null && metadbStructs.size() > 0)
				{
					for(MssMetadbStruct metadbStruct :metadbStructs)
					{
						if(columns.get("name") == null){
			        		columns.put("name",new ArrayList<String>());
			        	}
			        	if(columns.get("type") == null){
			        		columns.put("type",new ArrayList<String>());
			        	}
			        	columns.get("type").add(Consts.fTypeIdMap().get(metadbStruct.getTypeid()));
			        	columns.get("name").add(metadbStruct.getStcode());
					}
				}
			} catch (GateServiceException e) {
				e.printStackTrace();
			}*/
		}
		else if (Constant.MSS_HTTP.equals(taskOutput.getTopType()))
		{
	
			try
            {
	            columns = HttpApiUtil.getMssAllColumns(taskOutput.getTopHost(), taskOutput.getTopPort(), taskOutput.getTopTableName());
            }
            catch (Exception e)
            {
	            e.printStackTrace();
            }
		}
		return columns;
	}

	@Override
	@Transactional(readOnly=false)
	public void deleteByTaskId(String taskId) throws Exception {
		String hql = "delete from TaskOutput t where t.taskId = ?0";
		topDao.executeHql(hql, taskId);
	}

	@Override
	public TaskOutput getByTaskId(String taskId) throws Exception {
		String hql = "from TaskOutput t where t.taskId = ?0";
		return topDao.get(hql, taskId);
	}

	@Override
	@Transactional(readOnly=false)
	public void saveList(List<TaskOutput> taskOutputs) throws Exception {
		for(TaskOutput taskOutput : taskOutputs){
			this.save(taskOutput);
		}
	}
	
	/**
	 * 根据ID获取任务的输出
	 * @author lihf
	 * @date 2016年4月14日	上午11:35:33
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	public TaskOutput getTaskOutput(String id) throws Exception
	{
		if(StringUtil.isEmpty(id))
		{
			throw new Exception("参数不能为空！");
		}
		return taskOutputDao.get(TaskOutput.class, id);
	}
	
	/**
	 * 根据web任务ID获取任务的输出
	 * @author lihf
	 * @date 2016年4月14日	上午11:35:33
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	public TaskOutput getByWebTaskId(String taskId) throws Exception
	{
		if(StringUtil.isEmpty(taskId))
		{
			throw new Exception("参数不能为空！");
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("taskId", taskId);
		String hql = " from TaskOutput t where 1=1 and t.taskId=:taskId ";
		TaskOutput taskOutput = taskOutputDao.get(hql, params);
		return taskOutput;
		
	}
}
