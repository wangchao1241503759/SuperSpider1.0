<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<c:choose>
	<c:when test="${taskType=='db'}">
		<c:set var="taskTypeName" value="数据库(DB)采集"></c:set>
	</c:when>
	<c:when test="${taskType=='web'}">
		<c:set var="taskTypeName" value="网络资源(WEB)采集"></c:set>
	</c:when>
	<c:when test="${taskType=='ftp'}">
		<c:set var="taskTypeName" value="FTP采集"></c:set>
	</c:when>
	<c:when test="${taskType=='file'}">
		<c:set var="taskTypeName" value="本地文件采集"></c:set>
	</c:when>
	<c:otherwise>
	<c:set var="taskTypeName" value="未知"></c:set>
	</c:otherwise>
</c:choose>
<table width="890px;">
	<tr>
		<td width="92px;">任务名称:</td>
		<td width="198px">
			<input id="taskId" name = "taskId" type="hidden" value="${task.taskId}"></input>
			<input id="taskState" name = "taskState" type="hidden" value="0"></input>
			<input id="fieldMappingData" type="hidden" name = "fieldMappingData"></input> 
			<input id="attributeMappingData" type="hidden" name = "attributeMappingData"></input> 
			<input validType="nameValid" id="taskName" name = "taskName" onblur="validSameTaskName(this);" data-options="required:true" class="easyui-validatebox" style="width: 200px;" value="${task.taskName}"></input>
		</td>
		<td width="75px;">任务类型:</td>
		<td width="200px;"><input class="easyui-textbox" style="width: 200px;" value="${taskTypeName}" disabled=true/><input id="taskType" name="taskType" class="easyui-textbox" style="width: 201px;display: none;" value="${task.taskType}"/></td>
		<td width="80px;"></td>
		<td width="150px;"></td>
	</tr>
	<tr>
		<td>任务说明：</td>
		<td colspan="3">
		   <div style="width: 100px;">
			   <textarea id="taskDescription" name="taskDescription" rows="3" cols="82" name="description" style="font-size: 12px;padding-top:0px; font-family: '微软雅黑'" maxlength="100">${task.taskDescription}</textarea>
		   </div>
		</td>
	</tr>
	<tr>
		<td>任务优先级：</td>
		<td colspan="3">
			<select id="priorityLevel"  name="priorityLevel" class="easyui-combobox" style="width: 515px" data-options="panelHeight:'auto'">
			</select> 
		</td>
	</tr>
	<tr>
		<td>任务计划：</td>
		<td colspan="3">
			<select id="taskPlanId"  name="taskPlanId" style="width: 434px" data-options="panelHeight:'auto'">
			</select> 
			<a href="#" class="easyui-linkbutton" data-options="align:'right',iconCls:'icon-standard-add'" onclick="planAdd()">新建计划</a>
		</td>
	</tr>
	<tr>
		<td>任务计划描述：</td>
		<td colspan="3">
			<div id="planDescritpion"></div>
		</td>
	</tr>
	<tr>
		<td>创建人:</td>
		<td>
			<input id="taskCreatorStr" name="taskCreatorStr" class="easyui-textbox" style="width: 205px;" value="${user.name}" disabled=true></input>
			<input id="taskCreator" name="taskCreator" class="easyui-textbox" style="width: 205px;display: none;" value="${user.loginName}"></input>
		</td>
		<td>创建时间:</td>
		<td><input id="taskCreateDateStr" name="taskCreateDateStr" class="easyui-textbox" style="width: 200px;" value="${task.taskCreateDate}" disabled="disabled"></input>
			<input id="taskCreateDate" name="taskCreateDate" class="easyui-textbox" style="width: 200px;display: none;" value="${task.taskCreateDate}"></input>
		</td>
	</tr>
