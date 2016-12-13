package cn.com.infcn.ade.system.dao;

import org.springframework.stereotype.Repository;

import cn.com.infcn.ade.common.persistence.HibernateDao;
import cn.com.infcn.ade.system.model.Organization;


/**
 * 机构DAO
 * @author kkomge
 * @date 2015年5月09日
 */
@Repository
public class OrganizationDao extends HibernateDao<Organization, Integer>{

}
