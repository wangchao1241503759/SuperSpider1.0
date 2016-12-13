/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年9月20日
 */
package cn.com.infcn.superspider.pagemodel;

import java.io.Serializable;

/**
 * @author lihf
 * @date 2016年9月20日
 */
public class TaskStatisticsConfig implements Serializable
{

	/**
	 * 
	 */
    private static final long serialVersionUID = 7307981750736937543L;
	private String dimension;  //维度（年、季、月、日）
	private String startYear;	//开始年
	private String endYear;		//结束年
	private String startYearMonthrange; //按月的开始年
	private String startMonth;			//开始月
	private String endYearMonthrange;	//按月的结束年
	private String endMonth;	//结束月
	private String startDate;	//按天，开始日期
	private String endDate;		//按天，结束日期
	private String taskRange;	//任务范围，单任务还是多任务
	private String taskId;		//单任务时，任务ID
	private String tasktype;	//多任务时，任务类型有哪些
	private String guideline;	//统计指标
	/**
	 * @return the dimension
	 */
	public String getDimension()
	{
		return dimension;
	}
	/**
	 * @param dimension the dimension to set
	 */
	public void setDimension(String dimension)
	{
		this.dimension = dimension;
	}
	/**
	 * @return the startYear
	 */
	public String getStartYear()
	{
		return startYear;
	}
	/**
	 * @param startYear the startYear to set
	 */
	public void setStartYear(String startYear)
	{
		this.startYear = startYear;
	}
	/**
	 * @return the endYear
	 */
	public String getEndYear()
	{
		return endYear;
	}
	/**
	 * @param endYear the endYear to set
	 */
	public void setEndYear(String endYear)
	{
		this.endYear = endYear;
	}
	/**
	 * @return the startYearMonthrange
	 */
	public String getStartYearMonthrange()
	{
		return startYearMonthrange;
	}
	/**
	 * @param startYearMonthrange the startYearMonthrange to set
	 */
	public void setStartYearMonthrange(String startYearMonthrange)
	{
		this.startYearMonthrange = startYearMonthrange;
	}
	/**
	 * @return the startMonth
	 */
	public String getStartMonth()
	{
		return startMonth;
	}
	/**
	 * @param startMonth the startMonth to set
	 */
	public void setStartMonth(String startMonth)
	{
		this.startMonth = startMonth;
	}
	/**
	 * @return the endYearMonthrange
	 */
	public String getEndYearMonthrange()
	{
		return endYearMonthrange;
	}
	/**
	 * @param endYearMonthrange the endYearMonthrange to set
	 */
	public void setEndYearMonthrange(String endYearMonthrange)
	{
		this.endYearMonthrange = endYearMonthrange;
	}
	/**
	 * @return the endMonth
	 */
	public String getEndMonth()
	{
		return endMonth;
	}
	/**
	 * @param endMonth the endMonth to set
	 */
	public void setEndMonth(String endMonth)
	{
		this.endMonth = endMonth;
	}
	/**
	 * @return the startDate
	 */
	public String getStartDate()
	{
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public String getEndDate()
	{
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}
	/**
	 * @return the taskRange
	 */
	public String getTaskRange()
	{
		return taskRange;
	}
	/**
	 * @param taskRange the taskRange to set
	 */
	public void setTaskRange(String taskRange)
	{
		this.taskRange = taskRange;
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
	 * @return the tasktype
	 */
	public String getTasktype()
	{
		return tasktype;
	}
	/**
	 * @param tasktype the tasktype to set
	 */
	public void setTasktype(String tasktype)
	{
		this.tasktype = tasktype;
	}
	/**
	 * @return the guideline
	 */
	public String getGuideline()
	{
		return guideline;
	}
	/**
	 * @param guideline the guideline to set
	 */
	public void setGuideline(String guideline)
	{
		this.guideline = guideline;
	}
}
