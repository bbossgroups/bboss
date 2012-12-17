<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<!-- 
	batchutil标签实现数据库批处理操作
	statement:指定批处理语句
	dbname:批处理语句执行的对应的数据库连接池名称，在poolman.xml文件中进行配置

	
-->
<%
	
    String insert = "insert into sqltest(batch,object_id) values('0','1')";
	String sql = "update sqltest set batch='1' where object_id=1";
	String sql1 = "update sqltest set batch='2' where object_id=1";
	String sql2 = "update sqltest set batch='3' where object_id=1";
	String sql3 = "update sqltest set batch='4' where object_id=1";
	String sql4 = "update sqltest set batch='5' where object_id=1";
%>
<html>
	<head>
		<title>测试在batchutil标签上直接执行数据库批处理操作</title>
	</head>
	<body>
		<table>
			<pg:batchutil dbname="bspf" type="common">
				<pg:statement sql="<%=insert%>" />
				<pg:statement sql="<%=sql %>" />
				<pg:statement sql="<%=sql1 %>" />
				<pg:statement sql="<%=sql2 %>" />				
				<pg:statement sql="<%=sql3 %>" />
				<pg:statement sql="<%=sql4 %>" />
			</pg:batchutil>
		</table>
	</body>
</html>
