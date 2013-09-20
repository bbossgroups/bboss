<%
/*
 * <p>Title: 监控系统环境配置信息 总信息页面</p>
 * <p>Description: 监控bs数据库配置信息与在线用户信息</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-9-8
 * @author gao.tang
 * @version 1.0
 */
 %>
<%@page contentType="text/html;charset=UTF-8" session="false"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>监控</title>
<%@ include file="/include/css.jsp"%>
		<script src="../inc/js/func.js"></script>
		<script type="text/javascript">
		function openUrl(url){
			window.open(url);
			//window.open(url,"dialogWidth:"+screen.availWidth+";dialogHeight:"+screen.availHeight+";help:no;scroll:auto;status:no;maximize=yes;minimize=0");
		}
		</script>
	</head>

	<body class="contentbodymargin" onload="" scroll="no">
	<div style="width:100%;height:100%;overflow:auto">
	<table align="center">
	<tr>
	<td class="thin">监控系统环境配置信息</td>
	</tr>
	</table>
	<table width="80%" height="" border="0" cellpadding="0" cellspacing="1" class="thin">
	
	<!-- <caption>监控系统环境配置信息</caption><tr>
	<td align="center "class="thin">监控系统环境配置信息</td>
	</tr> -->
	
	<tr>
	<td width="20%">webservice服务监控</td><td><a href="../cxfservices" target="_blank">webservice服务监控</a></td>
	
	</tr>
	
	<tr>
	<td width="20%">数据库监控</td><td><a href="dbmonitor_.jsp" target="_blank">数据库监控页面</a></td>
	
	</tr>
	
	<tr>
	<td>SPI监控树</td><td><a href="spiFrame.jsp" target="_blank"  >SPI管理</a></td>
	</tr>
	<tr>
	<td>虚拟机内存使用情况</td><td><table width="100%">
	<tr><td>FreeMemory：<%= (Runtime.getRuntime().freeMemory() / 1024/1024 + "M")%></td></tr>
	<tr><td>TotalMemory：<%= ( Runtime.getRuntime().totalMemory() / 1024/1024 + "M")%></td></tr>
	<tr><td>MaxMemory：<%= (Runtime.getRuntime().maxMemory() / 1024/1024 + "M")%></td></tr></table></td>
	</tr>
	
	</table>
		</tab:tabContainer>
	</div>
				</body>
</html>