/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.  
 */
package com.frameworkset.common.poolman.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.frameworkset.spi.BaseApplicationContext;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.NestedSQLException;
import com.frameworkset.common.poolman.PoolManConstants;
import com.frameworkset.common.poolman.interceptor.InterceptorInf;
import com.frameworkset.common.poolman.jndi.ContextUtil;
import com.frameworkset.common.poolman.jndi.DummyContextFactory;
import com.frameworkset.common.poolman.monitor.AbandonedTraceExt;
import com.frameworkset.common.poolman.security.DBInfoEncrypt;
import com.frameworkset.common.poolman.sql.ColumnMetaData;
import com.frameworkset.common.poolman.sql.ForeignKeyMetaData;
import com.frameworkset.common.poolman.sql.IdGenerator;
import com.frameworkset.common.poolman.sql.PoolManDataSource;
import com.frameworkset.common.poolman.sql.PrimaryKeyMetaData;
import com.frameworkset.common.poolman.sql.TableMetaData;
import com.frameworkset.commons.dbcp.BasicDataSourceFactory;
import com.frameworkset.orm.adapter.DB;
import com.frameworkset.orm.adapter.DBFactory;
import com.frameworkset.orm.transaction.JDBCTransaction;
import com.frameworkset.orm.transaction.TXDataSource;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.util.StringUtil;

/**
 * JDBCPool is an ObjectPool of JDBC connection objects. It is also a
 * javax.sql.ConnectionEventListener, so it can respond to events fired by the
 * PooledConnection implementation, PoolManConnection.
 */
public class JDBCPool {
	private static Logger log = Logger.getLogger(JDBCPool.class);
	public static String TABLE_TYPE_VIEW = "VIEW";
	public static String TABLE_TYPE_TABLE = "TABLE";
	public static String TABLE_TYPE_ALL = "ALL";
	private DataSource datasource;
	
	public static final boolean nameMapping ;
	static {
		boolean namemp = false;
		try {
			Properties  p = BaseApplicationContext.fillProperties();
			String aaa = p.getProperty("column.nameMapping","false");
			if(aaa.trim().equals("true"))
			{
				namemp = true;
			}
			
		} catch (Exception e) {
			
		}
		nameMapping = namemp;
	}
	
	

//	protected JDBCPoolMetaData metadata;

	private boolean deployedDataSource;

	private JDBCPoolMetaData info;

	/**
	 * 启动时间
	 */
	private long startTime;

	/**
	 * 停止时间
	 */
	private long stopTime;

	private  Context ctx = null;
	private  Context dummyctx = null;
	private  boolean initcontexted = false;
	private IdGenerator idGenerator;
	private static Context buildContext(JDBCPoolMetaData meta)
	{
		Context ctx = null;
		if(meta != null)
		{
			if((meta.getJNDIName() == null || meta.getJNDIName().equals("")) 
					&& (meta.getExternaljndiName() == null || meta.getExternaljndiName().equals("")))
			{
				return null;
			}
		}
		if(meta != null && meta.getJndiclass() != null)
		{
			Hashtable environment = new Hashtable();
			environment.put(Context.INITIAL_CONTEXT_FACTORY, meta.getJndiclass());
			if(meta.getJndiurl() != null)
				environment.put(Context.PROVIDER_URL, meta.getJndiurl());
			if(meta.getJndiuser() != null)
				environment.put(Context.SECURITY_PRINCIPAL
						, meta.getJndiuser());
			if(meta.getJndipassword() != null)
				environment.put(Context.SECURITY_CREDENTIALS, meta.getJndipassword());
			ctx = ContextUtil.finaContext(environment, JDBCPool.class.getClassLoader());
		}
		else
		{
			ctx = ContextUtil.finaContext(null, JDBCPool.class.getClassLoader());
		}
		return ctx;
	}

	private  void initctx(JDBCPoolMetaData meta)
	{
		if(ctx == null)
		{

			ctx = buildContext(meta);
			if(meta.getJNDIName() != null && !meta.getJNDIName().equals(""))
			{
				try {
					this.dummyctx = (new DummyContextFactory()).getInitialContext(null);
				} catch (NamingException e) {
					log.info(e.getMessage());
				}
			}
				
			initcontexted = true;
			
		}
	}

	

	private Hashtable preparedStatementPool;

	private Map tableMetaDatasindexByTablename = new java.util.concurrent.ConcurrentHashMap();

	private Set tableMetaDatas = new TreeSet();
	
	
	public final static InterceptorInf defaultInterceptor = new com.frameworkset.common.poolman.interceptor.DummyInterceptor();
	private InterceptorInf interceptor = defaultInterceptor;
	public final static String defaultInterceptor_s = "com.frameworkset.common.poolman.interceptor.DummyInterceptor";
	
	/**
	 * 链接池的适配器
	 */
	private DB dbAdapter;

	private String status = "unknown";

	public TableMetaData getTableMetaData(String tableName) {

		return getTableMetaData(null, tableName);
	}

	public TableMetaData getTableMetaData(Connection con, String tableName) {
		if(this.externalDBName == null)
		{
			TableMetaData table = (TableMetaData) tableMetaDatasindexByTablename
					.get(tableName.toLowerCase());
			if (table == null) {
				// 如果数据库元数据没有启用元数据加载机制，则动态加载其数据
				if (this.info.getLoadmetadata().equalsIgnoreCase("false")) {
					synchronized (tableMetaDatasindexByTablename) {
						table = (TableMetaData) tableMetaDatasindexByTablename
								.get(tableName.toLowerCase());
						if (table == null) {
							table = this.getTableMetaDataFromDatabase(con,
									tableName);
						}
					}
				}
			}
			return table;
		}
		else
		{
			return SQLManager.getInstance().getPool(externalDBName).getTableMetaData(con, tableName);
		}
	}

	public ColumnMetaData getColumnMetaData(Connection con, String tableName,
			String columnName) {
		TableMetaData table = getTableMetaData(con, tableName);
		if (table != null) {
			// TableMetaData table = getTableMetaData(tableName);
			if (columnName == null || columnName.equals("")) {
				return null;
			}
			return table.getColumnMetaData(columnName);
		}
		return null;
	}

	public ColumnMetaData getColumnMetaData(String tableName, String columnName) {

		return getColumnMetaData(null, tableName, columnName);
	}

	public Set getColumnMetaDatas(String tableName) {

		return getColumnMetaDatas(null, tableName);
	}

	public Set getColumnMetaDatas(Connection con, String tableName) {
		TableMetaData table = getTableMetaData(con, tableName);
		if (table != null)
			return table.getColumns();
		return null;
	}

	public ForeignKeyMetaData getForeignKeyMetaData(String tableName,
			String columnName) {

		return getForeignKeyMetaData(null, tableName, columnName);
	}

	public ForeignKeyMetaData getForeignKeyMetaData(Connection con,
			String tableName, String columnName) {
		TableMetaData table = getTableMetaData(con, tableName);
		if (table != null)
			return table.getForeignKeyMetaData(columnName);
		return null;
	}

	public Set getForeignKeyMetaDatas(String tableName) {

		return getForeignKeyMetaDatas(null, tableName);
	}

	public Set getForeignKeyMetaDatas(Connection con, String tableName) {
		TableMetaData table = getTableMetaData(con, tableName);
		if (table != null)
			return table.getForeignKeys();
		return null;
	}

	public PrimaryKeyMetaData getPrimaryKeyMetaData(String tableName,
			String columnName) {

		return getPrimaryKeyMetaData(null, tableName, columnName);
	}

	public PrimaryKeyMetaData getPrimaryKeyMetaData(Connection con,
			String tableName, String columnName) {
		TableMetaData table = getTableMetaData(con, tableName);
		if (table != null)
			return table.getPrimaryKeyMetaData(columnName);
		return null;
	}

	public Set getPrimaryKeyMetaDatas(String tableName) {
		return getPrimaryKeyMetaDatas(null, tableName);
	}

	public Set getPrimaryKeyMetaDatas(Connection con, String tableName) {
		TableMetaData table = getTableMetaData(con, tableName);
		if (table != null)
			return table.getPrimaryKeys();
		return null;
	}

	private void _initAdaptor()
	{
		String dbtype = info.getDbtype();
		String driver = info.getDriver();
		
		if (dbtype == null) {
			
			try {
				log.debug("Init DBAdapter from driver:" + driver);
				dbAdapter = DBFactory.create(driver);
			} catch (InstantiationException ex) {
				log.error(ex.getMessage(),ex);
			}
		} 
		else
		{
			try {
				log.debug("Init DBAdapter from dbtype:" + dbtype);
				dbAdapter = DBFactory.create(dbtype);
			} catch (InstantiationException ex1) {
				log.error(ex1.getMessage(),ex1);
			}
		}
	}
	
