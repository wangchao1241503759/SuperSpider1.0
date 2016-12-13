package cn.com.infcn.superspider.listener;

import java.util.HashMap;
import java.util.Map;

//import org.apache.log4j.Logger;


import com.justme.superspider.xml.Site;

/**
 * @author WChao
 * @date 2016年2月28日
 */
public class TaskManager {
	
	//private final static Logger logger = Logger.getLogger(TaskManager.class);
	
	private static TaskManager taskManager = null;
	
	private TaskManager(){};
	
	private Map<String,TaskSpiderBean> spiders = new HashMap<String, TaskSpiderBean>();
	
	public static TaskManager getInstance(){
		if(taskManager == null){
			taskManager = new TaskManager();
		}
		return taskManager;
	}
	/**
	 * 添加爬虫;
	 * @param spider
	 * @return
	 */
	public TaskSpiderBean add(TaskSpiderBean spiderBean){
		Site site = spiderBean.getSpider().getSites().iterator().next();
		spiders.put(site.getId(),spiderBean);
		return spiderBean;
	}
	/**
	 * 获取爬虫;
	 * @param id
	 * @return
	 */
	public TaskSpiderBean get(String id){
		return spiders.get(id);
	}
	/**
	 * 删除爬虫;
	 * @param id
	 * @return
	 */
	public TaskSpiderBean remove(String id){
		return spiders.remove(id);
	}
}
