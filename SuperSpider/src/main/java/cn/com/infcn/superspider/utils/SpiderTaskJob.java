/**
 * 
 */
package cn.com.infcn.superspider.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.web.context.ContextLoader;

import cn.com.infcn.superspider.model.SchedulePlan;
import cn.com.infcn.superspider.model.Task;
import cn.com.infcn.superspider.service.impl.TaskServiceImpl;

/**
 * @description 
 * @author WChao
 * @date   2016年3月4日 	下午3:15:43
 */
public class SpiderTaskJob implements Job {
	//任务逻辑层接口;
	private TaskServiceImpl taskService = (TaskServiceImpl)ContextLoader.getCurrentWebApplicationContext().getBean("taskService");
	
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	  SchedulePlan schedulePlan = (SchedulePlan)context.getMergedJobDataMap().get("scheduleJob");
    	  try {
    		  List<Task> tasks = taskService.findTaskByPlan(schedulePlan.getId());
    		  if(tasks != null && tasks.size() > 0){
    			  for(Task task : tasks){
    				  taskService.start(task.getTaskId());
    			  }
    		  }
	          SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");    
	  	      System.out.println("任务名称 = [" + schedulePlan.getName() + "]"+ " 在 " + dateFormat.format(new Date())+" 时运行");
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    }
}
