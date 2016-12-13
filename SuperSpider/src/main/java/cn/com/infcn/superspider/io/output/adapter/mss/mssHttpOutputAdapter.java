/**
 * 
 */
package cn.com.infcn.superspider.io.output.adapter.mss;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoader;

import cn.com.infcn.ade.common.utils.DateUtils;
import cn.com.infcn.superspider.common.Constant;
import cn.com.infcn.superspider.io.mq.disk.DiskBlockingQueue;
import cn.com.infcn.superspider.io.output.DbModel;
import cn.com.infcn.superspider.io.output.Doc;
import cn.com.infcn.superspider.io.output.OutputAdapter;
import cn.com.infcn.superspider.io.output.OutputDispatcherFactory;
import cn.com.infcn.superspider.io.output.adapter.jdbc.jdbcOutputAdapter;
import cn.com.infcn.superspider.service.impl.TaskLogServiceImpl;
import cn.com.infcn.superspider.service.impl.TaskServiceImpl;
import cn.com.infcn.superspider.utils.HttpApiUtil;
import cn.com.infcn.superspider.utils.StringUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.justme.superspider.spider.SpiderListener;
import com.justme.superspider.xml.Site;

/**
 * @description 
 * @author WChao
 * @date   2016年1月12日 	下午6:27:07
 */
public class mssHttpOutputAdapter extends OutputAdapter {
	
	Logger logger = Logger.getLogger(jdbcOutputAdapter.class);
	
	//任务日志逻辑层接口;
	private TaskLogServiceImpl taskLogService = (TaskLogServiceImpl)ContextLoader.getCurrentWebApplicationContext().getBean("taskLogService"); 
	//任务逻辑层接口;
	public static TaskServiceImpl taskService = (TaskServiceImpl)ContextLoader.getCurrentWebApplicationContext().getBean("taskService");
	
	public mssHttpOutputAdapter(JSONObject obj, Site site) throws Throwable {
		this.site = site;
		init(obj);
	}
	public mssHttpOutputAdapter(JSONObject obj, Site site ,SpiderListener listener) throws Throwable {
		this(obj,site);
		this.listener = listener;
	}
	@Override
	public void init(JSONObject obj) {
		try {
			//1)初始化磁盘队列;
			this.support = OutputDispatcherFactory.pool.getDiskQueue("mssQueue_"+site.getId());
			//2) decorate to a blocking queue
			queue = new DiskBlockingQueue(this.support);
			dbModel = new DbModel(obj.getString(Constant.HOST), Integer.parseInt(obj.getString(Constant.PORT)),obj.getString(Constant.TYPE), obj.getString(Constant.DBNAME), obj.getString(Constant.USERNAME), obj.getString(Constant.PASSWORD), obj.getString(Constant.TABLE_NAME));
			JSONObject tableStruct = JSONObject.parseObject(obj.getString(Constant.TABLE_STRUCT));
		    this.dbModel.outputOption = obj;
		    this.dbModel.tableStruct = tableStruct;
		    String is_clear = tableStruct.getString(Constant.IS_CLEAR);
	        if("1".equals(is_clear))//清除持久化队列;
	        {
	        	queue.clear();
	        	HttpApiUtil.clearData(dbModel.getHost(), dbModel.getPort(), dbModel.getTablename());
	        }
		} catch (Exception e) {
			String errorInfo = e.getMessage();
			logger.error(errorInfo, e);
			listener.onError(Thread.currentThread(),null, errorInfo,e);
		}
	}

