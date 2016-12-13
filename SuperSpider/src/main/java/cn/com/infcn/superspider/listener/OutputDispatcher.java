/**
 * 
 */
package cn.com.infcn.superspider.listener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.nutz.dao.DaoException;

import cn.com.infcn.superspider.io.output.Doc;
import cn.com.infcn.superspider.io.output.OutputAdapter;
import cn.com.infcn.superspider.io.output.OutputDispatcherFactory;
import cn.com.infcn.superspider.io.output.OutputHandler;
import cn.com.infcn.superspider.io.output.Record;
import cn.com.infcn.superspider.io.output.TaskOutputHandler;
import cn.com.infcn.superspider.io.output.adapter.spider.SpiderOutputAdapter;
import cn.com.infcn.superspider.utils.ModelParseUtil;

import com.justme.superspider.spider.SpiderListener;
import com.justme.superspider.spider.SpiderListenerAdaptor;
import com.justme.superspider.util.CommonUtil;
import com.justme.superspider.xml.Site;

/**
 * 任务输出存储器;
 * 
 * @description
 * @author WChao
 * @date 2016年1月8日 下午3:02:57
 */
public class OutputDispatcher {
	
	Logger logger = Logger.getLogger(OutputDispatcher.class);
	
	private AtomicInteger atomicInteger = new AtomicInteger();//执行中存储适配器个数标志;
	private AtomicBoolean atomicStopFlag = new AtomicBoolean(false);//是否停止标志;
	
	public Site site;
	private SpiderListener listener = null;
	public OutputAdapter[] outputs = null;
	private ExecutorService pool = null;
	private OutputHandler handler = null;//调用外部业务处理相关操作接口;

	public final static OutputDispatcher me() {

		return new OutputDispatcher();
	}

	public OutputDispatcher init(Site site) {
		this.site = site;
		if(this.listener == null)
			this.listener = new SpiderListenerAdaptor();
		if(this.handler == null)
			this.handler = new TaskOutputHandler();
		initOutputs();
		initPool();
		return this;
	}

	public OutputDispatcher init(Site site , SpiderListener listener){
		this.listener = listener;
		return init(site);
	}
	
	public OutputDispatcher init(Site site , SpiderListener listener , OutputHandler handler){
		this.handler = handler;
		return init(site,listener);
	}
	
	public OutputDispatcher startup() {
		if(outputs == null || outputs.length == 0)
		return null;
		for (OutputAdapter output : outputs) {
			pool.execute(new OutputDispatcher._Executor(output));
		}
		return this;
	}

	public void initOutputs() {
		if (site != null)
			outputs = OutputDispatcherFactory.getInstance().create(site,listener);
	}

	public void initPool() {
		if (pool == null) {
			if (outputs == null || outputs.length == 0)
				return;
			int size = outputs.length;
			pool = new ThreadPoolExecutor(size, size, 60L, TimeUnit.SECONDS,
					new LinkedBlockingQueue<Runnable>());
			logger.info(Thread.currentThread().getName()
					+ "-init thread pool size->" + size + " success ");
		}
	}

	public void shutdown() {
		shutdown(false);
	}

	/**
	 * 
	 * @date 2013-6-3 下午05:57:25
	 * @param isNow是否立即关闭
	 */
	public void shutdown(boolean isNow) {
		shutdownNow(isNow);
	}

	public void shutdownNow() {
		shutdownNow(true);
	}

