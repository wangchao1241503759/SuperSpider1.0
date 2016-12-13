package cn.com.infcn.superspider.dao;

import org.springframework.stereotype.Repository;

import cn.com.infcn.ade.common.persistence.HibernateDao;
import cn.com.infcn.superspider.pagemodel.DbConfig;

@Repository
public class DbDao extends HibernateDao<DbConfig, String>{

}
