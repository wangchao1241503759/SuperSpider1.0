/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年4月11日
 */
package cn.com.infcn.superspider.pagemodel;

import java.io.Serializable;

/**
 * @author lihf
 * @date 2016年4月11日
 */
public class ProxyServer implements Serializable
{
    private static final long serialVersionUID = -338800430479584641L;
    
	private String id; 			//主键ID
	private String ip;			//服务器IP地址
	private String port;		//端口
	private String source;		//来源
	private String type;		//类型
	private String state;		//状态   启用：Y  停用:N
	private String proxyJson;	//代理所有的数据
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
	 * @return the ip
	 */
	public String getIp()
	{
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip)
	{
		this.ip = ip;
	}
	/**
	 * @return the port
	 */
	public String getPort()
	{
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(String port)
	{
		this.port = port;
	}
	/**
	 * @return the source
	 */
	public String getSource()
	{
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source)
	{
		this.source = source;
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
	 * @return the proxyJson
	 */
	public String getProxyJson()
	{
		return proxyJson;
	}
	/**
	 * @param proxyJson the proxyJson to set
	 */
	public void setProxyJson(String proxyJson)
	{
		this.proxyJson = proxyJson;
	}
}
