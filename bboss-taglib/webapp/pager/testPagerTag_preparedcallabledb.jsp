<%@ page contentType="text/html; charset=GBK" language="java" import="java.sql.*,java.util.List" errorPage=""%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<!-- 
	callableutil标签实现数据库函数和存储过程操作
	statement:存储过程或者函数
	dbname:批处理存储过程或者函数对应的数据库名称，在poolman.xml文件中进行配置

	
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
			<pg:batchutil dbname="bspf" type="prepared">
				<pg:statement sql="<%=sql %>" pretoken="#\\[" endtoken="\\]">
			    <pg:batch>
						<pg:sqlparam name="object_id" value="<%=object_id %>" type="int" />				
						<pg:sqlparam name="created" value="<%=created %>" type="timestamp" />	
					</pg:batch>
					<pg:batch>
						<pg:sqlparam name="object_id" value="<%=object_id %>" type="int" />				
						<pg:sqlparam name="created" value="<%=created %>" type="timestamp" />	
					</pg:batch>
				</pg:statement>
				<pg:statement sql="<%=sql1 %>" pretoken="#\\[" endtoken="\\]">
						<pg:sqlparam name="object_id" value="<%=object_id %>" type="int" />				
						<pg:sqlparam name="created" value="<%=created %>" type="timestamp" />	
				</pg:statement>
			</pg:batchutil>
	</table>
</body>
</html>
