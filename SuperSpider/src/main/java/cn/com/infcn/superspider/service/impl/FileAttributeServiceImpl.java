/**
 * 
 */
package cn.com.infcn.superspider.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.infcn.ade.common.persistence.HibernateDao;
import cn.com.infcn.ade.common.service.BaseService;
import cn.com.infcn.superspider.dao.FileAttributeDao;
import cn.com.infcn.superspider.dao.FilePubArributeAttributeDao;
import cn.com.infcn.superspider.model.FileAttribute;
import cn.com.infcn.superspider.model.FilePubArributeAttribute;
import cn.com.infcn.superspider.pagemodel.FileTypeAttribute;
import cn.com.infcn.superspider.service.FileAttributeServiceI;

/**
 * @description 
 * @author WChao
 * @date   2016年1月25日 	下午3:17:02
 */
@Service
@Transactional(readOnly=true)
public class FileAttributeServiceImpl extends BaseService<FileAttribute, String> implements FileAttributeServiceI {

	@Autowired
	private FileAttributeDao fileAttributeDao;
	@Autowired
	private FilePubArributeAttributeDao filePubAttributeAtributeDao;
	
	@Override
	public HibernateDao<FileAttribute, String> getEntityDao() {
		
		return fileAttributeDao;
	}

	@Override
	public List<FileAttribute> findAllAttribute() {
		return fileAttributeDao.findAll();
	}

	@Override
	public List<Map<String,Object>> findAttributeByDefault(String attributeDefault) {
		//此处不能用HQL做LEFT JOIN关联查询;
		String sql = "SELECT a.id AS arributeId ,a.attribute_name AS attributeName,d.id AS fileTypeId,d.file_type_value  AS fileTypeValue,"
				+ "d.file_type_name AS fileTypeName,a.is_default AS isDefault FROM file_pub_attribute a "
				+ "LEFT JOIN  file_pubattribute_attribute b ON a.id = b.pub_id "
				+ "LEFT JOIN  file_attribute c ON c.`id` = b.`attribute_id` "
				+ "LEFT JOIN  file_type d ON c.type_id = d.id "
				+ "WHERE  a.is_default = '"+attributeDefault+"';";
		List<FileAttribute> list = fileAttributeDao.executeListSql(sql);
		List<Map<String,Object>> returnList = null;
		if(list != null && list.size() > 0)
		{
			Map<String,Map<String,Object>>  tempMap = new HashMap<String,Map<String,Object>>();
			returnList = new ArrayList<Map<String,Object>>();
			for(Object attributeInfo : list)
			{
				Object[] attributeArray = (Object[])attributeInfo;
				String attributeId = attributeArray[0] == null ? "" : attributeArray[0].toString();
				String attributeName = attributeArray[1] == null ? "" : attributeArray[1].toString();
				String fileTypeValue = attributeArray[3] == null ? "" :attributeArray[3].toString();
				String isDefault = attributeArray[5] == null ? "" :attributeArray[5].toString();
				if(tempMap.get(attributeId) == null){
					Map<String,Object> attributes = new HashMap<String,Object>();
					attributes.put("fileAttributeId",attributeId);
					attributes.put("fileAttributeName", attributeName);
					attributes.put("fileTypeValue",fileTypeValue);
					attributes.put("isDefault",isDefault);
					tempMap.put(attributeId, attributes);
					returnList.add(attributes);
				}else{
					if("".equals(fileTypeValue))
						continue;
						if("".equals(tempMap.get(attributeId).get("fileTypeValue"))){
							tempMap.get(attributeId).put("fileTypeValue",tempMap.get(attributeId).get("fileTypeValue")+","+fileTypeValue);	
						}else{
							String[] typeValueArr = tempMap.get(attributeId).get("fileTypeValue").toString().split(",");
							boolean isHave = false;
							for(String value : typeValueArr)
							{
								if(value.equals(fileTypeValue))
								{
									isHave = true;
									break;
								}
							}
							if(!isHave)
							tempMap.get(attributeId).put("fileTypeValue",tempMap.get(attributeId).get("fileTypeValue")+","+fileTypeValue);	
						}
				}
			}
		}
		return returnList;
	}
	
