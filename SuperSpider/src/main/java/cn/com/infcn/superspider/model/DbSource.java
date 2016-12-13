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
 * @date   2015年12月21日 	下午1:12:49
 */
@Entity
@Table(name = "db_source")
@DynamicUpdate(true) @DynamicInsert(true)
public class DbSource {
	private String dsId;//主键;
	private String dsType;//数据库类型;
	private String dsHost;//主机ip;
	private Integer dsPort;//端口号;
	private String dsUserName;//用户名;
	private String dsPassWord;//密码;
	private String dsDbName;//数据库名称;
	private String dsTableSpace;//表空间名称;
	private String dsSchema;//别名(Schema);
	private Integer dsFetchSize;//一次获取数据数目;
	private String dsTableType;//选择数据方式;0：数据表\1:sql语句;
	private String dsTableName;//表名称;
	private String dsSql;//sql语句;
	private Float dsThread;//爬取线程数;
	private String taskId;//任务ID;
	public DbSource(){};
	public DbSource(String id)
	{
		this.dsId = id;
	}
	@Id
	@Column(name = "ID", length = 32, nullable = false)
	public String getDsId() {
		return dsId;
	}
	public void setDsId(String dsId) {
		this.dsId = dsId;
	}
	@Column(name = "DB_TYPE",length = 32)
	public String getDsType() {
		return dsType;
	}
	public void setDsType(String dsType) {
		this.dsType = dsType;
	}
	@Column(name = "HOST",length = 50)
	public String getDsHost() {
		return dsHost;
	}
	public void setDsHost(String dsHost) {
		this.dsHost = dsHost;
	}
	@Column(name = "PORT")
	public Integer getDsPort() {
		return dsPort;
	}
	public void setDsPort(Integer dsPort) {
		this.dsPort = dsPort;
	}
	@Column(name = "USERNAME",length = 32)
	public String getDsUserName() {
		return dsUserName;
	}
	public void setDsUserName(String dsUserName) {
		this.dsUserName = dsUserName;
	}
	@Column(name = "PASSWORD",length = 32)
	public String getDsPassWord() {
		return dsPassWord;
	}
	public void setDsPassWord(String dsPassWord) {
		this.dsPassWord = dsPassWord;
	}
	@Column(name = "TABLESPACE",length = 50)
	public String getDsTableSpace() {
		return dsTableSpace;
	}
	public void setDsTableSpace(String dsTableSpace) {
		this.dsTableSpace = dsTableSpace;
	}
	@Column(name = "DB_NAME",length = 100)
	public String getDsDbName() {
		return dsDbName;
	}
	public void setDsDbName(String dsDbName) {
		this.dsDbName = dsDbName;
	}
	@Column(name = "FETCH_SIZE",length = 32)
	public Integer getDsFetchSize() {
		return dsFetchSize;
	}
	public void setDsFetchSize(Integer dsFetchSize) {
		this.dsFetchSize = dsFetchSize;
	}
	@Column(name = "TABLE_NAME",length = 100)
	public String getDsTableName() {
		return dsTableName;
	}
	public void setDsTableName(String dsTableName) {
		this.dsTableName = dsTableName;
	}
	@Column(name = "DB_SQL",length = 500)
	public String getDsSql() {
		return dsSql;
	}
	public void setDsSql(String dsSql) {
		this.dsSql = dsSql;
	}
	@Column(name = "TABLE_TYPE",length = 1)
	public String getDsTableType() {
		return dsTableType;
	}
	public void setDsTableType(String dsTableType) {
		this.dsTableType = dsTableType;
	}
	@Column(name = "TASK_ID",length = 32)
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	@Column(name = "THREAD")
	public Float getDsThread() {
		return dsThread;
	}
	public void setDsThread(Float dsThread) {
		this.dsThread = dsThread;
	}
	@Column(name = "DB_SCHEMA")
	public String getDsSchema() {
		return dsSchema;
	}
	public void setDsSchema(String dsSchema) {
		this.dsSchema = dsSchema;
	}
	
}
