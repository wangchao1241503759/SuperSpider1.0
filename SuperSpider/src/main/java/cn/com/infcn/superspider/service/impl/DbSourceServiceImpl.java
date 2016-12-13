/**
 * 
 */
package cn.com.infcn.superspider.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.infcn.ade.common.persistence.HibernateDao;
import cn.com.infcn.ade.common.service.BaseService;
import cn.com.infcn.superspider.common.Constant;
import cn.com.infcn.superspider.dao.DbSourceDao;
import cn.com.infcn.superspider.model.DbSource;
import cn.com.infcn.superspider.service.DbSourceServiceI;
import cn.com.infcn.superspider.utils.ConnectionUtil;

/**
 * @description 
 * @author WChao
 * @date   2015年12月21日 	下午6:27:48
 */
@Service(value="dbSourceService")
@Transactional(readOnly=true)
public class DbSourceServiceImpl extends BaseService<DbSource,String> implements DbSourceServiceI {
	
	@Autowired
	private DbSourceDao dbSourceDao;
	@Override
	public HibernateDao<DbSource, String> getEntityDao() {
		
		return dbSourceDao;
	}

	@Override
	public List<String> getAllTables(DbSource dbSource) throws Exception {
		List<String> tables = new ArrayList<String>();
		if(Constant.MYSQL.equals(dbSource.getDsType()) || Constant.SQLSERVER.equals(dbSource.getDsType()) || Constant.ORACLE.equals(dbSource.getDsType()) || Constant.DB2.equals(dbSource.getDsType())){
			Connection conn = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
			conn = ConnectionUtil.getConnection(dbSource.getDsType(),dbSource.getDsHost(),dbSource.getDsPort(),dbSource.getDsDbName(),dbSource.getDsUserName(), dbSource.getDsPassWord());
	        String sql = "";
	        ResultSet rs2 = conn.getMetaData().getTables(conn.getCatalog(),null,"%",null);
	        while(rs2.next())
	        {
	        	System.out.println(rs2.getString(0));
			}
	        if(Constant.ORACLE.equals(dbSource.getDsType()))//Oracle数据库;
	        {
	        	sql = "SELECT TABLE_NAME as name FROM USER_TABLES";
	        }else{
	        	sql = "SELECT TABLE_NAME as name FROM information_schema.tables WHERE table_schema = '"+dbSource.getDsDbName()+"'";
	        }
	        pstmt = conn.prepareStatement(sql);
	        rs = pstmt.executeQuery();
	        while(rs.next()){
	        	tables.add(rs.getString("name"));
	        }
	        ConnectionUtil.closeConnection(conn, pstmt, rs);
		}
		return tables;
	}

	@Override
	@Transactional(readOnly=false)
	public void deleteByTaskId(String taskId) throws Exception {
		String hql = "delete from DbSource t where t.taskId = ?0";
		dbSourceDao.executeHql(hql, taskId);
	}

	@Override
	public DbSource getByTaskId(String taskId) throws Exception {
		String hql = "from DbSource t where t.taskId = ?0";
		return dbSourceDao.get(hql, taskId);
	}

	@Override
	@Transactional(readOnly=false)
	public void saveList(List<DbSource> dbSources) throws Exception {
		for(DbSource dbSource : dbSources){
			this.save(dbSource);
		}
	}
}
