---------------------------------
bboss-tablib关联工程：
---------------------------------
bboss-taglib->common_old_dbcp [frameworkset-pool.jar]
bboss-taglib->bbossaop [bboss-aop.jar]
bboss-taglib->common_old_util [frameworkset-util.jar]

bboss-taglib<-cms_baseline [frameworkset.jar]
bboss-taglib<-bbossaop [frameworkset.jar]
bboss-taglib<-cas server [frameworkset.jar]
bboss-taglib<-portal [frameworkset.jar]
bboss-taglib<-bboss-ws [frameworkset.jar]

#######update function list since bbossgroups-3.4 begin###########
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


#######update function list since bbossgroups-3.3 begin###########
------2011-10-14------------
o 完善convert标签，将值全部转换为字符串，只允许Map对象中存放的数据的key是字符串
o 完善cell标签及其子标签性能，改进使用反射机制的方法
#######update function list since bbossgroups-3.2 begin###########
------2011-08-06------------
o 标签库cell标签，逻辑标签的expression和expressionValue表达式中增加rowcount变量的支持
------2011-07-31------------
o 增加map和mapkey两个标签，用来循环迭代展示map中的value对象值或者value对象中的数据值以及mapkey值
使用方法如下：
<table>
	    <h3>map<String,po>对象信息迭代功能</h3>
		<pg:map requestKey="mapbeans">
		
			<tr class="cms_data_tr">
				<td>
					mapkey:<pg:mapkey/>
				</td> 
				<td>
					id:<pg:cell colName="id" />
				</td> 
				<td>
					name:<pg:cell colName="name" />
				</td> 
			</tr>
		</pg:map>
		
		
	</table>
	
	<table>
	    <h3>map<String,String>字符串信息迭代功能</h3>
		<pg:map requestKey="mapstrings">
		
			<tr class="cms_data_tr">
				<td>
					mapkey:<pg:mapkey/>
				</td> 
				<td>
					value:<pg:cell/>
				</td> 
				
			</tr>
		</pg:map>
		
		
	</table>
------2011-07-19------------
o cell标签提供actual属性，可以直接输出改属性设定的值，值可以为el表达式的值
------2011-07-13------------
o 修改empty和notempty两个逻辑标签增加对Collection和Map对象的为empty判断支持
o 修改rowcount标签，去除多余的空格
o 完善标签排序功能补丁
增加相应的指示箭头，标识升序和降序
相关文件
/bboss-mvc/WebRoot/include/pager.css
WebRoot\WEB-INF\lib\frameworkset.jar
具体的使用方法参考《基于基础框架开发约定.doc》中的章节3.5	分页字段排序设定

#######update function list since bbossgroups-3.1 begin###########
------2011-06-04------------
o 逻辑标签可以独立于list和beaninfo标签使用,增加以下属性：
		<attribute>
			<name>requestKey</name>
			<rtexprvalue>true</rtexprvalue>
		</attribute>
		<attribute>
			<name>sessionKey</name>
			<rtexprvalue>true</rtexprvalue>
		</attribute>
		<attribute>
			<name>pageContextKey</name>
			<rtexprvalue>true</rtexprvalue>
		</attribute>
		<attribute>
			<name>parameter</name>
			<rtexprvalue>true</rtexprvalue>
		</attribute>
		<attribute>
			<name>actual</name>
			<rtexprvalue>true</rtexprvalue>
		</attribute>
通过以上属性，可以方便地制定逻辑标签的期望值，
requestKey：指定从request的attribute属性中获取实际值，
sessionKey：指定从session的attribute属性中获取实际值，
pageContextKey：指定从pageContext的attribute属性中获取实际值，
parameter:指定从request的parameter中获取实际值
actual:直接指定实际值，可以是具体的值，也可以是一个el变量
上述属性还可以和property属性结合起来获取值对象中的属性值
------2011-06-04------------
o request和session标签增加日期dateformat格式属性

------2011-06-03------------
o 修复config标签enablecontextmenu属性不能正常工作的漏洞
------2011-05-26------------
o 新增empty和notempty两个逻辑标签使用方法和null、notnull一样
empty判断指定的字段的值是否是null，或者空串，如果条件成立，则执行标签体中的逻辑
notempty判断指定的字段的值既不是null也不是空串，则执行标签体得内容
------2011-05-24------------
o 修改null和notnull标签不能正确工作的问题
o 修复detail标签的提示信息不是很正确的问题；

