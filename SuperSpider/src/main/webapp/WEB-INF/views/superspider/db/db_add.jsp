<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>数据库采集配置</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link href="${ctx}/static/css/table.css" type="text/css" rel="stylesheet" />
</head>

<body style="font-family: '微软雅黑'" onload="loadcompleted()">
<div id="pagelayout" class="easyui-layout" style="overflow: none;border:none;/* max-height:495px; */" data-options="fit:true">
	<div style="height:30px;border-top:0px;border-left:0px;border-right:0px" data-options="region:'north'">
		<a href="javascript:void(0)" class="easyui-linkbutton"	iconCls="icon-hamburg-login" plain="true" onclick="allcollapse(false);">全部展开</a> 
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-logout" plain="true" onclick="allcollapse(true)">全部收起</a> 
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true"	onclick="save()">保存</a>
	</div>
	<div class="easyui-panel" id="mainlayout" style="overflow: auto;border:none;/* max-heighta:495px; */" data-options="region:'center'">
		<form id="configForm" action="${ctx}/db/save" method="post">
			<div id="baseinfo" class="easyui-panel" title="任务基本信息" style="padding: 5px; border:none" data-options="collapsible:true">
				<jsp:include page="/WEB-INF/views/superspider/task/part_task_add.jsp"/>
			</div>
			<div id="source" class="easyui-panel" title="数据源设置" style="padding: 5px;border:none" data-options="collapsible:true">
				<jsp:include page="/WEB-INF/views/superspider/db/db_source.jsp"/>
			</div>
			<div id="target" class="easyui-panel" title="数据输出设置" style="padding: 5px;border:none" data-options="collapsible:true">
				<jsp:include page="/WEB-INF/views/superspider/task/task_output.jsp"/>
			</div>
			<div id="regex" class="easyui-panel" title="采集规则设置" style="height: 400px;" data-options="collapsible:true">
				<jsp:include page="/WEB-INF/views/superspider/db/db_regex.jsp"/>
			</div>
		</form>
	</div>
</div>
<script>
var taskTabTitle = '任务管理';
var dbTabTitle = '数据库采集配置';
var webTabTitle = 'WEB采集配置';
var ftpTabTitle = 'FTP采集配置';
var fileTabTitle = '文件采集配置';
function save()
{
	$("#fieldmapping").datagrid('endEdit',lastIndex);
	var fieldMappingStr = getFieldMappingData();
	if(fieldMappingStr == "[]"){
		parent.$.messager.alert("至少选择一个字段映射关系!");
		return;
	}
	if(!validMssTargetFieldIsNull()){
		parent.$.messager.alert("请选择目标字段!");
		return;
	}
	$("#fieldMappingData").val(fieldMappingStr);
	$("#attributeMappingData").val(getContentMappingData());
	var taskId = $("#taskId").val();
	var url = '${ctx}/db/save';
	var isRunning = false;
	if(taskId){
		$.ajax({
			async:false,
			type:'post',
			url:"${ctx}/task/get?taskId="+taskId,
			success: function(data){
				if(1 == data.taskState){
					isRunning = true;
				}
			}
		});
	}
	if(!isRunning){
		var isValid = $('#configForm').form('validate');
		if(isValid){
			parent.$.messager.progress({
    		    title : '提示',
    		    text : '数据处理中，请稍后....'
	    	});
			//提交表单
			$.ajax({
				type:'post',
				url:url,
				data:$("#configForm").serialize(),
				success:function(data){
			    	if(data.success)
					{
			    		parent.$.messager.progress('close');
			    		parent.$.messager.show({ title : "提示",msg: "操作成功！", position: "bottomRight" });
			    		if(parent.mainTabs.tabs("exists",taskTabTitle)){
			    			parent.mainTabs.tabs("select",taskTabTitle);
			    			index = parent.mainTabs.tabs("getSelectedIndex");
			    			parent.mainTabs.tabs("refresh", index);
			    		}else{
			    			var taskTab ={
			    			  	title: taskTabTitle, href: "task/list", iniframe: true, closable: true, refreshable: true, iconCls: "icon-hamburg-delicious", repeatable: true, selected: true
			    			};
			    			parent.mainTabs.tabs("add",taskTab);
			    		}
			    		parent.mainTabs.tabs("close", dbTabTitle);
			    		//window.location.href='${ctx}/task/list';
					}
			    	else
			    	{
			    		parent.$.messager.alert('提示',data.msg,'info');
			    		parent.$.messager.progress('close');
			    	}
				}
			});
		}
	}else{
		parent.$.messager.alert("任务运行中，请先停止任务！");
	}
}

