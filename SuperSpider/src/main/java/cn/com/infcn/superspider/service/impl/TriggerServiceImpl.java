/**
 * 
 */
package cn.com.infcn.superspider.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;

import cn.com.infcn.ade.common.persistence.HibernateDao;
import cn.com.infcn.ade.common.service.BaseService;
import cn.com.infcn.superspider.dao.TriggerDao;
import cn.com.infcn.superspider.model.DbSource;
import cn.com.infcn.superspider.model.FieldMapping;
import cn.com.infcn.superspider.model.Trigger;
import cn.com.infcn.superspider.pagemodel.DbConfig;
import cn.com.infcn.superspider.service.TriggerServiceI;
import cn.com.infcn.superspider.utils.ConnectionUtil;
import cn.com.infcn.superspider.utils.DbUtil;
import cn.com.infcn.superspider.utils.StringUtil;

/**
 * @description 
 * @author WChao
 * @date   2016年2月19日 	上午9:51:40
 */
@Service(value="triggerService")
public class TriggerServiceImpl extends BaseService<Trigger, String> implements TriggerServiceI {
	Logger logger = Logger.getLogger(TriggerServiceImpl.class);
	@Autowired
	private TriggerDao triggerDao;
	@Autowired
	private DbSourceServiceImpl dbSourceService;
	
	@Override
	public HibernateDao<Trigger, String> getEntityDao() {
		return triggerDao;
	}
	@Override
	@Transactional(readOnly=false)
	public Map<String,Object> createTrigger(DbConfig dbConfig) {
		Trigger trigger = this.getTriggerByTask(dbConfig.getTaskId());
		Map<String,Object> returnData = new HashMap<String, Object>();
		DbSource dbSource = new DbSource();
		BeanUtils.copyProperties(dbConfig,dbSource);
		String[] oldTriggerArry = null;
		Connection conn = null;
		Statement pstmt = null;
//		PreparedStatement pstmt = null;
		String triggerScriptSql = "";
        try{
			conn = ConnectionUtil.getConnection(dbSource.getDsType(),dbSource.getDsHost(),dbSource.getDsPort(),dbSource.getDsDbName(),dbSource.getDsUserName(), dbSource.getDsPassWord());
//			conn.setAutoCommit(false);//事务回滚;
		if(trigger != null)
		{
			oldTriggerArry = DbUtil.getDeleteTriggerSql(trigger,dbConfig,false,conn);
		}
		DbServiceImpl dbService = (DbServiceImpl)ContextLoader.getCurrentWebApplicationContext().getBean("dbService"); 
		List<FieldMapping> fieldList = dbService.getAllColumns(dbSource);
		String[] triggerScriptArry = DbUtil.getTrigger(fieldList, dbConfig,conn);
	
			if(conn != null){
				if(oldTriggerArry != null)
				{
					for(String triggerScript : oldTriggerArry)
					{
						if(!StringUtil.isEmpty(triggerScript))
						{
							// pstmt = conn.prepareStatement(triggerScript);
							// pstmt.execute();
							pstmt = conn.createStatement();
							pstmt.executeUpdate(triggerScript);
						}
					}
				}
				if(triggerScriptArry.length > 0)
				{
					for(String triggerScript : triggerScriptArry)
					{
						if(!StringUtil.isEmpty(triggerScript))
						{
							triggerScriptSql += triggerScript+"\n\n";
							// pstmt = conn.prepareStatement(triggerScript);
							// pstmt.execute();
							pstmt = conn.createStatement();
							pstmt.executeUpdate(triggerScript);
						}
					}
				}
			}
        }catch (Exception e) {
        	logger.error(e.getMessage(),e);
        	returnData.put("status",0);
    		returnData.put("trigger", trigger);
    		returnData.put("msg",e.getMessage());
        	return returnData;
		}finally{
			ConnectionUtil.closeConnection(conn, pstmt, null);
		}
		if(dbConfig.getTriggerId() == null || "".equals(dbConfig.getTriggerId()))//新增
		{
			trigger = new Trigger();
			dbConfig.setTriggerId(StringUtil.generateUUID());
		}
		BeanUtils.copyProperties(dbConfig, trigger);
		if(trigger.getTriggerScript() == null || "".equals(trigger.getTriggerScript()))
			trigger.setTriggerScript(triggerScriptSql);
		trigger.setCreateStatus("1");//创建成功
		this.save(trigger);
		returnData.put("status",1);
		returnData.put("trigger", trigger);
		return returnData;
	}
	public Trigger getTriggerByTask(String taskId){
		String hql = "from Trigger t where t.taskId = ?0";
		return triggerDao.get(hql, taskId);
	}
	

	@Transactional(readOnly=false)
	public void deleteTriggerByTask(String taskId , DbSource dbSource){
		Trigger trigger = getTriggerByTask(taskId);
		if(trigger == null)
		return;
		Connection conn = null;
		Statement pstmt = null;
		try {
			if("1".equals(trigger.getCreateStatus())){//创建成功过触发器才删除;
				DbConfig dbConfig = null;
				dbConfig = new DbConfig();
				dbConfig.setTriggerName(trigger.getTriggerName());
				dbConfig.setTriggerTableName(trigger.getTriggerTableName());
				dbConfig.setDsType(dbSource.getDsType());
				boolean isDelete = true;
				String[] oldTriggerArry = DbUtil.getDeleteTriggerSql(trigger, dbConfig,isDelete,conn);
				conn = ConnectionUtil.getConnection(dbSource.getDsType(),dbSource.getDsHost(),dbSource.getDsPort(),dbSource.getDsDbName(),dbSource.getDsUserName(), dbSource.getDsPassWord());
				pstmt = conn.createStatement();
				for(String dropTrigger:oldTriggerArry)
				{
					pstmt.executeUpdate(dropTrigger);
				}
			}
			this.delete(trigger.getTriggerId());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ConnectionUtil.closeConnection(conn, pstmt, null);
		}
	}
	
	@Override
	@Transactional(readOnly=false)
	public void deleteTrigger(DbConfig dbConfig){
//		PreparedStatement pstmt = null;
		Statement pstmt = null;
		DbSource dbSource = new DbSource();
		BeanUtils.copyProperties(dbConfig,dbSource);
		Connection conn = ConnectionUtil.getConnection(dbSource.getDsType(),dbSource.getDsHost(),dbSource.getDsPort(),dbSource.getDsDbName(),dbSource.getDsUserName(), dbSource.getDsPassWord());
		Trigger trigger = this.get(dbConfig.getTriggerId());
		boolean isDelete = true;
		String[] oldTriggerArry = DbUtil.getDeleteTriggerSql(trigger, dbConfig,isDelete,conn);
		try {
			pstmt = conn.createStatement();
			for(String dropTrigger:oldTriggerArry)
			{
				if(!StringUtil.isEmpty(dropTrigger))
				{
					pstmt.executeUpdate(dropTrigger);
				}
			}
			this.delete(dbConfig.getTriggerId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
