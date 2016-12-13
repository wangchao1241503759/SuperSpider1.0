/**
 * 
 */
package cn.com.infcn.superspider.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.infcn.ade.common.controller.BaseController;
import cn.com.infcn.superspider.model.FileAttribute;
import cn.com.infcn.superspider.model.TaskInfo;
import cn.com.infcn.superspider.service.ConfigService;

/**
 * @author zyz
 *
 */
@Controller
@RequestMapping("spider/config")
public class ConfigController extends BaseController {
	
	@Autowired
	private ConfigService configService;
	
	/**
	 * 日志管理页面
	 */
	@RequestMapping(value="log",method = RequestMethod.GET)
	public String logManage() {
		return "spider/logManage";
	}
	
	/**
	 * 任务统计页面
	 */
	@RequestMapping(value="task/stat",method = RequestMethod.GET)
	public String taskStat() {
		return "spider/taskStat";
	}
	
	/**
	 * 任务监控页面
	 */
	@RequestMapping(value="task/monitor",method = RequestMethod.GET)
	public String taskMonitor() {
		return "spider/taskMonitor";
	}

	/**
	 * 任务管理页面
	 */
	@RequestMapping(value="task",method = RequestMethod.GET)
	public String taskManage() {
		return "spider/taskManage";
	}
	
	/**
	 * 计划管理页面
	 */
	@RequestMapping(value="plan/manage",method = RequestMethod.GET)
	public String planManage() {
		return "spider/planManage";
	}
	
	/**
	 * db配置页面
	 */
	@RequestMapping(value="db",method = RequestMethod.GET)
	public String dbConfig() {
		return "spider/dbConfig";
	}
	
	/**
	 * ftp配置页面
	 */
	@RequestMapping(value="ftp",method = RequestMethod.GET)
	public String ftpConfig() {
		return "spider/ftpConfig";
	}
	
	/**
	 * file配置页面
	 */
	@RequestMapping(value="file",method = RequestMethod.GET)
	public String fileConfig() {
		return "spider/fileConfig";
	}
	
	/**
	 * http配置页面
	 */
	@RequestMapping(value="http",method = RequestMethod.GET)
	public String httpConfig() {
		return "spider/httpConfig";
	}
	
	/**
	 * taskplan
	 */
	@RequestMapping(value="plan",method = RequestMethod.GET)
	public String cron() {
		return "spider/taskPlan";
	}
	
	/**
	 * taskinfo
	 */
	@RequestMapping(value="taskinfo",method = RequestMethod.GET)
	public String taskInfo() {
		return "spider/taskInfo";
	}
	
	/**
	 * taskoutput
	 */
	@RequestMapping(value="taskoutput",method = RequestMethod.GET)
	public String taskOutput() {
		return "spider/taskOutput";
	}
	
	/**
	 * dbregex
	 */
	@RequestMapping(value="db/regex",method = RequestMethod.GET)
	public String dbRegex() {
		return "spider/dbRegex";
	}
	
	/**
	 * dbsource
	 */
	@RequestMapping(value="db/source",method = RequestMethod.GET)
	public String dbSource() {
		return "spider/dbSource";
	}
	
	/**
	 * fileregex
	 */
	@RequestMapping(value="file/regex",method = RequestMethod.GET)
	public String fileRegex() {
		return "spider/fileRegex";
	}
	
	/**
	 * filesource
	 */
	@RequestMapping(value="file/source",method = RequestMethod.GET)
	public String fileSource() {
		return "spider/fileSource";
	}
	
	/**
	 * ftpregex
	 */
	@RequestMapping(value="ftp/regex",method = RequestMethod.GET)
	public String ftpRegex() {
		return "spider/ftpRegex";
	}
	
	/**
	 * ftpsource
	 */
	@RequestMapping(value="ftp/source",method = RequestMethod.GET)
	public String ftpSource() {
		return "spider/ftpSource";
	}
	
	/**
	 * httpregex
	 */
	@RequestMapping(value="http/regex",method = RequestMethod.GET)
	public String httpRegex() {
		return "spider/httpRegex";
	}
	
	/**
	 * httpsource
	 */
	@RequestMapping(value="http/source",method = RequestMethod.GET)
	public String httpSource() {
		return "spider/httpSource";
	}
	
	/**
	 * regex配置页面
	 */
	@RequestMapping(value="http/regexconfig",method = RequestMethod.GET)
	public String regexconfig() {
		return "spider/regexConfig";
	}
	
	/**
	 * proxy
	 */
	@RequestMapping(value="proxy",method = RequestMethod.GET)
	public String proxy() {
		return "spider/proxy";
	}
	
	/**
	 * taskjson
	 */
	@RequestMapping(value="task/json",method = RequestMethod.GET)
	@ResponseBody
	public List<TaskInfo> taskJson() {
		return configService.getTaskList();
	}
	
	/**
	 * fileattrjson
	 */
	@RequestMapping(value="fileattr/json",method = RequestMethod.GET)
	@ResponseBody
	public List<FileAttribute> fileAttributeJson() {
		return configService.getFileAttribute();
	}
}
