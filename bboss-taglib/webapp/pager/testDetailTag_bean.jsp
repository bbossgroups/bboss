<%@ page contentType="text/html; charset=GBK" language="java" import="java.sql.*,java.util.List,test.*" errorPage=""%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
TestBean b = new TestBean();
b.setId("id");
b.setName("name");
request.setAttribute("b",b);

%>
<html>
<head>
<title>测试在detail标签上直接执行数据库，获取列表信息实例</title>
</head>
<body>
	<table>
	    
		<pg:beaninfo requestKey="b">
		
			<tr class="cms_data_tr" id="<pg:cell colName="id" defaultValue=""/>">
				<td>
					<pg:cell colName="name" defaultValue=""/>
				</td> 
				<td>
					<pg:cell colName="id" defaultValue="" />
				</td>
				
			</tr>
		</pg:beaninfo>
		
		<!-- pg:rowcount/-->
	</table>
</body>
</html>
