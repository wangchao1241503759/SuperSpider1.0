<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.ifc.service.Register" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%
String mcode = null;
mcode = Register.getMachineCode();
%>

<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>系统注册机-INFCN</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	
	<link href="<%=basePath%>license/css/css.css" rel="stylesheet" type="text/css" />

    <script language="JavaScript">
      //增加
	  function tj_onclick(){
	    if (document.form1.orgname.value==""){
			alert("请输入正确的机构名称!");
			document.form1.orgname.focus();
		    return false;
	    }
	    if (document.form1.username.value==""){
			alert("请输入正确的用户名称!");
			document.form1.username.focus();
		    return false;
	    }
	    if (document.form1.serialcode.value==""){
			alert("请输入正确的注册号码!");
			document.form1.serialcode.focus();
		    return false;
	    }
	    document.form1.action = "<%=basePath%>license/WapRegisterSuccess.jsp";
	    document.form1.submit();
	  }
	  
	  //清空  
	  function cz_onclick(){
	    document.form1.orgname.value = '';
	    document.form1.username.value = '';
	    document.form1.serialcode.value = '';
	    document.form1.backup.value = ''; 
	  }
		document.onkeydown = function(e){
			if(!e) e = window.event;//火狐中是 window.event
			if((e.keyCode || e.which) == 13){
				return false;
			}
		}; 
	</script>
	
  </head>
  
  <body class="body">
     <form action="" method="post" name="form1">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		  <tr>
		    <td height="30">&nbsp;</td>
		  </tr>
		</table>
		<table width="1002" border="0" align="center" cellpadding="0" cellspacing="0" class="table_Background">
		  <tr>
		    <td align="left" valign="top">
			    <table width="264" border="0" cellpadding="0" cellspacing="0" class="table_01">
			      <tr>
			        <td height="24" align="left"><img src="<%=basePath%>license/images/organization.gif" width="138" height="16" /></td>
			      </tr>
			      <tr>
			        <td height="34" align="left" valign="top"><input type="text" name="orgname" class="form_01" value="" /></td>
			      </tr>
			      <tr>
			        <td height="24" align="left"><img src="<%=basePath%>license/images/createstaff.gif" width="133" height="16" /></td>
			      </tr>
			      <tr>
			        <td height="34" align="left" valign="top"><input type="text" name="username" class="form_01" value="" /></td>
			      </tr>
			      <tr>
			        <td height="24" align="left"><img src="<%=basePath%>license/images/machinecode.gif" width="133" height="16" /></td>
			      </tr>
			      <tr>
			        <td height="84" align="left" valign="top"><textarea name="machinecode" class="form_03" readonly="readonly"><%=mcode%></textarea></td>
			      </tr>
			      <tr>
			        <td height="24" align="left"><img src="<%=basePath%>license/images/license.gif" width="102" height="16" /></td>
			      </tr>
			      <tr>
			        <td height="80" align="left" valign="top"><textarea name="serialcode" class="form_04" /></textarea></td>
			      </tr>
			      <tr>
			        <td height="24" align="left"><img src="<%=basePath%>license/images/notes.gif" width="102" height="16" /></td>
			      </tr>
			      <tr>
			        <td height="80" align="left" valign="top"><textarea name="backup" class="form_02" /></textarea></td>
			      </tr>
			      <tr>
			        <td height="40" align="center" valign="bottom">
				        <table width="80%" border="0" cellspacing="0" cellpadding="0">
				          <tr>
				            <td align="center"><input type="image" src="<%=basePath%>license/images/tj.gif" width="107" height="27" onClick="tj_onclick()"></td>
				            <td align="center"><input type="image" src="<%=basePath%>license/images/cz.gif" width="107" height="27" onClick="cz_onclick()"></td>
				          </tr>
				        </table>
				    </td>
			      </tr>
			    </table>
		    </td>
		  </tr>
		</table>
     </form>
  </body>
</html>
