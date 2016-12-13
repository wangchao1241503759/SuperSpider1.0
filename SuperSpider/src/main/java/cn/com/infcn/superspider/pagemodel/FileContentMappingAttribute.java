/**
 * 
 */
package cn.com.infcn.superspider.pagemodel;

/**
 * @description 
 * @author WChao
 * @date   2016年4月14日 	下午4:21:14
 */
public class FileContentMappingAttribute {
	
	
	private String id;//主键id;
	private String fileAttributeId;//文件属性id;
	private String fileFieldType;//文件字段类型;
	private String extensionField;//扩展名字段名称;
	private String sourceFieldName;//内容字段名称;
	private String targetFieldName;//目标字段名称;
	private String targetFieldType;//目标字段类型;
	private String isDefault;//是否默认字段;
	private String taskId;//任务ID;
	private String fileAttributeName;
	private String fileTypeId;
	private String fileTypeValue;
	private String fileTypeName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFileAttributeId() {
		return fileAttributeId;
	}
	public void setFileAttributeId(String fileAttributeId) {
		this.fileAttributeId = fileAttributeId;
	}
	public String getFileFieldType() {
		return fileFieldType;
	}
	public void setFileFieldType(String fileFieldType) {
		this.fileFieldType = fileFieldType;
	}
	public String getExtensionField() {
		return extensionField;
	}
	public void setExtensionField(String extensionField) {
		this.extensionField = extensionField;
	}
	public String getSourceFieldName() {
		return sourceFieldName;
	}
	public void setSourceFieldName(String sourceFieldName) {
		this.sourceFieldName = sourceFieldName;
	}
	public String getTargetFieldName() {
		return targetFieldName;
	}
	public void setTargetFieldName(String targetFieldName) {
		this.targetFieldName = targetFieldName;
	}
	public String getTargetFieldType() {
		return targetFieldType;
	}
	public void setTargetFieldType(String targetFieldType) {
		this.targetFieldType = targetFieldType;
	}
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getFileAttributeName() {
		return fileAttributeName;
	}
	public void setFileAttributeName(String fileAttributeName) {
		this.fileAttributeName = fileAttributeName;
	}
	public String getFileTypeId() {
		return fileTypeId;
	}
	public void setFileTypeId(String fileTypeId) {
		this.fileTypeId = fileTypeId;
	}
	public String getFileTypeValue() {
		return fileTypeValue;
	}
	public void setFileTypeValue(String fileTypeValue) {
		this.fileTypeValue = fileTypeValue;
	}
	public String getFileTypeName() {
		return fileTypeName;
	}
	public void setFileTypeName(String fileTypeName) {
		this.fileTypeName = fileTypeName;
	}
	
}
