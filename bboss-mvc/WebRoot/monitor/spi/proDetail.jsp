
<%@page import="org.frameworkset.spi.assemble.ProviderInfoQueue"%>
<%@page import="java.util.List,org.frameworkset.spi.assemble.Pro"%>
<%@page import="java.lang.reflect.Method"%>
<%@page import="org.frameworkset.web.servlet.context.WebApplicationContext"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.net.URLEncoder"%><%
/**
 * 
 * <p>Title: 管理服务明细信息显示页面</p>
 *
 * <p>Description: 管理服务明细信息显示页面</p>
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
<%@page import="org.frameworkset.spi.assemble.*,org.frameworkset.spi.BaseApplicationContext,java.util.Iterator,java.util.Map"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>		

<% 
	String rootpath = request.getContextPath();
	String selected = request.getParameter("selected");
	String nodePath = request.getParameter("nodePath");	
	ProArray array = null;
	BaseApplicationContext context = BaseApplicationContext.getBaseApplicationContext(nodePath);
	boolean isWebApplicationContext = false;
	if(context instanceof WebApplicationContext)
	{
		isWebApplicationContext = true;
	}
	//String classType = request.getParameter("classType");
	//管理服务明细信息
	String proParentPath = request.getParameter("proParentPath");
	Pro providerManagerInfo =  null;
	if(proParentPath != null)
		providerManagerInfo = context.getInnerPro(proParentPath,selected) ;
	else
		providerManagerInfo = context.getProBean(selected) ;
	if(providerManagerInfo == null)
	{
		out.print("非全局bean组件");
		return ;
	}	
	//服务名称
	String name = proParentPath == null ?selected:proParentPath +  "[" + selected + "]";
	String pro_name = providerManagerInfo.getName() == null?"":providerManagerInfo.getName() ;
		Iterator iterator = null;
		boolean ismap = false;
		boolean isarray = false;
		String title = "";
		String componetType = "";
		int size = 0;
		if(providerManagerInfo.isList())
		{
			iterator = providerManagerInfo.getList().iterator();
			size = providerManagerInfo.getList().size();
			title = "List";
			componetType = providerManagerInfo.getList().getComponentType();
			if(proParentPath == null)
			{
				/**
				* vvvv^^list^^0#!#cccc^^map#!#dddd^^map
	 			* vvvv^^list^^0#!#cccc^^map#!#list^^0
				*/
				proParentPath = selected + "^^list";
			}
			else
			{
				proParentPath = proParentPath + "#!#" + selected + "^^list";
			}
		}
		else if(providerManagerInfo.isSet())
		{
			iterator = providerManagerInfo.getSet().iterator();
			title = "Set";
			size = providerManagerInfo.getSet().size();
			if(proParentPath == null)
			{
				/**
				* vvvv^^list^^0#!#cccc^^map#!#dddd^^map
	 			* vvvv^^list^^0#!#cccc^^map#!#list^^0
				*/
				proParentPath = selected + "^^set";
			}
			else
			{
				proParentPath = proParentPath + "#!#" + selected + "^^set";
			}
		}
		
		else if(providerManagerInfo.isMap())
		{
			iterator = providerManagerInfo.getMap().keySet().iterator();
			ismap = true;
			title = "Map";
			size = providerManagerInfo.getMap().size();
			componetType = providerManagerInfo.getMap().getComponentType();
			if(proParentPath == null)
			{
				/**
				* vvvv^^list^^0#!#cccc^^map#!#dddd^^map
	 			* vvvv^^list^^0#!#cccc^^map#!#list^^0
				*/
				proParentPath = selected + "^^map";
			}
			else
			{
				proParentPath = proParentPath + "#!#" + selected + "^^map";
			}
		}
		else if(providerManagerInfo.isArray())
		{
			//iterator = providerManagerInfo.getArray().keySet().iterator();
			array = providerManagerInfo.getArray();
			isarray = true;
			title = "Array";
			size = array.size();
			componetType = array.getComponentType();
			if(proParentPath == null)
			{
				/**
				* vvvv^^list^^0#!#cccc^^map#!#dddd^^map
	 			* vvvv^^list^^0#!#cccc^^map#!#list^^0
				*/
				proParentPath = selected + "^^array";
			}
			else
			{
				proParentPath = proParentPath + "#!#" + selected + "^^array";
			}
		}
		
		
		String editor = "";
		if(providerManagerInfo.getEditorString() != null)
		{
			editor = providerManagerInfo.getEditorString();
		}
		
		if(proParentPath != null && !proParentPath.equals(""))
		{
			proParentPath = URLEncoder.encode(proParentPath);
		}
		
	 %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><%=pro_name %></title>
		
