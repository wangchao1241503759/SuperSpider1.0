/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年7月12日
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
 * @date 2016年7月12日
 */
@Entity
@Table(name="ftp_source")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TFtpSource implements Serializable
{

    private static final long serialVersionUID = -1344269279041192138L;
    
	private String ftpId;	//主键ID
    private String taskId;			//任务ID
    private String charset; //站点字符集
	private String ftpIP;	//FTP服务器地址
	private String ftpPort;	//端口号
	private String ftpUserName; //用户名
	private String ftpPassword;	//密码
	private String ftpType;		//FTP类型
	private String ftpPath;		//FTP路径
	private String ftpFileName; //文件(目录)名匹配
	private String exclusion;	//排除
	private String ftpDownloadThreadNum; //下载线程数
	private String ftpPacketSize;		 //数据包大小
	private String ftpLimit;			 //文件大小范围 是否限制(Y:限制,N:不限制)
	private String ftpLimitStart;		 //限制开始xxKB
	private String ftpLimitEnd;		 	 //限制结束xxKB
	private String ftpIsSubDir;			 //是否采集子目录(Y:是,N:否)
	
	/**
	 * @return the ftpId
	 */
	@Id
	@Column(name="id",nullable=false,unique=true,insertable=false,updatable=false,length=32)
	public String getFtpId()
	{
		return ftpId;
	}
	/**
	 * @param ftpId the ftpId to set
	 */
	public void setFtpId(String ftpId)
	{
		this.ftpId = ftpId;
	}
	/**
	 * @return the taskId
	 */
	@Column(name="task_id",nullable=false,length=32)
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
	 * @return the charset
	 */
	@Column(name="charset",length=30)
	public String getCharset()
	{
		return charset;
	}
	/**
	 * @param charset the charset to set
	 */
	public void setCharset(String charset)
	{
		this.charset = charset;
	}
	/**
	 * @return the ftpIP
	 */
	@Column(name="ftp_ip",nullable=false,length=40)
	public String getFtpIP()
	{
		return ftpIP;
	}
	/**
	 * @param ftpIP the ftpIP to set
	 */
	public void setFtpIP(String ftpIP)
	{
		this.ftpIP = ftpIP;
	}
	/**
	 * @return the ftpPort
	 */
	@Column(name="ftp_port",length=20)
	public String getFtpPort()
	{
		return ftpPort;
	}
	/**
	 * @param ftpPort the ftpPort to set
	 */
	public void setFtpPort(String ftpPort)
	{
		this.ftpPort = ftpPort;
	}
	/**
	 * @return the ftpUserName
	 */
	@Column(name="ftp_user_name",length=100)
	public String getFtpUserName()
	{
		return ftpUserName;
	}
	/**
	 * @param ftpUserName the ftpUserName to set
	 */
	public void setFtpUserName(String ftpUserName)
	{
		this.ftpUserName = ftpUserName;
	}
	/**
	 * @return the ftpPassword
	 */
	@Column(name="ftp_password",length=100)
	public String getFtpPassword()
	{
		return ftpPassword;
	}
	/**
	 * @param ftpPassword the ftpPassword to set
	 */
	public void setFtpPassword(String ftpPassword)
	{
		this.ftpPassword = ftpPassword;
	}
	/**
	 * @return the ftpType
	 */
	@Column(name="ftp_type",length=20)
	public String getFtpType()
	{
		return ftpType;
	}
	/**
	 * @param ftpType the ftpType to set
	 */
	public void setFtpType(String ftpType)
	{
		this.ftpType = ftpType;
	}
	/**
	 * @return the ftpPath
	 */
	@Column(name="ftp_path",length=2000)
	public String getFtpPath()
	{
		return ftpPath;
	}
	/**
	 * @param ftpPath the ftpPath to set
	 */
	public void setFtpPath(String ftpPath)
	{
		this.ftpPath = ftpPath;
	}
	/**
	 * @return the ftpFileName
	 */
	@Column(name="ftp_file_name",length=200)
	public String getFtpFileName()
	{
		return ftpFileName;
	}
	/**
	 * @param ftpFileName the ftpFileName to set
	 */
	public void setFtpFileName(String ftpFileName)
	{
		this.ftpFileName = ftpFileName;
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
	 * @return the ftpDownloadThreadNum
	 */
	@Column(name="ftp_download_thread_num",length=5)
	public String getFtpDownloadThreadNum()
	{
		return ftpDownloadThreadNum;
	}
	/**
	 * @param ftpDownloadThreadNum the ftpDownloadThreadNum to set
	 */
	public void setFtpDownloadThreadNum(String ftpDownloadThreadNum)
	{
		this.ftpDownloadThreadNum = ftpDownloadThreadNum;
	}
	/**
	 * @return the ftpPacketSize
	 */
	@Column(name="ftp_packet_size",length=5)
	public String getFtpPacketSize()
	{
		return ftpPacketSize;
	}
	/**
	 * @param ftpPacketSize the ftpPacketSize to set
	 */
	public void setFtpPacketSize(String ftpPacketSize)
	{
		this.ftpPacketSize = ftpPacketSize;
	}
	/**
	 * @return the ftpLimit
	 */
	@Column(name="ftp_limit",length=1,columnDefinition="char")
	public String getFtpLimit()
	{
		return ftpLimit;
	}
	/**
	 * @param ftpLimit the ftpLimit to set
	 */
	public void setFtpLimit(String ftpLimit)
	{
		this.ftpLimit = ftpLimit;
	}
	/**
	 * @return the ftpLimitStart
	 */
	@Column(name="ftp_limit_start",length=50)
	public String getFtpLimitStart()
	{
		return ftpLimitStart;
	}
	/**
	 * @param ftpLimitStart the ftpLimitStart to set
	 */
	public void setFtpLimitStart(String ftpLimitStart)
	{
		this.ftpLimitStart = ftpLimitStart;
	}
	/**
	 * @return the ftpLimitEnd
	 */
	@Column(name="ftp_limit_end",length=50)
	public String getFtpLimitEnd()
	{
		return ftpLimitEnd;
	}
	/**
	 * @param ftpLimitEnd the ftpLimitEnd to set
	 */
	public void setFtpLimitEnd(String ftpLimitEnd)
	{
		this.ftpLimitEnd = ftpLimitEnd;
	}
	/**
	 * @return the ftpIsSubDir
	 */
	@Column(name="ftp_is_sub_dir",length=1,columnDefinition="char")
	public String getFtpIsSubDir()
	{
		return ftpIsSubDir;
	}
	/**
	 * @param ftpIsSubDir the ftpIsSubDir to set
	 */
	public void setFtpIsSubDir(String ftpIsSubDir)
	{
		this.ftpIsSubDir = ftpIsSubDir;
	}
	
}
