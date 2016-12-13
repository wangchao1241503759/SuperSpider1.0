/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年7月13日
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
 * @date 2016年7月13日
 */
@Entity
@Table(name="file_source")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TFileSource implements Serializable
{
    private static final long serialVersionUID = -3456655681220377510L;
    
	private String fileId;	//主键ID
    private String taskId;			//任务ID
	private String filePath;		//file路径
	private String fileName; //文件(目录)名匹配
	private String exclusion;	//排除
	private String fileLimit;			 //文件大小范围 是否限制(Y:限制,N:不限制)
	private String fileLimitStart;		 //限制开始xxKB
	private String fileLimitEnd;		 	 //限制结束xxKB
	private String fileIsSubDir;			 //是否采集子目录(Y:是,N:否)
	private String fileExtractThreadNum;	 //提取线程数
	/**
	 * @return the fileId
	 */
	@Id
	@Column(name="id",nullable=false,unique=true,length=32)
	public String getFileId()
	{
		return fileId;
	}
	/**
	 * @param fileId the fileId to set
	 */
	public void setFileId(String fileId)
	{
		this.fileId = fileId;
	}
	/**
	 * @return the taskId
	 */
	@Column(name="task_id",length=32)
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
	 * @return the filePath
	 */
	@Column(name="file_path",length=2000)
	public String getFilePath()
	{
		return filePath;
	}
	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}
	/**
	 * @return the fileName
	 */
	@Column(name="file_name",length=200)
	public String getFileName()
	{
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	/**
	 * @return the exclusion
	 */
	@Column(name="exclusion",length=3,columnDefinition="char")
	public String getExclusion()
	{
		return exclusion;
	}
	/**
	 * @param exclusion the exclusion to set
	 */
	public void setExclusion(String exclusion)
	{
		this.exclusion = exclusion;
	}
	/**
	 * @return the fileLimit
	 */
	@Column(name="file_limit",length=1,columnDefinition="char")
	public String getFileLimit()
	{
		return fileLimit;
	}
	/**
	 * @param fileLimit the fileLimit to set
	 */
	public void setFileLimit(String fileLimit)
	{
		this.fileLimit = fileLimit;
	}
	/**
	 * @return the fileLimitStart
	 */
	@Column(name="file_limit_start",length=50)
	public String getFileLimitStart()
	{
		return fileLimitStart;
	}
	/**
	 * @param fileLimitStart the fileLimitStart to set
	 */
	public void setFileLimitStart(String fileLimitStart)
	{
		this.fileLimitStart = fileLimitStart;
	}
	/**
	 * @return the fileLimitEnd
	 */
	@Column(name="file_limit_end",length=50)
	public String getFileLimitEnd()
	{
		return fileLimitEnd;
	}
	/**
	 * @param fileLimitEnd the fileLimitEnd to set
	 */
	public void setFileLimitEnd(String fileLimitEnd)
	{
		this.fileLimitEnd = fileLimitEnd;
	}
	/**
	 * @return the fileIsSubDir
	 */
	@Column(name="file_is_sub_dir",length=1,columnDefinition="char")
	public String getFileIsSubDir()
	{
		return fileIsSubDir;
	}
	/**
	 * @param fileIsSubDir the fileIsSubDir to set
	 */
	public void setFileIsSubDir(String fileIsSubDir)
	{
		this.fileIsSubDir = fileIsSubDir;
	}
	/**
	 * @return the fileExtractThreadNum
	 */
	@Column(name="file_extract_thread_num",length=5)
	public String getFileExtractThreadNum()
	{
		return fileExtractThreadNum;
	}
	/**
	 * @param fileExtractThreadNum the fileExtractThreadNum to set
	 */
	public void setFileExtractThreadNum(String fileExtractThreadNum)
	{
		this.fileExtractThreadNum = fileExtractThreadNum;
	}
}
