<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>数据采集任务管理</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
</head>
<body>
<div id="tb" style="padding:5px;height:auto">
	<div>
	    <form id="searchFrom" action="">
			<input type="text" name="filter_LIKES_name" class="easyui-validatebox" data-options="width:150,prompt: '计划名称'" />
			<input type="text" name="filter_GTD_createTime" class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width:150,prompt: '开始日期'"  /> - <input type="text" name="filter_LTD_createTime" class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width:150,prompt: '结束日期'"  /> 
			<input type="text" name="filter_EQS_creator" class="easyui-validatebox" data-options="width:150,prompt: '创建人'" />
			<input type="text" name="filter_task_reference" class="easyui-validatebox" data-options="width:150,prompt: '关联任务'" />
			<span class="toolbar-item dialog-tool-separator"></span>
			<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx();">查询</a>
		</form>
    	<a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-standard-clock-add" onclick="add();">添加</a>
    	<span class="toolbar-item dialog-tool-separator"></span>
    	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-clock-pause" plain="true" onclick="stop();">暂停</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-clock" plain="true" onclick="resume();">恢复</a>
        <span class="toolbar-item dialog-tool-separator"></span>
        <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-standard-clock-delete" onclick="del()">删除</a>
        <span class="toolbar-item dialog-tool-separator"></span>
        <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-standard-clock-edit" onclick="upd()">修改</a>
        <span class="toolbar-item dialog-tool-separator"></span>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-clock-play" plain="true" onclick="startNow();">立即运行一次</a>
   	</div>
</div>
<table id="planlist"></table>
<div id="dlg"></div>  
<script>
var dg;
var d;
$(function(){
	dg=$('#planlist').datagrid({    
		method: "get",
	    url:'${ctx}/schedulePlan/json', 
	    fit : true,
		fitColumns : true,
		border : false,
		idField : 'id',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:false,
	    columns:[[
	        {field:'id',title:'计划ID',checkbox:true},    
	        {field:'name',title:'计划名称',sortable:true,width:20},    
	        {field:'createTime',title:'创建时间',sortable:true,width:20,
	         formatter:function(value,row,index){
		      	var str="";
		      	if(value!=null && value!="")
		      	{
                  var unixTimestamp = new Date(value);  
                  str= unixTimestamp.toLocaleString();  
			    }
			    return str;
              }
	        },
	        {field:'creator',title:'创建人',sortable:true,width:15},
	        {field:'description',title:'计划描述',sortable:true,width:30},
	        {field:'taskMatchs',title:'关联任务',sortable:true,width:20},
	        {field:'status',title:'状态',sortable:true,width:10,
	         formatter:function(value,row,index){
		      	var str="";
		        if("0" == value){
		        	str="暂停";
		        }else if("1" == value){
		        	str="调度中";
		        }
			    return str;
              }
	        }
	    ]],
	    headerContextMenu: [
	        {
	            text: "冻结该列", disabled: function (e, field) { return dg.datagrid("getColumnFields", true).contains(field); },
	            handler: function (e, field) { dg.datagrid("freezeColumn", field); }
	        },
	        {
	            text: "取消冻结该列", disabled: function (e, field) { return dg.datagrid("getColumnFields", false).contains(field); },
	            handler: function (e, field) { dg.datagrid("unfreezeColumn", field); }
	        }
	    ],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
});
//添加
function add() {
	d=$("#dlg").dialog({   
	    title: '添加调度计划',    
	    width: 530,
	    height: 350,    
	    href:'${ctx}/schedulePlan/add',
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
				var planType = $("input[name='planType']:checked").val();
				if("0" == planType)
					setWeekCron();
				if("1" == planType)
					setDayCron();
				if("2" == planType)
					setHourCron();
				if("3" == planType)
					setMinuteCron();
				$("#mainform").submit();
			}
		},{
			text:'取消',
			handler:function(){
				d.panel('close');
			}
		}]
	});
}
//暂停
function stop(){
	var row = dg.datagrid('getSelections');
	if(rowIsNull(row)) return;
	var idList=[];
	for(var i = 0 ;i<row.length ;i++){
		idList.push(row[i].id);
	}
	parent.$.messager.confirm('提示', '确定要暂停任务？', function(data){
		if (data){
			$.ajax({
				type:'post',
				data:JSON.stringify(idList),
				url:"${ctx}/schedulePlan/stop",
				contentType:'application/json;charset=utf-8',
				success: function(data){
					if(data=='success'){
						dg.datagrid('reload');
						parent.$.messager.show({ title : "提示",msg: "操作成功！", position: "bottomRight" });
					}else{
						parent.$.messager.alert(data);
					}  
				}
			});
		}
	});
}

