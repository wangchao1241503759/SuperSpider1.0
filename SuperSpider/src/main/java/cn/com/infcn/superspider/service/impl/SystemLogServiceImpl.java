/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年7月26日
 */
package cn.com.infcn.superspider.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.infcn.ade.common.dao.BaseDaoI;
import cn.com.infcn.superspider.model.TSystemLog;
import cn.com.infcn.superspider.pagemodel.EasyUIDataGrid;
import cn.com.infcn.superspider.pagemodel.Log;
import cn.com.infcn.superspider.pagemodel.PageHelper;
import cn.com.infcn.superspider.service.SystemLogServiceI;
import cn.com.infcn.superspider.utils.DateUtil;
import cn.com.infcn.superspider.utils.HqlUtils;
import cn.com.infcn.superspider.utils.StringUtil;
import cn.com.infcn.superspider.utils.UUIDCreater;

/**
 * @author lihf
 * @date 2016年7月26日
 */
@Service
public class SystemLogServiceImpl implements SystemLogServiceI
{

	@Autowired
	private BaseDaoI<TSystemLog> systemLogDao;

	/**
	 * 添加系统日志
	 * @author lihf
	 * @date 2016年7月28日	下午6:37:13
	 * @param log
	 * @throws Exception
	 */
	@Override
	public void add(Log log) throws Exception
	{
		if(null==log)
		{
			throw new Exception("参数不能为空！");
		}
		TSystemLog t = new TSystemLog();
		BeanUtils.copyProperties(log, t);
		t.setId(UUIDCreater.getUUID());
		systemLogDao.save(t);
	}
	/**
	 * 获取任务日志的列表数据
	 *
	 * @author lihf
	 * @date 2016年7月26日 下午4:44:25
	 * @param log
	 * @param ph
	 * @return
	 * @throws Exception
	 */
	@Override
	public EasyUIDataGrid<?> dataGrid(Log log, PageHelper ph) throws Exception
	{
		EasyUIDataGrid<Log> dataGrid = new EasyUIDataGrid<Log>();
		List<Log> taskLogList = new ArrayList<Log>();
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from TSystemLog t where 1=1 ";
		List<TSystemLog> tList = systemLogDao.find(hql + whereHql(log, params) + HqlUtils.orderHql(ph), params, ph.getPage(), ph.getRows());

		for (TSystemLog t : tList)
		{
			Log taskLog = new Log();
			BeanUtils.copyProperties(t, taskLog);
			taskLogList.add(taskLog);
		}
		dataGrid.setRows(taskLogList);
		dataGrid.setTotal(systemLogDao.count("select count(t.id) " + hql + whereHql(log, params), params));
		return dataGrid;
	}

	/**
	 * 删除
	 * 
	 * @author lihf
	 * @date 2016年7月27日 下午3:56:19
	 * @param id
	 * @throws Exception
	 */
	@Override
	public void delete(String id) throws Exception
	{
		if (StringUtil.isEmpty(id))
		{
			throw new Exception("参数不能为空！");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		String hql = " from TSystemLog t where 1=1 and t.id=:id ";
		TSystemLog t = systemLogDao.get(hql, params);

		systemLogDao.delete(t);
	}

	/**
	 * 批量删除
	 * 
	 * @author lihf
	 * @date 2016年7月27日 下午3:56:27
	 * @param ids
	 * @throws Exception
	 */
	@Override
	public void deleteBatch(String ids) throws Exception
	{
		if (StringUtil.isEmpty(ids))
		{
			throw new Exception("参数不能为空！");
		}
		String[] idList = ids.split(",");
		if (null == idList || idList.length == 0)
		{
			throw new Exception("参数不能为空！");
		}
		for (String id : idList)
		{
			this.delete(id);
		}
	}
	
    /**
     * 删除全部
     * @author lihf
     * @date 2016年7月27日	下午3:56:27
     * @param ids
     * @throws Exception
     */
	@Override
    public void deleteAll() throws Exception
    {
		String hql = " delete from TSystemLog t ";
		systemLogDao.executeHql(hql);
    }
	

    /**
     * 根据id获取日志对象
     * @author lihf
     * @date 2016年7月27日	下午5:56:12
     * @param id
     * @return
     * @throws Exception
     */
	@Override
    public Log getLogById(String id) throws Exception
    {
		Log log = new Log();
		if (StringUtil.isEmpty(id))
		{
			throw new Exception("参数不能为空！");
		}
		TSystemLog t = systemLogDao.get(TSystemLog.class, id);
		if(null!=t)
		{
			BeanUtils.copyProperties(t, log);
		}
		return log;
    }

	/**
	 * 拼接查询条件
	 * 
	 * @author lihf
	 * @date 2016年7月26日 下午4:57:37
	 * @param log
	 * @param params
	 * @return
	 */
	private String whereHql(Log log, Map<String, Object> params)
	{
		String hql = "";
		StringBuffer sb = new StringBuffer();
		if (log != null)
		{
			if (!StringUtil.isEmpty(log.getTaskName()))
			{
				sb.append(" and t.taskName like :taskName");
				params.put("taskName", "%%" + log.getTaskName() + "%%");
			}
			if (!StringUtil.isEmpty(log.getLevel()))
			{
				sb.append(" and t.level =:level");
				params.put("level", log.getLevel());
			}
			if (!StringUtil.isEmpty(log.getSource()))
			{
				sb.append(" and t.source =:source");
				params.put("source", log.getSource());
			}
			if (!StringUtil.isEmpty(log.getCreateTimeStart()))
			{
				sb.append(" and t.createTime >=:createTimeStart");
				params.put("createTimeStart", DateUtil.dateValidation(log.getCreateTimeStart(), "yyyy-MM-dd"));
			}
			if (!StringUtil.isEmpty(log.getCreateTimeEnd()))
			{
				sb.append(" and t.createTime <=:createTimeEnd");
				params.put("createTimeEnd", DateUtil.dateValidation(log.getCreateTimeEnd(), "yyyy-MM-dd"));
			}
		}
		hql = sb.toString();
		return hql;
	}

}
