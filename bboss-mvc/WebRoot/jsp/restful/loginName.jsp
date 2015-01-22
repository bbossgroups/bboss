<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<html>
	<head>
		<title>姓名查询</title>
		<pg:config enablecontextmenu="false"/>
		<script type="text/javascript">
			function doquery(){
			 
				if($("#loginName3").val() == null || $("#loginName3").val() == "")
				{
					alert("请输入要查询的姓名!")
					return false;
				}
			 	var resturl = "<%=request.getContextPath() %>/query/loginnamequery/" + encodeURIComponent($("#loginName3").val());
				$("#queryresult").load(resturl);
				return false;
			}
		</script>
	</head>

	<body>
		    <table> 
		        <tr> 
		            <td>查询登录名：<input type="text" name="loginName3" id="loginName3"/> 
		            </td> 
		            
		            <td><input type="button" value="查询" onclick="doquery()"/> 
		            </td> 
		        </tr> 
		        <tr> 
		            <td>查询结果：
		            </td> 
		            <td id="queryresult"></td> 
		        </tr> 
		    </table>
	</body>
</html>
