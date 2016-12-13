<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>数据库规则</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
</head>
<body>
		<div id="regexTabs" class="easyui-tabs" data-options="fit:false,border:false,plain:false">
<!-- 			<div title="数据过滤设置"> -->
<!-- 				<table> -->
<!-- 					<tr> -->
<!-- 						<td>过滤规则设置：</td> -->
<!-- 						<td><select id="filedlist" class="easyui-combobox" -->
<!-- 							name="state" style="width: 100px;" data-options="panelHeight:'auto'"> -->
<!-- 								<option value="id">id</option> -->
<!-- 								<option value="name">name</option> -->
<!-- 								<option value="createtime">createtime</option> -->
<!-- 								<option value="creator">creator</option> -->
<!-- 								<option value="description">description</option> -->
<!-- 								<option value="title">title</option> -->
<!-- 								<option value="others">...</option> -->
<!-- 						</select></td> -->
<!-- 						<td><select id="compare" class="easyui-combobox" name="state" -->
<!-- 							style="width: 60px;" data-options="panelHeight:'auto'"> -->
<!-- 								<option value="=">等于</option> -->
<!-- 								<option value=">">大于</option> -->
<!-- 								<option value=">=">大于等于</option> -->
<%-- 								<option value="<">小于</option> --%>
<%-- 								<option value="<=">小于等于</option> --%>
<%-- 								<option value="<>">不等于</option> --%>
<!-- 								<option value="others">...</option> -->
<!-- 						</select></td> -->
<!-- 						<td><input id="f_value" class="easyui-textbox" -->
<!-- 							style="width: 200px;" value=""></input></td> -->
<!-- 						<td><a href="#" class="easyui-linkbutton" -->
<!-- 							data-options="align:'right',iconCls:'icon-standard-add'">添加条件</a></td> -->
<!-- 					</tr> -->
<!-- 				</table> -->
<!-- 				<div style="height: 5px;"></div> -->
<!-- 				<div style="height: 220px"> -->
<!-- 					<input id="condition" class="easyui-textbox" -->
<!-- 						data-options="multiline:true" value="id='编号1' and name='标题1'" -->
<!-- 						style="width: 600px; height: 95%" /> -->
<!-- 				</div> -->
<!-- 				<div style="height: 5px;"></div> -->
<!-- 				<div> -->
<!-- 					<a href="#" class="easyui-linkbutton" -->
<!-- 						data-options="align:'right',iconCls:'icon-standard-application-view-columns'">源数据预览</a> -->
<!-- 					<a href="#" class="easyui-linkbutton" -->
<!-- 						data-options="align:'right',iconCls:'icon-standard-application-view-columns'">目标数据预览</a> -->
<!-- 				</div> -->
<!-- 			</div> -->
			<div title="增量识别-触发器">
				<table>
					<tr>
						<td>触发器名称：</td><td><input class="easyui-textbox" style="width:260px"/></td>
						<td>临时表名称：</td><td><input class="easyui-textbox" style="width:260px"/></td>
					</tr>
					<tr>
						<td>主键字段：</td><td><input class="easyui-textbox" style="width:260px"/></td>
						<td>状态字段：</td><td><input class="easyui-textbox" style="width:260px"/></td>
					</tr>
					<tr><td colspan="4">触发器脚本：</td></tr>
					<tr><td colspan="4"><input class="easyui-textbox" style="width:100%;height:200px" data-options="multiline:true" value="create trigger..."></input></td></tr>
					<tr>
						<td colspan="2"><input type="checkbox" checked="checked">是否启用数据库增量采集</input></td>
						<td colspan="2" align="right"><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-standard-add'">创建触发器</a></td>
					</tr>
				</table>
			</div>
			<div title="字段转换与映射">
				<table id="fieldmapping" class="easyui-datagrid" title="字段映射"
					style="width: 700px; height: 290px"
					data-options="rownumbers:true,singleSelect:false,pagination:false,pageSize:10">
					<thead>
						<tr>
							<th data-options="field:'ck',checkbox:true"></th>
							<th data-options="field:'fieldname',width:80">字段名称</th>
							<th data-options="field:'fieldtype',width:80">字段类型</th>
							<th data-options="field:'expression',width:200">字段转换表达式</th>
							<th data-options="field:'source',width:80">字段来源</th>
							<th data-options="field:'tofieldname',width:80">目标字段名称</th>
							<th data-options="field:'tofieldtype',width:80">目标字段类型</th>
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
			<div title="文件字段内容提取">
				<div style="height:3px"></div>
				<div>
					文件字段类型：
					<select id="filetype" class="easyui-combobox"
							name="filetype" style="width: 100px;" data-options="panelHeight:'auto'">
								<option value="blob">大字段存储</option>
								<option value="ftppath">FTP路径</option>
					</select>
					扩展名字段：
					<select id="filedlist" class="easyui-combobox"
							name="filetype" style="width: 112px;" data-options="panelHeight:'auto'">
								<option value="id">id</option>
								<option value="name">name</option>
								<option value="description">description</option>
								<option value="creator">creator</option>
								<option value="ext">ext</option>
								<option value="content">content</option>
					</select>
					内容字段（路径/二进制）：
					<select id="filedlist" class="easyui-combobox"
							name="filetype" style="width: 112px;" data-options="panelHeight:'auto'">
								<option value="id">id</option>
								<option value="name">name</option>
								<option value="description">description</option>
								<option value="creator">creator</option>
								<option value="ext">ext</option>
								<option value="content">content</option>
					</select>
				</div>
				<div id="ftpconnect" style="display:none">
					<div style="height:3px"></div>
					FTP服务器地址：<input id="ftp_ip" class="easyui-textbox" style="width: 100px;" value="192.168.1.1"></input>
					端口号：<input id="ftp_port" class="easyui-textbox" style="width: 50px;" value=""></input>
					用户名：<input id="ftp_user" class="easyui-textbox" style="width: 100px;" value="" ></input>
					密码：<input id="ftp_password" class="easyui-textbox" style="width: 100px;" value=""></input>
					类型：
					<select id="ftp_type" class="easyui-combobox" name="state" style="width: 55px;" data-options="panelHeight:'auto'">
						<option value="FTP">FTP</option>
						<option value="SFTP">SFTP</option>
					</select>
					<a href="#" class="easyui-linkbutton" data-options="align:'right',iconCls:'icon-hamburg-settings'">测试连接</a>
				</div>
				<div style="height:3px"></div>
				<div>
					<table id="fileattr" class="easyui-datagrid" title="选择要提取的属性"
						style="width: 700px; height: 230px"
						data-options="rownumbers:true,singleSelect:false,url:'${ctx}/spider/config/fileattr/json',method:'get'">
						<thead>
							<tr>
								<th data-options="field:'ck',checkbox:true"></th>
								<th data-options="field:'attributeName',width:150">属性名称</th>
								<th data-options="field:'refrenceType',width:180">关联文件类型</th>
								<th data-options="field:'fieldName',width:150">目标字段名称</th>
								<th data-options="field:'fieldType',width:130">目标字段类型</th>
							</tr>
						</thead>
					</table>
				</div>
				<div style="height:5px"></div>
				<div>
					<a href="#" class="easyui-linkbutton" data-options="align:'right',iconCls:'icon-standard-add'">添加提取属性</a>
					<a href="#" class="easyui-linkbutton" data-options="align:'right',iconCls:'icon-standard-delete'">删除提取属性</a>
					<a href="#" class="easyui-linkbutton" data-options="align:'right',iconCls:'icon-standard-application-view-columns'">同名自动匹配</a>
				</div>
			</div>
		</div>
	
	<script>
		function getData() {
			var rows = [];
			rows.push({
				fieldname:'id',
				fieldtype:'varchar(64)',
				expression:'-',
				source:'源字段',
				tofieldname:'id',
				tofieldtype:'varchar(64)'
			});
			rows.push({
				fieldname:'name',
				fieldtype:'varchar(255)',
				expression:'-',
				source:'源字段',
				tofieldname:'name',
				tofieldtype:'varchar(255)'
			});
			rows.push({
				fieldname:'createtime',
				fieldtype:'datetime',
				expression:'-',
				source:'源字段',
				tofieldname:'createtime',
				tofieldtype:'varchar(64)'
			});
			rows.push({
				fieldname:'year',
				fieldtype:'int',
				expression:'left(createtime,4)',
				source:'转换字段',
				tofieldname:'year',
				tofieldtype:'int'
			});
			
			return rows;
		}

		function pagerFilter(data) {
			if (typeof data.length == 'number'
					&& typeof data.splice == 'function') { // is array
				data = {
					total : data.length,
					rows : data
				}
			}
			var dg = $(this);
			var opts = dg.datagrid('options');
			var pager = dg.datagrid('getPager');
			pager.pagination({
				onSelectPage : function(pageNum, pageSize) {
					opts.pageNumber = pageNum;
					opts.pageSize = pageSize;
					pager.pagination('refresh', {
						pageNumber : pageNum,
						pageSize : pageSize
					});
					dg.datagrid('loadData', data);
				}
			});
			if (!data.originalRows) {
				data.originalRows = (data.rows);
			}
			var start = (opts.pageNumber - 1) * parseInt(opts.pageSize);
			var end = start + parseInt(opts.pageSize);
			data.rows = (data.originalRows.slice(start, end));
			return data;
		}
		
		$(function() {
			
			var dg=$('#fieldmapping');
			dg.datagrid({
				//loadFilter : pagerFilter;
			}).datagrid('loadData', getData());
			
			$('#filetype').combobox({
				onChange : function(newValue, oldValue) {
					if(newValue=="blob"){
						$('#ftpconnect').hide();
					}else{
						$('#ftpconnect').show();
					}
				}
			});
		});
		</script>
</body>
</html>