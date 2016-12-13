/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年9月19日
 */
package cn.com.infcn.superspider.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.infcn.ade.common.dao.BaseDaoI;
import cn.com.infcn.superspider.common.Constant;
import cn.com.infcn.superspider.model.TTaskLog;
import cn.com.infcn.superspider.model.TaskStaticInfo;
import cn.com.infcn.superspider.pagemodel.EchartsConfig;
import cn.com.infcn.superspider.pagemodel.SeriesEntity;
import cn.com.infcn.superspider.pagemodel.TaskStatistics;
import cn.com.infcn.superspider.pagemodel.TaskStatisticsConfig;
import cn.com.infcn.superspider.service.TaskStaticServiceI;
import cn.com.infcn.superspider.utils.DateUtil;
import cn.com.infcn.superspider.utils.StringUtil;



/**
 * @author lihf
 * @date 2016年9月19日
 */
@Service
public class TaskStaticServiceImpl implements TaskStaticServiceI
{
	@Autowired
	private BaseDaoI<TaskStaticInfo> taskStatisticsDao;
	@Autowired
	private BaseDaoI<TTaskLog> taskLogDao;

	/**
	 * 添加任务统计
	 * @author lihf
	 * @date 2016年9月19日	上午11:04:46
	 * @param taskStatistics
	 * @throws Exception
	 */
	@Override
	public void add(TaskStatistics taskStatistics) throws Exception
	{
		if(null==taskStatistics)
		{
			throw new Exception("参数不能为空！");
		}
		TaskStaticInfo taskStaticInfo = new TaskStaticInfo();
		BeanUtils.copyProperties(taskStatistics, taskStaticInfo);
		taskStatisticsDao.save(taskStaticInfo);
	}
	
	/**
	 * 修改任务统计
	 * @author lihf
	 * @date 2016年9月19日	上午11:04:46
	 * @param taskStatistics
	 * @throws Exception
	 */
	public void edit(TaskStatistics taskStatistics) throws Exception
	{
		if(null==taskStatistics)
		{
			throw new Exception("参数不能为空！");
		}
		TaskStaticInfo taskStaticInfo = taskStatisticsDao.get(TaskStaticInfo.class, taskStatistics.getId());
		BeanUtils.copyProperties(taskStatistics, taskStaticInfo,new String[]{"id"});
		taskStatisticsDao.saveOrUpdate(taskStaticInfo);
		
	}
	
	/**
	 * 判断是否存在今天的任务统计数据
	 * @author lihf
	 * @date 2016年9月19日	下午3:20:29
	 * @param taskStatistics
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean isExistsTodayTask(TaskStatistics taskStatistics) throws Exception
	{
		boolean flag = false;
		String hql = "select count(1) from TaskStaticInfo t where 1=1 and t.statisticTime=:statisticTime and t.taskId=:taskId and t.startCount=:startCount ";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("statisticTime", taskStatistics.getStatisticTime());
		params.put("taskId", taskStatistics.getTaskId());
		long count = taskStatisticsDao.count(hql, params);
		if(count>0)
		{
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 获取任务统计信息
	 * @author lihf
	 * @date 2016年9月19日	下午3:50:33
	 * @param taskStatistics
	 * @return
	 * @throws Exception
	 */
	@Override
	public TaskStaticInfo getTaskStaticInfo(TaskStatistics taskStatistics) throws Exception
	{
		TaskStaticInfo taskStaticInfo = null;
		String hql = " from TaskStaticInfo t where 1=1 and t.statisticTime=:statisticTime and t.taskId=:taskId and t.startCount=:startCount ";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("statisticTime", taskStatistics.getStatisticTime());
		params.put("taskId", taskStatistics.getTaskId());
		params.put("startCount", taskStatistics.getStartCount());
		List<TaskStaticInfo> taskStaticInfoList= taskStatisticsDao.find(hql, params);
		if(null!=taskStaticInfoList && taskStaticInfoList.size()>0)
		{
			taskStaticInfo = taskStaticInfoList.get(0);
		}
		return taskStaticInfo;
		
	}
	
