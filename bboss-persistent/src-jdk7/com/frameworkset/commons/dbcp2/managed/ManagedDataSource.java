/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.frameworkset.commons.dbcp2.managed;

import com.frameworkset.commons.pool2.ObjectPool;
import com.frameworkset.commons.dbcp2.PoolingDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * The ManagedDataSource is a PoolingDataSource that creates ManagedConnections.
 *
 * @author Dain Sundstrom
 * @param <C> The kind of {@link Connection} to manage.
 * @version $Id: ManagedDataSource.java 1650024 2015-01-07 09:49:58Z tn $
 * @since 2.0
 */
public class ManagedDataSource<C extends Connection> extends PoolingDataSource<C> {
    private TransactionRegistry transactionRegistry;

    /**
     * Creates a ManagedDataSource which obtains connections from the specified pool and
     * manages them using the specified transaction registry.  The TransactionRegistry must
     * be the transaction registry obtained from the XAConnectionFactory used to create
     * the connection pool.  If not, an error will occur when attempting to use the connection
     * in a global transaction because the XAResource object associated with the connection
     * will be unavailable.
     *
     * @param pool the connection pool
     * @param transactionRegistry the transaction registry obtained from the
     * XAConnectionFactory used to create the connection pool object factory
     */
    public ManagedDataSource(ObjectPool<C> pool,
            TransactionRegistry transactionRegistry) {
        super(pool);
        this.transactionRegistry = transactionRegistry;
    }

    /**
     * Sets the transaction registry from the XAConnectionFactory used to create the pool.
     * The transaction registry can only be set once using either a connector or this setter
     * method.
     * @param transactionRegistry the transaction registry acquired from the XAConnectionFactory
     * used to create the pool
     */
    public void setTransactionRegistry(TransactionRegistry transactionRegistry) {
        if(this.transactionRegistry != null) {
            throw new IllegalStateException("TransactionRegistry already set");
        }
        if(transactionRegistry == null) {
            throw new NullPointerException("TransactionRegistry is null");
        }

        this.transactionRegistry = transactionRegistry;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (getPool() == null) {
            throw new IllegalStateException("Pool has not been set");
        }
        if (transactionRegistry == null) {
            throw new IllegalStateException("TransactionRegistry has not been set");
        }

        Connection connection = new ManagedConnection<C>(getPool(), transactionRegistry, isAccessToUnderlyingConnectionAllowed());
        return connection;
    }
}
