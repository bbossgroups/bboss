package bboss.org.apache.velocity.runtime.resource;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import bboss.org.apache.velocity.runtime.RuntimeConstants;
import bboss.org.apache.velocity.runtime.RuntimeServices;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of the resource cache for the default
 * ResourceManager.  The cache uses a <i>least recently used</i> (LRU)
 * algorithm, with a maximum size specified via the
 * <code>resource.manager.cache.size</code> property (identified by the
 * {@link
 * bboss.org.apache.velocity.runtime.RuntimeConstants#RESOURCE_MANAGER_DEFAULTCACHE_SIZE}
 * constant).  This property get be set to <code>0</code> or less for
 * a greedy, unbounded cache (the behavior from pre-v1.5).
 *
 * @author <a href="mailto:geirm@apache.org">Geir Magnusson Jr.</a>
 * @author <a href="mailto:dlr@finemaltcoding.com">Daniel Rall</a>
 * @version $Id$
 */
public class ResourceCacheImpl implements ResourceCache
{

    /**
     * A simple LRU Map based on {@link LinkedHashSet}.
     *
     * @param <K> The key type of the map.
     * @param <V> The value type of the map.
     */
    private static class LRUMap<K, V> extends LinkedHashMap<K, V>{

    	/**
         * The serial version uid;
         */
        private static final long serialVersionUID = 5889225121697975043L;

    	/**
    	 * The size of the cache.
    	 */
    	private int cacheSize;

    	/**
    	 * Constructor.
    	 *
    	 * @param cacheSize The size of the cache. After reaching this size, the
    	 * eldest-accessed element will be erased.
    	 */
    	public LRUMap(int cacheSize)
        {
            this.cacheSize = cacheSize;
        }

    	/** {@inheritDoc} */
    	@Override
        protected boolean removeEldestEntry(Entry<K, V> eldest)
        {
            return size() > cacheSize;
        }
    }

    /**
     * Cache storage, assumed to be thread-safe.
     */
    protected Map<Object, Resource> cache = new ConcurrentHashMap<>(512, 0.5f, 30);

    /**
     * Runtime services, generally initialized by the
     * <code>initialize()</code> method.
     */
    protected RuntimeServices rsvc = null;

    protected Logger log;

    /**
     * @see bboss.org.apache.velocity.runtime.resource.ResourceCache#initialize(bboss.org.apache.velocity.runtime.RuntimeServices)
     */
    @Override
    public void initialize(RuntimeServices rs )
    {
        rsvc = rs;

        int maxSize =
            rsvc.getInt(RuntimeConstants.RESOURCE_MANAGER_DEFAULTCACHE_SIZE, 89);
        if (maxSize > 0)
        {
            // Create a whole new Map here to avoid hanging on to a
            // handle to the unsynch'd LRUMap for our lifetime.
            Map<Object, Resource> lruCache = Collections.synchronizedMap(new LRUMap<>(maxSize));
            lruCache.putAll(cache);
            cache = lruCache;
        }
        rsvc.getLog().debug("initialized ({}) with {} cache map.", this.getClass(), cache.getClass());
    }

    /**
     * @see bboss.org.apache.velocity.runtime.resource.ResourceCache#get(java.lang.Object)
     */
    @Override
    public Resource get(Object key )
    {
        return cache.get( key );
    }

    /**
     * @see bboss.org.apache.velocity.runtime.resource.ResourceCache#put(java.lang.Object, bboss.org.apache.velocity.runtime.resource.Resource)
     */
    @Override
    public Resource put(Object key, Resource value )
    {
        return cache.put( key, value );
    }

    /**
     * @see bboss.org.apache.velocity.runtime.resource.ResourceCache#remove(java.lang.Object)
     */
    @Override
    public Resource remove(Object key )
    {
        return cache.remove( key );
    }

    /**
     * @see bboss.org.apache.velocity.runtime.resource.ResourceCache#clear()
     * @since 2.0
     */
    @Override
    public void clear()
    {
        cache.clear();
    }

    /**
     * @see bboss.org.apache.velocity.runtime.resource.ResourceCache#enumerateKeys()
     */
    @Override
    public Iterator<Object> enumerateKeys()
    {
        return cache.keySet().iterator();
    }
}

