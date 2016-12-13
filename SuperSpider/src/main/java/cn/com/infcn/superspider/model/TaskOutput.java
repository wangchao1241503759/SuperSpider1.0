/**
 * 
 */
package cn.com.infcn.superspider.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @description 
 * @author WChao
 * @date   2015年12月21日 	下午2:10:26
 */
@Entity
@Table(name = "task_output")
@DynamicUpdate(true) @DynamicInsert(true)
public class TaskOutput {
	
	private String topId;//主键;
	private String topType;//输出类型;
	private String topHost;//输出服务器地址;
	private Integer topPort;//端口号;
	private String topUserName;//用户名;
	private String topPassWord;//密码;
	private String topDbName;//数据库名;
	private String topTableName;//表名称;
	private String topSchema;//输出别名(Shcema);
	private String topIsClear;//是否清空数据库表;
	private String taskId;//任务ID;
	private Integer topThread;//输出线程数;
	private String topMssType;//输出元数据仓储所在库类型;
	
	public TaskOutput() {}
	public TaskOutput(String id)
	{
		this.topId = id;
	}
	@Id
	@Column(name = "ID", length = 32, nullable = false)
	public String getTopId() {
		return topId;
	}
	public void setTopId(String topId) {
		this.topId = topId;
	}
	@Column(name = "TYPE",length = 20)
	public String getTopType() {
		return topType;
	}
	public void setTopType(String topType) {
		this.topType = topType;
	}
	@Column(name = "HOST",length = 32)
	public String getTopHost() {
		return topHost;
	}
	public void setTopHost(String topHost) {
		this.topHost = topHost;
	}
	@Column(name = "PORT",length = 10)
	public Integer getTopPort() {
		return topPort;
	}
	public void setTopPort(Integer topPort) {
		this.topPort = topPort;
	}
	@Column(name = "USERNAME",length = 32)
	public String getTopUserName() {
		return topUserName;
	}
	public void setTopUserName(String topUserName) {
		this.topUserName = topUserName;
	}
	@Column(name = "PASSWORD",length = 32)
	public String getTopPassWord() {
		return topPassWord;
	}
	public void setTopPassWord(String topPassWord) {
		this.topPassWord = topPassWord;
	}
	@Column(name = "DB_NAME",length = 32)
	public String getTopDbName() {
		return topDbName;
	}
	public void setTopDbName(String topDbName) {
		this.topDbName = topDbName;
	}
	@Column(name = "TABLE_NAME",length = 32)
	public String getTopTableName() {
		return topTableName;
	}
	public void setTopTableName(String topTableName) {
		this.topTableName = topTableName;
	}
	@Column(name = "IS_CLEAR",length = 1)
	public String getTopIsClear() {
		return topIsClear;
	}
	public void setTopIsClear(String topIsClear) {
		this.topIsClear = topIsClear;
	}
	
	@Column(name = "TASK_ID",length = 32)
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	@Column(name = "THREAD")
	public Integer getTopThread() {
		return topThread;
	}
	public void setTopThread(Integer topThread) {
		this.topThread = topThread;
	}
	@Column(name = "MSS_TYPE",length = 32)
	public String getTopMssType() {
		return topMssType;
	}
	public void setTopMssType(String topMssType) {
		this.topMssType = topMssType;
	}
	@Column(name = "DB_SCHEMA")
	public String getTopSchema() {
		return topSchema;
	}
	public void setTopSchema(String topSchema) {
		this.topSchema = topSchema;
	}
	
}
