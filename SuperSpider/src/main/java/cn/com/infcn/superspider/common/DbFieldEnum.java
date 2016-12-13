package cn.com.infcn.superspider.common;

import java.util.HashMap;
import java.util.Map;

import cn.com.infcn.superspider.utils.DbUtil;

public enum DbFieldEnum {
	
	MYSQL字符("mysql",Constant.MYSQL_VARCHAR,"字符",new String[]{"oracle","sqlserver","db2"},Constant.ORACLE_VARCHAR2,Constant.SQLSERVER_VARCHAR,Constant.DB2_VARCHAR),
	MYSQL整数("mysql",Constant.MYSQL_INT,"整数",new String[]{"oracle","sqlserver","db2"},Constant.ORACLE_INT,Constant.SQLSERVER_INT,Constant.DB2_INT),
	MYSQL浮点("mysql",Constant.MYSQL_FLOAT,"浮点",new String[]{"oracle","sqlserver","db2"},Constant.ORACLE_FLOAT,Constant.SQLSERVER_FLOAT,Constant.DB2_FLOAT),
	MYSQL文本("mysql",Constant.MYSQL_TEXT,"文本",new String[]{"oracle","sqlserver","db2"},Constant.ORACLE_TEXT,Constant.SQLSERVER_TEXT,Constant.DB2_TEXT),
	MYSQL日期("mysql",Constant.MYSQL_DATE,"日期",new String[]{"oracle","sqlserver","db2"},Constant.ORACLE_DATE,Constant.SQLSERVER_DATE,Constant.DB2_DATE),
	MYSQL布尔("mysql",Constant.MYSQL_BOOLEAN,"布尔",new String[]{"oracle","sqlserver","db2"},Constant.ORACLE_BOOLEAN,Constant.SQLSERVER_BOOLEAN,Constant.DB2_BOOLEAN),
	
	ORACLE字符("oracle",Constant.ORACLE_VARCHAR2,"字符",new String[]{"mysql","sqlserver","db2"},Constant.MYSQL_VARCHAR,Constant.SQLSERVER_VARCHAR,Constant.DB2_VARCHAR),
	ORACLE整数("oracle",Constant.ORACLE_INT,"整数",new String[]{"mysql","sqlserver","db2"},Constant.MYSQL_INT,Constant.SQLSERVER_INT,Constant.DB2_INT),
	ORACLE浮点("oracle",Constant.ORACLE_FLOAT,"浮点",new String[]{"mysql","sqlserver","db2"},Constant.MYSQL_FLOAT,Constant.SQLSERVER_FLOAT,Constant.DB2_FLOAT),
	ORACLE文本("oracle",Constant.ORACLE_TEXT,"文本",new String[]{"mysql","sqlserver","db2"},Constant.MYSQL_TEXT,Constant.SQLSERVER_TEXT,Constant.DB2_TEXT),
	ORACLE日期("oracle",Constant.ORACLE_DATE,"日期",new String[]{"mysql","sqlserver","db2"},Constant.MYSQL_DATE,Constant.SQLSERVER_DATE,Constant.DB2_DATE),
	ORACLE布尔("oracle",Constant.ORACLE_BOOLEAN,"布尔",new String[]{"mysql","sqlserver","db2"},Constant.MYSQL_BOOLEAN,Constant.SQLSERVER_BOOLEAN,Constant.DB2_BOOLEAN),
	
	SQLSERVER字符("sqlserver",Constant.SQLSERVER_VARCHAR,"字符",new String[]{"mysql","oracle","db2"},Constant.MYSQL_VARCHAR,Constant.ORACLE_VARCHAR2,Constant.DB2_VARCHAR),
	SQLSERVER整数("sqlserver",Constant.SQLSERVER_INT,"整数",new String[]{"mysql","oracle","db2"},Constant.MYSQL_INT,Constant.ORACLE_INT,Constant.DB2_INT),
	SQLSERVER浮点("sqlserver",Constant.SQLSERVER_FLOAT,"浮点",new String[]{"mysql","oracle","db2"},Constant.MYSQL_FLOAT,Constant.ORACLE_FLOAT,Constant.DB2_FLOAT),
	SQLSERVER文本("sqlserver",Constant.SQLSERVER_TEXT,"文本",new String[]{"mysql","oracle","db2"},Constant.MYSQL_TEXT,Constant.ORACLE_TEXT,Constant.DB2_TEXT),
	SQLSERVER日期("sqlserver",Constant.SQLSERVER_DATE,"日期",new String[]{"mysql","oracle","db2"},Constant.MYSQL_DATE,Constant.ORACLE_DATE,Constant.DB2_DATE),
	SQLSERVER布尔("sqlserver",Constant.SQLSERVER_BOOLEAN,"布尔",new String[]{"mysql","oracle","db2"},Constant.MYSQL_BOOLEAN,Constant.ORACLE_BOOLEAN,Constant.DB2_BOOLEAN),
	
