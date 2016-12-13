/**
 * 
 */
package cn.com.infcn.superspider.service;

import java.util.List;

import cn.com.infcn.superspider.model.DbSource;
import cn.com.infcn.superspider.model.FieldMapping;
import cn.com.infcn.superspider.pagemodel.DbConfig;
import cn.com.infcn.superspider.pagemodel.TaskExportImportModel;

/**
 * @description 
 * @author WChao
 * @date   2015年12月21日 	下午5:37:21
 */
public interface DbServiceI{
	
	public List<String> getAllTables(DbSource dbSource);
	
	public List<FieldMapping> getAllColumns(DbSource dbSource);
	
	public List<FieldMapping> getAllColumnsBySQL(DbSource dbSource) throws Exception;
	
	public void deleteByTaskId(String taskId)throws Exception;
	
	public void saveDb(DbConfig dbConfig) throws Exception ;
	
	/**
	 * 导出db任务
	 * @author lihf
	 * @date 2016年7月8日	下午2:13:57
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public TaskExportImportModel exportTask(String taskId) throws Exception;
	/**
	 * 导入db任务
	 * @author lihf
	 * @date 2016年7月8日	下午4:07:15
	 * @param taskExportImportModel
	 * @throws Exception
	 */
	public void importTask(TaskExportImportModel taskExportImportModel,String isReplace) throws Exception;
	
	/**
	 * 复制db任务
	 * @author lihf
	 * @date 2016年10月17日	下午2:18:47
	 * @param taskExportImportModel
	 * @throws Exception
	 */
	public void copyTask(TaskExportImportModel taskExportImportModel) throws Exception;
	
}
