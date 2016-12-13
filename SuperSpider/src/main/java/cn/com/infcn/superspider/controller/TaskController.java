package cn.com.infcn.superspider.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.justme.superspider.spider.SpiderListener;

import cn.com.infcn.ade.common.controller.BaseController;
import cn.com.infcn.ade.common.persistence.Page;
import cn.com.infcn.ade.common.persistence.PropertyFilter;
import cn.com.infcn.ade.system.model.User;
import cn.com.infcn.ade.system.utils.UserUtil;
import cn.com.infcn.superspider.common.Constant;
import cn.com.infcn.superspider.listener.DbSpiderListener;
import cn.com.infcn.superspider.listener.FileSpiderListener;
import cn.com.infcn.superspider.listener.FtpSpiderListener;
import cn.com.infcn.superspider.listener.TaskManager;
import cn.com.infcn.superspider.listener.TaskSpiderBean;
import cn.com.infcn.superspider.listener.WebSpiderListener;
import cn.com.infcn.superspider.model.FormModel;
import cn.com.infcn.superspider.model.SchedulePlan;
import cn.com.infcn.superspider.model.Task;
import cn.com.infcn.superspider.pagemodel.Json;
import cn.com.infcn.superspider.pagemodel.LicenseParam;
import cn.com.infcn.superspider.pagemodel.TaskExportImportModel;
import cn.com.infcn.superspider.service.CheckHandlerI;
import cn.com.infcn.superspider.service.FileServiceI;
import cn.com.infcn.superspider.service.FtpServiceI;
import cn.com.infcn.superspider.service.WebServiceI;
import cn.com.infcn.superspider.service.impl.DbServiceImpl;
import cn.com.infcn.superspider.service.impl.SchedulePlanServiceImpl;
import cn.com.infcn.superspider.service.impl.TaskServiceImpl;
import cn.com.infcn.superspider.utils.FileUtils;

/**
 * @author WChao
 * @date 2015年12月17日
 */
@Controller
@RequestMapping("task")
public class TaskController extends BaseController{
	
	@Autowired
	private TaskServiceImpl taskService;
	@Autowired
	private SchedulePlanServiceImpl schedulePlanService;
	@Autowired
	private WebServiceI webService;
	@Autowired
	private DbServiceImpl dbService;
	@Autowired
	private FtpServiceI ftpService;
	@Autowired
	private FileServiceI fileService;
	@Autowired 
	private CheckHandlerI checkHandler;
	
