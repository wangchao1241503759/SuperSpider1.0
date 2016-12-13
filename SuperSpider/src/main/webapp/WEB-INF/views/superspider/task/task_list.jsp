<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>数据采集任务管理</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
</head>
<body>
<div id="tb" style="padding: 5px; height: auto">
	<div>
		<form id="searchFrom" action="">
			<input type="text" name="filter_LIKES_taskName" class="easyui-validatebox" data-options="width:150,prompt: '任务名称'" />
			<select id="filtertype" class="easyui-combobox" name="filter_EQS_taskType" data-options="width:150,prompt: '任务类型'" data-options="panelHeight:'auto'">
				<option value="db">数据库采集</option>
				<option value="web">WEB采集</option>
				<option value="ftp">FTP采集</option>
				<option value="file">文件采集</option>				
				<option value="">所有</option>
			</select>
			<input type="text" name="filter_GED_taskCreateDate" class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width:150,prompt: '开始日期'" /> - 
			<input type="text" name="filter_LED_taskCreateDate" class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width:150,prompt: '结束日期'" />
			<select id="filterstate" class="easyui-combobox" name="filter_EQS_taskState" data-options="width:150,prompt:'任务状态'" data-options="panelHeight:'auto'">
				<option value="0">闲置</option>
				<option value="1">执行中</option>
				<option value="-1">禁用</option>
				<option value="">所有</option>
			</select>
			<span class="toolbar-item dialog-tool-separator"></span> 
			<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx();">查询</a>
		</form>
		<a href="javascript:void(0)" class="easyui-menubutton" data-options="menu:'#task_type_set'" iconCls="icon-standard-clock-add" plain="true">创建任务</a>
		<div id="task_type_set">
			<div name="task_type" type="db" data-options="iconCls:'icon-hamburg-billing'">数据库采集</div>
			<div name="task_type" type="web" data-options="iconCls:'icon-hamburg-world'">WEB采集</div>
			<div name="task_type" type="ftp" data-options="iconCls:'icon-hamburg-archives'">FTP采集</div>
			<div name="task_type" type="file" data-options="iconCls:'icon-hamburg-folder'">文件采集</div>
		</div>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-clock-edit" plain="true" onclick="edit();">修改</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a id="btnCopy" class="easyui-linkbutton" iconCls="icon-standard-page-copy" plain="true" onclick="copyTask();">复制</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-clock-delete" plain="true" onclick="del();">删除</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a id="btnExport" class="easyui-linkbutton" iconCls="icon-hamburg-sign-out" plain="true" onclick="doExport();">导出</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a id="btnImport" class="easyui-linkbutton" iconCls="icon-hamburg-sign-in" plain="true" onclick="doImport();">导入</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a id="btnOnOff" class="easyui-linkbutton" iconCls="icon-standard-clock-pause" plain="true" onclick="on_off();">启用/禁用</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-clock-pause" id="startButton" plain="true" onclick="start();">启动</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-clock" id="stopButton" plain="true" onclick="stop();">停止</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a id="btnImport" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="showConsoleLog();">控制台</a>
	</div>
