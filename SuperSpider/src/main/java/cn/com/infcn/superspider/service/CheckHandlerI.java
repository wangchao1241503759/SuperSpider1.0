/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年4月20日
 */
package cn.com.infcn.superspider.service;

import java.util.List;

import org.aspectj.lang.JoinPoint;

import cn.com.infcn.superspider.pagemodel.LicenseParam;

/**
 * @author lihf
 * @date 2016年4月20日
 */
public interface CheckHandlerI
{

	/**
	 * 检测DB是否失效（AOP中使用）
	 * @author lihf
	 * @date 2016年4月20日	下午6:28:01
	 * @param joinPoint
	 * @throws Exception
	 */
	public void checkDataBase(JoinPoint joinPoint) throws Exception;
	
	
	/**
	 * 检测DB是否失效
	 * @author lihf
	 * @date 2016年4月21日	下午6:52:20
	 * @param dbType
	 * @throws Exception
	 */
	public void checkDB(LicenseParam licenseParam) throws Exception;
	
	/**
	 * 检测Db是否过期
	 * @author lihf
	 * @date 2016年4月21日	下午4:41:16
	 * @throws Exception
	 */
	public void checkExpireDate() throws Exception;
	
	/**
	 * 根据任务类型获取任务个数
	 * @author lihf
	 * @date 2016年8月31日	下午5:54:39
	 * @param taskType
	 * @return
	 * @throws Exception
	 */
	public int getTaskCountByTaskType(LicenseParam licenseParam) throws Exception;
	
	/**
	 * 检测Web是否失效
	 * @author lihf
	 * @date 2016年8月31日	下午6:42:17
	 * @param webConfig
	 * @throws Exception
	 */
	public void checkWeb(LicenseParam licenseParam) throws Exception;
	
	/**
	 * 检测Ftp是否失效
	 * @author lihf
	 * @date 2016年8月31日	下午6:42:44
	 * @param ftpConfig
	 * @throws Exception
	 */
	public void checkFtp(LicenseParam licenseParam) throws Exception;
	
	/**
	 * 检测File是否失效
	 * @author lihf
	 * @date 2016年8月31日	下午6:42:55
	 * @param fileConfig
	 * @throws Exception
	 */
	public void checkFile(LicenseParam licenseParam) throws Exception;
	
	/**
	 * 获取有效的模块(任务类型)
	 * @author lihf
	 * @date 2016年9月1日	下午12:20:25
	 * @return
	 * @throws Exception
	 */
	public List<String> getTaskTypeList() throws Exception;
	
	/**
	 * 检测模块的权限
	 * @author lihf
	 * @date 2016年9月2日	上午9:36:04
	 * @param taskType
	 * @throws Exption
	 */
	public void checkModuleAuth(LicenseParam licenseParam) throws Exception;
	
	/**
	 * 获取最在的任务数
	 * @author lihf
	 * @date 2016年9月2日	下午3:21:44
	 * @param taskType
	 * @return
	 * @throws Exception
	 */
	public int getMaxJobs(String taskType) throws Exception;
}