	/**
	 * 任务管理页面
	 */
	@RequestMapping(value="list",method = RequestMethod.GET)
	public String list(HttpServletRequest request) {
		request.setAttribute("db", request.getParameter("db"));
		request.setAttribute("web", request.getParameter("web"));
		request.setAttribute("ftp", request.getParameter("ftp"));
		request.setAttribute("file", request.getParameter("file"));

		return Constant.SUPERSPIDER+"/task/task_list";
	}
	/**
	 * 删除任务;
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="delete",method = RequestMethod.POST)
	@ResponseBody
	public Json delete(FormModel formModel) {
		Json json = new Json();
		try
        {
	        taskService.deleteBatch(formModel);
	        json.setSuccess(true);
	        json.setMsg("删除成功！");
        }
        catch (Exception e)
        {
	        json.setMsg(e.getMessage());
        }
		return json;
	}
	/**
	 * 获取任务;
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="get",method = RequestMethod.POST)
	@ResponseBody
	public Task get(String taskId) throws Exception{
		Task task = taskService.getBean(taskId);
		return task;
	}
	/**
	 * 获取任务json
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value="json",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<Task> page = getPage(request);
		if("id".equals(page.getOrderBy())){
			page.setOrderBy("taskCreateDate");
			page.setOrder("desc");
		}
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		if(filters != null && filters.size() > 0){//设置结束日期时间;
			for(PropertyFilter filter : filters){
				if(filter.getPropertyName().equals("taskCreateDate") && filter.getMatchType().toString().equals("LE")){
					Date date = (Date)filter.getMatchValue();
					date.setHours(23);
					date.setMinutes(59);
					date.setSeconds(59);
					filter.setMatchValue(date);
				}
			}
		}
		User user = UserUtil.getCurrentUser();
		if(user != null){
			PropertyFilter userPropertyFilter = new PropertyFilter("EQS_taskCreator",user.getLoginName());
			filters.add(userPropertyFilter);
		}
		page = taskService.search(page, filters);
		if(page.getResult() != null && page.getResult().size() > 0){
			for(Task task : page.getResult()){
				if(task.getTaskPlanId() != null && !"".equals(task.getTaskPlanId())){
					SchedulePlan sp = schedulePlanService.get(task.getTaskPlanId());
					task.setPlanName(sp.getName());
					task.setPlanDescription(sp.getDescription());
				}
			}
		}
		return getEasyUIData(page);
	}
	/**
	 * 启用/禁用任务;
	 */
	@RequestMapping(value="onOff",method = RequestMethod.POST)
	@ResponseBody
	public synchronized String onOff(FormModel formModel)throws Exception
	{
		int status = taskService.onOff(formModel.getTasks());
		if(status == 1){
			return "success";
		}
		return "fail";
	}
	/**
	 * 启动任务;
	 */
	@RequestMapping(value="start",method = RequestMethod.POST)
	@ResponseBody
	public synchronized String start(FormModel formModel)throws Exception
	{
		int status = taskService.start(formModel.getTaskIds());
		if(status == 1){
			return "success";
		}
		return "fail";
	}
	/**
	 * 停止任务;
	 */
	@RequestMapping(value="stop",method = RequestMethod.POST)
	@ResponseBody
	public synchronized Object stopSpider(FormModel formModel) throws Exception{
		int status = taskService.stop(formModel.getTaskIds());
		if(status == 1){
			return "success";
		}
		return "fail";
	}
	/**
	 * 查询所有正在运行的任务;
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="findRunningTask",method = RequestMethod.POST)
	@ResponseBody
	public List<Task> findRunningTask(@RequestBody List<String> idList)throws Exception{
		List<Task> list = taskService.findTaskByIds(idList);
		return list;
	}
	/**
	 * 任务添加页面
	 * @return
	 */
	@RequestMapping(value="add",method = RequestMethod.GET)
	public String add() {
		
		return Constant.SUPERSPIDER+"/task/task_add";
	}
	
	@RequestMapping(value="validSameTaskName",method = RequestMethod.GET)
	@ResponseBody
	public String validSameTaskName(HttpServletRequest request)throws Exception{
		String taskName = request.getParameter("taskName");
		String hql = "from Task t WHERE t.taskName = ?0";
		Task task = taskService.get(hql, taskName);
		if(task == null)
			return "0";
		return "1";
	}
	
