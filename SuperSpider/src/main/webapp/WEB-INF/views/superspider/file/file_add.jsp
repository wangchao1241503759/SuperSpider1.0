<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>FILE采集配置</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link href="${ctx}/static/css/table.css" type="text/css" rel="stylesheet" />
</head>

<body style="font-family: '微软雅黑'" onload="loadcompleted()">
<div id="pagelayout" class="easyui-layout" style="overflow: none;border:none;/* max-height:495px; */" data-options="fit:true">
	<div style="height:30px;border-top:0px;border-left:0px;border-right:0px" data-options="region:'north'">
		<a href="javascript:void(0)" class="easyui-linkbutton"	iconCls="icon-hamburg-login" plain="true" onclick="allcollapse(false);">全部展开</a> 
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-logout" plain="true" onclick="allcollapse(true)">全部收起</a> 
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true"	onclick="saveFileconfig()">保存</a>
	</div>
	<div class="easyui-panel" id="mainlayout" style="overflow: auto;border:none;/* max-heighta:495px; */" data-options="region:'center'">
		<form id="configForm" action="" method="post">
			<div id="baseinfo" class="easyui-panel" title="任务基本信息" style="padding: 5px; border:none" data-options="collapsible:true">
				<jsp:include page="/WEB-INF/views/superspider/task/part_task_add.jsp"/>
			</div>
			<div id="source" class="easyui-panel" title="数据源设置" style="padding: 5px;border:none" data-options="collapsible:true">
				<jsp:include page="/WEB-INF/views/superspider/file/file_source.jsp"/>
			</div>
			<div id="target" class="easyui-panel" title="数据输出设置" style="padding: 5px;border:none" data-options="collapsible:true">
				<jsp:include page="/WEB-INF/views/superspider/task/task_output.jsp"/>
			</div>
			<div id="regex" class="easyui-panel" title="采集规则设置" style="height: 600px;" data-options="maximizable:true,collapsible:true">
				<jsp:include page="/WEB-INF/views/superspider/file/file_regex.jsp"/>
			</div>
		</form>
	</div>
</div>
<script>

	function saveFileconfig()
	{
		
		var rows = $('#fileattr').datagrid('getSelections');
		var fileattrJson =$.toJSON(rows);
		var fileattr =$("#fileattrJson");
		fileattr.val(fileattrJson);

		var isValid = $("#configForm").form('validate');
		if(isValid)
		{
			isValid = checkForm();
		}
		if (!isValid) {
			parent.$.messager.progress('close');
			return;
		}
		parent.$.messager.progress({
			title : '提示',
			text : '数据处理中，请稍后....'
		});
		var url = '${pageContext.request.contextPath}/fileController/add';
		
		$.ajax({
			type:'post',
			url:url,
			data:$("#configForm").serialize(),
			success:function(result)
			{
				
				//result = $.parseJSON(result);
				if (result.success) {
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
		    		parent.mainTabs.tabs("close", fileTabTitle);
				} else {
					parent.$.messager.alert('错误', result.msg, 'error');
					parent.$.messager.progress('close');
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
		var rows = $('#fileattr').datagrid('getSelections');
		if(rows.length>0)
		{
			flag = true;
		}
		else
		{
			$.messager.alert('提示','需要选择文件字段内容属性！','info');
			flag = false;
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
// 			getHeight();
		}
	});
}

$(function(){
	$("#regex").panel({
		onMaximize: function () {
			//alert("Maximized");
// 			getHeight()
			$("#baseinfo").panel('close');
			$("#source").panel('close');
			$("#target").panel('close');
// 			getHeight()
			$("#regex").css("height","550px");
			//$("#browser").css("height","700px");
			var w=parseInt(document.getElementById('mainlayout').style.width)-30;
			$("#dg_type2").datagrid('resize',{width:w-5});
	 		},
	 		onRestore: function () {
			//alert("Maximized");
			$("#baseinfo").panel('open');
			$("#source").panel('open');
			$("#target").panel('open');
			$("#regex").css("height","600px");
			$("#browser").css("height","300px");
			var w=parseInt(document.getElementById('mainlayout').style.width)-30;
			$("#dg_type2").datagrid('resize',{width:w});
// 			getHeight()
			}
	
	});			
});

function getHeight()
{
	var h = $("#mainlayout").height();
	var baseinfo_h=$("#baseinfo").height();
	var source_h=$("#source").height();
	var target_h=$("#target").height();
	var regex_h=$("#regex").height();
	var browser_h = $("#browser").height();
	alert(h+"baseinfo_h="+baseinfo_h+"source_h="+source_h+"target_h="+target_h+"regex_h="+regex_h+"browser_h="+browser_h)
}
</script>
</body>
</html>