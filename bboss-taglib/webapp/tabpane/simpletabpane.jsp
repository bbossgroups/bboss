<%@page contentType="text/html; charset=UTF-8"%>
<!-- 
	抽屉标签演示demo，简单易懂
	
	导入标签定义文件
 -->
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<!-- 设置tabpane的相关配置文件，包括js和样式 -->
		<tab:tabConfig />
		<title></title>

		<script language="javascript">
	
</script>
	</head>
	<body bgcolor="#F7F8FC">

		<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" align="center">
			
			<tr>
				<td colspan="2" align="left">
				<!-- tabpane container，作为抽屉标签tabpane的容器，需要注意：
					id 属性的值必须保证应用全局唯一性，尤其是在启用cookie的时候，
					selectedTabPaneId 属性用来设置缺省选中的抽屉标签tabpane，其值为对应tabpane的id的属性值
					enablecookie 设置是否在cookie中记录上次选中的tabpane面板，记录之后，下次再进入该页面时默认选中的抽屉标签就是
					             cookie中记录的tabpane，如果允许记录cookie，那么container 标签的id属性必须保持全局唯一性，否则就会相互干扰。
					             缺省为false
					jsTabListener 指定tabpane面板切换监听js事件，当切换抽屉面板时，就会执行指定的js事件函数
					skin 设置抽屉标签的外观皮换，系统缺省提供了以下几种皮肤供选择：
						wireframe
						invisible
						default
						bluesky
						grassgreen
						amethyst
						bluegreen
						
						顾名思义，default即为缺省的外观主题
				 -->
					<tab:tabContainer id="simple-test-container" enablecookie="false" selectedTabPaneId="rule-config">
					<!--  
						具体的抽屉标签，
						id 抽屉的属性，保持同一个容器内全局唯一
						tabTitle  抽屉的标题
						lazeload 如果抽屉中内置了iframe标签，标识是否延迟到第一次访问该面板时才加载iframe中的页面
					-->
						<tab:tabPane id="mq-server-config" tabTitle="MQ服务器" lazeload="true">
						<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" align="center">
							<tr>
								<td colspan="2" align="left">
								<!--  
									iframe标签，
									通过iframe标签来生成tabpane中内置的iframe框架，这样做的好处就可以实现iframe框架的延迟加载功能，所谓
									延迟加载，就是当第一次进入相应的tabpane面板时才加载iframe框架对应的页面
								-->
								<tab:iframe  id="role-iframe" src="testiframe.jsp"  frameborder="0"
									 width="98%" height="98%"></tab:iframe>
								</td>
							</tr>
						</table>
						</tab:tabPane>

						<tab:tabPane id="mirror-info" tabTitle="镜像信息" lazeload="true">
						<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" align="center">			
							<tr>
								<td colspan="2" align="left">
											镜像信息	
								</td>
							</tr>
						</table>
						</tab:tabPane>

						<tab:tabPane id="mem-config" tabTitle="内存配置*" lazeload="true">
							<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" align="center">			
							<tr>
								<td colspan="2" align="left">
											内存配置*
								</td>
							</tr>
						</table>
						</tab:tabPane>
						
						<tab:tabPane id="connector-config" tabTitle="连接配置*" lazeload="true">
							<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" align="center">			
							<tr>
								<td colspan="2" align="left">
											连接配置*
								</td>
							</tr>
						</table>
						</tab:tabPane>
						
						<tab:tabPane id="user-config" tabTitle="用户配置" lazeload="true">
							<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" align="center">			
							<tr>
								<td colspan="2" align="left">
											用户配置
								</td>
							</tr>
						</table>
						</tab:tabPane>
						
						<tab:tabPane id="rule-config" tabTitle="路由规则配置" lazeload="true">
							<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" align="center">			
							<tr>
								<td colspan="2" align="left">
											路由规则配置
								</td>
							</tr>
						</table>
						</tab:tabPane>
											
						<tab:tabPane id="ssl-config" tabTitle="ssl配置" lazeload="true">
							<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" align="center">			
							<tr>
								<td colspan="2" align="left">
											ssl配置
								</td>
							</tr>
						</table>
						</tab:tabPane>
					</tab:tabContainer>
				</td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
			
			<tr>
				<td colspan="2" align="left">
					<!--  
						抽屉面板选择链接，通过selectedTabPaneId属性指定需要链接进入的抽屉面板
						可选的href属性用来指定自定义的连接地址url
					-->
					<tab:tabLink selectedTabPaneId="ssl-config">进入ssl配置</tab:tabLink>
				</td>
			</tr>
			
			<tr>
				<td align="left">
					<!--  
						抽屉面板选择导航器，可以通过这两个标签页来实现抽屉面板的上下导航
						tabContainerId 用来指定在抽屉容器tabContainerId中的抽屉之间进行导航
					-->
					<tab:prevTabButton tabContainerId="simple-test-container">上一个</tab:prevTabButton>
					<tab:nextTabButton tabContainerId="simple-test-container">下一个</tab:nextTabButton>
				</td>
			</tr>
		</table>
	</body>
</html>
