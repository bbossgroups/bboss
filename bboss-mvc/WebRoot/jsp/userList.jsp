<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
String user = request.getParameter("userName");
System.out.println("***********" + user);
System.out.println(org.frameworkset.web.servlet.support.WebApplicationContextUtils.getRequiredWebApplicationContext(pageContext));
System.out.println(org.frameworkset.web.servlet.support.WebApplicationContextUtils.getWebApplicationContext());
 %>
<!-- 
	测试在通过数据加载器获取分页列表数据，并且提供查询功能
-->
<html>
<head>
<title>测试在通过数据加载器获取分页列表数据，并且提供查询功能</title>
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/css/classic/tables.css"
			type="text/css">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/css/classic/main.css"
			type="text/css">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/css/classic/mainnav.css"
			type="text/css">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/css/classic/messages.css"
			type="text/css">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/css/classic/tooltip.css"
			type="text/css">
</head>
<body>
	<table  class="genericTbl">
				<tr class="cms_report_tr">
						<!--设置分页表头-->
					<form action="" method="post">
						<th  style="width:20%" class="order1 sorted">请输入表名：</th>
						<th  style="width:5%" colspan="100" class="order1 sorted"><input type="text" name="TABLE_NAME" value="<%=request.getParameter("TABLE_NAME") %>"><input type="submit" name="查询" value="查询"></th>
					</form>
				</tr>
						
	    
				
					<tr class="even">
						<!--设置分页表头-->

						<td width="2%" align=center style="width:5%">
						<input class="checkbox" 
							type="checkBox" hidefocus=true 
							name="checkBoxAll" 
							onClick="checkAll('checkBoxAll','ID')"> 
						</td>
						<td width="8%">
							id					</td>
						<td width="8%">
							name					</td>
							<td width="8%">
							password					</td>
						
					</tr>
				

					
				<pg:list requestKey="userList">
				
					<tr class="even">
						

						<td width="2%" align=center style="width:5%">
							<input class="checkbox" hideFocus onClick="checkOne('checkBoxAll','ID')" 
							type="checkbox" name="ID" 
							value="<pg:cell colName="id" defaultValue=""/>">
							<img border="0" src="${pageContext.request.contextPath}<pg:theme code="exclamation.gif"/>"
                                         alt="<pg:message code="probe.jsp.datasources.list.misconfigured.alt"/>"/>										
						</td>
						<td width="8%">
							<pg:cell colName="userName" defaultValue=""/>		
							<pg:message var="messagecode" code="probe.jsp.wrongparams"/>
							${messagecode}
										</td>
							
						
						<td width="8%">
							<pg:cell colName="userPassword" defaultValue=""/>		
							<pg:message var="messagecode" code="probe.jsp.wrongparams"/>
							${messagecode}
										</td>
						<td></td>
					</tr>
					</pg:list>
					
					
					<tr></tr>
				
	
				
	</table>
</body>
</html>
