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

import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;

import org.xml.sax.SAXException;

import com.frameworkset.commons.jocl.JOCLContentHandler;
import com.frameworkset.commons.pool.ObjectPool;


/**
 * A {@link Driver} implementation that obtains
 * {@link Connection}s from a registered
 * {@link ObjectPool}.
 *
 * @author Rodney Waldhoff
 * @author Dirk Verbeeck
 * @version $Revision: 500687 $ $Date: 2007-01-27 16:33:47 -0700 (Sat, 27 Jan 2007) $
 */
public class PoolingDriver implements Driver {
    /** Register an myself with the {@link DriverManager}. */
    static {
        try {
            DriverManager.registerDriver(new PoolingDriver());
        } catch(Exception e) {
        }
    }

    /** The map of registered pools. */
    protected static HashMap _pools = new HashMap();

    /** Controls access to the underlying connection */
    private static boolean accessToUnderlyingConnectionAllowed = false; 

    public PoolingDriver() {
    }

    /**
     * Returns the value of the accessToUnderlyingConnectionAllowed property.
     * 
     * @return true if access to the underlying is allowed, false otherwise.
     */
    public static synchronized boolean isAccessToUnderlyingConnectionAllowed() {
        return accessToUnderlyingConnectionAllowed;
    }

    /**
     * Sets the value of the accessToUnderlyingConnectionAllowed property.
     * It controls if the PoolGuard allows access to the underlying connection.
     * (Default: false)
     * 
     * @param allow Access to the underlying connection is granted when true.
     */
    public static synchronized void setAccessToUnderlyingConnectionAllowed(boolean allow) {
        accessToUnderlyingConnectionAllowed = allow;
    }

    /**
     * WARNING: This method throws DbcpExceptions (RuntimeExceptions)
     * and will be replaced by the protected getConnectionPool method.
     * 
     * @deprecated This will be removed in a future version of DBCP.
     */
    public synchronized ObjectPool getPool(String name) {
        try {
            return getConnectionPool(name);
        }
        catch (Exception e) {
            throw new DbcpException(e);
        }
    }
    
    public synchronized ObjectPool getConnectionPool(String name) throws SQLException {
        ObjectPool pool = (ObjectPool)(_pools.get(name));
        if(null == pool) {
            InputStream in = this.getClass().getResourceAsStream(String.valueOf(name) + ".jocl");
            if(null != in) {
                JOCLContentHandler jocl = null;
                try {
                    jocl = JOCLContentHandler.parse(in);
                }
                catch (SAXException e) {
                    throw new SQLNestedException("Could not parse configuration file", e);
                }
                catch (IOException e) {
                    throw new SQLNestedException("Could not load configuration file", e);
                }
                if(jocl.getType(0).equals(String.class)) {
                    pool = getPool((String)(jocl.getValue(0)));
                    if(null != pool) {
                        registerPool(name,pool);
                    }
                } else {
                    pool = ((PoolableConnectionFactory)(jocl.getValue(0))).getPool();
                    if(null != pool) {
                        registerPool(name,pool);
                    }
                }
            }
            else {
                throw new SQLException("Configuration file not found");
            }
        }
        return pool;
    }

    public synchronized void registerPool(String name, ObjectPool pool) {
        _pools.put(name,pool);
    }

    public synchronized void closePool(String name) throws SQLException {
        ObjectPool pool = (ObjectPool) _pools.get(name);
        if (pool != null) {
            _pools.remove(name);
            try {
                pool.close();
            }
            catch (Exception e) {
                throw new SQLNestedException("Error closing pool " + name, e);
            }
        }
    }
    
    public synchronized String[] getPoolNames() throws SQLException{
        Set names = _pools.keySet();
        return (String[]) names.toArray(new String[names.size()]);
    }

    public boolean acceptsURL(String url) throws SQLException {
        try {
            return url.startsWith(URL_PREFIX);
        } catch(NullPointerException e) {
            return false;
        }
    }

