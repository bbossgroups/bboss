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
import java.sql.SQLException;

import com.frameworkset.commons.pool.ObjectPool;

/**
 * A delegating connection that, rather than closing the underlying
 * connection, returns itself to an {@link ObjectPool} when
 * closed.
 *
 * @author Rodney Waldhoff
 * @author Glenn L. Nielsen
 * @author James House
 * @version $Revision: 479137 $ $Date: 2006-11-25 08:51:48 -0700 (Sat, 25 Nov 2006) $
 */
public class PoolableConnection extends DelegatingConnection {
    /** The pool to which I should return. */
    // TODO: Correct use of the pool requires that this connection is only every returned to the pool once.
    protected ObjectPool _pool = null;

    /**
     *
     * @param conn my underlying connection
     * @param pool the pool to which I should return when closed
     */
    public PoolableConnection(Connection conn, ObjectPool pool) {
        super(conn);
        _pool = pool;
    }

    /**
     *
     * @param conn my underlying connection
     * @param pool the pool to which I should return when closed
     * @param config the abandoned configuration settings
     * @deprecated AbandonedConfig is now deprecated.
     */
    public PoolableConnection(Connection conn, ObjectPool pool,
                              AbandonedConfig config) {
        super(conn, config);
        _pool = pool;
    }


    /**
     * Returns me to my pool.
     */
     public synchronized void close() throws SQLException {
        boolean isClosed = false;
        try {
            isClosed = isClosed();
        } catch (SQLException e) {
            try {
                _pool.invalidateObject(this); // XXX should be guarded to happen at most once
            } catch (Exception ie) {
                // DO NOTHING the original exception will be rethrown
            }
            throw new SQLNestedException("Cannot close connection (isClosed check failed)", e);
        }
        if (isClosed) {
            try {
                _pool.invalidateObject(this); // XXX should be guarded to happen at most once
            } catch (Exception ie) {
                // DO NOTHING, "Already closed" exception thrown below
            }
            throw new SQLException("Already closed.");
        } else {
            try {
                _pool.returnObject(this); // XXX should be guarded to happen at most once
            } catch(SQLException e) {
                throw e;
            } catch(RuntimeException e) {
                throw e;
            } catch(Exception e) {
                throw new SQLNestedException("Cannot close connection (return to pool failed)", e);
            }
        }
    }

    /**
     * Actually close my underlying {@link Connection}.
     */
    public void reallyClose() throws SQLException {
        super.close();
    }

}

