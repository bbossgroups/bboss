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
package com.frameworkset.common.poolman.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.PoolManConstants;
import com.frameworkset.common.poolman.sql.PrimaryKeyCacheManager;


/**
 * An object that manages several pools of objects.
 * @see SQLManager
 */
public class PoolManager  {
	private static Logger log = Logger.getLogger(PoolManager.class);

    protected Hashtable pools;
    protected List<String> poolnames;
    protected JDBCPool defaultpool;

    protected PoolManager() {
        this.pools = new Hashtable(1);
        this.defaultpool = null;
        poolnames = new ArrayList<String>();
    }
    /**
     * 
     * @return
     * @deprecated use method public List<String> getAllPoolNames()
     */
    public Enumeration getAllPoolnames() {
        return pools.keys();
    }
    
    public List<String> getAllPoolNames() {
        
        return poolnames;
    }


    public JDBCPool getPool(String name) {

        if (name == null)
            return this.defaultpool;

        if (pools.containsKey(name)) {
            try {
                return (JDBCPool) pools.get(name);
            } catch (Exception e) {
                log.error(e.getMessage(),e);
            }
        }
        log.info("ERROR: Could not locate " + name +
                                       ". This usually means that the " +
                                       PoolManConstants.XML_CONFIG_FILE +
                                       " file could not be found, or that it " +
                       "does not contain configuration data for a " +
                       "pool with a name equal to " + name);
        return null;
    }
    public JDBCPool getPoolIfExist(String name) {

        if (name == null)
            return this.defaultpool;

        if (pools.containsKey(name)) {
            try {
                return (JDBCPool) pools.get(name);
            } catch (Exception e) {
            	log.error(e.getMessage(),e);
            }
        }
        return null;
//        throw new NullPointerException("ERROR: Could not locate " + name +
//                                       ". This usually means that the " +
//                                       PoolManConstants.XML_CONFIG_FILE +
//                                       " file could not be found, or that it " +
//                       "does not contain configuration data for a " +
//                       "pool with a name equal to " + name);
    }
    
    public boolean exist(String name)
    {
        return this.pools.containsKey(name);
    }

    public void addPool(String id, JDBCPool newpool) {
//        if (this.pools.containsKey(id)) {
//            System.out.println("ERROR: A pool identified by the id " + id +
//                               " already exists, ignoring it.");
//        }
//        else 
        
        this.pools.put(id, newpool);
        if(!poolnames.contains(id))
        	poolnames.add(id);
        
        if (this.defaultpool == null)
        {
            this.defaultpool = newpool;
        }
        else 
        {
        	if(defaultpool.getDBName() .equals( newpool.getDBName()))
        	{
        		try {
					defaultpool.stopPool();
				} catch (Exception e) {
					
					log.error(e.getMessage(),e);
				}
        		this.defaultpool = newpool;
        	}
        }
        
    }

    public void removePool(String id) {
        if (pools.containsKey(id)) {
            int th = this.defaultpool.hashCode();
            int oh = pools.get(id).hashCode();
            if (th == oh)
                this.defaultpool = null;
            pools.remove(id);
            poolnames.remove(id);
        }
    }

    public Object requestObject() {
        try {
            return defaultpool.requestConnection();
        } catch (Exception e) {
            System.out.println("ERROR: Could not request object, returning NULL:");
        }
        return null;
    }

    public Object requestObject(String poolname) {
        JDBCPool pool = null;
        try {
            pool = (JDBCPool) pools.get(poolname);
        } catch (NullPointerException ne) {
        }
        if (pool != null) {
            try {
                return pool.requestConnection();
            } catch (Exception e) {
            	log.error("Could not request object, returning NULL:",e);
            }
        }
        return null;
    }

//    public void returnObject(Object o) {
////        defaultpool.returnConnection(o);
//    }

//    public void returnObject(Object o, String poolname) {
//        JDBCPool pool = null;
//        try {
//            pool = (JDBCPool) pools.get(poolname);
//        } catch (NullPointerException ne) {
//        }
//        if (pool != null)
//            pool.returnConnection(o);
//    }

    public void destroyPools() {
        if (this.pools != null) {
            for (Enumeration enum_ = pools.keys(); enum_.hasMoreElements();) {
                Object key = enum_.nextElement();
                JDBCPool pool = (JDBCPool) pools.get(key);
                try {
					pool.closeAllResources();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					log.error(e.getMessage(),e);
				}
            }
            pools.clear();
            pools = null;
        }
        this.defaultpool = null;
        this.poolnames = null;
        try {
			PrimaryKeyCacheManager.destroy();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
