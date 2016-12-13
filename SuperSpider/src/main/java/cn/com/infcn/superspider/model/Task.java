package cn.com.infcn.superspider.model;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * 任务基本信息模型;
 * @author WChao
 * @date 2015年12月16日
 */
@Entity
@Table(name = "task")
@DynamicUpdate(true) @DynamicInsert(true)
public class Task implements Serializable{
	
	private static final long serialVersionUID = -4528723911681535011L;
	private String taskId;//任务id;
	private String taskType;//任务类型;
	private String taskName;//任务名称;
	private String taskDescription;//任务说明;
	private String taskPlanId;//计划Id;
	private String planName;//计划名称;
	private String planDescription;//计划描述;
	private String taskCreator;//创建人;
	private Date   taskCreateDate;//创建时间;
	private Date   startDate;//开始时间;
	private Date   endDate;//结束时间;
	private String taskState;//任务状态;0：闲置，1正常执行，2：暂停，3，异常任务，4，异常终止
	private String completed;//同步完成;0:未完成;1:完成;
	private Integer finishCount=0;//完成数量;
	private Integer successCount=0;//成功数量;
	private Integer failCount=0;//失败数量;
	private String taskJson;//任务配置文件json信息;
	private String priorityLevel="0";//任务优先级;0:最低、1:低、2:中、3:高、4:最高
	private Integer startCount=0;//任务启动次数;
	
	public Task(){};
	public Task(String id)
	{
		this.taskId = id;
	}
	@Id
	@Column(name = "ID", length = 32, nullable = false)
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	@Column(name = "TASK_TYPE",length = 32)
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	@Column(name = "DESCRIPTION")
	public String getTaskDescription() {
		return taskDescription;
	}
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}
	@Column(name = "PLAN_ID",length = 32)
	public String getTaskPlanId() {
		return taskPlanId;
	}
	public void setTaskPlanId(String taskPlanId) {
		this.taskPlanId = taskPlanId;
	}
	@Column(name = "CREATOR",length = 20)
	public String getTaskCreator() {
		return taskCreator;
	}
	public void setTaskCreator(String taskCreator) {
		this.taskCreator = taskCreator;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE")
	public Date getTaskCreateDate() {
		return taskCreateDate;
	}
	public void setTaskCreateDate(Date taskCreateDate) {
		this.taskCreateDate = taskCreateDate;
	}
 	@Column(name = "TASK_NAME")
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	@Column(name = "STATE")
	public String getTaskState() {
		return taskState;
	}
	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}
	@Lob
	@Column(name = "JSON",columnDefinition = "LongText")
	public String getTaskJson() {
		return taskJson;
	}
	public void setTaskJson(String taskJson) {
		this.taskJson = taskJson;
	}
	@Column(name = "FINISH_COUNT",columnDefinition = "INT")
	public Integer getFinishCount() {
		return finishCount;
	}
	public void setFinishCount(Integer finishCount) {
		this.finishCount = finishCount;
	}
	@Column(name = "SUCCESS_COUNT",columnDefinition = "INT")
	public Integer getSuccessCount() {
		return successCount;
	}
	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}
	@Column(name = "FAIL_COUNT",columnDefinition = "INT")
	public Integer getFailCount() {
		return failCount;
	}
	public void setFailCount(Integer failCount) {
		this.failCount = failCount;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public String getPlanDescription() {
		return planDescription;
	}
	public void setPlanDescription(String planDescription) {
		this.planDescription = planDescription;
	}
	@Column(name = "COMPLETED",length = 5)
	public String getCompleted() {
		return completed;
	}
	public void setCompleted(String completed) {
		this.completed = completed;
	}

	/**
	 * @return the priorityLevel
	 */
	@Column(name = "priority_level", nullable = false, length = 1, columnDefinition = "char")
	public String getPriorityLevel()
	{
		return priorityLevel;
	}

	/**
	 * @param priorityLevel
	 *            the priorityLevel to set
	 */
	public void setPriorityLevel(String priorityLevel)
	{
		this.priorityLevel = priorityLevel;
	}
	@Column(name = "START_COUNT",columnDefinition = "INT")
	public Integer getStartCount() {
		return startCount;
	}
	
	public void setStartCount(Integer startCount) {
		this.startCount = startCount;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE")
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_DATE")
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}
