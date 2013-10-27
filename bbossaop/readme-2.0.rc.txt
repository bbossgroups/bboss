directory:
src--source code
test--test source code
sql-- db sql
lib--bboss-aop framework depends jars
distrib--bboss-aop release package.

---------------------------------
bbossaop相关联工程：
---------------------------------

bbossaop<-bbossevent 
[
	bboss-aop.jar,bboss-camel.jar,bboss-mina.jar,bboss-jms.jar,bboss-ws.jar,jgroups.jar
	
	org/frameworkset/spi/manager-rpc-jms.xml
	org/frameworkset/spi/manager-rpc-mina.xml
	org/frameworkset/spi/manager-rpc-service.xml
	org/frameworkset/spi/manager-rpc-webservices.xml
]
bbossaop<-cms_baseline 
[
	bboss-aop.jar,bboss-camel.jar,bboss-mina.jar,bboss-jms.jar,bboss-ws.jar,bboss-schedule.jar
	
	org/frameworkset/spi/manager-rpc-jms.xml
	org/frameworkset/spi/manager-rpc-mina.xml
	org/frameworkset/spi/manager-rpc-service.xml
	org/frameworkset/spi/manager-rpc-webservices.xml
]
bbossaop<-bboss-ws 
[
	bboss-aop.jar,bboss-camel.jar,bboss-mina.jar,bboss-jms.jar,bboss-ws.jar
	
	org/frameworkset/spi/manager-rpc-jms.xml
	org/frameworkset/spi/manager-rpc-mina.xml
	org/frameworkset/spi/manager-rpc-service.xml
	org/frameworkset/spi/manager-rpc-webservices.xml
]
bbossaop<-active-ext 
[
	bboss-aop.jar,bboss-camel.jar,bboss-mina.jar,bboss-jms.jar,bboss-ws.jar	
	
	org/frameworkset/spi/manager-rpc-jms.xml
	org/frameworkset/spi/manager-rpc-mina.xml
	org/frameworkset/spi/manager-rpc-service.xml
	org/frameworkset/spi/manager-rpc-webservices.xml
]
bbossaop<-bboss-taglib [bboss-aop.jar]
bbossaop<-bboss-util [bboss-aop.jar]
bbossaop<-kettle 
[
	bboss-aop.jar,bboss-camel.jar,bboss-mina.jar,bboss-jms.jar,bboss-ws.jar
		
	org/frameworkset/spi/manager-rpc-jms.xml
	org/frameworkset/spi/manager-rpc-mina.xml
	org/frameworkset/spi/manager-rpc-service.xml
	org/frameworkset/spi/manager-rpc-webservices.xml
]
bbossaop<-portal [bboss-aop.jar]
bbossaop<-cas server [bboss-aop.jar]

bbossaop->bboss-persistent [frameworkset-pool.jar]
bbossaop->bboss-taglib [frameworkset.jar] 监控树使用了标签库
bbossaop->bboss-util [frameworkset-util.jar]



---------------------------------
构建发布包:
---------------------------------
切换到命令行，直接在工程目录下运行ant即可
---------------------------------
注意事项：
---------------------------------
o 发布包名---bboss-aop.jar src test src-log  src-thread      
  

o 发布包名---bboss-ws.jar src-cxf

o 发布包名---bboss-camel.jar src-camel

o 发布包名---bboss-mina.jar src-mina

o 发布包名---bboss-netty.jar src-netty

o 发布包名---bboss-schedule.jar src-schedule
o 发布包名---bboss-tlq.jar src-tlq
o 发布包名---bboss-rmi.jar src-rmi



o resources---基本资源文件目录，存放配置文件



部署说明：
o distrib目录中包含了bboss aop框架发布的jar包：
bboss-aop.jar - aop框架的核心jar包
bboss-camel.jar - bboss camel路由组件包，提供camel路由规则支持，从而实现bboss组件之间的路由功能，包括本地服务调用路由和远程服务调用路由
bboss-jms.jar - bboss aop框架提供jms调用的标准接口，符合jms 1.1规范，同时提供了bboss aop远程服务调用的jms协议实现
bboss-mina.jar - bboss aop框架远程服务调用的mina协议实现
bboss-ws.jar - bboss aop框架中的webservcie服务发布框架实现，目前支持apache cxf 2.2.4，同时提供了bboss aop远程服务调用的webservice协议实现
jgroups.jar - 基于JGroups-2.10.0.CR1扩展包，添加了bboss aop框架的认证、鉴权、加/解密功能

同时也包含了jms，mina，webservice运行时依赖的jar包：
lib\camel- camel组件库 版本为camel 2.2.0
lib\cxf - webservice框架apache cxf 2.2.4组件包

lib\jms - jms规范jar包，active mq组件库 active mq 5.3.2
lib\mina - mina协议组件库 ，apache mina-2.0.0-RC1
lib\ bboss aop框架依赖的一下工具包

o resource目录为bboss aop部署配置文件的存放目录，部署到运行环境时，需要将resource下的xml文件拷贝到
classpath根目录下，例如classes目录。



more details see my blog [http://blog.csdn.net/yin_bp]
todo list:
运行aop/ioc的最小依赖包整理
o MyEclipse 8.5下解析xml物理文件报错误问题修复：win7/xp都可能出这个问题
2013-05-24 22:15:07][ERROR][org.frameworkset.spi.assemble.ServiceProviderManager] 从文件[F:/Workspaces/MyEclipse 8.5/microcredit/WebRoot/WEB-INF/conf/bboss-mvc.xml]装载管理服务失败，请检查文件是否存在，或者是否被正确定义。
java.net.MalformedURLException: unknown protocol: f
	at java.net.URL.<init>(URL.java:574)
	at java.net.URL.<init>(URL.java:464)
	at java.net.URL.<init>(URL.java:413)
	at com.sun.org.apache.xerces.internal.impl.XMLEntityManager.setupCurrentEntity(XMLEntityManager.java:650)
	at com.sun.org.apache.xerces.internal.impl.XMLVersionDetector.determineDocVersion(XMLVersionDetector.java:186)
	at com.sun.org.apache.xerces.internal.parsers.XML11Configuration.parse(XML11Configuration.java:771)
	at com.sun.org.apache.xerces.internal.parsers.XML11Configuration.parse(XML11Configuration.java:737)
	at com.sun.org.apache.xerces.internal.parsers.XMLParser.parse(XMLParser.java:107)
	at com.sun.org.apache.xerces.internal.parsers.AbstractSAXParser.parse(AbstractSAXParser.java:1205)
	at com.sun.org.apache.xerces.internal.jaxp.SAXParserImpl$JAXPSAXParser.parse(SAXParserImpl.java:522)
	at javax.xml.parsers.SAXParser.parse(SAXParser.java:395)
	at javax.xml.parsers.SAXParser.parse(SAXParser.java:277)
	at org.frameworkset.spi.assemble.ServiceProviderManager.parseXML(ServiceProviderManager.java:337)
	at org.frameworkset.spi.assemble.ServiceProviderManager.load(ServiceProviderManager.java:279)
	at org.frameworkset.spi.assemble.ServiceProviderManager.init(ServiceProviderManager.java:198)
	at org.frameworkset.spi.assemble.ServiceProviderManager.init(ServiceProviderManager.java:163)
	at org.frameworkset.spi.BaseApplicationContext.<init>(BaseApplicationContext.java:299)
	at org.frameworkset.spi.BaseApplicationContext.<init>(BaseApplicationContext.java:277)
	at org.frameworkset.spi.DefaultApplicationContext.<init>(DefaultApplicationContext.java:110)
	at org.frameworkset.web.servlet.context.WebApplicationContext.<init>(WebApplicationContext.java:98)
	at org.frameworkset.web.servlet.context.WebApplicationContext.getWebApplicationContext(WebApplicationContext.java:236)
	at org.frameworkset.web.servlet.DispatchServlet.createWebApplicationContext(DispatchServlet.java:620)
	at org.frameworkset.web.servlet.DispatchServlet.initWebApplicationContext(DispatchServlet.java:562)
	at org.frameworkset.web.servlet.DispatchServlet.initServletBean(DispatchServlet.java:528)
	at org.frameworkset.web.servlet.DispatchServlet.init_(DispatchServlet.java:1143)
	at org.frameworkset.web.servlet.DispatchServlet.init(DispatchServlet.java:1616)
	at org.apache.catalina.core.StandardWrapper.loadServlet(StandardWrapper.java:1213)
	at org.apache.catalina.core.StandardWrapper.load(StandardWrapper.java:1026)
	at org.apache.catalina.core.StandardContext.loadOnStartup(StandardContext.java:4421)
	at org.apache.catalina.core.StandardContext.start(StandardContext.java:4734)
	at org.apache.catalina.core.ContainerBase.addChildInternal(ContainerBase.java:799)
	at org.apache.catalina.core.ContainerBase.addChild(ContainerBase.java:779)
	at org.apache.catalina.core.StandardHost.addChild(StandardHost.java:601)
	at org.apache.catalina.startup.HostConfig.deployDescriptor(HostConfig.java:675)
	at org.apache.catalina.startup.HostConfig.deployDescriptors(HostConfig.java:601)
	at org.apache.catalina.startup.HostConfig.deployApps(HostConfig.java:502)
	at org.apache.catalina.startup.HostConfig.start(HostConfig.java:1317)
	at org.apache.catalina.startup.HostConfig.lifecycleEvent(HostConfig.java:324)
	at org.apache.catalina.util.LifecycleSupport.fireLifecycleEvent(LifecycleSupport.java:142)
	at org.apache.catalina.core.ContainerBase.start(ContainerBase.java:1065)
	at org.apache.catalina.core.StandardHost.start(StandardHost.java:840)
	at org.apache.catalina.core.ContainerBase.start(ContainerBase.java:1057)
	at org.apache.catalina.core.StandardEngine.start(StandardEngine.java:463)
	at org.apache.catalina.core.StandardService.start(StandardService.java:525)
	at org.apache.catalina.core.StandardServer.start(StandardServer.java:754)
	at org.apache.catalina.startup.Catalina.start(Catalina.java:595)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
	at java.lang.reflect.Method.invoke(Method.java:597)
	at org.apache.catalina.startup.Bootstrap.start(Bootstrap.java:289)
	at org.apache.catalina.startup.Bootstrap.main(Bootstrap.java:414)

#######update function list since bbossgroups-3.6 begin###########
1.优化ioc框架中拦截器和事务拦截器机制
2.将拦截器改为链式拦截器，并且全部改为单例模式
3.拦截器上增加拦截方法匹配模式


