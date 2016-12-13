<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script src="${ctx}/static/plugins/echarts-2.2.7/build/dist/echarts.js"></script>
<script type="text/javascript" charset="utf-8">

var taskCount = [];
var d;

$(function() {
	
	// 初始化柱状图
	$.ajax({
    	type: "POST",
        url: '${ctx}/taskMonitorController/getIndexBarInfo',
        cache: false,
        dataType : "json",
        success: function(data){
        	if(data != '' && data.length > 0){
        		var day = [];
        		var num = [];
        		for(var i = 0; i < data.length; i++){
        			day.push(data[i].day);
        			num.push(data[i].num);
        		}
        		initBar(day,num);
        	}  
        }
    }); 
	// 初始化折现图
	initLine();
	// 初始化datagrid
	initDatagrid();
});

// 折线图
function initLine(){
	
	require.config({
		paths : {
			echarts : '${ctx}/static/plugins/echarts-2.2.7/build/dist'
		}
	});
	require([ 
		      'echarts', 'echarts/chart/bar', 'echarts/chart/line' 
	],
	function(ec) {
		
		var myLine = ec.init(document.getElementById('mainLine'));
		var optionLine = {
		    title : {
		        text: '采集速度与任务数变化图'
		    },
		    tooltip : {
		        trigger: 'axis'
		    },
		    legend: {
		        data:['采集速度', '任务数量']
		    },
		    dataZoom : {
		        show : false,
		        start : 0,
		        end : 100
		    },
		    xAxis : [
		        {
		            type : 'category',
		            axisLabel: {
		            	rotate: 60,
		            },
		            data : (function (){
		                var now = new Date();
		                var res = [];
		                var len = 20;
		                while (len--) {
		                	res.unshift(new Date(now.setMinutes (now.getMinutes () - 1)).toLocaleTimeString().replace(/^\D*/, ''));
		                }
		                return res;
		            })()
		        },
		        {
		            type : 'category',
		            data : (function (){
		                var res = [];
		                var len = 20;
		                while (len--) {
		                    res.push("");
		                }
		                return res;
		            })()
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value',
		            scale: true,
		            name : '速度(条)',
		            splitNumber : 10,
		            boundaryGap: [0, 0.2]
		        },
		        {
		            type : 'value',
		            scale: true,
		            name : '任务量(个)',
		            min : 0,
		            max:10,
		            splitNumber : 10
		        }
		    ],
		    series : [
		        {
		            name:'任务数量',
		            type:'line',
		            xAxisIndex: 1,
		            yAxisIndex: 1,
		            data:(function (){
		                var res = [];
		                var len = 20;
		                while (len--) {
		                    res.push(0);
		                }
		                return res;
		            })()
		        },
		        {
		            name:'采集速度',
		            type:'line',
		            data:(function (){
		                var res = [];
		                var len = 20;
		                while (len--) {
		                    res.push(0);
		                }
		                return res;
		            })()
		        } 
		    ]
		};
		myLine.setOption(optionLine);
		var lastData = 0;
		var axisData;
		var timeTicket;
		clearInterval(timeTicket);
		timeTicket = setInterval(function (){
			
			$.ajax({
		    	type: "POST",
		        url: '${ctx}/taskMonitorController/getTaskRunningTask',
		        data : {
					taskId : "all"
				},
		        cache: false,
		        dataType : "json",
		        success: function(d){
		        	
		        	reload();
		        	$('#runTaskNum').html(d.taskNum);
					$('#collectNum').html(d.num);
				    lastData = d.num < 0 ? 0 : d.num;
				    axisData = d.date;
				    
				    //console.log("index_main    lastData = " + lastData + "  num = " + d.num + "  axisData = " + axisData);
				    
				    // 动态数据接口 addData
				    myLine.addData([
				        [
				            0,        // 系列索引
				            d.taskNum, // 新增数据
				            false,     // 新增数据是否从队列头部插入
				            false     // 是否增加队列长度，false则自定删除原有数据，队头插入删队尾，队尾插入删队头
				        ],
				        [
				            1,        // 系列索引
				            lastData, // 新增数据
				            false,    // 新增数据是否从队列头部插入
				            false,    // 是否增加队列长度，false则自定删除原有数据，队头插入删队尾，队尾插入删队头
				            axisData  // 坐标轴标签
				        ]
				    ]);
					
					
		        }
		    }); 
		   
		}, 1000);
    });
	
	
}

// 柱状图
function initBar(day, num){
	
	var month = new Date();
	require.config({
		paths : {
			echarts : '${ctx}/static/plugins/echarts-2.2.7/build/dist'
		}
	});
	require([ 
		      'echarts', 'echarts/chart/bar', 'echarts/chart/line' 
	],
	function(ec) {
		var myChart = ec.init(document.getElementById('main'));
		var option = {
		    title : {
		        text: '本月每天数据采集记录汇总',
		        subtext: month.getMonth() + 1 + "月份"
		    },
		    tooltip : {
		        trigger: 'axis'
		    },
		    legend: {
		        data:['采集记录数（条）']
		    },
		    calculable : true,
		    xAxis : [
		        {
		            type : 'category',
		            data : day 
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value'
		        }
		    ],
		    series : [
		        {
		            name:'采集记录数（条）',
		            type:'bar',
		            data: num, 
		            markLine : {
		                data : [
		                    {type : 'average', name : '平均值'}
		                ]
		            } 
		        }
		    ]
		};
		myChart.setOption(option);
    });
	
}

