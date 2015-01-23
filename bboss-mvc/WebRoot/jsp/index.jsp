<%@ page contentType="text/html;charset=UTF-8" language="java"
	session="false"%><%@ page import="java.io.ByteArrayOutputStream"%><%@ page import="java.io.PrintStream"%><%@ page import="java.io.File"%><%@ page import="java.io.File,org.frameworkset.util.CodeUtils,com.frameworkset.util.FileUtil,org.frameworkset.web.demo.DemoUtil,com.frameworkset.util.StringUtil"%><%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%><%@ taglib uri="/WEB-INF/commontag.tld" prefix="common"%><%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/index.htm";
%><html>
	<head>
		<title>欢迎您访问Bboss官方网站 - 打造中国自主开发框架品牌</title>
		  <meta name="description" content="Bboss mvc DEMO列表 资源和文档下载 bboss-mvc框架体系结构图 bboss-mvc框架请求处理流程图 web.xml部署文件部分说明了mvc框架的涉及web.xml主要配置内容 框架更新记录" />
    <meta name="keywords" content="bboss,mvc,ioc,persistent,taglib,bbossgroups,hessian,cxf,webservice,quartz,activiti,Bboss mvc DEMO列表,事务,ibatis,mybatis,hibernate,资源和文档下载 体系结构图, bboss-mvc请求处理流程图,web.xml部署文件,框架更新记录" />
   <script type='text/javascript' src='${pageContext.request.contextPath}/include/jquery-1.4.2.min.js' language='JavaScript'></script>
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
		
	</head>
	<body>
	<div><a name="top"></a><a href="http://yin-bp.iteye.com" target="_blank">bboss博客</a>&nbsp;&nbsp;&nbsp;<a href="https://github.com/bbossgroups/bbossgroups-3.5" target="_blank">Bboss GitHub</a>&nbsp;&nbsp;&nbsp;<a href="https://github.com/bbossgroups/bbossgroups-3.5/archive/master.zip" target="_blank">下载</a>&nbsp;&nbsp;&nbsp;<a href="#3719">友情链接</a>&nbsp;&nbsp;&nbsp;<a href="#1002">联系我们</a>	&nbsp;&nbsp;&nbsp;<a href="http://www.kaifazhe.me/" target="_blank">开发者.我@一个开发者的网站</a>	</div>
	<div id="caption">
	
    <ul id="top">
        <li id="logo"><a href="index.htm"><!--<img alt="bboss Logo" src="${pageContext.request.contextPath}/jsp/getqrcode.jpg" height="50">  --><img alt="bboss Logo" height="80" width="80" src="${pageContext.request.contextPath}/jsp/getqrcode.jpg"></a></li>
        
        <li id="runtime">
            <span class="uptime"><P>最新资讯：
            	<a href="http://yin-bp.iteye.com/blog/1877259" target="_blank">扩展Activiti-5.12轻松实现流程节点间自由跳转和任意驳回/撤回</a>&nbsp;
            	<a href="http://yin-bp.iteye.com/blog/1869404" target="_blank">bboss离线开发文档下载</a>
            	<a href="http://yin-bp.iteye.com/blog/1814978" target="_blank">bboss 发布和使用hessian服务方法介绍 </a> 
 							</P></span>
        </li>
    </ul>
