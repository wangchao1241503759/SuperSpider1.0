<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>数据采集任务管理</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'; font-size: 9px;">
	<div class="easyui-layout" data-options="fit: true">
		<table>
			<tr>
				<td>数据库类型</td>
				<td>
					<select id="source_dbtype" class="easyui-combobox" name="state" style="width: 204px;" data-options="panelHeight:'auto'">
						<option value="MySQL">MySQL</option>
						<option value="SQLServer">SQLServer</option>
						<option value="Oracle">Oracle</option>
						<option value="DB2">DB2</option>
						<option value="MongDB">MongoDB</option>
						<option value="Arcgis">Arcgis</option>
						<option value="TRS">TRS</option>
						<option value="others">...</option>
					</select>
				</td>
				<td>数据库服务器：</td>
				<td><input id="source_ip" class="easyui-textbox"
					style="width: 200px;" value="192.168.1.1"></input></td>
			</tr>
			<tr>
				<td>端口号：</td>
				<td><input id="source_port" class="easyui-textbox"
					style="width: 200px;" value=""></input></td>
				<td>用户名：</td>
				<td><input id="source_user" class="easyui-textbox"
					style="width: 200px;" value=""></input></td>
			</tr>
			<tr>
				<td>密码：</td>
				<td><input id="source_password" type="password"
					class="easyui-textbox" style="width: 200px;" value=""></input></td>
				<td id="dbname" valign="middle">数据库名称：</td>
				<td><input id="source_dbname" class="easyui-textbox" style="width: 200px;" value=""></input></td>
				<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-hamburg-settings'">测试连接</a></td>
			</tr>
<!-- 			<tr> -->
<!-- 				<td>是否启用断点：</td> -->
<!-- 				<td> -->
<!-- 					<div> -->
<!-- 						<input type="radio" name="breakpoint" checked="checked" value="01"></input><span>是</span> -->
<!-- 							<input type="radio" name="breakpoint" value="02"></input><span>否</span> -->
<!-- 					</div> -->
<!-- 				</td> -->
<!-- 			</tr> -->
			<tr><td style="height:10px"></td></tr>
			<tr>
				<td>表空间：</td>
				<td><input id="source_dbname" class="easyui-textbox" style="width: 200px;" value=""></input></td>
				<td>数据包大小：</td>
				<td><input id="packagesize" class="easyui-textbox" style="width: 200px;" value="5000"></input></td>
				<td>（记录数/条）</td>
			</tr>
			<tr>
				<td>选择数据方式：</td>
				<td align="left">
					<input type="radio" name="source_table" value="01" onclick="selectSourceData()"/><span>选择表</span>
					<input type="radio" name="source_table" checked="checked" value="02" onclick="selectSourceData()"/><span>编写sql</span>
				</td>
			</tr>
		</table>
		<div style="height:5px"></div>
		<div id="select_table" class="easyui-panel" style="border:none;padding-left:39px;display:none">
			数据表：
			<select id="source_table" class="easyui-combobox" name="state" style="width: 204px;" data-options="panelHeight:'auto'">
					<option value="table1">table1</option>
					<option value="table2">table2</option>
					<option value="table3">table3</option>
			</select>
		</div>
		<div id="sqlscripter" class="easyui-panel" style="border:none;padding-left:35px;display:block">
			sql语句：
			<input id="sqlscript" class="easyui-textbox" style="width: 495px;height:100px" value="select * from table1" data-options="multiline:true"></input>
			
			<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-hamburg-settings'">测试sql</a>		
		</div>
	</div>
	
	<script>
		$('#source_dbtype').combobox({
			onChange : function(newValue, oldValue) {
				var titleElement=document.getElementById("dbname");
				if(newValue=="Oracle"){
					titleElement.innerText="实例名：";
				}
				else{
					titleElement.innerText="数据库名称：";
				}
				
				if(newValue=="TRS"){
					$('#dbname').hide();
					$('#source_dbname').hide();
				}
				else{
					$('#dbname').show();
					$('#source_dbname').show();
				}
			}
		});
		
		function selectSourceData(){
			if($("input[name='source_table']:checked").val()=="01"){
				$('#select_table').show();
				$('#sqlscripter').hide();
			}else{
				$('#select_table').hide();
				$('#sqlscripter').show();
			}
		}
	</script>
</body>
</html>