function changeImage() {
	var imageFlag = $("#imageFlag").val();
	if (imageFlag == '0') {
		$("#start-month").show();
		$("#imageFlag").val(1);
		$("#btnCollapse").val('关闭');
	} else {
		$("#start-month").hide();
		$("#imageFlag").val(0);
		$("#btnCollapse").val('查看');
	}
}

// 重新加载datagride
function reload(){
	
	var str = taskCount.join(',');
	taskCount = [];
	$.ajax({
    	type: "POST",
        url: '${ctx}/taskMonitorController/getTaskRunningInfo',
        data : {
        	task : "all",
        	taskName : "all",
        	taskCount : str
		},
        cache: false,
        dataType : "json",
        success: function(data){
        	if(data != null && data.length > 0){
        		appendRow(data);
        	} else {
        		taskCount = [];
        		clearDatagrid();
        	}
        }
    }); 
	
}

//清除数据表格
function clearDatagrid(){
	
	var rows = d.datagrid('getRows');  
    if (rows != null && rows.length > 0) {  
        for (var i = rows.length - 1; i >= 0; i--) {  
            var index = d.datagrid('getRowIndex', rows[i]);  
            d.datagrid('deleteRow', index);  
        }  
    } 
}

// datagride 新增行
function appendRow(data){
	
    clearDatagrid();
    
	for(var i = 0; i < data.length; i++){
		d.datagrid("appendRow",{
			taskId : data[i].taskId,
			taskName : data[i].taskName,
			startTimeStr : data[i].startTimeStr,
			taskRunTime : data[i].taskRunTime,
			collectNum : data[i].collectNum,
			taskRunSpeed : data[i].taskRunSpeed,
			taskStatus : data[i].taskStatus,
			operation : '<a href="javascript:void(0);" style="text-align: center;" onclick="showLog(\'' + data[i].taskId + '\',\'' + data[i].taskName + '\',\'1\');"><font color="blue">查看日志</font></a>'
		}); 
		
		taskCount.push(data[i].taskId + "@" + data[i].collectNum);
	}
}

// datagride 初始化
function initDatagrid(){
	
	d = $('#datagrid').datagrid({
		title : '运行任务列表',
		pagination : false,
		pagePosition : 'bottom',
		idField : "id",
		pageSize : 10,
		pageList : [ 10, 20, 30, 40 ],
		fit : true,
		fitColumns : true,
		checkOnSelect : true,
		selectOnCheck : true,
		rownumbers:true,
		onLoadSuccess: function(data){
			var rows = $("#datagrid").datagrid("getRows");
			for(var i = 0; i < rows.length; i++){
				taskCount.push(rows[i].taskId + "@" + rows[i].collectNum);
			}  
	    },
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
			width : 80,
		}  ] ]
	});
}

function showLog(taskId,taskName,event){
	var logTab ={
        title: "采集日志", href: "taskLogController/taskLogList?taskId="+ taskId, iniframe: true, closable: true, refreshable: true, iconCls: "icon-hamburg-billing", repeatable: true, selected: true
    };
	parent.mainTabs.tabs("add",logTab);
}

</script>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<div style="margin-left:10px;margin-top:5px; font-size:14px;font-weight:bold;">数据采集总体情况：</div>
<hr size="1px" align="left" width="1000px"></hr>
<div style="margin-left:30px">
    <table style="width: 1000px; height: 30px;">
		<tr style="width: 100%">
			<th width="9%;">首次采集时间：</th>
			<td style="text-align: left; width: 49px;">${data.firstTime}</td>
			<th width="13%;">任务累计运行总次数：</th>
			<td style="text-align: left; width: 60px;" >${data.count}次</td>
			<th width="12%;" style="text-align: left;">累计采集数据总数：</th>
			<td style="text-align: left; width: 10px;">${data.number}条</td>
			<td style="text-align: left; width: 10px;"></td>
		</tr>
	</table>                                             
</div>     		
<div style="margin-left:10px;margin-top:5px;font-size:14px;font-weight:bold;">当月（${data.month}）数据采集情况：</div>
<hr size="1px" align="left" width="1000px"></hr>
<div style="margin-left:30px">
	<table style="width: 1000px; height: 30px;">
		<tr style="widows: 100%">
			<th width="7%;" style="">任务总数：</th>
			<td style="text-align: left; width: 60px;">${data.taskNum}个</td>
			<th width="9%;">任务运行次数：</th>
			<td style="text-align: left; width: 75px;">${data.runTaskNum}次</td>
			<th width="9%">采集数据数量：</th>
			<td style="text-align: left; width: 10px;">${data.collectNum}条</td>
			<td style="text-align: left; width: 10px;">
			<input type="button" id="btnCollapse" class="" value="查看" onclick="changeImage()"/>
			</td>
		</tr>
	</table>  
	<input id="imageFlag" type="hidden" value="0"/>
</div>           			
<div id="start-month" style="height:400px;display: none;">
	<div id="main" style="width: 1000px; height: 400px; margin-top: 20px;"></div>
</div>
<div style="margin-left:10px;font-size:14px;font-weight:bold;">今天（${data.day}）任务运行情况：</div>
<hr size="1px" align="left" width="1000px"></hr>
<div id="start-today" style="height:400px">
	<div id="mainLine" style="width: 1000px; height: 400px; margin-top: 20px;"></div>
</div>
<div style="margin-left:30px; margin-top: 10px;">
	<span>当前运行的任务总数：<span id="runTaskNum">0</span> 个</span>
	<span style="margin-left:40px">每秒采集速度：<span id="collectNum">0</span>条/秒</span>
</div>
<div id="tab" style="margin-top: 20px; width: 1000px; height: 150px;">
	<table id="datagrid"></table>
</div>
<div id="dd" style=""></div>
