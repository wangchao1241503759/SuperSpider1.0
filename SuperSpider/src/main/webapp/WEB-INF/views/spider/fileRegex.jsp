<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>文件解析规则</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'; font-size: 9px;">
	<div class="easyui-layout" data-options="fit: true">
		<div class="easyui-tabs" data-options="fit:false,border:false">
			<div title="文件属性提取">
				<table id="commonattr" class="easyui-datagrid" title="选择要提取的属性"
					style="width: 800px; height: 230px"
					data-options="rownumbers:true,singleSelect:false,url:'${ctx}/spider/config/fileattr/json',method:'get'">
					<thead>
						<tr>
							<th data-options="field:'ck',checkbox:true"></th>
							<th data-options="field:'attributeName',width:150">属性名称</th>
							<th data-options="field:'refrenceType',width:200">关联文件类型</th>
							<th data-options="field:'fieldName',width:150">目标字段名称</th>
							<th data-options="field:'fieldType',width:200">目标字段类型</th>
						</tr>
					</thead>
				</table>
				
				<div style="height:10px"></div>
				<div>
					<a href="#" class="easyui-linkbutton" data-options="align:'right',iconCls:'icon-standard-add'">添加提取属性</a>
					<a href="#" class="easyui-linkbutton" data-options="align:'right',iconCls:'icon-standard-delete'">删除提取属性</a>
					<a href="#" class="easyui-linkbutton" data-options="align:'right',iconCls:'icon-standard-application-view-columns'">同名自动匹配</a>
				</div>
			</div>
		</div>
	</div>
		
</body>
</html>