/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年9月5日
 */
package cn.com.infcn.superspider.utils;

import java.util.Date;

import cn.com.infcn.superspider.model.Task;
import cn.com.infcn.superspider.pagemodel.Log;
import cn.com.infcn.superspider.service.impl.TaskLogServiceImpl;
import cn.com.infcn.superspider.service.impl.TaskServiceImpl;

/**
 * @author lihf
 * @date 2016年9月5日
 */
public class ListenerUtil
{

	/**
	 * 开始启动任务时，写入日志
	 * @author lihf
	 * @date 2016年9月5日	下午4:36:46
	 * @param source
	 * @param taskName
	 * @param taskLogService
	 */
	public static void addLogByStart(String source,String taskName,TaskLogServiceImpl taskLogService,String taskId,int startCount)
	{
		Log log = new Log();
		try
		{
			log.setId(UUIDCreater.getUUID());
			log.setLevel(ConfigUtil.get("log_info"));
			log.setCreateTime(new Date());
			log.setSource(source);
			log.setEvent(ConfigUtil.get("log_event_start"));
			log.setContent(taskName+"任务启动");
			log.setTaskName(taskName);
			log.setTaskId(taskId);
			log.setStartCount(startCount);
			taskLogService.add(log);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 任务结束时，写入日志
	 * @author lihf
	 * @date 2016年9月5日	下午4:37:33
	 * @param source
	 * @param taskName
	 * @param taskLogService
	 */
	public static void addLogByEnd(String source,String taskName,TaskLogServiceImpl taskLogService,String taskId,int startCount)
	{
 		Log log = new Log();
 		try
 		{
 			log.setId(UUIDCreater.getUUID());
 			log.setLevel(ConfigUtil.get("log_info"));
 			log.setCreateTime(new Date());
 			log.setSource(source);
 			log.setEvent(ConfigUtil.get("log_event_end"));
 			log.setContent(taskName+"任务采集结束");
 			log.setTaskName(taskName);
			log.setTaskId(taskId);
			log.setStartCount(startCount);
 			taskLogService.add(log);
 		}
 		catch (Exception e)
 		{
 			e.printStackTrace();
 		}
	}
	
	/**
	 * 任务错误时，写入日志
	 * @author lihf
	 * @date 2016年9月5日	下午4:37:33
	 * @param source
	 * @param taskName
	 * @param taskLogService
	 */
	public static void addLogByError(String source,String taskName,String err,TaskLogServiceImpl taskLogService,String taskId,int startCount)
	{
 		Log log = new Log();
 		try
 		{
 			log.setId(UUIDCreater.getUUID());
 			log.setLevel(ConfigUtil.get("log_error"));
 			log.setCreateTime(new Date());
 			log.setSource(source);
 			log.setEvent(ConfigUtil.get("log_event_error"));
 			log.setContent(err);
 			log.setTaskName(taskName);
			log.setTaskId(taskId);
			log.setStartCount(startCount);
 			taskLogService.add(log);
 		}
 		catch (Exception e)
 		{
 			e.printStackTrace();
 		}
	}
	
	/**
	 * 任务结束时，写入采集数量日志
	 * @author lihf
	 * @date 2016年9月5日	下午4:37:33
	 * @param source
	 * @param taskName
	 * @param taskLogService
	 */
	public static void addLogByFinishNum(String source,String taskName,String taskId,TaskLogServiceImpl taskLogService,TaskServiceImpl taskService,int startCount)
	{
 		Log log = new Log();
 		try
 		{
 			Task task = taskService.getTaskById(taskId);
 			int size = 0;
 			Integer count = task.getSuccessCount();
 			if(null!=count)
 			{
 				size = count.intValue();
 			}
 			log.setId(UUIDCreater.getUUID());
 			log.setLevel(ConfigUtil.get("log_info"));
 			log.setCreateTime(new Date());
 			log.setSource(source);
 			log.setEvent(ConfigUtil.get("log_event_fetchfinshNum"));
	 		log.setContent(taskName+"采集成功，共采集"+size+"条数据");
 			log.setTaskName(taskName);
			log.setTaskId(taskId);
			log.setStartCount(startCount);
			log.setCollectNum(size);
 			taskLogService.add(log);
 		}
 		catch (Exception e)
 		{
 			e.printStackTrace();
 		}
	}
}
