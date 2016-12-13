/**
 * 
 */
package cn.com.infcn.superspider.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.infcn.ade.common.controller.BaseController;
import cn.com.infcn.superspider.common.Constant;
import cn.com.infcn.superspider.model.DbSource;
import cn.com.infcn.superspider.model.FieldMapping;
import cn.com.infcn.superspider.model.Task;
import cn.com.infcn.superspider.model.TaskOutput;
import cn.com.infcn.superspider.model.Trigger;
import cn.com.infcn.superspider.pagemodel.DbConfig;
import cn.com.infcn.superspider.pagemodel.FileContentMappingAttribute;
import cn.com.infcn.superspider.pagemodel.Json;
import cn.com.infcn.superspider.service.impl.DbFieldMappingServiceImpl;
import cn.com.infcn.superspider.service.impl.DbServiceImpl;
import cn.com.infcn.superspider.service.impl.DbSourceServiceImpl;
import cn.com.infcn.superspider.service.impl.FileAttributeServiceImpl;
import cn.com.infcn.superspider.service.impl.FileContentMappingServiceImpl;
import cn.com.infcn.superspider.service.impl.PluginServiceImpl;
import cn.com.infcn.superspider.service.impl.PointServiceImpl;
import cn.com.infcn.superspider.service.impl.TaskOutputServiceImpl;
import cn.com.infcn.superspider.service.impl.TaskServiceImpl;
import cn.com.infcn.superspider.service.impl.TriggerServiceImpl;
import cn.com.infcn.superspider.utils.DbUtil;
import cn.com.infcn.superspider.utils.StringUtil;
import cn.com.infcn.superspider.utils.WebUtil;

import com.alibaba.fastjson.JSONArray;

/**
 * @description 
 * @author WChao
 * @date   2015年12月21日 	下午5:30:15
 */
@Controller
@RequestMapping("db")
public class DbController extends BaseController{
	
	@Autowired
	private DbServiceImpl dbService;
	@Autowired
	private TaskServiceImpl taskService;
	@Autowired
	private DbSourceServiceImpl dbSourceService;
	@Autowired
	private TaskOutputServiceImpl taskOutputService;
	@Autowired
	private DbFieldMappingServiceImpl fieldMappingService;
	@Autowired
	private PluginServiceImpl pluginService;
	@Autowired
	private PointServiceImpl pointService;
	@Autowired
	private FileAttributeServiceImpl fileAttributeService;
	@Autowired
	private FileContentMappingServiceImpl fileContentMappingService;
	@Autowired
	private TriggerServiceImpl triggerService;
	
	/**
	 * Db添加页面
	 */
	@RequestMapping(value="add",method = RequestMethod.GET)
	public String add(HttpServletRequest request,Model model) {
		Task task = new Task();
		task.setTaskType("db");
		model.addAttribute("task",task);
		request.setAttribute("taskTabTitle",  request.getParameter("taskTabTitle"));
		request.setAttribute("db", request.getParameter("db"));
		request.setAttribute("web", request.getParameter("web"));
		request.setAttribute("ftp", request.getParameter("ftp"));
		request.setAttribute("file", request.getParameter("file"));
		request.setAttribute("taskType", request.getParameter("taskType"));
		return Constant.SUPERSPIDER+"/db/db_add";
	}
	/**
	 * 数据源设置
	 * @return
	 */
	@RequestMapping(value="source",method = RequestMethod.GET)
	public String source() {
		
		return Constant.SUPERSPIDER+"/db/db_source";
	}
	/**
	 * 获取所有的数据库表;
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getAllTables",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getAllTables(DbSource dbSource)throws Exception{
		Map<String,Object> tablesInfo = new HashMap<String, Object>();
		tablesInfo.put("fieldtypes",DbUtil.getTypes(dbSource.getDsType()));
		tablesInfo.put("tablenames", dbService.getAllTables(dbSource));
		return tablesInfo;
	}
	/**
	 * 获取所有的文件属性;
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getAllFileAttributes",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getAllFileAttribute(Task task)
	{
		Map<String,Object> fileAttributeInfo = new HashMap<String,Object>();
		fileAttributeInfo.put("default", fileAttributeService.findAttributeByDefault("1"));
		fileAttributeInfo.put("contentAttributes", fileContentMappingService.findContentMappingByTaskId(task.getTaskId()));
		
		return fileAttributeInfo;
	}
	/**
	 * 获取字段映射json
	 */
	@RequestMapping(value="getAllColumns",method = RequestMethod.POST)
	@ResponseBody
	public List<FieldMapping> getAllColumns(DbSource dbSource,HttpServletRequest request)throws Exception{
		
		return dbService.getAllColumns(dbSource);
	}
	
