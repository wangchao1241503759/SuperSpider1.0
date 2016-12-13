/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年7月5日
 */
package cn.com.infcn.superspider.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.infcn.ade.common.controller.BaseController;
import cn.com.infcn.superspider.model.TFtpSource;
import cn.com.infcn.superspider.model.Task;
import cn.com.infcn.superspider.model.TaskOutput;
import cn.com.infcn.superspider.pagemodel.FileContentMappingAttribute;
import cn.com.infcn.superspider.pagemodel.FtpConfig;
import cn.com.infcn.superspider.pagemodel.Json;
import cn.com.infcn.superspider.pagemodel.Node;
import cn.com.infcn.superspider.service.FtpServiceI;
import cn.com.infcn.superspider.service.FtpSourceServiceI;
import cn.com.infcn.superspider.service.TaskOutputServiceI;
import cn.com.infcn.superspider.service.TaskServiceI;
import cn.com.infcn.superspider.service.impl.FileContentMappingServiceImpl;
import cn.com.infcn.superspider.utils.StringUtil;
import cn.com.infcn.superspider.utils.WebUtil;

import com.alibaba.fastjson.JSONArray;
import com.justme.superspider.plugin.ftp.util.FTPUtil;

/**
 * FTP控制管理类
 * @author lihf
 * @date 2016年7月5日
 */
@Controller
@RequestMapping("ftpController")
public class FtpController extends BaseController
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FtpController.class);
	
	@Autowired
	private TaskServiceI taskService;
	
	@Autowired
	private FtpSourceServiceI ftpSourceService;
	
	@Autowired
	private TaskOutputServiceI taskOutputService;

	@Autowired
	private FtpServiceI ftpService;
	
	@Autowired
	private FileContentMappingServiceImpl fileContentMappingService;
	/**
	 * 跳转到添加页面
	 * @author lihf
	 * @date 2016年7月5日	上午11:29:23
	 * @param request
	 * @param taskType
	 * @param model
	 * @return
	 */
	@RequestMapping("addPage")
	public String addPage(HttpServletRequest request,String taskType,Model model)
	{
		Task task = new Task();
		task.setTaskType("ftp");
		model.addAttribute("task",task);
		request.setAttribute("taskType", taskType);
		return "/superspider/ftp/ftp_add";
	}
	
	/**
	 * 添加FTP任务
	 * @author lihf
	 * @date 2016年7月12日	下午4:57:31
	 * @param ftpConfig
	 * @return
	 */
	@RequestMapping("add")
	@ResponseBody
	public Json add(FtpConfig ftpConfig)
	{
		Json json = new Json();
		try
        {
			ftpConfig.setFileattrJson(WebUtil.getHtmlDecode(ftpConfig.getFileattrJson()));
	        ftpService.add(ftpConfig);
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
	 * @date 2016年7月12日	下午6:56:41
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
            TFtpSource ftpSource= ftpSourceService.getFtpSourceByTaskId(taskId);
            TaskOutput taskOutput = taskOutputService.getByTaskId(taskId);
            List<FileContentMappingAttribute> attributeList = fileContentMappingService.findFormatContentMappingByTaskId(taskId);
	        request.setAttribute("task", task);
	        request.setAttribute("ftpSource", ftpSource);
	        request.setAttribute("taskOutput", taskOutput);
	        request.setAttribute("taskType", task.getTaskType());
			model.addAttribute("attributeList",JSONArray.toJSONString(attributeList));
        }
        catch (Exception e)
        {
        	 logger.error(e.getMessage(), e);
        }
		return "/superspider/ftp/ftp_add";
	}
	
	/**
	 * 测试连接
	 * @author lihf
	 * @date 2016年7月25日	下午2:25:16
	 * @param request
	 * @return
	 */
	@RequestMapping("testConnection")
	@ResponseBody
	public Json testConnection(FtpConfig ftpConfig)
	{
		Json json = new Json();
		try
        {
			String url= ftpConfig.getFtpIP();
			int port = Integer.parseInt(ftpConfig.getFtpPort());
			String username = ftpConfig.getFtpUserName();
			String password = ftpConfig.getFtpPassword();
			
			if(url.startsWith("ftp://"))
			{
				url = url.replaceAll("ftp://", "");
			}
			
			if(StringUtil.isEmpty(username))
			{
				username = "anonymous";
			}
			
	        FTPClient ftp = new FTPClient();
	        ftp.connect(url, port);// 连接FTP服务器
	        // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
	        boolean loginFlag = ftp.login(username, password);// 登录
	        json.setSuccess(loginFlag);
        }
        catch (Exception e)
        {
	        json.setMsg(e.getMessage());
        }
		return json;
	}
	
	@RequestMapping("preview")
	public String preview(HttpServletRequest request,String taskType,Model model)
	{
		return "/superspider/ftp/ftp_preview";
	}
	
	/**
	 * 动态获取FTP树节点
	 * @author lihf
	 * @date 2016年8月26日	下午2:43:25
	 * @param id
	 * @return
	 */
	@RequestMapping("getTree")
	@ResponseBody
	public List<Node> getTreeJsonData(FtpConfig ftpConfig,String id)
	{
		List<Node> nodeList = new ArrayList<Node>();
		FTPUtil ftpUtil = null;
		try
        {
			String url= ftpConfig.getFtpIP();
			int port = Integer.parseInt(ftpConfig.getFtpPort());
			String username = ftpConfig.getFtpUserName();
			String password = ftpConfig.getFtpPassword();
			String charSet = ftpConfig.getCharset();
			
			if(url.startsWith("ftp://"))
			{
				url = url.replaceAll("ftp://", "");
			}
			
			if(StringUtil.isEmpty(username))
			{
				username = "anonymous";
			}
	        ftpUtil = new FTPUtil(url, port, username, password);
	        ftpUtil.setCharset(charSet);
	        ftpUtil.connectAndLogin();
	        ftpUtil.client.setFileTransferMode(FTPClient.PASSIVE_REMOTE_DATA_CONNECTION_MODE);
	        String pathname = id;
	        
	        FTPFile[] ftpFileList = ftpUtil.findFiles(pathname);
	        for (FTPFile ftpFile : ftpFileList)
	        {
		        Node node = new Node();
		        if (id.equals("/"))
		        {
		        	   node.setId(pathname + ftpFile.getName());
		        }
		        else
		        {
		        	   node.setId(pathname +"/"+ ftpFile.getName());
		        }
		        node.setText(ftpFile.getName());
		        if (ftpFile.isDirectory())
		        {
			        node.setState("closed");
		        }
		        nodeList.add(node);
	        }
        }
        catch (Exception e)
        {
	        e.printStackTrace();
        }
		finally
		{
			if(ftpUtil!=null)
			{
				
				ftpUtil.logoutAndDisconnect();
			}
		}
		return nodeList;
	}
}
