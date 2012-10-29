https://github.com/bbossgroups/bbossgroups-3.5.git
todo

1.url重写规则可以在以下级别指定：
	全局范围级别
	控制器级别
	控制器方法级别

2.添加拦截器功能
拦截器分为2个层次，-，基于框架级别的拦截器，这个是全局拦截器，是单实例模式的

3.国际化功能完善
6.mvc和gwt结合可行性研究

#######update function list since bbossgroups-3.6 begin###########
o list/map标签增加softparser属性，针对sessionKey、requestKey、pagecontextKey进行classdataList数据对象缓存，
 避免重复使用时重复生成数据对象，默认值为true
o 解决mvc 文件上传组件ie6兼容性问题,commons-fileupload-1.2.2在ie6下文件上传报错导致无法文件上传
解决办法，将common file upload的版本回退到1.2，经测试问题解决
o 添加注解org.frameworkset.web.servlet.handler.annotations.ExcludeMethod，标注方法不是mvc控制器方法，添加了ExcludeMethod注解的方法就不会被加入到mvc控制器的方法注册表中
o 完善控制器方法解析算法，排除属性的get/set方法，加强系统安全性
#######update function list since bbossgroups-3.5 begin###########
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


#######update function list since bbossgroups-3.4 begin###########
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
#######update function list since bbossgroups-3.3 begin###########
------2011-09-29-----------
o 完善spi组件监控模块
------2011-09-22-------------
o 值对象属性中如果包含MultipartFile类型时，允许表单不是附件上传表单，忽略MultipartFile的绑定操作
给出友好提示：
EvaluateMultipartFileParamWithNoName for type["+ type.getCanonicalName() +"] fail: form is not a multipart form,please check you form config.
------2011-09-18-------------
o 新增框架监控实例，访问地址：
http://localhost:8080/bboss-mvc/monitor/spiFrame.jsp
可以对框架管理的组件、mvc控制器、全局配置属性、sqlfile中的sql语句等配置信息进行监控
------2011-09-14-------------
o 去掉控制器方法参数类型MultipartFile、MultipartFile[]必须和RequestParam注解一起使用的限制
#######update function list since bbossgroups-3.2 begin###########
------2011-07-24-------------
o 控制器方法参数绑定机制增加MultipartFile、MultipartFile[]类型绑定支持,必须和RequestParam注解一起使用，使用方法如下：
public String uploadFileWithMultipartFile(@RequestParam(name="upload1")  MultipartFile file,
			ModelMap model)
public String uploadFileWithMultipartFiles(@RequestParam(name="upload1")  MultipartFile[] files,
			ModelMap model)
o PO对象属性数据绑定机制增加MultipartFile、MultipartFile[]类型绑定支持,可以和RequestParam注解一起使用，也可以直接与属性名称直接绑定，使用方法如下：
public String uploadFileWithFileBean(FileBean files)

FileBean是一个自定义的java bean，结构如下：
public class FileBean
{
	private MultipartFile upload1;
	@RequestParam(name="upload1")
	private MultipartFile[] uploads;
	@RequestParam(name="upload1")
	private MultipartFile anupload;
	//省略属性的get/set方法
}	
o 完善@ResponseBody注解，增加直接对文件下载功能的支持，只要控制器方法返回File对象即可
o 完善认证拦截器功能，增加认证失败后跳转页面的方式为redirect和forward两种，可以在拦截器上配置directtype属性
 来实现具体的跳转方式：
 <property class="org.frameworkset.web.interceptor.MyFirstInterceptor">
     			<!-- 配置认证检查拦截器拦截url模式规则 -->
     			<property name="patternsInclude">
     				<list componentType="string">
     					<property value="/**/*.htm"/>
     				</list>
     			</property>
     			<!-- 配置认证检查拦截器不拦截url模式规则 -->
     			<property name="patternsExclude">
     				<list componentType="string">
     					<property value="/*.html"/>
     				</list>
     			</property>
     			<property name="redirecturl" value="/login.jsp"/>
     			<property name="directtype" value="forward"/>
     		</property>
