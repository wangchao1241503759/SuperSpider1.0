/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年7月26日
 */
package cn.com.infcn.superspider.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.infcn.ade.common.controller.BaseController;
import cn.com.infcn.superspider.common.Constant;
import cn.com.infcn.superspider.pagemodel.EasyUIDataGrid;
import cn.com.infcn.superspider.pagemodel.Json;
import cn.com.infcn.superspider.pagemodel.Log;
import cn.com.infcn.superspider.pagemodel.PageHelper;
import cn.com.infcn.superspider.service.TaskLogServiceI;
import cn.com.infcn.superspider.utils.WebUtil;

/**
 * @author lihf
 * @date 2016年7月26日
 */
@Controller
@RequestMapping("taskLogController")
public class TaskLogController extends BaseController
{

	@Autowired
	private TaskLogServiceI taskLogService;
	
	/**
	 * 跳转到日志列表
	 * @author lihf
	 * @date 2016年7月27日	下午6:00:20
	 * @return
	 */
	@RequestMapping("taskLogList")
	public String manager(HttpServletRequest request)
	{
		request.setAttribute("taskId", request.getParameter("taskId"));
		return Constant.SUPERSPIDER+"/log/tasklog_list";
	}
	
	/**
	 * 跳转到查看页面
	 * @author lihf
	 * @date 2016年7月27日	下午6:00:33
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("viewPage")
	public String viewPage(HttpServletRequest request,String id)
	{
		Log log = new Log();
		try
        {
	        log = taskLogService.getLogById(id);
	        request.setAttribute("log", log);
        }
        catch (Exception e)
        {
	        // TODO: handle exception
        }
		return Constant.SUPERSPIDER+"/log/tasklog_view";
	}
	/**
	 * 获取采集任务日志列表数据
	 * @author lihf
	 * @date 2016年7月26日	下午5:04:41
	 * @param log
	 * @param ph
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("dataGrid")
	@ResponseBody
	public EasyUIDataGrid<?> dataGrid(Log log, PageHelper ph, HttpServletRequest rq) throws Exception
	{
		log.setTaskName(WebUtil.getHtmlDecode(log.getTaskName()));
		log.setTaskId(rq.getParameter("taskId"));
		log.setEvent(rq.getParameter("event"));
		return taskLogService.dataGrid(log, ph);
	}
	
	  /**
	   * 批量删除网站
	   * 
	   * @author lihf
	   * @date 2015年12月2日 下午5:20:52
	   * @param ids
	   *          网站ID
	   * @return
	   */
	  @RequestMapping("/deleteBatch")
	  @ResponseBody
	  public Json deleteBatch(String ids, HttpServletRequest request) {
	    Json json = new Json();
	    try {
	    	taskLogService.deleteBatch(ids);
	      json.setSuccess(true);
	      json.setMsg("删除成功！");
	    } catch (Exception e) {
	      json.setMsg(e.getMessage());
	    }
	    return json;
	  }
	  
	  @RequestMapping("/deleteAll")
	  @ResponseBody
	  public Json deleteAll(String ids, HttpServletRequest request) {
		  Json json = new Json();
		  try {
			  taskLogService.deleteAll();
			  json.setSuccess(true);
			  json.setMsg("删除成功！");
		  } catch (Exception e) {
			  json.setMsg(e.getMessage());
		  }
		  return json;
	  }
	  
	  /**
	   * 异常处理
	 * @author lihf
	 * @date 2016年10月24日	上午11:10:28
	 * @param id
	 * @param request
	 * @return
	 */
	  @RequestMapping("exceptionHandling")
	  @ResponseBody
	public Json exceptionHandling(String id, HttpServletRequest request)
	  {
		  Json json = new Json();
		  try {
			  taskLogService.appendExceptionHandling(id);
			  json.setSuccess(true);
			  json.setMsg("异常处理成功！");
		  } catch (Exception e) {
			  json.setMsg(e.getMessage());
		  }
		  return json;
	  }
	  
	/**
	 * @author lihf
	 * @date 2016年10月27日 下午2:41:17
	 * @return
	 */
	@RequestMapping("errortask")
	public String errortask()
	{
		return Constant.SUPERSPIDER + "/log/errortask_list";
	}

	/**
	 * 异常数据列表数据
	 * 
	 * @author lihf
	 * @date 2016年10月27日 上午11:32:50
	 * @param request
	 * @param ph
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("dataGridError")
	@ResponseBody
	public EasyUIDataGrid dataGridError(HttpServletRequest request, PageHelper ph) throws Exception
	{
		return taskLogService.dataGrid(ph);
	}
	
	/**
	 * 异常数据处理
	 * @author lihf
	 * @date 2016年10月27日	下午4:48:37
	 * @param request
	 * @param ids
	 * @return
	 */
	@RequestMapping("exceptionDataProcess")
	@ResponseBody
	public Json exceptionDataProcess(HttpServletRequest request,String ids)
	{
		Json json = new Json();
		try
        {
	        taskLogService.appendExceptionProcess(ids);
	        json.setSuccess(true);
	        json.setMsg("异常处理成功！");
        }
        catch (Exception e)
        {
	        json.setMsg(e.getMessage());
        }
		return json;
	}
}
