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
import cn.com.infcn.superspider.service.SystemLogServiceI;
import cn.com.infcn.superspider.utils.WebUtil;

/**
 * @author lihf
 * @date 2016年7月26日
 */
@Controller
@RequestMapping("systemLogController")
public class systemLogController extends BaseController
{

	@Autowired
	private SystemLogServiceI systemLogService;
	
	/**
	 * 跳转到日志列表
	 * @author lihf
	 * @date 2016年7月27日	下午6:00:20
	 * @return
	 */
	@RequestMapping("systemLogList")
	public String manager()
	{
		return Constant.SUPERSPIDER+"/log/systemlog_list";
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
	        log = systemLogService.getLogById(id);
	        request.setAttribute("log", log);
        }
        catch (Exception e)
        {
	        // TODO: handle exception
        }
		return Constant.SUPERSPIDER+"/log/systemlog_view";
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
	public EasyUIDataGrid<?> dataGrid(Log log, PageHelper ph) throws Exception
	{
		log.setTaskName(WebUtil.getHtmlDecode(log.getTaskName()));
		return systemLogService.dataGrid(log, ph);
	}
	
	  /**
	   * 批量删除
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
	    	systemLogService.deleteBatch(ids);
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
			  systemLogService.deleteBatch(ids);
			  json.setSuccess(true);
			  json.setMsg("删除成功！");
		  } catch (Exception e) {
			  json.setMsg(e.getMessage());
		  }
		  return json;
	  }
}
