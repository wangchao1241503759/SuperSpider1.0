/**
 * 
 */
package cn.com.infcn.superspider.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.infcn.ade.common.controller.BaseController;
import cn.com.infcn.superspider.common.Constant;
import cn.com.infcn.superspider.model.TaskOutput;
import cn.com.infcn.superspider.service.impl.TaskOutputServiceImpl;

/**
 * @description 
 * @author WChao
 * @date   2015年12月24日 	下午1:14:51
 */
@Controller
@RequestMapping("taskoutput")
public class TaskOutputController extends BaseController{
	
	@Autowired
	private TaskOutputServiceImpl taskOutputService;
	/**
	 * 任务输出页面
	 * @return
	 */
	@RequestMapping(value="output",method = RequestMethod.GET)
	public String output() {
		
		return Constant.SUPERSPIDER+"/task/task_output";
	}
	/**
	 * 获取所有的数据库表;
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getOutputAllTables",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,String> getOutputAllTables(TaskOutput taskOutput)throws Exception{
		return taskOutputService.getOutputAllTables(taskOutput);
	}
	/**
	 * 获取字段映射json
	 */
	@RequestMapping(value="getOutputAllColumns",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,List<String>> getOutputAllColumns(TaskOutput taskOutput,HttpServletRequest request){
		return taskOutputService.getAllColumns(taskOutput);
	}
}
