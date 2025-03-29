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

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.Properties;

/**
 * 通过模板启动数据源配置参数
 * <p>Title: TempConf.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 * @Date 2015年10月8日 下午3:41:22
 * @author biaoping.yin
 * @version 1.0
 */
public class DBConf implements Serializable {
    public static final String BALANCE_RANDOM = "random";
    public static final String BALANCE_ROUNDBIN = "roundbin";
	private boolean testWhileidle = true;
	private boolean enableShutdownHook;
    /**
     * 申请链接超时时间，单位：毫秒
     */
    private int maxWait = 6000;
	/**
	 * 空闲链接回收时间，空闲时间超过指定的值时，将被回收  
     * 对应于minEvictableIdleTimeMillis 属性：
     * 	minEvictableIdleTimeMillis the minimum number of milliseconds 
     * 	an object can sit idle in the pool before it is eligable for evcition
     * 	单位：毫秒
     * 	use minEvictableIdleTimeMilli
	 */
    
    private int minEvictableIdleTimeMilli = 5000;


    /**
     * 空闲链接清理时间间隔，单位：毫秒
     * 对应属性：timeBetweenEvictionRunsMillis
     * 		the amount of time (in milliseconds) to sleep between examining idle objects for eviction 
     */
    private int timeBetweenEvictionRunsMillis = 180000;

    /**
     * numTestsPerEvictionRun 
     * 	the number of idle objects to 
     * 	examine per run within the idle object eviction thread (if any)
     *
     * 	每次回收的链接个数 
     */
    private int numTestsPerEvictionRun = 5;
	/**
	 * Set max idle Times in seconds ,if exhaust this times the used connection object will be Abandoned removed if removeAbandoned is true.
	 default value is 300 seconds.

	 see removeAbandonedTimeout parameter in commons dbcp.
	 单位：秒
	 */
	private int maxIdleTime = 300;
	private boolean removeAbandoned ;
	private boolean logAbandoned = true;
	private String poolname;
	private String driver;
    /**
     * see  com.frameworkset.orm.adapter.DBFactory
     *     public static final String  DBDB2400 = "as400";
     *     public static final String  DBDB2App = "db2app";
     *     public static final String  DBDB2Net = "db2net";
     *     public static final String  DBCloudscape = "cloudscape";
     *     public static final String  DBHypersonicSQL = "hypersonic";
     *     public static final String  DBInterbase = "interbase";
     *     public static final String  DBInstantDB =  "instantdb";
     *     public static final String  DBMSSQL = "mssql";
     *     public static final String  DBMMysql = "mysql";
     *     public static final String  DBMariaDB = "mariadb";
     *     public static final String  DBOracle = "oracle";
     *     public static final String  DBPostgres = "postgresql";
     *     public static final String  DBSapDB = "sapdb";
     *     public static final String  DBSybase = "sybase";
     *     public static final String  DBWeblogic = "weblogic";
     *     public static final String  DBAxion = "axion";
     *     public static final String  DBInformix = "informix";
     *     public static final String  DBOdbc = "odbc";
     *     public static final String  DBAccess = "msaccess";
     *
     *     public static final String  DBDerby =  "derby";
     *     public static final String  DBHive =  "hive";
     *
     *     public static final String  DBNone = "";
     *     public static final String  SQLITEX = "sqlitex";
     *     public static final String  DBTDEngine = "tdengine";
     *     public static final String  dm = "dm";
     *     public static final String  clickhouse = "clickhouse";
     *     public static final String  yandex_clickhouse = "yandex_clickhouse";
     *     public static final String  druid = "druid";
     *     public static final String  avatica = "avatica";
     */
	private String dbtype;
	private boolean enablejta;
	/**
	 * https://doc.bbossgroups.com/#/persistent/encrypt
	 * 同时如果想对账号、口令、url之间的任意两个组合加密的话，用户可以自己继承 com.frameworkset.common.poolman.security.BaseDBInfoEncrypt类，参考默认插件，实现相应的信息加密方法并配置到aop.properties中即可。
	 */
	private String dbInfoEncryptClass;
	private String dbAdaptor;
	private String jdbcurl;
	private String username;
	private String password;
	private String readOnly;
	private String txIsolationLevel;
	private String validationQuery;
	private String jndiName;   
	private int initialConnections;
	private int minimumSize;
	private int maximumSize;
	private boolean usepool;
	private boolean  external;
    private Properties connectionProperties;