	/**
	 * 任务导出
	 * @param idList
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="doDataExport",method = RequestMethod.GET)
	public void doDataExport(HttpServletRequest request,HttpServletResponse response)throws Exception{
		 String[] idsArray=  request.getParameter("ids").split(",");
//		 Map<String,Object> resultMap = taskService.doDataExport(idsArray);
//		 Map<String,Object> resultMap = taskService.exportTask(idsArray);
		 Map<String,Object> resultMap = new HashMap<String, Object>();
			List<TaskExportImportModel> taskExportImportModelList = new ArrayList<TaskExportImportModel>();
			for(String id : idsArray){
				String taskId = id.replace("\"","");
				Task task = this.get(taskId);
				if(task.getTaskType().equalsIgnoreCase("web"))
				{
					TaskExportImportModel webTaskExportImportModel = webService.exportTask(taskId);
					taskExportImportModelList.add(webTaskExportImportModel);
				}
				else if(task.getTaskType().equalsIgnoreCase("db"))
				{
					TaskExportImportModel dbTaskExportImportModel = dbService.exportTask(taskId);
					taskExportImportModelList.add(dbTaskExportImportModel);
				}
				else if(task.getTaskType().equalsIgnoreCase("ftp"))
				{
					TaskExportImportModel dbTaskExportImportModel = ftpService.exportTask(taskId);
					taskExportImportModelList.add(dbTaskExportImportModel);
				}
				else if(task.getTaskType().equalsIgnoreCase("file"))
				{
					TaskExportImportModel dbTaskExportImportModel = fileService.exportTask(taskId);
					taskExportImportModelList.add(dbTaskExportImportModel);
				}
			}
			ResourceBundle bundle = ResourceBundle.getBundle("application");
			String filePath = bundle.getString("exportPath")+"/";
			String fileName = bundle.getString("exportName")+".json";
			File file=FileUtils.createFile(fileName,filePath);
		    FileUtils.writeToFileByStream(file.getAbsolutePath(), JSONObject.toJSONString(taskExportImportModelList),false);
		    resultMap.put("status",1);
		    resultMap.put("filepath", file.getAbsolutePath());
		    resultMap.put("filename", fileName);
		    
         response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(resultMap.get("filename").toString(), "UTF-8"));// filename是下载的文件的名，建议最好用英文
         InputStream in = null;  
         OutputStream out = null;// 创建一个输出流对象
         try {  
            in = new FileInputStream(resultMap.get("filepath").toString());  
            int len = 0;  
            byte[] buffer = new byte[1024];  
            out = response.getOutputStream();  
            while((len = in.read(buffer)) > 0) {  
                out.write(buffer,0,len);  
            }  
         }catch(Exception e) {  
            throw new RuntimeException(e);  
         }finally {  
            if(in != null) {  
                try {  
                    in.close();  
                }catch(Exception e) {  
                    throw new RuntimeException(e);  
                }  
                  
            }  
         }  
	}
	/**
	 * 任务导入跳转页面;
	 * @return
	 */
	@RequestMapping(value="forDataImport",method = RequestMethod.GET)
	public String forDataImport(){
		return Constant.SUPERSPIDER+"/task/task_import";
	}
	/**
	 * 任务导入
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="doDataImport",method = RequestMethod.POST)
	@ResponseBody
	public Json doDataImport(MultipartFile resourcedata,HttpServletRequest request,HttpSession session)throws Exception
	{
		User user = (User) session.getAttribute("user");
		Json json = new Json();
		try
		{
			// taskService.doDataImport(resourcedata);
			// taskService.importTask(resourcedata);
			
			//查询注册文件中各组件能创建最大的任务数量
			int maxDBJobs = checkHandler.getMaxJobs("db");
			int maxWebJobs = checkHandler.getMaxJobs("web");
			int maxFtpJobs = checkHandler.getMaxJobs("ftp");
			int maxFileJobs = checkHandler.getMaxJobs("file");
			
			
			//查询数据库中任务数量
			LicenseParam licenseParam = new LicenseParam();
			licenseParam.setTaskType("db");
			int dbCount = checkHandler.getTaskCountByTaskType(licenseParam);
			licenseParam.setTaskType("web");
			int webCount = checkHandler.getTaskCountByTaskType(licenseParam);
			licenseParam.setTaskType("ftp");
			int ftpCount = checkHandler.getTaskCountByTaskType(licenseParam);
			licenseParam.setTaskType("file");
			int fileCount = checkHandler.getTaskCountByTaskType(licenseParam);
			
			//定义能增加的数量
			int db_append_count = 0;
			int web_append_count = 0;
			int ftp_append_count = 0;
			int file_append_count = 0;
			
			//定义更新的数量
			int db_update_count = 0;
			int web_update_count = 0;
			int ftp_update_count = 0;
			int file_update_count = 0;
			
			//定义导入的任务的数量
			int db_task_count = 0;
			int web_task_count = 0;
			int ftp_task_count = 0;
			int file_task_count = 0;
			
			CommonsMultipartFile cmf = (CommonsMultipartFile) resourcedata;
			String content = FileUtils.convertStreamToString(cmf.getInputStream());
			String isReplace = request.getParameter("replace");
			List<TaskExportImportModel> taskExportImportModelList = JSONArray.parseArray(content, TaskExportImportModel.class);
			for (TaskExportImportModel t : taskExportImportModelList)
			{
				Task task = t.getTask();
				task.setTaskCreator(user.getLoginName());
				task.setStartCount(0);
				Task taskExists = taskService.getTaskById(task.getTaskId());

				//任务已经存在，需要覆盖的任务，任务不存在，则需要追加进去
				if ("web".equalsIgnoreCase(task.getTaskType()))
				{
					web_task_count +=1;
					//任务已经存在，且不覆盖原有的任务，则跳过
					if(null!=taskExists && !"Y".equalsIgnoreCase(isReplace))
					{
						continue;
					}
					//任务对象为空，表示数据库中没有，则需要追加
					if(null==taskExists)
					{
						//如果最大值大于
						if(maxWebJobs>=(webCount+web_append_count+1))
						{
							web_append_count +=1;
						}
						else
						{
							continue;
						}
					}
					else
					{
						//计算需要更新的个数
						web_update_count +=1;
					}
					
					webService.importTask(t,isReplace);
				}
				else if ("db".equalsIgnoreCase(task.getTaskType()))
				{
					db_task_count +=1;
					//任务已经存在，且不覆盖原有的任务，则跳过
					if(null!=taskExists && !"Y".equalsIgnoreCase(isReplace))
					{
						continue;
					}
					//任务对象为空，表示数据库中没有，则需要追加
					if(null==taskExists)
					{
						//如果最大值大于
						if(maxDBJobs>=(dbCount+db_append_count+1))
						{
							db_append_count +=1;
						}
						else
						{
							continue;
						}
					}
					else
					{
						//计算需要更新的个数
						db_update_count +=1;
					}
					dbService.importTask(t,isReplace);
				}
				else if ("ftp".equalsIgnoreCase(task.getTaskType()))
				{
					ftp_task_count +=1;
					//任务已经存在，且不覆盖原有的任务，则跳过
					if(null!=taskExists && !"Y".equalsIgnoreCase(isReplace))
					{
						continue;
					}
					//任务对象为空，表示数据库中没有，则需要追加
					if(null==taskExists)
					{
						//如果最大值大于
						if(maxFtpJobs>=(ftpCount+ftp_append_count+1))
						{
							ftp_append_count +=1;
						}
						else
						{
							continue;
						}
					}
					else
					{
						//计算需要更新的个数
						ftp_update_count +=1;
					}
					ftpService.importTask(t,isReplace);
				}
				else if ("file".equalsIgnoreCase(task.getTaskType()))
				{
					file_task_count +=1;
					//任务已经存在，且不覆盖原有的任务，则跳过
					if(null!=taskExists && !"Y".equalsIgnoreCase(isReplace))
					{
						continue;
					}
					//任务对象为空，表示数据库中没有，则需要追加
					if(null==taskExists)
					{
						//如果最大值大于
						if(maxFileJobs>=(fileCount+file_append_count+1))
						{
							file_append_count +=1;
						}
						else
						{
							continue;
						}
					}
					else
					{
						//计算需要更新的个数
						file_update_count +=1;
					}
					fileService.importTask(t,isReplace);
				}

			}
			String msg = "";
			StringBuffer sb = new StringBuffer();
			sb.append("任务导入结果：");
			sb.append("@");
			int index=0;
			if(db_task_count>0)
			{
				index +=1;
				sb.append(index).append("、").append("需要导入的数据库采集任务数：").append(db_task_count).append("，").append("实际导入数：").append(db_append_count+db_update_count);
//				if(db_task_count>(db_append_count+db_update_count))
//				{
//					sb.append("，").append("请确认License数满足要求；");
//				}
				sb.append("@");
			}
			if(web_task_count>0)
			{
				index +=1;
				sb.append(index).append("、").append("需要导入的WEB采集任务数：").append(web_task_count).append("，").append("实际导入数：").append(web_append_count+web_update_count);
//				if(web_task_count>(web_append_count+web_update_count))
//				{
//					sb.append("，").append("请确认License数满足要求；");
//				}
				sb.append("@");
			}
			if(ftp_task_count>0)
			{
				index +=1;
				sb.append(index).append("、").append("需要导入的FTP采集任务数：").append(ftp_task_count).append("，").append("实际导入数：").append(ftp_append_count+ftp_update_count);
//				if(ftp_task_count>(ftp_append_count+ftp_update_count))
//				{
//					sb.append("，").append("请确认License数满足要求；");
//				}
				sb.append("@");
			}
			if(file_task_count>0)
			{
				index +=1;
				sb.append(index).append("、").append("需要导入的文件采集任务数：").append(file_task_count).append("，").append("实际导入数：").append(file_append_count+file_update_count);
//				if(file_task_count>(file_append_count+file_update_count))
//				{
//					sb.append("，").append("请确认License数满足要求；");
//				}
				sb.append("@");
			}
			sb.append("@");
			sb.append("未全部导入的情况，请确认License数满足要求！");
			msg = sb.toString();
			json.setSuccess(true);
			json.setMsg(msg);
		}
		catch (Exception e)
		{
			json.setMsg(e.getMessage());
		}
		return json;
	}
	
	/**
	 * 获取任务类型
	 * @author lihf
	 * @date 2016年9月1日	下午12:55:43
	 * @return
	 */
	@RequestMapping("getTaskTypeList")
	@ResponseBody
	public List<String> getTaskTypeList()
	{
		
		List<String> taskTypeList = new ArrayList<String>();
		try
        {
	        taskTypeList = checkHandler.getTaskTypeList();
        }
        catch (Exception e)
        {
	        e.printStackTrace();
        }
		return taskTypeList;
	}
	
