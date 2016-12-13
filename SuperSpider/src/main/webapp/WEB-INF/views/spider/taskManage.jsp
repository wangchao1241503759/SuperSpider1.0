<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>采集任务管理</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script src="${ctx}/static/plugins/easyui/common/index.js" type="text/javascript"></script>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑';font-size: 9px;">
	<div id="tb" style="padding: 5px; height: auto">
		<form id="searchFrom" action="">
			<input type="text" name="filter_taskname" class="easyui-validatebox" data-options="width:150,prompt: '任务名称'" /> 
			<select id="filtertype"	class="easyui-combobox" name="state" data-options="width:150,prompt: '任务类型'" data-options="panelHeight:'auto'">
				<option value="db">数据库采集</option>
				<option value="ftp">FTP采集</option>
				<option value="file">文件采集</option>
				<option value="web">WEB采集</option>
				<option value="others">其它</option>
				<option value="all">所有</option>
			</select> 
			<input type="text" name="filter_GTD_createDate" class="easyui-my97"	datefmt="yyyy-MM-dd" data-options="width:150,prompt: '开始日期'" /> - 
			<input type="text" name="filter_LTD_createDate" class="easyui-my97"	datefmt="yyyy-MM-dd" data-options="width:150,prompt: '结束日期'" /> 
			<select	id="filterstate" class="easyui-combobox" name="state" data-options="width:150,prompt:'任务状态'" data-options="panelHeight:'auto'">
				<option value="wait">等待</option>
				<option value="doing">执行中</option>
				<option value="stop">停止</option>
				<option value="all">所有</option>
			</select>
			<script>
				$(function() {
					$("#filtertype").combobox('setValue', '');
					$("#filterstate").combobox('setValue', '');
				});
			</script>
			<span class="toolbar-item dialog-tool-separator"></span> 
		    <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search"	plain="true" onclick="">查询</a>
		</form>
		
<!-- 		选择要创建的任务类型： -->
<!-- 		<select id="tasktype" class="easyui-combobox" name="state" style="width: 150px" data-options="panelHeight:'auto'"> -->
<!-- 			<option value="db">数据库采集</option> -->
<!-- 			<option value="ftp">FTP采集</option> -->
<!-- 			<option value="file">文件采集</option> -->
<!-- 			<option value="web">WEB采集</option> -->
<!-- 			<option value="others">其它</option> -->
<!-- 		</select> -->
		<a id="btnRunPools" style="align:right" class="easyui-linkbutton" iconCls="icon-hamburg-settings" plain="true" onclick="startNow();">设置任务池</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a id="btnNew" class="easyui-linkbutton" iconCls="icon-standard-clock-add" plain="true" onclick="window.mainpage.mainTabs.addModule('数据库采集配置','spider/config/db','icon-hamburg-billing')">创建任务</a>
		<a id="btnEdit" class="easyui-linkbutton" iconCls="icon-standard-clock-edit" plain="true" onclick="del();">修改</a>
		<a id="btnDel" class="easyui-linkbutton" iconCls="icon-standard-clock-delete" plain="true" onclick="del();">删除</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a id="btnOnOff" class="easyui-linkbutton" iconCls="icon-standard-clock-pause" plain="true" onclick="on_off();">启用/禁用</a>
		<a id="btnRightPlay" class="easyui-linkbutton" iconCls="icon-standard-clock-play" plain="true" onclick="startNow();">运行</a>
		<a id="btnPause" class="easyui-linkbutton" iconCls="icon-standard-clock-pause" plain="true" onclick="stop();">停止</a>
		<span class="toolbar-item dialog-tool-separator"></span>	
		<a id="btnExport" class="easyui-linkbutton" iconCls="icon-hamburg-sign-out" plain="true" onclick="startNow();">导出</a>
		<a id="btnImport" class="easyui-linkbutton" iconCls="icon-hamburg-sign-in" plain="true" onclick="startNow();">导入</a>
		</div>
	<table id="tasklist" class="easyui-datagrid" data-options="url:'${ctx}/spider/config/task/json',method:'get'">
		<thead>
			<tr>
				<th data-options="field:'ck',checkbox:true"></th>
				<th data-options="field:'taskName',width:150">任务名称</th>
				<th data-options="field:'taskType',width:100">任务类型</th>
				<th data-options="field:'taskMemo',width:280">任务说明</th>
				<th data-options="field:'priority',width:60">优先级</th>
				<th data-options="field:'createTime',width:100">创建时间</th>
				<th data-options="field:'creator',width:100">创建人</th>
				<th data-options="field:'planName',width:100">计划名称</th>
<!-- 				<th data-options="field:'planDescription',width:300">计划描述</th> -->
				<th data-options="field:'taskState',width:60">任务状态</th>
			</tr>
		</thead>
	</table>
	
	<script>
		$('#tasklist').datagrid({
			fit : true,
			fitColumns : true,
			border : false,
			striped:true,
			pagination:true,
			rownumbers:true,
			pageNumber:1,
			pageSize : 10,
			pageList : [ 10, 20, 30, 40, 50 ],
			singleSelect:false,
			toolbar: '#tb'
		});
	</script>
</body>
</html>