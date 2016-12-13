/**
 * 
 */
package cn.com.infcn.superspider.pagemodel;

import java.util.Date;
import java.util.List;

import cn.com.infcn.superspider.model.FieldMapping;
import cn.com.infcn.superspider.model.FileContentMapping;

/**
 * @description 
 * @author WChao
 * @date   2015年12月21日 	下午5:25:57
 */
public class DbConfig{

	/**
	 * 任务基本信息;
	 */
	private String taskId;//任务id;
	private String taskType;//任务类型;
	private String taskName;//任务名称;
	private String taskDescription;//任务说明;
	private String taskPlanId;//计划Id;
	private String taskCreator;//创建人;
	private Date   taskCreateDate;//创建时间;
	private String taskState;//任务状态;0:闲置、1:执行中、2：停止;
	private String priorityLevel="0";//任务优先级;0:最低、1:低、2:中、3:高、4:最高
	
	/**
	 * 源数据配置信息
	 */
	private String dsId;//主键;
	private String dsType;//数据库类型;
	private String dsHost;//主机ip;
	private Integer dsPort;//端口号;
	private String dsUserName;//用户名;
	private String dsPassWord;//密码;
	private String dsDbName;//数据库名称;
	private String dsTableSpace;//表空间名称;
	private String dsSchema;//别名(Schema);
	private Integer dsFetchSize;//一次获取数据数目;
	private String dsTableType;//选择数据方式;0：数据表\1:sql语句;
	private String dsTableName;//表名称;
	private Float dsThread;//爬取线程数;
	private String dsSql;//sql语句;
	
	/**
	 * 输出配置信息;
	 */
	private String topId;//主键;
	private String topType;//输出类型;
	private String topHost;//输出服务器地址;
	private Integer topPort;//端口号;
	private String topUserName;//用户名;
	private String topPassWord;//密码;
	private String topDbName;//数据库名;
	private String topTableName;//表名称;
	private String topSchema;//输出别名(Shcema);
	private String topIsClear;//是否清空数据库表;
	private Integer topThread;//输出线程数;
	private String topMssType;//输出元数据仓储所在库类型;
	/**
	 * 增量触发器
	 */
	private String triggerId;//触发器id;
	private String triggerName;//触发器名称;
	private String triggerTableName;//触发器临时表名称;
	private String primaryField;//主键字段;
	private String statusField;//状态字段;
	private String triggerScript;//触发器脚本;
	private String isOpen;//是否启用触发器增量爬取;
	private String createStatus;//触发器创建状态;
	
	private String sourceFieldName;//文件字段内容字段名称;
	/**
	 * 字段映射规则配置信息;
	 */
	private List<FieldMapping> fieldList;
	private List<FileContentMapping> attributeList;//文件提取字段列表;
	private	String fieldMappingData;//字段映射前台数据字符串;
	private String attributeMappingData;//文件属性映射数据串;
	
