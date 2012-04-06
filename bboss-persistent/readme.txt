directory:
src--source code
test--test source code
conf--contain database pool config files
listener--database transaction leak listener file
lib--bboss persistent framework depends jars
dist--bboss persistent release package.


---------------------------------
bboss-persistent关联工程：
---------------------------------
bboss-persistent<-active-ext [frameworkset-pool.jar]
bboss-persistent<-bboss-ws [frameworkset-pool.jar]
bboss-persistent<-bbossaop [frameworkset-pool.jar]
bboss-persistent<-bbossevent [frameworkset-pool.jar]
bboss-persistent<-cms_baseline [frameworkset-pool.jar]
bboss-persistent<-bboss-taglib [frameworkset-pool.jar]
bboss-persistent<-kettle [frameworkset-pool.jar]
bboss-persistent<-portal [frameworkset-pool.jar]
bboss-persistent<-cas server [frameworkset-pool.jar]

to do list:
无
#######update function list since bbossgroups-3.5 begin###########
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
public void regexUtilvsVarialparserUtil()			
#######update function list since bbossgroups-3.4 begin###########

o 解决连接池中无法查找到tomcat 6和weblogic 容器数据源的问题
o 解决sql server jtd驱动无法正确找到DB adaptor的问题
o PreparedDBUtil增加public void setBlob(int i, String x) throws SQLException 方法，用来直接向blob类型字段中存入字符串
o 修改TestLob测试用例，用来演示SQLExecutor/ConfigSQLExecutor组件向Blob和clob中插入字符串的方法：
	@Test
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
	}
	
	注意如果
	public static class LobBean
	{
		private String id;
		@Column(type="blob")
		private String blobname;
		@Column(type="clob")
		private String clobname;
	}
	中type分别为blobfile或者clobfile时，就必须要求字段的值类型为以下三种：
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
#######update function list since bbossgroups-3.3 begin###########
------2011-09-22-------------
o 完善MultipartFile对象持久化功能，增加友好提示，如果对象属性类型为MultipartFile，要自动存储到数据库的blob或者clob字段时
需要添加@Column(type="blobfile")或者@Column(type="clobfile")；查询大字段数据时，避免将大字段注入到类型为MultipartFile的属性中
------2011-09-20-------------
o 完善ProArray对象序列化可能存在的问题
------2011-09-06-------------
o SQLParams中无法获取父类bean的字段定义信息
o ResultMap中无法获取父类bean的字段定义信息
o 将数字Wraper类型、Boolean、Charaset，Byte等Wraper类型的获取默认值调整为返回null
o SQLParams中getParamJavatype方法对Long，Double，Float，Short，Bloone处理不正确的问题


------2011-08-16-------------
o 解决动态sql语句中，bean属性没有set方法时导致逻辑判断不能正确解析的问题
------2011-08-15-------------
o 优化blob/clob处理，修改某些情况下blob/clob为空时报错的问题
#######update function list since bbossgroups-3.2 begin###########
------2011-08-05-------------
o 处理日期和时间类型时转换为字符串时，如果值为空时抛出空指针异常的问题修复
------2011-07-25-------------
o 解决sql server元数据获取为空的问题
------2011-07-24-------------
o 改进SQLParams api，可以直接对MultipartFile对象存入clob或者blob列。
sqlparams.addSQLParam("FILECONTENT", multipartfile,SQLParams.BLOBFILE);

对于大字段的处理建议采用以下方法：
sqlparams.addSQLParam("FILECONTENT", multipartfile,SQLParams.BLOBFILE);//直接传递MultipartFile对象进行插入
sqlparams.addSQLParam("FILECONTENT", inputStream, size,SQLParams.BLOBFILE);//直接传递InputStream对象以及流大小Size属性进行插入
------2011-07-15-------------
o 增加FieldRowHandler处理器，以便实现从blob/clob中获取单个字段文件对象的处理,其他类似类型数据也可以使用FieldRowHandler，使用示例如下：
public File getDownloadClobFile(String fileid) throws Exception
	{
		try
		{
			return SQLExecutor.queryTField(
											File.class,
											new FieldRowHandler<File>() {

												@Override
												public File handleField(
														Record record)
														throws Exception
												{

													// 定义文件对象
													File f = new File("d:/",record.getString("filename"));
													// 如果文件已经存在则直接返回f
													if (f.exists())
														return f;
													// 将blob中的文件内容存储到文件中
													record.getFile("filecontent",f);
													return f;
												}
											},
											"select filename,filecontent from CLOBFILE where fileid=?",
											fileid);
		}
		catch (Exception e)
		{
			throw e;
		}
	}
------2011-07-15------------
o 增加对文件上传入库和从db下载功能的支持,使用实例
上传
public boolean uploadFile(InputStream inputStream,long size, String filename) throws Exception {
		boolean result = true;
		String sql = "";
		try {
			sql = "INSERT INTO filetable (FILENAME,FILECONTENT,fileid,FILESIZE) VALUES(#[filename],#[FILECONTENT],#[FILEID],#[FILESIZE])";
			SQLParams sqlparams = new SQLParams();
			sqlparams.addSQLParam("filename", filename, SQLParams.STRING);
			sqlparams.addSQLParam("FILECONTENT", inputStream, size,SQLParams.BLOBFILE);
			sqlparams.addSQLParam("FILEID", UUID.randomUUID().toString(),SQLParams.STRING);
			sqlparams.addSQLParam("FILESIZE", size,SQLParams.LONG);
			SQLExecutor.insertBean(sql, sqlparams);			
			
		} catch (Exception ex) {
			ex.printStackTrace();
			result = false;
			throw new Exception("上传附件关联临控指令布控信息附件失败：" + ex);
		} finally {
			if(inputStream != null){
				inputStream.close();
			}
		}
		return result;
	}
下载	
	SQLExecutor.queryByNullRowHandler(new NullRowHandler(){
				@Override
				public void handleRow(Record record)
						throws Exception
				{
					record.getBlob("filecontent").getBinaryStream();
					StringUtil.sendFile(request, response, record.getString("filename"),record.getBlob("filecontent"));
				}}, "select * from filetable where fileid=?",fileid);
o 如果没有指定一条sql语句，PreparedDBUtil.executePreparedBatch将报出异常，这个不是很合理
直接改为info方式。

#######update function list since bbossgroups-3.1 begin###########

------2011-06-10------------
o 修正SQLExecutor中泛型字段查询API中的类型转换漏洞
------2011-06-8------------
o 修正不正常的提示信息，externaljndiName
o 修复只配置外部数据源的情况下，pool启动堆栈溢出问题
    java.lang.StackOverflowError
	at java.lang.Thread.currentThread(Native Method)
	at org.apache.xerces.util.SecuritySupport12$1.run(Unknown Source)
	at java.security.AccessController.doPrivileged(Native Method)
	at org.apache.xerces.util.SecuritySupport12.getContextClassLoader(Unknown Source)
	at org.apache.xerces.util.ObjectFactory.findClassLoader(Unknown Source)
	at org.apache.xerces.impl.dv.DTDDVFactory.getInstance(Unknown Source)
	at org.apache.xerces.impl.dv.DTDDVFactory.getInstance(Unknown Source)
	at 
	修改程序清单：
	com.frameworkset.common.poolman.sql.PoolMan
	com.frameworkset.common.poolman.util.JDBCPool
	com.frameworkset.common.poolman.util.SQLManager
	com.frameworkset.common.poolman.util.SQLUtil
	 	public static JDBCPool getJDBCPoolByJNDIName(String jndiname)
        {
            JDBCPool pool = SQLUtil.getSQLManager().getPoolByJNDIName(jndiname,true);
            return pool;
        }
        
        public static JDBCPool getJDBCPoolByJNDIName(String jndiname,boolean needcheckStart)
        {
            JDBCPool pool = SQLUtil.getSQLManager().getPoolByJNDIName(jndiname,needcheckStart);
            return pool;
        }
