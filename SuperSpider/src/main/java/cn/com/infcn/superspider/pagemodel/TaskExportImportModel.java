/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年7月8日
 */
package cn.com.infcn.superspider.pagemodel;

import java.util.List;

import cn.com.infcn.superspider.model.DbSource;
import cn.com.infcn.superspider.model.FieldMapping;
import cn.com.infcn.superspider.model.FileContentMapping;
import cn.com.infcn.superspider.model.SchedulePlan;
import cn.com.infcn.superspider.model.TFileSource;
import cn.com.infcn.superspider.model.TFtpSource;
import cn.com.infcn.superspider.model.TWebSource;
import cn.com.infcn.superspider.model.Task;
import cn.com.infcn.superspider.model.TaskOutput;

/**
 * @author lihf
 * @date 2016年7月8日
 */
public class TaskExportImportModel
{
	private Task task;
    private DbSource dbSource;				//DB数据源设置
	private TWebSource webSource;			//WEB数据源设置
	private TFtpSource ftpSource;			//ftp数据源设置
	private TFileSource fileSource;			//file数据源设置
	private TaskOutput taskOutput;			//数据输出设置
	private List<WebTypeRule> typelist;	//页面规则
	private List<WebParamSetting> paramlist; //参数设置
	private List<FieldMapping> fieldMappings;//字段映射列表;
	private List<FileContentMapping> fileContentMappings; //文件内容映射列表;	
	private List<SchedulePlan> schedulePlans;	//高度计划列表
	
	/**
	 * @return the task
	 */
	public Task getTask()
	{
		return task;
	}
	/**
	 * @param task the task to set
	 */
	public void setTask(Task task)
	{
		this.task = task;
	}
	/**
	 * @return the dbSource
	 */
	public DbSource getDbSource()
	{
		return dbSource;
	}
	/**
	 * @param dbSource the dbSource to set
	 */
	public void setDbSource(DbSource dbSource)
	{
		this.dbSource = dbSource;
	}
	/**
	 * @return the webSource
	 */
	public TWebSource getWebSource()
	{
		return webSource;
	}
	/**
	 * @param webSource the webSource to set
	 */
	public void setWebSource(TWebSource webSource)
	{
		this.webSource = webSource;
	}
	/**
	 * @return the ftpSource
	 */
	public TFtpSource getFtpSource()
	{
		return ftpSource;
	}
	/**
	 * @param ftpSource the ftpSource to set
	 */
	public void setFtpSource(TFtpSource ftpSource)
	{
		this.ftpSource = ftpSource;
	}
	/**
	 * @return the fileSource
	 */
	public TFileSource getFileSource()
	{
		return fileSource;
	}
	/**
	 * @param fileSource the fileSource to set
	 */
	public void setFileSource(TFileSource fileSource)
	{
		this.fileSource = fileSource;
	}
	/**
	 * @return the taskOutput
	 */
	public TaskOutput getTaskOutput()
	{
		return taskOutput;
	}
	/**
	 * @param taskOutput the taskOutput to set
	 */
	public void setTaskOutput(TaskOutput taskOutput)
	{
		this.taskOutput = taskOutput;
	}
	/**
	 * @return the typelist
	 */
	public List<WebTypeRule> getTypelist()
	{
		return typelist;
	}
	/**
	 * @param typelist the typelist to set
	 */
	public void setTypelist(List<WebTypeRule> typelist)
	{
		this.typelist = typelist;
	}
	/**
	 * @return the paramlist
	 */
	public List<WebParamSetting> getParamlist()
	{
		return paramlist;
	}
	/**
	 * @param paramlist the paramlist to set
	 */
	public void setParamlist(List<WebParamSetting> paramlist)
	{
		this.paramlist = paramlist;
	}
	/**
	 * @return the fieldMappings
	 */
	public List<FieldMapping> getFieldMappings()
	{
		return fieldMappings;
	}
	/**
	 * @param fieldMappings the fieldMappings to set
	 */
	public void setFieldMappings(List<FieldMapping> fieldMappings)
	{
		this.fieldMappings = fieldMappings;
	}
	public List<FileContentMapping> getFileContentMappings()
	{
		return fileContentMappings;
	}
	public void setFileContentMappings(List<FileContentMapping> fileContentMappings)
	{
		this.fileContentMappings = fileContentMappings;
	}
	public List<SchedulePlan> getSchedulePlans()
	{
		return schedulePlans;
	}
	public void setSchedulePlans(List<SchedulePlan> schedulePlans)
	{
		this.schedulePlans = schedulePlans;
	}
}
