package cn.com.infcn.superspider.io.output;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eweb4j.util.FileUtil;

import cn.com.infcn.superspider.common.Constant;
import cn.com.infcn.superspider.io.ConfigParse;
import cn.com.infcn.superspider.io.mq.disk.DiskQueuePool;
import cn.com.infcn.superspider.listener.OutputDispatcher;
import cn.com.infcn.superspider.utils.Sys;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.justme.superspider.spider.SpiderListener;
import com.justme.superspider.xml.Outputs;
import com.justme.superspider.xml.Site;

public class OutputDispatcherFactory{
	
	private final static Logger logger = Logger.getLogger(OutputDispatcherFactory.class);
	private static Map<String,OutputDispatcher> siteOutputs = new HashMap<String,OutputDispatcher>();
	private static OutputDispatcherFactory instance = null;
	public static Properties adapters = null;
	
	public static final DiskQueuePool pool = new DiskQueuePool(disk_queue_folder(),false); // 初始化MQ所在路径
	
	private OutputDispatcherFactory(){
		//构造器私有化;
	};
	private static String disk_queue_folder(){
		
		Object diskPath = OutputDispatcherFactory.getInstance().getProperty(Constant.DISK_QUEUE_PATH);
		
		return diskPath == null || "".equals(diskPath) ? FileUtil.getTopClassPath(ConfigParse.class)+File.separator+"DiskQueue" : diskPath.toString();
	}
	
	/**
	 * 懒单例模式实例化;
	 * @return
	 */
	public static OutputDispatcherFactory getInstance(){
		if(instance == null)
		{
			instance = new OutputDispatcherFactory();
			try {
				adapters = Sys.loadPropertiesResource("cn/com/infcn/superspider/io/"+Constant.ADAPTER_CONFIG_PROPERTIES);
			} catch (IOException e) {
				new Exception("读取任务输出适配器配置文件失败!",e);
			}
		}
		return instance;
	}
	/**
	 * 工厂停止销毁;
	 * @throws Exception
	 */
	public static void destroy() throws Exception{
		if(instance != null)
		{
			for(String key : siteOutputs.keySet())
			{
				siteOutputs.get(key).shutdown();
			}
			siteOutputs = null;
			if(adapters != null)
			{
				adapters.clear();
				adapters = null;
			}
			pool.close();// 清理掉Q环境
			instance = null;
		}
	}
	/**
	 * 获取输出存储器配置信息;
	 * @param key
	 * @return
	 */
	public Object getProperty(String key){
		if(adapters != null && key != null)
		{
			 return adapters.get(key);
		}
		return null;
	}
	/**
	 * 生产指定容器中的输出适配器实例集合;
	 * @param output
	 * @param container
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public OutputAdapter[] create(Site site,SpiderListener listener) {
		Outputs outputs = site.getOutputs();
		if(outputs == null)
			return null;
		List<OutputAdapter> result = new ArrayList<OutputAdapter>();
		JSONArray parse = ConfigParse.parse(outputs);
		for (Object obj : parse) {
			JSONObject job = (JSONObject) obj;
			String type = job.getString(Constant.TYPE);
			 if(adapters != null && type != null)
			 {
			 	String className = adapters.getProperty(type);
			 	if(className != null)
			 	{
					Class<OutputAdapter> adapterClass;
					try {
						adapterClass = (Class<OutputAdapter>) Class.forName(className);
						Constructor<?> cons = adapterClass.getConstructor(JSONObject.class,Site.class,SpiderListener.class);
						result.add((OutputAdapter) cons.newInstance(job.getJSONObject("obj"),site,listener));
					}catch (Exception err) {
						String errorInfo = err.getCause().toString();
						logger.error(errorInfo, err);
						listener.onError(Thread.currentThread(), null,errorInfo,err);
					}
			 	}
			}
		}
		return result.toArray(new OutputAdapter[result.size()]);
	}
	/**
	 * 增加输出适配器;
	 * @param container
	 * @param outputs
	 */
	public void addOutput(Site site , OutputDispatcher Output)
	{
		siteOutputs.put(site.getId(),Output);
	}
	/**
	 * 移除指定容器输出适配器;
	 * @param container
	 */
	public void removeOutput(Site site)
	{
		OutputDispatcher Output = siteOutputs.get(site.getId());
		if (Output != null) {
			siteOutputs.remove(site.getId());
		}
	}
	/**
	 * 获取指定容器的输出集合;
	 * @param container
	 * @return
	 */
	public OutputDispatcher getOutput(Site site)
	{
		return siteOutputs.get(site.getId());
	}
}
