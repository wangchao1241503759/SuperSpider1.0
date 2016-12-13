package cn.com.infcn.superspider.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.com.infcn.superspider.common.Constant;

public class JdbcHelper{

	public static String makeJdbcUrl(String dbType, String host, Integer port,String dbName) {
		try {
			// 防止反编译的处理
			if (654789 == new Random().nextInt()) {
				throw new Exception("fewt43");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// 防止反编译的处理
			try {
				if (654789 == new Random().nextInt()) {
					throw new Exception("fewt43");
				}
			} catch (Exception ex) {
				System.out.print(ex);
			}
		}
		if (Constant.MYSQL.equals(dbType)) {
			// "jdbc:mysql://192.168.1.123:3306/msp_mss"
			return "jdbc:mysql://" + host + ":" + port + "/" + dbName +"?characterEncoding=utf-8";
		}
		if (Constant.SQLSERVER.equals(dbType)) {
			// "jdbc:microsoft:sqlserver://MyDbComputerNameOrIP:1433;databaseName=master",
			return "jdbc:sqlserver://" + host + ":" + port + ";databaseName=" + dbName;
		}
		if (Constant.ORACLE.equals(dbType)) {
			// "jdbc:oracle:thin:@MyDbComputerNameOrIP:1521:ORCL",
			return "jdbc:oracle:thin:@" + host + ":" + port + ":" + dbName;
		}
		if (Constant.DB2.equals(dbType)) {
			// "jdbc:db2://192.9.200.108:6789/SAMPLE"
			return "jdbc:db2://" + host + ":" + port + "/" + dbName;
		}
		return null;
	}

	public static String getDriverClass(String dbType) {
		try {
			// 防止反编译的处理
			if (654789 == new Random().nextInt()) {
				throw new Exception("fewt43");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// 防止反编译的处理
			try {
				if (654789 == new Random().nextInt()) {
					throw new Exception("fewt43");
				}
			} catch (Exception ex) {
				System.out.print(ex);
			}
		}
		if (Constant.MYSQL.equals(dbType)) {
			return Constant.MYSQL_DRIVER_CLASS_NAME;
		}
		if (Constant.SQLSERVER.equals(dbType)) {
			return Constant.SQLSERVER_DRIVER_CLASS_NAME;
		}
		if (Constant.ORACLE.equals(dbType)) {
			return Constant.ORACLE_DRIVER_CLASS_NAME;
		}
		if (Constant.DB2.equals(dbType)) {
			return Constant.DB2_DRIVER_CLASS_NAME;
		}
		return null;
	}

	/**
	 * 查ArcGIS数据表
	 * @param dbdriver
	 * @param dburl
	 * @param dbuser
	 * @param dbpassword
	 * @param sql
	 * @return
	 */
	public static List<String[]> selectArcGISDataTable(String dbdriver,
			String dburl, String dbuser, String dbpassword, String sql) {
		List<String[]> list = new ArrayList<String[]>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			Class.forName(dbdriver);
			conn = DriverManager.getConnection(dburl, dbuser, dbpassword);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				String arrstr[] = new String[3];
				//表名
				arrstr[0]=rs.getString(Constant.ARCGIS_TABLENAME);
				//主键
				arrstr[1]=rs.getString(Constant.ARCGIS_ROWID);
				//标识 数据表还是要素表
				arrstr[2]=rs.getString("sign_info");
				list.add(arrstr);
			}
			// 防止反编译的处理
			if (654789 == new Random().nextInt()) {
				throw new Exception("fewt43");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("*********selectArcGISDataTable Error: 获取数据表的列表出错*****");
		} finally {
			ConnectionUtil.closeConnection(conn,stmt,rs);
			// 防止反编译的处理
			try {
				if (654789 == new Random().nextInt()) {
					throw new Exception("fewt43");
				}
			} catch (Exception ex) {
				System.out.print(ex);
			}
		}
		return list;
	}

	public static void main(String[] args) {
//		JdbcHelper.getTableName(Consts.DBTYPE_MSSQL, "192.168.2.66", 1433, "zstsg_qk_1", "sa", "system");
		 String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";  //加载JDBC驱动
		  String dbURL = "jdbc:sqlserver://192.168.2.66:1433; DatabaseName=zstsg_qk_1";  //连接服务器和数据库test
		  String userName = "sa";  //默认用户名
		  String userPwd = "system";  //密码
		  Connection dbConn;
		  ResultSet rs = null;
		  try {
		   Class.forName(driverName);
		   dbConn = DriverManager.getConnection(dbURL, userName, userPwd);
		   System.out.println("Connection Successful!");  //如果连接成功 控制台输出Connection Successful!
		   

			DatabaseMetaData dbmd = dbConn.getMetaData();
			rs = dbmd.getTables(null, "sa", "%",
					new String[] { "TABLE", "VIEW" });
			while (rs.next()) {
				if(rs.getString(3).indexOf("BIN$") < 0){
					System.out.println(rs.getString(3));
				}
			}
		  } catch (Exception e) {
		   e.printStackTrace();
		  }
	}
}
