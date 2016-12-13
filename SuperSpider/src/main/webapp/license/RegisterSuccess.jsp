<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="cn.com.infcn.reg.product.utils.CommonTools"%>
<%@ page import="java.util.Map"%>
<jsp:useBean id="li" scope="page" class="cn.com.infcn.reg.product.SDI3License" />
<%
	try {
%>
<html>
<head>

<title>系统注册机-INFCN</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link href="css/css.css" rel="stylesheet" type="text/css" />

<%
	String orgname = request.getParameter("orgname");
	String username = request.getParameter("username");
	String serialcode = request.getParameter("serialcode");
	String backup = request.getParameter("backup");

	li.loadSN(serialcode);

	String expiredday = CommonTools.dateToStr(li.getExpireDate(), "yyyy-MM-dd");
	long datecount = CommonTools.getDaysFromNow(li.getExpireDate());
	
	//String path = CommonTools.getWEBINFPath();
	String path = request.getSession().getServletContext().getRealPath("/WEB-INF");
	System.out.println(path);
	li.saveToLicenseFile(path, serialcode, orgname, username, backup);
	
	String db_count="";
	StringBuffer sb = new StringBuffer();
    int maxDBJobs = li.getMaxDBJobs();
    int maxWebJobs = li.getMaxWebJobs();
    int maxFtpJobs = li.getMaxFTPJobs();
    int maxFileJobs = li.getMaxFileJobs();
    sb.append("数据库可建").append(maxDBJobs).append("个任务；");
    sb.append("网络采集可建").append(maxWebJobs).append("个任务；");
    sb.append("FTP可建").append(maxFtpJobs).append("个任务；");
    sb.append("本地文件可建").append(maxFileJobs).append("个任务；");
	String[] dbTypes = li.getSupportDBType();
    sb.append("附加数据库类型有：");
	for(String dbtype:dbTypes)
	{
		sb.append(dbtype).append(";");
	}
	db_count = sb.toString();
%>
</head>

<body class="body">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td height="30">&nbsp;</td>
		</tr>
	</table>
	<table width="1002" border="0" align="center" cellpadding="0"
		cellspacing="0" class="Success_Background">
		<tr>
			<td align="left" valign="top">
				<table width="264" border="0" cellpadding="0" cellspacing="0"
					class="Success_table01">
					<tr>
						<td class="Success_font01">恭喜您注册成功！</td>
					</tr>
					<tr>
						<td class="Success_font02">您已是<span class="Success_font03">
								<%
								if (li.isTrial()) {
								%>试用用户<%
									} else {
								%>正式用户<%
									}
								%>
						</span>，享受本产品服务如下：
						</td>
					</tr>
				</table>
				<table width="250" border="0" cellpadding="0" cellspacing="0"
					class="table_02">
					<tr>
						<td width="250" align="left" valign="bottom"
							class="Success_font05">Registry / 注册机构：</td>
					</tr>
					<tr>
						<td height="22" align="left" valign="middle"
							background="images/01.gif"
							class="Success_font04"><%=orgname%></td>
					</tr>
					<tr>
						<td height="30" align="left" valign="bottom"
							class="Success_font05">Up staff / 注册人员：</td>
					</tr>
					<tr>
						<td height="22" align="left" valign="middle"
							background="images/02.gif"
							class="Success_font04"><%=username%></td>
					</tr>
					<tr>
						<td height="30" align="left" valign="bottom"
							class="Success_font05">Licensing / 许可授权：</td>
					</tr>
					<tr>
						<td height="22" align="left" valign="middle"
							background="images/03.gif"
							class="Success_font04"><%=expiredday%> 日 剩余 <%=datecount%> 天</td>
					</tr>
					<tr>
						<td height="30" align="left" valign="bottom"
							class="Success_font05">Total amount of Components / 每种采集组件支持的任务个数：</td>
					</tr>
					<tr>
						<td height="22" align="left" valign="middle"
							background="images/04.gif"
							class="Success_font04"><%=db_count%>  </td>
					</tr>
					<tr>
						<td height="30" align="left" valign="bottom"
							class="Success_font05">Information / 备注信息:</td>
					</tr>
					<tr>
						<td height="22" align="left" valign="middle"
							background="images/08.gif"
							class="Success_font04"><%=backup%></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>
<%
	} catch (Exception e) {
		e.printStackTrace();
		response.sendRedirect("RegisterFail.jsp");
	}
%>
