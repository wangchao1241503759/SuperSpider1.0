/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年4月6日
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
 * @date 2016年4月6日
 */
@Entity
@Table(name="web_source")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TWebSource implements Serializable
{

    private static final long serialVersionUID = -847421796641000229L;
    
    private String id;				//主键ID
    private String taskId;			//任务ID
    private String url; 			//web目标url
    private String downloaderId;	//下载器ID
    private String charset; 		//站点字符集
    private int requestTimeout;  	//请求超时时间
    private int retryTime; 		 	//出错重试次数
    private int loadWaitTime;		//异步加载等待时间
    private int fetchThreadNum;		//爬取线程数
    private	int fetchIntervalTime;	//爬取间隔时间
    private String needLogin;		//是否需要登录  Y:需要，N:不需要
    private String userName;		//用户名
    private String password;		//密码
    private String needProxy;		//是否使用代理  Y:需要，N:不需要
    private String httpMethod;		//请求访求
    
	/**
	 * @return the id
	 */
    @Id
    @Column(name="id",nullable=false,unique=true,length=32)
	public String getId()
	{
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}
	/**
	 * @return the taskId
	 */
	@Column(name="task_id",nullable=false,unique=true,length=32)
	public String getTaskId()
	{
		return taskId;
	}
	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}
	/**
	 * @return the url
	 */
	@Column(name="url",nullable=false,length=5000)
	public String getUrl()
	{
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url)
	{
		this.url = url;
	}
	/**
	 * @return the downloaderId
	 */
	@Column(name="downloader_id",nullable=false,length=32)
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
	 * @return the charset
	 */
	@Column(name="charset",length=30)
	public String getCharset()
	{
		return charset;
	}
	/**
	 * @param charset the charset to set
	 */
	public void setCharset(String charset)
	{
		this.charset = charset;
	}
	/**
	 * @return the requestTimeOut
	 */
	@Column(name="request_timeout",length=11,columnDefinition="int")
	public int getRequestTimeout()
	{
		return requestTimeout;
	}
	/**
	 * @param requestTimeOut the requestTimeOut to set
	 */
	public void setRequestTimeout(int requestTimeout)
	{
		this.requestTimeout = requestTimeout;
	}
	/**
	 * @return the retryTime
	 */
	@Column(name="retry_time",length=11,columnDefinition="int")
	public int getRetryTime()
	{
		return this.retryTime;
	}
	/**
	 * @param retryTime the retryTime to set
	 */
	public void setRetryTime(int retryTime)
	{
		this.retryTime = retryTime;
	}
	/**
	 * @return the loadWaitTime
	 */
	@Column(name="load_wait_time",length=11,columnDefinition="int")
	public int getLoadWaitTime()
	{
		return loadWaitTime;
	}
	/**
	 * @param loadWaitTime the loadWaitTime to set
	 */
	public void setLoadWaitTime(int loadWaitTime)
	{
		this.loadWaitTime = loadWaitTime;
	}
	/**
	 * @return the fetchThreadNum
	 */
	@Column(name="fetch_thread_num",length=11)
	public int getFetchThreadNum()
	{
		return fetchThreadNum;
	}
	/**
	 * @param fetchThreadNum the fetchThreadNum to set
	 */
	public void setFetchThreadNum(int fetchThreadNum)
	{
		this.fetchThreadNum = fetchThreadNum;
	}
	/**
	 * @return the fetchIntervalTime
	 */
	@Column(name="fetch_interval_time",length=11)
	public int getFetchIntervalTime()
	{
		return fetchIntervalTime;
	}
	/**
	 * @param fetchIntervalTime the fetchIntervalTime to set
	 */
	public void setFetchIntervalTime(int fetchIntervalTime)
	{
		this.fetchIntervalTime = fetchIntervalTime;
	}
	/**
	 * @return the needLogin
	 */
	@Column(name="need_login",length=1,columnDefinition="char")
	public String getNeedLogin()
	{
		return needLogin;
	}
	/**
	 * @param needLogin the needLogin to set
	 */
	public void setNeedLogin(String needLogin)
	{
		this.needLogin = needLogin;
	}
	/**
	 * @return the userName
	 */
	@Column(name="user_name",length=60)
	public String getUserName()
	{
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	/**
	 * @return the password
	 */
	@Column(name="password",length=30)
	public String getPassword()
	{
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}
	/**
	 * @return the needProxy
	 */
	@Column(name="need_proxy",length=1,columnDefinition="char")
	public String getNeedProxy()
	{
		return needProxy;
	}
	/**
	 * @param needProxy the needProxy to set
	 */
	public void setNeedProxy(String needProxy)
	{
		this.needProxy = needProxy;
	}
	/**
	 * @return the httpMethod
	 */
	@Column(name="http_method",length=20)
	public String getHttpMethod()
	{
		return httpMethod;
	}
	/**
	 * @param httpMethod the httpMethod to set
	 */
	public void setHttpMethod(String httpMethod)
	{
		this.httpMethod = httpMethod;
	}
    
    

}
