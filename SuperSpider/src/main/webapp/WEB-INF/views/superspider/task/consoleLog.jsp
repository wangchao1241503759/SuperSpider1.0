<%@ page language="java" contentType="text/html; charset=utf-8"  pageEncoding="utf-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<head>
	<style type="text/css" media="screen">
	#wrap{width:100%; height:100%;}
	.unit{padding:5px; border:solid 1px #000; height:auto; clear:both;}	
	.unit label{text-align:right;width:100px; line-height: 30px; display:inline-block;}
	.unit input{line-height: 30px; width:100px; height:30px;}
	.log {font-size:11px; font-family:Courier; height:98.2%; overflow: auto; background:black;}
	</style>
</head>
<div id="wrap">
	<div id="logs" class="unit log">
	</div>
</div>
<input type="hidden" id="taskId" value="${taskId}">
<input type="hidden" id="taskState" value="${taskState}">
<script type="text/javascript" charset="utf-8">
	$(function(){
		if($("#taskState").val() == "0"){
			$('#logs').html("<font color='#00FF00'>任务未启动....</font>");
		}
	});
	var setID = setInterval("getLogInfo()",100);
	function getLogInfo(){
		$.ajax({
	    	type: "POST",
	        url: '${pageContext.request.contextPath}/task/getConsoleLog',
	        data : {
				taskId : $("#taskId").val()
			},
	        cache: false,
	        dataType : "json",
	        success: function(data){
	        	for(var i = 0; i < data.length; i++){
	        		$('#logs').append(data[i] + "<br>");
	        		/* if(document.getElementsByTagName("font").length >= 500){
	        			$('#logs').html("");
	        		} */
	        		$('#logs').scrollTop($('#logs')[0].scrollHeight);
	        	}
	        }
	    }); 
	} 
	
</script>
