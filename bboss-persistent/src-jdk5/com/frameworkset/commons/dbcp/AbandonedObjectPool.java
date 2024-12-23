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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.frameworkset.commons.pool.PoolableObjectFactory;
import com.frameworkset.commons.pool.impl.GenericObjectPool;

/**
 * <p>An implementation of a Jakarta-Commons ObjectPool which
 * tracks JDBC connections and can recover abandoned db connections.
 * If logAbandoned=true, a stack trace will be printed for any
 * abandoned db connections recovered.
 *                                                                        
 * @author Glenn L. Nielsen
 * @version $Revision: 482015 $ $Date: 2006-12-03 19:22:09 -0700 (Sun, 03 Dec 2006) $
 * @deprecated This will be removed in a future version of DBCP.
 */
public class AbandonedObjectPool extends GenericObjectPool {

    /** 
     * DBCP AbandonedConfig 
     */
    private AbandonedConfig config = null;
    
    /**
     * A list of connections in use
     */
    private List trace = new ArrayList();
    
    private static final Logger log = Logger.getLogger(AbandonedObjectPool.class);

    /**
     * Create an ObjectPool which tracks db connections.
     *
     * @param factory PoolableObjectFactory used to create this
     * @param config configuration for abandoned db connections
     */
    public AbandonedObjectPool(PoolableObjectFactory factory,
                               AbandonedConfig config) {
        super(factory);
        this.config = config;
        System.out.println("AbandonedObjectPool is used (" + this + ")");
        System.out.println("   LogAbandoned: " + config.getLogAbandoned());
        System.out.println("   RemoveAbandoned: " + config.getRemoveAbandoned());
        System.out.println("   RemoveAbandonedTimeout: " + config.getRemoveAbandonedTimeout());
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
        if (config != null
                && config.getRemoveAbandoned()
                && (getNumIdle() < 2)
                && (getNumActive() > getMaxActive() - 3) ) {
            removeAbandoned();
        }
        Object obj = super.borrowObject();
        if (obj instanceof AbandonedTrace) {
            ((AbandonedTrace) obj).setStackTrace();
        }
        if (obj != null && config != null && config.getRemoveAbandoned()) {
            synchronized (trace) {
                trace.add(obj);
            }
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
        if (config != null && config.getRemoveAbandoned()) {
            synchronized (trace) {
                boolean foundObject = trace.remove(obj);
                if (!foundObject) {
                    return; // This connection has already been invalidated.  Stop now.
                }
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
        if (config != null && config.getRemoveAbandoned()) {
            synchronized (trace) {
                boolean foundObject = trace.remove(obj);
                if (!foundObject) {
                    return; // This connection has already been invalidated.  Stop now.
                }
            }
        }
        super.invalidateObject(obj);        
    }

    /**
     * Recover abandoned db connections which have been idle
     * greater than the removeAbandonedTimeout.
     */
    private void removeAbandoned() {
        // Generate a list of abandoned connections to remove
        long now = System.currentTimeMillis();
        long timeout = now - (config.getRemoveAbandonedTimeout() * 1000);
        ArrayList remove = new ArrayList();
        synchronized (trace) {
            Iterator it = trace.iterator();
            while (it.hasNext()) {
                AbandonedTrace pc = (AbandonedTrace) it.next();
                if (pc.getLastUsed() > timeout) {
                    continue;
                }
                if (pc.getLastUsed() > 0) {
                    remove.add(pc);
                }
            }
        }

        // Now remove the abandoned connections
        Iterator it = remove.iterator();
        while (it.hasNext()) {
            AbandonedTrace pc = (AbandonedTrace) it.next();
            if (config.getLogAbandoned()) {
                pc.printStackTrace();
                
            }             
            try {
                invalidateObject(pc);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
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
}

