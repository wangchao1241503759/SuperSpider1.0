/**
 * 
 */
package cn.com.infcn.superspider.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @description 
 * @author WChao
 * @date   2016年2月18日 	下午6:30:01
 */
@Entity
@Table(name = "spider_trigger")
@DynamicUpdate(true) @DynamicInsert(true)
public class Trigger {
	
	private String triggerId;//触发器id;
	private String triggerName;//触发器名称;
	private String triggerTableName;//触发器临时表名称;
	private String primaryField;//主键字段;
	private String statusField;//状态字段;
	private String triggerScript;//触发器脚本;
	private String isOpen;//是否启用触发器增量爬取;
	private String taskId;//任务ID;
	private String createStatus = "0";//创建状态;
	
	@Id
	@Column(name = "ID", length = 32, nullable = false)
	public String getTriggerId() {
		return triggerId;
	}
	public void setTriggerId(String triggerId) {
		this.triggerId = triggerId;
	}
	@Column(name = "TRIGGER_NAME",length = 50)
	public String getTriggerName() {
		return triggerName;
	}
	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}
	@Column(name = "TRIGGER_TABLE_NAME",length = 50)
	public String getTriggerTableName() {
		return triggerTableName;
	}
	public void setTriggerTableName(String triggerTableName) {
		this.triggerTableName = triggerTableName;
	}
	@Column(name = "PRIMARY_FIELD",length = 50)
	public String getPrimaryField() {
		return primaryField;
	}
	public void setPrimaryField(String primaryField) {
		this.primaryField = primaryField;
	}
	@Lob
	@Column(name = "TRIGGER_SCRIPT",columnDefinition = "LongText")
	public String getTriggerScript() {
		return triggerScript;
	}
	public void setTriggerScript(String triggerScript) {
		this.triggerScript = triggerScript;
	}
	@Column(name = "IS_OPEN",length = 2)
	public String getIsOpen() {
		return isOpen;
	}
	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}
	@Column(name = "TASK_ID",length = 32)
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	@Column(name = "STATUS_FIELD",length = 50)
	public String getStatusField() {
		return statusField;
	}
	public void setStatusField(String statusField) {
		this.statusField = statusField;
	}
	@Column(name = "CREATE_STATUS",length = 2)
	public String getCreateStatus() {
		return createStatus;
	}
	public void setCreateStatus(String createStatus) {
		this.createStatus = createStatus;
	}
}
