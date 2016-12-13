/**
 * 
 */
package cn.com.infcn.superspider.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.infcn.ade.common.persistence.HibernateDao;
import cn.com.infcn.ade.common.service.BaseService;
import cn.com.infcn.superspider.dao.SchedulePlanDao;
import cn.com.infcn.superspider.model.SchedulePlan;
import cn.com.infcn.superspider.service.SchedulePlanServiceI;
import cn.com.infcn.superspider.utils.StringUtil;

/**
 * @description 
 * @author WChao
 * @date   2016年3月2日 	下午6:00:29
 */
@Service(value="schedulePlanService")
@Transactional(readOnly=true)
public class SchedulePlanServiceImpl extends BaseService<SchedulePlan, String>  implements SchedulePlanServiceI {
	
	private static final Logger log = Logger.getLogger(SchedulePlanServiceImpl.class);
	
	@Autowired
	private SchedulePlanDao schedulePlanDao;
	@Autowired
	private Scheduler scheduler;
	
	@Override
	public HibernateDao<SchedulePlan, String> getEntityDao() {
		
		return schedulePlanDao;
	}

	@Override
	public List<SchedulePlan> getAllSchedulePlans() {
		List<SchedulePlan> plans = new ArrayList<SchedulePlan>();
		List<SchedulePlan> oldPlans = this.getAll();
		for(SchedulePlan schedulePlan : oldPlans){
			SchedulePlan newSchedulePlan = new SchedulePlan();
			BeanUtils.copyProperties(schedulePlan, newSchedulePlan);
			plans.add(newSchedulePlan);
		}
		return plans;
	}
	/**
	 * 保存调度计划;
	 */
	@Override
	@Transactional(readOnly=false)
	public void add(SchedulePlan schedulePlan){
		SchedulePlan newSchedulePlan = schedulePlan;
		if(newSchedulePlan.getId() == null || "".equals(newSchedulePlan.getId())){
			newSchedulePlan.setId(StringUtil.generateUUID());
			@SuppressWarnings("rawtypes")
			Class job = null;
			try {
				job = Class.forName(schedulePlan.getClassName());
			} catch (ClassNotFoundException e1) {
				log.error("任务类没找到");
				e1.printStackTrace();
			}
			@SuppressWarnings("unchecked")
			JobDetail jobDetail = JobBuilder.newJob(job).withIdentity(schedulePlan.getName(), schedulePlan.getGroup()).build();
			jobDetail.getJobDataMap().put("scheduleJob", schedulePlan);
	 
			//表达式调度构建器（可判断创建SimpleScheduleBuilder）
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(schedulePlan.getCronExpression());
			
			//按新的cronExpression表达式构建一个新的trigger
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(schedulePlan.getName(), schedulePlan.getGroup()).withSchedule(scheduleBuilder).build();
			try {
				scheduler.scheduleJob(jobDetail, trigger);
				log.info("定时任务添加成功");
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		}else{
			newSchedulePlan = this.get(schedulePlan.getId());
			BeanUtils.copyProperties(schedulePlan,newSchedulePlan);
			try {
				TriggerKey key = TriggerKey.triggerKey(newSchedulePlan.getName(), newSchedulePlan.getGroup());  
		        //Trigger trigger = scheduler.getTrigger(key);  
		        CronTrigger newTrigger = (CronTrigger) TriggerBuilder.newTrigger()  
		                    .withIdentity(key)  
		                    .withSchedule(CronScheduleBuilder.cronSchedule(newSchedulePlan.getCronExpression()))  
		                    .build();  
		        scheduler.rescheduleJob(key, newTrigger);
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		}
		if(newSchedulePlan.getCreateTime() == null)
		{
			newSchedulePlan.setCreateTime(new Date());
		}
		this.save(newSchedulePlan);
	}
	/**
	 * 暂停任务
	 * @param name 任务名
	 * @param group 任务组
	 */
	@Transactional(readOnly=false)
	public void stopJob(List<String> ids){
		for(String id : ids){
			SchedulePlan schedulePlan = this.get(id);
			if(schedulePlan == null)
				break;
			JobKey key = new JobKey(schedulePlan.getName(),schedulePlan.getGroup());
			try {
				scheduler.pauseJob(key);
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
			schedulePlan.setStatus("0");
			this.update(schedulePlan);
		}
	}
	
	/**
	 * 恢复任务
	 * @param name 任务名
	 * @param group 任务组
	 */
	@Transactional(readOnly=false)
	public void restartJob(List<String> ids){
		for(String id : ids){
			SchedulePlan schedulePlan = this.get(id);
			if(schedulePlan == null)
			break;
			JobKey key = new JobKey(schedulePlan.getName(),schedulePlan.getGroup());
			try {
				scheduler.resumeJob(key);
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
			schedulePlan.setStatus("1");
			this.update(schedulePlan);
		}
	}
	
	/**
	 * 立马执行一次任务
	 * @param name 任务名
	 * @param group 任务组
	 */
	@Transactional(readOnly=false)
	public void startNowJob(List<String> ids){
		for(String id : ids){
			SchedulePlan schedulePlan = this.get(id);
			if(schedulePlan == null)
			break;
			JobKey jobKey = JobKey.jobKey(schedulePlan.getName(),schedulePlan.getGroup());
			try {
				scheduler.triggerJob(jobKey);
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 批量删除调度计划;
	 */
	@Override
	@Transactional(readOnly=false)
	public void deleteBatch(List<String> ids) {
		for(String id : ids)
		{
			SchedulePlan schedulePlan = this.get(id);
			if(schedulePlan == null)
			break;
			JobKey key = new JobKey(schedulePlan.getName(),schedulePlan.getGroup());
			try {
				scheduler.deleteJob(key);
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
			this.delete(id);
		}
	}

	@Override
	@Transactional(readOnly=false)
	public void saveList(List<SchedulePlan> schedulePlans) {
		for(SchedulePlan schedulePlan : schedulePlans){
			this.save(schedulePlan);
		}
	}
}
