/**
 * 
 */
package cn.com.infcn.superspider.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import cn.com.infcn.ade.common.persistence.HibernateDao;
import cn.com.infcn.ade.common.service.BaseService;
import cn.com.infcn.ade.common.utils.DateUtils;
import cn.com.infcn.superspider.dao.TaskDao;
import cn.com.infcn.superspider.io.output.OutputDispatcherFactory;
import cn.com.infcn.superspider.listener.TaskManager;
import cn.com.infcn.superspider.listener.TaskSpiderBean;
import cn.com.infcn.superspider.listener.TaskStation;
import cn.com.infcn.superspider.model.DbSource;
import cn.com.infcn.superspider.model.FieldMapping;
import cn.com.infcn.superspider.model.FileContentMapping;
import cn.com.infcn.superspider.model.FormModel;
import cn.com.infcn.superspider.model.SchedulePlan;
import cn.com.infcn.superspider.model.Task;
import cn.com.infcn.superspider.model.TaskOutput;
import cn.com.infcn.superspider.pagemodel.TaskExportModel;
import cn.com.infcn.superspider.service.FileSourceServiceI;
import cn.com.infcn.superspider.service.FtpSourceServiceI;
import cn.com.infcn.superspider.service.TaskServiceI;
import cn.com.infcn.superspider.service.WebParamSettingServiceI;
import cn.com.infcn.superspider.service.WebSourceServiceI;
import cn.com.infcn.superspider.service.WebTypeRuleServiceI;
import cn.com.infcn.superspider.utils.FileUtils;
import cn.com.infcn.superspider.utils.StringUtil;
import cn.com.infcn.superspider.utils.XmlConfigUtil;

import com.alibaba.fastjson.JSONObject;
import com.justme.superspider.spider.SpiderListener;
import com.justme.superspider.spider.SuperSpider;
import com.justme.superspider.xml.Site;

/**
 * @description 
 * @author WChao
 * @date   2015年12月21日 	下午6:25:00
 */
@Service(value="taskService")
@Transactional(readOnly=true)
public class TaskServiceImpl extends BaseService<Task,String> implements TaskServiceI {
	
	Logger logger = Logger.getLogger(TaskServiceImpl.class);
	@Autowired
	private TaskDao taskDao;
	@Autowired
	private DbSourceServiceImpl dbSourceService;
	@Autowired
	private TaskOutputServiceImpl taskOutputService;
	@Autowired
	private DbFieldMappingServiceImpl fieldMappingService;
	@Autowired
	private FileContentMappingServiceImpl fileContentMappingService;
	@Autowired
	private SchedulePlanServiceImpl schedulePlanService;
	@Autowired
	private TriggerServiceImpl triggerService;
	//爬虫监听器逻辑层接口;
	@Autowired
	public  SpiderListenerServiceImpl spiderListenerService ; 
	@Autowired
	private WebSourceServiceI webSourceService;
	@Autowired
	private FtpSourceServiceI ftpSourceService;
	@Autowired
	private FileSourceServiceI fileSourceService;
	@Autowired
	private WebParamSettingServiceI webParamSettingService;
	@Autowired
	private WebTypeRuleServiceI webTypeRuleService;
	@Override
	public HibernateDao<Task, String> getEntityDao() {
		 
		return taskDao;
	}

	@Override
	@Transactional(readOnly=false)
	public void deleteBatch(FormModel formModel) throws Exception {
		for(String id : formModel.getTaskIds())
		{
			DbSource dbSource = dbSourceService.getByTaskId(id);
			dbSourceService.deleteByTaskId(id);//删除数据来源;
			taskOutputService.deleteByTaskId(id);//删除任务输出;
			fieldMappingService.deleteByTaskId(id);//删除字段映射;
			fileContentMappingService.deleteByTaskId(id);//删除内容提取字段映射;
			triggerService.deleteTriggerByTask(id,dbSource);//删除任务触发器;
			//删除web任务的数据
			Task task = taskDao.get(Task.class, id);
			if(null!=task && task.getTaskType().equalsIgnoreCase("web"))
			{
				webSourceService.deleteWebSourceByTaskId(id);	//删除web数据来源和代理服务器
				webParamSettingService.deleteWebParamSettingByTaskId(id); //删除参数设置
				webTypeRuleService.deleteWebTypeRuleListByTaskId(id); //删除web页面规则
			}
			if(null!=task && task.getTaskType().equalsIgnoreCase("ftp"))
			{
				ftpSourceService.deleteFtpSourceByTaskId(id);	//删除ftp数据来源
				
			}
			if(null!=task && task.getTaskType().equalsIgnoreCase("file"))
			{
				fileSourceService.deleteFileSourceByTaskId(id);	//删除file数据来源
				
			}
			this.delete(id);//删除任务;
		}
	}
	/**
	 * 启动任务;
	 */
	@Override
	@Transactional(readOnly=false)
	public int start(List<String> taskIds) throws Exception {
		
		for(String id : taskIds)
		{
			Thread taskThread = new Thread(new TaskRunnable(id,"start"));
			taskThread.start();
			Task task = this.get(id);
			if(task != null){
				task.setTaskState("1");
				this.update(task);
			}
		}
		return 1;
	}

