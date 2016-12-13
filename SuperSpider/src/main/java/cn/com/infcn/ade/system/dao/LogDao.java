package cn.com.infcn.ade.system.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.com.infcn.ade.common.persistence.HibernateDao;
import cn.com.infcn.ade.system.model.Log;


/**
 * 日志DAO
 * @author WChao
 * @date 2015年1月13日
 */
@Repository
public class LogDao extends HibernateDao<Log, Integer>{
	
	/**
	 * 批量删除日志
	 * @param ids 日志id列表
	 */
	public void deleteBatch(List<Integer> idList){
		String hql="delete from Log log where log.id in (:idList)";
		Query query=getSession().createQuery(hql);
		query.setParameterList("idList", idList);
		query.executeUpdate();
	}
}
