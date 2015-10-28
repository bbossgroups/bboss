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
package com.frameworkset.common.poolman;

import java.io.Serializable;

public interface PoolManConstants extends Serializable{

    /* RELEASE CONSTANTS */
    public static final String RELEASE_NAME = "PoolMan Resource Management Library";
    public static final String RELEASE_MAJOR_VERSION = "2";
    public static final String RELEASE_MINOR_VERSION = "1";
    public static final String RELEASE_FULL_NAME = RELEASE_NAME + " v" + RELEASE_MAJOR_VERSION +
            "." + RELEASE_MINOR_VERSION;

    /* DEFAULT CONFIG VALUES */
    public static final String XML_CONFIG_FILE = "poolman.xml";
    public static final String XML_CONFIG_FILE_TEMPLATE = "poolman_template.xml";
    //public static final String XML_CONFIG_FILE = "ftp://D:/project/sbgtoa/oa/WEB-INF/poolman.xml";
    public static final String PROPS_CONFIG_FILE = "poolman.props";
    public static final String OLDPROPS_CONFIG_FILE = "pool.props";
    public static final String DATASOURCE_SVC_CLASSNAME = "com.frameworkset.common.poolman.management.DataSourceService";
    public static final String GENERIC_SVC_CLASSNAME = "com.frameworkset.common.poolman.management.GenericPoolService";

    /* Whether or not to use JMX for configuration and management of all pools. */
    public static final boolean DEFAULT_USE_JMX = false;


    /* DEFAULT OBJECT POOL VALUES */
    public static final int DEFAULT_INITIAL_SIZE = 1;             // 1 object
    public static final int DEFAULT_TIMEOUT = 1200 ;               // 20 minutes
    public static final int DEFAULT_SKIMMER_SLEEP = 60 ;	      // 60秒
    public static final int DEFAULT_MAX_SIZE = Integer.MAX_VALUE; // System's max
    public static final int DEFAULT_MIN_SIZE = 0;                 // shrinks to 0
    public static final int DEFAULT_USERTIMEOUT = 60;	          // 60 seconds
    public static final int DEFAULT_SHRINKBY = 5;                 // Shrink 5 objects per GC cycle

    public static final int DEFAULT_ISO_LEVEL = java.sql.Connection.TRANSACTION_READ_COMMITTED;

    /* Create objects even if the max has been reached if there are no more
     * available in the pool. Let the GC PoolSkimmer automatically shrink
     * the pool back to acceptable size later.
     */
    public static final boolean DEFAULT_EMERGENCY_CREATES = true;

    /* DEFAULT JDBC POOL VALUES */
    public static final boolean DEFAULT_POOL_PREP_STATEMENTS= false;  // pool prepared statements by default
    public static final boolean DEFAULT_REMOVE_ON_EXC = false;       // exceptions don't kill cons
    public static final boolean DEFAULT_CACHE_ENABLED = false;       // no caching by default
    public static final int DEFAULT_CACHE_SIZE = 5;                  // 5 objects
    public static final int DEFAULT_CACHE_REFRESH = 30;              // 30 seconds

    //add by biaoping.yin on 2005/06/02
    public static final String DEFAULT_KEY_GENERATE = "auto";        // auto generate table primary key mode
    public static final String COMPOSITE_KEY_GENERATE = "composite"; // composite generate table primary key mode
    //add end
    
    /**
     *  标识数据源是否是外部DataSource，如果是外部DataSource则必须指定外部datasource的jndi名称
     */
    public static final boolean EXTERNAL = false;
    public final static String PROP_USEPOOL = "usepool";
    
    
    
    /*****************************************************************************
     * 					COMMONS-DBCP 属性名称定义开始								 *
     *****************************************************************************/
    public final static String PROP_DEFAULTAUTOCOMMIT = "defaultAutoCommit";
    public final static String PROP_DEFAULTREADONLY = "defaultReadOnly";
    public final static String PROP_DEFAULTTRANSACTIONISOLATION = "defaultTransactionIsolation";
    public final static String PROP_DEFAULTCATALOG = "defaultCatalog";
    public final static String PROP_DRIVERCLASSNAME = "driverClassName";
    public final static String PROP_MAXACTIVE = "maxActive";
    public final static String PROP_MAXIDLE = "maxIdle";
    public final static String PROP_MINIDLE = "minIdle";
    public final static String PROP_INITIALSIZE = "initialSize";
    public final static String PROP_MAXWAIT = "maxWait";
    public final static String PROP_TESTONBORROW = "testOnBorrow";
    public final static String PROP_TESTONRETURN = "testOnReturn";
    public final static String PROP_TIMEBETWEENEVICTIONRUNSMILLIS = "timeBetweenEvictionRunsMillis";
    public final static String PROP_NUMTESTSPEREVICTIONRUN = "numTestsPerEvictionRun";
    public final static String PROP_MINEVICTABLEIDLETIMEMILLIS = "minEvictableIdleTimeMillis";
   
    public final static String PROP_PASSWORD = "password";
    public final static String PROP_URL = "url";
    public final static String PROP_USERNAME = "username";
    public final static String PROP_VALIDATIONQUERY = "validationQuery";
    public final static String PROP_ACCESSTOUNDERLYINGCONNECTIONALLOWED = "accessToUnderlyingConnectionAllowed";
    
    public final static String PROP_REMOVEABANDONED = "removeAbandoned";
    public final static String PROP_REMOVEABANDONEDTIMEOUT = "removeAbandonedTimeout";
    public final static String PROP_LOGABANDONED = "logAbandoned";
    public final static String PROP_TESTWHILEIDLE = "testWhileIdle";
    public final static String PROP_POOLPREPAREDSTATEMENTS = "poolPreparedStatements";
    public final static String PROP_MAXOPENPREPAREDSTATEMENTS = "maxOpenPreparedStatements";
    public final static String PROP_CONNECTIONPROPERTIES = "connectionProperties";
    public final static String PROP_WHENEXHAUSTEDACTION = "whenExhaustedAction";
	public static final int maxOpenPreparedStatements = -1;
    
    
    
    /*****************************************************************************
     * 					COMMONS-DBCP 属性名称定义结束								 *
     *****************************************************************************/


}
