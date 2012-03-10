
<%@page import="org.frameworkset.spi.assemble.ProviderInfoQueue"%>
<%@page import="java.util.List,org.frameworkset.spi.assemble.Pro"%>
<%@page import="java.lang.reflect.Method"%>
<%@page import="org.frameworkset.web.servlet.context.WebApplicationContext"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%><%@page import="java.net.URLEncoder"%><%
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
<%@page import="org.frameworkset.spi.assemble.*,org.frameworkset.spi.BaseApplicationContext"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>		

<% 
	String rootpath = request.getContextPath();
	String selected = request.getParameter("selected");
	String nodePath = request.getParameter("nodePath");
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
	String name = proParentPath == null ?selected:proParentPath + "[" + selected + "]";
	String pro_name = providerManagerInfo.getName() == null?"":providerManagerInfo.getName() ;
	
	//是否是单实例模式
	boolean isSinglable = providerManagerInfo.isSinglable();
	
	//interceptor-管理服务器的拦截器
	String interceptor = providerManagerInfo.getTransactionInterceptorClass();
	interceptor = interceptor==null?"没有配置":interceptor;
	
	String beanclazz = providerManagerInfo.getClazz() == null?"":providerManagerInfo.getClazz();
	String factory_bean = providerManagerInfo.getFactory_bean() == null?"":providerManagerInfo.getFactory_bean();
	String factory_clazz = providerManagerInfo.getFactory_class() == null?"":providerManagerInfo.getFactory_class();
	String factory_method = providerManagerInfo.getFactory_method() == null?"":providerManagerInfo.getFactory_method();
	String init_method = providerManagerInfo.getInitMethod() == null?"":providerManagerInfo.getInitMethod();
	String destroy_method = providerManagerInfo.getDestroyMethod() == null?"":providerManagerInfo.getDestroyMethod();
	
	
	
	
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><%=pro_name %></title>
		<link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/treeview.css">
