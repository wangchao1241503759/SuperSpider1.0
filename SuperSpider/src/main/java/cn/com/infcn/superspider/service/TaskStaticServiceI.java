/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年9月19日
 */
package cn.com.infcn.superspider.service;

import cn.com.infcn.superspider.model.TaskStaticInfo;
import cn.com.infcn.superspider.pagemodel.EchartsConfig;
import cn.com.infcn.superspider.pagemodel.TaskStatistics;
import cn.com.infcn.superspider.pagemodel.TaskStatisticsConfig;

/**
 * @author lihf
 * @date 2016年9月19日
 */
public interface TaskStaticServiceI
{

	/**
	 * 添加任务统计
	 * @author lihf
	 * @date 2016年9月19日	上午11:04:46
	 * @param taskStatistics
	 * @throws Exception
	 */
	public void add(TaskStatistics taskStatistics) throws Exception;
	
	/**
	 * 修改任务统计
	 * @author lihf
	 * @date 2016年9月19日	上午11:04:46
	 * @param taskStatistics
	 * @throws Exception
	 */
	public void edit(TaskStatistics taskStatistics) throws Exception;
	
	/**
	 * 判断是否存在今天的任务统计数据
	 * @author lihf
	 * @date 2016年9月19日	下午3:20:29
	 * @param taskStatistics
	 * @return
	 * @throws Exception
	 */
	public boolean isExistsTodayTask(TaskStatistics taskStatistics) throws Exception;
	
	/**
	 * 获取任务统计信息
	 * @author lihf
	 * @date 2016年9月19日	下午3:50:33
	 * @param taskStatistics
	 * @return
	 * @throws Exception
	 */
	public TaskStaticInfo getTaskStaticInfo(TaskStatistics taskStatistics) throws Exception;
	
	/**
	 * 任务统计
	 * @author lihf
	 * @date 2016年9月20日	下午3:45:43
	 * @param taskStatisticsConfig
	 * @return
	 * @throws Exception
	 */
	public EchartsConfig getTaskStatistics(TaskStatisticsConfig taskStatisticsConfig) throws Exception;
}
