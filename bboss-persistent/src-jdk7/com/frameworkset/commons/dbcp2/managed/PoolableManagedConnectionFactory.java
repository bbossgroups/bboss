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

package com.frameworkset.commons.dbcp2.managed;
import java.sql.Connection;

import javax.management.ObjectName;

import com.frameworkset.commons.dbcp2.Constants;
import com.frameworkset.commons.dbcp2.DelegatingPreparedStatement;
import com.frameworkset.commons.dbcp2.PStmtKey;
import com.frameworkset.commons.dbcp2.PoolableConnection;
import com.frameworkset.commons.dbcp2.PoolableConnectionFactory;
import com.frameworkset.commons.dbcp2.PoolingConnection;
import com.frameworkset.commons.pool2.KeyedObjectPool;
import com.frameworkset.commons.pool2.PooledObject;
import com.frameworkset.commons.pool2.impl.GenericKeyedObjectPool;
import com.frameworkset.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import com.frameworkset.commons.pool2.impl.DefaultPooledObject;

/**
 * A {@link PoolableConnectionFactory} that creates {@link PoolableManagedConnection}s.
 *
 * @version $Id: PoolableManagedConnectionFactory.java 1659452 2015-02-13 04:00:39Z psteitz $
 * @since 2.0
 */
public class PoolableManagedConnectionFactory extends PoolableConnectionFactory {

    /** Transaction registry associated with connections created by this factory */
    private final TransactionRegistry transactionRegistry;

    /**
     * Create a PoolableManagedConnectionFactory and attach it to a connection pool.
     *
     * @param connFactory XAConnectionFactory
     */
    public PoolableManagedConnectionFactory(XAConnectionFactory connFactory,
            ObjectName dataSourceJmxName) {
        super(connFactory, dataSourceJmxName);
        this.transactionRegistry = connFactory.getTransactionRegistry();
    }

    /**
     * Uses the configured XAConnectionFactory to create a {@link PoolableManagedConnection}.
     * Throws <code>IllegalStateException</code> if the connection factory returns null.
     * Also initializes the connection using configured initialization sql (if provided)
     * and sets up a prepared statement pool associated with the PoolableManagedConnection
     * if statement pooling is enabled.
     */
    @Override
    synchronized public PooledObject<PoolableConnection> makeObject() throws Exception {
        Connection conn = getConnectionFactory().createConnection();
        if (conn == null) {
            throw new IllegalStateException("Connection factory returned null from createConnection");
        }
        initializeConnection(conn);
        if (getPoolStatements()) {
            conn = new PoolingConnection(conn);
            GenericKeyedObjectPoolConfig config = new GenericKeyedObjectPoolConfig();
            config.setMaxTotalPerKey(-1);
            config.setBlockWhenExhausted(false);
            config.setMaxWaitMillis(0);
            config.setMaxIdlePerKey(1);
            config.setMaxTotal(getMaxOpenPreparedStatements());
            ObjectName dataSourceJmxName = getDataSourceJmxName();
            long connIndex = getConnectionIndex().getAndIncrement();
            if (dataSourceJmxName != null) {
                StringBuilder base = new StringBuilder(dataSourceJmxName.toString());
                base.append(Constants.JMX_CONNECTION_BASE_EXT);
                base.append(Long.toString(connIndex));
                config.setJmxNameBase(base.toString());
                config.setJmxNamePrefix(Constants.JMX_STATEMENT_POOL_PREFIX);
            } else {
                config.setJmxEnabled(false);
            }
            KeyedObjectPool<PStmtKey,DelegatingPreparedStatement> stmtPool =
                new GenericKeyedObjectPool<PStmtKey, DelegatingPreparedStatement>((PoolingConnection)conn, config);
            ((PoolingConnection)conn).setStatementPool(stmtPool);
            ((PoolingConnection) conn).setCacheState(getCacheState());
        }
        return new DefaultPooledObject<PoolableConnection>(
                new PoolableManagedConnection(transactionRegistry, conn, getPool()));
    }
}
