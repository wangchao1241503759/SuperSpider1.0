/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年7月26日
 */
package cn.com.infcn.superspider.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.infcn.ade.common.dao.BaseDaoI;
import cn.com.infcn.superspider.common.Constant;
import cn.com.infcn.superspider.io.output.Doc;
import cn.com.infcn.superspider.io.output.ExceptionOutputHandlerimpl;
import cn.com.infcn.superspider.listener.DbSpiderListener;
import cn.com.infcn.superspider.listener.FileSpiderListener;
import cn.com.infcn.superspider.listener.FtpSpiderListener;
import cn.com.infcn.superspider.listener.OutputDispatcher;
import cn.com.infcn.superspider.listener.WebSpiderListener;
import cn.com.infcn.superspider.model.TTaskLog;
import cn.com.infcn.superspider.model.Task;
import cn.com.infcn.superspider.pagemodel.EasyUIDataGrid;
import cn.com.infcn.superspider.pagemodel.Log;
import cn.com.infcn.superspider.pagemodel.PageHelper;
import cn.com.infcn.superspider.service.TaskLogServiceI;
import cn.com.infcn.superspider.utils.ConfigUtil;
import cn.com.infcn.superspider.utils.DateUtil;
import cn.com.infcn.superspider.utils.HqlUtils;
import cn.com.infcn.superspider.utils.JsonFileFilter;
import cn.com.infcn.superspider.utils.ModelParseUtil;
import cn.com.infcn.superspider.utils.StringUtil;
import cn.com.infcn.superspider.utils.UUIDCreater;
import cn.com.infcn.superspider.utils.XmlConfigUtil;

import com.alibaba.fastjson.JSONArray;
import com.justme.superspider.spider.Counter;
import com.justme.superspider.spider.SpiderListener;
import com.justme.superspider.task.TaskQueue;
import com.justme.superspider.xml.Site;
import com.justme.superspider.xml.Target;

/**
 * @author lihf
 * @date 2016年7月26日
 */
@Service(value="taskLogService")
public class TaskLogServiceImpl implements TaskLogServiceI
{
	
	@Autowired
	private BaseDaoI<TTaskLog> taskLogDao;
	
	@Autowired
	private BaseDaoI<Task> taskDao;
	

	/**
	 * 添加任务日志
	 * @author lihf
	 * @date 2016年7月28日	下午6:37:13
	 * @param log
	 * @throws Exception
	 */
	@Override
	public void add(Log log) throws Exception
	{
		if(null==log)
		{
			throw new Exception("参数不能为空！");
		}
		TTaskLog t = new TTaskLog();
		BeanUtils.copyProperties(log, t);
		t.setId(UUIDCreater.getUUID());
		taskLogDao.save(t);
	}
	/**
	 * 获取任务日志的列表数据
	 *
	 * @author lihf
	 * @date 2016年7月26日 下午4:44:25
	 * @param log
	 * @param ph
	 * @return
	 * @throws Exception
	 */
	@Override
	public EasyUIDataGrid<?> dataGrid(Log log, PageHelper ph) throws Exception
	{
		EasyUIDataGrid<Log> dataGrid = new EasyUIDataGrid<Log>();
		List<Log> taskLogList = new ArrayList<Log>();
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from TTaskLog t where 1=1 ";
		List<TTaskLog> tList = taskLogDao.find(hql + whereHql(log, params) + HqlUtils.orderHql(ph), params, ph.getPage(), ph.getRows());

		for (TTaskLog t : tList)
		{
			Log taskLog = new Log();
			BeanUtils.copyProperties(t, taskLog);
			taskLogList.add(taskLog);
		}
		dataGrid.setRows(taskLogList);
		dataGrid.setTotal(taskLogDao.count("select count(t.id) " + hql + whereHql(log, params), params));
		return dataGrid;
	}

