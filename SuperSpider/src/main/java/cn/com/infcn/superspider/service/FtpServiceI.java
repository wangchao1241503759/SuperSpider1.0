/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年7月12日
 */
package cn.com.infcn.superspider.service;

import cn.com.infcn.superspider.pagemodel.FtpConfig;
import cn.com.infcn.superspider.pagemodel.TaskExportImportModel;

/**
 * @author lihf
 * @date 2016年7月12日
 */
public interface FtpServiceI
{

	/**
	 * 添加FTP采集任务
	 * @author lihf
	 * @date 2016年7月12日	下午5:05:20
	 * @param ftpConfig
	 * @throws Exception
	 */
	public void add(FtpConfig ftpConfig) throws Exception;
	
	/**
	 * 导出ftp任务
	 * @author lihf
	 * @date 2016年7月8日	下午2:13:57
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public TaskExportImportModel exportTask(String taskId) throws Exception;
	
	/**
	 * 导入ftp任务
	 * @author lihf
	 * @date 2016年7月8日	下午4:07:15
	 * @param taskExportImportModel
	 * @throws Exception
	 */
	public void importTask(TaskExportImportModel taskExportImportModel,String isReplace) throws Exception;
	
	/**
	 * 复制ftp任务
	 * @author lihf
	 * @date 2016年10月17日	下午2:33:54
	 * @param taskExportImportModel
	 * @throws Exception
	 */
	public void copyTask(TaskExportImportModel taskExportImportModel) throws Exception;
	
	
}
