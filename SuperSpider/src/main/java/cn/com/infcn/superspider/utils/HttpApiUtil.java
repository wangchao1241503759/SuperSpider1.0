/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年10月11日
 */
package cn.com.infcn.superspider.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.com.infcn.superspider.service.impl.TaskLogServiceImpl;
import cn.com.infcn.superspider.service.impl.TaskServiceImpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.justme.superspider.xml.Site;

/**
 * @author lihf
 * @date 2016年10月11日
 */
public class HttpApiUtil
{

	/**
	 * 获取元数据仓储的表
	 * @author lihf
	 * @date 2016年10月11日	下午4:28:11
	 * @param ip
	 * @param port
	 * @return
	 * @throws Exception
	 */
	public static JSONArray getMssTables(String ip,int port) throws Exception
	{
		HttpConnctionApi api = new HttpConnctionApi(ip, port);
		Map<String, Object> params = new HashMap<String, Object>();
		//获取表
		String jsonStr = api.post("MssTableApi", "state", 10000, params);
		if(StringUtil.isEmpty(jsonStr))
		{
			throw new Exception("没有获取到表信息！");
		}
		JSONObject jsonObj = JSONObject.parseObject(jsonStr);
		JSONObject obj = jsonObj.getJSONObject("obj");
		JSONArray jsonArray = obj.getJSONArray("db");
		
		return jsonArray;
	}
	
	/**
	 * 获取字段名称和类型列表
	 * @author lihf
	 * @date 2016年10月11日	下午5:04:43
	 * @param ip
	 * @param port
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public static Map<String,List<String>> getMssAllColumns(String ip,int port,String tableName) throws Exception
	{
		Map<String, List<String>> columns = new HashMap<String, List<String>>();
		Map<String, Object> params = new HashMap<String, Object>();
		HttpConnctionApi api = new HttpConnctionApi(ip, port);
		params.clear();
		params.put("name", tableName);
		String jsonStr = api.post("MssTableApi", "mapping", 10000, params);

		JSONObject jsonObj = JSONObject.parseObject(jsonStr);
		JSONObject obj = jsonObj.getJSONObject("obj");
		JSONObject jsonProperties = obj.getJSONObject("properties");
		if(jsonProperties != null)
		{
			Set<Entry<String, Object>> set = jsonProperties.entrySet();
			for (Iterator<Entry<String, Object>> iter = set.iterator(); iter.hasNext();)
			{
				Entry<String, Object> entry = iter.next();

				if (columns.get("name") == null)
				{
					columns.put("name", new ArrayList<String>());
				}
				if (columns.get("type") == null)
				{
					columns.put("type", new ArrayList<String>());
				}
				String type = (JSONObject.parseObject(entry.getValue().toString())).getString("type");
				columns.get("type").add(type);
				columns.get("name").add(entry.getKey());
			}
		}
		return columns;
	}
	
	/**
	 * 保存数据
	 * @author lihf
	 * @date 2016年10月11日	下午5:28:59
	 * @param ip
	 * @param port
	 * @param tableName
	 * @param collect
	 * @param site
	 * @throws Exception
	 */
	public static void insertData(String ip,int port,String tableName,JSONArray collect,Site site,TaskLogServiceImpl taskLogService,TaskServiceImpl taskService) throws Exception
	{
		Map<String, Object> params = new HashMap<String, Object>();
		HttpConnctionApi api = new HttpConnctionApi(ip, port);
		params.clear();
		params.put("name", tableName);
		params.put("jsonStr", JSONArray.toJSONString(collect));
		params.put("skiperr", false);
		
		api.post("MssDataApi", "insert", 60000, params) ;


	}

	/**
	 * @author lihf
	 * @date 2016年10月11日	下午6:08:59
	 * @param ip
	 * @param port
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public static String count(String ip,int port,String tableName) throws Exception
	{
		Map<String, Object> params = new HashMap<String, Object>();
		HttpConnctionApi api = new HttpConnctionApi(ip, port);
		params.clear();
		params.put("name", tableName);
		String count = api.post("MssTableApi", "count", 60000, params);
		return count;
	}
	
	/**
	 * 删除数据
	 * @author lihf
	 * @date 2016年10月11日	下午5:19:50
	 * @param ip
	 * @param port
	 * @param tableName
	 * @throws Exception
	 */
	public static void clearData(String ip,int port,String tableName) throws Exception
	{
		Map<String, Object> params = new HashMap<String, Object>();
		HttpConnctionApi api = new HttpConnctionApi(ip, port);
		params.clear();
		params.put("name", tableName);
		api.post("MssTableApi", "clear", 10000, params);
	}
	
	/**
	 * 根据ID删除表数据
	 * @author lihf
	 * @date 2016年10月13日	下午4:00:40
	 * @param ip
	 * @param port
	 * @param tableName
	 * @param id
	 * @throws Exception
	 */
	public static void deleteDataById(String ip,int port,String tableName,String id) throws Exception
	{
		Map<String, Object> params = new HashMap<String, Object>();
		HttpConnctionApi api = new HttpConnctionApi(ip, port);
		params.clear();
		params.put("name", tableName);
		params.put("id", id);
		api.post("MssDataApi", "delete", 10000, params);
	}
	
	/**
	 * 根据ID查找数据
	 * @author lihf
	 * @date 2016年10月13日	下午4:29:46
	 * @param ip
	 * @param port
	 * @param tableName
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public static JSONObject findById(String ip,int port,String tableName,String id) throws Exception
	{
		JSONObject jsonObject = null;
		Map<String, Object> params = new HashMap<String, Object>();
		HttpConnctionApi api = new HttpConnctionApi(ip, port);
		params.clear();
		params.put("name", tableName);
		params.put("id", id);
		String result = api.post("MssDataApi", "findById", 10000, params);
		if(!StringUtil.isEmpty(result))
		{
			jsonObject = JSONObject.parseObject(result);
		}
		return jsonObject;
	}


}