</div>
<table id="tasklist"></table>
<div id="dlg"></div>  
<script type="text/javascript">
var dg;
var d;
var dbTabTitle = '数据库采集配置';
var webTabTitle = 'WEB采集配置';
var ftpTabTitle = 'FTP采集配置';
var fileTabTitle = '文件采集配置';
$(function(){
	//initMenu();
	$("#task_type_set").find("div[name='task_type']").bind("click",function(){
			var taskType = $(this).attr("type");
			create(taskType);
	}); 
	//var currTab = parent.mainTabs.tabs('getSelected');
	//parent.mainTabs.tabs('setIconCls',{tab:currTab,iconCls:"icon-hamburg-delicious"});
	//parent.mainTabs.tabs('setTabTitle',{tab:currTab,title:dbTabTile});
	//parent.mainTabs.tabs('setHref',{tab:currTab,href:"task/list"});
	$("#filtertype").combobox('setValue', '');
	$("#filterstate").combobox('setValue', '');
	$("#taskType").combobox({
		onChange:function(newValue,oldValue){
			$("#taskType").combobox('setValue',newValue);
		 }
	});
	dg=$('#tasklist').datagrid({    
	method: "get",
    url:'${ctx}/task/json', 
    fit : true,
	fitColumns : true,
	border : false,
	idField : 'taskId',
	striped:true,
	pagination:true,
	rownumbers:true,
	pageNumber:1,
	pageSize : 10,
	pageList : [ 10, 20, 30, 40, 50 ],
	singleSelect:false,
    columns:[[    
        {field:'taskId',title:'任务ID',checkbox:true},    
        {field:'taskName',title:'任务名称',sortable:true,width:50},    
        {field:'taskType',title:'任务类型',sortable:true,width:20,
        	formatter : function(value, row, index) {
        		var typeName = "";
        		if(value == "db")
        		{
        			typeName = "数据库";
        		}else if(value == "ftp"){
        			typeName = "FTP";
        		}else if(value == "file"){
        			typeName = "文件";
        		}else if(value == "web"){
        			typeName = "WEB";
        		}
   				return typeName;
    		}
        },
        {field:'taskDescription',title:'任务说明',sortable:true,width:70},
        {field:'taskCreateDate',title:'创建时间',sortable:true,width:50,
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
        {field:'taskCreator',title:'创建人',sortable:true,width:30},
        {field:'planName',title:'计划名称',sortable:true,width:30},
        {field:'planDescription',title:'计划描述',sortable:true,width:50},
        {field:'finishCount',title:'已完成',sortable:true,width:30},
        {field:'successCount',title:'成功数',sortable:true,width:30},
        {field:'failCount',title:'失败数',sortable:true,width:30},
        {field:'taskState',title:'任务状态',sortable:true,width:20,
        	formatter : function(value, row, index) {
        		var state = "";
        		if(value == "0")
        		{
        			state = "闲置";
        		}else if(value == "1" || value == "3"){
        			state = "执行中";
        		}else if(value == "2"){
        			state = "暂停";
        		}else if(value == "-1"){
        			state = "禁用";
        		}
   				return state;
    		}
        }/* ,
        {field:'console',title:'控制台',sortable:true,width:20,
        	formatter : function(value, rowData, rowIndex) {
        		if(rowData.taskState == '1'){
        			return '<a href="javascript:void(0);" style="text-align: center;" onclick="showConsoleLog(\'' + rowData.taskId + '\');"><font color="blue">查看控制台</font></a>';
        		} else {
        			return '<a href="javascript:void(0);" style="text-align: center; cursor:default" onclick="return false;"><font color="#C0C0C0">查看控制台</font></a>';
        		}
        		
    		}
        } */
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
    onCheck:onCheck,
    onUncheck:onUncheck,
    onLoadSuccess:onLoadSuccess,
    toolbar:'#tb'
	});
	setInterval(function() {
		var idList=[];
		var runningRows=[];
		var rows = dg.datagrid("getRows");
		for(var i = 0 ;i<rows.length ;i++){
			if(rows[i].taskState == "1" || rows[i].taskState == "3"){
				idList.push(rows[i].taskId);
				runningRows.push(rows[i]);
			}
		}
		if(idList.length > 0){
			$.ajax({
				type : 'post',
				url : '${ctx}/task/findRunningTask',
				data:JSON.stringify(idList),
				contentType:'application/json;charset=utf-8',
				success : function(data) {
					if(data){
						for(var i = 0 ;i<runningRows.length ;i++){
							for(var j=0 ;j<data.length;j++){
								if(runningRows[i].taskId == data[j].taskId){
									var runningIndex =dg.datagrid('getRowIndex',runningRows[i]);
									dg.datagrid("updateRow",{
										index: runningIndex,
										row: {
											finishCount: data[j].finishCount,
											successCount: data[j].successCount,
											failCount:data[j].failCount,
											taskState:data[j].taskState
										}
									});
									break;
								}
							}
						}
					}
				}
		 	});
		}
	},1000);
});
function onCheck(index){
	checkButtonDisabled();
}
function onUncheck(index,rowData){
	checkButtonDisabled();
}
function checkButtonDisabled(){
	var checkedRows = dg.datagrid("getChecked");
	var isDisabled = false;
	for(var i = 0 ; i<checkedRows.length ; i++)
	{
		if(checkedRows[i].taskState == "-1"){
			isDisabled = true;
			break;
		}
	}
	if(isDisabled){
		$('#startButton').linkbutton('disable');    //禁用按钮
		$('#stopButton').linkbutton('disable');    //禁用按钮
	}else{
		$('#startButton').linkbutton('enable');    //启用按钮
		$('#stopButton').linkbutton('enable');    //启用按钮
	}
}
//创建任务;
function create(taskType)
{
	
	if("db" == taskType && checkModuleAuth(taskType))
	{
		var dbTab ={
	        title: dbTabTitle, href: "db/add?taskType="+taskType, iniframe: true, closable: true, refreshable: true, iconCls: "icon-hamburg-billing", repeatable: true, selected: true
	    };
		parent.mainTabs.tabs("add",dbTab);
	}else if("web" == taskType && checkModuleAuth(taskType))
	{
		var webTab ={
	        title: webTabTitle, href: "webController/addPage?taskType="+taskType, iniframe: true, closable: true, refreshable: true, iconCls: "icon-hamburg-world", repeatable: true, selected: true
	    };
		parent.mainTabs.tabs("add",webTab);
	}else if("ftp" == taskType && checkModuleAuth(taskType)){
		var ftpTab ={
		        title: ftpTabTitle, href: "ftpController/addPage?taskType="+taskType, iniframe: true, closable: true, refreshable: true, iconCls: "icon-hamburg-archives", repeatable: true, selected: true
		};
		parent.mainTabs.tabs("add",ftpTab);
	}else if("file" == taskType && checkModuleAuth(taskType)){
		var fileTab ={
		        title: fileTabTitle, href: "fileController/addPage?taskType="+taskType, iniframe: true, closable: true, refreshable: true, iconCls: "icon-hamburg-folder", repeatable: true, selected: true
		};
		parent.mainTabs.tabs("add",fileTab);
	}
	//window.parent.frames[0].frameElement.src = '${ctx}/db/add';
	/* parent.mainTabs.tabs('update',{
		tab:currTab,
	    options:{
	    	title:"New Title",
	    	iconCls:"icon-hamburg-billing",
	        href: '${ctx}/db/add',
	        closable:true
	    }
	});
	currTab.panel('refresh'); */
}
//删除;
function del()
{
	var rows = dg.datagrid('getSelections');
	if(rowIsNull(rows)) return;
	for (var i = 0; i < rows.length; i++) {
     	if(rows[i].taskState == "1"){
     		parent.$.messager.alert("请先停止正在运行中的任务！");
     		return;
     	}
    }
	parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
		if (data){
	        var _list = {};
	        for (var i = 0; i < rows.length; i++) {
	        	_list["taskIds[" + i + "]"] = rows[i].taskId;
	        }
	        parent.$.messager.progress({
    		    title : '提示',
    		    text : '数据处理中，请稍后....'
	    	});
	        $.ajax({
	            url: '${ctx}/task/delete',
	            data: _list,
	            type: "POST",
	            dataType:'json',
	            success: function (data) {
	            	if(data.success)
	            	{
		             	if(successTip('success')){
		             		
		                }
	            	}
	            	else
	            	{
	            		if(successTip(data.msg)){
		                	
		                }
	            	}
	            	parent.$.messager.progress('close');
                	dg.datagrid('clearSelections');
                	dg.datagrid('reload');
	            },error: function(e)
	            {
	            	parent.$.messager.progress('close');
	            	parent.$.messager.alert("系统错误:"+e);
	            }
	        });
		 } 
	});
}
//修改
function edit()
{
	 var checkedRows = dg.datagrid('getChecked');
// 	 if(rowIsNull(checkedRows)) return;
	if(checkedRows==null || checkedRows=="")
	{
		$.messager.alert('提示','请选择需要修改的任务！','info');
		return
	}
	if(checkedRows!="" && checkedRows.length!=1)
	{
		$.messager.alert('提示','请选择其中一条记录进行修改！','info');
		return
	}
	 var row = checkedRows[0];
	 gotoEdit(row.taskType,row.taskId);
	 /* */
}
function onLoadSuccess(data){
	var rows = data.rows;
	var checkedRows = dg.datagrid("getChecked");
   	var isDisabled = false;
   	for(var i = 0 ; i<checkedRows.length ; i++)
   	{
   		var checkedTaskId = checkedRows[i].taskId;
   		for(var j = 0 ;j<rows.length;j++)
   		{
   			if(checkedTaskId == rows[j].taskId){
   				if(rows[j].taskState == "-1"){
   					isDisabled = true;
   					break;
   				}
   			}
   		}
   	}
   	if(isDisabled){
   		$('#startButton').linkbutton('disable');    //禁用按钮
   		$('#stopButton').linkbutton('disable');    //禁用按钮
   	}else{
   		$('#startButton').linkbutton('enable');    //启用按钮
   		$('#stopButton').linkbutton('enable');    //启用按钮
   	}
}
function gotoEdit(type,taskId)
{	 
	//var currTab = parent.mainTabs.tabs('getSelected');
	if(type == "db"){
		 var dbTab ={
			title: dbTabTitle, href: "${ctx}/db/edit?taskId="+taskId, iniframe: true, closable: true, refreshable: true, iconCls: "icon-hamburg-billing", repeatable: true, selected: true
		 };
		 parent.mainTabs.tabs("add",dbTab);
		// window.location.href="${ctx}/db/edit?taskId="+taskId;
	 }
	else if(type == "web"){
		 var dbTab ={
			title: webTabTitle, href: "${ctx}/webController/editPage?taskId="+taskId, iniframe: true, closable: true, refreshable: true, iconCls: "icon-hamburg-world", repeatable: true, selected: true
		 };
		 parent.mainTabs.tabs("add",dbTab);
		// window.location.href="${ctx}/db/edit?taskId="+taskId;
	 }
	else if(type == "ftp"){
		 var dbTab ={
			title: ftpTabTitle, href: "${ctx}/ftpController/editPage?taskId="+taskId, iniframe: true, closable: true, refreshable: true, iconCls: "icon-hamburg-archives", repeatable: true, selected: true
		 };
		 parent.mainTabs.tabs("add",dbTab);
		// window.location.href="${ctx}/db/edit?taskId="+taskId;
	 }
	else if(type == "file"){
		 var dbTab ={
			title: fileTabTitle, href: "${ctx}/fileController/editPage?taskId="+taskId, iniframe: true, closable: true, refreshable: true, iconCls: "icon-hamburg-folder", repeatable: true, selected: true
		 };
		 parent.mainTabs.tabs("add",dbTab);
		// window.location.href="${ctx}/db/edit?taskId="+taskId;
	 }
}
function resetRowStatus(checkedRows){
	for(var i = 0 ; i<checkedRows.length ; i++)
	{
		var checkedRowIndex =dg.datagrid('getRowIndex',checkedRows[i]);
		dg.datagrid("checkRow",checkedRowIndex);
	}
}
function on_off()
{
	var checkedRows = dg.datagrid('getChecked');
	if(rowIsNull(checkedRows)) return;
	var _list = {};
    for (var i = 0; i < checkedRows.length; i++) {
    	_list["tasks[" + i + "].taskId"] = checkedRows[i].taskId;
    	_list["tasks[" + i + "].taskState"] = checkedRows[i].taskState;
    }
    parent.$.messager.progress({
	    title : '提示',
	    text : '任务处理中，请稍后....'
	});
    $.ajax({
        url: '${ctx}/task/onOff',
        data: _list,
        type: "POST",
        success: function (data) {
         	if(successTip(data)){
            }
         	parent.$.messager.progress('close');
         	var checkedRows = dg.datagrid("getChecked");
         	dg.datagrid('reload');
         	resetRowStatus(checkedRows);
        }
    });
}
//启动
function start()
{
	var rows = dg.datagrid('getSelections');
	if(rowIsNull(rows)) return;
	var _list = {};
    for (var i = 0; i < rows.length; i++) {
    	_list["taskIds[" + i + "]"] = rows[i].taskId;
    }
    parent.$.messager.progress({
	    title : '提示',
	    text : '任务启动中，请稍后....'
	});
    $.ajax({
        url: '${ctx}/task/start',
        data: _list,
        type: "POST",
        /* timeout:20000, */
        success: function (data) {
         	if(successTip(data)){
            }
         	parent.$.messager.progress('close');
         	dg.datagrid('reload');
        },error:function (XMLHttpRequest, textStatus, errorThrown) {
        	parent.$.messager.progress('close');
        	if(textStatus == "timeout"){
        		parent.$.messager.alert("启动超时!");
        	}else{
        		parent.$.messager.alert("启动错误:"+e);
        	}
        	dg.datagrid('reload');
        }
    });
}
function stop()
{
	var rows = dg.datagrid('getSelections');
	if(rowIsNull(rows)) return;
	var _list = {};
    for (var i = 0; i < rows.length; i++) {
    	_list["taskIds[" + i + "]"] = rows[i].taskId;
    }
    parent.$.messager.progress({
	    title : '提示',
	    text : '任务停止中，请稍后....'
	});
    $.ajax({
        url: '${ctx}/task/stop',
        data: _list,
        type: "POST",
        /* timeout:20000, */
        success: function (data) {
         	if(successTip(data)){
            	parent.$.messager.progress('close');
            	//$('#tasklist').datagrid('clearSelections');
            	dg.datagrid('reload');
            }
        },error:function (XMLHttpRequest, textStatus, errorThrown) {
        	parent.$.messager.progress('close');
        	if(textStatus == "timeout"){
        		parent.$.messager.alert("停止超时!");
        	}else{
        		parent.$.messager.alert("系统错误:"+e);
        	}
        	dg.datagrid('reload');
        }
    });
}

//创建查询对象并查询
function cx(){
	var obj=$("#searchFrom").serializeObject();
	dg.datagrid('load',obj); 
}
//执行导出;
function doExport(){
	var checkedRows = dg.datagrid('getChecked');
	if(rowIsNull(checkedRows)) return;
	var idList=[];
	for(var i = 0 ;i<checkedRows.length ;i++){
		idList.push(checkedRows[i].taskId);
	}
	/* parent.$.messager.progress({
		    title : '提示',
		    text : '任务导出中，请稍后....'
	}); */
	window.location="${ctx}/task/doDataExport?ids="+JSON.stringify(idList).replace("[","").replace("]","");
	//parent.$.messager.progress('close');
	/* $.ajax({
			type : 'post',
			url : '${ctx}/task/doDataExport',
			data:JSON.stringify(idList),
			contentType:'application/json;charset=utf-8',
			success : function(data) {
				var status = data.status;
				if(status == 1){
					//window.location.href="\""+filepath+"\"";
					successTip("success");
				}
	            parent.$.messager.progress('close');
			},error: function(e)
	        {
	        	parent.$.messager.progress('close');
	        	parent.$.messager.alert("系统错误:"+e);
	        }
	 }); */
}
//执行导入;
function doImport(){
	/* var p = parent.infcn.dialog({
		title : '添加导入信息',
		modal : true,
		maximizable : true,
		content : '<iframe id="userFrame" src="${pageContext.request.contextPath}/admin/resourceAction!resourceImport.action" frameborder="0" style="border:0;width:100%;height:99.4%;"></iframe>',
		width : 400,
		height : 200,
		buttons : [ {
			text : '添加',
			handler : function() {
				var f = p.contents().find('#userFrame')[0];
				f.contentWindow.submitForm(p, datagrid, parent);
				
				datagrid.datagrid('clearSelections');
				datagrid.datagrid('unselectAll');
			}
		} ],
		onLoad : function() {
			parent.$.messager.progress('close');
		}
	}); */
	d=$("#dlg").dialog({   
	    title: '添加导入信息',    
	    width: 400,
	    height: 200,    
	    href:'${ctx}/task/forDataImport',
	    modal:true,
	    buttons:[{
			text:'导入',
			handler:function(){
				submitForm(d,dg);
				/* var f = p.contents().find('#userFrame')[0];
				f.contentWindow.submitForm(p, datagrid, parent);
				
				datagrid.datagrid('clearSelections');
				datagrid.datagrid('unselectAll'); */
// 				$.messager.progress({
// 					title : '提示',
// 					text : '数据处理中，请稍后....'
// 				});
			}
		},{
			text:'取消',
			handler:function(){
				d.panel('close');
			}
		}]/* ,
		onLoad : function() {
			parent.$.messager.progress('close');
		} */
	});
	 /* $.ajax({
			type : 'post',
			url : '${pageContext.request.contextPath}/admin/dataExportAction!doDataImport.action?key='+key,
			dataType : 'JSON',
			success : function(data) {
				 $('#buttonImport').attr('onclick',"dataImport()");
				 $('#buttonImport').bind('onclick');
				 $.messager.progress("bar").progressbar('setValue',100);
				 clearInterval(rocessPercent);
				 var percentBai=setTimeout(function() {
					 $.messager.progress('close');
					 clearTimeout(percentBai);
					 parent.sy.messagerShow({
							msg : data.msg+"总共导入条数:"+totalNum+"条",
							title : '提示'
					 });
					 totalNum=0;
				 },1000);
			}
	 }); */
}

function initMenu()
{
	var task_type_set = $("#task_type_set");
	$("#task_type_set div[name='task_type']").hide();
	var url='${ctx}/task/getTaskTypeList';
	$.ajax({
		type:'post',
		url:url,
		dataType:'json',
		success:function(data){
			$.each(data,function(index,value){
				$("#task_type_set div[name='task_type'][type='"+value+"']").show();
		 	});
		},
		error:function(){
			alert("连接后台出错！");
		}
	});
}

function checkModuleAuth(taskType)
{
	var flag = false;
	var url="${ctx}/task/checkModuleAuth";
	$.ajax({
		type:'post',
		url:url,
		dataType:'json',
		data:{taskType:taskType},
		async:false,
		success:function(data){
			if(data.success)
			{
				flag = true;
			}
			else
			{
				$.messager.alert('提示',data.msg,'info');
			}
		},
		error:function(){
			alert("连接后台出错！");
		}
	});
	return flag;
}
// 查看控制台日志
function showConsoleLog(){
	
	parent.mainTabs.tabs("close","查看控制台");
	var rows = dg.datagrid('getSelections');
	if (rows.length == 1) {
		d = $("#dlg").dialog({
		    title: '【'+rows[0].taskName+'】控制台',    
		    width: 1000,
		    height: 750,    
		    href:'${ctx}/task/gotoConsolePage?taskId=' + rows[0].taskId +'&taskState=' + rows[0].taskState,
		    modal:true,
		    buttons:[{
				text:'关闭',
				handler:function(){
					clearInterval(setID);
					d.dialog('close');
				}
			}],
			onClose:function()
			{
				clearInterval(setID);
			}
		}); 
	} else if (rows.length > 1) {
		$.messager.alert('警告', '同一时间只能查看一个任务！', 'error');
	} else {
		$.messager.alert('警告', '请选择要查看任务！', 'error');
	}
	
}

/**
 * 复制任务
 */
function copyTask()
{
	var checkedRows = dg.datagrid('getChecked');
	if(rowIsNull(checkedRows)) return;
	var ids=[];
	if (checkedRows.length > 0) {
		$.messager.confirm('确认', '您确定要对所选任务进行复制吗？', function(con) {
			if (con) {
				parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
				for(var i = 0 ;i<checkedRows.length ;i++){
					ids.push(checkedRows[i].taskId);
				}
				$.getJSON('${pageContext.request.contextPath}/task/copyTask', {
					ids : ids.join(',')
				}, function(result) {
					if (result.result) {
						dg.datagrid('load');
						dg.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
					}
					if (result.success) {
						parent.$.messager.alert('成功', replace(result.msg), 'info');
						dg.datagrid('reload');
						dg.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
					} else {
						parent.$.messager.alert('警告', replace(result.msg), 'warning');
					}
					parent.$.messager.progress('close');
				});
			}
		});
	} else {
		$.messager.alert('警告', '请选择需要复制的任务', 'warning');
	}
}

function replace(strings){
	 
	 strings=strings.replaceAll("@","<br/>");
	 return strings;
	 }
</script>
</body>
</html>