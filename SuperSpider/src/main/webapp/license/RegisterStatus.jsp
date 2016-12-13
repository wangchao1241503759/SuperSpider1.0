<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="cn.com.infcn.reg.product.utils.CommonTools"%>
<jsp:useBean id="li" scope="page" class="cn.com.infcn.reg.product.MSP2License" />
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
	li.loadFromLicenseFile(CommonTools.getWEBINFPath());
	String orgname = li.getOrgName();
	String username = li.getUserName();
	String backup = li.getNote();
	String expiredday = CommonTools.dateToStr(li.getExpireDate(), "yyyy-MM-dd");
	long datecount = CommonTools.getDaysFromNow(li.getExpireDate());
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
						<td class="Success_font01">您已注册成功！</td>
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
							class="Success_font05">Total amount of data / 数据总量：</td>
					</tr>
					<tr>
						<td height="22" align="left" valign="middle"
							background="images/04.gif"
							class="Success_font04"><%=li.getTotalCount()%> 个</td>
					</tr>
					<tr>
						<td height="30" align="left" valign="bottom"
							class="Success_font05">Number of Meta DataBase / 元数据库数量：</td>
					</tr>
					<tr>
						<td height="22" align="left" valign="middle"
							background="images/05.gif"
							class="Success_font04"><%=li.getMetadbCount()%> 个</td>
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
		response.sendRedirect("RegisterFail.jsp");
	}
%>