	@Override
	public List<FileTypeAttribute> findAttributeJsonListDefault(String attributeDefault) {
		//此处不能用HQL做LEFT JOIN关联查询;
		String sql = "SELECT a.id AS arributeId ,a.attribute_name AS attributeName,d.id AS fileTypeId,d.file_type_value  AS fileTypeValue,"
				+ "d.file_type_name AS fileTypeName,a.is_default AS isDefault FROM file_pub_attribute a "
				+ "LEFT JOIN  file_pubattribute_attribute b ON a.id = b.pub_id "
				+ "LEFT JOIN  file_attribute c ON c.`id` = b.`attribute_id` "
				+ "LEFT JOIN  file_type d ON c.type_id = d.id "
				+ "WHERE  a.is_default = '"+attributeDefault+"';";
		List<FileAttribute> list = fileAttributeDao.executeListSql(sql);
		List<FileTypeAttribute> returnList = null;
		if(list != null && list.size() > 0)
		{
			Map<String,FileTypeAttribute>  tempMap = new HashMap<String,FileTypeAttribute>();
			returnList = new ArrayList<FileTypeAttribute>();
			for(Object attributeInfo : list)
			{
				
				Object[] attributeArray = (Object[])attributeInfo;
				String attributeId = attributeArray[0] == null ? "" : attributeArray[0].toString();
				String attributeName = attributeArray[1] == null ? "" : attributeArray[1].toString();
				String fileTypeId = attributeArray[2] == null ? "" : attributeArray[2].toString();
				String fileTypeValue = attributeArray[3] == null ? "" :attributeArray[3].toString();
				String fileTypeName = attributeArray[4] == null ? "" : attributeArray[4].toString();
				String isDefault = attributeArray[5] == null ? "" : attributeArray[5].toString();
				
				if(tempMap.get(attributeId) == null){
					FileTypeAttribute fileTypeAttribute = new FileTypeAttribute();
					fileTypeAttribute.setFileAttributeId(attributeId);
					fileTypeAttribute.setFileAttributeName(attributeName);
					fileTypeAttribute.setFileTypeId(fileTypeId);
					fileTypeAttribute.setFileTypeName(fileTypeName);
					fileTypeAttribute.setFileTypeValue(fileTypeValue);
					fileTypeAttribute.setIsDefault(isDefault);
					tempMap.put(attributeId, fileTypeAttribute);
					returnList.add(fileTypeAttribute);
				}else{
					if("".equals(fileTypeValue))
					continue;
					if("".equals(tempMap.get(attributeId).getFileTypeValue())){
						tempMap.get(attributeId).setFileTypeValue(tempMap.get(attributeId).getFileTypeValue()+","+fileTypeValue);
					}else{
						String[] typeValueArr = tempMap.get(attributeId).getFileTypeValue().split(",");
						boolean isHave = false;
						for(String value : typeValueArr)
						{
							if(value.equals(fileTypeValue))
							{
								isHave = true;
								break;
							}
						}
						if(!isHave)
						tempMap.get(attributeId).setFileTypeValue(tempMap.get(attributeId).getFileTypeValue()+","+fileTypeValue);	
					}
				}
			}
		}
		return returnList;
	}
	
