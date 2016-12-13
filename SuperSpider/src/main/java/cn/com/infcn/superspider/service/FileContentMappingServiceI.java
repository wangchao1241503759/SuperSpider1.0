/**
 * 
 */
package cn.com.infcn.superspider.service;

import java.util.List;

import cn.com.infcn.superspider.model.FileContentMapping;
import cn.com.infcn.superspider.model.Task;
import cn.com.infcn.superspider.pagemodel.FileContentMappingAttribute;

/**
 * @description 
 * @author WChao
 * @date   2016年1月25日 	下午3:54:46
 */
public interface FileContentMappingServiceI {
	
	public void saveBatch(List<FileContentMapping> attributeList,Task task)throws Exception;
	
	public void deleteByTaskId(String taskId)throws Exception;
	
	public List<FileContentMapping> findContentMappingByTaskId(String taskId);
	
	public List<FileContentMappingAttribute> findFormatContentMappingByTaskId(String taskId);
	
	public void saveList(List<FileContentMapping> fileContentMappings)throws Exception;
	
}
