/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年5月26日
 */
package cn.com.infcn.superspider.pagemodel;

import java.io.Serializable;

/**
 * @author lihf
 * @date 2016年5月26日
 */
public class WebFieldExtract implements Serializable
{
    private static final long serialVersionUID = 7198457204222518379L;
    
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
	 * @return the name
	 */
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

}
