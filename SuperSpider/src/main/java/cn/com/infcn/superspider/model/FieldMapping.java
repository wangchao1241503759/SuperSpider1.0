package cn.com.infcn.superspider.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * 字段映射模型;
 * @description 
 * @author WChao
 * @date   2015年12月23日 	下午1:58:28
 */
@Entity
@Table(name = "db_field_mapping")
@DynamicUpdate(true) @DynamicInsert(true)
public class FieldMapping implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fieldId;//字段ID;
	private String fieldName;//字段名称;
	private String fieldType;//字段类型;
	private String fieldExp;//字段转换表达式;
	private String filedSource;//字段来源;
	private String targetFieldName;//目标字段名称;
	private String targetFieldType;//目标字段类型;
	private String targetType;//目标类型;如:mysql、oracle、sqlserver、db2、mongo等为了字段类型映射;
	private Integer fieldOrdinalPosition;//字段位置;
	private String fieldStatus;//0:未操作;1:新增;2:修改;3:删除;
	private String taskId;//任务ID;
	
	public FieldMapping(){};
	public FieldMapping(String id)
	{
		this.fieldId = id;
	}
	@Id
	@Column(name = "ID", length = 32, nullable = false)
	public String getFieldId() {
		return fieldId;
	}
	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}
	@Column(name = "FIELD_NAME",length = 50)
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	@Column(name = "FIELD_TYPE",length = 32)
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	@Column(name = "FIELD_EXP",length = 200)
	public String getFieldExp() {
		return fieldExp;
	}
	public void setFieldExp(String fieldExp) {
		this.fieldExp = fieldExp;
	}
	@Column(name = "FIELD_SOURCE",length = 32)
	public String getFiledSource() {
		return filedSource;
	}
	public void setFiledSource(String filedSource) {
		this.filedSource = filedSource;
	}
	@Column(name = "TARGET_FIELD_NAME",length = 50)
	public String getTargetFieldName() {
		return targetFieldName;
	}
	public void setTargetFieldName(String targetFieldName) {
		this.targetFieldName = targetFieldName;
	}
	@Column(name = "TARGET_FIELD_TYPE",length = 32)
	public String getTargetFieldType() {
		return targetFieldType;
	}
	public void setTargetFieldType(String targetFieldType) {
		this.targetFieldType = targetFieldType;
	}
	@Column(name = "TARGET_TYPE",length = 32)
	public String getTargetType() {
		return targetType;
	}
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	@Column(name = "FIELD_ORDINAL_POSITION",length = 3)
	public Integer getFieldOrdinalPosition() {
		return fieldOrdinalPosition;
	}
	public void setFieldOrdinalPosition(Integer fieldOrdinalPosition) {
		this.fieldOrdinalPosition = fieldOrdinalPosition;
	}
	@Column(name = "FIELD_STATUS",length = 1)
	public String getFieldStatus() {
		return fieldStatus;
	}
	public void setFieldStatus(String fieldStatus) {
		this.fieldStatus = fieldStatus;
	}
	@Column(name = "TASK_ID",length = 32)
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
}
