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

package com.frameworkset.commons.dbcp2.cpdsadapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;

import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.PooledConnection;
import javax.sql.StatementEventListener;

import com.frameworkset.commons.dbcp2.DelegatingConnection;
import com.frameworkset.commons.dbcp2.PoolablePreparedStatement;
import com.frameworkset.commons.pool2.KeyedObjectPool;
import com.frameworkset.commons.pool2.KeyedPooledObjectFactory;
import com.frameworkset.commons.pool2.PooledObject;
import com.frameworkset.commons.pool2.impl.DefaultPooledObject;

/**
 * Implementation of PooledConnection that is returned by
 * PooledConnectionDataSource.
 *
 * @author John D. McNally
 * @version $Id: PooledConnectionImpl.java 1658644 2015-02-10 08:59:07Z tn $
 * @since 2.0
 */
class PooledConnectionImpl implements PooledConnection,
        KeyedPooledObjectFactory<PStmtKeyCPDS,PoolablePreparedStatement<PStmtKeyCPDS>> {

    private static final String CLOSED
            = "Attempted to use PooledConnection after closed() was called.";

    /**
     * The JDBC database connection that represents the physical db connection.
     */
    private Connection connection = null;

    /**
     * A DelegatingConnection used to create a PoolablePreparedStatementStub
     */
    private final DelegatingConnection<?> delegatingConnection;

    /**
     * The JDBC database logical connection.
     */
    private Connection logicalConnection = null;

    /**
     * ConnectionEventListeners
     */
    private final Vector<ConnectionEventListener> eventListeners;

    /**
     * StatementEventListeners
     */
    private final Vector<StatementEventListener> statementEventListeners =
            new Vector<StatementEventListener>();

    /**
     * flag set to true, once close() is called.
     */
    private boolean isClosed;

    /** My pool of {@link PreparedStatement}s. */
    private KeyedObjectPool<PStmtKeyCPDS, PoolablePreparedStatement<PStmtKeyCPDS>> pstmtPool = null;

    /**
     * Controls access to the underlying connection
     */
    private boolean accessToUnderlyingConnectionAllowed = false;

    /**
     * Wrap the real connection.
     * @param connection the connection to be wrapped
     */
    PooledConnectionImpl(Connection connection) {
        this.connection = connection;
        if (connection instanceof DelegatingConnection) {
            this.delegatingConnection = (DelegatingConnection<?>) connection;
        } else {
            this.delegatingConnection = new DelegatingConnection<Connection>(connection);
        }
        eventListeners = new Vector<ConnectionEventListener>();
        isClosed = false;
    }

    public void setStatementPool(
            KeyedObjectPool<PStmtKeyCPDS, PoolablePreparedStatement<PStmtKeyCPDS>> statementPool) {
        pstmtPool = statementPool;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addConnectionEventListener(ConnectionEventListener listener) {
        if (!eventListeners.contains(listener)) {
            eventListeners.add(listener);
        }
    }

    /* JDBC_4_ANT_KEY_BEGIN */
    @Override
    public void addStatementEventListener(StatementEventListener listener) {
        if (!statementEventListeners.contains(listener)) {
            statementEventListeners.add(listener);
        }
    }
    /* JDBC_4_ANT_KEY_END */

    /**
     * Closes the physical connection and marks this
     * <code>PooledConnection</code> so that it may not be used
     * to generate any more logical <code>Connection</code>s.
     *
     * @exception SQLException if an error occurs or the connection is already closed
     */
    @Override
    public void close() throws SQLException {
        assertOpen();
        isClosed = true;
        try {
            if (pstmtPool != null) {
                try {
                    pstmtPool.close();
                } finally {
                    pstmtPool = null;
                }
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new SQLException("Cannot close connection (return to pool failed)", e);
        } finally {
            try {
                connection.close();
            } finally {
                connection = null;
            }
        }
    }

    /**
     * Throws an SQLException, if isClosed is true
     */
    private void assertOpen() throws SQLException {
        if (isClosed) {
            throw new SQLException(CLOSED);
        }
    }

    /**
     * Returns a JDBC connection.
     *
     * @return The database connection.
     * @throws SQLException if the connection is not open or the previous logical connection is still open
     */
    @Override
    public Connection getConnection() throws SQLException {
        assertOpen();
        // make sure the last connection is marked as closed
        if (logicalConnection != null && !logicalConnection.isClosed()) {
            // should notify pool of error so the pooled connection can
            // be removed !FIXME!
            throw new SQLException("PooledConnection was reused, without"
                    + "its previous Connection being closed.");
        }

        // the spec requires that this return a new Connection instance.
        logicalConnection = new ConnectionImpl(
                this, connection, isAccessToUnderlyingConnectionAllowed());
        return logicalConnection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeConnectionEventListener(
            ConnectionEventListener listener) {
        eventListeners.remove(listener);
    }

    /* JDBC_4_ANT_KEY_BEGIN */
    @Override
    public void removeStatementEventListener(StatementEventListener listener) {
        statementEventListeners.remove(listener);
    }
    /* JDBC_4_ANT_KEY_END */

    /**
     * Closes the physical connection and checks that the logical connection
     * was closed as well.
     */
    @Override
    protected void finalize() throws Throwable {
        // Closing the Connection ensures that if anyone tries to use it,
        // an error will occur.
        try {
            connection.close();
        } catch (Exception ignored) {
        }

        // make sure the last connection is marked as closed
        if (logicalConnection != null && !logicalConnection.isClosed()) {
            throw new SQLException("PooledConnection was gc'ed, without"
                    + "its last Connection being closed.");
        }
    }

    /**
     * sends a connectionClosed event.
     */
    void notifyListeners() {
        ConnectionEvent event = new ConnectionEvent(this);
        Object[] listeners = eventListeners.toArray();
        for (Object listener : listeners) {
            ((ConnectionEventListener) listener).connectionClosed(event);
        }
    }

    // -------------------------------------------------------------------
    // The following code implements a PreparedStatement pool

    /**
     * Create or obtain a {@link PreparedStatement} from my pool.
     * @param sql the SQL statement
     * @return a {@link PoolablePreparedStatement}
     */
    PreparedStatement prepareStatement(String sql) throws SQLException {
        if (pstmtPool == null) {
            return connection.prepareStatement(sql);
        }
        try {
            return pstmtPool.borrowObject(createKey(sql));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new SQLException("Borrow prepareStatement from pool failed", e);
        }
    }

    /**
     * Create or obtain a {@link PreparedStatement} from my pool.
     * @param sql a <code>String</code> object that is the SQL statement to
     *            be sent to the database; may contain one or more '?' IN
     *            parameters
     * @param resultSetType a result set type; one of
     *         <code>ResultSet.TYPE_FORWARD_ONLY</code>,
     *         <code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or
     *         <code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
     * @param resultSetConcurrency a concurrency type; one of
     *         <code>ResultSet.CONCUR_READ_ONLY</code> or
     *         <code>ResultSet.CONCUR_UPDATABLE</code>
     *
     * @return a {@link PoolablePreparedStatement}
     * @see Connection#prepareStatement(String, int, int)
     */
    PreparedStatement prepareStatement(String sql, int resultSetType,
                                       int resultSetConcurrency)
            throws SQLException {
        if (pstmtPool == null) {
            return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
        }
        try {
            return pstmtPool.borrowObject(
                    createKey(sql,resultSetType,resultSetConcurrency));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new SQLException("Borrow prepareStatement from pool failed", e);
        }
    }

    /**
     * Create or obtain a {@link PreparedStatement} from my pool.
     * @param sql an SQL statement that may contain one or more '?' IN
     *        parameter placeholders
     * @param autoGeneratedKeys a flag indicating whether auto-generated keys
     *        should be returned; one of
     *        <code>Statement.RETURN_GENERATED_KEYS</code> or
     *        <code>Statement.NO_GENERATED_KEYS</code>
     * @return a {@link PoolablePreparedStatement}
     * @see Connection#prepareStatement(String, int)
     */
    PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
            throws SQLException {
        if (pstmtPool == null) {
            return connection.prepareStatement(sql, autoGeneratedKeys);
        }
        try {
            return pstmtPool.borrowObject(createKey(sql,autoGeneratedKeys));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new SQLException("Borrow prepareStatement from pool failed", e);
        }
    }

    PreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
    throws SQLException {
        if (pstmtPool == null) {
            return connection.prepareStatement(sql, resultSetType,
                    resultSetConcurrency, resultSetHoldability);
        }
        try {
            return pstmtPool.borrowObject(createKey(sql, resultSetType,
                    resultSetConcurrency, resultSetHoldability));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new SQLException("Borrow prepareStatement from pool failed", e);
        }
    }

    PreparedStatement prepareStatement(String sql, int columnIndexes[])
    throws SQLException {
        if (pstmtPool == null) {
            return connection.prepareStatement(sql, columnIndexes);
        }
        try {
            return pstmtPool.borrowObject(createKey(sql, columnIndexes));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new SQLException("Borrow prepareStatement from pool failed", e);
        }
    }

    PreparedStatement prepareStatement(String sql, String columnNames[])
    throws SQLException {
        if (pstmtPool == null) {
            return connection.prepareStatement(sql, columnNames);
        }
        try {
            return pstmtPool.borrowObject(createKey(sql, columnNames));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new SQLException("Borrow prepareStatement from pool failed", e);
        }
    }

    /**
     * Create a {@link PooledConnectionImpl.PStmtKey} for the given arguments.
     */
    protected PStmtKeyCPDS createKey(String sql, int autoGeneratedKeys) {
        return new PStmtKeyCPDS(normalizeSQL(sql), autoGeneratedKeys);
    }

    /**
     * Create a {@link PooledConnectionImpl.PStmtKey} for the given arguments.
     */
    protected PStmtKeyCPDS createKey(String sql, int resultSetType,
            int resultSetConcurrency, int resultSetHoldability) {
        return new PStmtKeyCPDS(normalizeSQL(sql), resultSetType,
                resultSetConcurrency, resultSetHoldability);
    }

    /**
     * Create a {@link PooledConnectionImpl.PStmtKey} for the given arguments.
     */
    protected PStmtKeyCPDS createKey(String sql, int columnIndexes[]) {
        return new PStmtKeyCPDS(normalizeSQL(sql), columnIndexes);
    }

    /**
     * Create a {@link PooledConnectionImpl.PStmtKey} for the given arguments.
     */
    protected PStmtKeyCPDS createKey(String sql, String columnNames[]) {
        return new PStmtKeyCPDS(normalizeSQL(sql), columnNames);
    }

    /**
     * Create a {@link PooledConnectionImpl.PStmtKey} for the given arguments.
     */
    protected PStmtKeyCPDS createKey(String sql, int resultSetType,
                               int resultSetConcurrency) {
        return new PStmtKeyCPDS(normalizeSQL(sql), resultSetType,
                            resultSetConcurrency);
    }

    /**
     * Create a {@link PooledConnectionImpl.PStmtKey} for the given arguments.
     */
    protected PStmtKeyCPDS createKey(String sql) {
        return new PStmtKeyCPDS(normalizeSQL(sql));
    }

    /**
     * Normalize the given SQL statement, producing a
     * canonical form that is semantically equivalent to the original.
     */
    protected String normalizeSQL(String sql) {
        return sql.trim();
    }

    /**
     * My {@link KeyedPooledObjectFactory} method for creating
     * {@link PreparedStatement}s.
     * @param key the key for the {@link PreparedStatement} to be created
     */
    @Override
    public PooledObject<PoolablePreparedStatement<PStmtKeyCPDS>> makeObject(PStmtKeyCPDS key) throws Exception {
        if (null == key) {
            throw new IllegalArgumentException();
        }
        // _openPstmts++;
        if (null == key.getResultSetType()
                && null == key.getResultSetConcurrency()) {
            if (null == key.getAutoGeneratedKeys()) {
                return new DefaultPooledObject<PoolablePreparedStatement<PStmtKeyCPDS>>(new PoolablePreparedStatement<PStmtKeyCPDS>(
                        connection.prepareStatement(key.getSql()),
                        key, pstmtPool, delegatingConnection));
            }
            return new DefaultPooledObject<PoolablePreparedStatement<PStmtKeyCPDS>>(new PoolablePreparedStatement<PStmtKeyCPDS>(
                            connection.prepareStatement(key.getSql(),
                                    key.getAutoGeneratedKeys().intValue()),
                            key, pstmtPool, delegatingConnection));
        }
        return new DefaultPooledObject<PoolablePreparedStatement<PStmtKeyCPDS>>(new PoolablePreparedStatement<PStmtKeyCPDS>(
                connection.prepareStatement(key.getSql(),
                        key.getResultSetType().intValue(),
                        key.getResultSetConcurrency().intValue()),
                        key, pstmtPool, delegatingConnection));
    }

    /**
     * My {@link KeyedPooledObjectFactory} method for destroying
     * {@link PreparedStatement}s.
     * @param key ignored
     * @param p the wrapped {@link PreparedStatement} to be destroyed.
     */
    @Override
    public void destroyObject(PStmtKeyCPDS key,
            PooledObject<PoolablePreparedStatement<PStmtKeyCPDS>> p)
            throws Exception {
        p.getObject().getInnermostDelegate().close();
    }

    /**
     * My {@link KeyedPooledObjectFactory} method for validating
     * {@link PreparedStatement}s.
     * @param key ignored
     * @param p ignored
     * @return {@code true}
     */
    @Override
    public boolean validateObject(PStmtKeyCPDS key,
            PooledObject<PoolablePreparedStatement<PStmtKeyCPDS>> p) {
        return true;
    }

    /**
     * My {@link KeyedPooledObjectFactory} method for activating
     * {@link PreparedStatement}s.
     * @param key ignored
     * @param p ignored
     */
    @Override
    public void activateObject(PStmtKeyCPDS key,
            PooledObject<PoolablePreparedStatement<PStmtKeyCPDS>> p)
            throws Exception {
        p.getObject().activate();
    }

    /**
     * My {@link KeyedPooledObjectFactory} method for passivating
     * {@link PreparedStatement}s.  Currently invokes {@link PreparedStatement#clearParameters}.
     * @param key ignored
     * @param p a wrapped {@link PreparedStatement}
     */
    @Override
    public void passivateObject(PStmtKeyCPDS key,
            PooledObject<PoolablePreparedStatement<PStmtKeyCPDS>> p)
            throws Exception {
        PoolablePreparedStatement<PStmtKeyCPDS> ppss = p.getObject();
        ppss.clearParameters();
        ppss.passivate();
    }

    /**
     * Returns the value of the accessToUnderlyingConnectionAllowed property.
     *
     * @return true if access to the underlying is allowed, false otherwise.
     */
    public synchronized boolean isAccessToUnderlyingConnectionAllowed() {
        return this.accessToUnderlyingConnectionAllowed;
    }

    /**
     * Sets the value of the accessToUnderlyingConnectionAllowed property.
     * It controls if the PoolGuard allows access to the underlying connection.
     * (Default: false)
     *
     * @param allow Access to the underlying connection is granted when true.
     */
    public synchronized void setAccessToUnderlyingConnectionAllowed(boolean allow) {
        this.accessToUnderlyingConnectionAllowed = allow;
    }
}
