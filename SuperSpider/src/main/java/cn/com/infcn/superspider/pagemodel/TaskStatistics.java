/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年9月19日
 */
package cn.com.infcn.superspider.pagemodel;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lihf
 * @date 2016年9月19日
 */
public class TaskStatistics implements Serializable
{
	/**
	 * 
	 */
    private static final long serialVersionUID = 4836498107978159416L;
	private String id;
	private String taskId;
	private String taskName;
	private int collectNum;
	private String source;		//日志来源
	private Date createTime;	//产生时间
	private int startCount;		//启动次数
	private String statisticTime;	//统计日期
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
	 * @return the collectNum
	 */
	public int getCollectNum()
	{
		return collectNum;
	}
	/**
	 * @param collectNum the collectNum to set
	 */
	public void setCollectNum(int collectNum)
	{
		this.collectNum = collectNum;
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
	 * @return the statisticTime
	 */
	public String getStatisticTime()
	{
		return statisticTime;
	}
	/**
	 * @param statisticTime the statisticTime to set
	 */
	public void setStatisticTime(String statisticTime)
	{
		this.statisticTime = statisticTime;
	}
}