------2011-06-2------------
o 修复使用Column注解设置对象属性与列名映射时，导致sql语句中绑定相应的属性变量值失败的漏洞
------2011-05-20------------
o 修复引用容器datasource漏洞
o 修复通过模板启动数据源参数设置问题
o 修复一系列空指针问题
o 修复使用外部数据源时，当外部数据源是oracle时无法获取数据源的元数据问题

------2011-05-17------------
o 修复sqlexecutor对日期类型timestamp处理丢失时分秒的缺陷
------2011-05-03------------
o 修复在事务环境下通过JDBCPool的方法
public TableMetaData getTableMetaDataFromDatabase(Connection con,
			String tableName)
获取不到特定数据源的表元数据的问题
------2011-05-03------------
o 修复preDBUtil.preparedSelect(params, dbName, sql, offset, long)
	在查询没有数据的情况下，preDBUtil.getMeta()返回的是null；
#######update function list since bbossgroups-3.1 end###########


#######update function list since bbossgroups-3.0 begin###########
------2011-04-25------------
o 修复获取数字类型的值时，一旦rs中返回null时，没有正确获取数字缺省值的问题
ValueExchange.convert方法
------2011-04-18------------
o 完善事务泄露检测机制，在manager-provider.xml中增加检测页面地址类型配置：
<!-- 数据库事务泄露检测url类型范围配置 -->
	<property name="transaction.leakcheck.files" >
		<array componentType="String">
			<property value=".jsp"/>
			<property value=".do"/>
			<property value=".page"/>
			<property value=".action"/>
			<property value=".ajax"/>
		</array>
	</property>
------2011-04-16------------
o 完善带返回值的事务管理模板组件支持泛型类型的返回
public void stringarraytoList(final List<ListBean> beans) throws Throwable {
		List<ListBean> ret = TemplateDBUtil.executeTemplate(
				new JDBCValueTemplate<List<ListBean>>(){
					public List<ListBean>  execute() throws Exception {
						String sql = "INSERT INTO LISTBEAN (" + "ID," + "FIELDNAME," 
						+ "FIELDLABLE," + "FIELDTYPE," + "SORTORDER,"
						+ " ISPRIMARYKEY," + "REQUIRED," + "FIELDLENGTH,"
						+ "ISVALIDATED" + ")" + "VALUES"
						+ "(#[id],#[fieldName],#[fieldLable],#[fieldType],#[sortorder]"
						+ ",#[isprimaryKey],#[required],#[fieldLength],#[isvalidated])";
						SQLExecutor.delete("delete from LISTBEAN");
						SQLExecutor.insertBeans(sql, beans);
						return beans;
				}
			}
		);
	}
------2011-04-11------------
o 完善ConfigSQLExecutor和SQLExecutor组件中所有和bean对象相关的接口，
	Object bean参数可以是普通的的值对象，也可以是一个SQLParams对象,也可以是一个Map对象
	使用方法参考测试用例：
	/bboss-persistent/test/com/frameworkset/sqlexecutor/ConfigSQLExecutorTest.java

------2011-04-07------------
o 增加根据变量名称从配置文件中获取sql语句的来操作数据库组件,对应sql配置文件提供定时刷新机制
  如果检测到sql文件被修改，就从新加载文件（前提是开启刷新机制）
com.frameworkset.common.poolman.ConfigSQLExecutor
具体的使用方法为：
ConfigSQLExecutor executor = new ConfigSQLExecutor("com/frameworkset/sqlexecutor/sqlfile.xml");
Map dbBeans  =  executor.queryObject(HashMap.class, "sqltest");
String result = executor.queryFieldBean("sqltemplate", bean);
配置文件：

<?xml version="1.0" encoding='gb2312'?>
<properties>
<description>
<![CDATA[
	sql配置文件
	可以通过名称属性name配置默认sql，特定数据库的sql通过在
	名称后面加数据库类型后缀来区分，例如：
	sqltest
	sqltest-oracle
	sqltest-derby
	sqltest-mysql
	等等，本配置实例就演示了具体配置方法
 ]]>
