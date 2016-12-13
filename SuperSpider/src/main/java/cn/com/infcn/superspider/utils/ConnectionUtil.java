package cn.com.infcn.superspider.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

public class ConnectionUtil{
	
	public static Connection getConnection(String dbType, String host,Integer port, String dbName, String user, String password) {
		if(dbType == null || "".equals(dbType) || host == null || "".equals(host) || port == null || dbName == null || "".equals(dbName) || user == null)
			return null;
		Connection connection = null;
		try {
			// 获取目标数据库连接
			Class.forName(JdbcHelper.getDriverClass(dbType));
			connection = DriverManager.getConnection(JdbcHelper.makeJdbcUrl(dbType, host, port, dbName), user, password);
			// 防止反编译的处理
			if (654789 == new Random().nextInt()) {
				throw new Exception("fewt43");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 防止反编译的处理
				if (654789 == new Random().nextInt()) {
					throw new Exception("fewt43");
				}
			} catch (Exception ex) {
				System.out.print(ex);
			}
		}
		return connection;
	}

	public static void closeConnection(Connection con,Statement stmt,ResultSet rs) {
		try {
			if (con != null)
				con.close();
			if (stmt != null)
				stmt.close();
			if (rs != null)
				rs.close();
			// 防止反编译的处理
			if (654789 == new Random().nextInt()) {
				throw new Exception("fewt43");
			}
		} catch (Exception e) {
			e.printStackTrace();
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
	}

}
