/**
 * 
 */
package cn.com.infcn.superspider.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.infcn.ade.common.persistence.HibernateDao;
import cn.com.infcn.ade.common.service.BaseService;
import cn.com.infcn.superspider.dao.FilePubAttributeDao;
import cn.com.infcn.superspider.model.FilePubAttribute;
import cn.com.infcn.superspider.service.FilePubAttributeServiceI;

/**
 * @description 
 * @author WChao
 * @date   2016年2月23日 	下午1:46:58
 */
@Service
@Transactional(readOnly=true)
public class FilePubArributeServiceImpl extends BaseService<FilePubAttribute, String> implements FilePubAttributeServiceI {

	@Autowired
	private FilePubAttributeDao filePubAttributeDao;
	
	@Override
	public HibernateDao<FilePubAttribute, String> getEntityDao() {
		
		return filePubAttributeDao;
	}

	
}
