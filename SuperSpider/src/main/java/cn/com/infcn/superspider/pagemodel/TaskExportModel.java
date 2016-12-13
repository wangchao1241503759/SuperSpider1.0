/**
 * 
 */
package cn.com.infcn.superspider.pagemodel;

import java.util.List;

import cn.com.infcn.superspider.model.DbSource;
import cn.com.infcn.superspider.model.FieldMapping;
import cn.com.infcn.superspider.model.FileContentMapping;
import cn.com.infcn.superspider.model.SchedulePlan;
import cn.com.infcn.superspider.model.Task;
import cn.com.infcn.superspider.model.TaskOutput;

/**
 * @description 
 * @author WChao
 * @date   2016年3月8日 	下午5:51:15
 */
public class TaskExportModel {
	
	private List<Task> tasks;//任务列表;
	private List<DbSource> dbSources;//数据源列表;
	private List<TaskOutput> taskOutputs;//任务输出列表;
	private List<FieldMapping> fieldMappings;//字段映射列表;
	private List<FileContentMapping> fileContentMappings;//文件内容映射列表;
	private List<SchedulePlan> schedulePlans;//调度计划列表;
	
	public List<Task> getTasks() {
		return tasks;
	}
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	public List<DbSource> getDbSources() {
		return dbSources;
	}
	public void setDbSources(List<DbSource> dbSources) {
		this.dbSources = dbSources;
	}
	public List<TaskOutput> getTaskOutputs() {
		return taskOutputs;
	}
	public void setTaskOutputs(List<TaskOutput> taskOutputs) {
		this.taskOutputs = taskOutputs;
	}
	public List<FieldMapping> getFieldMappings() {
		return fieldMappings;
	}
	public void setFieldMappings(List<FieldMapping> fieldMappings) {
		this.fieldMappings = fieldMappings;
	}
	public List<FileContentMapping> getFileContentMappings() {
		return fileContentMappings;
	}
	public void setFileContentMappings(List<FileContentMapping> fileContentMappings) {
		this.fileContentMappings = fileContentMappings;
	}
	public List<SchedulePlan> getSchedulePlans() {
		return schedulePlans;
	}
	public void setSchedulePlans(List<SchedulePlan> schedulePlans) {
		this.schedulePlans = schedulePlans;
	}
}
