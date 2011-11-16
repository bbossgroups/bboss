<%@ page contentType="text/html; charset=GBK" language="java" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/commontag.tld" prefix="common"%>
<%@ page import="org.frameworkset.web.servlet.support.RequestContext" %>
<%
String path = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		

		<title>发送xml数据demo</title>
<script type="text/javascript"> 
	function sendxml()
	{
		var xmlhttp =null;
		if(window.ActiveXObject)
		{
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
			
		}
		else if(window.XMLHttpRequest)
		{
			xmlhttp = new XMLHttpRequest();
			
		}
		else
		{
			alert("aaaa");
			return;
		}
		var xml = "<web><version>国产 1.0</version></web>";
		xmlhttp.open("POST","echo.page");
		xmlhttp.setRequestHeader("Content-Length",xml.length);
		xmlhttp.setRequestHeader("CONTENT-TYPE","text/xml;charset=UTF-8");
		xmlhttp.onreadystatechange=function(){
			if(xmlhttp.readyState == 4)
			{
				alert(xmlhttp.responseText);
			}
		}
		xmlhttp.send(xml);
	}
	</script>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
	
		<pg:config enablecontextmenu="false"/>
		
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
	
	
		<span id="checkboxtreecontainer">
				<script type="text/javascript">
				$(document).ready(function(){
					 sendxml();
					});
			</script>
		</span>			
	</body>