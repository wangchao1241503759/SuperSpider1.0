<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="fieldmappingTabDiv" class="easyui-tabs" data-options="fit:true,border:false,onSelect:onSelect" style="height: 800px;">
	<div title="页面类型">
		<input type="hidden" id="webTypeRuleJson" name="webTypeRuleJson" value="">
		 <table id="dg_type1" class="easyui-datagrid" 
		            data-options="toolbar:'#tbtype',
		            fit:true,
	                striped:true,
	                checkOnSelect:false,
	                selectOnCheck:true,
		            onBeforeEdit:onBeforeEditType,
		            onAfterEdit:onAfterEditType1,
		            fitColumns:true,
		            onDblClickCell:onDblClickCellType,
		            onClickRow:onClickRow1
 		            "> 
		        <thead>
		            <tr>
		                <th data-options="field:'typeId',width:80,checkbox:true">类型ID</th>
		                <th data-options="field:'typeName',width:250,editor:'textbox'">规则名称</th>
		                <th data-options="field:'typeLevel',width:250,editor:'textbox'">级别</th>
		                <th data-options="field:'isIncrement',width:250,editor:'combobox',editable:false">是否增量</th>
		                <th data-options="field:'typeMatchLabel',width:250,editor:'textbox',formatter:htmlEncodeByRegExp">页面匹配</th>
		                <th data-options="field:'typeFilterRule',width:250,hidden:true,editor:'textbox'">过滤规则</th>
		                <th data-options="field:'action',width:50,formatter:insertMatchLabelFormatter,align:'center'"></th>
		            </tr>
		        </thead>
		    </table>
		    <div id="tbtype" style="padding:2px 5px;">
		        <a href="#" class="easyui-linkbutton" iconCls="icon-standard-add" onclick="append1();">添加页面类型</a>
		        <a href="#" class="easyui-linkbutton" iconCls="icon-standard-delete" onclick="removeit1();">删除页面类型</a>
		        <a href="#" class="easyui-linkbutton" iconCls="icon-hamburg-up" onclick="moveUp();">上移</a>
		        <a href="#" class="easyui-linkbutton" iconCls="icon-hamburg-down" onclick="moveDown();">下移</a>
		    </div>
	</div>
	<div id="regex_param" title="解析参数设置">
			<input type="hidden" id="paramObjectListJson" name="paramObjectListJson" value="">
			 <div id="layout_split" class="easyui-layout" data-options="fit:true">
                <div id="layout_split_north" data-options="region:'north',split:true" style="height:200px">
		             <table class="table table-hover table-condensed" border="0">
						<tr>
							<td style="padding-left: 50px;display: none;">
								是否设置跳转：
					         	<input type="radio" name="jumpType"  value="Y" onclick="enabledInputType2();"/>是
								<input type="radio" name="jumpType"  value="N" checked="checked" onclick="disabledInputType2();"/>否
								<input type="hidden" id="attr" name="attr" value="" />
							</td>
							<td style="padding-left: 50px;display: none;">
								前缀：
					         	<input id="jumpParam" name="jumpParam" onblur="storeParamObjectTemp();" type="text"  placeholder="请输入前缀" class="easyui-validatebox span2" data-options="required:false" value="" maxlength="50" style="width:150px;height:22px;">
								"下一页"按钮：
					         	<input id="incrementationParam" name="incrementationParam" onblur="storeParamObjectTemp();" type="text" placeholder="请输入下一页按钮" class="easyui-validatebox span2" data-options="required:false" value="" maxlength="50" style="width:150px;height:22px;">
							</td>
						</tr>
					</table>
					<table id="dg_type2" class="easyui-datagrid" style="height:160px"
			            data-options="
			                fitColumns:true,
			                fit:true,
			                iconCls: 'icon-edit',
			                toolbar: '#ft',
			                striped:true,
			                checkOnSelect:false,
			                selectOnCheck:true,
							onDblClickCell:onDblClickCell,
			                onEndEdit: onEndEdit,
			                onBeforeEdit:onBeforeEditParam,
			                onAfterEdit:onParamAfterEdit,
			                onClickRow:onClickRow
			            ">
					        <thead>
					            <tr>
					            	<th data-options="field:'id',title:'字段ID',checkbox:true">字段ID</th>
					                <th data-options="field:'name',width:150,editor:'textbox'">字段名称</th>
					                <th data-options="field:'xpath',width:400,editor:'textbox',formatter:htmlEncodeByRegExp">xpath</th>
					                <th data-options="field:'regex',width:300,hidden:false,editor:'textbox',formatter:htmlEncodeByRegExp">正则表达式</th>
					                <th data-options="field:'exp',width:250,hidden:false,editor:'textbox',formatter:htmlEncodeByRegExp">解析表达式</th>
					                <th data-options="field:'content',width:80,editor:'combobox',editable:false">提取内容</th>
					                <th data-options="field:'isAutoJoin',width:100,editor:'combobox',editable:false">关联字段</th>
					                <th data-options="field:'isMultValue',width:100,editor:'combobox',editable:false">是否多值</th>
					                <th data-options="field:'extractFieldLength',width:100,editor:'textbox'">提取字段长度</th>
					                <th data-options="field:'fieldExp',hidden:true,width:100,editor:'textbox'">转换表达式</th>
					                <th data-options="field:'targetFieldName',hidden:true,width:100,editor:'textbox'">目标字段类型名称</th>
					                <th data-options="field:'targetFieldType',hidden:true,width:100,editor:'textbox'">目标字段类型</th>
					                <th data-options="field:'urlAttr',hidden:true,width:100,editor:'textbox'">链接属性</th>
					            </tr>
					        </thead>
					    </table>
					    <div id="ft" style="padding:2px 5px;">
					    <select id="typeList" name="typeList" class="easyui-combobox" style="width:150px;height:26px;panelHeight:100;">
					            </select>
					        <a href="#" class="easyui-linkbutton" iconCls="icon-standard-add" onclick="append();">添加提取字段</a>
					        <a href="#" class="easyui-linkbutton" iconCls="icon-standard-delete" onclick="removeit();">删除提取字段</a>
					        <!-- <a href="#" class="easyui-linkbutton" data-options="align:'right',iconCls:'icon-hamburg-exchange'" onclick="expandLevel();" >扩大一级</a> -->
					        <a href="#" class="easyui-linkbutton" data-options="align:'right',iconCls:'icon-hamburg-graphic'" onclick="regexDialog();" >正则表达式生成器</a>
					       	<span style="float:right;padding-right:10px;"><input type="checkbox" id="advancedAttr" name="advancedAttr" value="N" checked="checked" onclick="storeParamObjectTemp()">高级属性</input></span>
					    </div>
					
                </div>
                <div id="layout_split_center" data-options="region:'center'" style="height:100%;">
					<div id="browser" style="height: 800px;padding: 2px;">
								<span style="display: none">
								选择页面元素：
					         	<input type="radio" name="chooseElement"  value="field" checked="checked"/>提取字段
								<input type="radio" name="chooseElement"  value="nextPage" />"下一页"按钮
								</span>
					</div>
                </div>
            </div>
			


	</div>
	<div id="trans" title="字段映射与转换">
		 <input type="hidden" id="fieldMappingJson" name="fieldMappingJson" value="">
		 <table id="webfieldmapping" class="easyui-datagrid" data-options="rownumbers:true,toolbar:'#tb3',
		            fit:true,
		            fitColumns:true,
		            onDblClickRow: onFieldMappingDblClickRow,
		            onBeforeEdit:onFieldMappingBeforeEdit,
                    onAfterEdit:onFieldMappingAfterEdit,
                    onClickRow:onFieldMappingClickRow,
                    checkOnSelect:false,
					selectOnCheck:true,
					singleSelect:false,
                    striped:true,
                    enableHeaderClickMenu:false,
					enableHeaderContextMenu:false,
					enableRowContextMenu:false,
					onCheck:onCheckFieldMapping,
					onCheckAll:onCheckAllFieldMapping,
					onUncheck:onUnCheckFieldMapping,
					onUncheckAll:onUnCheckAllFieldMapping
 		            "> 
		        <thead>
		            <tr>
		                <th data-options="field:'fieldID',title:'字段ID',checkbox:true">字段ID</th>
						<th data-options="field:'fieldName',title:'字段名称',width:10">字段名称</th>
						<th data-options="field:'fieldExp',title:'字段转换表达式',width:30,editor:{type:'text'}">字段转换表达式</th>
						<th data-options="field:'filedSource',title:'字段来源',width:5">字段来源</th>
						<th data-options="field:'targetFieldName',title:'目标字段名称',width:10,editor:{type:'combobox',options:{valueField:'colvalue',textField:'colname',editable:false}}">目标字段名称</th>
						<th data-options="field:'targetFieldType',title:'目标字段类型',width:10">目标字段类型</th>
						<th data-options="field:'delField',formatter:delFieldFormatter">操作</th>
		            </tr>
		        </thead>
		    </table>
		    <div id="tb3" style="padding:2px 5px;">
		        <a href="#" class="easyui-linkbutton" onclick="appendfiledMappingRow();" data-options="align:'right',iconCls:'icon-standard-add'">添加转换字段</a>
				<a href="#" class="easyui-linkbutton" onclick="fieldMappingSameNameMatch();" data-options="align:'right',iconCls:'icon-hamburg-exchange'">同名自动匹配</a>
				<span style="color: green" id="matchTips"></span>
				<span style="color: red" id="mssMatchTips"></span>
		    </div>
	</div>
</div>



