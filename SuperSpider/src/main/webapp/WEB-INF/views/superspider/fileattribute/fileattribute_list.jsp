<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<title>数据采集任务管理</title>
</head>
<body>
<div id="fileattributetb" style="padding: 5px; height: auto">
	<!-- <div>
		<form id="searchFrom" action="">
			<input type="text" name="filter_LIKES_taskName" class="easyui-validatebox" data-options="width:150,prompt: '属性名称'" />
			<select id="filter_EQS_fileType" class="easyui-combobox"  name="filter_EQS_fileType" data-options="width:150,prompt: '文件类型'"></select>
			<span class="toolbar-item dialog-tool-separator"></span> 
			<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx();">查询</a>
		</form>
	</div> -->
</div>
<table id="attributeList"></table>
<script type="text/javascript">
var attrDg;
$(function(){
	var noDefaultIds = '${noDefaultIds}';
	attrDg=$('#attributeList').datagrid({    
		method: "get",
	    url:"${pageContext.request.contextPath}/fileAttributeController/getNoDefaultAttributes?noDefaultIds="+noDefaultIds, 
	    fit : true,
		fitColumns : true,
		border : false,
		idField : 'fileAttributeId',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:false,
	    columns:[[
	        {field:'fileAttributeId',title:'属性ID',checkbox:true},    
	        {field:'fileAttributeName',title:'属性名称',sortable:true,width:30},    
	        {field:'fileTypeValue',title:'关联文件类型',sortable:true,width:70}
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
	    toolbar:'#fileattributetb'
	});
	/* $("#filter_EQS_fileType").combobox("loadData", 
	[ {
        'id' : '0',
        'text' : '最低'
      }, {
        'id' : '1',
        'text' : '低'
      }, {
        'id' : '2',
        'text' : '中'
      }, {
        'id' : '3',
        'text' : '高'
      }, {
        'id' : '4',
        'text' : '最高'
      } ]
); */
	//$("#fileType").combobox('setValue','');
});
</script>
</body>
</html>