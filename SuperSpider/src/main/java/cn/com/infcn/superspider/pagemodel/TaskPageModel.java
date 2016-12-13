package cn.com.infcn.superspider.pagemodel;


import java.io.Serializable;
import java.util.Date;

import cn.com.infcn.superspider.model.SchedulePlan;

/**
 * 任务基本信息模型;
 * @author WChao
 * @date 2015年12月16日
 */
public class TaskPageModel implements Serializable{
	
	private static final long serialVersionUID = -4528723911681535011L;
	private String taskId;//任务id;
	private String taskType;//任务类型;
	private String taskName;//任务名称;
	private String taskDescription;//任务说明;
	private String taskPlanId;//计划Id;
	private String taskCreator;//创建人;
	private Date   taskCreateDate;//创建时间;
	private String taskState;//任务状态;0:闲置、1:执行中、2：停止;-1：禁用;
	private Integer finishCount=0;//完成数量;
	private Integer successCount=0;//成功数量;
	private Integer failCount=0;//失败数量;
	private String taskJson;//任务配置文件json信息;
	private SchedulePlan schedulePlan;//调度计划;
	public TaskPageModel(){};
	public TaskPageModel(String id)
	{
		this.taskId = id;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getTaskDescription() {
		return taskDescription;
	}
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}
	public String getTaskPlanId() {
		return taskPlanId;
	}
	public void setTaskPlanId(String taskPlanId) {
		this.taskPlanId = taskPlanId;
	}
	public String getTaskCreator() {
		return taskCreator;
	}
	public void setTaskCreator(String taskCreator) {
		this.taskCreator = taskCreator;
	}
	public Date getTaskCreateDate() {
		return taskCreateDate;
	}
	public void setTaskCreateDate(Date taskCreateDate) {
		this.taskCreateDate = taskCreateDate;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getTaskState() {
		return taskState;
	}
	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}
	public String getTaskJson() {
		return taskJson;
	}
	public void setTaskJson(String taskJson) {
		this.taskJson = taskJson;
	}
	public Integer getFinishCount() {
		return finishCount;
	}
	public void setFinishCount(Integer finishCount) {
		this.finishCount = finishCount;
	}
	public Integer getSuccessCount() {
		return successCount;
	}
	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}
	public Integer getFailCount() {
		return failCount;
	}
	public void setFailCount(Integer failCount) {
		this.failCount = failCount;
	}
	public SchedulePlan getSchedulePlan() {
		return schedulePlan;
	}
	public void setSchedulePlan(SchedulePlan schedulePlan) {
		this.schedulePlan = schedulePlan;
	}
}
