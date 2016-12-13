/**
 * 
 */
package cn.com.infcn.superspider.io.output;

import cn.com.infcn.superspider.listener.OutputDispatcher;
import cn.com.infcn.superspider.listener.TaskManager;

/**
 * @author yangc
 *
 */
public class TaskOutputHandler implements OutputHandler {

	@Override
	public void doHandler(OutputDispatcher dispatcher) throws Exception{
		String id = dispatcher.site.getId();
		System.out.println(dispatcher.site+"==========="+TaskManager.getInstance().get(id));
		// 可以停止
		TaskManager.getInstance().get(id).getTaskStation().stopTask(id);
	}

}
