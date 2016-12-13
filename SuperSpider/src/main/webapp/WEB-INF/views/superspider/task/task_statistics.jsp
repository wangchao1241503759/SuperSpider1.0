<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>采集任务统计</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script src="${ctx}/static/plugins/echarts-2.2.7/build/dist/echarts.js"></script>
<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript" charset="utf-8">
	var names = [];
	var data = [];
	var taskName = [];
	var series = [];
	$(function() {
		
		getBarData();
		getTaskList();
		
		$("#mainGross").show();
		$("#mainIncrement").hide();
		$("#mainElapsed").hide();
	});
	
	//获取下载器数据
	function getTaskList()
	{
		var url = '${ctx}/task/getTaskListAll';
		$("#taskId").combobox('reload',url);
	}
	// 初始化当前图表
	function initBar(names,data){
		//ECharts单文件引入
        require.config({
            paths: {
                echarts: '${ctx}/static/plugins/echarts-2.2.7/build/dist'
            }
        });
        
        require(
                [
                    'echarts',
                    'echarts/chart/bar', // 使用柱状图就加载bar模块，按需加载
                    'echarts/chart/line'
                ],
                function (ec) {
                    // 基于准备好的dom，初始化echarts图表
                    var myChart = ec.init(document.getElementById('mainGross')); 
                    
                    var option = {
//                		    title : {
//                		        text: '数据采集记录汇总',
//                		        x:'center'
//                		    },
               		    tooltip : {
               		        trigger: 'axis'
               		    },
                        legend: {
                            data:['累计采集数据总量（条）'],
                            x:'right'
                        },
                        xAxis : [
                            {
                            	name:'时间',
                                type : 'category',
//                                 data : ["衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]
                                data : names
                            }
                        ],
                        yAxis : [
                            {
                            	name:'条',
                                type : 'value'
                            }
                        ],
                        series : [
                            {
                                "name":"累计采集数据总量（条）",
                                "type":"bar",
                                "data":data
                            }
                        ]
                    };
            
                    // 为echarts对象加载数据 
                    myChart.setOption(option); 
                }
            );
	}
	
	function initLine(names,data){
		//ECharts单文件引入
        require.config({
            paths: {
                echarts: '${ctx}/static/plugins/echarts-2.2.7/build/dist'
            }
        });
        
        require(
                [
                    'echarts',
                    'echarts/chart/bar', // 使用柱状图就加载bar模块，按需加载
                    'echarts/chart/line'
                ],
                function (ec) {
                    // 基于准备好的dom，初始化echarts图表
                    var myChart = ec.init(document.getElementById('mainIncrement')); 
                    
                    var option = {
               		    tooltip : {
               		        trigger: 'axis'
               		    },
                        legend: {
                            data:['增量记录数'],
                            x:'right'
                        },
                        xAxis : [
                            {
                            	name:'时间',
                                type : 'category',
                                data : names
                            }
                        ],
                        yAxis : [
                            {
                            	name:'条',
                                type : 'value'
                            }
                        ],
                        series : [
                            {
                                "name":"增量记录数",
                                "type":"line",
                                "data":data
                            }
                        ]
                    };
            
                    // 为echarts对象加载数据 
                    myChart.setOption(option); 
                }
            );
	}
	
	function initLineElapsed(names,taskName,series){
		//ECharts单文件引入
        require.config({
            paths: {
                echarts: '${ctx}/static/plugins/echarts-2.2.7/build/dist'
            }
        });
        
        require(
                [
                    'echarts',
                    'echarts/chart/bar', // 使用柱状图就加载bar模块，按需加载
                    'echarts/chart/line'
                ],
                function (ec) {
                    // 基于准备好的dom，初始化echarts图表
                    var myChart = ec.init(document.getElementById('mainElapsed')); 
                    
                    var option = {
               		    tooltip : {
               		        trigger: 'axis'
               		    },
                        legend: {
                            data:taskName,
                            y:'bottom'
                        },
                        xAxis : [
                            {
                            	name:'采集次数',
                                type : 'category',
                                axisTick:{interval:'0',onGap:false},
                                data : names
                            }
                        ],
                        yAxis : [
                            {
                            	name:'秒',
                                type : 'value'
                            }
                        ],
                        series : series
                    };
            
                    // 为echarts对象加载数据 
                    myChart.setOption(option); 
                }
            );
	}


		function switchTimeRange(tag) {
			switch (tag) {
				case 1:
					$('#year_range').show();
					$('#month_range').hide();
					$('#day_range').hide();
					break;
				case 2:
					$('#year_range').hide();
					$('#month_range').show();
					$('#day_range').hide();
					break;
				case 3:
					$('#year_range').hide();
					$('#month_range').hide();
					$('#day_range').show();
					break;
				default:
					break;
			}
		}
		
		function switchTaskRange(tag) {
			switch (tag) {
				case 1:
					$('#singleTask').show();
					$('#multipleTask').hide();
					break;
				case 2:
					$('#singleTask').hide();
					$('#multipleTask').show();
					break;
				default:
					break;
			}
		}
		
		function stat(){
			getBarData();
			showTitle();
		}
		
		function getBarData()
		{
			// 初始化柱状图
			$.ajax({
		    	type: "POST",
		        url: '${ctx}/taskStatisticsController/getTaskStatisticsData',
		        cache: false,
		        dataType : "json",
		        data:$("#queryForm").serialize(),
		        success: function(result){
		        	if(result.success)
		        	{
		        			var echarts = result.obj;
			        		var jsonData = echarts.list;
				        	if(jsonData != '' && jsonData.length > 0){
				        		names = [];
				        		data = [];
				        		for(var i = 0; i < jsonData.length; i++){
				        			names.push(jsonData[i].names);
				        			data.push(jsonData[i].data);
				        		}
				        	}  
			    			var statType=$("input[name='guideline']:checked").val();
			    			switch(statType)
			    			{
				    			case "gross":
					        		initBar(names,data);
				    				break;
				    			case "increment":
					        		initLine(names,data);
				    				break;
				    			case "elapsed":
				    				names = echarts.xAxisList;
				    			    taskName = echarts.legendList;
				    			    series = echarts.seriesList;
				    				initLineElapsed(names,taskName,series);
				    				break;
				    			default:
				    				break;
			    			}
		        	}
		        	else
		        	{
		        		$.messager.alert('提示',result.msg,'info');
		        	}
		        }
		    }); 
		}
		
		function showTitle()
		{
			var statType=$("input[name='guideline']:checked").val();
			var dimensionType=$("input[name='dimension']:checked").val();
			switch(statType){
			case "gross":
				$("#mainGross").show();
				$("#mainIncrement").hide();
				$("#mainElapsed").hide();
				
				switch(dimensionType)
				{
					case "year":
						var startYear = $("#startYear").val();
						var endYear = $("#endYear").val();
						$("h2").empty();
						var msg = startYear+"至"+endYear+"各年度任务采集总量统计";
						$("h2").html(msg);
						break;
					case "quarter":
						var startYear = $("#startYear").val();
						var endYear = $("#endYear").val();
						$("h2").empty();
						var msg = startYear+"至"+endYear+"各季度任务采集总量统计";
						$("h2").html(msg);
						break;
					case "month":
						var startYear = $("input[name='startYearMonthrange']").val();
// 						var startYear = $("#startYearMonthrange").val();
						var endYear = $("#endYearMonthrange").val();
						var startMonth = $("#startMonth").combobox('getValue');
						var endMonth = $("#endMonth").combobox('getValue');
						$("h2").empty();
						var msg = startYear+"-"+startMonth+"至"+endYear+"-"+endMonth+"各月度任务采集总量统计";
						$("h2").html(msg);
						break;
					case "day":
// 						var startDate = $("#startDate").val();
// 						var endDate = $("#endDate").val();
						var startDate = $("input[name='startDate']").val();
						var endDate = $("input[name='endDate']").val();
						$("h2").empty();
						var msg = startDate+"至"+endDate+"各天任务采集总量统计";
						$("h2").html(msg);
						break;
					default:
						break;
				}	

				break;
			case "increment":
				$("#mainGross").hide();
				$("#mainIncrement").show();
				$("#mainElapsed").hide();
				
				switch(dimensionType)
				{
					case "year":
						var startYear = $("#startYear").val();
						var endYear = $("#endYear").val();
						$("h2").empty();
						var msg = startYear+"至"+endYear+"各年度任务采集增量变化统计";
						$("h2").html(msg);
						break;
					case "quarter":
						var startYear = $("#startYear").val();
						var endYear = $("#endYear").val();
						$("h2").empty();
						var msg = startYear+"至"+endYear+"各季度任务采集增量变化统计";
						$("h2").html(msg);
						break;
					case "month":
						var startYear = $("input[name='startYearMonthrange']").val();
						var endYear = $("#endYearMonthrange").val();
						var startMonth = $("#startMonth").combobox('getValue');
						var endMonth = $("#endMonth").combobox('getValue');
						$("h2").empty();
						var msg = startYear+"-"+startMonth+"至"+endYear+"-"+endMonth+"各月度任务采集增量变化统计";
						$("h2").html(msg);
						break;
					case "day":
						var startDate = $("input[name='startDate']").val();
						var endDate = $("input[name='endDate']").val();
						$("h2").empty();
						var msg = startDate+"至"+endDate+"各天任务采集增量变化统计";
						$("h2").html(msg);
						break;
					default:
						break;
				}	
				break;
			case "elapsed":
				$("#mainGross").hide();
				$("#mainIncrement").hide();
				$("#mainElapsed").show();
				
				switch(dimensionType)
				{
					case "year":
						var startYear = $("#startYear").val();
						var endYear = $("#endYear").val();
						$("h2").empty();
						var msg = startYear+"至"+endYear+"各年度任务采集耗时统计";
						$("h2").html(msg);
						break;
					case "quarter":
						var startYear = $("#startYear").val();
						var endYear = $("#endYear").val();
						$("h2").empty();
						var msg = startYear+"至"+endYear+"各季度任务采集耗时统计";
						$("h2").html(msg);
						break;
					case "month":
						var startYear = $("input[name='startYearMonthrange']").val();
						var endYear = $("#endYearMonthrange").val();
						var startMonth = $("#startMonth").combobox('getValue');
						var endMonth = $("#endMonth").combobox('getValue');
						$("h2").empty();
						var msg = startYear+"-"+startMonth+"至"+endYear+"-"+endMonth+"各月度任务采集耗时统计";
						$("h2").html(msg);
						break;
					case "day":
						var startDate = $("input[name='startDate']").val();
						var endDate = $("input[name='endDate']").val();
						$("h2").empty();
						var msg = startDate+"至"+endDate+"各天任务采集耗时统计";
						$("h2").html(msg);
						break;
					default:
						break;
				}	
				
				break;
			default:
				break;
			}
			
			


		}

