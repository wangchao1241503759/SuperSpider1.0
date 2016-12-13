package cn.com.infcn.superspider.service;

import java.util.List;

import cn.com.infcn.superspider.model.FieldMapping;
import cn.com.infcn.superspider.model.Task;
/**
 * @author WChao
 * @date 2015年12月28日
 */
public interface DbFieldMappingServiceI {

	public void saveBatch(List<FieldMapping> fieldList,Task task)throws Exception;
	
	public void deleteByTaskId(String taskId)throws Exception;
	
	public List<FieldMapping> findByTaskId(String taskId)throws Exception;
	
	public void saveList(List<FieldMapping> fieldList) throws Exception;
}
