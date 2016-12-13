<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>web采集配置</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
</head>

<body style="font-family: '微软雅黑'" onload="loadcompleted()">
	<div>
		<a href="javascript:void(0)" class="easyui-linkbutton"	iconCls="icon-hamburg-login" plain="true" onclick="allcollapse(false);">全部展开</a> 
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-logout" plain="true" onclick="allcollapse(true)">全部收起</a> 
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true"	onclick="save()">保存</a>
	</div>
	<div id="mainlayout" class="easyui-panel" style="overflow: auto;border:none" data-options="fit:true">
		<div id="baseinfo" class="easyui-panel" title="任务基本信息" style="height: 175px; border:none" data-options="collapsible:true">
			<script>
				$(function() {
					panelcollapse('#baseinfo', false);
				});
			</script>
				<iframe src="${ctx}/spider/config/taskinfo" frameborder="0" scrolling="yes" height="100%" width="100%"></iframe>
		</div>
		
		<div id="source" class="easyui-panel" title="数据源设置" style="height: 370px;border:none" data-options="collapsible:true"s>
			<script>
				$(function() {
					panelcollapse('#source', false);
				});
			</script>
			<iframe src="${ctx}/spider/config/http/source" frameborder="0" scrolling="yes" height="100%" width="100%"></iframe>
		</div>
		<div id="target" class="easyui-panel" title="数据输出设置" style="height: 170px; border:none" data-options="collapsible:true">
			<script>
				$(function() {
					panelcollapse('#target', false);
				});
			</script>
			<iframe src="${ctx}/spider/config/taskoutput" frameborder="0" scrolling="yes" height="100%" width="100%"></iframe>
		</div>
		<div id="regex" class="easyui-panel" title="采集规则设置"	style="height: 420px;border:none" data-options="collapsible:true">
			<script>
				$(function() {
					panelcollapse('#regex', false);
				});
			</script>
			<iframe src="${ctx}/spider/config/http/regex" frameborder="0"	scrolling="yes" height="100%" width="100%"></iframe>
		</div>
	</div>
	<div id="dlg"></div>
	<script>
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
			//panelcollapse('#baseinfo', false);
		}
		
		$(function() {
			$("#mainlayout").panel({
				onResize: function () {
					var w=parseInt(document.getElementById('mainlayout').style.width)-20;
					$("#baseinfo").panel('resize',{width:w});
					$("#source").panel('resize',{width:w});
					$("#target").panel('resize',{width:w});
					$("#regex").panel('resize',{width:w});
       	 		}});
		});
	</script>
</body>
</html>