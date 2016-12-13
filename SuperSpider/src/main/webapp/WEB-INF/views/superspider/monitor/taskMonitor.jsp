<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<style>
#base {
	width: 1000px;
	margin: 0 auto;
}
</style>
<title>任务监控</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!-- echart 文件  -->
<script src="${ctx}/static/plugins/echarts-2.2.7/build/dist/echarts.js"></script>
<script type="text/javascript" charset="utf-8">
	
	var runDatagrid; // 正在运行的数据表
	var finishDatagrid; // 已经完成任务数据表
	var taskId = 'all';  // 任务ID
	var name = '所有采集任务'; //图标名称
	var taskCombobox;
	
	$(function() {
		
		// 初始化资源类型下拉框
		initTaskCombobox();
		// 初始化任务监控图表
		init(0);
		// 初始化正在运行任务的数据表
		initRunDatagrid();
		// 已经完成的任务数据表
		unuseDatagrid();
		reloadUnuseDatagrid();
		
	});
	
	//初始化资源类型下拉框
	function initTaskCombobox(){
		
		taskCombobox = $("input[name=task]");
		taskCombobox.combobox({
			url : '${ctx}/taskMonitorController/getAllTask?taskId=' + taskId,
			valueField : 'taskId',
			textField : 'taskName',
			multiple : false,
			editable : false,
			panelHeight : 200,
			panelWidth : 250,
			onLoadSuccess : function() {
				taskCombobox.combobox('setValue', taskId);
			},
			onHidePanel: function(){
				taskId = taskCombobox.combobox('getValue');
				name = taskCombobox.combobox('getText');
				init(0);
			}
		});
		
	}
	// 初始化当前图表
	function init(num){
		
		// 初始化echarts实例
		require.config({
			paths : {
				echarts : '${ctx}/static/plugins/echarts-2.2.7/build/dist'
			}
		});
		require([ 
			      'echarts', 'echarts/chart/line' // 使用柱状图就加载bar模块，按需加载
		],
		function(ec) {
			// 基于准备好的dom，初始化echarts图表
			var myChart = ec.init(document.getElementById('main'));
			var option = {
				title : {
					text : name + '采集数量统计'
				},
				tooltip : {
					trigger : 'axis'
				},
				legend : {
					data : [ '采集速度']
				},
			    xAxis : [
			        {
			            type : 'category',
			            boundaryGap : false,
			            axisLabel: {
			            	rotate: 60
			            },
			            data : (function() {
							var now = new Date();
							var res = [];
							var len = 20;
							while (len--) {
								res.unshift(new Date(now.setMinutes (now.getMinutes () - 1)).toLocaleTimeString().replace(/^\D*/, ''));
							}
							return res;
						})() 
			        }
			    ],
			    yAxis : [
			        {
			        	name : '速度（条）',
			            type : 'value',
			            scale : true,
						boundaryGap: [0, 0.2],
						splitNumber : 10
			        }
			    ],
			    series : [{
					name : '采集速度',
					type : 'line',
					smooth:true,
					itemStyle: {normal: {areaStyle: {type: 'default'}}},
					data : (function() {
						var res = [];
						var len = 20;
						while (len--) {
							res.push(0);
						}
						return res;
					})() 
				}]
			};
			myChart.setOption(option);
			var lastData = num;
			var axisData;
			var timeTicket;
			clearInterval(timeTicket);
			timeTicket = setInterval(function() {
				
				
				$.ajax({
			    	type: "POST",
			        url: '${ctx}/taskMonitorController/getTaskRunningTask',
			        data : {
						taskId : taskId,
					},
			        cache: false,
			        dataType : "json",
			        success: function(data){
			        	$('#titleRunTaskNum').html(data.taskNum);
			        	$('#runTaskNum').html(data.taskNum);
			        	$("#collectNum").html(data.num);
			        	
						lastData = data.num < 0 ? 0 : data.num;
						axisData = data.date;
						//console.log("lastData = " + lastData + "  num = " + data.num + "  axisData = " + axisData);
						myChart.addData([ 
						    [   0, // 系列索引
								lastData, // 新增数据
								false, // 新增数据是否从队列头部插入
								false, // 是否增加队列长度，false则自定删除原有数据，队头插入删队尾，队尾插入删队头
								axisData // 坐标轴标签
							] 
						]);
						
						reloadRunTaskDatagrid();
						reloadUnuseDatagrid();
						
			        }
			    });  
			}, 1000);
			
	    });
	}
	
	// 过滤查询
	var taskStatus = 'all';
	var taskName = 'all';
	var taskCount = [];
	
	var filter_taskName = 'all';
	var filter_taskStatus = 'all';
	function _search(num) {
		
		if(num == 0){
			taskStatus = $("#taskStatus").combobox('getValue'); 
			taskName = $("#taskName").val();
			reloadRunTaskDatagrid();
		} else {
			filter_taskName = $('#filter_taskname').val();
			filter_taskStatus = $('#filter_taskStatus').combobox('getValue');
			reloadUnuseDatagrid();
		}
	}
	
	
	
	// 重新加载数据
	function reloadRunTaskDatagrid(){
		
		// 运行时任务
		var str = taskCount.join(',');
		$.ajax({
	    	type: "POST",
	        url: '${ctx}/taskMonitorController/getTaskRunningInfo',
	        data : {
	        	task : taskStatus,
	        	taskName : taskName,
	        	taskCount : taskCount.join(',')
			},
	        cache: false,
	        dataType : "json",
	        success: function(data){
	        	
	        	if(data != null && data.length > 0){
	        		taskCombobox.combobox('reload');  
	        		appendRow(data);
	        	} else {
	        		taskCount = [];
	        		clearDatagrid(runDatagrid);
	        	}
	        }
	    }); 
	}
	
	
	// 添加一行数据
	function appendRow(data){
		
		clearDatagrid(runDatagrid);
		
		for(var i = 0; i < data.length; i++){
			
			runDatagrid.datagrid("appendRow",{
				taskId : data[i].taskId,
				taskName : data[i].taskName,
				startTimeStr : data[i].startTimeStr,
				taskRunTime : data[i].taskRunTime,
				collectNum : data[i].collectNum,
				taskRunSpeed : data[i].taskRunSpeed,
				taskStatus : data[i].taskStatus,
				operation : '<a href="javascript:void(0);" style="text-align: center;" onclick="showLog(\'' + data[i].taskId + '\',\'' + data[i].taskName + '\',\'1\');"><font color="blue">查看日志</font></a>'
			}); 
			
		}
		
	}
	
	// 正在运行的任务数据表
	function initRunDatagrid(){
		
		// 初始化列表
		runDatagrid = $('#runDatagrid').datagrid({
			title : '执行任务列表',
			pagination : false,
			pagePosition : 'bottom',
			idField : "taskId",
			width : 1000,
			fit : true,
			fitColumns : true,
			checkOnSelect : true,
			selectOnCheck : true,
			rownumbers:true,
		    frozenColumns : [ [ {
				title : '序号',
				field : 'taskId',
				width : 80,
				hidden : true
			}] ],
			columns : [ [ {
				title : '任务名称',
				field : 'taskName',
				width : 80,
			}, {
				title : '开始时间',
				field : 'startTimeStr',
				width : 80
			}, {
				title : '运行时间',
				field : 'taskRunTime',
				width : 80,
			}, {
				title : '采集记录数',
				field : 'collectNum',
				width : 80,
			}, {
				title : '本次采集平均速度（条/秒）',
				field : 'taskRunSpeed',
				width : 95,
			}, {
				title : '任务运行状态',
				field : 'taskStatus',
				width : 80,
			}, {
				title : '详细',
				field : 'operation',
				width : 80
			} ] ]
		});
		
	}
	
	// initUnuseDatagrid unuseDatagrid
	function reloadUnuseDatagrid(){
		
		$.ajax({
	    	type: "POST",
	        url: '${ctx}/taskMonitorController/getNotRuningTask',
	        data : {
	        	taskName : filter_taskName,
	        	taskStatus : filter_taskStatus
			},
	        cache: false,
	        dataType : "json",
	        success: function(data){
	        	if(data != null && data.length > 0){
	        		appendFinishRow(data);
	        		$('#unuserdNum').html(data.length);
	        		$('#titleUnuserdNum').html(data.length);
	        	} else {
	        		clearDatagrid(finishDatagrid);
	        		$('#unuserdNum').html(0);
	        		$('#titleUnuserdNum').html(0);
	        	}
	        }
	    });
	}
	
	function appendFinishRow(data){
		
		clearDatagrid(finishDatagrid);
		for(var i = 0; i < data.length; i++){
			finishDatagrid.datagrid("appendRow",{
				taskNextRunTimeStr : data[i].taskNextRunTimeStr,
				taskRunTime : data[i].taskRunTime,
				taskName : data[i].taskName,
				collectNum : data[i].collectNum,
				taskRunSpeed : data[i].taskRunSpeed,
				taskStatus : data[i].taskStatus,
				operation : '<a href="javascript:void(0);" style="text-align: center;" onclick="showLog(\'' + data[i].taskId + '\',\'' + data[i].taskName + '\',\'0\');"><font color="blue">查看日志</font></a>'
			}); 
		}
	}
	
	// 已经完成的任务数据表
	function unuseDatagrid(){
		
		finishDatagrid = $('#finishDatagrid').datagrid({
			title : '闲置任务列表',
			pagination : false,
			pagePosition : 'bottom',
			idField : "taskId",
			pageSize : 10,
			pageList : [ 10, 20, 30, 40 ],
			fit : true,
			fitColumns : true,
			checkOnSelect : true,
			selectOnCheck : true,
			rownumbers:true,
			frozenColumns : [ [ {
				title : '序号',
				field : 'taskId',
				width : 80,
				hidden : true
			}] ],
			columns : [ [ {
				title : '任务名称',
				field : 'taskName',
				width : 80,
				sortable : true
			}, {
				title : '下次开始时间',
				field : 'taskNextRunTimeStr',
				width : 80
			}, {
				title : '上次运行耗时',
				field : 'taskRunTime',
				width : 80,
			}, {
				title : '上次采集记录数',
				field : 'collectNum',
				width : 80,
			}, {
				title : '上次采集平均速度（条/秒）',
				field : 'taskRunSpeed',
				width : 95,
				formatter : function(value, rowData, rowIndex) {
					return value + "条/秒";
				}
			}, {
				title : '任务状态',
				field : 'taskStatus',
				width : 80
			}, {
				title : '详细',
				field : 'operation',
				width : 80
			} ] ]
		});
		
	}
	
	// 清除数据表格
	function clearDatagrid(dg){
		
		var rows = dg.datagrid('getRows');  
	    if (rows != null && rows.length > 0) {  
	        for (var i = rows.length - 1; i >= 0; i--) {  
	            var index = dg.datagrid('getRowIndex', rows[i]);  
	            dg.datagrid('deleteRow', index);  
	        }  
	    } 
	}
	
	function showLog(taskId,taskName,event){
		var logTab ={
	        title: "采集日志", href: "taskLogController/taskLogList?taskId="+ taskId, iniframe: true, closable: true, refreshable: true, iconCls: "icon-hamburg-billing", repeatable: true, selected: true
	    };
		parent.mainTabs.tabs("add",logTab);
	}
