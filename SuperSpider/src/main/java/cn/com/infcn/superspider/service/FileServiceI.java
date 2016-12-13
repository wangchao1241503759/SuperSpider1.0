/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年7月13日
 */
package cn.com.infcn.superspider.service;

import cn.com.infcn.superspider.pagemodel.FileConfig;
import cn.com.infcn.superspider.pagemodel.TaskExportImportModel;

/**
 * @author lihf
 * @date 2016年7月13日
 */
public interface FileServiceI
{
	/**
	 * 添加本地文件采集任务
	 * @author lihf
	 * @date 2016年7月13日	下午4:09:46
	 * @param fileConfig
	 * @throws Exception
	 */
	public void add(FileConfig fileConfig) throws Exception;
	
	/**
	 * 导出file任务
	 * @author lihf
	 * @date 2016年7月8日	下午2:13:57
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public TaskExportImportModel exportTask(String taskId) throws Exception;
	
	/**
	 * 导入file任务
	 * @author lihf
	 * @date 2016年7月8日	下午4:07:15
	 * @param taskExportImportModel
	 * @throws Exception
	 */
	public void importTask(TaskExportImportModel taskExportImportModel,String isReplace) throws Exception;
	
	/**
	 * 复件file任务
	 * @author lihf
	 * @date 2016年10月17日	下午2:47:45
	 * @param taskExportImportModel
	 * @throws Exception
	 */
	public void copyTask(TaskExportImportModel taskExportImportModel) throws Exception;
	
}
