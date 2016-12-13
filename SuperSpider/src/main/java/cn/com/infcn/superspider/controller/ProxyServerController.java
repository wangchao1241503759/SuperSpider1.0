/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年4月11日
 */
package cn.com.infcn.superspider.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.infcn.ade.common.controller.BaseController;
import cn.com.infcn.superspider.pagemodel.EasyUIDataGrid;
import cn.com.infcn.superspider.pagemodel.Json;
import cn.com.infcn.superspider.pagemodel.ProxyServer;
import cn.com.infcn.superspider.service.ProxyServerServiceI;
import cn.com.infcn.superspider.utils.WebUtil;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lihf
 * @date 2016年4月11日
 */
@Controller
@RequestMapping("proxyServerController")
public class ProxyServerController extends BaseController
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ProxyServerController.class);
	
	@Autowired
	private ProxyServerServiceI proxyServerService;

	/**
	 * 跳转到列表页面
	 * @author lihf
	 * @date 2016年4月11日	上午11:31:24
	 * @return
	 */
	@RequestMapping("/manager")
	public String manager()
	{
		return "/superspider/proxyserver/proxyserver";
	}
	
	/**
	 * 获取代理服务列表
	 * @author lihf
	 * @date 2016年4月11日	下午1:37:23
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("dataGrid")
	@ResponseBody
	public EasyUIDataGrid dataGrid()
	{
		EasyUIDataGrid dataGrid = new EasyUIDataGrid();
		try
        {
			dataGrid = proxyServerService.getProxyServerList();
        }
        catch (Exception e)
        {
	        // TODO: handle exception
        }
		return dataGrid;
	}
	
	/**
	 * 删除代理服务器
	 * @author lihf
	 * @date 2016年4月11日	下午3:49:17
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(String id)
	{
		Json json = new Json();
		try
        {
	        proxyServerService.delete(id);
	        json.setSuccess(true);
	        json.setMsg("删除成功！");
        }
        catch (Exception e)
        {
        	json.setMsg(e.getMessage());
        	logger.error(e.getMessage(), e);
        }
		return json;
	}
	/**
	 * 删除代理服务器
	 * @author lihf
	 * @date 2016年4月11日	下午3:49:17
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteBatch")
	@ResponseBody
	public Json deleteBatch(String ids)
	{
		Json json = new Json();
		try
		{
			proxyServerService.deleteBatch(ids);
			json.setSuccess(true);
			json.setMsg("删除成功！");
		}
		catch (Exception e)
		{
			json.setMsg(e.getMessage());
			logger.error(e.getMessage(), e);
		}
		return json;
	}
	
	/**
	 * 检测是否能删除代理服务器
	 * @author lihf
	 * @date 2016年4月11日	下午3:49:17
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteCheck")
	@ResponseBody
	public Json deleteCheck(String ids)
	{
		Json json = new Json();
		try
		{
			proxyServerService.deleteCheck(ids);
			json.setSuccess(true);
			json.setMsg("检测通过！");
		}
		catch (Exception e)
		{
			json.setMsg(e.getMessage());
			logger.error(e.getMessage(), e);
		}
		return json;
	}
	
	/**
	 * 跳转到列表页面
	 * @author lihf
	 * @date 2016年4月11日	上午11:31:24
	 * @return
	 */
	@RequestMapping("/proxserverlist")
	public String proxservicelist()
	{
		return "/superspider/proxyserver/proxyserverlist";
	}
	
	
	/**
	 * 添加代理服务器地址
	 * @author lihf
	 * @date 2016年8月8日	上午11:38:45
	 * @return
	 */
	@RequestMapping("addOrEdit")
	@ResponseBody
	public Json addOrEdit(String proxyServerJson)
	{
		Json json = new Json();
		try
        {
			proxyServerJson = WebUtil.getHtmlDecode(proxyServerJson);
//	        List<ProxyServer> proxyServerList = JSONArray.parseArray(proxyServerJson, ProxyServer.class);
			ProxyServer proxyServer = null;
			proxyServer = JSONObject.parseObject(proxyServerJson, ProxyServer.class);
	        
	        proxyServerService.addOrEdit(proxyServer);
	        json.setSuccess(true);
	        json.setMsg("添加成功！");
        }
        catch (Exception e)
        {
        	json.setMsg("添加失败！");
        	logger.error(e.getMessage(), e);
        }
		return json;
	}
}