	private String externalDBName = null;
	
	
	/**
	 * 初始化数据库适配器
	 */
	private void initDBAdapter() {
		if(!this.info.isExternal())
		{
			_initAdaptor();
		}
		else //从外部数据源初始化dbadaptor
		{
			String external = this.info.getExternaljndiName();
			JDBCPool ep = null; 
			ep = DBUtil.getJDBCPoolByJNDIName(external,false);
			if(ep == null)
			{
				_initAdaptor();
			}
			else
			{
				externalDBName = ep.getDBName();
				
				{
					JDBCPool pool = SQLManager.getInstance().getPool(this.externalDBName,false);
					this.info.setExtenalInfo(pool.getJDBCPoolMetadata());
				}
			}
			
			
		}
		this.info.setDbtype(this.getDbAdapter().getDBTYPE());
	}
	
	
	
	private Properties getProperties()
	{
		Properties p = new Properties();
		p.setProperty(PoolManConstants.PROP_DRIVERCLASSNAME, info
				.getDriver());
		DBInfoEncrypt dbInfoEncrypt = DB.getDBInfoEncrypt();
		if(this.info.isEncryptdbinfo())
		{
//			dbInfoEncrypt.decrypt(data)
			p.setProperty(PoolManConstants.PROP_URL, dbInfoEncrypt.decryptDBUrl(info.getURL()));
		}
		else 
		{
			p.setProperty(PoolManConstants.PROP_URL, info.getURL());
		}
		
		if (info.getPassword() != null)
		{
			if(this.info.isEncryptdbinfo())
			{
//				dbInfoEncrypt.decrypt(data)
				p.setProperty(PoolManConstants.PROP_PASSWORD, dbInfoEncrypt.decryptDBPassword(info
						.getPassword()));
			}
			else 
			{
				p.setProperty(PoolManConstants.PROP_PASSWORD, info
						.getPassword());
			}
			
			
		}
		if (info.getUserName() != null)
		{
			if(this.info.isEncryptdbinfo())
			{
				p.setProperty(PoolManConstants.PROP_USERNAME, dbInfoEncrypt.decryptDBUser(info
						.getUserName()));
			}
			else 
			{
				p.setProperty(PoolManConstants.PROP_USERNAME, info
						.getUserName());
			}
			
		}
		p.setProperty(PoolManConstants.PROP_MAXACTIVE, info
				.getMaximumSize()
				+ "");
		p.setProperty(PoolManConstants.PROP_INITIALSIZE, info
				.getInitialConnections()
				+ "");
		p.setProperty(PoolManConstants.PROP_MAXIDLE, info
				.getMaximumSize()
				+ "");
		p.setProperty(PoolManConstants.PROP_MINIDLE, info
				.getMinimumSize()
				+ "");

		p.setProperty(
				PoolManConstants.PROP_TIMEBETWEENEVICTIONRUNSMILLIS,
				info.getSkimmerFrequency() + "");
		p.setProperty(PoolManConstants.PROP_NUMTESTSPEREVICTIONRUN,
				info.getShrinkBy() + "");
		p.setProperty(PoolManConstants.PROP_MINEVICTABLEIDLETIMEMILLIS,
				(info.getConnectionTimeout()) + "");

		p.setProperty(PoolManConstants.PROP_MAXWAIT, info.getMaxWait());
		p.setProperty(PoolManConstants.PROP_REMOVEABANDONED, info
				.getRemoveAbandoned());
		p.setProperty(PoolManConstants.PROP_REMOVEABANDONEDTIMEOUT,
				info.getUserTimeout() + "");
		p.setProperty(PoolManConstants.PROP_TESTWHILEIDLE, info
				.isTestWhileidle()
				+ "");
		p.setProperty(PoolManConstants.PROP_TESTONBORROW, "true");
		if (info.getValidationQuery() != null
				&& !info.getValidationQuery().equals("")) {
			p.setProperty(PoolManConstants.PROP_VALIDATIONQUERY, info
					.getValidationQuery());
		}
		p.setProperty(PoolManConstants.PROP_LOGABANDONED, info
				.isLogAbandoned()
				+ "");
		p.setProperty(PoolManConstants.PROP_DEFAULTAUTOCOMMIT, "true");
		if(info
				.isReadOnly() != null)
		p.setProperty(PoolManConstants.PROP_DEFAULTREADONLY, info
				.isReadOnly()
				+ "");
		String TxIsolationLevel = info
				.getTxIsolationLevel() ;
		if(TxIsolationLevel != null && !TxIsolationLevel.equals(""))
		{
			p.setProperty(
					PoolManConstants.PROP_DEFAULTTRANSACTIONISOLATION, TxIsolationLevel);
		}

		p.setProperty(PoolManConstants.PROP_POOLPREPAREDSTATEMENTS,
				info.isPoolPreparedStatements() + "");
		p.setProperty(PoolManConstants.PROP_MAXOPENPREPAREDSTATEMENTS,
				info.getMaxOpenPreparedStatements() + "");
		// if(info.getValidationQuery() != null)
		// p.setProperty(PoolManConstants.PROP_VALIDATIONQUERY,
		// info.getValidationQuery() );
//		if (info.isMaximumSoft())
//			p.setProperty(PoolManConstants.PROP_WHENEXHAUSTEDACTION,
//					GenericObjectPool.WHEN_EXHAUSTED_GROW + "");
//		else {
//			p.setProperty(PoolManConstants.PROP_WHENEXHAUSTEDACTION,
//					GenericObjectPool.WHEN_EXHAUSTED_BLOCK + "");
//		}
		
		p.setProperty(PoolManConstants.PROP_USEPOOL, info.isUsepool() + "");
		// public final static String PROP_DEFAULTREADONLY =
		// "defaultReadOnly";
		// public final static String PROP_DEFAULTTRANSACTIONISOLATION =
		// "defaultTransactionIsolation";
		// public final static String PROP_DEFAULTCATALOG =
		// "defaultCatalog";
		// public final static String PROP_DRIVERCLASSNAME =
		// "driverClassName";
		// public final static String PROP_MAXACTIVE = "maxActive";
		// public final static String PROP_MAXIDLE = "maxIdle";
		// public final static String PROP_MINIDLE = "minIdle";
		// public final static String PROP_INITIALSIZE = "initialSize";
		// public final static String PROP_MAXWAIT = "maxWait";
		// public final static String PROP_TESTONBORROW =
		// "testOnBorrow";
		// public final static String PROP_TESTONRETURN =
		// "testOnReturn";
		// public final static String PROP_TIMEBETWEENEVICTIONRUNSMILLIS
		// = "timeBetweenEvictionRunsMillis";
		// public final static String PROP_NUMTESTSPEREVICTIONRUN =
		// "numTestsPerEvictionRun";
		// public final static String PROP_MINEVICTABLEIDLETIMEMILLIS =
		// "minEvictableIdleTimeMillis";
		// public final static String PROP_TESTWHILEIDLE =
		// "testWhileIdle";
		// public final static String PROP_PASSWORD = "password";
		// public final static String PROP_URL = "url";
		// public final static String PROP_USERNAME = "username";
		// public final static String PROP_VALIDATIONQUERY =
		// "validationQuery";
		// public final static String
		// PROP_ACCESSTOUNDERLYINGCONNECTIONALLOWED =
		// "accessToUnderlyingConnectionAllowed";
		// public final static String PROP_REMOVEABANDONED =
		// "removeAbandoned";
		// public final static String PROP_REMOVEABANDONEDTIMEOUT =
		// "removeAbandonedTimeout";
		// public final static String PROP_LOGABANDONED =
		// "logAbandoned";
		// public final static String PROP_POOLPREPAREDSTATEMENTS =
		// "poolPreparedStatements";
		// public final static String PROP_MAXOPENPREPAREDSTATEMENTS =
		// "maxOpenPreparedStatements";
		// public final static String PROP_CONNECTIONPROPERTIES =
		// "connectionProperties";
		//				 

		return p;
	}
	
