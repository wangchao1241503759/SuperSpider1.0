package cn.com.infcn.superspider.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @author WChao
 * @date 2016年1月23日
 */
@Entity
@Table(name = "file_content_mapping")
@DynamicUpdate(true) @DynamicInsert(true)
public class FileContentMapping {
	
	private String id;//主键id;
	private String fileAttributeId;//文件属性id;
	private String fileAttributeName;//文件名称;
	private String fileFieldType;//文件字段类型;
	private String extensionField;//扩展名字段名称;
	private String sourceFieldName;//内容字段名称;
	private String targetFieldName;//目标字段名称;
	private String targetFieldType;//目标字段类型;
	private String isDefault;//是否默认字段;
	private String taskId;//任务ID;
	
	@Id
	@Column(name = "ID", length = 32, nullable = false)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name = "FILE_ATTRIBUTE_ID",length = 32)
	public String getFileAttributeId() {
		return fileAttributeId;
	}
	public void setFileAttributeId(String fileAttributeId) {
		this.fileAttributeId = fileAttributeId;
	}
	@Column(name = "FILE_FIELD_TYPE",length = 10)
	public String getFileFieldType() {
		return fileFieldType;
	}
	public void setFileFieldType(String fileFieldType) {
		this.fileFieldType = fileFieldType;
	}
	@Column(name = "EXTENSION_FIELD",length =100)
	public String getExtensionField() {
		return extensionField;
	}
	public void setExtensionField(String extensionField) {
		this.extensionField = extensionField;
	}
	@Column(name = "SOURCE_FIELD_NAME",length =100)
	public String getSourceFieldName() {
		return sourceFieldName;
	}
	public void setSourceFieldName(String sourceFieldName) {
		this.sourceFieldName = sourceFieldName;
	}
	@Column(name = "TARGET_FIELD_NAME",length =100)
	public String getTargetFieldName() {
		return targetFieldName;
	}
	public void setTargetFieldName(String targetFieldName) {
		this.targetFieldName = targetFieldName;
	}
	@Column(name = "TARGET_FIELD_TYPE",length =50)
	public String getTargetFieldType() {
		return targetFieldType;
	}
	public void setTargetFieldType(String targetFieldType) {
		this.targetFieldType = targetFieldType;
	}
	@Column(name = "TASK_ID",length = 32)
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	public String getFileAttributeName() {
		return fileAttributeName;
	}
	public void setFileAttributeName(String fileAttributeName) {
		this.fileAttributeName = fileAttributeName;
	}
}
