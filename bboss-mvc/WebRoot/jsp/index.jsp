<%@ page contentType="text/html;charset=UTF-8" language="java"
	session="false"%><%@ page import="java.io.ByteArrayOutputStream"%><%@ page import="java.io.PrintStream"%><%@ page import="java.io.File"%><%@ page import="java.io.File,org.frameworkset.util.CodeUtils,com.frameworkset.util.FileUtil,org.frameworkset.web.demo.DemoUtil,com.frameworkset.util.StringUtil"%><%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%><%@ taglib uri="/WEB-INF/commontag.tld" prefix="common"%><%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/index.htm";
%><html>
	<head>
		<title>欢迎您访问BbossGroups官方网站 - 打造中国自主开发框架品牌</title>
		  <meta name="description" content="Bboss mvc DEMO列表 资源和文档下载 bboss-mvc框架体系结构图 bboss-mvc框架请求处理流程图 web.xml部署文件部分说明了mvc框架的涉及web.xml主要配置内容 框架更新记录" />
    <meta name="keywords" content="Bboss mvc DEMO列表,资源和文档下载 体系结构图, bboss-mvc框架请求处理流程图,web.xml部署文件,框架更新记录" />
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
	</head>
	<body>
	<div class="bshare-custom"><a title="分享到QQ空间" class="bshare-qzone"></a><a title="分享到新浪微博" class="bshare-sinaminiblog"></a><a title="分享到人人网" class="bshare-renren"></a><a title="分享到开心网" class="bshare-kaixin001"></a><a title="分享到豆瓣" class="bshare-douban"></a><a title="更多平台" class="bshare-more bshare-more-icon"></a><span class="BSHARE_COUNT bshare-share-count">0</span></div><script type="text/javascript" charset="utf-8" src="http://static.bshare.cn/b/buttonLite.js#style=-1&amp;uuid=c4e6c7ab-15cc-4511-9020-4ac5e8fe0edd&amp;pophcol=2&amp;lang=zh"></script><script type="text/javascript" charset="utf-8" src="http://static.bshare.cn/b/bshareC2.js"></script>
	<div id="caption">
	<a name="top"></a>
    <ul id="top">
        <li id="logo"><a href="index.htm"><img alt="BbossGroups Logo" src="${pageContext.request.contextPath}/jsp/logo.png" height="50"></a></li>
        
        <li id="runtime">
            <span class="uptime"><P>最新资讯：
 <a href="http://yin-bp.iteye.com/blog/1506335" target="_blank">开源工作流引擎activiti与bboss整合使用方法浅析</a></P></span>
        </li><li id="title">aop/ioc - mvc - persistent - taglib - rpc - soa - devent</li>
    </ul>
