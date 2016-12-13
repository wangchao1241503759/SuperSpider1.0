package cn.com.infcn.superspider.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.infcn.ade.common.persistence.HibernateDao;
import cn.com.infcn.ade.common.service.BaseService;
import cn.com.infcn.superspider.dao.FieldMappingDao;
import cn.com.infcn.superspider.model.FieldMapping;
import cn.com.infcn.superspider.model.Task;
import cn.com.infcn.superspider.service.DbFieldMappingServiceI;
import cn.com.infcn.superspider.utils.StringUtil;

/**
 * @author WChao
 * @date 2015年12月28日
 */
@Service(value="fieldMappingService")
@Transactional(readOnly=true)
public class DbFieldMappingServiceImpl extends BaseService<FieldMapping, String> implements DbFieldMappingServiceI {
	
	@Autowired
	private FieldMappingDao fieldMappingDao;
	
	@Override
	public HibernateDao<FieldMapping, String> getEntityDao() {
		
		return fieldMappingDao;
	}

	@Transactional(readOnly=false)
	public void saveBatch(List<FieldMapping> fieldList,Task task) throws Exception {
		deleteByTaskId(task.getTaskId());//删除原来的;
		if(fieldList != null && fieldList.size() > 0){
			for(FieldMapping fieldMapping : fieldList){
				if(fieldMapping.getFieldId() == null || "".equals(fieldMapping.getFieldId()))
					fieldMapping.setFieldId(StringUtil.generateUUID());
				if(task != null)
					fieldMapping.setTaskId(task.getTaskId());
				this.save(fieldMapping);
			}
		}
	}

	@Override
	@Transactional(readOnly=false)
	public void deleteByTaskId(String taskId) throws Exception {
		String hql = "delete from FieldMapping t where t.taskId = ?0";
		fieldMappingDao.executeHql(hql, taskId);
	}

	@Override
	public List<FieldMapping> findByTaskId(String taskId) throws Exception {
		String hql = "from FieldMapping t where t.taskId = ?0 order by t.fieldOrdinalPosition asc";
		return fieldMappingDao.find(hql, taskId);
	}

	@Override
	@Transactional(readOnly=false)
	public void saveList(List<FieldMapping> fieldList) throws Exception {
		for(FieldMapping fieldMapping : fieldList){
			this.save(fieldMapping);
		}
	}
}
