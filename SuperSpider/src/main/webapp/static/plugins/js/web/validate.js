/**
 * EasyUI
 * 自定义验证
 * @author lihf
 */
$.extend($.fn.validatebox.defaults.rules, {
	checkYear : {
		validator : function(value, param) {
			return checkYear(value);
		},
		message : '输入项只能是4位数字字符！'
	},
	checkSecond : {
		validator : function(value, param) {
			return checkSecond(value);
		},
		message : '输入项只能是0-9999数字字符！'
	},
	checkNum : {
		validator : function(value, param) {
			return checkNum(value);
		},
		message : '输入项只能是数字字符！'
	},
	  // 开始日期不能大于结束日期校验
	  checkDateTo : {
	    validator : function(value, param) {
	      return endDateValidMatch(value,$(param[0]).datebox('getValue'));
	    },
	    message : '开始日期不能大于结束日期！'
	  },
	checkDataGrid : {
		validator : function(value, param) {
			return checkDataGridValidMatch(value,param[0],param[1],param[2],param[3],param[4]);
		},
		message : '该字段名称已经存在！'
	},
	  checkTypeDataGrid : {
		validator : function(value, param) {
			return checkTypeDataGridValidMatch(value, param[0], param[1], param[2]);
		},
		message : '该字段名称已经存在！'
	},
	checkLevelDataGrid : {
		validator : function(value, param) {
			return checkLevelDataGridValidMatch(value, param[0], param[1], param[2]);
		},
		message : '该级别只能是1-2个数字字符,并且不能重复！'
	},
	// 名称只能是1-50个中英文字符
	nameValid:{
	  validator : function(value, param) {
	    return nameValidMatch(value);
	  },
	  message : '该名称只能是1-50个中英文字符,不能包含特殊字符!！@#￥$%……^&*！'
	},
  checkProxyIPDataGrid : {
		validator : function(value, param) {
			return checkProxyIPDataGridValidMatch(value, param[0], param[1], param[2]);
		},
		message : '该输入项已经存在！'
	}
});


/*验证年 */
var checkYear = function(value, param)
{
	  var reg = /^[\d]{4}$/;
	  return (reg.test(value));
};

/*验证秒 */
var checkSecond = function(value, param)
{
	var reg = /^[\d]{1,4}$/;
	return (reg.test(value));
};

/*验证数字 */
var checkNum = function(value, param)
{
	var reg = /^[0-9]+$/;
	return (reg.test(value));
};


/* 开始日期不能大于结束日期 */
var endDateValidMatch=function(value,beginDate) {
  var flag = true;
  var arrd1 = beginDate.split('-'), arrd2 = value.split('-');
  var d1 = new Date(parseInt(arrd1[0], 10), parseInt(arrd1[1], 10) - 1, parseInt(arrd1[2], 10));
  var d2 = new Date(parseInt(arrd2[0], 10), parseInt(arrd2[1], 10) - 1, parseInt(arrd2[2], 10));
  if(d1>d2)
  {
     flag = false;
  }
  return flag;
};

/* 检测datagrid */
var checkDataGridValidMatch=function(value,id,name,paramObjectList,html_currentTypeId,editing) {
	var flag = true;
	var flag_combobox = false;
	var paramRows = $(id).datagrid('getRows');
	if (paramRows.length > 0) {
		for (var i = 0; i < paramRows.length; i++) {
			if ( i!=editing && paramRows[i].name == value) {
				 flag = false;
				 flag_combobox = true;
				break;
			}
		}

	}
	
	var typeId = $(html_currentTypeId).combobox('getValue');
	for (var i = 0; i < paramObjectList.length; i++) {
		var fieldExtractRows = paramObjectList[i].fieldExtractList;
		if (fieldExtractRows.length > 0) {
			for (var j = 0; j < fieldExtractRows.length; j++) {
				if (paramObjectList[i].typeId != typeId && fieldExtractRows[j].name == value 
						&& fieldExtractRows[j].id == value) {
					flag = false;
					flag_combobox = true;
					break;
				}
			}
		}
	}
//	$(html_currentTypeId).combobox({'disabled':flag_combobox});
	return flag;
};

/* 检测类型datagrid */
var checkTypeDataGridValidMatch=function(value,id,typeName,editing) {
	var flag = true;
	var rows = $(id).datagrid('getRows');
	if (rows.length > 0) {
		for (var i = 0; i < rows.length; i++) {
			if( i!=editing && rows[i].typeName == value) {
				flag = false;
				break;
			}
		}
	}
	return flag;
};
/* 检测类型datagrid */
var checkLevelDataGridValidMatch=function(value,id,typeLevel,editing) {
	var flag = true;
	var reg = /^[\d]{1,2}$/;
	var rows = $(id).datagrid('getRows');
	if (rows.length > 0) {
		for (var i = 0; i < rows.length; i++) {
			if( i!=editing && rows[i].typeLevel == value || !reg.test(value)) {
				flag = false;
				break;
			}
		}
	}
	return flag;
};

/* 该名称只能是1-50个中英文字符,不能包含特殊字符!！@#￥$%……^&*！*/
var nameValidMatch=function(value) {
  var reg = /^[^!！@#￥$%……^&*  ]{1,50}$/;
//  var reg = /^[^((?!\!@#$%^&*).){1,3}$]{1,3}$/;
  return (reg.test(value));
};

/* 检测类型datagrid */
var checkProxyIPDataGridValidMatch=function(value,dataGridId,ip,editing) {
	var flag = true;
	var rows = $(dataGridId).datagrid('getRows');
	if (rows.length > 0) {
		for (var i = 0; i < rows.length; i++) {
			if( i!=editing && rows[i].ip == value) {
				flag = false;
				break;
			}
		}
	}
	return flag;
};
