/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年4月14日
 */
package cn.com.infcn.superspider.service;

import cn.com.infcn.superspider.model.TWebSource;

/**
 * @author lihf
 * @date 2016年4月14日
 */
public interface WebSourceServiceI
{

	/**
	 * 根据Id获取数据源
	 * @author lihf
	 * @date 2016年4月14日	上午11:08:05
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public TWebSource getWebSource(String id) throws Exception;
	
	/**
	 * 根据任务Id获取数据源
	 * @author lihf
	 * @date 2016年4月14日	上午11:09:07
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public TWebSource getWebSourceByTaskId(String taskId) throws Exception;
	
	/**
	 * 根据数据源的ID，获取代理服务
	 * @author lihf
	 * @date 2016年4月14日	下午2:10:20
	 * @param webSourceId
	 * @return
	 * @throws Exception
	 */
	public String getProxyServer(String webSourceId) throws Exception;
	
	/**
	 * 根据任务ID删除来源
	 * @author lihf
	 * @date 2016年6月16日	上午11:09:48
	 * @param taskId
	 * @throws Exception
	 */
	public void deleteWebSourceByTaskId(String taskId) throws Exception;
}
