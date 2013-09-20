<%
/**
 * 
 * <p>Title: SPI管理配置文件明细信息显示页面</p>
 *
 * <p>Description: SPI管理配置文件明细信息显示页面</p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: chinacreator</p>
 * @Date 2008-9-19
 * @author gao.tang
 * @version 1.0
 */
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8" session="false"%>
<%@page import="org.frameworkset.spi.BaseApplicationContext,org.frameworkset.spi.assemble.Pro"%>
<%@page import="org.frameworkset.spi.assemble.LinkConfigFile"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>	

<% 
	String rootpath = request.getContextPath();
	String selected = request.getParameter("selected");
	String classType = request.getParameter("classType");
	String nodePath = request.getParameter("nodePath");
	BaseApplicationContext contextbase = BaseApplicationContext.getBaseApplicationContext(nodePath);
	LinkConfigFile lnk = contextbase.getLinkConfigFile(selected);
	List list = null;
	Map map = null;
	Map pros = null;
	
	if(lnk != null){
	
		//子文件
		list = lnk.getLinkConfigFiles();
		//管理服务
		map = lnk.getMgrServices();
		pros = lnk.getProperties();
		
	}
	//System.out.println(lnk.getLinkConfigFiles().size());
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/treeview.css">
<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/tab.winclassic.css">
		<tab:tabConfig/>	
	</head>
	
	<body class="contentbodymargin" scroll="auto">
	<table height="10%"  width="100%" border="0" cellpadding="0" cellspacing="0" class="thin">
	<tr><td>
		<%if(selected != null){out.println("配置文件：" + selected); }
		  else
		  {
		  out.println("请选择spi管理节点！");
		  return;
		  }%>
	</td></tr>
	<tr><td>
		<%if(selected != null){out.println("容器标识：" + nodePath); }
		 %>
	</td></tr>
	
	<tr><td>
		<%if(selected != null){out.println("容器类型：" + contextbase.getClass().getCanonicalName()); }
		 %>
	</td></tr>
	</table >
	
	<tab:tabContainer id="compsitents" skin="amethyst">
	
	
	<tab:tabPane id="bussinessbeans" tabTitle="业务组件" >		
	<table class="thin" width="100%">
	<tr>
	<td class="headercolor">业务组件ID</td>
	<td class="headercolor">业务组件明细</td>  
	<td class="headercolor">业务组件类型</td>
	<td class="headercolor">业务组件描述</td>
	</tr>
	<%if(pros != null && !pros.isEmpty()){ 
		Iterator iterator = pros.keySet().iterator();
		while(iterator.hasNext()){
			String key = (String)iterator.next();
			Pro pro = (Pro)pros.get(key);
	%>
	<tr>
	<td><%=key %></td>	
	<%
		if(pro.isBean())
		{
			%>
	<td><a href="spi/beanDetail.jsp?selected=<%=key %>&nodePath=<%=nodePath %>" target="_blank" name="serviceDetail"  ><%
		out.print("name=" + pro.getName() + "<br>");		
		out.print("class=" + pro.getClazz() + "<br>");	
		out.print("singlable=" + pro.isSinglable() + "<br>");	
	
		
	 %></a></td>
	<%
		}
		else if(pro.isRefereced())
		{
		%>
	<td>
	<%
	if(pro.isAttributeRef()) { 
		String refid_ = pro.getRefid();
	%>
	<a href="spi/proDetail.jsp?selected=<%=refid_ %>&nodePath=<%=nodePath %>" target="_blank" name="serviceDetail"  ><%
		out.print("name=" + pro.getName() + "<br>");
				
		out.print("refid=" + pro.getRefid() + "<br>");
		out.print("引用类型：" );out.print("组件或者属性引用");
		
	 %></a>
	 <%} else if(pro.isServiceRef()) {
	 	String refserviceid = pro.getRefid();
	  %>
	<a href="managerserviceDetail.jsp?selected=<%=refserviceid %>" target="_blank" name="serviceDetail"  ><%
		out.print("name=" + pro.getName() + "<br>");
		
		out.print("refid=" + pro.getRefid() + "<br>");
		out.print("引用类型：" );out.print("管理服务引用");
		
	 %></a>
	 <%}%>
	 </td>
	<%}
	else
		{
			%>
	<td><a href="spi/proDetail.jsp?selected=<%=key %>&nodePath=<%=nodePath %>" target="_blank" name="serviceDetail"  ><%
		if(pro.getName() != null)
			out.print("name=" + pro.getName() + "<br>");
		if(pro.getValue() != null)
		{
			if(pro.isList())
				out.print("value=[List set]");
			else if(pro.isMap())
				out.print("value=[Map set]");
			else if(pro.isSet())
				out.print("value=[Set set]");
			else if(pro.isArray())
				out.print("value=[Array set]");
			else 
				out.print("value=" + pro.getValue() );		
		}
		
		if(pro.getClazz() != null)
			out.print("class=" + pro.getClazz() + "<br>");
			
		if(pro.getLabel() != null)
			out.print("label=" + pro.getLabel() + "<br>");
		if(pro.getEditorString() != null)
			out.print("属性编辑器：" + pro.getEditorString() + "<br>");	
		
		
		
		
		
	 %></a></td>
	<%
		}
	 %>
	
	<td ><%
		if(pro.isBean())
		{
			out.print("组件");
		}
		else if(pro.isRefereced())
		{
			out.print("引用");
		}
		else
		{
			out.print("全局属性");
		}
	 %></td>
	 <td ><%
	 if(pro.getDescription() != null)
	 	out.print(pro.getDescription());
	  %></td>
	</tr>
	<% 
	   }
	   out.print("<tr><td colspan='4'>总共配置了" + pros.size() + "个业务组件！</td></tr>");	
	  }else{ 
	%>
	<tr><td colspan="2">没有配置业务组件！</td></tr>
	<%} %>
	</table>		
	</tab:tabPane>
	
	<tab:tabPane id="subfiles" tabTitle="子模块文件" lazeload="true" >
	<table class="thin" width="100%">
	<tr height=100% width="100%"><td height=100% width="100%">
	
	<fieldset height=100% width="100%">
	<LEGEND align=left><strong>&nbsp;管理服务子模块文件&nbsp;</strong></LEGEND>
	<table height=100% width="100%" border="0" cellpadding="0" cellspacing="0" class="table">
	
	<tr ><td class="headercolor">子模块文件地址</td><td class="headercolor">明细查看</td></tr>
	<%if(list != null && list.size() > 0){
		for(int i = 0; i < list.size(); i++){
			LinkConfigFile linkConfigFile = (LinkConfigFile)list.get(i);
			String path = linkConfigFile.getConfigFile();
	%>
	<tr>
		<td><%=path %></td>
		<td><a href="configfileDetail.jsp?selected=<%=path %>&nodePath=<%=nodePath %>" target="_blank" name="fileDetail" >明细查看</a></td>
	</tr>
	<%
		}
		out.print("<tr><td colspan='2'>总共配置了" + list.size() + "个管理服务子模块文件！</td></tr>");
	  }else{ 
	%>
	<tr height=100% width="100%"><td height=100% width="100%" colspan="2">没有配置管理服务子模块文件！</td></tr>
	<%} %>
	</table>
	
	</fieldset>
	</td></tr>
	</table>
	</tab:tabPane>
	<tab:tabPane id="managers" tabTitle="管理服务">
	<table class="thin" width="100%">
	<tr height=100% width="100%"><td height=100% width="100%">
	<fieldset height=100% width="100%">
	<LEGEND align=left><strong>&nbsp;管理服务&nbsp;</strong></LEGEND>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table">
	<tr>
	<td class="headercolor">管理服务ID</td>
	<td class="headercolor">管理服务明细</td>
	</tr>
	<%if(map != null && !map.isEmpty()){ 
		Iterator iterator = map.keySet().iterator();
		while(iterator.hasNext()){
			String key = (String)iterator.next();
	%>
	<tr>
	<td><%=key %></td>
	<td><a href="managerserviceDetail.jsp?selected=<%=key %>" target="_blank" name="serviceDetail"  >明细查看</a></td>
	</tr>
	<% 
	   }
	   out.print("<tr><td colspan='2'>总共配置了" + map.size() + "个管理服务！</td></tr>");	
	  }else{ 
	%>
	<tr><td colspan="2">没有配置管理服务！</td></tr>
	<%} %>
	</table>
	</fieldset>
	</td></tr>
	</table>
	
	</tab:tabPane>
	
	
	
	
	</tab:tabContainer>
	
	</body>
</html>