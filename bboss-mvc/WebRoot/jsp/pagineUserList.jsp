<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<!-- 
	测试在通过数据加载器获取分页列表数据，并且提供查询功能
-->
<html>
<head>
<title>测试在通过数据加载器获取分页列表数据，并且提供查询功能</title>
</head>
<body>
	<table>
				<tr class="cms_report_tr">
						<!--设置分页表头-->
					<form action="" method="post">
						<td  style="width:20%">请输入表名：</td>
						<td  style="width:5%" colspan="100"><input type="text" name="TABLE_NAME" value="<%=request.getParameter("TABLE_NAME") %>"><input type="submit" name="查询" value="查询"></td>
					</form>
				</tr>
						
	    <pg:listdata dataInfo="org.frameworkset.spi.mvc.PagerUserList" keyName="TableInfoListData" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="15" scope="request" data="TableInfoListData" 
						  isList="false">
				
					<tr class="cms_report_tr">
						<!--设置分页表头-->

						<td width="2%" align=center style="width:5%">
						<input class="checkbox" 
							type="checkBox" hidefocus=true 
							name="checkBoxAll" 
							onClick="checkAll('checkBoxAll','ID')"> 
						</td>
						<td width="8%">
							TABLE_NAME					</td>
						<td width="8%">
							TABLE_ID_GENERATOR					</td>
							<td width="8%">
							TABLE_ID_TYPE					</td>
						
					</tr>
				

					
				<pg:list requestKey="userList">
				
					<tr class="cms_report_tr">
						

						<td width="2%" align=center style="width:5%">
							<input class="checkbox" hideFocus onClick="checkOne('checkBoxAll','ID')" 
							type="checkbox" name="ID" 
							value="<pg:cell colName="TABLE_NAME" defaultValue=""/>">
							<img border="0" src="${pageContext.request.contextPath}<pg:theme code="exclamation.gif"/>"
                                         alt="<pg:message code="probe.jsp.datasources.list.misconfigured.alt"/>"/>										
						</td>
						<td width="8%">
							<pg:cell colName="TABLE_NAME" defaultValue=""/>		
							<pg:message var="messagecode" code="probe.jsp.wrongparams"/>
							${messagecode}
										</td>
							
						
						<td width="8%">
							<pg:cell colName="TABLE_ID_GENERATOR" defaultValue=""/>		
							<pg:message var="messagecode" code="probe.jsp.wrongparams"/>
							${messagecode}
										</td>
						<td width="8%">
							<pg:cell colName="TABLE_ID_TYPE" defaultValue=""/>		
							<pg:message var="messagecode" code="probe.jsp.wrongparams"/>
							${messagecode}
										</td>
					</tr>
					</pg:list>
					
					
					<tr><pg:index/></tr>
				</pg:pager> 
	
				
	</table>
</body>
</html>
