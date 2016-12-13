/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年11月1日
 */
package cn.com.infcn.superspider.io.output;

import cn.com.infcn.superspider.listener.OutputDispatcher;

/**
 * 异常数据导入停止
 * @author lihf
 * @date 2016年11月1日
 */
public class ExceptionOutputHandlerimpl implements OutputHandler
{

	/* (non-Javadoc)
	 * @see cn.com.infcn.superspider.io.output.OutputHandler#doHandler(cn.com.infcn.superspider.listener.OutputDispatcher)
	 */
	@Override
	public void doHandler(OutputDispatcher dispatcher) throws Exception
	{
		dispatcher.shutdown();
	}

}
