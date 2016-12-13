<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<title>数据采集任务管理</title>
</head>
<body>
<form id="regexForm">
		<table>
	       	<tr>
	       		<td>
	       			开始：<input id="startRegex" name="startRegex" class="easyui-validatebox" onblur="getResultRegex()" data-options="required:false,prompt:'输入开始字符串'" style="width:200px;"/>
	       			结束：<input id="endRegex" name="endRegex" class="easyui-validatebox" onblur="getResultRegex()" data-options="required:false,prompt:'输入结束字符串'" style="width:200px;"/>
	       			<input id="rowRegex" name="rowRegex" type="hidden"/> 
	       			<a href="#" class="easyui-linkbutton" iconCls="icon-standard-add" onclick="appendRegex();">添加参数</a>
	       		</td>
	       	</tr>
        </table>
      	<table id="dg_regex_param" class="easyui-datagrid" title="已设置的参数列表："  data-options="onClickRow:onclickRegexRow,onBeforeEdit:onRegexBeforeEdit,onAfterEdit:onRegexAfterEdit,rownumbers:true,singleSelect:true,height:250"> 
	        <thead>
	            <tr>
	                <th data-options="field:'regexType',width:150,formatter:htmlEncodeByRegExp">参数</th>
	                <th data-options="field:'regexRepeat',width:150">重复次数</th>
	                <th data-options="field:'delField',formatter:delRegexParamFormatter">操作</th>
	            </tr>
	        </thead>
	    </table>
	    <table>
	    	<tr>
	    		<td><textarea id="resultRegex" name="resultRegex" rows="4" cols="101" placeholder="正则表达式" maxlength="5000"></textarea>
	    		<!-- <td><a href="#" class="easyui-linkbutton" iconCls="icon-hamburg-pencil" onclick="appendRule();">复制</a></td> -->
	    	</tr>
	    </table>
</form>
<script type="text/javascript">
var dg_regex = $('#dg_regex_param');
var lastRegexEditIndex;
var regexType_data = [];
var regexRepeat_data = [];
		if(Sys.chrome)  
		{  
			//alert("chrome");
			//browser.append(not_firefox_firefoxIdCode_NotIeWriter);
		}else if(Sys.firefox)  
		{  
			//alert("firefox");
			$("#resultRegex").attr({rows:"3",cols:"99"});
		}else if(Sys.opera)  
		{  
			//alert("opera");
			browser.append(not_firefox_firefoxIdCode_NotIeWriter);
		}else if(Sys.safari)  
		{  
			//alert("safari");
			browser.append(not_firefox_firefoxIdCode_NotIeWriter);
		}else if(Sys.ie) {  
			//alert("ie");
			$("#resultRegex").attr({cols:"105"});
		}
		else
		{
			alert("未知浏览器");
			browser.append(Ie_firefoxIdCode_IeWriter);
		}
	$.ajax({
    	url:'${pageContext.request.contextPath}/webController/getInitRegexData',
		dataType : 'json',
		success: function (data){
			regexType_data = data.param;
			regexRepeat_data = data.repeat;
		} 
    });
function appendRegex(){
	var rowslength = dg_regex.datagrid('getRows').length;
	dg_regex.datagrid('addEditor',[{
			field : 'regexType', 
			editor : {
				type : 'combobox'
			}
		},{
			field : 'regexRepeat', 
			editor : {
				type : 'combobox',
				options:{
					editable:true
				}
			}
		}
	]); 
	dg_regex.datagrid('appendRow',{});
	onclickRegexRow(rowslength);
}
//操作按钮;
function delRegexParamFormatter(value, row, index)
{
	var delButton = '<a href="javascript:;" onclick=\"deleteRegexRow(this);\"><div class="icon-hamburg-busy" style="width:28px;height:20px" title="删除字段"></div></a>';
	return delButton;
}
function onclickRegexRow(rowIndex, rowData){
	dg_regex.datagrid('clearSelections');
	dg_regex.datagrid('endEdit',lastRegexEditIndex);//结束最后一次编辑;
	lastRegexEditIndex=rowIndex;
	dg_regex.datagrid('beginEdit',rowIndex);//开始编辑;
	var colRegexType = dg_regex.datagrid('getEditor',{index:rowIndex,field: 'regexType' });
	var colRegexRepeat = dg_regex.datagrid('getEditor',{index:rowIndex,field: 'regexRepeat' });
	var regexTypeValue = $(colRegexType.target).combobox('getValue');
	var regexRepeatValue = $(colRegexRepeat.target).combobox('getValue') ? $(colRegexRepeat.target).combobox('getValue') : "";
	$(colRegexType.target).combobox({
		 data:regexType_data,
		 width:150,
		 onChange:function(newValue,oldValue){
			    $(colRegexType.target).combobox('select',newValue);
			    getResultRegex();
		 } 
	});
	$(colRegexRepeat.target).combobox({
		 data:regexRepeat_data,
		 width:150,
		 onChange:function(newValue,oldValue){
			 	$(colRegexRepeat.target).combobox('select',newValue);
			 	getResultRegex();
		 } 
	});
	$(colRegexType.target).combobox('select',regexTypeValue);
	$(colRegexRepeat.target).combobox('select',regexRepeatValue);
}
function getResultRegex(){
	var rows = dg_regex.datagrid('getRows');
	var _list = "[";
	if (rows.length > 0) {
		for (var i = 0; i < rows.length; i++) {
		    var regexTypeValue = "";
		    var regexRepeatValue = "";
		    if(lastRegexEditIndex == i){
		    	var colRegexType = dg_regex.datagrid('getEditor',{index:i,field: 'regexType' });
		    	regexTypeValue = colRegexType ? $(colRegexType.target).combobox('getValue'): "";
		    	var colRegexRepeat =  dg_regex.datagrid('getEditor',{index:i,field: 'regexRepeat' });
		    	regexRepeatValue = colRegexRepeat ? $(colRegexRepeat.target).combobox('getValue') : "";
		    }else{
		    	regexTypeValue = rows[i].regexType;
		    	regexRepeatValue = rows[i].regexRepeat;
		    }
			if(!(regexTypeValue || regexRepeatValue))
				continue;
			if(i > 0)
			_list+=",";
			if(regexTypeValue.indexOf("\"",0) > -1){
				regexTypeValue = regexTypeValue.replaceAll("\"","双引号");
			}
			_list+="{\"regexType\":\""+regexTypeValue+"\",";
			_list+="\"regexRepeat\":\""+regexRepeatValue+"\"}";
		}
    }
	_list+="]";
	$("#rowRegex").val(_list);
	$.ajax({
       	url:'${pageContext.request.contextPath}/webController/getRegexValue',
       	data:$("#regexForm").serialize(),
   		dataType : 'json',
   		type : 'POST',
   		success: function (data){
   			$("#resultRegex").val(data.obj);
   		} 
    });
}
function onRegexBeforeEdit(rowIndex, rowData)
{
}
function onRegexAfterEdit(rowIndex, rowData, changes)
{
}
function deleteRegexRow(obj){
	var rows = dg_regex.datagrid('getRows');
	var index = $(obj).parents("tr:first").attr("datagrid-row-index");
	var rowIndex = dg_regex.datagrid('getRowIndex',rows[index]);
	dg_regex.datagrid('deleteRow',rowIndex);
	if(dg_regex.datagrid('getRows').length > 0){
		getResultRegex();
	}else{
		$("#resultRegex").val("");
	}
}
</script>
</body>
</html>