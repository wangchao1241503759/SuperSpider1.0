/**
 * 
 */
package cn.com.infcn.superspider.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.com.infcn.ade.common.persistence.HibernateDao;
import cn.com.infcn.superspider.model.Trigger;

/**
 * @description 
 * @author WChao
 * @date   2016年2月19日 	上午9:54:32
 */
@Repository
@Transactional(readOnly=false)
public class TriggerDao extends HibernateDao<Trigger, String>{

}
