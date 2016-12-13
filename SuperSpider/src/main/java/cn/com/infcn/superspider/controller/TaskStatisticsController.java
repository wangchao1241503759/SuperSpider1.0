/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年9月14日
 */
package cn.com.infcn.superspider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.infcn.ade.common.controller.BaseController;
import cn.com.infcn.superspider.common.Constant;
import cn.com.infcn.superspider.pagemodel.EchartsConfig;
import cn.com.infcn.superspider.pagemodel.Json;
import cn.com.infcn.superspider.pagemodel.TaskStatisticsConfig;
import cn.com.infcn.superspider.service.TaskStaticServiceI;

/**
 * @author lihf
 * @date 2016年9月14日
 */
@Controller
@RequestMapping("taskStatisticsController")
public class TaskStatisticsController extends BaseController
{
	@Autowired
	private TaskStaticServiceI taskStaticService;
	
	/**
	 * 跳转到任务统计页面
	 * @author lihf
	 * @date 2016年9月14日	下午5:22:33
	 * @return
	 */
	@RequestMapping("manager")
	public String manager()
	{
		return Constant.SUPERSPIDER+"/task/task_statistics";
	}
	
	/**
	 * 获取任务统计数据
	 * @author lihf
	 * @date 2016年9月20日	下午4:55:14
	 * @param taskStatisticsConfig
	 * @return
	 */
	@RequestMapping("getTaskStatisticsData")
	@ResponseBody
	public Json getTaskStatisticsData(TaskStatisticsConfig taskStatisticsConfig){
		Json json = new Json();
		try
        {
			EchartsConfig echartsConfig = null;
			echartsConfig = taskStaticService.getTaskStatistics(taskStatisticsConfig);
			json.setSuccess(true);
			json.setObj(echartsConfig);
        }
        catch (Exception e)
        {
        	json.setMsg(e.getMessage());
        }
		return json;
	}

}