#######update function list since bbossgroups-3.1 end###########


#######update function list since bbossgroups-3.0 begin###########
to do list：
逻辑标签通用化，不局限与list标签中使用
运行标签库的最小依赖包整理
------2011-04-25------------
o 修改字符串过滤器，解决jquery或者ajax数据请求时，分页查询的中文乱码问题，改进字符串过滤器的性能
------2011-04-20------------
o 新增convert标签，支持字典数据值向名称的转换
其中的datas为一个map属性映射值，name对应于key，convert标签通过name获取到对应的属性值
然后显示到页面上，如果对应的值没有那么输出defaultValue对应的值，如果没有设置defaultValue
那么直接输出name。
<pg:convert convertData="datas" colName="name" defaultValue=""/>
pager-taglib.tld
frameworkset.jar
------2011-04-18------------
o 解决主页面通过ajax方式加载多个分页页面时，跳转功能不能正常使用的问题
pager.js
frameworkset.jar
------2011-04-14------------
o mvc中传递给分页标签的路径修改为带上下文的绝对地址，以免主页面的相对地址和分页对应的页面的相对路径不一致时，不能分页
------2011-03-24------------
o 修复mvc实现分页功能时，通过handleMapping注解指定的url路径无法进行分页的bug，修改的程序如下：
修复之前
com.frameworkset.common.tag.pager.tags.PagerContext
   public static String getPathwithinHandlerMapping(HttpServletRequest request)
	{
		return (String) request
		.getAttribute("org.frameworkset.web.servlet.HandlerMapping.pathWithinHandlerMapping");
		
		
			 
	}  
	修复之后
  com.frameworkset.common.tag.pager.tags.PagerContext
   public static String getPathwithinHandlerMapping(HttpServletRequest request)
	{
		 String urlpath = (String) request
		.getAttribute("org.frameworkset.web.servlet.HandlerMapping.pathWithinHandlerMapping");
		 if(urlpath != null && urlpath.startsWith("/") )
		 {
			 urlpath = request.getContextPath() + urlpath;
		 }
		 return urlpath;
			 
	}  
------2011-03-20------------
o 改进右键菜单功能，提升右键菜单性能，涉及的功能有：使用右键菜单的树标签和使用右键菜单的列表、分页标签，以及所有其他相关的页面
修改的程序有：
1.bboss-tablib/src/com/frameworkset/common/tag/contextmenu/ContextMenuTag.java
2./bboss-tablib/webapp/WEB-INF/templates/contextmenu/popscript.vm
3.jar包：frameworkset.jar
------2011-03-06------------
o 修复树标签复选框点击事件firefox兼容性问题
o 修复树标签默认选中节点上面设置点击事件时Boolean值向String转换异常问题

------2011-03-06------------
 
o list 和cell标签组合对String和数字、日期列表数据进行输出

#######update function list since bbossgroups-3.0 end###########
------------------------------------------------------------------
update function list in bbossgroups-2.0-rc2 since bbossgroups-2.0-rc1
------------------------------------------------------------------
2011-02-12
------------------------------------------------------------------

o 分页标签与mvc框架结合，通过mvc的控制器方法直接可以给分页标签提供数据
o 增加notcontain这notmatch两个正则表达式逻辑判断标签，和contain、match两个逻辑判断标签的功能相反

------------------------------------------------------------------
2010-09-03
------------------------------------------------------------------
。com.frameworkset.common.tag.pager.ObjectDataInfoImpl对象没有实现以下方法：
public long getItemCount() 
public int getDataSize()
导致调用时抛出异常。

实现上述两个方法后，功能正常。
------------------------------------------------------------------
2010-09-03
------------------------------------------------------------------
o 分页标签增加设置页面size功能,火狐浏览器兼容性修改
修改程序：
frameworkset.jar
/bboss-tablib/src/com/frameworkset/common/tag/pager/tags/IndexTag.java
/bboss-tablib/src/com/frameworkset/common/tag/pager/tags/PagerContext.java
/bboss-tablib/src/com/frameworkset/common/tag/pager/tags/PagerDataSet.java
/bboss-tablib/src/com/frameworkset/common/tag/pager/tags/PagerTag.java
/bboss-tablib/webapp/include/pager_custom.js
/bboss-tablib/webapp/include/pager.js

