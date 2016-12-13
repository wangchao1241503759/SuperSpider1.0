<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script type="text/javascript">
	// 构造表单
	var dataGrid;
	$(function() {
		dataGrid = $('#dataGrids').datagrid({
			url : '${pageContext.request.contextPath}/taskLogController/dataGridError',
			fit : true,
			fitColumns : true,
			border : false,
// 			pagination : true,
			idField : 'id',
			pageSize : 10,
			pageList : [ 10, 20, 30, 40, 50 ],
			sortName : 'taskName',
			sortOrder : 'desc',
			checkOnSelect : false,
			selectOnCheck : false,
			nowrap : false,
			onBeforeEdit:onBeforeEdit,
			onAfterEdit:onAfterEditProxy,
			onDblClickRow:onDblClickRowByProxy,
			onClickRow:onClickRowByProxy,
			frozenColumns : [ [ {
				field : 'id',
				title : '序号',
				width : 40,
      			align : 'center',
      			checkbox : true
			}]], 
			columns : [ [{
				field : 'taskName',
				title : '任务名称',
				width : 150,
				sortable : false
			}
			] ],
// 			toolbar : '#toolbar',
	        onBeforeEdit:function(index,row){  
	            row.editing = true;  
	            dataGrid.datagrid('refreshRow', index);  
	        },  
		});
	});

	//删除
	function deleteFun(id)
	{
		$.messager.confirm('确认', '您确定要删除该代理服务吗？', function(con) {
			if (con) {
				$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
				$.post('${pageContext.request.contextPath}/proxyServerController/delete', {
					id : id
				}, function(result) {
					if (result.success) {
						$.messager.alert('成功', result.msg, 'info');
						dataGrid.datagrid('reload');
					} else {
						$.messager.alert('警告', result.msg, 'warning');
					}
					$.messager.progress('close');
				}, 'JSON');
			}
		});
	}
	
	//添加
	function addFun() 
	{
		
		dataGrid.datagrid("appendRow",{id:'',ip:'',port:'',source:'',type:'',state:'Y'});
		var length = dataGrid.datagrid("getRows").length;  
        if(length > 0){  
            editIndex = length - 1;  
        }else{  
            editIndex = 0;  
        }         
        //$obj.datagrid("selectRow", editIndex);  
        dataGrid.datagrid("beginEdit", editIndex);   
	}
	
	function onDblClickRow(index)
	{
// 		var rows = dataGrid.datagrid("getRows");
// 		var currentRow = rows[indexRow];
// 		dataGrid.datagrid({
// 			onDblClickRow:function(index,row){
// 				$(this).datagrid('beginEdit', index);
// 			}
// 		});
		dataGrid.datagrid('beginEdit', index);
		var ed = dataGrid.datagrid('getEditor', {index:index,field:'ip'});
		$(ed.target).focus();
		
	}
	
	function onBeforeEdit(rowIndex, rowData) {
		// 	isChecked = RowIsChecked($('#fieldmapping'),rowIndex);
	}
	function onAfterEdit(rowIndex, rowData, changes) {
		// 	if(isChecked){
		// 		$("#fieldmapping").datagrid("checkRow",rowIndex);
		// 	}
	}
	
	function onAfterEditProxy(rowIndex, rowData, changes) {
		
// 		var rows = dataGrid.datagrid('getRows');
// 		for (var i = 0; i < rows.length; i++) {
// 			if (i != rowIndex && rowData.typeName == rows[i].typeName) {
// 				$.messager.alert('提示', rowData.typeName + '该规则名称已经存在！', 'info');
// 				//dg_type1.datagrid('updateRow',{index:rowIndex,row:{typeName:''}});
// 				//onDblClickCellType(rowIndex);
// 				dataGrid.datagrid('selectRow', rowIndex).datagrid(
// 						'beginEdit', rowIndex);
// 				break;
// 			}
// 		}

		if(rowData.ip=="" || rowData.port=="")
		{
			$.messager.alert('提示', 'IP地址或者端口不能为空！', 'info');
			dataGrid.datagrid('beginEdit', rowIndex);
		}
		else
		{
			
			var proxyServerJson =$.toJSON(rowData);
			var url = '${pageContext.request.contextPath}/proxyServerController/addOrEdit';
			$.ajax({
				type:'post',
				url:url,
				data:{proxyServerJson:proxyServerJson},
				success:function(result)
				{
					
					if (result.success) {
					  	//$.messager.alert('成功', result.msg, 'info');
					} else {
						$.messager.alert('错误', result.msg, 'error');
						onDblClickRowByProxy(rowIndex, rowData)
					}
				},
				error:function (XMLHttpRequest, textStatus, errorThrown) {
				    // 通常 textStatus 和 errorThrown 之中
				    // 只有一个会包含信息
				    this; // 调用本次AJAX请求时传递的options参数
				}
			});
		}
	}
	
	
	var editIndexProxy;
	function endEditingProxy(){
		var flag = true;
		if(typeof(editIndexProxy)!="undefined")
		{
			var rows = dataGrid.datagrid('getRows');
			for(var i=0;i<rows.length;i++)
			{
				var ip = dataGrid.datagrid('getEditor', {index:editIndexProxy,field:'ip'});
				var ipstr=$(ip.target).val();
				var port = dataGrid.datagrid('getEditor', {index:editIndexProxy,field:'port'});
				var portstr=$(port.target).val();
				if(ipstr=="" || portstr=="")
				{
					flag=false;
					break;
				}
			}
			
			if(flag)
			{
				dataGrid.datagrid('endEdit', editIndexProxy);
				editIndexProxy=undefined;
			}
		}
	    return flag;
	}
	
	function onDblClickRowByProxy(rowIndex, rowData)
	{
	  if (endEditingProxy())
       	{
		  editIndexProxy = rowIndex;
		  dataGrid.datagrid('addEditor',[
	          {
	  			field : 'ip', 
	  			editor : {
	  				type : 'text',
	  				options:{
	  					required:true
	  				}
	  			}
	   		  },{                                  
				  			field : 'port', 
				  			editor : {
				  				type : 'text',
				  				options:{
				  					required:true
				  				}
				  			}
	   		  },{                                  
				  			field : 'source', 
				  			editor : {
				  				type : 'text',
				  				options:{
				  					required:true
				  				}
				  			}
	   		  },{                                  
				  			field : 'type', 
				  			editor : {
				  				type : 'text',
				  				options:{
				  					required:true
				  				}
				  			}
	   		  },{                                  
				  			field : 'state', 
				  			editor : {
				  				type : 'text',
				  				options:{
				  					required:true
				  				}
				  			}
			   }
			]); 
		  dataGrid.datagrid('selectRow', rowIndex).datagrid('beginEdit', rowIndex);
        }
	}
	
	function onClickRowByProxy(rowIndex, rowData)
	{
        if (editIndexProxy != rowIndex){
            endEditingProxy();
        }
	}
    
</script>

  <div class="easyui-layout" data-options="fit : true,border : false">

    <div data-options="region:'center',border:false" title="" style="overflow: hidden;">
      <table id="dataGrids"></table>
    </div>
  </div>
  
    <div id="toolbar" style="display: none;">
      <a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton"
        data-options="plain:true,iconCls:'pencil_add'">添加</a>
  </div>
