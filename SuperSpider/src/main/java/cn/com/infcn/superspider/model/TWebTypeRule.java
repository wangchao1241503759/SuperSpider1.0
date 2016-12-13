/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年5月26日
 */
package cn.com.infcn.superspider.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @author lihf
 * @date 2016年5月26日
 */
@Entity
@Table(name="web_type_rule")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TWebTypeRule implements Serializable
{

    private static final long serialVersionUID = -2775193836462036442L;
    
	private String typeId;			//主键ID
    private String taskId;			//任务ID
    private String typeName;		//类型名称
    private String typeLevel;		//级别
    private String isIncrement;		//是否增量
    private String typeMatchLabel;	//页面匹配
    private String typeFilterRule;	//过滤规则（单项符合、全部符合）
    private List<TWebTypeMatchLabel> webTypeMatchLabelList;
    private TWebParamSetting webParamSetting;
	/**
	 * @return the typeId
	 */
    @Id
    @Column(name="type_id",nullable=false,unique=true,length=32)
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
	@Column(name="task_id",nullable=false,length=32)
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
	@Column(name="type_name",nullable=false,length=200)
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
	@Column(name="type_level",nullable=false,length=2)
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
	@Column(name="is_increment",length=5)
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
	@Column(name="type_match_label",nullable=true,columnDefinition="text")
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
	@Column(name="type_filter_rule",nullable=false,length=5)
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
	@OneToMany(fetch=FetchType.LAZY,mappedBy="webTypeRule")
	public List<TWebTypeMatchLabel> getWebTypeMatchLabelList()
	{
		return webTypeMatchLabelList;
	}
	/**
	 * @param webTypeMatchLabelList the webTypeMatchLabelList to set
	 */
	public void setWebTypeMatchLabelList(List<TWebTypeMatchLabel> webTypeMatchLabelList)
	{
		this.webTypeMatchLabelList = webTypeMatchLabelList;
	}
	/**
	 * @return the webParamSetting
	 */
	@OneToOne(fetch=FetchType.LAZY,mappedBy="webTypeRule")
	public TWebParamSetting getWebParamSetting()
	{
		return webParamSetting;
	}
	/**
	 * @param webParamSetting the webParamSetting to set
	 */
	public void setWebParamSetting(TWebParamSetting webParamSetting)
	{
		this.webParamSetting = webParamSetting;
	}


}
