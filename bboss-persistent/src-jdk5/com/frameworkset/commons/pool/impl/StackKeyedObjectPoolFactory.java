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

package com.frameworkset.commons.pool.impl;

import com.frameworkset.commons.pool.KeyedObjectPool;
import com.frameworkset.commons.pool.KeyedObjectPoolFactory;
import com.frameworkset.commons.pool.KeyedPoolableObjectFactory;

/**
 * A factory for creating {@link StackKeyedObjectPool} instances.
 *
 * @see StackKeyedObjectPool
 * @see KeyedObjectPoolFactory
 *
 * @author Rodney Waldhoff
 * @version $Revision: 155430 $ $Date: 2005-02-26 08:13:28 -0500 (Sat, 26 Feb 2005) $ 
 */
public class StackKeyedObjectPoolFactory implements KeyedObjectPoolFactory {
    public StackKeyedObjectPoolFactory() {
        this((KeyedPoolableObjectFactory)null,StackKeyedObjectPool.DEFAULT_MAX_SLEEPING,StackKeyedObjectPool.DEFAULT_INIT_SLEEPING_CAPACITY);
    }

    public StackKeyedObjectPoolFactory(int max) {
        this((KeyedPoolableObjectFactory)null,max,StackKeyedObjectPool.DEFAULT_INIT_SLEEPING_CAPACITY);
    }

    public StackKeyedObjectPoolFactory(int max, int init) {
        this((KeyedPoolableObjectFactory)null,max,init);
    }

    public StackKeyedObjectPoolFactory(KeyedPoolableObjectFactory factory) {
        this(factory,StackKeyedObjectPool.DEFAULT_MAX_SLEEPING,StackKeyedObjectPool.DEFAULT_INIT_SLEEPING_CAPACITY);
    }

    public StackKeyedObjectPoolFactory(KeyedPoolableObjectFactory factory, int max) {
        this(factory,max,StackKeyedObjectPool.DEFAULT_INIT_SLEEPING_CAPACITY);
    }

    public StackKeyedObjectPoolFactory(KeyedPoolableObjectFactory factory, int max, int init) {
        _factory = factory;
        _maxSleeping = max;
        _initCapacity = init;
    }

    public KeyedObjectPool createPool() {
        return new StackKeyedObjectPool(_factory,_maxSleeping,_initCapacity);
    }

    protected KeyedPoolableObjectFactory _factory = null;
    protected int _maxSleeping = StackKeyedObjectPool.DEFAULT_MAX_SLEEPING;
    protected int _initCapacity = StackKeyedObjectPool.DEFAULT_INIT_SLEEPING_CAPACITY;

}
