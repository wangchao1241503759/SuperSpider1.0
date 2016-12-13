/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年9月19日
 */
package cn.com.infcn.superspider.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cn.com.infcn.ade.common.dao.BaseDaoI;
import cn.com.infcn.superspider.model.TTaskLog;
import cn.com.infcn.superspider.model.TaskStaticInfo;
import cn.com.infcn.superspider.pagemodel.TaskStatistics;
import cn.com.infcn.superspider.service.CrontabServiceI;
import cn.com.infcn.superspider.service.TaskLogServiceI;
import cn.com.infcn.superspider.service.TaskServiceI;
import cn.com.infcn.superspider.service.TaskStaticServiceI;
import cn.com.infcn.superspider.utils.UUIDCreater;

/**
 * @author lihf
 * @date 2016年9月19日
 */
@Service
public class CrontabServiceImpl implements CrontabServiceI
{
	@Autowired
	private TaskServiceI taskService;
	@Autowired
	private TaskLogServiceI taskLogService;
	@Autowired
	private TaskStaticServiceI taskStaticService;
	@Autowired
	private BaseDaoI<TaskStaticInfo> taskStaticInfoDao;
	@Autowired
	private BaseDaoI<TTaskLog> taskLogDao;

	/**
	 * 添加任务统计的数据
	 * @author lihf
	 * @date 2016年9月18日	下午5:13:58
	 */
	@Scheduled(cron = "0 0/30 * * * ?")
	@Override
	public void addTaskStatisticsData()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try
        {
			List<TTaskLog> taskLogList = taskLogService.getTaskLogListByNoStatis();
			for(TTaskLog taskLog:taskLogList)
			{
				//1先查询统计表中是否已经有今天的记录
				String taskId = taskLog.getTaskId();
				int startCount = taskLog.getStartCount();
				String currentDay = dateFormat.format(taskLog.getCreateTime());
				
				TaskStatistics taskStatistics = new TaskStatistics();
				taskStatistics.setTaskId(taskId);
				taskStatistics.setStartCount(startCount);
				taskStatistics.setStatisticTime(currentDay);
				
				long collectNum = 0;
				collectNum = taskLog.getCollectNum();
				
				TaskStaticInfo taskStaticInfo = taskStaticService.getTaskStaticInfo(taskStatistics);
				if(null!=taskStaticInfo)
				{
					Long num = taskStaticInfo.getCollectNum();
					long num_old = 0;
					if(num!=null)
					{
						num_old = num;
					}
					long total = num_old + collectNum;
					taskStaticInfo.setCollectNum(total);
					taskStaticInfoDao.saveOrUpdate(taskStaticInfo);
				}
				else
				{
					taskStaticInfo = new TaskStaticInfo();
					BeanUtils.copyProperties(taskLog, taskStaticInfo);
					taskStaticInfo.setId(UUIDCreater.getUUID());

					taskStaticInfo.setCollectNum(collectNum);
					taskStaticInfo.setStatisticTime(currentDay);
					taskStaticInfoDao.saveOrUpdate(taskStaticInfo);
				}
				taskLog.setIsStatistics("Y");
				taskLogDao.saveOrUpdate(taskLog);
				
			}
        }
        catch (Exception e)
        {
	        e.printStackTrace();
        }
	}
}
