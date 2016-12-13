<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script>

	$(function(){
		$('#dlg').dialog('close');
		//disabledButton();
		getDownloader();
		getHttpMethod();
		initFetchThreadDatas();
		initCharsetDatas();
	});

	//启使用户的输入框
	function enabledInput()
	{
		$("#userName").attr("disabled",false);
		$("#password").attr("disabled",false);
	}
	
	//禁用用户的输入框
	function disabledInput()
	{
		$("#userName").attr("disabled",true);
		$("#password").attr("disabled",true);
		$("#userName").val("");
		$("#password").val("");
	}
	
	//启使用户的按钮
	function enabledButton()
	{
		$('#addButton').linkbutton('enable');
		$('#delButton').linkbutton('enable');
	}
	//禁用用户的按钮
	function disabledButton()
	{
		$('#addButton').linkbutton('disable');
		$('#delButton').linkbutton('disable');
	}
	//弹出代理服务器的列表
	function proxyServerListFun()
	{
		$('#dlg').dialog('open');
		$('#dlg').dialog('refresh', '${pageContext.request.contextPath}/proxyServerController/manager');
	}
	
	function insertDataToProxyServerList()
	{
		var isValid = $('#dataGrids').form('validateRow',editIndexProxy);
		if(isValid)
		{
			var dgn = $('#dataGrids');
	    	var dgy = $('#dataGridY');
	    	var rows = dgn.datagrid('getChecked');
	    	if(rows.length>0)
	    	{
	    		moveRowToHave(dgn,dgy);
	    		$('#dlg').dialog('close');
	    	}
	    	else
	    	{
	    		$.messager.alert('提示','请选择需要增加代理服务','info');
	    	}
		}
	}

	
	//移一行数据到已经选择的datagrid中
	function moveRowToHave(dgn,dgy){
		var rows = dgn.datagrid('getChecked');
		var rowsy = dgy.datagrid('getRows');
		for(var i=0; i<rows.length; i++){
			var row = rows[i];
			var flag = true;
			for(var j=0;j<rowsy.length;j++)
			{
				if(row.id==rowsy[j].id)
				{
					flag = false;
					break;
				}
			}
			if(flag)
			{
				dgy.datagrid('appendRow',row);
			}
			//dgn.datagrid('deleteRow', dgn.datagrid('getRowIndex',row));
		}
		getProxyIds();
	}
	
	
	function removeproxyServerListFun()
	{

    	var dgy = $('#dataGridY');
    	var rows = dgy.datagrid('getChecked');
    	if(rows.length>0)
    	{
    		for(var i=0; i<rows.length; i++){
    			var row = rows[i];
    			dgy.datagrid('deleteRow', dgy.datagrid('getRowIndex',row));
    		}
    		getProxyIds();
    	}
    	else
    	{
    		$.messager.alert('提示','请选择需要移除的代理服务','info');
    	}
	}
	
	function getProxyIds()
	{
		//把已经存进来的代理放在表单里面
		var dgy = $('#dataGridY');
		$("#proxyServerIds").val('');
		var ids = [];
		var rows = dgy.datagrid('getRows');
		for(var i=0;i<rows.length;i++)
		{
			ids.push(rows[i].id);
		}
		$("#proxyServerIds").val(ids.join(','));
	}
	
	//获取下载器数据
	function getDownloader()
	{
		var url = '${ctx}/webController/getWebDownloaderList';
		$("#downloaderId").combobox('reload',url);
		
		if('${webSource.downloaderId}')
		{
			$("#downloaderId").combobox('setValue','${webSource.downloaderId}');
		}
		else
		{
			$("#downloaderId").combobox('setValue','1');
		}
	}
	var httpMethod_data =[
	                      {'httpMethod':'GET','httpMethodName':'GET'},
	                      {'httpMethod':'POST','httpMethodName':'POST'}
	                      ]
	//获取请求方法
	function getHttpMethod()
	{
		//$("#downloaderId").combobox('reload',url);
		
		if('${webSource.httpMethod}')
		{
			$("#httpMethod").combobox('setValue','${webSource.httpMethod}');
		}
		else
		{
			$("#httpMethod").combobox('setValue','GET');
		}
	}

	//保存代理
	function saveProxy()
	{
		
	}
	
	function initFetchThreadDatas()
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
		$("#fetchThreadNum").combobox('loadData',threadDatas);
		
		if('${webSource.fetchThreadNum}')
		{
			$("#fetchThreadNum").combobox('setValue','${webSource.fetchThreadNum}');
		}
		else
		{
			$("#fetchThreadNum").combobox('setValue','1');
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
	
</script>
<table width="890px;">
	<tr>
		<input id="webId" name = "webId" type="hidden" value="${webSource.id}"></input>
		<td width="100px;">web目标url：</td>
		<td width="500px;" colspan="4">
			<textarea id="url" name="url" rows="3" cols="82" value="${webSource.url}" placeholder="请输入web目标url" class="easyui-validatebox span2" data-options="required:true" value="" maxlength="5000" style="width:500px;height:100px;">${webSource.url}</textarea>
		</td>
	</tr>
	<tr>
		<td width="100px;">下载器：</td>
		<td width="500px;" colspan="4">
			<select id="downloaderId" name="downloaderId"  value="${webSource.downloaderId}" placeholder="请输入下载器" class="easyui-combobox" data-options="required:true,valueField:'downloaderId',textField:'downloaderName'" readonly="readonly"  maxlength="50" style="width:508px;height:22px;"></select>
		</td>
	</tr>
	<tr>
		<td width="80px;">站点字符集：</td>
		<td width="195px;">
<%-- 		<input id="charset" name="charset" <c:choose><c:when test="${not empty webSource.charset}">value="${webSource.charset}"</c:when><c:otherwise>value="UTF-8"</c:otherwise></c:choose>  data-options="required:true" class="easyui-validatebox" style="width: 187px;"></input> --%>
			<select id="charset" class="easyui-combobox" name="charset" style="width: 195px;" data-options="panelHeight:120,editable:false">
		</td>
		<td width="90px;">请求超时时间：</td>
		<td width="187px;"><input validType="checkSecond" id="requestTimeout" name="requestTimeout" <c:choose><c:when test="${not empty webSource.requestTimeout}">value="${webSource.requestTimeout}"</c:when><c:otherwise>value="5"</c:otherwise></c:choose>  data-options="required:true" class="easyui-validatebox" style="width: 187px;"></input></td>
		<td>秒</td>
	</tr>
	<tr>
		<td width="80px;">出错重试次数：</td>
		<td width="187px;"><input validType="checkSecond" id="retryTime" name="retryTime"  <c:choose><c:when test="${not empty webSource.retryTime}">value="${webSource.retryTime}"</c:when><c:otherwise>value="5"</c:otherwise></c:choose>   data-options="required:true" class="easyui-validatebox" style="width: 187px;"></input></td>
		<td width="110px;">异步加载等待时间：</td>
		<td width="187px;"><input validType="checkSecond" id="loadWaitTime" name="loadWaitTime" <c:choose><c:when test="${not empty webSource.loadWaitTime}">value="${webSource.loadWaitTime}"</c:when><c:otherwise>value="5"</c:otherwise></c:choose>   data-options="required:true" class="easyui-validatebox" style="width: 187px;"></input></td>
		<td>秒</td>
	</tr>
	<tr>
		<td width="80px;">爬取线程数：</td>
		<td width="187px">
		<select id="fetchThreadNum" class="easyui-combobox" name="fetchThreadNum" style="width: 195px;" data-options="panelHeight:120,editable:false"></select>
<%-- 		<input validType="checkSecond" id="fetchThreadNum" name="fetchThreadNum" <c:choose><c:when test="${not empty webSource.fetchThreadNum}">value="${webSource.fetchThreadNum}"</c:when><c:otherwise>value="5"</c:otherwise></c:choose>  data-options="required:true" class="easyui-validatebox" style="width: 187px;"></input></td> --%>
		<td width="90px;">爬取间隔时间：</td>
		<td width="187px"><input validType="checkSecond" id="fetchIntervalTime" name="fetchIntervalTime" <c:choose><c:when test="${not empty webSource.fetchIntervalTime}">value="${webSource.fetchIntervalTime}"</c:when><c:otherwise>value="0"</c:otherwise></c:choose>   data-options="required:true" class="easyui-validatebox" style="width: 187px;"></input></td>
		<td>秒</td>
	</tr>
	<tr>
		<td width="80px;">是否需要登录：</td>
		<td width="187px" colspan="1" align="left">
			<input type="radio" name="needLogin" <c:if test="${webSource.needLogin == 'Y'}"> checked="checked" </c:if>  value="Y" onclick="enabledInput()">是</input>
			<input type="radio" name="needLogin" <c:if test="${webSource.needLogin == 'N'}"> checked="checked" </c:if> <c:if test="${empty webSource.needLogin}"> checked="checked" </c:if>  value="N" onclick="disabledInput()">否</input>
		</td>
		<td width="80px;">请求方法：</td>
		<td width="187px">
			<select id="httpMethod" name="httpMethod"  value="${webSource.httpMethod}" placeholder="请输入请求方法" class="easyui-combobox" data-options="required:true,valueField:'httpMethod',textField:'httpMethodName',data:httpMethod_data,panelHeight:120,editable:false"  maxlength="50" style="width:195px;height:22px;"></select>
		</td>
	</tr>
	<tr>
		<td width="80px;">用户名：</td>
		<td width="187px"><input id="userName" name="userName" value="${webSource.userName }"  data-options="required:false" class="easyui-textbox" style="width: 188px;" <c:choose><c:when test="${webSource.needLogin == 'Y'}"> </c:when> <c:otherwise>disabled="true"></c:otherwise></c:choose></input></td>
		<td width="90px;" align="right">密码：</td>
		<td width="187px"><input id="password" name="password" value="${webSource.password }"  data-options="required:false" class="easyui-textbox" style="width: 188px;" <c:choose><c:when test="${webSource.needLogin == 'Y'}"> </c:when> <c:otherwise>disabled="true"></c:otherwise></c:choose></input></td>
		<td></td>
	</tr>
	<tr>
		<td width="80px;">是否使用代理：</td>
		<td width="5px;" align="left">
			<input type="radio" name="needProxy" <c:if test="${webSource.needProxy == 'Y'}"> checked="checked" </c:if>  value="Y" onclick="enabledButton()">是</input>
			<input type="radio" name="needProxy" <c:if test="${webSource.needProxy == 'N'}"> checked="checked" </c:if> <c:if test="${empty webSource.needProxy}"> checked="checked" </c:if>  value="N" onclick="disabledButton()">否</input>
			<input type="hidden" id="proxyServerIds" name="proxyServerIds" value="${proxyIds}"/>
		</td>
		<td colspan="2" align="right">
			<a href="#" id="addButton" class="easyui-linkbutton" data-options="align:'right',iconCls:'icon-standard-add'" onclick="proxyServerListFun();" <c:choose><c:when test="${webSource.needProxy == 'Y'}"></c:when><c:otherwise>disabled="true"</c:otherwise></c:choose>>选择代理</a>
			<a href="#" id="delButton" class="easyui-linkbutton" data-options="align:'right',iconCls:'icon-standard-delete'" onclick="removeproxyServerListFun();" <c:choose><c:when test="${webSource.needProxy == 'Y'}"></c:when><c:otherwise>disabled="true"</c:otherwise></c:choose> >移除代理</a>
		</td>
	</tr>
	<tr>
		<td></td>
		<td colspan="4">
			<table id="dataGridY" class="easyui-datagrid" data-options="fitColumns:true,url:'${pageContext.request.contextPath}/webController/getProxyServer?proxyIds=${proxyIds}'" style="width:507px;height:150px">
		        <thead>
		            <tr>
		                <th data-options="field:'id',checkbox:true">选择</th>
		                <th data-options="field:'ip',width:150,align:'center',">服务器IP地址</th>
		                <th data-options="field:'port',width:50,align:'center'">端口</th>
		                <th data-options="field:'source',width:100,align:'center'">来源</th>
		                <th data-options="field:'type',width:50,align:'center'">类型</th>
		            </tr>
		        </thead>
			</table>
		</td>
	</tr>
</table>




<div id="dlg" class="easyui-dialog" title="代理服务器列表" style="width:800px;height:400px;padding:10px"
            data-options="modal : true,
                iconCls: 'icon-save',
                buttons: '#dlg-buttons',
                toolbar:'#tb'
            ">
        
    </div>
    <div id="dlg-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:insertDataToProxyServerList()">确认</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:$('#dlg').dialog('close')">取消</a>
    </div>
    
    	<div id="tb" style="padding:5px;height:auto">
		<div>
    		<a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-add" onclick="appendProxy()">添加</a>
    		<span class="toolbar-item dialog-tool-separator"></span>
	        <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-remove" onclick="deleteProxy()">删除</a>
<!--         	<span class="toolbar-item dialog-tool-separator"></span> -->
<!--         	<a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-edit" onclick="saveProxy()">保存</a> -->
    	</div>
	</div>