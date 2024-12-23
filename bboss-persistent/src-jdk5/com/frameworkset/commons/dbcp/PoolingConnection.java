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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import com.frameworkset.commons.pool.KeyedObjectPool;
import com.frameworkset.commons.pool.KeyedPoolableObjectFactory;

/**
 * A {@link DelegatingConnection} that pools {@link PreparedStatement}s.
 * <p>
 * My {@link #prepareStatement} methods, rather than creating a new {@link PreparedStatement}
 * each time, may actually pull the {@link PreparedStatement} from a pool of unused statements.
 * The {@link PreparedStatement#close} method of the returned {@link PreparedStatement} doesn't
 * actually close the statement, but rather returns it to my pool. (See {@link PoolablePreparedStatement}.)
 *
 * @see PoolablePreparedStatement
 * @author Rodney Waldhoff
 * @author Dirk Verbeeck
 * @version $Revision: 498524 $ $Date: 2007-01-21 21:44:45 -0700 (Sun, 21 Jan 2007) $
 */
public class PoolingConnection extends DelegatingConnection implements Connection, KeyedPoolableObjectFactory {
    /** My pool of {@link PreparedStatement}s. */
    protected KeyedObjectPool _pstmtPool = null;

    /**
     * Constructor.
     * @param c the underlying {@link Connection}.
     */
    public PoolingConnection(Connection c) {
        super(c);
    }

    /**
     * Constructor.
     * @param c the underlying {@link Connection}.
     * @param pool {@link KeyedObjectPool} of {@link PreparedStatement}s
     */
    public PoolingConnection(Connection c, KeyedObjectPool pool) {
        super(c);
        _pstmtPool = pool;
    }


    /**
     * Close and free all {@link PreparedStatement}s from my pool, and
     * close my underlying connection.
     */
    public synchronized void close() throws SQLException {
        if(null != _pstmtPool) {
            KeyedObjectPool oldpool = _pstmtPool;            
            _pstmtPool = null;
            try {
                oldpool.close();
            } catch(RuntimeException e) {
                throw e;
            } catch(SQLException e) {
                throw e;
            } catch(Exception e) {
                throw new SQLNestedException("Cannot close connection", e);
            }
        }
        getInnermostDelegate().close();
    }

    /**
     * Create or obtain a {@link PreparedStatement} from my pool.
     * @return a {@link PoolablePreparedStatement}
     */
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        try {
            return(PreparedStatement)(_pstmtPool.borrowObject(createKey(sql)));
        } catch(NoSuchElementException e) {
            throw new SQLNestedException("MaxOpenPreparedStatements limit reached", e); 
        } catch(RuntimeException e) {
            throw e;
        } catch(Exception e) {
            throw new SQLNestedException("Borrow prepareStatement from pool failed", e);
        }
    }

    /**
     * Create or obtain a {@link PreparedStatement} from my pool.
     * @return a {@link PoolablePreparedStatement}
     */
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        try {
            return(PreparedStatement)(_pstmtPool.borrowObject(createKey(sql,resultSetType,resultSetConcurrency)));
        } catch(NoSuchElementException e) {
            throw new SQLNestedException("MaxOpenPreparedStatements limit reached", e); 
        } catch(RuntimeException e) {
            throw e;
        } catch(Exception e) {
            throw new SQLNestedException("Borrow prepareStatement from pool failed", e);
        }
    }

    // ------------------- JDBC 3.0 -----------------------------------------
    // Will be commented by the build process on a JDBC 2.0 system

/* JDBC_3_ANT_KEY_BEGIN */

//    TODO: possible enhancement, cache these preparedStatements as well

//    public PreparedStatement prepareStatement(String sql, int resultSetType,
//                                              int resultSetConcurrency,
//                                              int resultSetHoldability)
//        throws SQLException {
//        return super.prepareStatement(
//            sql, resultSetType, resultSetConcurrency, resultSetHoldability);
//    }
//
//    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
//        throws SQLException {
//        return super.prepareStatement(sql, autoGeneratedKeys);
//    }
//
//    public PreparedStatement prepareStatement(String sql, int columnIndexes[])
//        throws SQLException {
//        return super.prepareStatement(sql, columnIndexes);
//    }
//
//    public PreparedStatement prepareStatement(String sql, String columnNames[])
//        throws SQLException {
//        return super.prepareStatement(sql, columnNames);
//    }

