package cn.com.infcn.ade.system.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import cn.com.infcn.ade.system.model.ScheduleJob;

/**
 * 定时任务工作类
 * @author WChao
 * @date 2015年1月13日
 */
@DisallowConcurrentExecution  
public class TaskA implements Job {
	 
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ScheduleJob scheduleJob = (ScheduleJob)context.getMergedJobDataMap().get("scheduleJob");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");    
	    System.out.println("任务名称 = [" + scheduleJob.getName() + "]"+ " 在 " + dateFormat.format(new Date())+" 时运行"); 
    }
}
