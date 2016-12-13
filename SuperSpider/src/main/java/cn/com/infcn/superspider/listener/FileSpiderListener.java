package cn.com.infcn.superspider.listener;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.eweb4j.util.CommonUtil;
import org.springframework.web.context.ContextLoader;

import cn.com.infcn.superspider.common.Constant;
import cn.com.infcn.superspider.io.output.Doc;
import cn.com.infcn.superspider.io.output.OutputDispatcherFactory;
import cn.com.infcn.superspider.pagemodel.Log;
import cn.com.infcn.superspider.service.impl.TaskLogServiceImpl;
import cn.com.infcn.superspider.service.impl.TaskServiceImpl;
import cn.com.infcn.superspider.utils.ConfigUtil;
import cn.com.infcn.superspider.utils.ListenerUtil;
import cn.com.infcn.superspider.utils.ModelParseUtil;
import cn.com.infcn.superspider.utils.UUIDCreater;

import com.alibaba.fastjson.JSONArray;
import com.justme.superspider.fetcher.Page;
import com.justme.superspider.spider.SpiderListenerAdaptor;
import com.justme.superspider.task.Task;
import com.justme.superspider.xml.Option;
import com.justme.superspider.xml.Site;
import com.justme.superspider.xml.Target;

@SuppressWarnings("unused")
public class FileSpiderListener extends SpiderListenerAdaptor {
	
	private Site site = null;
	
	private AtomicBoolean atomicFlag = new AtomicBoolean(true);//停止前标志，保证其它线程不会再进入回调Parse方法中;
	
	private AtomicBoolean atomicRunning = new AtomicBoolean(false) ;//保证Parse回调方法顺利执行完毕标志;
	
	private AtomicInteger start_Timeout_Integer = new AtomicInteger();//输出管理器启动超时次数;
	
	private OutputDispatcher dispatcher;//输出分发器;
	
	private final static Logger logger = Logger.getLogger(FileSpiderListener.class);
	
	public ConcurrentLinkedQueue<String> logs = new ConcurrentLinkedQueue<String>();
	
	private long startTime = 0;
	
	private long totalTimeMills = 0;
	//任务日志逻辑层接口;
	private TaskLogServiceImpl taskLogService = (TaskLogServiceImpl)ContextLoader.getCurrentWebApplicationContext().getBean("taskLogService"); 
	//任务逻辑层接口;
	public static TaskServiceImpl taskService = (TaskServiceImpl)ContextLoader.getCurrentWebApplicationContext().getBean("taskService");

	@Override
	public void onStartup(Site site) {
		this.site = site;
        try
        {
        	cn.com.infcn.superspider.model.Task task = taskService.getTaskById(site.getId());
	        site.getOptions().getOption().add(new Option(Constant.START_COUNT, task.getStartCount().toString()));
			// 添加任务日志
			ListenerUtil.addLogByStart(ConfigUtil.get("fileModule"), site.getName(), taskLogService, site.getId(),  task.getStartCount());
			dispatcher = OutputDispatcher.me().init(site,this).startup();// 启动输出适配器;
			OutputDispatcherFactory.getInstance().addOutput(site, dispatcher);// 添加任务输出适配器;
			totalTimeMills = System.currentTimeMillis();
        }
        catch (Exception err)
        {
        	String errorInfo = err.getCause().toString();
        	logger.error(errorInfo, err);
			this.onError(Thread.currentThread(), null,errorInfo,err);
			logs.offer(errorInfo);
        }
	};

