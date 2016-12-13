/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年5月26日
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
 * @date 2016年5月26日
 */
@Entity
@Table(name="web_field_extract")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TWebFieldExtract implements Serializable
{
    private static final long serialVersionUID = 522609374215200825L;
    
	private String id; 				//主键ID
	private String paramId;			//参数ID
	private String name;			//字段名称
	private String xpath;			//xpath
	private String exp;				//exp
	private String content;			//提取内容
	private String extractFieldLength; //提取字段长度
	private String fieldExp;			//字段转换表达式
	private String isAutoJoin;			//是否自动拼接
	private String isMultValue;			//是否多值
	private String targetFieldName;		//目标字段名称
	private String targetFieldType;		//目标字段类型
	private String urlAttr;				//链接属性
	private String regex;				//正则表达式
	private int	sort;					//排序
	private TWebParamSetting webParamSetting;
	 
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
	 * @return the paramId
	 */
	@Column(name="param_id",nullable=false,length=32)
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
	 * @return the name
	 */
	@Column(name="name",nullable=false,length=200)
	public String getName()
	{
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	/**
	 * @return the xpath
	 */
	@Column(name="xpath",length=2000)
	public String getXpath()
	{
		return xpath;
	}
	/**
	 * @param xpath the xpath to set
	 */
	public void setXpath(String xpath)
	{
		this.xpath = xpath;
	}
	/**
	 * @return the exp
	 */
	@Column(name="exp",nullable=true,length=500)
	public String getExp()
	{
		return exp;
	}
	/**
	 * @param exp the exp to set
	 */
	public void setExp(String exp)
	{
		this.exp = exp;
	}
	/**
	 * @return the content
	 */
	@Column(name="content",nullable=false,length=50)
	public String getContent()
	{
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content)
	{
		this.content = content;
	}
	/**
	 * @return the extractFieldLength
	 */
	@Column(name="extract_field_length",nullable=true,length=20)
	public String getExtractFieldLength()
	{
		return extractFieldLength;
	}
	/**
	 * @param extractFieldLength the extractFieldLength to set
	 */
	public void setExtractFieldLength(String extractFieldLength)
	{
		this.extractFieldLength = extractFieldLength;
	}
	/**
	 * @return the fieldExp
	 */
	@Column(name="field_exp",nullable=true,length=200)
	public String getFieldExp()
	{
		return fieldExp;
	}
	/**
	 * @param fieldExp the fieldExp to set
	 */
	public void setFieldExp(String fieldExp)
	{
		this.fieldExp = fieldExp;
	}
	/**
	 * @return the isAutoJoin
	 */
	@Column(name="is_auto_join",length=5)
	public String getIsAutoJoin()
	{
		return isAutoJoin;
	}
	/**
	 * @param isAutoJoin the isAutoJoin to set
	 */
	public void setIsAutoJoin(String isAutoJoin)
	{
		this.isAutoJoin = isAutoJoin;
	}
	/**
	 * @return the isMultValue
	 */
	@Column(name="is_mult_value",length=5)
	public String getIsMultValue()
	{
		return isMultValue;
	}
	/**
	 * @param isMultValue the isMultValue to set
	 */
	public void setIsMultValue(String isMultValue)
	{
		this.isMultValue = isMultValue;
	}
	/**
	 * @return the targetFieldName
	 */
	@Column(name="target_field_name",length=100)
	public String getTargetFieldName()
	{
		return targetFieldName;
	}
	/**
	 * @param targetFieldName the targetFieldName to set
	 */
	public void setTargetFieldName(String targetFieldName)
	{
		this.targetFieldName = targetFieldName;
	}
	/**
	 * @return the targetFieldType
	 */
	@Column(name="target_field_type",length=100)
	public String getTargetFieldType()
	{
		return targetFieldType;
	}
	/**
	 * @param targetFieldType the targetFieldType to set
	 */
	public void setTargetFieldType(String targetFieldType)
	{
		this.targetFieldType = targetFieldType;
	}
	/**
	 * @return the urlAttr
	 */
	@Column(name="url_attr",length=20)
	public String getUrlAttr()
	{
		return urlAttr;
	}
	/**
	 * @param urlAttr the urlAttr to set
	 */
	public void setUrlAttr(String urlAttr)
	{
		this.urlAttr = urlAttr;
	}

	/**
	 * @return the regex
	 */
	@Column(name="regex",length=2000)
	public String getRegex()
	{
		return regex;
	}
	/**
	 * @param regex the regex to set
	 */
	public void setRegex(String regex)
	{
		this.regex = regex;
	}
	/**
	 * @return the sort
	 */
	@Column(name="sort",length=11)
	public int getSort()
	{
		return sort;
	}
	/**
	 * @param sort the sort to set
	 */
	public void setSort(int sort)
	{
		this.sort = sort;
	}
	/**
	 * @return the webParamSetting
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="param_id",nullable=false,insertable=false,updatable=false)
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
