/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年4月12日
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
 * @date 2016年4月12日
 */
@Entity
@Table(name="websource_proxy")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TWebSourceProxy implements Serializable
{

    private static final long serialVersionUID = 2014939234265257728L;
    
    private String id;				//主键	
    private String webSourceId;		//web数据源ID
    private String proxyServerId;	//代理服务的ID
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
	 * @return the webSourceId
	 */
	@Column(name="websource_id",nullable=false,length=32)
	public String getWebSourceId()
	{
		return webSourceId;
	}
	/**
	 * @param webSourceId the webSourceId to set
	 */
	public void setWebSourceId(String webSourceId)
	{
		this.webSourceId = webSourceId;
	}
	/**
	 * @return the proxyServerId
	 */
	@Column(name="proxyserver_id",nullable=false,length=32)
	public String getProxyServerId()
	{
		return proxyServerId;
	}
	/**
	 * @param proxyServerId the proxyServerId to set
	 */
	public void setProxyServerId(String proxyServerId)
	{
		this.proxyServerId = proxyServerId;
	}

}