    public Connection connect(String url, Properties info) throws SQLException {
        if(acceptsURL(url)) {
            ObjectPool pool = getConnectionPool(url.substring(URL_PREFIX_LEN));
            if(null == pool) {
                throw new SQLException("No pool found for " + url + ".");
            } else {
                try {
                    Connection conn = (Connection)(pool.borrowObject());
                    if (conn != null) {
                        conn = new PoolGuardConnectionWrapper(pool, conn);
                    } 
                    return conn;
                } catch(SQLException e) {
                    throw e;
                } catch(NoSuchElementException e) {
                    throw new SQLNestedException("Cannot get a connection, pool error: " + e.getMessage(), e);
                } catch(RuntimeException e) {
                    throw e;
                } catch(Exception e) {
                    throw new SQLNestedException("Cannot get a connection, general error: " + e.getMessage(), e);
                }
            }
        } else {
            return null;
        }
    }

    /**
     * Invalidates the given connection.
     * 
     * @param conn connection to invalidate
     * @throws SQLException if the connection is not a 
     * <code>PoolGuardConnectionWrapper</code> or an error occurs invalidating
     * the connection
     * @since 1.2.2
     */
    public void invalidateConnection(Connection conn) throws SQLException {
        if (conn instanceof PoolGuardConnectionWrapper) { // normal case
            PoolGuardConnectionWrapper pgconn = (PoolGuardConnectionWrapper) conn;
            ObjectPool pool = pgconn.pool;
            Connection delegate = pgconn.delegate;
            try {
                pool.invalidateObject(delegate);
            } 
            catch (Exception e) { 
            }
            pgconn.delegate = null;
        }
        else {
            throw new SQLException("Invalid connection class");
        }
    }

    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    public int getMinorVersion() {
        return MINOR_VERSION;
    }

    public boolean jdbcCompliant() {
        return true;
    }

    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) {
        return new DriverPropertyInfo[0];
    }

    /** My URL prefix */
    protected static String URL_PREFIX = "jdbc:apache:commons:dbcp:";
    protected static int URL_PREFIX_LEN = URL_PREFIX.length();

    // version numbers
    protected static int MAJOR_VERSION = 1;
    protected static int MINOR_VERSION = 0;

    /**
     * PoolGuardConnectionWrapper is a Connection wrapper that makes sure a 
     * closed connection cannot be used anymore.
     */
    private class PoolGuardConnectionWrapper extends DelegatingConnection {

        private ObjectPool pool;
        private Connection delegate;
    
        PoolGuardConnectionWrapper(ObjectPool pool, Connection delegate) {
            super(delegate);
            this.pool = pool;
            this.delegate = delegate;
        }

        protected void checkOpen() throws SQLException {
            if(delegate == null) {
                throw new SQLException("Connection is closed.");
            }
        }
    
        public void close() throws SQLException {
            checkOpen();
            this.delegate.close();
            this.delegate = null;
            super.setDelegate(null);
        }

        public boolean isClosed() throws SQLException {
            if (delegate == null) {
                return true;
            }
            return delegate.isClosed();
        }

        public void clearWarnings() throws SQLException {
            checkOpen();
            delegate.clearWarnings();
        }

        public void commit() throws SQLException {
            checkOpen();
            delegate.commit();
        }

        public Statement createStatement() throws SQLException {
            checkOpen();
            return delegate.createStatement();
        }

        public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
            checkOpen();
            return delegate.createStatement(resultSetType, resultSetConcurrency);
        }

        public boolean equals(Object obj) {
            if (delegate == null){
                return false;
            }
            return delegate.equals(obj);
        }

        public boolean getAutoCommit() throws SQLException {
            checkOpen();
            return delegate.getAutoCommit();
        }

        public String getCatalog() throws SQLException {
            checkOpen();
            return delegate.getCatalog();
        }

        public DatabaseMetaData getMetaData() throws SQLException {
            checkOpen();
            return delegate.getMetaData();
        }

        public int getTransactionIsolation() throws SQLException {
            checkOpen();
            return delegate.getTransactionIsolation();
        }

        public Map getTypeMap() throws SQLException {
            checkOpen();
            return delegate.getTypeMap();
        }

        public SQLWarning getWarnings() throws SQLException {
            checkOpen();
            return delegate.getWarnings();
        }

        public int hashCode() {
            if (delegate == null){
                return 0;
            }
            return delegate.hashCode();
        }

        public boolean isReadOnly() throws SQLException {
            checkOpen();
            return delegate.isReadOnly();
        }

        public String nativeSQL(String sql) throws SQLException {
            checkOpen();
            return delegate.nativeSQL(sql);
        }

        public CallableStatement prepareCall(String sql) throws SQLException {
            checkOpen();
            return delegate.prepareCall(sql);
        }

        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
            checkOpen();
            return delegate.prepareCall(sql, resultSetType, resultSetConcurrency);
        }

        public PreparedStatement prepareStatement(String sql) throws SQLException {
            checkOpen();
            return delegate.prepareStatement(sql);
        }

        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
            checkOpen();
            return delegate.prepareStatement(sql, resultSetType, resultSetConcurrency);
        }

        public void rollback() throws SQLException {
            checkOpen();
            delegate.rollback();
        }

        public void setAutoCommit(boolean autoCommit) throws SQLException {
            checkOpen();
            delegate.setAutoCommit(autoCommit);
        }

        public void setCatalog(String catalog) throws SQLException {
            checkOpen();
            delegate.setCatalog(catalog);
        }

        public void setReadOnly(boolean readOnly) throws SQLException {
            checkOpen();
            delegate.setReadOnly(readOnly);
        }

        public void setTransactionIsolation(int level) throws SQLException {
            checkOpen();
            delegate.setTransactionIsolation(level);
        }

        public void setTypeMap(Map map) throws SQLException {
            checkOpen();
            delegate.setTypeMap(map);
        }

        public String toString() {
            if (delegate == null){
                return null;
            }
            return delegate.toString();
        }

        // ------------------- JDBC 3.0 -----------------------------------------
        // Will be commented by the build process on a JDBC 2.0 system