	/**
	 * 删除
	 * 
	 * @author lihf
	 * @date 2016年7月27日 下午3:56:19
	 * @param id
	 * @throws Exception
	 */
	@Override
	public void delete(String id) throws Exception
	{
		if (StringUtil.isEmpty(id))
		{
			throw new Exception("参数不能为空！");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		String hql = " from TTaskLog t where 1=1 and t.id=:id ";
		TTaskLog t = taskLogDao.get(hql, params);

		taskLogDao.delete(t);
	}

	/**
	 * 批量删除
	 * 
	 * @author lihf
	 * @date 2016年7月27日 下午3:56:27
	 * @param ids
	 * @throws Exception
	 */
	@Override
	public void deleteBatch(String ids) throws Exception
	{
		if (StringUtil.isEmpty(ids))
		{
			throw new Exception("参数不能为空！");
		}
		String[] idList = ids.split(",");
		if (null == idList || idList.length == 0)
		{
			throw new Exception("参数不能为空！");
		}
		for (String id : idList)
		{
			this.delete(id);
		}
	}
	
    /**
     * 删除全部
     * @author lihf
     * @date 2016年7月27日	下午3:56:27
     * @param ids
     * @throws Exception
     */
	@Override
    public void deleteAll() throws Exception
    {
		String hql = " delete from TTaskLog t where t.event!='"+Constant.TASK_LOG_TYPE_RUNTIME+"'";
		taskLogDao.executeHql(hql);
    }
	

    /**
     * 根据id获取日志对象
     * @author lihf
     * @date 2016年7月27日	下午5:56:12
     * @param id
     * @return
     * @throws Exception
     */
	@Override
    public Log getLogById(String id) throws Exception
    {
		Log log = new Log();
		if (StringUtil.isEmpty(id))
		{
			throw new Exception("参数不能为空！");
		}
		TTaskLog t = taskLogDao.get(TTaskLog.class, id);
		if(null!=t)
		{
			BeanUtils.copyProperties(t, log);
		}
		return log;
    }

	/**
	 * 拼接查询条件
	 * 
	 * @author lihf
	 * @date 2016年7月26日 下午4:57:37
	 * @param log
	 * @param params
	 * @return
	 */
	private String whereHql(Log log, Map<String, Object> params)
	{
		String hql = "";
		StringBuffer sb = new StringBuffer();
		if (log != null)
		{
			if (!StringUtil.isEmpty(log.getTaskName()))
			{
				sb.append(" and t.taskName like :taskName");
				params.put("taskName", "%%" + log.getTaskName() + "%%");
			}
			if (!StringUtil.isEmpty(log.getLevel()))
			{
				sb.append(" and t.level =:level");
				params.put("level", log.getLevel());
			}
			if (!StringUtil.isEmpty(log.getSource()))
			{
				sb.append(" and t.source =:source");
				params.put("source", log.getSource());
			}
			if (!StringUtil.isEmpty(log.getCreateTimeStart()))
			{
				sb.append(" and t.createTime >=:createTimeStart");
				params.put("createTimeStart", DateUtil.dateValidation(log.getCreateTimeStart(), "yyyy-MM-dd"));
			}
			if (!StringUtil.isEmpty(log.getCreateTimeEnd()))
			{
				sb.append(" and t.createTime <=:createTimeEnd");
				params.put("createTimeEnd", DateUtil.dateValidation(log.getCreateTimeEnd()+" 23:59:59", "yyyy-MM-dd HH:mm:ss"));
			}
			if(!StringUtil.isEmpty(log.getTaskId())){
				sb.append(" and t.taskId = :taskId ");
				params.put("taskId", log.getTaskId());
			}
			if(!StringUtil.isEmpty(log.getTaskId()) && "1".equals(log.getEvent())){
				sb.append(" and t.event ='"+Constant.TASK_LOG_TYPE_RUNTIME +"'");
			} else {
				sb.append(" and t.event !='"+Constant.TASK_LOG_TYPE_RUNTIME +"'");
			}
		}
		hql = sb.toString();
		return hql;
	}
	
	@Override
	public long getUnusedTask(String loginName) throws Exception {
		
		BigInteger countBySql = taskDao.countBySql("select count(*) from task where state = 0 and creator = '"+loginName+"'");
		countBySql.longValue();
		return countBySql.longValue();
	}
	@Override
	public List<TTaskLog> getInfoByTaskIdAndStartCount(Task task) {
		
		// TODO  去掉减1
		Map<String, Object> params = new HashMap<>();
		params.put("taskId", task.getTaskId());
		params.put("startCount", task.getStartCount() - 1);
		return this.taskLogDao.find("from TTaskLog where taskId = ? and startCount = ?", params);
	}
	@Override
	public TTaskLog getNewRuntimeInfoByTaskId(String taskId) {
		List<TTaskLog> list = this.taskLogDao.find("from TTaskLog where taskId = '"+taskId+"' and event = 'runtime' order by createTime desc");
		return (list != null && list.size() > 0) ? list.get(0) : null;
		
	}
	@Override
	public List<Map<String, Object>> getIndexBarInfo() {
		
		List<Map<String, Object>> resList = new ArrayList<>();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		//获取当前月第一天：
        Calendar c = Calendar.getInstance();    
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);
        String first = format.format(c.getTime());
        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();    
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
        String last = format.format(ca.getTime());
        
		List<Object[]> objList = this.taskLogDao.findBySql("select date_format(tl.create_time,'%y-%m-%d') as time , sum(tl.collect_num) as count from task_log tl " 
								+ " where tl.create_time between '"+first+"' and '"+last+"' and tl.`event` = 'fetchFinshNum'"
							 	+ " group by  time order by time asc");
		
		if(objList != null && objList.size() > 0){
			for (Object[] obj : objList) {
 				Map<String, Object> map = new HashMap<>();
				map.put("day", obj[0]);
				map.put("num", obj[1]);
				resList.add(map);
			}
		} else {
			Map<String, Object> map = new HashMap<>();
			map.put("day", 0);
			map.put("num", 0);
			resList.add(map);
		}
		
		return resList;
	}
	@Override
	public Map<String, Object> getIndexMainInfo() {
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
		
		Map<String, Object> map = new HashMap<>();
		
		// 任务最早开始时间
		if("无".equals(this.taskLogDao.findBySql("SELECT  CAST(IFNULL(MIN(tl.create_time),'无') AS CHAR(10)) AS TIME FROM task_log tl WHERE tl.event = 'start'").get(0))){
			map.put("firstTime", "无");
		} else {
			String time = String.valueOf(this.taskLogDao.findBySql("SELECT  CAST(IFNULL(MIN(tl.create_time),'无') AS CHAR(10)) AS TIME FROM task_log tl WHERE tl.event = 'start'").get(0));
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");  
		    ParsePosition pos = new ParsePosition(0);  
			map.put("firstTime", sf.format(sd.parse(time, pos)));
		}
		// 任务累计运行次数
		map.put("count", this.taskDao.findBySql("SELECT IFNULL(SUM(start_count), 0) AS COUNT FROM task").get(0));
		
		// 任务累计采集数量
		map.put("number", this.taskLogDao.findBySql("SELECT IFNULL(SUM(collect_num), 0) AS num FROM task_log WHERE event = 'fetchfinshnum'").get(0));
		
		// 当月 当天
		sf = new SimpleDateFormat("yyyy年MM月");
		map.put("month", sf.format(new Date(System.currentTimeMillis())));
		
		sf = new SimpleDateFormat("yyyy年MM月dd日");
		map.put("day", sf.format(new Date(System.currentTimeMillis())));
		
		// 查询当月的信息
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		//获取当前月第一天：
        Calendar c = Calendar.getInstance();    
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);
        String first = format.format(c.getTime());
        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();    
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
        String last = format.format(ca.getTime());
        
