<%@ page contentType="text/html; charset=GBK" language="java" import="java.sql.*,java.util.List" errorPage=""%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<!-- 
	dbutil标签实现update操作
	statement:数据库update语句
	dbname:update的相应数据库名称，在poolman.xml文件中进行配置

	
-->
<%
	String object_id = "1";
	
	String created = "2010-03-12 12:43:54";
	
	String sql = "update sqltest set created=#[created] where object_id=#[object_id]";
 %>
<html>
<head>
<title>测试在dbutil标签上直接执行数据库update操作</title>
</head>
<body>
	<table>
			
			<pg:dbutil statement="<%=sql %>" 
		    		  dbname="bspf"
		    		  action="update"
		    		  pretoken="#\\[" endtoken="\\]"
		    		  >
				<pg:sqlparam name="object_id" value="<%=object_id %>" type="int" />				
				<pg:sqlparam name="created" value="<%=created %>" type="timestamp" />				
			</pg:dbutil>
			<%=dbutil_result %>
			
		    
	</table>
</body>
</html>
