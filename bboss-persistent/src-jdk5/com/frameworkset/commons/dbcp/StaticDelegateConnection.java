/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.frameworkset.commons.dbcp;

import java.sql.Connection;
import java.sql.SQLException;

import com.frameworkset.commons.dbcp.NativeDataSource.StaticCount;

public class StaticDelegateConnection extends DelegatingConnection {

	private StaticCount staticCount;
	
	/**
     * Create a wrapper for the Connectin which traces this
     * Connection in the AbandonedObjectPool.
     *
     * @param c the {@link Connection} to delegate all calls to.
     */
    public StaticDelegateConnection(Connection c,StaticCount staticCount) {
        super(c);
        this.staticCount = staticCount;
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
    public StaticDelegateConnection(Connection c, AbandonedConfig config,StaticCount staticCount) {
        super(c,config);
        this.staticCount = staticCount;
    }
    
    
    public void close() throws SQLException
    {
    	try
    	{
    		super.close();
    	}
    	finally
    	{
    		this.staticCount.decrements(this);
    	}
    }
    
    

	

}
