/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年7月13日
 */
package cn.com.infcn.superspider.service;

import cn.com.infcn.superspider.model.TFileSource;

/**
 * @author lihf
 * @date 2016年7月13日
 */
public interface FileSourceServiceI
{

	/**
	 * 根据Id获取数据源
	 * @author lihf
	 * @date 2016年7月13日	下午4:15:56
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public TFileSource getFileSource(String id) throws Exception;
	

	/**
	 * 根据任务Id获取数据源
	 * @author lihf
	 * @date 2016年7月13日	下午4:16:29
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public TFileSource getFileSourceByTaskId(String taskId) throws Exception;
	
	
	/**
	 * 根据任务ID删除来源
	 * @author lihf
	 * @date 2016年7月13日	下午4:16:49
	 * @param taskId
	 * @throws Exception
	 */
	public void deleteFileSourceByTaskId(String taskId) throws Exception;
}
