package cn.com.infcn.superspider.pagemodel;

import java.io.Serializable;
import java.util.Date;

public class TaskStaticInfoVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String taskId;
	private String taskName;
	private Date startTime;
	private Date endTime;
	private Long collectNum;
	private String taskStatus;
	private String taskRunTime;
	private Date taskNextRunTime;
	private String taskRunSpeed;
	private String startTimeStr;
	private String taskNextRunTimeStr;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Long getCollectNum() {
		return collectNum;
	}
	public void setCollectNum(Long collectNum) {
		this.collectNum = collectNum;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public String getTaskRunTime() {
		return taskRunTime;
	}
	public void setTaskRunTime(String taskRunTime) {
		this.taskRunTime = taskRunTime;
	}
	public Date getTaskNextRunTime() {
		return taskNextRunTime;
	}
	public void setTaskNextRunTime(Date taskNextRunTime) {
		this.taskNextRunTime = taskNextRunTime;
	}
	public String getTaskRunSpeed() {
		return taskRunSpeed;
	}
	public void setTaskRunSpeed(String taskRunSpeed) {
		this.taskRunSpeed = taskRunSpeed;
	}
	public String getStartTimeStr() {
		return startTimeStr;
	}
	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}
	public String getTaskNextRunTimeStr() {
		return taskNextRunTimeStr;
	}
	public void setTaskNextRunTimeStr(String taskNextRunTimeStr) {
		this.taskNextRunTimeStr = taskNextRunTimeStr;
	}
	
	

}
