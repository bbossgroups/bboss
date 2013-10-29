<%@page import="org.frameworkset.web.servlet.i18n.SessionLocalResolver"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
session.setAttribute(SessionLocalResolver.SESSION_LOCAL_KEY, java.util.Locale.US);
org.frameworkset.spi.support.MessageSource messageSource = org.frameworkset.web.servlet.support.WebApplicationContextUtils.getWebApplicationContext();
out.println("sss" + messageSource);
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
				
					<tr class="cms_report_tr">
						<!--设置分页表头-->

						<th width="2%" align="center" style="width:5%" class="order1 sorted">
						<input class="checkbox" 
							type="checkBox" hidefocus=true 
							name="checkBoxAll" 
							onClick="checkAll('checkBoxAll','ID')"> 
						</th>
						<th width="8%" class="order1 sorted">
							id					</th>
						<th width="8%" class="order1 sorted">
							name					</th>
						
					</tr>
				

					
				<pg:list requestKey="testmodel">
				
					<tr class="even">
						

						<td width="2%" align=center style="width:5%">
							<input class="checkbox" hideFocus onClick="checkOne('checkBoxAll','ID')" 
							type="checkbox" name="ID" 
							value="<pg:cell colName="id" defaultValue=""/>">
							
							<img border="0" src="${pageContext.request.contextPath}<pg:theme code="exclamation.gif"/>"
                                         alt="<pg:message code="probe.jsp.datasources.list.misconfigured.alt"/>"/>										
						</td>
						<td width="8%">
							<pg:cell colName="name" defaultValue=""/>		
							<pg:message  code="probe.jsp.wrongparams"/>
							
										</td>
						<td></td>
					</tr>
					</pg:list>
					
					
					<tr></tr>
				
	
				
	</table>
</body>
</html>
