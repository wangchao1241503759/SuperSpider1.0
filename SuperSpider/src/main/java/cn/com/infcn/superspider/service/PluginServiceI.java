/**
 * 
 */
package cn.com.infcn.superspider.service;

import java.util.List;

import cn.com.infcn.superspider.model.PluginModel;

/**
 * @description 
 * @author WChao
 * @date   2015年12月29日 	下午7:02:55
 */
public interface PluginServiceI {
	
	List<PluginModel> findPluginByType(String type)throws Exception;
}
