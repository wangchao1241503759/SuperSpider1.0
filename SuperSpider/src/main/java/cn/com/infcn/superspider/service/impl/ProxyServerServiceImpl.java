/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年4月11日
 */
package cn.com.infcn.superspider.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import opennlp.tools.util.StringUtil;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.infcn.ade.common.dao.BaseDaoI;
import cn.com.infcn.superspider.model.TProxyServer;
import cn.com.infcn.superspider.model.TWebSourceProxy;
import cn.com.infcn.superspider.pagemodel.EasyUIDataGrid;
import cn.com.infcn.superspider.pagemodel.ProxyServer;
import cn.com.infcn.superspider.service.ProxyServerServiceI;

/**
 * @author lihf
 * @date 2016年4月11日
 */
@Service
@SuppressWarnings({"unchecked","rawtypes"})
public class ProxyServerServiceImpl implements ProxyServerServiceI
{
	@Autowired
	private BaseDaoI<TProxyServer> proxyServerDao;
	@Autowired
	private BaseDaoI<TWebSourceProxy> twebSourceProxyDao;

	/**
	 * 添加代理服务
	 * 
	 * @author lihf
	 * @date 2016年4月11日 上午9:55:46
	 * @param proxyServer
	 * @throws Exception
	 */
	@Override
	public void add(ProxyServer proxyServer) throws Exception
	{
		if (null == proxyServer)
		{
			throw new Exception("参数不能为空！");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ip", proxyServer.getIp());
		String hql = " from TProxyServer t where 1=1 and t.ip=:ip";
		long count = proxyServerDao.count("select count(t.id) " + hql, params);
		if (count > 0)
		{
			throw new Exception("该代理IP已经存在，请重新添加！");
		}
		TProxyServer t = new TProxyServer();
		BeanUtils.copyProperties(proxyServer, t);
		proxyServerDao.save(t);
	}

	/**
	 * 修改代理服务
	 * 
	 * @author lihf
	 * @date 2016年4月11日 上午9:56:43
	 * @param proxyServer
	 * @throws Exception
	 */
	@Override
	public void edit(ProxyServer proxyServer) throws Exception
	{
		if (null == proxyServer)
		{
			throw new Exception("参数不能为空！");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", proxyServer.getId());
		params.put("ip", proxyServer.getIp());
		String hql = " from TProxyServer t where 1=1 and t.id!=:id and t.ip=:ip";
		long count = proxyServerDao.count("select count(t.id) " + hql, params);
		if (count > 0)
		{
			throw new Exception("该代理IP已经存在，请重新添加！");
		}
		TProxyServer t = proxyServerDao.get(TProxyServer.class, proxyServer.getId());
		BeanUtils.copyProperties(proxyServer, t);
		proxyServerDao.saveOrUpdate(t);
	}

	/**
	 * 删除代理服务
	 * 
	 * @author lihf
	 * @date 2016年4月11日 上午9:56:56
	 * @param id
	 * @throws Exception
	 */
	@Override
	public void delete(String id) throws Exception
	{
		if (StringUtil.isEmpty(id))
		{
			throw new Exception("参数不能为空！");
		}
		TProxyServer t = proxyServerDao.get(TProxyServer.class, id);
		proxyServerDao.delete(t);
	}
	
	/**
	 * 批量删除代理服务
	 * @author lihf
	 * @date 2016年4月11日	上午9:56:56
	 * @param ids
	 * @throws Exception
	 */
	@Override
	public void deleteBatch(String ids) throws Exception
	{
		if (StringUtil.isEmpty(ids))
		{
			throw new Exception("参数不能为空！");
		}
		String[] idList = ids.split(",");
		if (null == idList || idList.length == 0)
		{
			throw new Exception("参数不能为空！");
		}
		StringBuffer sb = new StringBuffer();
	    sb.append(" where ");
	    for (String id : idList) {
	      sb.append(" t.proxyServerId = '").append(id).append("'").append(" or ");
	    }
	    String where = sb.toString().substring(0, sb.toString().length() - 3);
	    // 判断是否能删除
	    String check_hql = " from TWebSourceProxy t " + where;
	    List<TWebSourceProxy> twebSourceProxyList = twebSourceProxyDao.find(check_hql);
		for (String id : idList)
		{
			boolean delete_flag = true;
			if(null!=twebSourceProxyList && twebSourceProxyList.size()>0)
			{
				for (TWebSourceProxy temp_proxy : twebSourceProxyList)
				{
					if (temp_proxy.getProxyServerId().equalsIgnoreCase(id))
					{
						delete_flag = false;
						break;
					}
				}
			}
			if(delete_flag)
			{
				this.delete(id);
			}
		
		}
	}
	
	/**
	 * 批量删除检测是否符合删除条件代理服务
	 * @author lihf
	 * @date 2016年4月11日	上午9:56:56
	 * @param ids
	 * @throws Exception
	 */
	@Override
	public void deleteCheck(String ids) throws Exception
	{
		if (StringUtil.isEmpty(ids))
		{
			throw new Exception("参数不能为空！");
		}
		String[] idList = ids.split(",");
		if (null == idList || idList.length == 0)
		{
			throw new Exception("参数不能为空！");
		}
		StringBuffer sb = new StringBuffer();
	    sb.append(" where ");
	    for (String id : idList) {
	      sb.append(" t.proxyServerId = '").append(id).append("'").append(" or ");
	    }
	    String where = sb.toString().substring(0, sb.toString().length() - 3);
	    // 判断是否能删除
	    String check_hql = "select count(*) from TWebSourceProxy t " + where;
	    long count = twebSourceProxyDao.count(check_hql);
	    if (count > 0) {
	      throw new Exception("删除的代理服务器地址中，有代理服务器地址已经被引用，不能进行删除！");
	    }
	}

	/**
	 * 获取代理服务列表
	 * 
	 * @author lihf
	 * @date 2016年4月11日 上午10:01:07
	 * @return
	 * @throws Exception
	 */
	@Override
	public EasyUIDataGrid getProxyServerList() throws Exception
	{
		EasyUIDataGrid dataGrid = new EasyUIDataGrid();
		List<ProxyServer> proxyServerList = new ArrayList<ProxyServer>();
		String hql = " from TProxyServer t where 1=1 ";
		List<TProxyServer> tList = proxyServerDao.find(hql);
		if (tList != null && tList.size() > 0)
		{
			for (TProxyServer t : tList)
			{
				ProxyServer proxyServer = new ProxyServer();
				BeanUtils.copyProperties(t, proxyServer);
				proxyServerList.add(proxyServer);
			}
			dataGrid.setRows(proxyServerList);
			dataGrid.setTotal(Long.valueOf(String.valueOf(proxyServerList.size())));
		}
		return dataGrid;
	}

	/**
	 * 获取代理服务列表
	 * 
	 * @author lihf
	 * @date 2016年4月11日 上午10:01:07
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@Override
    public EasyUIDataGrid getProxyServerList(String ids) throws Exception
	{
		EasyUIDataGrid dataGrid = new EasyUIDataGrid();
		List<ProxyServer> proxyServerList = new ArrayList<ProxyServer>();
		if(StringUtil.isEmpty(ids))
		{
			throw new Exception("参数不能为空");
		}
	    String[] idList = ids.split(",");
	    if (null == idList || idList.length == 0) {
	      throw new Exception("参数不能为空！");
	    }
	    StringBuffer sb = new StringBuffer();
	    sb.append(" where ");
	    for (String id : idList) {
	      sb.append(" t.id = '").append(id).append("'").append(" or ");
	    }
	    String where = sb.toString().substring(0, sb.toString().length() - 3);
	    
	    String hql = " from TProxyServer t " + where;
		List<TProxyServer> tList = proxyServerDao.find(hql);
		if (tList != null && tList.size() > 0)
		{
			for (TProxyServer t : tList)
			{
				ProxyServer proxyServer = new ProxyServer();
				BeanUtils.copyProperties(t, proxyServer);
				proxyServerList.add(proxyServer);
			}
			dataGrid.setRows(proxyServerList);
			dataGrid.setTotal(Long.valueOf(String.valueOf(proxyServerList.size())));
		}
		return dataGrid;
	}
	
	/**
	 * 添加和修改代理服务
	 * @author lihf
	 * @date 2016年4月11日	上午9:55:46
	 * @param proxyServer
	 * @throws Exception
	 */
	@Override
	public void addOrEdit(ProxyServer proxyServer) throws Exception
	{
		if (null == proxyServer)
		{
			throw new Exception("参数不能为空！");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", proxyServer.getId());
		String hql = " from TProxyServer t where 1=1 and t.id=:id";
		long count = proxyServerDao.count("select count(t.id) " + hql, params);
		TProxyServer t = null;
		if (count > 0)
		{
			t = proxyServerDao.get(TProxyServer.class, proxyServer.getId());
		}
		else
		{
			 t = new TProxyServer();
		}
		BeanUtils.copyProperties(proxyServer, t);
		proxyServerDao.saveOrUpdate(t);
	}

}
