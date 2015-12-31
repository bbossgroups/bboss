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

import java.io.Serializable;
import java.util.Map;

import com.frameworkset.common.poolman.PoolManConstants;
import com.frameworkset.util.StringUtil;

public class JDBCPoolMetaData implements Serializable{

	/* POOL ATTRIBUTES (set to default beforehand) */

    protected String poolname;
    
    transient JDBCPoolMetaData extenalInfo = null;

    private int initialObjects = PoolManConstants.DEFAULT_INITIAL_SIZE;
    private int minimumSize = PoolManConstants.DEFAULT_MIN_SIZE;
    private int maximumSize = PoolManConstants.DEFAULT_MAX_SIZE;
    private int objectTimeout = PoolManConstants.DEFAULT_TIMEOUT;
    private int userTimeout = PoolManConstants.DEFAULT_USERTIMEOUT;
    private int skimmerFrequency = PoolManConstants.DEFAULT_SKIMMER_SLEEP;
    private int shrinkBy = PoolManConstants.DEFAULT_SHRINKBY;
    private boolean emergencyCreates = PoolManConstants.DEFAULT_EMERGENCY_CREATES;
    private String maxWait = "30";//30秒  
    private String datasourceFile ;
    /**自定义自增序列函数名称，和tableinfo中的sequence表主键配置结合使用*/
    private String seqfunction;
//    private boolean neadGetGenerateKeys;
    
    private String databaseProductName;
    private String driverName;
    private String databaseProductVersion;
    private String driverVersion;
    private Map<String,Object> datasourceParameters ; 
    
//    addDbMetaDataEntry(dbMetaData, "probe.jsp.dataSourceTest.dbMetaData.dbProdName", md.getDatabaseProductName());
//    addDbMetaDataEntry(dbMetaData, "probe.jsp.dataSourceTest.dbMetaData.dbProdVersion", md.getDatabaseProductVersion());
//    addDbMetaDataEntry(dbMetaData, "probe.jsp.dataSourceTest.dbMetaData.jdbcDriverName", md.getDriverName());
//    addDbMetaDataEntry(dbMetaData, "probe.jsp.dataSourceTest.dbMetaData.jdbcDriverVersion", md.getDriverVersion());
    
    private boolean usepool = true;
    private String logfile;
    private boolean debug = false;
    /* PHYSICAL CONNECTION ATTRIBUTES */
    private String driver;
    private String URL;
    private String username;
    private String password;

    private boolean nativeResults = false;

    /* POOL BEHAVIOR ATTRIBUTES */
    private String validationQuery;
    private String initialPoolSQL;
    private String initialConnectionSQL;
    private boolean external = PoolManConstants.EXTERNAL;
    
    private String externaljndiName;
    
    private boolean removeOnExceptions = PoolManConstants.DEFAULT_REMOVE_ON_EXC;
    private boolean poolingPreparedStatements = PoolManConstants.DEFAULT_POOL_PREP_STATEMENTS;
    private int maxOpenPreparedStatements = PoolManConstants.maxOpenPreparedStatements;
    

    /* TX ATTRIBUTES */
    private Integer transactionIsolationLevel = null;
    private int transactionTimeout = PoolManConstants.DEFAULT_USERTIMEOUT;

    /* QUERY CACHE ATTRIBUTES */
    private boolean cacheEnabled = PoolManConstants.DEFAULT_CACHE_ENABLED;
    private int cacheSize = PoolManConstants.DEFAULT_CACHE_SIZE;
    private int cacheRefreshInterval = PoolManConstants.DEFAULT_CACHE_REFRESH;
    

    /* DATASOURCE ATTRIBUTES */
    private String JNDIName;
    private String interceptor = "com.frameworkset.common.poolman.interceptor.DummyInterceptor";
    private String idGenerator;
    
    private String jndiclass;
    private String jndiuser;
    private String jndipassword;
    private boolean RETURN_GENERATED_KEYS;
    /**
     * 是否对数据库信息进行加密：
     * 账号信息
     * url信息
     * 口令信息
     */
    private boolean encryptdbinfo = false;
    private int queryfetchsize = 0;
    public String getJndiclass() {
		return jndiclass;
	}

