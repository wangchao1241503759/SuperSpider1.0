/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年7月13日
 */
package cn.com.infcn.superspider.pagemodel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cn.com.infcn.superspider.model.FileContentMapping;

/**
 * @author lihf
 * @date 2016年7月13日
 */
public class FileConfig implements Serializable
{
    private static final long serialVersionUID = -6216106832611177878L;
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
	private String fileId;	//主键ID
	private String filePath;		//file路径
	private String fileName; //文件(目录)名匹配
	private String exclusion;	//排除
	private String fileLimit;			 //文件大小范围 是否限制(Y:限制,N:不限制)
	private String fileLimitStart;		 //限制开始xxKB
	private String fileLimitEnd;		 	 //限制结束xxKB
	private String fileIsSubDir;			 //是否采集子目录(Y:是,N:否)
	private String fileExtractThreadNum;	 //提取线程数
    
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
	 * @return the fileId
	 */
	public String getFileId()
	{
		return fileId;
	}

	/**
	 * @param fileId the fileId to set
	 */
	public void setFileId(String fileId)
	{
		this.fileId = fileId;
	}

	/**
	 * @return the filePath
	 */
	public String getFilePath()
	{
		return filePath;
	}

	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
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
	 * @return the fileLimit
	 */
	public String getFileLimit()
	{
		return fileLimit;
	}

	/**
	 * @param fileLimit the fileLimit to set
	 */
	public void setFileLimit(String fileLimit)
	{
		this.fileLimit = fileLimit;
	}

	/**
	 * @return the fileLimitStart
	 */
	public String getFileLimitStart()
	{
		return fileLimitStart;
	}

	/**
	 * @param fileLimitStart the fileLimitStart to set
	 */
	public void setFileLimitStart(String fileLimitStart)
	{
		this.fileLimitStart = fileLimitStart;
	}

	/**
	 * @return the fileLimitEnd
	 */
	public String getFileLimitEnd()
	{
		return fileLimitEnd;
	}

	/**
	 * @param fileLimitEnd the fileLimitEnd to set
	 */
	public void setFileLimitEnd(String fileLimitEnd)
	{
		this.fileLimitEnd = fileLimitEnd;
	}

	/**
	 * @return the fileIsSubDir
	 */
	public String getFileIsSubDir()
	{
		return fileIsSubDir;
	}

	/**
	 * @param fileIsSubDir the fileIsSubDir to set
	 */
	public void setFileIsSubDir(String fileIsSubDir)
	{
		this.fileIsSubDir = fileIsSubDir;
	}

	/**
	 * @return the fileExtractThreadNum
	 */
	public String getFileExtractThreadNum()
	{
		return fileExtractThreadNum;
	}

	/**
	 * @param fileExtractThreadNum the fileExtractThreadNum to set
	 */
	public void setFileExtractThreadNum(String fileExtractThreadNum)
	{
		this.fileExtractThreadNum = fileExtractThreadNum;
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