	public String getDsId() {
		return dsId;
	}
	public void setDsId(String dsId) {
		this.dsId = dsId;
	}
	public String getDsType() {
		return dsType;
	}
	public void setDsType(String dsType) {
		this.dsType = dsType;
	}
	public String getDsHost() {
		return dsHost;
	}
	public void setDsHost(String dsHost) {
		this.dsHost = dsHost;
	}
	public Integer getDsPort() {
		return dsPort;
	}
	public void setDsPort(Integer dsPort) {
		this.dsPort = dsPort;
	}
	public String getDsUserName() {
		return dsUserName;
	}
	public void setDsUserName(String dsUserName) {
		this.dsUserName = dsUserName;
	}
	public String getDsPassWord() {
		return dsPassWord;
	}
	public void setDsPassWord(String dsPassWord) {
		this.dsPassWord = dsPassWord;
	}
	public String getDsDbName() {
		return dsDbName;
	}
	public void setDsDbName(String dsDbName) {
		this.dsDbName = dsDbName;
	}
	public String getDsTableSpace() {
		return dsTableSpace;
	}
	public void setDsTableSpace(String dsTableSpace) {
		this.dsTableSpace = dsTableSpace;
	}
	public Integer getDsFetchSize() {
		return dsFetchSize;
	}
	public void setDsFetchSize(Integer dsFetchSize) {
		this.dsFetchSize = dsFetchSize;
	}
	public String getDsTableType() {
		return dsTableType;
	}
	public void setDsTableType(String dsTableType) {
		this.dsTableType = dsTableType;
	}
	public String getDsTableName() {
		return dsTableName;
	}
	public void setDsTableName(String dsTableName) {
		this.dsTableName = dsTableName;
	}
	public String getDsSql() {
		return dsSql;
	}
	public void setDsSql(String dsSql) {
		this.dsSql = dsSql;
	}
	public String getTopId() {
		return topId;
	}
	public void setTopId(String topId) {
		this.topId = topId;
	}
	public String getTopType() {
		return topType;
	}
	public void setTopType(String topType) {
		this.topType = topType;
	}
	public String getTopHost() {
		return topHost;
	}
	public void setTopHost(String topHost) {
		this.topHost = topHost;
	}
	public Integer getTopPort() {
		return topPort;
	}
	public void setTopPort(Integer topPort) {
		this.topPort = topPort;
	}
	public String getTopUserName() {
		return topUserName;
	}
	public void setTopUserName(String topUserName) {
		this.topUserName = topUserName;
	}
	public String getTopPassWord() {
		return topPassWord;
	}
	public void setTopPassWord(String topPassWord) {
		this.topPassWord = topPassWord;
	}
	public String getTopDbName() {
		return topDbName;
	}
	public void setTopDbName(String topDbName) {
		this.topDbName = topDbName;
	}
	public String getTopTableName() {
		return topTableName;
	}
	public void setTopTableName(String topTableName) {
		this.topTableName = topTableName;
	}
	public String getTopIsClear() {
		return topIsClear;
	}
	public void setTopIsClear(String topIsClear) {
		this.topIsClear = topIsClear;
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
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
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
	public String getTaskState() {
		return taskState;
	}
	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}
	public List<FieldMapping> getFieldList() {
		return fieldList;
	}
	public void setFieldList(List<FieldMapping> fieldList) {
		this.fieldList = fieldList;
	}
	public Float getDsThread() {
		return dsThread;
	}
	public void setDsThread(Float dsThread) {
		this.dsThread = dsThread;
	}
	public Integer getTopThread() {
		return topThread;
	}
	public void setTopThread(Integer topThread) {
		this.topThread = topThread;
	}
	public String getTopMssType() {
		return topMssType;
	}
	public void setTopMssType(String topMssType) {
		this.topMssType = topMssType;
	}
	public List<FileContentMapping> getAttributeList() {
		return attributeList;
	}
	public void setAttributeList(List<FileContentMapping> attributeList) {
		this.attributeList = attributeList;
	}
	public String getTriggerId() {
		return triggerId;
	}
	public void setTriggerId(String triggerId) {
		this.triggerId = triggerId;
	}
	public String getTriggerName() {
		return triggerName;
	}
	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}
	public String getTriggerTableName() {
		return triggerTableName;
	}
	public void setTriggerTableName(String triggerTableName) {
		this.triggerTableName = triggerTableName;
	}
	public String getPrimaryField() {
		return primaryField;
	}
	public void setPrimaryField(String primaryField) {
		this.primaryField = primaryField;
	}
	public String getStatusField() {
		return statusField;
	}
	public void setStatusField(String statusField) {
		this.statusField = statusField;
	}
	public String getTriggerScript() {
		return triggerScript;
	}
	public void setTriggerScript(String triggerScript) {
		this.triggerScript = triggerScript;
	}
	public String getIsOpen() {
		return isOpen;
	}
	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}
	public String getFieldMappingData() {
		return fieldMappingData;
	}
	public void setFieldMappingData(String fieldMappingData) {
		this.fieldMappingData = fieldMappingData;
	}
	public String getAttributeMappingData() {
		return attributeMappingData;
	}
	public void setAttributeMappingData(String attributeMappingData) {
		this.attributeMappingData = attributeMappingData;
	}
	public String getDsSchema() {
		return dsSchema;
	}
	public void setDsSchema(String dsSchema) {
		this.dsSchema = dsSchema;
	}
	public String getTopSchema() {
		return topSchema;
	}
	public void setTopSchema(String topSchema) {
		this.topSchema = topSchema;
	}
	public String getCreateStatus() {
		return createStatus;
	}
	public void setCreateStatus(String createStatus) {
		this.createStatus = createStatus;
	}
	public String getSourceFieldName() {
		return sourceFieldName;
	}
	public void setSourceFieldName(String sourceFieldName) {
		this.sourceFieldName = sourceFieldName;
	}
	public String getPriorityLevel() {
		return priorityLevel;
	}
	public void setPriorityLevel(String priorityLevel) {
		this.priorityLevel = priorityLevel;
	}
}