	/**
	 * 根据公共属性获取关联属性
	 */
	@Override
	public List<FileAttribute> findAttributeByPub(String pub_id) {
		String pubHql = "from FilePubArributeAttribute t where t.filePubAttributeId = ?0";
		List<FilePubArributeAttribute> pubAttributes = filePubAttributeAtributeDao.find(pubHql, pub_id);
		List<FileAttribute> fileAttributes = new ArrayList<FileAttribute>();
		if(pubAttributes != null && pubAttributes.size() >0)
		{
			for(FilePubArributeAttribute pubAttribute : pubAttributes)
			{
				FileAttribute fileAttribute = this.get(pubAttribute.getFileAttributeId());
				fileAttributes.add(fileAttribute);
			}
		}
		return fileAttributes;
	}

	@Override
	public List<FileTypeAttribute> findAttributeBySearchDefault(String attributeDefault) {
		List<FileTypeAttribute> fileTypeAttributes = this.findAttributeJsonListDefault(attributeDefault);
		return fileTypeAttributes;
	}

	@Override
	public FileTypeAttribute getAttributeJsonById(String pubId) {
		//此处不能用HQL做LEFT JOIN关联查询;
		String sql = "SELECT a.id AS arributeId ,a.attribute_name AS attributeName,d.id AS fileTypeId,d.file_type_value  AS fileTypeValue,"
				+ "d.file_type_name AS fileTypeName,a.is_default AS isDefault FROM file_pub_attribute a "
				+ "LEFT JOIN  file_pubattribute_attribute b ON a.id = b.pub_id "
				+ "LEFT JOIN  file_attribute c ON c.`id` = b.`attribute_id` "
				+ "LEFT JOIN  file_type d ON c.type_id = d.id "
				+ "WHERE  a.id = '"+pubId+"';";
		List<FileAttribute> list = fileAttributeDao.executeListSql(sql);
		List<FileTypeAttribute> returnList = null;
		if(list != null && list.size() > 0)
		{
			Map<String,FileTypeAttribute>  tempMap = new HashMap<String,FileTypeAttribute>();
			returnList = new ArrayList<FileTypeAttribute>();
			for(Object attributeInfo : list)
			{
				Object[] attributeArray = (Object[])attributeInfo;
				String attributeId = attributeArray[0] == null ? "" : attributeArray[0].toString();
				String attributeName = attributeArray[1] == null ? "" : attributeArray[1].toString();
				String fileTypeId = attributeArray[2] == null ? "" : attributeArray[2].toString();
				String fileTypeValue = attributeArray[3] == null ? "" :attributeArray[3].toString();
				String fileTypeName = attributeArray[4] == null ? "" : attributeArray[4].toString();
				String isDefault = attributeArray[5] == null ? "" : attributeArray[5].toString();
				
				if(tempMap.get(attributeId) == null){
					FileTypeAttribute fileTypeAttribute = new FileTypeAttribute();
					fileTypeAttribute.setFileAttributeId(attributeId);
					fileTypeAttribute.setFileAttributeName(attributeName);
					fileTypeAttribute.setFileTypeId(fileTypeId);
					fileTypeAttribute.setFileTypeName(fileTypeName);
					fileTypeAttribute.setFileTypeValue(fileTypeValue);
					fileTypeAttribute.setIsDefault(isDefault);
					tempMap.put(attributeId, fileTypeAttribute);
					returnList.add(fileTypeAttribute);
				}else{
					if("".equals(fileTypeValue))
					continue;
					if("".equals(tempMap.get(attributeId).getFileTypeValue())){
						tempMap.get(attributeId).setFileTypeValue(tempMap.get(attributeId).getFileTypeValue()+","+fileTypeValue);
					}else{
						String[] typeValueArr = tempMap.get(attributeId).getFileTypeValue().split(",");
						boolean isHave = false;
						for(String value : typeValueArr)
						{
							if(value.equals(fileTypeValue))
							{
								isHave = true;
								break;
							}
						}
						if(!isHave)
						tempMap.get(attributeId).setFileTypeValue(tempMap.get(attributeId).getFileTypeValue()+","+fileTypeValue);	
					}
				}
			}
		}
		if(returnList == null)
			return null;
		return returnList.get(0);
	}
}
