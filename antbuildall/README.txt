版本：bboss4.0.8 
官网：http://www.bbossgroups.com 
项目博客：http://yin-bp.iteye.com/ 
Sourceforge站点：http://sourceforge.net/projects/bboss/files/ 
Github站点：https://github.com/bbossgroups/bbossgroups-3.5 
since 3.5.1
o 增加动态令牌机制，增加动态令牌检测过滤器（可独立使用，也可与安全认证过滤器结合使用）
增加dtoken标签（用来在表单或者请求中投放动态令牌）
增加assertdtoken标签（用来在接收请求的服务器jsp页面头部判断客户端是否正确传递令牌，并检测令

牌是否有效，如果没有令牌或者令牌无效，那么拒绝客户端请求）
增加assertdtoken注解（用来在接收请求的服务器mvc控制器方法执行前判断客户端是否正确传递令牌，

并检测令牌是否有效，如果没有令牌或者令牌无效，那么拒绝客户端请求）

令牌的过滤器的详细配置请参考web.xml文件：
/bboss-mvc/WebRoot/WEB-INF/web.xml
/bboss-mvc/WebRoot/WEB-INF/web-tokenfilter.xml（令牌过滤器独立使用配置示例）
/bboss-mvc/WebRoot/WEB-INF/web-token.xml（与安全认证过滤器结合使用示例）

动态令牌机制可以有效解决以下问题：
1.防止表单重复提交
2.防止跨站脚本编制漏洞
3.防止跨站请求伪造攻击
4.有效避免框架钓鱼攻击

o 持久层TransactionManager组件增加以下方法
	/**
	 * 在final方法中调用，用来在出现异常时对事务资源进行回收，首先对事务进行回滚，
	 * 然后回收资源（不输出日志），如果事务已经提交和回滚，则不做任何操作
	 */
	public void releasenolog()
	/**
	 * 在final方法中调用，用来在出现异常时对事务资源进行回收，首先对事务进行回滚，
	 * 然后回收资源，并将回事日志输出到日志文件中，如果事务已经提交和回滚，则不做任何操

作
	 */
	public void release()

这两个方法的示例如下：
public void testTX11() throws Exception
	{
		TransactionManager tm = new TransactionManager();
        try {
            tm.begin();
            
            //进行一系列db操作            
            //必须调用commit            
            tm.commit();
        }
        catch (Exception e) {            
            throw e;            
        } 
        finally
        {
        	tm.release();
        }
	}
	public void testTX() throws Exception
	{
		TransactionManager tm = new TransactionManager();
        try {
            tm.begin(TransactionManager.RW_TRANSACTION);
            
            //进行一系列db操作
            
            //注意对于RW_TRANSACTION事务可以不调用commit方法，tm.releasenolog()
            //方法会释放事务，调用commit也可以
            
            tm.commit();
        }
        catch (Exception e) {
            
            throw e;
            
        } 
        finally
        {
        	tm.releasenolog();
        }
	}
o 修改各模块构建jar包和资源文件分发脚本，使相关模块测试工程中的jar包和资源文件同步更新
-----------------------------------------------------------------------------------------------

bbossgroups-3.5 release futures

首先感谢[url=http://www.chinacreator.com]湖南科创信息[/url]、[url=http://www.sanygroup.com]三一重工[/url]等公司对bbossgroups项目的大力支持
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
o 修复IOC机制中三层及三层以上循环依赖注入引用关系无法正常解析的漏洞
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