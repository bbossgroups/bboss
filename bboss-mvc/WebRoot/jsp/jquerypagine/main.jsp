<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/commontag.tld" prefix="common"%>
<%@ page import="org.frameworkset.web.servlet.support.RequestContext" %>
<%
String path = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		

		<title>用JQuery实现分页列表查询</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
	
		<pg:config/>
		<script type="text/javascript">
			
		
			function deleteUsers(){
			  $("#form1").action="<%=path%>/jquerypagine/deleteusers.htm";
			  $("#form1").submit();
			 //  var value = $("#form1:checkbox").fieldValue();
			 //  alert(value.length);
			//	$("#pagecontainer").load("deleteusers.htm #pagecontent",{id:$("checkbox").val()});
			}
			
			
			function doquery()
			{
				//$("#pagecontainer").load("pagerqueryuser.htm #pagecontent",{userName:encodeURIComponent($("#userName").val())});
				$("#pagecontainer").load("pagerqueryuser.htm?name=读破肚皮 #pagecontent",{userName:$("#userName").val()});
				
			}
		</script>
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
	       <form action="" id="form1" method="post">
			<table class="genericTbl">
				
				<!--分页显示开始,分页标签初始化-->
				
					<tr >
						<th align="center">
							NAME:<input id="userName" name="userName" value=""/>
						</th>
						 
						<th>
							<input type="button" value="查询" onclick="doquery()"/>
						</th>
						<!-- 
						<th>
						
						  <ul class="options">
					  		   <li id="size">
									<a href="#">增加用户</a>
							  </li>
					      </ul>
						</th>
						
						<th>	  
						    <ul class="options">
							   <li id="abbreviations">
					  				<a href="javascript:deleteUsers()">批量删除</a>
					          </li>
					      </ul>
						</th>
						 -->
					</tr>
			</table>		
			<div id="pagecontainer">
				<script type="text/javascript">
				$(document).ready(function(){
					  $("#pagecontainer").load("pagerqueryuser.htm #pagecontent");
					});
				</script>
			</div>
			
			<div id="pagecontainer1">
				<script type="text/javascript">
				$(document).ready(function(){
					  $("#pagecontainer1").load("pagerqueryuser1.htm #pagecontent");
					});
				</script>
			</div>
			</form>
	</body>
</html>
