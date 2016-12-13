<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="fieldmappingTabDiv" class="easyui-tabs" data-options="fit:true,border:false" style="height: 400px;">
	<div title="字段转换与映射">
		<div id="fieldmappingtb" style="padding: 5px; height: auto">
			<div>
				<a href="#" class="easyui-linkbutton" onclick="appendfiledMappingRow();" data-options="align:'right',iconCls:'icon-standard-add'">添加转换字段</a>
				<a href="#" class="easyui-linkbutton" onclick="fieldMappingSameNameMatch();" data-options="align:'right',iconCls:'icon-hamburg-exchange'">同名自动匹配</a>
				<input type="hidden" id="primaryFieldMapping" >
				<span style="color: green" id="matchTips"></span>
				<span style="color: red" id="mssMatchTips"></span>
			</div>
		</div>
		<table id="fieldmapping" style="height: 339px;" class="easyui-datagrid" data-options="onClickCell:onFieldMappingClickCell,onBeforeEdit:onBeforeEdit,onAfterEdit:onAfterEdit,enableHeaderClickMenu:false,enableHeaderContextMenu:false,enableRowContextMenu:false,toolbar:'#fieldmappingtb',border : false,checkOnSelect:false,selectOnCheck:true,striped:true,rownumbers:true,singleSelect:false,fitColumns : true,onClickRow:onClickRow,onDblClickRow: onDblClickRow,onCheck:validMssFieldMapping,onUncheck:validMssFieldMapping,onUncheckAll:validMssFieldMapping,onCheckAll:validMssFieldMapping,fit:true" >
			<thead>
				<tr>
					<th data-options="field:'fieldID',title:'字段ID',checkbox:true">字段ID</th>
					<th data-options="field:'fieldName',title:'字段名称',width:10">字段名称</th>
					<th data-options="field:'fieldType',title:'字段类型',width:10">字段类型</th>
					<th data-options="field:'fieldExp',title:'字段转换表达式',width:30,editor:{type:'text'}">字段转换表达式</th>
					<th data-options="field:'filedSource',title:'字段来源',width:5">字段来源</th>
					<th data-options="field:'targetFieldName',title:'目标字段名称',width:10,editor:{type:'combobox',options:{valueField:'colvalue',textField:'colname',editable:false}}">目标字段名称</th>
					<th data-options="field:'targetFieldType',title:'目标字段类型',width:10">目标字段类型</th>
					<th data-options="field:'primaryField',title:'主键字段'">主键</th>
					<th data-options="field:'delField',formatter:delFieldFormatter">操作</th>
				</tr>
			</thead>
		</table>
	</div>
	<div title="文件字段内容提取">
		<div id="filetika" style="padding: 5px; height: auto">
			<div>
				文件字段类型：
				<select id="fileFieldType" class="easyui-combobox" name="fileFieldType" style="width: 100px;" data-options="panelHeight:'auto'">
					<option value="blob">大字段存储</option>
					<option value="ftppath">FTP路径</option>
				</select>
				扩展名字段：
				<select id="extensionField" class="easyui-combobox" name="extensionField" style="width: 150px;" data-options="panelHeight:120,editable:false"></select>
				内容字段（路径/二进制）：
				<select id="sourceFieldName" class="easyui-combobox" name="sourceFieldName" style="width: 150px;" data-options="panelHeight:120,editable:false"></select>
			</div>
			<div id="ftpconnect" style="padding-left:18px;display:none">
				<div style="height:3px"></div>
				FTP用户名：<input id="ftp_user" class="easyui-textbox" style="width: 136px;" value="" ></input>
				密码：<input id="ftp_password" class="easyui-textbox" style="width: 136px;" value=""></input>
				<a href="#" class="easyui-linkbutton" data-options="align:'right',iconCls:'icon-hamburg-settings'">测试连接</a>
			</div>
			<div style="padding-top: 5px;">
				<a href="#" class="easyui-linkbutton" data-options="align:'right',iconCls:'icon-standard-add'" onclick="tikaAttributeAdd();">添加提取属性</a>
				<a href="#" class="easyui-linkbutton" onclick="fileAttributeSameNameMatch();" data-options="align:'right',iconCls:'icon-hamburg-product'">同名自动匹配</a>
				<span style="color: green;" id="matchAttributeTips"></span>
			</div>
		</div>
		<table id="fileattr" class="easyui-datagrid" style="height: 325px;"  data-options="onBeforeEdit:onAttributeBeforeEdit,onAfterEdit:onAttributeAfterEdit,onDblClickRow: onArributeDblClickRow,onClickRow:onAttributeClickRow,enableHeaderClickMenu:false,enableHeaderContextMenu:false,enableRowContextMenu:false,toolbar:'#filetika',border : false,checkOnSelect:false,selectOnCheck:true,striped:true,rownumbers:true,singleSelect:false,fitColumns:true,fit:true">
			<thead>
				<tr>
					<th data-options="field:'fileAttributeId',checkbox:true"></th>
					<th data-options="field:'fileAttributeName',width:150">属性名称</th>
					<th data-options="field:'fileTypeValue',width:180">关联文件类型</th>
					<th data-options="field:'targetFieldName',width:150,editor:{type:'combobox',options:{valueField:'colvalue',textField:'colname',editable:false}}">目标字段名称</th>
					<th data-options="field:'targetFieldType',width:130">目标字段类型</th>
					<th data-options="field:'isDefault',hidden:true">是否默认属性</th>
					<th data-options="field:'delField',formatter:delAttributeFieldFormatter">操作</th>
				</tr>
			</thead>
		</table>
	</div>
	<div title="增量识别-触发器" style="padding:5px" >
		<div style="width:640px;">
			<table width="640px;" >
				<tr>
					<td width="90px;">触发器名称：</td>
					<td width="190px;"><input  class="easyui-validatebox" id="triggerName" name="triggerName" value="${trigger.triggerName}" style="width:210px" /></td>
					<td width="80px;">临时表名称：</td>
					<td width="210px;"><input  class="easyui-validatebox" id="triggerTableName" name="triggerTableName" value="${trigger.triggerTableName}" style="width:205px" /></td>
				</tr>
				<tr>
					<td>主键字段：</td><td><input id="primaryField" name="primaryField" value="${trigger.primaryField}" class="easyui-textbox" style="width:210px"/></td>
					<td>状态字段：</td><td><input id="statusField" name="statusField" value="${trigger.statusField}" class="easyui-textbox" style="width:205px"/></td>
				</tr>
				<tr><td colspan="4">触发器脚本：</td></tr>
				<tr>
					<td colspan="4">
					   <div style="width: 100px;">
							<textarea id="triggerScript" name="triggerScript" rows="10" cols="100" style="font-size: 12px;padding-top:30px; font-family: '微软雅黑'">${trigger.triggerScript}</textarea>
					   </div>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<%-- <input type="checkbox" id="isOpen" name="isOpen" onclick="setOpenStatus(this);" <c:if test="${trigger.isOpen == 1}"> checked="checked"  value="${trigger.isOpen}" </c:if> <c:if test="${trigger.isOpen != 1}">value="0" </c:if>/> 是否启用数据库增量采集--%>
						<input type="hidden" id="isOpen" name="isOpen" value="1"/>
						<input type="hidden" id="createStatus" name="createStatus" value="${trigger.createStatus}"></input>
						<input type="hidden" id="triggerId" name="triggerId" value="${trigger.triggerId}"></input>
					</td>
					<td colspan="2" align="right"><a href="#" class="easyui-linkbutton" onclick="deleteTrigger();" data-options="align:'right',iconCls:'icon-standard-delete'">删除触发器</a>&nbsp;<a href="#" style="margin-right: 10px;" class="easyui-linkbutton" data-options="iconCls:'icon-standard-add'" onclick="createTrigger();">创建触发器 </a></td>
				</tr>
				<tr>
					<td colspan="4"><span id="trigger_message"></span></td>
				</tr>
			</table>
		</div>
	</div>
