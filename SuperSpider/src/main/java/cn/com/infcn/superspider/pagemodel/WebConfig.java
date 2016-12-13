/**
 * 
 */
package cn.com.infcn.superspider.pagemodel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cn.com.infcn.superspider.model.FieldMapping;
import cn.com.infcn.superspider.model.TWebParamSetting;
import cn.com.infcn.superspider.model.TWebTypeRule;

/**
 * @description 
 * @author WChao
 * @date   2015年12月21日 	下午5:25:57
 */
public class WebConfig implements Serializable{

    private static final long serialVersionUID = 8479363493133971813L;
    
	/**
	 * 任务基本信息;
	 */
	private String taskId;//任务id;
	private String taskType;//任务类型;
	private String taskName;//任务名称;
	private String taskDescription;//任务说明;
	private String taskPlanId;//计划Id;
	private String taskCreator;//创建人;
	private Date   taskCreateDate;//创建时间;
	private String taskState;//任务状态;0:闲置、1:执行中、2：停止;
	private String priorityLevel;	//任务优先级;0:最低、1:低、2:中、3:高、4:最高
	
	/**
	 * 源数据配置信息
	 */
    private String webId;			//主键ID
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
    private String proxyServerIds;	//代理服务ids
    private String httpMethod;		//请求访求
    
    
	/**
	 * 输出配置信息;
	 */
	private String topId;//主键;
	private String topType;//输出类型;
	private String topHost;//输出服务器地址;
	private Integer topPort;//端口号;
	private String topUserName;//用户名;
	private String topPassWord;//密码;
	private String topDbName;//数据库名;
	private String topTableName;//表名称;
	private String topSchema;//输出别名(Shcema);
	private String topIsClear;//是否清空数据库表;
	private Integer topThread;//输出线程数;
	private String topMssType;//输出元数据仓储所在库类型;
	
	
    private String webTypeRuleJson;			//页面类型规则
    private String paramObjectListJson;		//解析参数设置
    private String fieldMappingJson;		//字段映射
    private WebDownloader webDownloader;	//下载器对象
    
    List<TWebTypeRule> typelist ;//页面类型规则集合对象
	List<TWebParamSetting> paramlist;//解析参数设置集合对象
	List<FieldMapping> fieldList;//字段映射集合对象;
	
