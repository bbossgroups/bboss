/*
 * Copyright 1999-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.frameworkset.commons.pool;

/**
 * An interface defining life-cycle methods for
 * instances to be served by a
 * {@link KeyedObjectPool KeyedObjectPool}.
 * <p>
 * By contract, when an {@link KeyedObjectPool KeyedObjectPool}
 * delegates to a <tt>KeyedPoolableObjectFactory</tt>,
 * <ol>
 *  <li>
 *   {@link #makeObject makeObject} 
 *   is called  whenever a new instance is needed.
 *  </li>
 *  <li>
 *   {@link #activateObject activateObject} 
 *   is invoked on every instance before it is returned from the
 *   pool.
 *  </li>
 *  <li>
 *   {@link #passivateObject passivateObject} 
 *   is invoked on every instance when it is returned to the
 *   pool.
 *  </li>
 *  <li>
 *   {@link #destroyObject destroyObject} 
 *   is invoked on every instance when it is being "dropped" from the
 *   pool (whether due to the response from
 *   {@link #validateObject validateObject}, or
 *   for reasons specific to the pool implementation.)
 *  </li>
 *  <li>
 *   {@link #validateObject validateObject} 
 *   is invoked in an implementation-specific fashion to determine if an instance
 *   is still valid to be returned by the pool.
 *   It will only be invoked on an {@link #activateObject "activated"}
 *   instance.
 *  </li>
 * </ol>
 *
 * @see KeyedObjectPool
 * 
 * @author Rodney Waldhoff
 * @version $Revision: 155430 $ $Date: 2005-02-26 08:13:28 -0500 (Sat, 26 Feb 2005) $ 
 */
public interface KeyedPoolableObjectFactory {
    /**
     * Create an instance that can be served by the pool.
     * @param key the key used when constructing the object
     * @return an instance that can be served by the pool.
     */
    Object makeObject(Object key) throws Exception;

    /**
     * Destroy an instance no longer needed by the pool.
     * @param key the key used when selecting the instance
     * @param obj the instance to be destroyed
     */
    void destroyObject(Object key, Object obj) throws Exception;

    /**
     * Ensures that the instance is safe to be returned by the pool.
     * Returns <tt>false</tt> if this instance should be destroyed.
     * @param key the key used when selecting the object
     * @param obj the instance to be validated
     * @return <tt>false</tt> if this <i>obj</i> is not valid and should
     *         be dropped from the pool, <tt>true</tt> otherwise.
     */
    boolean validateObject(Object key, Object obj);

    /**
     * Reinitialize an instance to be returned by the pool.
     * @param key the key used when selecting the object
     * @param obj the instance to be activated
     */
    void activateObject(Object key, Object obj) throws Exception;

    /**
     * Uninitialize an instance to be returned to the pool.
     * @param key the key used when selecting the object
     * @param obj the instance to be passivated
     */
    void passivateObject(Object key, Object obj) throws Exception;
}
