package cn.com.infcn.superspider.io.output;

import com.alibaba.fastjson.JSONObject;
import com.justme.superspider.plugin.db.util.JdbcHelper;

/**
 * 
 * @description 
 * @author WChao
 * @date   2016年1月8日 	上午10:30:09
 */
public class DbModel {
	
	//数据源驱动名称;
	public  String driverClassName = "";
	//数据源主机IP地址
	public  String host = "";
	//数据源主机端口号
	public  Integer port = null;
	//数据源数据库名
	public  String dbname = "";
	//表名称
	public  String tablename = "";
	//数据库类型
	public  String type = "";
	//数据源Url
	public  String jdbcurl = "";
	//数据源用户名
	public  String username = "";
	//数据源密码
	public  String password = "";
	//任务输出源配置参数;
	public  JSONObject outputOption = new JSONObject();
	//输出源表结构;
	public  JSONObject tableStruct = new JSONObject();
	
	public DbModel(){}
	public DbModel(String host,Integer port,String type,String dbname,String username,String password,String tablename)
	{
		this.driverClassName = JdbcHelper.getDriverClass(type);
		this.type = type;
        this.host = host;
        this.port = port;
        this.dbname = dbname;
        this.username = username;
        this.password = password;
        this.tablename = tablename;
        this.jdbcurl = JdbcHelper.makeJdbcUrl(type, host, port, dbname);
	}
	public String getDriverClassName() {
		return driverClassName;
	}
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getDbname() {
		return dbname;
	}
	public void setDbname(String dbname) {
		this.dbname = dbname;
	}
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getJdbcurl() {
		return jdbcurl;
	}
	public void setJdbcurl(String jdbcurl) {
		this.jdbcurl = jdbcurl;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public JSONObject getOutputOption() {
		return outputOption;
	}
	public void setOutputOption(JSONObject outputOption) {
		this.outputOption = outputOption;
	}
	public JSONObject getTableStruct() {
		return tableStruct;
	}
	public void setTableStruct(JSONObject tableStruct) {
		this.tableStruct = tableStruct;
	}
	@Override
	public String toString() {
		return "[type:"+this.type+",host:"+this.host+",port:"+port+",dbname:"+dbname+",username:"+username+",password:"+password+",tablename:"+tablename+"]";
	}
}