/* JDBC_3_ANT_KEY_END */


    /**
     * Create a PStmtKey for the given arguments.
     */
    protected Object createKey(String sql, int resultSetType, int resultSetConcurrency) {
        String catalog = null;
        try {
            catalog = getCatalog();
        } catch (Exception e) {}
        return new PStmtKey(normalizeSQL(sql), catalog, resultSetType, resultSetConcurrency);
    }

    /**
     * Create a PStmtKey for the given arguments.
     */
    protected Object createKey(String sql) {
        String catalog = null;
        try {
            catalog = getCatalog();
        } catch (Exception e) {}
        return new PStmtKey(normalizeSQL(sql), catalog);
    }

    /**
     * Normalize the given SQL statement, producing a
     * cannonical form that is semantically equivalent to the original.
     */
    protected String normalizeSQL(String sql) {
        return sql.trim();
    }

    /**
     * My {@link KeyedPoolableObjectFactory} method for creating
     * {@link PreparedStatement}s.
     * @param obj the key for the {@link PreparedStatement} to be created
     */
    public Object makeObject(Object obj) throws Exception {
        if(null == obj || !(obj instanceof PStmtKey)) {
            throw new IllegalArgumentException();
        } else {
            // _openPstmts++;
            PStmtKey key = (PStmtKey)obj;
            if(null == key._resultSetType && null == key._resultSetConcurrency) {
                return new PoolablePreparedStatement(getDelegate().prepareStatement(key._sql),key,_pstmtPool,this);
            } else {
                return new PoolablePreparedStatement(getDelegate().prepareStatement(key._sql,key._resultSetType.intValue(),key._resultSetConcurrency.intValue()),key,_pstmtPool,this);
            }
        }
    }

    /**
     * My {@link KeyedPoolableObjectFactory} method for destroying
     * {@link PreparedStatement}s.
     * @param key ignored
     * @param obj the {@link PreparedStatement} to be destroyed.
     */
    public void destroyObject(Object key, Object obj) throws Exception {
        //_openPstmts--;
        if(obj instanceof DelegatingPreparedStatement) {
            ((DelegatingPreparedStatement)obj).getInnermostDelegate().close();
        } else {
            ((PreparedStatement)obj).close();
        }
    }

    /**
     * My {@link KeyedPoolableObjectFactory} method for validating
     * {@link PreparedStatement}s.
     * @param key ignored
     * @param obj ignored
     * @return <tt>true</tt>
     */
    public boolean validateObject(Object key, Object obj) {
        return true;
    }

    /**
     * My {@link KeyedPoolableObjectFactory} method for activating
     * {@link PreparedStatement}s. (Currently a no-op.)
     * @param key ignored
     * @param obj ignored
     */
    public void activateObject(Object key, Object obj) throws Exception {
        ((DelegatingPreparedStatement)obj).activate();
    }

    /**
     * My {@link KeyedPoolableObjectFactory} method for passivating
     * {@link PreparedStatement}s.  Currently invokes {@link PreparedStatement#clearParameters}.
     * @param key ignored
     * @param obj a {@link PreparedStatement}
     */
    public void passivateObject(Object key, Object obj) throws Exception {
        ((PreparedStatement)obj).clearParameters();
        ((DelegatingPreparedStatement)obj).passivate();
    }

    public String toString() {
        return "PoolingConnection: " + _pstmtPool.toString();
    }

    /**
     * A key uniquely identifiying {@link PreparedStatement}s.
     */
    class PStmtKey {
        protected String _sql = null;
        protected Integer _resultSetType = null;
        protected Integer _resultSetConcurrency = null;
        protected String _catalog = null;
        
        PStmtKey(String sql) {
            _sql = sql;
        }

        PStmtKey(String sql, String catalog) {
            _sql = sql;
            _catalog = catalog;
        }

        PStmtKey(String sql, int resultSetType, int resultSetConcurrency) {
            _sql = sql;
            _resultSetType = new Integer(resultSetType);
            _resultSetConcurrency = new Integer(resultSetConcurrency);
        }

        PStmtKey(String sql, String catalog, int resultSetType, int resultSetConcurrency) {
            _sql = sql;
            _catalog = catalog;
            _resultSetType = new Integer(resultSetType);
            _resultSetConcurrency = new Integer(resultSetConcurrency);
        }

        public boolean equals(Object that) {
            try {
                PStmtKey key = (PStmtKey)that;
                return( ((null == _sql && null == key._sql) || _sql.equals(key._sql)) &&
                        ((null == _catalog && null == key._catalog) || _catalog.equals(key._catalog)) &&
                        ((null == _resultSetType && null == key._resultSetType) || _resultSetType.equals(key._resultSetType)) &&
                        ((null == _resultSetConcurrency && null == key._resultSetConcurrency) || _resultSetConcurrency.equals(key._resultSetConcurrency))
                      );
            } catch(ClassCastException e) {
                return false;
            } catch(NullPointerException e) {
                return false;
            }
        }

        public int hashCode() {
            if (_catalog==null)
                return(null == _sql ? 0 : _sql.hashCode());
            else
                return(null == _sql ? _catalog.hashCode() : (_catalog + _sql).hashCode());
        }

        public String toString() {
            StringBuilder buf = new StringBuilder();
            buf.append("PStmtKey: sql=");
            buf.append(_sql);
            buf.append(", catalog=");
            buf.append(_catalog);
            buf.append(", resultSetType=");
            buf.append(_resultSetType);
            buf.append(", resultSetConcurrency=");
            buf.append(_resultSetConcurrency);
            return buf.toString();
        }
    }
}
