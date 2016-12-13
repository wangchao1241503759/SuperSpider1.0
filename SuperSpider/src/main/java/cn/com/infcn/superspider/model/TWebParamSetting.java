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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @author lihf
 * @date 2016年5月26日
 */
@Entity
@Table(name="web_param_setting")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TWebParamSetting implements Serializable
{
    private static final long serialVersionUID = -4225357478464448891L;
    
	private String paramId;			//主键ID
	private String typeId;			//类型ID
    private String taskId;			//任务ID	
    private String jumpType;		//是否设置跳转
    private String jumpParam;		//跳转参数名称
    private String incrementationParam;//跳转增量值
    private String attr;			//链接属性值
    private String advancedAttr;	//高级属性
    private List<TWebFieldExtract> fieldExtractList;
    private TWebTypeRule webTypeRule;
	/**
	 * @return the paramId
	 */
    @Id
    @Column(name="param_id",nullable=false,unique=true,length=32)
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
	 * @return the jumpType
	 */
	@Column(name="jump_type",nullable=false,length=2)
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
	@Column(name="jump_param",length=200)
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
	@Column(name="incrementation_param",length=200)
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
	@Column(name="attr",length=20)
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
	@Column(name="advanced_attr",length=5)
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
	@OneToMany(fetch = FetchType.LAZY,mappedBy="webParamSetting")
	@OrderBy("sort ASC")
	public List<TWebFieldExtract> getFieldExtractList()
	{
		return fieldExtractList;
	}
	/**
	 * @param fieldExtractList the fieldExtractList to set
	 */
	public void setFieldExtractList(List<TWebFieldExtract> fieldExtractList)
	{
		this.fieldExtractList = fieldExtractList;
	}
	/**
	 * @return the webTypeRule
	 */
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="type_id",unique=true,nullable=false,insertable=false,updatable=false)
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
