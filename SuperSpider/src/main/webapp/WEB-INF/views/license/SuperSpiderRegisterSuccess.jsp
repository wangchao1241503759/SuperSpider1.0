<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.ifc.util.FileUtil"%>
<%@page import="com.ifc.service.Register"%>
<%@page import="com.ifc.service.WapVerify"%>
<jsp:useBean id="DateUtil" scope="page" class="com.ifc.util.DateUtil"/>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%try{%>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>系统注册机-INFCN</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<link href="<%=basePath%>license/css/css.css" rel="stylesheet" type="text/css" />
	
	<%
	String orgname = request.getParameter("orgname");
	String username = request.getParameter("username");
	String serialcode = request.getParameter("serialcode");
	String backup = request.getParameter("backup");
	
	Map<String,String> snMap = new HashMap<String,String>();
	snMap.put("orgname", orgname);
	snMap.put("username", username);
	snMap.put("serialcode", serialcode);
	snMap.put("backup", backup);
	
	WapVerify wv = Register.wapRegister(FileUtil.getBasePath(),snMap);
	
	String userTypeChar = null;
	String verTypeDesc = null;
	String objnum = null;
	String crawlernum = null;
	String threadspercrawler = null;
	String recnum = null;
	String expiredday = null;
	
	long datecount = 0;
	
	if(wv == null){
		response.sendRedirect("WapRegisterFail.jsp");
	}else{		
		userTypeChar = wv.getUserTypeChar();
		verTypeDesc = wv.getVerTypeDesc();
		objnum =String.valueOf( wv.getObjNum());
		crawlernum =String.valueOf( wv.getCrawlerNum());
		threadspercrawler = String.valueOf( wv.getThreadsPerCrawler());
		recnum = String.valueOf( wv.getRecNum());
		expiredday = String.valueOf( wv.getExpiredDay());
		datecount = DateUtil.getTwoDay(expiredday,"yyyy-MM-dd");
	}
	%>
  </head>
  
  <body class="body">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td height="30">&nbsp;</td>
	  </tr>
	</table>
	<table width="1002" border="0" align="center" cellpadding="0" cellspacing="0" class="Success_Background">
	  <tr>
	    <td align="left" valign="top">
		    <table width="264" border="0" cellpadding="0" cellspacing="0" class="Success_table01">
		      <tr>
		        <td class="Success_font01">恭喜您注册成功！ </td>
		      </tr>
		      <tr>
		        <td class="Success_font02">您已是<span class="Success_font03"><%=verTypeDesc %><%if(("N").equals(userTypeChar)){%>正式用户<%}else{%>试用用户<%}%></span>，享受服务如下：</td>
		      </tr>
		    </table>
		    <table width="250" border="0" cellpadding="0" cellspacing="0" class="table_02">
		      <tr>
		        <td width="250" align="left" valign="bottom" class="Success_font05">注册机构：</td>
		      </tr>
		      <tr>
		        <td height="22" align="left" valign="middle" background="<%=basePath%>license/images/01.gif" class="Success_font04"><%=orgname%></td>
		      </tr>
		      <tr>
		        <td height="30" align="left" valign="bottom" class="Success_font05">注册人员：</td>
		      </tr>
		      <tr>
		        <td height="22" align="left" valign="middle" background="<%=basePath%>license/images/02.gif" class="Success_font04"><%=username%></td>
		      </tr>
		      <tr>
		        <td height="30" align="left" valign="bottom" class="Success_font05">许可授权：</td>
		      </tr>
		      <tr>
		        <td height="22" align="left" valign="middle" background="<%=basePath%>license/images/03.gif" class="Success_font04">到期时间：<%=expiredday%>。剩余  <%=datecount%> 天</td>
		      </tr>
		      <tr>
		        <td height="30" align="left" valign="bottom" class="Success_font05">爬取对象数：</td>
		      </tr>
		      <tr>
		        <td height="22" align="left" valign="middle" background="<%=basePath%>license/images/06.gif" class="Success_font04"><%=objnum%> 个</td>
		      </tr>
		      <tr>
		        <td height="30" align="left" valign="bottom" class="Success_font05">爬虫数：</td>
		      </tr>
		      <tr>
		        <td height="22" align="left" valign="middle" background="<%=basePath%>license/images/07.gif" class="Success_font04"><%=crawlernum%> 个</td>
		      </tr>
		      <tr>
		        <td height="30" align="left" valign="bottom" class="Success_font05">每个爬虫的线程数：</td>
		      </tr>
		      <tr>
		        <td height="22" align="left" valign="middle" background="<%=basePath%>license/images/04.gif" class="Success_font04"><%=threadspercrawler%> 个</td>
		      </tr>
		      <tr>
		        <td height="30" align="left" valign="bottom" class="Success_font05">存储记录总数数：</td>
		      </tr>
		      <tr>
		        <td height="22" align="left" valign="middle" background="<%=basePath%>license/images/05.gif" class="Success_font04"><%=recnum%> 个</td>
		      </tr>
		      <tr>
		        <td height="30" align="left" valign="bottom" class="Success_font05">备注信息:</td>
		      </tr>
		      <tr>
		        <td height="22" align="left" valign="middle" background="<%=basePath%>license/images/08.gif" class="Success_font04"><%=backup%></td>
		      </tr>
		    </table>
	    </td>
	  </tr>
	</table>
  </body>
</html>
<%} catch (Exception e) {
    response.sendRedirect("WapRegisterFail.jsp");
}%>
