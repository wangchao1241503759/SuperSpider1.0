/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年4月6日
 */
package cn.com.infcn.superspider.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.infcn.ade.common.controller.BaseController;
import cn.com.infcn.reg.product.utils.CommonTools;
import cn.com.infcn.superspider.common.Constant;
import cn.com.infcn.superspider.common.RegexEnum;
import cn.com.infcn.superspider.model.FieldMapping;
import cn.com.infcn.superspider.model.TWebSource;
import cn.com.infcn.superspider.model.Task;
import cn.com.infcn.superspider.model.TaskOutput;
import cn.com.infcn.superspider.pagemodel.EasyUIDataGrid;
import cn.com.infcn.superspider.pagemodel.Json;
import cn.com.infcn.superspider.pagemodel.RegexParam;
import cn.com.infcn.superspider.pagemodel.WebConfig;
import cn.com.infcn.superspider.pagemodel.WebDownloader;
import cn.com.infcn.superspider.pagemodel.WebParamSetting;
import cn.com.infcn.superspider.pagemodel.WebTypeRule;
import cn.com.infcn.superspider.service.ProxyServerServiceI;
import cn.com.infcn.superspider.service.TaskOutputServiceI;
import cn.com.infcn.superspider.service.TaskServiceI;
import cn.com.infcn.superspider.service.WebDownloaderServiceI;
import cn.com.infcn.superspider.service.WebFieldExtractServiceI;
import cn.com.infcn.superspider.service.WebParamSettingServiceI;
import cn.com.infcn.superspider.service.WebServiceI;
import cn.com.infcn.superspider.service.WebSourceServiceI;
import cn.com.infcn.superspider.service.WebTypeRuleServiceI;
import cn.com.infcn.superspider.service.impl.DbFieldMappingServiceImpl;
import cn.com.infcn.superspider.utils.ConfigUtil;
import cn.com.infcn.superspider.utils.StringUtil;
import cn.com.infcn.superspider.utils.WebUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.justme.superspider.util.RegexUtil;

/**
 * Web控制类
 * @author lihf
 * @date 2016年4月6日
 */