	/**
	 * 检测模块权限
	 * @author lihf
	 * @date 2016年9月2日	上午9:31:06
	 * @param taskType
	 * @return
	 */
	@RequestMapping("checkModuleAuth")
	@ResponseBody
	public Json checkModuleAuth(String taskType)
	{
		Json json = new Json();
		try
        {
			LicenseParam licenseParam = new LicenseParam();
			licenseParam.setTaskType(taskType);
	        checkHandler.checkModuleAuth(licenseParam);
	        json.setSuccess(true);
        }
        catch (Exception e)
        {
	        json.setMsg(e.getMessage());
        }
		return json;
	}
	
	/**
	 * 跳转到日志查看页面
	 * @param taskId
	 * @param rq
	 * @return
	 */
	@RequestMapping("gotoConsolePage")
	public String gotoConsolePage(String taskId, HttpServletRequest rq){
		rq.setAttribute("taskId", taskId);
		rq.setAttribute("taskState", rq.getParameter("taskState"));
		return Constant.SUPERSPIDER+"/task/consoleLog";
		
	}
	
	@RequestMapping("getConsoleLog")
	@ResponseBody
	public List<String> getConsoleLog(String taskId, HttpServletRequest rq){
		Task task = taskService.get(Task.class, taskId);
		List<String> logs = new ArrayList<String>();
		int logs_size = 500;
		
		String taskType = task.getTaskType();
		TaskSpiderBean taskSpiderBean = TaskManager.getInstance().get(taskId);
		if(taskSpiderBean == null)
			return logs;
		SpiderListener listener = taskSpiderBean.getSpider().getListener();
		if(Constant.DB.equals(taskType)){
			
			DbSpiderListener dbListener = (DbSpiderListener) listener;
			while(logs.size() <= logs_size)
			{
				 if(dbListener.logs.size() == 0){
					 return logs;
				 }
				 logs.add(dbListener.logs.poll());
			}
			return logs;
			
		} else if(Constant.WEB.equals(taskType)){
			WebSpiderListener webListener = (WebSpiderListener) listener;
			while(logs.size() <= logs_size)
			{
				 if(webListener.logs.size() == 0){
					 return logs;
				 }
				 logs.add(webListener.logs.poll());
			}
			return logs;
		} else if(Constant.FTP.equals(taskType)){
			
			FtpSpiderListener ftpListener = (FtpSpiderListener) listener;
			while(logs.size() <= logs_size)
			{
				 if(ftpListener.logs.size() == 0){
					 return logs;
				 }
				 logs.add(ftpListener.logs.poll());
			}
			return logs;
		} else {
			FileSpiderListener fileListener = (FileSpiderListener) listener;
			while(logs.size() <= logs_size)
			{
				 if(fileListener.logs.size() == 0){
					 return logs;
				 }
				 logs.add(fileListener.logs.poll());
			}
			return logs;
		}
	}
	
