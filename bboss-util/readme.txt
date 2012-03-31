发布的jar包：
frameworkset.jar ditchnet-tabs-taglib.jar

---------------------------------
bboss-util关联工程：
---------------------------------

bboss-util->bbossaop [bboss-aop.jar]

bboss-util<-active-ext [frameworkset-util.jar]
bboss-util<-bboss-ws [frameworkset-util.jar]
bboss-util<-bbossaop [frameworkset-util.jar]
bboss-util<-bbossevent [frameworkset-util.jar]
bboss-util<-cms_baseline [frameworkset-util.jar]

bboss-util<-bboss-persistent [frameworkset-util.jar]
bboss-util<-bboss-taglib [frameworkset-util.jar]
bboss-util<-kettle [frameworkset-util.jar]
bboss-util<-portal [frameworkset-util.jar]
bboss-util<-cas server [frameworkset-util.jar]
#######update function list since bbossgroups-3.5 begin###########
o 修复ValueObjectUtil日期转换方法多线程安全问题，问题表现为多个不同的用户并发转换时间时，得到不可预期的结果
升级frameworkset-util.jar可以解决这个问题
/bboss-util/src/com/frameworkset/util/ValueObjectUtil.java
o SimpleStringUtil类中增加格式化Exception类为String的方法
o UTF8Convertor类中增加指定目录，指定过滤子目录名列表，指定文件类型列表的编码转换方法
o 解决附件下载中文名称文件在ie 6下无法下载的问题
o util功能：增加字符串编码集识别功能

o 合并StringUtil和SimpleStringUtil中的部分方法，StringUtil只保留和HttpServletRequest相关的的方法
o ValueObjectUtil类中增加一系列数据比较函数
#######update function list since bbossgroups-3.4 begin###########
o ClassInfo对象增加isprimary方法，标识对应的类型是否是bboss定义的基础数据类型范畴
o 增加获取方法参数名称的工具类LocalVariableTableParameterNameDiscoverer
获取LocalVariableTableParameterNameDiscoverer的方法如下：
ParameterNameDiscoverer parameterNameDiscoverer = ClassUtil.getParameterNameDiscoverer();
获取方法参数名称的方法如下：
ParameterNameDiscoverer parameterNameDiscoverer = ClassUtil.getParameterNameDiscoverer();
Method method = ClassInfo.class.getMethod("getDeclaredMethod", String.class);
String[] names = parameterNameDiscoverer.getParameterNames(method);
o 内置asm 4.0版本
#######update function list since bbossgroups-3.3 begin###########
------2011-10-14------------
o 优化ValueObjectUtil中根据属性名称获取字段值得方法，改进性能。
------2011-09-19------------
o 完善Velocity模板引擎模板路径配置机制，在bboss-aop.jar/aop.properties文件中增加approot配置，
用来指示应用上下文的绝对路径：
approot=D:/workspace/bbossgroups-3.2/bboss-mvc/WebRoot
以便能够查找到对应的模板根目录
由于标签库中使用了vm文件，这些文件存放在approot的/WEB-INF/templates目录下面，因此必须保证Velocity引擎启动后正确地找到
这个目录，在tomcat中是能够自动找到的，但是在weblogic等容器中无法自动找到这个目录，因此需要在bboss-aop.jar/aop.properties文件中增加approot配置

o 完善VelocityUtil类，解决找不到velocity.properties文件的问题

o 完善类型转换机制，支持lob字段向File/byte[]/String类型的转换
#######update function list since bbossgroups-3.2 begin###########
------2011-07-19------------
o StringUtil类中增加文件下载方法：
StringUtil.sendFile(request, response, record
							.getString("filename"), record
							.getBlob("filecontent"));
StringUtil.sendFile(request, response, file);							
------2011-06-14------------
o 支持数字向BigDecimal转换、数字数组向BigDecimal数组转换功能

#######update function list since bbossgroups-3.1 begin###########