	public void setJndiclass(String jndiclass) {
		this.jndiclass = jndiclass;
	}

	private String jndiurl;
    /**
	 * @return the interceptor
	 */
	public String getInterceptor() {
		return interceptor;
	}

	/**
	 * @param interceptor the interceptor to set
	 */
	public void setInterceptor(String interceptor) {
		this.interceptor = interceptor;
	}

    /**
     * 系统提供的缺省数据库表的主键生成机制可以通过两种方式生成数据库主键
     * 1.auto:自动生成
     * 2.composite：查询表的最大主键
     */
    private String keygenerate = PoolManConstants.DEFAULT_KEY_GENERATE;
    

    /**
     * 数据库类型
     */
    private String dbtype = null;
    
    /**
     * 是否加载元数据
     */
    private String loadmetadata = "false";
    
    /**
     * 是否自动产生主键
     */
    private boolean autoprimarykey = false;
    private boolean showsql = false;
    /**
     * 是否缓冲查询列表元数据
     */
    private boolean cachequerymetadata = true;
    
    /**
     * 标识分页查询是否使用高效查询，缺省为true
     * 为false时将不会执行高效查询
     */
    private boolean robotquery = true;
    
    /**
     * 检测空闲链接处理时，是否对空闲链接进行有效性检查控制开关
     * true-检查，都检查到有无效链接时，直接销毁无效链接
     * false-不检查，缺省值
     */
    private boolean testWhileidle = false; 
    
	/**
	 * 当链接超时时是否释放链接
	 */
	private String removeAbandoned = "false";
	
	/**
	 * 当链接超时释放链接时，是否打印后台日志
	 */
	private boolean logAbandoned = false;
	
	/**
	 * 设定链接是否是readOnly属性
	 */
	private Boolean readOnly = null;
	/**
	 * 是否启用jta datasource，如果启用将在jndi context中注册一个
	 * TXDatasource
	 * jta datasource的jndiname为 jndiName属性指定的值
	 * 默认为不启用，该属性在托管第三方数据源时有用
	 * 当enablejta == true时，必须在poolman.xml文件中指定jndiName属性
	 */
	private boolean enablejta =  false;
    
    
    
   

