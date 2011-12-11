todo

1.url重写规则可以在以下级别指定：
	全局范围级别
	控制器级别
	控制器方法级别

2.添加拦截器功能
拦截器分为2个层次，-，基于框架级别的拦截器，这个是全局拦截器，是单实例模式的

3.国际化功能完善
6.mvc和gwt结合可行性研究

#######update function list since bbossgroups-3.4 begin###########
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


