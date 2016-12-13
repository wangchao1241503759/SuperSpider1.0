/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年5月31日
 */
package cn.com.infcn.superspider.pagemodel;

import java.io.Serializable;


/**
 * @author lihf
 * @date 2016年5月31日
 */
public class WebTypeMatchLabel implements Serializable
{
    private static final long serialVersionUID = 3764045675606465217L;
    
	private String id;					//主键ID
	private String typeId;				//页面规则类型ID
	private String ruleType;			//规则类型（开始于、包含）
	private String ruleTypeValue;		//规则参数
	private WebTypeRule webTypeRule;
	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
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
	 * @return the ruleType
	 */
	public String getRuleType()
	{
		return ruleType;
	}
	/**
	 * @param ruleType the ruleType to set
	 */
	public void setRuleType(String ruleType)
	{
		this.ruleType = ruleType;
	}
	/**
	 * @return the ruleTypeValue
	 */
	public String getRuleTypeValue()
	{
		return ruleTypeValue;
	}
	/**
	 * @param ruleTypeValue the ruleTypeValue to set
	 */
	public void setRuleTypeValue(String ruleTypeValue)
	{
		this.ruleTypeValue = ruleTypeValue;
	}
	/**
	 * @return the webTypeRule
	 */
	public WebTypeRule getWebTypeRule()
	{
		return webTypeRule;
	}
	/**
	 * @param webTypeRule the webTypeRule to set
	 */
	public void setWebTypeRule(WebTypeRule webTypeRule)
	{
		this.webTypeRule = webTypeRule;
	}
}