	private void initPoolDatasource() throws Exception
	{
		try {
			DataSource _datasource = null;
			if(StringUtil.isEmpty(this.info.getDatasourceFile()))//没有指定IOC数据源配置文件，直接初始化内置数据源apache dbcp
			{
				Properties p = getProperties();
				_datasource =  BasicDataSourceFactory
						.createDBCP2DataSource(p);
			}
			else //从ioc配置文件中获取数据源实例
			{
				_datasource =  DatasourceUtil.getDataSource(info.getDatasourceFile());
			}

			if (this.info.getJNDIName() != null
					&& !this.info.getJNDIName().equals("")) {
				this.datasource = new PoolManDataSource(_datasource, info
						.getDbname(), info.getJNDIName());
			} else {
				this.datasource = _datasource;
			}
			if(this.info.isEnablejta())
			{
				this.datasource = new TXDataSource(datasource,this);
			}
			
			

		} catch (Exception e) {
			throw e;

		}
	}
	public void setUpCommonPool() throws Exception {

		if (!this.info.isExternal()) {
			
				initPoolDatasource();
			
		} else {

			try {
				// Context ctx = new InitialContext();
				if(this.externalDBName == null)//如果引用的是poolman.xml文件中定义的数据源externalDBName不会为空，externalDBName为空表示表示外部数据源始容器数据源或者外部数据源
				{
					DataSource _datasource = find(this.info.getExternaljndiName(),info);
					
					if (_datasource != null) {
						
						if (this.info.getJNDIName() != null
								&& !this.info.getJNDIName().equals("")) {
							this.datasource = new PoolManDataSource(_datasource,
									info.getDbname(), info.getJNDIName());
						} else {
							this.datasource = _datasource;
						}
						
						if(this.info.isEnablejta())
						{
							if(!(_datasource instanceof TXDataSource))
							{
//								if(_datasource instanceof PoolManDataSource)
//								{
//									
//								}
//								else
								{
									this.datasource = new TXDataSource(datasource,this);
								}
							}
						}
					}
					
				}
			} catch (NamingException e) {
				log.info("通过JNDI名称[" + info.getExternaljndiName()
						+ "]获取外部数据源失败:"+e.getMessage());
				// e.printStackTrace();
			}

		}

		

	}
	
	public DataSource find_(String jndiName) throws NamingException
	{
		
		if(ctx == null && this.dummyctx == null)
			return null;
		DataSource _datasource = ctx != null ?(DataSource) ctx.lookup(jndiName):null;
		if(_datasource != null)
			return _datasource;
		if(dummyctx != null)
			_datasource = (DataSource) dummyctx.lookup(jndiName);
		return _datasource;
	}

	public static DataSource find(String jndiName,JDBCPoolMetaData meta) throws NamingException {
		
			// Context ctx = new InitialContext();
			Map pools = SQLManager.getInstance().getPools();
			if(pools != null && pools.size() > 0)
			{
				Iterator its = pools.entrySet().iterator();
				String name = ContextUtil.handleJndiName(jndiName);
				Context ctx = null;
				while(its.hasNext())
				{
					try
					{
						Map.Entry ent = (Map.Entry)its.next();
						JDBCPool pool = (JDBCPool)ent.getValue();
						
						DataSource _datasource = (DataSource) pool.find_(name);
						if(_datasource != null)
							return _datasource;
					} catch (NamingException e) {
	
						continue;
						// e.printStackTrace();
					}
				}
				return null;
			}
			else
			{
				try {
					Context ctx = JDBCPool.buildContext(meta);
					DataSource _datasource = (DataSource) ctx.lookup(ContextUtil.handleJndiName(jndiName));
					return _datasource;
				} catch (NamingException e) {

					throw e;
					// e.printStackTrace();
				}
			}
		
	}
	
	
	public static DataSource find(String jndiName) throws NamingException {
		
		// Context ctx = new InitialContext();
		Map pools = SQLManager.getInstance().getPools();
		if(pools != null && pools.size() > 0)
		{
			Iterator its = pools.entrySet().iterator();
			String name = ContextUtil.handleJndiName(jndiName);
			Context ctx = null;
			while(its.hasNext())
			{
				try
				{
					Map.Entry ent = (Map.Entry)its.next();
					JDBCPool pool = (JDBCPool)ent.getValue();
					ctx = pool.ctx;
					if(ctx == null)
						continue;
					DataSource _datasource = (DataSource) ctx.lookup(name);
					if(_datasource != null)
						return _datasource;
				} catch (NamingException e) {

					continue;
					// e.printStackTrace();
				}
			}
			return null;
		}
		else
		{
			try {
				Context ctx = JDBCPool.buildContext(null);
				DataSource _datasource = (DataSource) ctx.lookup(ContextUtil.handleJndiName(jndiName));
				return _datasource;
			} catch (NamingException e) {

				throw e;
				// e.printStackTrace();
			}
		}
	
}

