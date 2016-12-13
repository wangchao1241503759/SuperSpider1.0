<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script>

	$(function(){
		$('#dlg').dialog('close');
		//disabledButton();
		getFtpTypeData();
		initThreadDatas();
		initCharsetDatas();
		init();
	});

	//启使用户的输入框
	function enabledInput()
	{
		$("#ftpLimitStart").attr("disabled",false);
		$("#ftpLimitEnd").attr("disabled",false);
		$("#ftpLimitStart").validatebox('enableValidation');
		$("#ftpLimitEnd").validatebox('enableValidation');
	}
	
	//禁用用户的输入框
	function disabledInput()
	{
		$("#ftpLimitStart").attr("disabled",true);
		$("#ftpLimitEnd").attr("disabled",true);
		$("#ftpLimitStart").val("");
		$("#ftpLimitEnd").val("");
		
		$('#ftpLimitStart').validatebox('disableValidation');
		$('#ftpLimitEnd').validatebox('disableValidation');
	}
	
	//启使用户的按钮
	function enabledButton()
	{
		$('#addButton').linkbutton('enable');
		$('#delButton').linkbutton('enable');
	}
	
	var contents = [
	    		    {id:'FTP',ftpTypeName:'FTP'},
	    		    {id:'SFTP',ftpTypeName:'SFTP'}
	    	   ];	
	
	//获取FTP类型
	function getFtpTypeData()
	{
		$("#ftpType").combobox('loadData',contents);
		
		if('${ftpSource.ftpType}')
		{
			$("#ftpType").combobox('setValue','${ftpSource.ftpType}');
		}
		else
		{
			$("#ftpType").combobox('setValue','FTP');
		}
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
		$("#ftpDownloadThreadNum").combobox('loadData',threadDatas);
		
		if('${ftpSource.ftpDownloadThreadNum}')
		{
			$("#ftpDownloadThreadNum").combobox('setValue','${ftpSource.ftpDownloadThreadNum}');
		}
		else
		{
			$("#ftpDownloadThreadNum").combobox('setValue','1');
		}
	}
	function initCharsetDatas()
	{
		var charsetDatas = [
		                    {value:'UTF-8',text:'UTF-8'},
		                    {value:'GBK',text:'GBK'},
		                    {value:'GB2312',text:'GB2312'},
		                    {value:'GB18030',text:'GB18030'},
		                    {value:'iso-8859-1',text:'iso-8859-1'},
		                    {value:'ASCII',text:'ASCII'},
		                    {value:'Unicode',text:'Unicode'}
		                    ];
		$("#charset").combobox('loadData',charsetDatas);
		
		if('${ftpSource.charset}')
		{
			$("#charset").combobox('setValue','${ftpSource.charset}');
		}
		else
		{
			$("#charset").combobox('setValue','UTF-8');
		}
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
		exclusion.val(exclusion_value)
	}
	
	//测试连接
	function testFtpConnection()
	{
		$.ajax({
			type:'post',
			data:$("#configForm").serialize(),
			url:"${pageContext.request.contextPath}/ftpController/testConnection",
			timeout:3000,
			success: function(data){
				if(data.success)
				{
					$("#ftp_connstatus").css({color:'green'}).text("连接成功!");
				}
				else
				{
					$("#ftp_connstatus").css({color:'red'}).text("连接失败,请检查参数配置!");
				}
			},error:function (XMLHttpRequest, textStatus, errorThrown) {
				if(textStatus == "timeout"){
					$("#ftp_connstatus").css({color:'red'}).text("连接超时,请检查参数配置!");
				}else{
					$("#ftp_connstatus").css({color:'red'}).text("连接失败,请检查参数配置!");
				}
	        }
		});
	}
	
	//预览
	function testFtpShow()
	{
		$('#dlg').dialog('open');
// 		$('#dlg').dialog('refresh', '${pageContext.request.contextPath}/ftpController/preview');
		$('#dlg').dialog('refresh', '${pageContext.request.contextPath}/ftpController/preview');

// 		var d=$("#preview").dialog({   
// 		    title: '查看',    
// 		      width : 400,
// 		      height : 300,  
// 		      href : '${pageContext.request.contextPath}/ftpController/preview',
// 		    maximizable:true,
// 		    modal:true,
// 		    buttons:[{
// 				text:'关闭',
// 				handler:function(){
// 					d.panel('close');
// 				}
// 			}]
// 		});
	}
	
	//获取FTP路径
	function onGetFtpPath()
	{
		//var path = $("#header").text();
		//alert(path)
		//var path = $("#preview_url").contents().find("#header").html();
		//var path =$("#preview_url").contents().find("header").html();
		//var path =$(window.frames["preview_url"].document).find("#header").text()
		//var path =$(window.frames["preview_url"].document).find("h1 #header").text()
		//var path =document.getElementById('preview_url').getgetElementById("header");
		//alert(path)
		
        var nodes = $('#tt').tree('getChecked');
        var s = '';
        for(var i=0; i<nodes.length; i++){
            if (s != '') s += '\r\n';
            s += nodes[i].id;
        }
        
        $("#ftpPath").val('');
        $("#ftpPath").val(s);
        $('#dlg').dialog('close');
        $("#ftpPath").validatebox('validate');
        
	}
	
	window.onload = function () {
		  /*
		   *  下面两种获取节点内容的方式都可以。
		   *  由于 IE6, IE7 不支持 contentDocument 属性，所以此处用了通用的
		   *  window.frames["iframe Name"] or window.frames[index]
		   */
		  var d = window.frames[".preview_url"].document;
		  d.getElementsByTagName('h1')[0].innerHTML = 'pp';
		  alert(d.getElementsByTagName('h1')[0]);
		}
	
	function init()
	{
		var ftpLimit = "${ftpSource.ftpLimit}";
		if(ftpLimit=='N' || ftpLimit=="")
		{
			disabledInput();
		}
	}