/* JDBC_3_ANT_KEY_BEGIN */

        public int getHoldability() throws SQLException {
            checkOpen();
            return delegate.getHoldability();
        }
    
        public void setHoldability(int holdability) throws SQLException {
            checkOpen();
            delegate.setHoldability(holdability);
        }

        public java.sql.Savepoint setSavepoint() throws SQLException {
            checkOpen();
            return delegate.setSavepoint();
        }

        public java.sql.Savepoint setSavepoint(String name) throws SQLException {
            checkOpen();
            return delegate.setSavepoint(name);
        }

        public void releaseSavepoint(java.sql.Savepoint savepoint) throws SQLException {
            checkOpen();
            delegate.releaseSavepoint(savepoint);
        }

        public void rollback(java.sql.Savepoint savepoint) throws SQLException {
            checkOpen();
            delegate.rollback(savepoint);
        }

        public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            checkOpen();
            return delegate.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            checkOpen();
            return delegate.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
            checkOpen();
            return delegate.prepareStatement(sql, autoGeneratedKeys);
        }

        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            checkOpen();
            return delegate.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
            checkOpen();
            return delegate.prepareStatement(sql, columnIndexes);
        }

        public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
            checkOpen();
            return delegate.prepareStatement(sql, columnNames);
        }

/* JDBC_3_ANT_KEY_END */

        /**
         * @see com.frameworkset.commons.dbcp.DelegatingConnection#getDelegate()
         */
        public Connection getDelegate() {
            if (isAccessToUnderlyingConnectionAllowed()) {
                return super.getDelegate();
            } else {
                return null;
            }
        }

        /**
         * @see com.frameworkset.commons.dbcp.DelegatingConnection#getInnermostDelegate()
         */
        public Connection getInnermostDelegate() {
            if (isAccessToUnderlyingConnectionAllowed()) {
                return super.getInnermostDelegate();
            } else {
                return null;
            }
        }
    }
}