	@Override
	@Transactional(readOnly=false)
	public int stop(List<String> taskIds) throws Exception {
		
		for(String id : taskIds)
		{
			Thread taskThread = new Thread(new TaskRunnable(id,"stop"));
			taskThread.start();
			Task task = this.get(id);
			if(task != null){
				task.setTaskState("0");
				this.update(task);
			}
		}
		return 1;
	}
	
	@Override
	@Transactional(readOnly=false)
	public int start(String id) throws Exception {
		TaskSpiderBean spierBean = TaskManager.getInstance().get(id);
		Task task = this.get(id);
		if(spierBean == null)
		{
			Site site = XmlConfigUtil.toSite(task.getTaskJson());
			//初始化爬虫配置参数及监听器;
			SpiderListener spiderListener = spiderListenerService.getAndInitTaskSpiderListener(site, task);
			if(spiderListener == null)
				return 0;
			task.setTaskJson(JSONObject.toJSONString(site));
			if(task.getStartCount() == null)
				task.setStartCount(0);
			task.setStartCount(task.getStartCount()+1);
			this.update(task);
			task.setStartDate(DateUtils.parseDate(DateUtils.formatDateTime(new Date())));
			SuperSpider superspider = new SuperSpider().listen(spiderListener).init(site);
			TaskManager.getInstance().add(new TaskSpiderBean(superspider,TaskStation.me()));
			superspider.startup();
		}
		task.setFinishCount(0);
		task.setSuccessCount(0);
		task.setFailCount(0);
		task.setTaskState("1");
		this.update(task);
		return 1;
	}

	@Override
	@Transactional(readOnly=false)
	public int stop(String id) throws Exception {
		
		TaskSpiderBean spiderBean = TaskManager.getInstance().get(id);
		Task task = this.get(id);
		task.setEndDate(DateUtils.parseDate(DateUtils.formatDateTime(new Date())));
		task.setTaskState("0");
		this.update(task);
		if(spiderBean != null)
		{
			SuperSpider superSpider = spiderBean.getSpider();
			superSpider.shutdownNow(true);
			Site site = superSpider.getSites().iterator().next();
			TaskManager.getInstance().remove(site.getId());// 移除当前爬虫;
			OutputDispatcherFactory.getInstance().removeOutput(site);//移除任务输出
		}
		return 1;
	}
	/**
	 * 启用/禁用;
	 */
	@Override
	@Transactional(readOnly=false)
	public int onOff(List<Task> tasks) throws Exception {
		for(Task task : tasks){
			Task oldTask = this.get(task.getTaskId());
			if(!"-1".equals(oldTask.getTaskState())){
				oldTask.setTaskState("-1");
			}else{
				oldTask.setTaskState("0");
			}
			this.update(oldTask);
		}
		return 1;
	}
	
	@Override
	public Task getBean(String taskId) throws Exception {
		Task task = this.get(taskId);
		Task newTask = new Task();
		BeanUtils.copyProperties(task, newTask);
		return newTask;
	}
	
	@Override
	public List<Task> findTaskByStatus(String status) throws Exception {
		String hql = "from Task t where t.taskState = ?0";
		return taskDao.find(hql, status);
	}

	@Override
	public List<Task> findTaskByIds(List<String> ids) throws Exception {
		List<Task> tasks = new ArrayList<Task>();
		for(String id : ids){
			Task task = this.get(id);
			if(task != null){
				Task newTask = new Task();
				BeanUtils.copyProperties(task, newTask);
				tasks.add(newTask);
			}
		}
		return tasks;
	}