	@Override
	public void save(Doc doc) throws Exception {
		//增量标识;
		String increment = this.dbModel.tableStruct.getString(Constant.INCREMENT);
		String dateFormate = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
		JSONArray data = doc.getResult();
		String mssInfo = "开始写入元数据仓储,开始时间:"+dateFormate+"待写入条数:"+doc.getResult().size()+"条";
		logger.info(mssInfo);
		listener.onInfo(Thread.currentThread(),null, mssInfo);
		if(data == null || data.size() == 0)
			return;
		JSONArray insertResult = new JSONArray();//新增数据;
		JSONArray updateResult = new JSONArray();//修改数据;
		JSONArray deleteResult = new JSONArray();//删除数据;
		for(int i=0 ;i<data.size();i++)
		{
			JSONObject jsonObject = data.getJSONObject(i);
			processIncrementData(insertResult,updateResult,deleteResult,jsonObject);//处理增量爬取;
		}
		if(!"1".equals(increment)){
			long start = System.currentTimeMillis();
			save(data);//保存数据;
			long end = System.currentTimeMillis();
			String saveInfo = "保存"+data.size()+"条,耗时"+(end-start)+"毫秒";
			logger.info(saveInfo);
			listener.onInfo(Thread.currentThread(),null, saveInfo);
		}else{//增量爬取;
			if(insertResult.size() > 0){//增加操作;
				long start = System.currentTimeMillis();
				save(data);//保存数据;
				long end = System.currentTimeMillis();
				String saveInfo = "保存"+data.size()+"条,耗时"+(end-start)+"毫秒";
				logger.info(saveInfo);
				listener.onInfo(Thread.currentThread(),null, saveInfo);
			}
			if(deleteResult.size() > 0){//删除操作;
				long start = System.currentTimeMillis();
				delete(deleteResult);
				long end = System.currentTimeMillis();
				String delInfo = "删除"+deleteResult.size()+"条,耗时"+(end-start)+"毫秒";
				logger.info(delInfo);
				listener.onInfo(Thread.currentThread(),null, delInfo);
			}
			if(updateResult.size() > 0){//更新操作;
				long start = System.currentTimeMillis();
				save(updateResult);
				long end = System.currentTimeMillis();
				String updateInfo = "更新"+updateResult.size()+"条,耗时"+(end-start)+"毫秒";
				logger.info(updateInfo);
				listener.onInfo(Thread.currentThread(),null, updateInfo);
			}
		}
	}
	/**
	 * 保存数据;
	 * @param data
	 * @throws Exception
	 */
	public void save(JSONArray data)throws Exception{
		//增量主键字段;
		String primaryField = this.dbModel.tableStruct.getString(Constant.INCREMENT_PRIMARY_FIELD);
		if(!StringUtil.isEmpty(primaryField))
		{
			for(int i = 0; i < data.size(); i++){
				JSONObject jsonObject = data.getJSONObject(i);
				jsonObject.put("_id", jsonObject.get(primaryField));
			}
		}
		HttpApiUtil.insertData(this.dbModel.getHost(), this.dbModel.getPort(), this.dbModel.getTablename(), data,site,taskLogService,taskService);
	}
	/**
	 * 删除数据;
	 * @param data
	 * @throws Exception
	 */
	public void delete(JSONArray data)throws Exception{
		String primaryField = this.dbModel.tableStruct.getString(Constant.INCREMENT_PRIMARY_FIELD);
		for (int i = 0; i < data.size(); i++)
		{
			JSONObject jsonObject = data.getJSONObject(i);
			String id = jsonObject.getString(primaryField);
			HttpApiUtil.deleteDataById(this.dbModel.getHost(), this.dbModel.getPort(), this.dbModel.getTablename(), id);
		}
	}
	
	@Override
	public void shutdown(boolean isNow) {
		try {
			if(this.pool != null)
			{
				if (isNow)
					this.pool.shutdownNow();
				else
					this.pool.shutdown();
			}
		} catch (Exception e) {
			String errorInfo = "元数据仓储适配器关闭错误:"+e.getMessage();
			logger.error(errorInfo,e);
			listener.onError(Thread.currentThread(),null,errorInfo,e);
		}finally{
			if(queue != null)
			{
				 OutputDispatcherFactory.pool.releaseDiskQueue(this.support);//关闭磁盘队列;
			}
		}
	}
}
