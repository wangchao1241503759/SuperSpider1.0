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
import cn.com.infcn.superspider.dao.PluginDao;
import cn.com.infcn.superspider.model.PluginModel;
import cn.com.infcn.superspider.service.PluginServiceI;

/**
 * @description 
 * @author WChao
 * @date   2015年12月29日 	下午7:04:05
 */
@Service
@Transactional(readOnly=true)
public class PluginServiceImpl extends BaseService<PluginModel, String> implements PluginServiceI {

	@Autowired
	private PluginDao pluginDao;
	
	@Override
	public HibernateDao<PluginModel, String> getEntityDao() {
		return pluginDao;
	}

	@Override
	public List<PluginModel> findPluginByType(String type) throws Exception {
		String hql = "from PluginModel t where t.type = ?0";
		return pluginDao.find(hql, type);
	}
}
