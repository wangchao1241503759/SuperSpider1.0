<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script type="text/javascript">
	// 构造表单
	var dataGrid;
		dataGrid = $('#dataGrids').datagrid({
			url : '${pageContext.request.contextPath}/proxyServerController/dataGrid',
			fit : true,
			fitColumns : true,
			border : false,
// 			pagination : true,
			idField : 'id',
			pageSize : 10,
			pageList : [ 10, 20, 30, 40, 50 ],
			sortName : 'taskName',
			sortOrder : 'desc',
            striped:true,
            checkOnSelect:false,
            selectOnCheck:true,
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
				field : 'ip',
				title : '服务器IP地址',
				width : 150,
				sortable : false
			},{
				field : 'port',
				title : '端口',
				width : 250,
				sortable : false
			},{
				field : 'source',
				title : '来源',
				width : 250,
				sortable : false
			},{
				field : 'type',
				title : '类型',
				width : 150,
				sortable : false
			},{
				field : 'state',
				title : '状态',
				width : 150,
				hidden:true,
				formatter : function(value, row, index) {
					var str = '-';
						if(value=='Y')
					  	{
						  str="启用";
					  	}
						else if(value=='N')
						{
						  str="停用";
						}
					return str;
				}
			},{
				field : 'action',
				title : '操作',
				width : 150,
				formatter : function(value, row, index) {
					return '<a href="javascript:;" onclick=\"onDblClickRowByProxy('+index+');\"><div class="icon-edit" style="width:28px;height:20px" title="修改"></div></a>';
				}
			}] ]
// 			toolbar : '#toolbar',
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
		isChecked_type = RowIsChecked(dataGrid,rowIndex);
		if(isChecked_type){
			dataGrid.datagrid("checkRow",rowIndex);
		}
		else
		{
			dataGrid.datagrid("uncheckRow",rowIndex);
		}
	}
	function onAfterEdit(rowIndex, rowData, changes) {
		// 	if(isChecked){
		// 		$("#fieldmapping").datagrid("checkRow",rowIndex);
		// 	}
	}
	
	function onAfterEditProxy(rowIndex, rowData, changes) {
		
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
	
	
	var editIndexProxy = undefined;
	/**
	*结束编辑事件
	*/
	function endEditingProxy(){
		if (editIndexProxy == undefined){return true}
		if(dataGrid.datagrid('validateRow',editIndexProxy))
		{
			dataGrid.datagrid('endEdit', editIndexProxy);
			editIndexProxy = undefined;
	        return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	*双击事件
	*/
	function onDblClickRowByProxy(rowIndex, rowData)
	{
	  if (endEditingProxy())
       	{
		  editIndexProxy = rowIndex;
		  dataGrid.datagrid('addEditor',[
	          {
	  			field : 'ip', 
	  			editor : {
	  				type : 'validatebox',
	  				options:{
	  					validType : "checkProxyIPDataGrid['#dataGrids','ip',editIndexProxy]",
	  					required:true
	  				}
	  			}
	   		  },{                                  
				  			field : 'port', 
				  			editor : {
				  				type : 'validatebox',
				  				options:{
				  					required:true
				  				}
				  			}
	   		  },{                                  
				  			field : 'source', 
				  			editor : {
				  				type : 'validatebox',
				  				options:{
				  					required:true
				  				}
				  			}
	   		  },{                                  
				  			field : 'type', 
				  			editor : {
				  				type : 'validatebox',
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
	
	/**
	*单击事件
	*/
	function onClickRowByProxy(rowIndex, rowData)
	{
        endEditingProxy();
		var isChecked_type = RowIsChecked(dataGrid,rowIndex);
		if(!isChecked_type){
			dataGrid.datagrid("uncheckRow",rowIndex);
		}else{
			dataGrid.datagrid("checkRow",rowIndex);
		}
	}
	
	/**
	*判断是否已经选择
	*/
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
	
	//添加代理
	function appendProxy()
	{
		//alert(endEditingProxy())
		if(endEditingProxy())
		{
// 			var len = dataGrid.datagrid('getRows').length;
// 			dataGrids.datagrid('unselectAll');
			//dataGrids.datagrid('appendRow',{id:uuid(),ip:'',port:'',source:'',type:'',state:'Y'});
			//onDblClickRowByProxy(len);
			
			dataGrid.datagrid('appendRow',{id:uuid(),ip:'',port:'',source:'',type:'',state:'Y'});
		    var index = dataGrid.datagrid('getRows').length-1;
		    dataGrid.datagrid('refreshRow',index);
		    //onDblClickCellType(index);
		    onDblClickRowByProxy(index)
		}
		else
		{
			$.messager.alert('提示','需要把其他数据的必填项填写完整才能添加新的数据！','info');
		}
	}
	
	
	function deleteProxy()
	{
		var dataGrids = $("#dataGrids");
		var rows = dataGrids.datagrid('getChecked');
		var ids = [];
		if (rows.length > 0) {
			parent.$.messager.confirm('确认', '您确定要删除所选代理服务地址吗？', function(con) {
				if (con) {
					parent.$.messager.progress({
						title : '提示',
						text : '数据处理中，请稍后....'
					});
					for (var i = 0; i < rows.length; i++) {
						ids.push(rows[i].id);
					}
					
					//1、检测是否能删除
					$.getJSON('${pageContext.request.contextPath}/proxyServerController/deleteCheck', {
						ids : ids.join(',')
					}, function(result) {
						//2、成功表示能删除,则直接删除
						if (result.success) {
		 					$.getJSON('${pageContext.request.contextPath}/proxyServerController/deleteBatch', {
		 						ids : ids.join(',')
		 					}, function(result2) {
		 						//2.1、删除成功
		 						if (result2.success) {
		 							//parent.$.messager.alert('成功', result2.msg, 'info');
		 							dataGrids.datagrid('reload');
		 							dataGrids.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
		 						} else {
		 							parent.$.messager.alert('警告', result2.msg, 'warning');
		 						}
		 						parent.$.messager.progress('close');
		 					});
						} 
						else 
						{
							//3、表示有些数据被引用了，需要确认删除
							//parent.$.messager.alert('警告', result.msg, 'warning');
							parent.$.messager.confirm('确认', '您选择的代理服务地址记录中有些已经被引用了，您是否要忽略这些记录，继续删除其他代理服务地址吗？', function(con) {
							if (con) {
			 					$.getJSON('${pageContext.request.contextPath}/proxyServerController/deleteBatch', {
			 						ids : ids.join(',')
			 					}, function(result2) {
			 						//2.1、删除成功
			 						if (result2.success) {
			 							//parent.$.messager.alert('成功', result2.msg, 'info');
			 							dataGrids.datagrid('reload');
			 							dataGrids.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
			 						} else {
			 							parent.$.messager.alert('警告', result2.msg, 'warning');
			 						}
			 						parent.$.messager.progress('close');
			 					});
							}
							});
							parent.$.messager.progress('close');
						}
					});
					
					
				}
			});
		} else {
			parent.$.messager.alert('警告', '请选择需要删除的代理服务地址', 'warning');
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
