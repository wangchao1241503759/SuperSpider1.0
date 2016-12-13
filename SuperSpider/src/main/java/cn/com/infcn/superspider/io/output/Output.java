package cn.com.infcn.superspider.io.output;

import com.alibaba.fastjson.JSONObject;

public interface Output {
	
	void init(JSONObject obj);
	
	void save(Doc doc) throws Exception;
	
	void shutdown(boolean isNow);
	
}
