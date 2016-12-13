package cn.com.infcn.ade.system.dao;

import org.springframework.stereotype.Repository;

import cn.com.infcn.ade.common.persistence.HibernateDao;
import cn.com.infcn.ade.system.model.Dict;

/**
 * 字典DAO
 * @author WChao
 * @date 2015年1月13日
 */
@Repository
public class DictDao extends HibernateDao<Dict, Integer>{

}
