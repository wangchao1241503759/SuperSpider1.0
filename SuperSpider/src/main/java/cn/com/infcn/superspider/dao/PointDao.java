/**
 * 
 */
package cn.com.infcn.superspider.dao;

import org.springframework.stereotype.Repository;

import cn.com.infcn.ade.common.persistence.HibernateDao;
import cn.com.infcn.superspider.model.PointModel;

/**
 * @description 
 * @author WChao
 * @date   2015年12月29日 	下午7:05:56
 */
@Repository
public class PointDao extends HibernateDao<PointModel, String> {

}
