/**
 * 
 */
package cn.com.infcn.superspider.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.infcn.superspider.common.Constant;
import cn.com.infcn.superspider.listener.DbSpiderListener;
import cn.com.infcn.superspider.listener.FileSpiderListener;
import cn.com.infcn.superspider.listener.FtpSpiderListener;
import cn.com.infcn.superspider.listener.WebSpiderListener;
import cn.com.infcn.superspider.model.DbSource;
import cn.com.infcn.superspider.model.Task;
import cn.com.infcn.superspider.model.TaskOutput;
import cn.com.infcn.superspider.model.Trigger;
import cn.com.infcn.superspider.service.SpiderListenerServiceI;

import com.justme.superspider.spider.SpiderListener;
import com.justme.superspider.xml.Option;
import com.justme.superspider.xml.Site;
import com.justme.superspider.xml.Target;

/**
 * @description 
 * @author WChao
 * @date   2016年5月20日 	下午5:07:00
 */
@Service(value = "spiderListenerService")
public class SpiderListenerServiceImpl implements SpiderListenerServiceI{
	
	@Autowired
	private DbSourceServiceImpl dbSourceService;
	@Autowired
	private TaskOutputServiceImpl taskOutputService;
	@Autowired
	private TriggerServiceImpl triggerService;
	
	@Override
	public SpiderListener getAndInitTaskSpiderListener(Site site, Task task)throws Exception {
		SpiderListener spiderListener = null;
		if("db".equals(task.getTaskType())){//DB采集;
			spiderListener = this.getAndInitDbTaskSpiderListener(site, task);
		}else if("web".equals(task.getTaskType())){//WEB采集
			spiderListener = this.getAndInitWebTaskSpiderListener(site, task);
		}else if("ftp".equals(task.getTaskType())){//FTP采集
			spiderListener = this.getAndInitFtpTaskSpiderListener(site, task);
		}else if("file".equals(task.getTaskType())){//FILE采集
			spiderListener = this.getAndInitFileTaskSpiderListener(site, task);
		}
		return spiderListener;
	}
	/**
	 * 初始化Db爬虫任务监听器;
	 * @param site
	 * @param task
	 * @return
	 * @throws Exception
	 */
	private DbSpiderListener getAndInitDbTaskSpiderListener(Site site,Task task)throws Exception{
		Trigger trigger = triggerService.getTriggerByTask(task.getTaskId());
		TaskOutput taskOutput = taskOutputService.getByTaskId(task.getTaskId());
		List<Target> targets = site.getTargets().getTarget();
		String targetName = "";
		if(!"1".equals(taskOutput.getTopIsClear()) && trigger != null && "1".equals(trigger.getCreateStatus()) && "1".equals(task.getCompleted())){
			targetName = trigger.getTriggerTableName();//设置目标采集表为增量采集表
			if(site.getOption(Constant.INCREMENT) != null){
				site.getOptionPojo(Constant.INCREMENT).setValue("1");
			}
			else{
				site.getOptions().getOption().add(new Option(Constant.INCREMENT,"1"));
			}
			if(site.getOption(Constant.INCREMENT_PRIMARY_FIELD) != null){
				site.getOptionPojo(Constant.INCREMENT_PRIMARY_FIELD).setValue(trigger.getPrimaryField());
			}else{
				site.getOptions().getOption().add(new Option(Constant.INCREMENT_PRIMARY_FIELD,trigger.getPrimaryField()));
			}
		}else{
			DbSource dbSource = dbSourceService.getByTaskId(task.getTaskId());
			task.setCompleted("0");//设置任务为未完成;
			targetName = dbSource.getDsTableName();//设置目标采集表为源表
			if(site.getOption(Constant.INCREMENT) != null){
				site.getOptionPojo(Constant.INCREMENT).setValue("0");
			}
		}
		if(targets != null && targets.size() > 0){
			for(Target target : site.getTargets().getTarget()){
				target.setName(targetName);//设置目标名称;
			}
		}
		return new DbSpiderListener();
	}
	/**
	 * 初始化WEB任务监听器;
	 * @param site
	 * @param task
	 * @return
	 * @throws Exception
	 */
	private WebSpiderListener getAndInitWebTaskSpiderListener(Site site,Task task)throws Exception{
		
		return new WebSpiderListener();
	}
	/**
	 * 初始化Ftp任务监听器;
	 * @param site
	 * @param task
	 * @return
	 * @throws Exception
	 */
	private FtpSpiderListener getAndInitFtpTaskSpiderListener(Site site,Task task)throws Exception{
		
		return new FtpSpiderListener();
	}
	/**
	 * 初始化File任务监听器;
	 * @param site
	 * @param task
	 * @return
	 * @throws Exception
	 */
	private FileSpiderListener getAndInitFileTaskSpiderListener(Site site,Task task)throws Exception{
		
		return new FileSpiderListener();
	}
}