    /**
     * 配置外部数据源，这时候需要指定dbtype，以便识别数据库类型
     */
    private DataSource datasource; 
    /**
     * 是否启用数据源负载均衡
     */
    private boolean enableBalance = false;


    /**
     * 负载均衡算法，目前支持两种类型：
     * DBConf.BALANCE_RANDOM  随机算法
     * DBConf.BALANCE_ROUNDBIN   轮询算法
     */
    private String balance = DBConf.BALANCE_RANDOM;
	public boolean isColumnLableUpperCase() {
		return columnLableUpperCase;
	}



    public DBConf setColumnLableUpperCase(boolean columnLableUpperCase) {
		this.columnLableUpperCase = columnLableUpperCase;
        return this;
	}

	private boolean columnLableUpperCase = false;
	private String externaljndiName ;
	private boolean showsql ;
	private boolean encryptdbinfo  ;
	private Integer queryfetchsize;
	public DBConf() {
		// TODO Auto-generated constructor stub
	}
	public String getPoolname() {
		return poolname;
	}
	public DBConf setPoolname(String poolname) {
		this.poolname = poolname;
        return this;
	}

    public DBConf setDbName(String dbName) {
        this.poolname = dbName;
        return this;
    }
	public String getDriver() {
		return driver;
	}
	public DBConf setDriver(String driver) {
		this.driver = driver;
        return this;
	}
	public String getJdbcurl() {
		return jdbcurl;
	}
	public DBConf setJdbcurl(String jdbcurl) {
		this.jdbcurl = jdbcurl;
        return this;
	}
	public String getUsername() {
		return username;
	}
	public DBConf setUsername(String username) {
		this.username = username;
        return this;
	}
	public String getPassword() {
		return password;
	}
	public DBConf setPassword(String password) {
		this.password = password;
        return this;
	}
	public String getReadOnly() {
		return readOnly;
	}
	public DBConf setReadOnly(String readOnly) {
		this.readOnly = readOnly;
        return this;
	}
	public String getTxIsolationLevel() {
		return txIsolationLevel;
	}
	public DBConf setTxIsolationLevel(String txIsolationLevel) {
		this.txIsolationLevel = txIsolationLevel;
        return this;
	}
	public String getValidationQuery() {
		return validationQuery;
	}
	public DBConf setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
        return this;
	}
	public String getJndiName() {
		return jndiName;
	}
	public DBConf setJndiName(String jndiName) {
		this.jndiName = jndiName;
        return this;
	}
	public int getInitialConnections() {
		return initialConnections;
	}
	public DBConf setInitialConnections(int initialConnections) {
		this.initialConnections = initialConnections;
        return this;
	}
	public int getMinimumSize() {
		return minimumSize;
	}
	public DBConf setMinimumSize(int minimumSize) {
		this.minimumSize = minimumSize;
        return this;
	}
	public int getMaximumSize() {
		return maximumSize;
	}
	public DBConf setMaximumSize(int maximumSize) {
		this.maximumSize = maximumSize;
        return this;
	}
	public boolean isUsepool() {
		return usepool;
	}
	public DBConf setUsepool(boolean usepool) {
		this.usepool = usepool;
        return this;
	}
	public boolean isExternal() {
		return external;
	}
	public DBConf setExternal(boolean external) {
		this.external = external;
        return this;
	}
	public String getExternaljndiName() {
		return externaljndiName;
	}
	public DBConf setExternaljndiName(String externaljndiName) {
		this.externaljndiName = externaljndiName;
        return this;
	}
	public boolean isShowsql() {
		return showsql;
	}
	public DBConf setShowsql(boolean showsql) {
		this.showsql = showsql;
        return this;
	}
	public boolean isEncryptdbinfo() {
		return encryptdbinfo;
	}
	public DBConf setEncryptdbinfo(boolean encryptdbinfo) {
		this.encryptdbinfo = encryptdbinfo;
        return this;
	}
	public Integer getQueryfetchsize() {
		return queryfetchsize;
	}
	public DBConf setQueryfetchsize(Integer queryfetchsize) {
		this.queryfetchsize = queryfetchsize;
        return this;
	}
    /**
     * see  com.frameworkset.orm.adapter.DBFactory
     *     public static final String  DBDB2400 = "as400";
     *     public static final String  DBDB2App = "db2app";
     *     public static final String  DBDB2Net = "db2net";
     *     public static final String  DBCloudscape = "cloudscape";
     *     public static final String  DBHypersonicSQL = "hypersonic";
     *     public static final String  DBInterbase = "interbase";
     *     public static final String  DBInstantDB =  "instantdb";
     *     public static final String  DBMSSQL = "mssql";
     *     public static final String  DBMMysql = "mysql";
     *     public static final String  DBMariaDB = "mariadb";
     *     public static final String  DBOracle = "oracle";
     *     public static final String  DBPostgres = "postgresql";
     *     public static final String  DBSapDB = "sapdb";
     *     public static final String  DBSybase = "sybase";
     *     public static final String  DBWeblogic = "weblogic";
     *     public static final String  DBAxion = "axion";
     *     public static final String  DBInformix = "informix";
     *     public static final String  DBOdbc = "odbc";
     *     public static final String  DBAccess = "msaccess";
     *
     *     public static final String  DBDerby =  "derby";
     *     public static final String  DBHive =  "hive";
     *
     *     public static final String  DBNone = "";
     *     public static final String  SQLITEX = "sqlitex";
     *     public static final String  DBTDEngine = "tdengine";
     *     public static final String  dm = "dm";
     *     public static final String  clickhouse = "clickhouse";
     *     public static final String  yandex_clickhouse = "yandex_clickhouse";
     *     public static final String  druid = "druid";
     *     public static final String  avatica = "avatica";
     */
	public String getDbtype() {
		return dbtype;
	}

    /**
     * see  com.frameworkset.orm.adapter.DBFactory
     *     public static final String  DBDB2400 = "as400";
     *     public static final String  DBDB2App = "db2app";
     *     public static final String  DBDB2Net = "db2net";
     *     public static final String  DBCloudscape = "cloudscape";
     *     public static final String  DBHypersonicSQL = "hypersonic";
     *     public static final String  DBInterbase = "interbase";
     *     public static final String  DBInstantDB =  "instantdb";
     *     public static final String  DBMSSQL = "mssql";
     *     public static final String  DBMMysql = "mysql";
     *     public static final String  DBMariaDB = "mariadb";
     *     public static final String  DBOracle = "oracle";
     *     public static final String  DBPostgres = "postgresql";
     *     public static final String  DBSapDB = "sapdb";
     *     public static final String  DBSybase = "sybase";
     *     public static final String  DBWeblogic = "weblogic";
     *     public static final String  DBAxion = "axion";
     *     public static final String  DBInformix = "informix";
     *     public static final String  DBOdbc = "odbc";
     *     public static final String  DBAccess = "msaccess";
     *
     *     public static final String  DBDerby =  "derby";
     *     public static final String  DBHive =  "hive";
     *
     *     public static final String  DBNone = "";
     *     public static final String  SQLITEX = "sqlitex";
     *     public static final String  DBTDEngine = "tdengine";
     *     public static final String  dm = "dm";
     *     public static final String  clickhouse = "clickhouse";
     *     public static final String  yandex_clickhouse = "yandex_clickhouse";
     *     public static final String  druid = "druid";
     *     public static final String  avatica = "avatica";
     */
	public DBConf setDbtype(String dbtype) {
		this.dbtype = dbtype;
        return this;
	}

	public String getDbAdaptor() {
		return dbAdaptor;
	}

	public DBConf setDbAdaptor(String dbAdaptor) {
		this.dbAdaptor = dbAdaptor;
        return this;
	}

	public String getDbInfoEncryptClass() {
		return dbInfoEncryptClass;
	}

	/**
	 * https://doc.bbossgroups.com/#/persistent/encrypt
	 * 同时如果想对账号、口令、url之间的任意两个组合加密的话，用户可以自己继承 com.frameworkset.common.poolman.security.BaseDBInfoEncrypt类，参考默认插件，实现相应的信息加密方法并配置到aop.properties中即可。
	 */
	public DBConf setDbInfoEncryptClass(String dbInfoEncryptClass) {
		this.dbInfoEncryptClass = dbInfoEncryptClass;
        return this;
	}

	public boolean isEnablejta() {
		return enablejta;
	}

	public DBConf setEnablejta(boolean enablejta) {
		this.enablejta = enablejta;
        return this;
	}

	public boolean isTestWhileidle() {
		return testWhileidle;
	}

	public DBConf setTestWhileidle(boolean testWhileidle) {
		this.testWhileidle = testWhileidle;
        return this;
	}

	public int getMaxWait() {
		return maxWait;
	}

	public DBConf setMaxWait(int maxWait) {
		this.maxWait = maxWait;
        return this;
	}

    @Deprecated
	public int getConnectionTimeout() {
		return this.minEvictableIdleTimeMilli;
	}

    @Deprecated
    /**
     * user setMinEvictableIdleTimeMilli
     */
	public DBConf setConnectionTimeout(int connectionTimeout) {
		this.minEvictableIdleTimeMilli = connectionTimeout;
        return this;
	}

	public int getMaxIdleTime() {
		return maxIdleTime;
	}

	public DBConf setMaxIdleTime(int maxIdleTime) {
		this.maxIdleTime = maxIdleTime;
        return this;
	}

	public boolean isRemoveAbandoned() {
		return removeAbandoned;
	}

	public DBConf setRemoveAbandoned(boolean removeAbandoned) {
		this.removeAbandoned = removeAbandoned;
        return this;
	}

	public boolean isLogAbandoned() {
		return logAbandoned;
	}

	public DBConf setLogAbandoned(boolean logAbandoned) {
		this.logAbandoned = logAbandoned;
        return this;
	}

	public boolean isEnableShutdownHook() {
		return enableShutdownHook;
	}

	public DBConf setEnableShutdownHook(boolean enableShutdownHook) {
		this.enableShutdownHook = enableShutdownHook;
        return this;
	}
    public DBConf setConnectionProperties(Properties connectionProperties) {
        this.connectionProperties = connectionProperties;
        return this;
    }

    public Properties getConnectionProperties() {
        return connectionProperties;
    }
    public DBConf connectionProperty(String name,Object value){
        if(connectionProperties == null)
            connectionProperties = new Properties();
        connectionProperties.put(name,value);
        return this;
    }

    public String getBalance() {
        return balance;
    }

    /**
     * 负载均衡算法，目前支持两种类型：
     * DBConf.BALANCE_RANDOM  随机算法
     * DBConf.BALANCE_ROUNDBIN   轮询算法 
     * @param balance
     */
    public DBConf setBalance(String balance) {
        this.balance = balance;
        return this;
    }

    public boolean isEnableBalance() {
        return enableBalance;
    }

    /**
     * 是否启用数据源负载均衡 true 启用，false 不启用（默认值）
     * 针对Clickhouse-native-jdbc等没有自带负载均衡的驱动器起作用
     * @param enableBalance
     */
    public DBConf setEnableBalance(boolean enableBalance) {
        this.enableBalance = enableBalance;
        return this;
    }
    public DataSource getDatasource() {
        return datasource;
    }

    public DBConf setDatasource(DataSource datasource) {
        this.datasource = datasource;
        return this;
    }

    public int getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public DBConf setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
        return this;
    }

 

    public int getNumTestsPerEvictionRun() {
        return numTestsPerEvictionRun;
    }

    public DBConf setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
        return this;
    }

    public int getMinEvictableIdleTimeMilli() {
        return minEvictableIdleTimeMilli;
    }

    public DBConf setMinEvictableIdleTimeMilli(int minEvictableIdleTimeMilli) {
        this.minEvictableIdleTimeMilli = minEvictableIdleTimeMilli;
        return this;
    }
}
