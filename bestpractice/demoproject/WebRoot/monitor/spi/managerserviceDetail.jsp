
<%@page import="org.frameworkset.spi.assemble.ProviderInfoQueue"%>
<%@page import="java.util.List,org.frameworkset.spi.assemble.Pro"%><%
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
	//String classType = request.getParameter("classType");
	//管理服务明细信息
	ProviderManagerInfo providerManagerInfo = BaseApplicationContext.getBaseApplicationContext(nodePath).getServiceProviderManager().getProviderManagerInfo(selected) ;
	//管理服务者id
	String mgrid = providerManagerInfo.getId();
	//管理服务者jndi属性，目前未使用
	String mgrjndiname = providerManagerInfo.getJndiName();
	mgrjndiname = mgrjndiname==null?"没有配置":mgrjndiname;
	//是否是单实例模式
	boolean isSinglable = providerManagerInfo.isSinglable();
	//default-是否是缺省管理服务
	boolean isDefault = providerManagerInfo.isDefaultable();
	//interceptor-管理服务器的拦截器
	String interceptor = providerManagerInfo.getTransactionInterceptorClass();
	interceptor = interceptor==null?"没有配置":interceptor;
	
	
	
	
	
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
	
	<body class="contentbodymargin" scroll="no">
	<div style="width:100%;height:100%;overflow:auto">
	<table class="thin" width="100%">
		<tr><td colspan="3" class="headercolor">管理服务属性配置信息</td></tr>
		<tr>
		<td class="headercolor" width="20%">配置属性名</td>
		<td class="headercolor" width="30%">属性对应值</td>
		<td class="headercolor" width="50%">描述</td>
		</tr>
		<tr>
		<td width="20%">id</td><td width="30%"><%=mgrid %></td><td width="50%">id-管理服务者id</td>
		</tr>
		<tr>
		<td width="20%">jndiname</td><td width="30%"><%=mgrjndiname %></td><td width="50%">jndiname-管理服务者jndi属性，目前未使用</td>
		</tr>
		<tr>
		<td width="20%">singlable</td><td width="30%"><%=isSinglable %></td><td width="50%">singlable-是否是单实例模式</td>
		</tr>
		<tr>
		<td width="20%">default</td><td width="30%"><%=isDefault %></td><td width="50%">default-是否是缺省管理服务</td>
		</tr>
		<tr>
		<td width="20%">interceptor</td><td width="20%"><%=interceptor %></td><td width="50%">interceptor-管理服务器的拦截器</td>
		</tr>
	</table>
	<tab:tabContainer id="managerserviceDetail" skin="amethyst">
	
		<tab:tabPane id="provider" tabTitle="服务提供者" lazeload="true" >
			<table class="thin" width="100%">
			<tr><td colspan="3" class="headercolor">服务提供者配置信息</td></tr>
				
			<% 
				//服务提供者
				ProviderInfoQueue providerInfoQueue = providerManagerInfo.getProviderInfoQueue();
				for(int i = 0; i < providerInfoQueue.size(); i++){
					SecurityProviderInfo SecurityProviderInfo = providerInfoQueue.getSecurityProviderInfo(i);
					
					//type-服务提供者标识id
					String type = SecurityProviderInfo.getType();
					//used-服务提供者是否被启用,缺省值为false
					boolean used = SecurityProviderInfo.isUsed();
					//class-服务提供者对应的class类
					String clazz = SecurityProviderInfo.getProviderClass();
					//prior-provider调用的优先级
					int priorProvider = SecurityProviderInfo.getPrior();
					//default-是否是缺省的服务提供者
					boolean isdefault = SecurityProviderInfo.isIsdefault();
			%>
				<tr>
				<td width="100%" colspan="3" height="30"><strong>&lt;provider&gt;服务提供者:<%=type %>的配置信息</strong><br/>
				&lt;provider type=&quot;<%=type %>&quot; default=&quot;<%=isdefault %>&quot; class=&quot;<%=clazz %>&quot; /&gt;
				</td>
				</tr>
				<tr>
				<td class="headercolor" width="20%">配置属性名</td>
				<td class="headercolor" width="30%">属性对应值</td>
				<td class="headercolor" width="50%">描述</td>
				</tr>
				<tr>
				<tr>
				<td width="20%">used</td><td width="20%"><%=used %></td>
				<td width="50%">
				used-服务提供者是否被启用,缺省值为false；
				默认使用的管理服务器可以不配置used属性（只能有一个默认的管理服务器），必须将default属性配置成true
				</td>
				</tr>
				<tr>
				<td width="20%">class</td><td width="20%"><%=clazz %></td><td width="50%">class-服务提供者对应的class类</td>
				</tr>
				<tr>
				<td width="20%">prior-provider</td><td width="20%"><%=priorProvider %></td><td width="50%">prior-provider调用的优先级</td>
				</tr>
				</tr>
				<tr>
				<td width="20%">default</td><td width="20%"><%=isdefault %></td>
				<td width="50%">
					default-是否是缺省的服务提供者
				</td>
				</tr>
			<%
				}
			%>
			
			</table>
		</tab:tabPane>
		
		<tab:tabPane id="synchronize" tabTitle="同步方法" lazeload="true" >
			<table class="thin" width="100%">
			<tr><td colspan="3">是否启用同步机制：<%=providerManagerInfo.isSynchronizedEnabled()%></td></tr>
			<tr><td class="headercolor">方法名</td><td class="headercolor">模式</td><td class="headercolor">描述</td></tr>
			<% 
				List synchronizedMethodLists = providerManagerInfo.getSynchronizedMethodLists();;
				for(int i = 0; synchronizedMethodLists != null && i < synchronizedMethodLists.size(); i++){
					SynchronizedMethod synchronizedMethod = (SynchronizedMethod)synchronizedMethodLists.get(i);
					//enabled-是否启用同步功能，如果没有启用同步功能即使配置了多个服务提供者的同步方法，所有的同步功能将不起作用
					String methodName = synchronizedMethod.getMethodName();
					List mathodParam = synchronizedMethod.getParams();
					String pattern = synchronizedMethod.getPattern();
					if(pattern != null){
			%>
				<tr>
				<td><%=pattern %></td>
				<td>正则表达式</td>
				<td>
				如果涉及的方法名称是一个正则表达式的匹配模式，则无需配置方法参数,<br/>
					模式为* 表示匹配所有的方法。
				模式匹配，模式匹配的顺序受配置位置的影响，如果配置在后面或者中间，那么会先执行之前的方法匹配，如果匹配上了就不会
			对该模式方法进行匹配了，否则执行匹配操作。<br/>
			如果匹配上特定的方法名称，那么这个方法就是需要进行同步的方法<br/>
			例如：模式testInt.*匹配接口中以testInt开头的任何方法，具体的模式定义规则参考JAVA正则表达式的规则</td>
				</tr>
			<%			
					}else{
			%>
				<tr><td><%=methodName %>(
			<% 
						for(int j = 0; mathodParam != null && j < mathodParam.size(); j++){
							Pro param = (Pro)mathodParam.get(j);
							if(j < mathodParam.size() - 1){
								out.print(param.getClazz()+",");
							}else{
								out.print(param.getClazz());
							}		
			%>	
			<%
						}
			%>
				)</td>
				<td>方法全名</td>
				<td>匹配方法</td>
				</tr>
			<%				
					}
				}
			%>
			</table>
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
				NO_TRANSACTION<br>
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
					String txtype = String.valueOf(synchronizedMethod.getTxtype());
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
		
		<tab:tabPane id="reference" tabTitle="引用配置" lazeload="true" >
			<% 
				List referencesList = providerManagerInfo.getReferences();
				
			%>
			<table class="thin" width="100%">
			<tr><td colspan="4">
				fieldname-对应的管理服务提供者中的字段名称，必选属性<br>
				refid-引用的管理服务的id，对应manager节点的id属性，必选属性<br>
				reftype-对应的管理服务提供者标识，可选属性<br>
				value-对应字段fieldname的值<br>
			</td></tr>
			<tr>
				<td class="headercolor">字段名称</td>
				<td class="headercolor">引用字段的值</td>
				<td class="headercolor">对应的管理服务提供者标识</td>
				<td class="headercolor">引用字段类型</td>
				
			</tr>
			<% 
				if(referencesList != null && referencesList.size() > 0){
					for(int rf = 0; rf < referencesList.size(); rf++){
						Pro reference = (Pro)referencesList.get(rf);
						//fieldname-对应的管理服务提供者中的字段名称，必选属性
						String fieldname = reference.getName();
						//refid-引用的管理服务的id，对应manager节点的id属性，必选属性
						String refid = reference.getRefid();
						refid = refid == null?"没有配置":"<a href='managerserviceDetail.jsp?selected="+refid+"' target='_blank'>"+refid+"</a>";
						//reftype-对应的管理服务提供者类型，可选属性
						String reftype = reference.getReftype();
						
						
						String clazz = reference.getClazz();
						reftype = reftype == null?clazz:reftype;
						reftype = reftype == null?"没有配置":reftype;
						//value-对应字段fieldname的值
						Object value = reference.getValue();
						value = value == null?"没有配置":value;
			%>
			<tr>
				<td ><%=fieldname %></td>
				<td ><%=value %></td>
				<td ><%=refid %></td>
				<td ><%=reftype %></td>
			</tr>		
			<%
					}
				}else{
			%>
				<tr><td colspan="4">没有配置引用！</td></tr>
			<%		
				}
			%>
			
			</table>
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
		
	</tab:tabContainer>
	</div>
	</body>
</html>