</description>
	<property name="sqltest"><![CDATA[select * from LISTBEAN]]>
	</property>	
	<property name="sqltest-oracle"><![CDATA[select * from LISTBEAN]]>
	</property>
	<property name="sqltemplate"><![CDATA[select FIELDNAME from LISTBEAN where FIELDNAME=#[fieldName]]]>
	</property>
	<property name="sqltemplate-oracle"><![CDATA[select FIELDNAME from LISTBEAN where FIELDNAME=#[fieldName]  ]]>
	</property>	
	<property name="dynamicsqltemplate"><![CDATA[select *  from CIM_ETL_REPOSITORY  where 1=1 
					#if($HOST_ID && !$HOST_ID.equals("")) and HOST_ID = #[HOST_ID] #end  
						and PLUGIN_ID = #[PLUGIN_ID] and CATEGORY_ID = #[CATEGORY_ID] and APP = #[APP]]]>	
	</property>
</properties>

刷新机制的配置方法：
在manager-provider.xml文件中添加以下配置项即可：
<property name="sqlfile.refresh_interval" value="10000"/>
当value大于0时就开启sqlfile文件的更新检测机制，每隔value指定的时间间隔就检测一次，有更新就重新加载，否则不重新加载
------2011-04-06------------
o 增加一组查询单个字段的泛型接口，使用方法如下：
		String sql = "select REQUIRED from LISTBEAN ";
		int id=  SQLExecutor.queryTField(int.class, sql);
		long id=  SQLExecutor.queryTField(long.class, "select seq_name.nextval from LISTBEAN ");
		String sql = "select FIELDLABLE from LISTBEAN ";
		String id=  SQLExecutor.queryTField(String.class, sql);
		System.out.println(id);
o 3.0api增加返回List<HashMap>结果集的查询接口支持，使用方法如下（以预编译语句为例）：
@Test
	public void queryListMap() throws SQLException
	{
		String sql = "select * from LISTBEAN name=?";
		List<HashMap> dbBeans  =  SQLExecutor.queryListWithDBName(HashMap.class, "mysql", sql,"ttt");
		System.out.println(dbBeans);
	}
	
	
	public void queryListMapWithbeanCondition() throws SQLException
	{
		String sql = "select * from LISTBEAN name=#[name]";
		ListBean beanobject = new ListBean();
		beanobject.setName("duoduo");
		List<HashMap> dbBeans  =  SQLExecutor.queryListWithDBName(HashMap.class, "mysql", sql,beanobject);
		System.out.println(dbBeans);
	}
	
------2011-03-30------------
o 3.0api中完善对java.util.Date类型对象属性数据的处理
------2011-03-04------------
o 增加一个根据参数启动数据源的api，可以控制数据源是连接池数据源还是非连接池数据源
DBUtil中增加以下静态方法：
public static void startPool(String poolname,String driver,String jdbcurl,String username,String password,
        		String readOnly,
        		String txIsolationLevel,
        		String validationQuery,
        		String jndiName,   
        		int initialConnections,
        		int minimumSize,
        		int maximumSize,
        		boolean usepool,
        		boolean  external,
        		String externaljndiName        		
        		)
        		
o 数据源配置文件中增加usepool元素 ，可以控制数据源是连接池数据源还是非连接池数据源
<?xml version="1.0" encoding="gb2312"?>
<poolman>
	<datasource>
		<dbname>bspf</dbname>
		<loadmetadata>false</loadmetadata>
		<jndiName>jdbc/mysql-ds</jndiName>
		<driver>oracle.jdbc.driver.OracleDriver</driver>
		<usepool>false</usepool>
		<url>jdbc:oracle:thin:@//172.16.25.139:1521/dtjf</url>	
		<username>dtjf</username>
		<password>dtjf</password>
		<txIsolationLevel>READ_COMMITTED</txIsolationLevel>		
	    <logAbandoned>true</logAbandoned>
		<readOnly>false</readOnly>
		<keygenerate>composite</keygenerate>	
		<autoprimarykey>false</autoprimarykey>
		<showsql>false</showsql>
	</datasource>
</poolman>       		

o 调整非连接池数据源的监控数据采集和相关属性展示接口
------2011-03-02------------
 
o 修改odbc 驱动下使用o/r mapping查询，大字段处理异常问题
o 扩展SQLExecutor组件增加所有查询List结果集方法对泛型的支持
o 修复double类型数据向int类型转换的问题，新增单个值转换为数组的功能，支持数字类型数组之间的相互转换

#######update function list since bbossgroups-3.0 end###########
------------------------------------------------------------------------
update function list in bbossgroups-3.0 since bbossgroups-2.0-rc2
------------------------------------------------------------------------
o SQLExecutor组件中新增一组数据库操作api

------------------------------------------------------------------------
update function list in bbossgroups-2.0-rc2 since bbossgroups-2.0-rc1
------------------------------------------------------------------------
----------------------------------------
bbossgroups-2.1-RC - 2011-02-9
----------------------------------------
o 新增从数据库中直接获取表元数据和所有表元数据的api
----------------------------------------
bbossgroups-2.1-RC - 2011-01-20
----------------------------------------
o 完善连接池、外部连接池监控相关信息获取方式，如果外部连接池对应的实际连接池如果是一个内部的连接池
那么外部连接池的启动时间，停用时间将直接是实际连接池的时间。

----------------------------------------
bbossgroups-2.1-RC - 2010-12-30
----------------------------------------
o 增加从配置文件启动连接池方法-使用方法参考测试用例com.frameworkset.common.Monitor
o 增加在sql语句中设置逻辑判断语句功能,例如：
			PreparedDBUtil dbutil = new PreparedDBUtil();
			String listRepositorySql = "select *  from CIM_ETL_REPOSITORY  where 1=1 " +
					"#if($HOST_ID && !$HOST_ID.equals(\"\")) " +
					"	and HOST_ID = #[HOST_ID]" +
					"#end  " +
					" and PLUGIN_ID = #[PLUGIN_ID] " +
					" and CATEGORY_ID = #[CATEGORY_ID] and APP = #[APP]";
			String sql = listRepositorySql.toString();

			try {
				// 查询参数
				SQLParams params = new SQLParams();
				params.addSQLParam("HOST_ID", null,//设置host_id变量为的值Null或者空串""或者直接忽略添加该参数，那么语句中的and HOST_ID = #[HOST_ID]"将被忽略掉
												   //"#if($HOST_ID && !$HOST_ID.equals(\"\")) " +
												   //"	and HOST_ID = #[HOST_ID]" +
												   //"#end
						SQLParams.STRING);
				params.addSQLParam("PLUGIN_ID", "pluginid",
						SQLParams.STRING);
				params.addSQLParam("CATEGORY_ID", "catogoryid",
						SQLParams.STRING);
				params.addSQLParam("APP", "app", SQLParams.STRING);

				dbutil.preparedSelect(params, "bspf",
								sql);
				// 执行查询
				dbutil.executePrepared();

select *  from CIM_ETL_REPOSITORY  where 1=1 #if($HOST_ID && !$HOST_ID.equals("")) and HOST_ID = #[HOST_ID] #end   and PLUGIN_ID = #[PLUGIN_ID]  and CATEGORY_ID = #[CATEGORY_ID] and APP = #[APP]
----------------------------------------
bbossgroups-2.1-RC - 2010-12-01
----------------------------------------
o 修改查询元数据缓冲机制，采用分连接池缓冲的机制，避免数据库之间的冲突

o SQLParams和Params类添加copy方法，对sql参数进行复制，以便在标签库中进行分页查询时，当页面记录数发生变化时保持sqlparams参数

------------------------------------------------------------------------
update function list in bbossgroups-2.0-rc1 since bbossgroups-2.0-rc
------------------------------------------------------------------------
----------------------------------------
bbossgroups-2.0-rc1 - 2010-07-30
----------------------------------------
select to_char(inserttime,'HH24MISS'),count(*) from TEST_CURRENT 
group by to_char(inserttime,'HH24MISS') order by to_char(inserttime,'HH24MISS')
truncate table TEST_CURRENT
purge recyclebin
----------------------------------------
bbossgroups-2.0-rc1 - 2010-08-03
----------------------------------------
o 多数据库事务完善：
如果存在两个连接池bspf和mq上的一个事务，如果mq实际引用了bspf做为外部数据源，那么mq的对应子事务和bspf的子事务合并也就是说实际上
mq直接使用bspf的事务。
o 外部数据源监控信息完善，源数据加载完善，配置信息精简
<datasource external="true">

    <dbname>mq</dbname>
    <externaljndiName>jdbc/mysql-ds</externaljndiName>
	<showsql>false</showsql>

  </datasource>
  <datasource external="true">
    <dbname>kettle</dbname>
   
    <externaljndiName>jdbc/mysql-ds</externaljndiName>
	<showsql>false</showsql>
  </datasource>
  
  
----------------------------------------
bbossgroups-2.0-rc1 - 2010-07-23
----------------------------------------


o 增加to_char函数的适配,to_date函数适配，目前修改的有oracle，mysql
----------------------------------------
bbossgroups-2.0-rc1 - 2010-07-22
----------------------------------------
o 扩展db适配器数据库日期转换函数，增加指定转换日期格式参数方法
o 优化/bboss-persistent/src/com/frameworkset/common/poolman/management/BaseTableManager.java中加载tableinfo信息，uuid和sequence时无需计算表的最大值
o mysql 数据库适配器bug修改
/bboss-persistent/src/com/frameworkset/orm/adapter/DBMM.java
public String getIDMAXSql(String table_name,String table_id_name,String table_id_prefix,String type)
	{
    	//SUBSTR(table_id_name,LENGTH(table_id_prefix))
    	String maxSql = "select max("+ table_id_name + ") from " + table_name;
    	if(type.equalsIgnoreCase("string") || type.equalsIgnoreCase("java.lang.string"))
    	{
    		if(table_id_prefix != null && !table_id_prefix.trim().equals(""))
    		{
    		//将bigint 修改为DECIMAL，否则会报错，具体原因是mysql的cast函数不制止bigint类型
    			maxSql = "select max(CAST(SUBSTRING(" + table_id_name + ",len(" + table_id_prefix + ") + 1) as DECIMAL))) from " + table_name;
    		}
    		else
    		{
    		//将bigint 修改为DECIMAL，否则会报错，具体原因是mysql的cast函数不制止bigint类型
    			maxSql = "select max(CAST(" + table_id_name + " as DECIMAL)) from " + table_name;
    		}
    	}
		
		return maxSql;
	}

update function list:
----------------------------------------
bbossgroup 1.0rc - 2010-07-7
----------------------------------------
o 扩展适配器功能，用户可以自定义特定数据库的适配器
所实现的适配器必须从com.frameworkset.orm.adapter.DB继承或者其com.frameworkset.orm.adapter.DB子类继承
自定义适配器配置方法：
poolman.xml文件中配置：
<poolman>
	<adaptor dbtype="oracle">com.frameworkset.orm.adaptors.MyOracle
	</adaptor>
	<adaptor dbtype="oracle_other">com.frameworkset.orm.adaptors.OtherOracle
	</adaptor>
	<adaptor dbtype="db2net">com.frameworkset.orm.adaptors.MyDB2Adaptor
	</adaptor>
	.........

</poolman>

o DBUtil增加数据库连接池创建方法：
        public static void startPool(String poolname,String driver,String jdbcurl,String username,String password,String readOnly,String validationQuery)
    	{
        	SQLUtil.getSQLManager().startPool(poolname, driver, jdbcurl, username, password, readOnly, validationQuery);
    	}
    	
    	
o DBUtil增加拦截器,用来满足应用平台特殊的需求，其他应用无需关心这些问题
    	
    	public static InterceptorInf getInterceptorInf(String dbname)
    	{
    		return SQLUtil.getSQLManager().getPool(dbname).getInterceptor();
    	}
    	相关的程序：
    	com.frameworkset.common.poolman.interceptor.DummyInterceptor
    	com.frameworkset.common.poolman.interceptor.InterceptorInf
    	poolman.xml文件中配置InterceptorInf的方法：
    	在datasource中增加以下节点即可：
    	<interceptor>com.frameworkset.orm.adaptors.MyInterceptor</interceptor>
    	自定义的拦截器需要从com.frameworkset.common.poolman.interceptor.InterceptorInf接口继承，实现以下两个方法：
    	public String getDefaultDBName();
		public String convertSQL(String sql,String dbtype,String dbname);
    	或者从com.frameworkset.common.poolman.interceptor.DummyInterceptor继承，过载方法：
    	public String convertSQL(String sql, String dbtype, String dbname) {
			return sql;
		}
	
		public String getDefaultDBName() {
			return SQLManager.getInstance().getPool(null).getDBName();
			
		}
    	

o 修改weglobic环境下jndi查找失败bug
相关程序
com.frameworkset.common.poolman.util.JDBCPool
----------------------------------------
bbossgroup 1.0rc - 2010-07-2
----------------------------------------
o 修改db2源数据加载表数据时加载所有用户数据的问题，修改程序：
/bboss-persistent/src/com/frameworkset/common/poolman/util/JDBCPool.java
/bboss-persistent/src/com/frameworkset/orm/adapter/DB.java
----------------------------------------
bbossgroup 1.0rc - 2010-05-27
----------------------------------------
o 新增监控服务组件
com.frameworkset.common.poolman.monitor
----------------------------------------
bbossgroup 1.0rc - 2010-05-21
----------------------------------------
o 增加uuid主键生成机制
/bboss-persistent/src/com/frameworkset/common/poolman/sql/PrimaryKey.java
新增uuid机制将生成36位长度的字符串唯一主键值

----------------------------------------
bbossgroup 1.0rc - 2010-05-07
----------------------------------------

o mysql主键生成机制bug修复
生成主键的方法中直接写死了dbname和sequence名称
o com.frameworkset.commons.dbcp.DelegatingResultSet去除了对OracleResultSet的依赖
----------------------------------------
bbossgroup 1.0rc - 2010-05-05
----------------------------------------
o 新增关闭连接池方法
----------------------------------------
 DBUtil.stopPool(dbname);
 DBUtil.startPool(dbname);
bbossgroup 1.0rc - 2010-04-29
----------------------------------------
o 增加pool的监控信息
starttime
stoptime
----------------------------------------
bbossgroup 1.0rc - 2010-04-24
----------------------------------------
o 增加mysql主键生成机制
1.创建sequence数据库
CREATE DATABASE sequence; 
在sequence上创建表：
CREATE TABLE sequence.sequence_data ( 
sequence_name varchar(100) NOT NULL, 
sequence_increment int(11) unsigned NOT NULL DEFAULT 1, 
sequence_min_value int(11) unsigned NOT NULL DEFAULT 1, 
sequence_max_value bigint(20) unsigned NOT NULL DEFAULT 18446744073709551615, 
sequence_cur_value bigint(20) unsigned DEFAULT 1, 
sequence_cycle boolean NOT NULL DEFAULT FALSE, 
PRIMARY KEY (sequence_name) 
) ENGINE=MyISAM;
创建表sequence
-- This code will create sequence with default values. 
--INSERT INTO sequence.sequence_data 
--(sequence_name) 
--VALUE 
--(sq_my_sequence) ; 

 
-- You can also customize the sequence behavior. 
--INSERT INTO sequence.sequence_data 
--(sequence_name, sequence_increment, sequence_max_value) 
--VALUE 
--(sq_sequence_2, 10, 100) 
--;

创建获取sequence值的函数：
CREATE FUNCTION nextval (seq_name varchar(100)) 
RETURNS bigint(20) NOT DETERMINISTIC 
BEGIN
DECLARE cur_val bigint(20); 
SELECT
sequence_cur_value INTO cur_val 
FROM
sequence.sequence_data 
WHERE
sequence_name = seq_name ; 
IF cur_val IS NOT NULL THEN
UPDATE
sequence.sequence_data 
SET
sequence_cur_value = IF ( 
(sequence_cur_value + sequence_increment) > sequence_max_value, 
IF ( 
sequence_cycle = TRUE, 
sequence_min_value, 
NULL
), 
sequence_cur_value + sequence_increment ) 
WHERE
sequence_name = seq_name ; 
END IF; 
RETURN cur_val; 
END;

修改的程序
/bboss-persistent/src/com/frameworkset/common/poolman/sql/PrimaryKey.java
/bboss-persistent/src/com/frameworkset/orm/adapter/DB.java--增加sequence生成主键方法
/bboss-persistent/src/com/frameworkset/orm/adapter/DBMM.java--增加sequence生成主键方法

o 修改bug,当连接池还没有初始化时通过以下方法获取主键时报NullpointException
PreparedDBUtil.getNextPrimaryKey("mysql",
							"cim_dbpool"))
修改程序
/bboss-persistent/src/com/frameworkset/common/poolman/DBUtil.java
添加静态初始化代码

----------------------------------------
1.0.5 - 2010-04-01
----------------------------------------
o 添加获取数据库池名称列表的方法
com.frameworkset.common.poolman.util.SQLManager
com.frameworkset.common.poolman.util.PoolManager

 public List<String> getAllPoolNames() {
        assertLoaded();
        return super.getAllPoolNames();
    }

com/frameworkset/common/poolman/util/SQLUtil.java

/**
	 * Get a Vector containing all the names of the database pools currently in
	 * use.
	 */
	public Enumeration getAllPoolnames() {
		SQLManager datab = getSQLManager();
		if (datab == null)
			return null;
		return datab.getAllPoolnames();
	}
    /**
----------------------------------------
1.0.5 - 2010-03-30
----------------------------------------
o 解决mysql数据库调用Record.getClob和Record.getBlob方法报类型转换异常问题
原因分析mysql中blob和clob字段返回的jdbc类型分别为LONGVARBINARY和LONGVARCHAR
而/bboss-persistent/src/com/frameworkset/common/poolman/handle/ValueExchange.java类中将
LONGVARBINARY和LONGVARCHAR类型的字段读取时采用ResultSet.getObject方法来获取，默认返回byte[]数组类型
解决办法：

LONGVARBINARY-采用ResultSet.getBlob方法来获取
LONGVARCHAR-采用ResultSet.getClob方法来获取

在数据库适配器com.frameworkset.orm.adapter.DB中提供LONGVARBINARY和LONGVARCHAR类型值的适配器方法，以便不同的数据库对这两种类型的数据进行特殊处理
mysql数据库适配器com.frameworkset.orm.adapter.DBMM覆盖DB中的方法做blob和clob处理，其他类型数据库暂时按DB中的默认方法来处理
----------------------------------------
1.0.5 - 2010-03-29
----------------------------------------
o 修复问题，DBUtil.getDBDate(new Date())方法时报格式不正确问题
解决办法
com.frameworkset.orm.adapter.DBOracle中新增变量db_format，保证java日期格式DATE_FORMAT和oracle日期格式保持一致
//DD-MM-YYYY HH24:MI:SS old format
    public static final String db_format = "yyyy-MM-dd HH24:mi:ss";


o 增加sql模板预编译操作组件
com.frameworkset.common.poolman.SQLExecutor
SQLExecutor主要用来支撑bboss标签库预编译增、删、改、查、批处理功能，后续将直接开放给应用程序使用

主要提供以下特性：
数据库预编译/普通查询操作，
插入，删除，修改
预编译插入，删除，修改
批处理预编译/普通插入，删除，修改操作

系统提供注解模式来支持所有预编译操作模式
SQLExecutor组件在原有数据库组件的基础上开发的一个新的组件，继承原有全部功能。


----------------------------------------
1.0.5 - 2010-01-19
----------------------------------------

o 增加事务类型枚举类com.frameworkset.orm.annotation.TransactionType
定义了以下几种类型：
/**
     * 始终创建新事务
     */
    NEW_TRANSACTION,
    
    /**
     * 如果没有事务创建新事务，有事务加入当前事务
     */
    REQUIRED_TRANSACTION,
    /**
     * 有事务就加入事务，没有不创建事务,默认情况
     */
    MAYBE_TRANSACTION,
    /**
     * 没有事务
     */
    NO_TRANSACTION,
    
    /**
     * 未知事务类型
     */
    UNKNOWN_TRANSACTION,
    
    /**
     * 读写事务类型，支持数据库读写操作，事务中所做的操作
     * 其他事务都可以看到
     */
    RW_TRANSACTION

----------------------------------------
1.0.5 - 2010-01-19
----------------------------------------
o oracle和derby，mysql处理lob字段的区别：
对lob字段的写操作oracle，derby，mysql中基本一致
读lob字段时，oracle支持在读取事务之外还可以读取blob字段和clob字段的内容，但是derby和mysql中就不可以，这里以clob字段为示例，例如：

oracle中作以下操作是可以的：
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {
			//查询大字段内容并且将大字段存放到文件中
			dbUtil.preparedSelect( "select id,clobname from test");
			dbUtil.executePrepared();//执行读取事务
			
			for(int i = 0; i < dbUtil.size(); i ++)
			{
				
				dbUtil.getFile(i, "clobname", new java.io.File("d:/route" + i + ".txt")); //读取clob字段到文件中，在读取的事务之外操作clob字段
//				String clobvalue = dbUtil.getString(i, "clobname");//获取clob字段到字符串变量中
//				Clob clob = dbUtil.getClob(i, "clobname");//获取clob字段值到clob类型变量中
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			dbUtil = null;
		}

那么在mysql和derby中怎么做呢，那就要借助于NullRowHandler处理器或者RowHandler处理器来实现一个事务内对blob字段的处理了(blob为例)：
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {

			//查询大字段内容并且将大字段存放到文件中
			dbUtil.preparedSelect("derby", "select id,blobname from test");
			final List<File> datas = new ArrayList<File>();//存储blob字段blobname到datas列表中
			dbUtil.executePreparedWithRowHandler(new NullRowHandler(){

                @Override
                public void handleRow(Record origine) throws Exception
                {
                    File file = origine.getFile("blobname", new java.io.File("resources/lob/reader/dominspector_" + origine.getRowid() +".rar"));
                    datas.add(file);
                }});
			
			System.out.println("testblobRead dbUtil.size():"+dbUtil.size());//输出结果集的大小
			System.out.println("testblobRead datas.size():"+datas.size());//输出结果集的大小
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
		finally
		{
			
		}

o DBUtil中增加了以下方法,用来支持不带返回值的普通查询和分页查询操作：

public void executeSelectWithRowHandler(String sql,Connection con,NullRowHandler rowhandler)  throws SQLException
public void executeSelectWithRowHandler(String sql,NullRowHandler rowhandler)  throws SQLException
public void executeSelectWithRowHandler(String dbname,String sql,NullRowHandler rowhandler)  throws SQLException
public void executeSelectWithRowhandler(String dbname, String sql, Connection con,NullRowHandler rowhandler) throws SQLException
public void executeSelectWithRowHandler(String sql,long offset,int pagesize,Connection con,NullRowHandler rowhandler)  throws SQLException
public void executeSelectWithRowHandler(String sql,long offset,int pagesize,NullRowHandler rowhandler)  throws SQLException
public void executeSelectWithRowHandler(String dbname,String sql,long offset,int pagesize,NullRowHandler rowhandler)  throws SQLException
public void executeSelectWithRowhandler(String dbname, String sql,long offset,int pagesize, Connection con,NullRowHandler rowhandler) throws SQLException

用户可以通过上述方法自由地构造自己的查询结果集合    ,可参考简单的测试用列：
/bboss-persistent/test/com/frameworkset/common/rowhandler/DBUtilRowHandler.java

o PreparedDBUtil中添加两个方法,用来支持不带返回值的普通查询和分页查询操作：

public void executePreparedWithRowHandler(NullRowHandler rowhandler) throws SQLException
public void executePreparedWithRowHandler(Connection con,NullRowHandler rowhandler) throws SQLException
用户可以通过上述方法自由地构造自己的查询结果集合      ,可参考简单的测试用列：
/bboss-persistent/test/com/frameworkset/common/rowhandler/PreparedDBUtilRowhandler.java


o 增加抽象类com.frameworkset.common.poolman.handle.NullRowHandler，

public abstract class NullRowHandler extends BaseRowHandler
{

    。。。。。。
    

    public abstract void handleRow(Record origine) throws Exception;
}
用来处理自定义返回值类型，具体实现类实现抽象方法public abstract void handleRow(Record origine) throws Exception;参数record为当前记录可以进行相应的处理

具体的使用方法，请参考测试用例/bboss-persistent/test/com/frameworkset/derby/TestLob.java中的方法：
public void testBlobRead()  throws Exception

o 修正使用mysql和derby时的一些问题，

o mysql,derby数据库的datasource配置文件poolman.xml中的readOnly属性要设置为false

o 增加注解方式的数据库事务管理
	@Transaction("REQUIRED_TRANSACTION")
    @RollbackExceptions("") //@RollbackExceptions("{exception1,excpetion2}")
    详细情况参看测试用例/bbossaop/test/com/chinacreator/spi/transaction/annotation
----------------------------------------
1.0.5 - 2009-12-14
----------------------------------------
o 修改prepareddbutil中的分页查询方法，将offset参数类型由int修改为long
/bboss-persistent/src/com/frameworkset/common/poolman/PreparedDBUtil.java
----------------------------------------
1.0.5 - 2009-6-16
----------------------------------------

o PoolManager 增加以下方法：
 public boolean exist(String name)
    {
        return this.pools.containsKey(name);
    }
  SQLUtil  增加以下方法：
  判断数据库连接池是否存在
   public static boolean exist(String dbname)
	    {
	      SQLManager datab = getSQLManager();
              return  datab.exist(dbname);
	    }
   

o 修改公司信息

o 修改邮件地址错误问题
If the StackTrace contains an InstanceAlreadyExistsException, then you have  encountered a ClassLoader linkage problem.  Please email poolman@codestudio.com **
	at com.frameworkset.common.poolman.util.SQLManager.requestConnection(SQLManager.java:190)
	at com.frameworkset.common.poolman.util.SQLUtil.getConection(SQLUtil.java:930)
	at com.chinacreator.mq.transfer.send.SendBigData.execute(SendBigData.java:268)
o 增加Serializable对象获取接口
com.frameworkset.common.poolman.DBUtil

	
	public Serializable getSerializable(int rowid,String fieldname) throws SQLException
	{
	    inrange(rowid, fieldname);
            return allResults[getTrueRowid(rowid)].getSerializable(fieldname);
	}
	
	public Serializable getSerializable(int rowid,int columnIndex)throws SQLException
        {
	    inrange(rowid, columnIndex);
            return allResults[getTrueRowid(rowid)].getSerializable(columnIndex);
        }

com.frameworkset.common.poolman.Record
public Serializable getSerializable(String field) throws SQLException
    {
        Blob blog = this.getBlob(field);
        if(blog == null)
            return null;
        Serializable object = null;
        InputStream in = null;        
        java.io.ObjectInputStream objectin = null;
        try
        {
            
            in = blog.getBinaryStream();
            objectin = new ObjectInputStream(in);
            
            object = (Serializable)objectin.readObject();
        }
        catch(SQLException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new NestedSQLException(e);
        }
        return object;
    }
    
    public Serializable getSerializable(int parameterIndex) throws SQLException
    {
        Blob blog = this.getBlob(parameterIndex);
        if(blog == null)
            return null;
        Serializable object = null;
        InputStream in = null;        
        java.io.ObjectInputStream objectin = null;
        try
        {
            
            in = blog.getBinaryStream();
            objectin = new ObjectInputStream(in);
            
            object = (Serializable)objectin.readObject();
        }
        catch(SQLException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new NestedSQLException(e);
        }
        return object;
        
    }
o 从bboss-persistent\src\com\frameworkset\common\poolman\Record.java
  获取blob对象堆栈溢出的问题：
  java.lang.StackOverflowError
	at com.frameworkset.common.poolman.Record.getBlob(Record.java:1630)
  原因分析：以下方法陷入死循环
  public Blob getBlob (String parameterName) throws SQLException
{
	return (Blob)this.getBlob(parameterName);
}
修改为：
public Blob getBlob (String parameterName) throws SQLException
    {
    	return (Blob)this.getObject(parameterName);
    }
  即可。
  
o 异常：java.util.ConcurrentModificationException: concurrent access to HashMap attempted by Thread[Thread-58,5,main]
错误描述：
	[09-4-28 16:37:53:375 CST] 0000003b SystemErr     R java.util.ConcurrentModificationException: concurrent access to HashMap attempted by Thread[Thread-58,5,main]
	at java.util.HashMap.onEntry(HashMap.java(Inlined Compiled Code))
	at java.util.HashMap.transfer(HashMap.java(Compiled Code))
	at java.util.HashMap.resize(HashMap.java(Inlined Compiled Code))
	at java.util.HashMap.addEntry(HashMap.java(Compiled Code))
	at java.util.HashMap.put(HashMap.java(Compiled Code))
	at com.frameworkset.common.poolman.sql.PrimaryKeyCache.loaderPrimaryKey(PrimaryKeyCache.java:143)
	at com.frameworkset.common.poolman.sql.PrimaryKeyCache.getIDTable(PrimaryKeyCache.java:95)
	at com.frameworkset.common.poolman.util.StatementParser.refactorInsertStatement(StatementParser.java:421)
	at com.frameworkset.common.poolman.util.StatementParser.refactorInsertStatement(StatementParser.java:362)
	at com.frameworkset.common.poolman.DBUtil.doJDBCInsert(DBUtil.java:1372)
	at com.frameworkset.common.poolman.DBUtil.executeInsert(DBUtil.java:1166)
	at com.frameworkset.common.poolman.DBUtil.executeInsert(DBUtil.java:1176)
	at com.frameworkset.common.poolman.DBUtil.executeInsert(DBUtil.java:1172)
	at com.chinacreator.sms.BaseMTWriterImpl.traceSendSuccess(BaseMTWriterImpl.java:61)
	at com.chinacreator.sms.thread.QueueServiceHandleThread.run(QueueServiceHandleThread.java:79)
	at java.lang.Thread.run(Thread.java:570)

[09-4-28 16:37:53:375 CST] 0000003b SystemErr     R 	at java.util.HashMap.onEntry(HashMap.java(Inlined Compiled Code))
[09-4-28 16:37:53:375 CST] 0000003b SystemErr     R 	at java.util.HashMap.transfer(HashMap.java(Compiled Code))
[09-4-28 16:37:53:375 CST] 0000003b SystemErr     R 	at java.util.HashMap.resize(HashMap.java(Inlined Compiled Code))
[09-4-28 16:37:53:375 CST] 0000003b SystemErr     R 	at java.util.HashMap.addEntry(HashMap.java(Compiled Code))
[09-4-28 16:37:53:375 CST] 0000003b SystemErr     R 	at java.util.HashMap.put(HashMap.java(Compiled Code))
[09-4-28 16:37:53:375 CST] 0000003b SystemErr     R 	at com.frameworkset.common.poolman.sql.PrimaryKeyCache.loaderPrimaryKey(PrimaryKeyCache.java:143)
[09-4-28 16:37:53:375 CST] 0000003b SystemErr     R 	at com.frameworkset.common.poolman.sql.PrimaryKeyCache.getIDTable(PrimaryKeyCache.java:95)
[09-4-28 16:37:53:375 CST] 0000003b SystemErr     R 	at com.frameworkset.common.poolman.util.StatementParser.refactorInsertStatement(StatementParser.java:421)
[09-4-28 16:37:53:375 CST] 0000003b SystemErr     R 	at com.frameworkset.common.poolman.util.StatementParser.refactorInsertStatement(StatementParser.java:362)
[09-4-28 16:37:53:375 CST] 0000003b SystemErr     R 	at com.frameworkset.common.poolman.DBUtil.doJDBCInsert(DBUtil.java:1372)
[09-4-28 16:37:53:375 CST] 0000003b SystemErr     R 	at com.frameworkset.common.poolman.DBUtil.executeInsert(DBUtil.java:1166)
[09-4-28 16:37:53:375 CST] 0000003b SystemErr     R 	at com.frameworkset.common.poolman.DBUtil.executeInsert(DBUtil.java:1176)
[09-4-28 16:37:53:375 CST] 0000003b SystemErr     R 	at com.frameworkset.common.poolman.DBUtil.executeInsert(DBUtil.java:1172)
[09-4-28 16:37:53:375 CST] 0000003b SystemErr     R 	at com.chinacreator.sms.BaseMTWriterImpl.traceSendSuccess(BaseMTWriterImpl.java:61)
[09-4-28 16:37:53:375 CST] 0000003b SystemErr     R 	at com.chinacreator.sms.thread.QueueServiceHandleThread.run(QueueServiceHandleThread.java:79)
[09-4-28 16:37:53:375 CST] 0000003b SystemErr     R 	at java.lang.Thread.run(Thread.java:570)

问题分析：这个错误只是偶尔出现，在服务器压力大，或者数据库压力大的时候容易出现，参照文档：http://jira.codehaus.org/browse/XFIRE-1119
解决方案：找到这个类中的方法：
com.frameworkset.common.poolman.sql.PrimaryKeyCache
里面对应的Map，将map修改为同步Map：
	id_tables = new java.util.concurrent.ConcurrentHashMap(new HashMap());
	
o 增加行处理器对泛型的支持

public abstract class RowHandler<T>
public abstract void handleRow(T rowValue,Record record);



----------------------------------------
1.0.4 - 2009-5-22
----------------------------------------
o 调整com.frameworkset.common.poolman.handle.RowHandler

将原来的接口改为抽象类
将接口方法public void handleRow(Object rowValue,Record origine)
改为抽象方法public abstract void handleRow(Object rowValue,Record origine)
增加init和destroy方法，以便进行查询源数据和数据库名称dbname的初始化和销毁，这些信息对存储过程和函数调用中的行处理器不起作用。

增加方法
public SchemaType getSchemaType(String colName)
以便在行处理器中 获取列的java数据类型名称和数据库类型名称
        
o 修改com/frameworkset/common/poolman/Record.java类
增加记录的原始行号信息和get/set方法：
/**
	 * 设置记录对应的数据库原始记录行号
	 */
	private int rowid;
	public void setRowid(int rowid)
	{
	    this.rowid = rowid;
	}
	public int getRowid()
        {
            return rowid;
        }
 
o ResoultMap类中对Record类的初始化方法进行了优化

o 扩展xml行处理器

可以自定义根节点名称
可以自定义字符集
可以自定义版本号
添加构造xml节点串的两个方法

实现
在com.frameworkset.common.poolman.handle.XMLRowHandler程序中添加以下方法：
        /**
         * rowValue类型为StringBuffer
         */
	public void handleRow(Object rowValue,Record origine) 的默认实现
	
/**
	 * 返回xml串的根节点名称
	 * 缺省为records，用户可以扩展这个方法
	 * @return
	 */
	public String getRootName()
	{
	    return "records";
	}
	
	/**
         * 返回xml的编码字符集
         * 缺省为gb2312，用户可以扩展这个方法
         * @return
         */
        public String getEncoding()
        {
            return "gb2312";
        }
        
        
        /**
         * 返回xml语法的版本号
         * 缺省为1.0，用户可以扩展这个方法
         * @return
         */
        public String getVersion()
        {
            return "1.0";
        }

构建xml节点串的方法
public static String buildNode(String columnNodeName,//xml节点名称
                                      String columnName,//结点列名name属性的值
                                      String columnType, //结点列jdbc类型属性名称值
                                      String columnJavaType, //结点列java类型属性名称值
                                      String value,//结点值
                                      String split)//结点与节点之间的分割符

public static String buildNode(String columnNodeName, //xml节点名称
                                Map attributes,//节点属性集
                                String value, //节点值
                                String split)//节点间的分割符
这些方法都有缺省实现，如果不一致的话可以在子类中覆盖。

使用实例
public class TestXMLHandler {
    public static void testCustomXMLHandler()
    {
        PreparedDBUtil db = new PreparedDBUtil();
        try {
            db.preparedSelect("select * from tableinfo");
//            String results_1 = db.executePreparedForXML();
            String results_ = db.executePreparedForXML(new XMLRowHandler(){
//
//               
                public void handleRow(Object rowValue, Record origine)  {
                    StringBuffer record = (StringBuffer )rowValue;
                    record.append("    <record>\r\n");
   
                    try {
                        SchemaType schemaType = super.getSchemaType("TABLE_NAME"); 
                        record.append(super.buildNode("attribute", 
                                                      "TABLE_NAME", 
                                                      schemaType.getName(),
                                                      schemaType.getJavaType(), 
                                                      origine.getString("TABLE_NAME"),
                                                      "\r\n"));
                        schemaType = super.getSchemaType("table_id_name");
                        record.append(super.buildNode("attribute", 
                                                      "table_id_name", 
                                                      schemaType.getName(),
                                                      schemaType.getJavaType(),  
                                                      origine.getString("table_id_name"),
                                                      "\r\n"));
                        schemaType = super.getSchemaType("TABLE_ID_INCREMENT");
                        record.append(super.buildNode("attribute", 
                                                      "TABLE_ID_INCREMENT", 
                                                      schemaType.getName(),
                                                      schemaType.getJavaType(),  
                                                      origine.getString("TABLE_ID_INCREMENT"),
                                                      "\r\n"));
                        
                        schemaType = super.getSchemaType("TABLE_ID_GENERATOR");
                        record.append(super.buildNode("attribute", 
                                                      "TABLE_ID_GENERATOR", 
                                                      schemaType.getName(),
                                                      schemaType.getJavaType(), 
                                                      origine.getString("TABLE_ID_GENERATOR"),
                                                      "\r\n"));
                        schemaType = super.getSchemaType("TABLE_ID_TYPE");
                        record.append(super.buildNode("attribute", 
                                                      "TABLE_ID_TYPE", 
                                                      schemaType.getName(),
                                                      schemaType.getJavaType(),  
                                                      origine.getString("TABLE_ID_TYPE"),
                                                      "\r\n"));
                        
                    } catch (SQLException e) {
                        
                        throw new RowHandlerException(e);
                    }
                    record.append("    </record>\r\n");
                }

                
                public String getEncoding() {
                    // TODO Auto-generated method stub
                    return "gbk";
                }

               
                public String getRootName() {
                    // TODO Auto-generated method stub
                    return "tableinfo";
                }

               
                public String getVersion() {
                    // TODO Auto-generated method stub
                    return "2.0";
                }
                
            });
            
            System.out.println(results_);
//            System.out.println(results_1);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    public static void testDefualtXML()
    {
        PreparedDBUtil db = new PreparedDBUtil();
        try {
            db.preparedSelect("select * from tableinfo");
            String results_1 = db.executePreparedForXML();

            System.out.println(results_1);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args)
    {
        testCustomXMLHandler();
    }
}

o 修改bug，执行o/r mapping 查询时，如果数字类型/byte/boolean的数据值为null时，会报以下异常：
Build ValueObject for ResultSet[select * from mq_node where NODE_NAME='test'] Get Column[CA_ID] from  ResultSet to com.chinacreator.mq.client.MqNode@10cec16.CA_ID[int] failed:null
ERROR 01-06 17:30:25,093 - Build ValueObject for ResultSet[select * from mq_node where NODE_NAME='test'] Get Column[CA_ID] from  ResultSet to com.chinacreator.mq.client.MqNode@10cec16.CA_ID[int] failed:null
java.lang.IllegalArgumentException
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
	at java.lang.reflect.Method.invoke(Method.java:585)
	at com.frameworkset.common.poolman.ResultMap.buildValueObject(ResultMap.java:193)
	at com.frameworkset.common.poolman.StatementInfo.buildResultMap(StatementInfo.java:731)
	at com.frameworkset.common.poolman.util.SQLUtil.innerExecuteJDBC(SQLUtil.java:540)
	at com.frameworkset.common.poolman.DBUtil.executeSelectForObject(DBUtil.java:3753)
	at com.frameworkset.common.poolman.DBUtil.executeSelectForObject(DBUtil.java:3742)
	at com.frameworkset.common.poolman.DBUtil.executeSelectForObject(DBUtil.java:3618)
	at com.chinacreator.mq.client.MqNodeService.getNodeByName(MqNodeService.java:150)
	at be.ibridge.kettle.consumer_stream.ConsumerService.buildMQClient(ConsumerService.java:63)
	at be.ibridge.kettle.consumer_stream.Consumer.processRow(Consumer.java:155)
	at be.ibridge.kettle.consumer_stream.Consumer.run(Consumer.java:200)

原因分析：
数据库查询返回的结果集，由于数字类型/byte/boolean为null时，原来的处理程序直接返回null，而不是返回具体的数字类型/byte/boolean的缺省值，导致将null值设置给对象属性失败。

解决办法，修改对应数字类型/byte/boolean的handle接口提供null值的转换函数,将null转换为相应的缺省值。
com/frameworkset/common/poolman/handle/type/BigDecimalTypeHandler.java
com/frameworkset/common/poolman/handle/type/BooleanTypeHandler.java
com/frameworkset/common/poolman/handle/type/ByteTypeHandler.java
com/frameworkset/common/poolman/handle/type/DoubleTypeHandler.java
com/frameworkset/common/poolman/handle/type/FloatTypeHandler.java
om/frameworkset/common/poolman/handle/type/IntegerTypeHandler.java
com/frameworkset/common/poolman/handle/type/LongTypeHandler.java
com/frameworkset/common/poolman/handle/type/ShortTypeHandler.java

----------------------------------------
1.0.3-1 - 2009-4-16
----------------------------------------

o 问题描述：
当sql语句中即出现同名列又有不同名列时，就会在读值时报空指针异常。
控制台信息：

java.lang.NullPointerException
	at com.frameworkset.common.poolman.Record.seekField(Record.java:694)
	at com.frameworkset.common.poolman.Record.getObject(Record.java:724)
	at com.frameworkset.common.poolman.Record.getString(Record.java:156)
	at com.frameworkset.common.poolman.DBUtil.getValue(DBUtil.java:292)
	at com.chinacreator.jspreport.DimenDao.findReportBeans(DimenDao.java:133)
	at com.chinacreator.jspreport.DimenDao.getReport(DimenDao.java:222)
	at com.chinacreator.jspreport.report.ReportList.getDataList(ReportList.java:31)
	at com.frameworkset.common.tag.pager.DataInfoImpl.getItemCount(DataInfoImpl.java:147)
	at com.frameworkset.common.tag.pager.tags.PagerContext.setDataInfo(PagerContext.java:1094)
	at com.frameworkset.common.tag.pager.tags.PagerContext.initContext(PagerContext.java:954)
	at com.frameworkset.common.tag.pager.tags.PagerContext.init(PagerContext.java:209)
	at com.frameworkset.common.tag.pager.tags.PagerTag.doStartTag(PagerTag.java:1030)
	at org.apache.jsp.jspreport.report_005flist_jsp._jspService(report_005flist_jsp.java:299)
	at org.apache.jasper.runtime.HttpJspBase.service(HttpJspBase.java:133)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:856)
	at org.apache.jasper.servlet.JspServletWrapper.service(JspServletWrapper.java:311)
	at org.apache.jasper.servlet.JspServlet.serviceJspFile(JspServlet.java:301)
	at org.apache.jasper.servlet.JspServlet.service(JspServlet.java:248)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:856)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:284)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:204)
	at com.frameworkset.common.filter.CharsetEncodingFilter.doFilter(CharsetEncodingFilter.java:92)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:233)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:204)
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:256)
	at org.apache.catalina.core.StandardValveContext.invokeNext(StandardValveContext.java:151)
	at org.apache.catalina.core.StandardPipeline.invoke(StandardPipeline.java:563)
	at org.apache.catalina.core.StandardContextValve.invokeInternal(StandardContextValve.java:245)
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:199)
	at org.apache.catalina.core.StandardValveContext.invokeNext(StandardValveContext.java:151)
	at org.apache.catalina.core.StandardPipeline.invoke(StandardPipeline.java:563)
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:195)
	at org.apache.catalina.core.StandardValveContext.invokeNext(StandardValveContext.java:151)
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:164)
	at org.apache.catalina.core.StandardValveContext.invokeNext(StandardValveContext.java:149)
	at org.apache.catalina.core.StandardPipeline.invoke(StandardPipeline.java:563)
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:156)
	at org.apache.catalina.core.StandardValveContext.invokeNext(StandardValveContext.java:151)
	at org.apache.catalina.core.StandardPipeline.invoke(StandardPipeline.java:563)
	at org.apache.catalina.core.ContainerBase.invoke(ContainerBase.java:972)
	at org.apache.coyote.tomcat5.CoyoteAdapter.service(CoyoteAdapter.java:209)
	at org.apache.coyote.http11.Http11Processor.process(Http11Processor.java:670)
	at org.apache.coyote.http11.Http11Protocol$Http11ConnectionHandler.processConnection(Http11Protocol.java:517)
	at org.apache.tomcat.util.net.TcpWorkerThread.runIt(PoolTcpEndpoint.java:575)
	at org.apache.tomcat.util.threads.ThreadPool$ControlRunnable.run(ThreadPool.java:666)
	at java.lang.Thread.run(Thread.java:595)


