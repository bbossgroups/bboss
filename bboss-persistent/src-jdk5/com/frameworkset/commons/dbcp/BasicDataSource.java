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

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import com.frameworkset.commons.pool.impl.GenericKeyedObjectPool;
import com.frameworkset.commons.pool.impl.GenericKeyedObjectPoolFactory;
import com.frameworkset.commons.pool.impl.GenericObjectPool;


/**
 * <p>Basic implementation of <code>javax.sql.DataSource</code> that is
 * configured via JavaBeans properties.  This is not the only way to
 * combine the <em>commons-dbcp</em> and <em>commons-pool</em> packages,
 * but provides a "one stop shopping" solution for basic requirements.</p>
 *
 * @author Glenn L. Nielsen
 * @author Craig R. McClanahan
 * @author Dirk Verbeeck
 * @version $Revision: 506087 $ $Date: 2007-02-11 11:37:43 -0700 (Sun, 11 Feb 2007) $
 */
public class BasicDataSource implements DataSource {

    // ------------------------------------------------------------- Properties

    /**
     * The default auto-commit state of connections created by this pool.
     */
    protected boolean defaultAutoCommit = true;

    /**
     * Returns the default auto-commit property.
     * 
     * @return true if default auto-commit is enabled
     */
    public synchronized boolean getDefaultAutoCommit() {
        return this.defaultAutoCommit;
    }

    /**
     * <p>Sets default auto-commit state of connections returned by this
     * datasource.</p>
     * <p>
     * Note: this method currently has no effect once the pool has been
     * initialized.  The pool is initialized the first time one of the
     * following methods is invoked: <code>getConnection, setLogwriter,
     * setLoginTimeout, getLoginTimeout, getLogWriter.</code></p>
     * 
     * @param defaultAutoCommit default auto-commit value
     */
    public synchronized void setDefaultAutoCommit(boolean defaultAutoCommit) {
        this.defaultAutoCommit = defaultAutoCommit;
        this.restartNeeded = true;
    }


    /**
     * The default read-only state of connections created by this pool.
     */
    protected Boolean defaultReadOnly = null;

    /**
     * Returns the default readOnly property.
     * 
     * @return true if connections are readOnly by default
     */
    public synchronized boolean getDefaultReadOnly() {
        if (this.defaultReadOnly != null) {
            return this.defaultReadOnly.booleanValue();
        }
        return false;
    }

    /**
     * <p>Sets defaultReadonly property.</p>
     * <p>
     * Note: this method currently has no effect once the pool has been
     * initialized.  The pool is initialized the first time one of the
     * following methods is invoked: <code>getConnection, setLogwriter,
     * setLoginTimeout, getLoginTimeout, getLogWriter.</code></p>
     * 
     * @param defaultReadOnly default read-only value
     */
    public synchronized void setDefaultReadOnly(boolean defaultReadOnly) {
        this.defaultReadOnly = defaultReadOnly ? Boolean.TRUE : Boolean.FALSE;
        this.restartNeeded = true;
    }

    /**
     * The default TransactionIsolation state of connections created by this pool.
     */
    protected int defaultTransactionIsolation = PoolableConnectionFactory.UNKNOWN_TRANSACTIONISOLATION;

    /**
     * Returns the default transaction isolation state of returned connections.
     * 
     * @return the default value for transaction isolation state
     * @see Connection#getTransactionIsolation
     */
    public synchronized int getDefaultTransactionIsolation() {
        return this.defaultTransactionIsolation;
    }

    /**
     * <p>Sets the default transaction isolation state for returned
     * connections.</p>
     * <p>
     * Note: this method currently has no effect once the pool has been
     * initialized.  The pool is initialized the first time one of the
     * following methods is invoked: <code>getConnection, setLogwriter,
     * setLoginTimeout, getLoginTimeout, getLogWriter.</code></p>
     * 
     * @param defaultTransactionIsolation the default transaction isolation
     * state
     * @see Connection#getTransactionIsolation
     */
    public synchronized void setDefaultTransactionIsolation(int defaultTransactionIsolation) {
        this.defaultTransactionIsolation = defaultTransactionIsolation;
        this.restartNeeded = true;
    }


    /**
     * The default "catalog" of connections created by this pool.
     */
    protected String defaultCatalog = null;

    /**
     * Returns the default catalog.
     * 
     * @return the default catalog
     */
    public synchronized String getDefaultCatalog() {
        return this.defaultCatalog;
    }

