<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>系统日志管理</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
</head>
<body>
<div id="tb" style="padding:5px;height:auto">
	<div>
	    <form id="searchFrom" action="">
    		<select	id="level" class="easyui-combobox" name="level" data-options="width:150,prompt:'日志级别'" data-options="panelHeight:'auto'">
				<option value="INFO">信息</option>
				<option value="WARN">警告</option>
				<option value="ERROR">错误</option>
			</select>
			<input type="text" name="createTimeStart" class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width:150,prompt: '开始日期'" /> - 
			<input type="text" name="createTimeEnd" class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width:150,prompt: '结束日期'" />
			<select id="source" class="easyui-combobox" name="source" data-options="width:150,prompt: '来源'" data-options="panelHeight:'auto'">
				<option value="db">数据库采集组件</option>
				<option value="ftp">FTP采集组件</option>
				<option value="file">文件采集组件</option>
				<option value="web">WEB采集组件</option>
				<option value="">所有</option>
			</select>
			<span class="toolbar-item dialog-tool-separator"></span>
			<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx();">查询</a>
		</form>
    	<a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-hamburg-future" onclick="viewFun();">查看详细</a>
    	<span class="toolbar-item dialog-tool-separator"></span>
        <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-remove" onclick="batchDeleteFun()">删除</a>
        <span class="toolbar-item dialog-tool-separator"></span>
        <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-standard-calendar-delete" onclick="deleteAllFun()">清除所有</a>
   	</div>
</div>
<table id="taskloglist"></table>
<div id="dlg"></div> 
<script>
var dg;
var d;
$(function(){
	dg=$('#taskloglist').datagrid({    
		method: "get",
	    url:'${ctx}/systemLogController/dataGrid', 
	    fit : true,
		fitColumns : true,
		border : false,
		idField : 'id',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:false,
	    columns:[[
	        {field:'id',title:'ID',checkbox:true},    
	        {field:'level',title:'日志级别',sortable:true,width:20,
	        	 formatter:function(value,row,index){
	        		var str='';
	        		if(value=="error" || value=="ERROR")
	        		{
	        			 str = '错误';
	        		}
	        		else if(value=="warn" || value=="WARN")
	        		{
	        			 str = '警告';
	        		}
	        		else if(value=="info" || value=="INFO")
	        		{
	        			 str = '信息';
	        		}
	        		return str;
	        	 }
	        },    
	        {field:'createTime',title:'创建时间',sortable:true,width:20,
	         formatter:function(value,row,index){
		      	var str="";
		      	if(value!=null && value!="")
		      	{
                  var unixTimestamp = new Date(value);  
                  str= unixTimestamp.toLocaleString();  
			    }
			    return str;
              }
	        },
	        {field:'source',title:'日志来源',sortable:true,width:15,
	        	 formatter:function(value,row,index){
		        		var str='';
		        		if(value=="db")
		        		{
		        			 str = '数据库采集组件';
		        		}
		        		else if(value=="ftp")
		        		{
		        			 str = 'FTP采集组件';
		        		}
		        		else if(value=="file")
		        		{
		        			 str = '文件采集组件';
		        		}
		        		else if(value=="web")
		        		{
		        			 str = 'WEB采集组件';
		        		}
		        		return str;
		        	 }	
	        },
	        {field:'event',title:'事件',sortable:true,width:30},
	        {field:'content',title:'详细内容',sortable:true,width:20},
	        {field:'taskName',title:'任务名称',sortable:true,width:10}
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
	
	
	
 	$("#level").combobox('setValue', '');
	$("#source").combobox('setValue', '');
});


// 批量删除
function batchDeleteFun() {
	var rows = dg.datagrid('getChecked');
	var ids = [];
	if (rows.length > 0) {
		parent.$.messager.confirm('确认', '删除后无法恢复您确定要删除？', function(con) {
			if (con) {
				parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
				for (var i = 0; i < rows.length; i++) {
					ids.push(rows[i].id);
				}
				$.getJSON('${pageContext.request.contextPath}/systemLogController/deleteBatch', {
					ids : ids.join(',')
				}, function(result) {
					if (result.result) {
						dg.datagrid('load');
						dg.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
					}
					if (result.success) {
						parent.$.messager.alert('成功', result.msg, 'info');
						dg.datagrid('reload');
						dg.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
					} else {
						parent.$.messager.alert('警告', result.msg, 'warning');
					}
					parent.$.messager.progress('close');
				});
			}
		});
	} else {
		parent.$.messager.alert('警告', '请选择需要删除的记录', 'warning');
	}
}
// 删除全部
function deleteAllFun() {
	var rows = dg.datagrid('getRows');
	var ids = [];
	if (rows.length > 0) {
		parent.$.messager.confirm('确认', '删除后无法恢复您确定要删除？', function(con) {
			if (con) {
				parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
				for (var i = 0; i < rows.length; i++) {
					ids.push(rows[i].id);
				}
				$.getJSON('${pageContext.request.contextPath}/systemLogController/deleteAll', {
					ids : ids.join(',')
				}, function(result) {
					if (result.result) {
						dg.datagrid('load');
						dg.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
					}
					if (result.success) {
						parent.$.messager.alert('成功', result.msg, 'info');
						dg.datagrid('reload');
						dg.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
					} else {
						parent.$.messager.alert('警告', result.msg, 'warning');
					}
					parent.$.messager.progress('close');
				});
			}
		});
	} else {
		parent.$.messager.alert('警告', '没有需要删除的记录', 'warning');
	}
}


//弹窗查看
function viewFun(){
	var row = dg.datagrid('getChecked');
	if (row.length == 1) {
		var id = row[0].id;
		if(rowIsNull(row)) return;
		d=$("#dlg").dialog({   
		    title: '查看',    
		      width : 400,
		      height : 300,  
		      href : '${pageContext.request.contextPath}/systemLogController/viewPage?id=' + id,
		    maximizable:true,
		    modal:true,
		    buttons:[{
				text:'关闭',
				handler:function(){
					d.panel('close');
				}
			}]
		});
	}
	else
	{
		parent.$.messager.alert('警告', '请选择一条需要查看的记录', 'warning');
	}
}

//创建查询对象并查询
function cx(){
	var obj=$("#searchFrom").serializeObject();
	dg.datagrid('load',obj); 
}
</script>
</body>
</html>