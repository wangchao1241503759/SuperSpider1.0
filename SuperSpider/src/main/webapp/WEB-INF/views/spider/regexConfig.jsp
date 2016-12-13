<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>数据采集任务管理</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'; font-size:9px;">
	<table>
		<tr>
			<td>
				选择规则类型：
				<select id="regextype" class="easyui-combobox"	data-options="panelHeight:'auto'" style="width: 90px">
					<option value="startwith">开始于</option>
					<option value="endwith">结束于</option>
					<option value="include">包含</option>
					<option value="equal">等于</option>
					<option value="regex">正则表达式</option>
				</select> 
				<input type="text" name="regexparam" class="easyui-validatebox"	style="width: 240px" data-options="prompt: '输入链接匹配字符串'"/> 
				<input type="checkbox" value="negate">取反</input>
			</td>
			<td align="right" width="240px" style="padding-left:30px">
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-standard-add'">添加规则</a> 
				<a href="#"	class="easyui-linkbutton" data-options="iconCls:'icon-standard-delete'">删除规则</a>
			</td>
		</tr>
	</table>
	<table id="regextable" class="easyui-datagrid" title="已设置的规则列表"
		style="width: 760px; height: 240px"
		data-options="rownumbers:true,singleSelect:false,pagination:false,pageSize:10">
		<thead>
			<tr>
				<th data-options="field:'ck',checkbox:true"></th>
				<th data-options="field:'regextype',width:120">规则类型</th>
				<th data-options="field:'regexparam',width:500">规则参数</th>
			</tr>
		</thead>
	</table>
	<div>
		过滤规则选项： <input type="radio" name="filteroption" checked="checked"
			value="01"/><span>单项符合</span> <input type="radio"
			name="filteroption" value="02"/><span>全部符合</span>
		</td>
	</div>
	
	<script>
		function getData() {
			var rows = [];
			rows.push({
				regextype:'包含',
				regexparam:'sina.com',
			});
			rows.push({
				regextype:'结束于',
				regexparam:'.cn',
			});
			rows.push({
				regextype:'开始于',
				regexparam:'sports',
			});
			rows.push({
				regextype:'不包含',
				regexparam:'sohu',
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
			$('#regextable').datagrid({
				loadFilter : pagerFilter
			}).datagrid('loadData', getData());
		});
		</script>
</body>
</html>