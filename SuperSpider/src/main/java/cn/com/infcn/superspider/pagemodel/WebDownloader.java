/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年6月6日
 */
package cn.com.infcn.superspider.pagemodel;

import java.io.Serializable;

/**
 * @author lihf
 * @date 2016年6月6日
 */
public class WebDownloader implements Serializable
{
    private static final long serialVersionUID = 1383216881353001933L;
    
	private String downloaderId;		//主键ID
	private String downloader;			//下载器
	private String downloaderName;		//下载器名称
	private String parseName;			//解析器名称
	private String state;				//状态（启用：Y,停用：N）	

	/**
	 * @return the state
	 */
	public String getState()
	{
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state)
	{
		this.state = state;
	}
	/**
	 * @return the downloaderId
	 */
	public String getDownloaderId()
	{
		return downloaderId;
	}
	/**
	 * @param downloaderId the downloaderId to set
	 */
	public void setDownloaderId(String downloaderId)
	{
		this.downloaderId = downloaderId;
	}
	/**
	 * @return the downloader
	 */
	public String getDownloader()
	{
		return downloader;
	}
	/**
	 * @param downloader the downloader to set
	 */
	public void setDownloader(String downloader)
	{
		this.downloader = downloader;
	}
	/**
	 * @return the downloaderName
	 */
	public String getDownloaderName()
	{
		return downloaderName;
	}
	/**
	 * @param downloaderName the downloaderName to set
	 */
	public void setDownloaderName(String downloaderName)
	{
		this.downloaderName = downloaderName;
	}
	/**
	 * @return the parseName
	 */
	public String getParseName()
	{
		return parseName;
	}
	/**
	 * @param parseName the parseName to set
	 */
	public void setParseName(String parseName)
	{
		this.parseName = parseName;
	}
}
