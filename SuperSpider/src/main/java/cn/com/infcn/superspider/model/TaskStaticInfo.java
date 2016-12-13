package cn.com.infcn.superspider.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TaskStaticInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "task_static_info", catalog = "superspider")
public class TaskStaticInfo implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1096596768849246774L;
	private String id;
	private String taskId;
	private String taskName;
	private Date startTime;
	private Date endTime;
	private Long collectNum;
	private String taskStatus;
	private String taskRunTime;
	private Date taskNextRunTime;
	private Integer taskRunSpeed;
	private String source;		//日志来源
	private Date createTime;	//产生时间
	private int startCount;		//启动次数
	private String statisticTime;	//统计日期

	// Constructors

	/** default constructor */
	public TaskStaticInfo() {
	}

	/** minimal constructor */
	public TaskStaticInfo(String id) {
		this.id = id;
	}

	/** full constructor */
	public TaskStaticInfo(String id, String taskId, String taskName,
			Date startTime, Date endTime, Long collectNum, String taskStatus,
			String taskRunTime, Date taskNextRunTime, Integer taskRunSpeed) {
		this.id = id;
		this.taskId = taskId;
		this.taskName = taskName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.collectNum = collectNum;
		this.taskStatus = taskStatus;
		this.taskRunTime = taskRunTime;
		this.taskNextRunTime = taskNextRunTime;
		this.taskRunSpeed = taskRunSpeed;
	}

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "task_id", length = 32)
	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Column(name = "task_name", length = 50)
	public String getTaskName() {
		return this.taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "start_time", length = 19)
	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "end_time", length = 19)
	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column(name = "collect_num")
	public Long getCollectNum() {
		return this.collectNum;
	}

	public void setCollectNum(Long collectNum) {
		this.collectNum = collectNum;
	}

	@Column(name = "task_status", length = 10)
	public String getTaskStatus() {
		return this.taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	@Column(name = "task_run_time")
	public String getTaskRunTime() {
		return this.taskRunTime;
	}

	public void setTaskRunTime(String taskRunTime) {
		this.taskRunTime = taskRunTime;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "task_next_run_time", length = 19)
	public Date getTaskNextRunTime() {
		return this.taskNextRunTime;
	}

	public void setTaskNextRunTime(Date taskNextRunTime) {
		this.taskNextRunTime = taskNextRunTime;
	}

	@Column(name = "task_run_speed")
	public Integer getTaskRunSpeed() {
		return this.taskRunSpeed;
	}

	public void setTaskRunSpeed(Integer taskRunSpeed) {
		this.taskRunSpeed = taskRunSpeed;
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
	 * @return the statisticTime
	 */
	@Column(name="statistic_time",length=20)
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