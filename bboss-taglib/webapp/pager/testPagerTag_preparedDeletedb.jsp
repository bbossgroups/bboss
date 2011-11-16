<%@ page contentType="text/html; charset=GBK" language="java" import="java.sql.*,java.util.List" errorPage=""%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<!-- 
	测试在dbutil标签上直接执行数据库删除操作-预编译操作
	statement:数据库delete语句
	dbname:查询的相应数据库名称，在poolman.xml文件中进行配置
	
-->
<%
	String object_id = "15";
	
	String sql = "delete from sqltest where object_id=#[object_id] ";
 %>
<html>
<head>
<title>测试在dbutil标签上直接执行数据库delete操作</title>
</head>
<body>
	<table>
			<pg:dbutil statement="<%=sql %>" 
		    		  dbname="bspf"
		    		  action="delete"
		    		  pretoken="#\\[" endtoken="\\]"
		    		  >
				<pg:sqlparam name="object_id" value="<%=object_id %>" type="int"/>
			</pg:dbutil>
			<%=dbutil_result %>
		    
	</table>
</body>
</html>
