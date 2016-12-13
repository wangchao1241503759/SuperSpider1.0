/**
 * 
 */
package cn.com.infcn.superspider.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.infcn.ade.common.controller.BaseController;
import cn.com.infcn.superspider.pagemodel.DbConfig;
import cn.com.infcn.superspider.service.impl.TriggerServiceImpl;

/**
 * @description 
 * @author WChao
 * @date   2016年2月19日 	上午9:49:57
 */
@Controller
@RequestMapping("trigger")
public class TriggerController extends BaseController{
	
	@Autowired
	private TriggerServiceImpl triggerService;
	/**
	 * 创建触发器;
	 */
	@RequestMapping(value="createCrigger",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> createTrigger(DbConfig dbConfig)
	{
		Map<String,Object> status = triggerService.createTrigger(dbConfig);
		return status;
	}
	
	/**
	 * 删除触发器;
	 */
	@RequestMapping(value="deleteCrigger",method = RequestMethod.POST)
	@ResponseBody
	public String deleteTrigger(DbConfig dbConfig)
	{
		triggerService.deleteTrigger(dbConfig);
		return "success";
	}
}