	@Override
	public List<Task> findTaskByPlan(String planId) throws Exception {
		String hql = "from Task t where t.taskPlanId = ?0";
		return taskDao.find(hql, planId);
	}
	
	@Override
	public Map<String,Object> doDataExport(String[] ids) throws Exception {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		TaskExportModel tem = new TaskExportModel();
		List<Task> tasks = new ArrayList<Task>();
		List<TaskOutput> taskOutputs = new ArrayList<TaskOutput>();
		List<DbSource> dbSources = new ArrayList<DbSource>();
		List<FieldMapping> fieldMappings = new ArrayList<FieldMapping>();
		List<FileContentMapping> fileContentMappings = new ArrayList<FileContentMapping>();
		List<SchedulePlan> schedulePlans = new ArrayList<SchedulePlan>();
		tem.setTasks(tasks);
		tem.setDbSources(dbSources);
		tem.setTaskOutputs(taskOutputs);
		tem.setFieldMappings(fieldMappings);
		tem.setFileContentMappings(fileContentMappings);
		tem.setSchedulePlans(schedulePlans);
		for(String id : ids){
			String taskId = id.replace("\"","");
			Task task = this.get(taskId);
			DbSource dbSource = dbSourceService.getByTaskId(taskId);
			TaskOutput taskOutput = taskOutputService.getByTaskId(taskId);
			List<FieldMapping> exprotFieldMappings = fieldMappingService.findByTaskId(taskId);
			List<FileContentMapping> exportFileContentMappings = fileContentMappingService.findContentMappingByTaskId(taskId);
			if(task.getTaskPlanId() != null && !"".equals(task.getTaskPlanId())){
				SchedulePlan schedulePlan = schedulePlanService.get(task.getTaskPlanId());
				schedulePlans.add(schedulePlan);
			}
			tasks.add(task);
			dbSources.add(dbSource);
			fieldMappings.addAll(exprotFieldMappings);
			fileContentMappings.addAll(exportFileContentMappings);
			taskOutputs.add(taskOutput);
		}
		ResourceBundle bundle = ResourceBundle.getBundle("application");
		String filePath = bundle.getString("exportPath")+"/";
		String fileName = bundle.getString("exportName")+".json";
		File file=FileUtils.createFile(fileName,filePath);
	    FileUtils.writeToFileByStream(file.getAbsolutePath(), JSONObject.toJSONString(tem),false);
	    resultMap.put("status",1);
	    resultMap.put("filepath", file.getAbsolutePath());
	    resultMap.put("filename", fileName);
		return resultMap;
	}

	@Override
	@Transactional(readOnly=false)
	public int doDataImport(MultipartFile file) throws Exception {
		CommonsMultipartFile cmf = (CommonsMultipartFile)file;
	   /* DiskFileItem fi = (DiskFileItem)cmf.getFileItem(); 
        File jsonFile = fi.getStoreLocation();*/
		String content = FileUtils.convertStreamToString(cmf.getInputStream());
		TaskExportModel tem=JSONObject.parseObject(content,TaskExportModel.class);
		if(tem.getTasks() != null)
		this.saveList(tem.getTasks());//保存任务;
		if(tem.getDbSources() != null)
		this.dbSourceService.saveList(tem.getDbSources());
		if(tem.getFieldMappings() != null)
		this.fieldMappingService.saveList(tem.getFieldMappings());
		if(tem.getFileContentMappings() != null)
		this.fileContentMappingService.saveList(tem.getFileContentMappings());
		if(tem.getTaskOutputs() != null)
		this.taskOutputService.saveList(tem.getTaskOutputs());
		if(tem.getSchedulePlans() != null)
		this.schedulePlanService.saveList(tem.getSchedulePlans());
		return 1;
	}
	
