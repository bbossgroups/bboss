<%@ page contentType="text/html; charset=GBK" language="java" import="java.sql.*,java.util.List" errorPage=""%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<!-- 
	测试在list标签上直接执行数据库，获取列表信息实例
	statement:数据库查询语句
	dbname:查询的相应数据库名称，在poolman.xml文件中进行配置
-->
<html>
<head>
<title>测试在listpager标签上直接执行数据库，获取分页列表信息实例</title>
</head>
<body>
<form name="com.frameworkset.goform" id="com.frameworkset.goform" method="post"></form>
	<table>
	    
		<pg:list autosort="true" id="testid" statement="select * from tableinfo order by table_id_value desc" 
	    		  dbname="bspf" isList="false" maxPageItems="5" form="com.frameworkset.goform">
			<pg:header>
				<pg:title type="td" width="15%" className="headercolor" title="表名" sort="true" colName="table_name"/>
				<pg:title type="td" width="15%" className="headercolor"  sort="true" colName="table_id_name" title="表id名"/>
				<pg:title type="td" width="15%" className="headercolor"  sort="true" colName="table_id_value" title="表id值"/>				
			</pg:header>
			<pg:param name="table_name"/>
			<tr class="cms_data_tr" id="<pg:cell colName="table_name" defaultValue=""/>">
				<td> <pg:rowid offset="false" increament="1"/>
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
		<tr><td><pg:rowcount id="testid"/></td><td colspan="2">
		<pg:index custom="true" id="testid"/>
		</td></tr>		
		
	</table>
</body>
</html>
