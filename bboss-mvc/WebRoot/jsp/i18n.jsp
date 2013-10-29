<%@page import="java.util.Locale"%>
<%@page import="org.frameworkset.web.servlet.i18n.WebMessageSourceUtil"%>
<%@page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>
<%@page import="org.frameworkset.web.servlet.i18n.SessionLocalResolver"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
//session.setAttribute(SessionLocalResolver.SESSION_LOCAL_KEY, java.util.Locale.US);
org.frameworkset.spi.support.MessageSource messageSource = org.frameworkset.web.servlet.support.WebApplicationContextUtils.getWebApplicationContext();
//messageSource.getMessage("test.multifiles");

//out.println("sss:" + messageSource.getMessage("test.multifiles"));
out.println(RequestContextUtils.getRequestContextLocalName(pageContext));
String local = RequestContextUtils.getRequestContextLocalName(pageContext);
String local_request = RequestContextUtils.getRequestContextLocalName(request);
Locale locale = RequestContextUtils.getRequestContextLocal(request);
//String message = messageSource.getMessage("probe.jsp.wrongparams", local_request);
messageSource = WebMessageSourceUtil.getMessageSource("org/frameworkset/servlet/i18n/messages_module");
out.println(messageSource.getMessage("sany.pdp.module.personcenter", locale));
MessageSource ms = WebMessageSourceUtil.getMessageSource("/WEB-INF/messages_pdp,"+
		"/WEB-INF/messages_pdp_common,/WEB-INF/conf/appbom/messages_appbom", pageContext.getServletContext()) ;
%>
<html>
<head>
<title>主题和国际化,你选择的国家代码-<pg:locale/>-local_request[<%=local_request %>]</title>
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
							
							<pg:message  code="probe.jsp.wrongparams" />
							
										</td>
						<td width="8%">
							
							<pg:message  code="test.multifiles"/>
							
										</td>
					</tr>
					
					
					
					
	
				
	</table>
</body>
</html>
