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


import com.frameworkset.common.poolman.PoolManConstants;
import com.frameworkset.common.poolman.management.BaseTableManager;
import com.frameworkset.common.poolman.management.PoolManBootstrap;
import com.frameworkset.common.poolman.management.PoolManConfiguration;
import com.frameworkset.orm.adapter.DB;
import com.frameworkset.orm.transaction.TXDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class SQLManager extends PoolManager{

	private static Logger log = LoggerFactory.getLogger(SQLManager.class);

    /**
     * Singleton is instantiated here in order to bypass the
     * double-check locking problem in many VM's.
     */
    private static SQLManager myself ;
	static void initMyself(){
		if(myself == null) {
			synchronized (SQLManager.class) {
				if(myself == null) {
					myself = new SQLManager();
				}
			}
		}
	}
	public static void setSQLMappingdir(String sqlMappingdir){
		PoolManConfiguration.setSqlMappingDir(sqlMappingdir);
	}
	public static void setRefresh_interval(Long refresh_interval) {
		PoolManConfiguration.setRefresh_interval( refresh_interval);
	}

    private String configFile = PoolManConstants.XML_CONFIG_FILE;

    public static void destroy()
    {
    	if(myself != null)
    	{
    		myself.destroyPools();
    		myself = null;
    	}
    }
    /**
     * This method retrieves the singleton SQLManager instance
     * created when the class was first loaded.
     * @return SQLManager
     */
    public static SQLManager getInstance() {
		initMyself();
        return myself;
    }

    /**
     * If a configuration file other than the default is specified,
     * then the singleton instance will be recreated using the new
     * file. Subsequent invocations of this method with the same
     * file name passed as a parameter will not cause a recreation,
     * it will simply return the singleton created based on the previous
     * parsing of that config file.
     *
     * @return SQLManager
     * @param  confFile The name of the file to use for configuration.
     */
    public static SQLManager getInstance(String confFile) {
    	initMyself();
        if (!confFile.equals(myself.getConfigFile())) {
            synchronized (SQLManager.class) {
                if (!confFile.equals(myself.getConfigFile())) {
                    myself = null;
                    myself = new SQLManager();
                    myself.setConfigFile(confFile);
                }
            }
        }
        return myself;
    }
    
    public List<String> getAllPoolNames() {
        assertLoaded();
        return super.getAllPoolNames();
    }
    
    public List<String> getAllPoolNamesIfExist() {
//        assertLoaded();
        return super.getAllPoolNames();
    }

    /*
      TO DO: ADD THIS AGAIN
    public static SQLManager getInstance(Properties p) {}
    */


    /* Private Constructors. */

    private SQLManager() {
        super();
    }

    public JDBCPool createPool(JDBCPoolMetaData metad) {
    	try {
    		JDBCPool pool = this.getPoolIfExist(metad.getDbname());
			if(pool != null)
				pool.stopPool();
		} catch (Exception e) {
			log.warn("stop pool failed:",e);
		}
        JDBCPool jpool = new JDBCPool(metad);
        addPool(metad.getName(), jpool);
        return jpool;
    }


    public void setConfigFile(String filename) {
        this.configFile = filename;
    }

    public String getConfigFile() {
        return this.configFile;
    }
    
    private void assertLoaded() {
        try {
            if (this.pools.size() < 1) {
                synchronized (SQLManager.class) {
                    if (this.pools.size() < 1) {
                        new com.frameworkset.common.poolman.management.PoolManBootstrap(this.configFile).start();
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Fatal Error while attempting " + " to Configure PoolMan: " + e.getMessage() + " " + e.toString());
        }
    }

    /**
     * 根据dbname获取数据源
     * @param dbname
     * @return
     */
    public static DataSource getDatasourceByDBName(String dbname)
    {
    	JDBCPool pool = SQLManager.getInstance().getPool(dbname);
    	if(pool != null)
    		return pool.getDataSource();
    	throw new IllegalArgumentException("获取数据源失败："+dbname +"不存在，Check your datasource is started and config is right.");
    }
    
    /**
     * 根据dbname获取数据源
    
     * @return
     */
    public static DataSource getDatasource()
    {
    	JDBCPool pool = SQLManager.getInstance().getPool(null);
    	if(pool != null)
    		return pool.getDataSource();
    	throw new IllegalArgumentException("获取数据源失败：Check your datasource is started and config is right.");
    }
    
    
    /**
     * 根据dbname获取数据源
     * @param dbname
     * @return
     */
    public static DataSource getTXDatasourceByDBName(String dbname)
    {
    	JDBCPool pool = SQLManager.getInstance().getPool(dbname);
    	if(pool != null)
    	{
    		DataSource datasource = pool.getDataSource();
    		if(!(datasource instanceof TXDataSource))
    			return new TXDataSource( pool.getDataSource(),pool);
    		else
    			return datasource;
    	}
    	throw new IllegalArgumentException("获取数据源失败："+dbname +"不存在，Check your datasource is started and config is right.");
    }
    
    /**
     * 根据dbname获取数据源
      
     * @return
     */
    public static DataSource getTXDatasource()
    {
    	JDBCPool pool = SQLManager.getInstance().getPool(null);
    	if(pool != null)
    	{
    		DataSource datasource = pool.getDataSource();
    		if(!(datasource instanceof TXDataSource))
    			return new TXDataSource( pool.getDataSource(),pool);
    		else
    			return datasource;
    	}
    	throw new IllegalArgumentException("获取数据源失败：Check your datasource is started and config is right!");
    }
    
    
    /**
     * 将ds转换为TXDatasource事务代理数据源
     * @param ds
     * @return
     */
    public static DataSource getTXDatasource(DataSource ds)
    {
    	
    	if(ds != null)
    	{
    		if(ds instanceof TXDataSource)
    			return ds;
    		return new TXDataSource( ds,null);
    	}
    	throw new IllegalArgumentException("获取数据源失败：ds is null");
    }
  
    /**
     * Overridden implementation ensures the config is loaded.
     */
    public JDBCPool getPool(String name) {
//        assertLoaded();
//        return super.getPool(name);
    	return getPool(name,true);
    }
    
    /**
     * Overridden implementation ensures the config is loaded.
     */
    public JDBCPool getPool(String name,boolean needcheckStart) {
        if(needcheckStart) 
        	assertLoaded();
        return super.getPool(name);
    }

	/**
	 * Overridden implementation ensures the config is loaded.
	 */
	public JDBCPool getPoolIfExist(String name,boolean remove) {
		if(name == null)
			name = this.getDefaultDBNameIfExist();
		return super.getPoolIfExist(name,remove);
	}


	/**
     * Overridden implementation ensures the config is loaded.
     */
    public JDBCPool getPoolIfExist(String name) {
		return getPoolIfExist( name,false);
    }
    
    

    public JDBCPool getPoolByJNDIName(String name,boolean needcheckStart) {
        if(needcheckStart)
        	assertLoaded();
        for (Iterator enum_ = this.pools.entrySet().iterator(); enum_.hasNext();) {
        	Map.Entry e =  (Map.Entry)enum_.next();
            JDBCPool jpool = (JDBCPool) e.getValue();
            String name_ = jpool.getJDBCPoolMetadata().getJNDIName();
            if(name_ != null && name_.equals(name))
                return jpool;

        }
        String errorString = "pool with jndiname[" + name + "] does not exist or this is container datasource.  Please check your " + PoolManConstants.XML_CONFIG_FILE;
        log.warn(errorString);
//        return null;
        return null;
//        throw new NullPointerException(errorString);
    }
    
    public JDBCPool getPoolByJNDIName(String name) {
    	return getPoolByJNDIName(name,true);
    }

//    /**
//     * Overridden implementation ensures the config is loaded.
//     */
//    public Enumeration getAllPoolnames() {
//        assertLoaded();
//        return super.getAllPoolnames();
//    }

    public void checkCredentials(String dbname, String user, String passwd) throws SQLException {
        assertLoaded();
        JDBCPool pool = (JDBCPool) getPool(dbname);
        pool.checkCredentials(user, passwd);
    }

    /**
     * Get a connection from the first (default) database connection pool.
     */
    public Connection requestConnection() throws SQLException {
        assertLoaded();
        try {
            JDBCPool pool = (JDBCPool) this.defaultpool;
            return pool.requestConnection();
        } catch (NullPointerException ne) {
            throw new SQLException("No default pool! Check your datasource is started and config is right!");
        }
    }

    /**
     * Return a connection to the default pool.
     */
    public void returnConnection(Connection con) {
        try {
            con.close();
        } catch (SQLException se) {
        }
    }

    /**
     * Get a connection from a particular database pool.
     */
    public Connection requestConnection(String dbname) throws SQLException {

        if ((dbname == null) || (dbname.equals("")))
            return requestConnection();

        assertLoaded();
        try {
            JDBCPool pool = (JDBCPool) this.pools.get(dbname);
            return pool.requestConnection();
        } catch (NullPointerException ne) {
            throw new SQLException("No pool named " + dbname + "! Check your datasource is started and config is right" +
                                   "\n** DEBUG: If the StackTrace contains an " +
                                   "InstanceAlreadyExistsException, then you have " +
                                   " encountered a ClassLoader linkage problem. " +
                                   " Please submit issue to https://gitee.com/bboss/bboss/issues or https://github.com/bbossgroups/bboss/issues**");
        }
    }
    
    public Map getPools()
    {
    	return this.pools;
    }

    /**
     * Return a connection to a particular database pool. No Longer necessary:
     * The con is a handle that will cause the PooledConnection to return to
     * the correct pool. Method kept for backwards-compatibility purposes.
     */
    public void returnConnection(String dbname, Connection con) {
        returnConnection(con);
    }

    /** Static method that closes the statement and result sets in one place;
     * this is here as a convenience to shorten the finally block
     * in statements.  Both arguments may be null.
     * @param statement the statement to be closed
     * @param resultSet the resultSet to be closed
     */
    public static void closeResources(Statement statement, ResultSet resultSet) {
        closeResultSet(resultSet);
        closeStatement(statement);
    }

    public void collectResources(Statement s, ResultSet r) {
        SQLManager.closeResources(s, r);
    }

    /** Closes the given statement.  It is here to get rid of
     * the extra try block in finally blocks that need to close statements
     * @param statement the statement to be closed. May be null.
     */
    public static void closeStatement(Statement statement) {
        try {
            if (statement != null)
                statement.close();
            statement = null;
        } catch (SQLException e) {
        }
    }

    /** This method closes the given resultset.  It is here to get
     * rid of the extra try block in finally blocks.
     * @param rs the ResultSet to be closed. May be null.
     */
    public static void closeResultSet(ResultSet rs) {
        try {
            if (rs != null)
                rs.close();
            rs = null;
        } catch (SQLException e) {
        }
    }

    /**
     * 获取缺省的数据库连接池名称
     * @return String 缺省的数据库名称
     */
    public String getDefaultDBName()
    {
        assertLoaded();        
        if(defaultpool == null)
        	throw new java.lang.NullPointerException("获取默认数据源名称失败：请确保数据源正常启动，检查配置文件是否配置正确.");
        return this.defaultpool.getInterceptor().getDefaultDBName();
    }
    
    /**
     * 获取缺省的数据库连接池名称
     * @return String 缺省的数据库名称
     */
    public String getDefaultDBNameIfExist()
    {
//        assertLoaded();        
    	if(defaultpool == null)
    		return null;
        return this.defaultpool.getInterceptor().getDefaultDBName();
    }




	/**
	 * 获取数据库适配器
	 * @return DB
	 */
	public DB getDBAdapter()
	{
		return getDBAdapter(null);
	}

	/**
	 * 获取给定数据库名称的数据库适配器
	 * @return DB
	 */
	public DB getDBAdapter(String dbName)
	{
		return ((JDBCPool)getPool(dbName)).getDbAdapter();
	}
	
	
	
	public static String getRealDBNameFromExternalDBName(String externalName)
	{
		if(externalName == null)
		{
			return null;
		}
		JDBCPool pool = SQLManager.getInstance().getPool(externalName);
		if(pool == null )
			return externalName;
		
		if(!pool.isExternal())
		{
			return pool.getDBName();
		}
		else
		{
			String dbname = pool.getExternalDBName(); 
			if(dbname == null)
				return pool.getDBName();
			
			return getRealDBNameFromExternalDBName(dbname);
			
		}
		
	}
	
	public static String getRealDBNameFromExternalDBNameIfExist(String externalName)
	{
//		if(externalName == null)
//		{
//			return null;
//		}
		JDBCPool pool = SQLManager.getInstance().getPoolIfExist(externalName);
		if(pool == null )
			return externalName;
		
		
		if(!pool.isExternal())
		{
			return pool.getDBName();
		}
		else
		{
			String dbname = pool.getExternalDBName(); 
			if(dbname == null)
				return pool.getDBName();
			return getRealDBNameFromExternalDBNameIfExist(dbname);
			
		}
		
	}

	
	public void stopPool(String dbname,boolean remove) throws Exception {
		JDBCPool pool = this.getPoolIfExist(dbname,remove);
        if(pool != null)
        {
        	if(!pool.isExternal() ||  pool.getExternalDBName() == null)
        		pool.stopPool();
        	else
        	{
        		log.warn("忽略外部数据源["+dbname+"]的停止操作");
        	}
        }
        
        BaseTableManager.removePrimaryKeyCache(dbname);
		
	}
	public void stopPool(String dbname) throws Exception {
		stopPool( dbname,false);

	}

	public String statusCheck(String dbname) {
//		if(dbname == null || dbname.equals(""))
//			dbname = SQLUtil.getSQLManager().getDefaultDBName();
//		 JDBCPool pool = SQLUtil.getSQLManager().getPool(dbname);
		JDBCPool pool = this.getPoolIfExist(dbname);
         if(pool != null)
             return pool.statusChecker();
         return "unloaded";
		
	}
	
	public static boolean startPool(String poolname) throws Exception
    {
//		if(poolname == null || poolname.equals(""))
//			poolname = SQLUtil.getSQLManager().getDefaultDBName();
//		JDBCPool pool = SQLUtil.getSQLManager().getPool(poolname);
		JDBCPool pool = SQLUtil.getSQLManager().getPoolIfExist(poolname);
		if(pool != null)
		{
			if(pool.getStatus().equals("start"))
			{
				log.warn("连接池[" + pool.getDBName() + "]已经启动。无需再启动,或者请停止后再启动.");
				return false;
			}
//				throw new IllegalStateException("连接池[" + pool.getDBName() + "]已经启动。无需再启动,或者请停止后再启动.");
				
		}
//		if(pool != null && pool.isExternal())
//		{
//			if(pool.getExternalDBName() != null)
//				startRealDBNameFromExternalDBName(pool.getExternalDBName());
//		}
		PoolManBootstrap boot = new PoolManBootstrap();
		boot.startDB(poolname);
		return true;
    }
	
	public static boolean startRealDBNameFromExternalDBName(String dbname) throws Exception
	{
		
//		JDBCPool pool = SQLUtil.getSQLManager().getPool(dbname);
		JDBCPool pool = SQLUtil.getSQLManager().getPoolIfExist(dbname);
		if(pool != null)
		{
			if(pool.isExternal() && pool.getExternalDBName() != null)
			{
				return startRealDBNameFromExternalDBName(pool.getExternalDBName());
			}
			if(!pool.getStatus().equals("start"))
			{
				PoolManBootstrap boot = new PoolManBootstrap();
				boot.startDB(dbname);
				return true;
			}	
			return false;
		}
		else
		{
			PoolManBootstrap boot = new PoolManBootstrap();
			boot.startDB(dbname);
			return true;
		}
		
		
		
		
		
	}
	
	
	public static boolean startPool(String poolname,String driver,String jdbcurl,String username,String password,String readOnly,String validationQuery)
	{
//		JDBCPool pool = SQLUtil.getSQLManager().getPoolIfExist(poolname);
//		if(pool != null)
//		{
//			if(pool.getStatus().equals("start"))
//			{
////				throw new IllegalStateException("连接池[" + poolname + "]已经启动。无需再启动,或者请停止后再启动.");
//				log.warn("连接池[" + poolname + "]已经启动。无需再启动,或者请停止后再启动.");
//				return;
//			}
//				
//		}
//		Map<String,String> values = new HashMap<String,String>();
//		values.put("dbname", poolname);
//		values.put("jndiname", "");
//		values.put("driver", driver);
//		values.put("url", jdbcurl);
//		values.put("username", username);
//		values.put("password", password);
//		
//		values.put("readonly", readOnly);
//		values.put("validationquery", validationQuery);
//		
//		PoolManBootstrap.startFromTemplte(values);
		return startPool( poolname, driver, jdbcurl, username, password, readOnly, validationQuery,false);
	}
	
	public static boolean startPool(String poolname,String driver,String jdbcurl,String username,String password,String validationQuery,Integer fetchsize)
	{
		DBConf tempConf = new DBConf();
		tempConf.setPoolname(poolname);
		tempConf.setDriver(driver);
		tempConf.setJdbcurl(jdbcurl);
		tempConf.setUsername(username);
		tempConf.setPassword(password);
		tempConf.setValidationQuery(validationQuery);
		//tempConf.setTxIsolationLevel("READ_COMMITTED");
		tempConf.setJndiName("jndi-"+poolname);
		tempConf.setInitialConnections(10);
		tempConf.setMinimumSize(10);
		tempConf.setMaximumSize(20);
		tempConf.setUsepool(true);
		tempConf.setExternal(false);
		tempConf.setEncryptdbinfo(false);
		tempConf.setShowsql(false);
		tempConf.setQueryfetchsize(fetchsize);
		return startPool(tempConf);
	}
	public static boolean startPool(String poolname,String driver,String jdbcurl,String username,String password,String validationQuery)
	{
//		JDBCPool pool = SQLUtil.getSQLManager().getPoolIfExist(poolname);
//		if(pool != null)
//		{
//			if(pool.getStatus().equals("start"))
//			{
////				throw new IllegalStateException("连接池[" + poolname + "]已经启动。无需再启动,或者请停止后再启动.");
//				log.warn("连接池[" + poolname + "]已经启动。无需再启动,或者请停止后再启动.");
//				return;
//			}
//				
//		}
//		Map<String,String> values = new HashMap<String,String>();
//		values.put("dbname", poolname);
//		values.put("jndiname", "");
//		values.put("driver", driver);
//		values.put("url", jdbcurl);
//		values.put("username", username);
//		values.put("password", password);
//		
//		values.put("readonly", readOnly);
//		values.put("validationquery", validationQuery);
//		
//		PoolManBootstrap.startFromTemplte(values);
//		TempConf tempConf = new TempConf();
//		tempConf.setPoolname(poolname);
//		tempConf.setDriver(driver);
//		tempConf.setJdbcurl(jdbcurl);
//		tempConf.setUsername(username);
//		tempConf.setPassword(password);
//		tempConf.setValidationQuery(validationQuery);
//		tempConf.setTxIsolationLevel("READ_COMMITTED");
//		tempConf.setJndiName("jndi-"+poolname);
//		tempConf.setInitialConnections(10);
//		tempConf.setMinimumSize(10);
//		tempConf.setMaximumSize(20);
//		tempConf.setUsepool(true);
//		tempConf.setExternal(false);
//		tempConf.setEncryptdbinfo(false);
//		tempConf.setShowsql(false);
//		tempConf.setQueryfetchsize(0);
//		startPool(tempConf);
		return startPool(poolname,driver,jdbcurl,username,password,validationQuery,0);
		/**
		 * startPool( poolname, driver, jdbcurl, username, password,
	    		 readOnly,
	    		 "READ_COMMITTED",
	    		validationQuery,
	    		"jndi-"+poolname,   
	    		10,
	    		10,
	    		20,
	    		true,
	    		false,
	    		null,false,encryptdbinfo
	    		);
	    		
	    		startPool(poolname,  driver,  jdbcurl,  username,  password,
	    		  readOnly,
	    		  txIsolationLevel,
	    		  validationQuery,
	    		  jndiName,   
	    		  initialConnections,
	    		  minimumSize,
	    		  maximumSize,
	    		  usepool,
	    		   external,
	    		  externaljndiName ,  showsql ,  encryptdbinfo  ,0    		
	    		);
		 */
//		startPool( poolname, driver, jdbcurl, username, password, (String)null, validationQuery,false);
	}
	
	
	public static boolean startNoPool(String poolname,String driver,String jdbcurl,String username,String password,String validationQuery)
	{
		return startNoPool(poolname,driver,jdbcurl,username,password,validationQuery,0);
	}
	public static boolean startNoPool(String poolname,String driver,String jdbcurl,String username,String password,String validationQuery,Integer fetchsize)
	{
//		JDBCPool pool = SQLUtil.getSQLManager().getPoolIfExist(poolname);
//		if(pool != null)
//		{
//			if(pool.getStatus().equals("start"))
//			{
////				throw new IllegalStateException("连接池[" + poolname + "]已经启动。无需再启动,或者请停止后再启动.");
//				log.warn("连接池[" + poolname + "]已经启动。无需再启动,或者请停止后再启动.");
//				return;
//			}
//				
//		}
//		Map<String,String> values = new HashMap<String,String>();
//		values.put("dbname", poolname);
//		values.put("jndiname", "");
//		values.put("driver", driver);
//		values.put("url", jdbcurl);
//		values.put("username", username);
//		values.put("password", password);
//		
//		values.put("readonly", readOnly);
//		values.put("validationquery", validationQuery);
//		
//		PoolManBootstrap.startFromTemplte(values);
		DBConf tempConf = new DBConf();
		tempConf.setPoolname(poolname);
		tempConf.setDriver(driver);
		tempConf.setJdbcurl(jdbcurl);
		tempConf.setUsername(username);
		tempConf.setPassword(password);
		tempConf.setReadOnly(null);
	
		//tempConf.setTxIsolationLevel("READ_COMMITTED");
		tempConf.setValidationQuery(validationQuery);
		tempConf.setJndiName("jndi-"+poolname);
		tempConf.setInitialConnections(10);
		tempConf.setMinimumSize(10);
		tempConf.setMaximumSize(20);
		tempConf.setUsepool(false);
		tempConf.setExternal(false);
		tempConf.setExternaljndiName(null);
		tempConf.setShowsql(false);
		tempConf.setEncryptdbinfo(false);
		tempConf.setQueryfetchsize(fetchsize);
		return startPool(tempConf);
//		startPool( poolname, driver, jdbcurl, username, password,
//	    		 null,
//	    		 "READ_COMMITTED",
//	    		validationQuery,
//	    		"jndi-"+poolname,   
//	    		10,
//	    		10,
//	    		20,
//	    		false,
//	    		false,
//	    		null,false,false
//	    		);
	}
	public static boolean startPool(String poolname,String driver,String jdbcurl,String username,String password,String readOnly,String validationQuery,boolean encryptdbinfo)
	{

//		startPool( poolname, driver, jdbcurl, username, password,
//	    		 readOnly,
//	    		 "READ_COMMITTED",
//	    		validationQuery,
//	    		"jndi-"+poolname,   
//	    		10,
//	    		10,
//	    		20,
//	    		true,
//	    		false,
//	    		null,false,encryptdbinfo
//	    		);

		DBConf tempConf = new DBConf();
		tempConf.setPoolname(poolname);
		tempConf.setDriver(driver);
		tempConf.setJdbcurl(jdbcurl);
		tempConf.setUsername(username);
		tempConf.setPassword(password);
		tempConf.setReadOnly(readOnly);
	
		//tempConf.setTxIsolationLevel("READ_COMMITTED");
		tempConf.setValidationQuery(validationQuery);
		tempConf.setJndiName("jndi-"+poolname);
		tempConf.setInitialConnections(10);
		tempConf.setMinimumSize(10);
		tempConf.setMaximumSize(20);
		tempConf.setUsepool(false);
		tempConf.setExternal(false);
		tempConf.setExternaljndiName(null);
		tempConf.setShowsql(false);
		tempConf.setEncryptdbinfo(encryptdbinfo);
		tempConf.setQueryfetchsize(null);
		return startPool(tempConf);
	}
	public static boolean startPool(String poolname,String driver,String jdbcurl,String username,String password,
    		String readOnly,
    		String txIsolationLevel,
    		String validationQuery,
    		String jndiName,   
    		int initialConnections,
    		int minimumSize,
    		int maximumSize,
    		boolean usepool,
    		boolean  external,
    		String externaljndiName ,boolean showsql ,boolean encryptdbinfo      		
    		)
	{
//		startPool(poolname,  driver,  jdbcurl,  username,  password,
//	    		  readOnly,
//	    		  txIsolationLevel,
//	    		  validationQuery,
//	    		  jndiName,   
//	    		  initialConnections,
//	    		  minimumSize,
//	    		  maximumSize,
//	    		  usepool,
//	    		   external,
//	    		  externaljndiName ,  showsql ,  encryptdbinfo  ,0    		
//	    		);
		DBConf tempConf = new DBConf();
		tempConf.setPoolname(poolname);
		tempConf.setDriver(driver);
		tempConf.setJdbcurl(jdbcurl);
		tempConf.setUsername(username);
		tempConf.setPassword(password);
		tempConf.setReadOnly(readOnly);
	
		tempConf.setTxIsolationLevel(txIsolationLevel);
		tempConf.setValidationQuery(validationQuery);
		tempConf.setJndiName(jndiName);
		tempConf.setInitialConnections(initialConnections);
		tempConf.setMinimumSize(minimumSize);
		tempConf.setMaximumSize(maximumSize);
		tempConf.setUsepool(usepool);
		tempConf.setExternal(external);
		tempConf.setExternaljndiName(externaljndiName);
		tempConf.setShowsql(showsql);
		tempConf.setEncryptdbinfo(encryptdbinfo);
		tempConf.setQueryfetchsize(null);
		return startPool(tempConf);
	}
	public static boolean startPool(String poolname,String driver,String jdbcurl,String username,String password,
								 String readOnly,
								 String txIsolationLevel,
								 String validationQuery,
								 String jndiName,
								 int initialConnections,
								 int minimumSize,
								 int maximumSize,
								 boolean usepool,
								 boolean  external,
								 String externaljndiName ,boolean showsql ,boolean encryptdbinfo  ,int queryfetchsize
	)
	{
		return startPool(  poolname,  driver,  jdbcurl,  username,  password,
				  readOnly,
				  txIsolationLevel,
				  validationQuery,
				  jndiName,
		  initialConnections,
		  minimumSize,
		  maximumSize,
		  usepool,
		   external,
		  externaljndiName ,  showsql ,  encryptdbinfo  ,  queryfetchsize,(String)null,(String)null	);
	}

	public static boolean startPool(String poolname,String driver,String jdbcurl,String username,String password,
    		String readOnly,
    		String txIsolationLevel,
    		String validationQuery,
    		String jndiName,   
    		int initialConnections,
    		int minimumSize,
    		int maximumSize,
    		boolean usepool,
    		boolean  external,
    		String externaljndiName ,boolean showsql ,boolean encryptdbinfo  ,Integer queryfetchsize  ,String dbtype,String dbAdaptor
    		)
	{
		JDBCPool pool = SQLUtil.getSQLManager().getPoolIfExist(poolname);
		if(pool != null)
		{
			if(pool.getStatus().equals("start"))
			{
				log.debug("连接池[" + poolname + "]已经启动。无需再启动,或者请停止后再启动.");
				return false;
			}
				
		}
		DBConf temConf = new DBConf();
		temConf.setPoolname(poolname);
		temConf.setDriver(driver);
		temConf.setJdbcurl(jdbcurl);
		temConf.setUsername(username);
		temConf.setPassword(password);
		temConf.setReadOnly(readOnly);
		temConf.setTxIsolationLevel(txIsolationLevel);
		temConf.setValidationQuery(validationQuery);
		temConf.setJndiName(jndiName);
		temConf.setInitialConnections(initialConnections);
		temConf.setMinimumSize(minimumSize);
		temConf.setMaximumSize(maximumSize);
		temConf.setUsepool(usepool);
		temConf.setExternal(external);
		temConf.setExternaljndiName(externaljndiName);
		temConf.setShowsql(showsql);
		temConf.setEncryptdbinfo(encryptdbinfo);
		temConf.setQueryfetchsize(queryfetchsize);
		temConf.setDbAdaptor(dbAdaptor);
		temConf.setDbtype(dbtype);
		return startPool(temConf);

	}

	/**
	 * 如果数据源不存在在创建并返回true，否则返回false
	 * @param temConf
	 * @return
	 */
	public static boolean startPool(DBConf temConf)
	{
		JDBCPool pool = SQLUtil.getSQLManager().getPoolIfExist(temConf.getPoolname());
		if(pool != null)
		{
			if(pool.getStatus().equals("start"))
			{
				log.debug("连接池[" + temConf.getPoolname() + "]已经启动。无需再启动,或者请停止后再启动.");
				return false;
			}
				
		}
		
		
		if(temConf.getTxIsolationLevel() == null)
			temConf.setTxIsolationLevel("");
		Map<String,Object> values = new HashMap<String,Object>();
		values.put("dbname", temConf.getPoolname());
		if(temConf.getJndiName() != null && !temConf.getJndiName().equals(""))
		{
			values.put("dbname_datasource_jndiname", temConf.getJndiName());

		}
		else
		{
			values.put("dbname_datasource_jndiname", "");
		}
		if(temConf.isEnablejta())
			values.put("enablejta", "true");
		else
			values.put("enablejta", "false");
		values.put("columnLableUpperCase",temConf.isColumnLableUpperCase());
		values.put("driver", temConf.getDriver());
		values.put("jdbcurl", temConf.getJdbcurl());
		if(temConf.getUsername() == null) 
			values.put("username", "");
		else
			values.put("username", temConf.getUsername());
			
		if(temConf.getPassword() == null) 
			values.put("password", "");
		else
			values.put("password", temConf.getPassword());
		boolean cachequerymetadata = true;
		values.put("cachequerymetadata", cachequerymetadata+"");
		if(temConf.getReadOnly() == null)			
			values.put("readOnly", "");
		else
			values.put("readOnly", temConf.getReadOnly());
		if(temConf.getTxIsolationLevel() != null )			
			values.put("txIsolationLevel", temConf.getTxIsolationLevel());
		else
			values.put("txIsolationLevel", "");
		
		if(temConf.getValidationQuery() != null )
			values.put("validationQuery", temConf.getValidationQuery());
		else
			values.put("validationQuery", "");
		
		if(temConf.getInitialConnections() >= 0)
			values.put("initialConnections", ""+temConf.getInitialConnections());
		else
			values.put("initialConnections", ""+2);
		if(temConf.getMinimumSize() >= 0)
			values.put("minimumSize", ""+temConf.getMinimumSize());
		else
			values.put("minimumSize", ""+2);
		if(temConf.getMaximumSize() > 0)
			values.put("maximumSize", ""+temConf.getMaximumSize());
		else
			values.put("maximumSize", ""+10);
		
		int maxWait = 60;
		if(maxWait > 0)
		{
			maxWait = maxWait * 1000;
			values.put("maxWait", ""+maxWait);
		}
		else
			values.put("maxWait", ""+60000);
		boolean maximumSoft = false;
		values.put("maximumSoft", ""+maximumSoft);
		values.put("usepool", temConf.isUsepool()+"");
		values.put("external", temConf.isExternal()+"");
		values.put("showsql", temConf.isShowsql()+"");
		values.put("encryptdbinfo", temConf.isEncryptdbinfo()+"");
		
		boolean testWhileidle = true;
		values.put("testWhileidle", testWhileidle+"");
		int shrinkBy = 5;
		values.put("shrinkBy", shrinkBy+"");
		
		int connectionTimeout = 36000000;
		values.put("connectionTimeout", connectionTimeout+"");
		int skimmerFrequency = 180000;
		values.put("skimmerFrequency", skimmerFrequency+"");
		boolean logAbandoned = true;
		values.put("logAbandoned", logAbandoned+"");
		/**
		 * Set max idle Times in seconds ,if exhaust this times the used connection object will be Abandoned removed if removeAbandoned is true.
		default value is 300 seconds.
		
		see removeAbandonedTimeout parameter in commons dbcp. 
		单位：秒
		 */
		int userTimeout = 300;
		values.put("userTimeout", userTimeout+"");
		boolean removeAbandoned = false;
		values.put("removeAbandoned", removeAbandoned+"");
		if(temConf.getExternaljndiName() != null && !temConf.getExternaljndiName().equals(""))
			values.put("externaljndiName", temConf.getExternaljndiName());
		if(temConf.getQueryfetchsize() != null)
			values.put("queryfetchsize", temConf.getQueryfetchsize());
		if(temConf.getDbAdaptor() != null){
			values.put("dbAdaptor",temConf.getDbAdaptor());
		}
		if(temConf.getDbtype() != null){
			values.put("dbtype",temConf.getDbtype());
		}
		if(temConf.getDbInfoEncryptClass() != null && !temConf.getDbInfoEncryptClass().equals("")){
			values.put("dbInfoEncryptClass",temConf.getDbInfoEncryptClass());
		}
		PoolManBootstrap.startFromTemplte(values);
		return true;
	}
	
	public static boolean startPool(String poolname,String driver,String jdbcurl,String username,String password,
    		String readOnly,
    		String txIsolationLevel,
    		String validationQuery,
    		String jndiName,   
    		int initialConnections,
    		int minimumSize,
    		int maximumSize,
    		boolean usepool,
    		boolean  external,
    		String externaljndiName ,boolean showsql       		
    		)
	{
		return startPool( poolname, driver, jdbcurl, username, password,
	    		 readOnly,
	    		 txIsolationLevel,
	    		 validationQuery,
	    		 jndiName,   
	    		 initialConnections,
	    		 minimumSize,
	    		 maximumSize,
	    		 usepool,
	    		  external,
	    		 externaljndiName , showsql ,false     		
	    		);
	}
	
	public JDBCPoolMetaData getJDBCPoolMetaData(String dbname)
	{
		try
		{
			JDBCPool pool = this.getPool(dbname);
			if(pool == null)
				return null;
			else
			{
				return pool.getJDBCPoolMetadata();
			}
		}
		catch(Exception e)
		{
			return null;
		}
	}

	
	
	
	


}


