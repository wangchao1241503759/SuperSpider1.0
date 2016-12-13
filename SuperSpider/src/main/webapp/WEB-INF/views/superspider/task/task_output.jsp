<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<table  width="890px;">
		<tr>
			<td width="90px;">输出类型:</td>
			<td width="210px;">
				<input id="topId" name = "topId" type="hidden" value="${taskOutput.topId}"></input>
				<select id="topType" name="topType" style="width: 208px" data-options="panelHeight:'auto',required:true">
					<option value="mysql">MySQL</option>
					<option value="sqlserver">SQLServer</option>
					<option value="oracle">Oracle</option>
					<option value="db2">DB2</option>
					<option value="mongo">MongoDB</option>
<!-- 					<option value="mss">元数据仓储3.1</option> -->
					<option value="mss_http">元数据仓储4.0</option>
				</select>
			</td>
			<td width="80px;">服务器地址：</td>
			<td width="200px;"><input id="topHost" name="topHost" data-options="required:true"  class="easyui-validatebox" style="width: 200px;" value="${taskOutput.topHost}"></input></td>
			<td width="80px;"></td>
			<td width="150px;"></td>
		</tr>
		<tr>
			<td>端口号：</td>
			<td><input id="topPort" name="topPort" data-options="required:true"  class="easyui-validatebox" style="width: 200px;" value="${taskOutput.topPort}"></input></td>
			<td>用户名：</td>
			<td><input id="topUserName" name="topUserName"    class="easyui-validatebox" style="width: 200px;" value="${taskOutput.topUserName}"></input></td>
		</tr>
		<tr>
			<td>密码：</td>
			<td><input id="topPassWord" name="topPassWord"   type="password"  class="easyui-validatebox" style="width: 200px;" value="${taskOutput.topPassWord}"></input></td>
			<td>数据库名称：</td>
			<td ><input id="topDbName" name="topDbName"  data-options="required:true"  class="easyui-validatebox" style="width: 200px;" value="${taskOutput.topDbName}"></input></td>
			<td ><a href="#" class="easyui-linkbutton" onclick="loadOutputTableColumns()" data-options="align:'right',iconCls:'icon-hamburg-settings'">测试连接</a></td>
			<td><span id="out_connstatus" style="color: green"></span></td>
		</tr>
		<tr>
			<td>数据表：</td>
			<td>
				<select id="topTableName" class="easyui-combobox" name="topTableName" style="width: 208px;" data-options="panelHeight:120">
				</select>
			</td>
			<td>输出线程数：</td>
			<td>
				<select id="topThread" class="easyui-combobox" name="topThread" style="width: 208px;" data-options="panelHeight:120,editable:false">
				</select>
			</td>
		</tr>
		<tr>
			<td>清空目标表：</td>
			<td colspan="1">
				<table>
					<tr>
						<td><input type="radio" name="topIsClear" <c:if test="${taskOutput.topIsClear == 0 || taskOutput.topIsClear == null}"> checked="checked" </c:if> value="0"/><span>否</span><br /></td>
						<td><input type="radio" name="topIsClear" <c:if test="${taskOutput.topIsClear == 1}"> checked="checked" </c:if> value="1"/><span>是</span><br /></td>
					</tr>
				</table>
			</td>
			<td>模式(schema)</td>
			<td colspan="1">
				<input id="topSchema" name="topSchema"   class="easyui-validatebox" style="width: 200px;" value="${taskOutput.topSchema}"></input>
			</td>
			<td style="display: none;">元数据库类型</td>
			<td style="display: none;">
				<select id="topMssType" class="easyui-combobox" name="topMssType" style="width: 208px;" data-options="panelHeight:120">
					<option value="MySQL">MySQL</option>
					<option value="ORACLE">ORACLE</option>
					<option value="MS SQL Server">MS SQL Server</option>
					<option value="IBM DB2">IBM DB2</option>
					<option value="Mongo">Mongo</option>
				</select>
			</td>
		</tr>
	</table>
