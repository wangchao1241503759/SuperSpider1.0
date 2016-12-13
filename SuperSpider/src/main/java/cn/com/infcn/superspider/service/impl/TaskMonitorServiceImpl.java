/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年8月1日
 */
package cn.com.infcn.superspider.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.infcn.ade.common.dao.BaseDaoI;
import cn.com.infcn.ade.system.utils.TimeUtil;
import cn.com.infcn.superspider.model.SchedulePlan;
import cn.com.infcn.superspider.model.TTaskLog;
import cn.com.infcn.superspider.model.Task;
import cn.com.infcn.superspider.model.TaskStaticInfo;
import cn.com.infcn.superspider.pagemodel.TaskStaticInfoVO;
import cn.com.infcn.superspider.service.TaskLogServiceI;
import cn.com.infcn.superspider.service.TaskMonitorServiceI;
import cn.com.infcn.superspider.utils.StringUtil;

/**
 * @author lihf
 * @date 2016年8月1日
 */
@Service
public class TaskMonitorServiceImpl implements TaskMonitorServiceI {

	private SimpleDateFormat sf = new SimpleDateFormat("hh:mm:ss");
	private SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	private DecimalFormat df = null;	
	
	private static int num = 0;

	@Autowired
	private BaseDaoI<Task> taskDao;
	@Autowired
	private BaseDaoI<TTaskLog> taskLogDao;
	@Autowired
	private TaskLogServiceI taskLogServer;
	@Autowired
	private BaseDaoI<SchedulePlan> schedulePlanDao;

