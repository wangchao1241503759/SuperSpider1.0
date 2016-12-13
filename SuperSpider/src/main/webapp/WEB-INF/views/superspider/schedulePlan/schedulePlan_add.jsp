<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>计划设置</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
</head>
<body style="font-family: '微软雅黑'; font-size: 9px">
	<div class="easyui-layout" data-options="fit: true">
	<form id="mainform" action="${ctx}/schedulePlan/add" method="post">
		<div class="easyui-panel" title="请输入计划名称：" style="padding: 5px">
			<table>
				<tr>
					<td>计划名称：</td>
					<td colspan="3">
					<input id="palnId" type="hidden" name="id" value="${schedulePlan.id}"/>
					<input id="planName" data-options="required:true" class="easyui-validatebox" name="name" style="width: 100%" value="${schedulePlan.name}"></input></td>
				</tr>
				<tr>
					<td>创建人:</td>
					<td><input id="creator" name="creatorDisable" class="easyui-textbox" style="width: 120px" value="${user.name}" disabled=true></input>
						<input type="hidden" name="creator" value="${user.name}"/>
					</td>
					<td>创建时间:</td>
					<td>
						<input id="createTimeDisable" name="createtimeDisable" class="easyui-textbox" style="width: 120px" value="${schedulePlan.createTimeStr}" disabled=true></input>
						<input id="createTime" name="createTime" type="hidden" value="${schedulePlan.createTimeStr}" ></input>
					</td>
				</tr>
			</table>
		</div>
		<div id="plan_setting" class="easyui-panel" title="计划设置" style="width: 470px; height: 170px; padding: 5px" data-options="fit: true">
			<div style="padding: 5px">
			        计划频率：<input type="radio" name="planType" value="0" <c:if test="${schedulePlan.planType == 0 || schedulePlan.planType == null}"> checked="checked" </c:if> onclick="switchItem('0')"/>按周 
					   <input type="radio" name="planType" value="1" <c:if test="${schedulePlan.planType == 1}"> checked="checked" </c:if> onclick="switchItem('1')"/>按天
					   <input type="radio" name="planType" value="2" <c:if test="${schedulePlan.planType == 2}"> checked="checked" </c:if> onclick="switchItem('2')"/>按小时 
					   <input type="radio" name="planType" value="3" <c:if test="${schedulePlan.planType == 3}"> checked="checked" </c:if> onclick="switchItem('3')"/>按分钟
			</div>
			<div id="weeksetting" data-options="border:false,region:'center'" style="padding: 5px;">
				<div class="line" style="padding: 5px">
					每<input class="numberspinner" id="weekNum" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"  
                        onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'0')}else{this.value=this.value.replace(/\D/g,'')}"  style="width:60px;" data-options="min:1,max:7" value="1">周执行一次
				</div>
				<div class="imp weekList" style="padding: 5px">
					<input name="checkbox" type="checkbox" value="1"/>星期一 
					<input name="checkbox" type="checkbox" value="2"/>星期二 
					<input name="checkbox" type="checkbox" value="3"/>星期三
					<input name="checkbox" type="checkbox" value="4"/>星期四
					<input name="checkbox" type="checkbox" value="5"/>星期五 
					<input name="checkbox" type="checkbox" value="6"/>星期六
					<input name="checkbox" type="checkbox" value="7"/>星期天
				</div>
				<input type="hidden" id="weekStr" value=""/>
				<div style="padding: 5px">
					执行时间：<input id="executetime" class="easyui-my97" datefmt="HH:mm:ss" style="width: 80px;" value="00:00:00" readonly="readonly"></input>
				</div>
			</div>

			<div id="daysetting" data-options="border:false,region:'center'" style="padding: 5px;display: none;">
				<div class="line" style="padding: 5px;">
					每<input class="numberspinner" style="width: 30px;" id="dayNum" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"  
                                    onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'0')}else{this.value=this.value.replace(/\D/g,'')}"  style="width:60px;" data-options="min:1,max:31" value="1">天执行一次
				</div>
				<div style="padding: 5px">
					执行时间：<input id="executeDaytime" class="easyui-my97" datefmt="HH:mm:ss" style="width: 80px;" value="00:00:00"></input>
				</div>
			</div>
			<div id="hoursetting" data-options="border:false,region:'center'" style="padding: 5px;display: none;">
				<div class="line" style="padding: 5px">
					每<input class="numberspinner" id="hourNum" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"  
                                    onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'0')}else{this.value=this.value.replace(/\D/g,'')}"  style="width: 30px;" style="width:60px;"   value="1">小时执行一次
				</div>
			</div>
			<div id="minsetting" data-options="border:false,region:'center'" style="padding: 5px;display: none;">
				<div class="line" style="padding: 5px">
					每<input class="numberspinner" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"  
                                    onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'0')}else{this.value=this.value.replace(/\D/g,'')}"  id="minuteNum" style="width: 30px" value="1">分钟执行一次
				</div>
			</div>
			<div style="display: none;">
				Cron表达式:<input type="hidden"  name="cronExpression" style="width:100%;" value="${schedulePlan.cronExpression}" id="cronExpression" readonly="readonly"/>
						  <input type="hidden" id="cycleNum" name="cycleNum" value="${schedulePlan.cycleNum}"/>
						  <input type="hidden" id="weekCycle" name="weekCycle" value="${schedulePlan.weekCycle}"/>
						  <input type="hidden" id="executeTime" name="executeTime" value="${schedulePlan.executeTime}"/>
						  <input type="hidden" id="description" name="description" value="${schedulePlan.description}"/>
			</div>
		</div>
		</form>
	</div>

