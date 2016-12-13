/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年5月26日
 */
package cn.com.infcn.superspider.pagemodel;

import java.io.Serializable;
import java.util.List;

/**
 * @author lihf
 * @date 2016年5月26日
 */
public class WebParamSetting implements Serializable
{
    
    private static final long serialVersionUID = 4274490077431573020L;
	private String paramId;			//主键ID
	private String typeId;			//类型ID
    private String taskId;			//任务ID	
    private String jumpType;		//是否设置跳转
    private String jumpParam;		//跳转参数名称
    private String incrementationParam;//跳转增量值
    private String attr;			//链接属性值
    private String advancedAttr;	//高级属性
    private List<WebFieldExtract> fieldExtractList;
	/**
	 * @return the paramId
	 */
	public String getParamId()
	{
		return paramId;
	}
	/**
	 * @param paramId the paramId to set
	 */
	public void setParamId(String paramId)
	{
		this.paramId = paramId;
	}
	/**
	 * @return the typeId
	 */
	public String getTypeId()
	{
		return typeId;
	}
	/**
	 * @param typeId the typeId to set
	 */
	public void setTypeId(String typeId)
	{
		this.typeId = typeId;
	}
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
	 * @return the jumpType
	 */
	public String getJumpType()
	{
		return jumpType;
	}
	/**
	 * @param jumpType the jumpType to set
	 */
	public void setJumpType(String jumpType)
	{
		this.jumpType = jumpType;
	}
	/**
	 * @return the jumpParam
	 */
	public String getJumpParam()
	{
		return jumpParam;
	}
	/**
	 * @param jumpParam the jumpParam to set
	 */
	public void setJumpParam(String jumpParam)
	{
		this.jumpParam = jumpParam;
	}
	/**
	 * @return the incrementationParam
	 */
	public String getIncrementationParam()
	{
		return incrementationParam;
	}
	/**
	 * @param incrementationParam the incrementationParam to set
	 */
	public void setIncrementationParam(String incrementationParam)
	{
		this.incrementationParam = incrementationParam;
	}
	/**
	 * @return the attr
	 */
	public String getAttr()
	{
		return attr;
	}
	/**
	 * @param attr the attr to set
	 */
	public void setAttr(String attr)
	{
		this.attr = attr;
	}
	/**
	 * @return the advancedAttr
	 */
	public String getAdvancedAttr()
	{
		return advancedAttr;
	}
	/**
	 * @param advancedAttr the advancedAttr to set
	 */
	public void setAdvancedAttr(String advancedAttr)
	{
		this.advancedAttr = advancedAttr;
	}
	/**
	 * @return the fieldExtractList
	 */
	public List<WebFieldExtract> getFieldExtractList()
	{
		return fieldExtractList;
	}
	/**
	 * @param fieldExtractList the fieldExtractList to set
	 */
	public void setFieldExtractList(List<WebFieldExtract> fieldExtractList)
	{
		this.fieldExtractList = fieldExtractList;
	}

}
