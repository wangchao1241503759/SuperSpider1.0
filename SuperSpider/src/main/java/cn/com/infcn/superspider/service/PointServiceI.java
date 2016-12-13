/**
 * 
 */
package cn.com.infcn.superspider.service;

import java.util.List;

import cn.com.infcn.superspider.model.PluginModel;
import cn.com.infcn.superspider.model.PointModel;

/**
 * @description 
 * @author WChao
 * @date   2015年12月29日 	下午7:03:11
 */
public interface PointServiceI {
	
	List<PointModel> findPointByPlugin(PluginModel pluginModel)throws Exception;
}
