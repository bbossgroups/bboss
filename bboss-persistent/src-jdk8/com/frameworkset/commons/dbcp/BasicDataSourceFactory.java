/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.frameworkset.commons.dbcp;

import com.frameworkset.common.poolman.PoolManConstants;
import com.frameworkset.commons.dbcp2.BasicDataSource;
import com.frameworkset.commons.dbcp2.NativeDataSource;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.util.*;

/**
 * <p>JNDI object factory that creates an instance of
 * <code>BasicDataSource</code> that has been configured based on the
 * <code>RefAddr</code> values of the specified <code>Reference</code>,
 * which must match the names and data types of the
 * <code>BasicDataSource</code> bean properties.</p>
 *
 * @author Craig R. McClanahan
 * @author Dirk Verbeeck
 * @version $Revision: 828639 $ $Date: 2009-10-22 06:27:43 -0400 (Thu, 22 Oct 2009) $
 */
public class BasicDataSourceFactory  {

    private final static String PROP_DEFAULTAUTOCOMMIT = "defaultAutoCommit";
    private final static String PROP_DEFAULTREADONLY = "defaultReadOnly";
    private final static String PROP_DEFAULTTRANSACTIONISOLATION = "defaultTransactionIsolation";
    private final static String PROP_DEFAULTCATALOG = "defaultCatalog";
    private final static String PROP_DRIVERCLASSNAME = "driverClassName";
    private final static String PROP_MAXACTIVE = "maxActive";
    private final static String PROP_MAXIDLE = "maxIdle";
    private final static String PROP_MINIDLE = "minIdle";
    private final static String PROP_INITIALSIZE = "initialSize";
    private final static String PROP_MAXWAIT = "maxWait";
    private final static String PROP_TESTONBORROW = "testOnBorrow";
    private final static String PROP_TESTONRETURN = "testOnReturn";
    private final static String PROP_TIMEBETWEENEVICTIONRUNSMILLIS = "timeBetweenEvictionRunsMillis";
    private final static String PROP_NUMTESTSPEREVICTIONRUN = "numTestsPerEvictionRun";
    private final static String PROP_MINEVICTABLEIDLETIMEMILLIS = "minEvictableIdleTimeMillis";
    private final static String PROP_TESTWHILEIDLE = "testWhileIdle";
	private final static String PROP_validateDatasourceWhenCreate = "validateDatasourceWhenCreate";

    private final static String PROP_PASSWORD = "password";
    private final static String PROP_URL = "url";
    private final static String PROP_USERNAME = "username";
    private final static String PROP_VALIDATIONQUERY = "validationQuery";
    private final static String PROP_VALIDATIONQUERY_TIMEOUT = "validationQueryTimeout";
    /**
     * The property name for initConnectionSqls.
     * The associated value String must be of the form [query;]*
     * @since 1.3
     */
    private final static String PROP_INITCONNECTIONSQLS = "initConnectionSqls";
    private final static String PROP_ACCESSTOUNDERLYINGCONNECTIONALLOWED = "accessToUnderlyingConnectionAllowed";
    private final static String PROP_REMOVEABANDONED = "removeAbandoned";
    private final static String PROP_REMOVEABANDONEDTIMEOUT = "removeAbandonedTimeout";
    private final static String PROP_LOGABANDONED = "logAbandoned";
    private final static String PROP_POOLPREPAREDSTATEMENTS = "poolPreparedStatements";
    private final static String PROP_MAXOPENPREPAREDSTATEMENTS = "maxOpenPreparedStatements";
    private final static String PROP_CONNECTIONPROPERTIES = "connectionProperties";

