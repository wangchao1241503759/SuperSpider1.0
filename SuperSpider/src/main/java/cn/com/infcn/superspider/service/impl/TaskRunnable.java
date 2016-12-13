/**
 * 
 */
package cn.com.infcn.superspider.service.impl;

import org.springframework.web.context.ContextLoader;

/**
 * @description 
 * @author WChao
 * @date   2016年4月11日 	下午3:20:46
 */
public class TaskRunnable implements Runnable{
	
	private String taskId = null;
	private String flag = null;
	//任务逻辑层接口;
	private TaskServiceImpl taskService = (TaskServiceImpl)ContextLoader.getCurrentWebApplicationContext().getBean("taskService");
	
	public TaskRunnable() {}
	
	public TaskRunnable(String taskId,String flag) {
		this.taskId = taskId;
		this.flag = flag;
	}
	
	@Override
	public void run() {
		try {
			if("start".equals(flag)){
				this.taskService.start(taskId);
			}else if("stop".equals(flag)){
				this.taskService.stop(taskId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
		}
	}
}
