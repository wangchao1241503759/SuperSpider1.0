/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年6月6日
 */
package cn.com.infcn.superspider.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.infcn.ade.common.dao.BaseDaoI;
import cn.com.infcn.superspider.model.TWebDownloader;
import cn.com.infcn.superspider.pagemodel.WebDownloader;
import cn.com.infcn.superspider.service.WebDownloaderServiceI;

/**
 * @author lihf
 * @date 2016年6月6日
 */
@Service
public class WebDownloaderServiceImpl implements WebDownloaderServiceI
{

	@Autowired
	private BaseDaoI<TWebDownloader> webDownloaderDao;
	/**
	 * 获取下载器列表
	 * @author lihf
	 * @date 2016年6月6日	下午3:17:49
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<WebDownloader> getWebDownloaderList() throws Exception
	{
		List<WebDownloader> webDownloaderList = new ArrayList<WebDownloader>();
		String hql = " from TWebDownloader t where t.state = 'Y'";
		List<TWebDownloader> twebDownloaderList = webDownloaderDao.find(hql);
		for(TWebDownloader t:twebDownloaderList)
		{
			WebDownloader webDownloader = new WebDownloader();
			BeanUtils.copyProperties(t, webDownloader);
			webDownloaderList.add(webDownloader);
		}
		return webDownloaderList;
	}
	
	/**
	 * 根据下载器ID，获取下载器对象
	 * @author lihf
	 * @date 2016年6月6日	下午4:02:42
	 * @param downloaderId
	 * @return
	 */
	@Override
	public WebDownloader getWebDownloaderById(String downloaderId) throws Exception
	{
		WebDownloader webDownloader = null;
		TWebDownloader t = webDownloaderDao.get(TWebDownloader.class, downloaderId);
		if(null!=t)
		{
			webDownloader = new WebDownloader();
			BeanUtils.copyProperties(t, webDownloader);
		}
		return webDownloader;
	}

}
