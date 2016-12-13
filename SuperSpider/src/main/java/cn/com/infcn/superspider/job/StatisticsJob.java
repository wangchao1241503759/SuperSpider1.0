package cn.com.infcn.superspider.job;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.com.infcn.superspider.common.Constant;
import cn.com.infcn.superspider.model.TTaskLog;
import cn.com.infcn.superspider.model.Task;
import cn.com.infcn.superspider.pagemodel.Log;
import cn.com.infcn.superspider.service.TaskLogServiceI;
import cn.com.infcn.superspider.service.TaskServiceI;

@Component
public class StatisticsJob {

	@Autowired
	private TaskServiceI taskService;
	@Autowired
	private TaskLogServiceI taskLogService;

	@Scheduled(cron = "0 */1 * * * ?")
	public void statisticsGatherInfo() {
		
		try {	
			
			//获取运行行的任务
			List<Task> taskList = taskService.findTaskByStatus("1");
			if(taskList != null && taskList.size() > 0){
				for (Task task : taskList) {
					
					Log log = new Log();
					log.setTaskId(task.getTaskId());
					log.setTaskName(task.getTaskName());
					// 加个次数
					TTaskLog taskLog = taskLogService.getNewRuntimeInfoByTaskId(task.getTaskId());
					if(taskLog == null){
						log.setCollectNum(task.getFinishCount());
					} else {
						log.setCollectNum(task.getFinishCount() - taskLog.getCollectNum());
					}
					
					log.setContent(task.getTaskName() + "运行时每分钟的采集的条数");
					log.setEvent(Constant.TASK_LOG_TYPE_RUNTIME);
					log.setCreateTime(new Date());
					log.setLevel(Constant.TASK_LOG_LEVEL_INFO);
					log.setSource(task.getTaskType());
					this.taskLogService.add(log);
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