	/**
	 * 根据任务状态，获取任务列表
	 * 
	 * @author lihf
	 * @date 2016年8月2日 上午9:24:40
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Task> getTaskRunningList(String taskId, String loginName) throws Exception {
		
		StringBuffer sb = new StringBuffer("from Task where state in (1,3,4) and taskCreator = '"+loginName+"'");
		
		if (!StringUtil.isEmpty(taskId) && !"all".equals(taskId)) {
			List<Task> list = new ArrayList<>();
			Task task = this.taskDao.get(Task.class, taskId);
			list.add(task);
			return list;
		}
		
		return taskDao.find(sb.toString());
		
		
	}

	@Override
	public List<Task> getTaskAllList(String loginName) throws Exception {
		return this.taskDao.find("from Task t where taskCreator = '"+loginName+"'");
	}

	@Override
	public void saveTaskLogInfo(TTaskLog taskLog) throws Exception {
		this.taskLogDao.save(taskLog);
	}

	@Override
	public Map<String, Object> getTaskRunningTask(String taskId, String loginName) throws Exception {
		int count = 0;
		Long runtime = 0L;
		Map<String, Object> map = new HashMap<>();
		List<Task> list = this.getTaskRunningList(taskId,loginName);
		if (list != null && list.size() > 0) {
			for (Task task : list) {
				count += task.getFinishCount();
				runtime += TimeUtil.getRuntime(sd.format(task.getStartDate()));
			}
			map.put("num", count - num);
			map.put("taskNum", list.size());
			map.put("date", sf.format(new Date(System.currentTimeMillis())));
			long speed = runtime <= 0 ? 0:count/runtime;
			map.put("speed", speed);
		} else {
			map.put("num", 0);
			map.put("taskNum", 0);
			map.put("date", sf.format(new Date(System.currentTimeMillis())));
			map.put("speed", 0);
			num = 0;
		}
		num = count; // 记录时间差
		return map;

	}

	@Override
	public Map<String, Object> initMonitorInfo(String loginName) throws Exception {

		Map<String, Object> map = new HashMap<>();

		// 正在执行的任务数据
		List<Task> runingList = this.getTaskRunningList("all", loginName);
		if (runingList != null) {
			map.put("runingSize", runingList.size());
		}

		// 任务总数
		List<Task> list = this.getTaskAllList(loginName);
		if (list != null) {
			map.put("taskNum", list.size());
		}
		// 成功的任务
		Long num = taskLogServer.getUnusedTask(loginName);
		map.put("unuserdNum", num);

		// 查询已完成任务数据
		return map;
	}

	@Override
	public List<TaskStaticInfoVO> getTaskRunningInfo(TaskStaticInfo t, Map<String, Object> map, String loginName) throws Exception {
		
		List<TaskStaticInfoVO> resList = new ArrayList<>();
		
		StringBuffer sb = new StringBuffer("from Task where 1 = 1 ");
		
		if(!StringUtil.isEmpty(t.getTaskStatus()) && "normal".equals(t.getTaskStatus())){
			sb.append(" and taskState = 1");// 正常执行
		} else if(!StringUtil.isEmpty(t.getTaskStatus()) && "exception_ignore".equals(t.getTaskStatus())){
			sb.append(" and taskState = 3");// 处理异常后正常执行
		} else if(!StringUtil.isEmpty(t.getTaskStatus()) && "all".equals(t.getTaskStatus())){
			sb.append(" and taskState in (1,3)");// 处理异常后正常执行
		}
		if(!StringUtil.isEmpty(t.getTaskName()) && !"all".equals(t.getTaskName())){
			sb.append(" and  taskName like '%"+t.getTaskName()+"%'");
		}
		sb.append(" and taskCreator = '"+loginName+"'");
		
		List<Task> runList = this.taskDao.find(sb.toString());
		
		if (runList != null && runList.size() > 0) {
			
			for (Task task : runList) {
				
				TaskStaticInfoVO tsi = new TaskStaticInfoVO();
				tsi.setTaskId(task.getTaskId());
				Date stareTime  = task.getStartDate() == null ? new Date() : task.getStartDate();
				tsi.setStartTimeStr(sd.format(stareTime));// 开始时间
				tsi.setTaskName(task.getTaskName());// 任务名称
				
				Long runtime = TimeUtil.getRuntime(sd.format(stareTime));
				tsi.setTaskRunTime(TimeUtil.formatSecond(runtime.toString()));// 运行时长
				tsi.setCollectNum(Long.valueOf(task.getFinishCount()));// 采集记录数
				long count = Long.valueOf(task.getFinishCount()).longValue();
				
				Double n = Double.valueOf(String.valueOf(count)) / Double.valueOf(String.valueOf(runtime));
				if(n < 1){
					df = new DecimalFormat("0.000");
				} else {
					df = new DecimalFormat("###########.000");
				}
				tsi.setTaskRunSpeed(df.format(n));// 采集速度
				if("1".equals(task.getTaskState())){
					tsi.setTaskStatus("正常运行");
				} else if("3".equals(task.getTaskState())) {
					tsi.setTaskStatus("成功处理异常后，正常运行");
				}
				resList.add(tsi);
			}
		}
		return resList;
	}

	@Override
	public List<TaskStaticInfoVO> getNotRuningTask(TaskStaticInfoVO v, String loginName) throws Exception {
		
		List<TaskStaticInfoVO> resList = new ArrayList<>();
		
		StringBuffer sb = new StringBuffer("from Task t where t.taskState not in (1,2) ");
		if(!StringUtil.isEmpty(v.getTaskName()) && !"all".equals(v.getTaskName())){
			sb.append(" and t.taskName like '%"+v.getTaskName()+"%'");
		}
		if(!StringUtil.isEmpty(v.getTaskStatus()) && "normal".equals(v.getTaskStatus())){// 闲置任务
			sb.append(" and t.taskState = 0 ");
		} else if(!StringUtil.isEmpty(v.getTaskStatus()) && "failed".equals(v.getTaskStatus())){// 出现异常终止
			sb.append(" and t.taskState = 4 ");
		} else if(!StringUtil.isEmpty(v.getTaskStatus()) && "exception_ignore".equals(v.getTaskStatus())){// 处理异常成功运行
			sb.append(" and t.taskState = 3 ");
		} else { // 所有
			sb.append(" and t.taskState in (0,3,4) ");
		}
		sb.append(" and taskCreator = '"+loginName+"'");
		
		List<Task> list = this.taskDao.find(sb.toString());
		if(list != null && list.size() > 0){
			for (Task task : list) {
				
				TaskStaticInfoVO vo = new TaskStaticInfoVO();
				vo.setTaskId(task.getTaskId());
				vo.setTaskName(task.getTaskName());// 任务名称
				// 下次开始时间
				if(!StringUtil.isEmpty(task.getTaskPlanId())){
					
					Date endDate = task.getEndDate() == null ? new Date() : task.getEndDate();
					SchedulePlan plan = this.schedulePlanDao.get(SchedulePlan.class, task.getTaskPlanId());
					
					if("0".equals(plan.getPlanType())){// 按周执行
						
						String[] arr = plan.getWeekCycle().split(",");
						Calendar calendar = Calendar.getInstance();      
						calendar.setTime(new Date());      
					    int num = calendar.get(Calendar.DAY_OF_WEEK) - 1 < 0 ? 7 : calendar.get(Calendar.DAY_OF_WEEK) - 1;
					    String day = "";
					    for (String d : arr) {
					    	String str = TimeUtil.getNextDay(endDate, ((7 - num) + ((plan.getCycleNum() - 1) * 7) + Integer.valueOf(d)));
					    	day += str.substring(0, 10) +" " + plan.getExecuteTime() + "<br>";
						}
					    vo.setTaskNextRunTimeStr(day);
					    
					} else if("1".equals(plan.getPlanType())){// 按天执行
						
						String str = TimeUtil.getNextDay(endDate, plan.getCycleNum()).substring(0, 10) + plan.getExecuteTime();
						vo.setTaskNextRunTimeStr(str);
						
					} else if("2".equals(plan.getPlanType())){// 按小时执行
						
						vo.setTaskNextRunTimeStr(TimeUtil.getNextHourTime(endDate, plan.getCycleNum()));
						
					} else {// 按分钟执行
						
						vo.setTaskNextRunTimeStr(TimeUtil.getNextMinute(endDate, plan.getCycleNum()));
					}
				} else {
					vo.setTaskNextRunTimeStr(" --- ");
				}
				// 上次运行耗时
				if(task.getStartDate() != null && task.getEndDate() != null){
					vo.setTaskRunTime(TimeUtil.formatSecond((task.getEndDate().getTime() - task.getStartDate().getTime())/1000 + ""));
				} else {
					vo.setTaskRunTime(" --- ");
				}
				// 采集记录
				vo.setCollectNum(task.getFinishCount().longValue());
				
				// 上次采集平均速度
				if(task.getStartDate() != null && task.getEndDate() != null){
					String time = (task.getEndDate().getTime() - task.getStartDate().getTime())/1000 + "";
					Double n = Double.valueOf(String.valueOf(task.getFinishCount()))/Double.valueOf(String.valueOf(time));
					if(n < 1){
						df = new DecimalFormat("0.000");
					} else {
						df = new DecimalFormat("###########.000");
					}
					vo.setTaskRunSpeed(df.format(n));
				} else {
					vo.setTaskRunSpeed("0");
				}
				// 任务状态
				if("0".equals(task.getTaskState())){
					vo.setTaskStatus("闲置");
				} else if("3".equals(task.getTaskState())){
					vo.setTaskStatus("成功处理异常-闲置");
				} else if("4".equals(task.getTaskState())){
					vo.setTaskStatus("出错终止-闲置");
				}
				
				resList.add(vo);
			}
		}
		return resList;
	}

}
