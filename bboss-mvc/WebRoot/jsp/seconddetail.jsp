<%@ page contentType="text/html;charset=UTF-8" language="java"
	session="false"%><%@ page import="java.io.ByteArrayOutputStream"%><%@ page import="java.io.PrintStream"%><%@ page import="java.util.*"%><%@ page import="java.text.SimpleDateFormat"%><%@ page import="org.frameworkset.web.demo.SiteDemoBean"%><%@ page import="org.frameworkset.web.demo.FormUrl"%><%@ page import="java.io.File,org.frameworkset.util.CodeUtils,com.frameworkset.util.FileUtil,org.frameworkset.web.demo.DemoUtil,com.frameworkset.util.StringUtil"%><%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%><%
    String parentPath = request.getRealPath("/");
    parentPath = parentPath.replaceAll("\\\\","/");

%><html>
  <pg:beaninfo requestKey="demobean" >
<head>
	<title>bboss mvc demo code- <pg:cell colName="cnname"/></title>
		 <meta name="description" content="bboss mvc demo code- <pg:cell colName="cnname"/>,<pg:cell colName="description" />" />
    <meta name="keywords" content="demo code,<pg:cell colName="cnname"/>" />
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
	<div id="contentBody">
	<div class="embeddedBlockContainer">
			<h1>
				DEMO配置结构示意图：<a href="#top" name="1">
			<img border="0" src="<%=request.getContextPath() %>/jsp/top.gif" alt="Top">
			</a>
			</h1>
			<div class="shadow">
				<img border="0" src="<%=request.getContextPath() %>/jsp/demostructure.png" >
			</div>
		</div>
	<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/include/syntaxhighlighter/styles/SyntaxHighlighter.css"></link>
	<script language="javascript" src="${pageContext.request.contextPath}/include/syntaxhighlighter/shCore.js"></script>
	<script language="javascript" src="${pageContext.request.contextPath}/include/syntaxhighlighter/shBrushJava.js"></script>
	<script language="javascript" src="${pageContext.request.contextPath}/include/syntaxhighlighter/shBrushXml.js"></script>
	<script language="javascript" src="${pageContext.request.contextPath}/include/syntaxhighlighter/shBrushJScript.js"></script>
	
        <div class="embeddedBlockContainer">
        <a name="top"></a>
			<h1>
				DEMO- [<pg:cell colName="cnname"/>]代码明细阅读指南
			</h1>
			<div class="shadow">
				<div class="info">
					<p>
						<ul>	
						<li><a href="#1">配置结构示意图</a></li>	
						<li><a href="#2">demo的描述信息</a></li>								
						<li><a href="#3">demo的控制器类以及字符编码信息（避免展示时中文乱码）</a></li>
						<li><a href="#4">demo依赖的业务组件和javabean对象文件清单以及字符编码信息（避免展示时中文乱码）</a></li>
						<li><a href="#5">demo的配置文件路径以及字符编码信息（避免展示时中文乱码）</a></li>
						<li><a href="#6">demo的jsp页面清单以及字符编码信息（避免展示时中文乱码）</a></li>
						<li><a href="#7">demo的访问地址清单</a></li>
						</ul>
					</p>
				</div>
			</div>
		</div>
		
		<div class="embeddedBlockContainer">
			<h1>
				DEMO功能描述- <pg:cell colName="cnname"/><a href="#top" name="2">
			<img border="0" src="<%=request.getContextPath() %>/jsp/top.gif" alt="Top">
			</a>
			</h1>
			<div class="shadow">
				<pg:cell colName="description" />
			</div>
		</div>
		
		
      
		<div class="embeddedBlockContainer">
			<h1>
				控制器实现类- <pg:cell colName="cnname"/><a href="#top" name="3">
			<img border="0" src="<%=request.getContextPath() %>/jsp/top.gif" alt="Top">
			</a>
			</h1>
			<div class="shadow">
				<div class="info">
					<p>
					<%
							 SiteDemoBean bean = (SiteDemoBean)beaninfo.getOrigineObject();
					
							  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							  File file = null;
							  String content = null;
							  String charset = null;
							%>
						<pg:notnull colName="controllerClass">
							<%
							 
							 file = new File(StringUtil.getNormalPath(parentPath,bean.getControllerClass()));
							if(!file.exists())
							{
								file = new File(new File(parentPath).getParent(),bean.getControllerClass());
							}
							
							%>
							类路径：
							<span class="value"><%=file.getAbsolutePath()%></span>
							Size:&nbsp;
						
							
							<span class="value"><%=file.length()%>b</span> Last modified:&nbsp;
							<span class="value"><%=sdf.format(new Date(file.lastModified()))%></span> Encoding:&nbsp;
							<span class="value"><pg:cell colName="controllerClassCharset" defaultValue=""/></span>
							
						</pg:notnull>
						
					</p>
				</div>
			</div>
			
			<table cellspacing="0" id="resultsTable">
				<tbody>
					<tr>
						<td>
							<pre name="code" class="java">
							<pg:notnull colName="controllerClass"><%
								 charset = bean.getControllerClassCharset();
								
								if(charset!= null&&!charset.equals(""))
								 //content = StringUtil.HTMLNoBREncode(FileUtil.getFileContent(parentPath+bean.getControllerClass(),charset));
								 	content = DemoUtil.getDemoContentCache().getFileContent(file.getAbsolutePath(),charset,true);
								else 
							     	//content = StringUtil.HTMLNoBREncode(FileUtil.getFileContent(parentPath+bean.getControllerClass()));
							     	content = DemoUtil.getDemoContentCache().getFileContent(file.getAbsolutePath(),null,true);
								out.print(content);
								%>
							 </pg:notnull>
							</pre>
							
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		
		
		
		<div class="embeddedBlockContainer">
			<h1>
				控制器依赖的业务组件和PO对象- <pg:cell colName="cnname"/><a href="#top" name="4"><img border="0" src="<%=request.getContextPath() %>/jsp/top.gif" alt="Top">
			</a>
			</h1>
			<div class="shadow">
		  
	                   	<%
						  List<String> beanClass = bean.getBeanClass();
	                   	  if(beanClass != null&&beanClass.size()>0)
						  for(int i=0;i<beanClass.size();i++){
						  
							  file = new File(StringUtil.getNormalPath(parentPath,beanClass.get(i)));
							  if(!file.exists())
							  {
							  	file = new File(new File(parentPath).getParent(),beanClass.get(i));
							  	
							  }
							  
						%>
				<div class="info">
					<p>
						JavaBean路径：
						<span class="value"><%=file.getAbsolutePath()%></span>
						Size:&nbsp;
						
						<span class="value"><%=file.length()%>b</span> Last modified:&nbsp;
						<span class="value"><%=sdf.format(new Date(file.lastModified()))%></span> Encoding:&nbsp;
						<span class="value"><pg:cell colName="beanClassCharset" defaultValue=""/></span> 
					</p>
				</div>
			</div>
			
			<table cellspacing="0" id="resultsTable">
				<tbody>
					<tr>
						<td>
							<pre name="code" class="java"><%
							charset = bean.getBeanClassCharset();
						    // content = StringUtil.HTMLNoBREncode(FileUtil.getFileContent(parentPath+beanClass.get(i)));
						    if(charset == null || charset.equals(""))
						    {
						     	content =DemoUtil.getDemoContentCache().getFileContent(file.getAbsolutePath(),null,true);
						    }
						    else
						    {
						    	content =DemoUtil.getDemoContentCache().getFileContent(file.getAbsolutePath(),charset,true);
						    }
						    	
							out.print(content);
							%>
							</pre>
						</td>
					</tr>
				</tbody>
			</table>
            <%}else out.print("无javaBean"); %>
		</div>
		<div class="embeddedBlockContainer">
			<h1>
				控制器和url地址映射配置文件- <pg:cell colName="cnname"/><a href="#top" name="5">
			<img border="0" src="<%=request.getContextPath() %>/jsp/top.gif" alt="Top">
			</a>
			</h1>
			<div class="shadow">
				<div class="info">
					<p>
						文件路径：
						<span class="value"><%=parentPath%><pg:cell colName="configFile" defaultValue=""/></span>
						Size:&nbsp;
						<%
						file = new File(StringUtil.getNormalPath(parentPath,bean.getConfigFile()));
						%>
						<span class="value"><%=file.length()%>b</span> Last modified:&nbsp;
						<span class="value"><%=sdf.format(new Date(file.lastModified()))%></span> Encoding:&nbsp;
						<span class="value"><pg:cell colName="configFileCharset" defaultValue=""/></span> 
					</p>
				</div>
			</div>
			
			<table cellspacing="0" id="resultsTable">
				<tbody>
					<tr>
						<td>
							<pre name="code" class="xml"><%
							if(bean.getConfigFile() == null || "".equals(bean.getConfigFile()))
							{
								out.print("");
							}
							else
							{
								charset = bean.getConfigFileCharset();
								if(charset!= null&&!charset.equals(""))
								 //content = StringUtil.HTMLNoBREncode(FileUtil.getFileContent(parentPath+bean.getConfigFile(),charset));
								 content =DemoUtil.getDemoContentCache().getFileContent(StringUtil.getNormalPath(parentPath,bean.getConfigFile()),charset,true);
								else 
							     //content = StringUtil.HTMLNoBREncode(FileUtil.getFileContent(parentPath+bean.getConfigFile()));
							     content =DemoUtil.getDemoContentCache().getFileContent(StringUtil.getNormalPath(parentPath,bean.getConfigFile()),null,true);								
								out.print(content);							
							}
							%>
							</pre>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="embeddedBlockContainer">
		<h1>
			jsp/js资源清单-<pg:cell colName="cnname"/><a href="#top" name="6"><img border="0" src="<%=request.getContextPath() %>/jsp/top.gif" alt="Top">
			</a>
			</h1>
			
		<pg:list colName="formlist">
		
			<pg:cell colName="description"/>
			<div class="shadow">
				<div class="info">
					<p>
						文件路径：
						<span class="value"><%=parentPath%><pg:cell colName="formPath"/></span>
						Size:&nbsp;
						<%
						 FormUrl furl = (FormUrl)dataSet.getOrigineObject();
						 File formfile = new File(StringUtil.getNormalPath(parentPath,furl.getFormPath()));
						%>
						<span class="value"><%=formfile.length()%>b</span> Last modified:&nbsp;
						<span class="value"><%=sdf.format(new Date(formfile.lastModified())) %></span> Encoding:&nbsp;
						<span class="value"><pg:cell colName="charset"/></span>&nbsp;
					</p>
				</div>
			</div>
			
			<table cellspacing="0" id="resultsTable">
				<tbody>
					<tr>
						<td>
							<pre name="code" class="jscript"><%
							charset = furl.getCharset();
							if(charset!= null&&!charset.equals(""))
							 content = DemoUtil.getDemoContentCache().getFileContent(StringUtil.getNormalPath(parentPath,furl.getFormPath()),charset,true);
							 
							else 
						     content = DemoUtil.getDemoContentCache().getFileContent(StringUtil.getNormalPath(parentPath,furl.getFormPath()),null,true);
							out.print(content);
							%>
							</pre>
						</td>
					</tr>
				</tbody>
			</table>
		
		</pg:list>
		</div>
		<div class="embeddedBlockContainer">
			<h1>
				访问入口地址- <pg:cell colName="cnname"/><a href="#top" name="7"><img border="0" src="<%=request.getContextPath() %>/jsp/top.gif" alt="Top">
			</a>
			</h1>
			<div class="shadow">
				<pg:list colName="visturl" >
				<a title="title"
					href="${pageContext.request.contextPath}<pg:cell />" class="okValue"
					target="listbean"
					>
					<div id="rs_2">
					<pg:cell />
					</div> </a>
					</pg:list>
			</div>
		</div>
	</div>
	
	
   

</body>
 </pg:beaninfo>
 <script language="javascript">
if(!$.browser.msie) {
dp.SyntaxHighlighter.ClipboardSwf = '${pageContext.request.contextPath}/include/syntaxhighlighter/clipboard.swf';
dp.SyntaxHighlighter.HighlightAll('code');
}
</script>
<!--<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1254131450'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s11.cnzz.com/z_stat.php%3Fid%3D1254131450%26show%3Dpic2' type='text/javascript'%3E%3C/script%3E"));</script>-->
</html>