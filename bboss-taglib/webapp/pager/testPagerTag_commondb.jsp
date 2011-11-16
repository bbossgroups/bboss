<%@ page contentType="text/html; charset=GBK" language="java" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<!-- 
	测试在pager标签上直接执行预编译数据库操作，获取列表/分页信息实例
	statement:数据库查询语句
	dbname:查询的相应数据库名称，在poolman.xml文件中进行配置
	
-->
<%
	
	String object_id = com.frameworkset.util.StringUtil.getParameter(request,"object_id","20");	
	
	String sql = "select * from sqltest  where object_id=" + object_id + " order by object_name";
 %>
<html>
<head>
<title>测试在pager标签上直接执行数据库操作，获取分页列表信息实例</title>
</head>
<body>
	<table>
			
		    <pg:pager statement="<%=sql %>" 
		    		  dbname="bspf" 
		    		  isList="false" 
		    		  pager_infoName="pager_info11" 
		    		  >		   	
		    		  <%
		    		  System.out.println("pager_info11.getDataSize():" + pager_info11.getDataSize());
		    		  %>
		    		  <tr><td colspan="3"><pg:index/></td></tr>
			<pg:list >
			<%
		    		  System.out.println("pager_info11.getDataSize()2:" + pager_info11.getDataSize());
		    		  %>
				<tr class="cms_data_tr" id="<pg:cell colName="object_id" defaultValue=""/>">
					<td>
						<pg:cell colName="owner" defaultValue=""/>
					</td> 
					<td>
						<pg:cell colName="object_id" defaultValue="" />
					</td>
					<td class="tablecells" align=center height='30' width="5%">
						<pg:cell colName="object_name" defaultValue=""/>
					</td>	
				</tr>
			</pg:list>
			<tr><td colspan="3"><br><br></td></tr>
			<%
		    		  System.out.println("pager_info11.getDataSize()1:" + pager_info11.getDataSize());
		    		  %>
			</pg:pager>
		
	</table>
</body>
</html>
