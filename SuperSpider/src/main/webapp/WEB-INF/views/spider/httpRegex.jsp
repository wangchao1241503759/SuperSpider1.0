<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>http规则设置</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'; font-size: 9px;">
	<div class="easyui-layout" data-options="fit: true">
		<div class="easyui-tabs" data-options="fit:false,border:false">
			<div title="采集范围控制">
				<div style="height:5px"></div>
				<div style="width:760px;height:290px">
					<iframe src="${ctx}/spider/config/http/regexconfig" frameborder="0"	scrolling="yes" height="100%" width="100%"></iframe>
				</div>
				<div style="height:10px"></div>
				<div style="font-weight: bold; color: blue; font-size: 13px;">说明：页面过滤用于设置http爬取时过滤出指定的链接内容，以减少不相关链接对http爬取效率的影响</div>
			</div>
			<div title="页面匹配规则">
				<div class="easyui-panel" style="width: 100%; height: 340px; padding: 10px;border:none" data-options="fit:true">
					<div class="easyui-layout" data-options="fit:true">
						<div data-options="split:false"	style="width: 760px; height:320px; border:none;padding: 0px">
							<div class="easyui-panel" title="页面匹配规则列表：" style="height:280px;border:block;padding:5px" data-options="region:'north'">
								<table style="width:100%">
									<tr>
										<td bgcolor="gray" style="text-align: center;font-weight: bold; color: white"><input type="checkbox"></input></td>
										<td bgcolor="gray" style="width:30%;text-align: center;font-weight: bold; color: white">规则名称</td>
										<td bgcolor="gray" style="width:40px;text-align: center;font-weight: bold; color: white">级别</td>
										<td colspan="2" bgcolor="gray" style="width:60%;text-align: center;font-weight: bold; color: white">页面匹配</td>
									</tr>
									<tr>
										<td style="text-align: center" bgcolor="lightblue"><input type="checkbox" checked="checked"></input></td>
										<td style="text-align: center" bgcolor="lightblue">分类页匹配规则</td>
										<td style="text-align: center" bgcolor="lightblue">1</td>
										<td style="text-align: center" bgcolor="lightblue">开始于：category</td>
										<td style="text-align: center; width:10px" bgcolor="lightblue" onclick="add()">…</td>
									</tr>
									<tr>
										<td style="text-align: center"><input type="checkbox"></input></td>
										<td style="text-align: center">列表页匹配规则</td>
										<td style="text-align: center">2</td>
										<td style="text-align: center">包含：products</td>
										<td style="text-align: center; width:10px" bgcolor="lightblue" onclick="add()">…</td>
									</tr>
									<tr>
										<td style="text-align: center"><input type="checkbox"></input></td>
										<td style="text-align: center">内容页匹配规则</td>
										<td style="text-align: center">3</td>
										<td style="text-align: center">包含：detail</td>
										<td style="text-align: center; width:10px" bgcolor="lightblue" onclick="add()">…</td>
									</tr>
									<tr>
										<td style="text-align: center"><input type="checkbox"></input></td>
										<td style="text-align: center">参数页匹配规则</td>
										<td style="text-align: center">4</td>
										<td style="text-align: center">包含：productparam</td>
										<td style="text-align: center; width:10px" bgcolor="lightblue" onclick="add()">…</td>
									</tr>
									<tr>
										<td style="text-align: center"><input type="checkbox"></input></td>
										<td style="text-align: center">评价页匹配规则</td>
										<td style="text-align: center">5</td>
										<td style="text-align: center">包含：evaluate</td>
										<td style="text-align: center; width:10px" bgcolor="lightblue" onclick="add()">…</td>
									</tr>
									<tr>
										<td style="text-align: center"><input type="checkbox"></input></td>
										<td style="text-align: center">...</td>
										<td style="text-align: center">6</td>
										<td style="text-align: center">包含：sina.com</td>
										<td style="text-align: center; width:10px" bgcolor="lightblue" onclick="add()">…</td>
									</tr>
								</table>
							</div>
							<div style="height:5px"></div>
							<div class="easyui-panel" style="height:30px;border:none" data-options="region:'south'">
								<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-standard-add'">添加匹配规则</a>
								<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-standard-delete'">删除匹配规则</a>
								<span style="width:50px"></span>
								<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-hamburg-up'">上移</a>
								<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-hamburg-down'">下移</a>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div title="解析参数设置">
			    <div style="height:3px"></div>
				<div style="height: 340px;border:none">
					<div style="font-weight:bold">
						<select class="easyui-combobox" style="width:180px" data-options="panelHeight:'auto'">
							<option value="regex1">分类页匹配规则</option>
							<option value="regex2">列表页匹配规则</option>
							<option value="regex3">内容页匹配规则</option>
							<option value="regex4">参数页匹配规则</option>
							<option value="regex5">评价页匹配规则</option>
							<option value="regex6">...</option>
						</select>
						<span style="color: red">参数设置：</span>
					</div>
					<div style="height:3px"></div>
					<table>
						<tr>
							<td>是否设置跳转：</td>
							<td>
								<input type="radio" name="jumptag" value="01" onclick="enableInput()"></input><span>是</span> 
								<input type="radio" name="jumptag" checked="checked" value="02" onclick="enableInput()"></input><span>否</span>
							</td>
							<td style="width:43px"></td>
							<td>跳转参数名称：<input id="param_name" class="easyui-textbox" style="width:190px"></input></td>
							<td style="width:3px"></td>
							<td>跳转增量值：<input id="param_value" class="easyui-textbox" style="width:190px"></input></td>
						</tr>
					</table>
					<table>
						<tr>
							<td>
								<div class="easyui-panel" style="width:200px;height:233px;padding:5px">
									<ul id="fieldtree" class="easyui-tree" data-options="animate:true"></ul>	
								</div>
							</td>
							<td>
								<table id="fieldregex" class="easyui-datagrid" title="字段提取规则"
									style="width: 555px; height: 233px"
									data-options="rownumbers:true,singleSelect:false,pagination:false,pageSize:10">
									<thead>
										<tr>
											<th data-options="field:'ck',checkbox:true"></th>
											<th data-options="field:'regextype',width:80">类型</th>
											<th data-options="field:'expression',width:380">表达式</th>
										</tr>
									</thead>
								</table>
							</td>
						</tr>
						<tr>
							<td>
								<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-standard-add'">添加字段</a>
								<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-standard-delete'">删除字段</a>
							</td>
							<td align="right">
								<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-standard-add'">添加表达式</a>
								<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-standard-delete'">删除表达式</a>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div title="字段映射与转换">
				<table id="fieldmapping" class="easyui-datagrid" title="字段映射"
					style="width: 768px; height: 290px"
					data-options="rownumbers:true,singleSelect:false,pagination:false,pageSize:10">
					<thead>
						<tr>
							<th data-options="field:'ck',checkbox:true"></th>
							<th data-options="field:'fieldname',width:80">字段名称</th>
							<th data-options="field:'expression',width:300">字段转换表达式</th>
							<th data-options="field:'source',width:80">字段来源</th>
							<th data-options="field:'tofieldname',width:80">目标字段名称</th>
							<th data-options="field:'tofieldtype',width:120">目标字段类型</th>
						</tr>
					</thead>
				</table>
				<div style="height: 5px;"></div>
				<div>
					<a href="#" class="easyui-linkbutton"
						data-options="align:'right',iconCls:'icon-standard-add'">添加转换字段</a>
					<a href="#" class="easyui-linkbutton"
						data-options="align:'right',iconCls:'icon-standard-delete'">删除转换字段</a>
					<a href="#" class="easyui-linkbutton"
						data-options="align:'right',iconCls:'icon-standard-application-view-columns'">同名自动匹配</a>
				</div>
			</div>
		</div>
	</div>
	
	<script type="text/javascript">
	function enableInput(){
	    $('#param_name').attr("disabled",$("input[name='jumptag']:checked").val()=="02");
	    $('#param_value').attr("disabled",$("input[name='jumptag']:checked").val()=="02");
	}	
	
		function initializeFieldTree(){
			var root={id:'1',text:'字段列表'};
			$('#fieldtree').tree('append',{parent:null,data:root});
			var rootNode = $('#fieldtree').tree('getRoot');
			var nodes = [{id:'101',text:'title'},
			             {id:'102',text:'creator'},
			             {id:'103',text:'createtime'},
			             {id:'104',text:'author'},
			             {id:'105',text:'description'},
			             {id:'106',text:'content'}];
			$('#fieldtree').tree('append',{parent:rootNode.target,data:nodes});
		}
		
		$(function(){
			initializeFieldTree();
		});
		
		function switchItem(tag){
			switch(tag){
			case 1:
				$("#fieldmap").show();
				$("#jumpregex").hide();
				break;
			case 2:
				$("#fieldmap").hide();
				$("#jumpregex").show();
				break;
			default:
				break;
			}
		}

		//弹窗设置匹配规则
		function add() {
			var handle = parent.$("#dlg");
			d = handle.dialog({
				title : '设置匹配规则',
				width : 775,
				height : 370,
				closed : false,
				cache : false,
				maximizable : false,
				resizable : false,

				href : '${ctx}/spider/config/http/regexconfig',
				modal : true,
				buttons : [ {
					text : '确认',
					handler : function() {
						d.panel('close');
					}
				}, {
					text : '取消',
					handler : function() {
						d.panel('close');
					}
				} ]
			});
		}
		
		function getData(){
			var rows = [];
			rows.push({
				regextype:'xpath',
				expression:'//*[@id="J_ItemList"]/div[59]/div/div',
			});
			rows.push({
				regextype:'exp',
				expression:'exp expression',
			});
			rows.push({
				regextype:'regex',
				expression:'regex expression',
			});
			return rows;
		}
		
		function getMappingData() {
			var rows = [];
			rows.push({
				fieldname:'title',
				expression:'-',
				source:'源字段',
				tofieldname:'title',
				tofieldtype:'varchar(255)'
			});
			rows.push({
				fieldname:'creator',
				expression:'-',
				source:'源字段',
				tofieldname:'creator',
				tofieldtype:'varchar(20)'
			});
			rows.push({
				fieldname:'createtime',
				expression:'-',
				source:'源字段',
				tofieldname:'createtime',
				tofieldtype:'date'
			});
			rows.push({
				fieldname:'author',
				expression:'-',
				source:'源字段',
				tofieldname:'author',
				tofieldtype:'varchar(20)'
			});
			rows.push({
				fieldname:'description',
				expression:'-',
				source:'源字段',
				tofieldname:'description',
				tofieldtype:'varchar(1000)'
			});
			rows.push({
				fieldname:'content',
				expression:'-',
				source:'源字段',
				tofieldname:'content',
				tofieldtype:'blob'
			});
			rows.push({
				fieldname:'summary',
				expression:'left(content,200)',
				source:'转换字段',
				tofieldname:'summary',
				tofieldtype:'varchar(255)'
			});
			
			return rows;
		}
		
		$(function(){
			switchItem(1);
			enableInput();
			$('#fieldregex').datagrid({
				//loadFilter : pagerFilter
			}).datagrid('loadData', getData());
			$('#fieldmapping').datagrid({
				//loadFilter : pagerFilter
			}).datagrid('loadData', getMappingData());
		});
	</script>
</body>
</html>