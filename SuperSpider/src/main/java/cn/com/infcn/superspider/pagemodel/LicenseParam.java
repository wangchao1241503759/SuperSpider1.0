/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年9月2日
 */
package cn.com.infcn.superspider.pagemodel;

import java.io.Serializable;

/**
 * @author lihf
 * @date 2016年9月2日
 */
public class LicenseParam implements Serializable
{

    private static final long serialVersionUID = -2601023431606800299L;
	private String dbType;		//数据库类型oracle,mysql,...
	private String taskType;	//任务类型	db,web,ftp,file
	private String operation;	//操作 add,edit
	private String scope;		//使用范围  下拉选择时select
	/**
	 * @return the dbType
	 */
	public String getDbType()
	{
		return dbType;
	}
	/**
	 * @param dbType the dbType to set
	 */
	public void setDbType(String dbType)
	{
		this.dbType = dbType;
	}
	/**
	 * @return the taskType
	 */
	public String getTaskType()
	{
		return taskType;
	}
	/**
	 * @param taskType the taskType to set
	 */
	public void setTaskType(String taskType)
	{
		this.taskType = taskType;
	}
	/**
	 * @return the operation
	 */
	public String getOperation()
	{
		return operation;
	}
	/**
	 * @param operation the operation to set
	 */
	public void setOperation(String operation)
	{
		this.operation = operation;
	}
	/**
	 * @return the scope
	 */
	public String getScope()
	{
		return scope;
	}
	/**
	 * @param scope the scope to set
	 */
	public void setScope(String scope)
	{
		this.scope = scope;
	}
}