标签定义文件/bboss-tablib/webapp/WEB-INF/pager-taglib.tld为index标签添加sizescope属性
<!-- 
		设置页面显示记录范围，默认为
		"5","10","20","30","40","50","60","70","80","90","100"
		用户可以自定义这个范围，以逗号分隔即可
		如果在pager标签和list标签上指定的maxPageItems属性对应的页面记录条数不在sizescope范围中，那么
		将把maxPageItems作为第一个选项加入到sizescope中
		 -->
		<attribute>
			<name>sizescope</name>
			<rtexprvalue>true</rtexprvalue>
		</attribute>


使用jquery相关的样式，表格的鼠标移动与点击样式：

table_gray表格样式
.table_gray{
border:1px solid #4eadf7;
border-collapse:collapse;
}
.table_gray td{
border:1px solid #4eadf7;
border-collapse:collapse;
text-align: center;
}
.table_gray thead td{
background-color:#7cc5fa;
font-size:12px;
font-weight:bold;
}
.table_gray .down{
background:url(images/th_down.png) right no-repeat;
padding-right:6px;
}

.table_gray .updown{
background:url(images/th_updown.png) right no-repeat;
padding-right:6px;
}

//奇数行样式
.space_color{
background-color:#d7edfd;
}

//光标移动到的行样式
tr.highlight {
	background: #C8F3FB;
}