</div>
<script>
	var lastIndex;
	var lastArributeGridIndex;
	var isAttributeChecked;
	var isChecked;
	var fieldData = [];
	var fieldChangeData = [];
	var attributeData = [];//文件内容提取字段数据;
	var outColumns = [];//输出任务的列数据;
	var outColumnsTypes = [];//输出任务的列数据类型;
	function initFieldData(){
		
		var fieldList = eval('${fieldList}');
		if(fieldList){
			$.each(fieldList,function(index,obj){
				 if(obj.filedSource == "转换字段")
				 {
					 //该数组存放所有的转换字段；
					 fieldChangeData.push({
						 fieldId:obj.fieldId,
						 fieldName:obj.fieldName,
						 fieldType:obj.fieldType,
						 fieldExp:obj.fieldExp.replaceAll("&apos;","'"),
						 filedSource:obj.filedSource,
						 targetFieldName:obj.targetFieldName,
						 targetFieldType:obj.targetFieldType,
						 fieldOrdinalPosition:obj.fieldOrdinalPosition,
						 taskId:obj.taskId
					 });
				 }
				 //该数组存放保存的所有字段
				 fieldData.push({
					 fieldId:obj.fieldId,
					 fieldName:obj.fieldName,
					 fieldType:obj.fieldType,
					 fieldExp:obj.fieldExp.replaceAll("&apos;","'"),
					 filedSource:obj.filedSource,
					 targetFieldName:obj.targetFieldName,
					 targetFieldType:obj.targetFieldType,
					 fieldOrdinalPosition:obj.fieldOrdinalPosition,
					 taskId:obj.taskId
				  });
			 });
		}
	}
	
	function getRowData(data) {
		var rows = [];
		for(var i=0 ;i<data.length;i++)
		{
			var fieldExpStr = "";
			var targetFieldNameStr = "";
			var targetFieldTypeStr = "";
			//给fieldRow赋值;
			for(var j = 0 ; j<fieldData.length ; j++)
			{
				if(data[i].fieldName == fieldData[j].fieldName && data[i].filedSource == fieldData[j].filedSource){
					fieldExpStr = fieldData[j].fieldExp;
					targetFieldNameStr = fieldData[j].targetFieldName;
					targetFieldTypeStr = fieldData[j].targetFieldType;
					break;
				}
			}
			rows.push({
				fieldName:data[i].fieldName,
				fieldType:data[i].fieldType,
				fieldExp:fieldExpStr,
				filedSource:data[i].filedSource,
				targetFieldName:targetFieldNameStr,
				targetFieldType:targetFieldTypeStr,
				delField:""
			});
		}
		return rows;
	}
	
	function initAttributeData()
	{
		var attributeList = eval('${attributeList}');
		var fileFieldType = "";
		var extensionField = "";
		var sourceFieldName = "";
		if(attributeList){
			$.each(attributeList,function(index,obj){
				 //该数组存放保存的所有字段
				 attributeData.push({
					 fileAttributeId:obj.fileAttributeId,
					 fileFieldType:obj.fileFieldType,
					 fileAttributeId:obj.fileAttributeId,
					 fileAttributeName:obj.fileAttributeName,
					 fileTypeValue:obj.fileTypeValue,
					 isDefault:obj.isDefault,
					 extensionField:obj.extensionField,
					 sourceFieldName:obj.sourceFieldName,
					 targetFieldName:obj.targetFieldName,
					 targetFieldType:obj.targetFieldType
			     });
				 fileFieldType = obj.fileFieldType;
				 extensionField	= obj.extensionField;
				 sourceFieldName = obj.sourceFieldName;
			});
			$("#fileFieldType").combobox("setValue",fileFieldType);
			$("#extensionField").combobox("setValue",extensionField);
			$("#sourceFieldName").combobox("setValue",sourceFieldName);
		}
	}
	/*将后台List转化为属性行数据*/
	function getArributeRowData(data){
		var rows = [];
		for(var j = 0 ; j<attributeData.length ; j++)
		{
			if(attributeData[j].isDefault == "0"){
				data.push(attributeData[j]);
			}
		}
		for(var i=0 ;i<data.length;i++)
		{
			var targetFieldNameStr = "";
			var targetFieldTypeStr = "";
			//给fieldRow赋值;
			for(var j = 0 ; j<attributeData.length ; j++)
			{
				if(data[i].fileAttributeId == attributeData[j].fileAttributeId){
					targetFieldNameStr = attributeData[j].targetFieldName;
					targetFieldTypeStr = attributeData[j].targetFieldType;
					break;
				}
			}
			rows.push({
				fileAttributeId:data[i].fileAttributeId,
				fileAttributeName:data[i].fileAttributeName,
				fileTypeValue:data[i].fileTypeValue,
				isDefault:data[i].isDefault,
				targetFieldName:targetFieldNameStr,
				targetFieldType:targetFieldTypeStr
			});
		}
		return rows;
	}
	function loadFieldMappingGrid(tableName){
		var dsTableName = '${dbSource.dsTableName}';
		$.ajax({
			type:'post',
			data:$("#configForm").serialize(),
			url:"${pageContext.request.contextPath}/db/getAllColumns",
			success: function(data){
				var sourceFieldData = getRowData(data);
				$('#fieldmapping').datagrid('loadData', sourceFieldData);
				if(tableName == dsTableName)
				{
					var rows = $('#fieldmapping').datagrid('getRows');
					for(var i = 0 ; i<rows.length ; i++)
					{
						var row = rows[i];
						for(var j = 0 ; j<fieldData.length ; j++)
						{
							if(row.fieldName == fieldData[j].fieldName && row.filedSource == fieldData[j].filedSource){
								$('#fieldmapping').datagrid('checkRow',i);
								break;
							}
						}
					}
					for(var i = 0 ; i<fieldChangeData.length ; i++)
					{
						var changeRow = fieldChangeData[i];
						$('#fieldmapping').datagrid('appendRow',{fieldName:changeRow.fieldName,fieldType:changeRow.fieldType,fieldExp:changeRow.fieldExp,filedSource:changeRow.filedSource,targetFieldName:changeRow.targetFieldName,targetFieldType:changeRow.targetFieldType});
						$('#fieldmapping').datagrid('checkRow',$('#fieldmapping').datagrid('getRows').length-1);
					}
				}
				var attributeSourceFields = [];
				if(sourceFieldData)
				{
					for(var i=0;i<sourceFieldData.length;i++)
					{
						attributeSourceFields.push({text:sourceFieldData[i].fieldName,value:sourceFieldData[i].fieldName});
					}
				}
				$("#extensionField").combobox('loadData',attributeSourceFields);//初始化扩展字段下拉列表;
				$("#sourceFieldName").combobox('loadData',attributeSourceFields);//初始化内容字段列表;
				initPrimaryField($('#fieldmapping'));//初始化主键字段;
			}
		});
	}
	function loadFieldMappingGridBySQL(sql){
		var dsTableName = '${dbSource.dsTableName}';
		$.ajax({
			type:'post',
			data:$("#configForm").serialize(),
			url:"${pageContext.request.contextPath}/db/getAllColumnsBySQL",
			success: function(data){
				$("#sqlconnstatus").text('');
				if(data.success)
				{
					$("#sqlconnstatus").css({color:'green'}).text("测试SQL通过");
					var sourceFieldData = getRowData(data.obj);
					$('#fieldmapping').datagrid('loadData', sourceFieldData);
					var rows = $('#fieldmapping').datagrid('getRows');
					for(var i = 0 ; i<rows.length ; i++)
					{
						var row = rows[i];
						for(var j = 0 ; j<fieldData.length ; j++)
						{
							if(row.fieldName == fieldData[j].fieldName && row.filedSource == fieldData[j].filedSource){
								$('#fieldmapping').datagrid('checkRow',i);
								break;
							}
						}
					}
					for(var i = 0 ; i<fieldChangeData.length ; i++)
					{
						var changeRow = fieldChangeData[i];
						$('#fieldmapping').datagrid('appendRow',{fieldName:changeRow.fieldName,fieldType:changeRow.fieldType,fieldExp:changeRow.fieldExp,filedSource:changeRow.filedSource,targetFieldName:changeRow.targetFieldName,targetFieldType:changeRow.targetFieldType});
						$('#fieldmapping').datagrid('checkRow',$('#fieldmapping').datagrid('getRows').length-1);
					}
					var attributeSourceFields = [];
					if(sourceFieldData)
					{
						for(var i=0;i<sourceFieldData.length;i++)
						{
							attributeSourceFields.push({text:sourceFieldData[i].fieldName,value:sourceFieldData[i].fieldName});
						}
					}
					$("#extensionField").combobox('loadData',attributeSourceFields);//初始化扩展字段下拉列表;
					$("#sourceFieldName").combobox('loadData',attributeSourceFields);//初始化内容字段列表;
				}
				else
				{
					$("#sqlconnstatus").css({color:'red'}).text(data.msg);
					$('#fieldmapping').datagrid('loadData', { total: 0, rows: [] });
				}
			},error:function (XMLHttpRequest, textStatus, errorThrown) {
				if(textStatus == "timeout"){
					$("#sqlconnstatus").css({color:'red'}).text("连接超时,请检查参数配置!");
				}else{
					 $("#sqlconnstatus").css({color:'red'}).text("连接失败,请检查参数配置!");
				}
				$('#fieldmapping').datagrid('loadData', { total: 0, rows: [] });
			}
		});
	}
	function fieldMappingSameNameMatch(){
		var rows = $('#fieldmapping').datagrid('getRows');
		var checkedRows = $('#fieldmapping').datagrid("getChecked");
		var matchNum = 0;
		var checkIndex = [];
		for(var i=0 ; i<rows.length;i++){
			var row = rows[i];
			for(var j=0;j<outColumns.length;j++){
				if(row.fieldName == outColumns[j].value)
				{
					matchNum ++;
					row.targetFieldName = row.fieldName;
					row.targetFieldType = outColumnsTypes[j].value;
					
					//记录自动选中同名字段
					checkIndex.push(i);
					break;
				}
			}
			$('#fieldmapping').datagrid('refreshRow',i);
		}
		//自动选中同名字段
		for(var i = 0 ; i<checkIndex.length ; i++)
		{
			$("#fieldmapping").datagrid("checkRow",checkIndex[i]);
		}
		for(var i = 0 ; i<checkedRows.length ; i++)
		{
			var checkedRowIndex = $('#fieldmapping').datagrid('getRowIndex',checkedRows[i]);
			$("#fieldmapping").datagrid("checkRow",checkedRowIndex);
		}
		if(matchNum == 0){
			$("#matchTips").text("未发现同名字段!");
		}else if(matchNum > 0){
			$("#matchTips").text("已匹配"+matchNum+"个字段!");
		}
	}
	
	var textArray = ["clob","blob","longblob","text","longtext"];
	var dateArray = ["date","datetime","time","timestamp"];
	
	function validMssFieldMapping(){
		var validMssFieldMappingInfo = "";
		var topType = $("#topType").combobox("getValue");
		if(topType != "mss")
		{
			$("#mssMatchTips").text("");
			return;
		}
		var checkedRows = $('#fieldmapping').datagrid('getChecked');
		for(var i = 0 ; i < checkedRows.length ; i++){
			var checkedRow = checkedRows[i];
			var fieldType = checkedRow.fieldType.toLowerCase();
			var fieldName = checkedRow.fieldName;
			var targetFieldType = checkedRow.targetFieldType.toLowerCase();
			var isHave = false;
			for(var t=0 ; t<textArray.length ; t++){
				if(fieldType == textArray[t]){
					if(targetFieldType != "长文本"){
						isHave = true;
						validMssFieldMappingInfo += fieldName+",";
						break;
					}
				}
			}
			if(isHave)
			continue;
			for(var d=0 ;d<dateArray.length ; d++){
				if(fieldType == dateArray[d]){
					if(targetFieldType == "数值"){
						validMssFieldMappingInfo += fieldName+",";
						break;
					}
				}
			}
		}
		
		if(validMssFieldMappingInfo != ""){
			$("#mssMatchTips").text("存在文本(TEXT)或者日期(DATE)类型字段类型匹配不正确,可能会造成数据同步失败,请纠正！");
		}else{
			$("#mssMatchTips").text("");
		}
	}
	
	//文件提取属性同名自动匹配
	function fileAttributeSameNameMatch(){
		var rows = $('#fileattr').datagrid('getRows');
		var checkedRows = $('#fileattr').datagrid("getChecked");
		var matchNum = 0;
		for(var i=0 ; i<rows.length;i++){
			var row = rows[i];
			for(var j=0;j<outColumns.length;j++){
				if(row.fileAttributeName == outColumns[j].value)
				{
					matchNum ++;
					row.targetFieldName = row.fileAttributeName;
					row.targetFieldType = outColumnsTypes[j].value;
					break;
				}
			}
			$('#fileattr').datagrid('refreshRow',i);
		}
		for(var i = 0 ; i<checkedRows.length ; i++)
		{
			var checkedRowIndex = $('#fileattr').datagrid('getRowIndex',checkedRows[i]);
			$("#fileattr").datagrid("checkRow",checkedRowIndex);
		}
		if(matchNum == 0){
			$("#matchAttributeTips").text("未发现同名字段!");
		}else if(matchNum > 0){
			$("#matchAttributeTips").text("已匹配"+matchNum+"个字段!");
		}
	}
	//加载文件属性Grid;
	function loadAttributeMappingGrid()
	{
		$.ajax({
			type:'post',
			data:$("#configForm").serialize(),
			url:"${pageContext.request.contextPath}/db/getAllFileAttributes",
			success: function(data){
				$('#fileattr').datagrid('loadData', getArributeRowData(data['default']));
				var rows = $('#fileattr').datagrid('getRows');
				for(var i = 0 ; i<rows.length ; i++)
				{
					var row = rows[i];
					for(var j = 0 ; j<attributeData.length ; j++)
					{
						if(row.fileAttributeId == attributeData[j].fileAttributeId){
							$('#fileattr').datagrid('checkRow',i);
							break;
						}
					}
				}
			}
		});
	}
	//初始化输出任务数据库列数据;
	function initOutputColumn()
	{
		var rows = $('#fieldmapping').datagrid('getRows');
		var checkedRows = $('#fieldmapping').datagrid("getChecked");
		for(var i=0 ; i<rows.length;i++){
			var row = rows[i];
			//清除之前选择表匹配的内容
			row.targetFieldName = '';
			row.targetFieldType = '';
			$('#fieldmapping').datagrid('refreshRow',i);
		}
		for(var i = 0 ; i<checkedRows.length ; i++)
		{
			var checkedRowIndex = $('#fieldmapping').datagrid('getRowIndex',checkedRows[i]);
			$("#fieldmapping").datagrid("checkRow",checkedRowIndex);
		}
		
		$.ajax({
        	url:'${pageContext.request.contextPath}/taskoutput/getOutputAllColumns',
        	data:$("#configForm").serialize(),
			dataType : 'json',
			type : 'POST',
			success: function (data){
				//先清空原来数组,否则累加递增;
				outColumns = [];
				outColumnsTypes = [];
				if(data['name'])
				{
					for(var i=0;i<data['name'].length;i++)
					{
						outColumns.push({text:data['name'][i],value:data['name'][i]});
					}
					for(var i=0;i<data['type'].length;i++)
					{
						outColumnsTypes.push({text:data['type'][i],value:data['type'][i]});
					}
				}
			} 
        });
	}
	function initPrimaryField(dg){
		var rows = dg.datagrid('getRows');
		var primaryField = $("#primaryField").val();
		for(var i = 0 ; i < rows.length ;i++){
			var row = rows[i];
			if(row.fieldName == primaryField){
				row.primaryField = '<a href="javascript:;"><div class="icon-standard-key" style="width:28px;height:20px" title="主键"></div></a>';
			}
			var isChecked = RowIsChecked(dg,i);
			dg.datagrid('updateRow', {
				index : i,
				row : row
			});
			if(isChecked){
				dg.datagrid("checkRow",i);
			}
		}
	}
	function onFieldMappingClickCell(rowIndex, field, value){
		if(field == "primaryField"){
			var rows = $('#fieldmapping').datagrid('getRows');
			for(var i = 0 ; i < rows.length ;i++){
				var row = rows[i];
				row.primaryField ='';
				var isChecked = RowIsChecked($('#fieldmapping'),i);
				$('#fieldmapping').datagrid('updateRow', {
					index : i,
					row : row
				});
				if(isChecked){
					$("#fieldmapping").datagrid("checkRow",i);
				}
			}
			var currentRow = rows[rowIndex];
			if(value){
				currentRow.primaryField = '';
				$("#primaryField").val("");
			}else{
				currentRow.primaryField = '<a href="javascript:;"><div class="icon-standard-key" style="width:28px;height:20px" title="主键"></div></a>';
				$("#primaryField").val(currentRow.fieldName);
			}
			var isChecked = RowIsChecked($('#fieldmapping'),rowIndex);
			$('#fieldmapping').datagrid('updateRow', {
				index : rowIndex,
				row : currentRow
			});
			if(isChecked){
				$("#fieldmapping").datagrid("checkRow",rowIndex);
			}
		}
	}
	//操作按钮;
	function delFieldFormatter(value, row, index)
	{
		var delButton = "";
		if(row.filedSource == "转换字段")
		{
			delButton = '<a href="javascript:;" onclick=\"deleteFiledMappingRow(this);\"><div class="icon-hamburg-busy" style="width:28px;height:20px" title="删除字段"></div></a>';
		}
		return delButton;
	}
	
	//删除一行转换字段;
	function deleteFiledMappingRow(obj)
	{
		var rows = $('#fieldmapping').datagrid('getRows');
		var index = $(obj).parents("tr:first").attr("datagrid-row-index");
		parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
			if (data){
				var rowIndex = $('#fieldmapping').datagrid('getRowIndex',rows[index]);
				$('#fieldmapping').datagrid('deleteRow',rowIndex);
			} 
		});
	}
	
	//添加一行转换字段;
	function appendfiledMappingRow(){
		var rowslength = $('#fieldmapping').datagrid('getRows').length;
		$('#fieldmapping').datagrid('appendRow',{fieldName:"NewField"+(rowslength+1),filedSource:'转换字段',targetFieldType:""});
		var editIndex = rowslength;
		onDblClickRow(editIndex);
	}
	function onBeforeEdit(rowIndex, rowData)
	{
		isChecked = RowIsChecked($('#fieldmapping'),rowIndex);
	}
	function onAfterEdit(rowIndex, rowData, changes)
	{
		if(isChecked){
			$("#fieldmapping").datagrid("checkRow",rowIndex);
		}
	}
	function onAttributeBeforeEdit(rowIndex, rowData)
	{
		isAttributeChecked = RowIsChecked($('#fileattr'),rowIndex);
	}
	function onAttributeAfterEdit(rowIndex, rowData, changes)
	{
		if(isAttributeChecked){
			$('#fileattr').datagrid("checkRow",rowIndex);
		}
	}
	function RowIsChecked(gridObj,rowIndex){
		var isChecked = false;
		var checkedRows = gridObj.datagrid("getChecked");
		for(var i = 0 ; i<checkedRows.length ; i++)
		{
			var checkedRowIndex = gridObj.datagrid('getRowIndex',checkedRows[i]);
			if(checkedRowIndex == rowIndex){
				isChecked = true;
				break;
			}
		}
		return isChecked;
	}
	function onClickRow(rowIndex, rowData)
	{
		$("#fieldmapping").datagrid('endEdit',lastIndex);
		var isChecked = RowIsChecked($('#fieldmapping'),rowIndex);
		if(!isChecked){
			$("#fieldmapping").datagrid("uncheckRow",rowIndex);
		}else{
			$("#fieldmapping").datagrid("checkRow",rowIndex);
		}
	}
	
	function onAttributeClickRow(rowIndex,rowData)
	{
		$("#fileattr").datagrid('endEdit',lastArributeGridIndex);
		var isChecked = RowIsChecked($("#fileattr"),rowIndex);
		if(!isChecked){
			$("#fileattr").datagrid("uncheckRow",rowIndex);
		}else{
			$("#fileattr").datagrid("checkRow",rowIndex);
		}
	}
	
	function onArributeDblClickRow(rowIndex)
	{
		var fieldAttributeDg = $("#fileattr");
		var rows = fieldAttributeDg.datagrid('getRows');//get current page rows
        var row = rows[rowIndex];
        fieldAttributeDg.datagrid('endEdit',lastArributeGridIndex);
        lastArributeGridIndex=rowIndex;
        fieldAttributeDg.datagrid('beginEdit',rowIndex);
        var colEd = fieldAttributeDg.datagrid('getEditor',{index:rowIndex,field: 'targetFieldName' });
        var width = $(colEd.target).parent().find("span.combo:first").width()+2;
        $.ajax({
        	url:'${pageContext.request.contextPath}/taskoutput/getOutputAllColumns',
        	data:$("#configForm").serialize(),
			dataType : 'json',
			type : 'POST',
			success: function (data){
				var columnNames = [];
				var columnTypes = [];
				if(data['name'])
				{
					for(var i=0;i<data['name'].length;i++)
					{
						columnNames.push({colvalue:data['name'][i],colname:data['name'][i]});
						columnTypes.push({typeValue:data['type'][i],colname:data['name'][i]});
					}
				}
				$(colEd.target).combobox({
					 data:columnNames,
					 width:width,
					 onChange:function(newValue,oldValue){
						 for (i = 0; i < columnTypes.length; i++) {
							 if (columnTypes[i].colname == newValue){
								 row.targetFieldType = columnTypes[i].typeValue;
								 break;
							 }
						 }
					 } 
				});
				$(colEd.target).combobox('select', row.targetFieldName);
			} 
        });
	}
	//双击行点击事件;
	function onDblClickRow(rowIndex){
		var fieldMappingDg = $("#fieldmapping");
		var rows = fieldMappingDg.datagrid('getRows');//get current page rows
        var row = rows[rowIndex];
        if(row.filedSource == "转换字段")
        {
        	fieldMappingDg.datagrid('addEditor',[{
	    			field : 'fieldName', 
	    			editor : {
	    				type : 'text',
	    				options:{
	    					required:true
	    				}
	    			}
    			},{
	    			field : 'fieldType', 
	    			editor : {
	    				type : 'combobox',
	    				options:{
	    					valueField:'colvalue',
	    					textField:'colname',
	    					editable:false
	    				}
	    			}
    			}
    		]); 
        }else{
        	fieldMappingDg.datagrid('removeEditor',[{
	    			field : 'fieldName'
	    		},{
	    			field : 'fieldType'
				}
        	]);
        }
		fieldMappingDg.datagrid('endEdit',lastIndex);
		lastIndex=rowIndex;
		fieldMappingDg/*.datagrid('checkRow',rowIndex)*/.datagrid('beginEdit',rowIndex);
        var colEd = fieldMappingDg.datagrid( 'getEditor',{index:rowIndex,field: 'targetFieldName' });
        var colFieldType = fieldMappingDg.datagrid( 'getEditor',{index:rowIndex,field: 'fieldType' });
        var width = $(colEd.target).parent().find("span.combo:first").width()+2;
        $.ajax({
        	url:'${pageContext.request.contextPath}/taskoutput/getOutputAllColumns',
        	data:$("#configForm").serialize(),
			dataType : 'json',
			type : 'POST',
			success: function (data){
				var columnNames = [];
				var columnTypes = [];
				var fieldTypes = [];
				if(source_field_types){
					for(key in source_field_types){
						fieldTypes.push({colvalue:source_field_types[key],colname:key});
					}
				}
				if(data['name'])
				{
					for(var i=0;i<data['name'].length;i++)
					{
						columnNames.push({colvalue:data['name'][i],colname:data['name'][i]});
						columnTypes.push({typeValue:data['type'][i],colname:data['name'][i]});
					}
				}
				$(colEd.target).combobox({
					 data:columnNames,
					 width:width,
					 onChange:function(newValue,oldValue){
						var checkedRows = $('#fieldmapping').datagrid('getChecked');
						var isHave = false;
						for(var i = 0 ; i < checkedRows.length ; i++){
							var checkedRow = checkedRows[i];
							var targetFieldName = checkedRow.targetFieldName;
							var checkedRowIndex =  $('#fieldmapping').datagrid('getRowIndex',checkedRow);
						 	if(targetFieldName == newValue && rowIndex != checkedRowIndex){
						 		isHave = true;
						 		break;
						 	}
						}
						if(isHave){
							$("#mssMatchTips").text("此目标字段已经匹配,请纠正...");
							$(colEd.target).combobox('setValue',oldValue);
							return;
						}
						for (i = 0; i < columnTypes.length; i++) {
						 	if (columnTypes[i].colname == newValue){
								 row.targetFieldType = columnTypes[i].typeValue;
								 break;
						 	}
						}
					 } 
				});
				$(colEd.target).combobox('select', row.targetFieldName);
				$(colFieldType.target).combobox('loadData' , fieldTypes);
			} 
        });
	}
	function setOpenStatus(obj){
		if($(obj).attr("checked")){
			$(obj).removeAttr("checked");
			$(obj).val("0");
		}else{
			$(obj).attr("checked","checked");
			$(obj).val("1");
		}
	}
	//删除触发器;
	function deleteTrigger()
	{
		var triggerId = $("#triggerId").val();
		if(!triggerId){
			parent.$.messager.show({ title : "提示",msg: "没有要删除的触发器！", position: "bottomRight" });
			return;
		}
		parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除触发器？', function(data){
			if (data){
		        $.ajax({
		            url: '${pageContext.request.contextPath}/trigger/deleteCrigger',
		            type: "POST",
		            data:$("#configForm").serialize(),
		            success: function (data) {
		             	if(successTip(data)){
		             		$("#triggerId").val("");
							$("#triggerName").val("");
							$("#triggerTableName").val("");
							$("#primaryField").val("");
							$("#statusField").val("");
							$("#triggerScript").val("");
							$("#createStatus").val("");
		                }
		             	$("#trigger_message").css({color:'green'}).html("触发器删除成功!");
		            },error:function (XMLHttpRequest, textStatus, errorThrown) {
			        	parent.$.messager.progress('close');
			        	if(textStatus == "timeout"){
			        		parent.$.messager.alert("触发器删除超时!");
			        	}else{
			        		$("#trigger_message").css({color:'red'}).html("触发器删除失败!");
			        		parent.$.messager.alert("触发器删除错误:"+e);
			        	}
			        }
		        });
			 } 
		});
	}
	//创建触发器;
	function createTrigger()
	{
		var createStatus = $("#createStatus").val();
		if(createStatus == 1){
			$.easyui.messager.alert("提示", "触发器已经创建,请删除后重试!", "info");
			return;
		}
		var dsTableName = $("#dsTableName").combobox('getValue');
		var triggerTableName = $("#triggerTableName");
		var triggerName = $("#triggerName");
		var primaryField = $("#primaryField");
		if(!dsTableName)
		{
			$("#trigger_message").css({color:'red'}).html("请选择源数据表!");
			return;
		}else{
			if(!triggerName.val()){
				triggerName.focus();
				return;
			}
			if(!triggerTableName.val()){
				triggerTableName.focus();
				return;
			}
			if(!primaryField.val()){
				primaryField.focus();
				return;
			}
			$.ajax({
	        	url:'${pageContext.request.contextPath}/trigger/createCrigger',
	        	data:$("#configForm").serialize(),
	        	dataType : 'json',
				type : 'POST',
				success: function (data){
					if("1" == data.status){
						$("#triggerId").val(data.trigger.triggerId);
						$("#triggerName").val(data.trigger.triggerName);
						$("#triggerTableName").val(data.trigger.triggerTableName);
						$("#primaryField").val(data.trigger.primaryField);
						$("#statusField").val(data.trigger.statusField);
						$("#triggerScript").val(data.trigger.triggerScript);
						$("#createStatus").val(data.trigger.createStatus);
						parent.$.messager.show({ title : "提示",msg: "触发器创建成功！", position: "bottomRight" });
						$("#trigger_message").css({color:'green'}).html("触发器创建成功!");
					}else{
						$.easyui.messager.alert("错误提醒", "触发器创建错误!", "error");
						$("#trigger_message").css({color:'red'}).html(data.msg);
					}
				},error:function (XMLHttpRequest, textStatus, errorThrown) {
		        	parent.$.messager.progress('close');
		        	if(textStatus == "timeout"){
		        		parent.$.messager.alert("创建超时!");
		        	}else{
		        		$("#trigger_message").css({color:'red'}).html("触发器创建失败!");
		        		parent.$.messager.alert("触发器创建错误:"+e);
		        	}
		        } 
	        });
		}
	}
	
	//添加提取属性
	function tikaAttributeAdd() {
		var rows = $("#fileattr").datagrid('getRows');
		var noDefaultIds = "";
		for(var i = 0 ; i< rows.length ; i++){
			var row = rows[i];
			if(row.isDefault == "0"){
				noDefaultIds += row.fileAttributeId + ",";
			}
		}
		d=$("#planDlg").dialog({   
		    title: '添加提取属性',    
		    width: 650,
		    height: 430,    
		    href:"${pageContext.request.contextPath}/fileAttributeController/forFileAttributeList?noDefaultIds="+noDefaultIds,
		    modal:true,
		    buttons:[{
				text:'添加',
				handler:function(){
					var rows = $('#attributeList').datagrid('getChecked');
					if(rowIsNull(rows)) return;
					moveRowToHave($('#attributeList'),$('#fileattr'));
					d.panel('close');
					//$("#mainform").submit();
				}
			},{
				text:'取消',
				handler:function(){
					d.panel('close');
				}
			}]
		});
	}
	
	function moveRowToHave(dgn,dgy){
		var rows = dgn.datagrid('getChecked');
		for(var i=0; i<rows.length; i++){
			var row = rows[i];
			dgy.datagrid('appendRow',row);
		}
	}
	
	//操作按钮;
	function delAttributeFieldFormatter(value, row, index)
	{
		var delButton = "";
		if(row.isDefault == "0")
		{
			delButton = '<a href="javascript:;" onclick=\"deleteAttributeFiledMappingRow(this);\"><div class="icon-hamburg-busy" style="width:28px;height:20px" title="删除提取属性"></div></a>';
		}
		return delButton;
	}
	
	//删除一行属性字段;
	function deleteAttributeFiledMappingRow(obj)
	{
		var rows = $("#fileattr").datagrid('getRows');
		var index = $(obj).parents("tr:first").attr("datagrid-row-index");
		parent.$.messager.confirm('提示','删除后无法恢复您确定要删除？', function(data){
			if(data){
				var rowIndex = $("#fileattr").datagrid('getRowIndex',rows[index]);
				$("#fileattr").datagrid('deleteRow',rowIndex);
			}
		});
	}
	
	$(function() {
		if (Sys.ie){
			$("#triggerScript").attr("cols","104");
		}else if(Sys.chrome){
			$("#triggerScript").attr("cols","100");
		}
		initFieldData();
		initAttributeData();
		loadAttributeMappingGrid();
		$('#filetype').combobox({
			onChange : function(newValue, oldValue) {
				if(newValue == "blob"){
					$('#ftpconnect').hide();
				}else{
					$('#ftpconnect').show();
				}
			}
		});
	});
</script>
