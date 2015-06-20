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
	<div><a name="top"></a><a href="http://yin-bp.iteye.com" target="_blank">bboss博客</a>&nbsp;&nbsp;&nbsp;<a href="https://github.com/bbossgroups/bbossgroups-3.5" target="_blank">Bboss GitHub</a>&nbsp;&nbsp;&nbsp;<a href="https://github.com/bbossgroups/bbossgroups-3.5/archive/master.zip" target="_blank">下载</a>&nbsp;&nbsp;&nbsp;<a href="#3719">友情链接</a>&nbsp;&nbsp;&nbsp;<a href="#1002">联系我们</a>		</div>
	<div id="caption">
    <ul id="top">
        
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
				bboss是什么：
			</h1>
			<div class="shadow">
				<div class="info">
					<p>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;bboss是一个j2ee开源框架，为企业级应用开发提供一站式解决方案，并能有效地支撑移动应用开发。bboss功能涵盖ioc，mvc，jsp自定义标签库，持久层，全局事务托管，安全认证，SSO，web会话共享，cxfwebservice服务发布和管理，hessian服务发布和管理等功能。另外还提供了符合中国式自由流的bboss activiti工作流引擎。在不断的实践过程，越来越多的好东西被吸纳到bboss这个大家庭中，使得bboss能够更好地应用于企业应用项目中，能够更好地解决开发过程中碰到的实际问题。 
          </p>
					<p>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;基于bboss，可以快速地开发构建稳定、高效、健壮、可扩展的企业级应用系统。 
					</p>
				</div>
			</div>	
			<h1>
				bboss使用指南：
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
						  <li>1.<a href="http://yin-bp.iteye.com/category/55606" target="_blank">aop/ioc</a>业务组件管理、依赖注入（属性注入、构造器注入、工厂模式注入）、声明式事务等;可扩展性强，用户可以自行任意为ioc元素增加扩展属性，以便实现自定义的ioc依赖注入功能（其他ioc框架扩展ioc语法，可能需要编写xsd描述文件，bboss的ioc配置语法是松散而严谨的，很容易扩展而且不需要编写xsd文件）</li>    
    <li>2.<a href="http://yin-bp.iteye.com/category/327706" target="_blank">rpc</a>(http/netty/mina/jms/webservice/rmi/jgroups/restful) 安全高效,可非常方便地将bboss ioc管理的业务组件发布成RPC服务</li>
    <li>3.<a href="http://yin-bp.iteye.com/category/65637" target="_blank">distribute event framework</a>，分布式事件广播组件，基于jgroups。</li>
    <li>4.<a href="http://yin-bp.iteye.com/category/69334" target="_blank">jsp taglib</a> 分页、树、列表、抽屉式、逻辑标签</li>
    <li>5.<a href="http://yin-bp.iteye.com/category/132426" target="_blank">mvc/restful</a>简单高效的mvc框架，很好地支持xml,json,文件上传下载，jsonp，传统/html5文件上传下载，灵活的参数绑定机制，与bboss标签库分页标签无缝对接</li>
    <li>6.<a href="http://yin-bp.iteye.com/category/327707" target="_blank">国际化组件</a>，支持国际化属性文件热加载、缓存、高性能，支持从classpath和web应用目录中加载资源文件，提供国际化标签和国际化编程API</li>
    <li>7.<a href="http://yin-bp.iteye.com/category/55607" target="_blank">persistent framework</a>，多数据库支持，丰富的api，提供查询行处理器，全局事务管理（可整合托管bboss，ibatis，hibernate，spring等持久层的事务），支持多数据库事务，多种事务编程模式，很好地和业界主流数据源结合（dbcp,c3p0,proxool,weblogic,druid等），支持业界主流的o/r mapping机制，支持xml配置sql风格的api，也支持直接在java程序中直接使用sql的api；提供了持久层<a href="<%=request.getContextPath()%>/monitor/dbmonitor_.jsp" target="_blank">连接池的监控功能</a></li>
    <li>8.<a href="http://yin-bp.iteye.com/category/327708" target="_blank">xml-bean serializable</a>,高效，很好地支持各种java数据类型和复杂对象结构，支持引用关系的序列化,提供序列化插件机制，可以根据需要定制对象的序列化行为</li>
    <li>9.<a href="http://yin-bp.iteye.com/blog/630527" target="_blank">jms </a>开发套件</li>
    <li>10.<a href="http://yin-bp.iteye.com/category/327709" target="_blank">cxf webservice</a> 服务发布和客户端代理,查看：<a href="http://bbossgroups.group.iteye.com/group/wiki/3091-webservice-bboss-aop" target="_blank">服务发布教程</a></li>
    <li>11.<a href="http://yin-bp.iteye.com/blog/1776315" target="_blank">quartz任务</a>引擎管理</li>
    <li>12.<a href="http://yin-bp.iteye.com/category/327710" target="_blank">hession服务管理及发布</a>，客户端调用支持</li>
    <li>13.集群<a href="http://yin-bp.iteye.com/category/327553" target="_blank">会话共享</a>，跨容器跨平台，跨站跨应用会话共享及SSO，高效，配置简单,提供实用的会话统计监控和会话集中管理功能（会话查询，会话删除，会话属性数据查看等等）</li>
    <li>14.<a href="http://yin-bp.iteye.com/category/327711" target="_blank">安全认证SSO</a>，ticket令牌管理（令牌生成和校验），支持集群环境令牌管理和校验，提供令牌编程注解和令牌断言jsp标签，采用令牌标签可防止表单重复提交功能
    <li>15.<a href="http://yin-bp.iteye.com/category/327712" target="_blank">bboss 工作流引擎</a>（基于开源activiti 5.12扩展），遵循bpmn规范，支持中国式自由流，支持任意驳回、驳回后再回到驳回点、撤销、任意跳转，支持抄送到人和部门，支持单实例/多实例切换，支持多实例串并行切换，自动跳过无处理人的节点，可自动跳过相同处理人的节点，能够很好地保持业务事务和工作流事务的一致性。</li> 
    <li>16.<a href="<%=request.getContextPath()%>/monitor/console.htm" target="_blank">完备的框架监控体系</a></li>

						</ul>
					</p>
				</div>
			</div>
			   <h1>
				bboss源码下载及版本更新记录<a href="#top" name="998">
			<img border="0" src="<%=request.getContextPath() %>/jsp/top.gif" alt="Top">
			</a>
			</h1>
			<div class="shadow">
				<div class="info">
					<p><ul>
				
						<li><a href="https://github.com/bbossgroups/bbossgroups-3.5" target="_blank">github源码库（开发库，实时更新）</a>&nbsp;<a href="https://github.com/bbossgroups/bbossgroups-3.5/archive/master.zip" target="_blank">下载</a></li>
						<li><a href="https://github.com/bbossgroups/bbossgroups-3.5/commits/master" target="_blank">版本更新记录</a></li>
						
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
			
		</div>
		<h1>视频教程下载<a href="#top" name="4722">
			<img border="0" src="<%=request.getContextPath() %>/jsp/top.gif" alt="Top">
			</a></h1>
			<div  class="shadow" style="width:860;">
		       <div class="info" id="downloadVidioList">
     			</div>
			
		</div>
		<h1>工具下载<a href="#top" name="8722">
			<img border="0" src="<%=request.getContextPath() %>/jsp/top.gif" alt="Top">
			</a></h1>
			<div  class="shadow" style="width:860;">
		       <div class="info" id="downloadToolList">
     			</div>
			
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
						<li><a href="http://yin-bp.iteye.com/blog/1026261" target="_blank">搭建bboss mvc eclipse开发工程，编写第一个实例</a></li>
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
				<div id="detail">	<b>QQ:122054810<br/>
					
					QQ交流群：
				21220580
				166471282
				166471103
				154752521
				3625720</b><br/><b>微信公众号：</b><img alt="bboss Logo" height="120" width="120" src="${pageContext.request.contextPath}/jsp/getqrcode.jpg"> </div>		
			</p>
			</div>
			
			</div>
			</div>
			<h1>回到顶部<a href="#top" name="3727">
			<img border="0" src="<%=request.getContextPath() %>/jsp/top.gif" alt="Top">
			</a>
			
			</h1>
					
			
	</body>
<script type="text/javascript">
				$(document).ready(function(){
					  $("#downloadToolList").load("tools/downloadList.htm");
					  $("#downloadList").load("files/downloadList.htm");
					  $("#downloadVidioList").load("vidios/downloadList.htm");
					});
			</script>
<!--<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1254131450'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s11.cnzz.com/z_stat.php%3Fid%3D1254131450%26show%3Dpic2' type='text/javascript'%3E%3C/script%3E"));</script>-->			
</html>