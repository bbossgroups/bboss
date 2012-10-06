release version : bbossgroups-3.6.0
release date: 2012/10/06

bboss group website:
http://www.bbossgroups.com

bboss group project blog:
http://yin-bp.iteye.com/

bboss group project sourceforge site url:
http://sourceforge.net/projects/bboss/files/

bboss group project github source url:
https://github.com/bbossgroups/bbossgroups-3.5

bbossgroups-3.6.0 release futures

一、IOC模块
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

二、bboos-mvc mvc模块
o 增加word、word转pdf插件
o 添加注解org.frameworkset.web.servlet.handler.annotations.ExcludeMethod，标注方法不是mvc控制器方法，添加了ExcludeMethod注解的方法就不会被加入到mvc控制器的方法注册表中
o 完善控制器方法解析算法，排除属性的get/set方法，加强系统安全性
o 增加locale标签,用来输出国家代码，使用方法：
<pg:locale/>
输出：en_US,zh_CN等等
o mvc国际化配置调整
在web.xml文件的DispatchServlet中增加以下配置
<init-param>
			<param-name>messagesources</param-name>
			<param-value>/WEB-INF/messages,/WEB-INF/messages1</param-value>
		</init-param>
		<init-param>
			<param-name>useCodeAsDefaultMessage</param-name>
			<param-value>true</param-value>
		</init-param>
messagesources用来指定国际化配置文件清单
useCodeAsDefaultMessage用来指定如果没有在相应的配置文件中找到相应的code对应的配置项是否直接输出code，默认值为true
为true时输出，false不输出	
完整的配置如下：
<servlet>
		<servlet-name>mvcdispather</servlet-name>
		<servlet-class>org.frameworkset.web.servlet.DispatchServlet</servlet-class>
		<init-param>
			<param-name>contextConfigLocation</param-name>
			<param-value>/WEB-INF/bboss-*.xml,/WEB-INF/conf/bboss-*.xml</param-value>
		</init-param>
		<init-param>
			<param-name>messagesources</param-name>
			<param-value>/WEB-INF/messages,/WEB-INF/messages1</param-value>
		</init-param>
		<init-param>
			<param-name>useCodeAsDefaultMessage</param-name>
			<param-value>true</param-value>
		</init-param>
		<load-on-startup>0</load-on-startup>
	</servlet>
o mvc控制器方法响应插件MappingJacksonHttpMessageConverter支持jsonp数据响应（跨站跨域通讯协议）
使用方法如下：
服务器端：
服务器端部署在实例工程：bestpractice\demoproject中，服务端程序为/demoproject/src/org/frameworkset/mvc/HelloWord.java

服务端控制器方法通过制定注解@ResponseBody的datatype属性为jsonp来指示mvc框架需要将响应值转换为jsonp函数+json参数数据的合法JavaScript脚本返回到客户端
以便实现跨站跨域请求交互，HelloWord.java提供了两个方法一个是应用于使用jquery的请求，一个应用于JavaScript标记请求
public @ResponseBody(datatype="jsonp") JsonpBean jsonpwithjquery()
	{
		JsonpBean jsonpbean = new JsonpBean();
		jsonpbean.setPrice("91.42");
		jsonpbean.setSymbol("IBM jquery jsonp");
		return jsonpbean;

	}
	public @ResponseBody(datatype="jsonp") JsonpBean jsonp()
	{
		JsonpBean jsonpbean = new JsonpBean();
		jsonpbean.setPrice("91.42");
		jsonpbean.setSymbol("IBM");
		return jsonpbean;
	}
	分别对应请求地址为：
	http://localhost:8081/demoproject/examples/jsonpwithjquery.page
	http://localhost:8081/demoproject/examples/jsonp.page	
客户端实现:
客户端应用和服务器应用属于两个不同站点或者不同域名，
客户端部署在bboss-mvc的web应用中,对应的访问地址为：
http://localhost:8081/bboss-mvc/jsp/jsonp/testjsonp.jsp
testjsonp.jsp相关的代码如下：
<!-- 普通的jsonp调用示例开始，定义跨域回调函数 -->
	<script type="text/javascript">
        	function jsonpCallback(result)
        	{
				alert("aaa:" + result.symbol);//弹出跨站 请求返回的json数据对象的symbol属性的值
        	}
    	</script>
    <!-- 普通的jsonp调用示例，向另一个应用demoproject发起mvc请求，并指参数callback（参数名字可任意指定）指定回调函数jsonpCallback-->
	<script type="text/javascript" src="http://localhost:8081/demoproject/examples/jsonp.page?jsonp_callback=jsonpCallback"></script>
	<!-- 普通的jsonp调用示例结束-->
	
	<!-- 采用jquery实现jsonp调用示例开始-->
	<script src="<%=request.getContextPath() %>/include/jquery-1.4.2.min.js" type="text/javascript"></script>   
	<!-- 采用jquery实现jsonp调用示例--> 
	<script type="text/javascript">
        $(function() {
            $.getJSON("http://localhost:8081/demoproject/examples/jsonpwithjquery.page?jsonp_callback=?", function(data) {
            	alert("bbb:" + data.symbol);//弹出跨站 请求返回的json数据对象的symbol属性的值
            });
            //jsonp1337140657188({"postalcodes":[{"adminName2":"Westchester","adminCode2":"119","postalcode":"10504","adminCode1":"NY","countryCode":"US","lng":-73.700942,"placeName":"Armonk","lat":41.136002,"adminName1":"New York"}]});
            $.getJSON("http://www.geonames.org/postalCodeLookupJSON?postalcode=10504&country=US&callback=?", function(data) {
            	alert( data.postalcodes[0].adminName2);//这是一个互联网跨域调用的实例，确保能够上网，弹出跨站 请求返回的json数据对象数组属性的第一个元素的属性adminName2的值
            }); 
        });        
    </script>
	<!-- 采用jquery实现jsonp调用示例结束-->

