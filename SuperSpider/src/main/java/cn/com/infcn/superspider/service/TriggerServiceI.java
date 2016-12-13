/**
 * 
 */
package cn.com.infcn.superspider.service;

import java.util.Map;

import cn.com.infcn.superspider.pagemodel.DbConfig;

/**
 * @description 
 * @author WChao
 * @date   2016年2月19日 	上午9:51:09
 */
public interface TriggerServiceI {
	
	public Map<String,Object> createTrigger(DbConfig dbConfig);
	
	public void deleteTrigger(DbConfig dbConfig);
}
