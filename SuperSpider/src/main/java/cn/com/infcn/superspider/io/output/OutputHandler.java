package cn.com.infcn.superspider.io.output;

import cn.com.infcn.superspider.listener.OutputDispatcher;

public interface OutputHandler {
	//输出存储业务层接口;
	public void doHandler(OutputDispatcher dispatcher)throws Exception;
}