	/**
	 * 获取字段映射json
	 */
	@RequestMapping("/getAllColumnsBySQL")
	@ResponseBody
	public Json getAllColumnsBySQL(DbSource dbSource,HttpServletRequest request){
		Json json = new Json();
		List<FieldMapping> fieldList = new ArrayList<FieldMapping>();
		try
		{
			fieldList = dbService.getAllColumnsBySQL(dbSource);
			json.setSuccess(true);
			json.setObj(fieldList);
			json.setMsg("测试连接通过！");
		}
		catch (Exception e)
		{
			json.setMsg(e.getMessage());
		}
		return json;
	}
	
	/**
	 * 数据采集配置保存;
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="save",method = RequestMethod.POST)
	@ResponseBody
	public Json doSave(DbConfig dbConfig){
		Json json = new Json();
		try
        {
			dbConfig.setDsSql(dbConfig.getDsSql().replaceAll("&quot;", "\"").replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
			dbConfig.setFieldMappingData(WebUtil.getHtmlDecode(dbConfig.getFieldMappingData()));
			dbConfig.setAttributeMappingData(WebUtil.getHtmlDecode(dbConfig.getAttributeMappingData()));
	        dbService.saveDb(dbConfig);
	        json.setSuccess(true);
	        json.setMsg("添加成功！");
        }
        catch (Exception e)
        {
	        json.setMsg(e.getMessage());
        }
		return json;
	}
	/**
	 * 数据采集配置保存;
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="edit",method = RequestMethod.GET)
	public String edit(HttpServletRequest request,String taskId,Model model) throws Exception{
		Task task = taskService.get(taskId);
		DbSource dbSource = dbSourceService.getByTaskId(taskId);
		TaskOutput taskOutput = taskOutputService.getByTaskId(taskId);
		List<FieldMapping> fieldList = fieldMappingService.findByTaskId(taskId);
		List<FileContentMappingAttribute> attributeList = fileContentMappingService.findFormatContentMappingByTaskId(taskId);
		Trigger trigger = triggerService.getTriggerByTask(taskId);
		model.addAttribute("task",task);
		model.addAttribute("dbSource",dbSource);
		model.addAttribute("taskOutput",taskOutput);
		model.addAttribute("fieldList",JSONArray.toJSONString(fieldList).replaceAll("'","&apos;"));
		model.addAttribute("attributeList",JSONArray.toJSONString(attributeList));
		model.addAttribute("trigger",trigger);
		request.setAttribute("taskTabTitle",  request.getParameter("taskTabTitle"));
		request.setAttribute("db", request.getParameter("db"));
		request.setAttribute("web", request.getParameter("web"));
		request.setAttribute("ftp", request.getParameter("ftp"));
		request.setAttribute("file", request.getParameter("file"));
        request.setAttribute("taskType", task.getTaskType());
		return Constant.SUPERSPIDER+"/db/db_add";
	}
	public static void main(String[] args) {
		for(int i = 0;i<30;i++){
			System.out.println(StringUtil.generateUUID());
		}
	}
}