//行选中的样式
tr.selected {
	background: #FF8C05;
	color: #fff;


------------------------------------------------------------------
2010-09-01
------------------------------------------------------------------
o 修改过滤器com.frameworkset.common.filter.CharacterEncodingHttpServletRequestWrapper，解决特定情况下中文乱码
比如：
发起请求：http://localhost:8080/test/test.jsp?key=多多
先执行以下语句
String values[] = request.getParameterValues("key");//得到的values为中文数组：{"多多"}
然后再执行以下语句
String values = request.getParameter("key");//得到的值为中文乱码


------------------------------------------------------------------
update function list in bbossgroups-2.0-rc since bbossgroups-1.0
------------------------------------------------------------------
2010-07-31
------------------------------------------------------------------
o 增加菜单项禁用的提示功能
------------------------------------------------------------------
update function list in bbossgroups-1.0 
------------------------------------------------------------------
o pager标签库中 新增csslink标签和jscript标签，config标签
 通过csslink标签导入的css文件能够自动去重功能，也就是避免在页面上重复导入同一个css文件
通过jscript标签导入的js文件能够自动去重功能 ，也就是避免在页面上重复导入同一个js文件
通过congfig标签默认导入页面js和css文件（能够自动去重功能 ，也就是避免在页面上重复导入以下js，css文件）：

	/include/pager.js
	/include/jquery-1.4.2.min.js
	/include/treeview.css
	/include/themes/default/easyui.css
	/include/themes/icon.css
	/include/jquery.easyui.min.js
	
使用方法：

<pg:csslink src="contextpath/ccc.css"/>
<pg:jscript src="contextpath/ccc.js"/>

<pg:config/>
firefox兼容性问题修改：
o 修改tabs.js文件，修复tabpane在火狐下的问题，火狐下不支持直接以id.attrname的方式获取属性值
o tree标签：
/bboss-tablib/src/com/frameworkset/common/tag/tree/impl/NodeHelper.java
o 右键菜单
解决右键菜单在firefox浏览器下无法正常显示问题
解决无法展示多级右键菜单的问题
改进右键菜单添加接口如下：
			Menu menu = new Menu();
			menu.addContextMenuItem(Menu.MENU_OPEN);
			menu.addContextMenuItem(Menu.MENU_EXPAND);
			menu.addContextMenuItem("添加","javascript:edit('添加')",Menu.icon_edit);
			
			menu.addSeperate();
			menu.addContextMenuItem("编辑编辑编辑编辑","javascript:edit('编辑')",Menu.icon_add);
			
			Menu.ContextMenuItem sitemenuitem2 = menu.addContextMenuItem("sitemenuitem2","javascript:edit('sitemenuitem2')",Menu.icon_ok);
			sitemenuitem2.addSubContextMenuItem("子menusubmenuitem_","javascript:edit('子menusubmenuitem_')",Menu.icon_ok);	
			sitemenuitem2.addSubContextMenuItem("子cut","javascript:edit('子cut')",Menu.icon_cut);				
			sitemenuitem2.addSubContextMenuItem("子icon_back","javascript:edit('子icon_back')",Menu.icon_back);
			sitemenuitem2.addSubContextMenuItem("子icon_cancel","javascript:edit('子icon_cancel')",Menu.icon_cancel);
			sitemenuitem2.addSubContextMenuItem("子icon_help","javascript:edit('子icon_help')",Menu.icon_help);
			sitemenuitem2.addSubContextMenuItem("子icon_no","javascript:edit('子icon_no')",Menu.icon_no);
			sitemenuitem2.addSubContextMenuItem("子icon_print","javascript:edit('子icon_print')",Menu.icon_print);
			sitemenuitem2.addSubContextMenuItem("子icon_redo","javascript:edit('子icon_redo')",Menu.icon_redo);
			sitemenuitem2.addSubContextMenuItem("子icon_reload","javascript:edit('icon_reload')",Menu.icon_reload);
			sitemenuitem2.addSubContextMenuItem("icon_remove","javascript:edit('icon_remove')",Menu.icon_remove);
			sitemenuitem2.addSubContextMenuItem("icon_save","javascript:edit('icon_save')",Menu.icon_save);
			sitemenuitem2.addSubContextMenuItem("icon_search","javascript:edit('icon_search')",Menu.icon_search);
			sitemenuitem2.addSubContextMenuItem("icon_undo","javascript:edit('icon_undo')",Menu.icon_undo);
			ContextMenuItem third = sitemenuitem2.addSubContextMenuItem("第二层","javascript:edit('icon_undo')",Menu.icon_undo);
			third.addSubContextMenuItem("三层", "javascript:edit('icon_undo')",Menu.icon_undo);
----------------------------------------
1.0.2 - 2010-4-22
----------------------------------------
o cell标签改造
支持long型的字段直接转换为java.util.Date类型
----------------------------------------
1.0.2 - 2010-4-22
----------------------------------------
o bug修改
bug 1 jquery特殊字符转义
/bboss-tablib/webapp/include/pager.js
/bboss-tablib/webapp/WEB-INF/templates/tree.vm

var ret = object.replace(/:/g,"\\:");
    	ret = ret.replace(/\./g,"\\.");
    	
bug 2 采用jquery后，当树中只有一个节点，并且不展示根节点时，生成的树脚本中多生成了一个</div>
----------------------------------------
1.0.2 - 2010-4-15
----------------------------------------

o 脚本控制调整
/cms_baseline/sourcecode/cms/WebRoot/WEB-INF/templates/contextmenu/popmenu.vm
/bboss-tablib/src/com/frameworkset/common/tag/contextmenu/ContextMenuTag.java
/bboss-tablib/src/com/frameworkset/common/tag/pager/config/PageConfig.java
/bboss-tablib/src/com/frameworkset/common/tag/pager/tags/IndexTag.java
/bboss-tablib/src/com/frameworkset/common/tag/tree/impl/NodeHelper.java
/bboss-tablib/src/com/frameworkset/common/tag/tree/impl/TreeTag.java
----------------------------------------
1.0.2 - 2010-4-14
----------------------------------------
o 标签库jquery改造，右键菜单修改：creatorcim还未同步
/bboss-tablib/src/com/frameworkset/common/tag/pager/config/PageConfig.java
/cms_baseline/sourcecode/cms/WebRoot/include/pager.js
/cms_baseline/sourcecode/cms/WebRoot/WEB-INF/templates/contextmenu/parentpopmenu.vm
/cms_baseline/sourcecode/cms/WebRoot/WEB-INF/templates/contextmenu/popmenu.vm
/cms_baseline/sourcecode/cms/WebRoot/WEB-INF/templates/contextmenu/popscript.vm
/cms_baseline/sourcecode/cms/WebRoot/WEB-INF/templates/tree.vm
/cms_baseline/sourcecode/cms/WebRoot/WEB-INF/pager-taglib.tld
/bboss-tablib/src/com/frameworkset/common/tag/pager/tags/IndexTag.java
----------------------------------------
1.0.2 - 2010-3-11
----------------------------------------
o 分页跳转排序排序bug修复，sorkey为null时，添加了如下格式的参数：sortkey=null,进行判断后不加就可以
/bboss-tablib/src/com/frameworkset/common/tag/pager/tags/IndexTag.java
/bboss-tablib/webapp/include/pager.js


o 修改树标签递归选择bug
向上递归
向下递归
向上部分递归
/WEB-INF/templates/tree.vm
o pager标签jquery改造

导航链接全部需要改造为jquery模式
title排序链接改造为jquery模式
增加jquery查询模式标签组（未完成）

相关的文件：
/bboss-tablib/webapp/WEB-INF/pager-taglib.tld
/bboss-tablib/webapp/include/pager.js
/bboss-tablib/src/com/frameworkset/common/tag/pager/tags/IndexTag.java
/bboss-tablib/src/com/frameworkset/common/tag/pager/tags/PagerDataSet.java
/bboss-tablib/src/com/frameworkset/common/tag/pager/tags/PagerContext.java
/bboss-tablib/src/com/frameworkset/common/tag/pager/tags/PagerTag.java
/bboss-tablib/src/com/frameworkset/common/tag/pager/tags/TitleTag.java
/bboss-tablib/src/com/frameworkset/common/tag/pager/config/PageConfig.java



o 树标签prototype.js迁移到jquery
修改文件/creatorcim/cimconsole/WEB-INF/templates/contextmenu/popmenu.vm，由prototype.js换到jquery
内容<script language="javascript" src="${contextpath}/include/jquery-1.4.2.min.js"></script>

修改/WEB-INF/templates/tree.vm模板文件将所有的prototye内容替换为jquery内容

新增程序标签config
com.frameworkset.common.tag.pager.config.PageConfig

/bboss-tablib/webapp/WEB-INF/pager-taglib.tld增加以下标签配置
<tag>
		<name>config</name>
		<tagclass>com.frameworkset.common.tag.pager.config.PageConfig</tagclass>
		<bodycontent>JSP</bodycontent>
		
	</tag>
页面引用方法：<pg:config/>，标签执行后输出以下内容：
<script src="/cimconsole/include/jquery-1.4.2.min.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="/cimconsole/include/treeview.css"/>

修改程序
/bboss-tablib/src/com/frameworkset/common/tag/tree/impl/ListNodeHelper.java
/bboss-tablib/src/com/frameworkset/common/tag/tree/impl/NodeHelper.java
/bboss-tablib/src/com/frameworkset/common/tag/tree/impl/TreeTag.java
屏蔽prototype脚本的投放，改为投放以下脚本：
<script src="/cimconsole/include/jquery-1.4.2.min.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="/cimconsole/include/treeview.css"/>


//NodeHelper.getPrototypeScript(ret,request.getContextPath()); //jquery
//            	if(!this.enablecontextmenu) //jquery
//            		NodeHelper.getPrototypeScript(ret,request.getContextPath()); 

<a firsted="true" name="icon_root" 修改为<a firsted="true" id="icon_root"

修改tree标签增加jquery属性
 <!-- 
        	是否jquery装载,true-是，false-不是
        	默认值：false;
        	为false时,标签库将自动为页面引入以下样式和脚本，否则不导入
        	<script src="/cimconsole/include/jquery-1.4.2.min.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="/cimconsole/include/treeview.css"/>
        	true时，上述样式和脚本将通过外部导入
         -->
        <attribute> 
            <name>jquery</name>
            <required>false</required>
            <rtexprvalue>true</rtexprvalue>
        </attribute>


o 新增执行数据库预编译模板操作相关标签

dbutil-执行数据库增、删、改操作（预编译和普通）
sqlparams-用于支持在pager标签，beaninfo标签，list标签上执行预编译操作的绑定变量集合，同时可以指定sql绑定变量的定义语法分界符。

batchutil-执行预编译批处理、普通批处理操作
statement-指定batchutil要执行的批处理语句，可以是预编译sql语句，也可以是普通sql语句
batch-指定statement指定的预编译sql语句的一组绑定变量
sqlparam-用来指定预编译操作的sql绑定变量参数的值、数据类型、数据格式，只能内置在dbutil，sqlparams，statement,batch三个标签中。

dbutil标签的属性说明如下：

	statement：指定要执行的sql语句，可以普通sql语句，也可以是带绑定变量的sql模板语句，必须要写的属性
	dbname：指定数据库连接池名称，可选属性，默认为poolman中配置的第一个连接池
	pretoken：指定预编译sql语句的绑定变量定义前置分界符，必须与后置分界符endtoken一起指定。可选属性，默认为#[
	endtoken：指定预编译sql语句的绑定变量定义后置分界符，必须与前置分界符pretoken一起指定。可选属性，默认为]
	action:指定sql对应数据库操作类型，包括：delete,insert,update,必填选项		
	result:指定数据库操作结果存储变量的名称，可选属性，默认值变量名称为：dbutil_result

