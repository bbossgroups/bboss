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

import com.frameworkset.commons.pool.ObjectPool;
import com.frameworkset.commons.pool.ObjectPoolFactory;
import com.frameworkset.commons.pool.PoolableObjectFactory;

/**
 * A factory for creating {@link StackObjectPool} instances.
 *
 * @see StackObjectPool
 * @see StackKeyedObjectPoolFactory
 *
 * @author Rodney Waldhoff
 * @version $Revision: 155430 $ $Date: 2005-02-26 08:13:28 -0500 (Sat, 26 Feb 2005) $ 
 */
public class StackObjectPoolFactory implements ObjectPoolFactory {
    public StackObjectPoolFactory() {
        this((PoolableObjectFactory)null,StackObjectPool.DEFAULT_MAX_SLEEPING,StackObjectPool.DEFAULT_INIT_SLEEPING_CAPACITY);
    }

    public StackObjectPoolFactory(int max) {
        this((PoolableObjectFactory)null,max,StackObjectPool.DEFAULT_INIT_SLEEPING_CAPACITY);
    }

    public StackObjectPoolFactory(int max, int init) {
        this((PoolableObjectFactory)null,max,init);
    }

    public StackObjectPoolFactory(PoolableObjectFactory factory) {
        this(factory,StackObjectPool.DEFAULT_MAX_SLEEPING,StackObjectPool.DEFAULT_INIT_SLEEPING_CAPACITY);
    }

    public StackObjectPoolFactory(PoolableObjectFactory factory, int max) {
        this(factory,max,StackObjectPool.DEFAULT_INIT_SLEEPING_CAPACITY);
    }

    public StackObjectPoolFactory(PoolableObjectFactory factory, int max, int init) {
        _factory = factory;
        _maxSleeping = max;
        _initCapacity = init;
    }

    public ObjectPool createPool() {
        return new StackObjectPool(_factory,_maxSleeping,_initCapacity);
    }

    protected PoolableObjectFactory _factory = null;
    protected int _maxSleeping = StackObjectPool.DEFAULT_MAX_SLEEPING;
    protected int _initCapacity = StackObjectPool.DEFAULT_INIT_SLEEPING_CAPACITY;

}
