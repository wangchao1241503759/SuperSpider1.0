/**
 * treegrid点击行,级联选中
 */
$.extend($.fn.datagrid.methods,{
		addEditor : function(jq, param) { 
			if (param instanceof Array) { 
				$.each(param, function(index, item) {  
					var e = $(jq).datagrid('getColumnOption', item.field); 
					e.editor = item.editor;
				});
			} else {
				var e = $(jq).datagrid('getColumnOption', param.field);
				e.editor = param.editor;
			}
		},
		removeEditor : function(jq, param) { 
			if (param instanceof Array) { 
				$.each(param, function(index, item) { 
					var e = $(jq).datagrid('getColumnOption', item.field);
					e.editor = {};
				});
			} else { 
				var e = $(jq).datagrid('getColumnOption', param.field);
				e.editor = {};
			}
		}
});

$.extend($.fn.tabs.methods, {
	setTabTitle:function(jq,opts){
		return jq.each(function(){
			var tab = opts.tab;
			var options = tab.panel("options");
			var tab = options.tab;
			options.title = opts.title;
			var title = tab.find("span.tabs-title");
			title.html(opts.title);
		});
	},
	setIconCls:function(jq,opts){
		return jq.each(function(){
			var tab = opts.tab;
			var options = tab.panel("options");
			var tab = options.tab;
			options.iconCls = opts.iconCls;
			var icon = tab.find("span.tabs-icon");
			icon.attr("class","tabs-icon "+opts.iconCls);
		});
	},
	setHref:function(jq,opts){
		return jq.each(function(){
			var tab = opts.tab;
			var options = tab.panel("options");
			var tab = options.tab;
			options.href = opts.href;
			//tab.panel('refresh');
		});
	}
});
var Sys = {};
var ua = navigator.userAgent.toLowerCase();
var s;
(s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
(s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
(s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
(s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
(s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;