<script>
function switchItem(tag) {
	
	switch (tag) {
		case "0":
			$('#weeksetting').show();
			$('#daysetting').hide();
			$('#hoursetting').hide();
			$('#minsetting').hide();
			break;
		case "1":
			$('#weeksetting').hide();
			$('#daysetting').show();
			$('#hoursetting').hide();
			$('#minsetting').hide();
			break;
		case "2":
			$('#weeksetting').hide();
			$('#daysetting').hide();
			$('#hoursetting').show();
			$('#minsetting').hide();
			break;
		case "3":
			$('#weeksetting').hide();
			$('#daysetting').hide();
			$('#hoursetting').hide();
			$('#minsetting').show();
			break;
		default:
			break;
		}
	}
	//提交表单
	$('#mainform').form({    
	    onSubmit: function(){    
	    	var isValid = $(this).form('validate');
			return isValid;	// 返回false终止表单提交
	    },    
	    success:function(data){
	    	if(data == "success"){
	    		if(typeof($("#taskId").val()) != "undefined"){
	    			$.ajax({
	    	            url: "${ctx}/schedulePlan/json",
	    	            type: "get",
	    	            success: function (data) {
	    					if(data.rows)
	    					{
	    						planDatas = [];
	    						planDescriptions = [];
	    						for(var i=0;i<data.rows.length;i++)
	    						{
	    							planDatas.push({text:data.rows[i].name,value:data.rows[i].id});
	    							planDescriptions.push({text:data.rows[i].description,value:data.rows[i].id});
	    						}
	    					}
	    					$("#taskPlanId").combobox('loadData' , planDatas);
	    					d.panel('close');
	    					parent.$.messager.show({ title : "提示",msg: "操作成功！", position: "bottomRight" });
	    	            },error: function(e)
	    	            {
	    	            	parent.$.messager.progress('close');
	    	            	parent.$.messager.alert("系统错误:"+e);
	    	            }
	    		    });
	    		}else{
	    			successTip(data,dg,d);
	    		}
	    	}
	    	else
	    	alert(data);
	    }    
	});
/**
 * 设置按周的Cron的表达式;
 */
function setWeekCron(){
	var weekNum = $("#weekNum").val();
	var weekStr = "*";
	var checkedLength = $("input[name='checkbox']:checked").length;
	var description = "每隔"+weekNum+"周";
	if(checkedLength < 7)
	{
		$("input[name='checkbox']:checked").each(function(index,obj){
			if(index == 0)
			 weekStr = "";
			weekStr+=$(obj).val();
			if(index <checkedLength-1)
				weekStr+=",";
		});
	}
	var executetime = $("#executetime").datebox("getValue");
	var con = "";
	var times = executetime.split(":");
	for(var i=times.length-1; i>=0;i--){
		con += parseInt(times[i])+" ";
	}
	con += "? * ";
	if(weekNum > 1){
		con += weekStr+"/"+weekNum;
	}else if(checkedLength == 7){
		con += "*";
	}else if(checkedLength<7){
		con += weekStr;
	}else{
		con += "*";
	}
	if(checkedLength == 7)
		weekStr = "1,2,3,4,5,6,7";
	if(weekStr == "*"){
		description+="的，任意一天";
	}else{
		description+="的,星期"+weekStr;
	}
	description+="的"+executetime+"执行!";
	$("#cycleNum").val(weekNum);
	$("#weekCycle").val(weekStr);
	$("#executeTime").val(executetime);
	$("#cronExpression").val(con);
	$("#description").val(description);
}
 /**
  * 设置按天的Cron的表达式;
  */
