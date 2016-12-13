/**
 * 
 */
package cn.com.infcn.superspider.io.output;

import java.io.ByteArrayInputStream;
import java.util.concurrent.ThreadPoolExecutor;

import cn.com.infcn.superspider.common.Constant;
import cn.com.infcn.superspider.io.mq.disk.DiskBlockingQueue;
import cn.com.infcn.superspider.io.mq.disk.DiskQueue;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.justme.superspider.spider.SpiderListener;
import com.justme.superspider.xml.Site;

/**
 * @author WChao
 *
 */
public abstract class OutputAdapter implements Output {
	
	//任务队列;
	public DiskBlockingQueue queue = null;
	//待包装磁盘队列;
	public DiskQueue support = null;
	//每个适配器都有属于自己的一个线程池
	public ThreadPoolExecutor pool;
	//线程数;
	public int thread = 1;
	//数据库模型;
	public DbModel dbModel;
	//所属站点;
	public Site site = null;
	//爬虫监听器;
	public SpiderListener listener = null;
	
	/**
	 * 处理byte的blob字段数据;
	 * @param jsonObj
	 */
	public void processByteData(JSONObject jsonObj)
	{
		for(String key : jsonObj.keySet())
		{
			Object value = jsonObj.get(key);
			if(value instanceof byte[])
			{
				jsonObj.put(key,new ByteArrayInputStream((byte[])value));
			}
		}
	}
	/**
	 * 处理增量数据;
	 * @param jsonObj
	 */
	public void processIncrementData(JSONArray insertResult,JSONArray updateResult,JSONArray deleteResult,JSONObject jsonObj){
		//同步信号量,在插入数据库时进行同步控制;
		JSONObject targetFields = this.dbModel.tableStruct.getJSONObject(Constant.TARGET_FIELDS);
		//增量标识;
		String increment = this.dbModel.tableStruct.getString(Constant.INCREMENT);
		//增量主键字段;
		String primaryField = this.dbModel.tableStruct.getString(Constant.INCREMENT_PRIMARY_FIELD) == null ? "" : this.dbModel.tableStruct.getString(Constant.INCREMENT_PRIMARY_FIELD).toString();
		if(!"1".equals(increment))
			return;
		if(jsonObj.containsKey(Constant.TRIGGER_STATUS)){
			String status = jsonObj.getString(Constant.TRIGGER_STATUS);
			if("insert".equals(status)){
				insertResult.add(jsonObj);
				if(targetFields.getString(primaryField) == null)
				jsonObj.remove(primaryField);
			}else if("update".equals(status)){
				updateResult.add(jsonObj);
			}else if("delete".equals(status)){
				deleteResult.add(jsonObj);
			}
			jsonObj.remove(Constant.TRIGGER_STATUS);
		}else{
			insertResult.add(jsonObj);
			if(targetFields.getString(primaryField) == null)
			jsonObj.remove(primaryField);
		}
	}
}