#######update function list since bbossgroups-3.5 begin###########
------2012-05-22-------------
o 改进sql语句管理组件SQLUtil，将sql语句前后的空格去掉
------2012-05-08------------- 
o bboss国际化功能完善
1.增加国际化属性配置文件管理组件org.frameworkset.spi.support.HotDeployResourceBundleMessageSource
HotDeployResourceBundleMessageSource的使用方法为：
HotDeployResourceBundleMessageSource messagesource = new HotDeployResourceBundleMessageSource( );
messagesource.setBasenames(new String[] {"org/frameworkset/spi/support/messages"});
messagesource.setUseCodeAsDefaultMessage(true);
System.out.println(messagesource.getMessage("probe.jsp.generic.abbreviations",  Locale.US));
System.out.println(messagesource.getMessage("probe.jsp.generic.abbreviations",  Locale.US));

是否需要对HotDeployResourceBundleMessageSource实例管理的资源文件启用热加载机制
通过属性	private boolean changemonitor = true;来控制
	 true 启用
	 false 关闭
默认启用
设置changemonitor属性值的方法为：
messagesource.setChangemonitor(false);

通过ioc容器来管理messagesource对象的方法如下：
 <property name="messageSource" class="org.frameworkset.spi.support.HotDeployResourceBundleMessageSource" >
        <property name="basename" value="/WEB-INF/messages"/>
        <property name="useCodeAsDefaultMessage" value="true"/>
 </property>
 
 DefaultApplicationContext context = DefaultApplicationContext.getApplicationContext("org/frameworkset/spi/support/messages.xml");
MessageSource messagesource = context.getTBeanObject("messageSource",MessageSource.class);

2.每个ioc容器默认自带一个MessageSource对象，对应的国际化资源配置文件和ioc组件配置必须在同一个包路径下：
/bbossaop/test/org/frameworkset/spi/support/messages.xml
/bbossaop/test/org/frameworkset/spi/support/messages_zh_CN.properties
/bbossaop/test/org/frameworkset/spi/support/messages_en_US.properties
而且名称必须是messages，相应的语言信息部分为_en_US.properties
使用的实例为：
DefaultApplicationContext context = DefaultApplicationContext.getApplicationContext("org/frameworkset/spi/support/messages.xml");
		System.out.println(context.getMessage("Trans.Log.TransformationIsToAllocateStep", new Object[]{"a","b"}, (Locale)null));
		System.out.println(context.getMessage("Trans.Log.TransformationIsToAllocateStep", new Object[]{"a","b"}, Locale.SIMPLIFIED_CHINESE));
		System.out.println(context.getMessage("StepLoader.RuntimeError.UnableToInstantiateClass.TRANS0006",Locale.US));
		Resource resource = context.getResource("org/frameworkset/spi/support/messages");
		System.out.println(resource);
		
3.国际化配置文件热加载机制配置：
默认5秒检测文件是否改动，如果有改动mvc框架会重新加载有修改的文件，没有修改的文件不会重新加载
如果想屏蔽改机制请修改bboss-aop.jar/aop.properties文件的选项
#国际化属性文件变更检测时间间隔，单位为毫秒，默认为5秒间隔
resourcefile.refresh_interval=5000
若果>0则启用热加载机制，<=0则屏蔽热加载机制，开发环境请开启，生产环境请关闭		

o DefaultApplicationContext类中增加使用URL对象构建组件容器的方法，功能类似于从文件路径直接构建组件容器的方法
public static DefaultApplicationContext getApplicationContext(URL configfile)
o 改进mvc文件加载过滤器搜索文件规则，大幅提升mvc容器启动速度
升级bboss-aop.jar即可，相关的程序：
/bbossaop/src/org/frameworkset/spi/assemble/callback/DefaultAssembleCallbackResolver.java
o 新增抽象类beaninfoware，用来向组件中注入Pro对象
org.frameworkset.spi.BeanInfoAware
使用示例：
public class BeanInfoAwareTest extends org.frameworkset.spi.BeanInfoAware implements InitializingBean {
	public void afterPropertiesSet() throws Exception {
		String extrattr = super.beaninfo.getStringExtendAttribute("extrattr");//获取扩展属性值
		String extrattr_default = super.beaninfo.getStringExtendAttribute("extrattr","defaultvalue");//获取扩展属性值，如果没有指定则返回后面的默认值
	}
}

修改的程序:
/bbossaop/src/org/frameworkset/spi/assemble/BeanAccembleHelper.java
/bbossaop/src/org/frameworkset/spi/BeanInfoAware.java
/bbossaop/test/org/frameworkset/spi/beans/manager-beans.xml
/bbossaop/test/org/frameworkset/spi/beans/TestBeanContext.java
#######update function list since bbossgroups-3.4 begin###########
o 为了和避免和官方jgroups包冲突，将bboss中的jgroups包路径全部由org.jgroups改为bboss.org.jgroups
jg-magic-map.xml文件名称改为bboss-magic-map.xml
jg-protocol-ids.xml文件名称改为bboss-protocol-ids.xml

为了避免不必要的麻烦，jgroups.jar包名称暂时没有改，如果存在包名称冲突用户可根据实际情况进行修改名称（一般不需要修改）

o 改进ioc依赖注入机制，支持循环依赖注入功能（之前得版本不支持循环依赖注入），并且支持任何层面的对象引用，引用的定义为：

refid格式：                                                                                      含义
1.attr:serviceid                             根据服务标识引用容器服务组件 
2.attr:serviceid[0]                          根据服务标识及数组下标引用容器服务组件对应的下标为0对应的元素（容器服务组件类型为list，set，array三种类型）
3.attr:serviceid[key]                        根据服务标识及map key引用容器服务组件对应的索引为key对应的元素（容器服务组件类型为map类型）
4.attr:serviceid{0}                          根据服务标识及构造函数参数位置下标引用容器服务组件对应的下标为0对应的构造函数参数（容器服务组件为构造函数注入）
5.attr:serviceid->innerattributename         根据服务标识及服务组件的属性名称引用容器服务组件属性值 
6.attr:serviceid->innerattributename[0]      根据服务标识及服务属性名称以及属性数组下标引用容器服务组件属性中对应的下标为0对应的元素（容器服务组件类型为list，set，array三种类型）
7.attr:serviceid->innerattributename[key]    根据服务标识及服务属性名称以及map key引用容器服务组件属性对应的索引为key对应的元素（容器服务组件类型为map类型）
8.attr:serviceid->innerattributename{0}      根据服务标识及服务属性名称对应属性的造函数参数位置下标引用容器服务组件对应的下标为0对应的构造函数参数（容器服务组件为构造函数注入）

其中属性的引用是不限制层级的

o 统一将rpc协议序列化机制切换到bboss的序列化机制
已经完成切换的协议：
http，webservice，netty，rmi，mina,jms
未完成的协议：
jgroups 
因此采用jgroups协议时，所有的参数数据都要实现序列化接口，其他协议则不需要
o 改进http协议和webservice rpc协议性能，改进反射机制
o ioc属性注入中相应的属性不再需要set方法
o 引进最新的bboss soa序列化机制，对于static，final，transient类型的属性不进行序列化，对于添加了@ExcludeField的注解不序列化
需要序列化的字段不再需要get/set方法
#######update function list since bbossgroups-3.3 begin###########
------2011-10-22-----------
o 增加webservice服务JaxWsProxyFactory组件,用来获取webservice服务组件的客户端调用代理组件
org.frameworkset.spi.remote.webservice.JaxWsProxyFactory
使用方法如下：
 org.frameworkset.web.ws.WSService wsservice = JaxWsProxyFactory.getWSClient("http://localhost:8080/bboss-mvc/cxfservices/mysfirstwsservicePort",
                                             org.frameworkset.web.ws.WSService.class);

------2011-10-20-----------
o 改进远程协议，客户端直接通过ClientProxyContext类根据服务接口生成服务的调用桩程序,客户端不再需要服务组件的aop配置文件和实现类
使用方法参考测试用例：
//获取mvc容器中组件的远程服务调用接口，mvc容器由服务端mvc框架自动初始化
		ClientInf mvcinf = ClientProxyContext.getWebMVCClientBean(
				"(http::172.16.25.108:8080/bboss-mvc/http.rpc)" +
				"/client.proxy.demo?user=admin&password=123456",
				ClientInf.class);
		
		//获取ApplicationContext类型容器中组件的远程服务调用接口
		//ApplicationContext容器必须是以下方式创建
//		ApplicationContext context = ApplicationContext.getApplicationContext("org/frameworkset/web/ws/testwsmodule.xml");
		WSService WSService = ClientProxyContext.getSimpleClientBean("org/frameworkset/web/ws/testwsmodule.xml", 
				"(http::172.16.25.108:8080/bboss-mvc/http.rpc)" +
				"/mysfirstwsservice?user=admin&password=123456", 
				WSService.class);
		
		//获取服务器端默认容器中组件的远程服务调用接口
		//服务器端默认容器manager-provider.xml必须是以下方式创建
//		ApplicationContext context = ApplicationContext.getApplicationContext();
		//以下是传统的远程服务获取方式，必须要求本地有相应的接口和组件实现以及配置文件，新的api已经消除了这种限制
//		context.getTBeanObject("(http::172.16.25.108:8080/bboss-mvc/http.rpc)" +
//				"/client.proxy.simpledemo?user=admin&password=123456",  ClientInf.class);
		ClientInf defaultinf = ClientProxyContext.getApplicationClientBean( "(http::172.16.25.108:8080/bboss-mvc/http.rpc)" +
				"/client.proxy.simpledemo?user=admin&password=123456", ClientInf.class);
		
		//获取客户端调用代理接口
		//服务器端容器org/frameworkset/spi/ws/webserivce-modules.xml必须是以下方式创建
//		DefaultApplicationContext context = DefaultApplicationContext.getApplicationContext("org/frameworkset/spi/ws/webserivce-modules.xml");
		ClientInf simpleinf = ClientProxyContext.getSimpleClientBean("org/frameworkset/spi/ws/webserivce-modules.xml",//容器标识
		                                                            "(http::172.16.25.108:8080/bboss-mvc/http.rpc)/client.proxy.simpledemo?user=admin&password=123456",//服务组件地址 
		                                                            ClientInf.class);//服务接口		                                                            
		
		//环境预热
		mvcinf.helloworld("aaaa，多多");
		WSService.sayHello("aaaa，多多");
		simpleinf.helloworld("aaaa，多多");
------2011-10-19-----------
o 修复组件生命周期管理缺陷，该问题表现为通过destroy-method指定组件销毁方法不起作用
<property name="test.destorybeans" init-method="init" destroy-method="destroy"
		class="org.frameworkset.spi.beans.DestroyBean"/>
------2011-10-10-----------
o 改进在weblogic上需发布webservice服务功能
改进后无需在resources/org/frameworkset/spi/ws/webserivce-modules.xml中配置ws.base.contextpath参数

------2011-09-29-----------
o 完善Pro对象中集合list，map，set，array的监控方法
------2011-09-19-----------
o 解决SOAApplicationContext存在的构造函数缺陷问题
------2011-09-18-----------
o 修复webservie服务在weblogic上发布失败缺陷，增加如下配置：
配置文件：
resources/org/frameworkset/spi/ws/webserivce-modules.xml中增加以下属性配置（tomcat中不需要）：
	<property name="ws.base.contextpath" 
			  value="http://172.16.25.108:7001/WebRoot/cxfservices/"/>