sqlparams标签属性说明如下：
	pretoken：指定预编译sql语句的绑定变量定义前置分界符，必须与后置分界符endtoken一起指定。可选属性，默认为#[
	endtoken：指定预编译sql语句的绑定变量定义后置分界符，必须与前置分界符pretoken一起指定。可选属性，默认为]
	sqlparamskey:指定将参数存储在request 属性集中的变量名称，用来和pager，beaninfo，list标签设置好模板sql的绑定变量参数值
	sqlparams必须和pager，beaninfo，list标签一起使用
	
batchutil标签属性说明如下：
		dbname-批处理操作对应的数据库连接池的名称，可选属性，默认为poolman中配置的第一个连接池
		type-批处理操作类型，取值范围common,prepared,可选属性，默认值为prepared
		batchOptimize-优化预编译批处理操作控制变量
		
statement标签属性说明如下：
	sql-指定批处理的sql语句，可以使预编译sql语句和普通sql语句
	pretoken-指定预编译sql语句的绑定变量定义前置分界符，必须与后置分界符endtoken一起指定。可选属性，默认为#[
	endtoken-指定预编译sql语句的绑定变量定义后置分界符，必须与前置分界符pretoken一起指定。可选属性，默认为]
		
sqlparam 属性说明：
	name：绑定变量名称，必须和预编译sql模板中的变量名称保持一致，必选项
	value：变量值，必选项
	type：变量类型，可选项，默认值为string,对应的取值范围如下：
			bigdecimal
        	boolean
        	byte
        	byte[]
			date
			double 
			float
			int 
			long
			short 
			string
			time
			timestamp
			blob
			clob
			blobfile
			clobfile
	dataformat：数据格式，主要用来指定日期类型(date,time,timestamp)的存储格式
 

