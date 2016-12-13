/**
 * 
 */
package cn.com.infcn.superspider.dao;

import org.springframework.stereotype.Repository;

import cn.com.infcn.ade.common.persistence.HibernateDao;
import cn.com.infcn.superspider.model.SchedulePlan;

/**
 * @description 
 * @author WChao
 * @date   2016年3月2日 	下午6:01:54
 */
@Repository(value="schedulePlanDao")
public class SchedulePlanDao extends HibernateDao<SchedulePlan, String>{

}
