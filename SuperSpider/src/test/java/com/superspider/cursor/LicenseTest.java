package com.superspider.cursor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
//import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


//import java.util.concurrent.TimeUnit;

//import java.sql.ResultSetMetaData;

//import com.alibaba.fastjson.JSONObject;


public class LicenseTest {
	
	public static ScheduledExecutorService syncService ;
	
	public static void main(String[] args) {
		try {
			/*syncService = Executors.newSingleThreadScheduledExecutor();
	        syncService.scheduleAtFixedRate(new Runnable() {
	            @Override
	            public void run() {
		              System.out.println("lsdkfsdf");
	            }
	        }, 1000, 10000, TimeUnit.MILLISECONDS);*/
			//String sql = "select '篇名','作者' FROM spider_87fd77cd7b60499db18970604a6ea5f2_CJF_ALL t ";
			//String sql = "select CN from CJF_ALL";
			String sql = "select * from 249b7185f69d4d839a1188287db5b1bd_2 where task_status = '0';";
			//String sql = "select \"专题名称\" FROM CJFXLAST t";
			//String sql = "select * from (select docid,Author_E,ROW_NUMBER() OVER ( ORDER BY (SELECT 0) ) as rownumber FROM ZWQK_tbl) t  WHERE t.rownumber BETWEEN "+0+" AND "+1000;
			//String sql = "select * from (select 作者,机构,中文关键词,中文摘要,英文篇名,英文作者,英文摘要,英文关键词,基金,文献号,中文刊名,拼音刊名,英文刊名,年,期,CN,ISSN,文件名,页,专题代码,子栏目代码,专题名称,专辑代码,更新日期,专题子栏目代码,第一责任人,SCI收录刊,EI收录刊,核心期刊,中英文篇名,复合关键词,中英文摘要,主题,出版日期,导航代码,机标关键词,SYS_VSM,来源数据库,页数,文件大小,中英文作者,中英文刊名,DOI,作者简介,文章属性,docid,fenleihao,fenleiming,parents,ROW_NUMBER() OVER ( ORDER BY (SELECT 0) ) as rownumber FROM cnki_CJFD1999_ztfl) t ";
			//String mysql = "select * from sqlserver_youhua_3000";
			//String oraclesql = "SELECT t.* from \"qianwan\" t where ROWNUM >= 0 and ROWNUM <= "+Integer.MAX_VALUE;
			//BETWEEN
			//String db2sql = "SELECT * FROM (SELECT t.*,rownumber() over() as rn from \"jjj\" t ) as table_alias where table_alias.rn BETWEEN "+600000+" AND "+Integer.MAX_VALUE;
			Class.forName("com.mysql.jdbc.Driver");
			//Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			//String sql2= "select \"doc_source\",\"qk_name\",\"st_issue_date\",\"qk_roll\",\"qk_time\",\"title\",\"author\",\"keywords\",\"abstract\",\"db_source\",\"qk_doi\",\"doc_id\",\"sub_year\",\"haspdf\",\"rownumber\" FROM spider_5b9ece9d8a5540fa95b3b180a73acd93_View_es t WHERE t.rownumber > 0 and t.rownumber < 100000";//  WHERE t.rownumber > 0 and t.rownumber < 100000
			//Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			//Class.forName("oracle.jdbc.driver.OracleDriver");
			//Class.forName("com.ibm.db2.jcc.DB2Driver");
			//String dburl = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ=Mobile.mdb";
			//DriverManager.registerDriver(new JdbcOdbcDriver());
			//Connection conn = DriverManager.getConnection("jdbc:sqlserver://192.168.3.169:1433;databaseName=student;selectMethod=cursor","sa","98765432");
			//Connection conn = DriverManager.getConnection("jdbc:sqlserver://192.168.10.45:1433;databaseName=VipData;selectMethod=cursor","sa","sa");
			//Connection conn = DriverManager.getConnection("jdbc:sqlserver://192.168.10.159:1433;databaseName=FSSP;selectMethod=cursor","sa","infcn@123");
		    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/superspider_datas?useUnicode=true&characterEncoding=utf-8","root","123");
			//Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.3.113:1521:ORCL","infcn","123");
			//Connection conn = DriverManager.getConnection("jdbc:db2://192.168.3.93:50000/center","root","98765432");
			//PreparedStatement pst = conn.prepareStatement(sql);
			//com.microsoft.sqlserver.jdbc.SQLServerStatement createStatement = (com.microsoft.sqlserver.jdbc.SQLServerStatement) conn.createStatement() ;
		    long start = System.currentTimeMillis();
		    //String sql2 ="IF EXISTS(SELECT 1 FROM sys.views WHERE name='spider_5b9ece9d8a5540fa95b3b180a73acd93_View_es') DROP VIEW spider_5b9ece9d8a5540fa95b3b180a73acd93_View_es";
		    Statement createStatement = conn.createStatement();
		   // PreparedStatement pst = conn.prepareStatement(sql);
		    //pst.executeUpdate();
		    //pst.setFetchSize(Integer.MIN_VALUE);
		    /* pst.setFetchDirection(ResultSet.FETCH_FORWARD);*/
			//ResultSet rs = pst.executeQuery();
			ResultSet rs = createStatement.executeQuery(sql);
			int n= 10000000;
			int m = 0;
			/*ResultSetMetaData rsmd = rs.getMetaData();// rs为查询结果集
			int count = rsmd.getColumnCount();*/
			//System.out.println("耗时:"+(System.currentTimeMillis()-start)/1000+"秒!");
			System.out.println((m++)+"=耗时:"+(System.currentTimeMillis()-start)/1000+"秒!");
			while(rs.next()){
				/*JSONObject jsonObj = new JSONObject();
				for (int i = 1; i <= count; i++) {
					String columnName = rsmd.getColumnName(i);
					jsonObj.put(columnName, rs.getObject(columnName));
				}*/
				//System.out.println(rs.getString(2));
				System.out.println((m++)+"=耗时:"+(System.currentTimeMillis()-start)/1000+"秒!");
				//System.out.println(rs.getString(1)+rs.getString(2)+rs.getString(3)+rs.getString(4)+rs.getString(5)+rs.getString(6));
				if(n--<=0){
					rs.close();
					break ;
				}
			}
			System.out.println("耗时:"+(System.currentTimeMillis()-start)/1000+"秒!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*System.out.println(CommonTools.getMachinecode());
		 
		SEI3DBLicense license1 = new SEI3DBLicense();
		
		
		try {
			
			//license1.loadFromLicenseFile("");
			
			//license1.loadSN("8a1c9e56979c9f7d7bc1c8ec71e2188ef3885591c03c7e41c4c6c0f7e1e4db552fd9e67dfdfe9a0ed13d89539f45b142ccebb91dbf23c6d6afdbedef7a6c983a#84fe0ce020edc66e78e847293e080cd7e4bfc2450b77e10c5ec69e8b583b74a73e2eb48981eb6fce0b0b33b8c980394a825d23df6022c3d91d544f2c754ed6b6");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		
		System.out.println(license1);*/
		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = sdf.parse("2017-01-01 00:01:02");
//        SEILicense seiLicense = new SEILicense().new SEIL;
//        SEILicense.
//        // 数据库相关的License控制信息
//        seiLicense.getDbLicenseParam();
//        SE dbLicenseParam = seiLicense.getDbLicenseParam();
//        dbLicenseParam.setExpireDate(date);
//        Map<String, Object> dbSupport = new HashMap<String, Object>();
//        dbSupport.put("oracle", 2);
//        dbSupport.put("mysql", 2);
//        dbLicenseParam.setDbSupport(dbSupport);
//
//        // 文件相关的控制信息
//        fileLicenseParam = new FileLicenseParam();
//        fileLicenseParam.setExpireDate(date);
//        fileLicenseParam.setMaxDirectory(10);
//        fileLicenseParam.setMaxFile(50);
//        fileLicenseParam.setMaxDirectoryLevel(2);
//
//        // 网络采集相关的控制信息
//        webLicenseParam = new WebLicenseParam();
//        webLicenseParam.setExpireDate(date);
//        webLicenseParam.setMaxPageLevel(10);
//        webLicenseParam.setMaxWebSource(2);
//
//        // FTP相关的控制信息
//        ftpLicenseParam = new FileLicenseParam();
//        ftpLicenseParam.setExpireDate(date);
//        ftpLicenseParam.setMaxDirectory(10);
//        ftpLicenseParam.setMaxFile(50);
//        ftpLicenseParam.setMaxDirectoryLevel(2);

	}
}
