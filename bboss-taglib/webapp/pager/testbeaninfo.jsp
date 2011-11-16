<%@ page contentType="text/html; charset=GBK" language="java" import="java.sql.*,java.util.List" errorPage=""%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<%
	String contextpath = request.getContextPath();
	out.println("contextpath:"+contextpath);
%>
<!-- 
	测试在list标签上直接执行数据库，获取列表信息实例
	statement:数据库查询语句
	dbname:查询的相应数据库名称，在poolman.xml文件中进行配置
-->
<html>
<head>
<title>测试在list标签上直接执行数据库，获取列表信息实例</title>
</head>
<body>
	<table>
	    
		<pg:beaninfo statement="select content from td_cms_document where document_id=3110" 
	    		  dbname="bspf">
		
			<tr class="cms_data_tr">
				<td>
					<pg:cell colName="content" defaultValue="qqq"/>
				</td> 
				
			</tr>
		</pg:beaninfo>
		
		<!-- pg:rowcount/-->
	</table>
</body>
</html>
