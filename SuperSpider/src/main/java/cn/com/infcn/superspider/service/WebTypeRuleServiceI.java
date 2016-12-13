/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年5月27日
 */
package cn.com.infcn.superspider.service;

import java.util.List;

import cn.com.infcn.superspider.model.TWebTypeRule;
import cn.com.infcn.superspider.pagemodel.WebTypeRule;

/**
 * @author lihf
 * @date 2016年5月27日
 */
public interface WebTypeRuleServiceI
{

	/**
	 * 根据任务ID获取页面类型规则列表
	 * @author lihf
	 * @date 2016年5月27日	下午4:56:22
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public List<WebTypeRule> getWebTypeRuleListByTaskId(String taskId) throws Exception;
	
	/**
	 * 根据任务ID删除页面类型规则
	 * @param taskId
	 * @throws Exception
	 */
	public void deleteWebTypeRuleListByTaskId(String taskId) throws Exception;
	
	/**
	 * 根据ID获取页面类型规则
	 * @param taskId
	 * @throws Exception
	 */
	public TWebTypeRule getWebTypeRuleById(String typeId) throws Exception;
}