	public JDBCPool(JDBCPoolMetaData metad) {
		initctx(metad);
		
		this.deployedDataSource = false;
		this.info = metad;
		if(StringUtil.isNotEmpty(info.getIdGenerator()))
		{
			try
			{
				this.idGenerator = (IdGenerator)Class.forName(info.getIdGenerator()).newInstance();
			}
			catch(Exception e)
			{
				log.info("初始化主键生成个器失败："+info.getIdGenerator()+",使用默认com.frameworkset.common.poolman.sql.StrongUuidGenerator.");
				idGenerator = new com.frameworkset.common.poolman.sql.StrongUuidGenerator();
			}
		}
		else
		{
			idGenerator = new com.frameworkset.common.poolman.sql.StrongUuidGenerator();
		}
		this.preparedStatementPool = new Hashtable();
		if (null == info.getJNDIName() || info.getJNDIName().equals(""))
			log
					.debug("JDBCPool["
							+ info.getDbname()
							+ "]: No JNDI name specified, The DataSource will not be binded to NamingContext.");
		else
			log.debug("JDBCPool[" + info.getDbname() + "]:INNER JNDI Name is "
					+ info.getJNDIName());
		if(this.info.getInterceptor() != null && !this.info.getInterceptor().equals("") &&  !this.info.getInterceptor().equals(defaultInterceptor_s))
		{
			try {
				this.interceptor = (InterceptorInf)Class.forName(this.info.getInterceptor()).newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		startPool();

	}

	public String statusChecker() {
//		if(this.externalDBName == null)
			return status;
//		return SQLManager.getInstance().getPool(externalDBName).statusChecker();
		
	}

	
	public void startPool() {
		if (status.equals("start"))
			return;
		if (!status.equals("stop") && !status.equals("unknown") && !status.equals("failed"))
			return;
		initDBAdapter();
		
//		if(!this.isExternal())
		{
			if(this.datasource != null)
			{
				try {

					DatasourceUtil.closeDS(datasource);
				} catch (Exception e) {
					
				}
				datasource = null;
			}
		}

		

		try {

			this.setUpCommonPool();
			this.startTime = System.currentTimeMillis();
			this.status = "start";
			init();
			this.initDBProductInfo();
			log.debug("Load Database Meta Data=" + info.getLoadmetadata());
			if (info.getLoadmetadata() != null
					&& info.getLoadmetadata().equalsIgnoreCase("true") && !inited ) {
				log.debug("Load Database[" + info.getName()
						+ "] Meta Data beginning.....");
				if(this.externalDBName == null)
				{
					
					try {
						
						initDatabaseMetaData(null);
					} finally{
						inited = true;
					}
					
				}
				else
				{
					inited = true;
				}
				log.debug("Load Database[" + info.getName()
						+ "] Meta Data success.");
			} else {
				log.debug("Ignore Load Database[" + info.getName()
						+ "] Meta Data .");
			}
		} catch (Exception e) {
			this.status = "failed";
			log.error("initializing JDBCPool[" + info.getName()
						+ "] failed:", e);
		}

	}
	
	private void initDBProductInfo()
	{
		
            Connection conn = null;
            try {
            	conn = requestConnection();;
                DatabaseMetaData md = conn.getMetaData();

                

                this.info.setDatabaseProductName(md.getDatabaseProductName());
                this.info.setDatabaseProductVersion(md.getDatabaseProductVersion());
                this.info.setDriverName(md.getDriverName());
                this.info.setDriverVersion(md.getDriverVersion());
                
                md = null;
                
            }catch(Exception e)
            {
            	
            }
            finally {
            	if(conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
            }
	}

	/**
	 * 如果数据库表的元数据发生了变化，则重新装载这些数据
	 * 
	 * @param tableName
	 */
	public void updateTableMetaData(String tableName) {
		if(this.externalDBName == null)
		{
			Connection con = null;
	
			ResultSet rs = null;
			ResultSet columnrs = null;
			ResultSet primaryKeysrs = null;
			DatabaseMetaData metaData = null;
			ResultSet foreignrs = null;
	
			try {
				con = requestConnection();
	
				metaData = con.getMetaData();
//				String schemaName = this.getDbAdapter().getSchema(info);
//				if(schemaName == null)
//					schemaName = metaData.getUserName();
				String schemaName = getSchemaName_( metaData,this.getDbAdapter().getSchema(info));
				TableMetaData tableMetaData = null;
				// rs =
				// metaData.getTables(null,"sysmanager".toUpperCase(),"td_sm_job".toUpperCase(),new
				// String[] {"TABLE"});
				rs = metaData.getTables(null, schemaName, tableName
						.toUpperCase(), new String[] { "TABLE", "VIEW" });
				while (rs.next()) {
					tableName = rs.getString("TABLE_NAME");
	
					// System.out.println();
					// System.out.println("TABLE_SCHEM:"+rs.getString("TABLE_SCHEM"));
					// System.out.println("tableName:"+tableName);
					String tableType = rs.getString("TABLE_TYPE");
	
					tableMetaData = new TableMetaData();
					tableMetaData.setTableName(tableName);
					tableMetaData.setTableType(tableType);
					try {
						String tableRemark = rs.getString("REMARKS");
						tableRemark = this.getDbAdapter().getTableRemarks(con, tableName,
								tableRemark);
						tableMetaData.setRemarks(tableRemark);
					} catch (Exception e) {
	
					}
	
					// columnrs =
					// metaData.getColumns(null,"sysmanager".toUpperCase(),tableName,"%");
					columnrs = metaData.getColumns(null, schemaName,
							tableName, "%");
					while (columnrs.next()) {
						ColumnMetaData column = new ColumnMetaData(this.getDbAdapter());
						try {
							column.setColumnName(columnrs.getString("COLUMN_NAME"));
						} catch (Exception e) {
	
						}
						try {
							column.setColunmSize(columnrs.getInt("COLUMN_SIZE"));
						} catch (Exception e) {
	
						}
						try {
							column.setTypeName(columnrs.getString("TYPE_NAME"));
						} catch (Exception e) {
	
						}
						try {
							column.setIsNullable(columnrs.getString("IS_NULLABLE"));
						} catch (Exception e) {
	
						}
						try {
							column.setDECIMAL_DIGITS(columnrs
									.getInt("DECIMAL_DIGITS"));
						} catch (Exception e) {
	
						}
						try {
							column.setColumnDefaultValue(columnrs
									.getString("COLUMN_DEF"));
						} catch (Exception e) {
	
						}
						try {
							column.setNumPrecRadix(columnrs
									.getInt("NUM_PREC_RADIX"));
	
						} catch (Exception e) {
	
						}
						try {
							column.setDataType(columnrs.getInt("DATA_TYPE"),column.getTypeName());
						} catch (Exception e) {
	
						}
						try {
							column.setCHAR_OCTET_LENGTH(columnrs
									.getInt("CHAR_OCTET_LENGTH"));
						} catch (Exception e) {
	
						}
	
						try {
							String remarks_c = columnrs.getString("REMARKS");
							remarks_c = this.getDbAdapter().getColumnRemarks(con, tableName,
									column.getColumnName(), remarks_c);
	
							column.setRemarks(remarks_c);
						} catch (Exception e) {
	
						}
	
						tableMetaData.addColumns(column);
					}
					columnrs.close();
	
					/**
					 * 构建主键信息
					 */
					primaryKeysrs = metaData.getPrimaryKeys(null, schemaName, tableName);
					while (primaryKeysrs.next()) {
						PrimaryKeyMetaData primaryKeyMetaData = new PrimaryKeyMetaData(
								this.getDbAdapter());
						try {
							primaryKeyMetaData.setColumnName(primaryKeysrs
									.getString("COLUMN_NAME"));
						} catch (Exception e) {
	
						}
						try {
							primaryKeyMetaData.setPkName(primaryKeysrs
									.getString("PK_NAME"));
						} catch (Exception e) {
	
						}
						try {
							primaryKeyMetaData.setKeySEQ(primaryKeysrs
									.getInt("KEY_SEQ"));
						} catch (Exception e) {
	
						}
						// try
						// {
						// primaryKeyMetaData.setDataType(primaryKeysrs.getInt("DATA_TYPE"));
						// }
						// catch(Exception e)
						// {
						//						
						// }
						primaryKeyMetaData.setColumn(tableMetaData
								.getColumnMetaData(primaryKeyMetaData
										.getColumnName().toLowerCase()));
						tableMetaData.addPrimaryKey(primaryKeyMetaData);
	
					}
					primaryKeysrs.close();
					/**
					 * 构建外键信息
					 */
					foreignrs = metaData.getImportedKeys(null, schemaName, tableName);
					while (foreignrs.next()) {
						ForeignKeyMetaData foreignKeyMetaData = new ForeignKeyMetaData(
								this.getDbAdapter());
						try {
							foreignKeyMetaData.setPKTABLE_NAME(foreignrs
									.getString("PKTABLE_NAME"));
						} catch (Exception e) {
	
						}
						try {
							foreignKeyMetaData.setPKCOLUMN_NAME(foreignrs
									.getString("PKCOLUMN_NAME"));
						} catch (Exception e) {
	
						}
						try {
							foreignKeyMetaData.setFKTABLE_NAME(foreignrs
									.getString("FKTABLE_NAME"));
						} catch (Exception e) {
	
						}
						try {
							foreignKeyMetaData.setFKCOLUMN_NAME(foreignrs
									.getString("FKCOLUMN_NAME"));
						} catch (Exception e) {
	
						}
						try {
							foreignKeyMetaData.setColumnName(foreignrs
									.getString("FKCOLUMN_NAME"));
						} catch (Exception e) {
	
						}
						try {
							foreignKeyMetaData.setKEY_SEQ(foreignrs
									.getString("KEY_SEQ"));
						} catch (Exception e) {
	
						}
						try {
							foreignKeyMetaData.setUPDATE_RULE(foreignrs
									.getInt("UPDATE_RULE"));
						} catch (Exception e) {
	
						}
						try {
							foreignKeyMetaData.setDELETE_RULE(foreignrs
									.getInt("DELETE_RULE"));
						} catch (Exception e) {
	
						}
						try {
							foreignKeyMetaData.setFK_NAME(foreignrs
									.getString("FK_NAME"));
						} catch (Exception e) {
	
						}
						try {
							foreignKeyMetaData.setPK_NAME(foreignrs
									.getString("PK_NAME"));
						} catch (Exception e) {
	
						}
						try {
							foreignKeyMetaData.setDEFERRABILITY(foreignrs
									.getString("DEFERRABILITY"));
						} catch (Exception e) {
	
						}
						// try
						// {
						// foreignKeyMetaData.setDataType(foreignrs.getInt("DATA_TYPE"));
						// }
						// catch(Exception e)
						// {
						//						
						// }
						foreignKeyMetaData.setColumn(tableMetaData
								.getColumnMetaData(foreignKeyMetaData
										.getColumnName().toLowerCase()));
						tableMetaData.addForeignKey(foreignKeyMetaData);
	
					}
	
					foreignrs.close();
					//
					// ResultSet rs4 =
					// metaData.getExportedKeys(null,null,tableName);
	
					// this.tableMetaData.put(rsMeta.)
					this.tableMetaDatasindexByTablename.put(
							tableName.toLowerCase(), tableMetaData);
					tableMetaDatas.add(tableMetaData);
				}
				// System.out.println("this.tableMetaDatasindexByTablename.put(tableName.toLowerCase(),tableMetaData):"
				// + tableName + "=" + tableMetaData);
				rs.close();
				// metaData.
			} catch (SQLException e) {
				// TODO Auto-generated catch block
	
			} catch (Exception e) {
				// e.printStackTrace();
			}
	
			finally {
				try {
					if (rs != null)
						rs.close();
				} catch (Exception e) {
	
				}
				try {
					if (columnrs != null)
						columnrs.close();
				} catch (Exception e) {
	
				}
				try {
					if (primaryKeysrs != null)
						primaryKeysrs.close();
				} catch (Exception e) {
	
				}
				try {
					if (foreignrs != null)
						foreignrs.close();
				} catch (Exception e) {
	
				}
				try {
					if (con != null)
						con.close();
				} catch (Exception e) {
	
				}
	
			}
		}
		else
		{
			SQLManager.getInstance().getPool(externalDBName).updateTableMetaData(tableName);
		}
	}
	public String getDatabaseSchema(DatabaseMetaData databaseMetaData) throws Throwable
	{
		return getSchemaName_( databaseMetaData,this.getDbAdapter().getSchema(info));
	}
	
	public void refreshDatabaseMetaData()
	{
		ResultSet rs = null;
		
		DatabaseMetaData metaData = null;
		
		Connection con = null;
		try {
			tableMetaDatasindexByTablename.clear();
			tableMetaDatas.clear();
			con = requestConnection();

			metaData = con.getMetaData();
			String schemaName = getSchemaName_( metaData,this.getDbAdapter().getSchema(info));
//			String schemaName = this.getDbAdapter().getSchema(info);
//			if(schemaName == null)
//				schemaName = metaData.getUserName();
			// rs =
			// metaData.getTables(null,"sysmanager".toUpperCase(),"td_sm_job".toUpperCase(),new
			// String[] {"TABLE"});
			rs = metaData.getTables(null, schemaName, "%",
					new String[] { "TABLE", "VIEW" });
			while (rs.next()) {
				String tableName = rs.getString("TABLE_NAME");
				if (tableName.startsWith("BIN$"))
					continue;
				if (tableMetaDatasindexByTablename.containsKey(tableName
						.toLowerCase())) {
					System.out.println("table [" + tableName + "] 已经加载，忽略！");
					continue;
				}
				log.debug("load table[" + tableName + "]'s metadata.");
				// System.out.println();
				// System.out.println("TABLE_SCHEM:"+rs.getString("TABLE_SCHEM"));
				// System.out.println("tableName:"+tableName);
				TableMetaData tableMetaData = buildTableMetaData(con,rs,metaData ,true);
				if(tableMetaData == null)
					continue;
				//
				// ResultSet rs4 =
				// metaData.getExportedKeys(null,null,tableName);

				// this.tableMetaData.put(rsMeta.)
				this.tableMetaDatasindexByTablename.put(
						tableName.toLowerCase(), tableMetaData);
				tableMetaDatas.add(tableMetaData);
			}
			rs.close();

			// metaData.
		} catch (SQLException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {

			}
			
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {

			}

		}
	}
	/**
	 * 初始化数据库元数据
	 * 
	 */
	private void initDatabaseMetaData(Connection con) {
		

		ResultSet rs = null;
		
		DatabaseMetaData metaData = null;
		

		try {
			if(con == null)
				con = requestConnection();

			metaData = con.getMetaData();
			String schemaName = getSchemaName_( metaData,this.getDbAdapter().getSchema(info));
//			String schemaName = this.getDbAdapter().getSchema(info);
//			if(schemaName == null)
//				schemaName = metaData.getUserName();
			// rs =
			// metaData.getTables(null,"sysmanager".toUpperCase(),"td_sm_job".toUpperCase(),new
			// String[] {"TABLE"});
			rs = metaData.getTables(null, schemaName, "%",
					new String[] { "TABLE", "VIEW" });
			while (rs.next()) {
				String tableName = rs.getString("TABLE_NAME");
				if (tableName.startsWith("BIN$"))
					continue;
				if (tableMetaDatasindexByTablename.containsKey(tableName
						.toLowerCase())) {
					System.out.println("table [" + tableName + "] 已经加载，忽略！");
					continue;
				}
				log.debug("load table[" + tableName + "]'s metadata.");
				// System.out.println();
				// System.out.println("TABLE_SCHEM:"+rs.getString("TABLE_SCHEM"));
				// System.out.println("tableName:"+tableName);
				TableMetaData tableMetaData = buildTableMetaData(con,rs,metaData ,true);
				if(tableMetaData == null)
					continue;
				//
				// ResultSet rs4 =
				// metaData.getExportedKeys(null,null,tableName);

				// this.tableMetaData.put(rsMeta.)
				this.tableMetaDatasindexByTablename.put(
						tableName.toLowerCase(), tableMetaData);
				tableMetaDatas.add(tableMetaData);
			}
			rs.close();

			// metaData.
		} catch (SQLException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {

			}
			
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {

			}

		}

	}

	/**
	 * 初始化数据库元数据 added by biaoping.yin on 20080529
	 * 获取到的表元数据将被直接刷新到缓冲区中
	 */
	public TableMetaData getTableMetaDataFromDatabase(
			String tableName)
	{
		return getTableMetaDataFromDatabase(null,
				tableName);
	}
	
	
	
	private String[] convertTableTypes(String tableTypes[])
	{
		if(tableTypes == null || tableTypes.length == 0)
			return new String[] { "TABLE", "VIEW" };
		
		for(int i = 0; i < tableTypes.length; i ++)
		{
			if(tableTypes[i].equals(JDBCPool.TABLE_TYPE_ALL))
				return new String[] { "TABLE", "VIEW" };
			
			
		}
		
		return tableTypes;
	}
	/**
	 * 获取表集合信息
	 */
	public List<TableMetaData> getTablesFromDatabase(String tableNamePattern)
	{
		return getTablesFromDatabase(null,tableNamePattern);
	}
	
	/**
	 * 获取表集合信息
	 */
	public List<TableMetaData> getTablesFromDatabase(String tableNamePattern,String tableTypes[])
	{
		return getTablesFromDatabase(null,tableNamePattern,tableTypes);
	}
	
	public List<TableMetaData> getTablesFromDatabase(Connection con) {
		return getTablesFromDatabase(con,(String)null);
	}
	
	public List<TableMetaData> getTablesFromDatabase(Connection con,String tabletypes[]) {
		return getTablesFromDatabase(con,(String)null, tabletypes);
	}
	
	
	public List<TableMetaData> getTablesFromDatabase(Connection con,String tableNamePattern)
	{
		return getTablesFromDatabase(con,tableNamePattern,null);
	}
	/**
	 * 获取表集合信息
	 * @param con
	 * @param tableName
	 * @return
	 */
	public List<TableMetaData> getTablesFromDatabase(Connection con,String tableNamePattern,String tableTypes[]) {
		return getTablesFromDatabase(con,tableNamePattern,tableTypes,false);
	}
	/**
	 * 获取表集合信息
	 */
	public List<TableMetaData> getTablesFromDatabase()
	{
		return getTablesFromDatabase((Connection)null);
	}
	
	/**
	 * 获取表集合信息
	 */
	public List<TableMetaData> getTablesFromDatabase(String tabletypes[])
	{
		return getTablesFromDatabase((Connection)null,tabletypes);
	}
	
	
	
	/**
	 * 获取表集合信息
	 */
	public List<TableMetaData> getTablesFromDatabase(String tableNamePattern,boolean loadColumns)
	{
		return getTablesFromDatabase(null,tableNamePattern,loadColumns);
	}
	
	/**
	 * 获取表集合信息
	 */
	public List<TableMetaData> getTablesFromDatabase(String tableNamePattern,String tableTypes[],boolean loadColumns)
	{
		return getTablesFromDatabase(null,tableNamePattern,tableTypes,loadColumns);
	}
	
	public List<TableMetaData> getTablesFromDatabase(Connection con,boolean loadColumns) {
		return getTablesFromDatabase(con,(String)null,loadColumns);
	}
	
	public List<TableMetaData> getTablesFromDatabase(Connection con,String tabletypes[],boolean loadColumns) {
		return getTablesFromDatabase(con,(String)null, tabletypes,loadColumns);
	}
	
	
	public List<TableMetaData> getTablesFromDatabase(Connection con,String tableNamePattern,boolean loadColumns)
	{
		return getTablesFromDatabase(con,tableNamePattern,null,loadColumns);
	}
	/**
	 * 获取表集合信息
	 * @param con
	 * @param tableName
	 * @return
	 */
	public List<TableMetaData> getTablesFromDatabase(Connection con,String tableNamePattern,String tableTypes[],boolean loadColumns) {
		List<TableMetaData> tableMetaDatas = new ArrayList<TableMetaData>();
		ResultSet rs = null;
		DatabaseMetaData metaData = null;
		boolean outcon = true;
		
		JDBCTransaction tx = null;
		try {
			if (con == null) {
				tx = TransactionManager.getTransaction();
				if (tx == null)
					con = requestConnection();
				else
					con = tx.getConnection(this.getDBName());
				outcon = false;
			}

			metaData = con.getMetaData();
			String schemaName = getSchemaName_( metaData,this.getDbAdapter().getSchema(info));
//			String schemaName = this.getDbAdapter().getSchema(info);
//			if(schemaName == null)
//				schemaName = metaData.getUserName();

			// rs =
			// metaData.getTables(null,"sysmanager".toUpperCase(),"td_sm_job".toUpperCase(),new
			// String[] {"TABLE"});
			String temp = this.getDbAdapter()
			.getSchemaTableTableName(info, tableNamePattern);
			rs = metaData.getTables(null, schemaName, temp == null?"%":temp,
					this.convertTableTypes(tableTypes)
					);
			while (rs.next()) {
				TableMetaData tableMetaData = buildTableMetaData(con,rs,metaData ,loadColumns);
				if(tableMetaData == null)
					continue;
				//
				// ResultSet rs4 =
				// metaData.getExportedKeys(null,null,tableName);

				// this.tableMetaData.put(rsMeta.)
				
				tableMetaDatas.add(tableMetaData);
			}
			rs.close();

			// metaData.
		} catch (SQLException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {

			}
			
			try {
				if (con != null) {
					if (!outcon && tx == null)
						con.close();
				}
			} catch (Exception e) {

			}
			con = null;

		}
		return tableMetaDatas;
	}
	/**
	 * 获取表集合信息
	 */
	public List<TableMetaData> getTablesFromDatabase(boolean loadColumns)
	{
		return getTablesFromDatabase((Connection)null, loadColumns);
	}
	
	/**
	 * 获取表集合信息
	 */
	public List<TableMetaData> getTablesFromDatabase(String tabletypes[],boolean loadColumns)
	{
		return getTablesFromDatabase((Connection)null,tabletypes,loadColumns);
	}
	
	
	
	

	private TableMetaData buildTableMetaData(Connection con,ResultSet rs,DatabaseMetaData metaData ,boolean loadColumns) throws SQLException
	{
		ResultSet columnrs = null;
		ResultSet primaryKeysrs = null;
		
		ResultSet foreignrs = null;
		String tableName = rs.getString("TABLE_NAME");
		if (tableName.startsWith("BIN$"))
			return null;
		log.debug("load table[" + tableName + "]'s metadata.");

		String tableType = rs.getString("TABLE_TYPE");

		TableMetaData tableMetaData = new TableMetaData();
		tableMetaData.setTableName(tableName);
		tableMetaData.setTableType(tableType);
		try {
			String tableRemark = rs.getString("REMARKS");

			tableRemark = this.getDbAdapter().getTableRemarks(con, tableName,
					tableRemark);
			tableMetaData.setRemarks(tableRemark);
		} catch (Exception e) {

		}
		String schemaName = getSchemaName_( metaData,this.getDbAdapter().getSchema(info));
//		String schemaName = this.getDbAdapter().getSchema(info);
//		if(schemaName == null)
//			schemaName = metaData.getUserName();
		if(loadColumns)
		{
			
			try {
				
				columnrs = metaData.getColumns(null, schemaName, tableName, "%");
	
				while (columnrs.next()) {
					ColumnMetaData column = new ColumnMetaData(
							this.getDbAdapter());
					try {
						column.setColumnName(columnrs
								.getString("COLUMN_NAME"));
					} catch (Exception e) {
	
					}
					try {
						column
								.setColunmSize(columnrs
										.getInt("COLUMN_SIZE"));
					} catch (Exception e) {
	
					}
					try {
						column.setTypeName(columnrs.getString("TYPE_NAME"));
					} catch (Exception e) {
	
					}
					try {
						column.setIsNullable(columnrs
								.getString("IS_NULLABLE"));
					} catch (Exception e) {
	
					}
					try {
						column.setColumnDefaultValue(columnrs
								.getString("COLUMN_DEF"));
					} catch (Exception e) {
	
					}
					try {
						column.setNumPrecRadix(columnrs
								.getInt("NUM_PREC_RADIX"));
	
					} catch (Exception e) {
	
					}
					try {
						column.setDataType(columnrs.getInt("DATA_TYPE"),column.getTypeName());
					} catch (Exception e) {
	
					}
					try {
						column.setDECIMAL_DIGITS(columnrs
								.getInt("DECIMAL_DIGITS"));
					} catch (Exception e) {
	
					}
					try {
						column.setNumPrecRadix(columnrs
								.getInt("NUM_PREC_RADIX"));
	
					} catch (Exception e) {
	
					}
	
					try {
						String remarks_c = columnrs.getString("REMARKS");
						remarks_c = this.getDbAdapter().getColumnRemarks(con,
								tableName, column.getColumnName(),
								remarks_c);
	
						column.setRemarks(remarks_c);
	
					} catch (Exception e) {
	
					}
	
					tableMetaData.addColumns(column);
				}
				columnrs.close();
			} catch (Exception e) {
				columnrs.close();
				columnrs = null;
				e.printStackTrace();
			}
	
			/**
			 * 构建主键信息
			 */
			try {
				primaryKeysrs = metaData.getPrimaryKeys(null, schemaName, tableName);
	
				while (primaryKeysrs.next()) {
					PrimaryKeyMetaData primaryKeyMetaData = new PrimaryKeyMetaData(
							this.getDbAdapter());
					try {
						primaryKeyMetaData.setColumnName(primaryKeysrs
								.getString("COLUMN_NAME"));
					} catch (Exception e) {
	
					}
					try {
						primaryKeyMetaData.setPkName(primaryKeysrs
								.getString("PK_NAME"));
					} catch (Exception e) {
	
					}
					try {
						primaryKeyMetaData.setKeySEQ(primaryKeysrs
								.getInt("KEY_SEQ"));
					} catch (Exception e) {
	
					}
					// try
					// {
					// primaryKeyMetaData.setDataType(primaryKeysrs.getInt("DATA_TYPE"));
					// }
					// catch(Exception e)
					// {
					//						
					// }
					primaryKeyMetaData.setColumn(tableMetaData
							.getColumnMetaData(primaryKeyMetaData
									.getColumnName().toLowerCase()));
					tableMetaData.addPrimaryKey(primaryKeyMetaData);
	
				}
				primaryKeysrs.close();
				primaryKeysrs = null;
			} catch (Exception e) {
				primaryKeysrs.close();
				primaryKeysrs = null;
				e.printStackTrace();
			}
	
			try {
				/**
				 * 构建外键信息
				 */
				foreignrs = metaData.getImportedKeys(null, schemaName, tableName);
				while (foreignrs.next()) {
					ForeignKeyMetaData foreignKeyMetaData = new ForeignKeyMetaData(
							this.getDbAdapter());
					try {
						foreignKeyMetaData.setPKTABLE_NAME(foreignrs
								.getString("PKTABLE_NAME"));
					} catch (Exception e) {
	
					}
					try {
						foreignKeyMetaData.setPKCOLUMN_NAME(foreignrs
								.getString("PKCOLUMN_NAME"));
					} catch (Exception e) {
	
					}
					try {
						foreignKeyMetaData.setFKTABLE_NAME(foreignrs
								.getString("FKTABLE_NAME"));
					} catch (Exception e) {
	
					}
					try {
						foreignKeyMetaData.setFKCOLUMN_NAME(foreignrs
								.getString("FKCOLUMN_NAME"));
					} catch (Exception e) {
	
					}
					try {
						foreignKeyMetaData.setColumnName(foreignrs
								.getString("FKCOLUMN_NAME"));
					} catch (Exception e) {
	
					}
					try {
						foreignKeyMetaData.setKEY_SEQ(foreignrs
								.getString("KEY_SEQ"));
					} catch (Exception e) {
	
					}
					try {
						foreignKeyMetaData.setUPDATE_RULE(foreignrs
								.getInt("UPDATE_RULE"));
					} catch (Exception e) {
	
					}
					try {
						foreignKeyMetaData.setDELETE_RULE(foreignrs
								.getInt("DELETE_RULE"));
					} catch (Exception e) {
	
					}
					try {
						foreignKeyMetaData.setFK_NAME(foreignrs
								.getString("FK_NAME"));
					} catch (Exception e) {
	
					}
					try {
						foreignKeyMetaData.setPK_NAME(foreignrs
								.getString("PK_NAME"));
					} catch (Exception e) {
	
					}
					try {
						foreignKeyMetaData.setDEFERRABILITY(foreignrs
								.getString("DEFERRABILITY"));
					} catch (Exception e) {
	
					}
	
					foreignKeyMetaData.setColumn(tableMetaData
							.getColumnMetaData(foreignKeyMetaData
									.getColumnName().toLowerCase()));
					tableMetaData.addForeignKey(foreignKeyMetaData);
	
				}
				foreignrs.close();
			} catch (Exception e) {
	
				foreignrs.close();
				foreignrs = null;
				e.printStackTrace();
			}
		}
		return tableMetaData;
	}
	
	private String getSchemaName_(DatabaseMetaData metaData,String schemaName) throws SQLException
	{
		if(schemaName == null)
			schemaName = metaData.getUserName();
		else if(schemaName == DB.NULL_SCHEMA)
			schemaName = null;
		return schemaName;
	}
	/**
	 * 初始化数据库元数据 added by biaoping.yin on 20080529
	 * 获取到的表元数据将被直接刷新到缓冲区中
	 */
	public TableMetaData getTableMetaDataFromDatabase(Connection con,
			String tableName) {

		
		if(this.externalDBName == null)
		{
			
			ResultSet rs = null;
			
			DatabaseMetaData metaData = null;
			
			TableMetaData tableMetaData = null;
			boolean outcon = true;
	
			JDBCTransaction tx = null;
			try {
				if (con == null) {
					tx = TransactionManager.getTransaction();
					if (tx == null)
						con = requestConnection();
					else
						con = tx.getConnection(this.getDBName());
					outcon = false;
				}
	
				metaData = con.getMetaData();
				String schemaName = getSchemaName_( metaData,this.getDbAdapter().getSchema(info));
//				String schemaName = this.getDbAdapter().getSchema(info);
//				if(schemaName == null)
//					schemaName = metaData.getUserName();
				// rs =
				// metaData.getTables(null,"sysmanager".toUpperCase(),"td_sm_job".toUpperCase(),new
				// String[] {"TABLE"});
				rs = metaData.getTables(null, schemaName, this.getDbAdapter()
						.getSchemaTableTableName(info, tableName), new String[] {
						"TABLE", "VIEW" });
				while (rs.next()) {
					tableMetaData = buildTableMetaData(con,rs,metaData ,true);
					if(tableMetaData == null)
						continue;
					//
					// ResultSet rs4 =
					// metaData.getExportedKeys(null,null,tableName);
	
					// this.tableMetaData.put(rsMeta.)
					this.tableMetaDatasindexByTablename.put(
							tableMetaData.getTableName().toLowerCase(), tableMetaData);
					tableMetaDatas.add(tableMetaData);
	
				}
				rs.close();
	
				// metaData.
			} catch (SQLException e) {
				e.printStackTrace();
				// TODO Auto-generated catch block
	
			} catch (Exception e) {
				e.printStackTrace();
			}
	
			finally {
				try {
					if (rs != null)
						rs.close();
				} catch (Exception e) {
	
				}
				
				try {
					if (con != null) {
						if (!outcon && tx == null)
							con.close();
					}
				} catch (Exception e) {
	
				}
				con = null;
	
			}
			return tableMetaData;
		}
		else
		{
			return SQLManager.getInstance().getPool(externalDBName).getTableMetaDataFromDatabase(con, tableName);
		}

	}

	public void init() throws Exception {


		deployDataSource();

	}

	/** Associates a DataSource view with this pool. */
	public void setDataSource(PoolManDataSource ds) {
		this.datasource = ds;
	}

	public DataSource getDataSource() {
		if (this.datasource == null) {
			log.info(info.getDbname() + " has no associated DataSource.");
			throw new NullPointerException(info.getDbname()
					+ " has no associated DataSource.");
		}
		return this.datasource;
	}

	public void deployDataSource() {

		if (this.datasource == null || this.info.getJNDIName() == null
				|| this.info.getJNDIName().equals(""))
			return;

		try {
			log.debug("Bind the DataSource view of this pool to JNDI");
			String jndiName = this.info.getJNDIName();
			
			
			if (!exist(ctx, jndiName,info)) {
				bind(ctx, jndiName, this.datasource,info);
			}

			this.deployedDataSource = true;

			log.info("DataSource bound to JNDI under name[" + jndiName + "]");
			

		} catch (Exception e) {
			
			log.info(e.getMessage());
		}
	}

	/**
	 * Bind val to name in ctx, and make sure that all intermediate contexts
	 * exist.
	 * 
	 * @param ctx
	 *            the root context
	 * @param name
	 *            the name as a string
	 * @param val
	 *            the object to be bound
	 * @throws NamingException
	 */
	public void bind(Context ctx, String name, Object val,JDBCPoolMetaData meta)
			throws NamingException {
		try {
			initctx(meta);			
			ctx.rebind(name, val);
			log.debug("binding datasource  to container context  with: " + name);
			
		} catch (Exception e) {
			Name n = ctx.getNameParser("").parse(name);
			while (n.size() > 1) {
				String ctxName = n.get(0);

				Context subctx = null;
				try {
					log.debug("lookup: " + ctxName);
					subctx = (Context) ctx.lookup(ctxName);
				} catch (NameNotFoundException nfe) {
				}

				if (subctx != null) {
					log.debug("Found subcontext: " + ctxName);
					ctx = subctx;
				} else {
					log.info("Creating subcontext: " + ctxName);
					ctx = ctx.createSubcontext(ctxName);
				}
				n = n.getSuffix(1);
			}
			log.debug("binding: " + n);
			ctx.rebind(n, val);
		}
		finally
		{
			this.dummyctx.rebind(name, val);
			log.debug("Bound name " + name + " to dummy context.");
		}
		
	}

	/**
	 * 判断jndiName是否存在
	 * 
	 * @param ctx
	 * @param jndiName
	 * @return
	 */
	private boolean exist(Context ctx, String jndiName,JDBCPoolMetaData meta) {
		try {
			initctx(meta);
			ctx = this.ctx;
			DataSource datasource = (DataSource) find_(ContextUtil.handleJndiName(jndiName));
			if (datasource == null) {

				return false;
			} else {
				System.out.println("DataSource bound to JNDI under name["
						+ jndiName + "]");
				return true;
			}
		} catch (Exception e) {
			// e.printStackTrace();
			return false;
		}
	}

	public void undeployDataSource() throws Exception {

		if (this.datasource == null || this.info.getJNDIName() == null
				|| this.info.getJNDIName().equals(""))
			return;

		try {
			try {

				DatasourceUtil.closeDS(datasource);
			} catch (Exception e) {
				throw e;
			}
			try {
				
				if(ctx != null)
					ctx.unbind(this.info.getJNDIName());
			} catch (Exception e) {

			}
			this.deployedDataSource = false;
			
		} catch (Exception e) {
			throw e;
		}
	}

	public boolean isDataSourceDeployed() {
		return this.deployedDataSource;
	}


	/**
	 * Determine whether or not this pool is using a SQLCache (configured in
	 * poolman.xml and disabled by default).
	 */
	public boolean usingCache() {
		// if (this.sqlcache == null)
		// return false;
		return true;
	}

	/** Force the cache to refresh. */
	public void refreshCache() {

	}

	public String getDriver() {

		return info.getDriver();
	}

	public String getURL() {
		if(this.externalDBName == null)
			return info.getURL();
		JDBCPoolMetaData meta = SQLManager.getInstance().getJDBCPoolMetaData(externalDBName);
		if(meta != null)
			return meta.getURL();
		return info.getURL();
		
	}

	public String getUserName() {
		if(this.externalDBName == null)
			return info.getUserName();
		JDBCPoolMetaData meta = SQLManager.getInstance().getJDBCPoolMetaData(externalDBName);
		if(meta != null)
			return meta.getUserName();
		return info.getUserName();
		
	}

	public String getPassword() {
		if(this.externalDBName == null)
			return info.getPassword();
		JDBCPoolMetaData meta = SQLManager.getInstance().getJDBCPoolMetaData(externalDBName);
		if(meta != null)
			return meta.getPassword();
		return info.getPassword();
		
	}

	public int getTransactionIsolation() {
		if(this.externalDBName == null)
			return info.getIsolationLevel();
		JDBCPoolMetaData meta = SQLManager.getInstance().getJDBCPoolMetaData(externalDBName);
		if(meta != null)
			return meta.getIsolationLevel();
		return info.getIsolationLevel();
		
	}

	/**
	 * added by biaoping.yin on 20050601 获取数据库主键生成模式
	 * 
	 * @return String
	 */
	public String getKeygenerate() {
		if(this.externalDBName == null)
			return info.getKeygenerate();
		JDBCPoolMetaData meta = SQLManager.getInstance().getJDBCPoolMetaData(externalDBName);
		if(meta != null)
			return meta.getKeygenerate();
		return info.getKeygenerate();
		
	}

	/**
	 * added by biaoping.yin on 20050601 获取数据库的类型
	 * 
	 * @return String
	 */
	public String getDBType() {
//		return info.getDbtype();
		return this.getDbAdapter().getDBTYPE();
	}

	public boolean isUsingNativeResults() {

			return info.isNativeResults();

		
	}

	public void checkCredentials(String username, String password)
			throws SQLException {
		if(this.externalDBName == null)
		{
			if ((this.info.getUserName().equals(username))
					&& (this.info.getPassword().equals(password))) {
				log
						.debug("Invalid Username/Password: " + username + "/"
								+ password);
				throw new SQLException("Invalid Username/Password: " + username
						+ "/" + password);
			}
		}
		else
		{
			SQLManager.getInstance().getPool(externalDBName).checkCredentials(username, password);
		}
	}



	/**
	 * Retrieves a PooledConnection impl and returns its Handle. 
	 * 直接从原始池中获取connection，如果datasource 是一个TXDataSource
	 * 那么需要查找TXDataSource内部包含的原始数据源，然后从中获取对应的connection
	 */
	public Connection requestConnection() throws SQLException {
		if(datasource != null)
		{
			if(!(datasource instanceof TXDataSource))
				return this.datasource.getConnection();
			else
			{
				return ((TXDataSource)this.datasource).getSRCDataSource().getConnection();
			}
		}
		else
		{
			if(this.isExternal() )
			{
				if(this.externalDBName == null)
				{
					throw new NestedSQLException("Request Connection failed:DB Pool[dbname=" + this.info.getDbname() + ",jndiname="+ this.info.getJNDIName() +",extenerjndiname="+ this.info.getExternaljndiName() +"] is stopped or not inited. Please restarted the pool.");
				}
				else
				{
					return SQLManager.getInstance().getPool(externalDBName).requestConnection();
				}
			
			}
			else
			{
				throw new NestedSQLException("Request Connection failed:DB Pool[" + this.info.getDbname() + "] is stopped. Please restarted the pool.");	
			}
		}
	
	}



	/**
	 * 获取当前链接池中正在使用的链接 接口只对内部数据源有用，外部数据源返回-1
	 * 
	 * @return
	 */
	public int getNumActive() {
		if(this.externalDBName == null)
		{

			return DatasourceUtil.getNumActive(datasource);
		}
		else
		{
			return SQLManager.getInstance().getPool(externalDBName).getNumActive();
		}
	}
	
 
	/**
	 * 获取当前链接池中正在使用的链接 接口只对内部数据源有用，外部数据源返回-1
	 * 非连接池数据源为实现该功能
	 * @return
	 */
	public List<AbandonedTraceExt> getGoodTraceObjects() {
		if(this.externalDBName == null)
		{
			return DatasourceUtil.getGoodTraceObjects(datasource);
		}
		else
		{
			return SQLManager.getInstance().getPool(externalDBName).getGoodTraceObjects();
		}
	}
	

	private DataSource getSRCDataSource(TXDataSource ds)
	{
		return ds.getSRCDataSource();
	}

	/**
	 * 获取并发最大使用链接数，记录链接池到目前为止并发使用链接的最大数目， 外部数据源返回-1
	 * 
	 * @return
	 */
	public int getMaxNumActive() {
		if(this.externalDBName == null)
		{

			return DatasourceUtil.getMaxNumActive(datasource);
		}
		else
		{
			return SQLManager.getInstance().getPool(externalDBName).getMaxNumActive();
		}
	}
	
	 
    /**
     * 返回最大峰值出现的时间点
     * @return
     */
    public long getMaxActiveNumTime()
    {
    	if(this.externalDBName == null)
		{

			return DatasourceUtil.getMaxActiveNumTime(datasource);
		}
		else
		{
			return SQLManager.getInstance().getPool(externalDBName).getMaxActiveNumTime();
		}
    }

	/**
	 * 获取当前链接池中空闲的链接数 接口只对内部数据源有用，外部数据源返回-1
	 * 非连接池数据源为实现该功能
	 * @return
	 */
	public int getNumIdle() {
		if(this.externalDBName == null)
		{
			

			return DatasourceUtil.getNumIdle(datasource);
		}
		else
		{
			return SQLManager.getInstance().getPool(externalDBName).getNumIdle();
		}
	}

	public void stopPool() throws Exception {

		closeAllResources();
	}

	/**
	 * Overriden in order to ensure that JNDI resources are disposed of
	 * properly.
	 * @throws Exception 
	 */
	public void closeAllResources() throws Exception {
		if (this.status.equals("stop"))
			return;
		if (!this.status.equals("start"))
			return;
		
//		System.out.println("Shutdown poolman[" + this.getDBName() + "] start.");
		log.debug("Shutdown datasource[" + this.getDBName() + "] start.");
			
			undeployDataSource();
			this.stopTime = System.currentTimeMillis();
			this.status = "stop";
			if (this.tableMetaDatas != null) {
				try {
					tableMetaDatas.clear();
					tableMetaDatas = new TreeSet();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(this.preparedStatementPool != null)
				this.preparedStatementPool.clear();
			if(tableMetaDatasindexByTablename != null)
				this.tableMetaDatasindexByTablename.clear();
			this.datasource = null;
			this.inited = false;
	
//		System.out.println("Shutdown poolman[" + this.getDBName() + "] ok.");
		log.debug("Shutdown datasource[" + this.getDBName() + "] ok.");
		
	}

	/**
	 * Static method that closes the Connection, Statement and ResultSets in one
	 * place.
	 */
	public static void closeResources(Connection con, Statement statement,
			ResultSet resultset) {
		closeResultSet(resultset);
		closeStatement(statement);
		closeConnection(con);
	}

	/**
	 * Static method that closes the statement and result sets in one place;
	 * this is here as a convenience to shorten the finally block in statements.
	 * Both arguments may be null;
	 * 
	 * @param statement
	 *            the statement to be closed
	 * @param resultSet
	 *            the resultSet to be closed
	 */
	public static void closeResources(Statement statement, ResultSet resultSet) {

		closeResultSet(resultSet);
		closeStatement(statement);
	}

	public static void closeConnection(Connection con) {
		try {
			if (con != null && !con.isClosed())
				con.close();
		} catch (SQLException e) {
		}
		con = null;
	}

	/**
	 * Closes the given statement. It is here to get rid of the extra try block
	 * in finally blocks that need to close statements
	 * 
	 * @param statement
	 *            the statement to be closed. may be null
	 */
	public static void closeStatement(Statement statement) {
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
		}
		statement = null;
	}

	/**
	 * This method closes the given ResultSet.
	 * 
	 * @param rs
	 *            the ResultSet to be closed. May be null.
	 */
	public static void closeResultSet(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
		}
		// //added by biaoping.yin on 2005.03.28
		catch (Exception e) {
			// System.out.println("");
		}
		rs = null;
	}

	/**
	 * 获取数据库适配器
	 * 
	 * @return DB
	 */
	public DB getDbAdapter() {
		if(this.externalDBName == null)
		{
			return dbAdapter;
			
		}
		else
		{
			return SQLManager.getInstance().getDBAdapter(externalDBName);
		}
			
	}

	public String getDBName() {
		return info.getDbname();
	}

	private boolean inited = false;

	public Set getTableMetaDatas() {

		if(this.externalDBName == null)
		{
			String load = this.info.getLoadmetadata();
			if (load.equalsIgnoreCase("false") && !inited) {
				synchronized (tableMetaDatas) {
					if (!inited) {
	
						try {
							this.initDatabaseMetaData(null);
							inited = true;
	
						} catch (Exception e) {
							inited = true;
	
						}
	
						// return tableMetaDatas;
	
					}
				}
				// return tableMetaDatas;
			}
			return tableMetaDatas;
		}
		else
		{
			return SQLManager.getInstance().getPool(externalDBName).getTableMetaDatas();
		}
	}

	public boolean isRobotQuery() {
//		if(this.externalDBName == null)
		{
			return this.info.isRobotquery();
		}
//		else
//		{
//			return SQLManager.getInstance().getPool(externalDBName).isRobotQuery();
//		}
	}

	public JDBCPoolMetaData getJDBCPoolMetadata() {
		return info;
	}

	public String getStatus() {
		if(this.externalDBName == null)
			return status;
		return SQLManager.getInstance().getPool(externalDBName).getStatus();
		
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getStartTime() {
		if(this.externalDBName == null)
			return startTime;
		return SQLManager.getInstance().getPool(externalDBName).getStartTime();
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getStopTime() {
		if(this.externalDBName == null)
			return stopTime;
		return SQLManager.getInstance().getPool(externalDBName).getStopTime();
	}

	/**
	 * @return the interceptor
	 */
	public InterceptorInf getInterceptor() {
		return interceptor;
	}

	/**
	 * @param interceptor the interceptor to set
	 */
	public void setInterceptor(InterceptorInf interceptor) {
		this.interceptor = interceptor;
	}


	public boolean isExternal()
	{
		return getJDBCPoolMetadata().isExternal();
	}

	/**
	 * @return the externalDBName
	 */
	public String getExternalDBName() {
		return externalDBName;
	}
	
	public boolean isAutoprimarykey() {

			return this.info.getAutoprimarykey();

	}
	
	public boolean showsql()
	{
		return this.info.isShowsql();
	}

	public IdGenerator getIdGenerator() {
		if(this.externalDBName == null)
		{
			
			return this.idGenerator;
		}
		else
		{
			return SQLManager.getInstance().getPool(externalDBName).getIdGenerator();
		}
	}
	public boolean getRETURN_GENERATED_KEYS()
	{
		if(this.externalDBName == null)
		{
			return this.info.getRETURN_GENERATED_KEYS();
		}
		else
		{
			return SQLManager.getInstance().getPool(externalDBName).getRETURN_GENERATED_KEYS();
		}
	}

	public void increamentMaxTotalConnections(int nums) {
		if(this.datasource != null)
		{
			DatasourceUtil.increamentMaxTotalConnections(datasource, nums);
		}
		
	}
}
