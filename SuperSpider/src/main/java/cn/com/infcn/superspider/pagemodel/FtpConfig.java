/**
 * 
 */
package cn.com.infcn.superspider.pagemodel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cn.com.infcn.superspider.model.FileContentMapping;

/**
 * @description 
 * @author WChao
 * @date   2015年12月21日 	下午5:25:57
 */
public class FtpConfig implements Serializable{

    
    private static final long serialVersionUID = -789681071998783945L;
    
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
	private String ftpId;	//主键ID
    private String charset; //站点字符集
	private String ftpIP;	//FTP服务器地址
	private String ftpPort;	//端口号
	private String ftpUserName; //用户名
	private String ftpPassword;	//密码
	private String ftpType;		//FTP类型
	private String ftpPath;		//FTP路径
	private String ftpFileName; //文件(目录)名匹配
	private String exclusion;	//排除
	private String ftpDownloadThreadNum; //下载线程数
	private String ftpPacketSize;		 //数据包大小
	private String ftpLimit;			 //文件大小范围 是否限制(Y:限制,N:不限制)
	private String ftpLimitStart;		 //限制开始xxKB
	private String ftpLimitEnd;		 	 //限制结束xxKB
	private String ftpIsSubDir;			 //是否采集子目录(Y:是,N:否)
    
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
	
	private String fileattrJson;	//文件属性
	List<FileContentMapping> fileattrList;//字段映射集合对象;

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
	 * @return the ftpId
	 */
	public String getFtpId()
	{
		return ftpId;
	}

	/**
	 * @param ftpId the ftpId to set
	 */
	public void setFtpId(String ftpId)
	{
		this.ftpId = ftpId;
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
	 * @return the ftpIP
	 */
	public String getFtpIP()
	{
		return ftpIP;
	}

	/**
	 * @param ftpIP the ftpIP to set
	 */
	public void setFtpIP(String ftpIP)
	{
		this.ftpIP = ftpIP;
	}

	/**
	 * @return the ftpPort
	 */
	public String getFtpPort()
	{
		return ftpPort;
	}

	/**
	 * @param ftpPort the ftpPort to set
	 */
	public void setFtpPort(String ftpPort)
	{
		this.ftpPort = ftpPort;
	}

	/**
	 * @return the ftpUserName
	 */
	public String getFtpUserName()
	{
		return ftpUserName;
	}

	/**
	 * @param ftpUserName the ftpUserName to set
	 */
	public void setFtpUserName(String ftpUserName)
	{
		this.ftpUserName = ftpUserName;
	}

	/**
	 * @return the ftpPassword
	 */
	public String getFtpPassword()
	{
		return ftpPassword;
	}

	/**
	 * @param ftpPassword the ftpPassword to set
	 */
	public void setFtpPassword(String ftpPassword)
	{
		this.ftpPassword = ftpPassword;
	}

	/**
	 * @return the ftpType
	 */
	public String getFtpType()
	{
		return ftpType;
	}

	/**
	 * @param ftpType the ftpType to set
	 */
	public void setFtpType(String ftpType)
	{
		this.ftpType = ftpType;
	}

	/**
	 * @return the ftpPath
	 */
	public String getFtpPath()
	{
		return ftpPath;
	}

	/**
	 * @param ftpPath the ftpPath to set
	 */
	public void setFtpPath(String ftpPath)
	{
		this.ftpPath = ftpPath;
	}

	/**
	 * @return the ftpFileName
	 */
	public String getFtpFileName()
	{
		return ftpFileName;
	}

	/**
	 * @param ftpFileName the ftpFileName to set
	 */
	public void setFtpFileName(String ftpFileName)
	{
		this.ftpFileName = ftpFileName;
	}

	/**
	 * @return the exclusion
	 */
	public String getExclusion()
	{
		return exclusion;
	}

	/**
	 * @param exclusion the exclusion to set
	 */
	public void setExclusion(String exclusion)
	{
		this.exclusion = exclusion;
	}

	/**
	 * @return the ftpDownloadThreadNum
	 */
	public String getFtpDownloadThreadNum()
	{
		return ftpDownloadThreadNum;
	}

	/**
	 * @param ftpDownloadThreadNum the ftpDownloadThreadNum to set
	 */
	public void setFtpDownloadThreadNum(String ftpDownloadThreadNum)
	{
		this.ftpDownloadThreadNum = ftpDownloadThreadNum;
	}

	/**
	 * @return the ftpPacketSize
	 */
	public String getFtpPacketSize()
	{
		return ftpPacketSize;
	}

	/**
	 * @param ftpPacketSize the ftpPacketSize to set
	 */
	public void setFtpPacketSize(String ftpPacketSize)
	{
		this.ftpPacketSize = ftpPacketSize;
	}

	/**
	 * @return the ftpLimit
	 */
	public String getFtpLimit()
	{
		return ftpLimit;
	}

	/**
	 * @param ftpLimit the ftpLimit to set
	 */
	public void setFtpLimit(String ftpLimit)
	{
		this.ftpLimit = ftpLimit;
	}

	/**
	 * @return the ftpLimitStart
	 */
	public String getFtpLimitStart()
	{
		return ftpLimitStart;
	}

	/**
	 * @param ftpLimitStart the ftpLimitStart to set
	 */
	public void setFtpLimitStart(String ftpLimitStart)
	{
		this.ftpLimitStart = ftpLimitStart;
	}

	/**
	 * @return the ftpLimitEnd
	 */
	public String getFtpLimitEnd()
	{
		return ftpLimitEnd;
	}

	/**
	 * @param ftpLimitEnd the ftpLimitEnd to set
	 */
	public void setFtpLimitEnd(String ftpLimitEnd)
	{
		this.ftpLimitEnd = ftpLimitEnd;
	}

	/**
	 * @return the ftpIsSubDir
	 */
	public String getFtpIsSubDir()
	{
		return ftpIsSubDir;
	}

	/**
	 * @param ftpIsSubDir the ftpIsSubDir to set
	 */
	public void setFtpIsSubDir(String ftpIsSubDir)
	{
		this.ftpIsSubDir = ftpIsSubDir;
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
	 * @return the fileattrJson
	 */
	public String getFileattrJson()
	{
		return fileattrJson;
	}

	/**
	 * @param fileattrJson the fileattrJson to set
	 */
	public void setFileattrJson(String fileattrJson)
	{
		this.fileattrJson = fileattrJson;
	}

	/**
	 * @return the fileattrList
	 */
	public List<FileContentMapping> getFileattrList()
	{
		return fileattrList;
	}

	/**
	 * @param fileattrList the fileattrList to set
	 */
	public void setFileattrList(List<FileContentMapping> fileattrList)
	{
		this.fileattrList = fileattrList;
	}
}