</div>

		<div id="mainBody">
			
    	<div class="embeddedBlockContainer">
			<h1>
				bboss官方站点使用指南：
			</h1>
			<div class="shadow">
				<div class="info">
					<p>
						<ul>		
						<li><a href="#1000">bboss特色</a></li>	
						<li><a href="#998">bboss下载及更新记录</a></li>
						<li><a href="#999">bboss源码ant构建方法</a></li>	
						<li><a href="#3723">bboss-mvc体系结构图</a></li>
						<li><a href="#3724">bboss-mvc请求处理流程图</a></li>
							
						<li><a href="#3720">Demo列表中包含每个demo的基本信息，包括名称，访问地址清单，功能说明，查看demo相关的所有代码和配置文件明细</a>
							<ul><li>点击demo的访问地址清单中的地址可以访问demo的实际功能并进行相应的操作</li>
							<li>点击代码明细，可以进入查看demo的实现代码，包括控制器类，业务组件，java对象，mvc配置文件，jsp页面</li></ul>
						</li>
						<li><a href="#3721">bboss-mvc框架demo的部署部分介绍了如何下载和部署demo应用</a></li>
						<li><a href="#3722">资源和文档下载</a></li>
						<li><a href="#4722">视频教程下载</a></li>
						<li><a href="#8722">工具下载</a></li>
						<li><a href="#3725">web.xml部署文件部分说明了mvc框架的涉及web.xml主要配置内容</a></li>		
						
						<li><a href="#3719">友情链接</a></li>
						<li><a href="#1002">联系我们</a></li>		
						</ul>
					</p>
				</div>
			</div>
			<h1>
				bboss特色<a href="#top" name="1000">
			<img border="0" src="<%=request.getContextPath() %>/jsp/top.gif" alt="Top">
			</a>
			</h1>
			<div class="shadow">
				<div class="info">
					<p>
						<ul>		
						<li>1.aop/ioc - mvc - persistent - taglib - rpc - soa - devent-serializable</li>			
						<li>2.aop/ioc业务组件管理、依赖注入（属性注入、构造器注入、工厂模式注入）、声明式事务等    </li>	
    <li>3.rpc(http/netty/mina/jms/webservice/rmi/jgroups/restful) 安全高效 </li>	
   <li>4.distribute event framework </li>	
    <li>5.jsp taglib 分页、树、列表、抽屉式、逻辑标签 </li>	
    <li>6.mvc/restful（很好地支持jsonp，传统/html5文件上传下载，json） </li>	
    <li>7.国际化组件（属性文件热加载、缓冲、高性能、支持从classpath和web应用目录中加载资源文件） </li>	
    <li>8.persistent framework，全局事务管理（可整合托管bboss，ibatis，hibernate，spring等持久层的事务），很好地和业界主流数据源结合（dbcp,c3p0,proxool,weblogic,druid等）</li>	
     <li>9.xml-bean serializable（高效，很好地支持各种java数据类型和复杂对象结果，支持引用关系的序列化） </li>	
    <li>10.jms 开发套件 </li>	
    <li>11.cxf webservice 服务发布和客户端代理 </li>	
    <li>12.cluster with jgroups </li>	
    <li>13.quartz任务引擎管理 </li>	
    <li>14.hession管理及发布，客户端调用支持 </li>	
						  <li>15.<a href="<%=request.getContextPath()%>/monitor/console.htm" target="_blank">完备的框架监控体系</a></li>
						
						</ul>
					</p>
				</div>
			</div>
			   <h1>
				bboss下载及更新记录<a href="#top" name="998">
			<img border="0" src="<%=request.getContextPath() %>/jsp/top.gif" alt="Top">
			</a>
			</h1>
			<div class="shadow">
				<div class="info">
					<p><ul>
				
						<li><a href="https://github.com/bbossgroups/bbossgroups-3.5" target="_blank">github源码库（开发库，实时更新）</a>&nbsp;<a href="https://github.com/bbossgroups/bbossgroups-3.5/archive/master.zip" target="_blank">下载</a></li>
						<li><a href="https://github.com/bbossgroups/bbossgroups-3.5/commits/master" target="_blank">更新记录</a></li>
						
						</ul>	
					</p>
				</div>
			</div>
					
      <h1>
				bboss源码ant构建方法<a href="#top" name="999">
			<img border="0" src="<%=request.getContextPath() %>/jsp/top.gif" alt="Top">
			</a>
			</h1>
			<div class="shadow">
				<div class="info">
					<p>
						参考博客文章： 
						<a href="http://yin-bp.iteye.com/blog/1462842" target="_blank">《bboss 版本ant构建方法》</a>
					</p>
				</div>
			</div>
			 <h1>bboss-mvc体系结构图<a href="#top" name="3723">
			<img border="0" src="<%=request.getContextPath() %>/jsp/top.gif" alt="Top">
			</a></h1>
			<div class="shadow">
				<div class="info">
					<p>
						<div id="detail">
		                    
		                        <span class="value"><img src="<%=request.getContextPath()%>/jsp/bboss_archetech.png" width="800" height="400"/></span>
		                </div>
						
					</p>
				</div>
			</div>
			 <h1>bboss-mvc请求处理流程图<a href="#top" name="3724">
			<img border="0" src="<%=request.getContextPath() %>/jsp/top.gif" alt="Top">
			</a></h1>
		<div class="shadow">
				<div class="info">
					<p>
						<div id="detail">
		                    
		                        <span class="value"><img src="<%=request.getContextPath()%>/jsp/bboss_mvc_handler_flow.png"/></span>
		                </div>
						
					</p>
				</div>
			</div>
			<div class="blockContainer">

				 <h1>Bboss mvc DEMO列表<a href="#top" name="3720">
			<img border="0" src="<%=request.getContextPath() %>/jsp/top.gif" alt="Top">
			</a></h1>
				<table cellspacing="0" cellpadding="0" id="app" class="genericTbl">
					<thead>
						<tr>
							
							<th class="sortable">
								<a href="?d-16387-o=2&amp;size=&amp;d-16387-s=1">名称</a>
							</th>
							<th class="sortable">
								<a href="?d-16387-o=2&amp;size=&amp;d-16387-s=2">访问地址</a>
							</th>
						<th class="sortable">
								<a href="?d-16387-o=2&amp;size=&amp;d-16387-s=5">代码明细</a>
							</th>
							<th class="sortable">
								描述
							</th>
							
						   
						</tr>
					</thead>
					
					
					
					<tbody>
					<pg:list requestKey="demobeans">
						<tr class="odd">
							
							<td>
							
								<a href="#"><pg:cell colName="cnname" defaultValue=""/></a>
							</td>
							
							<td><pg:list colName="visturl" >
								<a title="title"
									href="${pageContext.request.contextPath}<pg:cell />" class="okValue"
									target="listbean"
									>
									<div id="rs_2">
									<pg:cell />
									</div> </a>
									</pg:list>
							</td>
							<td>
								<a href="detail.htm?demoname=<pg:cell colName="name" />" target="detail">查看代码</a>
							</td>
							
						
							<td>
								<pg:cell colName="description" defaultValue=""></pg:cell>
							</td>
							
							
						</tr>
						
						</pg:list>
						
					</tbody>
					
					
					
				</table>


			</div>
		</div>
		<h1>bboss-mvc框架demo的部署<a href="#top" name="3721">
			<img border="0" src="<%=request.getContextPath() %>/jsp/top.gif" alt="Top">
			</a></h1>
		<div class="shadow">
		 
				<div class="info">
					<p>
						<div id="detail">
		                    
		                        <span class="value">参考博客文章：<a href="http://yin-bp.iteye.com/blog/1026245" target="demodeploy">《bboss mvc demo构建部署方法》</a> </span>
		                </div>
						
					</p>
				</div>
			</div>
			<h1>bboss-mvc资源下载<a href="#top" name="3722">
			<img border="0" src="<%=request.getContextPath() %>/jsp/top.gif" alt="Top">
			</a></h1>
			<div  class="shadow" style="width:860;">
		       <div class="info" id="downloadList">
     			</div>
			<script type="text/javascript">
				$(document).ready(function(){
					  $("#downloadList").load("files/downloadList.htm");
					});
			</script>
		</div>
		<h1>视频教程下载<a href="#top" name="4722">
			<img border="0" src="<%=request.getContextPath() %>/jsp/top.gif" alt="Top">
			</a></h1>
			<div  class="shadow" style="width:860;">
		       <div class="info" id="downloadVidioList">
     			</div>
			<script type="text/javascript">
				$(document).ready(function(){
					  $("#downloadVidioList").load("vidios/downloadList.htm");
					});
			</script>
		</div>
		<h1>工具下载<a href="#top" name="8722">
			<img border="0" src="<%=request.getContextPath() %>/jsp/top.gif" alt="Top">
			</a></h1>
			<div  class="shadow" style="width:860;">
		       <div class="info" id="downloadToolList">
     			</div>
			<script type="text/javascript">
				$(document).ready(function(){
					  $("#downloadToolList").load("tools/downloadList.htm");
					});
			</script>
		</div>
			 <h1>web.xml配置示例<a href="#top" name="3725">
			<img src="<%=request.getContextPath() %>/jsp/top.gif" border="0" alt="Top">
			</a></h1>
		<div class="shadow">
				<div class="info">
					<p>
					
				         <span class="value">
				         web.xml中可以配置事务泄露监听器，申明mvc请求分发器以及url和控制器映射匹配规则，设定字符编码过滤器和出错处理页面，cxf webservice服务发布servlet,安全认证过滤器配置，http rpc servlet配置等<br/><br/><br/>
						 
						 
						 <img src="<%=request.getContextPath()%>/jsp/mapping.jpg"/><br/>
						 这个是控制器映射匹配，在web.xml中申明后缀名，mvc会自动识别并匹配以此后缀名的映射地址。并加载所有在WIN-INF目录下"bboss-"开头的xml文件.<br/><br/><br/>
						</span>	
					</p>
				</div>
			</div>
			
			
			<div class="blockContainer">
			<h1>友情链接<a href="#top" name="3719">
			<img border="0" src="<%=request.getContextPath() %>/jsp/top.gif" alt="Top">
			</a></h1>
			<div class="shadow">
				<div class="info"><p>
						<ul>			
						<li><a href="http://yin-bp.iteye.com/" target="_blank">bboss官方博客</a></li>
						<li><a href="https://github.com/bbossgroups/bbossgroups-3.5" target="_blank">bboss github托管地址</a></li>			
						<li><a href="http://www.iteye.com/wiki/bbossgroups/3092-mvc-bboss-config" target="_blank">bboss 开发系列文章之一 最佳实践</a> </li>
						<li><a href="http://www.iteye.com/wiki/bbossgroups/3090-mvc-aop-bboss" target="_blank">搭建bboss mvc eclipse开发工程，编写第一个实例</a></li>
						<li><a href="http://www.iteye.com/wiki/bbossgroups/3094-persistent" target="_blank">bboss SQLExecutor组件api使用实例</a></li>
						<li><a href="http://www.iteye.com/wiki/bbossgroups/3091-webservice-bboss-aop" target="_blank">bboss webservice引擎使用方法</a></li>
						<li><a href="http://www.iteye.com/wiki/bbossgroups/3089-bbossgroups-aop-rmi" target="_blank">bboss rmi组件服务发布和rmi客服端获取方法</a></li>
						<li><a href="http://yin-bp.iteye.com/blog/1026245" target="_blank">bboss mvc demo部署方法</a></li>
						<li><a href="http://www.kaifazhe.me/" target="_blank">开发者.我@一个开发者的网站</a></li>
						
						
						
						</ul>
					</p>
			</div>
			</div>
			</div>
			
			<div class="blockContainer">
			<h1>联系我们<a href="#top" name="1002">
			<img border="0" src="<%=request.getContextPath() %>/jsp/top.gif" alt="Top">
			</a></h1>
			<div class="shadow">
				<div class="info"><p>
				<div id="detail">	<b>QQ交流群：
				21220580
				166471282
				166471103
				154752521
				3625720</b> </div>		
			</p>
			</div>
			</div>
			</div>
			<h1>回到顶部<a href="#top" name="3727">
			<img border="0" src="<%=request.getContextPath() %>/jsp/top.gif" alt="Top">
			</a>
			
			</h1>
					
			
	</body>

<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1254131450'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s11.cnzz.com/z_stat.php%3Fid%3D1254131450%26show%3Dpic2' type='text/javascript'%3E%3C/script%3E"));</script>
</html>