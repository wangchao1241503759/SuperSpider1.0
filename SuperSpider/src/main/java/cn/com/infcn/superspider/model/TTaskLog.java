/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年7月26日
 */
package cn.com.infcn.superspider.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @author lihf
 * @date 2016年7月26日
 */
@Entity
@Table(name="task_log")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TTaskLog implements Serializable
{

    private static final long serialVersionUID = 2529759906539381421L;
    
	private String id;			//主键
	private String taskId;		//任务ID
	private String level;		//日志级别
	private Date createTime;	//产生时间
	private String source;		//日志来源
	private String event;		//事件
	private String content;		//详细内容
	private String taskName;	//任务名称
	private int collectNum; 	// 采集条数
	private int startCount;		//启动次数
	private String isStatistics; //是否已统计Y:已经统计,N为未统计
	private String status;		//状态
	
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
	@Column(name="task_id",length=32)
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
	@Column(name="level",nullable=false,length=60)
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
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_time",nullable=false,length=20)
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
	@Column(name="source",length=100)
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
	@Column(name="event",length=100)
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
	@Column(name="content",length=2000)
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
	@Column(name="task_name",nullable=false)
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
	
	@Column(name="collect_num",length=10)
	public int getCollectNum() {
		return collectNum;
	}
	public void setCollectNum(int collectNum) {
		this.collectNum = collectNum;
	}
	/**
	 * @return the startCount
	 */
	@Column(name = "start_count",columnDefinition = "INT")
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
	@Column(name="is_statistics",length=1)
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
	@Column(name="status",length=1,columnDefinition="char")
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