特别说明：
这pretoken 、endtoken两个属性主要用来支撑在标签预编译查询功能时定义sql语句变量的语法
例如：
pretoken = "#\\["
endtoken = "\\]"
上面的变量值就是默认的变量分界符，开发人员可以指定自己的分界符

后续将要实现的功能：
增加存储过程，函数执行标签

使用实例，参考测试用例：
beaninfo:/bboss-tablib/webapp/pager/testDetailTag_prepareddb.jsp
list:/bboss-tablib/webapp/pager/testListPagertag_prepareddb.jsp
pager:/bboss-tablib/webapp/pager/testPagerTag_prepareddb.jsp
dbutil:
	新增-/bboss-tablib/webapp/pager/testPagerTag_preparedInsert.jsp
	删除-/bboss-tablib/webapp/pager/testPagerTag_preparedDeletedb.jsp
	更新-/bboss-tablib/webapp/pager/testPagerTag_preparedUpdatedb.jsp

batchutil:
	普通批处理操作-/bboss-tablib/webapp/pager/testPagerTag_batchdb.jsp
	预编译批处理操作-/bboss-tablib/webapp/pager/testPagerTag_preparedbatchdb.jsp

o 扩展分页，列表，详细信息页面直接设置statement执行数据库查询功能，增加预编译查询方式

