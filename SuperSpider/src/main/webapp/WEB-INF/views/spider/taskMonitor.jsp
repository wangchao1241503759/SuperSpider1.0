<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>任务监控</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
</head>
<body>
	<div class="easyui-panel" style="border:none" data-options="fit: true">
		<div style="margin-left:10px;margin-top:5px; font-size:14px;font-weight:bold;">任务运行动态监测：</div>
        <hr size="1px" align="left" width="700px"></hr>
        <div style="margin-left:10px;">
        	<select id="task" class="easyui-combobox" style="width: 300px;" data-options="prompt: '请选择需要监控的任务',panelHeight:'auto'">
				<option value="all">所有运行任务</option>
				<option value="doc">公文数据采集</option>
				<option value="rules">管理制度采集</option>
				<option value="archive">档案数据采集</option>
				<option value="stat">统计报表数据采集</option>
			</select>
        </div>
        <div style="margin-top:5px; font-size:14px;font-weight:bold;text-align:center;width:700px">所有运行任务 - 5分钟采集速度监测</div>
        <div><img id="start-today-img" src="${ctx}/static/images/speed5.png" style="width: 700px;margin-left: 10px"></div>
        <div style="margin-left:10px;margin-top:5px; font-size:14px;font-weight:bold;">任务状态监控：</div>
        <hr size="1px" align="left" width="700px"></hr>
        <div style="margin-left:20px;">
   			<span>任务总数：7个</span>
   			<span style="margin-left:30px">运行任务数：4个</span>
   			<span style="margin-left:30px">已经完成任务数：3个</span>  
   			<span style="margin-left:30px">异常任务数：3个</span>
   			<span style="margin-left:30px">5分钟采集平均速度：6,150条/秒</span>                                           
   		</div>
   		<div style="margin-top:5px; margin-left:10px; font-size:14px;font-weight:bold;text-align:left;width:700px">正在执行的任务（4）</div>
   		<div style="margin-left: 12px">
   			<input type="text" name="filter_taskname" style="width: 200px;" class="easyui-validatebox" data-options="prompt: '请输入任务名称'" />
   			<select id="task" class="easyui-combobox" style="width: 200px;" data-options="prompt: '请选择任务状态',panelHeight:'auto'">
				<option value="all">所有</option>
				<option value="normal">正常运行</option>
				<option value="exception_ignore">成功处理异常后，正常运行</option>
			</select>
			<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search">查询</a>
   		</div>
   		<img id="start-today-img" src="${ctx}/static/images/monitor-running.png" style="width: 680px;margin-left: 10px"/>
   		<div style="margin-top:5px; margin-left:10px; font-size:14px;font-weight:bold;text-align:left;width:700px">已完成的任务（3）</div>
   		<div style="margin-left: 12px">
   			<input type="text" name="filter_taskname" style="width: 200px;" class="easyui-validatebox" data-options="prompt: '请输入任务名称'" />
   			<select id="task" class="easyui-combobox" style="width: 200px;" data-options="prompt: '请选择任务状态',panelHeight:'auto'">
				<option value="all">所有</option>
				<option value="normal">闲置</option>
				<option value="failed">出错终止-闲置</option>
				<option value="exception_ignore">成功处理异常-闲置</option>
			</select>
			<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search">查询</a>
   		</div>
   		<img id="start-today-img" src="${ctx}/static/images/monitor-stop.png" width="680px" style="margin-left: 10px"/>
	</div>
</body>
</html>