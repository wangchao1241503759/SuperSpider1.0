<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>文件数据源</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'; font-size : 9px;">
	<table>
		<tr>
			<td>文件路径:</td>
			<td colspan="3"><input id="ftppath" class="easyui-filebox" name="file1"	style="width: 475px" value="d:\documents" /></td>
		</tr>
		<tr>
			<td>文件(目录)名匹配：</td>
			<td colspan="2"><input class="easyui-textbox" style="width: 475px" value="abc*.xlsx;*def.docx;g*hi.pdf;"></input></td>
			<td><input type="checkbox" value="negate">排除</input></td>
		</tr>
		<tr>
			<td>文件大小范围：</td>
			<td colspan="3">
				<input type="radio" name="filesize" checked="checked" value="01" onclick="enableInput()"/><span>限制</span>
				<input type="radio"	name="filesize" value="02" onclick="enableInput()"/><span>不限制</span>
				<input id="filesize_min" class="numberspinner" style="width: 100px;" data-options="min:0,max:59" value="0" /> kb 至 
				<input id="filesize_max" class="numberspinner" style="width: 100px;" data-options="min:1,max:59" value="1" /> kb
			</td>
		</tr>
		<tr>
			<td>是否采集子目录：</td>
			<td>
				<input type="radio" name="clear" checked="checked" value="01"/><span>是</span>
				<input type="radio"	name="clear" value="02"/><span>否</span>
			</td>
		</tr>
	</table>

<!-- 	<div style="height: 10px;"></div> -->
<!-- 	<div> -->
<!-- 		<div>文件类型：</div> -->
<!-- 		<div style="padding: 5px;"> -->
<!-- 			<input type="checkbox" value="word">word(doc,docx)</input> <input -->
<!-- 						type="checkbox" value="excel">excel(xls,xlsx)</input> <input -->
<!-- 						type="checkbox" value="ppt">ppt(ppt,pptx)</input> <input -->
<!-- 						type="checkbox" value="pdf">pdf</input> <input type="checkbox" -->
<!-- 						value="txt">txt</input> <input type="checkbox" value="xml">xml</input> -->
<!-- 			<input type="checkbox" value="html">html/htm</input> <input -->
<!-- 						type="checkbox" value="rtf">rtf</input> <input type="checkbox" -->
<!-- 						value="eml">eml</input> <input type="checkbox" value="jpg">jpg</input> -->
<!-- 					<input type="checkbox" value="png">png</input> -->
<!-- 		</div> -->
<!-- 	</div> -->

	<script>
		function enableInput(){
			$('#filesize_min').attr('disabled',$("input[name='filesize']:checked").val()=="02");
			$('#filesize_max').attr('disabled',$("input[name='filesize']:checked").val()=="02");
		}
	
	</script>
</body>
</html>