weblogic服务器发布webservice服务时需要指定ws.base.contextpath属性，
	 以便服务能够正常发布,其格式为：
	 http://ip:port/appcontext/webservicecontext
	 http代表传输协议
	 ip：服务端ip
	 port:服务端端口
	 appcontext:服务端上下文
	 webservicecontext：web.xml中配置的webservice服务引擎Servlet拦截url串，例如cxf在web.xml中的配置如下：
	 
	 <servlet>
		<display-name>cxf</display-name>
		<servlet-name>cxf</servlet-name>
		<servlet-class>org.apache.cxf.transport.servlet.RPCCXFServlet</servlet-class>
		<load-on-startup>2</load-on-startup>
	</servlet>
	<servlet-mapping>
		<servlet-name>cxf</servlet-name>
		<url-pattern>/cxfservices/*</url-pattern>
	</servlet-mapping>
	
	则webservicecontext的值就对应为cxfservices。
	
	以下是一个配置示例：
	 http://172.16.25.108:7001/WebRoot/cxfservices/
@WebService
public class WSServiceInMVCImpl implements WSServiceInMVC {

	public String sayMvsHello(String duoduo) {
		System.out.println("MVC欢迎您!" + duoduo);
		return "MVC欢迎您!" + duoduo;
	}
	@WebMethod(exclude=true)
	public String sayMvsHello_(String duoduo) {
		System.out.println("MVC欢迎您!" + duoduo);
		return "MVC欢迎您!" + duoduo;
	}
}

通过注解WebService声明服务组件
通过注解声明不需要发布为服务方法的方法：@WebMethod(exclude=true)，weblogic服务器在发布服务时，无法处理参数为接口类型的服务方法，最终导致服务发布失败，因此一般不要定义带接口参数的服务方法，如果服务组件中有这种类型的方法，可以通过@WebMethod(exclude=true)注解，并把exclude属性设置为true来声明不将该方法发布为服务方法。

为了更加规范化webservice服务方法，需要将服务参数都加上WebParam注解，例如：
public String sayMvsHello_(@WebParam(name="duoduo") String duoduo) {


------2011-08-26-----------
o 完善bbossgroups监控模块，以便能够监控spi的所有配置：
ioc组件基本配置信息，所属组件容器类型，所属配置文件路径，属性注入配置，构建函数注入配置，声明式事务配置，拦截器配置信息，mvc跳转路径映射配置信息
rmi服务配置信息，webservice服务配置信息，组件扩展属性配置，全局参数信息配置
sql配置文件信息，sql语句配置信息，sql文件实时扫描信息
监控页面的访问地址：

------2011-08-26-----------
o BaseApplicationContext中增加以下方法
public static BaseApplicationContext getBaseApplicationContext(String configfile)
public Pro getInnerPro(String parent,String name)

#######update function list since bbossgroups-3.2 begin###########
------2011-07-16-----------
o 增加netty协议消息大小配置参数，解决爆炒过默认1M时发送失败的问题
<!-- 能够解码的最大数据size，超过时，将抛异常，默认20M -->
			<property name="maxFramgeLength_" value="20971520" />
			
			<!-- 编码块大小 -->
			<property name="estimatedLength_" value="1024" />
------2011-07-04-----------
o 修改默认服务发布时，serviceport带了ws:前缀
------2011-07-02-----------
o 修复注入属性缺陷：
当属性值注入以后没有立即退出注入循环，而是把循环跑完，对性能有一定的影响。
修改程序：
/bbossaop/src/org/frameworkset/spi/assemble/BeanAccembleHelper.java
o 修复配置文件sql不能安照特定数据库类型或者到指定数据库sql语句的缺陷
#######update function list since bbossgroups-3.1 begin###########
------2011-05-28-----------
o 修复quartz引擎加载带参数方法任务时，后台抛空指针异常，这是3.1版本产生的新问题，影响新
 版本的使用
o 完善任务调度管理组件org.frameworkset.task.TaskService
完善stopService方法和startService方法，解决停止引擎后，无法启动引擎的问题，增加方法stopService(boolean force)
增加方法，一边在执行public void deleteJob(String jobname, String groupid)方法后重新加载作业
public void startExecuteJob(String groupid, String jobname)


------2011-05-20-----------
o 增加rmi服务发布和rmi客服端组件获取功能
发布服务方法，通过 rmi:address指定发布服务的唯一地址：
<property name="rmi_service_test"
		 rmi:address="rmi_service_test"
		 class="org.frameworkset.spi.remote.rmi.RMIServiceTest"/>
服务器ip和port的指定在rmi协议配置文件中：
/bbossaop/resources/org/frameworkset/spi/manager-rpc-rmi.xml
	<!-- 
					服务器绑定端口
				 -->
			<property name="connection.bind.port" value="1099" />
			
rmi客服端组件配置和获取：
配置
<property name="rmi_service_client_test" factory-class="org.frameworkset.spi.remote.rmi.RMIUtil" factory-method="lookupService">
		<construction>
			<property name="servicaddress" value="//172.16.25.108:1099/rmi_service_test"/>			
		</construction>
	</property>	

获取：
	BaseApplicationContext context ;
	@Before
	public void init()
	{
		context = ApplicationContext.getApplicationContext("org/frameworkset/spi/remote/rmi/rmi-client.xml");
	}
	@Test 
	public void test() throws RemoteException
	{
		RMIServiceTestInf test = (RMIServiceTestInf)context.getBeanObject("rmi_service_client_test");
		System.out.println(test.sayHello("多多"));
	}
		
------2011-05-15-----------
o 改进国际化机制，每个组件容器相关的国际化文件必须和组件容器的根配置文件在同级目录，并且名称以下格式命名：
messages_en_US.properties
messages_zh_CN.properties
o 改进sql配置文件刷新机制，将3.1版本中每个sql配置文件定义一个守护进程改为单一守护进程检测机制，即所有的sql文件是否
变更的检测由一个守护进程完成，这个进程中维护一个文件队列，刷新事件配置也由manager-provider.xml文件改为：
/bbossaop/src/aop.properties中配置：
sqlfile.refresh_interval=5000
同时将sql配置文件的管理容器改为SOAFileApplicationContext，以便提升系统性能


------2011-05-11-----------
o 改进ApplicationContext，WebApplicationContext容器，分离出SOAApplicationContext、BaseApplicationContext和DefaultApplicationContext三个类型的容器，他们的职责分别为：
ApplicationContext：和原来的功能ApplicationContext的功能一致，包括基本的aop/ioc功能，远程服务，全局属性管理，拦截器，包含声明式事务管理，是BaseApplicationContext的子类
SOAApplicationContext/SOAFileApplicationContext：两个轻量级的ioc容器，包含全局属性管理，不包含远程服务，拦截器，不包含声明式事务管理，是DefaultApplicationContext的子类
DefaultApplicationContext：和ApplicationContext的区别就是不包含远程服务的功能，和远程服务相关的组件没有依赖关系，是BaseApplicationContext的子类
BaseApplicationContext：是一个抽象容器，所有的上层容器都间接或直接实现了这个容器，提供公共的基础功能。
WebApplicationContext：是mvc框架的控制器和业务组件管理容器，是DefaultApplicationContext的子类，拥有DefaultApplicationContext的所有功能。


------2011-05-06------------
o 修复循环依赖机制漏洞
o 修复quartz任务调度导致模块中没有配置任何任务时，后台抛出类型转换异常
o 修复3.0版本中没有将组件实例机制定义为单例模式的漏洞，但是ppt培训教程中却明确指出该版本中的组件默认为单例模式

上述3个漏洞修复的程序为：
/bbossaop/src/org/frameworkset/spi/assemble/BeanAccembleHelper.java
/bbossaop/src/org/frameworkset/spi/assemble/Pro.java
/bbossaop/src/org/frameworkset/spi/assemble/ProviderParser.java
	
#######update function list since bbossgroups-3.1 end###########

#######update function list since bbossgroups-3.0 begin###########
------2011-04-24------------
o 改进webservice服务装载功能，可以从mvc和所有的applicationcontext中配置和装载webservice服务：
Mvc框架的ws服务无需特殊处理
普通的applicationcontext容器中的ws服务对应的模块配置文件需要配置在org/frameworkset/spi/ws/webserivce-modules.xml文件中

------2011-04-21------------
o 增加组件异步调用机制，使用方法参考测试用例：
/bbossaop/test/org/frameworkset/spi/asyn/AsynbeanTest.java
可以通过Async注解标注组件中需要异步执行的方法即可，可以指定超时时间，是否需要返回结果，是否需要回调处理返回结果

------2011-04-20------------
o 完善Pro对象对ProList，ProSet，ProMap，ProArray的处理机制
o ApplicationContext组件新增一组获取ProArray对象的接口
public ProArray getArrayProperty(String name) ;
public ProArray getProArrayProperty(String name, ProArray defaultValue) ;
------2011-04-18------------
o 解决获取空的ProList时导致aop框架启动失败的问题
------2011-04-11------------
o 新增array元素，通过该元素可以实现各种类型数组数据的注入功能
------2011-04-07------------
o 完善ApplicationContext组件的生命周期管理机制
o ApplicationContext组件增加获取long值属性的api
------2011-04-06------------
o 完善mvc框架配置文件导入方式
可以用,号分隔导入各个文件,例如：
<servlet>
	<servlet-name>mvcdispather</servlet-name>
	<servlet-class>org.frameworkset.web.servlet.DispatchServlet</servlet-class>
	<init-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>/WEB-INF/bboss-*.xml,
					/WEB-INF/conf/bboss-*.xml</param-value>
	</init-param>
。。。。。。
</servlet>
------2011-03-30------------
o ioc中属性注入时，如果属性没有定义set方法，会抛出异常，导致类注入初始化失败，修改为提示而不是失败方式

------2011-03-21------------
 
o 修复jms不能发送长度为0的jms消息bug
/bbossaop/src-jms/org/frameworkset/mq/RequestDispatcher.java

------2011-03-10------------
 
o 将组件管理模式默认为设置为单例模式
------2011-03-09------------
 
o 扩展list,map,set元素类型定义，添加componentType属性，用来标识容器中存放的对象类型，componentType的取值范围如下：
bean：标识容器元素对象类型是组件对象类型
String：标识容器元素对象类型是String对象

该属性可以用来方便将组件类型的list和字符串类型的list注入到其他组件中。

相应地在ProList、ProMap、ProSet对象上增加了以下方法：
ProList： public List getComponentList()
ProMap：public Map getComponentMap()
ProSet：public Set getComponentSet()

使用案例如下：

<property name="/index.htm,/detail.htm"
    		  f:demo_sites="attr:demo_sites"
			    class="org.frameworkset.web.demo.SiteDemoController" singlable="true"/>
			    
	<property name="demo_sites">
		<list componentType="bean">
			<property f:name="listbean" 
					f:cnname="集合po对象绑定实例" 
					class="org.frameworkset.web.demo.SiteDemoBean">
				<property name="controllerClass" value="D:/workspace/bbossgroup-2.0-RC2-mvc/bboss-mvc/test/org/frameworkset/spi/mvc/ListBeanBindController.java"/>	
				<property name="configFile" value="D:/workspace/bbossgroup-2.0-RC2-mvc/bboss-mvc/WebRoot/WEB-INF/bboss-listbean.xml"/>
				<property name="visturl">
					<list componentType="String">
						<property value="/databind/showstringarraytoList.htm"/>
						<property value="/databind/showlist.htm"/>
					</list>
				</property>
				<property name="formlist">
					<list  componentType="bean">
						<property f:formPath="D:/workspace/bbossgroup-2.0-RC2-mvc/bboss-mvc/WebRoot/jsp/databind/table.jsp" 
								 f:charset="UTF-8"
								class="org.frameworkset.web.demo.FormUrl">							
								<property name="description"><![CDATA[
									表单table.jsp, 对应于/databind/showlist.htm跳转页面 
								]]></property>
						</property>
						<property f:formPath="D:/workspace/bbossgroup-2.0-RC2-mvc/bboss-mvc/WebRoot/jsp/databind/stringarraytoList.jsp" 
								class="org.frameworkset.web.demo.FormUrl">
							<property name="description"><![CDATA[
									表单stringarraytoList.jsp, 对应于/databind/showstringarraytoList.htm跳转页面 
							]]></property>
						</property>
						<property f:formPath="D:/workspace/bbossgroup-2.0-RC2-mvc/bboss-mvc/WebRoot/jsp/databind/tableinfo.jsp" 
								class="org.frameworkset.web.demo.FormUrl">
							<property name="description"><![CDATA[
									表单/bboss-mvc/WebRoot/jsp/databind/tableinfo.jsp, 对应于/databind/showbean.htm跳转页面 
							]]></property>
						</property>
					</list>
				</property>
				<property name="description"><![CDATA[
					集合po对象绑定实例 字符串数组转List数据绑定实例 
				]]></property>
			</property>
		</list>
	</property>
	
	类如下：SiteDemoController
	
	public class SiteDemoController {
	private List<SiteDemoBean> demo_sites;
	
	public String index(ModelMap model)
	{		
		model.addAttribute("demobeans", demo_sites);
		return "index";
	}
	
	public String detail(ModelMap model,@RequestParam(name="demoname") String demoname)
	{
		SiteDemoBean bean = null;
		for(int i = 0; i < demo_sites.size(); i ++)
		{
			SiteDemoBean bean_ = demo_sites.get(i);
			if(demoname.equals(bean_.getName()))
			{
				bean = bean_;
				break;
			}
			
		}
		model.addAttribute("demobean", bean);
		
		return "seconddetail";
	}

	/**
	 * @return the demo_sites
	 */
	public List<SiteDemoBean> getDemo_sites() {
		return demo_sites;
	}

	/**
	 * @param demoSites the demo_sites to set
	 */
	public void setDemo_sites(List<SiteDemoBean> demoSites) {
		demo_sites = demoSites;
	}

}
	

