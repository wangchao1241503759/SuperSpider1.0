/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年5月31日
 */
package cn.com.infcn.superspider.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @author lihf
 * @date 2016年5月31日
 */
@Entity
@Table(name="web_type_match_label")
@DynamicInsert(true)
@DynamicUpdate
public class TWebTypeMatchLabel implements Serializable
{
    private static final long serialVersionUID = -4540490221541180035L;
    
	private String id;					//主键ID
	private String typeId;				//页面规则类型ID
	private String ruleType;			//规则类型（开始于、包含）
	private String ruleTypeValue;		//规则参数
	private TWebTypeRule webTypeRule;
	/**
	 * @return the id
	 */
    @Id
    @Column(name="id",nullable=false,unique=true,length=32)
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
	@Column(name="type_id",nullable=false,length=32)
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
	@Column(name="rule_type",nullable=false,length=60)
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
	@Column(name="rule_type_value",nullable=false,length=2000)
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
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="type_id",nullable=false,insertable=false,updatable=false)
	public TWebTypeRule getWebTypeRule()
	{
		return webTypeRule;
	}
	/**
	 * @param webTypeRule the webTypeRule to set
	 */
	public void setWebTypeRule(TWebTypeRule webTypeRule)
	{
		this.webTypeRule = webTypeRule;
	}
}
