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
 * 任务计划pojo
 * @author WChao
 * @date 2016年3月2日
 */
@Entity
@Table(name = "schedule_plan")
@DynamicUpdate(true) @DynamicInsert(true)
public class SchedulePlan implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;//计划id;
	private String name;	//任务名
	private String group ="spiderTaskJob";	//任务组
	private String creator;	//创建者;
	private Date createTime;//创建时间;
	private String createTimeStr;//格式化日期字符串;
	private String planType;//调度类型;
	private Integer cycleNum;//周期数量;
	private String weekCycle;//周个数;
	private String executeTime;//执行时间;
	private String cronExpression;	//cron表达式
	private String description;	//描述
	private String status;//调度计划状态;1:调度中;0:暂停;
	private String taskMatchs;//关联任务;
	private String className ="cn.com.infcn.superspider.utils.SpiderTaskJob";	//要执行的任务类路径名

	public SchedulePlan() {
		super();
	}

	public SchedulePlan(String name, String creator, Date createTime,String cronExpression,String description,String className) {
		super();
		this.name = name;
		this.creator = creator;
		this.createTime = createTime;
		this.cronExpression = cronExpression;
		this.description = description;
		this.className=className;
	}
	@Id
	@Column(name = "ID", length = 32, nullable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	@Column(name = "CRONEXPRESSION")
	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	@Column(name = "CLASS_NAME")
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	@Column(name = "CREATOR")
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Column(name = "PLAN_TYPE", length = 2)
	public String getPlanType() {
		return planType;
	}

	public void setPlanType(String planType) {
		this.planType = planType;
	}
	@Column(name = "CYCLE_NUM")
	public Integer getCycleNum() {
		return cycleNum;
	}

	public void setCycleNum(Integer cycleNum) {
		this.cycleNum = cycleNum;
	}
	@Column(name = "WEEK_CYCLE")
	public String getWeekCycle() {
		return weekCycle;
	}

	public void setWeekCycle(String weekCycle) {
		this.weekCycle = weekCycle;
	}
	@Column(name = "EXECUTE_TIME")
	public String getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(String executeTime) {
		this.executeTime = executeTime;
	}
	@Column(name = "JOB_GROUP")
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTaskMatchs() {
		return taskMatchs;
	}

	public void setTaskMatchs(String taskMatchs) {
		this.taskMatchs = taskMatchs;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
}
