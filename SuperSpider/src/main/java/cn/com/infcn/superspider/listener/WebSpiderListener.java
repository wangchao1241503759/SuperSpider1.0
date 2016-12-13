/**
 * 
 */
package cn.com.infcn.superspider.listener;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.eweb4j.util.CommonUtil;
import org.springframework.web.context.ContextLoader;

import cn.com.infcn.superspider.common.Constant;
import cn.com.infcn.superspider.io.output.Doc;
import cn.com.infcn.superspider.io.output.OutputDispatcherFactory;
import cn.com.infcn.superspider.service.impl.TaskLogServiceImpl;
import cn.com.infcn.superspider.service.impl.TaskServiceImpl;
import cn.com.infcn.superspider.utils.ConfigUtil;
import cn.com.infcn.superspider.utils.ListenerUtil;
import cn.com.infcn.superspider.utils.ModelParseUtil;

import com.justme.superspider.fetcher.FetchResult;
import com.justme.superspider.fetcher.Page;
import com.justme.superspider.spider.SpiderListenerAdaptor;
import com.justme.superspider.task.Task;
import com.justme.superspider.url.TargetMatcher;
import com.justme.superspider.xml.Option;
import com.justme.superspider.xml.Site;
/**
 * @description 
 * @author WChao
 * @date   2016年5月20日 	下午2:11:26
 */
public class WebSpiderListener extends SpiderListenerAdaptor {
	
	private Site site = null;
	
	public ConcurrentLinkedQueue<String> logs = new ConcurrentLinkedQueue<String>();
	
	private AtomicBoolean atomicFlag = new AtomicBoolean(true);//停止前标志，保证其它线程不会再进入回调Parse方法中;
	
	private AtomicInteger atomicRunningInteger = new AtomicInteger() ;//保证Parse回调方法顺利执行完毕标志;
	
	private AtomicInteger start_Timeout_Integer = new AtomicInteger();//输出管理器启动超时次数;
	
	private final static Logger logger = Logger.getLogger(WebSpiderListener.class);
	
	//任务日志逻辑层接口;
	private TaskLogServiceImpl taskLogService = (TaskLogServiceImpl)ContextLoader.getCurrentWebApplicationContext().getBean("taskLogService"); 
	
	//任务逻辑层接口;
	public static TaskServiceImpl taskService = (TaskServiceImpl)ContextLoader.getCurrentWebApplicationContext().getBean("taskService"); 
		
	private long startTime = 0;
	
	private OutputDispatcher dispatcher;//输出分发器;
	
	 @Override
	public void onStartup(Site site)
	{
		this.site = site;
        try
        {
	        cn.com.infcn.superspider.model.Task task = taskService.getTaskById(site.getId());//获取业务Task对象;
	        site.getOptions().getOption().add(new Option(Constant.START_COUNT, task.getStartCount().toString()));//添加任务启动次数，后面环节采集日志用;
	        //添加任务日志
	        ListenerUtil.addLogByStart(ConfigUtil.get("webModule"), site.getName(), taskLogService, site.getId(), task.getStartCount());
	        dispatcher = OutputDispatcher.me().init(site,this).startup();// 启动输出适配器;
	        OutputDispatcherFactory.getInstance().addOutput(site, dispatcher);// 添加任务输出适配器;
        }
        catch (Exception err)
        {
        	String errorInfo = err.getCause().toString();
			logger.error(errorInfo, err);
			this.onError(Thread.currentThread(), null,errorInfo,err);
			logs.offer(errorInfo);
        }
	}
	 
	 @Override
	 public void onBegin(Thread thread, Task task) {
		startTime = System.currentTimeMillis();
		if(logs.size() > 500)
			logs.clear();
	 }
	 
	 @Override
	 public void onFetch(Thread thread, Task task, FetchResult result) {
		 logger.info("[SuperSpider] "+CommonUtil.getNowTime("HH:mm:ss")+" [FETCH] ~ ");
		 logger.info("fetch result ->" + result + " from -> " + task.sourceUrl);
         logs.offer("<font color='#00FF00'>[SuperSpider] "+CommonUtil.getNowTime("HH:mm:ss")+" [FETCH] ~ fetch result ->" + result + " from -> " + task.sourceUrl+"</font>");
         if(result.getStatusCode()!=200)
         {
        	 Throwable e  = new Throwable(result.toString());
        	 onError(thread, null, result.toString(), e);
         }
     }
	 
