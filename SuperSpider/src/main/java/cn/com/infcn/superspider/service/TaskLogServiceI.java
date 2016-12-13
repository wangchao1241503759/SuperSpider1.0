/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年7月26日
 */
package cn.com.infcn.superspider.service;

import java.util.List;
import java.util.Map;

import cn.com.infcn.superspider.model.TTaskLog;
import cn.com.infcn.superspider.model.Task;
import cn.com.infcn.superspider.pagemodel.EasyUIDataGrid;
import cn.com.infcn.superspider.pagemodel.Log;
import cn.com.infcn.superspider.pagemodel.PageHelper;

/**
 * @author lihf
 * @date 2016年7月26日
 */
public interface TaskLogServiceI
{

	/**
	 * 添加任务日志
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
    
    /**
     * 获取已经完成的任务数
     * @return
     */
	public long getUnusedTask(String loginName) throws Exception;
	/**
	 *  通过任务ID和运行次数获取信息
	 * @param task
	 * @return
	 */
	public List<TTaskLog> getInfoByTaskIdAndStartCount(Task task);
	/**
	 * 通过运行中任务ID获取最新的信息
	 * @param taskId
	 * @return 
	 */
	public TTaskLog getNewRuntimeInfoByTaskId(String taskId);
	
	/**
	 * 获取首页本月每天采集数量信息
	 * @return
	 */
	public List<Map<String, Object>> getIndexBarInfo();
	
	/**
	 * 查询页面基础信息
	 * @return 
	 */
	public Map<String, Object> getIndexMainInfo();
	/**
	 * 获取没有统计的任务日志列表数据
	 * @author lihf
	 * @date 2016年9月18日	下午5:20:13
	 * @param log
	 * @return
	 * @throws Exception
	 */
	public List<TTaskLog> getTaskLogListByNoStatis() throws Exception;
	
	/**
	 * 异常数据处理
	 * @author lihf
	 * @date 2016年10月24日	上午11:14:21
	 * @param id
	 * @throws Exception
	 */
	public void appendExceptionHandling(String id) throws Exception;
	
	/**
	 * 异常数据列表
	 * @author lihf
	 * @date 2016年10月27日	上午11:35:27
	 * @param ph
	 * @return
	 * @throws Exception
	 */
	public EasyUIDataGrid<Task> dataGrid(PageHelper ph) throws Exception;
	
	/**
	 * 批量处理异常数据
	 * @author lihf
	 * @date 2016年10月27日	下午4:57:46
	 * @param ids
	 * @throws Exception
	 */
	public void appendExceptionProcess(String ids) throws Exception;

}
