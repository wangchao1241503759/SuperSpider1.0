package cn.com.infcn.superspider.common;

public class Constant {
	/**
	 * Mysql数据库;
	 */
	public static final String MYSQL = "mysql";
	/**
	 * SqlServer数据库;
	 */
	public static final String SQLSERVER = "sqlserver";
	/**
	 * Oracle数据库
	 */
	public static final String ORACLE = "oracle";
	/**
	 * Db2数据库;
	 */
	public static final String DB2 = "db2";
	/**
	 * Mongo数据库;
	 */
	public static final String MONGO = "mongo";
	/**
	 * Trs数据库;
	 */
	public static final String TRS = "trs";
	/**
	 * 爬虫内置适配器;
	 */
	public static final String SPIDER = "spider";
	
	
	/**
	 * 触发器类型
	 */
	public static final String TRIGGER_TYPE_INSERT="insert";
	public static final String TRIGGER_TYPE_UPDATE="update";
	public static final String TRIGGER_TYPE_DELETE="delete";
	/**
	 * 元数据仓储
	 */
	public static final String MSS = "mss";
	/**
	 * 元数据仓储3.2
	 */
	public static final String MSS_HTTP = "mss_http";
	/**
	 * Mysql数据库驱动;
	 */
	public static final String MYSQL_DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
	/**
	 * SqlServer数据库驱动;
	 */
	public static final String SQLSERVER_DRIVER_CLASS_NAME = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	/**
	 * 爬虫视图标识前缀;
	 */
	public static final String SPIDER_ = "spider_";
	/**
	 * Oracle数据库驱动;
	 */
	public static final String ORACLE_DRIVER_CLASS_NAME = "oracle.jdbc.driver.OracleDriver";
	/**
	 * DB2数据库驱动;
	 */
	public static final String DB2_DRIVER_CLASS_NAME = "com.ibm.db2.jcc.DB2Driver";
	/**
	 * 适配器配置文件名;
	 */
	public static final String ADAPTER_CONFIG_PROPERTIES = "config.properties";

	/**
	 * ArcGIS
	 */
	public static final String ARCGIS_TABLENAME = "table_name";
	public static final String ARCGIS_ROWID = "rowid_column";
	
	/**
	 * 连接数量大小;
	 */
	public static final String FETCH_SIZE = "fetchSize";
	/**
	 * 连接数量数值大小;
	 */
	public static final String FETCH_SIZE_NUM = "1000";
	
	public static final String TABLE_NAME = "tablename";
	
	public static final String SKIP = "skip";
	
	public static final String LIMIT = "limit";
	
	public static final String SCHEMA = "schema";
	/**
	 * JDBC连接信息;
	 */
	public static final String HOST = "host";
	public static final String PORT = "port";
	public static final String DBNAME = "dbname";
	public static final String TYPE = "type";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String DB_TYPE = "db_type";

	/**
	 * 视图根目录名;
	 */
	public static final String SUPERSPIDER = "superspider";
	
	/**
	 * 任务拉取点;
	 */
	public static final String TASK_POLL = "task_poll";
	/**
	 * 任务开始点;
	 */
	public static final String BEGIN = "begin";
	/**
	 * 任务连接点
	 */
	public static final String FETCH = "fetch";
	/**
	 * 任务挖掘点;
	 */
	public static final String DIG = "dig";
	/**
	 * 任务去重点;
	 */
	public static final String DUP_REMOVAL = "dup_removal";
	/**
	 * 任务加入队列点;
	 */
	public static final String TASK_PUSH = "task_push";
	/**
	 * 任务目标确认点;
	 */
	public static final String TARGET = "target";
	/**
	 * 任务解析转换点;
	 */
	public static final String PARSE = "parse";
	/**
	 * 任务类型DB
	 */
	public static final String DB = "db";
	/**
	 * 任务类型WEB
	 */
	public static final String WEB = "web";
	/**
	 * 任务类型FTP
	 */
	public static final String FTP = "ftp";
	/**
	 * 任务类型FILE
	 */
	public static final String FILE = "file";
	/**
	 * 字段映射常量标识;
	 */
	public static final String SOURCE_FIELD = "源字段";
	/**
	 * 字段映射常量标识;
	 */
	public static final String CHANGE_FIELD = "转换字段";
	/**
	 * 系统表名称;
	 */
    public static final String INDEX_TABLENAME = "system.indexes";
    /// <summary>
    /// profile表名称
    /// </summary>
    public static final String PROFILE_TABLENAME = "system.profile";
    
	public static final String THREAD = "thread";
	
	public static final Integer THREAD_NUM = 1000;
	
	public static final String TABLE_STRUCT = "tablestruct";
	
	public static final String IS_CLEAR = "is_clear";
	
	/**
	 * 关系型数据库各种字段映射;
	 */
	public static final String DEFAULT_VARCHAR_LENGTH = "255";//默认字符串长度
	
	public static final int MYSQL_VARCHAR_MAX_LENGTH = 21842;//默认字符串长度
	
	public static final String MYSQL_VARCHAR = "varchar";//MYSQL字符
	public static final String MYSQL_INT = "int";//MYSQL整数
	public static final String MYSQL_FLOAT = "double";//MYSQL浮点
	public static final String MYSQL_TEXT = "longtext";//MYSQL文本
	public static final String MYSQL_DATE = "datetime";//MYSQL日期
	public static final String MYSQL_BOOLEAN = "boolean";//MYSQL布尔
	public static final String MYSQL_BLOB = "longblob";//MYSQL的BLOB;
	
