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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.frameworkset.commons.pool2.KeyedObjectPool;

/**
 * A {@link DelegatingPreparedStatement} that cooperates with
 * {@link PoolingConnection} to implement a pool of {@link PreparedStatement}s.
 * <p>
 * My {@link #close} method returns me to my containing pool. (See {@link PoolingConnection}.)
 *
 * @param <K> the key type
 *
 * @see PoolingConnection
 * @author Rodney Waldhoff
 * @author Glenn L. Nielsen
 * @author James House
 * @author Dirk Verbeeck
 * @version $Id: PoolablePreparedStatement.java 1649430 2015-01-04 21:29:32Z tn $
 * @since 2.0
 */
public class PoolablePreparedStatement<K> extends DelegatingPreparedStatement {
    /**
     * The {@link KeyedObjectPool} from which I was obtained.
     */
    private final KeyedObjectPool<K,PoolablePreparedStatement<K>> _pool;

    /**
     * My "key" as used by {@link KeyedObjectPool}.
     */
    private final K _key;

    private volatile boolean batchAdded = false;

    /**
     * Constructor
     * @param stmt my underlying {@link PreparedStatement}
     * @param key my key" as used by {@link KeyedObjectPool}
     * @param pool the {@link KeyedObjectPool} from which I was obtained.
     * @param conn the {@link java.sql.Connection Connection} from which I was created
     */
    public PoolablePreparedStatement(PreparedStatement stmt, K key,
            KeyedObjectPool<K, PoolablePreparedStatement<K>> pool,
            DelegatingConnection<?> conn) {
        super(conn, stmt);
        _pool = pool;
        _key = key;

        // Remove from trace now because this statement will be
        // added by the activate method.
        if(getConnectionInternal() != null) {
            getConnectionInternal().removeTrace(this);
        }
    }

    /**
     * Add batch.
     */
    @Override
    public void addBatch() throws SQLException {
        super.addBatch();
        batchAdded = true;
    }

    /**
     * Clear Batch.
     */
    @Override
    public void clearBatch() throws SQLException {
        batchAdded = false;
        super.clearBatch();
    }

    /**
     * Return me to my pool.
     */
    @Override
    public void close() throws SQLException {
        // calling close twice should have no effect
        if (!isClosed()) {
            try {
                _pool.returnObject(_key, this);
            } catch(SQLException e) {
                throw e;
            } catch(RuntimeException e) {
                throw e;
            } catch(Exception e) {
                throw new SQLException("Cannot close preparedstatement (return to pool failed)", e);
            }
        }
    }

    @Override
    public void activate() throws SQLException{
        setClosedInternal(false);
        if(getConnectionInternal() != null) {
            getConnectionInternal().addTrace(this);
        }
        super.activate();
    }

    @Override
    public void passivate() throws SQLException {
        // DBCP-372. clearBatch with throw an exception if called when the
        // connection is marked as closed.
        if (batchAdded) {
            clearBatch();
        }
        setClosedInternal(true);
        if(getConnectionInternal() != null) {
            getConnectionInternal().removeTrace(this);
        }

        // The JDBC spec requires that a statement closes any open
        // ResultSet's when it is closed.
        // FIXME The PreparedStatement we're wrapping should handle this for us.
        // See bug 17301 for what could happen when ResultSets are closed twice.
        List<AbandonedTrace> resultSets = getTrace();
        if( resultSets != null) {
            ResultSet[] set = resultSets.toArray(new ResultSet[resultSets.size()]);
            for (ResultSet element : set) {
                element.close();
            }
            clearTrace();
        }

        super.passivate();
    }
}
