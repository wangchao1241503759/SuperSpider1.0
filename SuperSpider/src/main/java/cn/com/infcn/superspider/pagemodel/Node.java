/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年8月26日
 */
package cn.com.infcn.superspider.pagemodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lihf
 * @date 2016年8月26日
 */
public class Node implements Serializable
{

    private static final long serialVersionUID = 8560278535638963244L;
	private String id;
	private String text;
	private String state;
	private boolean checked;
	private List<Node> children = new ArrayList<Node>();
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
	 * @return the text
	 */
	public String getText()
	{
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text)
	{
		this.text = text;
	}
	/**
	 * @return the state
	 */
	public String getState()
	{
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state)
	{
		this.state = state;
	}
	/**
	 * @return the checked
	 */
	public boolean isChecked()
	{
		return checked;
	}
	/**
	 * @param checked the checked to set
	 */
	public void setChecked(boolean checked)
	{
		this.checked = checked;
	}
	/**
	 * @return the children
	 */
	public List<Node> getChildren()
	{
		return children;
	}
	/**
	 * @param children the children to set
	 */
	public void setChildren(List<Node> children)
	{
		this.children = children;
	}
}
