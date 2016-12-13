/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd
 * @date：2016年5月29日
 */
package cn.com.infcn.superspider.service;

import java.util.List;

import cn.com.infcn.superspider.pagemodel.WebFieldExtract;

/**
 * @author lihf
 * @date 2016年5月29日
 * @version 1.00.00
 */
public interface WebFieldExtractServiceI
{

	/**
	 * 根据任务ID获取字段属性的列表
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public List<WebFieldExtract> getWebFieldExtractListByTaskId(String taskId) throws Exception;
}