o beaninfo标签，pager标签，list标签增加以下属性：
sqlparamskey:指定将绑定变量参数存储在request 属性集中的变量名称，以便pager，beaninfo，list标签获取sql的绑定变量参数值


上述功能相关的文件：
/bboss-tablib/webapp/WEB-INF/pager-taglib.tld
frameworkset.jar
frameworkset-pool.jar
frameworkset-util.jar
bboss-aop.jar

----------------------------------------
1.0.2 - 2010-1-7
----------------------------------------
o 增加ant构建脚本:build.xml，build.properties
o 增加标签库测试用例war应用webapp目录
o 增加distrib目录存放发布后的相关文件

----------------------------------------
1.0.2 - 2010-1-4
----------------------------------------
o 添加tabpane标签库源码目录
o 树标签库treetag.tld中新增标签query，定义如下：
<!-- 
	 	实现树标签的查询功能，适用于静态树和动静结合的树
		只能查找已经展示出来的树节点
	  -->
	 <tag>
		<name>query</name>
        <tagclass>com.frameworkset.common.tag.tree.impl.QueryTag</tagclass>		
        <bodycontent>JSP</bodycontent>
        <!-- 
        	rootid:指定树的根节点id，默认值为0，对应于treedata的rootid属性值
         -->        
        <attribute>
          <name>rootid</name>
          <required>false</required>
          <rtexprvalue>true</rtexprvalue>
        </attribute>
        <!-- 
        	templatepath:输出查询框、查询按钮的velocity模板脚本文件路径，相对于velocity模板文件夹的根目录
        	例如：模板目录为d:/templates/treequery.vm,那么templatepath的值就为treequery.vm
         -->        
        <attribute>
          <name>templatepath</name>
          <required>false</required>
          <rtexprvalue>true</rtexprvalue>
        </attribute>
	 </tag>
使用方法：
	 <tree:query/> <!-- rootid默认为0，templatepath默认为treequery.vm -->
	 <tree:query rootid="0"/> <!-- templatepath默认为treequery.vm -->
	 <tree:query templatepath="treequery.vm"/> <!-- rootid默认为0 -->
	 <tree:query rootid="0" templatepath="treequery.vm"/>


2009.12.7
---------------------------------------------------------------------------------
o 修改index标签，增加页面范围设置
<!-- 
			中间页面样式名称
		 -->
		<attribute>
			<name>classname</name>
			<required>false</required>
			<rtexprvalue>true</rtexprvalue>
		</attribute>
		
		<!-- 
			展示的中间页面数，默认为-1,即不展示中间页
		 -->
		<attribute>
			<name>tagnumber</name>
			<required>false</required>
			<rtexprvalue>true</rtexprvalue>
		</attribute>
		
		<!-- 
			中间页扩展属性
		 -->
		<attribute>
			<name>centerextend</name>
			<required>false</required>
			<rtexprvalue>true</rtexprvalue>
		</attribute>
修改的程序
WebRoot/WEB-INF/pager-taglib.tld
/bboss-taglib/src/com/frameworkset/common/tag/pager/tags/IndexTag.java		

使用方法：<pg:index tagnumber="10"/>

o 修改index标签添加以下属性：
<!--
			控制页面是使用pager.js还是pager_custom.js
			为true时使用pager_custom.js，这时要求页面上要有以下form表单：
			   <form name="com.frameworkset.goform" method="post"></form>
			为false时使用pager.js
			本属性的默认值为false
		-->
		<attribute>
			<name>custom</name>
			<required>false</required>
			<rtexprvalue>true</rtexprvalue>
		</attribute>