        map.put("taskNum", taskDao.countBySql("SELECT COUNT(id) FROM task").intValue());
        
        map.put("runTaskNum", this.taskLogDao.findBySql("SELECT IFNULL(COUNT(id), 0) FROM task_log tl WHERE tl.create_time BETWEEN '"+first+"' AND '"+last+"' AND tl.event = 'start'").get(0));
        
        map.put("collectNum", this.taskLogDao.findBySql("SELECT IFNULL(SUM(tl.collect_num), 0) FROM task_log tl WHERE tl.create_time BETWEEN '"+first+"' AND '"+last+"' AND tl.event = 'fetchFinshNum'").get(0));
		
		return map;
		
	}
	
	/**
	 * 获取没有统计的任务日志列表数据
	 * @author lihf
	 * @date 2016年9月18日	下午5:20:13
	 * @param log
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<TTaskLog> getTaskLogListByNoStatis() throws Exception
	{
		String hql = "from TTaskLog t where (t.isStatistics !='Y' or t.isStatistics IS NULL) and t.event='"+Constant.TASK_LOG_TYPE_RUNTIME+"'";
		List<TTaskLog> taskLogList = taskLogDao.find(hql);
		return taskLogList;
	}
	
	/**
	 * 异常数据处理
	 * @author lihf
	 * @date 2016年10月24日	上午11:14:21
	 * @param id
	 * @throws Exception
	 */
	@Override
    public void appendExceptionHandling(String id) throws Exception
	{
		if(StringUtil.isEmpty(id))
		{
			throw new Exception("参数不能为空！");
		}
		
//		//1获取异常数据的任务
//		TTaskLog tTaskLog = taskLogDao.get(TTaskLog.class, id);
//		if(null == tTaskLog)
//		{
//			throw new Exception("没有找到任务日志！");
//		}
//		String taskId = tTaskLog.getTaskId();
		String taskId = id;
		Task task = taskDao.get(Task.class, taskId);
		if(null == task)
		{
			throw new Exception("没有找到任务！");
		}
		
		//2根据任务判断异常数据文件是否存在
		String errorPath = ConfigUtil.get("errorPath");
		String jsonFileName = errorPath+File.separator+task.getTaskName()+"_"+task.getTaskId()+"_error.json";
		
		File file = new File(jsonFileName);
		if(!file.exists())
		{
			throw new Exception("异常数据"+jsonFileName+"不存在！");
		}
		
		Site site = XmlConfigUtil.toSite(task.getTaskJson());
		
		SpiderListener listener = null;
		if("db".equalsIgnoreCase(task.getTaskType()))
		{
			listener = new DbSpiderListener();
		}
		else if("web".equalsIgnoreCase(task.getTaskType()))
		{
			listener = new WebSpiderListener();
		}
		else if("ftp".equalsIgnoreCase(task.getTaskType()))
		{
			listener = new FtpSpiderListener();
		}
		else if("file".equalsIgnoreCase(task.getTaskType()))
		{
			listener = new FileSpiderListener();
		}
		//初始化网站的队列容器
		site.queue = new TaskQueue();
		site.queue.init();
		//初始化网站目标Model计数器
		site.counter = new Counter();
		OutputDispatcher output = OutputDispatcher.me().init(site, listener,new ExceptionOutputHandlerimpl()).startup();// 启动输出适配器;
		
		JSONArray jsonArray = null;
		Target target = null;
		
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		FileInputStream fis = new FileInputStream(file);

		int b = fis.read(buf);
		while (b != -1)
		{
			bos.write(buf, 0, b);
			b = fis.read(buf);
		}
		
		byte[] text = bos.toByteArray();
		String json = new String(text);

		String[] jsonArrayList = json.split("@@@");
		for (String jsonArrayText : jsonArrayList)
		{
			jsonArray = JSONArray.parseArray(jsonArrayText);
			Doc doc = ModelParseUtil.toDoc(output, jsonArray, target);
			if (doc != null)
			{
				output.pushQueue(doc);
			}
		}
		fis.close();
		//修改日志状态
//		List<TTaskLog> taskLogList = null;
//		Map<String,Object> params = new HashMap<String,Object>();
//		params.put("taskId", taskId);
//		String hql = " from TTaskLog t where 1=1 and t.taskId=taskId";
//		taskLogList = taskLogDao.find(hql,params);
//		for(TTaskLog taskLog:taskLogList)
//		{
//			TTaskLog log = taskLogDao.get(TTaskLog.class,taskLog.getId());
//			log.setStatus("1");
//			taskLogDao.saveOrUpdate(log);
//		}
		//删除文件
		file.delete();

		// 停止任务输出存储器;
//		if (output != null)
//		{
//			output.shutdown();
//		}
	}
	
	/**
	 * 异常数据列表
	 * @author lihf
	 * @date 2016年10月27日	上午11:35:27
	 * @param ph
	 * @return
	 * @throws Exception
	 */
	@Override
	public EasyUIDataGrid<Task> dataGrid(PageHelper ph) throws Exception
	{
		EasyUIDataGrid<Task> dataGrid = new EasyUIDataGrid<Task>();
		
		//获取异常数据目录地址
		String errorPath = ConfigUtil.get("errorPath");
		
		File file = new File(errorPath);
		
		JsonFileFilter filter = new JsonFileFilter();
		File[] jsonFiles = file.listFiles(filter);
		List<Task> taskList = new ArrayList<Task>();
		if(null!=jsonFiles && jsonFiles.length>0)
		{
			for(File f:jsonFiles)
			{
				Task task = new Task();
				String taskId = f.getName().substring(f.getName().length()-43,f.getName().length()-11);
				String taskName = f.getName().substring(0,f.getName().length()-44);
				task.setTaskName(taskName);
				task.setTaskId(taskId);
				taskList.add(task);
			}
		}
		dataGrid.setTotal((long) taskList.size());
		dataGrid.setRows(taskList);
//		if(!file.exists())
//		{
//			throw new Exception("异常数据"+jsonFileName+"不存在！");
//		}
		return dataGrid;
	}
	
	/**
	 * 批量处理异常数据
	 * @author lihf
	 * @date 2016年10月27日	下午4:57:46
	 * @param ids
	 * @throws Exception
	 */
	public void appendExceptionProcess(String ids) throws Exception
	{
		if(StringUtil.isEmpty(ids))
		{
			throw new Exception("参数不能为空！");
		}
		
		String[] taskIdList = ids.split(",");
		for(String taskId:taskIdList)
		{
			appendExceptionHandling(taskId);
		}
	}

}
