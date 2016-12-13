/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年7月12日
 */
package cn.com.infcn.superspider.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.infcn.ade.common.dao.BaseDaoI;
import cn.com.infcn.ade.common.utils.StringUtils;
import cn.com.infcn.superspider.model.TFtpSource;
import cn.com.infcn.superspider.service.FtpSourceServiceI;
import cn.com.infcn.superspider.utils.StringUtil;

/**
 * @author lihf
 * @date 2016年7月12日
 */
@Service
public class FtpSourceServiceImpl implements FtpSourceServiceI
{

	@Autowired
	private BaseDaoI<TFtpSource> ftpSourceDao;
	/**
	 * 根据Id获取数据源
	 * 
	 * @author lihf
	 * @date 2016年7月12日 下午7:20:13
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	public TFtpSource getFtpSource(String id) throws Exception
	{
		if (StringUtil.isEmpty(id))
		{
			throw new Exception("参数不能为空！");
		}
		return ftpSourceDao.get(TFtpSource.class, id);
	}

	/**
	 * 根据任务Id获取数据源
	 * 
	 * @author lihf
	 * @date 2016年7月12日 下午7:20:52
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	@Override
	public TFtpSource getFtpSourceByTaskId(String taskId) throws Exception
	{
		if (StringUtil.isEmpty(taskId))
		{
			throw new Exception("参数不能为空！");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("taskId", taskId);
		String hql = " from TFtpSource t where 1=1 and t.taskId=:taskId ";
		TFtpSource t = ftpSourceDao.get(hql, params);
		return t;
	}

	/**
	 * 根据任务ID删除来源
	 * 
	 * @author lihf
	 * @date 2016年7月12日 下午7:21:02
	 * @param taskId
	 * @throws Exception
	 */
	@Override
	public void deleteFtpSourceByTaskId(String taskId) throws Exception
	{
		if(StringUtils.isEmpty(taskId))
		{
			throw new Exception("参数不能为空！");
		}
		String hql = " from TFtpSource t where t.taskId=:taskId ";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("taskId", taskId);
		List<TFtpSource> tftpSourceList = ftpSourceDao.find(hql, params);
		if(null!=tftpSourceList && tftpSourceList.size()>0)
		{
			for(TFtpSource twebsource:tftpSourceList)
			{
				ftpSourceDao.delete(twebsource);
			}
		}
	}

}
