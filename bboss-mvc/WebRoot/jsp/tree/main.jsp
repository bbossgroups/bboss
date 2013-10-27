<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/commontag.tld" prefix="common"%>
<%@ page import="org.frameworkset.web.servlet.support.RequestContext" %>
<%
String path = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		

		<title>树demo</title>
<script type="text/javascript"> 
	function edit(message)
	{
		alert(message);
	}
	</script>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
	
		<pg:config />
		
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
					  $("#checkboxtreecontainer").load("checkboxtree.htm",{treetype:"checkboxtree_"});
					});
			</script>
		</span>
		<span id="contextmenutree">
				<script type="text/javascript">
				$(document).ready(function(){
					  $("#contextmenutree").load("contextmenutree.htm",{treetype:"contextmenutree_"});
					});
			</script>
		</span>
		<span id="radiotree">
				<script type="text/javascript">
				$(document).ready(function(){
					  $("#radiotree").load("radiotree.htm",{treetype:"radiotree_"});
					});
			</script>
		</span>
		
		<span id="querycontextmenutree">
				<script type="text/javascript">
				$(document).ready(function(){
					  $("#querycontextmenutree").load("querycontextmenutree.htm",{treetype:"querytree_"});
					});
			</script>
		</span>
		
		
		
		
			
	</body>