</script>
<table width="890px;">

	<tr>
		<input id="ftpId" name = "ftpId" type="hidden" value="${ftpSource.ftpId}"></input>
		<td width="80px;">FTP服务器地址：</td>
		<td width="187px;"><input id="ftpIP" name="ftpIP" <c:choose><c:when test="${not empty ftpSource.ftpIP}">value="${ftpSource.ftpIP}"</c:when><c:otherwise>value=""</c:otherwise></c:choose>  data-options="required:true" class="easyui-validatebox" style="width: 187px;"></input></td>
		<td width="90px;">端口号：</td>
		<td width="187px;"><input id="ftpPort" name="ftpPort" <c:choose><c:when test="${not empty ftpSource.ftpPort}">value="${ftpSource.ftpPort}"</c:when><c:otherwise>value="21"</c:otherwise></c:choose>  data-options="required:true" class="easyui-validatebox" style="width: 187px;"></input></td>
	</tr>
	<tr>
		<td width="80px;">用户名：</td>
		<td width="187px;"><input id="ftpUserName" name="ftpUserName"  <c:choose><c:when test="${not empty ftpSource.ftpUserName}">value="${ftpSource.ftpUserName}"</c:when><c:otherwise>value=""</c:otherwise></c:choose>   data-options="required:false" class="easyui-validatebox" style="width: 187px;"></input></td>
		<td width="110px;">密码：</td>
		<td width="187px;"><input id="ftpPassword" name="ftpPassword" type="password" <c:choose><c:when test="${not empty ftpSource.ftpPassword}">value="${ftpSource.ftpPassword}"</c:when><c:otherwise>value=""</c:otherwise></c:choose>   data-options="required:false" class="easyui-validatebox" style="width: 187px;"></input></td>
	</tr>
	<tr>
		<td width="80px;">FTP类型：</td>
		<td width="195px;" >
			<select id="ftpType" name="ftpType"  value="${ftpSource.ftpType}" placeholder="请输入FTP类型" class="easyui-combobox" data-options="required:true,valueField:'id',textField:'ftpTypeName',panelHeight:80,editable:false"  maxlength="50" style="width:195px;height:22px;"></select>
		</td>
		<td >字符集：</td>
<%-- 		<input id="charset" name="charset" <c:choose><c:when test="${not empty ftpSource.charset}">value="${ftpSource.charset}"</c:when><c:otherwise>value="GBK"</c:otherwise></c:choose>  data-options="required:true" class="easyui-validatebox" style="width: 50px;"></input></td> --%>
		<td  width="195px;"><select id="charset" class="easyui-combobox" name="charset" style="width: 195px;" data-options="panelHeight:120,editable:false">
			</select></td>
		<td ><a href="#" class="easyui-linkbutton" onclick="testFtpConnection()" data-options="align:'right',iconCls:'icon-hamburg-settings'">测试连接</a>
		<a href="#" class="easyui-linkbutton" onclick="testFtpShow()" data-options="align:'right',iconCls:'icon-hamburg-settings'">预览</a></td>
		<td><span id="ftp_connstatus" style="color: green"></span></td>
	</tr>
	<tr>
		<td width="100px;">FTP路径:</td>
		<td width="500px;" colspan="4">
