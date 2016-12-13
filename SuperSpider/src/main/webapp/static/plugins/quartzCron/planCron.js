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
    	if(data == "success")
    	successTip(data,dg,d);
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
	$("#cycleNum").val(weekNum);
	$("#weekCycle").val(weekStr);
	$("#executeTime").val(executetime);
	$("#cronExpression").val(con);
}
 /**
  * 设置按天的Cron的表达式;
  */
function setDayCron(){
	var dayNum = $("#dayNum").val();
	var dayTime = $("#executeDaytime").datebox("getValue");
	var con = "";
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
}
/**
*设置小时Cron的表达式;
*/
function setHourCron(){
	var hourNum = $("#hourNum").val();
	var con = "0 0 */"+hourNum+" * * ?";
	$("#cycleNum").val(hourNum);
	$("#weekCycle").val("");
	$("#executeTime").val("");
	$("#cronExpression").val(con);
}
/**
 * 设置分钟Cron的表达式;
 */
function setMinuteCron(){
	var minuteNum = $("#minuteNum").val();
	var con = "0 */"+minuteNum+" * * * ?";
	$("#cycleNum").val(minuteNum);
	$("#weekCycle").val("");
	$("#executeTime").val("");
	$("#cronExpression").val(con);
}
