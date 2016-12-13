package cn.com.infcn.superspider.pagemodel;


import com.justme.superspider.spider.SuperSpider;

import cn.com.infcn.superspider.model.Task;

/**
 * @author WChao
 * @date 2016年2月28日
 */
public class TaskRunningStatus {
	private Task task;//任务引用
	private SuperSpider superSpider;//爬虫对象;
	private String status = "1";//正在运行;
	public TaskRunningStatus(){};
	public TaskRunningStatus(Task task,SuperSpider superspider){
		this.task = task;
		this.superSpider = superspider;
	}
	public synchronized void finishSum(int count){
		task.setFinishCount(task.getFinishCount()+count);
	}
	public synchronized void successSum(int count){
		task.setSuccessCount(task.getSuccessCount()+count);
	}
	public synchronized void failSum(int count){
		task.setFailCount(task.getFailCount()+count);
	}
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public SuperSpider getSuperSpider() {
		return superSpider;
	}
	public void setSuperSpider(SuperSpider superSpider) {
		this.superSpider = superSpider;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
