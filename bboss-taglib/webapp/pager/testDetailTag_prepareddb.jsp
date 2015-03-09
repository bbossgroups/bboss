<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.sql.*,java.util.List" errorPage=""%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<!-- 
	测试在beaninfo标签上直接执行预编译查询操作，获取详细信息实例
	statement:数据库查询语句
	dbname:查询的相应数据库名称，在poolman.xml文件中进行配置
-->

<%
	String table_name="td_sm_user";
	String statement="select * from tableinfo where table_name=$[table_name] " ;
	
%>
<html>
<head>
<title>测试在beaninfo标签上直接执行数据库，获取分页列表信息实例</title>
</head>
<body>

	<table>
		<pg:sqlparams sqlparamskey="key" pretoken="\\$\\[" endtoken="\\]">
				<pg:sqlparam name="table_name" value="<%=table_name %>" type="string"/>
					
		</pg:sqlparams> 
	   <pg:beaninfo statement="<%=statement %>" 
	    		  dbname="bspf"
	    		   sqlparamskey="key">
		
			<tr class="cms_data_tr" id="<pg:cell colName="table_name" defaultValue=""/>">
				<td>
					table_name:
				</td> 
				<td>
					<pg:cell colName="table_name" defaultValue=""/>
				</td> 
				<td>
					table_id_name：
				</td>
				<td>
					<pg:cell colName="table_id_name" defaultValue="" />
				</td>
				<td class="tablecells" align=center height='30' width="5%">
					table_id_value:
				</td>	
				<td class="tablecells" align=center height='30' width="5%">
					<pg:cell colName="table_id_value" defaultValue=""/>
				</td>	
			</tr>
		</pg:beaninfo>	
		
	</table>
</body>
</html>
