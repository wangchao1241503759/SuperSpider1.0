/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年9月22日
 */
package cn.com.infcn.superspider.pagemodel;

import java.io.Serializable;
import java.util.List;

/**
 * @author lihf
 * @date 2016年9月22日
 */
public class SeriesEntity implements Serializable
{

	/**
	 * 
	 */
    private static final long serialVersionUID = -4545758433414259405L;
	private String name;
	private String type;
	private List<String> data;
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
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type)
	{
		this.type = type;
	}
	/**
	 * @return the data
	 */
	public List<String> getData()
	{
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(List<String> data)
	{
		this.data = data;
	}
}