</script>
</head>
<body>
	<div class="easyui-panel" style="border: none" data-options="fit: true">
		<div id="base">
			<div
				style="margin-left: 10px; margin-top: 10px; font-size: 14px; font-weight: bold;">任务运行动态监测：</div>
			<hr size="1px" align="left" width="1000px"></hr>
			<div style="margin-left: 10px;">
				<input id="task" name="task" style="width: 250px;">
			</div>
			<div id="echarts">
				<div id="main" style="width: 1000px; height: 400px; margin-top: 20px;"></div>
			</div>
			<div style="margin-left: 10px; margin-top: 10px; font-size: 14px; font-weight: bold;">任务状态监控：</div>
			<hr size="1px" align="left" width="1000px"
				style="text-align: center;"></hr>
			<div style="margin-left: 20px;">
				<table style="width: 1000px; height: 30px;">
					<tr>
						<th width="60px;">任务总数：</th>
						<td style="text-align: left;">${map.taskNum}个</td>
						<th width="65px;">运行任务数:</th>
						<td style="text-align: left;"><span id="runTaskNum">0</span>个</td>
						<th width="100px;">闲置任务数：</th>
						<td style="text-align: left;"><span id="unuserdNum">${map.unuserdNum}个</td>
						<th width="75px;">异常任务数：</th>
						<td style="text-align: left;">0个</td>
						<th width="120px;">每秒采速度:</th>
						<td style="text-align: left;"><span id="collectNum">0</span>条/秒</td>

					</tr>
				</table>
			</div>
			<div style="margin-top: 5px; margin-left: 10px; font-size: 14px; font-weight: bold; text-align: left; width: 700px">执行任务数：<span id="titleRunTaskNum">${map.runingSize}个</div>
			<hr size="1px" align="left" width="1000px"></hr>
			<div style="margin-left: 12px; margin-top: 10px;">
				<form id="searchForm">

					<input type="text" name="taskName" id="taskName" style="width: 200px; height: 21px;" class="easyui-validatebox" data-options="prompt: '请输入任务名称'" />&nbsp;&nbsp;&nbsp;&nbsp; 
					<select id="taskStatus" name="taskStatus" class="easyui-combobox" style="width: 200px; height: 25px;" data-options="prompt: '请选择任务状态',panelHeight:'auto'">
						<option value="all">所有</option>
						<option value="normal">正常运行</option>
						<option value="exception_ignore">成功处理异常后，正常运行</option>
					</select> 
					<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="_search(0);">查询</a>
				</form>
			</div>
			<div style="margin-top: 10px; width: 1000px; height: 300px;">
				<table id="runDatagrid"></table>
			</div>
			<div style="margin-top: 5px; margin-left: 10px; font-size: 14px; font-weight: bold; text-align: left; width: 700px">闲置任务数：<span id="titleUnuserdNum">${map.unuserdNum}个</div>
			<hr size="1px" align="left" width="1000px"></hr>
			<div style="margin-left: 12px; margin-top: 10px;">
				<input type="text" id="filter_taskname" name="filter_taskname" style="width: 200px; height: 21px;" class="easyui-validatebox" data-options="prompt: '请输入任务名称'" />&nbsp;&nbsp;&nbsp;&nbsp; 
				<select id="filter_taskStatus" class="easyui-combobox" style="width: 200px; height: 25px;" data-options="prompt: '请选择任务状态',panelHeight:'auto'">
					<option value="all">所有</option>
					<option value="normal">闲置</option>
					<option value="failed">出错终止-闲置</option>
					<option value="exception_ignore">成功处理异常-闲置</option>
				</select> 
				<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="_search(1)">查询</a>
			</div>
			<div style="width: 1000px; height: 300px; margin-top: 10px;">
				<table id="finishDatagrid"></table>
			</div>
		</div>
	</div>
	<div id="dd" style=""></div>
</body>
</html>