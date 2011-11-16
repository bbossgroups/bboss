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
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.NestedSQLException;
import com.frameworkset.common.poolman.PoolManConstants;
import com.frameworkset.common.poolman.interceptor.InterceptorInf;
import com.frameworkset.common.poolman.management.PoolManConfiguration;
import com.frameworkset.common.poolman.sql.ColumnMetaData;
import com.frameworkset.common.poolman.sql.ForeignKeyMetaData;
import com.frameworkset.common.poolman.sql.PoolManDataSource;
import com.frameworkset.common.poolman.sql.PrimaryKeyMetaData;
import com.frameworkset.common.poolman.sql.TableMetaData;
import com.frameworkset.commons.dbcp.BasicDataSource;
import com.frameworkset.commons.dbcp.BasicDataSourceFactory;
import com.frameworkset.commons.dbcp.NativeDataSource;
import com.frameworkset.commons.pool.impl.GenericObjectPool;
import com.frameworkset.orm.adapter.DB;
import com.frameworkset.orm.adapter.DBFactory;
import com.frameworkset.orm.transaction.JDBCTransaction;
import com.frameworkset.orm.transaction.TransactionManager;

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

	public static Context ctx = null;
	private static boolean initcontexted = false;

	private static void initctx()
	{
		if(ctx == null)
		{
			try {
				// org.apache.naming.java.URLContextFactory s;
	
				// System.setProperty(Context.INITIAL_CONTEXT_FACTORY,"com.frameworkset.common.poolman.management.JndiDataSourceFactory");
				if (PoolManConfiguration.jndi_principal != null) {
					Hashtable environment = new Hashtable();
					// environment.put("java.naming.factory.initial",
					// "weblogic.jndi.WLInitialContextFactory");
					// environment.put("java.naming.provider.url",
					// "t3://localhost:7001");
	
//					environment.put("java.naming.security.principal",
//							PoolManConfiguration.jndi_principal);
//					environment.put("java.naming.security.credentials",
//							PoolManConfiguration.jndi_credentials);
					ctx = new InitialContext(environment);
				} else {
					ctx = new InitialContext();
				}
				testJNDI();
	
			} catch (NamingException e) {
				
				try {
					System
							.setProperty(Context.INITIAL_CONTEXT_FACTORY,
									"com.frameworkset.common.poolman.jndi.DummyContextFactory");
					ctx = new InitialContext();
				} catch (NamingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			catch (Exception e) {
				
				try {
					System
							.setProperty(Context.INITIAL_CONTEXT_FACTORY,
									"com.frameworkset.common.poolman.jndi.DummyContextFactory");
					ctx = new InitialContext();
				} catch (NamingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			initcontexted = true;
			
		}
	}

	public static void testJNDI() throws NamingException {
		ctx.rebind("test", "1");
		ctx.unbind("test");

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
				log.error(ex.getMessage());
			}
		} else if(driver != null){
			try {
				log.debug("Init DBAdapter from dbtype:" + dbtype);
				dbAdapter = DBFactory.create(dbtype);
			} catch (InstantiationException ex1) {
				log.error(ex1.getMessage());
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
		p.setProperty(PoolManConstants.PROP_URL, info.getURL());
		if (info.getPassword() != null)
			p.setProperty(PoolManConstants.PROP_PASSWORD, info
					.getPassword());
		if (info.getUserName() != null)
			p.setProperty(PoolManConstants.PROP_USERNAME, info
					.getUserName());
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
		p.setProperty(PoolManConstants.PROP_DEFAULTREADONLY, info
				.isReadOnly()
				+ "");

		p.setProperty(
				PoolManConstants.PROP_DEFAULTTRANSACTIONISOLATION, info
						.getTxIsolationLevel()
						+ "");

		p.setProperty(PoolManConstants.PROP_POOLPREPAREDSTATEMENTS,
				info.isPoolPreparedStatements() + "");
		p.setProperty(PoolManConstants.PROP_MAXOPENPREPAREDSTATEMENTS,
				info.getMaxOpenPreparedStatements() + "");
		// if(info.getValidationQuery() != null)
		// p.setProperty(PoolManConstants.PROP_VALIDATIONQUERY,
		// info.getValidationQuery() );
		if (info.isMaximumSoft())
			p.setProperty(PoolManConstants.PROP_WHENEXHAUSTEDACTION,
					GenericObjectPool.WHEN_EXHAUSTED_GROW + "");
		else {
			p.setProperty(PoolManConstants.PROP_WHENEXHAUSTEDACTION,
					GenericObjectPool.WHEN_EXHAUSTED_BLOCK + "");
		}
		
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
	private void initPoolDatasource()
	{
		try {

			
			Properties p = getProperties();
			DataSource _datasource =  BasicDataSourceFactory
					.createDataSource(p);

			if (this.info.getJNDIName() != null
					&& !this.info.getJNDIName().equals("")) {
				this.datasource = new PoolManDataSource(_datasource, info
						.getDbname(), info.getJNDIName());
			} else {
				this.datasource = _datasource;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	public void setUpCommonPool() {

		if (!this.info.isExternal()) {
			
				initPoolDatasource();
			
		} else {

			try {
				// Context ctx = new InitialContext();
				if(this.externalDBName == null)
				{
					DataSource _datasource = find(this.info.getExternaljndiName());
					if (_datasource != null) {
						if (this.info.getJNDIName() != null
								&& !this.info.getJNDIName().equals("")) {
							this.datasource = new PoolManDataSource(_datasource,
									info.getDbname(), info.getJNDIName());
						} else {
							this.datasource = _datasource;
						}
					}
				}
			} catch (NamingException e) {
				log.error("通过JNDI名称[" + info.getExternaljndiName()
						+ "]获取外部数据源失败:", e);
				// e.printStackTrace();
			}

		}

		// /**
		// * 首先创建一个对象池来保存数据库连接
		// *
		// * 使用 commons.pool 的 GenericObjectPool对象
		// */
		// ObjectPool connectionPool = new GenericObjectPool();
		//
		// /**
		// * 创建一个 DriverManagerConnectionFactory对象 连接池将用它来获取一个连接
		// */
		//
		// Properties properties = new Properties();
		// properties.put("user", info.getUserName());
		// properties.put("password", info.getPassword());
		// ConnectionFactory connectionFactory = new
		// DriverManagerConnectionFactory(
		// this.info.getURL(), properties);
		//
		// /**
		// * 创建一个PoolableConnectionFactory 对象。
		// */
		// PoolableConnectionFactory poolableConnectionFactory = new
		// PoolableConnectionFactory(
		// connectionFactory, connectionPool, null, null, false,
		// true);
		//
		// // connectionPool.setFactory(poolableConnectionFactory);
		//
		// driver.registerPool(info.getDbname(), connectionPool);
		//			

	}

	public static DataSource find(String jndiName) throws NamingException {
		try {
			// Context ctx = new InitialContext();
			initctx();
			DataSource _datasource = (DataSource) ctx.lookup(jndiName);
			return _datasource;
		} catch (NamingException e) {

			throw e;
			// e.printStackTrace();
		}
	}

	public JDBCPool(JDBCPoolMetaData metad) {

		
		this.deployedDataSource = false;
		this.info = metad;
		
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
		if (!status.equals("stop") && !status.equals("unknown"))
			return;
		initDBAdapter();
		
//		if(!this.isExternal())
		{
			if(this.datasource != null)
			{
				try {
					if (this.datasource instanceof BasicDataSource) {
						((BasicDataSource) datasource).close();
					} else if (datasource instanceof PoolManDataSource) {
						((PoolManDataSource) datasource).close();
					}
				} catch (Exception e) {
					
				}
				datasource = null;
			}
		}
//		else
//		{
//			if(this.datasource != null)
//				try {
//					if (this.datasource instanceof BasicDataSource) {
//						((BasicDataSource) datasource).close();
//					} else if (datasource instanceof PoolManDataSource) {
//						((PoolManDataSource) datasource).close();
//					}
//				} catch (Exception e) {
//					
//				}
//			this.datasource = null;
//		}
		this.setUpCommonPool();
		this.startTime = System.currentTimeMillis();
		this.status = "start";

		try {

			
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
			log.debug("JDBCPool: Exception while initializing", e);
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
							column.setDataType(columnrs.getInt("DATA_TYPE"));
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
						column.setDataType(columnrs.getInt("DATA_TYPE"));
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

		// temporarily unset initConnectionSQL until after initPoolSQL executes
		// String initConSQL = info.getInitialConnectionSQL();
		//
		// // super.init();
		// // set up cache
		// // if (info.isCacheEnabled()) {
		// // this.sqlcache = new SQLCache(this, info.getCacheSize(),
		// // info.getCacheRefreshInterval());
		// // }
		//
		// // perform initial pool SQL
		// if (info.getInitialPoolSQL() != null) {
		// // try {
		// // // PoolManConnection pc = (PoolManConnection) create();
		// // // Connection c = pc.getPhysicalConnection();
		// // // Statement s = c.createStatement();
		// // // s.execute(info.getInitialPoolSQL());
		// // } catch (SQLException sqe) {
		// // log.debug("Init Pool SQL suffered a SQLException: " + sqe);
		// // throw new SQLException("Init Pool SQL suffered a SQLException: "
		// // + sqe);
		// // }
		// }
		//
		// // now reset initConSQL
		// info.setInitialConnectionSQL(initConSQL);

		// bind the DataSource view of this pool to JNDI
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
			log.info("Intial Context : " + jndiName);
			System.out.println("Intial Context : " + jndiName);
			// Context ctx = new InitialContext();
			if (!exist(ctx, jndiName)) {
				bind(ctx, jndiName, this.datasource);
			}

			this.deployedDataSource = true;

			log.info("DataSource bound to JNDI under name[" + jndiName + "]");
			System.out.println("DataSource bound to JNDI under name["
					+ jndiName + "]");

		} catch (Exception e) {
			e.printStackTrace();
			log
					.error("PoolMan JDBCPool unable to locate a default JNDI provider, "
							+ "DataSource is still available -- for example, get the DataSource via "
							+ "PoolMan.findDataSource("
							+ this.info.getDbname()
							+ ") --  "
							+ "but is not available through JNDI: "
							+ e.getMessage());
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
	public static void bind(Context ctx, String name, Object val)
			throws NamingException {
		try {
			initctx();
			log.debug("binding: " + name);
			ctx.rebind(name, val);
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
		log.debug("Bound name: " + name);
	}

	/**
	 * 判断jndiName是否存在
	 * 
	 * @param ctx
	 * @param jndiName
	 * @return
	 */
	private boolean exist(Context ctx, String jndiName) {
		try {
			initctx();
			DataSource datasource = (DataSource) ctx.lookup(jndiName);
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
				if (this.datasource instanceof BasicDataSource) {
					((BasicDataSource) datasource).close();
				} else if (datasource instanceof PoolManDataSource) {
					((PoolManDataSource) datasource).close();
				}
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

	// /** Associates a SQLCache with this pool. */
	// public void setCache(SQLCache cache) {
	// this.sqlcache = cache;
	// }

	// /** @return SQLCache The SQLCache associated with this pool. */
	// public SQLCache getCache() {
	// return this.sqlcache;
	// }

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
		// if (usingCache())
		// this.sqlcache.forceRefresh();
	}

	public String getDriver() {
//		if(this.externalDBName == null)
//			return info.getDriver();
//		JDBCPoolMetaData meta = SQLManager.getInstance().getJDBCPoolMetaData(externalDBName);
//		if(meta != null)
//			return meta.getDriver();
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
//		if(this.externalDBName == null)
			return info.isNativeResults();
//		JDBCPoolMetaData meta = SQLManager.getInstance().getJDBCPoolMetaData(externalDBName);
//		if(meta != null)
//			return meta.isNativeResults();
//		return info.isNativeResults();
		
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

//	/** Returns a connection to the pool. */
//	public void returnConnection(Object pcon) {
//
//		// pcon.clean();
//		// super.checkIn(pcon);
//	}

//	private Object connectionForOracle10g() throws SQLException {
//		try {
//			OracleDataSource ods = new OracleDataSource();
//			ods.setURL(info.getURL());
//			ods.setUser(info.getUserName());
//			ods.setPassword(info.getPassword());
//			Connection con = ods.getConnection();
//			con.setTransactionIsolation(info.getIsolationLevel());
//
//			// set auto commit
//			con.setAutoCommit(true);
//			if (info.getInitialConnectionSQL() != null) {
//				try {
//					Statement s = con.createStatement();
//					s.execute(info.getInitialConnectionSQL());
//					s.close();
//				} catch (SQLException sqe) {
//					log.debug("Init SQL Suffered a SQLException: " + sqe);
//					throw new RuntimeException(
//							"Init SQL Suffered a SQLException: " + sqe);
//				}
//			}
//			//	        
//			// PoolManConnection pcon = new PoolManConnection(con, this);
//			// pcon.addConnectionEventListener(this);
//			return con;
//
//			// return pcon;
//		} catch (SQLException sqlex) {
//			String emsg = "SQLException occurred in JDBCPool: "
//					+ sqlex.toString() + "\nparams: " + info.getDriver() + ", "
//					+ info.getURL() + ". Please check your username, password "
//					+ "and other connectivity info.";
//			log.info(emsg, sqlex);
//			throw new SQLException(emsg);
//		} catch (Exception e) {
//			log.info(e.getMessage(), e);
//			throw new SQLException(e.getMessage());
//		}
//	}
//
//	private Object connectionForOracle9i() throws SQLException {
//		try {
//
//			Driver d = (Driver) (Class.forName(info.getDriver()).newInstance());
//			Properties p = new Properties();
//			p.put("user", info.getUserName());
//			p.put("password", info.getPassword());
//
//			if (!d.acceptsURL(info.getURL())) {
//				log.info("Driver cannot accept URL: " + info.getURL());
//				throw new SQLException("Driver cannot accept URL: "
//						+ info.getURL());
//			}
//			Connection con = d.connect(info.getURL(), p);
//
//			// set tx isolation level
//			con.setTransactionIsolation(info.getIsolationLevel());
//
//			// set auto commit
//			con.setAutoCommit(true);
//
//			// execute initialConnectionSQL, if any
//			if (info.getInitialConnectionSQL() != null) {
//				try {
//					Statement s = con.createStatement();
//					s.execute(info.getInitialConnectionSQL());
//					s.close();
//				} catch (SQLException sqe) {
//					log.debug("Init SQL Suffered a SQLException: " + sqe);
//					throw new RuntimeException(
//							"Init SQL Suffered a SQLException: " + sqe);
//				}
//			}
//
//			// PoolManConnection pcon = new PoolManConnection(con, this);
//			// pcon.addConnectionEventListener(this);
//			//
//			// return pcon;
//			return con;
//
//			/*
//			 * OLD CODE Class.forName(info.getDrivername()).newInstance();
//			 * Connection con = DriverManager.getConnection(info.getUrl(),
//			 * info.getUsername(), info.getPassword());
//			 */
//
//		} catch (ClassNotFoundException cnfex) {
//			String msg = "Looks like the driver for your database was not found. Please verify that it is in your CLASSPATH and "
//					+ "listed properly in the poolman.xml file.";
//			log.info(msg, cnfex);
//			throw new SQLException(msg);
//		} catch (SQLException sqlex) {
//			String emsg = "SQLException occurred in JDBCPool: "
//					+ sqlex.toString() + "\nparams: " + info.getDriver() + ", "
//					+ info.getURL() + ". Please check your username, password "
//					+ "and other connectivity info.";
//			log.info(emsg, sqlex);
//			throw new SQLException(emsg);
//		} catch (Exception e) {
//			log.info(e.getMessage(), e);
//			throw new SQLException(e.getMessage());
//		}
//	}

//	/**
//	 * Creates a physical Connection and PooledConnection wrapper for it (a
//	 * PoolManConnection).
//	 */
//	protected Object create() throws SQLException {
//
//		if (info.getDriver() == null || info.getURL() == null) {
//			log.info("No Driver and/or URL found!");
//			throw new SQLException("No Driver and/or URL found!");
//		}
//
//		if (false)
//			return this.connectionForOracle10g();
//		else
//			return this.connectionForOracle9i();
//
//	}

	// /** Checks to see if the current connection is valid. */
	// protected boolean validate(Object o) {
	//
	// boolean valid = false;
	// PoolManConnection pcon = (PoolManConnection) o;
	//
	// if (this.info.getValidationQuery() != null) {
	//
	// // execute validationSQL
	// Connection con = null;
	// Statement vs = null;
	// ResultSet rs = null;
	// try {
	// // Postgres fails on raw connection...
	//            	
	// con = pcon.getPhysicalConnection();
	// vs = con.createStatement();
	// vs.execute(this.info.getValidationQuery());
	// valid = true;
	// } catch (Exception e) {
	// log.debug("Failed to validate Connection using validationQuery: " +
	// this.info.getValidationQuery(), e);
	// }
	// finally {
	// closeResources(vs, rs);
	// }
	// }
	//
	// else {
	// Connection con = null;
	// Statement vs = null;
	// try {
	// if(pcon.isClosed())
	// return false;
	// con = pcon.getPhysicalConnection();
	// vs = con.createStatement();
	// valid = true;
	// //valid = !pcon.isClosed();
	// } catch (SQLException e) {
	// }
	// finally
	// {
	// if(vs != null)
	// closeResources(vs, null);
	// }
	// }
	//
	// return valid;
	// }

//	/** Closes a physical database connection. */
//	protected void expire(Object o) {
//		// try {
//		// PoolManConnection pcon = (PoolManConnection) o;
//		// pcon.removeConnectionEventListener(this);
//		// try {
//		// pcon.commit();
//		// } catch (SQLException se) {
//		// }
//		// pcon.close();
//		// } catch (Exception e) {
//		// }
//		// o = null;
//	}

	/** Retrieves a PooledConnection impl and returns its Handle. */
	public Connection requestConnection() throws SQLException {
		if(datasource != null)
		{
			return this.datasource.getConnection();
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
		// try {
		// PoolManConnection pcon = (PoolManConnection) super.checkOut();
		// return pcon.getConnection();
		// } catch (SQLException sqle) {
		// log.error(sqle.getMessage(), sqle);
		// throw new SQLException(sqle.getMessage());
		// } catch (Exception e) {
		// log.error("A non-SQL error occurred when requesting a connection:",
		// e);
		// throw new SQLException(e.getMessage());
		// }
	}

//	public PreparedStatement requestPooledStatement(String sql) {
//
//		PreparedStatement ps = null;
//
//		if (preparedStatementPool.containsKey(sql)) {
//			ArrayList statements = (ArrayList) preparedStatementPool.get(sql);
//			synchronized (statements) {
//				if (statements.size() > 0) {
//					ps = (PreparedStatement) statements.remove(0);
//				}
//			}
//		}
//
//		return ps;
//	}

	// /** Retuns a PreparedStatement to the statement pool */
	// public void returnPooledStatement(PoolManPreparedStatement ps) {
	//
	// ArrayList statements = null;
	// if (info.isPoolPreparedStatements()) {
	// if (preparedStatementPool.containsKey(ps.getSQL())) {
	// statements = (ArrayList) preparedStatementPool.get(ps.getSQL());
	// }
	// else {
	// statements = new ArrayList();
	// }
	//
	// synchronized (statements) {
	// statements.add(ps.getNativePreparedStatement());
	// preparedStatementPool.put(ps.getSQL(), statements);
	// }
	// }
	// else {
	// closeStatement(ps.getNativePreparedStatement());
	// }
	// }

//	public int numStatementPools() {
//		return preparedStatementPool.size();
//	}

//	public int numPooledStatements(String sql) {
//		int num = 0;
//		if (preparedStatementPool.containsKey(sql)) {
//			ArrayList statements = (ArrayList) preparedStatementPool.get(sql);
//			num = statements.size();
//		}
//		return num;
//	}

	/**
	 * 获取当前链接池中正在使用的链接 接口只对内部数据源有用，外部数据源返回-1
	 * 
	 * @return
	 */
	public int getNumActive() {
		if(this.externalDBName == null)
		{
			if (this.datasource instanceof BasicDataSource) {
				return ((BasicDataSource) this.datasource).getNumActive();
			}
			else if (this.datasource instanceof NativeDataSource) {
				return ((NativeDataSource) this.datasource).getNumActive();
			}
			else if (this.datasource instanceof PoolManDataSource) {
				PoolManDataSource temp = (PoolManDataSource) this.datasource;
				DataSource temp_ = temp.getInnerDataSource();
				if (temp_ != null) {
					if (temp_ instanceof BasicDataSource) {
						return ((BasicDataSource) temp_).getNumActive();
					}
					else if (temp_ instanceof NativeDataSource) {
						return ((NativeDataSource) temp_).getNumActive();
					}
				}
			}
	
			return -1;
		}
		else
		{
			return SQLManager.getInstance().getPool(externalDBName).getNumActive();
		}
	}
	
	/**
	 * 获取当前链接池中正在使用的链接 接口只对内部数据源有用，外部数据源返回-1
	 * 非连接池数据源为实现该功能
	 * @deprecated 谨慎使用本接口
	 * @return
	 */
	public List getTraceObjects() {
		if(this.externalDBName == null)
		{
			if (this.datasource instanceof BasicDataSource) {
				return ((BasicDataSource) this.datasource).getTraceObjects();
			} 
			else if (this.datasource instanceof NativeDataSource) {
				return ((NativeDataSource) this.datasource).getTraces();
			}
			else if (this.datasource instanceof PoolManDataSource) {
				PoolManDataSource temp = (PoolManDataSource) this.datasource;
				DataSource temp_ = temp.getInnerDataSource();
				if (temp_ != null) {
					if (temp_ instanceof BasicDataSource) {
						return ((BasicDataSource) temp_).getTraceObjects();
					}
					else if (temp_ instanceof NativeDataSource) {
						return ((NativeDataSource) temp_).getTraces();
					}
				}
			}
	
			return new ArrayList();
		}
		else
		{
			return SQLManager.getInstance().getPool(externalDBName).getTraceObjects();
		}
	}
	
	

	/**
	 * 获取并发最大使用链接数，记录链接池到目前为止并发使用链接的最大数目， 外部数据源返回-1
	 * 
	 * @return
	 */
	public int getMaxNumActive() {
		if(this.externalDBName == null)
		{
			if (this.datasource instanceof BasicDataSource) {
				return ((BasicDataSource) this.datasource).getMaxNumActive();
			} 
			else if (this.datasource instanceof NativeDataSource) {
				return ((NativeDataSource) this.datasource).getMaxNumActive();
			}
			else if (this.datasource instanceof PoolManDataSource) {
				PoolManDataSource temp = (PoolManDataSource) this.datasource;
				DataSource temp_ = temp.getInnerDataSource();
				if (temp_ != null) {
					if (temp_ instanceof BasicDataSource) {
						return ((BasicDataSource) temp_).getMaxNumActive();
					}
					else if (temp_ instanceof NativeDataSource) {
						return ((NativeDataSource) temp_).getMaxNumActive();
					}
				}
			}
	
			return -1;
		}
		else
		{
			return SQLManager.getInstance().getPool(externalDBName).getMaxNumActive();
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
			if (this.datasource instanceof BasicDataSource) {
				return ((BasicDataSource) this.datasource).getNumIdle();
			} else if (this.datasource instanceof PoolManDataSource) {
				PoolManDataSource temp = (PoolManDataSource) this.datasource;
				DataSource temp_ = temp.getInnerDataSource();
				if (temp_ != null) {
					if (temp_ instanceof BasicDataSource) {
						return ((BasicDataSource) temp_).getNumIdle();
					}
				}
			}
			return -1;
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
		
		System.out.println("Shutdown poolman[" + this.getDBName() + "] start.");
		log.debug("Shutdown poolman[" + this.getDBName() + "] start.");
			
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
//		}
//		else
//		{
//			try {
//				
//				if(ctx != null)
//					ctx.unbind(this.info.getJNDIName());
//			} catch (Exception e) {
//
//			}
//			this.datasource = null;
//			SQLManager.getInstance().getPool(externalDBName).closeAllResources();
//			this.stopTime = System.currentTimeMillis();
//			this.status = "stop";
//		}		
		System.out.println("Shutdown poolman[" + this.getDBName() + "] ok.");

		
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

	// public static void main(String[] args)
	// {
	// Set s = DBUtil.getColumnMetaDatas("tableinfo");
	// s.getClass();
	// }
	
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
//		if(this.externalDBName == null)
			return this.info.getAutoprimarykey();
//		else
//		{
//			return SQLManager.getInstance().getPool(externalDBName).isAutoprimarykey();
//		}
	}
}
