<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<table width="890px;">
	<tr>
		<td width="90px;">数据库类型:</td>
		<td width="210px;">
			<input id="dsId" name = "dsId" type="hidden" value="${dbSource.dsId}"></input>
			<select id="source_dbtype"  name="dsType" style="width: 208px;" data-options="panelHeight:'auto',required:true">
				<option value="mysql">MySQL</option>
				<option value="sqlserver">SQL Server</option>
				<option value="oracle">Oracle</option>
				<option value="db2">DB2</option>
				<option value="mongo">MongoDB</option>
				<option value="trs">TRS</option>
				<option value="arcgis">Arcgis</option>
			</select>
		</td>
		<td width="80px;">服务器地址：</td>
		<td width="200px;"><input id="dsHost" name="dsHost"  data-options="required:true" class="easyui-validatebox" style="width: 200px;" value="${dbSource.dsHost}"></input></td>
		<td width="80px;"></td>
		<td width="150px;"></td>
	</tr>
	<tr>
		<td>端口号：</td>
		<td><input id="dsPort" name="dsPort"  data-options="required:true" class="easyui-validatebox" style="width: 200px;" value="${dbSource.dsPort}"></input></td>
		<td>用户名：</td>
		<td><input id="dsUserName" name="dsUserName"   class="easyui-validatebox" style="width: 200px;" value="${dbSource.dsUserName}"></input></td>
	</tr>
	<tr>
		<td>密码：</td>
		<td><input id="dsPassWord" name="dsPassWord" type="password"  class="easyui-validatebox" style="width: 200px;" value="${dbSource.dsPassWord}"></input></td>
		<td valign="middle" id="dbNameStr">数据库名称：</td>
		<td ><input id="dsDbName" name="dsDbName"  data-options="required:true"  class="easyui-validatebox" style="width: 200px;" value="${dbSource.dsDbName}"></input></td>
		<td><a href="#" class="easyui-linkbutton" onclick="testDsConnection()" data-options="iconCls:'icon-hamburg-settings'">测试连接</a></td>
		<td><span id="connstatus" style="color: green"></span></td>
	</tr>
	<tr>
		<td>模式(schema)</td>
		<td>
			<input id="dsSchema" name="dsSchema"  class="easyui-textbox" style="width: 205px;" value="${dbSource.dsSchema}"></input>
		</td>
		<td>数据包大小：</td>
		<td><input id="dsFetchSize" name="dsFetchSize" class="easyui-textbox" style="width: 205px;" value="${dbSource.dsFetchSize}"></input></td>
		<td>（记录数/条）</td>
	</tr>
	<tr>
		<td>获取方式：</td>
		<td align="left">
			<input type="radio" name="dsTableType" <c:if test="${dbSource.dsTableType == 0}"> checked="checked" </c:if> <c:if test="${dbSource == null}"> checked="checked" </c:if> value="0" onclick="selectSourceData()"/><span>选择表</span>
			<input type="radio" name="dsTableType" <c:if test="${dbSource.dsTableType == 1}"> checked="checked" </c:if>  value="1" onclick="selectSourceData()"/><span>编写sql</span>
		</td>
		<td>爬取间隔：</td>
		<td>
			<input id="dsThread" name="dsThread" onblur="validThread(this);" class="easyui-textbox" style="width: 205px;" value="${dbSource.dsThread}"></input>
		</td>
		<td>（秒）</td>
	</tr>
	<tr id="dsTableNameTr">
		<td>数据表：</td>
		<td>
			<select id="dsTableName"  class="easyui-combobox" name="dsTableName" style="width: 209px;" data-options="panelHeight:120,editable:false">
			</select>
		</td>
	</tr>
	<tr id="dsSqlTr" style="height: 10px;">
		<td>sql语句：</td>
		<td colspan="3">
			<div style="width: 100px;">
				<textarea id="dsSql" name="dsSql" rows="4" cols="87">${dbSource.dsSql}</textarea>
			</div>
		</td>
		<td><a href="#" class="easyui-linkbutton" onclick="testSQLConnection()" data-options="iconCls:'icon-standard-script'">测试SQL</a></td>
	</tr>
	<tr>
		<td></td>
		<td colspan="4"><span id="sqlconnstatus" style="color: green"></span></td>
	</tr>
