<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>数据采集任务管理</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
</head>
<body>
	<div id="tb" style="padding:5px;height:auto">
		<div>
	    	<form id="searchFrom" action="">
	    		<select	id="filter_state" class="easyui-combobox" name="state" data-options="width:150,prompt:'日志级别'" data-options="panelHeight:'auto'">
					<option value="wait">信息</option>
					<option value="doing">警告</option>
					<option value="stop">错误</option>
				</select>
				<input type="text" name="filter_startDate" class="easyui-my97"	datefmt="yyyy-MM-dd" data-options="width:150,prompt:'开始日期'" /> - 
				<input type="text" name="filter_endDate" class="easyui-my97"	datefmt="yyyy-MM-dd" data-options="width:150,prompt:'结束日期'" /> 
				<select id="filter_type" class="easyui-combobox" name="state" data-options="width:150,prompt: '来源'" data-options="panelHeight:'auto'">
					<option value="db">数据库采集组件</option>
					<option value="ftp">FTP采集组件</option>
					<option value="file">文件采集组件</option>
					<option value="web">WEB采集组件</option>
					<option value="others">其它</option>
					<option value="all">所有</option>
				</select>
				<input type="text" name="filter_level" class="easyui-validatebox" data-options="width:150,prompt:'任务名称'"/>
				<script>
					$(function() {
						$("#filter_type").combobox('setValue', '');
						$("#filter_state").combobox('setValue', '');
					});
				</script>
				
				<span class="toolbar-item dialog-tool-separator"></span> 
				<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search"	plain="true" onclick="">查询</a>
				<div>
					<a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-hamburg-future" onclick="del()">查看详细</a>
					<a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-remove" onclick="del()">清除选中的日志</a>
					<a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-standard-calendar-delete" onclick="del()">清除所有</a>
				</div>
			</form>
		</div>
	</div>
	
	<table id="logList"></table>
	
	<script>
		var dg;
		var d;
		$(function(){
			dg=$('#logList').datagrid({
			//title:'日志列表',
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
		    columns:[[    
		        {field:'ck',title:'选择',checkbox:true},
		        {field:'logLevel',title:'日志级别',sortable:true,width:80},   
		        {field:'createTime',title:'产生时间',sortable:true,width:100},    
		        {field:'origin',title:'日志来源',sortable:true,width:100,},
		        {field:'event',title:'事件',sortable:true,width:200},
		        {field:'detail',title:'详细内容',sortable:true,width:350},
		        {field:'taskName',title:'任务名称',sortable:true,width:120,}
		    ]],
		    headerContextMenu: [
		        {
		            text: "冻结该列", disabled: function (e, field) { return dg.datagrid("getColumnFields", true).contains(field); },
		            handler: function (e, field) { dg.datagrid("freezeColumn", field); }
		        },
		        {
		            text: "取消冻结该列", disabled: function (e, field) { return dg.datagrid("getColumnFields", false).contains(field); },
		            handler: function (e, field) { dg.datagrid("unfreezeColumn", field); }
		        }
		    ],
		    enableHeaderClickMenu: true,
		    enableHeaderContextMenu: true,
		    enableRowContextMenu: false,
		    toolbar:'#tb'
			});
 			$('#logList').datagrid('loadData', getData());
		});
		
		function getData() {
			var rows = [];
			rows.push({
				logLevel:'信息',
				createTime:'2015-12-11',
				origin:'系统登录',
				event:'管理员登录',
				detail:'管理员于2015-12-11 10：10登录系统成功',
				taskName:'-'
			});
			rows.push({
				logLevel:'错误',
				createTime:'2015-12-11',
				origin:'web采集组件',
				event:'连接数据源',
				detail:'青岛政务信息采集 连接数据源出错，错误代码404，原因：...',
				taskName:'青岛政务信息采集'
			});
			rows.push({
				logLevel:'信息',
				createTime:'2015-12-11',
				origin:'db采集组件',
				event:'采集完成',
				detail:'发改委内网数据采集成功，共采集10万条数据',
				taskName:'发改委内网数据采集'
			});
			rows.push({
				logLevel:'警告',
				createTime:'2015-12-11',
				origin:'ftp采集组件',
				event:'文件数据提取',
				detail:'xxx文件无法提取全文信息，只能提取文件元数据',
				taskName:'国家图书馆数据采集'
			});
			return rows;
		}
	</script>
</body>
</html>