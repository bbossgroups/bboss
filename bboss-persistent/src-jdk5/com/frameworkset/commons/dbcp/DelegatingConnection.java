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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * A base delegating implementation of {@link Connection}.
 * <p>
 * All of the methods from the {@link Connection} interface
 * simply check to see that the {@link Connection} is active,
 * and call the corresponding method on the "delegate"
 * provided in my constructor.
 * <p>
 * Extends AbandonedTrace to implement Connection tracking and
 * logging of code which created the Connection. Tracking the
 * Connection ensures that the AbandonedObjectPool can close
 * this connection and recycle it if its pool of connections
 * is nearing exhaustion and this connection's last usage is
 * older than the removeAbandonedTimeout.
 *
 * @author Rodney Waldhoff
 * @author Glenn L. Nielsen
 * @author James House
 * @author Dirk Verbeeck
 * @version $Revision: 500687 $ $Date: 2007-01-27 16:33:47 -0700 (Sat, 27 Jan 2007) $
 */
public class DelegatingConnection extends AbandonedTrace
        implements Connection {
    /** My delegate {@link Connection}. */
    protected Connection _conn = null;

    protected boolean _closed = false;
    
    /**
     * Create a wrapper for the Connectin which traces this
     * Connection in the AbandonedObjectPool.
     *
     * @param c the {@link Connection} to delegate all calls to.
     */
    public DelegatingConnection(Connection c) {
        super();
        _conn = c;
    }

    /**
     * Create a wrapper for the Connection which traces
     * the Statements created so that any unclosed Statements
     * can be closed when this Connection is closed.
     *
     * @param c the {@link Connection} to delegate all calls to.
     * @param config the configuration for tracing abandoned objects
     * @deprecated AbandonedConfig is now deprecated.
     */
    public DelegatingConnection(Connection c, AbandonedConfig config) {
        super(config);
        _conn = c;
    }

    /**
     * Returns a string representation of the metadata associated with
     * the innnermost delegate connection.
     * 
     * @since 1.2.2
     */
    public String toString() {
        String s = null;
        
        Connection c = this.getInnermostDelegate();
        if (c != null) {
            try {
                if (c.isClosed()) {
                    s = "connection is closed";
                }
                else {
                    DatabaseMetaData meta = c.getMetaData();
                    if (meta != null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(meta.getURL());
                        sb.append(", UserName=");
                        sb.append(meta.getUserName());
                        sb.append(", ");
                        sb.append(meta.getDriverName());
                        s = sb.toString();
                    }
                }
            }
            catch (SQLException ex) {
                s = null;
            }
        }
        
        if (s == null) {
            s = super.toString();
        }
        
        return s;
    }

    /**
     * Returns my underlying {@link Connection}.
     * @return my underlying {@link Connection}.
     */
    public Connection getDelegate() {
        return _conn;
    }
    
    /**
     * Compares innermost delegate to the given connection.
     * 
     * @param c connection to compare innermost delegate with
     * @return true if innermost delegate equals <code>c</code>
     * @since 1.2.2
     */
    public boolean innermostDelegateEquals(Connection c) {
        Connection innerCon = getInnermostDelegate();
        if (innerCon == null) {
            return c == null;
        } else {
            return innerCon.equals(c);
        }
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        Connection delegate = getInnermostDelegate();
        if (delegate == null) {
            return false;
        }
        if (obj instanceof DelegatingConnection) {    
            DelegatingConnection c = (DelegatingConnection) obj;
            return c.innermostDelegateEquals(delegate);
        }
        else {
            return delegate.equals(obj);
        }
    }

    public int hashCode() {
        Object obj = getInnermostDelegate();
        if (obj == null) {
            return 0;
        }
        return obj.hashCode();
    }


    /**
     * If my underlying {@link Connection} is not a
     * <tt>DelegatingConnection</tt>, returns it,
     * otherwise recursively invokes this method on
     * my delegate.
     * <p>
     * Hence this method will return the first
     * delegate that is not a <tt>DelegatingConnection</tt>,
     * or <tt>null</tt> when no non-<tt>DelegatingConnection</tt>
     * delegate can be found by transversing this chain.
     * <p>
     * This method is useful when you may have nested
     * <tt>DelegatingConnection</tt>s, and you want to make
     * sure to obtain a "genuine" {@link Connection}.
     */
    public Connection getInnermostDelegate() {
        Connection c = _conn;
        while(c != null && c instanceof DelegatingConnection) {
            c = ((DelegatingConnection)c).getDelegate();
            if(this == c) {
                return null;
            }
        }
        return c;
    }

    /** Sets my delegate. */
    public void setDelegate(Connection c) {
        _conn = c;
    }

    /**
     * Closes the underlying connection, and close
     * any Statements that were not explicitly closed.
     */
    public void close() throws SQLException
    {
        passivate();
        _conn.close();
    }

    protected void handleException(SQLException e) throws SQLException {
        throw e;
    }

    public Statement createStatement() throws SQLException {
        checkOpen();
        try {
            return new DelegatingStatement(this, _conn.createStatement());
        }
        catch (SQLException e) {
            handleException(e);
            return null;
        }
    }

    public Statement createStatement(int resultSetType,
                                     int resultSetConcurrency) throws SQLException {
        checkOpen();
        try {
            return new DelegatingStatement
                (this, _conn.createStatement(resultSetType,resultSetConcurrency));
        }
        catch (SQLException e) {
            handleException(e);
            return null;
        }
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        checkOpen();
        try {
            return new DelegatingPreparedStatement
                (this, _conn.prepareStatement(sql));
        }
        catch (SQLException e) {
            handleException(e);
            return null;
        }
    }

    public PreparedStatement prepareStatement(String sql,
                                              int resultSetType,
                                              int resultSetConcurrency) throws SQLException {
        checkOpen();
        try {
            return new DelegatingPreparedStatement
                (this, _conn.prepareStatement
                    (sql,resultSetType,resultSetConcurrency));
        }
        catch (SQLException e) {
            handleException(e);
            return null;
        }
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        checkOpen();
        try {
            return new DelegatingCallableStatement(this, _conn.prepareCall(sql));
        }
        catch (SQLException e) {
            handleException(e);
            return null;
        }
    }

    public CallableStatement prepareCall(String sql,
                                         int resultSetType,
                                         int resultSetConcurrency) throws SQLException {
        checkOpen();
        try {
            return new DelegatingCallableStatement
                (this, _conn.prepareCall(sql, resultSetType,resultSetConcurrency));
        }
        catch (SQLException e) {
            handleException(e);
            return null;
        }
    }

    public void clearWarnings() throws SQLException
    { checkOpen(); try { _conn.clearWarnings(); } catch (SQLException e) { handleException(e); } }
    
    public void commit() throws SQLException
    { checkOpen(); try { _conn.commit(); } catch (SQLException e) { handleException(e); } }
    
    public boolean getAutoCommit() throws SQLException
    { checkOpen(); try { return _conn.getAutoCommit(); } catch (SQLException e) { handleException(e); return false; } 
    }
    public String getCatalog() throws SQLException
    { checkOpen(); try { return _conn.getCatalog(); } catch (SQLException e) { handleException(e); return null; } }
    
    public DatabaseMetaData getMetaData() throws SQLException
    { checkOpen(); try { return _conn.getMetaData(); } catch (SQLException e) { handleException(e); return null; } }
    
    public int getTransactionIsolation() throws SQLException
    { checkOpen(); try { return _conn.getTransactionIsolation(); } catch (SQLException e) { handleException(e); return -1; } }
    
    public Map getTypeMap() throws SQLException
    { checkOpen(); try { return _conn.getTypeMap(); } catch (SQLException e) { handleException(e); return null; } }
    
    public SQLWarning getWarnings() throws SQLException
    { checkOpen(); try { return _conn.getWarnings(); } catch (SQLException e) { handleException(e); return null; } }
    
    public boolean isReadOnly() throws SQLException
    { checkOpen(); try { return _conn.isReadOnly(); } catch (SQLException e) { handleException(e); return false; } }
    
    public String nativeSQL(String sql) throws SQLException
    { checkOpen(); try { return _conn.nativeSQL(sql); } catch (SQLException e) { handleException(e); return null; } }
    
    public void rollback() throws SQLException
    { checkOpen(); try {  _conn.rollback(); } catch (SQLException e) { handleException(e); } }
    
    public void setAutoCommit(boolean autoCommit) throws SQLException
    { checkOpen(); try { _conn.setAutoCommit(autoCommit); } catch (SQLException e) { handleException(e); } }

    public void setCatalog(String catalog) throws SQLException
    { checkOpen(); try { _conn.setCatalog(catalog); } catch (SQLException e) { handleException(e); } }

    public void setReadOnly(boolean readOnly) throws SQLException
    { checkOpen(); try { _conn.setReadOnly(readOnly); } catch (SQLException e) { handleException(e); } }

    public void setTransactionIsolation(int level) throws SQLException
    { checkOpen(); try { _conn.setTransactionIsolation(level); } catch (SQLException e) { handleException(e); } }

    public void setTypeMap(Map map) throws SQLException
    { checkOpen(); try { _conn.setTypeMap(map); } catch (SQLException e) { handleException(e); } }

    public boolean isClosed() throws SQLException {
         if(_closed || _conn.isClosed()) {
             return true;
         }
         return false;
    }

    protected void checkOpen() throws SQLException {
        if(_closed) {
            throw new SQLException
                ("Connection " + _conn + " is closed.");
        }
    }

    protected void activate() {
        _closed = false;
        setLastUsed();
        if(_conn instanceof DelegatingConnection) {
            ((DelegatingConnection)_conn).activate();
        }
    }

    protected void passivate() throws SQLException {
        try {
            // The JDBC spec requires that a Connection close any open
            // Statement's when it is closed.
            List statements = getTrace();
            if( statements != null) {
                Statement[] set = new Statement[statements.size()];
                statements.toArray(set);
                for (int i = 0; i < set.length; i++) {
                    set[i].close();
                }
                clearTrace();
            }
            setLastUsed(0);
            if(_conn instanceof DelegatingConnection) {
                ((DelegatingConnection)_conn).passivate();
            }
        }
        finally {
            _closed = true;
        }
    }

    // ------------------- JDBC 3.0 -----------------------------------------
    // Will be commented by the build process on a JDBC 2.0 system

/* JDBC_3_ANT_KEY_BEGIN */

    public int getHoldability() throws SQLException
    { checkOpen(); try { return _conn.getHoldability(); } catch (SQLException e) { handleException(e); return 0; } }

    public void setHoldability(int holdability) throws SQLException
    { checkOpen(); try { _conn.setHoldability(holdability); } catch (SQLException e) { handleException(e); } }

    public java.sql.Savepoint setSavepoint() throws SQLException
    { checkOpen(); try { return _conn.setSavepoint(); } catch (SQLException e) { handleException(e); return null; } }

    public java.sql.Savepoint setSavepoint(String name) throws SQLException
    { checkOpen(); try { return _conn.setSavepoint(name); } catch (SQLException e) { handleException(e); return null; } }

    public void rollback(java.sql.Savepoint savepoint) throws SQLException
    { checkOpen(); try { _conn.rollback(savepoint); } catch (SQLException e) { handleException(e); } }

    public void releaseSavepoint(java.sql.Savepoint savepoint) throws SQLException
    { checkOpen(); try { _conn.releaseSavepoint(savepoint); } catch (SQLException e) { handleException(e); } }

    public Statement createStatement(int resultSetType,
                                     int resultSetConcurrency,
                                     int resultSetHoldability) throws SQLException {
        checkOpen();
        try {
            return new DelegatingStatement(this, _conn.createStatement(
                resultSetType, resultSetConcurrency, resultSetHoldability));
        }
        catch (SQLException e) {
            handleException(e);
            return null;
        }
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType,
                                              int resultSetConcurrency,
                                              int resultSetHoldability) throws SQLException {
        checkOpen();
        try {
            return new DelegatingPreparedStatement(this, _conn.prepareStatement(
                sql, resultSetType, resultSetConcurrency, resultSetHoldability));
        }
        catch (SQLException e) {
            handleException(e);
            return null;
        }
    }

    public CallableStatement prepareCall(String sql, int resultSetType,
                                         int resultSetConcurrency,
                                         int resultSetHoldability) throws SQLException {
        checkOpen();
        try {
            return new DelegatingCallableStatement(this, _conn.prepareCall(
                sql, resultSetType, resultSetConcurrency, resultSetHoldability));
        }
        catch (SQLException e) {
            handleException(e);
            return null;
        }
    }

    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        checkOpen();
        try {
            return new DelegatingPreparedStatement(this, _conn.prepareStatement(
                sql, autoGeneratedKeys));
        }
        catch (SQLException e) {
            handleException(e);
            return null;
        }
    }

    public PreparedStatement prepareStatement(String sql, int columnIndexes[]) throws SQLException {
        checkOpen();
        try {
            return new DelegatingPreparedStatement(this, _conn.prepareStatement(
                sql, columnIndexes));
        }
        catch (SQLException e) {
            handleException(e);
            return null;
        }
    }

    public PreparedStatement prepareStatement(String sql, String columnNames[]) throws SQLException {
        checkOpen();
        try {
            return new DelegatingPreparedStatement(this, _conn.prepareStatement(
                sql, columnNames));
        }
        catch (SQLException e) {
            handleException(e);
            return null;
        }
    }
/* JDBC_3_ANT_KEY_END */
}
