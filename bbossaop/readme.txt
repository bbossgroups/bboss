directory:
src--source code
test--test source code
sql-- db sql
lib--bboss-aop framework depends jars
bin--bboss-aop release package.

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
	bboss-aop.jar,bboss-camel.jar,bboss-mina.jar,bboss-jms.jar,bboss-ws.jar
	
	
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
o 发布包名---bboss-aop.jar src test src-log        
    包含内容：
  classes下的com包，org包，bboss-magic-map.xml

o 发布包名---bboss-ws.jar src-cxf

o 发布包名---bboss-camel.jar src-camel

o 发布包名---bboss-mina.jar src-mina

o resources---基本资源文件目录，存放配置文件



部署说明：
o distrib目录中包含了bboss aop框架发布的jar包：
bboss-aop.jar - aop框架的核心jar包
bboss-camel.jar - bboss camel路由组件包，提供camel路由规则支持，从而实现bboss组件之间的路由功能，包括本地服务调用路由和远程服务调用路由
bboss-jms.jar - bboss aop框架提供jms调用的标准接口，符合jms 1.1规范，同时提供了bboss aop远程服务调用的jms协议实现
bboss-mina.jar - bboss aop框架远程服务调用的mina协议实现
bboss-ws.jar - bboss aop框架中的webservcie服务发布框架实现，目前支持apache cxf 2.2.4，同时提供了bboss aop远程服务调用的webservice协议实现
jgroups.jar - 基于JGroups-2.8.0.GA扩展包，添加了bboss aop框架的认证、鉴权、加/解密功能

同时也包含了jms，mina，webservice运行时依赖的jar包：
lib\camel- camel组件库 版本为camel 2.0
lib\cxf - webservice框架apache cxf 2.2.4组件包

lib\jms - jms规范jar包，active mq组件库 active mq 5.2.0
lib\mina - mina协议组件库 ，apache mina-2.0.0-M6
lib\ bboss aop框架依赖的一下工具包

o resource目录为bboss aop部署配置文件的存放目录，部署到运行环境时，需要将resource下的xml文件拷贝到
classpath根目录下，例如classes目录。



more details see my blog [http://blog.csdn.net/yin_bp]

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
o bean 组件property中的拦截器添加生效
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