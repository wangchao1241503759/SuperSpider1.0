/**
 * 
 */
package cn.com.infcn.superspider.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.infcn.ade.common.persistence.HibernateDao;
import cn.com.infcn.ade.common.service.BaseService;
import cn.com.infcn.superspider.dao.PointDao;
import cn.com.infcn.superspider.model.PluginModel;
import cn.com.infcn.superspider.model.PointModel;
import cn.com.infcn.superspider.service.PointServiceI;

/**
 * @description 
 * @author WChao
 * @date   2015年12月29日 	下午7:05:05
 */
@Service
@Transactional(readOnly=true)
public class PointServiceImpl extends BaseService<PointModel,String> implements PointServiceI {

	@Autowired
	private PointDao pointDao;
	
	@Override
	public HibernateDao<PointModel, String> getEntityDao() {
		return pointDao;
	}

	@Override
	public List<PointModel> findPointByPlugin(PluginModel pluginModel)throws Exception {
		String hql = "from PointModel t where t.pluginId = ?0";
		return pointDao.find(hql, pluginModel.getId());
	}
}
