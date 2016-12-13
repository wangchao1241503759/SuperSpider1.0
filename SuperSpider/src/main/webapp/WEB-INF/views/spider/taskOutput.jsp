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
				<td>数据库类型：</td>
				<td>
					<select id="target_dbtype" class="easyui-combobox" name="state" style="width: 209px" data-options="panelHeight:'auto'">
						<option value="MySQL">MySQL</option>
						<option value="SQLServer">SQLServer</option>
						<option value="Oracle">Oracle</option>
						<option value="DB2">DB2</option>
						<option value="MongDB">MongoDB</option>
						<option value="msp">元数据仓储</option>
						<option value="others">...</option>
					</select>
				</td>
				<td id="lb_dbserver">数据库服务器：</td>
				<td><input id="target_ip" class="easyui-textbox" style="width: 205px;" value="192.168.1.1"></input></td>
			</tr>
			<tr>
				<td>端口号：</td>
				<td><input id="target_port" class="easyui-textbox" style="width: 205px;" value=""></input></td>
				<td>用户名：</td>
				<td><input id="target_user" class="easyui-textbox" style="width: 205px;" value=""></input></td>
			</tr>
			<tr>
				<td>密码：</td>
				<td><input id="target_password" type="password" class="easyui-textbox" style="width: 205px;" value=""></input></td>
				<td id="lb_dbname">数据库名称：</td>
				<td><input id="target_dbname" class="easyui-textbox" style="width: 205px;" value=""></input></td>
				<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-hamburg-settings'">测试连接</a></td>
			</tr>
			<tr><td height="10px"></td></tr>
			<tr>
				<td id="lb_tb">数据表：</td>
				<td id="se_tb">
					<select id="target_table" class="easyui-combobox" name="state" style="width: 209px;" data-options="panelHeight:'auto'">
						<option value="table1">期刊</option>
						<option value="table2">文献</option>
						<option value="table3">办公</option>
						<option value="table3">...</option>
					</select>
				</td>
				<td></td>
				<td>
					是否清空目标数据表：
					<input type="radio" name="clear" checked="checked" value="01"/><span>是</span>
					<input type="radio" name="clear" value="02"/><span>否</span>
				</td>
			</tr>
		</table>
		
	</div>
	
	<script>
		//目标数据库改变
		$('#target_dbtype').combobox({
			onChange : function(newValue, oldValue) {
				if (newValue == "msp") {
					
					document.getElementById("lb_dbserver").innerText="配置库服务器：";
					document.getElementById("lb_dbname").innerText="配置库名称：";
					document.getElementById("lb_tb").innerText="元数据库：";
					
// 					$('#target_ip').val("192.168.1.100");
// 					$('#target_port').val("80");
// 					$('#target_user').val("msp_admin");
// 					$('#target_password').val("12345");
					
// 					$('#lb_tb').hide();
// 					$('#se_tb').hide();
				} else {
					document.getElementById("lb_dbserver").innerText="数据库服务器：";
					document.getElementById("lb_dbname").innerText="数据库名称：";
					document.getElementById("lb_tb").innerText="数据表：";
					
// 					$('#target_ip').val("192.168.1.1");
// 					$('#target_port').val("");
// 					$('#target_user').val("");
// 					$('#target_password').val("");
					
// 					$('#lb_tb').show();
// 					$('#se_tb').show();
				}
// 				$('#target_ip').attr("disabled", newValue == "msp");
// 				$('#target_port').attr("disabled", newValue == "msp");
// 				$('#target_user').attr("disabled", newValue == "msp");
// 				$('#target_password').attr("disabled", newValue == "msp");
			}
		});
	</script>
</body>
</html>