#######update function list since bbossgroups-3.0 end###########

------------------------------------------------------------------
update function list in bbossgroups-2.0-rc2 since bbossgroups-2.0-rc1
------------------------------------------------------------------
2011-01-20
------------------------------------------------------------------
o aop框架中，quartz任务调度做了以下的功能扩展
1.用户可以在aop中配置quartz的所有参数，将参数配置在quartz.config map中就可以了 
	<property name="quartz.config">
		<map>
			<property name="org.quartz.scheduler.instanceName" value="DefaultQuartzScheduler111" />
			<property name="org.quartz.scheduler.rmi.export" value="false" />
			<property name="org.quartz.scheduler.rmi.proxy" value="false" />
			<property name="org.quartz.scheduler.wrapJobExecutionInUserTransaction" value="false" />
			<property name="org.quartz.threadPool.class" value="org.quartz.simpl.SimpleThreadPool" />
			<property name="org.quartz.threadPool.threadCount" value="10" />
			<property name="org.quartz.threadPool.threadPriority" value="5" />
			<property name="org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread" value="true" />
			<property name="org.quartz.jobStore.misfireThreshold" value="6000" />
			<property name="org.quartz.jobStore.class" value="org.quartz.simpl.RAMJobStore" />
		</map>
	</property>

具体配置，可以参考/bbossaop/resources/org/frameworkset/task/quarts-task.xml文件
改进后可以在quarts-task.xml文件中配置quartz的启动参数和所有需要启动的服务和任务

2.调度任务上增加属性shouldRecover="false"，这个属性在集群环境中使用
<property name="workbroker" jobid="workbroker"
	action="org.frameworkset.task.TestJob"
	cronb_time="0 56 14 * * ?" used="true"
	shouldRecover="false"
	>
	<map>
		<property name="send_count" value="2" />
	</map>
</property>

shouldRecover属性必须设置为 true，当Quartz服务被中止后，再次启动或集群中其他机器接手任务时会尝试恢复执行之前未完成的所有任务。

3.可以启动多个quartz实例，每个实例对应一个quarts-task.xml配置文件
使用方法如下：
	TaskService.getTaskService().startService();
	TaskService.getTaskService().stopService();
	
	TaskService.getTaskService("org/frameworkset/task/test-quarts-task.xml").startService();
	TaskService.getTaskService("org/frameworkset/task/test-quarts-task.xml").stopService();
	
	
4.扩展原有任务执行Execute类模式，增加直接配置一个aop管理组件的特定方法和某个class的特定方法做为任务执行的动作
同时可以为这些操作通过contruction元素指定执行参数
配置方法如下：

					<!-- 组件方式指定任务执行程序,通过bean-name指定任务程序组件，method指定要执行的方法
					通过
					construction指定方法的参数值，多个参数按照参数的顺序指定
					多个property属性即可。
					-->
					<property name="beanmethodjob" jobid="beanmethodjob"						
						bean-name="methodjob"
						method="action"
						cronb_time="10 * * * * ?" used="true"
						shouldRecover="false">
					</property>
					<!--  
					class方式指定 任务程序，method方法对应要执行的方法，通过
					construction指定方法的参数值，多个参数按照参数的顺序指定
					多个property属性即可。
					
					 -->
					<property name="beanclassmethodjob" jobid="beanclassmethodjob"						
						bean-class="org.frameworkset.task.ClassMethodJob"
						method="action" 
						cronb_time="10 * * * * ?" used="true"
						shouldRecover="false">
						<construction>
							<property name="hello" value="hello" />
						</construction>
					</property>



------------------------------------------------------------------
2011-01-18
------------------------------------------------------------------
o 去除配置文件不存在的异常堆栈信息
------------------------------------------------------------------
2011-01-15
------------------------------------------------------------------
引入新的组件的创建机制
property元素上增加factory-bean/factory-class和factory-method两个属性，也就是对应property元素管理的组件
实例通过factory-bean或者factory-class对应的组件中factory-method对应的方法来创建。
factory-bean和factory-class的区别：
factory-bean指定的值是一个组件id，对应在aop组件配置文件中的其他组件的一个引用，同时factory-method指定的方法名称是这个组件的
一个实例方法。
factory-class指定的是创建组件的工厂实现类的全路径，同时factory-method指定的方法名称是这个组件的
一个静态方法。
如果factory-method指定的方法带有参数，可以通过construction节点包含property节点的方式来指定，多个property节点的排列顺序
要和方法参数的顺序一致，类型要可以转换。

具体的实例可以参考测试用例：
/bbossaop/test/org/frameworkset/spi/beans/factory

------------------------------------------------------------------
2011-01-08
------------------------------------------------------------------
规划：
全局事务处理的实现
dwr与aop的结合
ajaxrpc与aop的结合

------------------------------------------------------------------
2010-09-29
------------------------------------------------------------------
o 修改public Object getObject(Object defaultValue)方法bug

将
	Object value = this.getTrueValue();
        if(this.value != null)
            return value;
        return defaultValue;
        
        修改为：
        if(this.value != null)
            return value;
    	Object value = this.getTrueValue();

        return value != null ?value:defaultValue;

------------------------------------------------------------------
2010-09-29
------------------------------------------------------------------
o 增加rpc.evaluatelocaladdress配置参数
/bbossaop/resources/org/frameworkset/spi/manager-rpc-service.xml
rpc.evaluatelocaladdress的作用如下：
是否对远程地址进行本地地址校验，如果设置为true
				则对远程地址进行本地地址判断，如果是local地址将转换为本地调用，否则执行远程调用
			 如果设置为false，一律当成远程方法调用
			 默认为false
------------------------------------------------------------------
2010-09-27
------------------------------------------------------------------
o 完善rpc异常处理机制
客服端调用远程组件服务时，对于服务端抛回的异常（业务异常和RemoteException），并将业务异常统一封装为
RemoteException，这样就导致客户端程序无法直接捕获服务端的业务异常。进行修复后直接将业务异常直接返回给
客服端。

o 修复http/https协议的相关问题


2010-09-19
------------------------------------------------------------------
o 修复RPCRequest可能的死锁隐患
------------------------------------------------------------------
2010-09-16
------------------------------------------------------------------
o 增加http/https协议的支持
http协议的服务器可以独立的http server，也可以是tomcat，jetty，weblogic，WebSphere等应用服务器。对应的jar包为bboss-http.jar
http/https协议配置文件为
------------------------------------------------------------------
2010-09-03
------------------------------------------------------------------
o 直接通过pro的getBean方法获取引用组件对象空指针bug修复

------------------------------------------------------------------
2010-08-27
------------------------------------------------------------------
o 修复rest风格请求中对netty和rmi协议未处理的bug

------------------------------------------------------------------
update function list in bbossgroups-2.0-rc since bbossgroups-1.0
------------------------------------------------------------------
2010-07-26
------------------------------------------------------------------
o jgroups 加密和认证机制完善
o 增加服务组件获取客服端请求参数方法，增加RequestContext上下文处理类，用来获取客服端传递的系统参数
使用方法：
    public Object getParameter()
	{
		String value = RequestContext.getRequestContext().getStringParameter("parameterKey");
		System.out.println("value:" + value);
		return value;
	}

------------------------------------------------------------------
2010-07-23
------------------------------------------------------------------
o 修复严重错误，该问题表现为，对一个单实例的远程服务组件并发发起多个方法调用时会出现以下现象：
     请求响应结果丢失，一个rpc请求接收其他请求的结果，导致不可以预料的错误，比如类型转换错误
     
  修改程序为：
  /bbossaop/src/org/frameworkset/spi/remote/RPCMessage.java
  
  这个问题需要发布程序补丁bboss rpc classcast and timeout exception patch.zip