	 @Override
	public void onNewUrls(Thread thread, Task task, Collection<String> newUrls) {
		 logger.info("[SuperSpider] "+CommonUtil.getNowTime("HH:mm:ss")+" [NEW_URLS] ~ ");
		 logger.info(newUrls.size() + ", " + newUrls);
		 logger.info("\t from -> "+task.url);
    	 logs.offer("<font color='#00FF00'>[SuperSpider] "+CommonUtil.getNowTime("HH:mm:ss")+" [NEW_URLS] ~ "+newUrls.size() + ", " + newUrls+"</font>");
    	 logs.offer("<font color='#00FF00'>\t from -> "+task.url+"</font>");
    	 
	}
	public void onDigUrls(Thread thread, Task task, String fieldName, Collection<String> urls) {
		 //Integer level = WebUtil.getLevel(fieldName,task.site);//关联字段所属目标级别;(1级、2级、3级 、等...)
		 
		 logger.info("[SuperSpider] "+CommonUtil.getNowTime("HH:mm:ss")+" [DIG] ~ ");
		 logger.info("field->" + fieldName + ", "+urls.size() + ", " + urls);
		 logger.info("\t from -> "+task.url);
         
         logs.offer("<font color='#00FF00'>[SuperSpider] "+CommonUtil.getNowTime("HH:mm:ss")+" [DIG] ~ " + fieldName + ", "+urls.size() + ", " + urls +"</font>");
         logs.offer("<font color='#00FF00'>\t from -> "+task.url+"</font>");
     }
     public void onInfo(Thread thread, Task task, String info) {
    	 logger.info("[SuperSpider] "+CommonUtil.getNowTime("HH:mm:ss")+" [INFO] ~ ");
    	 logger.info(info);
         
         logs.offer("<font color='#00FF00'>[SuperSpider] "+CommonUtil.getNowTime("HH:mm:ss")+" [INFO] ~ "+info+"</font>");
     }
     
