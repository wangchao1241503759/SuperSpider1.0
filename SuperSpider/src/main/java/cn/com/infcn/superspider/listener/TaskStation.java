/**
 * 
 */
package cn.com.infcn.superspider.listener;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.eweb4j.util.CommonUtil;
import org.springframework.web.context.ContextLoader;

import com.alibaba.fastjson.JSONArray;
import com.justme.superspider.plugin.db.util.ConnectionUtil;
import com.justme.superspider.xml.Site;

import cn.com.infcn.superspider.common.Constant;
import cn.com.infcn.superspider.model.Task;
import cn.com.infcn.superspider.model.Trigger;
import cn.com.infcn.superspider.service.impl.TaskServiceImpl;
import cn.com.infcn.superspider.service.impl.TriggerServiceImpl;
import cn.com.infcn.superspider.utils.DateUtils;
import cn.com.infcn.superspider.utils.FileUtils;

/**
 * @author WChao
 *
 */
public class TaskStation {
	
	private final static Logger logger = Logger.getLogger(TaskManager.class);
	//任务逻辑层接口;
	public static TaskServiceImpl taskService = (TaskServiceImpl)ContextLoader.getCurrentWebApplicationContext().getBean("taskService");
	private TaskStation(){};
	public static TaskStation me(){
		return new TaskStation();
	}
	/**
	 * 更新任务成功状态;
	 * @param taskId
	 * @param size
	 */
	public synchronized void updateTaskSuccess(String taskId,JSONArray result){
		Task task;
		int size = result.size();
		try {
			task = taskService.getBean(taskId);
			if(task == null)
				return;
			task.setSuccessCount(task.getSuccessCount()+size);
			//task.setFinishCount(task.getSuccessCount()+task.getFailCount());
			taskService.update(task);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 更新任务成功状态和数量
	 * @author lihf
	 * @date 2016年10月26日	下午2:49:47
	 * @param taskId
	 * @param size
	 */
	public synchronized void updateTaskSuccess(String taskId,int size){
		Task task;
		try {
			task = taskService.getBean(taskId);
			if(task == null)
				return;
			task.setSuccessCount(task.getSuccessCount()+size);
			taskService.update(task);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	/**
	 * 更新任务已完成状态信息;
	 * @param taskId
	 * @param result
	 */
	public synchronized void updateTaskComplete(String taskId,JSONArray result){
		Task task;
		int size = result.size();
		try {
			task = taskService.getBean(taskId);
			if(task == null)
				return;
			if(task.getSuccessCount() == 0 && task.getFailCount() == 0){
				task.setFinishCount(size);
			}
			else{
				task.setFinishCount(task.getSuccessCount()+task.getFailCount());
			}
			taskService.update(task);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	/**
	 * 更新任务错误状态;
	 * @param taskId
	 * @param size
	 */
	public synchronized void updateTaskError(String taskId,JSONArray result,String status){
		Task task = null;
		try {
			task = taskService.getBean(taskId);
			if(task == null)
				return;
			if(result != null){
				ResourceBundle bundle = ResourceBundle.getBundle("application");
				String filePath = bundle.getString("errorPath")+"/";
				String fileName = task.getTaskName()+"_"+task.getTaskId()+"_error.json";
				File file=FileUtils.createFile(fileName,filePath);
				FileUtils.writeToFileByStream(file.getAbsolutePath(), result.toJSONString()+"@@@",true);
				int size = result.size();
				task.setFailCount(task.getFailCount()+size);
			}
			if(status != null && !"".equals(status)){
				task.setTaskState(status);//更新任务异常状态;
			}
			taskService.update(task);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	@SuppressWarnings("static-access")
	public synchronized void updateDbTaskStopStatus( List<Map<String, Object>> models,com.justme.superspider.task.Task task){
		int fetchSize = Integer.parseInt(task.site.getOption(Constant.FETCH_SIZE).toString());
		if(models == null || models.size() < fetchSize || "1".equals(task.site.getOption(Constant.IS_DIG))){
			String docKey = CommonUtil.md5("completed");
			if(task.site.db.deleteDoc(docKey)){
				task.site.db.putDoc(docKey, 1);
			}
			//将任务内存状态更改为已完毕状态;
			TaskSpiderBean taskSpiderBean = null;
			AtomicInteger atomicInteger = new AtomicInteger();
			try{
				taskSpiderBean = TaskManager.getInstance().get(task.site.getId());
				while(taskSpiderBean == null){
					atomicInteger.incrementAndGet();
					taskSpiderBean = TaskManager.getInstance().get(task.site.getId());
					if(atomicInteger.get()>3){
						break;
					}
					Thread.currentThread().sleep(1000);
				}
			}catch(Exception e){
				logger.error(e.getMessage(),e);
			}finally{
				try {
					task.site.isStop = true;//设置爬取已完成状态标志;
					Task taskModel = taskService.getBean(task.site.getId());
					taskModel.setCompleted("1");
					taskService.update(taskModel);
					logger.info("任务["+task.site.getName()+"]读取完毕,当前时间:"+DateUtils.getCurrentDate());
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				}
			}
		}
	}
	
	@SuppressWarnings("static-access")
	public synchronized void updateWebTaskStopStatus(com.justme.superspider.task.Task task){
			//将任务内存状态更改为已完毕状态;
			TaskSpiderBean taskSpiderBean = null;
			AtomicInteger atomicInteger = new AtomicInteger();
			try{
				taskSpiderBean = TaskManager.getInstance().get(task.site.getId());
				while(taskSpiderBean == null){
					atomicInteger.incrementAndGet();
					taskSpiderBean = TaskManager.getInstance().get(task.site.getId());
					if(atomicInteger.get()>3){
						break;
					}
					Thread.currentThread().sleep(1000);
				}
			}catch(Exception e){
				logger.error(e.getMessage(),e);
			}finally{
				try {
					task.site.isStop = true;//设置爬取已完成状态标志;
					Task taskModel = taskService.getBean(task.site.getId());
					taskModel.setCompleted("1");
					taskService.update(taskModel);
					logger.info("任务["+task.site.getName()+"]爬取完毕,当前时间:"+DateUtils.getCurrentDate());
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				}
			}
	}
	
	public static void clearIncrementTable(Site site){
		String inCrement = site.getOptionPojo(Constant.INCREMENT) == null ? "" : site.getOption(Constant.INCREMENT).toString();
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			if("1".equals(inCrement))
				return;
			TriggerServiceImpl triggerService = (TriggerServiceImpl)ContextLoader.getCurrentWebApplicationContext().getBean("triggerService");
			Trigger trigger = triggerService.getTriggerByTask(site.getId());
			if(trigger != null && "1".equals(trigger.getCreateStatus())){//存在触发器,并且创建成功!
				String triggerTableName = trigger.getTriggerTableName();
				String deleteSql = "delete from "+triggerTableName;
				conn = ConnectionUtil.getConnection(site);
				if(conn == null)
					return;
				pstmt = conn.prepareStatement(deleteSql);
				pstmt.execute();
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
		}finally{
			ConnectionUtil.closeConnection(conn, pstmt, null);
		}
	}
	/**
	 * 停止任务;
	 * @param taskId
	 * @throws Exception 
	 */
	public void stopTask(String taskId) throws Exception{
		taskService.stop(taskId);
	}
}
