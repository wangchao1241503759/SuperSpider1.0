/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年5月27日
 */
package cn.com.infcn.superspider.service;

import java.util.List;

import cn.com.infcn.superspider.pagemodel.WebParamSetting;

/**
 * @author lihf
 * @date 2016年5月27日
 */
public interface WebParamSettingServiceI
{

	/**
	 * 根据任务ID获取参数设置列表
	 * @author lihf
	 * @date 2016年5月27日	下午6:07:48
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public List<WebParamSetting> getWebParamSettingByTaskId(String taskId) throws Exception;
	
	/**
	 * 根据任务ID删除参数设置
	 * @param taskId
	 * @throws Exception
	 */
	public void deleteWebParamSettingByTaskId(String taskId) throws Exception;
}
