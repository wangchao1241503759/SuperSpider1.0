package cn.com.infcn.ade.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.infcn.ade.common.persistence.HibernateDao;
import cn.com.infcn.ade.common.service.BaseService;
import cn.com.infcn.ade.system.dao.AreaInfoDao;
import cn.com.infcn.ade.system.model.AreaInfo;

/**
 * 区域service
 * @author kkomge
 * @date 2015年5月09日
 */
@Service
@Transactional(readOnly=true)
public class AreaInfoService extends BaseService<AreaInfo, Integer>{
	
	@Autowired
	private AreaInfoDao areaInfoDao;
	
	@Override
	public HibernateDao<AreaInfo, Integer> getEntityDao() {
		return areaInfoDao;
	}

}