	/**
	 * 任务统计
	 * @author lihf
	 * @date 2016年9月20日	下午3:45:43
	 * @param taskStatisticsConfig
	 * @return
	 * @throws Exception
	 */
	public EchartsConfig getTaskStatistics(TaskStatisticsConfig taskStatisticsConfig) throws Exception
	{
		EchartsConfig echartsConfig = new EchartsConfig();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> params = new HashMap<String,Object>();
		//按年
		if("year".equalsIgnoreCase(taskStatisticsConfig.getDimension()))
		{
			if("gross".equalsIgnoreCase(taskStatisticsConfig.getGuideline()))
			{
				String sql = "SELECT SUM(collect_num) collect_num,YEAR( FROM_UNIXTIME( UNIX_TIMESTAMP(create_time),'%Y-%m-%d %H:%i:%S') ) AS YEAR FROM task_static_info where 1=1 "+ whereSql(taskStatisticsConfig,params,true) +" GROUP BY YEAR ORDER BY YEAR";
				
				List<Object[]> taskYearList = taskStatisticsDao.findBySql(sql, params);
				if(null!=taskYearList && taskYearList.size()>0)
				{
					for(Object o:taskYearList)
					{
						Object[] object = (Object[]) o;
						Map<String,Object> dataMap = new HashMap<String,Object>();
						dataMap.put("data", object[0]);
						dataMap.put("names", object[1]+"年");
						list.add(dataMap);
					}
				}else {
					Map<String, Object> map = new HashMap<String,Object>();
					map.put("data", 0);
					map.put("names", 0);
					list.add(map);
				}
			}
			else if("increment".equalsIgnoreCase(taskStatisticsConfig.getGuideline()))
			{
				//计算增量
				String sql = "SELECT SUM(collect_num) collect_num,YEAR( FROM_UNIXTIME( UNIX_TIMESTAMP(create_time),'%Y-%m-%d %H:%i:%S') ) AS YEAR FROM task_static_info where 1=1 "+ whereSql(taskStatisticsConfig,params,true) +" GROUP BY YEAR ORDER BY YEAR";
				
				List<Object[]> taskYearList = taskStatisticsDao.findBySql(sql, params);
				if(null!=taskYearList && taskYearList.size()>0)
				{
					for(Object o:taskYearList)
					{
						Object[] object = (Object[]) o;
						Map<String,Object> dataMap = new HashMap<String,Object>();
						
						params.clear();
						String sql_pre_year = "SELECT IFNULL(SUM(collect_num),0) collect_num FROM task_static_info WHERE 1=1 "+ whereSql(taskStatisticsConfig,params,false) +" AND DATE_FORMAT( create_time,'%Y')=DATE_FORMAT(DATE_ADD('"+object[1]+"-01-01', INTERVAL -1 YEAR),'%Y')";
						List<Object[]> taskYearList_pre_year = taskStatisticsDao.findBySql(sql_pre_year, params);
						int pre_year_sum= 0;
						if(null!=taskYearList_pre_year && taskYearList_pre_year.size()>0)
						{
							Object oo=taskYearList_pre_year.get(0);
							pre_year_sum = ((BigDecimal)oo).intValue();
						}
						dataMap.put("data",  ((BigDecimal)object[0]).intValue() - pre_year_sum);
						dataMap.put("names", object[1]+"年");
						list.add(dataMap);
					}
				}else {
					Map<String, Object> map = new HashMap<String,Object>();
					map.put("data", 0);
					map.put("names", 0);
					list.add(map);
				}
			}
			else if("elapsed".equalsIgnoreCase(taskStatisticsConfig.getGuideline()))
			{
				//计算单次采集耗时
				echartsConfig = calculateElapsed(echartsConfig, taskStatisticsConfig, params);
			}
		}
		else if("quarter".equalsIgnoreCase(taskStatisticsConfig.getDimension()))
		{
			//按季
			if("gross".equalsIgnoreCase(taskStatisticsConfig.getGuideline()))
			{
				String sql = "SELECT SUM(collect_num) collect_num,YEAR( FROM_UNIXTIME( UNIX_TIMESTAMP(create_time),'%Y-%m-%d %H:%i:%S') ) AS YEAR ,QUARTER( FROM_UNIXTIME( UNIX_TIMESTAMP(create_time),'%Y-%m-%d %H:%i:%S') ) AS QUARTER FROM task_static_info where 1=1 "+ whereSql(taskStatisticsConfig,params,true) +" GROUP BY YEAR,QUARTER ORDER BY YEAR,QUARTER";
				
				List<Object[]> taskYearList = taskStatisticsDao.findBySql(sql, params);
				if(null!=taskYearList && taskYearList.size()>0)
				{
					for(Object o:taskYearList)
					{
						Object[] object = (Object[]) o;
						Map<String,Object> dataMap = new HashMap<String,Object>();
						dataMap.put("data", object[0]);
						dataMap.put("names", object[1]+"年"+object[2]+"季度");
						list.add(dataMap);
					}
				}else {
					Map<String, Object> map = new HashMap<String,Object>();
					map.put("data", 0);
					map.put("names", 0);
					list.add(map);
				}
			}
			else if("increment".equalsIgnoreCase(taskStatisticsConfig.getGuideline()))
			{
				//计算增量
				String sql = "SELECT SUM(collect_num) collect_num,YEAR( FROM_UNIXTIME( UNIX_TIMESTAMP(create_time),'%Y-%m-%d %H:%i:%S') ) AS YEAR ,QUARTER( FROM_UNIXTIME( UNIX_TIMESTAMP(create_time),'%Y-%m-%d %H:%i:%S') ) AS QUARTER FROM task_static_info where 1=1 "+ whereSql(taskStatisticsConfig,params,true) +" GROUP BY YEAR,QUARTER ORDER BY YEAR,QUARTER";
				
				List<Object[]> taskYearList = taskStatisticsDao.findBySql(sql, params);
				if(null!=taskYearList && taskYearList.size()>0)
				{
					for(Object o:taskYearList)
					{
						Object[] object = (Object[]) o;
						Map<String,Object> dataMap = new HashMap<String,Object>();
						
						params.clear();
						int year =(int) object[1];
						int quarter = (int) object[2];
						if(quarter==1)
						{
							quarter = 4;
							year = year-1;
						}
						else
						{
							quarter = quarter-1;
						}
						String sql_pre_quarter = "SELECT IFNULL(SUM(collect_num),0) collect_num,YEAR( FROM_UNIXTIME( UNIX_TIMESTAMP(create_time),'%Y-%m-%d %H:%i:%S') ) AS YEAR ,QUARTER( FROM_UNIXTIME( UNIX_TIMESTAMP(create_time),'%Y-%m-%d %H:%i:%S') ) AS QUARTER FROM task_static_info where 1=1 "+ whereSql(taskStatisticsConfig,params,false) +
								" AND YEAR( FROM_UNIXTIME( UNIX_TIMESTAMP(create_time),'%Y-%m-%d %H:%i:%S') )='"+year+"' AND QUARTER( FROM_UNIXTIME( UNIX_TIMESTAMP(create_time),'%Y-%m-%d %H:%i:%S') )='"+quarter+"'"+
								" GROUP BY YEAR,QUARTER ORDER BY YEAR,QUARTER";
						List<Object[]> taskQuarterList_pre_quarter = taskStatisticsDao.findBySql(sql_pre_quarter, params);
						int pre_quarter_sum= 0;
						if(null!=taskQuarterList_pre_quarter && taskQuarterList_pre_quarter.size()>0)
						{
							Object[] oo=taskQuarterList_pre_quarter.get(0);
							pre_quarter_sum = ((BigDecimal)oo[0]).intValue();
						}
						dataMap.put("data",  ((BigDecimal)object[0]).intValue() - pre_quarter_sum);
//						dataMap.put("data", object[0]);
						dataMap.put("names", object[1]+"年"+object[2]+"季度");
						list.add(dataMap);
					}
				}else {
					Map<String, Object> map = new HashMap<String,Object>();
					map.put("data", 0);
					map.put("names", 0);
					list.add(map);
				}

			}
			else if("elapsed".equalsIgnoreCase(taskStatisticsConfig.getGuideline()))
			{
				//计算单次采集耗时
				echartsConfig = calculateElapsed(echartsConfig, taskStatisticsConfig, params);
			}
		}
		else if("month".equalsIgnoreCase(taskStatisticsConfig.getDimension()))
		{
			//按月
			if("gross".equalsIgnoreCase(taskStatisticsConfig.getGuideline()))
			{
				String sql = "SELECT SUM(collect_num) collect_num,YEAR( FROM_UNIXTIME( UNIX_TIMESTAMP(create_time),'%Y-%m-%d %H:%i:%S') ) AS YEAR ,MONTH( FROM_UNIXTIME( UNIX_TIMESTAMP(create_time),'%Y-%m-%d %H:%i:%S') ) AS MONTH FROM task_static_info WHERE 1=1 "+ whereSql(taskStatisticsConfig,params,true) +" GROUP BY YEAR,MONTH ORDER BY YEAR,MONTH";
				
				List<Object[]> taskYearList = taskStatisticsDao.findBySql(sql, params);
				if(null!=taskYearList && taskYearList.size()>0)
				{
					for(Object o:taskYearList)
					{
						Object[] object = (Object[]) o;
						Map<String,Object> dataMap = new HashMap<String,Object>();
						dataMap.put("data", object[0]);
						dataMap.put("names", object[1]+"年"+object[2]+"月");
						list.add(dataMap);
					}
				}else {
					Map<String, Object> map = new HashMap<String,Object>();
					map.put("data", 0);
					map.put("names", 0);
					list.add(map);
				}
			}
			else if("increment".equalsIgnoreCase(taskStatisticsConfig.getGuideline()))
			{
				//计算增量
				String sql = "SELECT SUM(collect_num) collect_num,YEAR( FROM_UNIXTIME( UNIX_TIMESTAMP(create_time),'%Y-%m-%d %H:%i:%S') ) AS YEAR ,MONTH( FROM_UNIXTIME( UNIX_TIMESTAMP(create_time),'%Y-%m-%d %H:%i:%S') ) AS MONTH FROM task_static_info WHERE 1=1 "+ whereSql(taskStatisticsConfig,params,true) +" GROUP BY YEAR,MONTH ORDER BY YEAR,MONTH";
				
				List<Object[]> taskYearList = taskStatisticsDao.findBySql(sql, params);
				if(null!=taskYearList && taskYearList.size()>0)
				{
					for(Object o:taskYearList)
					{
						Object[] object = (Object[]) o;
						Map<String,Object> dataMap = new HashMap<String,Object>();

						params.clear();
						String sql_pre_month = "SELECT IFNULL(SUM(collect_num),0) collect_num FROM task_static_info WHERE 1=1 "+ whereSql(taskStatisticsConfig,params,false) +" AND DATE_FORMAT( create_time,'%Y-%m')=DATE_FORMAT(DATE_ADD('"+object[1]+"-"+object[2]+"-01', INTERVAL -1 MONTH),'%Y-%m')";
						List<Object[]> taskMonthList_pre_month = taskStatisticsDao.findBySql(sql_pre_month, params);
						int pre_month_sum= 0;
						if(null!=taskMonthList_pre_month && taskMonthList_pre_month.size()>0)
						{
							Object oo=taskMonthList_pre_month.get(0);
							pre_month_sum = ((BigDecimal)oo).intValue();
						}
						dataMap.put("data",  ((BigDecimal)object[0]).intValue() - pre_month_sum);
						dataMap.put("names", object[1]+"年"+object[2]+"月");
						list.add(dataMap);
					}
				}else {
					Map<String, Object> map = new HashMap<String,Object>();
					map.put("data", 0);
					map.put("names", 0);
					list.add(map);
				}
			}
			else if("elapsed".equalsIgnoreCase(taskStatisticsConfig.getGuideline()))
			{
				//计算单次采集耗时
				echartsConfig = calculateElapsed(echartsConfig, taskStatisticsConfig, params);
			}
		}
		else if("day".equalsIgnoreCase(taskStatisticsConfig.getDimension()))
		{
			//按日
			if("gross".equalsIgnoreCase(taskStatisticsConfig.getGuideline()))
			{
				String sql = "SELECT SUM(collect_num) collect_num,DATE_FORMAT(create_time,'%Y-%m-%d') AS DAY FROM task_static_info  WHERE 1=1 "+ whereSql(taskStatisticsConfig,params,true) +" GROUP BY DAY ORDER BY DAY ";

				List<Object[]> taskYearList = taskStatisticsDao.findBySql(sql, params);
				if (null != taskYearList && taskYearList.size() > 0)
				{
					for (Object o : taskYearList)
					{
						Object[] object = (Object[]) o;
						Map<String, Object> dataMap = new HashMap<String, Object>();
						dataMap.put("data", object[0]);
						dataMap.put("names", object[1]);
						list.add(dataMap);
					}
				}
				else
				{
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("data", 0);
					map.put("names", 0);
					list.add(map);
				}
			}
			else if("increment".equalsIgnoreCase(taskStatisticsConfig.getGuideline()))
			{
				//计算增量
				String sql = "SELECT SUM(collect_num) collect_num,DATE_FORMAT(create_time,'%Y-%m-%d') AS DAY FROM task_static_info  WHERE 1=1 "+ whereSql(taskStatisticsConfig,params,true) +" GROUP BY DAY ORDER BY DAY ";
				List<Object[]> taskYearList = taskStatisticsDao.findBySql(sql, params);
				if (null != taskYearList && taskYearList.size() > 0)
				{
					for (Object o : taskYearList)
					{
						Object[] object = (Object[]) o;
						Map<String, Object> dataMap = new HashMap<String, Object>();
						params.clear();
						String sql_pre_day = "SELECT IFNULL(SUM(collect_num),0) collect_num FROM task_static_info WHERE 1=1 "+ whereSql(taskStatisticsConfig,params,false) +" AND DATE_FORMAT( create_time,'%Y-%m-%d')=DATE_FORMAT(DATE_ADD('"+object[1]+"', INTERVAL -1 DAY),'%Y-%m-%d')";
						List<Object[]> taskDayList_pre_day = taskStatisticsDao.findBySql(sql_pre_day, params);
						int pre_day_sum= 0;
						if(null!=taskDayList_pre_day && taskDayList_pre_day.size()>0)
						{
							Object oo=taskDayList_pre_day.get(0);
							pre_day_sum = ((BigDecimal)oo).intValue();
						}
						dataMap.put("data",  ((BigDecimal)object[0]).intValue() - pre_day_sum);
						dataMap.put("names", object[1]);
						list.add(dataMap);
					}
				}
				else
				{
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("data", 0);
					map.put("names", 0);
					list.add(map);
				}
			}
			else if("elapsed".equalsIgnoreCase(taskStatisticsConfig.getGuideline()))
			{
				//计算单次采集耗时
				echartsConfig = calculateElapsed(echartsConfig, taskStatisticsConfig, params);
			}
		}
		echartsConfig.setList(list);
		return echartsConfig;
	}
	
	
	/**
	 * 计算单次采集耗时
	 * @author lihf
	 * @date 2016年9月22日	下午4:28:42
	 * @param echartsConfig
	 * @param taskStatisticsConfig
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public EchartsConfig calculateElapsed(EchartsConfig echartsConfig,TaskStatisticsConfig taskStatisticsConfig,Map<String,Object> params)
	{
		String sql_max_startcount = "select MAX(start_count) from task_log ";
		List<Object[]> objectList= taskLogDao.findBySql(sql_max_startcount);
		int max_start_count = 0;
		if(null!=objectList)
		{
			Object o = objectList.get(0);
			max_start_count = ((Integer) o).intValue();
		}
		//查找最大的次数
		List xAxisList = new ArrayList();
		if(max_start_count>0)
		{
			for(int i=0;i<=max_start_count;i++)
			{
				xAxisList.add(String.valueOf(i));
			}
		}
		String hql_startList = " from TTaskLog t where 1=1 "+ whereSql(taskStatisticsConfig,params,true) +" and t.event='"+Constant.TASK_LOG_TYPE_START+"' order by startCount";
		String hql_endList = " from TTaskLog t where 1=1 and t.event='"+Constant.TASK_LOG_TYPE_END+"'";
		
		List<TTaskLog> taskLogStartList = taskLogDao.find(hql_startList, params);
		List<TTaskLog> taskLogEndList = taskLogDao.find(hql_endList);
		
		List<String> legendList = new ArrayList<String>();
		List<SeriesEntity> seriesList = new ArrayList<SeriesEntity>();
		for(TTaskLog taskLogStart:taskLogStartList)
		{
			String taskName = taskLogStart.getTaskName();
			for(TTaskLog taskLogEnd:taskLogEndList)
			{
				//任务ID和开启次数相等
				if (taskLogStart.getTaskId().equalsIgnoreCase(taskLogEnd.getTaskId()) && taskLogStart.getStartCount() == taskLogEnd.getStartCount())
				{
					if(!legendList.contains(taskName))
					{
						legendList.add(taskName);
					}
					int differ_s  = DateUtil.getTimeDelta(taskLogStart.getCreateTime(), taskLogEnd.getCreateTime());
					
					//如果已经存在一部分数据
					if(seriesList.size()>0)
					{
						boolean flag = false;
						for(SeriesEntity seriesEntity:seriesList)
						{
							if(taskName.equalsIgnoreCase(seriesEntity.getName()))
							{
								flag = true;
								seriesEntity.getData().add(String.valueOf(differ_s));
							}
						}
						
						if(!flag)
						{
							//新添加数据
							SeriesEntity seriesEntity = new SeriesEntity();
							seriesEntity.setName(taskName);
							seriesEntity.setType("line");
							
							List data = new ArrayList();
							data.add(String.valueOf(differ_s));
							seriesEntity.setData(data);
							seriesList.add(seriesEntity);
						}
						
					}
					else
					{
						//新添加数据
						SeriesEntity seriesEntity = new SeriesEntity();
						seriesEntity.setName(taskName);
						seriesEntity.setType("line");
						
						List data = new ArrayList();
						data.add(String.valueOf(differ_s));
						seriesEntity.setData(data);
						seriesList.add(seriesEntity);
					}
				}
			}
		}
		
		//如果没有数据，加载不了图，所以添加一条数据
		if(xAxisList.size()==0)
		{
			xAxisList.add("0");
		}
		if(legendList.size()==0)
		{
			legendList.add("0");
		}
		if(seriesList.size()==0)
		{
			//添加空数据
			SeriesEntity seriesEntity = new SeriesEntity();
			seriesEntity.setName("0");
			seriesEntity.setType("line");
			
			List data = new ArrayList();
			data.add("0");
			seriesEntity.setData(data);
			seriesList.add(seriesEntity);
		}
		echartsConfig.setxAxisList(xAxisList);
		echartsConfig.setLegendList(legendList);
		echartsConfig.setSeriesList(seriesList);
		
		return echartsConfig;
	}
	
	/**
	 * 查询条件
	 * @author lihf
	 * @date 2016年9月21日	上午11:19:07
	 * @param taskStatisticsConfig
	 * @param params
	 * @return
	 */
	private String whereSql(TaskStatisticsConfig taskStatisticsConfig, Map<String, Object> params,boolean flag)
	{
		String sql = "";
		StringBuffer sb = new StringBuffer();
		if (taskStatisticsConfig != null)
		{
			//增量不需要这些条件
			if(flag)
			{
				if ("year".equalsIgnoreCase(taskStatisticsConfig.getDimension()) || "quarter".equalsIgnoreCase(taskStatisticsConfig.getDimension()))
				{
					if(StringUtil.isEmpty(taskStatisticsConfig.getStartYear()))
					{
						taskStatisticsConfig.setStartYear("0");
					}
					if(StringUtil.isEmpty(taskStatisticsConfig.getEndYear()))
					{
						taskStatisticsConfig.setEndYear("0");
					}
					sb.append(" and YEAR( FROM_UNIXTIME( UNIX_TIMESTAMP(create_time),'%Y-%m-%d %H:%i:%S') ) BETWEEN :startYear and :endYear ");
					params.put("startYear", Integer.parseInt(taskStatisticsConfig.getStartYear()));
					params.put("endYear", Integer.parseInt(taskStatisticsConfig.getEndYear()));
				}
				if ("month".equalsIgnoreCase(taskStatisticsConfig.getDimension()))
				{
					sb.append(" and DATE_FORMAT( create_time,'%Y-%m') >=:startYearMont AND DATE_FORMAT( create_time,'%Y-%m')<=:endYearMont");
					params.put("startYearMont", taskStatisticsConfig.getStartYearMonthrange()+"-"+taskStatisticsConfig.getStartMonth());
					params.put("endYearMont", taskStatisticsConfig.getEndYearMonthrange()+"-"+taskStatisticsConfig.getEndMonth());
				}
				if ("day".equalsIgnoreCase(taskStatisticsConfig.getDimension()))
				{
					sb.append(" and DATE_FORMAT( create_time,'%Y-%m-%d') >=:startDate AND DATE_FORMAT( create_time,'%Y-%m-%d')<=:endDate");
					params.put("startDate", taskStatisticsConfig.getStartDate());
					params.put("endDate", taskStatisticsConfig.getEndDate());
				}
			}
			
			//单任务条件
			if("single".equalsIgnoreCase(taskStatisticsConfig.getTaskRange()))
			{
				sb.append(" and task_id=:taskId");
				params.put("taskId", taskStatisticsConfig.getTaskId());
			}
			
			//多任务条件
			if("multiple".equalsIgnoreCase(taskStatisticsConfig.getTaskRange()))
			{
				String taskTypeStr = taskStatisticsConfig.getTasktype();
				if(!StringUtil.isEmpty(taskTypeStr))
				{
					String[] taskTypes = taskTypeStr.split(",");
					if(null!=taskTypes && taskTypes.length>0)
					{
						sb.append(" and source in(");
						for(int i=0;i<taskTypes.length;i++)
						{
							sb.append("'").append(taskTypes[i]).append("'");
							if(i<taskTypes.length-1)
							{
								sb.append(",");
							}
						}
						sb.append(")");
						
					}
				}
			}

		}
		sql = sb.toString();
		return sql;
	}

}
