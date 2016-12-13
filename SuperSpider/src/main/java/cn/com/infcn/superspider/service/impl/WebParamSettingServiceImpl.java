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
import cn.com.infcn.superspider.model.TWebFieldExtract;
import cn.com.infcn.superspider.model.TWebParamSetting;
import cn.com.infcn.superspider.pagemodel.WebFieldExtract;
import cn.com.infcn.superspider.pagemodel.WebParamSetting;
import cn.com.infcn.superspider.service.WebParamSettingServiceI;

/**
 * @author lihf
 * @date 2016年5月27日
 */
@Service
public class WebParamSettingServiceImpl implements WebParamSettingServiceI
{

	@Autowired
	private BaseDaoI<TWebParamSetting> webParamSettingDao;
	@Autowired
	private BaseDaoI<TWebFieldExtract> webFieldExtractDao;
	/**
	 * 根据任务ID获取参数设置列表
	 * @author lihf
	 * @date 2016年5月27日	下午6:07:48
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<WebParamSetting> getWebParamSettingByTaskId(String taskId) throws Exception
	{
		List<WebParamSetting> webParamSettingList = new ArrayList<WebParamSetting>();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("taskId", taskId);
		String hql = " from TWebParamSetting t where t.taskId=:taskId ";
		List<TWebParamSetting> twebParamSettingList = webParamSettingDao.find(hql, params);
		for(TWebParamSetting t:twebParamSettingList)
		{
			WebParamSetting webParamSetting = new WebParamSetting();
			BeanUtils.copyProperties(t, webParamSetting,new String[] {"fieldExtractList"});
			List<TWebFieldExtract> twebFieldExtractList = t.getFieldExtractList();
			List<WebFieldExtract> webFieldExtractList = new ArrayList<WebFieldExtract>();
			for(TWebFieldExtract t_twebFieldExtract:twebFieldExtractList)
			{
				WebFieldExtract webFieldExtract = new WebFieldExtract();
				BeanUtils.copyProperties(t_twebFieldExtract, webFieldExtract);
				webFieldExtractList.add(webFieldExtract);
			}
			webParamSetting.setFieldExtractList(webFieldExtractList);
			webParamSettingList.add(webParamSetting);
		}
		return webParamSettingList;
	}

	/**
	 * 根据任务ID删除参数设置
	 * @param taskId
	 * @throws Exception
	 */
	@Override
	public void deleteWebParamSettingByTaskId(String taskId) throws Exception
	{
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("taskId", taskId);
		String hql = " from TWebParamSetting t where t.taskId=:taskId ";
		List<TWebParamSetting> twebParamSettingList = webParamSettingDao.find(hql, params);
		for(TWebParamSetting t:twebParamSettingList)
		{
			List<TWebFieldExtract> twebFieldExtractList = t.getFieldExtractList();
			for(TWebFieldExtract t_twebFieldExtract:twebFieldExtractList)
			{
				webFieldExtractDao.delete(t_twebFieldExtract);
			}
			webParamSettingDao.delete(t);
		}
	}
}