修改程序
com/frameworkset/common/poolman/Record.java


------------------------------
 1.0.3 - 2009-4-13
------------------------------

 o 单独使用poolman，未建立表tableinfo，后台报SQLException异常
修改程序：
com/frameworkset/common/poolman/management/BaseTableManager.java

 o sql server 2005翻页查询异常问题
原因是sqlserver中需要这样创建statement
Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY); 
但是poolman中是这样获取Statement ：stmt=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
因此报下面的错误：
com.microsoft.sqlserver.jdbc.SQLServerException: 不支持此游标类型/并发组
合。

修改方法：
通过适配器方法来获取游标类型：

修改的程序
com\frameworkset\orm\adapter\DB.java
com\frameworkset\orm\adapter\DBMSSQL.java
com\frameworkset\common\poolman\StatementInfo.java
com\frameworkset\common\poolman\DBUtil.java


o 增加获取DataSource的接口
修改的程序：
com\frameworkset\common\poolman\util\SQLUtil.java
增加以下三个方法
获取缺省连接池对应的datasource
public static DataSource getDataSource()
	
获取给定数据库连接池名对应的datasource
	public static DataSource getDataSource(String dbname)
      
通过jndi名称查找数据源
  public static DataSource getDataSourceByJNDI(String jndiname) throws NamingException
      