//恢复
function resume(){
	var row = dg.datagrid('getSelections');
	if(rowIsNull(row)) return;
	var idList=[];
	for(var i = 0 ;i<row.length ;i++){
		idList.push(row[i].id);
	}
	parent.$.messager.confirm('提示', '确定要恢复任务？', function(data){
		if (data){
			$.ajax({
				type:'post',
				data:JSON.stringify(idList),
				url:"${ctx}/schedulePlan/resume",
				contentType:'application/json;charset=utf-8',
				success: function(data){
					if(data=='success'){
						dg.datagrid('reload');
						parent.$.messager.show({ title : "提示",msg: "操作成功！", position: "bottomRight" });
					}else{
						parent.$.messager.alert(data);
					}  
				}
			});
		}
	});
}
//立即运行一次
function startNow(){
	var row = dg.datagrid('getSelections');
	if(rowIsNull(row)) return;
	var idList=[];
	for(var i = 0 ;i<row.length ;i++){
		idList.push(row[i].id);
	}
	parent.$.messager.confirm('提示', '确定要立即运行一次该任务？', function(data){
		if (data){
			$.ajax({
				type:'post',
				data:JSON.stringify(idList),
				url:"${ctx}/schedulePlan/startNow",
				contentType:'application/json;charset=utf-8',
				success: function(data){
					if(data=='success'){
						//dg.datagrid('reload');
						parent.$.messager.show({ title : "提示",msg: "操作成功！", position: "bottomRight" });
					}else{
						parent.$.messager.alert(data);
					}  
				}
			});
		}
	});
}
//删除
function del(){
	var rows = dg.datagrid('getSelections');
	if(rowIsNull(rows)) return;
	var idList=[];
	for(var i = 0 ;i<rows.length ;i++){
		idList.push(rows[i].id);
	}
	$.ajax({
		type:'post',
		data:JSON.stringify(idList),
		url:"${ctx}/schedulePlan/isMatchTask",
		contentType:'application/json;charset=utf-8',
		success: function(data){
			if(!data){
				parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
					if (data){
						$.ajax({
							type:'post',
							data:JSON.stringify(idList),
							url:"${ctx}/schedulePlan/delete",
							contentType:'application/json;charset=utf-8',
							success: function(data){
								if(data=='success'){
									dg.datagrid('reload');
									dg.datagrid('clearSelections');
									parent.$.messager.show({ title : "提示",msg: "操作成功！", position: "bottomRight" });
								}else{
									parent.$.messager.alert('提示',data);
								}
							}
						});
					} 
				});
			}else{
				alert("所删除的任务计划已经关联任务，请取消关联后，重试！");
			}
			/* if(data=='success'){
				dg.datagrid('reload');
				dg.datagrid('clearSelections');
				parent.$.messager.show({ title : "提示",msg: "操作成功！", position: "bottomRight" });
			}else{
				parent.$.messager.alert('提示',data);
			} */
		}
	});
}

//弹窗修改
function upd(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	d=$("#dlg").dialog({   
	    title: '修改调度计划',    
	    width: 530,    
	    height: 350,    
	    href:'${ctx}/schedulePlan/update/'+row.id,
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'修改',
			handler:function(){
				var planType = $("input[name='planType']:checked").val();
				if("0" == planType)
					setWeekCron();
				if("1" == planType)
					setDayCron();
				if("2" == planType)
					setHourCron();
				if("3" == planType)
					setMinuteCron();
				$("#mainform").submit();
			}
		},{
			text:'取消',
			handler:function(){
					d.panel('close');
				}
		}]
	});
}
//创建查询对象并查询
function cx(){
	var obj=$("#searchFrom").serializeObject();
	dg.datagrid('load',obj); 
}
</script>
</body>
</html>