o 修改SQLUtil组件，增加变量替换功能
详细使用方法参考测试用例：org.frameworkset.spi.persistent.SQLUtilTest

o 修改cglib堆栈溢出bug
采用cglib ioc机制时，当组件配置了声明式事务，在执行的时候将报堆栈溢出错误。

相关的程序：
/bbossaop/src/org/frameworkset/spi/cglib/BaseCGLibProxy.java
/bbossaop/src/org/frameworkset/spi/cglib/CGLibProxy.java
/bbossaop/src/org/frameworkset/spi/cglib/SynCGLibProxy.java
/bbossaop/src/org/frameworkset/spi/cglib/SynTXCGLibProxy.java
/bbossaop/src/org/frameworkset/spi/ApplicationContext.java
/bbossaop/src/org/frameworkset/spi/cglib/AopProxyFilter.java(新增)

o 新增/bbossaop/src/org/frameworkset/persitent/util/SQLUtil.java类
作用是可以管理sql语句，可以加载不同的配置sql文件，并提取其中的sql语句




------------------------------------------------------------------

2010-07-11
-----------------------------------------------------
o 升级以下组件：
mina-2.0.0-RC1
active mq 5.3.2
camel 2.2.0
-----------------------------------------------
2010-06-30
-----------------------------------------------------
o 修复rpc方法调用异常堆栈不能正确反映调用执行堆栈的问题
------------------------------------------------------
2010-06-24
-----------------------------------------------------
o jgroups协议升级到JGroups-2.10.0.CR1
建议：升级了新版的jgroups后，基于jgroups的rpc协议最好只在集群环境下使用，使用的方法参考测试用例。
原来进行jgroups集群单播和多播调用的方法进行了调整，详细情况请看测试用例：
/bbossaop/test/org/frameworkset/spi/remote/jgroup/TestClient.java
广播调用：
RPCTestInf testInf = (RPCTestInf)BaseSPIManager.getBeanObject("(jgroup::all)/rpc.test");
多播调用：
RPCTestInf testInf = (RPCTestInf)BaseSPIManager.getBeanObject("(jgroup::creator-cc-27488;creator-cc-51859)/rpc.test");
单播调用：
String address_ = "test";
	RPCTestInf testInf = (RPCTestInf)BaseSPIManager.getBeanObject("(jgroup::" + address_ + ")/rpc.test");
	
Address address_ = JGroupHelper.getJGroupHelper().getAppservers().get(0);
	RPCTestInf testInf = (RPCTestInf)BaseSPIManager.getBeanObject("(jgroup::" + address_ + ")/rpc.test");
	
广播和多播调用的结果处理：
		方式一：
			Object ret = testInf.getCount();
            Object ret_40561 = BaseSPIManager.getRPCResult("creator-cc-27488", ret,Target.BROADCAST_TYPE_JRGOUP);
            Object ret_64357 = BaseSPIManager.getRPCResult("creator-cc-51859", ret,Target.BROADCAST_TYPE_JRGOUP);
    	方式二：
    	Object ret = testInf.getCount();
		int size = BaseSPIManager.getRPCResultSize(ret);
		for(int j = 0; j < size; j ++)
		{
			Object ret_1186 = BaseSPIManager.getRPCResult(j, ret);
			System.out.println("ret_1186:" + j + " = "+ret_1186);
		}
		
-----------------------------------------------
2010-06-19
-----------------------------------------------------
o 采用cglib实现aop代理
保留原来的java动态代理模式，增加cglib模式的支持，默认采用cglib模式
用户可以自己配置aop.proxy.type属性来切换代理模式
<!-- 
		aop实现机制：
		default java动态代理模式
		cglib cglib模式
	 -->
	<property name="aop.proxy.type" value="cglib"/>
	
	aop.proxy.type属性配置在manager-provider.xml文件中

-----------------------------------------------
2010-06-18
-----------------------------------------------------
o 修改bean缓冲规则，带参数的bean也可被缓冲(注意缓冲区溢出)
o 增加rmi组件，可以使用rmi来实现远程通讯协议
rmi协议的特性，rmi组件可以注册到同一个rmi注册中心，因此可以利用rmi远程服务组件指定唯一的应用标识，然后将其注册到rmi注册中心
客服端在调用时可以指定特定的标识，然后查找到对应服务器的rmi组件然后完成基于rmi协议的远程组件调用，如果没有指定唯一的应用标识
那么默认为注册中心和rmi组件服务器在同一个服务器上
使用方法如下：
服务器启动：
RMIServer.getRMIServer().start();
客服端组件调用：
RPCTestInf testInf = (RPCTestInf)BaseSPIManager.getBeanObject("(rmi::172.16.17.216:1099)/rpc.test?app1");
long start = System.currentTimeMillis();
for(int i = 0; i < 10; i ++)
	System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
	
long end = System.currentTimeMillis();
System.out.println("消耗时间：" + (end - start) / 1000 + "秒");

o 修改协议校验逻辑，所有的协议全部采用FC组件来完成可用性校验

o 修改cxf webservice协议bug
-----------------------------------------------
2010-06-14
-----------------------------------------------------
o jms模块增加对国内消息中间件-东方通tlq的支持

bboss-tlq.jar，增加连接工类org.frameworkset.tlq.TLQConnectionFactory
<!-- 
		jms连接工厂配置org.frameworkset.tlq.TLQConnectionFactory
	 -->
	<property name="test.tlq.ConnectionFactory" singlable="true" class="org.frameworkset.tlq.TLQConnectionFactory">	
		<construction>		
			<property name="connectURL" 
				      value="tlkq://172.16.17.143:10241/"/>				
			<property name="username" 
				      value="system" />	
			<property name="password" 
				      value="manager" />
		</construction>			
	</property>
	使用tlq的jmstemplate：
	<property name="test.jmstemplate" class="org.frameworkset.mq.JMSTemplate">
			<construction>		
				<property name="connectionfactory" 
		      			refid="attr:test.tlq.ConnectionFactory"/>	        
			</construction>		
	</property>
	
	简单消息发送方法：
	
		ApplicationContext context = ApplicationContext.getApplicationContext("org/frameworkset/spi/remote/jms/tlq_jms.xml");
		JMSTemplate template = (JMSTemplate)context.getBeanObject("test.jmstemplate");
        try
        {
            template.send("testQueue1", "ahello");
            
        }
        catch (JMSException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            template.stop();
        }
	其他实例参考测试用例：
	org/frameworkset/spi/remote/jms/TestTlq.java


-----------------------------------------------------


2010-05-17
-----------------------------------------------------
o 增加故障检测组件
-----------------------------------------------
org.frameworkset.spi.remote.health.RPCValidator
public static boolean validator(String contextpath)
返回true，标识服务器可达，并能正常工作
返回false，标识服务器不可达，不能正常工作
ApplicationContext添加以下方法：
public static String getRealPath(String parent,String file)
该方法用来获取服务的正式路径
/bbossaop/resources/org/frameworkset/spi/manager-rpc-service.xml中增加以下服务：
<!-- 
			协议健康检测服务，所有的服务都可以采用这个服务来做判断
		 -->		
		<property name="rpc.fc" singlable="true"
						  class="org.frameworkset.spi.remote.health.HealthCheckServiceImpl"/>
/bbossaop/src/org/frameworkset/spi/remote/RPCHelper.java中增加了一下方法：
	public static String buildContextAddress(String protocol,String ip,String port)
    public static String buildContextAddress(String protocol,String url)
    
    public static String buildAddress(String protocol,String ip,String port,String serviceid)
    
    public static String buildAddress(String protocol,String url,String serviceid)
    
    public static String buildAuthAddress(String protocol,String ip,String port,String serviceid,String user,String password)
    
    public static String buildAuthAddress(String protocol,String url,String serviceid,String user,String password)
    
