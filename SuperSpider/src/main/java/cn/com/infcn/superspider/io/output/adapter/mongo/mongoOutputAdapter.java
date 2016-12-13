package cn.com.infcn.superspider.io.output.adapter.mongo;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cn.com.infcn.superspider.common.Constant;
import cn.com.infcn.superspider.common.MongoDao;
import cn.com.infcn.superspider.io.mq.disk.DiskBlockingQueue;
import cn.com.infcn.superspider.io.output.DbModel;
import cn.com.infcn.superspider.io.output.Doc;
import cn.com.infcn.superspider.io.output.OutputDispatcherFactory;
import cn.com.infcn.superspider.io.output.OutputAdapter;
import cn.com.infcn.superspider.io.output.adapter.jdbc.jdbcOutputAdapter;

import com.alibaba.fastjson.JSONObject;
import com.justme.superspider.spider.SpiderListener;
import com.justme.superspider.xml.Site;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoInterruptedException;

/**
 * mongo输出
 * @author WChao
 *
 */
public class mongoOutputAdapter extends OutputAdapter{
	
	Logger logger = Logger.getLogger(jdbcOutputAdapter.class);
	
	private MongoDao mongoDao = null;
	
	private JSONObject obj = null;
	
	public mongoOutputAdapter(JSONObject obj, Site site) throws Exception {
		this.site = site;
		init(obj);
	}
	public mongoOutputAdapter(JSONObject obj, Site site ,SpiderListener listener) throws Throwable {
		this(obj,site);
		this.listener = listener;
	}
	@Override
	public void init(JSONObject obj){
		try {
			//1)初始化磁盘队列;
			this.support = OutputDispatcherFactory.pool.getDiskQueue("mongoQueue_"+site.getId());
			//2) decorate to a blocking queue
			queue = new DiskBlockingQueue(this.support);
			this.obj = obj;
			dbModel = new DbModel(obj.getString(Constant.HOST), obj.getInteger(Constant.PORT), Constant.MONGO, obj.getString(Constant.DBNAME), obj.getString(Constant.USERNAME), obj.getString(Constant.PASSWORD), obj.getString(Constant.TABLE_NAME));
			mongoDao = new MongoDao(dbModel.getHost(), dbModel.getPort(), dbModel.getDbname());
			JSONObject tableStruct = JSONObject.parseObject(obj.getString(Constant.TABLE_STRUCT));
		    this.dbModel.outputOption = obj;
		    this.dbModel.tableStruct = tableStruct;
		    String is_clear = tableStruct.getString(Constant.IS_CLEAR);
		    if("1".equals(is_clear))
			{
		    	queue.clear();//清除持久化队列;
		    	DBObject dbo = new BasicDBObject();
		    	this.mongoDao.remove(dbModel.getTablename(),dbo);
			}
		} catch (Exception e) {
			String errorInfo = "["+site.getName()+"]"+e.getMessage();
			logger.error(errorInfo, e);
			listener.onError(Thread.currentThread(),null, errorInfo,e);
		}
	}
	
	@Override
	public void save(Doc doc)throws Exception{
		//同步信号量,在插入数据库时进行同步控制;
		if(doc.getResult()!=null && doc.getResult().size() > 0){
			if (dbModel.getTablename() == null) {
				throw new Exception("not set tableName! in " + obj.toJSONString().replace("\n", " "));
			}
			List<DBObject> resultsDatas = new ArrayList<DBObject>();
			for(int i=0 ;i<doc.getResult().size();i++)
			{
				BasicDBObject basicObject = new BasicDBObject();
				JSONObject jsonObject = doc.getResult().getJSONObject(i);
				jsonObject.remove("$target");//移除爬虫内置参数
				try {
					//mongoDao.save(dbModel.getTablename(), jsonObject);
					basicObject.putAll(jsonObject);
					resultsDatas.add(basicObject);
				} catch (MongoInterruptedException e) {
					e.printStackTrace();
				}
			}
			long start = System.currentTimeMillis();
			mongoDao.saveBatch(dbModel.getTablename(),resultsDatas);
			long end = System.currentTimeMillis();
			String saveInfo = "["+site.getName()+"]保存"+doc.getResult().size()+"条,耗时"+(end-start)+"毫秒";
			logger.info(saveInfo);
			listener.onInfo(Thread.currentThread(),null,saveInfo);
		}
	}
	
	@Override
	public void shutdown(boolean isShutdownNow) {
		try {
			if(this.pool != null)
			{
				if (isShutdownNow)
					this.pool.shutdownNow();
				else
					this.pool.shutdown();
			}
			if(mongoDao != null){
				if(mongoDao.getClient() != null){
					mongoDao.getClient().close();
				}
			}
		}catch(Throwable e){
			logger.error(Thread.currentThread()+"OutputAdapter.name->"+site.getName()+".ThreadPool shutdown failed.",e);
		}finally{
			if(queue != null)
			{
				OutputDispatcherFactory.pool.releaseDiskQueue(this.support);//关闭磁盘队列;
			}
		}
	}
}
