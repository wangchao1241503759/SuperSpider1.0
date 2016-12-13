package cn.com.infcn.superspider.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.infcn.ade.common.controller.BaseController;
import cn.com.infcn.ade.common.persistence.Page;
import cn.com.infcn.ade.common.persistence.PropertyFilter;
import cn.com.infcn.ade.system.model.User;
import cn.com.infcn.ade.system.utils.UserUtil;
import cn.com.infcn.superspider.common.Constant;
import cn.com.infcn.superspider.model.SchedulePlan;
import cn.com.infcn.superspider.model.Task;
import cn.com.infcn.superspider.service.impl.SchedulePlanServiceImpl;
import cn.com.infcn.superspider.service.impl.TaskServiceImpl;
import cn.com.infcn.superspider.utils.DateUtils;

/**
 * 定时任务 controller
 * @author WChao
 * @date 2016年3月2日
 */
@Controller
@RequestMapping("schedulePlan")
public class SchedulePlanController extends BaseController{
	
	@Autowired
	private SchedulePlanServiceImpl schedulePlanService;
	@Autowired
	private TaskServiceImpl taskService;
	
	/**
	 * 默认页面
	 * 
	 * @return
	 */
	@RequestMapping(value="list",method = RequestMethod.GET)
	public String list() {
		
		return Constant.SUPERSPIDER+"/schedulePlan/schedulePlan_list";
	}
	
	/**
	 * 获取定时任务 json
	 * @throws Exception 
	 */
	@RequestMapping(value="json",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getAllPlans(HttpServletRequest request) throws Exception{
		Page<SchedulePlan> page = getPage(request);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		User user = UserUtil.getCurrentUser();
		if(user != null){
			PropertyFilter userPropertyFilter = new PropertyFilter("EQS_creator",user.getName());
			filters.add(userPropertyFilter);
		}
		page = schedulePlanService.search(page, filters);
		if(page.getResult() != null && page.getResult().size() > 0){
			for(SchedulePlan schedulePlan : page.getResult()){
				String taskMatches = "";
				List<Task> tasks = taskService.findTaskByPlan(schedulePlan.getId());
				if(tasks != null && tasks.size()>0){
					for(int i = 0 ; i< tasks.size() ; i++){
						taskMatches += tasks.get(i).getTaskName();
						if(i<tasks.size()-1){
							taskMatches+=";";
						}
					}
				}
				schedulePlan.setTaskMatchs(taskMatches);
			}
		}
		return getEasyUIData(page);
	}
	
	/**
	 * 添加跳转
	 * @param model
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String createForm() {
		return Constant.SUPERSPIDER+"/schedulePlan/schedulePlan_add";
	}

	/**
	 * 添加
	 * @param user
	 * @param model
	 */
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public String create(@Valid SchedulePlan schedulePlan) {
		//验证cron表达式
		if(CronExpression.isValidExpression(schedulePlan.getCronExpression())){
			schedulePlan.setStatus("1");
			schedulePlanService.add(schedulePlan);
		}else{
			return "Cron表达式不正确";
		}
		return "success";
	}

	@RequestMapping(value="delete",method = RequestMethod.POST)
	@ResponseBody
	public String delete(@RequestBody List<String> idList)throws Exception{
		schedulePlanService.deleteBatch(idList);
		return "success";
	}
	
	@RequestMapping(value="isMatchTask",method = RequestMethod.POST)
	@ResponseBody
	public boolean isMatchTask(@RequestBody List<String> idList)throws Exception{
		boolean isMatch = false;
		for(String id : idList){
			List<Task> taskList = taskService.findTaskByPlan(id);
			if(taskList != null && taskList.size() > 0){
				isMatch = true;
				break;
			}
		}
		return isMatch;
	}
	/**
	 * 修改调度计划跳转
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id")String id, Model model) {
		SchedulePlan schedulePlan = schedulePlanService.get(id);
		Date createTime = schedulePlan.getCreateTime();
		if(createTime != null){
			schedulePlan.setCreateTimeStr(DateUtils.getdatestr(createTime,"yyyy-MM-dd HH:mm:ss"));
		}
		model.addAttribute("schedulePlan",schedulePlan);
		return Constant.SUPERSPIDER+"/schedulePlan/schedulePlan_add";
	}
	/**
	 * 暂停任务
	 */
	@RequestMapping("stop")
	@ResponseBody
	public String stop(@RequestBody List<String> idList) {
		schedulePlanService.stopJob(idList);
		return "success";
	}
	/**
	 * 立即运行一次
	 */
	@RequestMapping("startNow")
	@ResponseBody
	public String stratNow(@RequestBody List<String> idList) {
		schedulePlanService.startNowJob(idList);
		return "success";
	}

	/**
	 * 恢复
	 */
	@RequestMapping("resume")
	@ResponseBody
	public String resume(@RequestBody List<String> idList) {
		schedulePlanService.restartJob(idList);
		return "success";
	}
	public static void main(String[] args) {
		String test = "* * * ? * 3,5,7/5";
		System.out.println(CronExpression.isValidExpression(test));
	}
	
}