    /**
     * <p>Sets the default catalog.</p>
     * <p>
     * Note: this method currently has no effect once the pool has been
     * initialized.  The pool is initialized the first time one of the
     * following methods is invoked: <code>getConnection, setLogwriter,
     * setLoginTimeout, getLoginTimeout, getLogWriter.</code></p>
     * 
     * @param defaultCatalog the default catalog
     */
    public synchronized void setDefaultCatalog(String defaultCatalog) {
        if ((defaultCatalog != null) && (defaultCatalog.trim().length() > 0)) {
            this.defaultCatalog = defaultCatalog;
        }
        else {
            this.defaultCatalog = null;
        }
        this.restartNeeded = true;
    }

  
    /**
     * The fully qualified Java class name of the JDBC driver to be used.
     */
    protected String driverClassName = null;

    /**
     * Returns the jdbc driver class name.
     * 
     * @return the jdbc driver class name
     */
    public synchronized String getDriverClassName() {
        return this.driverClassName;
    }

    /**
     * <p>Sets the jdbc driver class name.</p>
     * <p>
     * Note: this method currently has no effect once the pool has been
     * initialized.  The pool is initialized the first time one of the
     * following methods is invoked: <code>getConnection, setLogwriter,
     * setLoginTimeout, getLoginTimeout, getLogWriter.</code></p>
     * 
     * @param driverClassName the class name of the jdbc driver
     */
    public synchronized void setDriverClassName(String driverClassName) {
        if ((driverClassName != null) && (driverClassName.trim().length() > 0)) {
            this.driverClassName = driverClassName;
        }
        else {
            this.driverClassName = null;
        }
        this.restartNeeded = true;
    }


    /**
     * The maximum number of active connections that can be allocated from
     * this pool at the same time, or non-positive for no limit.
     */
    protected int maxActive = GenericObjectPool.DEFAULT_MAX_ACTIVE;

    /**
     * <p>Returns the maximum number of active connections that can be
     * allocated at the same time.
     * </p>
     * <p>A non-positive number means that there is no limit.</p>
     * 
     * @return the maximum number of active connections
     */
    public synchronized int getMaxActive() {
        return this.maxActive;
    }

