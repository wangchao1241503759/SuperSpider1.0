package cn.com.infcn.ade.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.infcn.ade.common.persistence.HibernateDao;
import cn.com.infcn.ade.common.service.BaseService;
import cn.com.infcn.ade.system.dao.LogDao;
import cn.com.infcn.ade.system.model.Log;

/**
 * 日志service
 * @author WChao
 * @date 2015年1月14日
 */
@Service
@Transactional(readOnly=true)
public class LogService extends BaseService<Log, Integer> {
	
	@Autowired
	private LogDao logDao;
	
	@Override
	public HibernateDao<Log, Integer> getEntityDao() {
		return logDao;
	}
	
	/**
	 * 批量删除日志
	 * @param idList
	 */
	@Transactional(readOnly=false)
	public void deleteLog(List<Integer> idList){
		logDao.deleteBatch(idList);
	}
	
}
