<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
th, td { padding:3px; font-size:12px;}
</style>
<div style="padding: 5px;overflow: hidden;">
	<form id="taskDataForm" method="post"  enctype="multipart/form-data">
		<table class="tableForm">
			<tr>
				<th style="width: 120px;">资源JSON文件:</th>
				<td>
					<input id="resourcedata" name="resourcedata" type="file"  class="easyui-validatebox"/>
				</td>
			</tr>
			<tr>
				<td colspan="2" style="width: 220px;">
				<input type="checkbox" id="replace" name="replace" value="Y" checked="checked" onclick="setValue()"></input>
				任务如果存在替换现有任务
				</td>
			</tr>
		</table>
	</form>
</div>
<script>
function setValue()
{
	var replace = $("#replace");
	var replaceChecked=replace.prop("checked");
// 	alert(replaceChecked)
	var replace_value = '';
	if(replaceChecked)
	{
		replace_value = 'Y';
		replace.val('Y')
	}
	else
	{
		replace_value = 'N';
		replace.val('N')
	}
	
}

String.prototype.endWith=function(endStr){
	  var d=this.length-endStr.length;
	  return (d>=0&&this.lastIndexOf(endStr)==d);
};

function submitForm(d, dg) {
	var f = $("#taskDataForm");
	f.form('submit',{
		url : '${pageContext.request.contextPath}/task/doDataImport',
	    onSubmit: function(){
	    	if($("#resourcedata").val()){
	    		if($("#resourcedata").val().endWith(".json")){
					$.messager.progress({
						title : '提示',
						text : '数据处理中，请稍后....'
					});
	    			return true;
	    		}else{
	    			alert("文件格式不对,请选择.json文件");
	    			return false;
	    		}
	    	}else{
	    		alert("请选择资源文件!");
	    		return false;//返回false终止表单提交
	    	}
	    },
		success : function(data) {
			successTipJson(data,dg,d);
		},
		error : function (err) {
			alert("保存失败");
		}
	});
}

/**
 * ajax返回提示
 * @param data	返回的数据
 * @param dg datagrid
 * @param d	弹窗
 * @returns {Boolean} ajax是否成功
 */
function successTipJson(data,dg,d){
	var data= eval('(' + data + ')');
	var msg=replace(data.msg);
	$.messager.progress('close');
	if(data.success){
		if(dg!=null)
			dg.datagrid('reload');
		if(d!=null)
			d.panel('close');
		
		parent.$.messager.alert('提示',msg,'info');
		//parent.$.messager.show({ title : "提示",msg: msg, position: "bottomRight" });
		return true;
	}else{
		parent.$.messager.alert('提示',msg,'info');
		return false;
	}  
}
 
 function replace(strings){
	 
	 strings=strings.replaceAll("@","<br/>");
	 return strings;
	 }
</script>