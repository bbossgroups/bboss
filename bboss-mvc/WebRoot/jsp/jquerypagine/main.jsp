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
	
		<pg:config enablecontextmenu="false"/>
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
				$("#pagecontainer").load("pagerqueryuser.htm?name=读破肚皮 #pagecontent",
				               { userName: [$("#userName").val(),$("#userName").val(),$("#userName").val()] } );
				
			}
			
			function dobeanparamsquery()
			{
				$("#pagecontainer1").load("pagerqueryuser1.htm #pagecontent",{ name:"多多",userName: [$("#beanuserName").val(),$("#beanuserName").val(),$("#beanuserName").val()] });
			}
			
			function domapparamsquery()
			{
				$("#pagecontainer2").load("pagerqueryuser2.htm #pagecontent",{ name:"多多",userName: [$("#mapuserName").val(),$("#mapuserName").val(),$("#mapuserName").val()] });
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
							<input type="button" value="数组条件查询" onclick="doquery()"/>
						</th>
						
					</tr>
			</table>
			</form>		
			<div id="pagecontainer">
				
			</div>
			<table class="genericTbl">
				
				<!--分页显示开始,分页标签初始化-->
				
					<tr >
						<th align="center">
							NAME:<input id="beanuserName" name="beanuserName" value=""/>
						</th>
						
						<th>
							<input type="button" value="bean参数查询" onclick="dobeanparamsquery()"/>
						</th>
						
					</tr>
			</table>
			<div id="pagecontainer1">
				
			</div>
			
			
			<table class="genericTbl">
				
				
				
					<tr >
						<th align="center">
							NAME:<input id="mapuserName" name="mapuserName" value=""/>
						</th>
						
						<th>
							<input type="button" value="map参数查询" onclick="domapparamsquery()"/>
						</th>
						
					</tr>
			</table>
			<div id="pagecontainer2">
				<script type="text/javascript">
				$(document).ready(function(){
					//opt:包含pageurl, containerid, selector三个属性
					bboss.pager.pagerevent = {
							beforeload:function(opt){
								if(opt.containerid == 'pagecontainer2' || opt.containerid == 'pagecontainer1')
									alert("beforeload containerid:"+opt.containerid);
								if(opt.containerid == 'pagecontainer2')
									{
										alert("测试前处理返回结果为false效果！阻止页面跳转！");
										return false;
									
									}
									
								
							},
							afterload:function(opt){
								if(opt.containerid == 'pagecontainer2' || opt.containerid == 'pagecontainer1')
									alert("afterload containerid:"+opt.containerid);
							}};
					   $("#pagecontainer").load("pagerqueryuser.htm #pagecontent");
					   $("#pagecontainer1").load("pagerqueryuser1.htm #pagecontent"); 
					  $("#pagecontainer2").load("pagerqueryuser2.htm #pagecontent");
					});
				</script>
			</div>
			
			
	</body>
</html>
