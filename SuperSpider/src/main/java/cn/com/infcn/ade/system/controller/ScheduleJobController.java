package cn.com.infcn.ade.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.infcn.ade.system.model.ScheduleJob;
import cn.com.infcn.ade.system.service.ScheduleJobService;

/**
 * 定时任务 controller
 * @author WChao
 * @date 2015年1月14日
 */
@Controller
@RequestMapping("system/scheduleJob")
public class ScheduleJobController {
	
	@Autowired
	private ScheduleJobService scheduleJobService;
	
	/**
	 * 默认页面
	 * 
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "system/scheduleJobList";
	}
	
	/**
	 * 获取定时任务 json
	 */
	@RequiresRoles("admin")
	@RequestMapping("json")
	@ResponseBody
	public Map<String, Object> getAllJobs(Model model){
		List<ScheduleJob> scheduleJobs = scheduleJobService.getAllScheduleJob();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", scheduleJobs);
		map.put("total", scheduleJobs.size());
		return map;
	}
	
	/**
	 * 获取正在运行的定时任务
	 */
	@RequiresRoles("admin")
	@RequestMapping("running/json")
	@ResponseBody
	public Map<String, Object> getAllJobsRun(Model model){
		List<ScheduleJob> scheduleJobs = scheduleJobService.getAllRuningScheduleJob();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", scheduleJobs);
		map.put("total", scheduleJobs.size());
		return map;
	}
	
	/**
	 * 添加跳转
	 * @param model
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String createForm() {
		return "system/scheduleJobForm";
	}

	/**
	 * 添加
	 * @param user
	 * @param model
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public String create(@Valid ScheduleJob scheduleJob) {
		//验证cron表达式
		if(CronExpression.isValidExpression(scheduleJob.getCronExpression())){
			scheduleJob.setStatus("1");
			scheduleJobService.add(scheduleJob);
			return "success";
		}else{
			return "Cron表达式不正确";
		}
	}
	
	/**
	 * 暂停任务
	 */
	@RequiresRoles("admin")
	@RequestMapping("/{name}/{group}/stop")
	@ResponseBody
	public String stop(@PathVariable String name, @PathVariable String group) {
		scheduleJobService.stopJob(name, group);
		return "success";
	}

	/**
	 * 删除任务
	 */
	@RequiresRoles("admin")
	@RequestMapping("/{name}/{group}/delete")
	@ResponseBody
	public String delete(@PathVariable String name, @PathVariable String group) {
		scheduleJobService.delJob(name, group);
		return "success";
	}

	/**
	 * 修改表达式
	 */
	@RequiresRoles("admin")
	@RequestMapping("/{name}/{group}/update")
	@ResponseBody
	public String update(@PathVariable String name, @PathVariable String group,@RequestParam String cronExpression) {
		//验证cron表达式
		if(CronExpression.isValidExpression(cronExpression)){
			scheduleJobService.modifyTrigger(name, group, cronExpression);
			return "success";
		}else{
			return "Cron表达式不正确";
		}
	}

	/**
	 * 立即运行一次
	 */
	@RequiresRoles("admin")
	@RequestMapping("/{name}/{group}/startNow")
	@ResponseBody
	public String stratNow(@PathVariable String name, @PathVariable String group) {
		scheduleJobService.startNowJob(name, group);
		return "success";
	}

	/**
	 * 恢复
	 */
	@RequiresRoles("admin")
	@RequestMapping("/{name}/{group}/resume")
	@ResponseBody
	public String resume(@PathVariable String name, @PathVariable String group) {
		scheduleJobService.restartJob(name, group);
		return "success";
	}
	
	/**
	 * 获取所有trigger
	 */
	public void getTriggers(HttpServletRequest request) {
		List<ScheduleJob> scheduleJobs = scheduleJobService.getTriggersInfo();
		System.out.println(scheduleJobs.size());
		request.setAttribute("triggers", scheduleJobs);
	}
	
	/**
	 * cron表达式生成页
	 */
	@RequestMapping("quartzCron")
	public String quartzCronCreate(){
		return "system/quartzCron";
	}
}