com/frameworkset/common/poolman/handle/type/CallableStatementResultSet.java
增加以下方法：
public Object getObject(int arg0, Map arg1) throws SQLException


com/frameworkset/common/poolman/util/JDBCPool.java
增加方法：
public static DataSource find(String jndiName) throws NamingException


增加全局变量：
public static Context ctx = null;
	
o 新增bboss jndi 上下文环境
新增jndi上下文环境，解决系统脱离应用服务器单独运行时无法进行jndi绑定的问题。
新增和修改的程序如下：
com/frameworkset/common/poolman/jndi/DummyContext.java（新增）
com/frameworkset/common/poolman/jndi/DummyContextFactory.java（新增）
com/frameworkset/common/poolman/util/JDBCPool.java（修改）



o DBUtil执行两个表的联合查询，如果两表的查询字段中包含相同字段名但没有指定这些字段的别名时，查询结果中前面的表字段的值被后面的表字段的值覆盖。例如：
select table_a.id,table_b.id from table_a,table_b;

代码段如下：
DBUtil dbUtil = new DBUtil();
		dbUtil.executeSelect("select table_a.id,table_b.id from table_a,table_b ");
		System.out.println("table_a.id" + dbUtil.getInt(0, 0));
		System.out.println("table_b.id" + dbUtil.getInt(0, 1));

