<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<!-- 
	bboss-mvc框架实现文件上传功能
-->
<html>
<head>
<title>bboss-mvc框架实现文件上传功能-ok</title>
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
					
	<table class="genericTbl">
	<tr >
						<!--设置分页表头-->

					<th  style="width:20%" class="order1 sorted">文件名称</th>
					
					<th  style="width:50%" class="order1 sorted">文件路径：</th>
					
					<th  style="width:20%" class="order1 sorted">文件大小：</th>
					
				</tr>
	<pg:list requestKey="uploadfiles">
				<tr class="even">
						<!--设置分页表头-->

					
					<td  style="width:20%"><pg:cell colName="fileName" defaultValue=""/></td>
					
					<td  style="width:50%"><pg:cell colName="filePath" defaultValue=""/></td>
					
					<td  style="width:5%" ><pg:cell colName="fileSize" defaultValue=""/> bytes</td>
					
				</tr>
				</pg:list>
	</table>
	
</body>
</html>
