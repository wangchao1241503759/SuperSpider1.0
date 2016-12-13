<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>系统日志</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
</head>
<body style="font-family: '微软雅黑'; font-size: 9px">
	<div class="easyui-layout" data-options="fit: true">
	<form id="mainform" action="" method="post">
		<div class="easyui-panel" style="padding: 5px">
			<table class="table table-hover table-condensed">
				<tr>
					<td>日志级别：</td>
					<td >
						<c:choose>
							<c:when test="${log.level=='ERROR'}">错误</c:when>
							<c:when test="${log.level=='WARN'}">警告</c:when>
							<c:when test="${log.level=='INFO'}">信息</c:when>
							<c:when test="${log.level=='error'}">错误</c:when>
							<c:when test="${log.level=='warn'}">警告</c:when>
							<c:when test="${log.level=='info'}">信息</c:when>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td>产生时间：</td>
					<td >
						<fmt:formatDate value="${log.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
				</tr>
				<tr>
					<td>日志来源：</td>
					<td >
					
						<c:choose>
							<c:when test="${log.source=='db'}">数据库采集组件</c:when>
							<c:when test="${log.source=='ftp'}">FTP采集组件</c:when>
							<c:when test="${log.source=='file'}">文件采集组件</c:when>
							<c:when test="${log.source=='web'}">WEB采集组件</c:when>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td>事件：</td>
					<td >
						${log.event}
					</td>
				</tr>
				<tr>
					<td>详细内容：</td>
					<td >
						${log.content}
					</td>
				</tr>
			</table>
		</div>
		</form>
	</div>

<script>

</script>
</body>
</html>