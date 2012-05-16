<%@page import="org.frameworkset.web.servlet.i18n.SessionLocalResolver"%>
<%@ page contentType="text/html; charset=GBK" language="java" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
session.setAttribute(SessionLocalResolver.SESSION_LOCAL_KEY, java.util.Locale.US);
org.frameworkset.spi.support.MessageSource messageSource = org.frameworkset.web.servlet.support.WebApplicationContextUtils.getWebApplicationContext();
//messageSource.getMessage("test.multifiles");

//out.println("sss:" + messageSource.getMessage("test.multifiles"));
%>
<html>
<head>
<title>主题和国际化</title>
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/css/classic/tables.css"
			type="text/css">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/css/classic/main.css"
			type="text/css">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/css/classic/mainnav.css"
			type="text/css">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/css/classic/messages.css"
			type="text/css">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/css/classic/tooltip.css"
			type="text/css">
</head>
<body>
	<table  class="genericTbl">
				
					
				

					
			
				
					<tr class="even">
						

						<td width="8%">
							
							<pg:message  code="probe.jsp.wrongparams"/>
							
										</td>
						<td width="8%">
							
							<pg:message  code="test.multifiles"/>
							
										</td>
					</tr>
					
					
					
					
	
				
	</table>
</body>
</html>
