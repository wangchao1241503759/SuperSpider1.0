/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年6月6日
 */
package cn.com.infcn.superspider.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @author lihf
 * @date 2016年6月6日
 */
@Entity
@Table(name="web_downloader")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TWebDownloader implements Serializable
{
    private static final long serialVersionUID = -3238658157101558729L;
    
	private String downloaderId;		//主键ID
	private String downloader;			//下载器
	private String downloaderName;		//下载器名称
	private String parseName;			//解析器名称
	private String state;				//状态（启用：Y,停用：N）	

	/**
	 * @return the downloaderId
	 */
	@Id
	@Column(name="downloader_id",nullable=false,unique=true,length=32)
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
	@Column(name="downloader",nullable=false,unique=true,length=500,columnDefinition="varchar(500)")
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
	@Column(name="downloader_name",nullable=false,unique=true,length=100)
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
	@Column(name="parse_name",length=100)
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
	/**
	 * @return the state
	 */
	@Column(name="state",nullable=false,length=1)
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
}
