package cn.com.infcn.superspider.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.com.infcn.ade.common.controller.BaseController;
import cn.com.infcn.superspider.common.Constant;

/**
 * 
 * @author WChao
 * @date 2015年12月18日
 */
@Controller
@RequestMapping("taskPlan")
public class TaskPlanController extends BaseController{

	/**
	 * 计划管理页面
	 */
	@RequestMapping(value="list",method = RequestMethod.GET)
	public String list() {
		
		return Constant.SUPERSPIDER+"/taskPlan/taskPlan_list";
	}
}