我们bboss mvc对jsonp提供了默认的支持，服务端json数据请求分别带了参数jsonp_callback
http://localhost:8081/demoproject/examples/jsonpwithjquery.page?jsonp_callback=?
jsonpCallback=?，是因为使用了jquery的$.getJSON方法来发起该请求，回调函数是个匿名函数，jquery框架会为该匿名函数产生一个随机函数名称，然后将?替换为实际的函数名称
提交给服务器端
http://localhost:8081/demoproject/examples/jsonp.page?jsonp_callback=jsonpCallback
jsonp_callback=jsonpCallback，可以看出我们已经直接指定了回调函数的名称，就是之前定义的实名函数jsonpCallback()

需要说明的是，回调函数对应的参数名称jsonp_callback是MappingJacksonHttpMessageConverter内置的默认的回调函数参数名称，
我们可以全局地改变这个参数的名称，在bboss-mvc.xml文件中在MappingJacksonHttpMessageConverter插件上修改f:jsonpCallback属性的值即可：
<property class="org.frameworkset.http.converter.json.MappingJacksonHttpMessageConverter"
					f:jsonpCallback="jsonp_callback"/>

	
执行实例：
我们只需要将bboss-mvc下的WebRoot和bestpractice\demoproject\WebRoot对应的应用部署到tomcat并启动tomcat，然后在浏览器中输入
http://localhost:8081/bboss-mvc/jsp/jsonp/testjsonp.jsp
既可以看到实际的效果	
o org.frameworkset.http.converter.FileMessageConvertor插件支持下载Resource接口对应的资源
目前支持Resource接口的以下实现：
ClassPathResource -- 适用于应classpath下面的资源
ServletContextResource --适用于web应用根目录及子目录下的资源
FileSystemResource --适用于文件系统中文件资源
UrlResource --适用于url连接对应资源
ByteArrayResource--适用于二进制资源

使用方法如下：
public @ResponseBody
	Resource exportExeclTemplate(HttpServletRequest request, HttpServletResponse response, String excelType,
			String module) throws Exception {
		
		String fileName = "com/sany/mms/background/action/exceldata.xls";
		
		ClassPathResource classpath = new ClassPathResource(fileName);
		return classpath;
	}

------2012-05-15-------------
o 附件上传功能完善：
1.支持html5附件上传，目前只支持单次上传一个文件
2.增加IgnoreFieldNameMultipartFile接口用来标识不知道input file的名称的情况下，将上传的附件与方法参数或者对象属性进行绑定
public @ResponseBody String upload(IgnoreFieldNameMultipartFile[] filedata,String testparam) throws IllegalStateException, IOException
public @ResponseBody String upload(IgnoreFieldNameMultipartFile filedata,String testparam) throws IllegalStateException, IOException
详情请查看测试用例：
\bestpractice\demoproject\src\org\frameworkset\mvc\FileController.java
\bestpractice\demoproject\WebRoot\xheditor\demos\demo08.jsp

------2012-05-08------------- 
o mvc国际化功能完善：
增加org.frameworkset.web.servlet.i18n.SessionLocalResolver类，以便从session中获取用户登录时存储的Locale对象，默认的key为
org.frameworkset.web.servlet.i18n.SESSION_LOCAL_KEY
如果用户需要使用SessionLocalResolver必须在bboss-mvc.xml文件中增加以下配置：
 <property name="localeResolver" class="org.frameworkset.web.servlet.i18n.SessionLocalResolver"/>
以便覆盖默认的LocalResolver组件org.frameworkset.web.servlet.i18n.AcceptHeaderLocaleResolver(默认根据客户端所处的Locale来解析相应的code)

mvc国际化标签的使用方式：
<pg:message  code="probe.jsp.wrongparams"/>

mvc国际化组件messageSource的获取方法：
org.frameworkset.spi.support.MessageSource messageSource = org.frameworkset.web.servlet.support.WebApplicationContextUtils.getWebApplicationContext();
详情请参考测试jsp页面：/bboss-mvc/WebRoot/jsp/demo.jsp

mvc国际化配置文件路径默认为
/bboss-mvc/WebRoot/WEB-INF/messages.properties
/bboss-mvc/WebRoot/WEB-INF/messages_zh_CN.properties
/bboss-mvc/WebRoot/WEB-INF/messages_en_US.properties
等等
如果要覆盖默认的配置，请修改bboss-mvc.jar/org/frameworkset/web/servlet/DispatcherServlet.properties文件内容：
messageSource.basename=/WEB-INF/messages
这里不需要带国家标识，系统会自动在/WEB-INF/目录下查找对应国家语言的配置文件，如果有多个配置文件可以用逗号分割，例如：
messageSource.basename=/WEB-INF/messages,/WEB-INF/messages1,/WEB-INF/messages2

