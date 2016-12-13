/**
 * 
 */
package cn.com.infcn.superspider.listener;

import com.justme.superspider.spider.SuperSpider;

/**
 * @author yangc
 *
 */
public class TaskSpiderBean {
	
	private SuperSpider spider;
	private TaskStation taskStation;
	
	public TaskSpiderBean(SuperSpider spider,TaskStation taskStation){
		this.spider = spider;
		this.taskStation = taskStation;
	}

	public SuperSpider getSpider() {
		return spider;
	}

	public TaskStation getTaskStation() {
		return taskStation;
	}
}
