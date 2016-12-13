<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>采集任务统计</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
</head>
<body>
	<div class="easyui-panel" style="border:none"  data-options="fit: true">
		<div style="margin-left:10px;margin-top:5px; font-size:14px;font-weight:bold;">任务统计参数设置：</div>
		<hr size="1px" align="left" width="700px"></hr>
		<div style="margin-left:30px;margin-top: 10px">
			<span style="margin-left: 5px">统计维度：</span>
			<input type="radio" name="dimension" value="01" onclick="switchTimeRange(1)">按年</input>
			<input type="radio" name="dimension" value="02" style="margin-left: 20px" checked="checked" onclick="switchRange(1)">按季</input>
			<input type="radio" name="dimension" value="03" style="margin-left: 20px" onclick="switchTimeRange(2)">按月</input>
			<input type="radio" name="dimension" value="04" style="margin-left: 20px" onclick="switchTimeRange(2)">按周</input>
			<input type="radio" name="dimension" value="05" style="margin-left: 20px" onclick="switchTimeRange(3)">按天</input>
		</div>
		<div style="margin-left:30px;margin-top: 10px">
			<span style="margin-left: 5px">统计区间：</span>
			<span id="year_range">
				<input type="text" name="start_year" style="width: 90px;" class="easyui-validatebox" data-options="prompt: '起始年'" /> 年 - 
				<input type="text" name="start_year" style="width: 90px;" class="easyui-validatebox" data-options="prompt: '结束年'" /> 年
			</span>
			<span id="month_range" style="display: none;">
				<input type="text" name="start_year_monthrange" style="width: 80px;" class="easyui-validatebox" data-options="prompt: '起始年'" /> 年
				<select id="start_month" class="easyui-combobox" style="width: 50px;" data-options="panelHeight:'auto'">
					<option value="1">1</option>
					<option value="2">2</option>
					<option value="3">3</option>
					<option value="4">4</option>
					<option value="5">5</option>
					<option value="6">6</option>
					<option value="7">7</option>
					<option value="8">8</option>
					<option value="9">9</option>
					<option value="10">10</option>
					<option value="11">11</option>
					<option value="12">12</option>
				</select>月
				 - 
				<input type="text" name="end_year_monthrange" style="width: 80px;" class="easyui-validatebox" data-options="prompt: '结束年'" /> 年
				<select id="end_month" class="easyui-combobox" style="width: 50px;" data-options="panelHeight:'auto'">
					<option value="1">1</option>
					<option value="2">2</option>
					<option value="3">3</option>
					<option value="4">4</option>
					<option value="5">5</option>
					<option value="6">6</option>
					<option value="7">7</option>
					<option value="8">8</option>
					<option value="9">9</option>
					<option value="10">10</option>
					<option value="11">11</option>
					<option value="12">12</option>
				</select>月
			</span>
			<span id="day_range" style="display: none;">
				<input type="text" name="start_date" class="easyui-my97"	datefmt="yyyy-MM-dd" data-options="width:150,prompt: '开始日期'" /> - 
				<input type="text" name="end_date" class="easyui-my97"	datefmt="yyyy-MM-dd" data-options="width:150,prompt: '结束日期'" /> 
			</span>
		</div>
		<div style="margin-left:30px;margin-top: 10px;">
			<span style="margin-left: 5px">统计方式：</span>
			<select id="taskRange" class="easyui-combobox" style="width: 80px;" data-options="panelHeight:'auto'">
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
				<select id="task" class="easyui-combobox" style="width: 150px" data-options="prompt: '请选择需要统计的任务',panelHeight:'auto'">
					<option value="doc">公文数据采集</option>
					<option value="rules">管理制度采集</option>
					<option value="archive">档案数据采集</option>
					<option value="stat">统计报表数据采集</option>
				</select>
			</span>
			<span id="multipleTask" style="display: none;margin-left: 10px">
				<input type="checkbox" name="tasktype" value="01">DB采集任务</input>
				<input type="checkbox" name="tasktype" value="01">WEB采集任务</input>
				<input type="checkbox" name="tasktype" value="01">FTP采集任务</input>
				<input type="checkbox" name="tasktype" value="01">文件采集任务</input>
			</span>
		</div>
		<div style="margin-left:30px;margin-top: 10px">
			<span style="margin-left: 5px">统计指标：</span>
			<input type="radio" name="guideline" value="01" style="margin-left: 0px" checked="checked" >采集数据总量</input>
			<input type="radio" name="guideline" value="02" style="margin-left: 20px">增量数据变化</input>
			<input type="radio" name="guideline" value="03" style="margin-left: 20px">单次采集耗时</input>
			<a class="easyui-linkbutton" style="margin-left: 240px" iconCls="icon-standard-chart-pie" onclick="stat()">统计</a>
		</div>
		
		<div style="margin-left:10px;margin-top:20px; font-size:14px;font-weight:bold;">任务统计结果：</div>
		<hr size="1px" align="left" width="700px"></hr>
		
		<img id="statResult" width="600px" style="border: none"/>
	</div>
	
	<script>
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
			var imgSrc="${ctx}/static/images/";
			var statType=parseInt($("input[name='guideline']:checked").val());
			
			switch(statType){
			case 1:
				imgSrc=imgSrc+"stat-total.png";
				break;
			case 2:
				imgSrc=imgSrc+"stat-change.png";
				break;
			case 3:
				imgSrc=imgSrc+"stat-time.png";
				break;
			default:
				break;
			}
			var img=document.getElementById("statResult");
			img.src=imgSrc;
		}
	</script>
</body>
</html>