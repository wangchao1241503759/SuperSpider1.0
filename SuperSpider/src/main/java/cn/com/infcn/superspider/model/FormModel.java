/**
 * 
 */
package cn.com.infcn.superspider.model;

import java.util.List;

/**
 * @description 
 * @author WChao
 * @date   2015年12月28日 	下午3:01:37
 */
public class FormModel {
	//业务相关属性;
	private List<String> taskIds;//业务相关任务ids;
	private List<Task> tasks;//任务列表;

	public List<String> getTaskIds() {
		return taskIds;
	}

	public void setTaskIds(List<String> taskIds) {
		this.taskIds = taskIds;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	
}