    /* PHYSICAL CONNECTION METHODS */
	public static final String[] driver_names = new String[]{"driverClass","driverClassName","driver","driver-class","driverName"};
    public String getDriver() {
    	if(!StringUtil.isEmpty(this.driver))
    	{
    		return this.driver;
    	}
    	else if(this.datasourceParameters != null && this.datasourceParameters.size() > 0)
    	{
    		String dr = null;
    		for(int i = 0; i < driver_names.length; i ++)
    		{
    			dr = (String)this.datasourceParameters.get(driver_names[i]);
    			if(!StringUtil.isEmpty(dr))
    			{
    				this.driver = dr;
    				break;
    			}
    		}
    		return this.driver;
    	}
    	else
    	{
    		return this.driver;
    	}
    	
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getURL() {
        return this.URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getUserName() {
        return this.username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /* DATASOURCE METHODS */

    public String getDbname() {
        return getName();
    }

    public void setDbname(String n) {
        setName(n);
    }

    public String getJNDIName() {
        return this.JNDIName;
    }

    public void setJNDIName(String n) {
        this.JNDIName = n;
    }

    public boolean isNativeResults() {
        return this.nativeResults;
    }

    public void setNativeResults(boolean b) {
        this.nativeResults = b;
    }

    /* POOL BEHAVIOR METHODS */
    public boolean isPoolPreparedStatements() {
        return poolingPreparedStatements;
    }

    public void setPoolPreparedStatements(boolean poolingPreparedStatements) {
        this.poolingPreparedStatements = poolingPreparedStatements;
    }

    public String getValidationQuery() {
        return this.validationQuery;
    }

    public void setValidationQuery(String sql) {
        this.validationQuery = sql;
    }

    public String getInitialPoolSQL() {
        return this.initialPoolSQL;
    }

    public void setInitialPoolSQL(String sql) {
        this.initialPoolSQL = sql;
    }

    public String getInitialConnectionSQL() {
        return this.initialConnectionSQL;
    }

    public void setInitialConnectionSQL(String sql) {
        this.initialConnectionSQL = sql;
    }

    public boolean isRemoveOnExceptions() {
        return this.removeOnExceptions;
    }

    public void setRemoveOnExceptions(boolean b) {
        this.removeOnExceptions = b;
    }

    /* POOLED CONNECTION METHODS */

    public int getInitialConnections() {
        return getInitialObjects();
    }

    public void setInitialConnections(int n) {
        setInitialObjects(n);
    }

    public int getConnectionTimeout() {
        return getObjectTimeout();
    }

    public void setConnectionTimeout(int n) {
        setObjectTimeout(n);
    }

    /* TX METHODS */

    public int getTransactionTimeout() {
        return this.transactionTimeout;
    }

    public void setTransactionTimeout(int n) {
        this.transactionTimeout = n;
    }

    public Integer getIsolationLevel() {
        return this.transactionIsolationLevel;
    }

    public void setIsolationLevel(Integer n) {
        this.transactionIsolationLevel = n;
    }

    public String getTxIsolationLevel() {
        return convertIsoToString(getIsolationLevel());
    }

    public void setTxIsolationLevel(String s) {
        setIsolationLevel(convertIsoToInt(s));
    }

    private int convertIsoToInt(String s) {
    	if(s == null || s.equals(""))
    		
    		return -100;

        int n = PoolManConstants.DEFAULT_ISO_LEVEL;

        s = s.toUpperCase().trim();

        if (s.equals("NONE"))
            n = java.sql.Connection.TRANSACTION_NONE;
        else if (s.equals("READ_COMMITTED"))
            n = java.sql.Connection.TRANSACTION_READ_COMMITTED;
        else if (s.equals("READ_UNCOMMITTED"))
            n = java.sql.Connection.TRANSACTION_READ_UNCOMMITTED;
        else if (s.equals("REPEATABLE_READ"))
            n = java.sql.Connection.TRANSACTION_REPEATABLE_READ;
        else if (s.equals("SERIALIZABLE"))
            n = java.sql.Connection.TRANSACTION_SERIALIZABLE;
        else
            System.out.println("Unrecognized isolation level " + s +
                               " using default setting of " +
                               convertIsoToString(n));

        return n;

    }

    private String convertIsoToString(Integer n) {

    	if(n == null)
    		return null;
        String result = null;

        switch (n) {
            case java.sql.Connection.TRANSACTION_NONE:
                result = "NONE";
                break;
            case java.sql.Connection.TRANSACTION_READ_COMMITTED:
                result = "READ_COMMITTED";
                break;
            case java.sql.Connection.TRANSACTION_READ_UNCOMMITTED:
                result = "READ_UNCOMMITTED";
                break;
            case java.sql.Connection.TRANSACTION_REPEATABLE_READ:
                result = "REPEATABLE_READ";
                break;
            case java.sql.Connection.TRANSACTION_SERIALIZABLE:
                result = "SERIALIZABLE";
                break;
            default:
                break;
        }

        return result;

    }

    /* QUERY CACHE METHODS */

    public boolean isCacheEnabled() {
        return this.cacheEnabled;
    }

    public void setCacheEnabled(
            boolean b) {
        this.cacheEnabled = b;
    }

    public int getCacheSize() {
        return this.cacheSize;
    }

    public void setCacheSize(
            int n) {
        this.cacheSize = n;
    }

    public int getCacheRefreshInterval() {
        return this.cacheRefreshInterval;
    }

    public void setCacheRefreshInterval(
            int seconds) {
        this.cacheRefreshInterval = seconds;
    }

    /**
     * @return Returns the keygenerate.
     */
    public String getKeygenerate() {
        return keygenerate;
    }

    public String getDbtype() {
        return dbtype;
    }

    /**
     * @param keygenerate The keygenerate to set.
     */
    public void setKeygenerate(String keygenerate) {
        this.keygenerate = keygenerate;
    }

    public void setDbtype(String dbtype) {
        this.dbtype = dbtype;
    }

	public String getLoadmetadata() {
		return loadmetadata;
	}

	public void setLoadmetadata(String loadmetadata) {
		this.loadmetadata = loadmetadata;
	}
	



    /* POOL ID METHODS */

    public String getName() {
        return this.poolname;
    }

    public void setName(String name) {
        this.poolname = name;
    }

    /* POOL PROPERTY METHODS */

    public int getInitialObjects() {
        return this.initialObjects;
    }

    public void setInitialObjects(int n) {
        this.initialObjects = n;
    }

    public int getMinimumSize() {
        return this.minimumSize;
    }

    public void setMinimumSize(int n) {
        this.minimumSize = n;
    }

    public int getMaximumSize() {
        return this.maximumSize;
    }

    public void setMaximumSize(int n) {
        this.maximumSize = n;
    }

    public int getObjectTimeout() {
        return this.objectTimeout;
    }

    public void setObjectTimeout(int n) {
        this.objectTimeout = n;
    }

    public int getUserTimeout() {
        return this.userTimeout;
    }

    public void setUserTimeout(int n) {
        this.userTimeout = n;
    }

    public int getSkimmerFrequency() {
        return this.skimmerFrequency;
    }

    public void setSkimmerFrequency(int n) {
        this.skimmerFrequency = n;
    }

    public int getShrinkBy() {
        return this.shrinkBy;
    }

    public void setShrinkBy(int n) {
        this.shrinkBy = n;
    }

    public String getLogFile() {
        return this.logfile;
    }

    public void setLogFile(String filename) {
        this.logfile = filename;
    }

    public boolean isDebugging() {
        return this.debug;
    }

    public void setDebugging(boolean b) {
        this.debug = b;
    }

    public boolean isMaximumSoft() {
        return this.emergencyCreates;
    }

    public void setMaximumSoft(boolean b) {
        this.emergencyCreates = b;
    }

	public boolean isExternal() {
		return external;
	}

	public void setExternal(boolean external) {
		this.external = external;
	}

	public String getExternaljndiName() {
		return externaljndiName;
	}

	public void setExternaljndiName(String externaljndiName) {
		this.externaljndiName = externaljndiName;
	}
	
	public String getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(String maxWait) {
		this.maxWait = maxWait;
	}
	



	public String getRemoveAbandoned() {
		return removeAbandoned;
	}

	public void setRemoveAbandoned(String removeAbandoned) {
		this.removeAbandoned = removeAbandoned;
	}

	public boolean isRobotquery() {
		return robotquery;
	}

	public void setRobotquery(boolean robotquery) {
		this.robotquery = robotquery;
	}

	public boolean isTestWhileidle() {
		return testWhileidle;
	}

	public void setTestWhileidle(boolean testWhileidle) {
		this.testWhileidle = testWhileidle;
	}

	public boolean isLogAbandoned() {
		return logAbandoned;
	}

	public void setLogAbandoned(boolean logAbandoned) {
		this.logAbandoned = logAbandoned;
	}

	public Boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}
	/**
	 * 是否自动同步sequence
	 */
	boolean synsequence = false;
	public boolean synsequence() {
		
		return synsequence;
	}
	
	public void setSynsequence(boolean synsequence)
	{
		this.synsequence = synsequence;
	}

	public int getMaxOpenPreparedStatements() {
		return maxOpenPreparedStatements;
	}

	public void setMaxOpenPreparedStatements(int maxOpenPreparedStatements) {
		this.maxOpenPreparedStatements = maxOpenPreparedStatements;
	}

	public boolean getAutoprimarykey() {
		return autoprimarykey;
	}

	public void setAutoprimarykey(boolean autoprimarykey) {
		this.autoprimarykey = autoprimarykey;
	}

	public boolean isShowsql() {
		return showsql;
	}

	public void setShowsql(boolean showsql) {
		this.showsql = showsql;
	}

	public boolean cachequerymetadata() {
		return cachequerymetadata;
	}

	public void setCachequerymetadata(boolean cachequerymetadata) {
		this.cachequerymetadata = cachequerymetadata;
	}

	

	/**
	 * @param extenalInfo the extenalInfo to set
	 */
	public void setExtenalInfo(JDBCPoolMetaData extenalInfo) {
		this.extenalInfo = extenalInfo;
		if(extenalInfo != null)
		{
		    setDriver(extenalInfo.getDriver());		    

		    setURL(extenalInfo.getURL()) ;

		    

		    setUserName(extenalInfo.getUserName());

		    
		    setPassword(extenalInfo.getPassword()) ;

		    
		    
		    

		    setNativeResults(extenalInfo.isNativeResults());

		    

		    setPoolPreparedStatements(extenalInfo.isPoolPreparedStatements()) ;
		    

		    setValidationQuery(extenalInfo.getValidationQuery());
		    

		    setInitialPoolSQL(extenalInfo.getInitialPoolSQL());

		    

		    setInitialConnectionSQL(extenalInfo.getInitialConnectionSQL()) ;

		    
		    setRemoveOnExceptions(extenalInfo.isRemoveOnExceptions());

		    
		    setInitialConnections(extenalInfo.getInitialConnections());

		    setConnectionTimeout(extenalInfo.getConnectionTimeout());
		    setTransactionTimeout(extenalInfo.getTransactionTimeout());

		    setIsolationLevel(extenalInfo.getIsolationLevel());


		    setTxIsolationLevel(extenalInfo.getTxIsolationLevel());

		    
		    
		    setCacheEnabled(extenalInfo.isCacheEnabled());
		    setCacheSize(extenalInfo.getCacheSize());
		    setCacheRefreshInterval(extenalInfo.getCacheRefreshInterval());

		    setKeygenerate(extenalInfo.getKeygenerate());

		    setDbtype(extenalInfo.getDbtype());

			setLoadmetadata(extenalInfo.getLoadmetadata());
			


		    setInitialObjects(extenalInfo.getInitialObjects());
		    setMinimumSize(extenalInfo.getMinimumSize());


		    setMaximumSize(extenalInfo.getMaximumSize());


		    setObjectTimeout(extenalInfo.getObjectTimeout())		    ;
		    setUserTimeout(extenalInfo.getUserTimeout());

		    

		    setSkimmerFrequency(extenalInfo.getSkimmerFrequency());

		    setShrinkBy(extenalInfo.getShrinkBy());


		    
		    

		    
		    setMaximumSoft(extenalInfo.isMaximumSoft());			

			

			
			
			
			setMaxWait(extenalInfo.getMaxWait());
			



			setRemoveAbandoned(extenalInfo.getRemoveAbandoned());

			
			setRobotquery(extenalInfo.isRobotquery());

			
			setTestWhileidle(extenalInfo.isTestWhileidle());

			

			setLogAbandoned(extenalInfo.isLogAbandoned());

			

			setReadOnly(extenalInfo.isReadOnly());
			
			setSynsequence(extenalInfo.synsequence());
			setMaxOpenPreparedStatements(extenalInfo.getMaxOpenPreparedStatements());


			setAutoprimarykey(extenalInfo.getAutoprimarykey()) ;			

			setCachequerymetadata(extenalInfo.cachequerymetadata());
			
			setDatabaseProductName(extenalInfo.getDatabaseProductName());
			
			setDatabaseProductVersion(extenalInfo.getDatabaseProductVersion());
			
			setDriverName(extenalInfo.getDriverName());
			
			setDriverVersion(extenalInfo.getDriverVersion());
			this.setJndiclass(extenalInfo.getJndiclass());
			this.setJndiurl(extenalInfo.getJndiurl());
			this.setJndiuser(extenalInfo.getJndiuser());
			this.setJndipassword(extenalInfo.getJndipassword());
			this.setUsepool(extenalInfo.isUsepool());
			this.setEncryptdbinfo(extenalInfo.isEncryptdbinfo());
			this.setEnablejta(extenalInfo.isEnablejta());
			this.setDatasourceFile(extenalInfo.getDatasourceFile());
			this.setDatasourceParameters(getDatasourceParameters());
			this.setSeqfunction(extenalInfo.getSeqfunction());
			this.setRETURN_GENERATED_KEYS(extenalInfo.getRETURN_GENERATED_KEYS());
			this.setQueryfetchsize(extenalInfo.getQueryfetchsize());
//			this.setNeadGetGenerateKeys(extenalInfo.isNeadGetGenerateKeys());
		}
	}

	/**
	 * @return the databaseProductName
	 */
	public String getDatabaseProductName() {
		return databaseProductName;
	}

	/**
	 * @param databaseProductName the databaseProductName to set
	 */
	public void setDatabaseProductName(String databaseProductName) {
		this.databaseProductName = databaseProductName;
	}

	/**
	 * @return the driverName
	 */
	public String getDriverName() {
		return driverName;
	}

	/**
	 * @param driverName the driverName to set
	 */
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	/**
	 * @return the databaseProductVersion
	 */
	public String getDatabaseProductVersion() {
		return databaseProductVersion;
	}

	/**
	 * @param databaseProductVersion the databaseProductVersion to set
	 */
	public void setDatabaseProductVersion(String databaseProductVersion) {
		this.databaseProductVersion = databaseProductVersion;
	}

	/**
	 * @return the driverVersion
	 */
	public String getDriverVersion() {
		return driverVersion;
	}

	/**
	 * @param driverVersion the driverVersion to set
	 */
	public void setDriverVersion(String driverVersion) {
		this.driverVersion = driverVersion;
	}

	/**
	 * @return the usepool
	 */
	public boolean isUsepool() {
		return usepool;
	}

	/**
	 * @param usepool the usepool to set
	 */
	public void setUsepool(boolean usepool) {
		this.usepool = usepool;
	}

	public String getJndiurl() {
		return jndiurl;
	}

	public void setJndiurl(String jndiurl) {
		this.jndiurl = jndiurl;
	}

	public String getJndiuser() {
		return jndiuser;
	}

	public void setJndiuser(String jndiuser) {
		this.jndiuser = jndiuser;
	}

	public String getJndipassword() {
		return jndipassword;
	}

	public void setJndipassword(String jndipassword) {
		this.jndipassword = jndipassword;
	}

	public boolean isEncryptdbinfo() {
		return encryptdbinfo;
	}

	public void setEncryptdbinfo(boolean encryptdbinfo) {
		this.encryptdbinfo = encryptdbinfo;
	}

	public boolean isEnablejta() {
		return enablejta;
	}

	public void setEnablejta(boolean enablejta) {
		this.enablejta = enablejta;
	}

	public String getDatasourceFile() {
		return datasourceFile;
	}

	public void setDatasourceFile(String datasourceFile) {
		this.datasourceFile = datasourceFile;
	}

	public Map<String, Object> getDatasourceParameters() {
		return datasourceParameters;
	}

	public void setDatasourceParameters(Map<String, Object> datasourceParameters) {
		this.datasourceParameters = datasourceParameters;
	}
	
	public void initDatasourceParameters()
	{
		if(!StringUtil.isEmpty(this.datasourceFile ))
		{
			this.datasourceParameters = DatasourceUtil.getDataSourceParameters(this.datasourceFile);
		}
	}

	public String getSeqfunction() {
		return seqfunction;
	}

	public void setSeqfunction(String seqfunction) {
		this.seqfunction = seqfunction;
	}

	public String getIdGenerator() {
		return idGenerator;
	}

	public void setIdGenerator(String idGenerator) {
		this.idGenerator = idGenerator;
	}

	public boolean getRETURN_GENERATED_KEYS() {
		return RETURN_GENERATED_KEYS;
	}

	public void setRETURN_GENERATED_KEYS(boolean rETURN_GENERATED_KEYS) {
		this.RETURN_GENERATED_KEYS = rETURN_GENERATED_KEYS;
	}

	public int getQueryfetchsize() {
		return queryfetchsize;
	}

	public void setQueryfetchsize(int queryfetchsize) {
		this.queryfetchsize = queryfetchsize;
	}

//	public boolean isNeadGetGenerateKeys() {
//		return neadGetGenerateKeys;
//	}
//
//	public void setNeadGetGenerateKeys(boolean neadGetGenerateKeys) {
//		this.neadGetGenerateKeys = neadGetGenerateKeys;
//	}
}
