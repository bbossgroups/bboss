<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>

		<title>DatePicker</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/jsp/datepicker/My97DatePicker/WdatePicker.js"></script>
         <link rel="shortcut icon"
		href="${pageContext.request.contextPath}/css/favicon.gif">
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
	<h1>日期格式转换demo转换结果</h1>
	<form action="dateconvert.html" method="post">
	<table class="genericTbl">
	   <tr>
	   <th class="order1 sorted" >demo描述
	   </th>
	   <th class="order1 sorted">演示区
	   </th>
	   </tr>
	 <tr class="even" >
	   
	    <td align="right">
	   普通触发：
	    </td>
	    <td>
	  	<%=request.getAttribute("java.util.Date") %>
	   </td>
	   </tr>
	  
	
		
		<tr class="even">
	    <td align="right">
		精确到日期：
		</td>
	    <td>
		<%=request.getAttribute("java.sql.Date") %>
		</td>
	   </tr>
	   <tr class="even">
	    <td align="right">
		精确具体时间：
		</td>
	    <td>
		<%=request.getAttribute("java.sql.Timestamp") %>
		</td>
	    </tr>
	    
	     <tr class="even">
	    <td align="right">
		返回：
		</td>
	    <td>
		<input type="submit" value="返回"/>
		</td>
	    </tr>
		
		</table>
		</form>
	</body>
</html>