<%-- 			<input type="text" id="ftpPath" name="ftpPath"  value="${ftpSource.ftpPath}" placeholder="请输入FTP路径" class="easyui-validatebox span2" data-options="required:true" value="" maxlength="5000" style="width:500px;height:22px;"> --%>
			<textarea id="ftpPath" name="ftpPath" rows="3" cols="82" value="${ftpSource.ftpPath}" placeholder="请输入FTP路径" class="easyui-validatebox span2" data-options="required:true" value="" maxlength="5000" style="width:500px;height:100px;">${ftpSource.ftpPath}</textarea>
		</td>
	</tr>
	<tr>
		<td width="100px;">文件(目录)名匹配：</td>
		<td width="500px;" colspan="3">
			<input type="text" id="ftpFileName" name="ftpFileName"  value="${ftpSource.ftpFileName}" placeholder="请输入文件(目录)名匹配" class="easyui-validatebox span2" data-options="required:true" value="" maxlength="5000" style="width:500px;height:22px;">
		</td>
		<td><input type="checkbox" id="exclusion" name="exclusion" <c:choose><c:when test="${not empty ftpSource.exclusion}">value="${ftpSource.exclusion}" <c:if test="${ftpSource.exclusion=='Y'}"> checked="checked" </c:if></c:when><c:otherwise>value="N"</c:otherwise></c:choose>  value="N" onclick="setValue()">排除</input></td>
	</tr>
	<tr>
		<td width="80px;">下载线程数：</td>
		<td width="187px">
			<select id="ftpDownloadThreadNum" class="easyui-combobox" name="ftpDownloadThreadNum" style="width: 187px;" data-options="panelHeight:120,editable:false">
			</select>
		</td>
		<td width="90px;">数据包大小：</td>
		<td width="187px"><input validType="checkNum" id="ftpPacketSize" name="ftpPacketSize" <c:choose><c:when test="${not empty ftpSource.ftpPacketSize}">value="${ftpSource.ftpPacketSize}"</c:when><c:otherwise>value="5000"</c:otherwise></c:choose>   data-options="required:true" class="easyui-validatebox" style="width: 187px;"></input></td>
		<td>（字节数kb）</td>
	</tr>
	<tr>
		<td width="80px;">文件大小范围：</td>
		<td width="387px" align="left" colspan="3">
			<input type="radio" name="ftpLimit" <c:if test="${ftpSource.ftpLimit == 'Y'}"> checked="checked" </c:if>  value="Y" onclick="enabledInput()">限制</input>
			<input type="radio" name="ftpLimit" <c:if test="${ftpSource.ftpLimit == 'N'}"> checked="checked" </c:if> <c:if test="${empty ftpSource.ftpLimit}"> checked="checked" </c:if>  value="N" onclick="disabledInput()">不限制</input>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input validType="checkNum" id="ftpLimitStart" name="ftpLimitStart" maxlength="7" value="${ftpSource.ftpLimitStart }"  data-options="required:true" class="easyui-validatebox" style="width: 90px;" <c:choose><c:when test="${ftpSource.ftpLimit == 'Y'}"> </c:when> <c:otherwise>disabled="true"></c:otherwise></c:choose></input>kb
		至&nbsp;&nbsp;<input validType="checkNum" id="ftpLimitEnd" name="ftpLimitEnd" maxlength="7" value="${ftpSource.ftpLimitEnd }"  data-options="required:true" class="easyui-validatebox" style="width: 90px;" <c:choose><c:when test="${ftpSource.ftpLimit == 'Y'}"> </c:when> <c:otherwise>disabled="true"></c:otherwise></c:choose></input>kb</td>
	</tr>
	<tr>
		<td width="80px;">是否采集子目录：</td>
		<td width="5px;" align="left">
			<input type="radio" name="ftpIsSubDir" <c:if test="${ftpSource.ftpIsSubDir == 'Y'}"> checked="checked" </c:if>  <c:if test="${empty ftpSource.ftpIsSubDir}"> checked="checked" </c:if> value="Y" onclick="enabledButton()">是</input>
			<input type="radio" name="ftpIsSubDir" <c:if test="${ftpSource.ftpIsSubDir == 'N'}"> checked="checked" </c:if>  value="N">否</input>
		</td>
		
	</tr>
</table>


<div id="dlg" class="easyui-dialog" title="预览" style="width:600px;height:400px;padding:10px"
            data-options="modal : true,
                iconCls: 'icon-save',
                buttons: '#dlg-buttons'
             ">
        
    </div>
    <div id="dlg-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="javacript:onGetFtpPath()">确认</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:$('#dlg').dialog('close')">取消</a>
    </div>

<div id="preview"></div>