	/**
	 * 导入任务
	 * @author lihf
	 * @date 2016年7月8日	下午3:57:32
	 * @param file
	 * @throws Exception
	 */
	@Override
	public void importTask(MultipartFile file) throws Exception 
	{
		
//	        CommonsMultipartFile cmf = (CommonsMultipartFile) file;
//	        String content = FileUtils.convertStreamToString(cmf.getInputStream());
//	        List<TaskExportImportModel> taskExportImportModelList = JSONArray.parseArray(content, TaskExportImportModel.class);
//	        for(TaskExportImportModel t:taskExportImportModelList)
//	        {
//	        	Task task = t.getTask();
//	        	if(task.getTaskType().equalsIgnoreCase("web"))
//	        	{
//	        		webService.importTask(t);
//	        	}
//	        	
//	        }
	        
//	        //保存页面类型规则
//	        List<TWebTypeRule> typelist = JSONArray.parseArray(webConfig.getWebTypeRuleJson(), TWebTypeRule.class);
//	        List<TWebParamSetting> paramlist = JSONArray.parseArray(webConfig.getParamObjectListJson(), TWebParamSetting.class);
//	        List<FieldMapping> fieldList = JSONArray.parseArray(webConfig.getFieldMappingJson(), FieldMapping.class);

	}

	@Override
	@Transactional(readOnly=false)
	public void saveList(List<Task> tasks) throws Exception {
		for(Task task : tasks){
			this.save(task);
		}
	}
	
	/**
	 * 根据任务ID获取任务
	 * @author lihf
	 * @date 2016年4月14日	上午10:33:49
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	public Task getTaskById(String id) throws Exception
	{
		if(StringUtil.isEmpty(id))
		{
			throw new Exception("参数不能为空！");
		}
		Task task = taskDao.get(Task.class, id);
		return task;
		
	}
	
	/**
	 * 导出任务
	 * @author lihf
	 * @date 2016年7月8日	下午3:15:40
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String,Object> exportTask(String[] ids) throws Exception
	{
		Map<String,Object> resultMap = new HashMap<String, Object>();
//		List<TaskExportImportModel> taskExportImportModelList = new ArrayList<TaskExportImportModel>();
//		for(String id : ids){
//			String taskId = id.replace("\"","");
//			Task task = this.get(taskId);
//			if(task.getTaskType().equalsIgnoreCase("web"))
//			{
//				TaskExportImportModel webTaskExportImportModel = webService.exportTask(taskId);
//				taskExportImportModelList.add(webTaskExportImportModel);
//			}
//			else if(task.getTaskType().equalsIgnoreCase("db"))
//			{
//				TaskExportImportModel dbTaskExportImportModel = dbService.exportTask(taskId);
//				taskExportImportModelList.add(dbTaskExportImportModel);
//			}
//		}
//		ResourceBundle bundle = ResourceBundle.getBundle("application");
//		String filePath = bundle.getString("exportPath")+"/";
//		String fileName = bundle.getString("exportName")+".json";
//		File file=FileUtils.createFile(fileName,filePath);
//	    FileUtils.writeToFileByStream(file.getAbsolutePath(), JSONObject.toJSONString(taskExportImportModelList),false);
//	    resultMap.put("status",1);
//	    resultMap.put("filepath", file.getAbsolutePath());
//	    resultMap.put("filename", fileName);
		return resultMap;
	}
	
	/**
	 * 根据任务名称获取个数
	 * @author lihf
	 * @date 2016年10月14日	下午3:12:01
	 * @param taskName
	 * @return
	 * @throws Exception
	 */
	public String getTaskNameByName(String taskName,int num) throws Exception
	{
		String newTaskName=taskName;
		Long count = 0L;
		Map<String,Object> params = new HashMap<String,Object>();
		if(num>0)
		{
			newTaskName = taskName+"("+num+")";
		}
		params.put("taskName", newTaskName);
		String hql = " from Task t where 1=1 and taskName=:taskName";
		count = taskDao.countHqlResult("select count(*) " + hql, params);
		if (count>0)
		{
			num +=1;
			newTaskName = getTaskNameByName(taskName,num);
		}
		return newTaskName;
		
	}
	
	/**
	 * 检测任务名称是否存在
	 * @author lihf
	 * @date 2016年11月17日	上午9:55:40
	 * @param taskId
	 * @param taskName
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean checkTaskNameIsExists(String taskId,String taskName) throws Exception
	{
		boolean flag = false;
		if(StringUtil.isEmpty(taskName))
		{
			throw new Exception("参数不能为空！");
		}
		Long count = 0L;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("taskId", taskId);
		params.put("taskName", taskName);
		String hql = " from Task t where 1=1 and t.taskName = :taskName and t.taskId!=:taskId";
		count = taskDao.countHqlResult("select count(*) " + hql, params);
		if (count>0)
		{
			flag = true;
		}
		return flag;
	}
}