	@Override
	public void onAfterShutdown(Site site, Object... args) {
		try{
			// 停止任务输出存储器;
			OutputDispatcher outputDispatcher = OutputDispatcherFactory.getInstance().getOutput(site);
			if (outputDispatcher != null) {
				outputDispatcher.shutdown();
			}
			//添加任务日志
	 		ListenerUtil.addLogByFinishNum(ConfigUtil.get("fileModule"), site.getName(), site.getId(), taskLogService, taskService,Integer.parseInt(site.getOption(Constant.START_COUNT).toString()));
	 		ListenerUtil.addLogByEnd(ConfigUtil.get("fileModule"), site.getName(), taskLogService,site.getId(),Integer.parseInt(site.getOption(Constant.START_COUNT).toString()));
		}catch (Exception err)
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
	public void onDigUrls(Thread thread, Task task, String fieldName, Collection<String> urls) {
		System.out.println("[DIG-URL] ~ " + urls);
		logs.offer("<font color='#00FF00'>[DIG-URL] ~ " + urls +"</font>");
	}
	@Override
	public void onNewUrls(Thread thread, Task task, Collection<String> newUrls) {
		 System.out.print("[SuperSpider] "+CommonUtil.getNowTime("HH:mm:ss")+" [NEW_URLS] ~ ");
    	 System.out.println(newUrls.size() + ", " + newUrls);
    	 System.out.println("\t from -> "+task.url);
    	 
    	 logs.offer("<font color='#00FF00'>[SuperSpider] "+CommonUtil.getNowTime("HH:mm:ss")+" [NEW_URLS] ~ " + newUrls.size() + ", " + newUrls + "</font>");
    	 logs.offer("<font color='#00FF00'>\t from -> "+task.url +"</font>");
	}
	
	@Override
	public void onInfo(Thread thread, Task task, String info) {
		System.out.println(CommonUtil.getNowTime("HH:mm:ss")+"[INFO] ~ "+info);
		logs.offer("<font color='#00FF00'>" + CommonUtil.getNowTime("HH:mm:ss")+"[INFO] ~ "+info +"</font>");
	}

	@Override
	public void onTargetPage(Thread thread, Task task, Page page) {
		System.out.println("[TARGET] ~ " + page.getUrl());
		logs.offer("<font color='#00FF00'>[TARGET] ~ " + page.getUrl() +"</font>");
	}

	@Override
	public void onError(Thread thread, Task task, String err, Throwable e) {
		
		System.out.println("[ERROR] ~" + err);
		logs.offer("<font color='#FF0000'>[ERROR] ~" + err +"</font>");
 		//添加任务日志
		if(task == null){
			ListenerUtil.addLogByError(ConfigUtil.get("fileModule"), this.site.getName(), err, taskLogService,site.getId(),Integer.parseInt(site.getOption(Constant.START_COUNT).toString()));
		}
	}

	@Override
	public void onParse(Thread thread, Task task,List<Map<String, Object>> models) {
		long endTime = System.currentTimeMillis();
		String parseInfo = "回调数据处理中...本次读取耗时:"+(endTime-startTime)+"毫秒,当前总耗时:"+(endTime-totalTimeMills)+"毫秒!";
		logger.info(parseInfo);
		logs.offer("<font color='#00FF00'>[SuperSpider] "+CommonUtil.getNowTime("HH:mm:ss")+" [PARSE] ~ "+parseInfo+"</font>");
		if(!atomicFlag.get() || models.isEmpty()){
		   return ;
		}
		try{
			atomicRunning.compareAndSet(false, true);
			// System.out.println("[on_ParseoneTarget]~"+models);
			// 调用输出数据存储模块进行数据持久化存储;
			if (validOutputStart()) {
				// 调用模型转换工具将models转换为业务相关数据;
				JSONArray jsonArray = ModelParseUtil.toFileJsonData(task,models);
				Doc doc = ModelParseUtil.toDoc(dispatcher, jsonArray,task.target);
				if (doc != null)
					dispatcher.pushQueue(doc); 
			}
			String parseBackInfo = "回调数据处理完毕....";
			logger.info(parseBackInfo);
			logs.offer("<font color='#00FF00'>[SuperSpider] "+CommonUtil.getNowTime("HH:mm:ss")+" [PARSE] ~ "+parseBackInfo+"</font>");
		}catch (Exception err) {
			String errorInfo = err.getCause().toString();
			logger.error(errorInfo, err);
			this.onError(Thread.currentThread(), null,errorInfo,err);
			logs.offer(errorInfo);
		}finally{
			atomicRunning.compareAndSet(true,false);
		}
	}

	@Override
	public void onBeforeShutdown(Site site,Object... args){
		atomicFlag.compareAndSet(true, false);
		while(atomicRunning.get()){
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
		File file = new File("D:/ftp/Multimedia");
		System.out.println(file.getPath());
		/*final AtomicBoolean atomicBoolean = new AtomicBoolean(true);
		Thread thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					atomicBoolean.set(false);
					try{
						System.out.println("线程1更新状态"+atomicBoolean.get());
						//Thread.sleep(1000);
					}catch(Exception e){}
				}
			}
		});
		Thread thread2 = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					try{
						if(!atomicBoolean.get()){
							atomicBoolean.set(true);
							System.out.println("线程2更新状态"+atomicBoolean.get());
						}else{
							Thread.sleep(1000);
							System.out.println("线程2睡眠...");
						}
					}catch(Exception e){
						
					}finally{
						
					}
				}
			}
		});
		thread1.start();
		thread2.start();*/
	}
}
