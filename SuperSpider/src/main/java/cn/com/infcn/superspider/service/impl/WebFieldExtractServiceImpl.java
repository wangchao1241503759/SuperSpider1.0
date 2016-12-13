/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd
 * @date：2016年5月29日
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
import cn.com.infcn.superspider.service.WebFieldExtractServiceI;

/**
 * @author lihf
 * @date 2016年5月29日
 * @version 1.00.00
 */
@Service
public class WebFieldExtractServiceImpl implements WebFieldExtractServiceI
{

	@Autowired
	private BaseDaoI<TWebParamSetting> webParamSettingDao;
	/**
	 * 根据任务ID获取字段属性的列表
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<WebFieldExtract> getWebFieldExtractListByTaskId(String taskId) throws Exception
	{
		List<WebFieldExtract> webFieldExtractList = new ArrayList<WebFieldExtract>();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("taskId", taskId);
		String hql = " from TWebParamSetting t where t.taskId=:taskId ";
		List<TWebParamSetting> twebParamSettingList = webParamSettingDao.find(hql, params);
		for(TWebParamSetting t:twebParamSettingList)
		{
			List<TWebFieldExtract> twebFieldExtractList = t.getFieldExtractList();
			for(TWebFieldExtract t_twebFieldExtract:twebFieldExtractList)
			{
				WebFieldExtract webFieldExtract = new WebFieldExtract();
				BeanUtils.copyProperties(t_twebFieldExtract, webFieldExtract);
				webFieldExtractList.add(webFieldExtract);
			}
		}
		return webFieldExtractList;
	}

}