</table>
<div id="planDlg"></div>  
<script type="text/javascript">
var d;
var planDatas = [];
var planDescriptions = [];
$(function(){
		if (Sys.ie){
			$("#taskDescription").attr("cols","88");
		}else if(Sys.chrome){
			$("#taskDescription").attr("cols","82");
		}
		var date = getCurrentTime();
		var taskCreateDate = '${task.taskCreateDate}';
		if(taskCreateDate == ""){
			$("#taskCreateDateStr").val(date);
			$("#taskCreateDate").val(date);
		}else{
			var formatTaskCreateDate = taskCreateDate.substring(0,taskCreateDate.indexOf("."));
			$("#taskCreateDateStr").val(formatTaskCreateDate);
			$("#taskCreateDate").val(formatTaskCreateDate);
		}
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
				if('${task.taskPlanId}'){
					$("#taskPlanId").combobox("setValue",'${task.taskPlanId}');
				}
            },error: function(e)
            {
            	parent.$.messager.progress('close');
            	parent.$.messager.alert("系统错误:"+e);
            }
	    });
		$("#taskPlanId").combobox({
			onChange : function(newValue, oldValue) {
				var description = "";
				for(var i = 0 ;i<planDescriptions.length;i++)
				{
					if(planDescriptions[i].value == newValue){
						description = planDescriptions[i].text;
						break;
					}
				}
				$("#planDescritpion").html("注："+description);
			}
		}); 
		
		priorityLevelLoadData();
	});
	
	//任务优先级加载下拉数据
	function priorityLevelLoadData()
	{
		$("#priorityLevel").combobox(
			{
				valueField:'id',
			    textField:'text',
				"data": 
				[ {
			        'id' : '0',
			        'text' : '最低'
			      }, {
			        'id' : '1',
			        'text' : '低'
			      }, {
			        'id' : '2',
			        'text' : '中'
			      }, {
			        'id' : '3',
			        'text' : '高'
			      }, {
			        'id' : '4',
			        'text' : '最高'
			      } ],
			      value:'${task.priorityLevel}'
			}
		); 
		
		if('${task.priorityLevel}'=="")
		{
			$("#priorityLevel").combobox('setValue','4');
		}
	}
	function validSameTaskName(obj){
		
		var taskName = $(obj).val();
		var taskId = $("#taskId").val();
		if(taskName == "")
			return;
// 		$.ajax({
//             url: "${ctx}/task/validSameTaskName?taskName="+taskName,
//             type: "get",
//             success: function (data) {
// 				if(data == 1){
// 					parent.$.messager.show({ title : "提示",msg: "此任务名已经存在,请修改!"});
// 					$(obj).focus();
// 				}
//             },error: function(e)
//             {
//             	parent.$.messager.alert("系统错误:"+e);
//             }
// 	    });

		var url = '${pageContext.request.contextPath}/task/checkTaskNameIsExists';
		$.ajax({
			type:'post',
			url:url,
			data:{taskId:taskId,taskName:taskName},
			async:false,
			success:function(result)
			{
				if (!result.success) {
					parent.$.messager.show({ title : "提示",msg: "此任务名已经存在,请修改!"});
					$(obj).focus();
				}
			},
			error:function (XMLHttpRequest, textStatus, errorThrown) {
			    // 通常 textStatus 和 errorThrown 之中
			    // 只有一个会包含信息
			    this; // 调用本次AJAX请求时传递的options参数
			}
		});
	}
	//弹窗新建计划
	function planAdd() {
		d=$("#planDlg").dialog({   
		    title: '添加调度计划',    
		    width: 530,
		    height: 350,    
		    href:'${ctx}/schedulePlan/add',
		    modal:true,
		    buttons:[{
				text:'确认',
				handler:function(){
					var planType = $("input[name='planType']:checked").val();
					if("0" == planType)
						setWeekCron();
					if("1" == planType)
						setDayCron();
					if("2" == planType)
						setHourCron();
					if("3" == planType)
						setMinuteCron();
					$("#mainform").submit();
				}
			},{
				text:'取消',
				handler:function(){
					d.panel('close');
				}
			}]
		});
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
</script>
