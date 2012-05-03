<%
/*
 * <p>Title: 监控连接池信息</p>
 * <p>Description: 连接池使用情况</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-9-8
 * @author gao.tang
 * @version 1.0
 */
 %>
<%@ page session="false" contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.common.poolman.DBUtil"%>

<%@page import="java.util.*"%>
<%@page import="com.frameworkset.common.poolman.util.JDBCPoolMetaData"%>

<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>	
		
<%

	String userAccount = "admin";
	
%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>bboss连接池使用情况与配置信息</title>
<%@ include file="/include/css.jsp"%>
		<tab:tabConfig/>	
		<script src="../../inc/js/func.js"></script>
		<script type="text/javascript" language="Javascript">
		function flushBotton(){
			document.location = document.location;
		}
		
		
		</script>
		</head>

	<body class="contentbodymargin" onload="" scroll="no">
	<div style="width:100%;height:100%;overflow:auto">
	<div align="right"><input type="button" class="input" value="刷新页面" onclick="flushBotton()"></div>
	
		
	
	
	<tab:tabContainer id="singleMonitorinfo">
	<% 
		//List poollist = new ArrayList();
		DBUtil dbUtil = new DBUtil();
		Enumeration enum_ = dbUtil.getAllPoolnames();
		while(enum_.hasMoreElements()){
			String poolname = (String)enum_.nextElement();
			JDBCPoolMetaData metadata = DBUtil.getPool(poolname).getJDBCPoolMetadata();
		String title = "数据库："+poolname+" 的链接情况";
	%>
	
	<tab:tabPane id="<%=poolname %>" tabTitle="<%=title%>" >
	
	<form  name="LogForm"  method="post">
	<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="1" class="thin">
					<tr>
					<table>
					<% 
						//是否是外部数据源,false不是，true是
						boolean isExternal = false;
						String exterJNDI = metadata.getExternaljndiName();
						if(exterJNDI != null && !"".equals(exterJNDI)){
							isExternal = true;
						}
					%>
					<tr><td colspan="3">
						<%if(!isExternal){ %>
						数据源：<%=poolname %> 的链接情况
						<%}else{ %>
						外部数据源：<%=poolname %> 的链接情况
						<%} %>
					</td>
					</tr>
					<tr class="tr">
						
						<td width="16%" height="25" class="detailtitle" align="right">空闲连接：</td>
						<td height="25">
						<%=DBUtil.getNumIdle(poolname)%>
						</td>
						</tr>
						
						<tr class="tr">
						<td width="16%" height="25" class="detailtitle" align="right">正在使用连接：</td>
						<td height="25" >
						<%=DBUtil.getNumActive(poolname)%>
						</td>
						</tr>
						
						<tr class="tr">
						<td width="16%" height="25" class="detailtitle" align="right">使用连接高峰值：</td>
						<td height="25" >
						<%=DBUtil.getMaxNumActive(poolname)%>
						</td>
						</tr>
					</table>
					
					
						<tr>
						<table border="1">
						<caption>数据库：<%=poolname %>的配置信息</caption>
						<tr>
						<th>配置属性名</th>
						<th>属性对应值</th>
						<th>缺省值</th>
						<th>描述</th>
						</tr>
						
						<tr>
						<td>dbname</td>
						<td height="25"><%=metadata.getDbname() %></td>
						<td>无缺省值</td>
						<td>数据库名称</td>
						</tr>
						
						<tr>
						<td>driver</td>
						<td height="25"><%=metadata.getDriver() %></td>
						<td>无缺省值</td>
						<td>数据库驱动</td>
						</tr>
						
						<tr>
						<td>url</td>
						<td height="25">
						<%if(userAccount.equals("admin")){ %><%=metadata.getURL() %><%}else{ %>
						******
						<%} %>
						</td>
						<td>无缺省值</td>
						<td>数据库链接地址</td>
						</tr>
						
						<tr>
						<td>jndiName</td>
						<td height="25">
						<%=metadata.getJNDIName() %>
						</td>
						<td>无缺省值</td>
						<td>jndi名称</td>
						</tr>
						
						<tr>
						<td>external</td>
						<td height="25">
						<%=metadata.isExternal() %>
						</td>
						<td>false</td>
						<td>标识数据源是否是外部DataSource，如果是外部DataSource则必须指定外部datasource的jndi名称，
							true是外部DataSource
						</td>
						</tr>
						
						<tr>
						<td>externaljndiName</td>
						<td height="25">
						<%=exterJNDI %>
						</td>
						<td>无缺省值</td>
						<td>外部数据源对应的jndi名称，如果不为null说明对应的就是外部数据源，根据该jndi名称找到对应的真实数据源</td>
						</tr>
						
						
						<tr>
						<td>username</td>
						<td height="25">
						<%if(userAccount.equals("admin")){ %><%=metadata.getUserName() %>
						<%}else{ %>
						******
						<%} %></td>
						<td>无缺省值</td>
						<td>数据库用户名</td>
						</tr>
						
						<tr>
						<td>password</td>
						<td height="25"><%if(userAccount.equals("admin")){ %><%=metadata.getPassword() %><%}else{ %>
						******
						<%} %></td>
						<td>无缺省值</td>
						<td>数据库密码</td>
						</tr>
						
						<tr>
						<td>loadmetadata</td>
						<td height="25"><%=metadata.getLoadmetadata() %></td>
						<td>false</td>
						<td>是否加载数据库源数据</td>
						</tr>
						
						<tr>
						<td>txIsolationLevel</td>
						<td height="25"><%=metadata.getTxIsolationLevel() %></td>
						<td>READ_COMMITTED</td>
						<td>事务分离级别</td>
						</tr>
						
						<tr>
						<td>initialConnections</td>
						<td height="25"><%=metadata.getInitialConnections() %></td>
						<td>1</td>
						<td>初始链接数,缺省为1</td>
						</tr>
						
						<tr>
						<td>minimumSize</td>
						<td height="25"><%=metadata.getMinimumSize() %></td>
						<td>0</td>
						<td>最小空闲链接数,缺省为0，根据配置的不同可改为maximumSize的一半，如果maximumSize为200则minimumSize配为100</td>
						</tr>
						
						<tr>
						<td>maximumSize</td>
						<td height="25"><%=metadata.getMaximumSize() %></td>
						<td>整数的最大值</td>
						<td>允许的最大链接数,缺省值为整数的最大值 </td>
						</tr>
						
						<tr>
						<td>maximumSoft</td>
						<td height="25"><%=metadata.isMaximumSoft() %></td>
						<td>false</td>
						<td>控制connection达到maximumSize是否允许再创建新的connection——true：允许，缺省值 ;false：不允许 </td>
						</tr>
						
						<tr>
						<td>removeAbandoned</td>
						<td height="25"><%=metadata.getRemoveAbandoned() %></td>
						<td>false</td>
						<td>是否检测超时链接（事务超时链接）
    						true-检测，如果检测到有事务超时的链接，系统将强制回收（释放）该链接;
    						false-不检测，默认值
    					</td>
						</tr>
						
						<tr>
						<td>userTimeout</td>
						<td height="25"><%=metadata.getUserTimeout() %></td>
						<td>60 秒</td>
						<td>链接使用超时时间（事务超时时间）  单位：秒
    					</td>
						</tr>
						
						<tr>
						<td>logAbandoned</td>
						<td height="25"><%=metadata.isLogAbandoned() %></td>
						<td>true</td>
						<td>系统强制回收链接时，是否输出后台日志————true-输出，默认值;false-不输出
    					</td>
						</tr>
						
						<tr>
						<td>readOnly</td>
						<td height="25"><%=metadata.isReadOnly() %></td>
						<td>true</td>
						<td>数据库会话是否是readonly，缺省为true
    					</td>
						</tr>
						
						<tr>
						<td>skimmerFrequency</td>
						<td height="25"><%=metadata.getSkimmerFrequency() %></td>
						<td>60 秒</td>
						<td>回收空闲链接操作间隔时间,秒（s）为单位，缺省为60秒</td>
						</tr>
						
						
						<tr>
						<td>connectionTimeout</td>
						<td height="25"><%=metadata.getConnectionTimeout() %></td>
						<td>1200 秒</td>
						<td>
						单位：秒;
						空闲链接回收时间，空闲时间超过指定的值时，将被回收;缺省为1200秒</td>
						</tr>
						
						<tr>
						<td>shrinkBy</td>
						<td height="25"><%=metadata.getShrinkBy() %></td>
						<td>5</td>
						<td>每次回收的链接数,回收进程每次最多回收的空闲链接数，缺省值是5</td>
						</tr>
						
						<tr>
						<td>testWhileidle</td>
						<td height="25"><%=metadata.isTestWhileidle() %></td>
						<td>false</td>
						<td> 检测空闲链接处理时，是否对空闲链接进行有效性检查控制开关——
      						 true-检查，都检查到有无效链接时，直接销毁无效链接；
     						false-不检查，缺省值</td>
						</tr>
						
						
						<tr>
						<td>keygenerate</td>
						<td height="25"><%=metadata.getKeygenerate() %></td>
						<td>auto</td>
						<td>定义数据库主键生成机制
        缺省的采用系统自带的主键生成机制，
        外步程序可以覆盖系统主键生成机制
        由值来决定——
        auto:自动，一般在生产环境下采用该种模式，
               解决了单个应用并发访问数据库添加记录产生冲突的问题，效率高，如果生产环境下有多个应用并发访问同一数据库时，必须采用composite模式;
        composite：结合自动和实时从数据库中获取最大的主键值两种方式来处理，开发环境下建议采用该种模式，
                   解决了多个应用同时访问数据库添加记录时产生冲突的问题，效率相对较低， 如果生产环境下有多个应用并发访问同一数据库时，必须采用composite模式</td>
						</tr>
						
						<tr>
						<td>maxWait</td>
						<td height="25"><%=metadata.getMaxWait() %>&nbsp;秒</td>
						<td>30 秒</td>
						<td>获取链接等待超时时间,单位：秒，缺省等待时间30秒。当应用程序请求链接时，链接池中所有的链接都处于使用状态，并且已经达到最大连接数时，该请求就会处于等待状态，如果等待的时间超过30秒（maxWait配置的值）时，系统将抛出请求链接超时异常。</td>
						</tr>
						
						<tr>
						<td>validationQuery</td>
						<td height="25"><%=metadata.getValidationQuery() %></td>
						<td>无缺省值</td>
						<td>校验sql语句。应用程序从链接池申请链接时，连接池对申请的链接执行校验sql语句，如果执行成功，则标识该链接是有效的，直接返回给应用程序，否则将该连接直接从链接池中销毁，并且重新到池中获取新的链接
						如此反复执行，直接获取到有效的连接为止
						</td>
						</tr>
						
						<tr>
						<td>poolingPreparedStatements</td>
						<td height="25"><%=metadata.isPoolPreparedStatements() %></td>
						<td>false</td>
						<td>预编译statement池化标记--true：池化;false：不池化，默认值
						</td>
						</tr>
						
						<tr>
						<td>maxOpenPreparedStatements</td>
						<td height="25"><%=metadata.getMaxOpenPreparedStatements() %></td>
						<td>-1</td>
						<td>每个connection最大打开的预编译statements数,默认值为-1
						</td>
						</tr>
						
					
						
						<tr>
						<td>showsql</td>
						<td height="25"><%=metadata.isShowsql() %></td>
						<td>false</td>
						<td>是否在后台输出执行的sql语句，true输出执行的sql语句
						</td>
						</tr>
						
						
						
						<tr>
						<td>enablejta</td>
						<td height="25"><%=metadata.isEnablejta() %></td>
						<td>false</td>
						<td>是否启用jta datasource，如果启用将在jndi context中注册一个
						  TXDatasource
						  jta datasource的jndiname为 jndiName属性指定的值
						  默认为不启用，该属性在托管第三方数据源时有用
						  当enablejta == true时，必须在poolman.xml文件中指定jndiName属性
						</td>
						</tr>
						<tr>
						<td>encryptdbinfo</td>
						<td height="25"><%=metadata.isEncryptdbinfo() %></td>
						<td>false</td>
						<td>是否加密数据库信息，包括url，useraccount,password,提供以下插件：
						
						#DESDBInfoEncrypt-采用des算法对数据库url，账号，密码进行加密和解密操作
						#DBInfoEncryptclass=com.frameworkset.common.poolman.security.DESDBInfoEncrypt
						
						#DESDBPasswordEncrypt-采用des算法对数据库密码进行加密和解密操作
						DBInfoEncryptclass=com.frameworkset.common.poolman.security.DESDBPasswordEncrypt
						
						#DESDBUserEncrypt-采用des算法对数据库用户名进行加密和解密操作
						#DBInfoEncryptclass=com.frameworkset.common.poolman.security.DESDBUserEncrypt
						
						#DESDBUrlEncrypt-采用des算法对数据库url进行加密和解密操作
						#DBInfoEncryptclass=com.frameworkset.common.poolman.security.DESDBUrlEncrypt
						#DESDBUserAndPasswordEncrypt-采用des算法对数据库用户名/口令进行加密和解密操作
						#DBInfoEncryptclass=com.frameworkset.common.poolman.security.DESDBUserAndPasswordEncrypt
						
						对应的配置在bboss-aop.jar/aop.properties文件中，你只需要放开需要的加密插件，关闭另外的插件即可，数据库信息的加密请使用如下方法：
						com.frameworkset.common.poolman.security.DESCipher aa = new com.frameworkset.common.poolman.security.DESCipher();
						String password = aa.encrypt("123456");		
						String user = aa.encrypt("root");		
						String url = aa.encrypt("jdbc:mysql://localhost:3306/cim");
						</td>
						</tr>
						
						</table>
						</tr>
						
						
						<tr>
						<td>
						*********************************************************************************
						</td>
						
						</tr>
						
			
			  </table>
			  </form>
			  
			  </tab:tabPane>
			  <% 
				}	
			%>
	
	</tab:tabContainer>

	</div>
				</body>
</html>