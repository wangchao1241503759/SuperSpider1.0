/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年7月12日
 */
package cn.com.infcn.superspider.service;

import cn.com.infcn.superspider.model.TFtpSource;

/**
 * @author lihf
 * @date 2016年7月12日
 */
public interface FtpSourceServiceI
{

	/**
	 * 根据Id获取数据源
	 * @author lihf
	 * @date 2016年7月12日	下午7:20:13
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public TFtpSource getFtpSource(String id) throws Exception;
	

	/**
	 * 根据任务Id获取数据源
	 * @author lihf
	 * @date 2016年7月12日	下午7:20:52
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public TFtpSource getFtpSourceByTaskId(String taskId) throws Exception;
	
	

	/**
	 * 根据任务ID删除来源
	 * @author lihf
	 * @date 2016年7月12日	下午7:21:02
	 * @param taskId
	 * @throws Exception
	 */
	public void deleteFtpSourceByTaskId(String taskId) throws Exception;
}
