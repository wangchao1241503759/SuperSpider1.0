package cn.com.infcn.superspider.service;

import java.util.List;

import cn.com.infcn.superspider.model.DbSource;

public interface DbSourceServiceI {
	
	public List<String> getAllTables(DbSource dbSource)throws Exception;
	
	public void deleteByTaskId(String taskId)throws Exception;
	
	public DbSource getByTaskId(String taskId)throws Exception;
	
	public void saveList(List<DbSource> dbSources)throws Exception;
}
