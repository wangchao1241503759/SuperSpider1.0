<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>系统日志管理</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
</head>
<body>
<table id="taskloglist"></table>
<script>
$(function(){
	$('#taskloglist').datagrid({    
	    url:'${ctx}/taskLogController/dataGrid?taskId='+$('#taskId').val()+'&event=' + $('#event').val(), 
	    fit : true,
		fitColumns : true,
		border : false,
		idField : 'id',
		sortName:'createTime',
		sortOrder:'desc',
		striped:true,
		rowTooltip: true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:false,
	    columns:[[
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
	        {field:'event',title:'事件',sortable:true,width:10,
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
		        		} else if(value=="runtime"){
		        			 str = '正在运行中';
		        		}
		        		return str;
		        	 }
	        },
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
	
});

</script>
<input type="hidden" id="taskId" value="${taskId}">
<input type="hidden" id="event" value="${event}">
</body>
</html>