function validMssTargetFieldIsNull(){
	var topType = $("#topType").combobox("getValue");
	if(topType == "mss")
	{
		var checkedRows = $('#fieldmapping').datagrid('getChecked');
		for(var i = 0 ; i < checkedRows.length ; i++){
			var checkedRow = checkedRows[i];
			
			var targetFieldName = checkedRow.targetFieldName;
		 	if(targetFieldName == "")
		 		return false;
			
		}
	}
	return true;
}

function getFieldMappingData(){
	var _list = "[";
	var startIndex = 0;
	var rows = $('#fieldmapping').datagrid('getSelections');
	//var deleted = $('#fieldmapping').datagrid('getChanges', "deleted");
	var outType = $("#topType").combobox("getValue");
	if (rows.length) {
		for (var i = 0; i < rows.length; i++) {
		    var row = rows[i];
		    var fieldID = typeof(row.fieldID) == "undefined" ? "":row.fieldID;
			if(i > 0)
			_list+=",";
			_list+="{\"fieldId\":\""+fieldID+"\","; //这里list要和后台的参数名List<FieldMapping> fieldList一样
			_list+="\"fieldName\":\""+row.fieldName+"\",";
			_list+="\"fieldType\":\""+row.fieldType+"\",";
			_list+="\"fieldExp\":\""+row.fieldExp+"\",";
			_list+="\"filedSource\":\""+row.filedSource+"\",";
			_list+="\"targetFieldName\":\""+row.targetFieldName+"\",";
			_list+="\"targetFieldType\":\""+row.targetFieldType+"\",";
			_list+="\"targetType\":\""+outType+"\",";
			_list+="\"fieldOrdinalPosition\":\""+(startIndex+1)+"\",";
			_list+="\"fieldStatus\":\"1\"}";
		    startIndex++;
		}
	//alert(startIndex+"新增");
    }
	_list+="]";
	return _list;
}
function getContentMappingData(){
	var _list = "[";
	var startIndex = 0;
	var rows = $('#fileattr').datagrid('getSelections');
	var fileFieldType = $("#fileFieldType").combobox("getValue");
	var extensionField = $("#extensionField").combobox("getValue");
	var sourceFieldName = $("#sourceFieldName").combobox("getValue");
	if (rows.length) {
		for (var i = 0; i < rows.length; i++) {
		    var row = rows[i];
		    var fileAttributeId = typeof(row.fileAttributeId) == "undefined" ? "":row.fileAttributeId;
		    var targetFieldName = typeof(row.targetFieldName) == "undefined" ? "":row.targetFieldName;
		    var targetFieldType = typeof(row.targetFieldType) == "undefined" ? "":row.targetFieldType;
		    if(i > 0)
			_list+=",";
			_list+="{\"fileAttributeId\":\""+fileAttributeId+"\","; //这里list要和后台的参数名List<FieldMapping> fieldList一样
			_list+="\"fileFieldType\":\""+fileFieldType+"\",";
			_list+="\"extensionField\":\""+extensionField+"\",";
			_list+="\"sourceFieldName\":\""+sourceFieldName+"\",";
			_list+="\"targetFieldName\":\""+targetFieldName+"\",";
			_list+="\"targetFieldType\":\""+targetFieldType+"\"}";
		    startIndex++;
		}
	//alert(startIndex+"新增");
    }
	_list+="]";
	return _list;
}
//加载任务计划页
function loadcron() {
	$('#plan').load("${ctx}/spider/config/plan");
}

function panelcollapse(panelid, flag) {
	if (flag) {
		$(panelid).panel('collapse');
	} else {
		$(panelid).panel('expand');
	}
}

function allcollapse(flag) {
	panelcollapse('#baseinfo', flag);
	panelcollapse('#source', flag);
	panelcollapse('#target', flag);
	panelcollapse('#regex', flag);
}

function loadcompleted(panelid, flag) {
	allcollapse(false);
	/* var currTab = parent.mainTabs.tabs('getSelected');
	alert(currTab.panel('options').title);
	parent.mainTabs.tabs('setIconCls',{tab:currTab,iconCls:"icon-hamburg-billing"});
	parent.mainTabs.tabs('setTabTitle',{tab:currTab,title:"数据库采集配置"}); */
	$("#mainlayout").panel({
		onResize: function () {
			var w=parseInt(document.getElementById('mainlayout').style.width)-20;
			$("#baseinfo").panel('resize',{width:w});
			$("#source").panel('resize',{width:w});
			$("#target").panel('resize',{width:w});
			$("#regex").panel('resize',{width:w});
		}
	});
}
</script>
</body>
</html>