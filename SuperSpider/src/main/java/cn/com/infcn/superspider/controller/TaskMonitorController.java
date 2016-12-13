/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年8月1日
 */
package cn.com.infcn.superspider.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.infcn.ade.common.controller.BaseController;
import cn.com.infcn.ade.system.model.User;
import cn.com.infcn.superspider.common.Constant;
import cn.com.infcn.superspider.model.Task;
import cn.com.infcn.superspider.model.TaskStaticInfo;
import cn.com.infcn.superspider.pagemodel.TaskStaticInfoVO;
import cn.com.infcn.superspider.service.TaskLogServiceI;
import cn.com.infcn.superspider.service.TaskMonitorServiceI;
import cn.com.infcn.superspider.utils.StringUtil;

/**
 * @author lihf
 * @date 2016年8月1日
 */
@Controller
@RequestMapping("taskMonitorController")
public class TaskMonitorController extends BaseController 
{

	@Autowired
	private TaskMonitorServiceI taskMonitorService;
	@Autowired
	private TaskLogServiceI taskLogService;
	
	/**
	 * 跳转到任务监控页面
	 * 
	 * @author lihf
	 * @date 2016年8月1日	下午2:26:02
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("manager")
	public String manager(HttpServletRequest request, HttpSession session) throws Exception
	{	
		User user = (User) session.getAttribute("user");
		Map<String, Object> map = this.taskMonitorService.initMonitorInfo(user.getLoginName());
		request.setAttribute("map", map);
		return Constant.SUPERSPIDER+"/monitor/taskMonitor";
	}
	
	/**
	 *  初始化日志详情
	 * @param rq
	 * @return
	 */
	@RequestMapping("gotoLogPage")
	public String gotoLogPage(HttpServletRequest rq){
		
		rq.setAttribute("taskId", rq.getParameter("taskId"));
		rq.setAttribute("event", rq.getParameter("event"));
		
		return Constant.SUPERSPIDER+"/monitor/taskLog";
	}
	
	/**
	 * @author lihf
	 * @date 2016年8月2日	下午3:11:50
	 * @return
	 */
	public List<Task> getTaskRunningList()
	{
		return null;
	}
	
	/**
	 *  获取当前运行的所有任务
	 * @author yangy
	 * @return JSON
	 */
	@RequestMapping("getAllTask")
	@ResponseBody
	public List<Task> getAllTask(HttpSession session, @Valid Task t){
		
		User user = (User) session.getAttribute("user");
		
		List<Task> list = null;
		try {
			list = this.taskMonitorService.getTaskRunningList(t.getTaskId(), user.getLoginName());
			Task task = new Task();
			task.setTaskId("all");
			task.setTaskName("所有采集任务");
			list.add(task);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
		
	}
	
	/**
	 *  监控当前运行人的每分钟条数
	 */
	@RequestMapping("getTaskRunningTask")
	@ResponseBody
	public Map<String, Object> getTaskRunningTask(HttpServletRequest rq, HttpSession session){
		
		try {
			User user = (User) session.getAttribute("user");
			String taskId = rq.getParameter("taskId");
			return this.taskMonitorService.getTaskRunningTask(taskId, user.getLoginName());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	@RequestMapping("getTaskRunningInfo")
	@ResponseBody
	public List<TaskStaticInfoVO> getTaskRunningInfo(HttpServletRequest rq, TaskStaticInfo t, HttpSession session){
		
		try {
			User user = (User) session.getAttribute("user");
			String taskStatus = rq.getParameter("task");
			String taskName = rq.getParameter("taskName");
			String str = rq.getParameter("taskCount");
			Map<String, Object> map = new HashMap<>();
			if(!StringUtil.isEmpty(str)){
				String[] arr = str.split(",");
				for (String string : arr) {
					map.put(string.split("@")[0], string.split("@")[1]);
				}
			}
			
			t = new TaskStaticInfo();
			t.setTaskStatus(taskStatus);
			t.setTaskName(taskName);
			return this.taskMonitorService.getTaskRunningInfo(t, map, user.getLoginName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	/**
	 *  获取首页本月每天采集数据量
	 * @return
	 */
	@RequestMapping("getIndexBarInfo")
	@ResponseBody
	public List<Map<String, Object>> getIndexBarInfo(){
		return this.taskLogService.getIndexBarInfo();
	}
	
	/**
	 *  初始化index_main页面信息
	 * @param rq
	 * @return
	 */
	@RequestMapping("initIndexMainPage")
	public String initIndexMainPage(HttpServletRequest rq){
		
		Map<String, Object> map = this.taskLogService.getIndexMainInfo();
		rq.setAttribute("data", map);
		return "system/index_main";
	}
	/**
	 *  获取限制任务列表
	 * @return
	 */
	@RequestMapping("getNotRuningTask")
	@ResponseBody
	public List<TaskStaticInfoVO> getNotRuningTask(HttpServletRequest rq, HttpSession session){
		
		try {
			
			User user = (User) session.getAttribute("user");
			
			String taskName = rq.getParameter("taskName");
			String taskStatus = rq.getParameter("taskStatus");
			
			TaskStaticInfoVO v = new TaskStaticInfoVO();
			v.setTaskName(taskName);
			v.setTaskStatus(taskStatus);
			
			return this.taskMonitorService.getNotRuningTask(v, user.getLoginName());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
