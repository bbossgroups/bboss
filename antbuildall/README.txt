release version : bbossgroups-3.4
release date: 2011/10/22

bboss group website:
http://www.bbossgroups.com

bboss group project blog:
http://yin-bp.javaeye.com/

bboss group project sourceforge site url:
http://sourceforge.net/projects/bboss/files/
-----------------------------------------------------------------------------------------------

bbossgroups-3.4 release futures
重点功能：
1.bbossgroups监控模块，以便能够监控spi的所有配置：
ioc组件基本配置信息，所属组件容器类型，所属配置文件路径，属性注入配置，构建函数注入配置，声明式事务配置，拦截器配置信息，mvc跳转路径映射配置信息
rmi服务配置信息，webservice服务配置信息，组件扩展属性配置，全局参数信息配置
sql配置文件信息，sql语句配置信息，sql文件实时扫描信息
监控页面的访问地址：http://www.bbossgroups.com/monitor/console.htm

2.改进webservice服务发布模块，使得服务发布与容器的无关性，兼容所有应用服务器

3.改进rpc框架，增加ClientProxyContext组件，客户端可以通过服务接口生成服务的调用桩程序,客户端不再需要服务组件的aop配置文件和实现类

4.去掉控制器方法参数类型MultipartFile、MultipartFile[]必须和RequestParam注解一起使用的限制

5.改进序列化/反序列化功能，大幅提升性能，并将rpc框架中的序列化/反序化机制由xstream换成自带的序列化控件

具体明细如下：

1.aop/ioc

1.1 增加webservice服务JaxWsProxyFactory组件,用来获取webservice服务组件的客户端调用代理组件
org.frameworkset.spi.remote.webservice.JaxWsProxyFactory
使用方法如下：
 org.frameworkset.web.ws.WSService wsservice = JaxWsProxyFactory.getWSClient("http://localhost:8080/bboss-mvc/cxfservices/mysfirstwsservicePort",
                                             org.frameworkset.web.ws.WSService.class);

1.2 改进远程协议，客户端直接通过ClientProxyContext类根据服务接口生成服务的调用桩程序,客户端不再需要服务组件的aop配置文件和实现类
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

1.3 修复组件生命周期管理缺陷，该问题表现为通过destroy-method指定组件销毁方法不起作用
<property name="test.destorybeans" init-method="init" destroy-method="destroy"
		class="org.frameworkset.spi.beans.DestroyBean"/>

1.4 改进在weblogic上发布webservice服务功能
改进后无需在resources/org/frameworkset/spi/ws/webserivce-modules.xml中配置ws.base.contextpath参数


1.5 完善Pro对象中集合list，map，set，array的监控方法

1.6 解决SOAApplicationContext存在的构造函数缺陷

1.7 修复webservie服务在weblogic上发布失败缺陷，增加如下配置：
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



1.8 完善bbossgroups监控模块，以便能够监控spi的所有配置：
ioc组件基本配置信息，所属组件容器类型，所属配置文件路径，属性注入配置，构建函数注入配置，声明式事务配置，拦截器配置信息，mvc跳转路径映射配置信息
rmi服务配置信息，webservice服务配置信息，组件扩展属性配置，全局参数信息配置
sql配置文件信息，sql语句配置信息，sql文件实时扫描信息
监控页面的访问地址：http://localhost:8080/bboss-mvc/monitor/console.htm

1.9 BaseApplicationContext中增加以下方法
public static BaseApplicationContext getBaseApplicationContext(String configfile)
public Pro getInnerPro(String parent,String name)

---------------------------------------------------------------------
2.mvc
2.1 完善spi组件监控模块
2.2 值对象属性中如果包含MultipartFile类型时，允许表单不是附件上传表单，忽略MultipartFile的绑定操作
给出友好提示：
EvaluateMultipartFileParamWithNoName for type["+ type.getCanonicalName() +"] fail: form is not a multipart form,please check you form config.
2.3 新增框架监控实例，访问地址：
http://localhost:8080/bboss-mvc/monitor/spiFrame.jsp
可以对框架管理的组件、mvc控制器、全局配置属性、sqlfile中的sql语句等配置信息进行监控
2.4 去掉控制器方法参数类型MultipartFile、MultipartFile[]必须和RequestParam注解一起使用的限制

----------------------------------------------------------------------
3.persistent
3.1 完善MultipartFile对象持久化功能，增加友好提示，如果对象属性类型为MultipartFile，要自动存储到数据库的blob或者clob字段时
需要添加@Column(type="blobfile")或者@Column(type="clobfile")；查询大字段数据时，避免将大字段注入到类型为MultipartFile的属性中
3.2 完善ProArray对象序列化可能存在的问题
3.3 SQLParams中无法获取父类bean的字段定义信息
3.4 ResultMap中无法获取父类bean的字段定义信息
3.5 将数字Wraper类型、Boolean、Charaset，Byte等Wraper类型的获取默认值调整为返回null
3.6 SQLParams中getParamJavatype方法对Long，Double，Float，Short，Bloone处理不正确的问题


3.7 解决动态sql语句中，bean属性没有set方法时导致逻辑判断不能正确解析的问题
3.8 优化blob/clob处理，修改某些情况下blob/clob为空时报错的问题

---------------------------------------------------------------
4.taglib
4.1 完善convert标签，将值全部转换为字符串，只允许Map对象中存放的数据的key是字符串
4.2 完善cell标签及其子标签性能，改进使用反射机制的方法

-----------------------------------------------------------------
5.util
5.1 优化ValueObjectUtil中根据属性名称获取字段值得方法，改进性能。
5.2 完善Velocity模板引擎模板路径配置机制，在bboss-aop.jar/aop.properties文件中增加approot配置，
用来指示应用上下文的绝对路径：
approot=D:/workspace/bbossgroups-3.2/bboss-mvc/WebRoot
以便能够查找到对应的模板根目录
由于标签库中使用了vm文件，这些文件存放在approot的/WEB-INF/templates目录下面，因此必须保证Velocity引擎启动后正确地找到
这个目录，在tomcat中是能够自动找到的，但是在weblogic等容器中无法自动找到这个目录，因此需要在bboss-aop.jar/aop.properties文件中增加approot配置

5.3 完善VelocityUtil类，解决找不到velocity.properties文件的问题

5.4 完善类型转换机制，支持lob字段向File/byte[]/String类型的转换

---------------------------------------------------------------
6.序列化反序列化
6.1 性能优化,将一些结构属性采用缩写，具体对应关系如下，减少序列化产生的xml串长度
properties转换为ps
property转换为p
name转换为n
value转换为v
class转换为cs

list转换为l
array转换为a
map转换为m
set转换为s


soa:type_null_value转换为s:nvl
soa:type转换为s:t
componentType转换为cmt

6.2 修复set转换问题
6.3 修复枚举类型转换问题
6.4 修护HashMap，ArrayList，TreeSet子类序列化问题
6.5 增加以下序列化/反序列化api
6.7 对象xml序列化接口完善 
ArrayBean bean1 = new ArrayBean(); 
String xmlcontent = ObjectSerializable.toXML(beanObject); 
ObjectSerializable.toXML(Object obj, Writer out); //将序列化得包输出到out对象中
6.8 xml反序列化接口完善
ArrayBean bean1 = ObjectSerializable. toBean("xmlcontent,ArrayBean.class);
ArrayBean bean1 = ObjectSerializable. toBean(InputStream instream,ArrayBean.class); //反序列化的xml字符串来源于inputstream对象。

