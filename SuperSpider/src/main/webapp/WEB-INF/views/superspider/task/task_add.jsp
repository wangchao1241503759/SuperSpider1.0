<%@ page language="java" contentType="text/html; charset=UTF-8"pageEncoding="UTF-8"%>
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
				<td>任务名称:</td>
				<td><input id="taskName" name = "taskName" class="easyui-textbox"
					style="width: 200px;" value=""></input></td>
				<td>任务类型:</td>
				<td><input id="taskType" name="taskType" class="easyui-textbox"
					style="width: 200px;" value="(db\ftp\file\web)获取任务" disabled=true></input></td>
			</tr>
			<tr>
				<td>任务说明：</td>
				<td colspan="3"><input id="description" name="description" class="easyui-textbox"
					style="width: 462px;" value="采集任务说明"></input></td>

			</tr>
			<tr>
				<td>任务计划：</td>
				<td colspan="3">
					<div style="width: 100%">
						<select id="taskplan" class="easyui-combobox" name="planId"
							style="width: 380px" data-options="panelHeight:'auto'">
							<option value="plan1">周末凌晨采集</option>
							<option value="plan2">每天凌晨采集</option>
							<option value="plan3">每2周的周末凌晨采集</option>
							<option value="plan4">每2小时采集一次</option>
							<option value="plan5">每3天凌晨采集</option>
							<option value="其它">其它</option>
						</select> <a href="#" class="easyui-linkbutton"
							data-options="align:'right',iconCls:'icon-standard-add'"
							onclick="add()">新建计划</a>
					</div>
				</td>
			</tr>
			<tr>
				<td>任务计划描述：</td>
				<td colspan="3">
					<div>注：从2016年1月1日开始，每周六、周日24：00开始执行</div>
				</td>
			</tr>
			<tr>
				<td>创建人:</td>
				<td><input id="creator" class="easyui-textbox"
					style="width: 200px;" value="数据获取管理员" disabled=true></input></td>
				<td>创建时间:</td>
				<td><input id="createDate" class="easyui-textbox"
					style="width: 200px;" value="2015-12-31 10:00" disabled=true></input></td>
			</tr>
		</table>
	</div>

	<script type="text/javascript">
		//弹窗新建计划
		function add() {
			var handle=parent.parent.$("#dlg");
			d = handle.dialog({
				title : '新建计划',
				width : 510,
				height : 330,
				closed : false,
				cache : false,
				maximizable : false,
				resizable : false,

				href:'${ctx}/spider/config/plan',
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
	</script>
</body>
</html>