<div id="ppg" class="easyui-dialog" title="设置匹配规则" style="width:800px;height:400px;padding:10px"
            data-options="modal : true,
                buttons: '#ppg-buttons',
                onClose:onClose
            ">
            <table>
            	<tr>
            		<td>
            			选择规则类型：<select id="ruleType" class="easyui-combobox" name="ruleType" data-options="data:ruleData" style="width:200px;"></select>
            			<input id="ruleParam" name="ruleParam" class="easyui-validatebox" data-options="required:false,prompt:'输入链接匹配字符串'" style="width:200px;"/>
            			<a href="#" class="easyui-linkbutton" iconCls="icon-standard-add" onclick="appendRule();">添加规则</a>
		        		<a href="#" class="easyui-linkbutton" iconCls="icon-standard-delete" onclick="deleteRule();">删除规则</a>
            		</td>
            	</tr>
            </table>
       		 <table id="dg_rule" class="easyui-datagrid" title="已设置的规则列表：" 
		            data-options="rownumbers:true,height:250
 		            "> 
		        <thead>
		            <tr>
		                <th data-options="field:'id',width:80,checkbox:true">类型ID</th>
		                <th data-options="field:'ruleType',width:250,editor:'textbox'">规则类型</th>
		                <th data-options="field:'ruleTypeValue',width:250,editor:'textbox',formatter:htmlEncodeByRegExp">规则参数</th>
		            </tr>
		        </thead>
		    </table>
		    <table>
		    	<tr>
		    		<td>
		    			过滤规则选项：
		    			<input type="radio" name="filterRule" value="or" checked="checked"/>单项符合
						<input type="radio" name="filterRule" value="and" />全部符合
		    		</td>
		    	</tr>
		    </table>
</div>
<div id="ppg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:insertDataToRuleList()">确认</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:$('#ppg').dialog('close');">取消</a>
</div>

<script type="text/javascript">
var lastIndex;
//存储解析参数设置的数据
var paramObjectList = [];


var outColumns = [];//输出任务的列数据;
var outColumnsTypes = [];//输出任务的列数据类型;
var fieldMappingJsonList = []; //字段映射列表
var checkfieldMappingJsonList = []; //字段映射列表
$(function(){
	

	$('#ppg').dialog('close');
	onClose();
	onChange();
	
	//判断不同的浏览器
	var browser = $("#browser");
	//IE内核ID(非IE写法)
	var browserObjectNotIE = "<object "+
		" class=\"notIE\""+
		" id=\"BrowserControl\""+
		" TYPE=\"application/x-itst-activex\""+
		" ALIGN=\"baseline\" BORDER=\"0\""+
		" WIDTH=\"100%\" HEIGHT=\"100%\""+
		" clsid=\"{6CB7BF91-C158-41EE-A38E-A79F3708584A}\""+
		" event_OnElementSelected=\"OnSelected\">"+
		"</object>";
	
	//火狐内核插件ID（IE写法）
	//(火狐浏览器加载IE内核ID，用非IE的HTML写法)（非火狐浏览器加载火狐内核ID，IE浏览器使用IE的HTML写法，其他使用非IE写法）
	var browserObjectIE =" <object "+
			" id=\"BrowserControl\""+ 
			" TYPE=\"application/x-itst-activex\""+
			" classid=\"clsid:D2600156-B400-4972-A82D-1461C443EF63\""+
			" Width=\"100%\""+
			" Height=\"100%\">"+
		" </object>";


		
		
		
	//(火狐浏览器加载IE内核ID，用非IE的HTML写法)	(火狐使用)
	var firefox_IEIdCode_NotIeWriter="<object "+
// 	" class=\"notIE\""+
	" id=\"BrowserControl\""+
	" TYPE=\"application/x-itst-activex\""+
	" ALIGN=\"baseline\" BORDER=\"0\""+
	" WIDTH=\"100%\" HEIGHT=\"100%\""+
	" clsid=\"{6CB7BF91-C158-41EE-A38E-A79F3708584A}\""+
	" event_OnElementSelected=\"OnSelected\">"+
	"</object>";
	
	//非火狐浏览器加载火狐内核ID,非IE浏览器,使用非IE写法(非火狐非IE使用)
	var not_firefox_firefoxIdCode_NotIeWriter="<object "+
// 	" class=\"notIE\""+
	" id=\"BrowserControl\""+
	" TYPE=\"application/x-itst-activex\""+
	" ALIGN=\"baseline\" BORDER=\"0\""+
	" WIDTH=\"100%\" HEIGHT=\"100%\""+
	" clsid=\"{D2600156-B400-4972-A82D-1461C443EF63}\""+
	" event_OnElementSelected=\"OnSelected\">"+
	"</object>";
	
	
	//非火狐浏览器加载火狐内核ID,IE浏览器,使用IE写法(IE使用)
	var Ie_firefoxIdCode_IeWriter=" <object "+
	" id=\"BrowserControl\""+ 
	" TYPE=\"application/x-itst-activex\""+
	" classid=\"clsid:D2600156-B400-4972-A82D-1461C443EF63\""+
	" Width=\"100%\""+
	" Height=\"100%\">"+
" </object>";
	
	
   	try{
   		if(Sys.chrome)  
   		{  
   			//alert("chrome");
			browser.append(firefox_IEIdCode_NotIeWriter);
   		}else if(Sys.firefox)  
   		{  
   			//alert("firefox");
   			browser.append(firefox_IEIdCode_NotIeWriter);
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
   			browser.append(Ie_firefoxIdCode_IeWriter);
   		}
   		else
   		{
   			alert("未知浏览器");
   			browser.append(Ie_firefoxIdCode_IeWriter);
   		}
     }catch(err) {
    	 alert(err.message);
    	 //browser.append(not_firefox_firefoxIdCode_NotIeWriter);
     }
	
	initWebTypeRuleGrid();
	disabledInputType2();
	onkeydown();

});
onkeydown();
	//检测是否安装了浏览器控件
	function checkBrowers()
	{
		var checkBrowser = browserInstalled();
		if(!checkBrowser)
		{
			parent.$.messager.confirm('提示', '您未安装操作浏览器插件,是否下载安装？', function(data) {
				if (data) {
					var download_url = '${pageContext.request.contextPath}/webController/download';
					window.open(download_url, "_blank");
				}
			});
		}	
	}	

	/**
	*回车事件
	*/
	function onkeydown(){
		document.onkeydown = function(e){ 
		    var ev = document.all ? window.event : e;
		    if(ev.keyCode==13) {

		    	if(tabIndex==0)
		    	{
		    		$('#dg_type1').datagrid('acceptChanges');  
		    		$('#dg_type1').datagrid('endEdit', editIndexType);
		    		var rows = $('#dg_type1').datagrid('getRows');
		    		if(rows.length>0)
		    		{
		    			if(rows.length-1 != editIndexType)
		    			{
		    				editIndexType = editIndexType+1;
		    				$('#dg_type1').datagrid('validateRow',editIndexType);
		    			}
		    		}
		    	}
		    	else if(tabIndex==1)
		    	{
		    		$('#dg_type2').datagrid('acceptChanges');  
		    		$('#dg_type2').datagrid('endEdit', editIndex);
		    		var rows = $('#dg_type2').datagrid('getRows');
		    		if(rows.length>0)
		    		{
		    			if(rows.length-1 != editIndex)
		    			{
		    				editIndex = editIndex+1;
		    				$('#dg_type2').datagrid('validateRow',editIndex);
		    			}
		    		}
		    	}
		     }
		}
	}


//修改时初始化页面类型规则
function initWebTypeRuleGrid()
{
// 	var webTypeRuleList = eval('${webTypeRuleListJson}');
// 	if(webTypeRuleList){
// 		 dg_type1.datagrid('loadData',webTypeRuleList);
// 	}
// 	var fieldMappingListJson = eval('${fieldMappingListJson}');
// 	if(fieldMappingListJson){
// 		 $("#webfieldmapping").datagrid('loadData',fieldMappingListJson);
// 	}
	var taskId = '${task.taskId}';
	var url='${pageContext.request.contextPath}/webController/getWebParamSettingList';
	$.ajax({
		type:'post',
		url:url,
		data:{taskId:taskId},
		dataType:'json',
		success:function(data){
			paramObjectList = data
			
			//替换html标签字符串"<",">"
// 			for(var i=0;i<paramObjectList.length;i++)
// 			{
// 				var fieldExtractRows = paramObjectList[i].fieldExtractList;
// 				if(fieldExtractRows.length > 0){
// 					for(var j=0;j<fieldExtractRows.length;j++)
// 					{
// 						var xpath = fieldExtractRows[j].xpath;
// 						if(xpath!=null && xpath!='' && xpath.length>0)
// 						{
// 							xpath = xpath.replaceAll(/</g, "&lt;").replaceAll(/>/g, "&gt;");
// 						}
// 						fieldExtractRows[j].xpath = xpath;
						
// 						var regex = fieldExtractRows[j].regex;
// 						if(regex!=null && regex!='' && regex.length>0)
// 						{
// 							regex = regex.replaceAll(/</g, "&lt;").replaceAll(/>/g, "&gt;");
// 						}
// 						fieldExtractRows[j].regex = regex;
						
// 						var exp = fieldExtractRows[j].exp;
// 						if(exp!=null && exp!='' && exp.length>0)
// 						{
// 							exp = exp.replaceAll(/</g, "&lt;").replaceAll(/>/g, "&gt;");
// 						}
// 						fieldExtractRows[j].exp = exp;
// 					}
// 					paramObjectList[i].fieldExtractList = fieldExtractRows;
// 				}
// 			}
		},
		error:function(){
			alert("连接后台出错！")
		}
	});
	
	var url='${pageContext.request.contextPath}/webController/getWebTypeRuleList';
	$.ajax({
		type:'post',
		url:url,
		data:{taskId:taskId},
		dataType:'json',
		success:function(data){
			webTypeRuleList = data
			dg_type1.datagrid('loadData',webTypeRuleList);
		},
		error:function(){
			alert("连接后台出错！")
		}
	});
	
	var url='${pageContext.request.contextPath}/webController/getFieldMappingList';
	$.ajax({
		type:'post',
		url:url,
		data:{taskId:taskId},
		dataType:'json',
		async:false,
		success:function(data){
			fieldMappingJsonList = data;
			//重新赋值最新的转换字段
			zhuanRows = [];
			for(var j=0;j<fieldMappingJsonList.length;j++){
				var row = fieldMappingJsonList[j];
				if(row.filedSource == "转换字段"){
					zhuanRows.push(row);
				}
				if(row.fieldStatus=="1")
				{
					checkfieldMappingJsonList.push(row);
				}
			}
			$("#webfieldmapping").datagrid('loadData',data);
		},
		error:function(){
			alert("连接后台出错！")
		}
	});
	
	initOutputColumn();
	
}

