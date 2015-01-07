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

import java.util.ArrayList;
import java.util.List;

import com.frameworkset.common.poolman.monitor.AbandonedTraceExt;
import com.frameworkset.common.poolman.monitor.PoolMonitorUtil;
import com.frameworkset.commons.pool.PoolableObjectFactory;
import com.frameworkset.commons.pool.impl.GenericObjectPool;

public class LogAbandonedObjectPool  extends GenericObjectPool {

    /** 
     * DBCP AbandonedConfig 
     */
    private final AbandonedConfig config;
    
    /**
     * A list of connections in use
     */
    private final List trace = new ArrayList();

    /**
     * Create an ObjectPool which tracks db connections.
     *
     * @param factory PoolableObjectFactory used to create this
     * @param config configuration for abandoned db connections
     */
    public LogAbandonedObjectPool(PoolableObjectFactory factory,
                               AbandonedConfig config) {
        super(factory);
        this.config = config;
    }

    /**
     * Get a db connection from the pool.
     *
     * If removeAbandoned=true, recovers db connections which
     * have been idle > removeAbandonedTimeout and
     * getNumActive() > getMaxActive() - 3 and
     * getNumIdle() < 2
     * 
     * @return Object jdbc Connection
     * @throws Exception if an exception occurs retrieving a 
     * connection from the pool
     */
    public Object borrowObject() throws Exception {
       
        Object obj = super.borrowObject();
//        if (obj instanceof AbandonedTrace) 
//        {
            ((AbandonedTrace) obj).setGoodStackTrace();
//        }
            synchronized (trace) {
                trace.add(obj);
            }
         
        return obj;
    }

    /**
     * Return a db connection to the pool.
     *
     * @param obj db Connection to return
     * @throws Exception if an exception occurs returning the connection
     * to the pool
     */
    public void returnObject(Object obj) throws Exception {
        
            synchronized (trace) {
                boolean foundObject = trace.remove(obj);
                if (!foundObject) {
                    return; // This connection has already been invalidated.  Stop now.
                }
            }
         
        super.returnObject(obj);
    }

    /**
     * Invalidates an object from the pool.
     *
     * @param obj object to be returned
     * @throws Exception if an exception occurs invalidating the object
     */
    public void invalidateObject(Object obj) throws Exception {
          
            synchronized (trace) {
                boolean foundObject = trace.remove(obj);
                if (!foundObject) {
                    return; // This connection has already been invalidated.  Stop now.
                }
            }
        
        super.invalidateObject(obj);        
    }

    
	/**
	 * @return the trace
	 */
	public List getTraces() {
		List dest = new ArrayList();
		try
		{
			if(trace != null)
				dest.addAll(trace);
		}
		catch(Exception e)
		{
			
		}
		return dest;
	}
	
	public List<AbandonedTraceExt> getTraceObjects() {
		List dest = null;
		synchronized (trace) {
			if(trace.size() > 0)
			{
				dest = new ArrayList(trace);				
			}
			else
			{
//				List<AbandonedTraceExt> list = new ArrayList<AbandonedTraceExt>();
//				return list;
			}
        }
		
		if(dest != null && dest.size() > 0)
		{
			return PoolMonitorUtil.converAbandonedTrace(dest);
		}
		else
		{
			List<AbandonedTraceExt> list = new ArrayList<AbandonedTraceExt>();
			return list;
		}
			
	}

}