</script>
</head>
<body>
	<div class="easyui-panel" style="border:none"  data-options="fit: true">
		<form id="queryForm" action="">
		<div style="margin-left:10px;margin-top:5px; font-size:14px;font-weight:bold;">任务统计参数设置：</div>
		<hr size="1px" align="left" width="700px"></hr>
		<div style="margin-left:30px;margin-top: 10px">
			<span style="margin-left: 5px">统计维度：</span>
			<input type="radio" name="dimension" value="year" onclick="switchTimeRange(1)">按年</input>
			<input type="radio" name="dimension" value="quarter" style="margin-left: 20px" checked="checked" onclick="switchTimeRange(1)">按季</input>
			<input type="radio" name="dimension" value="month" style="margin-left: 20px" onclick="switchTimeRange(2)">按月</input>
<!-- 			<input type="radio" name="dimension" value="04" style="margin-left: 20px" onclick="switchTimeRange(2)">按周</input> -->
			<input type="radio" name="dimension" value="day" style="margin-left: 20px" onclick="switchTimeRange(3)">按天</input>
		</div>
		<div style="margin-left:30px;margin-top: 10px">
			<span style="margin-left: 5px">统计区间：</span>
			<span id="year_range">
				<input type="text" validType="checkYear" id="startYear" name="startYear" style="width: 90px;" class="easyui-validatebox" data-options="required:true,prompt: '起始年'" /> 年 - 
				<input type="text" validType="checkYear" id="endYear" name="endYear" style="width: 90px;" class="easyui-validatebox" data-options="required:true,prompt: '结束年'" /> 年
			</span>
			<span id="month_range" style="display: none;">
				<input type="text" validType="checkYear"　id="startYearMonthrange" name="startYearMonthrange" style="width: 80px;" class="easyui-validatebox" data-options="required:true,prompt: '起始年'" /> 年
				<select id="startMonth" name="startMonth" class="easyui-combobox" style="width: 50px;" data-options="panelHeight:'auto'">
					<option value="01">1</option>
					<option value="02">2</option>
					<option value="03">3</option>
					<option value="04">4</option>
					<option value="05">5</option>
					<option value="06">6</option>
					<option value="07">7</option>
					<option value="08">8</option>
					<option value="09">9</option>
					<option value="10">10</option>
					<option value="11">11</option>
					<option value="12">12</option>
				</select>月
				 - 
				<input type="text"　validType="checkYear" id="endYearMonthrange" name="endYearMonthrange" style="width: 80px;" class="easyui-validatebox" data-options="required:true,prompt: '结束年'" /> 年
				<select id="endMonth" name="endMonth" class="easyui-combobox" style="width: 50px;" data-options="panelHeight:'auto'">
					<option value="01">1</option>
					<option value="02">2</option>
					<option value="03">3</option>
					<option value="04">4</option>
					<option value="05">5</option>
					<option value="06">6</option>
					<option value="07">7</option>
					<option value="08">8</option>
					<option value="09">9</option>
					<option value="10">10</option>
					<option value="11">11</option>
					<option value="12">12</option>
				</select>月
			</span>
			<span id="day_range" style="display: none;">
				<input type="text" id="startDate" name="startDate" class="easyui-my97"	datefmt="yyyy-MM-dd" data-options="required:true,width:150,prompt: '开始日期'" /> - 
				<input validType="checkDateTo['#startDate']" type="text" id="endDate" name="endDate" class="easyui-my97"	datefmt="yyyy-MM-dd" data-options="required:true,width:150,prompt: '结束日期'" /> 
			</span>
		</div>
		<div style="margin-left:30px;margin-top: 10px;">
			<span style="margin-left: 5px">统计方式：</span>
			<select id="taskRange" name="taskRange" class="easyui-combobox" style="width: 80px;" data-options="panelHeight:'auto'">
				<option value="single">单任务</option>
				<option value="multiple">多任务</option>
			</select>
			<script>
				$('#taskRange').combobox({
				    onChange:function(newValue,oldValue){
				        if(newValue == 'single') {
				        	switchTaskRange(1);
				        }
				        else{
				        	switchTaskRange(2);
				        }
				    }
				});
			</script>