查询的结果显示table_a.id的值变为table_b.id字段的值。

原因分析：

执行本次查询后，结果集元数据为：
[id,id]
可见两个查询字段的列名相同，有没有指定别名字段，由于DBUtil临时存放结果集的数据结构为一个hash表，key为字段名，值为相应字段的值，这样就导致原来的字段的值被前面的字段的值覆盖。
解决办法：
A.	为字段指定不同的别名
B.	修改原来的存储机制，为相同的列名建立不同的别名
建立别名的规则为：列名+#$_+列索引，例如：
将原来的[id,id]转换为[id,id#$_1]


修改的程序清单为：
/bboss-persistent/src/com/frameworkset/common/poolman/sql/PoolManResultSetMetaData.java
/bboss-persistent/src/com/frameworkset/common/poolman/ResultMap.java
/bboss-persistent/src/com/frameworkset/common/poolman/Record.java
/bboss-persistent/src/com/frameworkset/common/poolman/DBUtil.java

测试脚本：
create table TABLE_A
(
  ID  NUMBER(10),
  ID1 NUMBER(10)
)
/
create table TABLE_b
(
  ID  NUMBER(10),
  ID1 NUMBER(10)
)
/
insert into table_a (id,id1) value(1,11)
/
insert into table_b (id,id1) value(2,22)
/

测试程序：
/bboss-persistent/test/com/frameworkset/common/TestTwoTablewithSameCol.java
执行查询结果正确。

o  sqlserver中如果数据库表有列的类型是char(1),那么, DBUtil查询这个字段的时候,就报错
出错的原因可能是poolman.xml文件中指定的数据库驱动程序不正确。
<jndiName>hb_datasource_jndiname</jndiName>
<driver>com.microsoft.sqlserver.jdbc.SQLServerDriver</driver>
正确的配法为：
<jndiName>hb_datasource_jndiname</jndiName>
<driver>com.microsoft.jdbc.sqlserver.SQLServerDriver</driver>
现在不明确是否存在com.microsoft. sqlserver.jdbc.SQLServerDriver驱动程序，
估计这个驱动程序是sqlserver 2005版本的驱动程序。
需要修正程序：
/bboss-persistent/src/com/frameworkset/orm/adapter/DBFactory.java
以便能够正确地找到sql server的适配器（不同数据库的适配器，是通过驱动程序查找的）。修正后，问题解决。

o  改造oracle分页机制
修改程序：
com/frameworkset/orm/adapter/DBOracle.java




------------------------------
 1.0.2 - DEC 24, 2008
------------------------------

 o Improved  blob/clob handle perfermence.
 o Fixed maybe memery leaker in handle blob/clob columns.

First version of bboss persitent released.

------------------------------
 1.0.1 - DEC 14, 2008
------------------------------

First version of bboss persitent released.

hibernate.rar 
这个文件包含了bboss persistent 和 hibernate结合的程序，包括hibernate使用bboss persistent 生成主键
hibernate使用bboss persistent 提供的数据源

[http://blog.csdn.net/yin_bp]