</table>
<script>
var source_dbType;
var source_field_types;
$(function(){
	if (Sys.ie){
		$("#dsSql").attr("cols","87");
	}else if(Sys.chrome){
		$("#dsSql").attr("cols","83");
	}
	$("#source_dbtype").combobox({
		onChange : function(newValue, oldValue) {
			if(newValue == "db2")
			{
				//$('#fieldmapping').datagrid('loadData', { total: 0, rows: [] });
			}else{
				//loadFieldMappingGrid(newValue);
			}
			if(newValue!="" && $("#taskId").val()=="")
			{
				checkDb(newValue,"add");
			}
			if(newValue!="" && $("#taskId").val()!="")
			{
				checkDb(newValue,"edit");
			}
		}
	
	});
	$("#source_dbtype").combobox('setValue','');
	
	function checkDb(dbType,operation)
	{
		$.ajax({
			type : 'post',
			url : "${ctx}/license/checkDbTypeLicense",
			async:true,
			data:{dbType:dbType,operation:operation},
			dataType:'json',
			success : function(result) {
		    	if(result.success)
				{
		    		
				}
		    	else
		    	{
		    		$.messager.alert('提示',result.msg,'info');
		    		$("#source_dbtype").combobox('setValue','');
		    	}
			}

		});

	}
	selectSourceData();
	if('${dbSource.dsThread}')
	{
		$("#dsThread").val('${dbSource.dsThread}');
	}else{
		$("#dsThread").val(0);
	}
	if('${dbSource.dsType}'){
		$("#source_dbtype").combobox("setValue",'${dbSource.dsType}');
	}
	source_dbType = $("#source_dbtype").combobox("getValue");
	var dsId = '${dbSource.dsId}';
	var dsTableType = '${dbSource.dsTableType}';
	if(dsTableType == 1){
		testSQLConnection();
	}else if(dsId){
		testDsConnection();
	}
	$("#dsTableName").combobox({
		onChange : function(newValue, oldValue) {
			if(newValue == "")
			{
				$('#fieldmapping').datagrid('loadData', { total: 0, rows: [] });
			}else{
				$("#matchTips").text("");
				$("#mssMatchTips").text("");
				loadFieldMappingGrid(newValue);
			}
		}
	});
});

function validThread(obj)
{
	var value = $(obj).val();
	if(!isfloat(value))
	{
		$(obj).val('${dbSource.dsThread}');
	}
}
//验证数字(整数、浮点数都可以通过)
function isfloat(oNum){
	 if(!oNum) return false;
	 var strP=/^(-?\d+)(\.\d+)?$/;
	 if(!strP.test(oNum)) return false;
	 try{
	  	if(parseFloat(oNum)!=oNum) return false;
	 }catch(ex){
	   return false;
	 }
	 return true;
}
function testDsConnection(){
	var dsType = $("#source_dbtype").combobox('getValue');
	var dsHost = $("#dsHost").val();
	var dsPort = $("#dsPort").val();
	var dsUserName = $("#dsUserName").val();
	var dsPassWord = $("#dsPassWord").val();
	var dsDbName = $("#dsDbName").val();
	if(dsType == "mongo")
	{
		if(dsHost && dsPort && dsDbName){
			loadTableNames();
		}else{
			 $("#connstatus").css({color:'red'}).text("连接失败,请检查参数配置!");
		}
	}else{
		if(dsType && dsHost && dsPort && dsUserName && dsPassWord && dsDbName){
			loadTableNames();
		}else{
			$("#connstatus").css({color:'red'}).text("连接失败,请检查参数配置!");
		}
	}
}
function loadTableNames()
{
	var dsTableName = '${dbSource.dsTableName}';
	var oldDsDbName = '${dbSource.dsDbName}';
	var dsDbName = $("#dsDbName").val();
	$.ajax({
		type:'post',
		timeout:30000,
		data:$("#configForm").serialize(),
		url:"${pageContext.request.contextPath}/db/getAllTables",
		success: function(data){
			var dsTableObj = $("#dsTableName");
			var tablenames = data['tablenames'];
			source_field_types = data['fieldtypes'];
			if(tablenames){
				$("#connstatus").css({color:'green'}).text("连接成功!");
				var tableDatas = "[";
				for(var i=0,j=tablenames.length;i<j;i++){
					tableDatas +="{text:'"+tablenames[i]+"',value:'"+tablenames[i]+"'}";
					if(i<j-1)
					{
						tableDatas +=",";
					}
				}
				tableDatas +="]";
				tableDatas = eval(tableDatas);
				dsTableObj.combobox('loadData', tableDatas);
				if(dsTableName && oldDsDbName == dsDbName){
					dsTableObj.combobox('setText',dsTableName);
					dsTableObj.combobox('setValue',dsTableName);
				}else{
					dsTableObj.combobox('setText',tablenames[0]);
					dsTableObj.combobox('setValue', tablenames[0]);
				}
			}else{
				 dsTableObj.combobox('setText','');
				 dsTableObj.combobox('setValue','');
				 dsTableObj.combobox('loadData', [{}]);
				 $("#connstatus").css({color:'red'}).text("连接失败,请检查参数配置!");
			}
		},error:function (XMLHttpRequest, textStatus, errorThrown) {
			if(textStatus == "timeout"){
				$("#connstatus").css({color:'red'}).text("连接超时,请检查参数配置!");
			}else{
				 $("#connstatus").css({color:'red'}).text("连接失败,请检查参数配置!");
			}
        }
	});
}
function selectSourceData(){
	if($("input[name='dsTableType']:checked").val()=="0"){
		$('#dsTableNameTr').show();
		$('#dsSqlTr').hide();
	}else{
		$('#dsTableNameTr').hide();
		$('#dsSqlTr').show();
	}
}


function testSQLConnection()
{
	var dsSql = $("#dsSql").val();
	if(dsSql!="" && dsSql!=null)
	{
		
		loadFieldMappingGridBySQL(dsSql);
	}
	else
	{
		$("#sqlconnstatus").css({color:'red'}).text("连接失败,请检查参数配置！");
	}
}
</script>