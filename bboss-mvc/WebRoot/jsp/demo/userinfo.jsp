<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<html>
	<head>
		<title>bboss-mvc - 用户管理demo</title>
	</head>
	<link rel="shortcut icon"
			href="${pageContext.request.contextPath}/css/favicon.gif">
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
	
	<link rel="stylesheet"
		href="${pageContext.request.contextPath}/css/classic/main.css"
		type="text/css" />
	<link rel="stylesheet"
		href="${pageContext.request.contextPath}/css/classic/messages.css"
		type="text/css" />
	<script type="text/javascript">
	function addUser()
	{	
		document.getElementById("userOperationForm").action = "${pageContext.request.contextPath }/addUser.htm";
		document.getElementById("userOperationForm").submit();
	}
	
	function updateUser()
	{
		document.getElementById("userOperationForm").action = "${pageContext.request.contextPath }/updateUser.htm";
	    document.getElementById("userOperationForm").submit();
	}
	
	function deleteUser()
	{
		document.getElementById("userOperationForm").action = "${pageContext.request.contextPath }/deleteUser.htm";
	    document.getElementById("userOperationForm").submit();
	}
	
	function deleteAllUser()
	{
		document.getElementById("userOperationForm").action = "${pageContext.request.contextPath }/deleteAllUser.htm";
	    document.getElementById("userOperationForm").submit();
	}
	
	function queryUser()
	{
		document.getElementById("userOperationForm").action = "${pageContext.request.contextPath }/userinfo.htm";
	    document.getElementById("userOperationForm").submit();
	}
	</script>
	<body>
	<div>errors:<pg:errors >
			<p><pg:error/></p>
		</pg:errors >
	</div>
	<div>globalerrors:<pg:globalerrors >
			<p><pg:error/></p>
		</pg:globalerrors >
	</div>
		<pg:beaninfo requestKey="user">
			<form id="userOperationForm" action="${pageContext.request.contextPath }/updateUser.htm"
				method="post">
				<table class="genericTbl">
					<tr >
						<th style="width: 5%" colspan="100"  class="order1 sorted">
							用户id：<input type="text" name="userId"
								value="<pg:cell colName="userId" defaultValue=""/>" />
								<pg:error colName="userId"/>
						</th>
						<th style="width: 20%" class="order1 sorted">
							请输入用户名称：
							<input type="text" name="userName"
								value="<pg:cell colName="userName" defaultValue=""/>" />
								<pg:error colName="userName"/>
						</th>
						<th style="width: 20%" class="order1 sorted">
							请输入用户口令：
							<input type="text" name="userPassword"
								value="<pg:cell colName="userPassword" defaultValue=""/>" />
								<pg:error colName="userPassword" arguments="1,2,3"/>
						</th>
					</tr>
					</table>
					<div>
				<ul class="options">
					<li id="size">
						<a href="#" onClick="addUser()">增加用户信息</a>
					</li>
					<li id="abbreviations">
						<a href="#" onClick="deleteUser()">删除用户信息</a>
					</li>
					<li id="size">
						<a href="#" onClick="updateUser()">修改用户信息</a>
					</li>
					<li id="abbreviations">
						<a href="#" onClick="queryUser()">获取用户信息</a>
						
					</li>
					
					<li id="abbreviations">
						<a href="#" onClick="deleteAllUser()">删除所有用户信息</a>
						
					</li>
					
				</ul>
		  </div>
			</form>
		</pg:beaninfo>
		</body>
		