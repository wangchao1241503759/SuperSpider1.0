/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年4月20日
 */
package cn.com.infcn.superspider.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.infcn.ade.common.dao.BaseDaoI;
import cn.com.infcn.reg.product.SDI3License;
import cn.com.infcn.reg.product.utils.CommonTools;
import cn.com.infcn.superspider.model.DbSource;
import cn.com.infcn.superspider.model.Task;
import cn.com.infcn.superspider.pagemodel.DbConfig;
import cn.com.infcn.superspider.pagemodel.LicenseParam;
import cn.com.infcn.superspider.service.CheckHandlerI;


/**
 * @author lihf
 * @date 2016年4月20日
 */
@Service
public class CheckHandlerImpl implements CheckHandlerI
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CheckHandlerImpl.class);

	@Autowired
	private BaseDaoI<DbSource> dbSourceDao;
	
	@Autowired
	private BaseDaoI<Task> taskDao;

	private SDI3License license;

	/**
	 * 初始化准备
	 * 
	 * @author lihf
	 * @date 2016年4月20日 下午7:14:33
	 */
	public void init() throws Exception
	{
		this.license = new SDI3License();
		String path = CommonTools.getWEBINFPath();
		if(!path.startsWith(File.separator))
		{
			path = File.separator + path;
		}
		logger.debug("-----------------License文件路径-------------------------------"+path);
		this.license.loadFromLicenseFile(path);
	}

	/**
	 * 检测DB是否失效
	 *
	 * @author lihf
	 * @date 2016年4月20日 下午6:28:01
	 * @param joinPoint
	 * @throws Exception
	 */
	@SuppressWarnings("null")
	@Deprecated
	@Override
	public void checkDataBase(JoinPoint joinPoint) throws Exception
	{
		init();
		Date expireDate = this.license.getExpireDate();
		Date currentDay = new Date();
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
		String currentDayStr = dataFormat.format(currentDay);
		Date toDay = dataFormat.parse(currentDayStr);
		boolean verifyFlag = toDay.after(expireDate);
		if (verifyFlag)
		{
			throw new Exception("注册已过期，请重新注册！");
		}

		logger.debug("正在访问类名为：" + joinPoint.getTarget().getClass().getName() + "，方法 名为：" + joinPoint.getSignature().getName());
		logger.debug("-----------------检测DB是否失效开始-------------------------------");
		Object[] args = joinPoint.getArgs();
		for (int i = 0; i < args.length; i++)
		{
			logger.debug(args[i]);
			if (args[i] instanceof DbConfig)
			{
				DbConfig dbConfig = (DbConfig) args[i];
//				Map<String, Integer> map = licenseDb.getDbSupport();
				Map<String, Integer> map = null;
				String dbType = dbConfig.getDsType();
				Integer num = map.get(dbType);
				if (null == num)
				{
					throw new Exception("注册中没有" + dbType + "的数据库类型！");
				}
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("dsType", dbType);
				String hql = " select count(t.dsId) from DbSource t where t.dsType=:dsType ";
				long count = dbSourceDao.count(hql, params);
				if (count >= num.longValue())
				{
					throw new Exception("注册中" + dbType + "的数据库的数量为" + num.longValue() + "个！");
				}

			}
		}
		logger.debug("-----------------检测DB是否失效结束-------------------------------");
	}
	
	
	@Override
	public void checkDB(LicenseParam licenseParam) throws Exception
	{
		init();
		checkExpireDate();
		
		//String taskType = licenseParam.getTaskType();
		String dbType = licenseParam.getDbType();
		String operation = licenseParam.getOperation();
		String scope = licenseParam.getScope();

		logger.debug("-----------------检测DB是否失效开始-------------------------------");
		
		if(!"select".equalsIgnoreCase(scope))
		{
			int task_count = getTaskCountByTaskType(licenseParam);
			int maxDBJobs = license.getMaxDBJobs();
			if("add".equalsIgnoreCase(operation))
			{
				if (task_count >= maxDBJobs)
				{
					throw new Exception("您最多能创建"+maxDBJobs+"个“数据库采集”任务，已达到使用上限，请购买License！");
				}
			}
			else
			{
				if (task_count > maxDBJobs)
				{
					throw new Exception("您最多能创建"+maxDBJobs+"个“数据库采集”任务，已达到使用上限，请购买License！");
				}
			}
		}
		
		if(!(dbType.equalsIgnoreCase("Oracle") || dbType.equalsIgnoreCase("MySQL") || dbType.equalsIgnoreCase("SQLServer") || dbType.equalsIgnoreCase("DB2")))
		{
			String[] dbTypes = license.getSupportDBType();
			if (null != dbTypes && dbTypes.length>0)
			{
				if("mongo".equalsIgnoreCase(dbType))
				{
					dbType="MongoDB";
				}
				boolean flag =false;
				for(String dbType_temp:dbTypes)
				{
					if(dbType.equalsIgnoreCase(dbType_temp))
					{
						flag = true;
						break;
					}
				}
				if(!flag)
				{
					throw new Exception("您没有“" + dbType + "”使用授权，请购买License！");
				}
			}
			else
			{
				throw new Exception("您没有“" + dbType + "”使用授权，请购买License！");
			}
		}
		logger.debug("-----------------检测DB是否失效结束-------------------------------");
	}
	
	/**
	 * 检测Db是否过期
	 * @author lihf
	 * @date 2016年4月21日	下午4:41:16
	 * @throws Exception
	 */
	@Override
	public void checkExpireDate() throws Exception
	{
		init();
		Date expireDate = this.license.getExpireDate();
		Date currentDay = new Date();
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
		String currentDayStr = dataFormat.format(currentDay);
		Date toDay = dataFormat.parse(currentDayStr);
		boolean verifyFlag = toDay.after(expireDate);
		if (verifyFlag)
		{
			throw new Exception("注册已过期，请重新注册！");
		}
	}
	
	/**
	 * 根据任务类型获取任务个数
	 * @author lihf
	 * @date 2016年8月31日	下午5:54:39
	 * @param taskType
	 * @return
	 * @throws Exception
	 */
	@Override
	public int getTaskCountByTaskType(LicenseParam licenseParam) throws Exception
	{
		String taskType = licenseParam.getTaskType();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("taskType", taskType);
		String hql = " select count(t.id) from Task t where t.taskType=:taskType ";
		long count=taskDao.count(hql, params);
		int count_int = (int)count;
		return count_int;
	}
	
	/**
	 * 检测Web是否失效
	 * @author lihf
	 * @date 2016年8月31日	下午6:42:17
	 * @param webConfig
	 * @throws Exception
	 */
	public void checkWeb(LicenseParam licenseParam) throws Exception
	{
		init();
		checkExpireDate();
		
		String operation = licenseParam.getOperation();

		logger.debug("-----------------检测WEB是否失效开始-------------------------------");
//		String taskType = webConfig.getTaskType();
		int task_count = getTaskCountByTaskType(licenseParam);
		int maxWebJobs = license.getMaxWebJobs();
		if ("add".equalsIgnoreCase(operation))
		{
			if (task_count >= maxWebJobs)
			{
				throw new Exception("您最多能创建"+maxWebJobs+"个“WEB采集”任务，已达到使用上限，请购买License！");
			}
		}
		else
		{
			if (task_count > maxWebJobs)
			{
				throw new Exception("您最多能创建"+maxWebJobs+"个“WEB采集”任务，已达到使用上限，请购买License！");
			}
		}
	}
	
	/**
	 * 检测Ftp是否失效
	 * @author lihf
	 * @date 2016年8月31日	下午6:42:44
	 * @param ftpConfig
	 * @throws Exception
	 */
	public void checkFtp(LicenseParam licenseParam) throws Exception
	{
		init();
		checkExpireDate();

		String operation = licenseParam.getOperation();
		
		logger.debug("-----------------检测FTP是否失效开始-------------------------------");
//		String taskType = ftpConfig.getTaskType();
		int task_count = getTaskCountByTaskType(licenseParam);
		int maxFtpJobs = license.getMaxFTPJobs();
		if ("add".equalsIgnoreCase(operation))
		{
			if (task_count >= maxFtpJobs)
			{
				throw new Exception("您最多能创建"+maxFtpJobs+"个“FTP采集”任务，已达到使用上限，请购买License！");
			}
		}
		else
		{
			if (task_count > maxFtpJobs)
			{
				throw new Exception("您最多能创建"+maxFtpJobs+"个“FTP采集”任务，已达到使用上限，请购买License！");
			}
		}
	}
	
	/**
	 * 检测File是否失效
	 * @author lihf
	 * @date 2016年8月31日	下午6:42:55
	 * @param fileConfig
	 * @throws Exception
	 */
	public void checkFile(LicenseParam licenseParam) throws Exception
	{
		init();
		checkExpireDate();

		String operation = licenseParam.getOperation();
		
		logger.debug("-----------------检测File是否失效开始-------------------------------");
//		String taskType = fileConfig.getTaskType();
		int task_count = getTaskCountByTaskType(licenseParam);
		int maxFileJobs = license.getMaxFileJobs();
		if("add".equalsIgnoreCase(operation))
		{
			if (task_count >= maxFileJobs)
			{
				throw new Exception("您最多能创建"+maxFileJobs+"个“文件采集”任务，已达到使用上限，请购买License！");
			}
		}
		else
		{
			if (task_count > maxFileJobs)
			{
				throw new Exception("您最多能创建"+maxFileJobs+"个“文件采集”任务，已达到使用上限，请购买License！");
			}
		}
	}

	/**
	 * 获取有效的模块(任务类型)
	 * @author lihf
	 * @date 2016年9月1日	下午12:20:25
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<String> getTaskTypeList() throws Exception
	{
		List<String> taskTypeList = new ArrayList<String>();
		init();
		checkExpireDate();
		
		int maxDBJobs = license.getMaxDBJobs();
		int maxWebJobs = license.getMaxWebJobs();
		int maxFtpJobs = license.getMaxFTPJobs();
		int maxFileJobs = license.getMaxFileJobs();
		
		if(maxDBJobs>0)
		{
			taskTypeList.add("db");
		}
		if(maxWebJobs>0)
		{
			taskTypeList.add("web");
		}
		if(maxFtpJobs>0)
		{
			taskTypeList.add("ftp");
		}
		if(maxFileJobs>0)
		{
			taskTypeList.add("file");
		}
		return taskTypeList;
	}
	
	/**
	 * 检查模块的权限
	 * @author lihf
	 * @date 2016年9月2日	上午9:36:04
	 * @param taskType
	 * @throws Exception
	 */
	public void checkModuleAuth(LicenseParam licenseParam) throws Exception
	{
		init();
		checkExpireDate();
		
		if(null==licenseParam)
		{
			throw new Exception("检查模块权限的参数不能为空！");
		}
		
		String taskType = licenseParam.getTaskType();
		
		if("db".equalsIgnoreCase(taskType))
		{
			int maxDBJobs = license.getMaxDBJobs();
			if(maxDBJobs>0)
			{
				int task_count = getTaskCountByTaskType(licenseParam);
				if(task_count>=maxDBJobs)
				{
					throw new Exception("您最多能创建"+maxDBJobs+"个“数据库采集”任务，已达到使用上限，请购买License！");
				}
			}
			else
			{
				throw new Exception("您没有“数据库采集”组件使用授权，请购买License！");
			}
		}
		else if("web".equalsIgnoreCase(taskType))
		{
			int maxWebJobs = license.getMaxWebJobs();
			if(maxWebJobs>0)
			{
				int task_count = getTaskCountByTaskType(licenseParam);
				if(task_count>=maxWebJobs)
				{
					throw new Exception("您最多能创建"+maxWebJobs+"个“WEB采集”任务，已达到使用上限，请购买License！");
				}
			}
			else
			{
				throw new Exception("您没有“WEB采集”组件使用授权，请购买License！");
			}
		}
		else if("ftp".equalsIgnoreCase(taskType))
		{
			int maxFtpJobs = license.getMaxFTPJobs();
			if(maxFtpJobs>0)
			{
				int task_count = getTaskCountByTaskType(licenseParam);
				if(task_count>=maxFtpJobs)
				{
					throw new Exception("您最多能创建"+maxFtpJobs+"个“FTP采集”任务，已达到使用上限，请购买License！");
				}
			}
			else
			{
				throw new Exception("您没有“FTP采集”组件使用授权，请购买License！");
			}
		}
		else if("file".equalsIgnoreCase(taskType))
		{
			int maxFileJobs = license.getMaxFileJobs();
			if(maxFileJobs>0)
			{
				int task_count = getTaskCountByTaskType(licenseParam);
				if(task_count>=maxFileJobs)
				{
					throw new Exception("您最多能创建"+maxFileJobs+"个“文件采集”任务，已达到使用上限，请购买License！");
				}
			}
			else
			{
				throw new Exception("您没有“文件采集”组件使用授权，请购买License！");
			}
		}
		else
		{
			throw new Exception("没有该组件！");
		}
	}
	
	/**
	 * 获取最在的任务数
	 * @author lihf
	 * @date 2016年9月2日	下午3:21:44
	 * @param taskType
	 * @return
	 * @throws Exception
	 */
	public int getMaxJobs(String taskType) throws Exception
	{
		init();
		checkExpireDate();
		
		int maxJobs = 0;
		if("db".equalsIgnoreCase(taskType))
		{
			maxJobs = license.getMaxDBJobs();
		}
		else if("web".equalsIgnoreCase(taskType))
		{
			maxJobs = license.getMaxWebJobs();
		}
		else if("ftp".equalsIgnoreCase(taskType))
		{
			maxJobs = license.getMaxFTPJobs();
		}
		else if("file".equalsIgnoreCase(taskType))
		{
			maxJobs = license.getMaxFileJobs();
		}
		return maxJobs;
	}
}
