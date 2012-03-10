<%
/*
 * <p>Title: 监控数据库信息页面</p>
 * <p>Description: 服务器链接数据库的状态，和使用情况</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-9-8
 * @author gao.tang
 * @version 1.0
 */
 %>

<%@ page session="false" contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.common.poolman.DBUtil"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="java.util.*"%>
<%@page import="java.util.Enumeration,
				com.frameworkset.common.poolman.util.JDBCPoolMetaData"%>
<%@page import="com.frameworkset.platform.remote.Utils"%>
<%@page import="org.jgroups.blocks.GroupRequest"%>
<%@page import="java.util.Iterator"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>				
<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkAccess(request,response,false);
	String userAccount = accessControl.getUserAccount();
	//System.out.println("ip = " + accessControl.getlocalIpAdd());
	boolean isCluster = Utils.clusterstarted();
	

	
%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>poolman连接池使用情况与配置信息</title>
<%@ include file="/include/css.jsp"%>
		<tab:tabConfig/>	
		<script src="../inc/js/func.js"></script>
		</head>

	<body class="contentbodymargin" onload="" scroll="no">
	<div style="width:100%;height:100%;overflow:auto">
	<tab:tabContainer id="monitor" skin="amethyst">
	
	<tab:tabPane id="allServerMonitorInfo" tabTitle="所有服务器poolman信息" lazeload="true" >
		<tab:iframe id="allServer-monitorinfo" src="allServerMonitorInfo.jsp" frameborder="0" scrolling="no" width="100%" height="580">
		</tab:iframe>
	</tab:tabPane>
	
	<%if(isCluster){ %>
	<tab:tabPane id="allMonitor" tabTitle="所有服务器poolman实时信息" lazeload="true" >
		<tab:iframe id="all-monitor" src="clusterMonitor.jsp" frameborder="0" scrolling="no" width="100%" height="580">
		</tab:iframe>
	</tab:tabPane>
	
	<tab:tabPane id="allServerState" tabTitle="所有服务器状态" lazeload="true" >
		<tab:iframe id="allServer-State" src="allServerState.jsp" frameborder="0" scrolling="no" width="100%" height="580">
		</tab:iframe>
	</tab:tabPane>
	
	<% 
		}
	%>
	
	
	
	</tab:tabContainer>
	</div>
				</body>
</html>