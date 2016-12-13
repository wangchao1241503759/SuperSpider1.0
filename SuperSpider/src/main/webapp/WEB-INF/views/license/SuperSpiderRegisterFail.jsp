<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>系统注册机-INFCN</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link href="<%=basePath%>static/css/license.css" rel="stylesheet" type="text/css" />
	<script language="JavaScript">
      //重新注册
	  function tj_onclick(){
	    document.form1.action = "<%=basePath%>license/SuperSpiderRegister.jsp";
	    document.form1.submit();
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
		<table width="1002" border="0" align="center" cellpadding="0" cellspacing="0" class="Failure_Background">
		  <tr>
		     <td align="left" valign="top">
			    <table width="264" border="0" cellpadding="0" cellspacing="0" class="Success_table01">
			      <tr>
			        <td class="failure_font01">抱歉，您此次注册失败！</td>
			      </tr>
			      <tr>
			        <td class="Success_font02">享受本产品服务您需要重新注册</td>
			      </tr>
			    </table>
			    <table width="205" border="0" cellpadding="0" cellspacing="0" class="Failure_table01">
			      <tr>
			        <td><input type="image" src="<%=basePath%>static/images/up.jpg" width="205" height="61" onFocus="this.blur()" onClick="tj_onclick()"></td>
			      </tr>
			    </table>
			    <table width="430" border="0" cellpadding="0" cellspacing="0" class="Failure_table03">
			      <tr>
			        <td align="left">*&nbsp;注册失败原因如下:</td>
			      </tr>
			    </table>
			    <table width="430" border="0" cellpadding="0" cellspacing="0" class="Failure_table02">
			      <tr>
			        <td align="left">
			            1、您输入的注册码有错误，请校验。<br />
			            2、系统注册过期，请确认。<br />
			            3、您申请的并发用户数不能低于零。<br />
			        </td>
			      </tr>
			    </table>
		     </td>
		  </tr>
		</table>
	</form>
  </body>
</html>