-----------------------------------------------------						  	
2010-05-10
-----------------------------------------------------
o 增加服务器停止方法
org.frameworkset.spi.remote.RPCHelper.getRPCHelper().stopRPCServices();
-----------------------------------------------
2010-05-05
-----------------------------------------------------
o SecurityContext中增加一系列应用参数获取方法
-----------------------------------------------
2010-05-04
-----------------------------------------------------
o 新增applicationcontext后，属性引用解析时，仍然从默认上下文中获取组件实例，这是个bug，修改后相关的程序如下：
/bbossaop/src/org/frameworkset/spi/assemble/BeanAccembleHelper.java中的方法：
public Object getRefValue(Pro property, CallContext callcontext,
			Object defaultValue) 
			
			property.getApplicationContext().getProvider(
			property.getApplicationContext().getBeanObject(

-----------------------------------------------
2010-04-07
-----------------------------------------------------
o 引入新的组件的创建机制（待实现），已经于2011-01-15日提供
property元素上增加factory-bean和factory-method两个属性，也就是对应property元素管理的组件
实例通过factory-bean对应的组件中factory-method对应的方法来创建。

o 源码包src中添加了以下程序，便于spring程序向bbossaop迁移，还需验证和测试
org.springframework.beans.factory.InitializingBean
org.springframework.beans.factory.DisposableBean
同时将org.frameworkset.spi.InitializingBean从org.springframework.beans.factory.InitializingBean
继承
同时将org.frameworkset.spi.DisposableBean从org.springframework.beans.factory.InitializingBean
继承

这样就可以很方便地将spring管理的组件迁移到bbossaop中来

o 新增rpc协议：jboss-netty

对应的源码文件目录src-netty
协议配置文件bbossaop/resources/org/frameworkset/spi/manager-rpc-netty.xml
o mina协议增加ssl支持
/bbossaop/key_generate.bat 生成证书的脚本，生成的证书默认存放在D:\workspace\bbossgroup\bbossaop\resources\keystore目录下

客服端发起ssl通讯，服务端为其建立ssl传输通道。如果要启用mina ssl，需要在manager-mina-rpc.xml文件中配置ssl的相关选项：
<property name="rpc.protocol.mina.params">
		<map>
			。。。。
			
			<!-- 
				ssl配置参数开始
				如果启用ssl，那么必须在rpc.protocol.mina.ssl.client和
				rpc.protocol.mina.ssl.server中配置证书的相关信息
				服务器端只需要配置rpc.protocol.mina.ssl.server
				客服端只需要配置rpc.protocol.mina.ssl.client
			 -->
			<property name="enablessl" value="true" />
<!--			<property name="enabledCipherSuites" value="SSL_DH_anon_WITH_RC4_128_MD5"  >-->
<!--				<editor class="org.frameworkset.spi.assemble.StringArrayEditor"/>-->
<!--			</property>-->
				<!-- 下述参数不要配置  -->
<!--			<property name="enabledProtocols" value="TLS" >-->
<!--				<editor class="org.frameworkset.spi.assemble.StringArrayEditor"/>-->
<!--			</property>-->
<!--			<property name="needClientAuth" value="false" />-->
<!--			<property name="wantClientAuth" value="false" />-->
			
			<!-- ssl配置参数结束 -->
		</map>
	</property>
	
		<!--  
			mina框架的ssl client参数
		-->
	<property name="rpc.protocol.mina.ssl.client">
		<map>
			<!-- 连接超时时间，默认值60秒，单位：秒 -->
			<property name="keyStore" value="keystore/client.ks" />
				<!-- 
					服务器绑定端口
				 -->
			<property name="keyStorePassword" value="123456" />
				<!-- 
					服务器绑定ip
				 -->
			<property name="trustStore" value="keystore/client.ts" />
			
			<property name="trustStorePassword" value="123456" />
		</map>
	</property>
	
	<!--  
			mina框架的ssl server参数
		-->
	<property name="rpc.protocol.mina.ssl.server">
		<map>
			<!-- 连接超时时间，默认值60秒，单位：秒 -->
			<property name="keyStore" value="keystore/server.ks" />
				<!-- 
					服务器绑定端口
				 -->
			<property name="keyStorePassword" value="123456" />
				<!-- 
					服务器绑定ip
				 -->
			<property name="trustStore" value="keystore/server.ts" />
			
			<property name="trustStorePassword" value="123456" />
		</map>
	</property>

o 修改以下bug
实现org.frameworkset.spi.DisposableBean接口的组件不管是单实例模式还是多例模式，都会将组件实例添加
到待销毁的对象池中（java虚拟机关闭前会主动销毁放在销毁池中的对象，也就是调用DisposableBean接口中的destroy方法），
这样单实例的对象是没有问题的，但是多实例的组件就会有问题（对象泄漏的问题），这个问题必须修改。
修改方法：多实例的组件（singlable属性为false)即使实现了org.frameworkset.spi.DisposableBean接口也不会
放到销毁池中。
涉及的程序：
/bbossaop/src/org/frameworkset/spi/assemble/BeanAccembleHelper.java--根据BeanInf的接口isSinglable判断DisposableBean组件是否要放入销毁池
/bbossaop/src/org/frameworkset/spi/assemble/BeanInf.java --增加接口 public boolean isSinglable();
/bbossaop/src/org/frameworkset/spi/assemble/SecurityProviderInfo.java--实现接口 public boolean isSinglable();
-----------------------------------------------
2010-03-25
-----------------------------------------------------
o 集成quartz调度引擎
源码程序包：src-schedule
任务调度配置文件：/bbossaop/resources/org/frameworkset/task/quarts-task.xml
quartz配置文件：/bbossaop/resources/quartz.properties
发布的jar：bboss-schedule.jar
quartz版本：支持所有quartz版本，目前bboss aop框架中集成的版本号为quartz-all-1.6.6.jar
开发文档待补充
-----------------------------------------------
2010-03-22
-----------------------------------------------------

o 增加模块化的单独文件的功能
bbossgroups-1.0及以前的版本全部只支持manager-provider.xml文件为总根配置文件的配置模型
bbossgroups-1.0-rc及以后的版本支持多个配置文件作为根配置文件的配置模型，这种模型中每个根文件表示独立
的组件工厂上下文，彼此之间互不相关，这样必将影响远程服务调用时组件的寻址算法，原来只在一个组件上下文
中寻址，现在有多个上下文，每个上下文中可能存在相同标识的组件，因此重新定义了服务组件的寻址算法，保证调用组件客服端的上下文和组件
服务器端的上下文保持一致。
新增程序：
org.frameworkset.spi.ApplicationContext
ApplicationContext类主要用来构建不同的组件容器的上下文环境，ApplicationContext包含一下以下静态方法：
	/**
	 * 获取默认上下文的bean组件管理容器，配置文件从manager-provider.xml文件开始
	 * @return
	 */
	public static ApplicationContext getApplicationContext()
	/**
	 * 获取指定根配置文件上下文bean组件管理容器，配置文件从参数configfile对应配置文件开始
	 * 不同的上下文件环境容器互相隔离，组件间不存在依赖关系，属性也不存在任何引用关系。
	 * @return
	 */
	public static ApplicationContext getApplicationContext(String configfile)
	
上述两个静态方法用来创建组件容器实例，当创建好ApplicationContext实例后就可以在其上调用与BaseSPIManager组件
中提供的一系列静态方法功能一致的实用方法。默认ApplicationContext组件容器相应方法和BaseSPIManager组件中
提供的方法功能一致。
使用实例：

本地服务调用

	ApplicationContext context = ApplicationContext.getApplicationContext("org/frameworkset/spi/beans/testapplicationcontext.xml");
	  RestfulServiceConvertor convertor = (RestfulServiceConvertor)context.getBeanObject("rpc.restful.convertor");
	
	System.out.println(convertor.convert("a", "rpc.test"));

远程服务调用

	 ApplicationContext context = ApplicationContext.getApplicationContext("org/frameworkset/spi/beans/testapplicationcontext.xml");
     RestfulServiceConvertor convertor = (RestfulServiceConvertor)context.getBeanObject("(mina::192.168.11.102:1186)/rpc.restful.convertor");
     System.out.println(convertor.convert("a", "rpc.test"));




update function list:
------------------------------
 1.0.7 - 2010-03-08
------------------------------
o 增加restful风格rpc服务协议rest,定义的语法如下：
(rest::a/b/c)/rpc.test
系统将逐步解析a/b/c这三个节点的地址：
a,b,c分别代表远程服务器地址
系统根据a,b,c的顺序来路由远程服务调用，首先将远程请求发送到a服务器，然后由a路由到b服务器，再由b路由到c服务器
当c处理完请求后再将结果返回给b，b再返回给a。这样就完成了一层restful风格的远程服务调用过程。

a，b，c分别代表服务器的标识符，这个标识符可以对应于一个真正的物理服务器地址，他们之间的映射关系可以在一个注册中心中维护
每个服务器都会有这样一个注册中心用来存放在本机注册的标识符合物理服务器地址的映射关系。

aop框架提供了一个接口来获取映射关系：
org.frameworkset.spi.remote.restful.RestfulServiceConvertor
接口方法为：
public String convert(String restfuluddi,String serviceid);
其中参数restfuluddi对应于服务器标识符a,b,c，参数serviceid为请求的远程服务id
经过该方法转换的地址：可以为以下格式：
"(" + protocol + "::" +  uri + ")/" + serviceid + "?user=" + user + "&password=" + password;
其中的protocol，uri，user，password就是根据服务器标识符从注册中心中查询出的信息。

以下是一个简单的接口实现：
public class RestfulServiceConvertorImplTest implements RestfulServiceConvertor
{

    public String convert(String restfuluddi, String serviceid)
    {
        if(restfuluddi.equals("a"))
        {
            String uri = "172.16.17.216:1187";
            String user = "admin";
            String password = "123456";
            String protocol = "mina";
            String returl = "(" + protocol + "::" +  uri + ")/" + serviceid + "?user=" + user + "&password=" + password;
            
            return returl;
        }
        else if(restfuluddi.equals("b"))
        {
            String uri = "172.16.17.216:1187";
            String user = "admin";
            String password = "123456";
            String protocol = "mina";
            String returl = "(" + protocol + "::" +  uri + ")/" + serviceid + "?user=" + user + "&password=" + password;
            
            return returl;
        }
        else if(restfuluddi.equals("c"))
        {
            String uri = "172.16.17.216:1187";
            String user = "admin";
            String password = "123456";
            String protocol = "mina";
            String returl = "(" + protocol + "::" +  uri + ")/" + serviceid + "?user=" + user + "&password=" + password;
            
            return returl;
        }
        
        else if(restfuluddi.equals("d"))
        {
            String uri = "172.16.17.216:1187";
            String user = "admin";
            String password = "123456";
            String protocol = "mina";
            String returl = "(" + protocol + "::" +  uri + ")/" + serviceid + "?user=" + user + "&password=" + password;
            
            return returl;
        }
        else 
        {
            String uri = "172.16.17.216:1187";
            String user = "admin";
            String password = "123456";
            String protocol = "jgroup";
            String returl = "(" + protocol + "::" +  uri + ")/" + serviceid + "?user=" + user + "&password=" + password;
            
            return returl;
        }
            
    }

}
RestfulServiceConvertor接口的实现类被配置在manager-rpc-service.xml文件中：
		<!-- 
			restful风格地址转换器
		 -->
		<property name="rpc.restful.convertor" singlable="true"
					      class="org.frameworkset.spi.serviceid.RestfulServiceConvertorImplTest"/>
开发人员可以实现自己的地址转换器，直接替换rpc.restful.convertor配置即可。
				      
再看看一个简单restful风格的服务调用示例：
		RPCTestInf testInf = (RPCTestInf)BaseSPIManager.getBeanObject("(rest::a/b/c/d)/rpc.test");
        //循环执行10次服务调用
        for(int i = 0; i < 10; i ++)
        {
            try
            {
                System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

补充说明：
框架在解析rest风格的地址时，能够自动识别节点对应的地址是否是本地地址，假设存在以下地址：
	(rest::a/b/c)/rpc.test
如果框架在解析地址时，发现a是本地地址，将继续解析下一个地址b，直到碰到一个远程地址时才执行远程服务调用，如果全部是本地地址，那么整个rest风格的远程服务
调用就是一个本地调用。
rest风格地址中节点对应的物理地址的协议可以是aop框架目前提供的协议中的任何一种：jms，webservice，mina，jgroups

rest风格地址后面带的认证参数将被忽略。


o bboss.org.jgroups.protocols.pbcast.STREAMING_STATE_TRANSFER升级到2.9
FIXED for https://jira.jboss.org/jira/browse/JGRP-1136
o 以下类升级到2.9
bboss.org.jgroups.protocols.UNICAST
bboss.org.jgroups.stack.AckReceiverWindow
bboss.org.jgroups.stack.AckSenderWindow
FIXED for https://jira.jboss.org/jira/browse/JGRP-1122

o 以下类升级到2.9
bboss.org.jgroups.protocols.pbcast.NAKACK
bboss.org.jgroups.stack.NakReceiverWindow

FIXED for 
https://jira.jboss.org/jira/browse/JGRP-1104
https://jira.jboss.org/jira/browse/JGRP-1133
------------------------------
 1.0.7 - 2010-02-11
------------------------------
o jgroup 2.7.0 GA 升级到2.8.0 GA


------------------------------
 1.0.7 - 2009-05-05
------------------------------

o 增加权限、认证、消息加密功能，插件式模块-org/frameworkset/spi/manager-rpc-service.xml
<property name="system.securityManager" class="org.frameworkset.spi.security.SecurityManagerImpl">
			<construction>
				<property name="securityconfig" 
					      refid="attr:rpc.security" />	
			</construction>	
		</property>
		
		<property name="rpc.security" >
			<map>
				<property name="rpc.login.module" enable="true" class="org.frameworkset.spi.security.LoginModuleTest"/>
				<property name="rpc.authority.module" enable="true" class="org.frameworkset.spi.security.AuthorityModuleTest"/>
				<property name="data.encrypt.module" enable="true" class="org.frameworkset.spi.security.EncryptModuleTest"/>	
			</map>
		</property>
o 增加组播调用结果异常处理功能，一旦一组接口调用中有一个返回异常，则整个调用已异常方式结束
o bean 组件property中的拦截器生效
org/frameworkset/spi/assemble/ProviderParser.java
/bbossaop/src/org/frameworkset/spi/BaseSPIManager.java
测试用例
/bbossaop/test/org/frameworkset/spi/properties/interceptor
o 增加注解方式的数据库事务管理
	@Transaction("REQUIRED_TRANSACTION")
    @RollbackExceptions("") //@RollbackExceptions("{exception1,excpetion2}")
    详细情况参看测试用例/bbossaop/test/org/frameworkset/spi/transaction/annotation
o 属性引用功能完善
	bean创建方式修改
	新增局部bean管理容器（特定的容器管理命名空间），支持组件局部范围引用，和组件

o 完善监控功能
  a). 监控树种增加全局属性property节点和明细查询页面
  b). 支持对property定义的组件节点和明细查询页面

o property元素新增editor子节点，指定属性注入时的属性编辑和转换器

系统默认支持字符串向其他基本类行转换:					 
					 * int,char,short,double,float,long,boolean,byte
					 * java.sql.Date,java.util.Date,
					 * Integer
					 * Long
					 * Float
					 * Short
					 * Double
					 * Character
					 * Boolean
					 * Byte
					如果不是基本数据类型那就需要通过自定义的属性编辑器来实现，属性编辑器必需实现接口:
						com.frameworkset.util.EditorInf
						
						Object getValueFromObject(Object fromValue) ;    
    					Object getValueFromString(String fromValue);


o 扩展aop框架rpc协议，支持mina协议实现rpc服务，目前支持四种协议：jgroup，mina,jms，webservice
详细的配置请看配置文件：
/bbossaop/src/org/frameworkset/spi/manager-rpc-service.xml
/bbossaop/src/org/frameworkset/spi/manager-rpc-webservice.xml
/bbossaop/src/org/frameworkset/spi/manager-rpc-jms.xml
/bbossaop/src/org/frameworkset/spi/manager-rpc-mina.xml

相关依赖的的协议框架版本说明
webservice - apache-cxf-2.2.4
mina - mina-2.0.0-M6
jms - 支持jms 规范接口
jgroup - JGroups-2.7.0.GA

使用方法请看测试用例：
org/frameworkset/spi/remote/xxxx/TestClient.java
org/frameworkset/spi/remote/xxxx/TestServer.java
其中的xxxx标识具体的协议名称：jgroup，mina,jms，webservice

o property节点增加了init-method，destroy-method两个属性
		init-method，destroy-method两个属性分别对应aop框架提供的两个InitializingBean和DisposableBean
		实现的方法，如果组件已经实现了InitializingBean就不需要指定init-method属性
		如果组件实现了DisposableBean接口就不需要指定destroy-method属性
		
o 增加生命周期管理接口
public interface InitializingBean {
    
    void afterPropertiesSet() throws Exception;

}

public interface DisposableBean
{
    /**
     * Invoked by a BeanFactory on destruction of a singleton.
     * @throws Exception in case of shutdown errors.
     * Exceptions will get logged but not rethrown to allow
     * other beans to release their resources too.
     */
    void destroy() throws Exception;
}

InitializingBean接口中定义了afterPropertiesSet方法，组件可以实现这个方法，一旦部署的组件实例被创建后，aop框架将
调用接口方法afterPropertiesSet对组件实例进行初始化

DisposableBean定义了destroy方法，组件可以实现这个方法，一旦系统退出操作被调用时，aop框架将调用组件的destroy方法来
销毁组件实例，以释放资源，适用于单列模式

相关程序
/bbossaop/src/org/frameworkset/spi/BaseSPIManager.java
/bbossaop/src/org/frameworkset/spi/BeanDestroyHook.java
/bbossaop/src/org/frameworkset/spi/assemble/BeanAccembleHelper.java
  
o BaseSPIManager组件中增加了方法：
	public static Object getBeanObject(String name)--用来获取普通组件的实例<通过property定义的组件>
													 通过manager，provider指定的组件任然使用
													 public static Object getProvider(String providerManagerType)接口
													 获取
	
	
	
o 根据新的属性定义语法，修改系统的依赖注入规则

o 增加自定义的Map类型：org.frameworkset.spi.assemble.ProviderParser$ProMap<K, V>
o 增加自定义的Map类型：org.frameworkset.spi.assemble.ProviderParser$ProList<K, V>
o 增加自定义的Map类型：org.frameworkset.spi.assemble.ProviderParser$ProSet<K, V>
o 修改属性配置中property节点的定义：
    properties(property*)
	properties-attributelist{
		name-指定一组属性的分组名称		
	}
	property(property*,map,list,set)
	property-attributelist{
		name-指定属性名称
		label-指定属性的标记
		value-指定属性的值，属性值也可以通过property节点的内置文本指定
		class-指定值对应的java类型名称：int,boolean,string,用户自定义的类型	
		refid-节点属性值为refid对应的属性或者服务提供者的值
		      如果是属性前面用：attr@作为前缀
		      如果引用的是服务：service@作为前缀，
		      对于引用的解析将采用反向索引的方式的替换还没有加载的引用属性和服务
	}
	map(property+)
	list(property+)
	set(property+)

  修改了节点的定义方法
  
  使用说明：
  配置：
  <property name="connection.params">
		<!-- http://activemq.apache.org/maven/activemq-core/xsddoc/http___activemq.org_config_1.0/element/connectionFactory.html -->
			<map>
				<property label="alwaysSessionAsync"  name="alwaysSessionAsync" value="true" class="boolean">
					<description> <![CDATA[If this flag is set then a seperate thread is not used for dispatching messages for each Session in the Connection.
									 However, a separate thread is always used if there is more than one session, 
									or the session isn't in auto acknowledge or dups ok mode]]></description>
				</property>
				<property label="alwaysSyncSend" name="alwaysSyncSend" value="false" class="boolean">
					<description> <![CDATA[Set true if always require messages to be sync sent  ]]></description>
				</property>
				<property label="closeTimeout" name="closeTimeout" value="15000" >
					<description> <![CDATA[Sets the timeout before a close is considered complete. ]]></description>
				</property>
				<property label="copyMessageOnSend" name="copyMessageOnSend" value="true" >
					<description> <![CDATA[Should a JMS message be copied to a new JMS Message object as part of the send() method in JMS.  ]]></description>
				</property>
				<property label="disableTimeStampsByDefault" name="disableTimeStampsByDefault" value="false" >
					 <description> <![CDATA[Sets whether or not timestamps on messages should be disabled or not.]]></description>
				</property>
				<property label="dispatchAsync" name="dispatchAsync" value="true" >
					<description> <![CDATA[ Enables or disables the default setting of whether or not consumers have their messages <a href="http://activemq.apache.org/consumer-dispatch-async.html">dispatched synchronously or asynchronously by the broker</a>. ]]></description>
				</property>
				<property label="objectMessageSerializationDefered" name="objectMessageSerializationDefered" value="false" >
					<description> <![CDATA[When an object is set on an ObjectMessage, the JMS spec requires the object to be serialized by that set method. ]]></description>
				</property>
				<property label="optimizeAcknowledge" name="optimizeAcknowledge" value="false" >
					<description> <![CDATA[ ]]></description>
				</property>
				<property label="optimizedMessageDispatch" name="optimizedMessageDispatch" value="true" >
					<description> <![CDATA[ If this flag is set then an larger prefetch limit is used - only applicable for durable topic subscribers. ]]></description>
				</property>
				<property label="producerWindowSize" name="producerWindowSize" value="0" >
					<description> <![CDATA[ producerWindowSize ]]></description>
				</property>
				<property label="statsEnabled" name="statsEnabled" value="false" >
					<description> <![CDATA[ statsEnabled ]]></description>
				</property>
				<property label="useAsyncSend" name="useAsyncSend" value="false" >
					<description> <![CDATA[ Forces the use of <a href="http://activemq.apache.org/async-sends.html">Async Sends</a> which adds a massive performance boost; but means that the send() method will return immediately whether the message has been sent or not which could lead to message loss.   ]]></description>
				</property>
				<property label="useCompression" name="useCompression" value="false" >
					<description> <![CDATA[  Enables the use of compression of the message bodies   ]]></description>
				</property>
				<property label="useRetroactiveConsumer" name="useRetroactiveConsumer" value="false" >
					<description> <![CDATA[  Sets whether or not retroactive consumers are enabled   ]]></description>
				</property>
				<property label="watchTopicAdvisories" name="watchTopicAdvisories" value="true" >
					<description> <![CDATA[  watchTopicAdvisories   ]]></description>
				</property>
				<property label="sendTimeout" name="sendTimeout" value="0" >
					<description> <![CDATA[  sendTimeout   ]]></description>
				</property>
				<property label="durableTopicPrefetch" name="durableTopicPrefetch" value="100">
					<description> <![CDATA[  durableTopicPrefetch   ]]></description>
				</property>
				<property label="inputStreamPrefetch" name="inputStreamPrefetch" value="100" >
					<description> <![CDATA[  inputStreamPrefetch   ]]></description>
				</property>
				<property label="maximumPendingMessageLimit" name="maximumPendingMessageLimit" value="0" >
					<description> <![CDATA[  maximumPendingMessageLimit   ]]></description>
				</property>
				<property label="optimizeDurableTopicPrefetch" name="optimizeDurableTopicPrefetch" value="1000" >
					<description> <![CDATA[  optimizeDurableTopicPrefetch  ]]></description>
				</property>
				<property label="queueBrowserPrefetch" name="queueBrowserPrefetch" value="500" >
					<description> <![CDATA[  queueBrowserPrefetch]]></description>
				</property>
				<property label="queuePrefetch" name="queuePrefetch" value="1000" >
					<description> <![CDATA[  queuePrefetch]]></description>
				</property>
				<property label="topicPrefetch" name="topicPrefetch" value="32766" >
					<description> <![CDATA[ topicPrefetch ]]></description>
				</property>
				<property label="是否使用链接池" name="useConnectionPool" value="是否使用链接池" >
					<description> <![CDATA[ true使用，false不使用 ]]></description>
				</property>				
			</map>
		</property>	
		
		java程序：
  ProMap map = BaseSPIManager.getMapProperty("connection.params");
        System.out.println(map);
        System.out.println("alwaysSessionAsync：" + map.getBoolean("alwaysSessionAsync"));
        System.out.println("closeTimeout：" + map.getInt("closeTimeout"));
        System.out.println("useConnectionPool String：" + map.getString("useConnectionPool"));
        System.out.println("useConnectionPool Pro object:：" + map.getPro("useConnectionPool"));
        
        /**
         * 非法获取整数
         */
        try
        {
            System.out.println("useConnectionPool int：" + map.getInt("useConnectionPool"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        /**
         * 非法获取boolean
         */
        try
        {
            System.out.println("useConnectionPool boolean：" + map.getBoolean("useConnectionPool"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
o BaseSPIManager新增获取当前所有连通的节点清单的方法
/**
     * 获取当前所有连通的节点清单
     */
    @SuppressWarnings("unchecked")
    public static List<RPCAddress> getAllNodes()
    
  具体的使用方法为：
  public static void testGetAllNodes()
    {
        List<RPCAddress> addrs = BaseSPIManager.getAllNodes();
    }  
    
   RPCAddress的定义如下：
   public class RPCAddress
{
    private String ip;
    private InetAddress ipAddress;
    public String getIp()
    {
        return ip;
    }
    public void setIp(String ip)
    {
        this.ip = ip;
    }
    public int getPort()
    {
        return port;
    }
    public void setPort(int port)
    {
        this.port = port;
    }
    private int port;
    public RPCAddress(String ip,int port)
    {
        this.ip = ip;
        this.port = port;
    }
    public RPCAddress(InetAddress ipAddress, int port)
    {
        this.ipAddress = ipAddress;
        this.port = port;
        this.ip = this.ipAddress.getHostAddress();
    }
    
    public String getHostName()
    {
        return this.ipAddress.getHostName();
    }
    
    public String getCanonicalHostName()
    {
        return this.ipAddress.getCanonicalHostName();
    }
} 
o 修改D:\workspace\bbossaop\src\com\chinacreator\spi\assemble\ProviderParser.java类
	增加ProMap内部静态类，这个类扩展了java.util.HashMap类，增加了以下方法用来提供int，
	boolean，String三种类型属性的获取和默认值获取方法：
	
	public int getInt(String key)
	public int getInt(String key,int defaultValue)
	public boolean getBoolean(String key)
	public boolean getBoolean(String key,boolean defaultValue)
	public String getString(String key)
	public String getString(String key,String defaultValue)
	
	bboss aop框架将会采用ProMap类作为所有Map类型的参数存储容器
	
o 更新数据库连接池
bbossaop/lib/frameworkset-pool.jar

o 扩展属性property节点功能
  支持list和map两种类型的属性，支持属性嵌套
 bbossaop/src/org/frameworkset/spi/BaseSPIManager.java
bbossaop/test/com/chinacreator/spi/properties/TestProperties.java
/bbossaop/test/com/chinacreator/spi/properties/mq-init.xml
bbossaop/src/org.frameworkset.spi.assemble.ProviderParser 
bbossaop/src/org/frameworkset/spi/assemble/ServiceProviderManager.java
/bbossaop/src/manager-provider.xml
/bbossaop/src/org/framework/persitent/util/SQL.java

BaseSPIManager.java中增加以下接口：
public static Object getObjectProperty(String name) ;
public static Object getObjectProperty(String name,Object defaultValue) ;
public static Set getSetProperty(String name) ;
public static Set getSetProperty(String name,Set defaultValue) ;
public static List getListProperty(String name) ;
public static List getListProperty(String name,List defaultValue) ;
public static Map getMapProperty(String name) ;
public static Map getMapProperty(String name,Map defaultValue) ;

在文件/bbossaop/test/com/chinacreator/spi/properties/mq-init.xml中包含了map，list，set参数的配置实例。
  
o 升级远程通讯组件，提升远程通讯的性能
2009年5月2日

升级jgroup
jgroup由2.4.1 升级到2.7.0

实现bboss aop 远程服务的安全与认证功能
/bbossaop/src/org/frameworkset/remote/SecurityContext.java

实现思路：

认证，增加用户口令和密码验证机制，采用ssl 安全的网络连接
鉴权，支持组件角色访问控制功能，支持组件方法访问控制功能


实现多种网络传播方式

unicast::192.19:1180;192.19:1182 单播调用方式
muticast::192.19:1180;192.19:1182 多播调用方式，要求每个节点必须是当前集群中的节点，否则系统报错

实现路由功能

实现unicat不依赖于组播功能

o 实现异步回调功能

o 协议文件配置
tcp 
udp -默认值
增加以下属性
<property name="cluster_protocol" value="udp"/>	
增加配置文件
replSync-service-aop-tcp.xml

o  事件管理框架远程事件无法发送的问题
原因是：
/bbossaop/src/org/frameworkset/remote/Target.java类没有序列化导致其子类
/bbossevent/src/org/frameworkset/event/EventTarget.java在发送时无法识别父类target中的对象

o 修改属性定义语法，可以通过节点值指定属性值
propertie节点中可以包含属性值
修改的程序
/bbossaop/src/org/frameworkset/spi/assemble/ProviderParser.java

提供property节点的校验功能，如果没有指定name属性，在后台抛出相应的异常信息

o  单列模式下创建对象实例的同步锁
修改的程序如下
D:\workspace\bbossaop\src\com\chinacreator\spi\assemble\SecurityProviderInfo.java
public Object getSingleProvider(Context parent) 
 /**
     * 单列模式下创建对象实例的同步锁
     */
    private Object lock = new Object();

o 增加sql语句配置管理处理类
D:\workspace\bbossaop\src\org\framework\persitent\util\SQL.java
通过这个类，我们可以先将不同数据库的sql语句配置在aop框架全局属性文件中，然后通过这个SQL.java程序来获取不同的数据库类型的ql语句
例如：
<properties name="insertContact">		
		<property name="insertContact-mysql">
			<![CDATA[insert into CONTACTS (ANNIVERSARY_DAY, ANNIVERSARY_MONTH, BIRTH_DAY, 
					BIRTH_MONTH, EMAIL_ALTERNATE, EMAIL_PRIMARY, 
					FIRST_NAME, GSM_NO_ALTERNATE, GSM_NO_PRIMARY, HOME_ADDRESS, 
					HOME_CITY, HOME_COUNTRY, HOME_FAKS, HOME_PHONE, 
					HOME_PROVINCE, HOME_ZIP, LAST_NAME, MIDDLE_NAME, 
					NICK_NAME, PERSONAL_NOTE, SEX, SPOUSE_NAME, TITLE, 
					USERNAME, WEB_PAGE, WORK_ADDRESS, 
					WORK_ASSISTANT_NAME, WORK_CITY, WORK_COMPANY, WORK_COUNTRY,
					 WORK_DEPARTMENT, WORK_FAKS, WORK_JOB_TITLE, WORK_MANAGER_NAME, 
					 WORK_OFFICE, WORK_PHONE, WORK_PROFESSION, WORK_PROVINCE, 
					 WORK_ZIP) 
					 values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 
					 ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)]]> 
	
		</property>
		<property name="insertContact-oracle">
			<![CDATA[insert into CONTACTS (id,ANNIVERSARY_DAY, ANNIVERSARY_MONTH, BIRTH_DAY, 
					BIRTH_MONTH, EMAIL_ALTERNATE, EMAIL_PRIMARY, 
					FIRST_NAME, GSM_NO_ALTERNATE, GSM_NO_PRIMARY, HOME_ADDRESS, 
					HOME_CITY, HOME_COUNTRY, HOME_FAKS, HOME_PHONE, 
					HOME_PROVINCE, HOME_ZIP, LAST_NAME, MIDDLE_NAME, 
					NICK_NAME, PERSONAL_NOTE, SEX, SPOUSE_NAME, TITLE, 
					USERNAME, WEB_PAGE, WORK_ADDRESS, 
					WORK_ASSISTANT_NAME, WORK_CITY, WORK_COMPANY, WORK_COUNTRY,
					 WORK_DEPARTMENT, WORK_FAKS, WORK_JOB_TITLE, WORK_MANAGER_NAME, 
					 WORK_OFFICE, WORK_PHONE, WORK_PROFESSION, WORK_PROVINCE, 
					 WORK_ZIP) 
					 values (SEQ_CONTACT.NEXTVAL,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 
					 ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)]]> 
	
		</property>
</properties>

我们通过SQL.java提供的方法来获取相应的sql语句：

String sql = SQL.getSQL(dbname,"insertContact");
说明:
dbname对应于poolman.xml文件中配置的数据源中的dbname，持久层框架会自动识别对应的数据源的数据库类型，然后获取到
不同的insertContact语句，假如dbname对应的数据库是mysql，那么就会获取到insertContact-mysql对应的sql语句
如果是oracle，那么获取到的insertContact-oracle对应的语句。


     

------------------------------
 1.0.6 - 2009-04-24
------------------------------

o add rpc component to bboss aop,supports rpc service like ejb or rmi using bboss aop.

o add global system property confige function

more details see my blog [http://blog.csdn.net/yin_bp]

1.	增加远程管理组件
2.	实现aop 框架远程服务调用功能
3.	增加全局属性配置功能



------------------------------
 1.0.5 - 2009-02-12
------------------------------
 
 o extend ioc function, support constructor ioc,such as:
   
   <manager id="constructor.a" 
		singlable="true">
		<provider type="A" default="true"
			class="com.chinacreator.spi.constructor.ConstructorImpl" />		
		<construction>	
			<param value="hello world"/>
			<param refid="interceptor.a"/>
			<param type="com.chinacreator.spi.constructor.Test"/>
		</construction>
	</manager>
   
   note:
   add construction node to support construction ioc.
   sub node param of construction give contructor's parameters,the param node order of construction node is according to 
   the order of Constructor of the service.
   throw param node you can :
       give the value of a constructor parameter.
       give a service reference for a constructor parameter.
       give a common class that can been instance by the ioc container for a constructor parameter.    
 o extend properties ioc ，add a properties [class] for reference node to support:
      give a common class that can been instance by the ioc container to set the value of a field of the service.
      such as:
      <reference fieldname="test" class="com.chinacreator.spi.reference.Test"/>
      

------------------------------
 1.0.4 - 2009-02-05
------------------------------
 o update frameworkset-util.jar.
 o update frameworkset-pool.jar.
 o update frameworkset.jar.
 o support all interfaces of spi service.
   expample:
   class A implements interface AI,and AI extends interface BaseAI,and so on;
   A extends class BaseA and BaseA implements interface IBaseAI and so on;
   
   then you config service A in manager-provider-xxxx.xml with name servevie.a,use BaseSPIManager to get an instance of A,the following is allowed:
   
   AI inst = (AI)BaseSPIManager.getProvider("servevie.a");
   BaseAI inst = (BaseAI)BaseSPIManager.getProvider("servevie.a");
   IBaseAI inst = (IBaseAI)BaseSPIManager.getProvider("servevie.a");
   
   
 
 
------------------------------
 1.0.3 - 2009-02-04
------------------------------
 o Fixed java.lang.NullPointerException in org/frameworkset/spi/interceptor/InterceptorWrapper.java.
 
 
------------------------------
 1.0.2 - 2008-12-25
------------------------------ 


------------------------------
 1.0.1 - 2008-12-24
------------------------------ 
First version of bboss-aop-1.0.1 released.

[http://blog.csdn.net/yin_bp]
[http://www.openbboss.com]