	DB2字符("db2",Constant.DB2_VARCHAR,"字符",new String[]{"mysql","oracle","sqlserver"},Constant.MYSQL_VARCHAR,Constant.ORACLE_VARCHAR2,Constant.SQLSERVER_VARCHAR),
	DB2整数("db2",Constant.DB2_INT,"整数",new String[]{"mysql","oracle","sqlserver"},Constant.MYSQL_INT,Constant.ORACLE_INT,Constant.SQLSERVER_INT),
	DB2浮点("db2",Constant.DB2_FLOAT,"浮点",new String[]{"mysql","oracle","sqlserver"},Constant.MYSQL_FLOAT,Constant.ORACLE_FLOAT,Constant.SQLSERVER_FLOAT),
	DB2文本("db2",Constant.DB2_TEXT,"文本",new String[]{"mysql","oracle","sqlserver"},Constant.MYSQL_TEXT,Constant.ORACLE_TEXT,Constant.SQLSERVER_TEXT),
	DB2日期("db2",Constant.DB2_DATE,"日期",new String[]{"mysql","oracle","sqlserver"},Constant.MYSQL_DATE,Constant.ORACLE_DATE,Constant.SQLSERVER_DATE),
	DB2布尔("db2",Constant.DB2_BOOLEAN,"布尔",new String[]{"mysql","oracle","sqlserver"},Constant.MYSQL_BOOLEAN,Constant.ORACLE_BOOLEAN,Constant.SQLSERVER_BOOLEAN),
	
	TRS字符型("trs",Constant.TRS_CHAR,"字符型",new String[]{"mysql","oracle","sqlserver","db2"},Constant.MYSQL_VARCHAR,Constant.ORACLE_VARCHAR2,Constant.SQLSERVER_VARCHAR,Constant.DB2_VARCHAR),
	TRS短语型("trs",Constant.TRS_PHRASE,"短语型",new String[]{"mysql","oracle","sqlserver","db2"},Constant.MYSQL_TEXT,Constant.ORACLE_TEXT,Constant.SQLSERVER_TEXT,Constant.DB2_TEXT),
	TRS数值型("trs",Constant.TRS_NUMBER,"数值型",new String[]{"mysql","oracle","sqlserver","db2"},Constant.MYSQL_FLOAT,Constant.ORACLE_FLOAT,Constant.SQLSERVER_FLOAT,Constant.DB2_FLOAT),
	TRS全文型("trs",Constant.TRS_DOCUMENT,"全文型",new String[]{"mysql","oracle","sqlserver","db2"},Constant.MYSQL_TEXT,Constant.ORACLE_TEXT,Constant.SQLSERVER_TEXT,Constant.DB2_TEXT),
	TRS日期型("trs",Constant.TRS_DATE,"日期型",new String[]{"mysql","oracle","sqlserver","db2"},Constant.MYSQL_DATE,Constant.ORACLE_DATE,Constant.SQLSERVER_DATE,Constant.DB2_DATE),
	TRS二进制型("trs",Constant.TRS_BIT,"二进制型",new String[]{"mysql","oracle","sqlserver","db2"},Constant.MYSQL_BLOB,Constant.ORACLE_BLOB,Constant.SQLSERVER_BLOB,Constant.DB2_BLOB);
	
	public static final String TRS_NUMBER = "NUMBER";//数值型
	public static final String TRS_PHRASE = "PHRASE";//短语型
	public static final String TRS_CHAR = "CHAR";//字符型
	public static final String TRS_DATE = "DATE";//日期型
	public static final String TRS_DOCUMENT = "DOCUMENT";//全文型
	public static final String TRS_BIT = "BIT";//二进制型
	
	
	private Map<String,String> mappingTypes = new HashMap<String, String>();
	private String value;
	private String type;
	private String desc;//描述信息
	
	private DbFieldEnum(String type,String value,String desc,Object... types){
		this.type = type;
		this.value = value;
		this.desc = desc;
		String[] dbTypes = (String[])types[0];
		for(int i = 1 ;i<types.length;i++)
		{
			mappingTypes.put(dbTypes[i-1],types[i].toString());
		}
	}
	public Map<String, String> getMappingTypes() {
		return mappingTypes;
	}

	public void setMappingTypes(Map<String, String> mappingTypes) {
		this.mappingTypes = mappingTypes;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public static void main(String[] args) {
		System.out.println(DbUtil.getTypes("trs"));
		System.out.println(DbFieldEnum.values()[0].getDesc());
	}
}
