/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年7月26日
 */
package cn.com.infcn.superspider.service;

import cn.com.infcn.superspider.pagemodel.EasyUIDataGrid;
import cn.com.infcn.superspider.pagemodel.Log;
import cn.com.infcn.superspider.pagemodel.PageHelper;

/**
 * @author lihf
 * @date 2016年7月26日
 */
public interface SystemLogServiceI
{

	/**
	 * 添加系统日志
	 * @author lihf
	 * @date 2016年7月28日	下午6:37:13
	 * @param log
	 * @throws Exception
	 */
	public void add(Log log) throws Exception;
	
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
	public EasyUIDataGrid<?> dataGrid(Log log, PageHelper ph) throws Exception;
	
    /**
     * 删除
     * @author lihf
     * @date 2016年7月27日	下午3:56:19
     * @param id
     * @throws Exception
     */
    public void delete(String id) throws Exception;

    /**
     * 批量删除
     * @author lihf
     * @date 2016年7月27日	下午3:56:27
     * @param ids
     * @throws Exception
     */
    public void deleteBatch(String ids) throws Exception;
    
    /**
     * 删除全部
     * @author lihf
     * @date 2016年7月27日	下午3:56:27
     * @param ids
     * @throws Exception
     */
    public void deleteAll() throws Exception;
    
    /**
     * 根据id获取日志对象
     * @author lihf
     * @date 2016年7月27日	下午5:56:12
     * @param id
     * @return
     * @throws Exception
     */
    public Log getLogById(String id) throws Exception;
}
