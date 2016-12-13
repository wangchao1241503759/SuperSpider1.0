package cn.com.infcn.superspider.service;

import java.util.List;

import cn.com.infcn.superspider.model.SchedulePlan;

public interface SchedulePlanServiceI {
	/**
	 * 获取所有的调度计划;
	 * @return
	 */
	public List<SchedulePlan> getAllSchedulePlans();
	/**
	 * 添加调度计划;
	 * @param schedulePlan
	 * @return
	 */
	public void add(SchedulePlan schedulePlan);
	/**
	 * 批量删除;
	 * @param ids
	 */
	public void deleteBatch(List<String> ids);
	
	public void saveList(List<SchedulePlan> schedulePlans);
}
