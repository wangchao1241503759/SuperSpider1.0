/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年4月7日
 */
package cn.com.infcn.superspider.service;

import cn.com.infcn.superspider.pagemodel.TaskExportImportModel;
import cn.com.infcn.superspider.pagemodel.WebConfig;

/**
 * @author lihf
 * @date 2016年4月7日
 */
public interface WebServiceI
{

	/**
	 * 添加web任务
	 * @author lihf
	 * @date 2016年4月7日	下午5:33:09
	 * @param webConfig
	 * @throws Exception
	 */
	public void add(WebConfig webConfig) throws Exception;
	
	/**
	 * 修改web任务
	 * @author lihf
	 * @date 2016年4月7日	下午5:33:09
	 * @param webConfig
	 * @throws Exception
	 */
	public void edit(WebConfig webConfig) throws Exception;
	
	/**
	 * 导出web任务
	 * @author lihf
	 * @date 2016年7月8日	下午2:13:57
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public TaskExportImportModel exportTask(String taskId) throws Exception;
	
	/**
	 * 导入web任务
	 * @author lihf
	 * @date 2016年7月8日	下午4:07:15
	 * @param taskExportImportModel
	 * @throws Exception
	 */
	public void importTask(TaskExportImportModel taskExportImportModel,String isReplace) throws Exception;
	
	/**
	 * 复制任务
	 * @author lihf
	 * @date 2016年10月14日	下午2:49:15
	 * @param taskExportImportModel
	 * @throws Exception
	 */
	public void copyTask(TaskExportImportModel taskExportImportModel) throws Exception;
	
}