<script>
var tableDatas = [];
$(function(){
	initOutThreadDatas();//初始化线程数;
	if('${taskOutput.topThread}')
	{
		$("#topThread").combobox("setValue",'${taskOutput.topThread}');
	}else{
		$("#topThread").combobox("setValue",1);
	}
	$("#topTableName").combobox({
		onChange : function(newValue, oldValue) {
			var oldTopTableName = $("#topTableName").combobox("getValue");
			var newTopTableName = "";
			if(typeof(newValue) == "undefined")
			{
				newTopTableName = oldValue;
				$("#topTableName").combobox("setValue",oldValue);
			}else if(newValue){
				newTopTableName = newValue;
				$("#topTableName").combobox("setValue",newValue);
				//初始化字段基本信息列表
// 				if($("#taskType").val()=="db" || $("#taskType").val()=="ftp" || $("#taskType").val()=="file")
// 				{
					initOutputColumn();
// 				}
			}
			/* alert(oldValue +"========="+newTopTableName);
			if(oldTopTableName != newTopTableName)
			clearTargetFieldType(); */
		}
	});
	//目标数据库改变
	$("#topType").combobox({
		onChange : function(newValue, oldValue) {
			if (newValue == "mss" || newValue == "mss_http") {
				$("#topMssType").parents("td:first").prev().show();
				$("#topMssType").parents("td:first").show();
				
				$("#topSchema").parents("td:first").prev().hide();
				$("#topSchema").parents("td:first").hide();
			} else {
				$("#topMssType").parents("td:first").prev().hide();
				$("#topMssType").parents("td:first").hide();
				
				$("#topSchema").parents("td:first").prev().show();
				$("#topSchema").parents("td:first").show();
			}
		}
	});
	if('${taskOutput.topMssType}'){
		$("#topMssType").combobox("setValue",'${taskOutput.topMssType}');
	}
	if('${taskOutput.topType}'){
		$("#topType").combobox("setValue",'${taskOutput.topType}');
	}
	var topId = '${taskOutput.topId}';
	if(topId){
		loadOutputTableColumns();
	}
	if('${taskOutput.topTableName}'){
		$("#topTableName").combobox("setValue",'${taskOutput.topTableName}');
	}
});

function clearTargetFieldType(){
	var rows = $('#fieldmapping').datagrid('getRows');
	var checkedRows = $('#fieldmapping').datagrid("getChecked");
	$("#mssMatchTips").text("");
	for(var i = 0 ; i < rows.length ; i++){
		var row = rows[i];
		row.targetFieldName = "";
		row.targetFieldType = "";
		$('#fieldmapping').datagrid('refreshRow',i);
	}
	for(var i = 0 ; i<checkedRows.length ; i++)
	{
		var checkedRowIndex = $('#fieldmapping').datagrid('getRowIndex',checkedRows[i]);
		$("#fieldmapping").datagrid("checkRow",checkedRowIndex);
	}
}

function initOutThreadDatas()
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
	$("#topThread").combobox('loadData',threadDatas);
}
function getOutputColumnData(data)
{
	var outPutColumns = [];
	for(var i=0 ;i<data.length;i++)
	{
		outPutColumns.push({
			fieldName:data[i].fieldName,
			fieldType:data[i].fieldType,
			fieldExp:data[i].fieldExp,
			filedSource:data[i].filedSource,
			targetFieldName:data[i].targetFieldName,
			targetFieldType:data[i].targetFieldType,
			fieldOrdinalPosition:data[i].fieldOrdinalPosition
		});
	}
	return outPutColumns;
}
function loadOutputTableColumns(){
	$.ajax({
		type:'post',
		data:$("#configForm").serialize(),
		url:"${pageContext.request.contextPath}/taskoutput/getOutputAllTables",
		timeout:3000,
		success: function(data){
			var topTableObj = $("#topTableName");
			var topTableName = '${taskOutput.topTableName}';
			var oldTopDbName = '${taskOutput.topDbName}';
			var topDbName = $("#topDbName").val();
			topTableObj.combobox('clear').combobox('setValue','');
			tableDatas = [];
			if(data && typeof(data) == "object"){
				var firstValue = "";
				if(data['status'] == '1')
				{
					$("#out_connstatus").css({color:'green'}).text("连接成功!");
					for(var key in data)
					{
						if(key != "status")//过滤标识连接状态的key值;
						{
							if(!firstValue)
								firstValue = data[key];
							tableDatas.push({text:key,value:data[key]});
						}
					}
					topTableObj.combobox('loadData', tableDatas);
					if(topTableName && oldTopDbName == topDbName){
						topTableObj.combobox('setText',topTableName);
						topTableObj.combobox('setValue',topTableName);
					}else{
						topTableObj.combobox('setText',firstValue);
						topTableObj.combobox('setValue', firstValue);
					}
				}else{
					topTableObj.combobox('clear');
					$("#out_connstatus").css({color:'red'}).text("连接失败,请检查参数配置!");
				}
			}
		},error:function (XMLHttpRequest, textStatus, errorThrown) {
			$("#topTableName").combobox('clear');
			if(textStatus == "timeout"){
				$("#out_connstatus").css({color:'red'}).text("连接超时,请检查参数配置!");
			}else{
				$("#out_connstatus").css({color:'red'}).text("连接失败,请检查参数配置!");
			}
        }
	});
}
</script>