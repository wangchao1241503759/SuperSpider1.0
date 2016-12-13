<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>系统日志管理</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>

<style type="text/css">
<!--
img, input, select, button{vertical-align:middle;}
-->
</style>
</head>
<body>
<div id="tb" style="padding:5px;height:auto">
	<div>
	    <form id="searchFrom" action="">
    		<select	id="level" class="easyui-combobox" name="level" data-options="width:150,prompt:'日志级别'" data-options="panelHeight:120,editable:false">
				<option value="INFO">信息</option>
				<option value="WARN">警告</option>
				<option value="ERROR">错误</option>
			</select>
			<input type="text" name="createTimeStart" class="easyui-my97" datefmt="yyyy-MM-dd" readonly="readonly" data-options="width:150,prompt: '开始日期'" /> - 
			<input type="text" name="createTimeEnd" class="easyui-my97" datefmt="yyyy-MM-dd" readonly="readonly" data-options="width:150,prompt: '结束日期'" />
			<select id="source" class="easyui-combobox" name="source" data-options="width:150,prompt: '来源'" data-options="panelHeight:'auto'">
				<option value="db">数据库采集组件</option>
				<option value="ftp">FTP采集组件</option>
				<option value="file">文件采集组件</option>
				<option value="web">WEB采集组件</option>
				<option value="">所有</option>
			</select>
			<input type="text" name="taskName" class="easyui-validatebox" data-options="width:150,prompt:'任务名称'"/>
			<span class="toolbar-item dialog-tool-separator"></span>
			<input type="hidden" id="taskId" value="${taskId}"/>
			<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx();">查询</a>
		</form>
    	<span class="toolbar-item dialog-tool-separator"></span>
        <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-remove" onclick="batchDeleteFun()">删除</a>
        <span class="toolbar-item dialog-tool-separator"></span>
        <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-standard-calendar-delete" onclick="deleteAllFun()">清除所有</a>
        <span class="toolbar-item dialog-tool-separator"></span>
        <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-standard-application-go" onclick="exceptionProcess();">导入异常数据</a>
   	</div>
</div>
<table id="taskloglist"></table>
<div id="dlg"></div> 
<div id="dd"></div> 
<script>
var dg;
var d;
$(function(){
	
	var url = '${ctx}/taskLogController/dataGrid';
	if($('#taskId').val() != ''){
		url = '${ctx}/taskLogController/dataGrid?taskId=' + $('#taskId').val();
	} 
	dg=$('#taskloglist').datagrid({    
		method: "get",
	    url: url,
	    fit : true,
		fitColumns : true,
		border : false,
		idField : 'id',
		sortName:'createTime',
		sortOrder:'desc',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:false,
	    columns:[[
	        {field:'ck',checkbox:true},  
	        {field:'id',title:'id',hidden:true},    
	        {field:'level',title:'日志级别',sortable:true,width:7,
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
	        {field:'createTime',title:'创建时间',sortable:true,width:15,
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
	        {field:'source',title:'日志来源',sortable:true,width:10,
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
	        {field:'event',title:'事件',sortable:true,width:6,
	        	 formatter:function(value,row,index){
		        		var str='';
		        		if(value=="error" || value=="ERROR")
		        		{
		        			 str = '错误';
		        		}
		        		else if(value=="start")
		        		{
		        			 str = '开始';
		        		}
		        		else if(value=="end")
		        		{
		        			 str = '结束';
		        		}
		        		else if(value=="fetchFinshNum")
		        		{
		        			 str = '采集数量';
		        		}
		        		return str;
		        	 }
	        },
	        {field:'content',title:'详细内容',sortable:true,width:37},
	        {field:'taskName',title:'任务名称',sortable:true,width:20}
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
	    rowTooltip: true,
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
				$.getJSON('${pageContext.request.contextPath}/taskLogController/deleteBatch', {
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
				$.getJSON('${pageContext.request.contextPath}/taskLogController/deleteAll', {
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
		      href : '${pageContext.request.contextPath}/taskLogController/viewPage?id=' + id,
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
	dg.datagrid('unselectAll').datagrid('uncheckAll');
}

/**
 * 异常数据导入
 */
function exceptionHandling(id)
{
	
	parent.$.messager.confirm('提示', '您确定要导入异常数据吗？', function(data){
		if (data){
				$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
				var url = '${pageContext.request.contextPath}/taskLogController/exceptionHandling';
				
				$.ajax({
					type:'post',
					url:url,
					data:{id:id},
					success:function(result)
					{
						if (result.success) {
						  	$.messager.alert('成功', result.msg, 'info');
						} else {
							parent.$.messager.alert('错误', result.msg, 'error');
						}
						parent.$.messager.progress('close');
					},
					error:function (XMLHttpRequest, textStatus, errorThrown) {
					    // 通常 textStatus 和 errorThrown 之中
					    // 只有一个会包含信息
					    this; // 调用本次AJAX请求时传递的options参数
					}
				});	
		}});
}

/**
 * 异常数据处理
 */
function exceptionProcess()
{
// 	parent$.modalDialog({
// 		title : '异常数据处理',
// 		width : 800,
// 		height : 400,
// 		href : '${ctx}/taskLogController/dataGridError'
// 	});
	$('#dd').dialog({    
	    title: '异常数据处理',    
	    width: 800,    
	    height: 400,    
	    closed: false,    
	    cache: false,    
	    href: '${ctx}/taskLogController/errortask',    
	    modal: true,
	    buttons:[
	        {
	        	text:'保存',
				handler:function(){
					dataGrid = $('#dataGrids');
					var checkrows = dataGrid.datagrid('getChecked');
					if(checkrows!=null && checkrows.length>0)
					{
						parent.$.messager.confirm('确认', '您确定要处理所选的异常数据吗？', function(con) {
							if (con) 
							{
								parent.$.messager.progress({
									title : '提示',
									text : '数据处理中，请稍后....'
								});
								var ids = [];
								for(var i=0;i<checkrows.length;i++)
								{
									ids.push(checkrows[i].taskId);
								}
								var url='${ctx}/taskLogController/exceptionDataProcess';
								$.ajax({
									type:'post',
									url:url,
									data:{ids:ids.join(',')},
									dataType:'json',
									success:function(data)
									{
										if (data.success) {
											parent.$.messager.alert('提示', data.msg, 'info');
											dataGrid.datagrid('load');
											dataGrid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
										}
										else
										{
											parent.$.messager.alert('提示', data.msg, 'error');
										}
										parent.$.messager.progress('close');
									},
									error:function (XMLHttpRequest, textStatus, errorThrown) {
									    // 通常 textStatus 和 errorThrown 之中
									    // 只有一个会包含信息
									    this; // 调用本次AJAX请求时传递的options参数
									    parent.$.messager.progress('close');
									}
								});
							}
						});
					}
					else
					{
						$.messager.alert('提示','请至少选择一条记录进行处理','info');
					}
				}
	        },
	        {
	        	text:'关闭',
				handler:function(){
					$('#dd').dialog('close');
				}
	        }
	    ]
	});    
	//$('#dd').dialog('refresh', '${ctx}/taskLogController/dataGridError');  


}

</script>
</body>
</html>