    /**
     * Sets the maximum number of active connections that can be
     * allocated at the same time.
     * 
     * @param maxActive the new value for maxActive
     * @see #getMaxActive()
     */
    public synchronized void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
        if (connectionPool != null) {
            connectionPool.setMaxActive(maxActive);
        }
    }

    /**
     * The maximum number of connections that can remain idle in the
     * pool, without extra ones being released, or negative for no limit.
     */
    protected int maxIdle = GenericObjectPool.DEFAULT_MAX_IDLE;

    /**
     * <p>Returns the maximum number of connections that can remain idle in the
     * pool.
     * </p>
     * <p>A negative value indicates that there is no limit</p>
     * 
     * @return the maximum number of idle connections
     */
    public synchronized int getMaxIdle() {
        return this.maxIdle;
    }

    /**
     * Sets the maximum number of connections that can remail idle in the
     * pool.
     * 
     * @see #getMaxIdle()
     * @param maxIdle the new value for maxIdle
     */
    public synchronized void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
        if (connectionPool != null) {
            connectionPool.setMaxIdle(maxIdle);
        }
    }

    /**
     * The minimum number of active connections that can remain idle in the
     * pool, without extra ones being created, or 0 to create none.
     */
    protected int minIdle = GenericObjectPool.DEFAULT_MIN_IDLE;

    /**
     * Returns the minimum number of idle connections in the pool
     * 
     * @return the minimum number of idle connections
     * @see GenericObjectPool#getMinIdle()
     */
    public synchronized int getMinIdle() {
        return this.minIdle;
    }

    /**
     * Sets the minimum number of idle connections in the pool.
     * 
     * @param minIdle the new value for minIdle
     * @see GenericObjectPool#setMinIdle(int)
     */
    public synchronized void setMinIdle(int minIdle) {
       this.minIdle = minIdle;
       if (connectionPool != null) {
           connectionPool.setMinIdle(minIdle);
       }
    }

    /**
     * The initial number of connections that are created when the pool
     * is started.
     * 
     * @since 1.2
     */
    protected int initialSize = 0;
    
    /**
     * Returns the initial size of the connection pool.
     * 
     * @return the number of connections created when the pool is initialized
     */
    public synchronized int getInitialSize() {
        return this.initialSize;
    }
    
    /**
     * <p>Sets the initial size of the connection pool.</p>
     * <p>
     * Note: this method currently has no effect once the pool has been
     * initialized.  The pool is initialized the first time one of the
     * following methods is invoked: <code>getConnection, setLogwriter,
     * setLoginTimeout, getLoginTimeout, getLogWriter.</code></p>
     * 
     * @param initialSize the number of connections created when the pool
     * is initialized
     */
    public synchronized void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
        this.restartNeeded = true;
    }

    /**
     * The maximum number of milliseconds that the pool will wait (when there
     * are no available connections) for a connection to be returned before
     * throwing an exception, or -1 to wait indefinitely.
     */
    protected long maxWait = GenericObjectPool.DEFAULT_MAX_WAIT;

    /**
     * <p>Returns the maximum number of milliseconds that the pool will wait
     * for a connection to be returned before throwing an exception.
     * </p>
     * <p>Returns -1 if the pool is set to wait indefinitely.</p>
     * 
     * @return the maxWait property value
     */
    public synchronized long getMaxWait() {
        return this.maxWait;
    }

    /**
     * Sets the maxWait property.
     * 
     * @param maxWait the new value for maxWait
     * @see #getMaxWait()
     */
    public synchronized void setMaxWait(long maxWait) {
        this.maxWait = maxWait;
        if (connectionPool != null) {
            connectionPool.setMaxWait(maxWait);
        }
    }

    /**
     * Prepared statement pooling for this pool.
     */
    protected boolean poolPreparedStatements = false;
    
    /**
     * Returns true if we are pooling statements.
     * 
     * @return true if prepared statements are pooled
     */
    public synchronized boolean isPoolPreparedStatements() {
        return this.poolPreparedStatements;
    }

    /**
     * <p>Sets whether to pool statements or not.</p>
     * <p>
     * Note: this method currently has no effect once the pool has been
     * initialized.  The pool is initialized the first time one of the
     * following methods is invoked: <code>getConnection, setLogwriter,
     * setLoginTimeout, getLoginTimeout, getLogWriter.</code></p>
     * 
     * @param poolingStatements pooling on or off
     */
    public synchronized void setPoolPreparedStatements(boolean poolingStatements) {
        this.poolPreparedStatements = poolingStatements;
        this.restartNeeded = true;
    }

    /**
     * The maximum number of open statements that can be allocated from
     * the statement pool at the same time, or non-positive for no limit.  Since 
     * a connection usually only uses one or two statements at a time, this is
     * mostly used to help detect resource leaks.
     */
    protected int maxOpenPreparedStatements = GenericKeyedObjectPool.DEFAULT_MAX_TOTAL;

    /**
     * Gets the value of the {@link #maxOpenPreparedStatements} property.
     * 
     * @return the maximum number of open statements
     * @see #maxOpenPreparedStatements
     */
    public synchronized int getMaxOpenPreparedStatements() {
        return this.maxOpenPreparedStatements;
    }

    /** 
     * <p>Sets the value of the {@link #maxOpenPreparedStatements}
     * property.</p>
     * <p>
     * Note: this method currently has no effect once the pool has been
     * initialized.  The pool is initialized the first time one of the
     * following methods is invoked: <code>getConnection, setLogwriter,
     * setLoginTimeout, getLoginTimeout, getLogWriter.</code></p>
     * 
     * @param maxOpenStatements the new maximum number of prepared statements
     * @see #maxOpenPreparedStatements
     */
    public synchronized void setMaxOpenPreparedStatements(int maxOpenStatements) {
        this.maxOpenPreparedStatements = maxOpenStatements;
        this.restartNeeded = true;
    }

    /**
     * The indication of whether objects will be validated before being
     * borrowed from the pool.  If the object fails to validate, it will be
     * dropped from the pool, and we will attempt to borrow another.
     */
    protected boolean testOnBorrow = true;

    /**
     * Returns the {@link #testOnBorrow} property.
     * 
     * @return true if objects are validated before being borrowed from the
     * pool
     * 
     * @see #testOnBorrow
     */
    public synchronized boolean getTestOnBorrow() {
        return this.testOnBorrow;
    }

    /**
     * Sets the {@link #testOnBorrow} property. This property determines
     * whether or not the pool will validate objects before they are borrowed
     * from the pool. For a <code>true</code> value to have any effect, the 
     * <code>validationQuery</code> property must be set to a non-null string.
     * 
     * @param testOnBorrow new value for testOnBorrow property
     */
    public synchronized void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
        if (connectionPool != null) {
            connectionPool.setTestOnBorrow(testOnBorrow);
        }
    }

    /**
     * The indication of whether objects will be validated before being
     * returned to the pool.
     */
    protected boolean testOnReturn = false;

    /**
     * Returns the value of the {@link #testOnReturn} property.
     * 
     * @return true if objects are validated before being returned to the
     * pool
     * @see #testOnReturn
     */
    public synchronized boolean getTestOnReturn() {
        return this.testOnReturn;
    }

    /**
     * Sets the <code>testOnReturn</code> property. This property determines
     * whether or not the pool will validate objects before they are returned
     * to the pool. For a <code>true</code> value to have any effect, the 
     * <code>validationQuery</code> property must be set to a non-null string.
     * 
     * @param testOnReturn new value for testOnReturn property
     */
    public synchronized void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
        if (connectionPool != null) {
            connectionPool.setTestOnReturn(testOnReturn);
        }
    }

    /**
     * The number of milliseconds to sleep between runs of the idle object
     * evictor thread.  When non-positive, no idle object evictor thread will
     * be run.
     */
    protected long timeBetweenEvictionRunsMillis =
        GenericObjectPool.DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS;
        
    /**
     * Returns the value of the {@link #timeBetweenEvictionRunsMillis}
     * property.
     * 
     * @return the time (in miliseconds) between evictor runs
     * @see #timeBetweenEvictionRunsMillis
     */
    public synchronized long getTimeBetweenEvictionRunsMillis() {
        return this.timeBetweenEvictionRunsMillis;
    }

    /**
     * Sets the {@link #timeBetweenEvictionRunsMillis} property.
     * 
     * @param timeBetweenEvictionRunsMillis the new time between evictor runs
     * @see #timeBetweenEvictionRunsMillis
     */
    public synchronized void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
        if (connectionPool != null) {
            connectionPool.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        }
    }

    /**
     * The number of objects to examine during each run of the idle object
     * evictor thread (if any).
     */
    protected int numTestsPerEvictionRun =
        GenericObjectPool.DEFAULT_NUM_TESTS_PER_EVICTION_RUN;

    /**
     * Returns the value of the {@link #numTestsPerEvictionRun} property.
     * 
     * @return the number of objects to examine during idle object evictor
     * runs
     * @see #numTestsPerEvictionRun
     */
    public synchronized int getNumTestsPerEvictionRun() {
        return this.numTestsPerEvictionRun;
    }

    /**
     * Sets the value of the {@link #numTestsPerEvictionRun} property.
     * 
     * @param numTestsPerEvictionRun the new {@link #numTestsPerEvictionRun} 
     * value
     * @see #numTestsPerEvictionRun
     */
    public synchronized void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
        if (connectionPool != null) {
            connectionPool.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        }
    }

    /**
     * The minimum amount of time an object may sit idle in the pool before it
     * is eligable for eviction by the idle object evictor (if any).
     */
    protected long minEvictableIdleTimeMillis =
        GenericObjectPool.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS;

    /**
     * Returns the {@link #minEvictableIdleTimeMillis} property.
     * 
     * @return the value of the {@link #minEvictableIdleTimeMillis} property
     * @see #minEvictableIdleTimeMillis
     */
    public synchronized long getMinEvictableIdleTimeMillis() {
        return this.minEvictableIdleTimeMillis;
    }

    /**
     * Sets the {@link #minEvictableIdleTimeMillis} property.
     * 
     * @param minEvictableIdleTimeMillis the minimum amount of time an object
     * may sit idle in the pool 
     * @see #minEvictableIdleTimeMillis
     */
    public synchronized void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
        if (connectionPool != null) {
            connectionPool.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        }
    }

    /**
     * The indication of whether objects will be validated by the idle object
     * evictor (if any).  If an object fails to validate, it will be dropped
     * from the pool.
     */
    protected boolean testWhileIdle = false;

    /**
     * Returns the value of the {@link #testWhileIdle} property.
     * 
     * @return true if objects examined by the idle object evictor are
     * validated
     * @see #testWhileIdle
     */
    public synchronized boolean getTestWhileIdle() {
        return this.testWhileIdle;
    }

    /**
     * Sets the <code>testWhileIdle</code> property. This property determines
     * whether or not the idle object evictor will validate connections.  For a
     * <code>true</code> value to have any effect, the 
     * <code>validationQuery</code> property must be set to a non-null string.
     * 
     * @param testWhileIdle new value for testWhileIdle property
     */
    public synchronized void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
        if (connectionPool != null) {
            connectionPool.setTestWhileIdle(testWhileIdle);
        }
    }

    /**
     * [Read Only] The max number of objects {@link #borrowObject} borrowed
     * from the pool , but not yet returned ahead of now.
     * 
     * @return the current max number of active connections
     */
    public int getMaxNumActive() {
        if (connectionPool != null) {
            return connectionPool.getMaxNumActive();
        } else {
            return 0;
        }
    }
    
    /**
     * [Read Only] The current number of active connections that have been
     * allocated from this data source.
     * 
     * @return the current number of active connections
     */
    public int getNumActive() {
        if (connectionPool != null) {
            return connectionPool.getNumActive_();
        } else {
            return 0;
        }
    }


    /**
     * [Read Only] The current number of idle connections that are waiting
     * to be allocated from this data source.
     * 
     * @return the current number of idle connections
     */
    public int getNumIdle() {
        if (connectionPool != null) {
            return connectionPool.getNumIdle_();
        } else {
            return 0;
        }
    }

    /**
     * The connection password to be passed to our JDBC driver to establish
     * a connection.
     */
    protected String password = null;

    /**
     * Returns the password passed to the JDBC driver to establish connections.
     * 
     * @return the connection password
     */
    public synchronized String getPassword() {
        return this.password;
    }

    /** 
     * <p>Sets the {@link #password}.</p>
     * <p>
     * Note: this method currently has no effect once the pool has been
     * initialized.  The pool is initialized the first time one of the
     * following methods is invoked: <code>getConnection, setLogwriter,
     * setLoginTimeout, getLoginTimeout, getLogWriter.</code></p>
     * 
     * @param password new value for the password
     */
    public synchronized void setPassword(String password) {
        this.password = password;
        this.restartNeeded = true;
    }

    /**
     * The connection URL to be passed to our JDBC driver to establish
     * a connection.
     */
    protected String url = null;

    /**
     * Returns the JDBC connection {@link #url} property.
     * 
     * @return the {@link #url} passed to the JDBC driver to establish
     * connections
     */
    public synchronized String getUrl() {
        return this.url;
    }

    /** 
     * <p>Sets the {@link #url}.</p>
     * <p>
     * Note: this method currently has no effect once the pool has been
     * initialized.  The pool is initialized the first time one of the
     * following methods is invoked: <code>getConnection, setLogwriter,
     * setLoginTimeout, getLoginTimeout, getLogWriter.</code></p>
     * 
     * @param url the new value for the JDBC connection url
     */
    public synchronized void setUrl(String url) {
        this.url = url;
        this.restartNeeded = true;
    }

    /**
     * The connection username to be passed to our JDBC driver to
     * establish a connection.
     */
    protected String username = null;

    /**
     * Returns the JDBC connection {@link #username} property.
     * 
     * @return the {@link #username} passed to the JDBC driver to establish
     * connections
     */
    public synchronized String getUsername() {
        return this.username;
    }

    /** 
     * <p>Sets the {@link #username}.</p>
     * <p>
     * Note: this method currently has no effect once the pool has been
     * initialized.  The pool is initialized the first time one of the
     * following methods is invoked: <code>getConnection, setLogwriter,
     * setLoginTimeout, getLoginTimeout, getLogWriter.</code></p>
     * 
     * @param username the new value for the JDBC connection username
     */
    public synchronized void setUsername(String username) {
        this.username = username;
        this.restartNeeded = true;
    }

    /**
     * The SQL query that will be used to validate connections from this pool
     * before returning them to the caller.  If specified, this query
     * <strong>MUST</strong> be an SQL SELECT statement that returns at least
     * one row.
     */
    protected String validationQuery = null;

    /**
     * Returns the validation query used to validate connections before
     * returning them.
     * 
     * @return the SQL validation query
     * @see #validationQuery
     */
    public synchronized String getValidationQuery() {
        return this.validationQuery;
    }

    /** 
     * <p>Sets the {@link #validationQuery}.</p>
     * <p>
     * Note: this method currently has no effect once the pool has been
     * initialized.  The pool is initialized the first time one of the
     * following methods is invoked: <code>getConnection, setLogwriter,
     * setLoginTimeout, getLoginTimeout, getLogWriter.</code></p>
     * 
     * @param validationQuery the new value for the validation query
     */
    public synchronized void setValidationQuery(String validationQuery) {
        if ((validationQuery != null) && (validationQuery.trim().length() > 0)) {
            this.validationQuery = validationQuery;
        } else {
            this.validationQuery = null;
        }
        this.restartNeeded = true;
    }

    /** 
     * Controls access to the underlying connection.
     */
    private boolean accessToUnderlyingConnectionAllowed = false; 

    /**
     * Returns the value of the accessToUnderlyingConnectionAllowed property.
     * 
     * @return true if access to the underlying connection is allowed, false
     * otherwise.
     */
    public synchronized boolean isAccessToUnderlyingConnectionAllowed() {
        return this.accessToUnderlyingConnectionAllowed;
    }

    /**
     * <p>Sets the value of the accessToUnderlyingConnectionAllowed property.
     * It controls if the PoolGuard allows access to the underlying connection.
     * (Default: false)</p>
     * <p>
     * Note: this method currently has no effect once the pool has been
     * initialized.  The pool is initialized the first time one of the
     * following methods is invoked: <code>getConnection, setLogwriter,
     * setLoginTimeout, getLoginTimeout, getLogWriter.</code></p>
     * 
     * @param allow Access to the underlying connection is granted when true.
     */
    public synchronized void setAccessToUnderlyingConnectionAllowed(boolean allow) {
        this.accessToUnderlyingConnectionAllowed = allow;
        this.restartNeeded = true;
    }

    // ----------------------------------------------------- Instance Variables

    // TODO: review & make isRestartNeeded() public, restartNeeded protected

    /**
     * A property setter has been invoked that will require the connection
     * pool to be re-initialized. Currently, restart is not triggered, so
     * this property has no effect.
     */
    private boolean restartNeeded = false;
    
    /**
     * Returns whether or not a restart is needed. 
     *  
     * Note: restart is not currently triggered by property changes.
     * 
     * @return true if a restart is needed
     */
    private synchronized boolean isRestartNeeded() {
        return restartNeeded;
    }

    /**
     * The object pool that internally manages our connections.
     */
    protected GenericObjectPool connectionPool = null;
    
    /**
     * The connection properties that will be sent to our JDBC driver when
     * establishing new connections.  <strong>NOTE</strong> - The "user" and
     * "password" properties will be passed explicitly, so they do not need
     * to be included here.
     */
    protected Properties connectionProperties = new Properties();

    /**
     * The data source we will use to manage connections.  This object should
     * be acquired <strong>ONLY</strong> by calls to the
     * <code>createDataSource()</code> method.
     */
    protected DataSource dataSource = null;

    /**
     * The PrintWriter to which log messages should be directed.
     */
    protected PrintWriter logWriter = new PrintWriter(System.out);


    // ----------------------------------------------------- DataSource Methods


    /**
     * Create (if necessary) and return a connection to the database.
     *
     * @throws SQLException if a database access error occurs
     * @return a database connection
     */
    public Connection getConnection() throws SQLException {
        return createDataSource().getConnection();
    }


    /**
     * <strong>BasicDataSource does NOT support this method.
     * </strong>
     *
     * @param username Database user on whose behalf the Connection
     *   is being made
     * @param password The database user's password
     *
     * @throws UnsupportedOperationException
     * @throws SQLException if a database access error occurs
     * @return nothing - always throws UnsupportedOperationException
     */
    public Connection getConnection(String username, String password) throws SQLException {
        // This method isn't supported by the PoolingDataSource returned by
        // the createDataSource
        throw new UnsupportedOperationException("Not supported by BasicDataSource");
        // return createDataSource().getConnection(username, password);
    }


    /**
     * <p>Returns the login timeout (in seconds) for connecting to the database.
     * </p>
     * <p>Calls {@link #createDataSource()}, so has the side effect
     * of initializing the connection pool.</p>
     *
     * @throws SQLException if a database access error occurs
     * @throws UnsupportedOperationException If the DataSource implementation
     *   does not support the login timeout feature.
     * @return login timeout in seconds
     */
    public int getLoginTimeout() throws SQLException {
        return createDataSource().getLoginTimeout();
    }


    /**
     * <p>Returns the log writer being used by this data source.</p>
     * <p>
     * Calls {@link #createDataSource()}, so has the side effect
     * of initializing the connection pool.</p>
     *
     * @throws SQLException if a database access error occurs
     * @return log writer in use
     */
    public PrintWriter getLogWriter() throws SQLException {
        return createDataSource().getLogWriter();
    }


    /**
     * <p>Set the login timeout (in seconds) for connecting to the
     * database.</p>
     * <p>
     * Calls {@link #createDataSource()}, so has the side effect
     * of initializing the connection pool.</p>
     *
     * @param loginTimeout The new login timeout, or zero for no timeout
     * @throws SQLException if a database access error occurs
     */
    public void setLoginTimeout(int loginTimeout) throws SQLException {
        createDataSource().setLoginTimeout(loginTimeout);
    }


    /**
     * <p>Sets the log writer being used by this data source.</p>
     * <p>
     * Calls {@link #createDataSource()}, so has the side effect
     * of initializing the connection pool.</p>
     *
     * @param logWriter The new log writer
     * @throws SQLException if a database access error occurs
     */
    public void setLogWriter(PrintWriter logWriter) throws SQLException {
        createDataSource().setLogWriter(logWriter);
        this.logWriter = logWriter;
    }

    private AbandonedConfig abandonedConfig;

    /**                       
     * Flag to remove abandoned connections if they exceed the
     * removeAbandonedTimout.
     *
     * Set to true or false, default false.
     * If set to true a connection is considered abandoned and eligible
     * for removal if it has been idle longer than the removeAbandonedTimeout.
     * Setting this to true can recover db connections from poorly written    
     * applications which fail to close a connection.      
     * @deprecated                   
     */                                                                   
    public boolean getRemoveAbandoned() {   
        if (abandonedConfig != null) {
            return abandonedConfig.getRemoveAbandoned();
        }
        return false;
    }                                    
                                 
    /**
     * @deprecated
     * @param removeAbandoned new removeAbandoned property value
     */
    public void setRemoveAbandoned(boolean removeAbandoned) {
        if (abandonedConfig == null) {
            abandonedConfig = new AbandonedConfig();
        }
        abandonedConfig.setRemoveAbandoned(removeAbandoned);
        this.restartNeeded = true;
    }                                                        
                                               
    /**
     * Timeout in seconds before an abandoned connection can be removed.
     *
     * Defaults to 300 seconds. 
     * @return abandoned connection timeout        
     * @deprecated                                
     */                                                                 
    public int getRemoveAbandonedTimeout() { 
        if (abandonedConfig != null) {
            return abandonedConfig.getRemoveAbandonedTimeout();
        }
        return 300;
    }                                        

    /**
     * @deprecated
     * @param removeAbandonedTimeout new removeAbandonedTimeout value
     */               
    public void setRemoveAbandonedTimeout(int removeAbandonedTimeout) {
        if (abandonedConfig == null) {
            abandonedConfig = new AbandonedConfig();
        }
        abandonedConfig.setRemoveAbandonedTimeout(removeAbandonedTimeout);
        this.restartNeeded = true;
    }                                                                  
                                                             
    /**
     * <p>Flag to log stack traces for application code which abandoned
     * a Statement or Connection.
     * </p>
     * <p>Defaults to false.                
     * </p>                                                            
     * <p>Logging of abandoned Statements and Connections adds overhead
     * for every Connection open or new Statement because a stack   
     * trace has to be generated. </p>
     *             
     * @deprecated                      
     */                                                          
    public boolean getLogAbandoned() {   
        if (abandonedConfig != null) {
            return abandonedConfig.getLogAbandoned();
        }
        return false;
    }                                 

    /**
     * @deprecated
     * @param logAbandoned new logAbandoned property value
     */
    public void setLogAbandoned(boolean logAbandoned) {
        if (abandonedConfig == null) {
            abandonedConfig = new AbandonedConfig();
        }
        abandonedConfig.setLogAbandoned(logAbandoned);
        this.restartNeeded = true;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Add a custom connection property to the set that will be passed to our
     * JDBC driver. This <strong>MUST</strong> be called before the first
     * connection is retrieved (along with all the other configuration
     * property setters). Calls to this method after the connection pool
     * has been initialized have no effect.
     *
     * @param name Name of the custom connection property
     * @param value Value of the custom connection property
     */
    public void addConnectionProperty(String name, String value) {
        connectionProperties.put(name, value);
        this.restartNeeded = true;
    }

    /**
     * Remove a custom connection property.
     * 
     * @param name Name of the custom connection property to remove
     * @see #addConnectionProperty(String, String)
     */
    public void removeConnectionProperty(String name) {
        connectionProperties.remove(name);
        this.restartNeeded = true;
    }

    /**
     * Close and release all connections that are currently stored in the
     * connection pool associated with our data source.
     *
     * @throws SQLException if a database error occurs
     */
    public synchronized void close() throws SQLException {
        GenericObjectPool oldpool = connectionPool;
        connectionPool = null;
        dataSource = null;
        try {
            if (oldpool != null) {
                oldpool.close();
            }
        } catch(SQLException e) {
            throw e;
        } catch(RuntimeException e) {
            throw e;
        } catch(Exception e) {
            throw new SQLNestedException("Cannot close connection pool", e);
        }
    }


    // ------------------------------------------------------ Protected Methods


    /**
     * <p>Create (if necessary) and return the internal data source we are
     * using to manage our connections.</p>
     *
     * <p><strong>IMPLEMENTATION NOTE</strong> - It is tempting to use the
     * "double checked locking" idiom in an attempt to avoid synchronizing
     * on every single call to this method.  However, this idiom fails to
     * work correctly in the face of some optimizations that are legal for
     * a JVM to perform.</p>
     *
     * @throws SQLException if the object pool cannot be created.
     */
    protected synchronized DataSource createDataSource()
        throws SQLException {

        // Return the pool if we have already created it
        if (dataSource != null) {
            return (dataSource);
        }

        // Load the JDBC driver class
        if (driverClassName != null) {
            try {
                Class.forName(driverClassName);
            } catch (Throwable t) {
                String message = "Cannot load JDBC driver class '" +
                    driverClassName + "'";
                logWriter.println(message);
                t.printStackTrace(logWriter);
                throw new SQLNestedException(message, t);
            }
        }

        // Create a JDBC driver instance
        Driver driver = null;
        try {
            driver = DriverManager.getDriver(url);
        } catch (Throwable t) {
            String message = "Cannot create JDBC driver of class '" +
                (driverClassName != null ? driverClassName : "") + 
                "' for connect URL '" + url + "'";
            logWriter.println(message);
            t.printStackTrace(logWriter);
            throw new SQLNestedException(message, t);
        }

        // Can't test without a validationQuery
        if (validationQuery == null) {
            setTestOnBorrow(false);
            setTestOnReturn(false);
            setTestWhileIdle(false);
        }

        // Create an object pool to contain our active connections
        if ((abandonedConfig != null) && (abandonedConfig.getRemoveAbandoned())) {
            connectionPool = new AbandonedObjectPool(null,abandonedConfig);
        }
        else {
            connectionPool = new GenericObjectPool();
        }
        connectionPool.setMaxActive(maxActive);
        connectionPool.setMaxIdle(maxIdle);
        connectionPool.setMinIdle(minIdle);
        connectionPool.setMaxWait(maxWait);
        connectionPool.setTestOnBorrow(testOnBorrow);
        connectionPool.setTestOnReturn(testOnReturn);
        connectionPool.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        connectionPool.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        connectionPool.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        connectionPool.setTestWhileIdle(testWhileIdle);
        connectionPool.setWhenExhaustedAction(this.whenExhaustedAction);
        
        // Set up statement pool, if desired
        GenericKeyedObjectPoolFactory statementPoolFactory = null;
        if (isPoolPreparedStatements()) {
            statementPoolFactory = new GenericKeyedObjectPoolFactory(null, 
                        -1, // unlimited maxActive (per key)
                        GenericKeyedObjectPool.WHEN_EXHAUSTED_FAIL, 
                        0, // maxWait
                        1, // maxIdle (per key) 
                        maxOpenPreparedStatements); 
        }

        // Set up the driver connection factory we will use
        if (username != null) {
            connectionProperties.put("user", username);
        } else {
            log("DBCP DataSource configured without a 'username'");
        }
        
        if (password != null) {
            connectionProperties.put("password", password);
        } else {
            log("DBCP DataSource configured without a 'password'");
        }
        
        DriverConnectionFactory driverConnectionFactory =
            new DriverConnectionFactory(driver, url, connectionProperties);

        // Set up the poolable connection factory we will use
        PoolableConnectionFactory connectionFactory = null;
        try {
            connectionFactory =
                new PoolableConnectionFactory(driverConnectionFactory,
                                              connectionPool,
                                              statementPoolFactory,
                                              validationQuery,
                                              defaultReadOnly,
                                              defaultAutoCommit,
                                              defaultTransactionIsolation,
                                              defaultCatalog,
                                              abandonedConfig);
            if (connectionFactory == null) {
                throw new SQLException("Cannot create PoolableConnectionFactory");
            }
            validateConnectionFactory(connectionFactory);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new SQLNestedException("Cannot create PoolableConnectionFactory (" + e.getMessage() + ")", e);
        }

        // Create and return the pooling data source to manage the connections
        dataSource = new PoolingDataSource(connectionPool);
        ((PoolingDataSource) dataSource).setAccessToUnderlyingConnectionAllowed(isAccessToUnderlyingConnectionAllowed());
        dataSource.setLogWriter(logWriter);
        
        try {
            for (int i = 0 ; i < initialSize ; i++) {
                connectionPool.addObject();
            }
        } catch (Exception e) {
            throw new SQLNestedException("Error preloading the connection pool", e);
        }
        
        return dataSource;
    }
    
    
    public List getTraceObjects()
    {
    	if (connectionPool != null && connectionPool instanceof AbandonedObjectPool)
    	{
    		return ((AbandonedObjectPool)connectionPool).getTraces();
    	}
    	return new ArrayList();
    }

    private static void validateConnectionFactory(PoolableConnectionFactory connectionFactory) throws Exception {
        Connection conn = null;
        try {
            conn = (Connection) connectionFactory.makeObject();
            connectionFactory.activateObject(conn);
            connectionFactory.validateConnection(conn);
            connectionFactory.passivateObject(conn);
        }
        finally {
            connectionFactory.destroyObject(conn);
        }
    }

    /**
     * Not used currently
     */
    private void restart() {
        try {
            close();
        } catch (SQLException e) {
            log("Could not restart DataSource, cause: " + e.getMessage());
        }
    }

    private void log(String message) {
        if (logWriter != null) {
            logWriter.println(message);
        }
    }

	public Object getJNDIName() {
		
		return null;
	}
	
	private byte whenExhaustedAction = GenericObjectPool.DEFAULT_WHEN_EXHAUSTED_ACTION;
	/**
	 * Added by biaoping.yin on 2007-07-22
	 * @param action
	 */
	public void setWhenExhaustedAction(byte action)
    {
		this.whenExhaustedAction = action;
		if(connectionPool != null)
			this.connectionPool.setWhenExhaustedAction(whenExhaustedAction);
    }
}
