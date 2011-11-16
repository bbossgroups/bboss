<%@ page contentType="text/html; charset=GBK" language="java" import="java.sql.*,java.util.List" errorPage=""%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<!-- 
	dbutil标签实现insert操作
	statement:数据库insert语句
	dbname:insert的相应数据库名称，在poolman.xml文件中进行配置

-->
<%
	String object_id = "1";
	String owner = "duoduo";
	String object_name = "table_insert";
	String created = "2010-03-12 16:52:10";
	String last_ddl_time = "2010-03-12 16:52:11";
	
	String sql = "insert into sqltest(object_id,owner,object_name,created,last_ddl_time) values(#[object_id],#[owner],#[object_name],#[created],#[last_ddl_time])";
 %>
<html>
<head>
<title>测试在dbutil标签上直接执行数据库插入操作</title>
</head>
<body>
	<table>
			<pg:dbutil statement="<%=sql %>" 
		    		  dbname="bspf"
		    		  pretoken="#\\[" endtoken="\\]"
		    		  action="insert">
				<pg:sqlparam name="object_id" value="<%=object_id %>" type="int"/>
				<pg:sqlparam name="owner" value="<%=owner %>" type="string"/>
				<pg:sqlparam name="object_name" value="<%=object_name%>" type="string"/>	
				<pg:sqlparam name="created" value="<%=created%>" type="date"/>		
				<pg:sqlparam name="last_ddl_time" value="<%=last_ddl_time%>" type="timestamp"/>
			</pg:dbutil>
		    
	</table>
</body>
</html>
