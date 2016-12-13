/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年8月1日
 */
package cn.com.infcn.superspider.service;

import java.util.List;
import java.util.Map;

import cn.com.infcn.superspider.model.TTaskLog;
import cn.com.infcn.superspider.model.Task;
import cn.com.infcn.superspider.model.TaskStaticInfo;
import cn.com.infcn.superspider.pagemodel.TaskStaticInfoVO;

/**
 * @author lihf
 * @date 2016年8月1日
 */
public interface TaskMonitorServiceI
{

	
	/**
	 * 根据任务状态，获取任务列表
	 * @author lihf
	 * @date 2016年8月2日	上午9:24:40
	 * @return
	 * @throws Exception
	 */
	public List<Task> getTaskRunningList(String taskId, String loginName) throws Exception;
	/**
	 *  获取全部爬去任务
	 * @return
	 * @throws Exception
	 */
	public List<Task> getTaskAllList(String loginName) throws Exception;
	/**
	 *  保存任务日志信息
	 * @throws Exception
	 */
	public void saveTaskLogInfo(TTaskLog taskLog) throws Exception;
	
	/**
	 * 获取正在运行的任务信息
	 * @return 
	 * @throws Exception 
	 */
	public Map<String, Object> getTaskRunningTask(String taskId, String loginName) throws Exception;
	
	/**
	 * 初始化监控页面信息
	 * @return
	 */
	public Map<String, Object> initMonitorInfo(String loginName) throws Exception;
	
	/**
	 * 获取正在运行的任务信息
	 * @param t
	 * @return
	 * @throws Exception
	 */
	public List<TaskStaticInfoVO> getTaskRunningInfo(TaskStaticInfo t, Map<String, Object> map, String loginName) throws Exception;
	
	
	public List<TaskStaticInfoVO> getNotRuningTask(TaskStaticInfoVO v, String loginName) throws Exception;
	
}