<!-- 
			导航按钮是否使用图片,默认值为false
			只有useimage=true时，imagedir和imageextend才起作用
			如果useimage=true时，没有指定imagedir和imageextend属性，那么采用默认属性
			使用方法
			<pg:index useimage="true" imagedir="/include/images" imageextend=" border=0 " tagnumber="-1"/>
		 -->
		<attribute>
			<name>useimage</name>
			<required>false</required>
			<rtexprvalue>true</rtexprvalue>
		</attribute>
		<!-- 
			导航按钮图片存放目录，存放的图片名称为：
			 first.gif-首 页
    		 next.gif-下一页
    		 pre.gif-上一页
    		 last.gif-尾 页
    		默认值为：/include/images
		 -->
		<attribute>
			<name>imagedir</name>
			<required>false</required>
			<rtexprvalue>true</rtexprvalue>
		</attribute>
		<!-- 
			导航图片的扩展属性串，默认值为：" border=0 "
		 -->
		<attribute>
			<name>imageextend</name>
			<required>false</required>
			<rtexprvalue>true</rtexprvalue>
		</attribute>

修改的程序
WebRoot/WEB-INF/pager-taglib.tld
/bboss-taglib/src/com/frameworkset/common/tag/pager/tags/IndexTag.java

升级方法
---------------------------------------------------------
1.在pager-taglib.tld的index标签中添加以下属性：

<tag>
		<name>index</name>
		。。。。。
		<!--
			控制页面是使用pager.js还是pager_custom.js
			为true时使用pager_custom.js，这时要求页面上要有以下form表单：
			   <form name="com.frameworkset.goform" method="post"></form>
			为false时使用pager.js
			本属性的默认值为false
		-->
		<attribute>
			<name>custom</name>
			<required>false</required>
			<rtexprvalue>true</rtexprvalue>
		</attribute>
<!-- 
			导航按钮是否使用图片,默认值为false
			只有useimage=true时，imagedir和imageextend才起作用
			如果useimage=true时，没有指定imagedir和imageextend属性，那么采用默认属性
			使用方法
			<pg:index useimage="true" imagedir="/include/images" imageextend=" border=0 " tagnumber="-1"/>
		 -->
		<attribute>
			<name>useimage</name>
			<required>false</required>
			<rtexprvalue>true</rtexprvalue>
		</attribute>
		<!-- 
			导航按钮图片存放目录，存放的图片名称为：
			 first.gif-首 页
    		 next.gif-下一页
    		 pre.gif-上一页
    		 last.gif-尾 页
    		默认值为：/include/images
		 -->
		<attribute>
			<name>imagedir</name>
			<required>false</required>
			<rtexprvalue>true</rtexprvalue>
		</attribute>
		<!-- 
			导航图片的扩展属性串，默认值为：" border=0 "
		 -->
		<attribute>
			<name>imageextend</name>
			<required>false</required>
			<rtexprvalue>true</rtexprvalue>
		</attribute>

<!-- 
			中间页面样式名称
		 -->
		<attribute>
			<name>classname</name>
			<required>false</required>
			<rtexprvalue>true</rtexprvalue>
		</attribute>
		
		<!-- 
			展示的中间页面数，默认为-1,即不展示中间页
		 -->
		<attribute>
			<name>tagnumber</name>
			<required>false</required>
			<rtexprvalue>true</rtexprvalue>
		</attribute>
		
		<!-- 
			中间页扩展属性
		 -->
		<attribute>
			<name>centerextend</name>
			<required>false</required>
			<rtexprvalue>true</rtexprvalue>
		</attribute>
		
	.......		
	</tag>
	
2.	将IndexTag.class类放入frameworkset.jar的路径：com\frameworkset\common\tag\pager\tags 替换原来类即可
---------------------------------------------------------------------------------------------
bboss taglib v1.0.1
2009.10.13 
---------------------------------------------------------------------------------------------
o 增加aop框架配置
Adding: bboss-taglib\lib\bboss-aop.jar  application/octet-stream
Adding: bboss-taglib\lib\bboss-event.jar  application/octet-stream
Modified: bboss-taglib\lib\frameworkset-pool.jar  



2009.01.08
-------------------------------------------------------------------------
fixed bug 1#:
dataSet is null exception.beaninfo tag's stack is same as the dataset(list tag).cms outline tag use the same stack as list tag.
解决变量dataSet的值没有正确设置的问题，
this.removeVariable();方法必须在recoverParentDataSet();之前调用

declare属性说明：
declare用来控制，是否声明新的dataSet变量和rowid变量，用在list嵌套使用的情况。
          标签库中始终会声明这些变量