function setDayCron(){
	var dayNum = $("#dayNum").val();
	var dayTime = $("#executeDaytime").datebox("getValue");
	var con = "";
	var description = "每隔"+dayNum+"天的,"+dayTime+"执行采集!";
	var times = dayTime.split(":");
	for(var i=times.length-1; i>=0;i--){
		con += parseInt(times[i])+" ";
	}
	if(dayNum > 1){
		con += "*/"+dayNum;
	}else{
		con += "*";
	}
	con +=" * ?";
	$("#cycleNum").val(dayNum);
	$("#weekCycle").val("");
	$("#executeTime").val(dayTime);
	$("#cronExpression").val(con);
	$("#description").val(description);
}
/**
*设置小时Cron的表达式;
*/
function setHourCron(){
	var hourNum = $("#hourNum").val();
	var description = "每隔"+hourNum+"小时,执行一次采集任务!";
	var con = "0 0 */"+hourNum+" * * ?";
	$("#cycleNum").val(hourNum);
	$("#weekCycle").val("");
	$("#executeTime").val("");
	$("#cronExpression").val(con);
	$("#description").val(description);
}
/**
 * 设置分钟Cron的表达式;
 */
function setMinuteCron(){
	var minuteNum = $("#minuteNum").val();
	var description = "每隔"+minuteNum+"分钟,执行一次采集任务!";
	var con = "0 */"+minuteNum+" * * * ?";
	$("#cycleNum").val(minuteNum);
	$("#weekCycle").val("");
	$("#executeTime").val("");
	$("#cronExpression").val(con);
	$("#description").val(description);
}
//获取当前系统时间;
function getCurrentTime()
 { 
     var now = new Date();
    
     var year = now.getFullYear();       //年
     var month = now.getMonth() + 1;     //月
     var day = now.getDate();            //日
    
     var hh = now.getHours();            //时
     var mm = now.getMinutes();          //分
     var ss = now.getSeconds();			//秒
     
     var clock = year + "-";
    
     if(month < 10)
         clock += "0";
    
     clock += month + "-";
    
     if(day < 10)
         clock += "0";
        
     clock += day + " ";
    
     if(hh < 10)
         clock += "0";
        
     clock += hh + ":";
     if (mm < 10) clock += '0'; 
     clock += mm + ":"; 
     
     if (ss < 10) ss += '0';
     clock += ss;
     
     return(clock); 
}

$(function(){
	var palnId = $("#palnId").val();
	var planType = $("input[name='planType']:checked").val();
	var createTime = $("#createTime").val();
	if(!createTime){
		$("#createTimeDisable").val(getCurrentTime());
		$("#createTime").val(getCurrentTime());
	}
	if(palnId){
		switchItem(planType);
		if("0" == planType){
			$("#weekNum").val($("#cycleNum").val());
			if($("#weekCycle").val()){
				var weeks = $("#weekCycle").val().split(",");
				$("input[name='checkbox']").each(function(index,obj){
					for(var i = 0 ; i<weeks.length ; i++){
						if($(obj).val() == weeks[i]){
							$(obj).attr("checked", true);
							break;
						}
					}
				});
			}
			$("#executetime").val($("#executeTime").val());
		}
		if("1" == planType){
			$("#dayNum").val($("#cycleNum").val());
			$("#executeDaytime").val($("#executeTime").val());
		}
		if("2" == planType){
			$("#hourNum").val($("#cycleNum").val());
		}
		if("3" == planType){
			$("#minuteNum").val($("#cycleNum").val());
		}
	}
});
</script>
</body>
</html>