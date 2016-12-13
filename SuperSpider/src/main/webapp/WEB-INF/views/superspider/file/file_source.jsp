<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script>

	$(function(){
		$('#dlg').dialog('close');
		initThreadDatas();
		init();
	});

	//启使用户的输入框
	function enabledInput()
	{
		$("#fileLimitStart").attr("disabled",false);
		$("#fileLimitEnd").attr("disabled",false);
		$("#fileLimitStart").validatebox('enableValidation');
		$("#fileLimitEnd").validatebox('enableValidation');
	}
	
	//禁用用户的输入框
	function disabledInput()
	{
		$("#fileLimitStart").attr("disabled",true);
		$("#fileLimitEnd").attr("disabled",true);
		$("#fileLimitStart").val("");
		$("#fileLimitEnd").val("");
		$("#fileLimitStart").validatebox('disableValidation');
		$("#fileLimitEnd").validatebox('disableValidation');
	}
	
	//启使用户的按钮
	function enabledButton()
	{
		$('#addButton').linkbutton('enable');
		$('#delButton').linkbutton('enable');
	}

	
	
	function setValue()
	{
		var exclusion = $("#exclusion");
		var exclusionChecked=exclusion.prop("checked");
		var exclusion_value = '';
		if(exclusionChecked)
		{
			exclusion_value = 'Y';
		}
		else
		{
			exclusion_value = 'N';
		}
		exclusion.val(exclusion_value);
	}
	
	function initThreadDatas()
	{
		var threadDatas = "[";
		for(var i = 0 ; i<50 ; i++)
		{
				threadDatas +="{text:'"+(i+1)+"',value:'"+(i+1)+"'}";
				if(i<49)
				{
					threadDatas +=",";
				}
		}
		threadDatas +="]";
		threadDatas = eval(threadDatas);
		$("#fileExtractThreadNum").combobox('loadData',threadDatas);
		
		if('${fileSource.fileExtractThreadNum}')
		{
			$("#fileExtractThreadNum").combobox('setValue','${fileSource.fileExtractThreadNum}');
		}
		else
		{
			$("#fileExtractThreadNum").combobox('setValue','1');
		}
	}
	
	function init()
	{
		var fileLimit = "${fileSource.fileLimit}";
		if(fileLimit=='N' || fileLimit=="")
		{
			disabledInput();
		}
	}
</script>
<table width="890px;">

	<tr>
		<input id="fileId" name = "fileId" type="hidden" value="${fileSource.fileId}"></input>
		<td width="100px;">文件路径:</td>
		<td width="500px;" colspan="4">
			<input type="text" id="filePath" name="filePath"  value="${fileSource.filePath}" placeholder="请输入文件路径" class="easyui-validatebox span2" data-options="required:true" value="" maxlength="5000" style="width:500px;height:22px;">
		</td>
	</tr>
	<tr>
		<td width="100px;">文件(目录)名匹配：</td>
		<td width="500px;" colspan="3">
			<input type="text" id="fileName" name="fileName"  value="${fileSource.fileName}" placeholder="请输入文件(目录)名匹配" class="easyui-validatebox span2" data-options="required:true" value="" maxlength="5000" style="width:500px;height:22px;">
		</td>
		<td><input type="checkbox" id="exclusion" name="exclusion" value="${fileSource.exclusion}" <c:if test="${fileSource.exclusion=='Y'}"> checked="checked" </c:if> onclick="setValue()">排除</input></td>
	</tr>
	<tr>
		<td width="80px;">提取线程数：</td>
		<td width="187px">
			<select id="fileExtractThreadNum" class="easyui-combobox" name="fileExtractThreadNum" style="width: 187px;" data-options="panelHeight:120,editable:false">
			</select>
		</td>
	</tr>
	<tr>
		<td width="80px;">文件大小范围：</td>
		<td width="187px" align="left">
			<input type="radio" name="fileLimit" <c:if test="${fileSource.fileLimit == 'Y'}"> checked="checked" </c:if>  value="Y" onclick="enabledInput()">限制</input>
			<input type="radio" name="fileLimit" <c:if test="${fileSource.fileLimit == 'N'}"> checked="checked" </c:if> <c:if test="${empty fileSource.fileLimit}"> checked="checked" </c:if>  value="N" onclick="disabledInput()">不限制</input>
		</td>
		<td width="100px"><input validType="checkNum" id="fileLimitStart" name="fileLimitStart" maxlength="7" value="${fileSource.fileLimitStart }"  data-options="required:true" class="easyui-validatebox" style="width: 90px;" <c:choose><c:when test="${fileSource.fileLimit == 'Y'}"> </c:when> <c:otherwise>disabled="true"></c:otherwise></c:choose></input>kb</td>
		<td width="110px">至&nbsp;&nbsp;<input validType="checkNum" id="fileLimitEnd" name="fileLimitEnd" maxlength="7" value="${fileSource.fileLimitEnd }"  data-options="required:true" class="easyui-validatebox" style="width: 90px;" <c:choose><c:when test="${fileSource.fileLimit == 'Y'}"> </c:when> <c:otherwise>disabled="true"></c:otherwise></c:choose></input>kb</td>
	</tr>
	<tr>
		<td width="80px;">是否采集子目录：</td>
		<td width="5px;" align="left">
			<input type="radio" name="fileIsSubDir" <c:if test="${fileSource.fileIsSubDir == 'Y'}"> checked="checked" </c:if> <c:if test="${empty fileSource.fileIsSubDir}"> checked="checked" </c:if> value="Y" onclick="enabledButton()">是</input>
			<input type="radio" name="fileIsSubDir" <c:if test="${fileSource.fileIsSubDir == 'N'}"> checked="checked" </c:if> value="N">否</input>
		</td>
	</tr>
</table>
