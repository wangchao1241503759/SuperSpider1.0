/**
 * 
 */
package cn.com.infcn.superspider.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.infcn.ade.common.persistence.HibernateDao;
import cn.com.infcn.ade.common.service.BaseService;
import cn.com.infcn.superspider.dao.FileContentMappingDao;
import cn.com.infcn.superspider.model.FileContentMapping;
import cn.com.infcn.superspider.model.Task;
import cn.com.infcn.superspider.pagemodel.FileContentMappingAttribute;
import cn.com.infcn.superspider.pagemodel.FileTypeAttribute;
import cn.com.infcn.superspider.service.FileContentMappingServiceI;
import cn.com.infcn.superspider.utils.StringUtil;

/**
 * @description 
 * @author WChao
 * @date   2016年1月25日 	下午3:58:49
 */
@Service
@Transactional(readOnly=true)
public class FileContentMappingServiceImpl extends BaseService<FileContentMapping, String> implements FileContentMappingServiceI {

	@Autowired
	private FileContentMappingDao fileContentMappingDao;
	@Autowired
	private FilePubArributeServiceImpl filePubAttributeService;
	@Autowired
	private FileAttributeServiceImpl fileAttributeService;
	
	@Override
	@Transactional(readOnly=false)
	public void saveBatch(List<FileContentMapping> attributeList, Task task)throws Exception {
		deleteByTaskId(task.getTaskId());//删除原来的;
		if(attributeList != null && attributeList.size() > 0){
			for(FileContentMapping fileContentMapping : attributeList){
				if(fileContentMapping.getId() == null || "".equals(fileContentMapping.getId()))
					fileContentMapping.setId(StringUtil.generateUUID());
				if(task != null)
					fileContentMapping.setTaskId(task.getTaskId());
				this.save(fileContentMapping);
			}
		}
		
	}

	@Override
	@Transactional(readOnly=false)
	public void deleteByTaskId(String taskId) throws Exception {
		String hql = "delete from FileContentMapping t where t.taskId = ?0";
		fileContentMappingDao.executeHql(hql, taskId);
	}
	
	@Override
	public List<FileContentMapping> findContentMappingByTaskId(String taskId) {
		 String hql = "from FileContentMapping t where t.taskId = ?0";
		 List<FileContentMapping> list =  fileContentMappingDao.find(hql,taskId);
		return list;
	}
	
	@Override
	public List<FileContentMappingAttribute> findFormatContentMappingByTaskId(String taskId) {
		 String hql = "from FileContentMapping t where t.taskId = ?0";
		 List<FileContentMapping> list =  fileContentMappingDao.find(hql,taskId);
		 List<FileContentMappingAttribute> returnDatas = new ArrayList<FileContentMappingAttribute>();
		if(list != null && list.size() >0 ){
			for(FileContentMapping fileContentMapping : list){
				FileContentMappingAttribute fileContentMappingAttribute = new FileContentMappingAttribute();
				BeanUtils.copyProperties(fileContentMapping, fileContentMappingAttribute);
				FileTypeAttribute fileTypeAttribute = fileAttributeService.getAttributeJsonById(fileContentMapping.getFileAttributeId());
				if(fileTypeAttribute != null){
					BeanUtils.copyProperties(fileTypeAttribute, fileContentMappingAttribute);
				}
				returnDatas.add(fileContentMappingAttribute);
			}
		}
		return returnDatas;
	}
	@Override
	public HibernateDao<FileContentMapping, String> getEntityDao() {
		return fileContentMappingDao;
	}

	@Override
	@Transactional(readOnly=false)
	public void saveList(List<FileContentMapping> fileContentMappings)throws Exception {
		for(FileContentMapping fileContentMapping : fileContentMappings){
			this.save(fileContentMapping);
		}
	}

}
