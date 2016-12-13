/**
 * 
 */
package cn.com.infcn.superspider.service;

import java.util.List;
import java.util.Map;

import cn.com.infcn.superspider.model.TaskOutput;

/**
 * @description 
 * @author WChao
 * @date   2015年12月24日 	下午1:12:36
 */
public interface TaskOutputServiceI {
	
	public Map<String,String> getOutputAllTables(TaskOutput taskOutput)throws Exception;
	
	public Map<String,List<String>> getAllColumns(TaskOutput taskOutput)throws Exception;
	
	public void deleteByTaskId(String taskId)throws Exception;
	
	public TaskOutput getByTaskId(String taskId)throws Exception;
	
	public void saveList(List<TaskOutput> taskOutputs)throws Exception;
	
	/**
	 * 根据ID获取任务的输出
	 * @author lihf
	 * @date 2016年4月14日	上午11:35:33
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public TaskOutput getTaskOutput(String id) throws Exception;
	/**
	 * 根据web任务ID获取任务的输出
	 * @author lihf
	 * @date 2016年4月14日	上午11:35:33
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public TaskOutput getByWebTaskId(String taskId) throws Exception;
}
