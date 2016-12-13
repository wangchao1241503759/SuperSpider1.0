<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>http数据源</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'; font-size: 9px;">
	<div class="easyui-layout" data-options="fit: true">
		<table>
			<tr>
				<td>web目标url：</td>
				<td colspan="3">
					<input id="source_uri" class="easyui-textbox" style="width: 99%;" value="http://sina.com.cn"></input>
				</td>
			</tr>
			
			<tr>
			    <td>站点字符集：</td>
				<td><input id="characterset" class="easyui-textbox" style="width: 190px;" value="utf-8"></input></td>
				<td>请求超时时间：</td>
				<td><input id="timeout" class="easyui-textbox" style="width: 190px;" value="10"></input></td>
				<td>秒</td>
			</tr>
			<tr>
				<td>出错重试次数：</td>
				<td><input id="retry" class="easyui-textbox" style="width: 190px;" value="3"></input></td>
				<td>异步加载等待时间：</td>
				<td><input id="asyn_timespan" class="easyui-textbox" style="width: 190px;" value="5"></input></td>
				<td>秒</td>
			</tr>
			<tr>
				<td>爬取线程数：</td>
				<td>
					<select id="source_dbtype" class="easyui-combobox" name="state" style="width: 194px;" data-options="panelHeight:'auto'">
						<option value="1">1</option>
						<option value="5">5</option>
						<option value="10">10</option>
						<option value="15">15</option>
						<option value="20">20</option>
						<option value="25">25</option>
						<option value="30">30</option>
						<option value="35">35</option>
						<option value="40">40</option>
						<option value="45">45</option>
						<option value="50">50</option>
					</select>
				</td>
				<td>爬取间隔时间：</td>
				<td><input id="fetch_timespan" class="easyui-textbox" style="width: 190px;" value="0"></input></td>
				<td>秒</td>
			</tr>
			<tr>
				<td>是否需要登录：</td>
				<td>
					<input id="needlogin" type="radio" name="login" checked="checked" value="01" onclick="enableInput()"/><span>是</span>
					<input id="nologin" type="radio" name="login" value="02" onclick="enableInput()"/><span>否</span>
				</td>
			</tr>
			<tr>
				<td align="right">用户名：</td>
				<td><input id="user" class="easyui-textbox" style="width: 190px;" value=""></input></td>
				<td align="right">密码：</td>
				<td><input id="password" class="easyui-textbox" style="width: 190px;" value=""></input></td>
			</tr>
<!-- 			<tr> -->
<!-- 				<td>是否启用断点：</td> -->
<!-- 				<td><input type="radio" name="breakpoint" checked="checked" value="01" onclick="enableInput()"/><span>是</span> -->
<!-- 					<input type="radio" name="breakpoint" value="02" onclick="enableInput()"/><span>否</span> -->
<!-- 				</td> -->
<!-- 				<td>是否抓取https：</td> -->
<!-- 				<td><input type="radio" name="includeHttps" checked="checked" value="01" onclick="enableInput()"/><span>是</span> -->
<!-- 					<input type="radio" name="includeHttps" value="02" onclick="enableInput()"/><span>否</span> -->
<!-- 				</td> -->
<!-- 			</tr> -->
<!-- 			<tr> -->
<!-- 			    <td>是否限制速度：</td> -->
<!-- 				<td> -->
<!-- 					<input type="radio" name="speed" checked="checked" value="01" onclick="enableInput()"/><span>是</span> -->
<!-- 					<input type="radio" name="speed" value="02" onclick="enableInput()"></input><span>否</span></td> -->
<!-- 				<td>爬取速度：<=</td> -->
<!-- 				<td><input id="fetch_speed" class="easyui-textbox" -->
<!-- 					style="width: 150px;" value=""></input>kb/s</td> -->
<!-- 			</tr> -->
			<tr>
				<td>是否使用代理：</td>
				<td>
					<input type="radio" name="proxy" checked="checked" value="01" onclick="enableInput()"/><span>是</span>
					<input type="radio" name="proxy" value="02" onclick="enableInput()"/><span>否</span>
				</td>
				<td colspan="2" align="right">
					<a id="select_proxy" href="#" class="easyui-linkbutton"	data-options="align:'right',iconCls:'icon-standard-add'" onclick="selectProxy()">选择代理</a>
					<a id="select_proxy" href="#" class="easyui-linkbutton"	data-options="align:'right',iconCls:'icon-standard-delete'">移除代理</a>
				</td>
			</tr>
			<tr>
				<td></td>
				<td colspan="3">
<!-- 					<input id="proxy_edit" class="easyui-textbox" style="width: 100%;height:40px" value="45.55.41.80:8080(美国:透明);195.154.231.43:3128(法国：匿名);"></input> -->
					<table id="proxylist" class="easyui-datagrid" title="已选择的代理服务器列表 " style="width: 502px; height: 140px"	data-options="rownumbers:true,singleSelect:false,pagination:false,pageSize:10">
						<thead>
							<tr>
								<th data-options="field:'ck',checkbox:true"></th>
								<th data-options="field:'proxyip',width:160">服务器地址</th>
								<th data-options="field:'proxyport',width:60">端口号</th>
								<th data-options="field:'proxyfrom',width:120">来源</th>
								<th data-options="field:'proxytype',width:60">类型</th>
							</tr>
						</thead>
					</table>
				</td>
			</tr>
		</table>
	</div>
	
	<script>
	function enableInput(){
	    $('#user').attr("disabled",$("input[name='login']:checked").val()=="02");
	    $('#password').attr("disabled",$("input[name='login']:checked").val()=="02");
	    
	    $('#select_proxy').attr("disabled",$("input[name='proxy']:checked").val()=="02");
	    $('#proxylist').attr("disabled",$("input[name='proxy']:checked").val()=="02");
	    
	    $('#fetch_speed').attr("disabled",$("input[name='speed']:checked").val()=="02");
	}
	
	//弹窗新建计划
	function selectProxy() {
		var handle=parent.$("#dlg");
		d = handle.dialog({
			title : '选择代理服务器',
			width : 600,
			height : 500,
			closed : false,
			cache : false,
			maximizable : false,
			resizable : false,

			href:'${ctx}/spider/config/proxy',
			modal : true,
			buttons : [ {
				text : '确认',
				handler : function() {
					d.panel('close');
				}
			}, {
				text : '取消',
				handler : function() {
					d.panel('close');
				}
			} ]
		});
	}
	
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