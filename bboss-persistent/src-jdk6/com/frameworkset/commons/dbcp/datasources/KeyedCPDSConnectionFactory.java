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

package com.frameworkset.commons.dbcp.datasources;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;

import com.frameworkset.commons.pool.KeyedObjectPool;
import com.frameworkset.commons.pool.KeyedPoolableObjectFactory;

/**
 * A {*link PoolableObjectFactory} that creates
 * {*link PoolableConnection}s.
 *
 * @author John D. McNally
 * @version $Revision: 907288 $ $Date: 2010-02-06 14:42:58 -0500 (Sat, 06 Feb 2010) $
 */
class KeyedCPDSConnectionFactory
    implements KeyedPoolableObjectFactory, ConnectionEventListener, PooledConnectionManager {

    private static final String NO_KEY_MESSAGE
            = "close() was called on a Connection, but "
            + "I have no record of the underlying PooledConnection.";

    private final ConnectionPoolDataSource _cpds;
    private final String _validationQuery;
    private final boolean _rollbackAfterValidation;
    private final KeyedObjectPool _pool;
    
    /** 
     * Map of PooledConnections for which close events are ignored.
     * Connections are muted when they are being validated.
     */
    private final Map /* <PooledConnection, null> */ validatingMap = new HashMap();
    
    /**
     * Map of PooledConnectionAndInfo instances
     */
    private final WeakHashMap /* <PooledConnection, PooledConnectionAndInfo> */ pcMap = new WeakHashMap();

    /**
     * Create a new <tt>KeyedPoolableConnectionFactory</tt>.
     * @param cpds the ConnectionPoolDataSource from which to obtain PooledConnection's
     * @param pool the {*link ObjectPool} in which to pool those {*link Connection}s
     * @param validationQuery a query to use to {*link #validateObject validate} {*link Connection}s.
     * Should return at least one row. May be <tt>null</tt>
     */
    public KeyedCPDSConnectionFactory(ConnectionPoolDataSource cpds,
                                      KeyedObjectPool pool,
                                      String validationQuery) {
        this(cpds , pool, validationQuery, false);  
    }

    /**
     * Create a new <tt>KeyedPoolableConnectionFactory</tt>.
     * @param cpds the ConnectionPoolDataSource from which to obtain
     * PooledConnections
     * @param pool the {@link KeyedObjectPool} in which to pool those
     * {@link Connection}s
     * @param validationQuery a query to use to {@link #validateObject validate}
     * {@link Connection}s.  Should return at least one row. May be <tt>null</tt>
     * @param rollbackAfterValidation whether a rollback should be issued after
     * {@link #validateObject validating} {@link Connection}s.
     */
    public KeyedCPDSConnectionFactory(ConnectionPoolDataSource cpds, 
                                      KeyedObjectPool pool, 
                                      String validationQuery,
                                      boolean rollbackAfterValidation) {
        _cpds = cpds;
        _pool = pool;
        pool.setFactory(this);
        _validationQuery = validationQuery;
        _rollbackAfterValidation = rollbackAfterValidation;
    }
    
    /**
     * Returns the keyed object pool used to pool connections created by this factory.
     * 
     * @return KeyedObjectPool managing pooled connections
     */
    public KeyedObjectPool getPool() {
        return _pool;
    }

    /**
     * Creates a new {@link PooledConnectionAndInfo} from the given {@link UserPassKey}.
     * 
     * @param key {@link UserPassKey} containing user credentials
     * @throws SQLException if the connection could not be created.
     * @see com.frameworkset.commons.pool.KeyedPoolableObjectFactory#makeObject(java.lang.Object)
     */
    public synchronized Object makeObject(Object key) throws Exception {
        Object obj = null;
        UserPassKey upkey = (UserPassKey)key;

        PooledConnection pc = null;
        String username = upkey.getUsername();
        String password = upkey.getPassword();
        if (username == null) {
            pc = _cpds.getPooledConnection();
        } else {
            pc = _cpds.getPooledConnection(username, password);
        }

        if (pc == null) {
            throw new IllegalStateException("Connection pool data source returned null from getPooledConnection");
        }

        // should we add this object as a listener or the pool.
        // consider the validateObject method in decision
        pc.addConnectionEventListener(this);
        obj = new PooledConnectionAndInfo(pc, username, password);
        pcMap.put(pc, obj);

        return obj;
    }

    /**
     * Closes the PooledConnection and stops listening for events from it.
     */
    public void destroyObject(Object key, Object obj) throws Exception {
        if (obj instanceof PooledConnectionAndInfo) {
            PooledConnection pc = ((PooledConnectionAndInfo)obj).getPooledConnection();
            pc.removeConnectionEventListener(this);
            pcMap.remove(pc);
            pc.close(); 
        }
    }

    /**
     * Validates a pooled connection.
     * 
     * @param key ignored
     * @param obj {@link PooledConnectionAndInfo} containing the connection to validate
     * @return true if validation suceeds
     */
    public boolean validateObject(Object key, Object obj) {
        boolean valid = false;
        if (obj instanceof PooledConnectionAndInfo) {
            PooledConnection pconn =
                ((PooledConnectionAndInfo)obj).getPooledConnection();
            String query = _validationQuery;
            if (null != query) {
                Connection conn = null;
                Statement stmt = null;
                ResultSet rset = null;
                // logical Connection from the PooledConnection must be closed
                // before another one can be requested and closing it will
                // generate an event. Keep track so we know not to return
                // the PooledConnection
                validatingMap.put(pconn, null);
                try {
                    conn = pconn.getConnection();
                    stmt = conn.createStatement();
                    rset = stmt.executeQuery(query);
                    if (rset.next()) {
                        valid = true;
                    } else {
                        valid = false;
                    }
                    if (_rollbackAfterValidation) {
                        conn.rollback();
                    }
                } catch(Exception e) {
                    valid = false;
                } finally {
                    if (rset != null) {
                        try {
                            rset.close();
                        } catch (Throwable t) {
                            // ignore
                        }
                    }
                    if (stmt != null) {
                        try {
                            stmt.close();
                        } catch (Throwable t) {
                            // ignore
                        }
                    }
                    if (conn != null) {
                        try {
                            conn.close();
                        } catch (Throwable t) {
                            // ignore
                        }
                    }
                    validatingMap.remove(pconn);
                }
            } else {
                valid = true;
            }
        } else {
            valid = false;
        }
        return valid;
    }

    public void passivateObject(Object key, Object obj) {
    }

    public void activateObject(Object key, Object obj) {
    }

    // ***********************************************************************
    // java.sql.ConnectionEventListener implementation
    // ***********************************************************************

    /**
     * This will be called if the Connection returned by the getConnection
     * method came from a PooledConnection, and the user calls the close()
     * method of this connection object. What we need to do here is to
     * release this PooledConnection from our pool...
     */
    public void connectionClosed(ConnectionEvent event) {
        PooledConnection pc = (PooledConnection)event.getSource();
        // if this event occurred because we were validating, or if this
        // connection has been marked for removal, ignore it
        // otherwise return the connection to the pool.
        if (!validatingMap.containsKey(pc)) {
            PooledConnectionAndInfo info =
                (PooledConnectionAndInfo) pcMap.get(pc);
            if (info == null) {
                throw new IllegalStateException(NO_KEY_MESSAGE);
            }
            try {
                _pool.returnObject(info.getUserPassKey(), info);
            } catch (Exception e) {
                System.err.println("CLOSING DOWN CONNECTION AS IT COULD " +
                "NOT BE RETURNED TO THE POOL");
                pc.removeConnectionEventListener(this);
                try {
                    _pool.invalidateObject(info.getUserPassKey(), info);
                } catch (Exception e3) {
                    System.err.println("EXCEPTION WHILE DESTROYING OBJECT " +
                            info);
                    e3.printStackTrace();
                }
            }
        }
    }

    /**
     * If a fatal error occurs, close the underlying physical connection so as
     * not to be returned in the future
     */
    public void connectionErrorOccurred(ConnectionEvent event) {
        PooledConnection pc = (PooledConnection)event.getSource();
        if (null != event.getSQLException()) {
            System.err
                .println("CLOSING DOWN CONNECTION DUE TO INTERNAL ERROR (" +
                         event.getSQLException() + ")");
        }
        pc.removeConnectionEventListener(this);

        PooledConnectionAndInfo info = (PooledConnectionAndInfo) pcMap.get(pc);
        if (info == null) {
            throw new IllegalStateException(NO_KEY_MESSAGE);
        }
        try {
            _pool.invalidateObject(info.getUserPassKey(), info);
        } catch (Exception e) {
            System.err.println("EXCEPTION WHILE DESTROYING OBJECT " + info);
            e.printStackTrace();
        }
    }
    
    // ***********************************************************************
    // PooledConnectionManager implementation
    // ***********************************************************************
    
    /**
     * Invalidates the PooledConnection in the pool.  The KeyedCPDSConnectionFactory
     * closes the connection and pool counters are updated appropriately.
     * Also clears any idle instances associated with the username that was used
     * to create the PooledConnection.  Connections associated with this user
     * are not affected and they will not be automatically closed on return to the pool.
     */
    public void invalidate(PooledConnection pc) throws SQLException {
        PooledConnectionAndInfo info = (PooledConnectionAndInfo) pcMap.get(pc);
        if (info == null) {
            throw new IllegalStateException(NO_KEY_MESSAGE);
        }
        UserPassKey key = info.getUserPassKey();
        try {
            _pool.invalidateObject(key, info);  // Destroy and update pool counters
            _pool.clear(key); // Remove any idle instances with this key
        } catch (Exception ex) {
            throw (SQLException) new SQLException("Error invalidating connection").initCause(ex);
        }
    }
    
    /**
     * Does nothing.  This factory does not cache user credentials.
     */
    public void setPassword(String password) {
    }
    
    /**
     * This implementation does not fully close the KeyedObjectPool, as
     * this would affect all users.  Instead, it clears the pool associated
     * with the given user.  This method is not currently used.
     */
    public void closePool(String username) throws SQLException {
        try {
            _pool.clear(new UserPassKey(username, null));
        } catch (Exception ex) {
            throw (SQLException) new SQLException("Error closing connection pool").initCause(ex);
        } 
    }
    
}