function regexDialog() {
	d=$("#planDlg").dialog({   
	    title: '正则表达式生成器',    
	    width: 650,
	    height: 440,    
	    href:"${pageContext.request.contextPath}/webController/forRegex",
	    modal:true,
	    onOpen:function(){
	    	$("#browser").hide();
	    },
	    onClose:function(){
	    	$("#browser").show();
	    },
	    buttons:[{
			text:'确定',
			handler:function(){
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
//初始化输出任务数据库列数据;
function initOutputColumn()
{
	var rows = $('#webfieldmapping').datagrid('getRows');
	var checkedRows = $('#webfieldmapping').datagrid("getChecked");
	for(var i=0 ; i<rows.length;i++){
		var row = rows[i];
		//清除之前选择表匹配的内容
		row.targetFieldName = '';
		row.targetFieldType = '';
		onFieldMappingAfterEdit('',row,'');
		$('#webfieldmapping').datagrid('refreshRow',i);
	}
	for(var i = 0 ; i<checkedRows.length ; i++)
	{
		var checkedRowIndex = $('#webfieldmapping').datagrid('getRowIndex',checkedRows[i]);
		$("#webfieldmapping").datagrid("checkRow",checkedRowIndex);
	}
	
	$.ajax({
    	url:'${pageContext.request.contextPath}/taskoutput/getOutputAllColumns',
    	data:$("#configForm").serialize(),
		dataType : 'json',
		type : 'POST',
		async:false,
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

//定义下拉规则对象
function ruleTypeObject(value,text)
{
	this.value=value;
	this.text=text;
}

//选择tab页签
var zhuanRows = [];
var tabIndex;
function onSelect(title,index)
{
	tabIndex = index;
	$('#dg_type1').datagrid('acceptChanges');
	$('#dg_type2').datagrid('acceptChanges');
	$("#webfieldmapping").datagrid('acceptChanges');
	if(endEditing())
	{
		if(index==1)
		{
			checkBrowers();
			var type1_rows = dg_type1.datagrid('getRows');
			var data_type = [];
			for(var i=0;i<type1_rows.length;i++)
			{
				var ruletypeobject = new ruleTypeObject(type1_rows[i].typeId,type1_rows[i].typeName);
				data_type.push(ruletypeobject);
			}
	
			$("#typeList").combobox('loadData',data_type);
			$("#typeList").combobox('setValue',data_type[0].value);
			
			//其的tab页签有改变数据，需要加载最新的
			$("#dg_type2").datagrid('loadData',[]); //清空列表中的数据
			$("#jumpParam").val('');
			$("#incrementationParam").val('');
			$("input:radio[name='jumpType'][value='N']").prop("checked",'checked');
			$("#advancedAttr").prop("checked",'');
			disabledInputType2();
			for(var i=0;i<paramObjectList.length;i++)
			{
				if(paramObjectList[i].typeId==data_type[0].value)
				{
					//$("#typeList").combobox('setValue',paramObjectList[i].typeId);
					if(paramObjectList[i].jumpType=='Y')
					{
						enabledInputType2();
					}
					$("input:radio[name='jumpType'][value='"+paramObjectList[i].jumpType+"']").prop("checked",'checked');
					$("#jumpParam").val(paramObjectList[i].jumpParam);
					$("#incrementationParam").val(paramObjectList[i].incrementationParam);
					$("#attr").val(paramObjectList[i].attr);
					if(paramObjectList[i].advancedAttr=='Y')
					{
						$("#advancedAttr").prop("checked",'checked');
						showColumn();
					}
					else
					{
						$("#advancedAttr").prop("checked",'');
						hideColumn();
					}
					$('#dg_type2').datagrid('loadData',	paramObjectList[i].fieldExtractList);
				}
			}
		}else if(index == 2)
		{
			zhuanRows = [];
			var fieldMappingRows = $('#webfieldmapping').datagrid('getRows');
			for(var j=0;j<fieldMappingRows.length;j++){
				var row = fieldMappingRows[j];
				if(row.filedSource == "转换字段"){
					zhuanRows.push(row);
				}
			}
			$('#webfieldmapping').datagrid('loadData',[]);
			for(var i=0;i<paramObjectList.length;i++)
			{
				var fieldExtractRows = paramObjectList[i].fieldExtractList;
				if(fieldExtractRows.length > 0){
					for(var j=0;j<fieldExtractRows.length;j++)
					{
						$('#webfieldmapping').datagrid('appendRow',{
							fieldID:fieldExtractRows[j].id,
							fieldName:fieldExtractRows[j].name,
							fieldExp:fieldExtractRows[j].fieldExp,
							targetFieldName:fieldExtractRows[j].targetFieldName,
							targetFieldType:fieldExtractRows[j].targetFieldType,
							filedSource:'源字段'
						});
					}
				}
			}
			for(var i = 0 ;i<zhuanRows.length;i++){
				$('#webfieldmapping').datagrid('appendRow',zhuanRows[i]);
			}
			
			//把已选择的记录勾上
			var rows = $('#webfieldmapping').datagrid('getRows');
			for(var i = 0 ; i<rows.length ; i++)
			{
				var row = rows[i];
				for(var j = 0 ; j<checkfieldMappingJsonList.length; j++)
				{
					if(row.fieldName == checkfieldMappingJsonList[j].fieldName && checkfieldMappingJsonList[j].fieldStatus=="1"){
						$('#webfieldmapping').datagrid('checkRow',i);
						break;
					}
				}
			}
		}
	}
}
/**
 * 获取当前标签
 */
function getTabIndex()
{
	var tab = $('#fieldmappingTabDiv').tabs('getSelected');
	var index = $('#fieldmappingTabDiv').tabs('getTabIndex',tab);
	$('#dg_type1').datagrid('acceptChanges');
	$('#dg_type2').datagrid('acceptChanges');
	$("#webfieldmapping").datagrid('acceptChanges');
}
//字段映射操作按钮;
function delFieldFormatter(value, row, index)
{
	var delButton = "";
	if(row.filedSource == "转换字段")
	{
		delButton = '<a href="javascript:;" onclick=\"deleteFiledMappingRow(this);\"><div class="icon-hamburg-busy" style="width:28px;height:20px" title="删除字段"></div></a>';
	}
	return delButton;
}
//添加一行转换字段;
function appendfiledMappingRow(){
	var rowslength = $('#webfieldmapping').datagrid('getRows').length;
	$('#webfieldmapping').datagrid('appendRow',{fieldName:"NewField"+(rowslength+1),filedSource:'转换字段',targetFieldType:""});
	var editIndex = rowslength;
	onFieldMappingDblClickRow(editIndex);
}

//双击行点击事件;
function onFieldMappingDblClickRow(rowIndex){
	var fieldMappingDg = $("#webfieldmapping");
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
			}
		]); 
    }else{
    	fieldMappingDg.datagrid('removeEditor',[{
    			field : 'fieldName'
    		}
    	]);
    }
	fieldMappingDg.datagrid('endEdit',lastIndex);
	lastIndex=rowIndex;
	fieldMappingDg.datagrid('beginEdit',rowIndex);
    var colEd = fieldMappingDg.datagrid('getEditor',{index:rowIndex,field: 'targetFieldName' });
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
					var checkedRows = $('#webfieldmapping').datagrid('getChecked');
					var isHave = false;
					for(var i = 0 ; i < checkedRows.length ; i++){
						var checkedRow = checkedRows[i];
						var targetFieldName = checkedRow.targetFieldName;
						var checkedRowIndex =  $('#webfieldmapping').datagrid('getRowIndex',checkedRow);
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

//存储映射字段
function storeWebFieldMapping()
{
	fieldMappingJsonList = [];
	$("#webfieldmapping").datagrid('acceptChanges');
	fieldMappingJsonList = $("#webfieldmapping").datagrid('getRows');
	
	//重新赋值最新的转换字段
	zhuanRows = [];
	var fieldMappingRows = $('#webfieldmapping').datagrid('getRows');
	for(var j=0;j<fieldMappingRows.length;j++){
		var row = fieldMappingRows[j];
		if(row.filedSource == "转换字段"){
			zhuanRows.push(row);
		}
	}
	//$("#fieldMappingJson").val(fieldMappingJson);	
}



//定义字段映射对象
function fieldMappingObject(fieldID,fieldName,fieldExp,filedSource,targetFieldName,targetFieldType,fieldStatus)
{
	this.fieldID = fieldID;
	this.fieldName = fieldName;
	this.fieldExp =  fieldExp;
	this.filedSource = filedSource;
	this.targetFieldName = targetFieldName;
	this.targetFieldType = targetFieldType;
	this.fieldStatus = fieldStatus;
}

//获取映射字段的所有数据
function getFieldMappingList()
{
	fieldMappingJsonList = [];
	//var checkRows = $("#webfieldmapping").datagrid('getChecked');
	for (var i = 0; i < paramObjectList.length; i++) {
		var fieldExtractRows = paramObjectList[i].fieldExtractList;
		if (fieldExtractRows.length > 0) {
			for (var j = 0; j < fieldExtractRows.length; j++) {
				//设置哪些是选中记录
				var fieldStatus = null;
				for(var k=0;k<checkfieldMappingJsonList.length;k++)
				{
					if(fieldExtractRows[j].name==checkfieldMappingJsonList[k].fieldName)
					{
						fieldStatus = "1";
						break;
					}
				}
				var fieldMapping = new fieldMappingObject('',fieldExtractRows[j].name,fieldExtractRows[j].fieldExp,'源字段',fieldExtractRows[j].targetFieldName,fieldExtractRows[j].targetFieldType,fieldStatus);
				fieldMappingJsonList.push(fieldMapping);
			}
		}
	}
	
	for(var i = 0 ;i<zhuanRows.length;i++){
		var fieldStatus = null;
		for(var k=0;k<checkfieldMappingJsonList.length;k++)
		{
			if(zhuanRows[i].fieldName==checkfieldMappingJsonList[k].fieldName)
			{
				fieldStatus = "1";
				break;
			}
		}
		var fieldMapping = new fieldMappingObject('',zhuanRows[i].fieldName,zhuanRows[i].fieldExp,'转换字段',zhuanRows[i].targetFieldName,zhuanRows[i].targetFieldType,fieldStatus);
		fieldMappingJsonList.push(fieldMapping);
		//fieldMappingJsonList.push(zhuanRows[i]);
	}
}

var mylist = new Array();

var contents = [
	    		    {value:'文本内容',text:'文本内容'},
	    		    {value:'附件',text:'附件'},
	    		    {value:'链接地址',text:'链接地址'},
	    		    {value:'自动挖掘',text:'自动挖掘'}
	    	   ];
var isAutoJoin_data = [
	    		    {value:'是',text:'是'},
	    		    {value:'否',text:'否'}
	    	   ];
var isMultValue_data = [
	    		    {value:'是',text:'是'},
	    		    {value:'否',text:'否'}
	    	   ];
var isIncrement_data = [
	    		    {value:'是',text:'是'},
	    		    {value:'否',text:'否'}
	    	   ];
var xpathType_data = [
	    		    {value:'xpath',text:'xpath'},
	    		    {value:'正则',text:'正则'}
	    	   ];



var dg_type1 = $('#dg_type1');
var editIndexType = undefined;
var isChecked_type;
function endEditing1(){
	if (editIndexType == undefined){return true}
	if($('#dg_type1').datagrid('validateRow',editIndexType))
	{
		$('#dg_type1').datagrid('endEdit', editIndexType);
		editIndexType = undefined;
        return true;
	}
	else
	{
		return false;
	}
}


function uuid() {
	var s = [];
	var hexDigits = "0123456789abcdef";
	for (var i = 0; i < 32; i++) {
	s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
	}
	s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
	s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
	s[8] = s[13] = s[18] = s[23];

	var uuid = s.join("");
	return uuid;
}

function append1(){
	if (endEditing1())
	{
		$('#dg_type1').datagrid('uncheckAll').datagrid('unselectAll');
		var count = $('#dg_type1').datagrid('getRows').length+1;
	    $('#dg_type1').datagrid('appendRow',{typeId:uuid(),typeName:'规则名称'+count,typeLevel:count,isIncrement:'否',typeMatchLabel:'','action':'...'});
	    var index = $('#dg_type1').datagrid('getRows').length-1;
	    $('#dg_type1').datagrid('uncheckAll').datagrid('unselectAll');
	    $('#dg_type1').datagrid('refreshRow',index);
	    onDblClickCellType(index);
	}
}


function onDblClickCellType(index){
	storeWebTypeRule(index);
        if (endEditing1())
       	{
        	editIndexType = index;
			$('#dg_type1').datagrid('addEditor',[
        		{
   			  			field : 'typeName', 
   			  			editor : {
   			  				type : 'validatebox',
   			  				options:{
//    			  					validType : 'email',
   			  					validType : "checkTypeDataGrid['#dg_type1','typeName',editIndexType]",
   			  					required:true
   			  				}
   			  			}
   				},{                                  
			  			field : 'typeLevel', 
			  			editor : {
			  				type : 'validatebox',
			  				options:{
			  					validType : "checkLevelDataGrid['#dg_type1','typeLevel',editIndexType]",
			  					required:true
			  				}
			  			}
   				},{                                  
			  			field : 'isIncrement', 
			  			editor : {
			  				type : 'combobox',
			  				options:{
								editable : false,
								panelHeight : 100,
								required:true
			  				}
			  			}
				}
//    				,{
// 			  			field : 'typeMatchLabel', 
// 			  			editor : {
// 			  				type : 'validatebox',
// 			  				readonly:true,
// 			  				options:{
// 			  					required:true
// 			  				}
// 			  			}
// 				}
			]); 
			 $('#dg_type1').datagrid('selectRow', index)
	         .datagrid('beginEdit', index);
			 
				var isIncrement = $('#dg_type1').datagrid('getEditor', {
					index : index,
					field : 'isIncrement'
				});
				$(isIncrement.target).combobox('loadData', isIncrement_data);
        }
}

//数据删除
Array.prototype.del = function(dx) 
{ 
    if(isNaN(dx)||dx>this.length){return false;} 
    this.splice(dx,1); 
};

function removeit1(index){
	var rows = $('#dg_type1').datagrid('getChecked');
	if(rows.length>0)
	{
		
		$.messager.confirm('提示','删除后无法恢复您确定要删除？', function(data){
			if(data){
			    //alert(paramObjectList.length+":"+webTypeRuleList.length)
// 			    if(rows.length==0)
// 			    {
// 					dg_type1.datagrid('deleteRow',ruleIndex);
// 			    }
			    for(var i=0;i<rows.length;i++)
			    {
			    	var rowsTypeId = rows[i].typeId;
			        var rowIndex = $('#dg_type1').datagrid('getRowIndex', rows[i]);
			        $('#dg_type1').datagrid('deleteRow', rowIndex);
			        $('#dg_type1').datagrid('uncheckAll').datagrid('unselectAll');
			        //数据组中相应的数据
			        for(var index=0;index<paramObjectList.length;index++)
					{
						if(paramObjectList[index].typeId==rowsTypeId)
						{
							paramObjectList.del(index);
							//alert(paramObjectList)
							break;
						}
					}
			        //数据组中相应的数据
			        for(var index=0;index<webTypeRuleList.length;index++)
					{
						if(webTypeRuleList[index].typeId==rowsTypeId)
						{
							webTypeRuleList.del(index);
						}
					}
			    }
			    //alert(paramObjectList.length+":"+webTypeRuleList.length)
			}});
	}
	else
	{
		$.messager.alert('提示','请选择您需要删除的数据？','info');
	}

}

//这个方法没有效
// Array.prototype.del=function(n){
// //n表示第几项，从0开始算起。  
// //prototype为对象原型，注意这里为对象增加自定义方法的方法。  
// 	if (n < 0)//如果n<0，则不进行任何操作。  
// 	{
// 		return this;
// 	}
// 	else
// 	{
// 		return this.slice(0, n).concat(this.slice(n + 1, this.length));
// 	}
// }

	function onClickRow1(rowIndex, rowData) {
		endEditing1();
		var isChecked_type = RowIsChecked($("#dg_type1"),rowIndex);
		if(!isChecked_type){
			$("#dg_type1").datagrid("uncheckRow",rowIndex);
		}else{
			$("#dg_type1").datagrid("checkRow",rowIndex);
		}
	}

	function moveUp() {
		if (endEditing1())
		{
			var rows = dg_type1.datagrid('getRows');
			var count = rows.length;
			if (count >= 2) {
				var checkedRows = dg_type1.datagrid('getChecked');
				var checkedLength = checkedRows.length;
				if (checkedLength == 0) {
					$.messager.alert('提示', '请选择需要移动的数据', 'info');
					return;
				}
	
				if (checkedLength > 1) {
					$.messager.alert('提示', '请选择单条数据进行移动', 'info');
					return;
				}
				if (checkedLength == 1) {
					var checkIndex = dg_type1.datagrid('getRowIndex',
							checkedRows[0]);
					if (checkIndex == 0) {
						return;
					}
					var beforeIndex = checkIndex - 1;
					var beforeRow = rows[beforeIndex];
	
					var beforeId = beforeRow.typeId;
					var beforeName = beforeRow.typeName;
					var beforeLevel = checkedRows[0].typeLevel;
					var beforeisIncrement = beforeRow.isIncrement;
					var beforeMatchLabel = beforeRow.typeMatchLabel;
	
					var currentRow = checkedRows[0];
					currentRow.typeLevel = beforeRow.typeLevel;
					dg_type1.datagrid('updateRow', {
						index : beforeIndex,
						row : currentRow
					});
					dg_type1.datagrid('updateRow', {
						index : checkIndex,
						row : {
							typeId : beforeId,
							typeName : beforeName,
							typeLevel : beforeLevel,
							isIncrement : beforeisIncrement,
							typeMatchLabel : beforeMatchLabel
						}
					});
					dg_type1.datagrid('refreshRow', checkIndex);
					dg_type1.datagrid('refreshRow', beforeIndex);
					dg_type1.datagrid('uncheckRow', checkIndex);
					dg_type1.datagrid('checkRow', beforeIndex);
					dg_type1.datagrid('selectRow', beforeIndex);
	
				}
	
			} else {
				$.messager.alert('提示', '不能进行移动操作', 'info');
			}
		}

	}

	function moveDown() {
		if (endEditing1())
		{
			var rows = dg_type1.datagrid('getRows');
			var count = rows.length;
			if (count >= 2) {
				var checkedRows = dg_type1.datagrid('getChecked');
				var checkedLength = checkedRows.length;
				if (checkedLength == 0) {
					$.messager.alert('提示', '请选择需要移动的数据', 'info');
					return;
				}
	
				if (checkedLength > 1) {
					$.messager.alert('提示', '请选择单条数据进行移动', 'info');
					return;
				}
				if (checkedLength == 1) {
					var checkIndex = dg_type1.datagrid('getRowIndex',
							checkedRows[0]);
					if (checkIndex == count) {
						return;
					}
					var afterIndex = checkIndex + 1;
					var afterRow = rows[afterIndex];
	
					var afterId = afterRow.typeId;
					var afterName = afterRow.typeName;
					var afterLevel = checkedRows[0].typeLevel;
					var afterisIncrement = afterRow.isIncrement;
					var afterMatchLabel = afterRow.typeMatchLabel;
	
					var currentRow = checkedRows[0];
					currentRow.typeLevel = afterRow.typeLevel;
					dg_type1.datagrid('updateRow', {
						index : afterIndex,
						row : currentRow
					});
					dg_type1.datagrid('updateRow', {
						index : checkIndex,
						row : {
							typeId : afterId,
							typeName : afterName,
							typeLevel : afterLevel,
							isIncrement : afterisIncrement,
							typeMatchLabel : afterMatchLabel
						}
					});
					dg_type1.datagrid('refreshRow', checkIndex);
					dg_type1.datagrid('refreshRow', afterIndex);
					dg_type1.datagrid('uncheckRow', checkIndex);
					dg_type1.datagrid('checkRow', afterIndex);
					dg_type1.datagrid('selectRow', afterIndex);
				}
	
			} else {
				$.messager.alert('提示', '不能进行移动操作', 'info');
			}
		}
	}

	//操作按钮;
	var ruleIndex;
	var typeMatchLabel;
	var typeFilterRule;
	function insertMatchLabelFormatter(value, row, index) {
		typeMatchLabel = row.typeMatchLabel;
		typeFilterRule = row.typeFilterRule;
		var openButton = "<a href=\"javascript:void(0);\" onclick=\"openDialog('"
				+ index
				+ "');\" style=\"text-decoration: none; \">...</a>";
		return openButton;
	}
	
	function htmlEncodeByRegExp(value, row, index)
	{
		var str="";
		if(value!=null && value !="")
		{
			str = HtmlUtil.htmlEncodeByRegExp(value);
		}
		return str;
	}

	//打开弹出窗口
	function openDialog(index) {
		if (endEditing1())
		{
			ruleIndex = index;
			var dg_type1_rows = dg_type1.datagrid('getRows');
			var currentRow = dg_type1_rows[index];
			if (webTypeRuleList.length > 0) {
				for (var i = 0; i < webTypeRuleList.length; i++) {
					if (webTypeRuleList[i].typeId == currentRow.typeId) {
						typeFilterRule = webTypeRuleList[i].typeFilterRule;
						if (typeof (webTypeRuleList[i].webTypeMatchLabelList) != "undefined"
								&& webTypeRuleList[i].webTypeMatchLabelList.length > 0) {
							for (var j = 0; j < webTypeRuleList[i].webTypeMatchLabelList.length; j++) {
								var webTypeMatchLabel_t = webTypeRuleList[i].webTypeMatchLabelList[j];
								$('#dg_rule')
										.datagrid(
												'appendRow',
												{
													id : webTypeMatchLabel_t.id,
													ruleType : webTypeMatchLabel_t.ruleType,
													ruleTypeValue : webTypeMatchLabel_t.ruleTypeValue
												});
							}
						}
					}
				}
			}
	
			if (typeFilterRule != "" && typeof (typeFilterRule) != "undefined") {
				$("input:radio[name='filterRule'][value=" + typeFilterRule + "]")
						.prop("checked", 'checked');
			}
			$('#ppg').dialog('open');
		}
	}

	//删除一行属性字段;
	function deleteAttributeFiledMappingRow(obj) {
		var rows = $("#fileattr").datagrid('getRows');
		var index = $(obj).parents("tr:first").attr("datagrid-row-index");
		parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data) {
			if (data) {
				var rowIndex = $("#fileattr").datagrid('getRowIndex',
						rows[index]);
				$("#fileattr").datagrid('deleteRow', rowIndex);
			}
		});
	}

	var ruleData = [ {
		value : "开始于",
		text : "开始于"
	}, {
		value : "结束于",
		text : "结束于"
	}, {
		value : "包含",
		text : "包含"
	}, {
		value : "等于",
		text : "等于"
	},{
		value : "不开始于",
		text : "不开始于"
	},{
		value : "不结束于",
		text : "不结束于"
	},{
		value : "不包含",
		text : "不包含"
	},{
		value : "不等于",
		text : "不等于"
	}, {
		value : "正则表达式",
		text : "正则表达式"
	}];

	var webTypeRuleList = [];

	//定义页面类型匹配对象
	function WebTypeMatchLabel(id, typeId, ruleType, ruleTypeValue) {
		this.id = id; //主键ID
		this.typeId = typeId; //页面规则类型ID
		this.ruleType = ruleType; //规则类型（开始于、包含）
		this.ruleTypeValue = ruleTypeValue; //规则参数
		//this.typeFilterRule = typeFilterRule;	//过滤规则
	}

	//定义页面规则对象
	function WebTypeRule(typeId, taskId, typeName, typeLevel,isIncrement, typeMatchLabel,
			typeFilterRule, webTypeMatchLabelList) {
		this.typeId = typeId;
		this.taskId = taskId;
		this.typeName = typeName;
		this.typeLevel = typeLevel;
		this.isIncrement = isIncrement;
		this.typeMatchLabel = typeMatchLabel;
		this.typeFilterRule = typeFilterRule;
		this.webTypeMatchLabelList = webTypeMatchLabelList;
	}

	//确认保存数据
	function storeWebTypeMatchLabel() {

		var dg_type1_rows = dg_type1.datagrid('getRows');
		var currentRow = dg_type1_rows[ruleIndex];
		var dg_rule_Rows = dg_rule.datagrid('getRows');

		var dataGridList = [];
		//var typeMatchLabel = "";
		for (var i = 0; i < dg_rule_Rows.length; i++) {
			var webTypeMatchLabel_temp = new WebTypeMatchLabel(
					dg_rule_Rows[i].id, currentRow.typeId,
					dg_rule_Rows[i].ruleType, dg_rule_Rows[i].ruleTypeValue);
			dataGridList.push(webTypeMatchLabel_temp);
		}
		var filterRule = $("input:radio[name='filterRule']:checked").val();
		var webTypeRule = new WebTypeRule(currentRow.typeId, "",
				currentRow.typeName, currentRow.typeLevel,currentRow.isIncrement,
				currentRow.typeMatchLabel, filterRule, dataGridList);

		var isHas = false; //是否已经存在当前页面的数据，默认不存在
		for (var i = 0; i < webTypeRuleList.length; i++) {
			if (webTypeRuleList[i].typeId == webTypeRule.typeId) {
				webTypeRuleList[i] = webTypeRule;
				isHas = true;
			}
		}

		if (!isHas) {
			webTypeRuleList.push(webTypeRule);
		}
	}

	//保存页面类型规则
	function storeWebTypeRule(index) {
		var dg_type1_rows = dg_type1.datagrid('getRows');
		var currentRow = dg_type1_rows[index];
		var dataGridList = [];
		var webTypeRule = new WebTypeRule(currentRow.typeId, "",
				currentRow.typeName, currentRow.typeLevel,currentRow.isIncrement,
				currentRow.typeMatchLabel, currentRow.typeFilterRule,
				dataGridList);
		var isHas = false; //是否已经存在当前页面的数据，默认不存在
		for (var i = 0; i < webTypeRuleList.length; i++) {
			if (webTypeRuleList[i].typeId == webTypeRule.typeId) {
				var webTypeMatchLabelList = webTypeRuleList[i].webTypeMatchLabelList;
				webTypeRule.webTypeMatchLabelList = webTypeMatchLabelList;
				webTypeRuleList[i] = webTypeRule;
				isHas = true;
			}
		}

		if (!isHas) {
			webTypeRuleList.push(webTypeRule);
		}
	}

	//添加规则
	var dg_rule = $('#dg_rule');
	function appendRule() {
		var ruleType = $("#ruleType").combobox("getValue");
		var ruleParam = $("#ruleParam").val();

		if (ruleType == "") {
			$.messager.alert('提示', '请选择规则类型', 'info');
			return;
		}
		if (ruleParam == "") {
			$.messager.alert('提示', '请输入链接匹配字符串', 'info');
			return;
		}
		$('#dg_rule').datagrid('appendRow', {
			id : uuid(),
			ruleType : ruleType,
			ruleTypeValue : ruleParam
		});
	}

	//删除规则
	function deleteRule() {
		var rows = $('#dg_rule').datagrid('getChecked');
		for (var i = 0; i < rows.length; i++) {
			var rowIndex = $('#dg_rule').datagrid('getRowIndex', rows[i]);
			$('#dg_rule').datagrid('deleteRow', rowIndex);
		}
	}

	//确认
	function insertDataToRuleList() {

		var dg_rule = $("#dg_rule");
		var rows = dg_rule.datagrid('getRows');
		//alert(ruleIndex)

		var dataList = [];
		for (var i = 0; i < rows.length; i++) {
			dataList.push(rows[i].ruleType + ":" + rows[i].ruleTypeValue);
		}
		var typeMatchLabel = dataList.join('|');
		var typeFilterRule = $("input:radio[name='filterRule']:checked").val();
		dg_type1.datagrid('updateRow', {
			index : ruleIndex,
			row : {
				typeMatchLabel : typeMatchLabel,
				typeFilterRule : typeFilterRule
			}
		});
		storeWebTypeMatchLabel();
		dg_rule.datagrid('loadData', []);
		$('#ppg').dialog('close');

	}

	function onClose() {
		dg_rule.datagrid('loadData', []);
	}

	function onAfterEditType1(rowIndex, rowData, changes) {
		var rows = dg_type1.datagrid('getRows');
		for (var i = 0; i < rows.length; i++) {
			if (i != rowIndex && rowData.typeName == rows[i].typeName) {
				$.messager.alert('提示', rowData.typeName + '该规则名称已经存在！', 'info');
				//dg_type1.datagrid('updateRow',{index:rowIndex,row:{typeName:''}});
				//onDblClickCellType(rowIndex);
				$('#dg_type1').datagrid('selectRow', rowIndex).datagrid(
						'beginEdit', rowIndex);
				break;
			}
// 			if (rowData.typeMatchLabel == "") {
// 				$.messager.alert('提示', rowData.typeName + '的页面匹配不能为空！', 'info');
// 				$('#dg_type1').datagrid('selectRow', rowIndex).datagrid(
// 						'beginEdit', rowIndex);
// 				break;
// 			}
		}
		storeWebTypeRule(rowIndex);
		if(isChecked_type){
			$("#dg_type1").datagrid("checkRow",rowIndex);
		}
	}
	//========================================================================================tab2
	//定义解析参数设置页面datagrid对象
	function paramDataGridRowObject(id, name, xpath, exp, content,
			extractFieldLength, fieldExp, isAutoJoin,targetFieldName,targetFieldType,urlAttr,regex,isMultValue) {
		this.id = id;
		this.name = name;
		this.xpath = xpath;
		this.exp = exp;
		this.content = content;
		this.extractFieldLength = extractFieldLength;
		this.fieldExp = fieldExp;
		this.isAutoJoin = isAutoJoin;
		this.targetFieldName = targetFieldName;
		this.targetFieldType = targetFieldType;
		this.urlAttr = urlAttr;
		this.regex = regex;
		this.isMultValue = isMultValue;
	}
	//定义解析参数设置对象
	function paramObject(typeId, jumpType, jumpParam, incrementationParam,
			attr,advancedAttr,fieldExtractList) {
		this.typeId = typeId;
		this.jumpType = jumpType;
		this.jumpParam = jumpParam;
		this.incrementationParam = incrementationParam;
		this.attr = attr;
		this.advancedAttr = advancedAttr;
		this.fieldExtractList = fieldExtractList;
	}
	
	
	function onChange() {

		$("#typeList").combobox(
						{
							onChange : function(newValue, oldValue) {
// 								if (!endEditing()) {
// 									//$("#typeList").combobox('setValue',oldValue);
// 									return
// 								}
// 								alert("newValue="+newValue+",oldValue="+oldValue)
// 								alert($("#typeList").combobox('getValue'));
// 								$("#dg_type2").datagrid('acceptChanges');
// 								if (!endEditing()) {
// 									//$("#typeList").combobox('setValue',oldValue);
// 									return
// 								}
// 								var rows = $("#dg_type2").datagrid('getRows');
// 								for(var i=0;i<rows.length;i++)
// 								{
// 									alert(rows[i].name)
// 								}
								var currentType = oldValue;
								if (currentType == "") {
									currentType = newValue;
								}
								if(oldValue !='')
								{
									storeParamObjectTempByTypeId(oldValue);
								}
								$("#dg_type2").datagrid('loadData', []); //清空列表中的数据
								$("#jumpParam").val('');
								$("#incrementationParam").val('');
								$("input:radio[name='jumpType'][value='N']").prop("checked", 'checked');
								$("#advancedAttr").prop("checked",'');
								disabledInputType2();
								//加载切换后新的选项中的数据
								for (var i = 0; i < paramObjectList.length; i++) {
									if (paramObjectList[i].typeId == newValue) {
										//$("#typeList").combobox('setValue',paramObjectList[i].typeId);
										if (paramObjectList[i].jumpType == 'Y') {
											enabledInputType2();
										}
										$("input:radio[name='jumpType'][value='"+ paramObjectList[i].jumpType+"']").prop("checked",'checked');
										$("#jumpParam").val(paramObjectList[i].jumpParam);
										$("#incrementationParam").val(paramObjectList[i].incrementationParam);
										$("#attr").val(paramObjectList[i].attr);
										if(paramObjectList[i].advancedAttr=='Y')
										{
											$("#advancedAttr").prop("checked",'checked');
											showColumn();
										}
										else
										{
											$("#advancedAttr").prop("checked",'');
											hideColumn();
										}
										$('#dg_type2').datagrid('loadData',paramObjectList[i].fieldExtractList);
									}
								}

							}
						});
	}

	function storeParamObjectTemp() {
		
		 //$('#dg_type2').datagrid('acceptChanges');
		var typeId = $("#typeList").combobox('getValue');
		var rows_type2 = $('#dg_type2').datagrid('getRows');
		var dataGridList = [];
		for (var i = 0; i < rows_type2.length; i++) {
			var paramDataGridRowObject_temp = new paramDataGridRowObject(
					rows_type2[i].id, rows_type2[i].name, rows_type2[i].xpath,
					rows_type2[i].exp, rows_type2[i].content,
					rows_type2[i].extractFieldLength, rows_type2[i].fieldExp,
					rows_type2[i].isAutoJoin,
					rows_type2[i].targetFieldName,
					rows_type2[i].targetFieldType,
					rows_type2[i].urlAttr,
					rows_type2[i].regex,
					rows_type2[i].isMultValue);
			dataGridList.push(paramDataGridRowObject_temp);
		}
		var jumpType = $("input:radio[name='jumpType']:checked").val();
		var jumpParam = $("#jumpParam").val();
		var incrementationParam = $("#incrementationParam").val();
		var attr = $("#attr").val();
		var advancedAttr =$("#advancedAttr").prop("checked");
		var advancedAttr_value = '';
		if(advancedAttr)
		{
			advancedAttr_value = 'Y';
			showColumn();
		}
		else
		{
			advancedAttr_value = 'N';
			hideColumn();
		}
		var paramObjectAll = new paramObject(typeId, jumpType, jumpParam,
				incrementationParam, attr,advancedAttr_value,dataGridList);

		var isHas = false; //是否已经存在当前页面的数据，默认不存在
		for (var i = 0; i < paramObjectList.length; i++) {
			if (paramObjectList[i].typeId == typeId) {
				paramObjectList[i] = paramObjectAll;
				isHas = true;
			}
		}

		if (!isHas) {
			paramObjectList.push(paramObjectAll);
		}
	}
	
	//高级属性，显示和隐藏列
	function showColumn()
	{
		$('#dg_type2').datagrid('showColumn','regex');
		$('#dg_type2').datagrid('showColumn','exp');
		$('#dg_type2').datagrid('showColumn','urlAttr');
		$("#advancedAttr").val('Y');
	}
	function hideColumn()
	{
		$('#dg_type2').datagrid('hideColumn','regex');
		$('#dg_type2').datagrid('hideColumn','exp');
		$('#dg_type2').datagrid('hideColumn','urlAttr');
		$("#advancedAttr").val('N');
	}

	//在下拉选页面规则时
	function storeParamObjectTempByTypeId(typeId) {
		if(typeId!='')
		{
			if(typeof(editIndex)!= "undefined")
			{
// 				var ed_id = $('#dg_type2').datagrid('getEditor', {index:editIndex,field:'id'});
// 				var id=$(ed_id.target).val();
				var id="";
				var ed_name = $('#dg_type2').datagrid('getEditor', {index:editIndex,field:'name'});
				var name=$(ed_name.target).val();
				
				var ed_xpath = $('#dg_type2').datagrid('getEditor', {index:editIndex,field:'xpath'});
				var xpath=$(ed_xpath.target).val();
				
				var ed_regex = $('#dg_type2').datagrid('getEditor', {index:editIndex,field:'regex'});
				var regex=$(ed_regex.target).val();
				
				var ed_exp = $('#dg_type2').datagrid('getEditor', {index:editIndex,field:'exp'});
				var exp=$(ed_exp.target).val();
				
				var ed_content = $('#dg_type2').datagrid('getEditor', {index:editIndex,field:'content'});
				var content=$(ed_content.target).combobox("getValue");
				
				var ed_isAutoJoin = $('#dg_type2').datagrid('getEditor', {index:editIndex,field:'isAutoJoin'});
				var isAutoJoin=$(ed_isAutoJoin.target).combobox("getValue");
				
				var ed_isMultValue = $('#dg_type2').datagrid('getEditor', {index:editIndex,field:'isMultValue'});
				var isMultValue=$(ed_isMultValue.target).combobox("getValue");
				
				var ed_extractFieldLength = $('#dg_type2').datagrid('getEditor', {index:editIndex,field:'extractFieldLength'});
				var extractFieldLength=$(ed_extractFieldLength.target).val();
				
				var ed_fieldExp = $('#dg_type2').datagrid('getEditor', {index:editIndex,field:'fieldExp'});
				var fieldExp=$(ed_fieldExp.target).val();
				
				var ed_targetFieldName = $('#dg_type2').datagrid('getEditor', {index:editIndex,field:'targetFieldName'});
				var targetFieldName=$(ed_targetFieldName.target).val();
				
				var ed_targetFieldType = $('#dg_type2').datagrid('getEditor', {index:editIndex,field:'targetFieldType'});
				var targetFieldType=$(ed_targetFieldType.target).val();
				
				var ed_urlAttr = $('#dg_type2').datagrid('getEditor', {index:editIndex,field:'urlAttr'});
				var urlAttr=$(ed_urlAttr.target).val();
			
				var paramDataGridRowObject_edit = new paramDataGridRowObject(
						id, name,xpath,exp, content,extractFieldLength,fieldExp,
						isAutoJoin,targetFieldName,targetFieldType,urlAttr,regex,isMultValue);
				
				var rows_type2 = $('#dg_type2').datagrid('getRows');
				var dataGridList = [];
				for (var i = 0; i < rows_type2.length; i++) {
					var paramDataGridRowObject_temp = new paramDataGridRowObject(
							rows_type2[i].id, rows_type2[i].name, rows_type2[i].xpath,
							rows_type2[i].exp, rows_type2[i].content,
							rows_type2[i].extractFieldLength, rows_type2[i].fieldExp,
							rows_type2[i].isAutoJoin,
							rows_type2[i].targetFieldName,
							rows_type2[i].targetFieldType,
							rows_type2[i].urlAttr,
							rows_type2[i].regex,
							rows_type2[i].isMultValue);
					if(editIndex == i)
					{
						paramDataGridRowObject_edit.id = rows_type2[i].id;
						dataGridList.push(paramDataGridRowObject_edit);
					}
					else
					{
						dataGridList.push(paramDataGridRowObject_temp);
					}
				}
				var jumpType = $("input:radio[name='jumpType']:checked").val();
				var jumpParam = $("#jumpParam").val();
				var incrementationParam = $("#incrementationParam").val();
				var attr = $("#attr").val();
				var advancedAttr =$("#advancedAttr").prop("checked");
				var advancedAttr_value = '';
				if(advancedAttr)
				{
					advancedAttr_value = 'Y';
					showColumn();
				}
				else
				{
					advancedAttr_value = 'N';
					hideColumn();
				}
				var paramObjectAll = new paramObject(typeId, jumpType, jumpParam,
						incrementationParam, attr,advancedAttr_value,dataGridList);
		
				var isHas = false; //是否已经存在当前页面的数据，默认不存在
				for (var i = 0; i < paramObjectList.length; i++) {
					if (paramObjectList[i].typeId == typeId) {
						paramObjectList[i] = paramObjectAll;
						isHas = true;
					}
				}
		
				if (!isHas) {
					paramObjectList.push(paramObjectAll);
				}
				
				editIndex = undefined;
			}
			
		}
	}

	var editIndex = undefined;
	var isChecked_param;
	function endEditing() {
		//storeParamObjectTemp();
		if (editIndex == undefined){return true}
		if($('#dg_type2').datagrid('validateRow',editIndex))
		{
			$('#dg_type2').datagrid('endEdit', editIndex);
	          editIndex = undefined;
              return true;
		}
		else
		{
			return false;
		}
		//         editIndex = undefined;
		//return true;
	}
	function onDblClickCell(index) {

		if (endEditing()) {
			editIndex = index;
			$('#dg_type2').datagrid('addEditor', [ {
				field : 'name',
				editor : {
					type : 'validatebox',
					options : {
						validType:"checkDataGrid['#dg_type2','name',paramObjectList,'#typeList',editIndex]",
						required : true
					}
				}
			}, {
				field : 'xpath',
				editor : {
					type : 'text',
					options : {
						required : true
					}
				}
			}, {
				field : 'exp',
				editor : {
					type : 'text',
					options : {
						required : true
					}
				}
			}, {
				field : 'content',
				editor : {
					type : 'combobox',
					options : {
						// 	    					valueField:'id',
						// 	    					textField:'name',
						editable : false,
						panelHeight : 100
					}
				}
			}, {
				field : 'extractFieldLength',
				editor : {
					type : 'text',
					options : {
						required : true
					}
				}
			}, {
				field : 'fieldExp',
				editor : {
					type : 'text',
					options : {
						required : true
					}
				}
			}, {
				field : 'isAutoJoin',
				editor : {
					type : 'combobox',
					options : {
						// 	    					valueField:'id',
						// 	    					textField:'name',
						editable : false,
						panelHeight : 100
					}
				}
			}, {
				field : 'isMultValue',
				editor : {
					type : 'combobox',
					options : {
						// 	    					valueField:'id',
						// 	    					textField:'name',
						editable : false,
						panelHeight : 100
					}
				}
			}, {
				field : 'regex',
				editor : {
					type : 'text',
					options : {
						required : true
					}
				}
			}, {
				field : 'targetFieldName',
				editor : {
					type : 'text',
					options : {
						required : true
					}
				}
			}, {
				field : 'targetFieldType',
				editor : {
					type : 'text',
					options : {
						required : true
					}
				}
			}, {
				field : 'urlAttr',
				editor : {
					type : 'text',
					options : {
						required : true
					}
				}
			} 
			]);
			$('#dg_type2').datagrid('selectRow', index).datagrid('beginEdit',
					index);
		}
		var content = $('#dg_type2').datagrid('getEditor', {
			index : index,
			field : 'content'
		});
		$(content.target).combobox('loadData', contents);
		var isAutoJoin = $('#dg_type2').datagrid('getEditor', {
			index : index,
			field : 'isAutoJoin'
		});
		$(isAutoJoin.target).combobox('loadData', isAutoJoin_data);
		var isMultValue = $('#dg_type2').datagrid('getEditor', {
			index : index,
			field : 'isMultValue'
		});
		$(isMultValue.target).combobox('loadData', isMultValue_data);
		
		if(isChecked_param){
			$("#dg_type2").datagrid("checkRow",rowIndex);
		}
		
		
// 		var rows = $("#dg_type2").datagrid('getRows');
// 		var rowData = rows[index];
// 		var xpath = $('#dg_type2').datagrid('getEditor', {
// 			index : index,
// 			field : 'xpath'
// 		});
// 		$(xpath.target).val(rowData.xpath.replaceAll("&lt;","<" ).replaceAll("&gt;",">"));
		
// 		var regex = $('#dg_type2').datagrid('getEditor', {
// 			index : index,
// 			field : 'regex'
// 		});
// 		$(regex.target).val(rowData.regex.replaceAll("&lt;","<" ).replaceAll("&gt;",">"));
		
// 		var exp = $('#dg_type2').datagrid('getEditor', {
// 			index : index,
// 			field : 'exp'
// 		});
// 		$(exp.target).val(rowData.exp.replaceAll("&lt;","<" ).replaceAll("&gt;",">"));
	}
	
	function onEndEdit(index, row) {
		var ed = $(this).datagrid('getEditor', {
			index : index,
			field : 'id'
		});
		//row.productname = $(ed.target).combobox('getText');
	}

	function append(xpath,attrs) {
		
		if(endEditing())
		{
			var typeList = $("#typeList").combobox('getValue');
			if (typeList == "" || typeList == "null" || typeList == null) {
				$.messager.alert('提示', '请先选择规则类型', 'info');
				return;
			}
			var attr  = getAttr(attrs);
			var count = $('#dg_type2').datagrid('getRows').length + 1;
			$('#dg_type2').datagrid('appendRow', {
				id : uuid(),
				name : 'NewField' + getFieldCount(),
				xpath : xpath,
				exp : '',
				content : '文本内容',
				isAutoJoin : '否',
				isMultValue : '是',
				urlAttr:attr
			});
			var index = $('#dg_type2').datagrid('getRows').length - 1;
			//        $('#dg_type2').datagrid('selectRow', editIndex)
			//                .datagrid('beginEdit', editIndex);
			onDblClickCell(index);
		}
	}
	function removeit(index) {
		var rows = $('#dg_type2').datagrid('getChecked');
		if(rows.length>0)
		{
			$.messager.confirm('提示','删除后无法恢复您确定要删除？', function(data){
				if(data){
					for (var i = 0; i < rows.length; i++) {
						var rowIndex = $('#dg_type2').datagrid('getRowIndex', rows[i]);
						$('#dg_type2').datagrid('deleteRow', rowIndex);
					}
					storeParamObjectTemp();
				}
			});
		}
		else
		{
			$.messager.alert('提示','请选择需要删除的数据？', 'info');
		}
	}

	function onClickRow(rowIndex, rowData) {
		endEditing();
		var isChecked_param = RowIsChecked($('#dg_type2'),rowIndex);
		if(!isChecked_param){
			$("#dg_type2").datagrid("uncheckRow",rowIndex);
		}else{
			$("#dg_type2").datagrid("checkRow",rowIndex);
		}
	}
	
	function onBeforeEditParam(rowIndex, rowData) {
		 	isChecked_param = RowIsChecked($('#dg_type2'),rowIndex);
			if(isChecked_param){
				$("#dg_type2").datagrid("checkRow",rowIndex);
			}
			else
			{
				$("#dg_type2").datagrid("uncheckRow",rowIndex);
			}
	}

	function onParamAfterEdit(rowIndex, rowData, changes) {
		checkParamSetting(rowIndex, rowData, changes);
		storeParamObjectTemp();
		if(isChecked_param){
			$("#dg_type2").datagrid("checkRow",rowIndex);
		}
		else
		{
			$("#dg_type2").datagrid("uncheckRow",rowIndex);
		}
// 		$("#dg_type2").datagrid('updateRow',
// 				{
// 					index:rowIndex,
// 					row:
// 					{
// 						xpath:rowData.xpath.replaceAll(/</g, "&lt;").replaceAll(/>/g, "&gt;"),
// 						regex:rowData.regex.replaceAll(/</g, "&lt;").replaceAll(/>/g, "&gt;"),
// 						exp:rowData.exp.replaceAll(/</g, "&lt;").replaceAll(/>/g, "&gt;")
// 					}
// 				}
// 			);
	}
	/*
	 * 点击字段映射单击行事件;
	 */
	function onFieldMappingClickRow(rowIndex, rowData) {
		$("#webfieldmapping").datagrid('endEdit', lastIndex);
		var isChecked = RowIsChecked($('#webfieldmapping'),rowIndex);
		if(!isChecked){
			$("#webfieldmapping").datagrid("uncheckRow",rowIndex);
		}else{
			$("#webfieldmapping").datagrid("checkRow",rowIndex);
		}
	}
	/**
	 * 删除字段映射转换字段行;
	 */
	function deleteFiledMappingRow(obj) {
		var rows = $("#webfieldmapping").datagrid('getRows');
		var index = $(obj).parents("tr:first").attr("datagrid-row-index");
		parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data) {
			if (data) {
				var rowIndex = $("#webfieldmapping").datagrid('getRowIndex',
						rows[index]);
				$("#webfieldmapping").datagrid('deleteRow', rowIndex);
				storeWebFieldMapping();
			}
		});
	}

	function onFieldMappingBeforeEdit(rowIndex, rowData) {
		isChecked = RowIsChecked($('#webfieldmapping'),rowIndex);
	}

	function onFieldMappingAfterEdit(rowIndex, rowData, changes) {
		for (var i = 0; i < paramObjectList.length; i++) {
			var fieldExtractRows = paramObjectList[i].fieldExtractList;
			if (fieldExtractRows.length > 0) {
				for (var j = 0; j < fieldExtractRows.length; j++) {
					if (fieldExtractRows[j].name == rowData.fieldName
							&& rowData.filedSource == '源字段') {
						fieldExtractRows[j].fieldExp = rowData.fieldExp;
						fieldExtractRows[j].targetFieldName = rowData.targetFieldName;
						fieldExtractRows[j].targetFieldType = rowData.targetFieldType;
						paramObjectList[i].fieldExtractList = fieldExtractRows;
						break;
					}
				}
			}
		}
		storeWebFieldMapping();
		if(isChecked){
			$("#webfieldmapping").datagrid("checkRow",rowIndex);
		}
	}
	


	function onBeforeEdit(rowIndex, rowData) {
// 			isChecked_type = RowIsChecked($("#dg_type1"),rowIndex);
	}
	function onAfterEdit(rowIndex, rowData, changes) {
// 			if(isChecked_type){
// 				$("#dg_type1").datagrid("checkRow",rowIndex);
// 			}
	}
	function onBeforeEditType(rowIndex, rowData) {
			isChecked_type = RowIsChecked($("#dg_type1"),rowIndex);
			if(isChecked_type){
				$("#dg_type1").datagrid("checkRow",rowIndex);
			}
			else
			{
				$("#dg_type1").datagrid("uncheckRow",rowIndex);
			}
	}

	//启使用户的输入框
	function enabledInputType2() {
		$("#jumpParam").attr("disabled", false);
		$("#incrementationParam").attr("disabled", false);
	}

	//禁用用户的输入框
	function disabledInputType2() {
		$("#jumpParam").attr("disabled", true);
		$("#incrementationParam").attr("disabled", true);
		$("#jumpParam").val("");
		$("#incrementationParam").val("");
		$("#attr").val("");
	}

	//扩大一级
	function expandLevel() {
		var checkRows = $("#dg_type2").datagrid('getChecked');
		if (checkRows.length > 0) {
			for (var i = 0; i < checkRows.length; i++) {
				var rowIndex = $("#dg_type2").datagrid('getRowIndex',
						checkRows[i]);
				var xpath = checkRows[i].xpath;
				var index = xpath.lastIndexOf("/");
				if (index != -1) {

					xpath = xpath.substring(0, index);

					$('#dg_type2').datagrid('updateRow', {
						index : rowIndex,
						row : {
							xpath : xpath
						}
					});
					$("#dg_type2").datagrid('checkRow',rowIndex);
					storeParamObjectTemp();
				}
			}

		} else {
			$.messager.alert('提示', '请选择需要扩大范围的数据！', 'info');
		}
	}
	//设置下一页参数
	function setNextPageParam(xpath, attrs, domain) {
		$("#jumpParam").val(domain);
		$("#incrementationParam").val(xpath);
		if (typeof (attrs) !== "undefined" && attrs != "" && attrs.length > 0) {

			var attributes = attrs.split(";");
			for (var i = 0; i < attributes.length; i++) {
				if (attributes[i].toLowerCase() == "href") {
					$("#attr").val('href');
					break;
				}
				if (attributes[i].toLowerCase() == "src") {
					$("#attr").val('src');
					break;
				}
				if (attributes[i].toLowerCase() == "url") {
					$("#attr").val('url');
					break;
				}
			}
		}
	}
	
	//获取属性值
	function getAttr(attrs)
	{
		var attr = "";
		if (typeof (attrs) !== "undefined" && attrs != "" && attrs.length > 0) {

			var attributes = attrs.split(";");
			for (var i = 0; i < attributes.length; i++) {
				if (attributes[i].toLowerCase() == "href") {
					attr = 'href';
					break;
				}
				if (attributes[i].toLowerCase() == "src") {
					attr = 'src';
					break;
				}
				if (attributes[i].toLowerCase() == "url") {
					attr = 'url';
					break;
				}
			}
		}
		return attr;
	}

	function checkParamSetting(rowIndex, rowData, changes) {
		var xpathValue = rowData.xpath;
		var index = xpathValue.lastIndexOf("/");
		if(changes.content=="链接地址" || changes.content=="自动挖掘")
		{
			if (index != -1) {
				var xpathValue_temp = xpathValue.substring(0, index);
				$('#dg_type2').datagrid('updateRow',{index:rowIndex,row:{xpath:xpathValue_temp}});
			}
		}
		else if(typeof(changes.content)=="undefined")
		{
			//没有改变，什么也不需要做
		}
		else
		{
			if (index != -1) {
				var xpathValue_temp = xpathValue.substring(index);
				if(xpathValue_temp!="/text()")
				{
					xpathValue += "/text()";
					$('#dg_type2').datagrid('updateRow',{index:rowIndex,row:{xpath:xpathValue}});
				}
			}
		}
// 		$('#dg_type2').datagrid('refreshRow',rowIndex);
// 		var paramRows = $("#dg_type2").datagrid('getRows');
// 		if (paramRows.length > 0) {
// 			for (var i = 0; i < paramRows.length; i++) {
// 				if (rowIndex != i && paramRows[i].name == rowData.name) {
// 					$.messager.alert('提示', rowData.name + '该字段名称已经存在！', 'info',function(){
// 						$('#dg_type2').datagrid('selectRow', rowIndex).datagrid('beginEdit', rowIndex);
// 						editIndex = rowIndex;
// 					});
// 					break;
// 				}
// 			}

// 		}
// 		var currentTypeId = $("#typeList").combobox('getValue');
// 		for (var i = 0; i < paramObjectList.length; i++) {
// 			var fieldExtractRows = paramObjectList[i].fieldExtractList;
// 			if (fieldExtractRows.length > 0) {
// 				for (var j = 0; j < fieldExtractRows.length; j++) {
// 					if (paramObjectList[i].typeId != currentTypeId
// 							&& fieldExtractRows[j].name == rowData.name) {
// 						$.messager.alert('提示', rowData.name + '该字段名称已经存在！','info',function()
// 						{
// 							$('#dg_type2').datagrid('selectRow', rowIndex).datagrid('beginEdit', rowIndex);
// 							editIndex = rowIndex;
// 						});
// 						break;
// 					}
// 				}
// 			}
// 		}
	}
	
	/**获取提取字段个数*/
	function getFieldCount()
	{
		var count = 0;
		for (var i = 0; i < paramObjectList.length; i++) {
			var fieldExtractRows = paramObjectList[i].fieldExtractList;
			if (fieldExtractRows.length > 0) {
				for (var j = 0; j < fieldExtractRows.length; j++) {
					count +=1;
				}
			}
		}
		return count;
	}
	//========================================================================tab3
		function fieldMappingSameNameMatch(){
		initOutputColumn();
		var rows = $('#webfieldmapping').datagrid('getRows');
		var checkedRows = $('#webfieldmapping').datagrid("getChecked");
		var matchNum = 0;
		var checkIndex = [];
		for(var i=0 ; i<rows.length;i++){
			var row = rows[i];
			if(outColumns.length>0)
			{
				for(var j=0;j<outColumns.length;j++){
					if(row.fieldName == outColumns[j].value)
					{
						matchNum ++;
						row.targetFieldName = row.fieldName;
						row.targetFieldType = outColumnsTypes[j].value;
						onFieldMappingAfterEdit('',row,'');
						//记录自动选中同名字段
						checkIndex.push(i);
						break;
					}
				}
			}
			else
			{
				//清除之前选择表匹配的内容
				row.targetFieldName = '';
				row.targetFieldType = '';
				onFieldMappingAfterEdit('',row,'');
			}
			$('#webfieldmapping').datagrid('refreshRow',i);
		}
		//自动选中同名字段
		for(var i = 0 ; i<checkIndex.length ; i++)
		{
			$("#webfieldmapping").datagrid("checkRow",checkIndex[i]);
		}
		for(var i = 0 ; i<checkedRows.length ; i++)
		{
			var checkedRowIndex = $('#webfieldmapping').datagrid('getRowIndex',checkedRows[i]);
			$("#webfieldmapping").datagrid("checkRow",checkedRowIndex);
		}
		if(matchNum == 0){
			$("#matchTips").text("未发现同名字段!");
		}else if(matchNum > 0){
			$("#matchTips").text("已匹配"+matchNum+"个字段!");
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
	
		function onCheckFieldMapping(rowIndex,rowData){
// 			alert("onCheck="+rowData.fieldName)
			if(checkfieldMappingJsonList.length>0)
			{
				var flag = false;
				for(var i=0;i<checkfieldMappingJsonList.length;i++)
				{
					if(checkfieldMappingJsonList.fieldName == rowData.fieldName)
					{
						flag = true;
						break;
					}
				}
				if(!flag)
				{
					var fieldStatus = "1";
					var checkfieldMapping = new fieldMappingObject(rowData.fieldID,rowData.fieldName,rowData.fieldExp,rowData.filedSource,rowData.targetFieldName,rowData.targetFieldType,fieldStatus);
					checkfieldMappingJsonList.push(checkfieldMapping);
				}
			}
			else
			{
				var fieldStatus = "1";
				var checkfieldMapping = new fieldMappingObject(rowData.fieldID,rowData.fieldName,rowData.fieldExp,rowData.filedSource,rowData.targetFieldName,rowData.targetFieldType,fieldStatus);
				checkfieldMappingJsonList.push(checkfieldMapping);
			}
		}
		
		function onCheckAllFieldMapping(rows){
// 			for(var i=0;i<rows.length;i++)
// 			alert("onCheckAll="+rows[i].fieldName)
			storeChecFieldkMapping();
		}
		function onUnCheckFieldMapping(rowIndex,rowData){
// 			alert("onUncheck="+rowData.fieldName)
			storeChecFieldkMapping();
		}
		
		function onUnCheckAllFieldMapping(rows){
// 			for(var i=0;i<rows.length;i++)
// 			alert("onUnCheckAll="+rows[i].fieldName)
			storeChecFieldkMapping();
		}
		
		function storeChecFieldkMapping()
		{
			checkfieldMappingJsonList=[];
			var checkRows = $("#webfieldmapping").datagrid('getChecked');
			for(var i=0;i<checkRows.length;i++)
			{
				var fieldStatus = "1";
				var checkfieldMapping = new fieldMappingObject(checkRows[i].fieldID,checkRows[i].fieldName,checkRows[i].fieldExp,checkRows[i].filedSource,checkRows[i].targetFieldName,checkRows[i].targetFieldType,fieldStatus);
				checkfieldMappingJsonList.push(checkfieldMapping);
			}
		}
	
	//浏览器选择属性字段值
	function onBrowerSelectValue(xpath, attrs, domain) {
		var chooseVule = $("input:radio[name='chooseElement']:checked").val();
		if (chooseVule == "field") {
			append(xpath + "/text()",attrs);
		}
		if (chooseVule == "nextPage") {
			$("input:radio[name='jumpType'][value='Y']").prop("checked",
					'checked');
			enabledInputType2();
			setNextPageParam(xpath, attrs, domain);
		}
	}

	var str = "";
	function OnSelected(xpath, attrs, domain) {

		//str += xpath + "<br/>";
		//document.getElementById("test").innerHTML=str;
		//alert(xpath);
		onBrowerSelectValue(xpath, attrs, domain);
	}

	//判断是否已经安装浏览器控件
	function browserInstalled() {
		var result = true;
		try
		{		
			BrowserControl.getXpath();
// 			var browserId = document.getElementById("BrowserControl");
// 			browserId.getXpath();
		}
		catch(e)
		{ 
			result = false;
		}

		return result;
	}
	
	String.prototype.replaceAll  = function(s1,s2){     
	    return this.replace(new RegExp(s1,"gm"),s2);     
	}  
</script>

<script language="javascript" for="BrowserControl" event="OnElementSelected(xpath,attrs,domain)">
  //alert(xpath);
  //append(xpath);
	onBrowerSelectValue(xpath,attrs,domain);
 </script>
