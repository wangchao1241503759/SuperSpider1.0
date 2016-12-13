<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>计划管理</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
</head>
<body>
	<div id="tb" style="padding:5px;height:auto">
    	<form id="searchFrom" action="">
			<input type="text" name="filter_taskname" class="easyui-validatebox" data-options="width:150,prompt: '计划名称'" />
			<input type="text" name="filter_GTD_createDate" class="easyui-my97"	datefmt="yyyy-MM-dd" data-options="width:150,prompt: '开始日期'" /> - 
			<input type="text" name="filter_LTD_createDate" class="easyui-my97"	datefmt="yyyy-MM-dd" data-options="width:150,prompt: '结束日期'" /> 
			<input type="text" name="filter_creator" class="easyui-validatebox"	data-options="width:150,prompt: '创建人'" />
			<input type="text" name="filter_task_reference" class="easyui-validatebox" data-options="width:150,prompt: '关联任务'" />
			<script>
				$(function() {
					$("#filtertype").combobox('setValue', '');
					$("#filterstate").combobox('setValue', '');
				});
			</script>
			
			<span class="toolbar-item dialog-tool-separator"></span> 
			<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search"	plain="true" onclick="">查询</a>
		
			<div>
    			<a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-add" onclick="add();">添加</a>
    			<span class="toolbar-item dialog-tool-separator"></span>
        		<a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-remove" onclick="del()">删除</a>
        		<span class="toolbar-item dialog-tool-separator"></span>
        		<a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-edit" onclick="upd()">修改</a>
    		</div>
		</form>
	</div>
	<table id="planlist" class="easyui-datagrid">
		<thead>
			<tr>
				<th data-options="field:'ck',checkbox:true"></th>
				<th data-options="field:'planName',width:120">计划名称</th>
				<th data-options="field:'createTime',width:100">创建时间</th>
				<th data-options="field:'creator',width:100">创建人</th>
				<th data-options="field:'planDescription',width:300">计划描述</th>
				<th data-options="field:'taskReference',width:320">关联任务</th>
			</tr>
		</thead>
	</table>
	<div id="dlg"></div>
	
	<script>
		function getData() {
			var rows = [];
			rows.push({
				planName:'周末凌晨采集',
				createTime:'2015-12-11',
				creator:'张有志',
				planDescription:'从2015-12-11开始，每周六、周日24：00执行',
				taskReference:'发改委内网数据采集'
			});
			rows.push({
				planName:'每天凌晨采集',
				createTime:'2015-12-11',
				creator:'张有志',
				planDescription:'从2015-12-11开始，每天24：00执行',
				taskReference:'青岛政务数据采集，'
			});
			rows.push({
				planName:'每2周的周末凌晨采集',
				createTime:'2015-12-11',
				creator:'张有志',
				planDescription:'从2015-12-11开始，每2周的周六、周日24：00执行',
				taskReference:'华能集团数据采集，北京电力医院感染数据采集'
			});
			rows.push({
				planName:'每2小时采集一次',
				createTime:'2015-12-11',
				creator:'张有志',
				planDescription:'从2015-12-11开始，每2小时执行一次',
				taskReference:'国家图书馆数据采集，北京电力医院门诊数据采集'
			});
			rows.push({
				planName:'每3天凌晨采集',
				createTime:'2015-12-11',
				creator:'张有志',
				planDescription:'从2015-12-11开始，每3天24：00执行',
				taskReference:'北京电力医院体检数据采集'
			});
			
			return rows;
		}

		$(function() {
			$('#planlist').datagrid({
				fit : true,
				fitColumns : true,
				border : false,
				striped:true,
				pagination:true,
				rownumbers:true,
				pageNumber:1,
				pageSize : 10,
				pageList : [ 10, 20, 30, 40, 50 ],
				singleSelect:false,
				toolbar: '#tb'
			});
			$('#planlist').datagrid('loadData', getData());
		});
		
		//弹窗新建计划
		function add() {
			var handle=$("#dlg");
			d = handle.dialog({
				title : '新建计划',
				width : 510,
				height : 330,
				closed : false,
				cache : false,
				maximizable : false,
				resizable : false,

				href:'${ctx}/spider/config/plan',
				modal : true,
				buttons : [ {
					text : '确认',
					handler : function() {
						d.panel('close');
					}
				}, {
					text : '取消',
					handler : function() {
						d.panel('close');
					}
				} ]
			});
		}
	</script>

</body>
</html>