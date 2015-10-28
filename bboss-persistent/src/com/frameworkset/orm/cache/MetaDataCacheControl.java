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
package com.frameworkset.orm.cache;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * 
 * 
 * <p>Title: MetaDataCacheControl.java</p>
 *
 * <p>Description: meta cache container control :flush,cache,remove</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *

 * @Date Oct 22, 2008 11:36:27 AM
 * @author biaoping.yin
 * @version 1.0
 */
public class MetaDataCacheControl implements CacheControl {
	private MemoryCacheLevel referenceType = MemoryCacheLevel.WEAK;
	  private Map cache = Collections.synchronizedMap(new HashMap());

	  /**
	   * Configures the cache
	   *
	   * @param props Optionally can contain properties [reference-type=WEAK|SOFT|STRONG]
	   */
	  public void setProperties(Properties props) {
	    String refType = props.getProperty("reference-type");
	    if (refType == null) {
	      refType = props.getProperty("referenceType");
	    }
	    if (refType != null) {
	      referenceType = MemoryCacheLevel.getByReferenceType(refType);
	    }
	  }

	  public MemoryCacheLevel getReferenceType() {
	    return referenceType;
	  }

	  public void setReferenceType(MemoryCacheLevel referenceType) {
	    this.referenceType = referenceType;
	  }

	  /**
	   * Add an object to the cache
	   *
	   * @param cacheModel The cacheModel
	   * @param key        The key of the object to be cached
	   * @param value      The object to be cached
	   */
	  public void putObject(CacheModel cacheModel, Object key, Object value) {
	    Object reference = null;
	    if (referenceType.equals(MemoryCacheLevel.WEAK)) {
	      reference = new WeakReference(value);
	    } else if (referenceType.equals(MemoryCacheLevel.SOFT)) {
	      reference = new SoftReference(value);
	    } else if (referenceType.equals(MemoryCacheLevel.STRONG)) {
	      reference = new StrongReference(value);
	    }
	    cache.put(key, reference);
	  }

	  /**
	   * Get an object out of the cache.
	   *
	   * @param cacheModel The cache model
	   * @param key        The key of the object to be returned
	   * @return The cached object (or null)
	   */
	  public Object getObject(CacheModel cacheModel, Object key) {
	    Object value = null;
	    Object ref = cache.get(key);
	    if (ref != null) {
	      if (ref instanceof StrongReference) {
	        value = ((StrongReference) ref).get();
	      } else if (ref instanceof SoftReference) {
	        value = ((SoftReference) ref).get();
	      } else if (ref instanceof WeakReference) {
	        value = ((WeakReference) ref).get();
	      }
	    }
	    return value;
	  }

	  public Object removeObject(CacheModel cacheModel, Object key) {
	    Object value = null;
	    Object ref = cache.remove(key);
	    if (ref != null) {
	      if (ref instanceof StrongReference) {
	        value = ((StrongReference) ref).get();
	      } else if (ref instanceof SoftReference) {
	        value = ((SoftReference) ref).get();
	      } else if (ref instanceof WeakReference) {
	        value = ((WeakReference) ref).get();
	      }
	    }
	    return value;
	  }

	  /**
	   * Flushes the cache.
	   *
	   * @param cacheModel The cache model
	   */
	  public void flush(CacheModel cacheModel) {
	    cache.clear();
	  }

	  /**
	   * Class to implement a strong (permanent) reference.
	   */
	  private static class StrongReference {
	    private Object object;

	    /**
	     * StrongReference constructor for an object
	     * @param object - the Object to store
	     */
	    public StrongReference(Object object) {
	      this.object = object;
	    }

	    /**
	     * Getter to get the object stored in the StrongReference
	     * @return - the stored Object
	     */
	    public Object get() {
	      return object;
	    }
	  }

}