	/**
	 * 
	 * @date 2013-6-3 下午05:57:25
	 * @param isNow
	 *            是否立即关闭
	 */
	@SuppressWarnings("static-access")
	public void shutdownNow(boolean isNow, Object... args) {
		atomicStopFlag.compareAndSet(false, true);
		while(atomicInteger.get()!=0){
			try {
				Thread.currentThread().sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (outputs != null) {
			for (OutputAdapter output : outputs) {
				output.shutdown(isNow);
				String destroyInfo = Thread.currentThread() + "OutputAdapter["+ output.site.getName() + "] destroy... ";
				logger.info(destroyInfo);
				listener.onInfo(Thread.currentThread(), null, destroyInfo);
			}
		}
		if (pool != null) {
			pool.shutdownNow();
			String shutdownInfo = Thread.currentThread()+ "Output shutdown...now ";
			logger.info(shutdownInfo);
			listener.onInfo(Thread.currentThread(), null, shutdownInfo);
		}
	}

	public void pushQueue(Doc doc) {
		for (OutputAdapter outputAdapter : outputs) {
			//long start = System.currentTimeMillis();
			//logger.info("开始入队列...");
			byte[] datas = ModelParseUtil.ObjectToByte(doc);
			outputAdapter.queue.offer(datas);
			//logger.info("入队列完毕...耗时:"+(System.currentTimeMillis()-start)+"毫秒");
		}
	}
	public void pushSpiderQueque(Doc doc){
		for(OutputAdapter outputAdapter : outputs){
			if(outputAdapter instanceof SpiderOutputAdapter){
				byte[] datas = ModelParseUtil.ObjectToByte(doc);
				outputAdapter.queue.offer(datas);
			}
		}
	}
	public void pushNotSpiderQueque(Doc doc){
		for(OutputAdapter outputAdapter : outputs){
			if(!(outputAdapter instanceof SpiderOutputAdapter)){
				byte[] datas = ModelParseUtil.ObjectToByte(doc);
				outputAdapter.queue.offer(datas);
			}
		}
	}
	private class _Executor implements Runnable {

		private OutputAdapter outputAdapter = null;
		LinkedBlockingQueue<Runnable> workQueques = new LinkedBlockingQueue<Runnable>();

		public _Executor(OutputAdapter outputAdapter) {
			this.outputAdapter = outputAdapter;
			int size = outputAdapter.thread;
			RejectedExecutionHandler rejectedHandler = new RejectedExecutionHandler() {
				public void rejectedExecution(Runnable r,ThreadPoolExecutor executor) {

				}
			};
			if (size > 0)
				this.outputAdapter.pool = new ThreadPoolExecutor(size, size,60L, TimeUnit.SECONDS, workQueques, rejectedHandler);
			else
				this.outputAdapter.pool = new ThreadPoolExecutor(0,Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,new SynchronousQueue<Runnable>(), rejectedHandler);
		}

		public void run() {
			int emptyCount = 0;
			while (!atomicStopFlag.get()) {
				byte[] docBytes = null;
				try {
					// 先检查线程池是否还有线程可用
					boolean isAvailable = (this.outputAdapter.pool.getTaskCount() - this.outputAdapter.pool.getCompletedTaskCount()) < this.outputAdapter.pool.getCorePoolSize();
					if (isAvailable){
						docBytes = outputAdapter.queue.poll();
						if (docBytes == null) {
							//采集端是否采集完毕标识;
							boolean get_IsStop = !site.isStop ? (site.queue.size() == 0 && site.counter.getActiveThreadCount().get() == 0) : site.isStop;
							//存储端是否存储完毕标识;
							boolean out_IsStop = (atomicInteger.get()==0 && outputAdapter.queue.size() == 0);
							TaskSpiderBean superSpider = TaskManager.getInstance().get(site.getId());
							if(superSpider == null || (get_IsStop && out_IsStop)){
								emptyCount++;
							}else{
								emptyCount = 0;
							}
							if(emptyCount > 5) {//延迟5秒停止
								if(!atomicStopFlag.getAndSet(true)){
									// 可以停止
									handler.doHandler(OutputDispatcher.this);
								}
								break;
							}
							long wait = CommonUtil.toSeconds(OutputDispatcherFactory.adapters.getProperty("waitQueque", "1")).longValue();
							String info = "consumer[" +site.getName()+"]"+ Thread.currentThread() + "-queue is empty -> " + wait + " seconds";
							listener.onInfo(Thread.currentThread(), null, info);
							logger.info(info);
							if (wait > 0) {
								try {
									Thread.sleep(wait * 1000);
								} catch (Exception e) {
									//e.printStackTrace();
								}
							}
							continue;
						}
						emptyCount = 0;
						Doc doc = (Record) ModelParseUtil.ByteToObject(docBytes);
						Worker worker = new Worker();
						worker.init(outputAdapter, doc);
						this.outputAdapter.pool.execute(worker);
						String diskInfo = "[" +site.getName() +"]磁盘队列:" + outputAdapter.queue.size();
						logger.info(diskInfo);
						listener.onInfo(Thread.currentThread(), null, diskInfo);
					}
				} catch (Exception e) {
					String errorInfo = e.getMessage();
					logger.error(errorInfo,e);
					listener.onError(Thread.currentThread(),null, errorInfo, e);
					if (site.isStop)
						break;
				} finally {
					if (pool == null)
						break;
				}
			}
		}
	}

	private class Worker implements Runnable {

		private Doc doc;
		private OutputAdapter outputAdapter;

		public void init(OutputAdapter outputAdapter, Doc doc) {
			this.outputAdapter = outputAdapter;
			this.doc = doc;
		}

		@Override
		public void run() {
			try {
				atomicInteger.incrementAndGet();
				outputAdapter.save(doc);// 执行数据存储;
				if(!(outputAdapter instanceof SpiderOutputAdapter))
				TaskManager.getInstance().get(site.getId()).getTaskStation().updateTaskSuccess(site.getId(),doc.getResult());//更新任务成功信息状态;
			} catch (Exception e) {
				String errorInfo = e.getCause() == null ? e.getMessage() : e.getCause().toString();
				Throwable throwbale = e.getCause() == null ? e : e.getCause().getCause();
				 if(e instanceof DaoException) {
					logger.error(throwbale);
					listener.onError(Thread.currentThread(), null, errorInfo,throwbale);
				}else{
					logger.error(throwbale);
					listener.onError(Thread.currentThread(), null, errorInfo,throwbale);
				}
				if(!(outputAdapter instanceof SpiderOutputAdapter))
				TaskManager.getInstance().get(site.getId()).getTaskStation().updateTaskError(site.getId(),doc.getResult(),"3");//更新任务失败信息状态;
			} finally {
				atomicInteger.decrementAndGet();
				if(!(outputAdapter instanceof SpiderOutputAdapter)){
					TaskManager.getInstance().get(site.getId()).getTaskStation().updateTaskComplete(site.getId(),doc.getResult());//更新任务已完成信息状态;
				}
			}
		}

	}
}
