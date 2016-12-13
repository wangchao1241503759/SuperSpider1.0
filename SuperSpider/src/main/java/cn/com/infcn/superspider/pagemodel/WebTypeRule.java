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
public class WebTypeRule implements Serializable
{

    private static final long serialVersionUID = 7266308094060799886L;
	private String typeId;			//主键ID
    private String taskId;			//任务ID
    private String typeName;		//类型名称
    private String typeLevel;		//级别
    private String isIncrement;		//是否增量
    private String typeMatchLabel;	//页面匹配
    private String typeFilterRule;	//过滤规则
    private List<WebTypeMatchLabel> webTypeMatchLabelList;
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
	 * @return the typeName
	 */
	public String getTypeName()
	{
		return typeName;
	}
	/**
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName)
	{
		this.typeName = typeName;
	}
	/**
	 * @return the typeLevel
	 */
	public String getTypeLevel()
	{
		return typeLevel;
	}
	/**
	 * @param typeLevel the typeLevel to set
	 */
	public void setTypeLevel(String typeLevel)
	{
		this.typeLevel = typeLevel;
	}
	/**
	 * @return the isIncrement
	 */
	public String getIsIncrement()
	{
		return isIncrement;
	}
	/**
	 * @param isIncrement the isIncrement to set
	 */
	public void setIsIncrement(String isIncrement)
	{
		this.isIncrement = isIncrement;
	}
	/**
	 * @return the typeMatchLabel
	 */
	public String getTypeMatchLabel()
	{
		return typeMatchLabel;
	}
	/**
	 * @param typeMatchLabel the typeMatchLabel to set
	 */
	public void setTypeMatchLabel(String typeMatchLabel)
	{
		this.typeMatchLabel = typeMatchLabel;
	}
	/**
	 * @return the typeFilterRule
	 */
	public String getTypeFilterRule()
	{
		return typeFilterRule;
	}
	/**
	 * @param typeFilterRule the typeFilterRule to set
	 */
	public void setTypeFilterRule(String typeFilterRule)
	{
		this.typeFilterRule = typeFilterRule;
	}
	/**
	 * @return the webTypeMatchLabelList
	 */
	public List<WebTypeMatchLabel> getWebTypeMatchLabelList()
	{
		return webTypeMatchLabelList;
	}
	/**
	 * @param webTypeMatchLabelList the webTypeMatchLabelList to set
	 */
	public void setWebTypeMatchLabelList(List<WebTypeMatchLabel> webTypeMatchLabelList)
	{
		this.webTypeMatchLabelList = webTypeMatchLabelList;
	}
	
}