</div>

		<div id="mainBody">
			
    	<div class="embeddedBlockContainer">
			<h1>
				bboss官方站点使用指南(建议在火狐浏览器下浏览)：
			</h1>
			<div class="shadow">
				<div class="info">
					<p>
						<ul>		
						<li><a href="#1000">bbossgroups特色</a></li>	
						<li><a href="#999">bbossgroups源码ant构建方法</a></li>	
						<li><a href="#998">bbossgroups下载</a></li>
							
						<li><a href="#3720">Demo列表中包含每个demo的基本信息，包括名称，访问地址清单，功能说明，查看demo相关的所有代码和配置文件明细</a>
							<ul><li>点击demo的访问地址清单中的地址可以访问demo的实际功能并进行相应的操作</li>
							<li>点击代码明细，可以进入查看demo的实现代码，包括控制器类，业务组件，java对象，mvc配置文件，jsp页面</li></ul>
						</li>
						<li><a href="#3721">bboss-mvc框架demo的部署部分介绍了如何下载和部署demo应用</a></li>
						<li><a href="#3722">资源和文档下载</a></li>
						<li><a href="#4722">视频教程下载</a></li>
						<li><a href="#3723">bboss-mvc框架体系结构图</a></li>
						<li><a href="#3724">bboss-mvc框架请求处理流程图</a></li>
						<li><a href="#3725">web.xml部署文件部分说明了mvc框架的涉及web.xml主要配置内容</a></li>						
						
						<li><a href="#3726">框架更新记录</a></li>
						
						<li><a href="#3719">友情链接</a></li>
						<li><a href="#1002">联系我们</a></li>		
						</ul>
					</p>
				</div>
			</div>
			<h1>
				bbossgroups特色<a href="#top" name="1000">
			<img border="0" src="<%=request.getContextPath() %>/jsp/top.gif" alt="Top">
			</a>
			</h1>
			<div class="shadow">
				<div class="info">
					<p>
						<ul>		
									
						<li>1.aop/ioc 业务组件管理、依赖注入、声明式事务等</li>
						<li>2.rpc(http/netty/mina/jms/webservice/rmi/jgroups/restful) 安全高效</li>						
						<li>3.persistent framework,multi db transaction</li>
						<li>4.mvc/restful</li>
						<li>5.jsp taglib 分页、树、列表、抽屉式、逻辑标签</li>
						<li>6.distribute event framework</li>
						<li>7.xml-bean serializable</li>
						 <li>8.cxf webservice 服务发布和客户端代理</li>
						  <li>9.quartz任务管理</li>
						  <li>10.cluster with jgroups</li>
						  <li>11.jms 开发套件</li>
						  <li>12.完备的框架监控体系</li>
						
						</ul>
					</p>
				</div>
			</div>
			   <h1>
				bboss下载<a href="#top" name="998">
			<img border="0" src="<%=request.getContextPath() %>/jsp/top.gif" alt="Top">
			</a>
			</h1>
			<div class="shadow">
				<div class="info">
					<p><ul>
					<li><a href="http://www.bbossgroups.com/file/download.htm?fileName=bbossgroups-3.5.1.zip" target="_blank">官网下载发布版本</a></li>
					 
					
						<li><a href="https://sourceforge.net/projects/bboss/files/" target="_blank">从sourceforge下载发布版本</a></li>
						<li><a href="https://github.com/bbossgroups/bbossgroups-3.5" target="_blank">从github源码库下载最新代码（开发库，实时更新）</a></li>
						</ul>	
					</p>
				</div>
			</div>
					
      <h1>
				bbossgroups源码ant构建方法<a href="#top" name="999">
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
			<div class="blockContainer">

				 <h1>Bboss mvc DEMO列表<a href="#top" name="3720">
			<img border="0" src="<%=request.getContextPath() %>/jsp/top.gif" alt="Top">
			</a></h1>
				<table cellspacing="0" cellpadding="0" id="app" class="genericTbl">
					<thead>
						<tr>
							<th class="order1 sorted">
								&nbsp;
							</th>
							<th class="sortable">
								<a href="?d-16387-o=2&amp;size=&amp;d-16387-s=1">名称</a>
							</th>
							<th class="sortable">
								<a href="?d-16387-o=2&amp;size=&amp;d-16387-s=2">访问地址</a>
							</th>
							<th class="sortable">
								&nbsp;
							</th>
							<th class="sortable">
								描述
							</th>
							<th class="sortable">
								<a href="?d-16387-o=2&amp;size=&amp;d-16387-s=5">代码明细</a>
							</th>
						   
						</tr>
					</thead>
					
					
					
					<tbody>
					<pg:list requestKey="demobeans">
						<tr class="odd">
							<td class="leftMostIcon">
								<a
									href="#"
									class="imglink"> <img title="Undeploy /bboss-mvc"
										alt="Undeploy" src="${pageContext.request.contextPath}/css/classic/img/bin.jpg"
										class="lnk"> </a>
							</td>
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
								<a href="#"
									class="imglink"
									onclick="#"> <img
										border="0" title="" alt="reload"
										src="${pageContext.request.contextPath}/css/classic/gifs/reset.gif" id="ri_2"> </a>
							</td>
							<td>
								<pg:cell colName="description" defaultValue=""></pg:cell>
							</td>
							<td>
								<a href="detail.htm?demoname=<pg:cell colName="name" />" target="detail">进入</a>
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
		                    
		                        <span class="value">参考博客文章：<a href="http://yin-bp.iteye.com/blog/1026245" target="demodeploy">《bbossgroups mvc demo构建部署方法》</a> </span>
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
		 <h1>bboss-mvc框架体系结构图<a href="#top" name="3723">
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
			 <h1>bboss-mvc框架请求处理流程图<a href="#top" name="3724">
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
			 <h1>web.xml的配置<a href="#top" name="3725">
			<img src="<%=request.getContextPath() %>/jsp/top.gif" border="0" alt="Top">
			</a></h1>
		<div class="shadow">
				<div class="info">
					<p>
						<div id="detail">	
		
						
							<table cellspacing="0" id="resultsTable">
								<tbody>
									<tr>
										<td>
											<pre name="code" class="xml"><%
											String content = null;
											content = DemoUtil.getDemoContentCache().getFileContent(request.getRealPath("/WEB-INF/web.xml"),"UTF-8",true);
											out.print(content);							
											%>
											</pre>
										</td>
									</tr>
								</tbody>
							</table>
									
				         </div> 
				         <span class="value">
				         此web.xml的主要功能是配置加载扩展包目录下jar包监听器，申明mvc请求分发器以及url和控制器映射匹配规则，设定字符编码过滤器和出错处理页面。<br/><br/><br/>
						 <img src="<%=request.getContextPath()%>/jsp/tags.jpg"/><br/>
						 这个监听器是指加载扩展jar包目录结构的监听器，声明它后它会加载引入扩展的jar包。<br/><br/><br/>
						 
						 <img src="<%=request.getContextPath()%>/jsp/mapping.jpg"/><br/>
						 这个是控制器映射匹配，在web.xml中申明后缀名，mvc会自动识别并匹配以此后缀名的映射地址。并加载所有在WIN-INF目录下"bboss-"开头的xml文件.<br/><br/><br/>
						</span>	
					</p>
				</div>
			</div>
			 <h1>框架更新记录<a href="#top" name="3726">
			<img border="0" src="<%=request.getContextPath() %>/jsp/top.gif" alt="Top">
			</a></h1>
			<div class="shadow">
				<div class="info">
					<p>
						<div id="detail">	
						
							<table cellspacing="0" id="resultsTable">
								<tbody>
									<tr>
										<td>
											<pre name="code" class="xml"><%
											content = DemoUtil.getDemoContentCache().getFileContent(request.getRealPath("/filesdown/releasenote.txt"),"UTF-8",true);
											out.print(content);							
											%>
											</pre>
										</td>
									</tr>
								</tbody>
							</table>
				         </div> 
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
						<li><a href="https://sourceforge.net/projects/bboss/files/" target="_blank">bbossgroups源码工程sourceforge下载地址</a></li>
						<li><a href="https://github.com/bbossgroups/bbossgroups-3.5" target="_blank">bbossgroups github托管地址</a></li>			
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
	<script language="javascript">
if(!$.browser.msie) {	
dp.SyntaxHighlighter.ClipboardSwf = '${pageContext.request.contextPath}/include/syntaxhighlighter/clipboard.swf';
dp.SyntaxHighlighter.HighlightAll('code');
}
</script>
<script src="http://s17.cnzz.com/stat.php?id=3269520&web_id=3269520" language="JavaScript"></script>
</html>