/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年6月6日
 */
package cn.com.infcn.superspider.service;

import java.util.List;

import cn.com.infcn.superspider.pagemodel.WebDownloader;

/**
 * @author lihf
 * @date 2016年6月6日
 */
public interface WebDownloaderServiceI
{
	/**
	 * 获取下载器列表
	 * @author lihf
	 * @date 2016年6月6日	下午3:17:49
	 * @return
	 * @throws Exception
	 */
	public List<WebDownloader> getWebDownloaderList() throws Exception;
	
	/**
	 * 根据下载器ID，获取下载器对象
	 * @author lihf
	 * @date 2016年6月6日	下午4:02:42
	 * @param downloaderId
	 * @return
	 */
	public WebDownloader getWebDownloaderById(String downloaderId) throws Exception;
}
