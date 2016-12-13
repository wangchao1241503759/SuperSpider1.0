/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年4月11日
 */
package cn.com.infcn.superspider.service;

import cn.com.infcn.superspider.pagemodel.EasyUIDataGrid;
import cn.com.infcn.superspider.pagemodel.ProxyServer;

/**
 * @author lihf
 * @date 2016年4月11日
 */
@SuppressWarnings("rawtypes")
public interface ProxyServerServiceI
{

	/**
	 * 添加代理服务
	 * @author lihf
	 * @date 2016年4月11日	上午9:55:46
	 * @param proxyServer
	 * @throws Exception
	 */
	public void add(ProxyServer proxyServer) throws Exception;
	
	/**
	 * 修改代理服务
	 * @author lihf
	 * @date 2016年4月11日	上午9:56:43
	 * @param proxyServer
	 * @throws Exception
	 */
	public void edit(ProxyServer proxyServer) throws Exception;
	
	/**
	 * 删除代理服务
	 * @author lihf
	 * @date 2016年4月11日	上午9:56:56
	 * @param id
	 * @throws Exception
	 */
	public void delete(String id) throws Exception;
	
	
	/**
	 * 批量删除代理服务
	 * @author lihf
	 * @date 2016年4月11日	上午9:56:56
	 * @param ids
	 * @throws Exception
	 */
	public void deleteBatch(String ids) throws Exception;
	
	/**
	 * 批量删除检测是否符合删除条件代理服务
	 * @author lihf
	 * @date 2016年4月11日	上午9:56:56
	 * @param ids
	 * @throws Exception
	 */
	public void deleteCheck(String ids) throws Exception;
	
	/**
	 * 获取代理服务列表
	 * @author lihf
	 * @date 2016年4月11日	上午10:01:07
	 * @return
	 * @throws Exception
	 */
	public EasyUIDataGrid getProxyServerList() throws Exception;
	
	/**
	 * 获取代理服务列表
	 * @author lihf
	 * @date 2016年4月11日	上午10:01:07
	 * @param ids
	 * @return
	 * @throws Exception
	 */
    public EasyUIDataGrid getProxyServerList(String ids) throws Exception;
    
	/**
	 * 添加和修改代理服务
	 * @author lihf
	 * @date 2016年4月11日	上午9:55:46
	 * @param proxyServer
	 * @throws Exception
	 */
	public void addOrEdit(ProxyServer proxyServer) throws Exception;
}
