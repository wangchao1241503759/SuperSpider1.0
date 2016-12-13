package cn.com.infcn.superspider.io.output;


import java.io.Serializable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 所有的抽象类所集成
 * 
 * @author WChao
 *
 */
public abstract class Doc implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Object id; // 唯一标识

	private String message;

	private JSONArray result = null;//返回结果

	private JSONObject ctx = new JSONObject(); //上下文参数

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	};

	public JSONObject getCtx() {
		return ctx;
	}

	public void setCtx(JSONObject ctx) {
		this.ctx = ctx;
	}

	public JSONArray getResult() {
		return result;
	}

	public void setResult(JSONArray result) {
		this.result = result;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