	public static final String ORACLE_VARCHAR2 = "varchar2";//ORACLE字符
	public static final String ORACLE_INT = "number";//ORACLE整数
	public static final String ORACLE_FLOAT = "number";//ORACLE浮点
	public static final String ORACLE_TEXT = "clob";//ORACLE文本
	public static final String ORACLE_DATE = "timestap";//ORACLE日期
	public static final String ORACLE_BOOLEAN = "number";//ORACLE布尔
	public static final String ORACLE_BLOB = "blob";//ORACLE的BLOB;
	
	
	public static final String SQLSERVER_VARCHAR = "varchar";//SQLSERVER字符
	public static final String SQLSERVER_INT = "int";//SQLSERVER整数
	public static final String SQLSERVER_FLOAT = "float";//SQLSERVER浮点
	public static final String SQLSERVER_TEXT = "text";//SQLSERVER文本
	public static final String SQLSERVER_DATE = "datetime";//SQLSERVER日期
	public static final String SQLSERVER_BOOLEAN = "bit";//SQLSERVER布尔
	public static final String SQLSERVER_BLOB = "image";//SQLSERVER的BLOB
	
	public static final String DB2_VARCHAR = "varchar";//DB2字符
	public static final String DB2_INT = "integer";//DB2整数
	public static final String DB2_FLOAT = "float";//DB2浮点
	public static final String DB2_TEXT = "clob";//DB2文本
	public static final String DB2_DATE = "timestamp";//DB2日期
	public static final String DB2_BOOLEAN = "smallint";//DB2布尔
	public static final String DB2_BLOB = "blob";//DB2的BLOB;
	
	public static final String TRS_NUMBER = "number";//数值型
	public static final String TRS_PHRASE = "phrase";//短语型
	public static final String TRS_CHAR = "char";//字符型
	public static final String TRS_DATE = "date";//日期型
	public static final String TRS_DOCUMENT = "document";//全文型
	public static final String TRS_BIT = "bit";//二进制型
	
	public static final String MONGO_VARCHAR = "STRING";//MONGO字符;
	
	public static final String CONTENT = "content";//文件内容;
	public static final String METADATA = "metadata";//元数据;
	public static final String FILE_SITE = "File Size";//文件大小;
	
	public static final String DISK_QUEUE_PATH = "diskQueuePath";//输出任务队列磁盘环境初始化路径;
	public static final String DISK_QUEUE_NAME = "diskQueueName";//输出任务磁盘队列名称;
	
	public static final String SOURCE_FIELDS = "sourcefields";//源数据字段;
	public static final String TARGET_FIELDS = "targetfields";//目标字段;
	public static final String BLOB_FIELDS = "blobfields";//blob大字段提取映射;
	public static final String BLOB_FIELD = "blobfield";//blob字段;
	
	public static final String META_DB_ID = "metaDbId";//元数据仓储库ID;
	public static final String MSS_TYPE = "msstype";//元数据仓储类型;
	
	public static final String IS_DIG = "is_dig";
	public static final String INCREMENT = "increment";//增量;
	public static final String INCREMENT_PRIMARY_FIELD = "primaryField";//增量主键字段;
	public static final String TRIGGER_STATUS = "trigger_status";//触发器增量状态;
	
	public static final String COMPLETED = "completed";//采集完成状态字段;
	public static final String SQLINDEX = "sqlIndex";//断点索引;
	
	public static final String TASK_URL = "task_url";//任务Url;
	public static final String SOURCE_URL = "source_url";//来源url;
	public static final String TARGET_NAME = "target_name";//目标名称;
	public static final String TARGET_STRUCT = "target_struct";//目标结构;
	public static final String TASK_STATUS = "task_status";//任务状态;
	public static final String ROOT_URL = "root_url";//多级关联时根Url
	
	public static final String LOCAL_PATH = "localPath";//Ftp下载路径;
	public static final String DATA_BYTE_SIZE = "data_byte_size";//FTP数据包大小;
	public static final String FILE_SIZE = "file_size";//Ftp文件大小限制
	public static final String SUB_DIRECTORY = "sub_directory";//FTP是否采集子目录;
	public static final String MATCH_FIELDS = "match_fields";//关联字段;
	public static final String APPENDIX_FIELDS = "appendix_fields";//附件字段;
	
	public static final String IS_MATCH = "is_match";//是否匹配;
	public static final String START_COUNT = "start_count";//启动次数;
	
	/**
	 * 采集任务日志类型 任务开始
	 */
	public static final String TASK_LOG_TYPE_START = "start";
	/**
	 * 采集任务日志类型 任务结束
	 */
	public static final String TASK_LOG_TYPE_END = "end";
	/**
	 * 采集任务日志类型 任务异常
	 */
	public static final String TASK_LOG_TYPE_ERROR = "error";
	/**
	 * 采集任务日志类型 任务执行成功
	 */
	public static final String TASK_LOG_TYPE_SUCCESS = "success";
	/**
	 * 采集任务日志级别 
	 */
	public static final String TASK_LOG_LEVEL_INFO = "info";
	/**
	 * 采集任务日志类型
	 */
	public static final String TASK_LOG_TYPE_RUNTIME = "runtime";

	
}
