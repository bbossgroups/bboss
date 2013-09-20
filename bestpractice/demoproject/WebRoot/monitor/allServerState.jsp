<%
/*
 * <p>Title: 监控服务器状态</p>
 * <p>Description: 服务器运行是否正常</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-9-8
 * @author gao.tang
 * @version 1.0
 */
 %>
<%@ page session="false" contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@page import="com.frameworkset.platform.remote.Utils"%>
<%@page import="org.frameworkset.spi.remote.RPCAddress"%>	
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<html>
  <head>
    <base href="<%=basePath%>">
<%@ include file="/include/css.jsp"%>
    <title>所有服务器状态信息</title>

	<script type="text/javascript" languge="Javascript">
	function flushButton(){
		document.location = document.location;
	}
	
	</script>

  </head>
  
  <body class="contentbodymargin" onload="" scroll="no">
	<div style="width:100%;height:100%;overflow:auto">
	<form  name="LogForm"  method="post">
	<table width="100%" height="" border="0" cellpadding="0" cellspacing="1" class="thin">
	<caption>监控所有服务器状态监控<div align="right" ><input type="button" value="刷新页面" class="input" onclick="flushButton()"></div></caption>
	<tr>
	<th>服务器名</th>
	<th>服务状态</th>
	</tr>
	<% 
		List<RPCAddress> servers = Utils.getAppservers();
		int serverC = 1;
		for(int i = 0; i < servers.size(); i++){
			RPCAddress ipAddress = servers.get(i);
			
			String ipPort = ipAddress.toString();
	%>
	<tr>
		<td><strong>server<%=serverC++%>-----<%=ipPort %></strong></td>
		<td><%=Utils.validateAddress(ipAddress)?"OK":"" %></td>
	</tr>
	<% 
		}
	%>
	</table>
	</form>
	</div>
	
	
  </body>
</html>
