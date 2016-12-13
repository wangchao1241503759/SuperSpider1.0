<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>WEB采集配置</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link href="${ctx}/static/css/table.css" type="text/css" rel="stylesheet" />
</head>

<body style="font-family: '微软雅黑'" onload="loadcompleted()">
<div id="pagelayout" class="easyui-layout" style="overflow: none;border:none;/* max-height:495px; */" data-options="fit:true">
	<div id="toolsbar" style="height:30px;border-top:0px;border-left:0px;border-right:0px" data-options="region:'north'">
		<a href="javascript:void(0)" class="easyui-linkbutton"	iconCls="icon-hamburg-login" plain="true" onclick="allcollapse(false);">全部展开</a> 
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-logout" plain="true" onclick="allcollapse(true)">全部收起</a> 
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true"	onclick="saveWebconfig()">保存</a>
	</div>
	<div class="easyui-panel" id="mainlayout" style="overflow: auto;border:none;/* max-heighta:495px; */" data-options="region:'center'">
		<form id="configForm" action="" method="post">
			<div id="baseinfo" class="easyui-panel" title="任务基本信息" style="padding: 5px; border:none" data-options="collapsible:true">
				<jsp:include page="/WEB-INF/views/superspider/task/part_task_add.jsp"/>
			</div>
			<div id="source" class="easyui-panel" title="数据源设置" style="padding: 5px;border:none" data-options="collapsible:true">
				<jsp:include page="/WEB-INF/views/superspider/web/web_source.jsp"/>
			</div>
			<div id="target" class="easyui-panel" title="数据输出设置" style="padding: 5px;border:none" data-options="collapsible:true">
				<jsp:include page="/WEB-INF/views/superspider/task/task_output.jsp"/>
			</div>
			<div id="regex" class="easyui-panel" title="采集规则设置" style="height: 600px;" data-options="maximizable:true,collapsible:true">
				<jsp:include page="/WEB-INF/views/superspider/web/web_rule_add.jsp"/>
			</div>
		</form>
	</div>
</div>
<script>

	function saveWebconfig()
	{
		getTabIndex();
		var webTypeRuleJson =$.toJSON(webTypeRuleList);
		var webTypeRule =$("#webTypeRuleJson");
		webTypeRule.val(webTypeRuleJson);
		
		var paramJson =$.toJSON(paramObjectList);
		var paramObjectListJson = $("#paramObjectListJson");
		paramObjectListJson.val(paramJson);
		
		getFieldMappingList();
		var fieldMappingJson = $.toJSON(fieldMappingJsonList);
		$("#fieldMappingJson").val(fieldMappingJson);
		
		var count=0;
		if(fieldMappingJsonList.length>0)
		{
			for(var i=0;i<fieldMappingJsonList.length;i++)
			{
				if(fieldMappingJsonList[i].fieldStatus=="1")
				{
					count++;
					break;
				}
			}
		}
		
		if(count == 0){
			parent.$.messager.alert("至少选择一个字段映射关系!");
			return;
		}
		
		$.messager.progress({
			title : '提示',
			text : '数据处理中，请稍后....'
		});
		var isValid = $("#configForm").form('validate');
		if(isValid)
		{
			isValid = checkForm();
		}
		if (!isValid) {
			$.messager.progress('close');
			return;
		}
		
		var url = '${pageContext.request.contextPath}/webController/add';
		
		$.ajax({
			type:'post',
			url:url,
			data:$("#configForm").serialize(),
			success:function(result)
			{
				
				if (result.success) {
			    	parent.$.messager.progress('close');
				  	$.messager.alert('成功', result.msg, 'info', function() {
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
			    		parent.mainTabs.tabs("close", webTabTitle);
					});
				} else {
					parent.$.messager.alert('错误', result.msg, 'error');
					$.messager.progress('close');
				}
			},
			error:function (XMLHttpRequest, textStatus, errorThrown) {
			    // 通常 textStatus 和 errorThrown 之中
			    // 只有一个会包含信息
			    this; // 调用本次AJAX请求时传递的options参数
			}
		});
	}

	//检验数据正确性
