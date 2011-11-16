<%@ page language="java" contentType="text/html; charset=GBK" session="false"%>

<%
	String id = request.getParameter("id");
%>
<html>

<head>
<title>spiπ‹¿Ì</title>
</head>
<frameset name="userId" value="" cols="30%,70%" frameborder="no" border="1" framespacing="3" style="width:100%;height:100%;">
  <frame src="spitree.jsp" name="spiTree" id="spiTree" />
  <frame src="configfileDetail.jsp" name="configfileDetail" id="configfileDetail" style="width:100%;height:100%;"/>
</frameset>
<body>

</body>
</html>