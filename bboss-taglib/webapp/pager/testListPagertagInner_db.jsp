<%@ page contentType="text/html; charset=GBK" language="java" import="java.sql.*,java.util.*,com.chinacreator.config.model.Operation" errorPage=""%>
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
	<table>
	<%
		List testList = new ArrayList();
		for(int i = 0; i < 1 ; i ++)
		{
				Operation op = new Operation();
				op.setName("name");
				op.setId("id");
				testList.add(op);
		}
		request.setAttribute("testList",testList);
		
		String aa = "td_sm_user";
	%>
	    
		<pg:list id="testid" statement="select * from tableinfo order by table_id_value desc" 
	    		  dbname="bspf" isList="true" maxPageItems="5">
		
			<tr class="cms_data_tr" id="<pg:cell colName="table_name" defaultValue=""/>">
				<td> <pg:rowid increament="1"/>
					<pg:cell colName="table_name" defaultValue=""/>
					<pg:cell expression="{rowid} + 1"/>
				</td> 
				<td>
					<pg:cell colName="table_id_name" defaultValue="" />
				</td>
				<td class="tablecells" align=center height='30' width="5%">
					<pg:cell colName="table_id_value" defaultValue=""/>
				</td>
				<td class="tablecells" align=center height='30' width="5%">
					rowid:<pg:equal expression="{0.rowid}*2" expressionValue="{0.rowid}*1">ddddd</pg:equal>
					
					<pg:equal colName="table_id_name" value="<%=aa%>">ddddd</pg:equal>
					<pg:equal colName="table_id_name" expressionValue="{table_id_name}">ddddd</pg:equal>
					<pg:equal expression="{table_id_name}" value="sss">ddddd</pg:equal>
					
					
					<pg:contain expression="{table_id_name}" pattern="[1-2]+">ddddd</pg:contain>
					<pg:match expression="{table_id_name}" pattern="[1-2]+">ddddd</pg:match>
					
					<pg:contain colName="table_id_name" pattern="[1-2]+">ddddd</pg:contain>
					<pg:match colName="table_id_name" pattern="[1-2]+">ddddd</pg:match>
					
					<pg:in expression="{table_id_name}" scope="1,2,3,4,5">ddddd</pg:in>
					
					<pg:notin expression="{table_id_name}" scope="1,2,3,4,5">ddddd</pg:notin>
					
					<pg:in colName="table_id_name" scope="1,2,3,4,5">ddddd</pg:in>
					
					<pg:notin colName="table_id_name" scope="1,2,3,4,5">ddddd</pg:notin>
					
					<pg:in expression="{table_id_name}" scope="{table_id_name}">ddddd</pg:in>
					
					<pg:notin expression="{table_id_name}" scope="{table_id_name}">ddddd</pg:notin>
					
					
					
				</td>	
				<td><pg:list requestKey="testList">
					<pg:cell colName="id" defaultValue="-1"/>
					
					<pg:cell expression="{rowid}"/>
					;<pg:cell index="0" colName="table_name" />
					<pg:cell colName="name" defaultValue="aa"/>
				</pg:list>
				</td>
			</tr>
		</pg:list>
		<tr><td><pg:rowcount id="testid"/></td><td colspan="2"><pg:index id="testid"/></td></tr>		
		
	</table>
</body>
</html>
