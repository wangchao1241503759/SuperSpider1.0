package cn.com.infcn.ade.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.infcn.ade.common.persistence.HibernateDao;
import cn.com.infcn.ade.common.service.BaseService;
import cn.com.infcn.ade.system.dao.RoleDao;
import cn.com.infcn.ade.system.model.Role;

/**
 * 角色service
 * @author WChao
 * @date 2015年1月13日
 */
@Service
@Transactional(readOnly = true)
public class RoleService extends BaseService<Role, Integer> {

	@Autowired
	private RoleDao roleDao;

	@Override
	public HibernateDao<Role, Integer> getEntityDao() {
		return roleDao;
	}
}