------2011-06-09------------
o 处理空字符串向日期类型转换后台报异常的缺陷
------2011-05-06------------
o 修复分页标签偶尔找不到vm模板文件的漏洞
上述漏洞修复的程序为：
/bboss-util/src/com/frameworkset/util/VelocityUtil.java
	
#######update function list since bbossgroups-3.1 end###########

#######update function list since bbossgroups-3.0 begin###########
to do list:
有时会找不到vm模板文件

------2011-04-07------------
o  修改DaemonThread进程，支持从外部指定刷新文件资源的时间间隔。
------2011-04-05------------
o  支持字符串向枚举类型转换、字符串数组向枚举类型数组转换功能
------2011-03-02------------
o  修复double类型数据向int类型转换的问题，新增单个值转换为数组的功能，支持数字类型数组之间的相互转换
#######update function list since bbossgroups-3.0 end###########

------------------------------------------------------------------------
update function list in bbossgroups-2.0-rc2 since bbossgroups-2.0-rc1
------------------------------------------------------------------------
o 修改 com/frameworkset/util/TransferObjectFactory.java中对象值拷贝不支持isXXXX方式获取属性值的问题
/bboss-util/src/com/frameworkset/util/TransferObjectFactory.java
----------------------------------------
bbossgroups-2.0-rc2 - 2010-11-28
----------------------------------------
o 完善ValueObjectUtil的getValue方法，避免因为调用get方法返回值为null后，重复查找Boolean方法的问题
增加getMethodByPropertyName方法，增加public static Object getValueByMethod(Object obj, Method method, Object[] params)方法
----------------------------------------
bbossgroups-2.0-rc2 - 2010-08-31
----------------------------------------
o 修复velocity模板没有正确初始化的问题，主要是在VelocityUtil上执行evalute时没有判别
  引擎是否已经初始化

----------------------------------------
bbossgroups-2.0-rc2 - 2010-08-23
----------------------------------------
o 修复com.frameworkset.util.VariableHandler中变量解析bug,
默认default_regex 修改为 "\\$\\{.+?)\\}"获取输入串中的变量，作为数组返回

------------------------------------------------------------------------
update function list in bbossgroups-2.0-rc1 since bbossgroups-2.0-rc
------------------------------------------------------------------------
----------------------------------------
bbossgroups-2.0-rc1 - 2010-07-23
----------------------------------------
o VelocityUtil中增加对字符串模板的解析方法
--------------------------
2010-03-10
--------------------------

o 变量解析程序中新增变量析取和替换接口

--------------------------
2010-01-07
----------------------
o 将spring相关的程序分离，单独形成包frameworkset-spring.jar,原来的包继续保留frameworkset-util.jar
o 添加ant构建脚本和属性配置文件:build.xml
 

--------------------------
2009-12-15
----------------------
o 新增变量解析程序
com.frameworkset.util.VariableHandler
功能说明：
可以根据默认的正则式default_regex = "\\$\\{([^\\}]+)\\}"获取输入串中的变量，作为数组返回
public static String[] variableParser(String inputString)
可以根据指定的正则式获取输入串中的变量，作为数组返回
public static String[] variableParser(String inputString,String regex)
可以根据指定的变量的前导符和后导符获取输入串中的变量，作为数组返回
 public static String[] variableParser(String inputString,String pretoken,String endtoken)


--------------------------

2009-09-28
----------------------
o   bboss-util/src/com/frameworkset/util/ValueObjectUtil.java
boolean值转换时，能够处理0,和1

o 修改VelocityUtil.java
o 修改com.frameworkset.util.ValueObjectUtil
	修改com.frameworkset.common.util.ValueObjectUtil使其与com.frameworkset.util.ValueObjectUtil的功能保持一致
o 增加测试用例包test

o 增加bsh-2.0b4.jar包，实现ValueObjectUtil中父类向子类转换的过程

