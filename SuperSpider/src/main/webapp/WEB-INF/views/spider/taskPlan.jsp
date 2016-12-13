<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>计划设置</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body style="font-family: '微软雅黑'; font-size: 9px">
	<div class="easyui-layout" data-options="fit: true">
		<div class="easyui-panel" title="请输入计划名称：" style="padding: 5px">
			<table>
				<tr>
					<td>计划名称：</td>
					<td colspan="3"><input id="plan_name" class="easyui-textbox" style="width: 100%" value=""></input></td>
				</tr>
				<tr>
					<td>创建人:</td>
					<td><input id="creator" class="easyui-textbox" style="width: 120px" value="数据获取管理员" disabled=true></input></td>
					<td>创建时间:</td>
					<td><input id="createtime" class="easyui-textbox" style="width: 120px" value="2015-12-31 10:00" disabled=true></input></td>
				</tr>
			</table>
		</div>
		<div id="plan_setting" class="easyui-panel" title="计划设置"
			style="width: 470px; height: 170px; padding: 5px" data-options="fit: true">
			<div style="padding: 5px">
				计划频率： <input type="radio" name="type" checked="checked"	onclick="switchItem(1)"/>按周 
					   <input type="radio" name="type" onclick="switchItem(2)"/>按天
					   <input type="radio" name="type" onclick="switchItem(3)"/>按小时 
					   <input type="radio" name="type" onclick="switchItem(4)"/>按分钟
			</div>
			<div id="weeksetting" data-options="border:false,region:'center'" style="padding: 5px;">
				<div class="line" style="padding: 5px">
					每<input class="numberspinner" style="width: 30px" data-options="min:1,max:7" value="1">周执行一次
				</div>
				<div class="imp weekList" style="padding: 5px">
					<input type="hidden" value="1" id="test"/>
					<input type="checkbox" value="week1"/>星期一 
					<input type="checkbox" value="week2"/>星期二 
					<input type="checkbox" value="week3"/>星期三
					<input type="checkbox" value="week4"/>星期四
					<input type="checkbox" value="week5"/>星期五 
					<input type="checkbox" value="week6"/>星期六
					<input type="checkbox" value="week7"/>星期天
				</div>
				<div style="padding: 5px">
					执行时间：<input id="executetime" class="easyui-textbox" style="width: 80px;" value="24:00:00"></input>
				</div>
			</div>

			<div id="daysetting" data-options="border:false,region:'center'" style="padding: 5px;display: none;">
				<div class="line" style="padding: 5px;">
					每<input class="numberspinner" style="width: 30px;" value="1">天执行一次
				</div>
				<div style="padding: 5px">
					执行时间：<input id="executetime" class="easyui-textbox" style="width: 80px;" value="24:00:00"></input>
				</div>
			</div>
			<div id="hoursetting" data-options="border:false,region:'center'" style="padding: 5px;display: none;">
				<div class="line" style="padding: 5px">
					每<input class="numberspinner" style="width: 30px;" value="1">小时执行一次
				</div>
			</div>
			<div id="minsetting"
				data-options="border:false,region:'center'" style="padding: 5px;display: none;">
				<div class="line" style="padding: 5px">
					每<input class="numberspinner" style="width: 30px" value="1">分钟执行一次
				</div>
			</div>
		</div>
	</div>

	<script>
		function switchItem(tag) {
// 			switch (tag) {
// 			case 1:
// 				$('#weeksetting').panel('open');
// 				$('#daysetting').panel('close');
// 				$('#hoursetting').panel('close');
// 				$('#minsetting').panel('close');
// 				break;
// 			case 2:
// 				$('#weeksetting').panel('close');
// 				$('#daysetting').panel('open');
// 				$('#hoursetting').panel('close');
// 				$('#minsetting').panel('close');
// 				break;
// 			case 3:
// 				$('#weeksetting').panel('close');
// 				$('#daysetting').panel('close');
// 				$('#hoursetting').panel('open');
// 				$('#minsetting').panel('close');
// 				break;
// 			case 4:
// 				$('#weeksetting').panel('close');
// 				$('#daysetting').panel('close');
// 				$('#hoursetting').panel('close');
// 				$('#minsetting').panel('open');
// 				break;
// 			default:
// 				break;
// 			}

									switch (tag) {
									case 1:
										$('#weeksetting').show();
										$('#daysetting').hide();
										$('#hoursetting').hide();
										$('#minsetting').hide();
										break;
									case 2:
										$('#weeksetting').hide();
										$('#daysetting').show();
										$('#hoursetting').hide();
										$('#minsetting').hide();
										break;
									case 3:
										$('#weeksetting').hide();
										$('#daysetting').hide();
										$('#hoursetting').show();
										$('#minsetting').hide();
										break;
									case 4:
										$('#weeksetting').hide();
										$('#daysetting').hide();
										$('#hoursetting').hide();
										$('#minsetting').show();
										break;
									default:
										break;
									}
		}

	</script>
</body>
</html>