function checkForm()
{
	var flag = false;
	if(webTypeRuleList!="" && webTypeRuleList.length>0)
	{
		var flagMatch = true;
		for(var i=0;i<webTypeRuleList.length;i++)
		{
			if(typeof(webTypeRuleList[i].webTypeMatchLabelList) == "undefined" || webTypeRuleList[i].webTypeMatchLabelList.length == 0)
			{
				flagMatch = false;
				break;
			}
			
		}
		if(flagMatch)
		{
			flag = true;
		}
		else
		{
			$.messager.alert('提示','页面匹配列表不能为空！','info');
			flag = false;
			return flag;
		}
	}
	else
	{
		$.messager.alert('提示','页面规则列表不能为空！','info');
		flag = false;
		return flag;
	}
	
	if(paramObjectList!="" && paramObjectList.length>0)
	{
		if(webTypeRuleList.length>paramObjectList.length)
		{
			$.messager.alert('提示','参数设置列表不能为空！','info');
			flag = false;
		}
		var flagFieldExtract = true;
		for(var j=0;j<paramObjectList.length;j++)
		{
			if(typeof(paramObjectList[j].fieldExtractList)== "undefined" || paramObjectList[j].fieldExtractList.length==0)
			{
				flagFieldExtract = false;
				break;
			}
			
		}
		if(flagFieldExtract)
		{
			flag = true;
		}
		else
		{
			$.messager.alert('提示','参数设置列表不能为空！','info');
			flag = false;
			return flag;
		}
	}
	else
	{
		$.messager.alert('提示','参数设置列表不能为空！','info');
		flag = false;
		return flag;
	}
	
	return flag;
}

var taskTabTitle = '任务管理';
var dbTabTitle = '数据库采集配置';
var webTabTitle = 'WEB采集配置';
var ftpTabTitle = 'FTP采集配置';
var fileTabTitle = '文件采集配置';

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
			$("#dg_type2").datagrid('resize',{width:w-5});
			
			var mainlayout_h = $("#mainlayout").height();
			var layout_split_north_h = $("#layout_split_north").height();

			$("#browser").height(mainlayout_h-layout_split_north_h-70);

			var toolsbar_h=$("#toolsbar").height();
			$("#regex").css("height",mainlayout_h-toolsbar_h);
		}
	});
}


$(function(){
	$("#regex").panel({
		onMaximize: function () 
		{
			//alert("Maximized");
			$("#baseinfo").panel('close');
			$("#source").panel('close');
			$("#target").panel('close');
			var w=parseInt(document.getElementById('mainlayout').style.width)-30;
			$("#dg_type2").datagrid('resize',{width:w});
			
			var mainlayout_h = $("#mainlayout").height();
			var layout_split_north_h = $("#layout_split_north").height();
			$("#browser").height(mainlayout_h-layout_split_north_h-70);
			var toolsbar_h=$("#toolsbar").height();
			$("#regex").css("height",mainlayout_h-toolsbar_h+"px");
			
	 	},
	 	onRestore: function () 
	 	{
	 		$("#baseinfo").panel('open');
			$("#source").panel('open');
			$("#target").panel('open');
			var w=parseInt(document.getElementById('mainlayout').style.width)-30;
			$("#dg_type2").datagrid('resize',{width:w});
			
			
			var mainlayout_h = $("#mainlayout").height();
			var toolsbar_h=$("#toolsbar").height();
			$("#regex").panel('resize',{height:mainlayout_h-toolsbar_h});
			
			var regex_jh = $("#regex").height();
			var layout_split_north_h = $("#layout_split_north").height();
			$("#browser").css("height",(regex_jh-layout_split_north_h-42));
			document.getElementById("mainlayout").scrollTop = 1200;
		}
	
	});	
	
	
	$("#layout_split_center").panel({
		onResize:function(width, height)
		{
			var regex_jh = $("#regex").height();
			var north_h = $("#layout_split_north").height();
			$("#browser").css("height",(regex_jh-north_h-45));
		}
	});
});
</script>
</body>
</html>