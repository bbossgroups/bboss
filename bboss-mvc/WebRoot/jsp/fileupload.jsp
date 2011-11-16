<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<!-- 
	bboss-mvc框架实现文件上传功能
-->
<html>
<head>
<title>bboss-mvc框架实现文件上传功能</title>
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
<head> 
 <title>jQuery Multiple File Upload Plugin v1.47 (2010-03-26)</title>
 <!--// documentation resources //-->
	<script src='../include/mutifile/jquery.js' type="text/javascript"></script>
	<script src='../include/mutifile/jquery.MultiFile.js' type="text/javascript"></script>




</head>
</head>
<body>
					<form action="<%=request.getContextPath() %>/demo/uploadFiles.htm" method="POST" enctype="multipart/form-data">
			<table class="genericTbl">
				<tr >
						<!--设置分页表头-->

					<th  style="width:20%" class="order1 sorted">请输入文件(最多两个文件)：
					<input name="test" type="file" class="multi" maxlength="20" accept="*.doc,*.zip,*.rar,*.ppt,*.pptx,*.doc,*.docx"/></th>
					
					<th  style="width:5%" colspan="100"  class="order1 sorted">
					<input type="submit" name="submit" value="上传"> 
					<!-- <input type="button" name="submit" onclick="alert('对不起，服务器空间有限，关闭附件上传功能。')"value="上传">-->
					</th>
					
				</tr>
			</table>
	</form>
</body>
</html>
