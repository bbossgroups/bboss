<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.frameworkset.common.poolman.monitor.PoolmanStatic"%>
<%@page import="com.frameworkset.common.poolman.monitor.PoolMonitorServiceImpl"%>
<%@page import="com.frameworkset.common.poolman.monitor.AbandonedTraceExt"%>
<%
/*
 * <p>Title: 监控连接池信息</p>
 * <p>Description: 连接池使用情况</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-9-8
 * @author gao.tang
 * @version 1.0
 */
 %>
<%@ page session="false" contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.common.poolman.DBUtil"%>

<%@page import="java.util.*"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>		
<%

		
	String dbname = request.getParameter("ds") ;
String sname = "";
	if(dbname != null && !dbname.equals(""))
	{
		sname = "数据库："+dbname+" 的链接情况";
		 
		 //Connection con = DBUtil.getConection();
		// Statement smt = con.createStatement();
		 //smt.executeQuery("select 1 from dual");
		// con.close();
		 //smt.close();
		 java.util.List<AbandonedTraceExt> traceobjects = DBUtil.getGoodTraceObjects(dbname);
		 request.setAttribute("traceobjects", traceobjects);  
	 	
	}
	 
	
	
%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><%=sname %></title>
<%@ include file="/common/jsp/css.jsp"%>
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/include/syntaxhighlighter/styles/SyntaxHighlighter.css"></link>
<script language="javascript" src="${pageContext.request.contextPath}/include/syntaxhighlighter/shCore.js"></script>
<script language="javascript" src="${pageContext.request.contextPath}/include/syntaxhighlighter/shBrushJava.js"></script>
<script language="javascript" src="${pageContext.request.contextPath}/include/syntaxhighlighter/shBrushXml.js"></script>
<script language="javascript" src="${pageContext.request.contextPath}/include/syntaxhighlighter/shBrushJScript.js"></script>
		<script type="text/javascript" language="Javascript">
		function flushBotton(){
			document.location = document.location;
		}
		
		
		</script>

		</head>

	<body >	
 
	<div class="title_box">
				<div class="rightbtn">
				<a href="javascript:void" class="bt_small"  onclick="flushBotton()" id="addButton"><span>刷新页面</span></a>
				</div>
				
				<strong>活动链接信息列表-共<pg:size requestKey="traceobjects"/>个活动链接</strong>
				 		
	</div>
	 
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
		<tr> <td width="40%"><strong>基本信息</strong></td><td><strong>堆栈信息</strong></td></tr>
			<pg:list requestKey="traceobjects">
			<tr>
			 
			<td>label:<pg:cell colName="label"/><br>
			    dburl:<pg:cell colName="dburl"/><br>			        
			    创建时间：<pg:cell colName="createTime" dateformat="yyyy-MM-dd HH:mm:ss"/><br>
			    最后使用时间：<pg:cell colName="lastUsed" dateformat="yyyy-MM-dd HH:mm:ss"/><br>
			 borrowedCount：<pg:cell colName="borrowedCount" /><br>
			 lastBorrowTime：<pg:cell colName="lastBorrowTime" dateformat="yyyy-MM-dd HH:mm:ss"/><br>
			 lastReturnTime：<pg:cell colName="lastReturnTime" dateformat="yyyy-MM-dd HH:mm:ss"/></td>				                 
			<td><pg:null colName="stackInfo" evalbody="true"><pg:yes></pg:yes><pg:no><pre name='code' class='java'> <pg:cell colName="stackInfo" htmlEncode='true'/> </pre></pg:no></pg:null></td>
			 </tr>
			</pg:list>
		</table>	
	
<script type="text/javascript">

 
		
	dp.SyntaxHighlighter.ClipboardSwf = '${pageContext.request.contextPath}/include/syntaxhighlighter/clipboard.swf';
	dp.SyntaxHighlighter.HighlightAll('code');
 

</script>

				</body>
</html>