<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<!-- 
	batchutil标签实现数据库预编译批处理操作
	statement:指定预编译批处理语句
	dbname:预编译批处理语句执行的对应的数据库连接池名称，在poolman.xml文件中进行配置

	alter table SQLTEST add clobdata clob;
alter table SQLTEST add blobdata blob;
-->
<%
	int object_id = 1;

	String created = "2010-03-12 12:43:54";
	String created1 = "2010-03-13 12:43:54";
	String created2 = "2010-03-14 12:43:54";
	String created3 = "2010-03-15 12:43:54";
	String created4 = "2010-03-18 12:43:54";
	
	java.io.File blobdata = new java.io.File("D:/workspace/bbossgroups-3.5/bboss-taglib/lib/ecs-1.4.2.jar");
	java.io.File clobdata = new java.io.File("D:\\bbossgroups-3.5.1\\bboss-taglib\\readme.txt");
	
	
	String sql = "update sqltest set created=#[created],clobdata=#[clobdata],blobdata=#[blobdata] where object_id=#[object_id]";//多条sql语句操作clob，blob会导致数据库记录行锁定
	String sql_1 = "update sqltest set created=#[created] where object_id=#[object_id]";
	try
	{
%>
<html>
	<head>
		<title>测试在batchutil标签上直接执行数据库预编译批处理操作</title>
	</head>
	<body>
		<table>
			<pg:batchutil dbname="bspf" type="prepared">
				<pg:statement sql="<%=sql %>" pretoken="#\\[" endtoken="\\]">
					<pg:batch>
						<pg:sqlparam name="object_id" value="<%=object_id %>" type="int" />
						<pg:sqlparam name="created" value="<%=created %>" type="timestamp" />
						<pg:sqlparam name="blobdata" value="<%=blobdata %>" type="blobfile" />
						<pg:sqlparam name="clobdata" value="<%=clobdata %>" type="clobfile" charset="GBK"/>
					</pg:batch>
				</pg:statement>
				<pg:statement sql="<%=sql_1 %>" pretoken="#\\[" endtoken="\\]">
					<pg:batch>
						<pg:sqlparam name="object_id" value="<%=object_id %>" type="int" />
						<pg:sqlparam name="created" value="<%=created1 %>"
							type="timestamp" />
						
					</pg:batch>
				</pg:statement>
				<pg:statement sql="<%=sql_1 %>" pretoken="#\\[" endtoken="\\]">

					<pg:sqlparam name="object_id" value="<%=object_id %>" type="int" />
					<pg:sqlparam name="created" value="<%=created2 %>" type="timestamp" />
					
				</pg:statement>
				<pg:statement sql="<%=sql_1 %>" pretoken="#\\[" endtoken="\\]">
					<pg:batch>
						<pg:sqlparam name="object_id" value="<%=object_id %>" type="int" />
						<pg:sqlparam name="created" value="<%=created3 %>"
							type="timestamp" />
						
					</pg:batch>
					<pg:batch>
						<pg:sqlparam name="object_id" value="<%=object_id %>" type="int" />
						<pg:sqlparam name="created" value="<%=created4 %>"
							type="timestamp" />
						
					</pg:batch>
				</pg:statement>
			</pg:batchutil>
		</table>
	</body>
</html>
<%}
							catch(Exception e){

								e.printStackTrace();
							}%>
