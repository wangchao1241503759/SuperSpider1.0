<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>代理服务器设置</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'; font-size:9px;">
	<div id="tb" style="padding:5px;height:auto">
		<div>
    		<a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加</a>
    		<span class="toolbar-item dialog-tool-separator"></span>
	        <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        	<a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-edit">修改</a>
    	</div>
	</div>
	
	<table id="proxylist" class="easyui-datagrid" title="代理服务器列表 "
		style="width: 585px; height: 385px"
		data-options="rownumbers:true,singleSelect:false,pagination:false,pageSize:10">
		<thead>
			<tr>
				<th data-options="field:'ck',checkbox:true"></th>
				<th data-options="field:'proxyip',width:200">服务器地址</th>
				<th data-options="field:'proxyport',width:50">端口号</th>
				<th data-options="field:'proxyfrom',width:100">来源</th>
				<th data-options="field:'proxytype',width:50">类型</th>
			</tr>
		</thead>
	</table>
	
	<script type="text/javascript">
		$(function(){
			loadData();
		});
		
		function getProxyData() {
			var rows = [];
			rows.push({
				proxyip:'45.55.41.80',
				proxyport:'8080',
				proxyfrom:'美国',
				proxytype:'透明'
			});
			rows.push({
				proxyip:'195.154.231.43',
				proxyport:'3128',
				proxyfrom:'法国',
				proxytype:'匿名'
			});

			return rows;
		}

		function loadData(){
			$('#proxylist').datagrid({
				//loadFilter : pagerFilter
			}).datagrid('loadData', getProxyData());
		}
	</script>
</body>
</html>