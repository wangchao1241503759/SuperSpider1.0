/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年7月26日
 */
package cn.com.infcn.superspider.pagemodel;

import java.io.Serializable;
import java.util.Date;

import cn.com.infcn.superspider.utils.UUIDCreater;

/**
 * @author lihf
 * @date 2016年7月26日
 */
public class Log implements Serializable
{

    private static final long serialVersionUID = -7002167080019126892L;
    
	private String id;			//主键
	private String taskId;		//任务ID
	private String level;		//日志级别
	private Date createTime;	//产生时间
	private String source;		//日志来源
	private String event;		//事件
	private String content;		//详细内容
	private String taskName;	//任务名称
	private String createTimeStart;	//开始日期
	private String createTimeEnd;	//结束日期
	private int collectNum; // 采集条数
	private int startCount;		//启动次数
	private String isStatistics; //是否已统计Y:已经统计,N为未统计
	private String status;		//状态
	
	public Log()
	{
		
	}
	public Log(String level,Date createTime,String source,String event,String content,String taskName,String createTimeStart,String createTimeEnd)
	{
		this.id = UUIDCreater.getUUID();
		this.level = level;
		this.createTime =createTime;
		this.source = source;
		this.event = event;
		this.content=content;
		this.taskName = taskName;
		this.createTimeStart = createTimeStart;
		this.createTimeEnd = createTimeEnd;
	}
	/**
	 * @return the id
	 */
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
	 * @return the level
	 */
	public String getLevel()
	{
		return level;
	}
	/**
	 * @param level the level to set
	 */
	public void setLevel(String level)
	{
		this.level = level;
	}
	/**
	 * @return the createTime
	 */
	public Date getCreateTime()
	{
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}
	/**
	 * @return the source
	 */
	public String getSource()
	{
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source)
	{
		this.source = source;
	}
	/**
	 * @return the event
	 */
	public String getEvent()
	{
		return event;
	}
	/**
	 * @param event the event to set
	 */
	public void setEvent(String event)
	{
		this.event = event;
	}
	/**
	 * @return the content
	 */
	public String getContent()
	{
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content)
	{
		this.content = content;
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
	 * @return the createTimeStart
	 */
	public String getCreateTimeStart()
	{
		return createTimeStart;
	}
	/**
	 * @param createTimeStart the createTimeStart to set
	 */
	public void setCreateTimeStart(String createTimeStart)
	{
		this.createTimeStart = createTimeStart;
	}
	/**
	 * @return the createTimeEnd
	 */
	public String getCreateTimeEnd()
	{
		return createTimeEnd;
	}
	/**
	 * @param createTimeEnd the createTimeEnd to set
	 */
	public void setCreateTimeEnd(String createTimeEnd)
	{
		this.createTimeEnd = createTimeEnd;
	}
	/**
	 * @return the collectNum
	 */
	public int getCollectNum() {
		return collectNum;
	}
	/**
	 * @param collectNum the collectNum to set
	 */
	public void setCollectNum(int collectNum) {
		this.collectNum = collectNum;
	}
	/**
	 * @return the startCount
	 */
	public int getStartCount()
	{
		return startCount;
	}
	/**
	 * @param startCount the startCount to set
	 */
	public void setStartCount(int startCount)
	{
		this.startCount = startCount;
	}
	/**
	 * @return the isStatistics
	 */
	public String getIsStatistics()
	{
		return isStatistics;
	}
	/**
	 * @param isStatistics the isStatistics to set
	 */
	public void setIsStatistics(String isStatistics)
	{
		this.isStatistics = isStatistics;
	}
	/**
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}
	
}