     public void onError(Thread thread, Task task, String err, Throwable e) {
    	 logger.error("[SuperSpider] "+CommonUtil.getNowTime("HH:mm:ss")+" [ERROR] ~ ");
    	 logger.error(err);
         
         logs.offer("<font color='#FF0000'>[SuperSpider] "+CommonUtil.getNowTime("HH:mm:ss")+" [ERROR] ~ "+err+"</font>");
         
         //添加任务日志
         if(task == null){
	  		ListenerUtil.addLogByError(ConfigUtil.get("webModule"), this.site.getName(), err, taskLogService,site.getId(),Integer.parseInt(site.getOption(Constant.START_COUNT).toString()));
     	}
     }
     public void onTargetPage(Thread thread, Task task, Page page) {
    	 logger.info("[SuperSpider] "+CommonUtil.getNowTime("HH:mm:ss")+" [TARGET] ~ ");
    	 logger.info(page.getUrl());
     }
     @Override
     public void onParse(Thread thread, Task task,List<Map<String, Object>> models)
 	 {
    	long endTime = System.currentTimeMillis();
    	String info = "本次爬取耗时:"+(endTime-startTime)+"毫秒!";
 		logger.info(info);
 		logs.offer("<font color='#00FF00'>[SuperSpider] "+CommonUtil.getNowTime("HH:mm:ss")+" [PARSE] ~ "+info+"</font>");
 		if(!atomicFlag.get()){
 		   return ;
 		}
 		try{
 			atomicRunningInteger.incrementAndGet();
 			// 调用输出数据存储模块进行数据持久化存储;
 			if (validOutputStart() && !site.isStop) {
 				//下载并重置Web附件路径;
 				if(ModelParseUtil.getAppendixField(site, task.target) != null)
 				ModelParseUtil.resetAndDownloadAppendixFile(site,this,task.target ,task.data);
 				if(!site.isStop){
	 				System.out.println(task.sort+"[on-Parse]-> ~"+task.data);
	 				logs.offer("<font color='#00FF00'>"+task.sort+"[on-Parse]-> ~"+task.data+"</font>");
	 				Integer lastLevel = site.getTargets().getTarget().size();
	 				if(TargetMatcher.getRelationField(task.target) == null || lastLevel.toString().equals(task.target.getId())){//没设置关联字段就认为是最后一级;
	 					Doc doc = ModelParseUtil.toDoc(dispatcher, task.data,task.target);
		 				if (doc != null){
		 					dispatcher.pushQueue(doc);
		 				}
	 				}
 				}
 			}
 			//logger.info("回调数据处理完毕....");
 		}catch (Exception err) {
 			String errorInfo = err.getCause().toString();
			logger.error(errorInfo, err);
			this.onError(Thread.currentThread(), null,errorInfo,err);
			logs.offer(errorInfo);
 		}finally{
 			atomicRunningInteger.decrementAndGet();
 		}
 	 }
     @Override
 	 public void onBeforeShutdown(Site site,Object... args){
 		atomicFlag.compareAndSet(true, false);
 		site.isStop = true;
 		while(atomicRunningInteger.get() > 0){
 			try {
 				Thread.sleep(500);
 			} catch (Exception err) {
 				String errorInfo = err.getCause().toString();
 				logger.error(errorInfo, err);
 				this.onError(Thread.currentThread(), null,errorInfo,err);
 				logs.offer(errorInfo);
 			}
 		}
 	 }
    @Override
 	public void onAfterShutdown(Site site, Object... args) {
    	try{
	 		// 停止任务输出存储器;
	 		OutputDispatcher outputDispatcher = OutputDispatcherFactory.getInstance().getOutput(site);
	 		
	 		if (outputDispatcher != null) {
	 			outputDispatcher.shutdown();
	 		}
	 		//添加任务日志
	 		ListenerUtil.addLogByFinishNum(ConfigUtil.get("webModule"), site.getName(), site.getId(), taskLogService, taskService,Integer.parseInt(site.getOption(Constant.START_COUNT).toString()));
	 		ListenerUtil.addLogByEnd(ConfigUtil.get("webModule"), site.getName(), taskLogService,site.getId(),Integer.parseInt(site.getOption(Constant.START_COUNT).toString()));
    	}catch (Exception err)
        {
    		String errorInfo = err.getCause().toString();
			logger.error(errorInfo, err);
			this.onError(Thread.currentThread(), null,errorInfo,err);
			logs.offer(errorInfo);
        }
 	}
    
    @SuppressWarnings("static-access")
	public synchronized boolean validOutputStart(){
		//调用输出数据存储模块进行数据持久化存储;
		OutputDispatcher outputDispatcher = OutputDispatcherFactory.getInstance().getOutput(site);
		while(outputDispatcher == null){
			start_Timeout_Integer.incrementAndGet();
			//System.out.println(dbSpiderListener.atomicFlag.get()+"=============valid");
			try {
				int start_timeout_num = 30;
				String start_timeout_info = "【"+site.getName()+"】输出存储器启动超时30s....";
				if(start_Timeout_Integer.get() > start_timeout_num || !this.atomicFlag.get())
				{
					logger.info(start_timeout_info);
					logs.offer("<font color='#00FF00'>[SuperSpider] "+CommonUtil.getNowTime("HH:mm:ss")+" [PARSE] ~ "+start_timeout_info+"</font>");
					start_Timeout_Integer.set(0);
					return false;
				}
				outputDispatcher = OutputDispatcherFactory.getInstance().getOutput(site);
				String time_out_info = Thread.currentThread().getName()+"【"+this.site.getName()+"】等待存储器启动休息1s,已等待"+start_Timeout_Integer.get()+"次";
				logger.info(time_out_info);
				logs.offer("<font color='#00FF00'>[SuperSpider] "+CommonUtil.getNowTime("HH:mm:ss")+" [PARSE] ~ "+time_out_info+"</font>");
				Thread.currentThread().sleep(1000);
			} catch (Exception err) {
				String errorInfo = err.getCause().toString();
				logger.error(errorInfo, err);
				this.onError(Thread.currentThread(), null,errorInfo,err);
				logs.offer(errorInfo);
				return false;
			}finally{
				
			}
		}
		return true;
	}
    
    public static void main(String[] args) {
		
	}
}