<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="<%=rootpath%>/include/contentpage.css">
		<link rel="stylesheet" type="text/css" href="<%=rootpath%>/include/tab.winclassic.css">
		<tab:tabConfig/>	
	</head>
	
	<body class="contentbodymargin" scroll="no">
	<div style="width:100%;height:100%;overflow:auto">
	<fieldset height=100% width="100%">
	<LEGEND align=left><strong>&nbsp;全局属性配置:<%=pro_name %>&nbsp;组件元素类型:<%=componetType == null?"属性对象":componetType %></strong></LEGEND>
	<table class="thin" width="100%">
		<tr><td colspan="3" class="headercolor">全局属性配置信息</td></tr>
		<tr>
		<td class="headercolor" width="20%">配置属性名</td>
		<td class="headercolor" width="30%">属性对应值</td>
		
		<td class="headercolor" width="50%">描述</td>
		
		</tr>
		<tr>
		<td width="20%">name</td><td width="30%"><%=pro_name %></td><td width="50%">全局属性名称，唯一标识一个全局属性，保持全局唯一性</td>
		</tr>
		<tr>
		<td width="20%">属性定义路径</td><td width="20%"><%=name %></td><td width="50%">属性定义路径信息</td>
		
		</tr>
		<tr>
		<td width="20%">value</td><td width="30%"><%
		
		if(providerManagerInfo.getValue() != null)
		{
			if(providerManagerInfo.isList())
				out.print("value=[List set]");
			else if(providerManagerInfo.isMap())
				out.print("value=[Map set]");
			else if(providerManagerInfo.isSet())
				out.print("value=[Set set]");
			else if(providerManagerInfo.isArray())
				out.print("value=[Array set]");
			else 
				out.print("value=" + providerManagerInfo.getValue() );		
		}
		
		
		%></td><td width="50%">全局属性值</td>
		</tr>
		<tr>
		
		<td width="20%">editor</td><td width="30%"><%=editor %></td><td width="50%">属性编辑器</td>
		</tr>
		<tr>
		<td width="20%">label</td><td width="30%"><%=providerManagerInfo.getLabel() != null?providerManagerInfo.getLabel():"" %></td><td width="50%">全局属性label属性，用来做页面时有用</td>
		
		</tr>			
	</table>
	</fieldset>
	
	
	
	
	<fieldset height=100% width="100%">
	<LEGEND align=left><strong>&nbsp;<%=title %>明细:<%=pro_name %>&nbsp;</strong></LEGEND>
	<table  height="50%"  width="100%" border="0" cellpadding="0" cellspacing="0"  class="thin">
	
	<tr>
		<td class="headercolor" width="20%">属性名</td>
		<td class="headercolor" width="30%">明细</td>
		<td class="headercolor" width="10%">类型</td>
		<td class="headercolor" width="50%">描述</td>
		</tr>
	<%if(iterator != null || array != null){ 
	
	Pro pro = null;
	String key = null;
		int i = 0;
		while(true){
			if(!isarray)
			{
				if(iterator.hasNext()){
				
					if(ismap)
					{
						key = (String)iterator.next();
						pro = (Pro)providerManagerInfo.getMap().get(key);
					}
					else
					{
						pro = (Pro)iterator.next();
						key = i + "";
						i ++;
						
					}
				}
				else
				{
					break;
				}
			}
			else
			{
				if(i < array.size())
				{
					pro = array.getPro(i);
					key = i + "";
					i ++;
				}
				else
				{
					break;
				}
			}
			String __name = pro.getName();
	%>
	
		<tr>
	<td><%=(__name == null?key:__name) %></td>	
	<td>
	<%
		if(pro.isBean())
		{
			%>
	<a href="beanDetail.jsp?selected=<%=key %>&proParentPath=<%=proParentPath %>&nodePath=<%=nodePath %>" target="_blank" name="serviceDetail"  ><%
		out.print((__name == null?"":"name=" + __name+ "<br>") );	
		out.print("class=" + pro.getClazz() + "<br>");	
		out.print("singlable=" + pro.isSinglable() + "<br>");	
		
		
	 %></a>
	<%
		}
		else if(pro.isRefereced())
		{
		%>
	
	<%
	if(pro.isAttributeRef()) { 
		String refid_ = pro.getRefid();
		Pro tmp_pro = context.getProBean(refid_);
		
	%>
	<a href="<%=tmp_pro !=null && tmp_pro.isBean()?"beanDetail.jsp":"proDetail.jsp"%>?selected=<%=refid_ %>&nodePath=<%=nodePath %>" target="_blank" name="serviceDetail"  ><%
		out.print((__name == null?"":"name=" + __name+ "<br>") );	
	
		out.print("refid=" + pro.getRefid() + "<br>");
		//out.print("引用类型：" );out.print("组件或者属性引用");
		
	 %></a>
	 <%} else if(pro.isServiceRef()) {
	 	String refserviceid = pro.getRefid();
	  %>
	<a href="../managerserviceDetail.jsp?selected=<%=refserviceid %>&nodePath=<%=nodePath %>" target="_blank" name="serviceDetail"  ><%
		//out.print("name=" +(__name == null?key:__name) + "<br>");
		out.print((__name == null?"":"name=" + __name+ "<br>") );	
	
		out.print("refid=" + pro.getRefid() + "<br>");
		//out.print("引用类型：" );out.print("管理服务引用");
		
	 %></a>
	 <%}%>
	 
	<%}
	else
		{
			%>
	<a href="proDetail.jsp?selected=<%=key %>&proParentPath=<%=proParentPath %>&nodePath=<%=nodePath %>" target="_blank" name="serviceDetail"  ><%
		out.print((ismap || __name == null?"":"name=" + __name+ "<br>") );	
		if(pro.getValue() != null)
		{
			if(pro.isList())
				out.print("value=[List set]<br>");
			else if(pro.isMap())
				out.print("value=[Map set]<br>");
			else if(pro.isSet())
				out.print("value=[Set set]<br>");
			else if(pro.isArray())
				out.print("value=[Array set]<br>");
			else 
				out.print("value=" + pro.getValue() + "<br>");		
		}
		
		//out.print("引用类型：" );out.print("组件或者属性引用");
		
	 %></a>
	<%
		}
	 %>
	</td>
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
	 out.print(pro.getDescription() == null?"":pro.getDescription());
	  %></td>
	</tr>
	<% 
	   }
	   out.print("<tr><td colspan='4'>总共配置了" + size + "个属性！</td></tr>");	
	  }else{ 
	%>
	<tr><td colspan="100">
	
	<%
	String key = "";
		if(providerManagerInfo.isBean())
		{
		 
			%>
			<a href="beanDetail.jsp?selected=<%=providerManagerInfo.getName() %>&nodePath=<%=nodePath %>" target="_blank" name="serviceDetail"  >
	<%
		out.print("name=" + providerManagerInfo.getName() + "<br>");		
		out.print("class=" + providerManagerInfo.getClazz() + "<br>");	
		out.print("singlable=" + providerManagerInfo.isSinglable() + "<br>");	
		
		
	 %>
	<%
		}
		else if(providerManagerInfo.isRefereced())
		{
		%>
	
	<%
	if(providerManagerInfo.isAttributeRef()) { 
		String refid_ = providerManagerInfo.getRefid();
		Pro tmp_pro = context.getProBean(refid_);
		
	%>
	<a href="<%=tmp_pro !=null && tmp_pro.isBean()?"beanDetail.jsp":"proDetail.jsp"%>?selected=<%=refid_ %>&nodePath=<%=nodePath %>" target="_blank" name="serviceDetail"  ><%
		out.print("name=" + refid_ + "<br>");
			
		out.print("refid=" + providerManagerInfo.getRefid() + "<br>");
		//out.print("引用类型：" );out.print("组件或者属性引用");
		
	 %></a>
	 <%} else if(providerManagerInfo.isServiceRef()) {
	 	String refserviceid = providerManagerInfo.getRefid();
	  %>
	<a href="../managerserviceDetail.jsp?selected=<%=refserviceid %>&nodePath=<%=nodePath %>" target="_blank" name="serviceDetail"  ><%
		out.print("name=" +refserviceid + "<br>");
			
		out.print("refid=" + providerManagerInfo.getRefid() + "<br>");
		//out.print("引用类型：" );out.print("管理服务引用");
		
	 %></a>
	 <%}%>
	 
	<%}
	
	%></td></tr><%
	} %>
	</table>
	</fieldset>
	
	<fieldset height=100% width="100%">
	<LEGEND align=left><strong>&nbsp;扩展属性&nbsp;</strong></LEGEND>
	<% 
				Map extendattrs = providerManagerInfo.getExtendsAttributes();
				
			%>
			<table class="thin" width="100%">
			
			<tr><td class="headercolor">扩展属性名称</td><td class="headercolor">扩展属性值</td></tr>
			<%
				if(extendattrs != null && extendattrs.size() > 0)
				{
					Iterator its = extendattrs.keySet().iterator();
					while(its.hasNext())
					{
						String namepath = (String)its.next();
			%>
			<tr><td><%=namepath %></td><td><%=extendattrs.get(namepath) %></td></tr>		
			<%
					}
				}else{
			%>
			<tr><td></td></tr>
			<%	
				} 
			%>
			
			</table>
	</fieldset>
	
	<fieldset height=100% width="100%">
	<LEGEND align=left><strong>&nbsp;属性构造参数&nbsp;</strong></LEGEND>
				<% 
					Construction construction = providerManagerInfo.getConstruction();
					if(construction == null)
					{
						%>
						
						<table class="thin" width="100%">
				<tr><td colspan="4">
					
				</td></tr></table>
						<%
						
					}
					else
					{
						List constructionparams = providerManagerInfo.getConstructorParams();
						
					%>
					<table class="thin" width="100%">
					<tr><td colspan="4">
						fieldname-对应的管理服务提供者中的字段名称，必选属性<br>
						refid-引用的管理服务的id，对应manager节点的id属性，必选属性<br>
						
						value-对应字段fieldname的值<br>
					</td></tr>
					<tr>
						<td class="headercolor">字段名称</td>
						<td class="headercolor">字段名称的值</td>
						<td class="headercolor">引用管理服务的id</td>
						<td class="headercolor">管理服务提供者标识</td>
					</tr>
					<% 
					    /**
					    vvvv^^list#!#cccc^^map#!#dddd^^map
	 * vvvv^^list#!#cccc^^map#!#0^^list
					    */
						if(constructionparams != null && constructionparams.size() > 0){
						if(proParentPath == null)
						{
							proParentPath = selected + "^^construction";
						}
						else
						{
							proParentPath = proParentPath +"#!#"+selected + "^^construction";
						}
							for(int i = 0; i < constructionparams.size(); i++){
								Pro pro = (Pro)constructionparams.get(i);
								String key = null;
								String _name = pro.getName();
								key = i + "";
						
			
	%>
	
		<tr>
	<td><%=(_name == null?key:_name) %></td>	
	<td>
	<%
		if(pro.isBean())
		{
			%>
	<a href="beanDetail.jsp?selected=<%=key %>&proParentPath=<%=proParentPath %>&nodePath=<%=nodePath %>" target="_blank" name="serviceDetail"  ><%
		out.print((_name == null?"":"name=" + _name+ "<br>") );		
		out.print("class=" + pro.getClazz() + "<br>");	
		out.print("singlable=" + pro.isSinglable() + "<br>");	
		
		
	 %></a>
	<%
		}
		else if(pro.isRefereced())
		{
		%>
	
	<%
	if(pro.isAttributeRef()) { 
		String refid_ = pro.getRefid();
		Pro tmp_pro = context.getProBean(refid_);
		
	%>
	<a href="<%=tmp_pro !=null && tmp_pro.isBean()?"beanDetail.jsp":"proDetail.jsp"%>?selected=<%=refid_ %>&nodePath=<%=nodePath %>" target="_blank" name="serviceDetail"  ><%
		out.print((_name == null?"":"name=" + _name+ "<br>") );	
		
		out.print("refid=" + pro.getRefid() + "<br>");
		//out.print("引用类型：" );out.print("组件或者属性引用");
		
	 %></a>
	 <%} else if(pro.isServiceRef()) {
	 	String refserviceid = pro.getRefid();
	  %>
	<a href="../managerserviceDetail.jsp?selected=<%=refserviceid %>&nodePath=<%=nodePath %>" target="_blank" name="serviceDetail"  ><%
		out.print((_name == null?"":"name=" + _name+ "<br>") );	
		
	
		out.print("refid=" + pro.getRefid() + "<br>");
		//out.print("引用类型：" );out.print("管理服务引用");
		
	 %></a>
	 <%}%>
	 
	<%}
	else
		{
			%>
	<a href="proDetail.jsp?selected=<%=key %>&proParentPath=<%=proParentPath %>&nodePath=<%=nodePath %>" target="_blank" name="serviceDetail"  ><%
		out.print((_name == null?"":"name=" + _name+ "<br>") );	
		if(pro.getValue() != null)
		{
			if(pro.isList())
				out.print("value=[List set]<br>");
			else if(pro.isMap())
				out.print("value=[Map set]<br>");
			else if(pro.isSet())
				out.print("value=[Set set]<br>");
			else if(pro.isArray())
				out.print("value=[Array set]<br>");
			else 
				out.print("value=" + pro.getValue() + "<br>");		
		}
		if(pro.getClazz() != null)
			out.print("class=" + pro.getClazz() + "<br>");		
		if(pro.getRefid() != null)
			out.print("refid=" + pro.getRefid() + "<br>");
		//out.print("引用类型：" );out.print("组件或者属性引用");
		
	 %></a>
	<%
		}
	 %>
	</td>
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
	  out.print(pro.getDescription() == null?"":pro.getDescription());
	  %></td>
	</tr>
	<% 
	   }
	   out.print("<tr><td colspan='4'>总共配置了" + constructionparams.size() + "个构造函数参数！</td></tr>");	
	  }else{ 
	%>
	<tr><td colspan="2">没有构造函数参数！</td></tr>
	<%} %>
					
					</table>
					<%} %>
				
	</fieldset>
	</div>
	</body>
</html>