	/**
	 * 获取所有的任务列表
	 * @author lihf
	 * @date 2016年9月23日	下午1:59:08
	 * @param request
	 * @return
	 */
	@RequestMapping("/getTaskListAll")
	@ResponseBody
	public List<Task> getTaskListAll(HttpServletRequest request)
	{
		List<Task> taskList = null;
		try
		{
			taskList = taskService.getAll();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return taskList;
	}
	
	/**
	 * 复制任务
	 * @author lihf
	 * @date 2016年10月14日	下午2:04:54
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("copyTask")
	@ResponseBody
	public Json copyTask(HttpServletRequest request, HttpServletResponse response,HttpSession session) throws Exception
	{
		User user = (User) session.getAttribute("user");
		String[] idsArray = request.getParameter("ids").split(",");
		Json json = new Json();
		List<TaskExportImportModel> taskExportImportModelList = new ArrayList<TaskExportImportModel>();
		try
        {
	        for (String id : idsArray)
	        {
		        String taskId = id;
		        Task task = this.get(taskId);
		        if (task.getTaskType().equalsIgnoreCase("web"))
		        {
			        TaskExportImportModel webTaskExportImportModel = webService.exportTask(taskId);
			        taskExportImportModelList.add(webTaskExportImportModel);
		        }
		        else if (task.getTaskType().equalsIgnoreCase("db"))
		        {
			        TaskExportImportModel dbTaskExportImportModel = dbService.exportTask(taskId);
			        taskExportImportModelList.add(dbTaskExportImportModel);
		        }
		        else if (task.getTaskType().equalsIgnoreCase("ftp"))
		        {
			        TaskExportImportModel dbTaskExportImportModel = ftpService.exportTask(taskId);
			        taskExportImportModelList.add(dbTaskExportImportModel);
		        }
		        else if (task.getTaskType().equalsIgnoreCase("file"))
		        {
			        TaskExportImportModel dbTaskExportImportModel = fileService.exportTask(taskId);
			        taskExportImportModelList.add(dbTaskExportImportModel);
		        }
	        }
	        
	        
	        /**
	         * 准备复制任务
	         */
	        
			//查询注册文件中各组件能创建最大的任务数量
			int maxDBJobs = checkHandler.getMaxJobs("db");
			int maxWebJobs = checkHandler.getMaxJobs("web");
			int maxFtpJobs = checkHandler.getMaxJobs("ftp");
			int maxFileJobs = checkHandler.getMaxJobs("file");
			
			
			//查询数据库中任务数量
			LicenseParam licenseParam = new LicenseParam();
			licenseParam.setTaskType("db");
			int dbCount = checkHandler.getTaskCountByTaskType(licenseParam);
			licenseParam.setTaskType("web");
			int webCount = checkHandler.getTaskCountByTaskType(licenseParam);
			licenseParam.setTaskType("ftp");
			int ftpCount = checkHandler.getTaskCountByTaskType(licenseParam);
			licenseParam.setTaskType("file");
			int fileCount = checkHandler.getTaskCountByTaskType(licenseParam);
			
			//定义能增加的数量
			int db_append_count = 0;
			int web_append_count = 0;
			int ftp_append_count = 0;
			int file_append_count = 0;
			
			
			//定义导入的任务的数量
			int db_task_count = 0;
			int web_task_count = 0;
			int ftp_task_count = 0;
			int file_task_count = 0;
			
			
			for (TaskExportImportModel t : taskExportImportModelList)
			{
				Task task = t.getTask();
				task.setTaskCreator(user.getLoginName());
				task.setStartCount(0);
//				Task taskExists = taskService.getTaskById(task.getTaskId());

				//任务已经存在，需要覆盖的任务，任务不存在，则需要追加进去
				if ("web".equalsIgnoreCase(task.getTaskType()))
				{
					web_task_count +=1;
					// 如果最大值大于
					if (maxWebJobs >= (webCount + web_append_count + 1))
					{
						web_append_count += 1;
					}
					else
					{
						continue;
					}

					webService.copyTask(t);
				}
				else if ("db".equalsIgnoreCase(task.getTaskType()))
				{
					db_task_count +=1;
					// 如果最大值大于
					if (maxDBJobs >= (dbCount + db_append_count + 1))
					{
						db_append_count += 1;
					}
					else
					{
						continue;
					}
					dbService.copyTask(t);
				}
				else if ("ftp".equalsIgnoreCase(task.getTaskType()))
				{
					ftp_task_count +=1;
					// 如果最大值大于
					if (maxFtpJobs >= (ftpCount + ftp_append_count + 1))
					{
						ftp_append_count += 1;
					}
					else
					{
						continue;
					}
					ftpService.copyTask(t);
				}
				else if ("file".equalsIgnoreCase(task.getTaskType()))
				{
					file_task_count +=1;
					// 如果最大值大于
					if (maxFileJobs >= (fileCount + file_append_count + 1))
					{
						file_append_count += 1;
					}
					else
					{
						continue;
					}
					fileService.copyTask(t);
				}

			}
			String msg = "";
			StringBuffer sb = new StringBuffer();
			sb.append("任务复制结果：");
			sb.append("@");
			int index=0;
			if(db_task_count>0)
			{
				index +=1;
				sb.append(index).append("、").append("需要复制的数据库采集任务数：").append(db_task_count).append("，").append("实际复制数：").append(db_append_count);
				sb.append("@");
			}
			if(web_task_count>0)
			{
				index +=1;
				sb.append(index).append("、").append("需要复制的WEB采集任务数：").append(web_task_count).append("，").append("实际复制数：").append(web_append_count);
				sb.append("@");
			}
			if(ftp_task_count>0)
			{
				index +=1;
				sb.append(index).append("、").append("需要复制的FTP采集任务数：").append(ftp_task_count).append("，").append("实际复制数：").append(ftp_append_count);
				sb.append("@");
			}
			if(file_task_count>0)
			{
				index +=1;
				sb.append(index).append("、").append("需要复制的文件采集任务数：").append(file_task_count).append("，").append("实际复制数：").append(file_append_count);
				sb.append("@");
			}
			sb.append("@");
			sb.append("未全部复制的情况，请确认License数满足要求！");
			msg = sb.toString();
			json.setSuccess(true);
			json.setMsg(msg);
        }
        catch (Exception e)
        {
	        json.setMsg(e.getMessage());
        }
		return json;
	}
	
	/**
	 * 检测任务名称是否存在
	 * @author lihf
	 * @date 2016年11月17日	上午10:20:22
	 * @param taskId
	 * @param taskName
	 * @return
	 */
	@RequestMapping("checkTaskNameIsExists")
	@ResponseBody
	public Json checkTaskNameIsExists(String taskId,String taskName)
	{
		Json json = new Json();
		try
        {
	        boolean flag = taskService.checkTaskNameIsExists(taskId, taskName);
	        if(flag)
	        {
	        	json.setMsg("该任务名称已经存在！");
	        }
	        else
	        {
	        	json.setSuccess(true);
	        }
        }
        catch (Exception e)
        {
        	json.setMsg(e.getMessage());
        }
		return json;
	}
}
