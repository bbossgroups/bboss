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
	//String clobdata = "clobdata";
	//byte[] blobdata = "clobdata".getBytes();
	//<pg:sqlparam name="blobdata" value="<%=blobdata %>" type="blob" />
	//<pg:sqlparam name="clobdata" value="<%=clobdata %>" type="clob" />
	File blobdata = new java.io.File("D:/workspace/bbossgroup/bboss-taglib/lib/ecs-1.4.2.jar");
	File clobdata = new java.io.File("D:/workspace/bbossgroup/bboss-taglib/readme.txt");
	
	
	String sql = "update sqltest set created=#[created],clobdata=#[clobdata],blobdata=#[blobdata] where object_id=#[object_id]";
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
						<pg:sqlparam name="clobdata" value="<%=clobdata %>" type="clobfile" />
					</pg:batch>
				</pg:statement>
				<pg:statement sql="<%=sql %>" pretoken="#\\[" endtoken="\\]">
					<pg:batch>
						<pg:sqlparam name="object_id" value="<%=object_id %>" type="int" />
						<pg:sqlparam name="created" value="<%=created1 %>"
							type="timestamp" />
						<pg:sqlparam name="blobdata" value="<%=blobdata %>" type="blobfile" />
						<pg:sqlparam name="clobdata" value="<%=clobdata %>" type="clobfile" />
					</pg:batch>
				</pg:statement>
				<pg:statement sql="<%=sql %>" pretoken="#\\[" endtoken="\\]">

					<pg:sqlparam name="object_id" value="<%=object_id %>" type="int" />
					<pg:sqlparam name="created" value="<%=created2 %>" type="timestamp" />
					<pg:sqlparam name="blobdata" value="<%=blobdata %>" type="blobfile" />
						<pg:sqlparam name="clobdata" value="<%=clobdata %>" type="clobfile" />
				</pg:statement>
				<pg:statement sql="<%=sql %>" pretoken="#\\[" endtoken="\\]">
					<pg:batch>
						<pg:sqlparam name="object_id" value="<%=object_id %>" type="int" />
						<pg:sqlparam name="created" value="<%=created3 %>"
							type="timestamp" />
							<pg:sqlparam name="blobdata" value="<%=blobdata %>" type="blobfile" />
						<pg:sqlparam name="clobdata" value="<%=clobdata %>" type="clobfile" />
					</pg:batch>
					<pg:batch>
						<pg:sqlparam name="object_id" value="<%=object_id %>" type="int" />
						<pg:sqlparam name="created" value="<%=created4 %>"
							type="timestamp" />
							<pg:sqlparam name="blobdata" value="<%=blobdata %>" type="blobfile" />
						<pg:sqlparam name="clobdata" value="<%=clobdata %>" type="clobfile" />
					</pg:batch>
				</pg:statement>
			</pg:batchutil>
		</table>
	</body>
</html>
