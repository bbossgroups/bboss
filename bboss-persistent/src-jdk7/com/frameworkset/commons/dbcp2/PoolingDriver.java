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
package com.frameworkset.commons.dbcp2;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import com.frameworkset.commons.pool2.ObjectPool;


/**
 * A {@link Driver} implementation that obtains
 * {@link Connection}s from a registered
 * {@link ObjectPool}.
 *
 * @author Rodney Waldhoff
 * @author Dirk Verbeeck
 * @version $Id: PoolingDriver.java 1649430 2015-01-04 21:29:32Z tn $
 * @since 2.0
 */
public class PoolingDriver implements Driver {
    /** Register myself with the {@link DriverManager}. */
    static {
        try {
            DriverManager.registerDriver(new PoolingDriver());
        } catch(Exception e) {
        }
    }

    /** The map of registered pools. */
    protected static final HashMap<String,ObjectPool<? extends Connection>> pools =
            new HashMap<String, ObjectPool<? extends Connection>>();

    /** Controls access to the underlying connection */
    private final boolean accessToUnderlyingConnectionAllowed;

    public PoolingDriver() {
        this(true);
    }

    /**
     * For unit testing purposes.
     */
    protected PoolingDriver(boolean accessToUnderlyingConnectionAllowed) {
        this.accessToUnderlyingConnectionAllowed = accessToUnderlyingConnectionAllowed;
    }


    /**
     * Returns the value of the accessToUnderlyingConnectionAllowed property.
     *
     * @return true if access to the underlying is allowed, false otherwise.
     */
    protected boolean isAccessToUnderlyingConnectionAllowed() {
        return accessToUnderlyingConnectionAllowed;
    }

    public synchronized ObjectPool<? extends Connection> getConnectionPool(String name)
            throws SQLException {
        ObjectPool<? extends Connection> pool = pools.get(name);
        if (null == pool) {
            throw new SQLException("Pool not registered.");
        }
        return pool;
    }

    public synchronized void registerPool(String name,
            ObjectPool<? extends Connection> pool) {
        pools.put(name,pool);
    }

    public synchronized void closePool(String name) throws SQLException {
        ObjectPool<? extends Connection> pool = pools.get(name);
        if (pool != null) {
            pools.remove(name);
            try {
                pool.close();
            }
            catch (Exception e) {
                throw new SQLException("Error closing pool " + name, e);
            }
        }
    }

    public synchronized String[] getPoolNames(){
        Set<String> names = pools.keySet();
        return names.toArray(new String[names.size()]);
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        try {
            return url.startsWith(URL_PREFIX);
        } catch(NullPointerException e) {
            return false;
        }
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        if(acceptsURL(url)) {
            ObjectPool<? extends Connection> pool =
                getConnectionPool(url.substring(URL_PREFIX_LEN));

            try {
                Connection conn = pool.borrowObject();
                if (conn == null) {
                    return null;
                }
                return new PoolGuardConnectionWrapper(pool, conn);
            } catch(SQLException e) {
                throw e;
            } catch(NoSuchElementException e) {
                throw new SQLException("Cannot get a connection, pool error: " + e.getMessage(), e);
            } catch(RuntimeException e) {
                throw e;
            } catch(Exception e) {
                throw new SQLException("Cannot get a connection, general error: " + e.getMessage(), e);
            }
        }
        return null;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Invalidates the given connection.
     *
     * @param conn connection to invalidate
     * @throws SQLException if the connection is not a
     * <code>PoolGuardConnectionWrapper</code> or an error occurs invalidating
     * the connection
     */
    public void invalidateConnection(Connection conn) throws SQLException {
        if (conn instanceof PoolGuardConnectionWrapper) { // normal case
            PoolGuardConnectionWrapper pgconn = (PoolGuardConnectionWrapper) conn;
            @SuppressWarnings("unchecked")
            ObjectPool<Connection> pool = (ObjectPool<Connection>) pgconn.pool;
            try {
                pool.invalidateObject(pgconn.getDelegateInternal());
            }
            catch (Exception e) {
            }
        }
        else {
            throw new SQLException("Invalid connection class");
        }
    }

    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    @Override
    public int getMinorVersion() {
        return MINOR_VERSION;
    }

    @Override
    public boolean jdbcCompliant() {
        return true;
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) {
        return new DriverPropertyInfo[0];
    }

    /** My URL prefix */
    protected static final String URL_PREFIX = "jdbc:apache:commons:dbcp:";
    protected static final int URL_PREFIX_LEN = URL_PREFIX.length();

    // version numbers
    protected static final int MAJOR_VERSION = 1;
    protected static final int MINOR_VERSION = 0;

    /**
     * PoolGuardConnectionWrapper is a Connection wrapper that makes sure a
     * closed connection cannot be used anymore.
     * @since 2.0
     */
    private class PoolGuardConnectionWrapper extends DelegatingConnection<Connection> {

        private final ObjectPool<? extends Connection> pool;

        PoolGuardConnectionWrapper(ObjectPool<? extends Connection> pool,
                Connection delegate) {
            super(delegate);
            this.pool = pool;
        }

        /**
         * @see com.frameworkset.commons.dbcp2.DelegatingConnection#getDelegate()
         */
        @Override
        public Connection getDelegate() {
            if (isAccessToUnderlyingConnectionAllowed()) {
                return super.getDelegate();
            }
            return null;
        }

        /**
         * @see com.frameworkset.commons.dbcp2.DelegatingConnection#getInnermostDelegate()
         */
        @Override
        public Connection getInnermostDelegate() {
            if (isAccessToUnderlyingConnectionAllowed()) {
                return super.getInnermostDelegate();
            }
            return null;
        }
    }
}