国际化配置文件热加载机制配置：
默认5秒检测文件是否改动，如果有改动mvc框架会重新加载有修改的文件，没有修改的文件不会重新加载
如果想屏蔽改机制请修改bboss-aop.jar/aop.properties文件的选项
#国际化属性文件变更检测时间间隔，单位为毫秒，默认为5秒间隔
resourcefile.refresh_interval=5000
若果>0则启用热加载机制，<=0则屏蔽热加载机制，开发环境请开启，生产环境请关闭
o 改进beaninfo，list标签异常处理方式，将系统级异常输出到日志文件中，日志级别为info级
o bboss mvc 站点资源下载可以点击文件名称下载
o 将官网的文件资源下载列表按照文件名称进行排序
o 增加安全认证过滤器,废除mvc安全认证拦截器,该过滤器具有以下属性：
	preventDispatchLoop = false;//循环跳转检测开关，true启用，false禁用，默认为false
	http10Compatible = true; //whether to stay compatible with HTTP 1.0 clients,true标识兼容，false标识不兼容，默认为true
	redirecturl = "/login.jsp"; //指定检测失败重定向地址，默认为login.jsp,即安全认证检测失败跳转向指定的页面

	/**
	 * 跳转方式，有以下三个值，默认为redirect
	 * include
	 * redirect
	 * forward
	 */
	directtype = "redirect";

	
	//明确指出需要检测的页面范围，多个用逗号分隔，可选，如果没有配置则扫描所有页面（忽略patternsExclude指定的相关页面）
	//可以是指定包含通配符*的页面地址，用来模糊匹配多个页面
	patternsInclude;
	//明确指出不需要检测的页面范围，多个用逗号分隔，可选，如果没有配置则扫描所有页面或者扫描patternsInclude指定的页面
	//可以是指定包含通配符*的页面地址，用来模糊匹配多个页面
	patternsExclude;
配置方法如下：
 <filter> 
    <filter-name>securityFilter</filter-name> 
    <filter-class>org.frameworkset.web.interceptor.MyFirstAuthFilter</filter-class> 
    <init-param> 
      <param-name>patternsExclude</param-name> 
      <param-value> 
    	/login.jsp
       </param-value> 
    </init-param> 
    <init-param> 
      <param-name>redirecturl</param-name> 
      <param-value>/login.jsp</param-value> 
    </init-param> 
    <init-param> 
      <param-name>preventDispatchLoop</param-name> 
      <param-value>true</param-value> 
    </init-param>     
  </filter> 

