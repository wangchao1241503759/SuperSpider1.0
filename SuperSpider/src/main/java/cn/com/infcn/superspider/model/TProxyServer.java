/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年4月7日
 */
package cn.com.infcn.superspider.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @author lihf
 * @date 2016年4月7日
 */
@Entity
@Table(name="proxy_server")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TProxyServer implements Serializable
{

    private static final long serialVersionUID = 8203747829442901427L;
    
	private String id; 			//主键ID
	private String ip;			//服务器IP地址
	private String port;		//端口
	private String source;		//来源
	private String type;		//类型
	private String state;		//状态   启用：Y  停用:N
	
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
	 * @return the ip
	 */
	@Column(name="ip",unique=true,nullable=false,length=60)
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
	@Column(name="port",length=20)
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
	@Column(name="source",length=200)
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
	@Column(name="type",length=60)
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
	@Column(name="state",length=1,columnDefinition="char")
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
	
}
