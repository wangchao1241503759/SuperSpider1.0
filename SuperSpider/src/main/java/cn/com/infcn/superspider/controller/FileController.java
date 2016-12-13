/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年7月13日
 */
package cn.com.infcn.superspider.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.infcn.ade.common.controller.BaseController;
import cn.com.infcn.superspider.model.TFileSource;
import cn.com.infcn.superspider.model.Task;
import cn.com.infcn.superspider.model.TaskOutput;
import cn.com.infcn.superspider.pagemodel.FileConfig;
import cn.com.infcn.superspider.pagemodel.FileContentMappingAttribute;
import cn.com.infcn.superspider.pagemodel.Json;
import cn.com.infcn.superspider.service.FileServiceI;
import cn.com.infcn.superspider.service.FileSourceServiceI;
import cn.com.infcn.superspider.service.TaskOutputServiceI;
import cn.com.infcn.superspider.service.TaskServiceI;
import cn.com.infcn.superspider.service.impl.FileContentMappingServiceImpl;
import cn.com.infcn.superspider.utils.WebUtil;

import com.alibaba.fastjson.JSONArray;

/**
 * @author lihf
 * @date 2016年7月13日
 */
@Controller
@RequestMapping("fileController")
public class FileController extends BaseController
{
	private static final Logger logger = Logger.getLogger(FileController.class);
	
	@Autowired
	private TaskServiceI taskService;
	
	@Autowired
	private FileSourceServiceI fileSourceService;
	
	@Autowired
	private TaskOutputServiceI taskOutputService;

	@Autowired
	private FileServiceI fileService;
	
	@Autowired
	private FileContentMappingServiceImpl fileContentMappingService;

	/**
	 * 跳转到添加页面
	 * @author lihf
	 * @date 2016年7月13日	上午10:54:27
	 * @param request
	 * @param taskType
	 * @param model
	 * @return
	 */
	@RequestMapping("addPage")
	public String addPage(HttpServletRequest request,String taskType,Model model)
	{
		Task task = new Task();
		task.setTaskType("file");
		model.addAttribute("task",task);
		request.setAttribute("taskType", taskType);
		return "/superspider/file/file_add";
	}
	
	/**
	 * 添加本地文件采集任务
	 * @author lihf
	 * @date 2016年7月13日	下午4:24:15
	 * @param fileConfig
	 * @return
	 */
	@RequestMapping("add")
	@ResponseBody
	public Json add(FileConfig fileConfig)
	{
		Json json = new Json();
		try
        {
			fileConfig.setFileattrJson(WebUtil.getHtmlDecode(fileConfig.getFileattrJson()));
	        fileService.add(fileConfig);
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
	 * 跳转到修改页面
	 * @author lihf
	 * @date 2016年7月13日	下午4:24:29
	 * @param request
	 * @param taskId
	 * @return
	 */
	@RequestMapping("editPage")
	public String editPage(HttpServletRequest request,String taskId,Model model)
	{
        try
        {
            Task task = taskService.getTaskById(taskId);
            TFileSource fileSource= fileSourceService.getFileSourceByTaskId(taskId);
            TaskOutput taskOutput = taskOutputService.getByTaskId(taskId);
            List<FileContentMappingAttribute> attributeList = fileContentMappingService.findFormatContentMappingByTaskId(taskId);
            
	        request.setAttribute("task", task);
	        request.setAttribute("fileSource", fileSource);
	        request.setAttribute("taskOutput", taskOutput);
	        request.setAttribute("taskType", task.getTaskType());
	        model.addAttribute("attributeList",JSONArray.toJSONString(attributeList));
        }
        catch (Exception e)
        {
        	 logger.error(e.getMessage(), e);
        }
		return "/superspider/file/file_add";
	}
}
