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

package com.frameworkset.commons.dbcp2.datasources;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.sql.ConnectionPoolDataSource;

import com.frameworkset.commons.pool2.KeyedObjectPool;
import com.frameworkset.commons.pool2.impl.GenericKeyedObjectPool;
import com.frameworkset.commons.pool2.impl.GenericKeyedObjectPoolConfig;

/**
 * <p>A pooling <code>DataSource</code> appropriate for deployment within
 * J2EE environment.  There are many configuration options, most of which are
 * defined in the parent class. All users (based on username) share a single
 * maximum number of Connections in this datasource.</p>
 *
 * <p>User passwords can be changed without re-initializing the datasource.
 * When a <code>getConnection(username, password)</code> request is processed
 * with a password that is different from those used to create connections in the
 * pool associated with <code>username</code>, an attempt is made to create a
 * new connection using the supplied password and if this succeeds, idle connections
 * created using the old password are destroyed and new connections are created
 * using the new password.</p>
 *
 * @author John D. McNally
 * @version $Id: SharedPoolDataSource.java 1649430 2015-01-04 21:29:32Z tn $
 * @since 2.0
 */
public class SharedPoolDataSource extends InstanceKeyDataSource {

    private static final long serialVersionUID = -1458539734480586454L;

    // Pool properties
    private int maxTotal = GenericKeyedObjectPoolConfig.DEFAULT_MAX_TOTAL;


    private transient KeyedObjectPool<UserPassKey,PooledConnectionAndInfo> pool = null;
    private transient KeyedCPDSConnectionFactory factory = null;

    /**
     * Default no-arg constructor for Serialization
     */
    public SharedPoolDataSource() {
    }

    /**
     * Close pool being maintained by this datasource.
     */
    @Override
    public void close() throws Exception {
        if (pool != null) {
            pool.close();
        }
        InstanceKeyDataSourceFactory.removeInstance(getInstanceKey());
    }


    // -------------------------------------------------------------------
    // Properties

    /**
     * Set {@link GenericKeyedObjectPool#getMaxTotal()} for this pool.
     */
    public int getMaxTotal() {
        return this.maxTotal;
    }

    /**
     * Get {@link GenericKeyedObjectPool#getMaxTotal()} for this pool.
     */
    public void setMaxTotal(int maxTotal) {
        assertInitializationAllowed();
        this.maxTotal = maxTotal;
    }


    // ----------------------------------------------------------------------
    // Instrumentation Methods

    /**
     * Get the number of active connections in the pool.
     */
    public int getNumActive() {
        return pool == null ? 0 : pool.getNumActive();
    }

    /**
     * Get the number of idle connections in the pool.
     */
    public int getNumIdle() {
        return pool == null ? 0 : pool.getNumIdle();
    }

    // ----------------------------------------------------------------------
    // Inherited abstract methods

    @Override
    protected PooledConnectionAndInfo
        getPooledConnectionAndInfo(String username, String password)
        throws SQLException {

        synchronized(this) {
            if (pool == null) {
                try {
                    registerPool(username, password);
                } catch (NamingException e) {
                    throw new SQLException("RegisterPool failed", e);
                }
            }
        }

        PooledConnectionAndInfo info = null;

        UserPassKey key = new UserPassKey(username, password);

        try {
            info = pool.borrowObject(key);
        }
        catch (Exception e) {
            throw new SQLException(
                    "Could not retrieve connection info from pool", e);
        }
        return info;
    }

    @Override
    protected PooledConnectionManager getConnectionManager(UserPassKey upkey)  {
        return factory;
    }

    /**
     * Returns a <code>SharedPoolDataSource</code> {@link Reference}.
     */
    @Override
    public Reference getReference() throws NamingException {
        Reference ref = new Reference(getClass().getName(),
            SharedPoolDataSourceFactory.class.getName(), null);
        ref.add(new StringRefAddr("instanceKey", getInstanceKey()));
        return ref;
    }

    private void registerPool(String username, String password)
            throws NamingException, SQLException {

        ConnectionPoolDataSource cpds = testCPDS(username, password);

        // Create an object pool to contain our PooledConnections
        factory = new KeyedCPDSConnectionFactory(cpds, getValidationQuery(),
                getValidationQueryTimeout(), isRollbackAfterValidation());
        factory.setMaxConnLifetimeMillis(getMaxConnLifetimeMillis());

        GenericKeyedObjectPoolConfig config =
                new GenericKeyedObjectPoolConfig();
        config.setBlockWhenExhausted(getDefaultBlockWhenExhausted());
        config.setEvictionPolicyClassName(getDefaultEvictionPolicyClassName());
        config.setLifo(getDefaultLifo());
        config.setMaxIdlePerKey(getDefaultMaxIdle());
        config.setMaxTotal(getMaxTotal());
        config.setMaxTotalPerKey(getDefaultMaxTotal());
        config.setMaxWaitMillis(getDefaultMaxWaitMillis());
        config.setMinEvictableIdleTimeMillis(
                getDefaultMinEvictableIdleTimeMillis());
        config.setMinIdlePerKey(getDefaultMinIdle());
        config.setNumTestsPerEvictionRun(getDefaultNumTestsPerEvictionRun());
        config.setSoftMinEvictableIdleTimeMillis(
                getDefaultSoftMinEvictableIdleTimeMillis());
        config.setTestOnCreate(getDefaultTestOnCreate());
        config.setTestOnBorrow(getDefaultTestOnBorrow());
        config.setTestOnReturn(getDefaultTestOnReturn());
        config.setTestWhileIdle(getDefaultTestWhileIdle());
        config.setTimeBetweenEvictionRunsMillis(
                getDefaultTimeBetweenEvictionRunsMillis());

        KeyedObjectPool<UserPassKey,PooledConnectionAndInfo> tmpPool =
                new GenericKeyedObjectPool<UserPassKey, PooledConnectionAndInfo>(factory, config);
        factory.setPool(tmpPool);
        pool = tmpPool;
    }

    @Override
    protected void setupDefaults(Connection con, String username) throws SQLException {
        Boolean defaultAutoCommit = isDefaultAutoCommit();
        if (defaultAutoCommit != null &&
                con.getAutoCommit() != defaultAutoCommit.booleanValue()) {
            con.setAutoCommit(defaultAutoCommit.booleanValue());
        }

        int defaultTransactionIsolation = getDefaultTransactionIsolation();
        if (defaultTransactionIsolation != UNKNOWN_TRANSACTIONISOLATION) {
            con.setTransactionIsolation(defaultTransactionIsolation);
        }

        Boolean defaultReadOnly = isDefaultReadOnly();
        if (defaultReadOnly != null &&
                con.isReadOnly() != defaultReadOnly.booleanValue()) {
            con.setReadOnly(defaultReadOnly.booleanValue());
        }
    }

    /**
     * Supports Serialization interface.
     *
     * @param in a <code>java.io.ObjectInputStream</code> value
     * @exception IOException if an error occurs
     * @exception ClassNotFoundException if an error occurs
     */
    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException {
        try
        {
            in.defaultReadObject();
            SharedPoolDataSource oldDS = (SharedPoolDataSource)
                new SharedPoolDataSourceFactory()
                    .getObjectInstance(getReference(), null, null, null);
            this.pool = oldDS.pool;
        }
        catch (NamingException e)
        {
            throw new IOException("NamingException: " + e);
        }
    }
}