o 修复mvc分页跳转页码为负数时，不能正常分页的问题
o 修复ResponseBody指定数据返回类型和字符集不生效的问题
#######update function list since bbossgroups-3.1 begin###########
------2011-06-09------------
o 注解控制器可以不用添加注解@Controller也可以被框架识别了
------2011-06-06------------
o 完善responsebody注解功能,增加datatype和charset两个属性
datatype：json,xml等值，用来指出输出数据的content类型
charset：用来指出reponse响应字符编码
详细使用方法请参考测试用例
org.frameworkset.web.enumtest.EnumConvertController
org.frameworkset.web.http.converter.json.JsonController
------2011-05-28------------
o 支持Map<Key,PO>类型参数绑定机制，通过这种机制可以非常方便地将表单提交过来的一个对象集合数据
根据key对应的字段值，形成Map索引对象。
使用方法如下：
public String mapbean(@MapKey("fieldName") Map<String,ListBean> beans, ModelMap model) {
		String sql = "INSERT INTO LISTBEAN (" + "ID," + "FIELDNAME,"
				+ "FIELDLABLE," + "FIELDTYPE," + "SORTORDER,"
				+ " ISPRIMARYKEY," + "REQUIRED," + "FIELDLENGTH,"
				+ "ISVALIDATED" + ")" + "VALUES"
				+ "(#[id],#[fieldName],#[fieldLable],#[fieldType],#[sortorder]"
				+ ",#[isprimaryKey],#[required],#[fieldLength],#[isvalidated])";
		TransactionManager tm = new TransactionManager();
		try {
			if(beans != null)
			{
				List temp =  convertMaptoList( beans);
				
				tm.begin();
				SQLExecutor.delete("delete from LISTBEAN");
				SQLExecutor.insertBeans(sql, temp);
				tm.commit();
				model.addAttribute("datas", temp);
			}
		} catch (Exception e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "path:mapbean";
	}
	详情请参考测试用例：
	/bboss-mvc/WebRoot/WEB-INF/bboss-mapbean.xml
------2011-05-11------------
o 完善restful实现机制，可以支持对控制器方法级别的restful全地址映射关系并修复了部分缺陷，3.0版本需要根据类级别和方法级别地址组合才能实现
  restful风格的功能，而且存在缺陷。
#######update function list since bbossgroups-3.1 end###########
#######update function list since bbossgroups-3.0 begin###########
------2011-04-30------------
o 绑定参数注解指定日期转换格式，以便保证按原始数据格式将参数转换为正确的日期
o 可以将日期类型(java.util.Date/java.sql.Date/java.sql.Timestamp)转换为long类型数据，也可以将long数据转换为日期类型(java.util.Date/java.sql.Date/java.sql.Timestamp)，
也可以进行long数组和日期类型(java.util.Date/java.sql.Date/java.sql.Timestamp)数组的相互转换
o 修复mvc框架控制器组件解析异常：
	java.lang.IllegalArgumentException: Class must not be null
	at org.frameworkset.util.Assert.notNull(Assert.java:112)
	at org.frameworkset.util.annotations.AnnotationUtils.findAnnotation(AnnotationUtils.java:129)
	at org.frameworkset.web.servlet.handler.HandlerUtils.determineUrlsForHandler(HandlerUtils.java:1965)

------2011-04-14------------
o mvc中传递给分页标签的导航路径修改为带上下文的绝对地址，以免在使用jquery模式局部分页时，主页面的相对地址和分页对应的页面的相对路径不一致时，不能正确地
进行分页导航
------2011-04-13------------
o 控制器方法中增加Map类型参数绑定机制，可以将request中的参数转换为Map对象，当参数是数组时存入数组值，否则存入单个值
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

------2011-04-05------------
o 控制器方法增加枚举类型，枚举数组类型参数的绑定功能
------2011-03-31------------
o 跳转路径可以通过path：元素直接指定，而无需注入
具体使用方法，参考demo
WebRoot/WEB-INF/bboss-path.xml
------2011-03-24------------
o 修复RequestContext.getPathWithinHandlerMappingPath(request)获取匹配路径时，作为相关页面的相对地址时，报404错误问题，修改的程序如下：
修复之前

org.frameworkset.web.servlet.support.RequestContext

public static String getPathWithinHandlerMappingPath(HttpServletRequest request)
{
	
	return (String) request
	.getAttribute(org.frameworkset.web.servlet.HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
}
	修改之后
	public static String getPathWithinHandlerMappingPath(HttpServletRequest request)
	{
		String urlpath = (String) request
		.getAttribute(org.frameworkset.web.servlet.HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		 if(urlpath != null && urlpath.startsWith("/") )
		 {
			 urlpath = request.getContextPath() + urlpath;
		 }
		 return urlpath;
//		return (String) request
//		.getAttribute(org.frameworkset.web.servlet.HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
	}
   

------2011-03-02------------
 
o 修改文件上传中文字符乱码问题，swfupload统一采用utf-8编码，因此将
jquery.MultiFile的编码也制定为utf-8
上传的jsp页面的编码统一制定为utf-8

#######update function list since bbossgroups-3.0 end###########
bbossgroups 2.1----bboss-mvc

***************************************************
2010-11-24 bboss-mvc 框架功能特性
***************************************************
o 单方法匹配controller
o 多方法匹配controller
o 注解controller
o restful controller
o 国际化
o 主题
o 数据校验信息展示标签
o 数据库校验框架
o json/rss/atom 支持
o 分页支持


o 消息转换器功能添加
	o 属性注入
	o 参数注入
o 拦截器	
o 参数校验
 参数非法性校验
校验信息反馈
校验回写
校验规则
数据绑定
校验错误回写
校验失败跳转页面设置和生效

报表实现

多文件上传

不同的应用服务器jar包加载：
在以下服务期测试通过：tomcat 6.0，weblogic 11(动态库未测试)，websphere 7，apusic
未通过服务器：ren


