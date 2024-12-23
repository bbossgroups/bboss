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

import com.frameworkset.commons.pool.ObjectPool;
import com.frameworkset.commons.pool.PoolableObjectFactory;

/**
 * A {@link PoolableObjectFactory} that creates
 * {@link PoolableConnection}s.
 *
 * @author John D. McNally
 * @version $Revision: 907288 $ $Date: 2010-02-06 14:42:58 -0500 (Sat, 06 Feb 2010) $
 */
class CPDSConnectionFactory
        implements PoolableObjectFactory, ConnectionEventListener, PooledConnectionManager {

    private static final String NO_KEY_MESSAGE
            = "close() was called on a Connection, but "
            + "I have no record of the underlying PooledConnection.";

    private final ConnectionPoolDataSource _cpds;
    private final String _validationQuery;
    private final boolean _rollbackAfterValidation;
    private final ObjectPool _pool;
    private String _username = null;
    private String _password = null;

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
     * Create a new <tt>PoolableConnectionFactory</tt>.
     * 
     * @param cpds the ConnectionPoolDataSource from which to obtain
     * PooledConnection's
     * @param pool the {@link ObjectPool} in which to pool those
     * {@link Connection}s
     * @param validationQuery a query to use to {@link #validateObject validate}
     * {@link Connection}s. Should return at least one row. May be 
     * <tt>null</tt>
     * @param username
     * @param password
     */
    public CPDSConnectionFactory(ConnectionPoolDataSource cpds,
                                 ObjectPool pool,
                                 String validationQuery,
                                 String username,
                                 String password) {
        this(cpds, pool, validationQuery, false, username, password);
    }
    
    /**
     * Create a new <tt>PoolableConnectionFactory</tt>.
     * 
     * @param cpds the ConnectionPoolDataSource from which to obtain
     * PooledConnection's
     * @param pool the {@link ObjectPool} in which to pool those {@link
     * Connection}s
     * @param validationQuery a query to use to {@link #validateObject
     * validate} {@link Connection}s. Should return at least one row.
     * May be <tt>null</tt>
     * @param rollbackAfterValidation whether a rollback should be issued
     * after {@link #validateObject validating} {@link Connection}s.
     * @param username
     * @param password
     */
     public CPDSConnectionFactory(ConnectionPoolDataSource cpds,
                                  ObjectPool pool,
                                  String validationQuery,
                                  boolean rollbackAfterValidation,
                                  String username,
                                  String password) {
         _cpds = cpds;
         _pool = pool;
         pool.setFactory(this);
         _validationQuery = validationQuery;
         _username = username;
         _password = password;
         _rollbackAfterValidation = rollbackAfterValidation;
     }
     
     /**
      * Returns the object pool used to pool connections created by this factory.
      * 
      * @return ObjectPool managing pooled connections
      */
     public ObjectPool getPool() {
         return _pool;
     }

    public synchronized Object makeObject() {
        Object obj;
        try {
            PooledConnection pc = null;
            if (_username == null) {
                pc = _cpds.getPooledConnection();
            } else {
                pc = _cpds.getPooledConnection(_username, _password);
            }

            if (pc == null) {
                throw new IllegalStateException("Connection pool data source returned null from getPooledConnection");
            }

            // should we add this object as a listener or the pool.
            // consider the validateObject method in decision
            pc.addConnectionEventListener(this);
            obj = new PooledConnectionAndInfo(pc, _username, _password);
            pcMap.put(pc, obj);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return obj;
    }

    /**
     * Closes the PooledConnection and stops listening for events from it.
     */
    public void destroyObject(Object obj) throws Exception {
        if (obj instanceof PooledConnectionAndInfo) {
            PooledConnection pc = ((PooledConnectionAndInfo)obj).getPooledConnection();
            pc.removeConnectionEventListener(this);
            pcMap.remove(pc);
            pc.close(); 
        }
    }

    public boolean validateObject(Object obj) {
        boolean valid = false;
        if (obj instanceof PooledConnectionAndInfo) {
            PooledConnection pconn =
                ((PooledConnectionAndInfo) obj).getPooledConnection();
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
                } catch (Exception e) {
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

    public void passivateObject(Object obj) {
    }

    public void activateObject(Object obj) {
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
        PooledConnection pc = (PooledConnection) event.getSource();
        // if this event occured becase we were validating, ignore it
        // otherwise return the connection to the pool.
        if (!validatingMap.containsKey(pc)) {
            Object info = pcMap.get(pc);
            if (info == null) {
                throw new IllegalStateException(NO_KEY_MESSAGE);
            }

            try {
                _pool.returnObject(info);
            } catch (Exception e) {
                System.err.println("CLOSING DOWN CONNECTION AS IT COULD "
                        + "NOT BE RETURNED TO THE POOL");
                pc.removeConnectionEventListener(this);
                try {
                    destroyObject(info);
                } catch (Exception e2) {
                    System.err.println("EXCEPTION WHILE DESTROYING OBJECT "
                            + info);
                    e2.printStackTrace();
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
            System.err.println(
                    "CLOSING DOWN CONNECTION DUE TO INTERNAL ERROR ("
                    + event.getSQLException() + ")");
        }
        pc.removeConnectionEventListener(this);

        Object info = pcMap.get(pc);
        if (info == null) {
            throw new IllegalStateException(NO_KEY_MESSAGE);
        }
        try {
            _pool.invalidateObject(info);
        } catch (Exception e) {
            System.err.println("EXCEPTION WHILE DESTROYING OBJECT " + info);
            e.printStackTrace();
        }
    }
    
    // ***********************************************************************
    // PooledConnectionManager implementation
    // ***********************************************************************
    
    /**
     * Invalidates the PooledConnection in the pool.  The CPDSConnectionFactory
     * closes the connection and pool counters are updated appropriately.
     * Also closes the pool.  This ensures that all idle connections are closed
     * and connections that are checked out are closed on return.
     */
    public void invalidate(PooledConnection pc) throws SQLException {
        Object info = pcMap.get(pc);
        if (info == null) {
            throw new IllegalStateException(NO_KEY_MESSAGE);
        }
        try {
            _pool.invalidateObject(info);  // Destroy instance and update pool counters
            _pool.close();  // Clear any other instances in this pool and kill others as they come back
        } catch (Exception ex) {
            throw (SQLException) new SQLException("Error invalidating connection").initCause(ex);
        }   
    }
    
    /**
     * Sets the database password used when creating new connections.
     * 
     * @param password new password
     */
    public synchronized void setPassword(String password) {
        _password = password;
    }
    
    /**
     * Verifies that the username matches the user whose connections are being managed by this
     * factory and closes the pool if this is the case; otherwise does nothing.
     */
    public void closePool(String username) throws SQLException {
        synchronized (this) {
            if (username == null || !username.equals(_username)) {
                return;
            }
        }
        try {
            _pool.close();
        } catch (Exception ex) {
            throw (SQLException) new SQLException("Error closing connection pool").initCause(ex);
        } 
    }
    
}