@Controller
@RequestMapping("webController")
@SuppressWarnings({"rawtypes"})
public class WebController extends BaseController
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(WebController.class);

	@Autowired 
	private WebServiceI webService;
	
	@Autowired
	private TaskServiceI taskService;
	
	@Autowired
	private WebSourceServiceI webSourceService;
	
	@Autowired
	private TaskOutputServiceI taskOutputService;
	
	@Autowired
	private ProxyServerServiceI proxyServerService;
	
	@Autowired
	private WebTypeRuleServiceI webTypeRuleService;
	
	@Autowired
	private WebParamSettingServiceI webParamSettingService;
	
	@Autowired
	private WebFieldExtractServiceI webFieldExtractService;
	
	@Autowired
	private DbFieldMappingServiceImpl fieldMappingService;
	
	@Autowired
	private WebDownloaderServiceI webDownloaderService;
	/**
	 * 
	 * @author lihf
	 * @date 2016年4月6日	上午11:08:55
	 * @return
	 */
	@RequestMapping("addPage")
	public String addPage(HttpServletRequest request,String taskType,Model model)
	{
		Task task = new Task();
		task.setTaskType("web");
		model.addAttribute("task",task);
		request.setAttribute("taskType", taskType);
		return "/superspider/web/web_add";
	}
	
	/**
	 * 添加web任务
	 * @author lihf
	 * @date 2016年4月7日	下午5:30:31
	 * @param webConfig
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public Json add(WebConfig webConfig)
	{
		Json json = new Json();
		try
        {
//			webConfig.setParamObjectListJson(webConfig.getParamObjectListJson().replaceAll("&quot;", "\""));
//			webConfig.setWebTypeRuleJson(webConfig.getWebTypeRuleJson().replaceAll("&quot;", "\""));
//			webConfig.setFieldMappingJson(webConfig.getFieldMappingJson().replaceAll("&quot;", "\""));
			webConfig.setParamObjectListJson(WebUtil.replaceHtmlDecode(WebUtil.getHtmlDecode(webConfig.getParamObjectListJson())));
			webConfig.setWebTypeRuleJson(WebUtil.replaceHtmlDecode(webConfig.getWebTypeRuleJson()));
			webConfig.setFieldMappingJson(WebUtil.replaceHtmlDecode(webConfig.getFieldMappingJson()));
	        webService.add(webConfig);
	        json.setSuccess(true);
	        json.setMsg("添加成功！");
        }
        catch (Exception e)
        {
        	json.setMsg(e.getMessage());
        	logger.error(e.getMessage(), e);
        }
		return json;
	}
	
	/**
	 * 
	 * @author lihf
	 * @date 2016年4月6日	上午11:08:55
	 * @return
	 */
	@RequestMapping("editPage")
	public String editPage(HttpServletRequest request,String taskId)
	{
		try
        {
	        Task task = taskService.getTaskById(taskId);
	        TWebSource webSource= webSourceService.getWebSourceByTaskId(taskId);
	        String proxyIds = webSourceService.getProxyServer(webSource.getId());
//	        EasyUIDataGrid dataGridProxyServiceList = new EasyUIDataGrid();
//	        if(!StringUtil.isEmpty(proxyIds))
//	        {
//	        	dataGridProxyServiceList = proxyServerService.getProxyServerList(proxyIds);
//	        }
	        TaskOutput taskOutput = taskOutputService.getByTaskId(taskId);
	        
//	        List<WebTypeRule> webTypeRuleList = webTypeRuleService.getWebTypeRuleListByTaskId(taskId);
	        
//	        List<FieldMapping> fieldMappingList = fieldMappingService.findByTaskId(taskId);
//	        List<WebParamSetting> webParamSettingList = webParamSettingService.getWebParamSettingByTaskId(taskId);
	        
//	        List<WebFieldExtract> fieldExtractList = new ArrayList<WebFieldExtract>();
//	        fieldExtractList = webFieldExtractService.getWebFieldExtractListByTaskId(taskId);
	        
	        request.setAttribute("task", task);
	        request.setAttribute("webSource", webSource);
//	        request.setAttribute("proxyServiceList", dataGridProxyServiceList);
	        request.setAttribute("taskOutput", taskOutput);
	        request.setAttribute("taskType", task.getTaskType());
	        request.setAttribute("proxyIds", proxyIds);
//	        String webTypeRuleListJson = JSONObject.toJSONString(webTypeRuleList);
//	        String fieldMappingListJson = JSONObject.toJSONString(fieldMappingList);
//	        String fieldExtractListJson = JSONObject.toJSONString(fieldExtractList);
//	        String webParamSettingListJson = JSONObject.toJSONString(webParamSettingList);
//	        net.sf.json.JSONArray webParamSettingListJson = net.sf.json.JSONArray.fromObject(webParamSettingList);
//	        request.setAttribute("webTypeRuleListJson", webTypeRuleListJson);
//	        request.setAttribute("fieldMappingListJson", fieldMappingListJson);
//	        request.setAttribute("webParamSettingListJson", webParamSettingListJson);
//	        request.setAttribute("fieldExtractListJson", fieldExtractListJson);
        }
        catch (Exception e)
        {
	        logger.error(e.getMessage(), e);
        }
		return "/superspider/web/web_edit";
	}
	/**
	 * 文件属性提取集合页面;
	 * @return
	 */
	@RequestMapping(value="forRegex",method = RequestMethod.GET)
	public String forFileAttributeList(HttpServletRequest request) {
		
		return Constant.SUPERSPIDER+"/web/web_regex";
	}
	
	@RequestMapping(value="getRegexValue",method = RequestMethod.POST)
	@ResponseBody
	public Json getRegexValue(RegexParam regexParam){
		Json json = new Json();
		json.setObj(produceRegex(regexParam));
		json.setSuccess(true);
        json.setMsg("生成成功！");
        return json;
	}
	
	public String produceRegex(RegexParam regexParam){
		JSONArray regexList = JSONArray.parseArray(WebUtil.getHtmlDecode(regexParam.getRowRegex()));
		RegexUtil resultRegex = new RegexUtil(WebUtil.getHtmlDecode(regexParam.getStartRegex()));
		if(!regexList.isEmpty()){
			RegexUtil nextRegex = new RegexUtil();
			//int repeatIndex = 0;
			for(int i=0 ; i<regexList.size() ; i++){
				JSONObject regexJson = regexList.getJSONObject(i);
				String regexRepeat = RegexEnum.getValue(regexJson.getString("regexRepeat"));
				String regexType = RegexEnum.getValue(regexJson.getString("regexType")).replaceAll("(双引号)",RegexEnum.双引号.getValue());
				if(regexRepeat != null && !"".equals(regexRepeat)){
					/*if(i == 0 || (i-1) == repeatIndex){
						resultRegex.replace(nextRegex.toString(),"");
						nextRegex.replaceAll("[\\[\\]\\*\\?\\+]","");
						nextRegex.or(regexType);
					}else{*/
						if(regexType.contains("[") && regexType.contains("]")){
							nextRegex = new RegexUtil(regexType);
						}else{
							nextRegex = new RegexUtil().or(regexType);
						}
					//}
					if(RegexUtil.zeroOrMore.equals(regexRepeat))
						nextRegex.repeatZeroOrMore();
					else if(RegexUtil.zeroOrOne.equals(regexRepeat))
						nextRegex.repeatZeroOrOne();
					else if(RegexUtil.oneOrMore.equals(regexRepeat))
						nextRegex.repeatOneOrMore();
					else{
						String[] repeatArr = regexRepeat.split(",");
						if(repeatArr.length == 1 && !regexRepeat.contains(",")){
							nextRegex.repeat(Integer.parseInt(repeatArr[0]));//重复num次
						}else if(repeatArr.length == 1){
							nextRegex.repeatMin(Integer.parseInt(repeatArr[0]));//至少重复num次
						}else if(repeatArr.length > 1){
							nextRegex.repeat(Integer.parseInt(repeatArr[0]),Integer.parseInt(repeatArr[1]));//重复min-max次
						}
					}
					resultRegex.append(nextRegex);
					//repeatIndex = i;
				}else{
					if(!RegexEnum.isContain(regexJson.getString("regexType"))){
						regexType = resultRegex.handleExpectChar(regexType);
					}
					resultRegex.append(regexType);
				}
			}
		}
		resultRegex.append(WebUtil.getHtmlDecode(regexParam.getEndRegex()));
		return resultRegex.toString();
	}
	@RequestMapping("/getInitRegexData")
	@ResponseBody
	public Map<String,JSONArray> getInitRegexData(){
		Map<String,JSONArray> regexParamMap = new HashMap<String,JSONArray>();
		List<RegexEnum> regexParamList = RegexEnum.getRegexByType("0");
		List<RegexEnum> regexRepeatList = RegexEnum.getRegexByType("1");
		JSONArray regexParamJsonArray = new JSONArray();
		JSONArray regexRepeatJsonArray = new JSONArray();
		for(RegexEnum regexEnum : regexParamList){
			JSONObject regexJson = new JSONObject();
			regexJson.put("text", regexEnum.name());
			regexJson.put("value", regexEnum.name());
			regexParamJsonArray.add(regexJson);
		}
		for(RegexEnum regexEnum : regexRepeatList){
			JSONObject regexJson = new JSONObject();
			regexJson.put("text", regexEnum.name());
			regexJson.put("value", regexEnum.name());
			regexRepeatJsonArray.add(regexJson);
		}
		regexParamMap.put("param", regexParamJsonArray);
		regexParamMap.put("repeat", regexRepeatJsonArray);
		return regexParamMap;
	}
	
	/**
	 * 获取参数设置
	 * @param request
	 * @param taskId
	 * @return
	 */
	@RequestMapping("/getWebParamSettingList")
	@ResponseBody
	public List<WebParamSetting> getWebParamSettingList(HttpServletRequest request,String taskId)
	{
		List<WebParamSetting> webParamSettingList = null;
		try
        {
	        webParamSettingList = webParamSettingService.getWebParamSettingByTaskId(taskId);
        }
        catch (Exception e)
        {
	        e.printStackTrace();
        }
		return webParamSettingList;
	}
	
	/**
	 * 获取页面规则
	 * @param request
	 * @param taskId
	 * @return
	 */
	@RequestMapping("/getWebTypeRuleList")
	@ResponseBody
	public List<WebTypeRule> getWebTypeRuleList(HttpServletRequest request,String taskId)
	{
		List<WebTypeRule> webTypeRuleList = null;
		try
		{
			webTypeRuleList = webTypeRuleService.getWebTypeRuleListByTaskId(taskId);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return webTypeRuleList;
	}
	
	/**
	 * 获取字段映射
	 * @param request
	 * @param taskId
	 * @return
	 */
	@RequestMapping("/getFieldMappingList")
	@ResponseBody
	public List<FieldMapping> getFieldMappingList(HttpServletRequest request,String taskId)
	{
		List<FieldMapping> fieldMappingList = null;
		try
		{
			fieldMappingList = fieldMappingService.findByTaskId(taskId);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return fieldMappingList;
	}
	
	@RequestMapping("/getWebDownloaderList")
	@ResponseBody
	public List<WebDownloader> getWebDownloaderList(HttpServletRequest request)
	{
		List<WebDownloader> webDownloaderList = null;
		try
		{
			webDownloaderList = webDownloaderService.getWebDownloaderList();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return webDownloaderList;
	}
	
	
	
	/**
	 * 获取已经选择的代理服务
	 * @author lihf
	 * @date 2016年4月14日	下午3:00:19
	 * @param proxyIds
	 * @return
	 */
	@RequestMapping("/getProxyServer")
	@ResponseBody
	public EasyUIDataGrid getProxyServer(String proxyIds)
	{
		EasyUIDataGrid dataGrid = new EasyUIDataGrid();
		try
        {
	        if (!StringUtil.isEmpty(proxyIds))
	        {
		        dataGrid = proxyServerService.getProxyServerList(proxyIds);
	        }
        }
        catch (Exception e)
        {
	        logger.error(e.getMessage(), e);
        }
		return dataGrid;
	}
	
	/**
	 * 修改web任务
	 * @author lihf
	 * @date 2016年4月7日	下午5:30:31
	 * @param webConfig
	 * @return
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(WebConfig webConfig)
	{
		Json json = new Json();
		try
        {
//			webConfig.setParamObjectListJson(webConfig.getParamObjectListJson().replaceAll("&quot;", "\""));
//			webConfig.setWebTypeRuleJson(webConfig.getWebTypeRuleJson().replaceAll("&quot;", "\""));
//			webConfig.setFieldMappingJson(webConfig.getFieldMappingJson().replaceAll("&quot;", "\""));
			webConfig.setParamObjectListJson(WebUtil.replaceHtmlDecode(webConfig.getParamObjectListJson()));
			webConfig.setWebTypeRuleJson(WebUtil.replaceHtmlDecode(webConfig.getWebTypeRuleJson()));
			webConfig.setFieldMappingJson(WebUtil.replaceHtmlDecode(webConfig.getFieldMappingJson()));
	        webService.edit(webConfig);
	        json.setSuccess(true);
	        json.setMsg("修改成功！");
        }
        catch (Exception e)
        {
        	json.setMsg(e.getMessage());
        	logger.error(e.getMessage(), e);
        }
		return json;
	}
	
	@RequestMapping("browserIE")
	public String browserIE(HttpServletRequest request,String taskType)
	{
		return "/superspider/web/browser_ie";
	}
	
	  /**
	   * 下载文件
	   * @author lihf
	   * @date 2015年12月23日	下午3:28:52
	   * @param id
	   * @param response
	   */
	  @RequestMapping("/download")
	  public void download(HttpServletResponse request, HttpServletResponse response) {
	    try {
	      response.setContentType("text/html;charset=UTF-8");
	      request.setCharacterEncoding("UTF-8");
	      BufferedInputStream bis = null;
	      BufferedOutputStream bos = null;
	      File downFile = null;
	      String fileDirectory = CommonTools.getWEBINFPath();
	      String browserWidgetName = ConfigUtil.get("browserWidgetName");
	      StringBuffer sb = new StringBuffer();
	      sb.append(fileDirectory).append(File.separator).append(browserWidgetName);
	      String filePath = sb.toString();
	      downFile = new File(filePath);
	      if(!downFile.exists())
	      {
	    	  throw new Exception("文件不存在！");
	      }
	      long fileLength = downFile.length();
	      response.setContentType("application/octet-stream");
	      String fileName = URLEncoder.encode(downFile.getName(), "UTF-8");
	      response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
	      response.setHeader("Content-Length", String.valueOf(fileLength));
	      bis = new BufferedInputStream(new FileInputStream(downFile));
	      bos = new BufferedOutputStream(response.getOutputStream());
	      byte[] buff = new byte[2048];
	      int bytesRead;
	      while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
	        bos.write(buff, 0, bytesRead);
	        bos.flush();
	      }
	      bis.close();
	      bos.close();
	    } catch (Exception e) {
	    	logger.error(e.getMessage(), e);
	    }
	  }

}