<!-- 			<input type="radio" name="taskRange" value="01" checked="checked"  onclick="switchTaskRange(1)">单任务</input> -->
<!-- 			<input type="radio" name="taskRange" value="02" onclick="switchTaskRange(2)">多任务</input> -->
			<span id="singleTask" style="margin-left: 10px">
				<select id="taskId" name="taskId" class="easyui-combobox" style="width: 150px" data-options="required:true,prompt: '请选择需要统计的任务',valueField:'taskId',textField:'taskName'">
<!-- 					<option value="doc">公文数据采集</option> -->
<!-- 					<option value="rules">管理制度采集</option> -->
<!-- 					<option value="archive">档案数据采集</option> -->
<!-- 					<option value="stat">统计报表数据采集</option> -->
				</select>
			</span>
			<span id="multipleTask" style="display: none;margin-left: 10px">
				<input type="checkbox" name="tasktype" value="db">DB采集任务</input>
				<input type="checkbox" name="tasktype" value="web">WEB采集任务</input>
				<input type="checkbox" name="tasktype" value="ftp">FTP采集任务</input>
				<input type="checkbox" name="tasktype" value="file">文件采集任务</input>
			</span>
		</div>
		<div style="margin-left:30px;margin-top: 10px">
			<span style="margin-left: 5px">统计指标：</span>
			<input type="radio" name="guideline" value="gross" style="margin-left: 0px" checked="checked" >采集数据总量</input>
			<input type="radio" name="guideline" value="increment" style="margin-left: 20px">增量数据变化</input>
			<input type="radio" name="guideline" value="elapsed" style="margin-left: 20px">单次采集耗时</input>
			<a class="easyui-linkbutton" style="margin-left: 240px" iconCls="icon-standard-chart-pie" onclick="stat()">统计</a>
		</div>
		
		<div style="margin-left:10px;margin-top:20px; font-size:14px;font-weight:bold;">任务统计结果：</div>
		<hr size="1px" align="left" width="700px"></hr>
		<div >
			<h2 style="margin-left: 200px;">各季度任务采集总量统计</h2>
		</div>
		<div id="mainGross" style="width: 800px; height: 350px; margin-top: 20px;"></div>
		<div id="mainIncrement" style="width: 800px; height: 350px; margin-top: 20px;"></div>
		<div id="mainElapsed" style="width: 800px; height: 350px; margin-top: 20px;"></div>
<!-- 		<img id="statResult" width="600px" style="border: none"/> -->
		</form>
	</div>
		

</body>
</html>