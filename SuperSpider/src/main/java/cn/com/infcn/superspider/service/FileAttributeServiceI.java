/**
 * 
 */
package cn.com.infcn.superspider.service;

import java.util.List;
import java.util.Map;

import cn.com.infcn.superspider.model.FileAttribute;
import cn.com.infcn.superspider.pagemodel.FileTypeAttribute;

/**
 * @description 
 * @author WChao
 * @date   2016年1月25日 	下午3:15:30
 */
public interface FileAttributeServiceI {

	public List<FileAttribute> findAllAttribute();
	
	public List<Map<String,Object>> findAttributeByDefault(String attributeDefault);
	
	public List<FileTypeAttribute>  findAttributeJsonListDefault(String attributeDefault);
	
	public FileTypeAttribute  getAttributeJsonById(String pubId);
	
	public List<FileTypeAttribute> findAttributeBySearchDefault(String attributeDefault);
	
	public List<FileAttribute> findAttributeByPub(String pub_id);
	
}
