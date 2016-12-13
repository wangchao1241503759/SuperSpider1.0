/**
 * 
 */
package cn.com.infcn.superspider.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import cn.com.infcn.superspider.model.FormModel;
import cn.com.infcn.superspider.model.Task;

/**
 * @description 
 * @author WChao
 * @date   2015年12月28日 	下午3:09:00
 */
public interface TaskServiceI {
	/**
	 * 批量删除任务;
	 * @param formModel
	 * @throws Exception
	 */
	public void deleteBatch(FormModel formModel)throws Exception;
	/**
	 * 获取任务bean;
	 * @param taskId
	 * @throws Exception
	 */
	public Task getBean(String taskId)throws Exception;
	/**
	 * 启用/禁用;
	 * @param tasks
	 * @return
	 * @throws Exception
	 */
	public int onOff(List<Task> tasks)throws Exception;
	/**
	 * 批量启动任务;
	 * @param formModel
	 * @throws Exception
	 */
	public int start(List<String> taskIds)throws Exception;
	/**
	 * 批量停止任务;
	 * @param taskIds
	 * @return
	 * @throws Exception
	 */
	public int stop(List<String> taskIds)throws Exception;
	/**
	 * 单个启动任务;
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int start(String id)throws Exception;
	/**
	 * 单个停止任务;
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int stop(String id)throws Exception;
	/**
	 * 根据任务状态查询任务;
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public List<Task> findTaskByStatus(String status)throws Exception;
	/**
	 * 根据任务ID查询任务;
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public List<Task> findTaskByIds(List<String> ids)throws Exception;
	/**
	 * 根据计划查询任务;
	 * @param planId
	 * @return
	 * @throws Exception
	 */
	public List<Task> findTaskByPlan(String planId)throws Exception;
	/**
	 * 任务导出;
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> doDataExport(String[] ids)throws Exception;
	/**
	 * 任务导入;
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public int doDataImport(MultipartFile file)throws Exception;
	/**
	 * 批量保存;
	 * @param tasks
	 * @throws Exception
	 */
	public void saveList(List<Task> tasks)throws Exception;
	
	/**
	 * 根据任务ID获取任务
	 * @author lihf
	 * @date 2016年4月14日	上午10:33:49
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Task getTaskById(String id) throws Exception;
	
	/**
	 * 导出任务
	 * @author lihf
	 * @date 2016年7月8日	下午3:16:11
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> exportTask(String[] ids) throws Exception;
	
	/**
	 * 导入任务
	 * @author lihf
	 * @date 2016年7月8日	下午3:57:32
	 * @param file
	 * @throws Exception
	 */
	public void importTask(MultipartFile file) throws Exception;
	
	/**
	 * 根据任务名称获取复制后的任务名称
	 * @author lihf
	 * @date 2016年10月14日	下午3:12:01
	 * @param taskName
	 * @return
	 * @throws Exception
	 */
	public String getTaskNameByName(String taskName,int num) throws Exception;
	
	/**
	 * 检测任务名称是否存在
	 * @author lihf
	 * @date 2016年11月17日	上午9:55:40
	 * @param taskId
	 * @param taskName
	 * @return
	 * @throws Exception
	 */
	public boolean checkTaskNameIsExists(String taskId,String taskName) throws Exception;
}