<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/tab.winclassic.css">
		<tab:tabConfig/>	
	</head>
	
	<body class="contentbodymargin" scroll="no">
	<div style="width:100%;height:100%;overflow:auto">
	<table class="thin" width="100%">
		<tr><td colspan="3" class="headercolor">业务组件所属容器信息</td></tr>
		<tr>
		<td class="headercolor" width="20%">容器标识</td>
		<td class="headercolor" width="30%">容器类型</td>
		<td class="headercolor" width="50%">组件所属配置文件</td>
		</tr>
		<tr>
		<td width="20%"><%=!isWebApplicationContext?nodePath:nodePath+"("+context.getConfigfile()+")" %></td>
		<td width="20%"><%=context.getClass().getCanonicalName() %></td>
		<td width="20%"><%=providerManagerInfo.getConfigFile() %></td>
		</tr>
	</table>
	<table class="thin" width="100%">
		<tr><td colspan="3" class="headercolor">业务组件属性配置信息</td></tr>
		<tr>
		<td class="headercolor" width="20%">配置属性名</td>
		<td class="headercolor" width="30%">属性对应值</td>
		
		<td class="headercolor" width="50%">描述</td>
		</tr>
		<tr>
		<td width="20%">name</td><td width="30%"><%=pro_name %></td><td width="50%">name-组件名称/标识</td>
		</tr>
		
		<tr>
		<td width="20%">组件定义路径</td><td width="20%"><%=name %></td><td width="50%">组件定义路径信息</td>
		
		</tr>
		
		<tr>
		<td width="20%">singlable</td><td width="30%"><%=isSinglable %></td><td width="50%">singlable-是否是单实例模式</td>
		</tr>
		
		<tr>
		<td width="20%">interceptor</td><td width="20%"><%=interceptor %></td><td width="50%">interceptor-组件的拦截器</td>
		
		</tr>
		
		<tr>
		<td width="20%">class</td><td width="20%"><%=beanclazz %></td><td width="50%">class-组件实现类</td>
		
		</tr>
		
		<tr>
		<td width="20%">factory_bean</td><td width="20%"><%
		if(factory_bean != null)
			{
				if(isWebApplicationContext)
				{
					factory_bean = factory_bean == ""?"":"<a href='beanDetail.jsp?isWebApplicationContext=true&selected="+factory_bean+"&nodePath="+nodePath+"' target='_blank'>"+factory_bean+"</a>";
				}
				else
				{
					factory_bean = factory_bean == ""?"":"<a href='beanDetail.jsp?isWebApplicationContext=false&selected="+factory_bean+"&nodePath="+nodePath+"' target='_blank'>"+factory_bean+"</a>";
				}
				out.print(factory_bean); 
								
			}%></td><td width="50%">factory_bean-组件实现工厂创建类</td>
		
		</tr>
		<tr>
		<td width="20%">factory_clazz</td><td width="20%"><%=factory_clazz %></td><td width="50%">factory_clazz-组件实现工厂创建类</td>
		
		</tr>
		<tr>
		<td width="20%">factory_method</td><td width="20%"><%=factory_method %></td><td width="50%">factory_method-组件实现工厂创建类</td>
		
		</tr>
		
		<tr>
		<td width="20%">init_method</td><td width="20%"><%=init_method %></td><td width="50%">init_method-组件实例被创建时，会调用的方法</td>
		
		</tr>
		
		<tr>
		<td width="20%">destroy_method</td><td width="20%"><%=destroy_method %></td><td width="50%">destroy_method-组件实例被销毁时，会调用的方法</td>
		
		</tr>
	</table>
	<tab:tabContainer id="beanifoDetail" skin="amethyst">
		<tab:tabPane id="reference" tabTitle="属性注入" lazeload="true" >
			<% 
				List referencesList = providerManagerInfo.getReferences();
				
			%>
			<table class="thin" width="100%">
			<tr><td colspan="4">
				
				refid-引用的管理服务的id，对应manager节点的id属性，必选属性<br>
				
				value-对应字段fieldname的值<br>
			</td></tr>
			<tr>
				<td class="headercolor">字段名称</td>
				<td class="headercolor">字段名称的值</td>
				<td class="headercolor">配置类型</td>
				<td class="headercolor">描述</td>
			</tr>
			<% 
			    
				if(referencesList != null && referencesList.size() > 0){
					String refproParentPath = proParentPath;
					if(refproParentPath == null)
					{
						refproParentPath = selected + "^^reference";
					}
					else
					{
						refproParentPath = refproParentPath +"#!#"+selected + "^^reference";
					}
					if(refproParentPath != null && !refproParentPath.equals(""))
		{
			refproParentPath = URLEncoder.encode(refproParentPath);
		}
					for(int i = 0; i < referencesList.size(); i++){
						Pro pro = (Pro)referencesList.get(i);
						
						String key = null;
						String _name = pro.getName();
						
						key = i + "";
	%>
	
		<tr>
	<td><%=key %></td>	
	<td>
	<%
		if(pro.isBean())
		{
			%>
	<a href="beanDetail.jsp?selected=<%=key %>&proParentPath=<%=refproParentPath %>&nodePath=<%=nodePath %>" target="_blank" name="serviceDetail"  ><%
		out.print("name=" + (_name == null?key:_name) + "<br>");		
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
		out.print("name=" + (_name == null?key:_name) + "<br>");
		
		out.print("refid=" + pro.getRefid() + "<br>");
		
		
	 %></a>
	 <%} else if(pro.isServiceRef()) {
	 	String refserviceid = pro.getRefid();
	  %>
	<a href="../managerserviceDetail.jsp?selected=<%=refserviceid %>&nodePath=<%=nodePath %>" target="_blank" name="serviceDetail"  ><%
		out.print("name=" +(_name == null?key:_name) + "<br>");
		
		out.print("refid=" + pro.getRefid() + "<br>");
		
		
	 %></a>
	 <%}%>
	 
	<%}
	else
		{
			%>
	<a href="proDetail.jsp?selected=<%=key %>&proParentPath=<%=refproParentPath %>&nodePath=<%=nodePath %>" target="_blank" name="serviceDetail"  ><%
		out.print("name=" + (_name == null?key:_name) + "<br>");
		if(pro.getValue() != null)
		{
			if(pro.isList())
				out.print("value=[List set]<br>");
			else if(pro.isMap())
				out.print("value=<Map set><br>");
			else if(pro.isSet())
				out.print("value=<Set set><br>");
			else if(pro.isArray())
				out.print("value=<Array set><br>");
			else 
				out.print("value=" + pro.getValue() + "<br>");		
		}
		if(pro.getClazz() != null)
			out.print("class=" + pro.getClazz() + "<br>");		
		if(pro.getRefid() != null)
			out.print("refid=" + pro.getRefid() + "<br>");
		
		
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
	   out.print("<tr><td colspan='4'>总共配置了" + referencesList.size() + "个属性注入参数！</td></tr>");	
	  }else{ 
	%>
	<tr><td colspan="4">没有属性注入参数！</td></tr>
	<%} %>
					
					</table>
					
		</tab:tabPane>
		<tab:tabPane id="construction" tabTitle="构造函数配置" >
				<% 
					Construction construction = providerManagerInfo.getConstruction();
					if(construction == null)
					{
						%>
						
						<table class="thin" width="100%">
				<tr><td colspan="4">
					没有定义构造函数
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
						value-对应字段fieldname的值<br>
					</td></tr>
					<tr>
						<td class="headercolor">字段名称</td>
						<td class="headercolor">字段名称的值</td>
						<td class="headercolor">类型</td>
						<td class="headercolor">描述</td>
					</tr>
					<% 
					    
						if(constructionparams != null && constructionparams.size() > 0){
							String constructproParentPath = proParentPath;
							if(constructproParentPath == null)
							{
								constructproParentPath = selected + "^^construction";
							}
							else
							{
								constructproParentPath = constructproParentPath +"#!#"+selected + "^^construction";
							}
							if(constructproParentPath != null && !constructproParentPath.equals(""))
		{
			constructproParentPath = URLEncoder.encode(constructproParentPath);
		}
							for(int i = 0; i < constructionparams.size(); i++){
								Pro pro = (Pro)constructionparams.get(i);
								String key = null;
								String _name = pro.getName();
								key = i + "";
						
			
	%>
	
		<tr>
	<td><%=key %></td>	
	<td>
	<%
		if(pro.isBean())
		{
			%>
	<a href="beanDetail.jsp?selected=<%=key %>&proParentPath=<%=constructproParentPath %>&nodePath=<%=nodePath %>" target="_blank" name="serviceDetail"  ><%
		out.print("name=" + (_name == null?key:_name) + "<br>");		
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
		out.print("name=" + (_name == null?key:_name) + "<br>");
		
		out.print("refid=" + pro.getRefid() + "<br>");
		//out.print("引用类型：" );out.print("组件或者属性引用");
		
	 %></a>
	 <%} else if(pro.isServiceRef()) {
	 	String refserviceid = pro.getRefid();
	  %>
	<a href="../managerserviceDetail.jsp?selected=<%=refserviceid %>&nodePath=<%=nodePath %>" target="_blank" name="serviceDetail"  ><%
		out.print("name=" +(_name == null?key:_name) + "<br>");
		
		out.print("refid=" + pro.getRefid() + "<br>");
		//out.print("引用类型：" );out.print("管理服务引用");
		
	 %></a>
	 <%}%>
	 
	<%}
	else
		{
			String url = "proDetail.jsp?selected="+key +"&proParentPath="+constructproParentPath +"&nodePath="+nodePath;
			if(beanclazz != null && beanclazz.equals("com.frameworkset.common.poolman.ConfigSQLExecutor") && pro != null)
			{
				url = "sqlconfigfileDetail.jsp?selected=sql:"+ pro.getValue() +"&classType=sqlapplicationmodule&nodePath="+nodePath;
			}
			%>
	<a href="<%=url%>" target="_blank" name="serviceDetail"  ><%
		out.print("name=" + (_name == null?key:_name) + "<br>");
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
			</tab:tabPane>
		
		
		
		
		
		
		
		<tab:tabPane id="transactions" tabTitle="事务方法" lazeload="true" >
		<% 
			List transactionMethodsList = providerManagerInfo.getTransactionMethods();
		%>
			<table class="thin" width="100%">
			<tr>
			<td colspan="4">
				定义需要进行事务控制的方法<br>
				属性说明：<br>
				name-方法名称，可以是一个正则表达式，正则表达式的语法请参考jakarta-oro的相关文档，如果使用<br>
				正则表达式的情况时，则方法中声明的方法参数将被忽略，但是回滚异常有效。<br>
				pattern-方法名称的正则表达式匹配模式，模式匹配的顺序受配置位置的影响，如果配置在后面或者中间，<br>
						那么会先执行之前的方法匹配，如果匹配上了就不会对该模式方法进行匹配了，否则执行匹配操作。<br>
						如果匹配上特定的方法名称，那么这个方法就是需要进行事务控制的方法<br>
						例如：模式testInt.*匹配接口中以testInt开头的任何方法<br>
				txtype-需要控制的事务类型，取值范围：<br>
				NEW_TRANSACTION，<br>
				REQUIRED_TRANSACTION，<br>
				MAYBE_TRANSACTION，<br>
				NO_TRANSACTION，<br>
				RW_TRANSACTION
			</td>
			</tr>
			<%if(transactionMethodsList == null || transactionMethodsList.size() == 0){ %>
			<tr><td>没有定义需要进行事务控制的方法！</td></tr>
			<%} %>
			</table>
			
			<% 
				
				for(int i = 0; transactionMethodsList != null && i < transactionMethodsList.size(); i++){
					SynchronizedMethod synchronizedMethod = (SynchronizedMethod)transactionMethodsList.get(i);
					//方法名称，name和pattern不能同时出现
					String methodName = synchronizedMethod.getMethodName();
					//pattern-匹配方法名称的正则表达式
					String pattern = synchronizedMethod.getPattern();
					//txtype-需要控制的事务类型，取值范围：NEW_TRANSACTION，REQUIRED_TRANSACTION，MAYBE_TRANSACTION，NO_TRANSACTION
					String txtype = synchronizedMethod.getTxtype() == null?"": synchronizedMethod.getTxtype().toString();
					//回滚异常
					List rollbackExceptionsList = synchronizedMethod.getRollbackExceptions();
					//参数列表
					List paramList = synchronizedMethod.getParams();
			%>
			<table class="thin" width="100%">
			<tr>
			<td class="bginputcolor" height="30" colspan="4"><strong>&lt;method&gt;
			<%
				if(pattern != null){
					out.print(pattern);
				}else{
					out.print(methodName);
				}
				out.print("(");
				for(int txt = 0; paramList != null && txt < paramList.size(); txt ++){
					Pro paramTxt = (Pro)paramList.get(txt);
					if(txt < paramList.size() - 1){
						out.print(paramTxt.getClazz() + ",");
					}else{
						out.print(paramTxt.getClazz());
					}
				}
				out.print(")");
				if(pattern != null && paramList != null){
					out.print("<br>正则表达式匹配的方法不需要配置参数！");
				} 
			%>
			</strong></td>
			</tr>
			<tr>
			<td width="200">事务类型</td><td colspan="3"><%=txtype %></td>
			</tr>
			<tr>
			<td width="200" rowspan="<%=rollbackExceptionsList.size()+1 %>">事务回滚异常</td>
			<td class="headercolor">异常类</td><td class="headercolor" >异常检测范围</td><td class="headercolor" >描述</td>
			</tr>
			<%if(rollbackExceptionsList != null && rollbackExceptionsList.size() > 0) {
				for(int ep = 0; ep < rollbackExceptionsList.size(); ep++){
					RollbackException rollbackException = (RollbackException)rollbackExceptionsList.get(ep);
					//异常类
					String epClass = rollbackException.getExceptionName();
					//INSTANCEOF = 1; IMPLEMENTS = 0;
					String type = "INSTANCEOF";
					if(rollbackException.getExceptionType() == 0){
						type = "IMPLEMENTS";
					}
			%>
			<tr>
			<td ><%=epClass %></td><td ><%=type %></td>
			<td>
			<%
				if(type.equals("IMPLEMENTS")){
					out.print("IMPLEMENTS只检测异常类本身，忽略异常类的子类");
				}
				else if(type.equals("INSTANCEOF")){
					out.print("INSTANCEOF检查异常类本身及其所有子类");
				} 
				
			%>
			</td>
			</tr>
			<%}
			}else{ %>
			<tr>
			<td colspan="4" align="center">没有配置回滚异常</td>
			</tr>
			<%} %>
			</table>
			<br>
			<%
				}
			%>
			
		</tab:tabPane>
		
		
		
		<tab:tabPane id="interceptor" tabTitle="拦截器" lazeload="true" >
			<% 
				List interceptorsList = providerManagerInfo.getInterceptors();
				
			%>
			<table class="thin" width="100%">
			<tr><td>
				class-拦截器的实现类，所有的拦截器都必须实现<br>
		      	com.frameworkset.proxy.Interceptor接口<br>
		      	目前系统中提供了以下缺省拦截器：<br>
		      	数据库事务管理拦截器（org.frameworkset.spi.<br>
		      	interceptor.TransactionInterceptor）,支持对声明式事务的管理<br>
			</td></tr>
			<tr><td class="headercolor">拦截器类</td></tr>
			<%
				if(interceptorsList != null && interceptorsList.size() > 0){
					for(int ip = 0; ip < interceptorsList.size(); ip ++){
						InterceptorInfo interceptorInfo = (InterceptorInfo)interceptorsList.get(ip);
						String clazz = interceptorInfo.getClazz();
			%>
			<tr><td><%=clazz %></td></tr>		
			<%
					}
				}else{
			%>
			<tr><td>没有配置拦截器！</td></tr>
			<%	
				} 
			%>
			
			</table>
		</tab:tabPane>
		
		<tab:tabPane id="mvcpaths" tabTitle="mvc路径映射" lazeload="true" >
			<% 
				Map mvcpaths = providerManagerInfo.getMvcpaths();
				
			%>
			<table class="thin" width="100%">
			
			<tr><td class="headercolor">映射名称</td><td class="headercolor">映射路径</td></tr>
			<%
				if(mvcpaths != null && mvcpaths.size() > 0)
				{
					Iterator its = mvcpaths.keySet().iterator();
					while(its.hasNext())
					{
						String namepath = (String)its.next();
			%>
			<tr><td><%=namepath %></td><td><%=mvcpaths.get(namepath) %></td></tr>		
			<%
					}
				}else{
			%>
			<tr><td></td></tr>
			<%	
				} 
			%>
			
			</table>
		</tab:tabPane>
		
		<tab:tabPane id="extendattrs" tabTitle="扩展属性" lazeload="true" >
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
		</tab:tabPane>
		
		<tab:tabPane id="webservices" tabTitle="webservice属性" lazeload="true" >
			<% 
				Map webservices = providerManagerInfo.getWSAttributes();
				
			%>
			<table class="thin" width="100%">
			
			<tr><td class="headercolor">webservice属性名称</td><td class="headercolor">webservice属性值</td></tr>
			<%
				if(webservices != null && webservices.size() > 0)
				{
					Iterator its = webservices.keySet().iterator();
					while(its.hasNext())
					{
						String namepath = (String)its.next();
			%>
			<tr><td><%=namepath %></td><td><a href="<%=request.getContextPath()%>/cxfservices/<%=webservices.get(namepath) %>?wsdl" target="webservice"><%=webservices.get(namepath) %></a></td></tr>		
			<%
					}
				}else{
			%>
			<tr><td></td></tr>
			<%	
				} 
			%>
			
			</table>
		</tab:tabPane>
		
		<tab:tabPane id="rmiattrs" tabTitle="rmi配置属性" >
		<% 
				Map rmiattrs = providerManagerInfo.getRMIAttributes();
				
			%>
			<table class="thin" width="100%">
			
			<tr><td class="headercolor">rmi服务属性名称</td><td class="headercolor">rmi服务属性值</td></tr>
			<%
				if(rmiattrs != null && rmiattrs.size() > 0)
				{
					Iterator its = rmiattrs.keySet().iterator();
					while(its.hasNext())
					{
						String namepath = (String)its.next();
			%>
			<tr><td><%=namepath %></td><td><%=rmiattrs.get(namepath) %></td></tr>		
			<%
					}
				}else{
			%>
			<tr><td></td></tr>
			<%	
				} 
			%>
			
			</table>
		</tab:tabPane>
		
		
	</tab:tabContainer>
	
	
		
	</div>
	</body>
</html>