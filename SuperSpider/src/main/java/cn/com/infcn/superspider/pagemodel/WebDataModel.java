/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年8月10日
 */
package cn.com.infcn.superspider.pagemodel;

import java.io.Serializable;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;

/**
 * Web数据处理对象
 * @author lihf
 * @date 2016年8月10日
 */
public class WebDataModel implements Serializable
{
    private static final long serialVersionUID = 270021778136801541L;
    
	private String taskId;		//任务ID
	private String taskName;	//任务名称
	private String level;		//任务级别
	private String relation;	//关联关系字段名
	private JSONArray jsonArray;	//级别数据
	private Map<String,JSONArray> jsonMap;	//当前级别的数据，key为task_url,value为JSONArray
	/**
	 * @return the taskId
	 */
	public String getTaskId()
	{
		return taskId;
	}
	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}
	/**
	 * @return the taskName
	 */
	public String getTaskName()
	{
		return taskName;
	}
	/**
	 * @param taskName the taskName to set
	 */
	public void setTaskName(String taskName)
	{
		this.taskName = taskName;
	}
	/**
	 * @return the level
	 */
	public String getLevel()
	{
		return level;
	}
	/**
	 * @param level the level to set
	 */
	public void setLevel(String level)
	{
		this.level = level;
	}
	/**
	 * @return the relation
	 */
	public String getRelation()
	{
		return relation;
	}
	/**
	 * @param relation the relation to set
	 */
	public void setRelation(String relation)
	{
		this.relation = relation;
	}
	/**
	 * @return the jsonArray
	 */
	public JSONArray getJsonArray()
	{
		return jsonArray;
	}
	/**
	 * @param jsonArray the jsonArray to set
	 */
	public void setJsonArray(JSONArray jsonArray)
	{
		this.jsonArray = jsonArray;
	}
	/**
	 * @return the jsonMap
	 */
	public Map<String, JSONArray> getJsonMap()
	{
		return jsonMap;
	}
	/**
	 * @param jsonMap the jsonMap to set
	 */
	public void setJsonMap(Map<String, JSONArray> jsonMap)
	{
		this.jsonMap = jsonMap;
	}
}
