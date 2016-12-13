/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年7月13日
 */
package cn.com.infcn.superspider.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.infcn.ade.common.dao.BaseDaoI;
import cn.com.infcn.ade.common.utils.StringUtils;
import cn.com.infcn.superspider.model.TFileSource;
import cn.com.infcn.superspider.service.FileSourceServiceI;
import cn.com.infcn.superspider.utils.StringUtil;

/**
 * @author lihf
 * @date 2016年7月13日
 */
@Service
public class FileSourceServiceImpl implements FileSourceServiceI
{
	@Autowired
	private BaseDaoI<TFileSource> fileSourceDao;
	/**
	 * 根据Id获取数据源
	 * 
	 * @author lihf
	 * @date 2016年7月13日 下午4:15:56
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	public TFileSource getFileSource(String id) throws Exception
	{
		if (StringUtil.isEmpty(id))
		{
			throw new Exception("参数不能为空！");
		}
		return fileSourceDao.get(TFileSource.class, id);
	}

	/**
	 * 根据任务Id获取数据源
	 * 
	 * @author lihf
	 * @date 2016年7月13日 下午4:16:29
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	@Override
	public TFileSource getFileSourceByTaskId(String taskId) throws Exception
	{
		if (StringUtil.isEmpty(taskId))
		{
			throw new Exception("参数不能为空！");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("taskId", taskId);
		String hql = " from TFileSource t where 1=1 and t.taskId=:taskId ";
		TFileSource t = fileSourceDao.get(hql, params);
		return t;
	}

	/**
	 * 根据任务ID删除来源
	 * 
	 * @author lihf
	 * @date 2016年7月13日 下午4:16:49
	 * @param taskId
	 * @throws Exception
	 */
	@Override
	public void deleteFileSourceByTaskId(String taskId) throws Exception
	{
		if(StringUtils.isEmpty(taskId))
		{
			throw new Exception("参数不能为空！");
		}
		String hql = " from TFileSource t where t.taskId=:taskId ";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("taskId", taskId);
		List<TFileSource> tftpSourceList = fileSourceDao.find(hql, params);
		if(null!=tftpSourceList && tftpSourceList.size()>0)
		{
			for(TFileSource tfilesource:tftpSourceList)
			{
				fileSourceDao.delete(tfilesource);
			}
		}
	}
}
