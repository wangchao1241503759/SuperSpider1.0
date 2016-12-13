<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="fieldmappingTabDiv" class="easyui-tabs" data-options="fit:true,border:false" style="height: 400px;">
	<div title="文件字段内容提取">
		<div id="filetika" style="padding: 5px; height: auto">
			<div style="padding-top: 5px;">
				<a href="#" class="easyui-linkbutton" data-options="align:'right',iconCls:'icon-standard-add'" onclick="tikaAttributeAdd();">添加提取属性</a>
				<a href="#" class="easyui-linkbutton" onclick="fileAttributeSameNameMatch();" data-options="align:'right',iconCls:'icon-hamburg-product'">同名自动匹配</a>
				<span style="color: green;" id="matchAttributeTips"></span>
			</div>
		</div>
		<input type="hidden" id="fileattrJson" name="fileattrJson" value="">
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
</div>
<script>
	var lastIndex;
	var lastArributeGridIndex;
	var isAttributeChecked;
	var isChecked;
	var fieldData = [];
	var attributeData = [];//文件内容提取字段数据;
	var outColumns = [];//输出任务的列数据;
	var outColumnsTypes = [];//输出任务的列数据类型;
	
	function initAttributeData()
	{
		var attributeList = eval('${attributeList}');
		if(attributeList){
			$.each(attributeList,function(index,obj){
				 //该数组存放保存的所有字段
				 attributeData.push({
					 fileAttributeId:obj.fileAttributeId,
					 fileAttributeId:obj.fileAttributeId,
					 fileAttributeName:obj.fileAttributeName,
					 fileTypeValue:obj.fileTypeValue,
					 isDefault:obj.isDefault,
					 targetFieldName:obj.targetFieldName,
					 targetFieldType:obj.targetFieldType
			     });
			});
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
	
	//文件提取属性同名自动匹配
	function fileAttributeSameNameMatch(){
		var rows = $('#fileattr').datagrid('getRows');
		var checkedRows = $('#fileattr').datagrid("getChecked");
		var matchNum = 0;
		var checkIndex = [];
		for(var i=0 ; i<rows.length;i++){
			var row = rows[i];
			for(var j=0;j<outColumns.length;j++){
				if(row.fileAttributeName == outColumns[j].value)
				{
					matchNum ++;
					row.targetFieldName = row.fileAttributeName;
					row.targetFieldType = outColumnsTypes[j].value;
					//记录自动选中同名字段
					checkIndex.push(i);
					break;
				}
			}
			$('#fileattr').datagrid('refreshRow',i);
		}
		//自动选中同名字段
		for(var i = 0 ; i<checkIndex.length ; i++)
		{
			$("#fileattr").datagrid("checkRow",checkIndex[i]);
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
		var rows = $('#fileattr').datagrid('getRows');
		var checkedRows = $('#fileattr').datagrid("getChecked");
		for(var i=0 ; i<rows.length;i++){
			var row = rows[i];
			//清除之前选择表匹配的内容
			row.targetFieldName = '';
			row.targetFieldType = '';
			$('#fileattr').datagrid('refreshRow',i);
		}
		for(var i = 0 ; i<checkedRows.length ; i++)
		{
			var checkedRowIndex = $('#fileattr').datagrid('getRowIndex',checkedRows[i]);
			$("#fileattr").datagrid("checkRow",checkedRowIndex);
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
