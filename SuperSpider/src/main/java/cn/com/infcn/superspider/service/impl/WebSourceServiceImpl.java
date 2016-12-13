/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年4月14日
 */
package cn.com.infcn.superspider.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.infcn.ade.common.dao.BaseDaoI;
import cn.com.infcn.ade.common.utils.StringUtils;
import cn.com.infcn.superspider.model.TWebSource;
import cn.com.infcn.superspider.model.TWebSourceProxy;
import cn.com.infcn.superspider.service.WebSourceServiceI;
import cn.com.infcn.superspider.utils.StringUtil;

/**
 * @author lihf
 * @date 2016年4月14日
 */
@Service
public class WebSourceServiceImpl implements WebSourceServiceI
{

	@Autowired
	private BaseDaoI<TWebSource> webSourceDao;
	@Autowired
	private BaseDaoI<TWebSourceProxy> webSourceProxyDao;

	/**
	 * 根据Id获取数据源
	 * 
	 * @author lihf
	 * @date 2016年4月14日 上午11:08:05
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	public TWebSource getWebSource(String id) throws Exception
	{
		if (StringUtil.isEmpty(id))
		{
			throw new Exception("参数不能为空！");
		}
		return webSourceDao.get(TWebSource.class, id);
	}

	/**
	 * 根据任务Id获取数据源
	 * 
	 * @author lihf
	 * @date 2016年4月14日 上午11:09:07
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	@Override
	public TWebSource getWebSourceByTaskId(String taskId) throws Exception
	{
		if (StringUtil.isEmpty(taskId))
		{
			throw new Exception("参数不能为空！");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("taskId", taskId);
		String hql = " from TWebSource t where 1=1 and t.taskId=:taskId ";
		TWebSource t = webSourceDao.get(hql, params);
		return t;
	}

	/**
	 * 根据数据源的ID，获取代理服务
	 * 
	 * @author lihf
	 * @date 2016年4月14日 下午2:10:20
	 * @param webSourceId
	 * @return
	 * @throws Exception
	 */
	@Override
    public String getProxyServer(String webSourceId) throws Exception
    {
		String ids = "";
		if (StringUtil.isEmpty(webSourceId))
		{
			throw new Exception("参数不能为空！");
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("webSourceId", webSourceId);
		String hql = " from TWebSourceProxy t where 1=1 and t.webSourceId=:webSourceId ";
		List<TWebSourceProxy> twebSourceProxyList = webSourceProxyDao.find(hql, params);
		if(null!=twebSourceProxyList && twebSourceProxyList.size()>0)
		{
			StringBuffer sb = new StringBuffer();
			for(TWebSourceProxy proxy:twebSourceProxyList)
			{
				sb.append(proxy.getProxyServerId()).append(",");
			}
			ids = sb.toString().substring(0, sb.toString().length() - 1);
		}
		return ids;
    }
	
	/**
	 * 根据任务ID删除来源
	 * @author lihf
	 * @date 2016年6月16日	上午11:09:48
	 * @param taskId
	 * @throws Exception
	 */
	public void deleteWebSourceByTaskId(String taskId) throws Exception
	{
		if(StringUtils.isEmpty(taskId))
		{
			throw new Exception("参数不能为空！");
		}
		String hql = " from TWebSource t where t.taskId=:taskId ";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("taskId", taskId);
		List<TWebSource> twebSourceList = webSourceDao.find(hql, params);
		if(null!=twebSourceList && twebSourceList.size()>0)
		{
			for(TWebSource twebsource:twebSourceList)
			{
				String delete_proxy_hql = " from TWebSourceProxy t where t.webSourceId=:webSourceId ";
				Map<String,Object> params_proxy = new HashMap<String,Object>();
				params_proxy.put("webSourceId", twebsource.getId());
				List<TWebSourceProxy> sourceProxyList = webSourceProxyDao.find(delete_proxy_hql, params_proxy);
				if(null!=sourceProxyList && sourceProxyList.size()>0)
				{
					for(TWebSourceProxy twsourceproxy:sourceProxyList)
					{
						webSourceProxyDao.delete(twsourceproxy);
					}
				}
				webSourceDao.delete(twebsource);
			}
		}
	}

}