	/**
	 * @return the taskId
	 */
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
	 * @return the taskType
	 */
	public String getTaskType()
	{
		return taskType;
	}
	/**
	 * @param taskType the taskType to set
	 */
	public void setTaskType(String taskType)
	{
		this.taskType = taskType;
	}
	/**
	 * @return the taskName
	 */
	public String getTaskName()
	{
		return taskName;
	}
	/**
	 * @param taskName the taskName to set
	 */
	public void setTaskName(String taskName)
	{
		this.taskName = taskName;
	}
	/**
	 * @return the taskDescription
	 */
	public String getTaskDescription()
	{
		return taskDescription;
	}
	/**
	 * @param taskDescription the taskDescription to set
	 */
	public void setTaskDescription(String taskDescription)
	{
		this.taskDescription = taskDescription;
	}
	/**
	 * @return the taskPlanId
	 */
	public String getTaskPlanId()
	{
		return taskPlanId;
	}
	/**
	 * @param taskPlanId the taskPlanId to set
	 */
	public void setTaskPlanId(String taskPlanId)
	{
		this.taskPlanId = taskPlanId;
	}
	/**
	 * @return the taskCreator
	 */
	public String getTaskCreator()
	{
		return taskCreator;
	}
	/**
	 * @param taskCreator the taskCreator to set
	 */
	public void setTaskCreator(String taskCreator)
	{
		this.taskCreator = taskCreator;
	}
	/**
	 * @return the taskCreateDate
	 */
	public Date getTaskCreateDate()
	{
		return taskCreateDate;
	}
	/**
	 * @param taskCreateDate the taskCreateDate to set
	 */
	public void setTaskCreateDate(Date taskCreateDate)
	{
		this.taskCreateDate = taskCreateDate;
	}
	/**
	 * @return the taskState
	 */
	public String getTaskState()
	{
		return taskState;
	}
	/**
	 * @param taskState the taskState to set
	 */
	public void setTaskState(String taskState)
	{
		this.taskState = taskState;
	}
	/**
	 * @return the priorityLevel
	 */
	public String getPriorityLevel()
	{
		return priorityLevel;
	}
	/**
	 * @param priorityLevel the priorityLevel to set
	 */
	public void setPriorityLevel(String priorityLevel)
	{
		this.priorityLevel = priorityLevel;
	}
	/**
	 * @return the webId
	 */
	public String getWebId()
	{
		return webId;
	}
	/**
	 * @param webId the webId to set
	 */
	public void setWebId(String webId)
	{
		this.webId = webId;
	}
	/**
	 * @return the url
	 */
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
	 * @return the requestTimeout
	 */
	public int getRequestTimeout()
	{
		return requestTimeout;
	}
	/**
	 * @param requestTimeout the requestTimeout to set
	 */
	public void setRequestTimeout(int requestTimeout)
	{
		this.requestTimeout = requestTimeout;
	}
	/**
	 * @return the retryTime
	 */
	public int getRetryTime()
	{
		return retryTime;
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
	 * @return the proxyServerIds
	 */
	public String getProxyServerIds()
	{
		return proxyServerIds;
	}
	/**
	 * @param proxyServerIds the proxyServerIds to set
	 */
	public void setProxyServerIds(String proxyServerIds)
	{
		this.proxyServerIds = proxyServerIds;
	}
	/**
	 * @return the httpMethod
	 */
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
	/**
	 * @return the topId
	 */
	public String getTopId()
	{
		return topId;
	}
	/**
	 * @param topId the topId to set
	 */
	public void setTopId(String topId)
	{
		this.topId = topId;
	}
	/**
	 * @return the topType
	 */
	public String getTopType()
	{
		return topType;
	}
	/**
	 * @param topType the topType to set
	 */
	public void setTopType(String topType)
	{
		this.topType = topType;
	}
	/**
	 * @return the topHost
	 */
	public String getTopHost()
	{
		return topHost;
	}
	/**
	 * @param topHost the topHost to set
	 */
	public void setTopHost(String topHost)
	{
		this.topHost = topHost;
	}
	/**
	 * @return the topPort
	 */
	public Integer getTopPort()
	{
		return topPort;
	}
	/**
	 * @param topPort the topPort to set
	 */
	public void setTopPort(Integer topPort)
	{
		this.topPort = topPort;
	}
	/**
	 * @return the topUserName
	 */
	public String getTopUserName()
	{
		return topUserName;
	}
	/**
	 * @param topUserName the topUserName to set
	 */
	public void setTopUserName(String topUserName)
	{
		this.topUserName = topUserName;
	}
	/**
	 * @return the topPassWord
	 */
	public String getTopPassWord()
	{
		return topPassWord;
	}
	/**
	 * @param topPassWord the topPassWord to set
	 */
	public void setTopPassWord(String topPassWord)
	{
		this.topPassWord = topPassWord;
	}
	/**
	 * @return the topDbName
	 */
	public String getTopDbName()
	{
		return topDbName;
	}
	/**
	 * @param topDbName the topDbName to set
	 */
	public void setTopDbName(String topDbName)
	{
		this.topDbName = topDbName;
	}
	/**
	 * @return the topTableName
	 */
	public String getTopTableName()
	{
		return topTableName;
	}
	/**
	 * @param topTableName the topTableName to set
	 */
	public void setTopTableName(String topTableName)
	{
		this.topTableName = topTableName;
	}
	/**
	 * @return the topSchema
	 */
	public String getTopSchema()
	{
		return topSchema;
	}
	/**
	 * @param topSchema the topSchema to set
	 */
	public void setTopSchema(String topSchema)
	{
		this.topSchema = topSchema;
	}
	/**
	 * @return the topIsClear
	 */
	public String getTopIsClear()
	{
		return topIsClear;
	}
	/**
	 * @param topIsClear the topIsClear to set
	 */
	public void setTopIsClear(String topIsClear)
	{
		this.topIsClear = topIsClear;
	}
	/**
	 * @return the topThread
	 */
	public Integer getTopThread()
	{
		return topThread;
	}
	/**
	 * @param topThread the topThread to set
	 */
	public void setTopThread(Integer topThread)
	{
		this.topThread = topThread;
	}
	/**
	 * @return the topMssType
	 */
	public String getTopMssType()
	{
		return topMssType;
	}
	/**
	 * @param topMssType the topMssType to set
	 */
	public void setTopMssType(String topMssType)
	{
		this.topMssType = topMssType;
	}
	/**
	 * @return the webTypeRuleJson
	 */
	public String getWebTypeRuleJson()
	{
		return webTypeRuleJson;
	}
	/**
	 * @param webTypeRuleJson the webTypeRuleJson to set
	 */
	public void setWebTypeRuleJson(String webTypeRuleJson)
	{
		this.webTypeRuleJson = webTypeRuleJson;
	}
	/**
	 * @return the paramObjectListJson
	 */
	public String getParamObjectListJson()
	{
		return paramObjectListJson;
	}
	/**
	 * @param paramObjectListJson the paramObjectListJson to set
	 */
	public void setParamObjectListJson(String paramObjectListJson)
	{
		this.paramObjectListJson = paramObjectListJson;
	}
	/**
	 * @return the fieldMappingJson
	 */
	public String getFieldMappingJson()
	{
		return fieldMappingJson;
	}
	/**
	 * @param fieldMappingJson the fieldMappingJson to set
	 */
	public void setFieldMappingJson(String fieldMappingJson)
	{
		this.fieldMappingJson = fieldMappingJson;
	}
	public List<TWebTypeRule> getTypelist() {
		return typelist;
	}
	public void setTypelist(List<TWebTypeRule> typelist) {
		this.typelist = typelist;
	}
	public List<TWebParamSetting> getParamlist() {
		return paramlist;
	}
	public void setParamlist(List<TWebParamSetting> paramlist) {
		this.paramlist = paramlist;
	}
	public List<FieldMapping> getFieldList() {
		return fieldList;
	}
	public void setFieldList(List<FieldMapping> fieldList) {
		this.fieldList = fieldList;
	}
	/**
	 * @return the webDownloader
	 */
	public WebDownloader getWebDownloader()
	{
		return webDownloader;
	}
	/**
	 * @param webDownloader the webDownloader to set
	 */
	public void setWebDownloader(WebDownloader webDownloader)
	{
		this.webDownloader = webDownloader;
	}
}
