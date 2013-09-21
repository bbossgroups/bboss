<%
/*
 * <p>Title: 监控数据库的使用链接情况</p>
 * <p>Description: 数据库链接的具体情况</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-9-8
 * @author gao.tang
 * @version 1.0
 */
 %>
<%@ page session="false" contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="com.frameworkset.common.poolman.DBUtil"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="java.util.*"%>
<%@page import="java.util.Enumeration,
				com.frameworkset.common.poolman.util.JDBCPoolMetaData"%>
<%@page import="com.frameworkset.platform.remote.Utils"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.frameworkset.spi.remote.RPCAddress"%>
<%@page import="org.frameworkset.spi.remote.RPCResponse"%>	

<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>				
<%
	//System.out.println("ip = " + accessControl.getlocalIpAdd());
	
	boolean isCluster = Utils.clusterstarted();
	//System.out.println("是否启用了集群 ： " + isCluster);
	%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>poolman连接池使用情况与配置信息</title>
<%@ include file="/include/css.jsp"%>
		<tab:tabConfig/>	
		<script src="../../inc/js/func.js"></script>
		<script type="text/javascript" language="Javascript">
		function flushBotton(){
			document.location = document.location;
		}
		
		</script>
		</head>

	<body class="contentbodymargin" onload="" scroll="no">
	<div style="width:100%;height:100%;overflow:auto">
	<form  name="LogForm"  method="post">
	<table width="100%" height="" border="0" cellpadding="0" cellspacing="1" class="thin">
	<caption>集群所有服务器数据库链接使用情况表<div align="right"><input type="button" class="input" value="刷新页面" onclick="flushBotton()"></div></caption>
	
	<%
	if(Utils.clusterstarted()){
		
		//Iterator iterator = Utils.getAppservers().iterator();
		
		
		//String[] ipPort = new String[vector.size()];
	 	//获取集群中每台服务器的链接池的链接使用情况
	    //Map<ip:port,Map<dbname,Object[idleconnections,usedconnections,maxusedconnections]>>
		Map map = Utils.getDataSourceStatus();
		//服务器ip于端口
		List<RPCAddress> appServers = Utils.getAppservers();
		Set servers = map.entrySet();
		//System.out.println("servers.size() = " + servers.size());
		//Iterator it = servers.iterator();
		int serversCount = 1;
		for(int appC = 0; appC < appServers.size(); appC++){
			RPCAddress ipAddress = appServers.get(appC);
			String ipPort = ipAddress.toString();
			Map objMap = (Map)(((RPCResponse)map.get(ipAddress)).getValue());
		//}
		//while(it.hasNext())
		//{
		//	Map.Entry entry = (Map.Entry)it.next();
		//	String ipPort = entry.getKey().toString();
		//	Rsp rsp = (Rsp)entry.getValue();
		
		//	Map objMap = (Map)rsp.getValue();
			if(objMap == null){
				out.println("该服务器退出~~~·");
				break;
			}
			Set poolmans = objMap.entrySet();
			Iterator itObjMap = poolmans.iterator();
			%>
			
			<tr>
			<td colspan="6" height="25" class="detailtitle"><strong>server<%=serversCount++ %>-----<%=ipPort %></strong></td>
			</tr>
			<tr class="tr">
		
				<td align="center" height="25" class="detailtitle">数据库名称</td>
				<td align="center" height="25" class="detailtitle">空闲连接</td>
				<td align="center" height="25" class="detailtitle">正在使用连接</td>
				<td align="center" height="25" class="detailtitle">使用连接高峰值</td>
				<td align="center" height="25" class="detailtitle">允许的最大链接数</td>
				<td align="center" height="25" class="detailtitle">最小链接数</td>
			</tr>
			<%
			while(itObjMap.hasNext()){
				Map.Entry entryPool = (Map.Entry)itObjMap.next();
				String poolName = entryPool.getKey().toString();
				Object[] object = (Object[])entryPool.getValue();
			//}	
			//for(int j = 0; j < poolnames.length; j++){
			//	Object[] object = (Object[])objMap.get(poolnames[j]);
				%>
		
		<tr class="tr">
		<td height="25" class="detailtitle"><%=poolName %></td>
		<td height="25" class="detailtitle"><%=object[0] %></td>
		<td height="25" class="detailtitle"><%=object[1] %></td>
		<td height="25" class="detailtitle"><%=object[2] %></td>
		<td height="25" class="detailtitle"><%=object[3] %></td>
		<td height="25" class="detailtitle"><%=object[4] %></td>
		</tr>
		
	
				<%
			}
			%>
			<tr class="tr">
			<th colspan="6"></th>
			</tr>
			<%
		}
	}
%>
</table>
	</form>
</div>
				</body>
</html>