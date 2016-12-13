/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年5月27日
 */
package cn.com.infcn.superspider.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.infcn.ade.common.dao.BaseDaoI;
import cn.com.infcn.superspider.model.TWebTypeMatchLabel;
import cn.com.infcn.superspider.model.TWebTypeRule;
import cn.com.infcn.superspider.pagemodel.WebTypeMatchLabel;
import cn.com.infcn.superspider.pagemodel.WebTypeRule;
import cn.com.infcn.superspider.service.WebTypeRuleServiceI;

/**
 * @author lihf
 * @date 2016年5月27日
 */
@Service
public class WebTypeRuleServiceImpl implements WebTypeRuleServiceI
{

	@Autowired
	private BaseDaoI<TWebTypeRule> webTypeRuleDao;
	@Autowired
	private BaseDaoI<TWebTypeMatchLabel> webTypeMatchLabelDao;
	/**
	 * 根据任务ID获取页面类型规则列表
	 * @author lihf
	 * @date 2016年5月27日	下午4:56:22
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<WebTypeRule> getWebTypeRuleListByTaskId(String taskId) throws Exception
	{
		List<WebTypeRule> webTypeRuleList = new ArrayList<WebTypeRule>();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("taskId", taskId);
		String hql = " from TWebTypeRule t where t.taskId=:taskId  order by t.typeLevel ASC";
		List<TWebTypeRule> twebTypeRuleList = webTypeRuleDao.find(hql, params);
		for(TWebTypeRule t:twebTypeRuleList)
		{
			WebTypeRule webTypeRule = new WebTypeRule();
			BeanUtils.copyProperties(t, webTypeRule);
			List<TWebTypeMatchLabel> twebTypeMatchLabelList =t.getWebTypeMatchLabelList();
			List<WebTypeMatchLabel> webTypeMatchLabelList = new ArrayList<WebTypeMatchLabel>();
			for(TWebTypeMatchLabel t_twebTypeMatchLabel:twebTypeMatchLabelList)
			{
				WebTypeMatchLabel webTypeMatchLabel = new WebTypeMatchLabel();
//				t_twebTypeMatchLabel.setRuleType(WebUtil.convertRuleTypeNameToCn(t_twebTypeMatchLabel.getRuleType()));
				BeanUtils.copyProperties(t_twebTypeMatchLabel, webTypeMatchLabel);
				webTypeMatchLabelList.add(webTypeMatchLabel);
			}
			webTypeRule.setWebTypeMatchLabelList(webTypeMatchLabelList);
			webTypeRuleList.add(webTypeRule);
		}
		return webTypeRuleList;
	}
	
	/**
	 * 根据任务ID删除页面类型规则
	 * @param taskId
	 * @throws Exception
	 */
	@Override
	public void deleteWebTypeRuleListByTaskId(String taskId) throws Exception
	{
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("taskId", taskId);
		String hql = " from TWebTypeRule t where t.taskId=:taskId ";
		List<TWebTypeRule> twebTypeRuleList = webTypeRuleDao.find(hql, params);
		for(TWebTypeRule t:twebTypeRuleList)
		{
			List<TWebTypeMatchLabel> twebTypeMatchLabelList = t.getWebTypeMatchLabelList();
			for(TWebTypeMatchLabel t_webTypeMatchLabel:twebTypeMatchLabelList)
			{
				webTypeMatchLabelDao.delete(t_webTypeMatchLabel);
			}
			webTypeRuleDao.delete(t);
		}
//		String hql = " delete TWebTypeRule t where t.taskId=:taskId ";
//		webTypeRuleDao.executeHql(hql, params);
	}
	
	/**
	 * 根据ID获取页面类型规则
	 * @param taskId
	 * @throws Exception
	 */
	public TWebTypeRule getWebTypeRuleById(String typeId) throws Exception
	{
		return webTypeRuleDao.get(TWebTypeRule.class, typeId);
	}

}
