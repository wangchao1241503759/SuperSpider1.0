/**
 * 
 */
package cn.com.infcn.superspider.service;

import com.justme.superspider.spider.SpiderListener;
import com.justme.superspider.xml.Site;

import cn.com.infcn.superspider.model.Task;

/**
 * @description 
 * @author WChao
 * @date   2016年5月20日 	下午5:06:11
 */
public interface SpiderListenerServiceI {
	
	public SpiderListener getAndInitTaskSpiderListener(Site site,Task task)throws Exception;

}
