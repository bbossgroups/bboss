<%--
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://probe.jstripe.com/d/license.shtml
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 --%>

<%@ page contentType="text/html;charset=UTF-8" language="java"
	session="false"%>
<%@ page import="java.io.ByteArrayOutputStream"%>
<%@ page import="java.io.PrintStream"%>
<%@ page import="java.io.File"%>
<%@ page import="java.io.File,org.frameworkset.util.CodeUtils,
com.frameworkset.util.FileUtil,com.frameworkset.util.StringUtil"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/commontag.tld" prefix="common"%>

<%
String path = request.getContextPath();
org.frameworkset.spi.remote.rmi.RMIServiceClientTest test = new org.frameworkset.spi.remote.rmi.RMIServiceClientTest();
test.init();test.test(); 
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/index.htm";
%>

<html>

	<head>
		<title>bboss-mvc - demo index</title>
		<base id="basehrefid" >
   <pg:config enablecontextmenu="false"/>
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
		<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/include/syntaxhighlighter/styles/SyntaxHighlighter.css"></link>
	<script language="javascript" src="${pageContext.request.contextPath}/include/syntaxhighlighter/shCore.js"></script>
	<script language="javascript" src="${pageContext.request.contextPath}/include/syntaxhighlighter/shBrushJava.js"></script>
	<script language="javascript" src="${pageContext.request.contextPath}/include/syntaxhighlighter/shBrushXml.js"></script>
	<script language="javascript" src="${pageContext.request.contextPath}/include/syntaxhighlighter/shBrushJScript.js"></script>
  <script type="text/javascript">
	  $(document).ready(function() {
		if($.browser.msie) {
		
			 $("#basehrefid").attr("href","<%=basePath%>");
			
		}
		else if($.browser.safari)
		{
			 $("#basehrefid").attr("href","<%=basePath%>");
		}
		else if($.browser.mozilla)
		{
			 $("#basehrefid").attr("href","#");
		}
		else if($.browser.opera)
		{
			 $("#basehrefid").attr("href","<%=basePath%>");
		} 
		else
		{
			 $("#basehrefid").attr("href","<%=basePath%>");
		}
	 });
 </script>
	</head>
	<body>
	<div id="caption">
	<a name="top"></a>
    <ul id="top">
        <li id="logo"><a href="index.htm"><img alt="LambdaProbe Logo" src="${pageContext.request.contextPath}/css/the-probe-logo.gif"></a></li>
        <li id="runtime">
            Bboss MVC Version Login.jsp
            <span class="uptime"></span>
        </li><li id="title">/aop - mvc - persistent - taglib</li>
    </ul>
</div>

		<div id="mainBody">
			
    	<div class="embeddedBlockContainer">
			<h1>
				会话超时，请重新登录系统。
			</h1>
			<div class="shadow">
				<div class="info">
					<p>
						<ul>		
						<li><a href="#3719">友情链接</a></li>				
						<li><a href="#3720">Demo列表中包含每个demo的基本信息，包括名称，访问地址清单，功能说明，查看demo相关的所有代码和配置文件明细</a></li>
						<li>点击demo的访问地址清单中的地址可以访问demo的实际功能并进行相应的操作</li>
						<li>点击代码明细，可以进入查看demo的实现代码，包括控制器类，业务组件，java对象，mvc配置文件，jsp页面</li>
						<li><a href="#3721">bboss-mvc框架demo的部署部分介绍了如何下载和部署demo应用</a></li>
						<li><a href="#3722">资源和文档下载</a></li>
						<li><a href="#3723">bboss-mvc框架体系结构图</a></li>
						<li><a href="#3724">bboss-mvc框架请求处理流程图</a></li>
						<li><a href="#3725">web.xml部署文件部分说明了mvc框架的涉及web.xml主要配置内容</a></li>						
						
						<li><a href="#3726">框架更新记录</a></li>
						</ul>
					</p>
				</div>
			</div>
			
			<div class="blockContainer">
			<h1>友情链接<a href="#top" name="3719">
			<img border="0" src="<%=request.getContextPath() %>/jsp/top.gif" alt="Top">
			</a></h1>
			<p>
						<ul>						
						<li><a href="http://www.iteye.com/wiki/bbossgroups/3092-mvc-bboss-config" target="_blank">bbossgroups 开发系列文章之一 最佳实践</a> </li>
						<li><a href="http://www.iteye.com/wiki/bbossgroups/3090-mvc-aop-bboss" target="_blank">搭建bboss mvc eclipse开发工程，编写第一个实例</a></li>
						<li><a href="http://www.iteye.com/wiki/bbossgroups/3094-persistent" target="_blank">bbossgroups SQLExecutor组件api使用实例</a></li>
						<li><a href="http://www.iteye.com/wiki/bbossgroups/3091-webservice-bboss-aop" target="_blank">bbossgroups webservice引擎使用方法</a></li>
						<li><a href="http://www.iteye.com/wiki/bbossgroups/3089-bbossgroups-aop-rmi" target="_blank">bbossgroups rmi组件服务发布和rmi客服端获取方法</a></li>
						<li><a href="http://yin-bp.iteye.com/blog/1026245" target="_blank">bbossgroups mvc demo部署方法</a></li>
						<li><a href="http://yin-bp.iteye.com/" target="_blank">bbossgroups官方博客</a></li>
						
						</ul>
					</p>
			
			</div>
		
        
			

			<h1>回到顶部<a href="#top" name="3727">
			<img border="0" src="<%=request.getContextPath() %>/jsp/top.gif" alt="Top">
			</a></h1>
	</body>

</html>