//要扫描的页面规则配置
  <filter-mapping> 
    <filter-name>securityFilter</filter-name> 
    <url-pattern>*.jsp</url-pattern> 
  </filter-mapping> 
  <filter-mapping> 
    <filter-name>securityFilter</filter-name> 
    <url-pattern>*.page</url-pattern> 
  </filter-mapping> 
  <filter-mapping> 
    <filter-name>securityFilter</filter-name> 
    <url-pattern>*.htm</url-pattern> 
  </filter-mapping>
  <filter-mapping> 
    <filter-name>securityFilter</filter-name> 
    <url-pattern>*.html</url-pattern> 
  </filter-mapping>
   <filter-mapping>  
        <filter-name>securityFilter</filter-name>  
        <url-pattern>*.ajax</url-pattern>  
    </filter-mapping>
    <filter-mapping>  
        <filter-name>securityFilter</filter-name>  
        <url-pattern>/rest/*</url-pattern>  
    </filter-mapping>
    
    
o 改进StringHttpMessageConverter转换器，增加responseCharset属性，用于全局指定@ResponseBody String类型相应的字符编码，
默认值为"ISO-8859-1"，使用方法为f:responseCharset="UTF-8"，例如：
<property class="org.frameworkset.http.converter.StringHttpMessageConverter" f:responseCharset="UTF-8"/>
这样以下的响应字符串将采用UTF-8编码输出：
public  @ResponseBody String queryMutiSex(SexType[] sex)
{
	return "你好";//将以UTF-8编码
}

如果在控制器方法级别指定了字符编码，则将覆盖StringHttpMessageConverter转换器上的字符编码,例如：
public  @ResponseBody(charset="GBK") String queryMutiSex(SexType[] sex)
{
	return "你好";//将以GBK响应编码
}

三、持久层
o TransactionManager组件增加release方法，
	  应用程序在final方法中调用，用来在出现异常时对事务资源进行回收，首先对事务进行回滚，
	  然后回收资源，如果事务没有开启或者已经提交或者已经回滚，则release方法不做任何操作
	
o poolman内置数据源apache common dbcp升级到1.4版本，apache common pool升级到1.5.4，
由于dbcp 1.4只支持jdk 1.6，所以对持久层进行相应的调整，调整后的持久层框架
同时支持在jdk 1.5和jdk 1.6下进行编译和打包（之前的持久层版本只能在jdk 1.5下面打包和编译）。
如果需要在jdk 1.5中使用请使用run.bat进行打包发布，发布的包在distrib目录下
如果需要在jdk 1.6中使用请使用run-jdk6.bat进行打包发布，发布的包在distrib目录下

为了兼容jdk 1.5版本的应用程序，同时在antbuildall工程下面的两个build-all.bat和build-all-jdk16.bat分别对应jdk 5和jdk 6的编译指令

工程目录下增加src-jdk5和src-jdk6两个源码目录，默认采用src-jdk6导入工程，如果环境是1.5请切换至src-jdk5同时将工程的jdk指定为jdk 1.5
o poolman.xml文件增加datasourceFile属性，用来指定定义数据源的ioc配置文件（基于bboss ioc框架）
以c3p0为例进行说明（不同的数据源请参考poolmna-third.xml文件）：
  <datasource>
	
    <dbname>c3p0</dbname>
	<loadmetadata>false</loadmetadata>
	<enablejta>true</enablejta>
    <jndiName>bspf_datasource_jndiname_1</jndiName>
    <datasourceFile>c3p0.xml</datasourceFile>
    <autoprimarykey>false</autoprimarykey>
	<showsql>false</showsql>
	 <!--
        定义数据库主键生成机制
        缺省的采用系统自带的主键生成机制，
        外步程序可以覆盖系统主键生成机制
        由值来决定
        auto:自动，一般在生产环境下采用该种模式，
               解决了单个应用并发访问数据库添加记录产生冲突的问题，效率高，如果生产环境下有多个应用并发访问同一数据库时，必须采用composite模式
        composite：结合自动和实时从数据库中获取最大的主键值两种方式来处理，开发环境下建议采用该种模式，
                   解决了多个应用同时访问数据库添加记录时产生冲突的问题，效率相对较低， 如果生产环境下有多个应用并发访问同一数据库时，必须采用composite模式
    -->
    <keygenerate>composite</keygenerate>

  </datasource>
  其中的<datasourceFile>c3p0.xml</datasourceFile>指定了c3p0 datasource的配置文件地址，c3p0.xml相对应classpath的根路径。
  
c3p0.xml参考测试配置文件/bboss-persistent/resources/c3p0.xml
<property name="datasource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
  <!-- 指定连接数据库的JDBC驱动 -->
  <property name="driverClass" value="oracle.jdbc.driver.OracleDriver"/>  
  <!-- 连接数据库所用的URL -->
  <property name="jdbcUrl"
   value="jdbc:oracle:thin:@//10.0.15.134:1521/orcl">
   <!-- 如果数据库url是加密的，则需要配置解密的编辑器 -->
	<!--<editor clazz="com.frameworkset.common.poolman.security.DecryptEditor"/> -->
   </property>  
  <!-- 连接数据库的用户名 -->
  <property name="user" value="SANYGCMP">
    <!-- 如果账号是加密的账号，则需要配置解密的编辑器 -->
<!--   	<editor clazz="com.frameworkset.common.poolman.security.DecryptEditor"/> -->
  </property>  
  <!-- 连接数据库的密码 -->
  <property name="password" value="SANYGCMP">
  <!-- 如果口令是加密的口令，则需要配置解密的编辑器 -->
<!--   	<editor clazz="com.frameworkset.common.poolman.security.DecryptEditor"/> -->
  </property>
  
  <!-- 设置数据库连接池的最大连接数 -->
  <property name="maxPoolSize" value="20"/>  
  <!-- 设置数据库连接池的最小连接数 -->
  <property name="minPoolSize" value="2"/>
  
  <!-- 设置数据库连接池的初始化连接数 -->
  <property name="initialPoolSize" value="2"/>
  <!-- 设置数据库连接池的连接的最大空闲时间,单位为秒 -->
  <property name="maxIdleTime" value="20"/>  
  <property name="preferredTestQuery" value="select 1 from dual"/>
  
</property>
 
 测试用例
 int num = SQLExecutor.queryObjectWithDBName(int.class, "c3p0", "select 1 from dual")
 
o poolman.xml文件中datasource增加<enablejta>true</enablejta>属性配置
enablejta和jndiName属性结合使用，当enablejta为true时将把数据源转换为TXDataSource注册jndi上下文中
主要应用于托管hibernate和ibatis等持久层框架事务

o 增加对datasource配置文件中对账号和密码的同时加密插件
com.frameworkset.common.poolman.security.DESDBUserAndPasswordEncrypt
o 新增TXDataSource数据源类，用来实现第三方数据库事务代理功能
com.frameworkset.orm.transaction.TXDataSource
TXDataSource可以托管hibernate，ibatis，mybatis等持久层框架的事务管理，原理如下：
我们只需要通过TXDataSource的构造函数传入需要托管事务的实际数据源DataSource即可，这个DataSource可以是bboss内置的数据源，也可以是第三方数据源（common dbcp，C3P0，Proxool ，Druid）等等
public TXDataSource(DataSource datasource)

这样我们只要通过TXDataSource实例的getConnection()方法既可以获取到事务环境中的connection资源从而实现数据库事务的托管功能。
TXDataSource数据源的具体使用方法：
我们以托管开源工作流activiti的事务作为示例，采用bboss内置数据源
在继续之前需要知道一下组件com.frameworkset.common.poolman.util.SQLManager中的两个工具方法：
 public static DataSource getTXDatasourceByDBName(String dbname) --直接获取bboss的内置数据源，并将该数据源转换为一个代理事务的数据源，bboss持久层的poolman.xml文件中需要定义dbname代表的数据源
 public static DataSource getTXDatasource(DataSource ds) --直接将ds数据源转换为一个代理事务的数据源

首先在poolman.xml文件中配置一个名称叫mysql的datasource
 <datasource>

    <dbname>mysql</dbname>
	<loadmetadata>false</loadmetadata>
    <jndiName>jdbc/mysql-ds</jndiName>
    <driver>com.mysql.jdbc.Driver</driver>

     <url>jdbc:mysql://localhost:3306/activiti</url> 

    <username>root</username>
    <password>123456</password>
    .........
</datasource>

然后在activiti的配置文件activiti.cfg.xml中做如下配置：
<properties>
  <property name="processEngineConfiguration" class="org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration">
     <property name="dataSource" factory-class="com.frameworkset.common.poolman.util.SQLManager" factory-method="getTXDatasourceByDBName">
    	<construction>
    		<property value="mysql" />
    	</construction>
    </property>
    <!-- Database configurations -->
    <property name="databaseSchemaUpdate" value="true" />    
    <!-- job executor configurations -->
    <property name="jobExecutorActivate" value="false" />    
    <!-- mail server configurations -->
    <property name="mailServerPort" value="5025" />    
    <property name="history" value="full" />
  </property>
</properties>

从activiti.cfg.xml配置文件中可以看出，我们已经可以使用bboss ioc框架来管理activiti流程引擎，bboss ioc容器中管理的组件都可以用于activiti的相关
活动环节和事件监听器中（该功能另外写文章介绍）。这里需要关注的是配置内容：
<property name="dataSource" factory-class="com.frameworkset.common.poolman.util.SQLManager" factory-method="getTXDatasourceByDBName">
    	<construction>
    		<property value="mysql" />
    	</construction>
    </property>
我们采用bboss ioc的静态工厂模式来定义一个TXDatasource并注入到activiti的org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration
组件中，这样activiti流程引擎的db事务就可以被bboss数据库事务所托管了。bboss数据库事务管理可以参考bbossgroups training.ppt中的相关事务介绍章节。
http://www.bbossgroups.com/file/download.htm?fileName=bbossgroups trainning.ppt

配置好了后就可以，启动流程引擎（相关方法请参考activiti的十分钟指南），看看两个事务管理的代码示例：
创建activiti的用户信息--将两个创建用户操作包含在事务中，activiti采用mybatis作为持久层框架
 TransactionManager tm = new TransactionManager();
    try
    {
	    tm.begin();//开启事务
	    identityService.saveUser(identityService.newUser("kermit"));
	    identityService.saveUser(identityService.newUser("gonzo"));	    
	    tm.commit();//提交事务
    }
    catch(Throwable e)
    {
    	try {
			tm.rollback();//回滚事务
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }

完成流程任务和业务逻辑处理相结合--将业务处理和流程操作包含在一个事务中，activiti采用mybatis作为持久层框架
 // Start process instance
	ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("taskAssigneeExampleProcess");
	TransactionManager tm = new TransactionManager();
	try {
		tm.begin();
		//处理业务逻辑,省略处理代码
		.....
	    // 获取用户任务列表
	    List<Task> tasks = taskService
	      .createTaskQuery()
	      .taskAssignee("kermit")
	      .list();
	    
	    Task myTask = tasks.get(0);
	    //完成任务
	    taskService.complete(myTask.getId());
	    
	    tm.commit();
	} catch (Throwable e) {
		try {
			tm.rollback();
		} catch (RollbackException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}





o 完善sql变量bean类型变量属性引用功能
更新包：
frameworkset-pool.jar
frameworkset-util.jar
o 持久层模板sql变量解析机制由正则表达式切换为bboss自带的变量解析机制，支持以下类型变量：
基本数据类型
日期类型
上述类型组合复杂类型如下：
   数组（一维数组，多维数组）
 List
 Map
 
以下是一个数组变量使用的简单示例：
String[] FIELDNAMES = new
String[]{"ss","testttt","sdds","insertOpreation","ss556"};
		String deleteAllsql = "delete from LISTBEAN where FIELDNAME in
(#[FIELDNAMES[0]],#[FIELDNAMES[1]],#[FIELDNAMES[2]],#[FIELDNAMES[3]],#[FIELDNAMES[4]])";
		Map conditions = new HashMap();
		conditions.put("FIELDNAMES", FIELDNAMES);
		
o 持久层框架模板sql中变量解析功能扩展和优化，由正则表达式切换为自主编写的sql变量解析机制
正则表达式只能解析简单的变量，无法解析复杂的变量格式
     #[HOST_ID]这种格式正则表达式能够解析
     #[HOST_ID->bb[0]]这种带引用的格式，正则表达就不能解析了
     VariableHandler.parserSQLStruction方法可以解析上述两种格式的变量，并且能够将复杂的变量

的信息以Variable列表的方式存储，以供持久层框架对这些变量求值

变量解析功能开发已经完成并经过各种场景的测试，下一步就是就是该机制替换原来的正则表达式方式。
具体的测试方法请查看测试用例：
/bboss-util/test/com/frameworkset/util/TestVaribleHandler.java中的相关方法
public void varialparserUtil()
public void regexUtilvsVarialparserUtil()		、

四、标签库更改
o bboss3.6.0分支相对于之前的分支版本（bboss3.5.1分支和master分支)的最大变化为：
更换cms.jar中程序包路径
com.chinacreator为
com.frameworkset.platform
这样就和bboss-cms[https://github.com/bbossgroups/bboss-cms.git]工程保持一致

bboss3.5.1分支和master分支任然保留对com.chinacreator的支持。

#######update function list since bbossgroups-3.5 begin###########
o cms导航索引样式调整，频道导航调整
o 修复notempty标签当collection集合元素为0时不能正常工作的缺陷
o treedata标签增加rootNameCode属性用来指定树根节点国际化名称
o titile标签增加titlecode属性：
<!--标题国际化代码，如果代码在mvc国际化相关配置文件中不存在，则输出code-->
		<attribute>
			<name>titlecode</name>
			<rtexprvalue>true</rtexprvalue>
		</attribute>
o tabPane标签增加tabTitleCode属性
<!-- 
		tabTitleCode:用来指定tab标题的国际化code，如果指定了tabTitleCode属性，那么就从mvc配置的国际化
		属性文件中获取对应的国际化代码输出。
		如果没有指定相应的code属性，那么输出tabTitle对应的值，如果tabTitle也没有指定则直接则直接输出tabTitleCode属性
		 -->
		<attribute>
			<name>tabTitleCode</name>
			<required>false</required>
			<rtexprvalue>true</rtexprvalue>
		</attribute>		
o 优化COMTree和DataInfoImpl中获取accesscontrol安全访问控制对象的方法，提升性能
o 修改在非jquery模式下index标签设置tagnumber属性后，相应的页码上面没有超链接的bug
<pg:index tagnumber="5" sizescope="10,20,50,100"/>
o 改进beaninfo，list标签异常处理方式，将系统级异常输出到日志文件中，日志级别为info级
o index标签增加usegoimage属性，为true时跳转到后面将出现go条片按钮，false不出现，默认不出现
标签库：convert标签改进，支持各种类型的key，之前只支持String类型的key，现在支持数字类型的key
o cell标签增加encodecount属性，用来指定用utf-8编码输出的次数，有些情况下需要编码2次
使用方法：
首先检查cell标签中是否包含了以下属性定义，如果没有则加到pager-taglib.tld文件中
		<attribute>
			<name>encodecount</name>
			<rtexprvalue>true</rtexprvalue>
		</attribute>
使用方法：
 <a href="<%=request.getContextPath() %>/file/downloadFile.htm?fileName=<pg:cell encode="true" encodecount="2" colName="fileName"/>">下载此文件</a>
 
 服务端控制器方法获取该参数的方法：
 public @ResponseBody File downloadFile(@RequestParam(decodeCharset="UTF-8")
	String fileName, HttpServletRequest request)	
在RequestParam注解中指定decodeCharset为UTF-8即可

修改程序
CellTag.java

o 完善字符过滤器，utf-8编码时，get方式下，在ie浏览器中可以自动识别中文参数，无需在js中escape编码即可解决中文乱码问题
可以在过滤器配置中指定checkiemodeldialog初始化参数为false来禁用该功能，默认值为false，如果要开启则改为true：
    <filter>  
        <filter-name>encodingFilter</filter-name>  
        <filter-class>com.frameworkset.common.filter.CharsetEncodingFilter</filter-class>
	    <init-param>
	      <param-name>checkiemodeldialog</param-name>
	      <param-value>false</param-value>
	    </init-param>
	    //other init parameters.
    </filter>  
o BaseTag和BaseBodyTag实现TryCatchFinally接口
o 逻辑比较标签改进，除了进行字符串比较外还能进行数字比较，只要实际值是数字优先进行数字比较，比较失败后再进行字符串比较
LogicELMatch
LogicEUMatch
LogicLowerMatch
LogicMatchTag
LogicNotMatchTag
LogicUpperMatch

目前只支持数字和String，日期类型的比较，复杂类型不能使用改方法进行比较	

五、bboss-util 工具包
o 修改DameonThread组件，添加监控文件时，判断文件是否已经被监控，如果已经监控则忽略
o com.framworkset.util.StringUtil增加下载Resource接口对应的资源的方法
public static void sendFile_(HttpServletRequest request, HttpServletResponse response, Resource in) throws Exception 
目前支持Resource接口的以下实现：
ClassPathResource -- 适用于应classpath下面的资源
ServletContextResource --适用于web应用根目录及子目录下的资源
FileSystemResource --适用于文件系统中文件资源
UrlResource --适用于url连接对应资源
ByteArrayResource--适用于二进制资源

o ValueObjectUtil增加cast(Object obj,Class toType)方法，用来将父类类型对象obj转换为子类型对象，支持数组类型和普通类型
o 增加变量、数组元素、list/set、map元素变量解析方法,使用方法如下：
 String url = "http://localhost:80/detail.html?user=#[account[0][0]]&password=#[password->aaa[0]->bb->cc[0]]love";
         URLStruction a = com.frameworkset.util.VariableHandler.parserSQLStruction(url);
List<String> tokens = a.getTokens();
	 		for (int k = 0; k < tokens.size(); k++) {
	 			System.out.println("tokens[" + k + "]:" + tokens.get(k));
	 		}
	 		List<Variable> variables = a.getVariables();
	
	 		for (int k = 0; k < variables.size(); k++) {
	
	 			Variable as = variables.get(k);
	
	 			System.out.println("变量名称：" + as.getVariableName());
	 			System.out.println("变量对应位置：" + as.getPosition());
	 			//如果变量是对应的数组或者list、set、map中元素的应用，则解析相应的元素索引下标信息
	 			List<Index> idxs = as.getIndexs();
	 			if(idxs != null)
	 			{
	 				for(int h = 0; h < idxs.size(); h ++)
	 				{
	 					Index idx = idxs.get(h);
	 					if(idx.getInt_idx() > 0)
	 					{
	 						System.out.println("元素索引下标："+idx.getInt_idx());
	 					}
	 					else
	 					{
	 						System.out.println("map key："+idx.getString_idx());
	 					}
	 				}
	 			}
	
	 		}
o 文件下载方法扩展，可以直接将Resource代表的资源进行下载
 public static void sendFile_(HttpServletRequest request, HttpServletResponse response, Resource in)	 		
o 修复ValueObjectUtil日期转换方法多线程安全问题，问题表现为多个不同的用户并发转换时间时，得到不可预期的结果
升级frameworkset-util.jar可以解决这个问题
/bboss-util/src/com/frameworkset/util/ValueObjectUtil.java
o SimpleStringUtil类中增加格式化Exception类为String的方法
o UTF8Convertor类中增加指定目录，指定过滤子目录名列表，指定文件类型列表的编码转换方法
o 解决附件下载中文名称文件在ie 6下无法下载的问题
o util功能：增加字符串编码集识别功能

o 合并StringUtil和SimpleStringUtil中的部分方法，StringUtil只保留和HttpServletRequest相关的的方法
o ValueObjectUtil类中增加一系列数据比较函数

-----------------------------------------------------------------------------------------------

bbossgroups-3.5 release futures


本次版本的主要改进：
1.本版本及后续版本的源码托管到 github
2.aop/ioc支持循环依赖注入功能，支持类似于C指针引用方式的对象属性引用及任何对象内部局部属性的引用，引用层级不受限制,bboss aop框架逐步成为能够做到该功能的少数几个框架之一。
3.序列化机制支持复杂对象及对象之间关系（单向引用，双向引用，树结构，网状结构）序列化和恢复功能，
    使得bboss序列化工具成为少数几个能够实现完全序列化功能的工具之一。
4.全面改进bbossgroups系列组件中使用的java反射机制，支持asm 4.0，全面改进性能和易用性：
   aop框架管理的业务对象、mvc控制器方法参数对象、标签展示对象、持久层or mapping对象中属性不再需要定义get/set方法即可完成值得注入和展示、绑定等功能

5.sourceforge 发布文件增加文件[url=http://sourceforge.net/projects/bboss/files/bbossgroups-3.5/distrib.zip/download]distrib.zip[/url]文件，包含所有框架发布的jars文件目录和框架完整的可运行的web应用bboss.war

更多的信息如下：  
    
一.bboss aop框架更新

o 为了和避免和官方jgroups包冲突，将bboss中的jgroups包路径全部由org.jgroups改为bboss.org.jgroups
jg-magic-map.xml文件名称改为bboss-magic-map.xml
jg-protocol-ids.xml文件名称改为bboss-protocol-ids.xml

为了避免不必要的麻烦，jgroups.jar包名称暂时没有改，如果存在包名称冲突用户可根据实际情况进行修改名称（一般不需要修改）

系统管理平台、应用支持平台、数据交换平台、请求服务平台等系统升级框架到3.5时需要注意，升级完毕后需要修改src-sys/com/chinacreator/remote/Utils.java文件中的以下引用路径：
[code="java"]import org.jgroups.Channel;
import org.jgroups.util.RspList;[/code]

为：
[code="java"]import bboss.org.jgroups.Channel;
import bboss.org.jgroups.util.RspList;[/code]
修改完毕后，将编译好的类文件更新到相应系统的system.jar包中。

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
未完成的协议(暂不做改进)：
jgroups 
因此采用jgroups协议时，所有的参数数据都要实现序列化接口，其他协议则不需要
o 改进http协议和webservice rpc协议性能，改进反射机制
o ioc属性注入中相应的属性不再需要set方法
o 引进最新的bboss soa序列化机制，对于static，final，transient类型的属性不进行序列化，对于添加了@ExcludeField的注解不序列化
需要序列化的字段不再需要get/set方法

---------------------------------------------------------------------
二.bboss mvc更新
------2012-2-7------------
o 集合类型（List,Map）,如果没有数据记录，则直接返回，修复没有数据情况下返回一条空记录的问题
------2011-12-11------------
o 更新最新的bboss aop框架，控制器需要注入的业务组件不再需要set方法
o 改进以下注解，name或者value属性不再是必须选项，如果没有指定name属性，则使用
方法参数名称作为name属性的值：
RequestParam
PathVariable
CookieValue
RequestHeader
Attribute

为了确保能够正确获取到mvc控制器方法的参数名称，必须在编译java代码时保证在class文件中生存java本地变量表
eclipse工具默认会生成，ant编译工具也会生成。
同时除了PathVariable注解，以下注解不在是控制器方法参数的必须注解，如果没有指定，基础类型参数将直接用方法名称从request中获取相关属性的值：
RequestParam
PathVariable
RequestHeader
Attribute
CookieValue

bboss中界定的基础数据类型如下：
		String.class,int.class ,Integer.class,
		long.class,Long.class,
		java.sql.Timestamp.class,java.sql.Date.class,java.util.Date.class,
		boolean.class ,Boolean.class,
		BigFile.class,
		float.class ,Float.class,
		short.class ,Short.class,
		double.class,Double.class,
		char.class ,Character.class,
		
		byte.class ,Byte.class,
		BigDecimal.class

------2011-11-2------------
o 改进jason转换器，避免和文件下载转换器的冲突

----------------------------------------------------------------------
三.bboss persistent更新
o 解决连接池中无法查找到tomcat 6和weblogic 容器数据源的问题
o 解决sql server jtd驱动无法正确找到DB adaptor的问题
o PreparedDBUtil增加public void setBlob(int i, String x) throws SQLException 方法，用来直接向blob类型字段中存入字符串
o 修改TestLob测试用例，用来演示SQLExecutor/ConfigSQLExecutor组件向Blob和clob中插入字符串的方法：
	[code="java"]@Test
	public void testNewSQLParamInsert() throws Exception
	{
		SQLParams params = new SQLParams();
		params.addSQLParam("id", "1", SQLParams.STRING);
		// ID,HOST_ID,PLUGIN_ID,CATEGORY_ID,NAME,DESCRIPTION,DATASOURCE_NAME,DRIVER,JDBC_URL,USERNAME,PASSWORD,VALIDATION_QUERY
		params.addSQLParam("blobname", "abcdblob",
				SQLParams.BLOB);
		params.addSQLParam("clobname", "abcdclob",
				SQLParams.CLOB);
		SQLExecutor.insertBean("insert into test(id,blobname,clobname) values(#[id],#[blobname],#[clobname])", params);
	}
	@Test
	public void testNewBeanInsert() throws Exception
	{
		LobBean bean = new LobBean();
		bean.id = "2";
		bean.blobname = "abcdblob";
		bean.clobname = "abcdclob";
		SQLExecutor.insertBean("insert into test(id,blobname,clobname) values(#[id],#[blobname],#[clobname])", bean);
	}[/code]	
	注意如果
	[code="java"]public static class LobBean
	{
		private String id;
		@Column(type="blob")//blob会通知持久层框架将属性blobname的值作为数据库blob字段类型进行存储
		private String blobname;
		@Column(type="clob")")//clob会通知持久层框架将属性blobname的值作为数据库clob字段类型进行存储

		private String clobname;
	}[/code]	中type分别为blobfile或者clobfile时，就必须要求字段的值类型为以下三种：
	1.java.io.File
	2.MultipartFile
	3.InputStream
	
	例如：
	public static class LobBean
	{
		private String id;
		@Column(type="blobfile")
		private File blobname;
		@Column(type="clobfile")
		private File clobname;
	}
o SQLExecutor/ConfigSQLExecutor组件增加单字段多记录结果类型为List<String>的查询功能，使用方法如下：
使用方法：
@Test
	public void dynamicquery() throws SQLException
	{
		 
		List<ListBean> result =  SQLExecutor.queryList(ListBean.class,"select id  from LISTBEAN");
		 
		 
		 System.out.println(result.size());
		 
		 List<String> result_string =  SQLExecutor.queryList(String.class,"select id  from LISTBEAN");
		 
		 
		 System.out.println(result_string.size());
		 
		 List<Integer> result_int =  SQLExecutor.queryList(Integer.class,"select id  from LISTBEAN");
		 
		 
		 System.out.println(result_int.size());
		 
	}
	
	@Test
	public void dynamicqueryObject() throws SQLException
	{
		 
		ListBean result =  SQLExecutor.queryObject(ListBean.class,"select id  from LISTBEAN");
		 
		 
		 System.out.println(result.getId());
		 
		 String result_string =  SQLExecutor.queryObject(String.class,"select id  from LISTBEAN");
		 
		 
		 System.out.println(result_string);
		 
		 int result_int =  SQLExecutor.queryObject(int.class,"select id  from LISTBEAN");
		 
		 
		 System.out.println(result_int);
		 
	}
o 修复执行clob 文件插入操作导致空指针的问题

sql = "INSERT INTO CLOBFILE (FILENAME,FILECONTENT,fileid,FILESIZE) VALUES(#[filename],#[FILECONTENT],#[FILEID],#[FILESIZE])";
			SQLParams sqlparams = new SQLParams();
			sqlparams.addSQLParam("filename", file.getOriginalFilename(), SQLParams.STRING);
			sqlparams.addSQLParam("FILECONTENT", file,SQLParams.CLOBFILE);
			sqlparams.addSQLParam("FILEID", UUID.randomUUID().toString(),SQLParams.STRING);
			sqlparams.addSQLParam("FILESIZE", file.getSize(),SQLParams.LONG);
			SQLExecutor.insertBean(sql, sqlparams);	
o 优化or mapping性能
o 更新最新的frameworkset-util.jar，持久层or mapping机制的bean的属性不再需要get/set方法
o 将动态添加的数据源的removeAbandoned属性设置为false

---------------------------------------------------------------

四.bboss taglib更新
------2011-12-11------------
o 更新最新的frameworkset-util.jar，cell标签读取的bean的属性不再需要get方法
------2011-11-20------------
o 调整jquery-1.4.2.min.js的load方法支持数组参数的传递
o 调整pager.js的loadPageContent方法支持数组参数的传递
o 分页参数传递支持map，bean中包含数据参数传递，支持数组参数传递，通过以下标签实现：
params标签：<pg:params name="userName" />
beanparams标签：<pg:beanparams name="user"/>

如果要使用这两个标签，需要更新以下程序：
/WEB-INF/lib/frameworkset.jar
/include/jquery-1.4.2.min.js
/include/pager.js


同时需要检查pager-taglib.tld中是否定义了一下params标签和beanparams标签：

<!--
		功能说明：为分页列表标签中自动设置的超链接添加参数数组
	-->
	<tag>
		<name>params</name>
		<tagclass>com.frameworkset.common.tag.pager.tags.ParamsTag</tagclass>
		<bodycontent>empty</bodycontent>
		<attribute>
			<name>id</name>
			<rtexprvalue>true</rtexprvalue>
		</attribute>
		<attribute>
			<name>name</name>
			<required>true</required>
			<rtexprvalue>true</rtexprvalue>
		</attribute>
		
		<attribute>
			<name>encode</name>
			<rtexprvalue>true</rtexprvalue>
		</attribute>
	</tag>
	
	
	<!--
		功能说明：为分页列表标签中自动设置的超链接添加参数，参数来自java对象属性或者Map中键值对
	-->
	<tag>
		<name>beanparams</name>
		<tagclass>com.frameworkset.common.tag.pager.tags.BeanParamsTag</tagclass>
		<bodycontent>empty</bodycontent>
		<attribute>
			<name>id</name>
			<rtexprvalue>true</rtexprvalue>
		</attribute>
		<attribute>
			<name>name</name>
			<required>true</required>
			<rtexprvalue>true</rtexprvalue>
		</attribute>
		
		<attribute>
			<name>encode</name>
			<rtexprvalue>true</rtexprvalue>
		</attribute>
	</tag>
------2011-11-18------------
o 增加params,beanparams标签
params:为分页列表标签中自动设置的超链接添加参数数组
beanparams:为分页列表标签中自动设置的超链接添加参数，参数来自java对象属性或者Map中键值对
bean属性可以指定com.frameworkset.common.tag.pager.IgnoreParam注解，beanparams标签会忽略带有
IgnoreParam注解的属性

------2011-11-2------------
o 修改covert标签空指针异常

-----------------------------------------------------------------
五.bboss util更新

o ClassInfo对象增加isprimary方法，标识对应的类型是否是bboss定义的基础数据类型范畴
o 增加获取方法参数名称的工具类LocalVariableTableParameterNameDiscoverer
获取LocalVariableTableParameterNameDiscoverer的方法如下：
ParameterNameDiscoverer parameterNameDiscoverer = ClassUtil.getParameterNameDiscoverer();
获取方法参数名称的方法如下：
ParameterNameDiscoverer parameterNameDiscoverer = ClassUtil.getParameterNameDiscoverer();
Method method = ClassInfo.class.getMethod("getDeclaredMethod", String.class);
String[] names = parameterNameDiscoverer.getParameterNames(method);
o 内置asm 4.0版本

---------------------------------------------------------------
六.bboss 序列化更新
o 引进序列化机制，对于static，final，transient类型的属性不进行序列化，对于添加了@ExcludeField的注解不序列化
o 对象中序列化的字段不再需要get/set方法
o 支持复杂对象及对象之间关系序列化和恢复功能