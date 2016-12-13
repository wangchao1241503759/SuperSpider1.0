/**
 * 
 *//*
package cn.com.infcn.superspider.io.output.adapter.mss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.com.infcn.ade.common.utils.DateUtils;
import cn.com.infcn.superspider.common.Constant;
import cn.com.infcn.superspider.io.mq.disk.DiskBlockingQueue;
import cn.com.infcn.superspider.io.output.DbModel;
import cn.com.infcn.superspider.io.output.Doc;
import cn.com.infcn.superspider.io.output.OutputDispatcherFactory;
import cn.com.infcn.superspider.io.output.OutputAdapter;
import cn.com.infcn.superspider.io.output.adapter.jdbc.jdbcOutputAdapter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.infcn.gate.service.bean.MssServer;
import com.infcn.gate.service.mssStorage.MssStorage;
import com.infcn.gate.service.util.Consts;
import com.justme.superspider.spider.SpiderListener;
import com.justme.superspider.xml.Site;

*//**
 * @description 
 * @author WChao
 * @date   2016年1月12日 	下午6:27:07
 *//*
public class mssOutputAdapter extends OutputAdapter {
	
	Logger logger = Logger.getLogger(jdbcOutputAdapter.class);
	
	private MssStorage mssStorage = null;
	
	
	public mssOutputAdapter(JSONObject obj, Site site) throws Throwable {
		this.site = site;
		init(obj);
	}
	public mssOutputAdapter(JSONObject obj, Site site ,SpiderListener listener) throws Throwable {
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
		    MssServer server = new MssServer(dbModel.getHost(),dbModel.getPort(),dbModel.getDbname(),dbModel.getUsername(),dbModel.getPassword(),tableStruct.getString(Constant.MSS_TYPE),null);
	        mssStorage = new MssStorage(server);
		    String is_clear = tableStruct.getString(Constant.IS_CLEAR);
	        if("1".equals(is_clear))//清除持久化队列;
	        {
	        	queue.clear();
	        	String metaDbId = this.dbModel.tableStruct.getString(Constant.META_DB_ID);
	        	mssStorage.clearData(metaDbId);
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
		String mssInfo = "["+site.getName()+"]开始写入元数据仓储,开始时间:"+dateFormate+"待写入条数:"+doc.getResult().size()+"条";
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
			String saveInfo = "["+site.getName()+"]保存"+data.size()+"条,耗时"+(end-start)+"毫秒";
			logger.info(saveInfo);
			listener.onInfo(Thread.currentThread(),null, saveInfo);
		}else{//增量爬取;
			if(insertResult.size() > 0){//增加操作;
				long start = System.currentTimeMillis();
				save(data);//保存数据;
				long end = System.currentTimeMillis();
				String saveInfo = "["+site.getName()+"]保存"+data.size()+"条,耗时"+(end-start)+"毫秒";
				logger.info(saveInfo);
				listener.onInfo(Thread.currentThread(),null, saveInfo);
			}
			if(deleteResult.size() > 0){//删除操作;
				long start = System.currentTimeMillis();
				delete(deleteResult);
				long end = System.currentTimeMillis();
				String delInfo = "["+site.getName()+"]删除"+deleteResult.size()+"条,耗时"+(end-start)+"毫秒";
				logger.info(delInfo);
				listener.onInfo(Thread.currentThread(),null, delInfo);
			}
			if(updateResult.size() > 0){//更新操作;
				long start = System.currentTimeMillis();
				update(updateResult);
				long end = System.currentTimeMillis();
				String updateInfo = "["+site.getName()+"]更新"+updateResult.size()+"条,耗时"+(end-start)+"毫秒";
				logger.info(updateInfo);
				listener.onInfo(Thread.currentThread(),null, updateInfo);
			}
		}
	}
	*//**
	 * 保存数据;
	 * @param data
	 * @throws Exception
	 *//*
	public void save(JSONArray data)throws Exception{
		String metadDbId = this.dbModel.tableStruct.getString(Constant.META_DB_ID);
		//增量主键字段;
		String primaryField = this.dbModel.tableStruct.getString(Constant.INCREMENT_PRIMARY_FIELD);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		// 状态
		dataMap.put(Consts.SAVE_MAP_STATUS_KEY, Consts.DATA_TYPE_INSERT);
		// 任务ID
		dataMap.put(Consts.SAVE_MAP_TASKID_KEY,site.getId());
		if(metadDbId != null)
		{
			List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
			for(int i = 0; i < data.size(); i++){
				JSONObject jsonObject = data.getJSONObject(i);
				Map<String, Object> map = new HashMap<String, Object>();
				map.putAll(jsonObject);
				map.put(Consts.SAVE_MAP_ORGID_KEY,jsonObject.get(primaryField));
				datas.add(map);
			}
			dataMap.put(Consts.SAVE_MAP_DATA_KYE, datas);
			mssStorage.saveData(metadDbId, dataMap);
		}
	}
	*//**
	 * 更新数据;
	 * @param data
	 * @throws Exception
	 *//*
	public void update(JSONArray data)throws Exception{
		String metadDbId = this.dbModel.tableStruct.getString(Constant.META_DB_ID);
		//增量主键字段;
		String primaryField = this.dbModel.tableStruct.getString(Constant.INCREMENT_PRIMARY_FIELD);
		Map<String, Object> map = new HashMap<>();
		for(int i = 0; i < data.size(); i++){
			JSONObject jsonObject = data.getJSONObject(i);
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.putAll(jsonObject);
			map.put(jsonObject.getString(primaryField), dataMap);
		}
		mssStorage.updateData(metadDbId, map);
	}
	*//**
	 * 删除数据;
	 * @param data
	 * @throws Exception
	 *//*
	public void delete(JSONArray data)throws Exception{
		String metadDbId = this.dbModel.tableStruct.getString(Constant.META_DB_ID);
		//增量主键字段;
		String primaryField = this.dbModel.tableStruct.getString(Constant.INCREMENT_PRIMARY_FIELD);
		List<String> deleteIds = new ArrayList<>();
		for(int i = 0; i < data.size(); i++){
			JSONObject jsonObject = data.getJSONObject(i);
			deleteIds.add(jsonObject.getString(primaryField));
		}
		mssStorage.deleteData(metadDbId, deleteIds);
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
			if(mssStorage != null)
			{
				//mssStorage.closeServer();//关闭元数据库连接
			}
		} catch (Exception e) {
			String errorInfo = "["+site.getName()+"]元数据仓储适配器关闭错误:"+e.getMessage();
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
*/