<%@ page contentType="text/html; charset=GBK" language="java" import="java.sql.*,java.util.List" errorPage=""%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
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
	    
		<pg:list statement="select * from tableinfo order by table_id_value desc" 
	    		  dbname="bspf">
		
			<tr class="cms_data_tr" id="<pg:cell colName="table_name" defaultValue=""/>">
				<td>
					<pg:cell colName="table_name" defaultValue=""/>
				</td> 
				<td>
					<pg:cell colName="table_id_name" defaultValue="" />
				</td>
				<td class="tablecells" align=center height='30' width="5%">
					<pg:cell colName="table_id_value" defaultValue=""/>
				</td>	
			</tr>
		</pg:list>
		
		<!-- pg:rowcount/-->
	</table>
</body>
</html>
