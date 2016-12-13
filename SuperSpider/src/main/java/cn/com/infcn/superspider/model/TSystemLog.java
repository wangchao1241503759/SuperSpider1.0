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
@Table(name="system_log")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TSystemLog implements Serializable
{

    private static final long serialVersionUID = -5692626874772012142L;
    
	private String id;			//主键
	private String level;		//日志级别
	private Date createTime;	//产生时间
	private String source;		//日志来源
	private String event;		//事件
	private String content;		//详细内容
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
	
}