    private final static String[] ALL_PROPERTIES = {
        PROP_DEFAULTAUTOCOMMIT,
        PROP_DEFAULTREADONLY,
        PROP_DEFAULTTRANSACTIONISOLATION,
        PROP_DEFAULTCATALOG,
        PROP_DRIVERCLASSNAME,
        PROP_MAXACTIVE,
        PROP_MAXIDLE,
        PROP_MINIDLE,
        PROP_INITIALSIZE,
        PROP_MAXWAIT,
        PROP_TESTONBORROW,
        PROP_TESTONRETURN,
        PROP_TIMEBETWEENEVICTIONRUNSMILLIS,
        PROP_NUMTESTSPEREVICTIONRUN,
        PROP_MINEVICTABLEIDLETIMEMILLIS,
        PROP_TESTWHILEIDLE,
        PROP_PASSWORD,
        PROP_URL,
        PROP_USERNAME,
        PROP_VALIDATIONQUERY,
        PROP_VALIDATIONQUERY_TIMEOUT,
        PROP_INITCONNECTIONSQLS,
        PROP_ACCESSTOUNDERLYINGCONNECTIONALLOWED,
        PROP_REMOVEABANDONED,
        PROP_REMOVEABANDONEDTIMEOUT,
        PROP_LOGABANDONED,
        PROP_POOLPREPAREDSTATEMENTS,
        PROP_MAXOPENPREPAREDSTATEMENTS,
        PROP_CONNECTIONPROPERTIES
    };

    
    /**
     * Internal constant to indicate the level is not set.
     */
    static final int UNKNOWN_TRANSACTIONISOLATION = -1;
    /**
     * Creates and configures a {@link BasicDataSource} instance based on the
     * given properties.
     * 
     * @param properties the datasource configuration properties
     * @throws Exception if an error occurs creating the data source
     */
    public static DataSource createDBCP2DataSource(Properties properties) throws Exception {
    	com.frameworkset.commons.dbcp2.BasicDataSource dataSource = new com.frameworkset.commons.dbcp2.BasicDataSource();
        String value = null;
        boolean usepool = Boolean.parseBoolean((String)properties.get(PoolManConstants.PROP_USEPOOL));    	
    	if(usepool)
    	{
	        value = properties.getProperty(PROP_DEFAULTAUTOCOMMIT);
	        if (value != null) {
	            dataSource.setDefaultAutoCommit(Boolean.valueOf(value).booleanValue());
	        }
	
	        value = properties.getProperty(PROP_DEFAULTREADONLY);
	        if (value != null &&   !"".equalsIgnoreCase(value)) {
	            dataSource.setDefaultReadOnly(Boolean.valueOf(value).booleanValue());
	        }
	
	        value = properties.getProperty(PROP_DEFAULTTRANSACTIONISOLATION);
	        if (value != null && !value.equals("")) {
	            int level = UNKNOWN_TRANSACTIONISOLATION;
	            if ("NONE".equalsIgnoreCase(value) || "".equalsIgnoreCase(value)) {
	                level = Connection.TRANSACTION_NONE;
	            }
	            else if ("READ_COMMITTED".equalsIgnoreCase(value)) {
	                level = Connection.TRANSACTION_READ_COMMITTED;
	            }
	            else if ("READ_UNCOMMITTED".equalsIgnoreCase(value)) {
	                level = Connection.TRANSACTION_READ_UNCOMMITTED;
	            }
	            else if ("REPEATABLE_READ".equalsIgnoreCase(value)) {
	                level = Connection.TRANSACTION_REPEATABLE_READ;
	            }
	            else if ("SERIALIZABLE".equalsIgnoreCase(value)) {
	                level = Connection.TRANSACTION_SERIALIZABLE;
	            }
	            else {
	                try {
	                    level = Integer.parseInt(value);
	                } catch (NumberFormatException e) {
	                    System.err.println("Could not parse defaultTransactionIsolation: " + value);
	                    System.err.println("WARNING: defaultTransactionIsolation not set");
	                    System.err.println("using default value of database driver");
	                    level = UNKNOWN_TRANSACTIONISOLATION;
	                }
	            }
	            dataSource.setDefaultTransactionIsolation(level);
	        }
	
	        value = properties.getProperty(PROP_DEFAULTCATALOG);
	        if (value != null) {
	            dataSource.setDefaultCatalog(value);
	        }
	
	        value = properties.getProperty(PROP_DRIVERCLASSNAME);
	        if (value != null) {
	            dataSource.setDriverClassName(value);
	        }
	
	        value = properties.getProperty(PROP_MAXACTIVE);
	        if (value != null) {
	            dataSource.setMaxTotal(Integer.parseInt(value));
	        }
	
	        value = properties.getProperty(PROP_MAXIDLE);
	        if (value != null) {
	            dataSource.setMaxIdle(Integer.parseInt(value));
	        }
	
	        value = properties.getProperty(PROP_MINIDLE);
	        if (value != null) {
	            dataSource.setMinIdle(Integer.parseInt(value));
	        }
	
	        value = properties.getProperty(PROP_INITIALSIZE);
	        if (value != null) {
	            dataSource.setInitialSize(Integer.parseInt(value));
	        }
	
	        value = properties.getProperty(PROP_MAXWAIT);
	        if (value != null) {
	            dataSource.setMaxWaitMillis(Long.parseLong(value));
	        }
	
	        value = properties.getProperty(PROP_TESTONBORROW);
	        if (value != null) {
	            dataSource.setTestOnBorrow(Boolean.valueOf(value).booleanValue());
	        }
	
	        value = properties.getProperty(PROP_TESTONRETURN);
	        if (value != null) {
	            dataSource.setTestOnReturn(Boolean.valueOf(value).booleanValue());
	        }
	
	        value = properties.getProperty(PROP_TIMEBETWEENEVICTIONRUNSMILLIS);
	        if (value != null) {
	            dataSource.setTimeBetweenEvictionRunsMillis(Long.parseLong(value));
	        }
	
	        value = properties.getProperty(PROP_NUMTESTSPEREVICTIONRUN);
	        if (value != null) {
	            dataSource.setNumTestsPerEvictionRun(Integer.parseInt(value));
	        }
	
	        value = properties.getProperty(PROP_MINEVICTABLEIDLETIMEMILLIS);
	        if (value != null) {
	            dataSource.setMinEvictableIdleTimeMillis(Long.parseLong(value));
	        }
	
	        value = properties.getProperty(PROP_TESTWHILEIDLE);
	        if (value != null) {
	            dataSource.setTestWhileIdle(Boolean.valueOf(value).booleanValue());
	        }
			value = properties.getProperty(PROP_validateDatasourceWhenCreate);
			if (value != null) {
				dataSource.setValidateDatasourceWhenCreate(Boolean.valueOf(value).booleanValue());
			}

	
	        value = properties.getProperty(PROP_PASSWORD);
	        if (value != null) {
	            dataSource.setPassword(value);
	        }
	
	        value = properties.getProperty(PROP_URL);
	        if (value != null) {
	            dataSource.setUrl(value);
	        }
	
	        value = properties.getProperty(PROP_USERNAME);
	        if (value != null) {
	            dataSource.setUsername(value);
	        }
	
	        value = properties.getProperty(PROP_VALIDATIONQUERY);
	        if (value != null) {
	            dataSource.setValidationQuery(value);
	        }
	
	        value = properties.getProperty(PROP_VALIDATIONQUERY_TIMEOUT);
	        if (value != null) {
	            dataSource.setValidationQueryTimeout(Integer.parseInt(value));
	        }
	        
	        value = properties.getProperty(PROP_ACCESSTOUNDERLYINGCONNECTIONALLOWED);
	        if (value != null) {
	            dataSource.setAccessToUnderlyingConnectionAllowed(Boolean.valueOf(value).booleanValue());
	        }
	
	        value = properties.getProperty(PROP_REMOVEABANDONED);
	        if (value != null) {
	            dataSource.setRemoveAbandonedOnBorrow(Boolean.valueOf(value).booleanValue());
	        }
	
	        value = properties.getProperty(PROP_REMOVEABANDONEDTIMEOUT);
	        if (value != null) {     
	            dataSource.setRemoveAbandonedTimeout(Integer.parseInt(value));
	        }
	
	        value = properties.getProperty(PROP_LOGABANDONED);
	        if (value != null) {
	            dataSource.setLogAbandoned(Boolean.valueOf(value).booleanValue());
	        }
	
	        value = properties.getProperty(PROP_POOLPREPAREDSTATEMENTS);
	        if (value != null) {
	            dataSource.setPoolPreparedStatements(Boolean.valueOf(value).booleanValue());
	        }
	
	        value = properties.getProperty(PROP_MAXOPENPREPAREDSTATEMENTS);
	        if (value != null) {
	            dataSource.setMaxOpenPreparedStatements(Integer.parseInt(value));
	        }
	
	        value = properties.getProperty(PROP_INITCONNECTIONSQLS);
	        if (value != null) {
	            StringTokenizer tokenizer = new StringTokenizer(value, ";");
	            
	            List<String> sqls = new ArrayList<String>();
	            while(tokenizer.hasMoreTokens())
	            {
	            	sqls.add(tokenizer.nextToken());
	            }
	            
	            dataSource.setConnectionInitSqls(sqls);
	        }
	
	        value = properties.getProperty(PROP_CONNECTIONPROPERTIES);
	        if (value != null) {
	          Properties p = getProperties(value);
	          Enumeration e = p.propertyNames();
	          while (e.hasMoreElements()) {
	            String propertyName = (String) e.nextElement();
	            dataSource.addConnectionProperty(propertyName, p.getProperty(propertyName));
	          }
	        }
	
	        // DBCP-215
	        // Trick to make sure that initialSize connections are created
	        if (dataSource.getInitialSize() > 0) {
	            dataSource.getLogWriter();
	        }
	        // Return the configured DataSource instance
	        return dataSource;
    	}
    	else
    	{
    		NativeDataSource nativedataSource = new NativeDataSource();
    		 value = properties.getProperty(PROP_DEFAULTAUTOCOMMIT);
 	        if (value != null) {
 	            nativedataSource.setDefaultAutoCommit(Boolean.valueOf(value).booleanValue());
 	        }
 	
 	        value = properties.getProperty(PROP_DEFAULTREADONLY);
 	        if (value != null &&  !"".equalsIgnoreCase(value)) {
 	            nativedataSource.setDefaultReadOnly(Boolean.valueOf(value).booleanValue());
 	        }
 	
 	        value = properties.getProperty(PROP_DEFAULTTRANSACTIONISOLATION);
 	        if (value != null&& !value.equals("")) {
 	            int level = UNKNOWN_TRANSACTIONISOLATION;
 	            if ("NONE".equalsIgnoreCase(value)  || "".equalsIgnoreCase(value)) {
 	                level = Connection.TRANSACTION_NONE;
 	            }
 	            else if ("READ_COMMITTED".equalsIgnoreCase(value)) {
 	                level = Connection.TRANSACTION_READ_COMMITTED;
 	            }
 	            else if ("READ_UNCOMMITTED".equalsIgnoreCase(value)) {
 	                level = Connection.TRANSACTION_READ_UNCOMMITTED;
 	            }
 	            else if ("REPEATABLE_READ".equalsIgnoreCase(value)) {
 	                level = Connection.TRANSACTION_REPEATABLE_READ;
 	            }
 	            else if ("SERIALIZABLE".equalsIgnoreCase(value)) {
 	                level = Connection.TRANSACTION_SERIALIZABLE;
 	            }
 	            else {
 	                try {
 	                    level = Integer.parseInt(value);
 	                } catch (NumberFormatException e) {
 	                    System.err.println("Could not parse defaultTransactionIsolation: " + value);
 	                    System.err.println("WARNING: defaultTransactionIsolation not set");
 	                    System.err.println("using default value of database driver");
 	                    level = UNKNOWN_TRANSACTIONISOLATION;
 	                }
 	            }
 	            nativedataSource.setDefaultTransactionIsolation(level);
 	        }
 	
 	        value = properties.getProperty(PROP_DEFAULTCATALOG);
 	        if (value != null) {
 	            nativedataSource.setDefaultCatalog(value);
 	        }
 	
 	        value = properties.getProperty(PROP_DRIVERCLASSNAME);
 	        if (value != null) {
 	            nativedataSource.setDriver(value);
 	        }
 	
 
  
 	         
 	
 	        value = properties.getProperty(PROP_PASSWORD);
 	        if (value != null) {
 	            nativedataSource.setPassword(value);
 	        }
 	
 	        value = properties.getProperty(PROP_URL);
 	        if (value != null) {
 	            nativedataSource.setUrl(value);
 	        }
 	
 	        value = properties.getProperty(PROP_USERNAME);
 	        if (value != null) {
 	            nativedataSource.setUser(value);
 	        }
 	
// 	        value = properties.getProperty(PROP_VALIDATIONQUERY);
// 	        if (value != null) {
// 	            nativedataSource.setValidationQuery(value);
// 	        }
// 	
// 	        value = properties.getProperty(PROP_VALIDATIONQUERY_TIMEOUT);
// 	        if (value != null) {
// 	            nativedataSource.setValidationQueryTimeout(Integer.parseInt(value));
// 	        }
 	        
// 	        value = properties.getProperty(PROP_ACCESSTOUNDERLYINGCONNECTIONALLOWED);
// 	        if (value != null) {
// 	            nativedataSource.setAccessToUnderlyingConnectionAllowed(Boolean.valueOf(value).booleanValue());
// 	        }
// 	
// 	        value = properties.getProperty(PROP_REMOVEABANDONED);
// 	        if (value != null) {
// 	            nativedataSource.setRemoveAbandonedOnBorrow(Boolean.valueOf(value).booleanValue());
// 	        }
// 	
// 	        value = properties.getProperty(PROP_REMOVEABANDONEDTIMEOUT);
// 	        if (value != null) {     
// 	            nativedataSource.setRemoveAbandonedTimeout(Integer.parseInt(value));
// 	        }
// 	
 	        value = properties.getProperty(PROP_LOGABANDONED);
 	        if (value != null) {
 	            nativedataSource.setLogAbandoned(Boolean.valueOf(value).booleanValue());
 	        }
 	
// 	        value = properties.getProperty(PROP_POOLPREPAREDSTATEMENTS);
// 	        if (value != null) {
// 	            nativedataSource.setPoolPreparedStatements(Boolean.valueOf(value).booleanValue());
// 	        }
// 	
// 	        value = properties.getProperty(PROP_MAXOPENPREPAREDSTATEMENTS);
// 	        if (value != null) {
// 	            nativedataSource.setMaxOpenPreparedStatements(Integer.parseInt(value));
// 	        }
    		 value = properties.getProperty(PROP_CONNECTIONPROPERTIES);
 	        if (value != null) {
 	          Properties p = getProperties(value);
 	          Enumeration e = p.propertyNames();
 	          while (e.hasMoreElements()) {
 	            String propertyName = (String) e.nextElement();
 	           nativedataSource.addConnectionProperty(propertyName, p.getProperty(propertyName));
 	          }
 	        }
 	       // Return the configured DataSource instance
 	        return nativedataSource;
    	}

      
    }

    /**
     * <p>Parse properties from the string. Format of the string must be [propertyName=property;]*<p>
     * @param propText
     * @return Properties
     * @throws Exception
     */
    static private Properties getProperties(String propText) throws Exception {
      Properties p = new Properties();
      if (propText != null) {
        p.load(new ByteArrayInputStream(propText.replace(';', '\n').getBytes()));
      }
      return p;
    }
}
