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

package com.frameworkset.commons.pool.impl;

import com.frameworkset.commons.pool.KeyedObjectPool;
import com.frameworkset.commons.pool.KeyedObjectPoolFactory;
import com.frameworkset.commons.pool.KeyedPoolableObjectFactory;

/**
 * A factory for creating {@link GenericKeyedObjectPool} instances.
 *
 * @see GenericKeyedObjectPool
 * @see KeyedObjectPoolFactory
 *
 * @author Rodney Waldhoff
 * @author Dirk Verbeeck
 * @version $Revision: 777748 $ $Date: 2009-05-22 20:00:44 -0400 (Fri, 22 May 2009) $
 * @since Pool 1.0
 */
public class GenericKeyedObjectPoolFactory implements KeyedObjectPoolFactory {
    /**
     * Create a new GenericKeyedObjectPoolFactory.
     *
     * @param factory the KeyedPoolableObjectFactory to used by created pools.
     * @see GenericKeyedObjectPool#GenericKeyedObjectPool(KeyedPoolableObjectFactory)
     */
    public GenericKeyedObjectPoolFactory(KeyedPoolableObjectFactory factory) {
        this(factory,GenericKeyedObjectPool.DEFAULT_MAX_ACTIVE,GenericKeyedObjectPool.DEFAULT_WHEN_EXHAUSTED_ACTION,GenericKeyedObjectPool.DEFAULT_MAX_WAIT,GenericKeyedObjectPool.DEFAULT_MAX_IDLE,GenericKeyedObjectPool.DEFAULT_TEST_ON_BORROW,GenericKeyedObjectPool.DEFAULT_TEST_ON_RETURN,GenericKeyedObjectPool.DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS,GenericKeyedObjectPool.DEFAULT_NUM_TESTS_PER_EVICTION_RUN,GenericKeyedObjectPool.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS,GenericKeyedObjectPool.DEFAULT_TEST_WHILE_IDLE);
    }

    /**
     * Create a new GenericKeyedObjectPoolFactory.
     *
     * @param factory the KeyedPoolableObjectFactory to used by created pools.
     * @param config a non-null GenericKeyedObjectPool.Config describing the configuration.
     * @see GenericKeyedObjectPool#GenericKeyedObjectPool(KeyedPoolableObjectFactory, GenericKeyedObjectPool.Config)
     * @throws NullPointerException when config is <code>null</code>.
     */
    public GenericKeyedObjectPoolFactory(KeyedPoolableObjectFactory factory, GenericKeyedObjectPool.Config config) throws NullPointerException {
        this(factory,config.maxActive,config.whenExhaustedAction,config.maxWait,config.maxIdle,config.maxTotal,config.minIdle,config.testOnBorrow,config.testOnReturn,config.timeBetweenEvictionRunsMillis,config.numTestsPerEvictionRun,config.minEvictableIdleTimeMillis,config.testWhileIdle,config.lifo);
    }

    /**
     * Create a new GenericKeyedObjectPoolFactory.
     *
     * @param factory the KeyedPoolableObjectFactory to used by created pools.
     * @param maxActive the maximum number of objects that can be borrowed from pools at one time.
     * @see GenericKeyedObjectPool#GenericKeyedObjectPool(KeyedPoolableObjectFactory, int)
     */
    public GenericKeyedObjectPoolFactory(KeyedPoolableObjectFactory factory, int maxActive) {
        this(factory,maxActive,GenericKeyedObjectPool.DEFAULT_WHEN_EXHAUSTED_ACTION,GenericKeyedObjectPool.DEFAULT_MAX_WAIT,GenericKeyedObjectPool.DEFAULT_MAX_IDLE, GenericKeyedObjectPool.DEFAULT_MAX_TOTAL,GenericKeyedObjectPool.DEFAULT_TEST_ON_BORROW,GenericKeyedObjectPool.DEFAULT_TEST_ON_RETURN,GenericKeyedObjectPool.DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS,GenericKeyedObjectPool.DEFAULT_NUM_TESTS_PER_EVICTION_RUN,GenericKeyedObjectPool.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS,GenericKeyedObjectPool.DEFAULT_TEST_WHILE_IDLE);
    }

    /**
     * Create a new GenericKeyedObjectPoolFactory.
     *
     * @param factory the KeyedPoolableObjectFactory to used by created pools.
     * @param maxActive the maximum number of objects that can be borrowed from pools at one time.
     * @param whenExhaustedAction the action to take when the pool is exhausted.
     * @param maxWait the maximum amount of time to wait for an idle object when the pool is exhausted.
     * @see GenericKeyedObjectPool#GenericKeyedObjectPool(KeyedPoolableObjectFactory, int, byte, long)
     */
    public GenericKeyedObjectPoolFactory(KeyedPoolableObjectFactory factory, int maxActive, byte whenExhaustedAction, long maxWait) {
        this(factory,maxActive,whenExhaustedAction,maxWait,GenericKeyedObjectPool.DEFAULT_MAX_IDLE, GenericKeyedObjectPool.DEFAULT_MAX_TOTAL,GenericKeyedObjectPool.DEFAULT_TEST_ON_BORROW,GenericKeyedObjectPool.DEFAULT_TEST_ON_RETURN,GenericKeyedObjectPool.DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS,GenericKeyedObjectPool.DEFAULT_NUM_TESTS_PER_EVICTION_RUN,GenericKeyedObjectPool.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS,GenericKeyedObjectPool.DEFAULT_TEST_WHILE_IDLE);
    }

    /**
     * Create a new GenericKeyedObjectPoolFactory.
     *
     * @param factory the KeyedPoolableObjectFactory to used by created pools.
     * @param maxActive the maximum number of objects that can be borrowed from pools at one time.
     * @param whenExhaustedAction the action to take when the pool is exhausted.
     * @param maxWait the maximum amount of time to wait for an idle object when the pool is exhausted.
     * @param testOnBorrow whether to validate objects before they are returned by borrowObject.
     * @param testOnReturn whether to validate objects after they are returned to returnObject.
     * @see GenericKeyedObjectPool#GenericKeyedObjectPool(KeyedPoolableObjectFactory, int, byte, long, boolean, boolean)
     */
    public GenericKeyedObjectPoolFactory(KeyedPoolableObjectFactory factory, int maxActive, byte whenExhaustedAction, long maxWait, boolean testOnBorrow, boolean testOnReturn) {
        this(factory,maxActive,whenExhaustedAction,maxWait,GenericKeyedObjectPool.DEFAULT_MAX_IDLE, GenericKeyedObjectPool.DEFAULT_MAX_TOTAL,testOnBorrow,testOnReturn,GenericKeyedObjectPool.DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS,GenericKeyedObjectPool.DEFAULT_NUM_TESTS_PER_EVICTION_RUN,GenericKeyedObjectPool.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS,GenericKeyedObjectPool.DEFAULT_TEST_WHILE_IDLE);
    }

    /**
     * Create a new GenericKeyedObjectPoolFactory.
     *
     * @param factory the KeyedPoolableObjectFactory to used by created pools.
     * @param maxActive the maximum number of objects that can be borrowed from pools at one time.
     * @param whenExhaustedAction the action to take when the pool is exhausted.
     * @param maxWait the maximum amount of time to wait for an idle object when the pool is exhausted.
     * @param maxIdle the maximum number of idle objects in the pools.
     * @see GenericKeyedObjectPool#GenericKeyedObjectPool(KeyedPoolableObjectFactory, int, byte, long, int)
     */
    public GenericKeyedObjectPoolFactory(KeyedPoolableObjectFactory factory, int maxActive, byte whenExhaustedAction, long maxWait, int maxIdle) {
        this(factory,maxActive,whenExhaustedAction,maxWait,maxIdle, GenericKeyedObjectPool.DEFAULT_MAX_TOTAL,GenericKeyedObjectPool.DEFAULT_TEST_ON_BORROW,GenericKeyedObjectPool.DEFAULT_TEST_ON_RETURN,GenericKeyedObjectPool.DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS,GenericKeyedObjectPool.DEFAULT_NUM_TESTS_PER_EVICTION_RUN,GenericKeyedObjectPool.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS,GenericKeyedObjectPool.DEFAULT_TEST_WHILE_IDLE);
    }

    /**
     * Create a new GenericKeyedObjectPoolFactory.
     *
     * @param factory the KeyedPoolableObjectFactory to used by created pools.
     * @param maxActive the maximum number of objects that can be borrowed from pools at one time.
     * @param whenExhaustedAction the action to take when the pool is exhausted.
     * @param maxWait the maximum amount of time to wait for an idle object when the pool is exhausted.
     * @param maxIdle the maximum number of idle objects in the pools.
     * @param maxTotal the maximum number of objects that can exists at one time.
     */
    public GenericKeyedObjectPoolFactory(KeyedPoolableObjectFactory factory, int maxActive, byte whenExhaustedAction, long maxWait, int maxIdle, int maxTotal) {
        this(factory,maxActive,whenExhaustedAction,maxWait,maxIdle, maxTotal, GenericKeyedObjectPool.DEFAULT_TEST_ON_BORROW,GenericKeyedObjectPool.DEFAULT_TEST_ON_RETURN,GenericKeyedObjectPool.DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS,GenericKeyedObjectPool.DEFAULT_NUM_TESTS_PER_EVICTION_RUN,GenericKeyedObjectPool.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS,GenericKeyedObjectPool.DEFAULT_TEST_WHILE_IDLE);
    }

    /**
     * Create a new GenericKeyedObjectPoolFactory.
     *
     * @param factory the KeyedPoolableObjectFactory to used by created pools.
     * @param maxActive the maximum number of objects that can be borrowed from pools at one time.
     * @param whenExhaustedAction the action to take when the pool is exhausted.
     * @param maxWait the maximum amount of time to wait for an idle object when the pool is exhausted.
     * @param maxIdle the maximum number of idle objects in the pools.
     * @param testOnBorrow whether to validate objects before they are returned by borrowObject.
     * @param testOnReturn whether to validate objects after they are returned to returnObject.
     * @see GenericKeyedObjectPool#GenericKeyedObjectPool(KeyedPoolableObjectFactory, int, byte, long, int, boolean, boolean)
     */
    public GenericKeyedObjectPoolFactory(KeyedPoolableObjectFactory factory, int maxActive, byte whenExhaustedAction, long maxWait, int maxIdle, boolean testOnBorrow, boolean testOnReturn) {
        this(factory,maxActive,whenExhaustedAction,maxWait,maxIdle, GenericKeyedObjectPool.DEFAULT_MAX_TOTAL,testOnBorrow,testOnReturn,GenericKeyedObjectPool.DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS,GenericKeyedObjectPool.DEFAULT_NUM_TESTS_PER_EVICTION_RUN,GenericKeyedObjectPool.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS,GenericKeyedObjectPool.DEFAULT_TEST_WHILE_IDLE);
    }

    /**
     * Create a new GenericKeyedObjectPoolFactory.
     *
     * @param factory the KeyedPoolableObjectFactory to used by created pools.
     * @param maxActive the maximum number of objects that can be borrowed from pools at one time.
     * @param whenExhaustedAction the action to take when the pool is exhausted.
     * @param maxWait the maximum amount of time to wait for an idle object when the pool is exhausted.
     * @param maxIdle the maximum number of idle objects in the pools.
     * @param testOnBorrow whether to validate objects before they are returned by borrowObject.
     * @param testOnReturn whether to validate objects after they are returned to returnObject.
     * @param timeBetweenEvictionRunsMillis the number of milliseconds to sleep between examining idle objects for eviction.
     * @param numTestsPerEvictionRun the number of idle objects to examine per run of the evictor.
     * @param minEvictableIdleTimeMillis the minimum number of milliseconds an object can sit idle in the pool before it is eligible for eviction.
     * @param testWhileIdle whether to validate objects in the idle object eviction thread.
     * @see GenericKeyedObjectPool#GenericKeyedObjectPool(KeyedPoolableObjectFactory, int, byte, long, int, boolean, boolean, long, int, long, boolean)
     */
    public GenericKeyedObjectPoolFactory(KeyedPoolableObjectFactory factory, int maxActive, byte whenExhaustedAction, long maxWait, int maxIdle, boolean testOnBorrow, boolean testOnReturn, long timeBetweenEvictionRunsMillis, int numTestsPerEvictionRun, long minEvictableIdleTimeMillis, boolean testWhileIdle) {
        this(factory, maxActive, whenExhaustedAction, maxWait, maxIdle, GenericKeyedObjectPool.DEFAULT_MAX_TOTAL, testOnBorrow, testOnReturn, timeBetweenEvictionRunsMillis, numTestsPerEvictionRun, minEvictableIdleTimeMillis, testWhileIdle);
    }

    /**
     * Create a new GenericKeyedObjectPoolFactory.
     *
     * @param factory the KeyedPoolableObjectFactory to used by created pools.
     * @param maxActive the maximum number of objects that can be borrowed from pools at one time.
     * @param whenExhaustedAction the action to take when the pool is exhausted.
     * @param maxWait the maximum amount of time to wait for an idle object when the pool is exhausted.
     * @param maxIdle the maximum number of idle objects in the pools.
     * @param maxTotal the maximum number of objects that can exists at one time.
     * @param testOnBorrow whether to validate objects before they are returned by borrowObject.
     * @param testOnReturn whether to validate objects after they are returned to returnObject.
     * @param timeBetweenEvictionRunsMillis the number of milliseconds to sleep between examining idle objects for eviction.
     * @param numTestsPerEvictionRun the number of idle objects to examine per run of the evictor.
     * @param minEvictableIdleTimeMillis the minimum number of milliseconds an object can sit idle in the pool before it is eligible for eviction.
     * @param testWhileIdle whether to validate objects in the idle object eviction thread.
     * @see GenericKeyedObjectPool#GenericKeyedObjectPool(KeyedPoolableObjectFactory, int, byte, long, int, int, boolean, boolean, long, int, long, boolean)
     */
    public GenericKeyedObjectPoolFactory(KeyedPoolableObjectFactory factory, int maxActive, byte whenExhaustedAction, long maxWait, int maxIdle, int maxTotal, boolean testOnBorrow, boolean testOnReturn, long timeBetweenEvictionRunsMillis, int numTestsPerEvictionRun, long minEvictableIdleTimeMillis, boolean testWhileIdle) {
        this(factory, maxActive, whenExhaustedAction, maxWait, maxIdle, maxTotal, GenericKeyedObjectPool.DEFAULT_MIN_IDLE , testOnBorrow, testOnReturn, timeBetweenEvictionRunsMillis, numTestsPerEvictionRun, minEvictableIdleTimeMillis, testWhileIdle);
    }

    /**
     * Create a new GenericKeyedObjectPoolFactory.
     *
     * @param factory the KeyedPoolableObjectFactory to used by created pools.
     * @param maxActive the maximum number of objects that can be borrowed from pools at one time.
     * @param whenExhaustedAction the action to take when the pool is exhausted.
     * @param maxWait the maximum amount of time to wait for an idle object when the pool is exhausted.
     * @param maxIdle the maximum number of idle objects in the pools.
     * @param maxTotal the maximum number of objects that can exists at one time.
     * @param minIdle the minimum number of idle objects to have in the pool at any one time.
     * @param testOnBorrow whether to validate objects before they are returned by borrowObject.
     * @param testOnReturn whether to validate objects after they are returned to returnObject.
     * @param timeBetweenEvictionRunsMillis the number of milliseconds to sleep between examining idle objects for eviction.
     * @param numTestsPerEvictionRun the number of idle objects to examine per run of the evictor.
     * @param minEvictableIdleTimeMillis the minimum number of milliseconds an object can sit idle in the pool before it is eligible for eviction.
     * @param testWhileIdle whether to validate objects in the idle object eviction thread.
     * @since Pool 1.3
     * @see GenericKeyedObjectPool#GenericKeyedObjectPool(KeyedPoolableObjectFactory, int, byte, long, int, int, int, boolean, boolean, long, int, long, boolean)
     */
    public GenericKeyedObjectPoolFactory(KeyedPoolableObjectFactory factory, int maxActive, byte whenExhaustedAction, long maxWait, int maxIdle, int maxTotal, int minIdle, boolean testOnBorrow, boolean testOnReturn, long timeBetweenEvictionRunsMillis, int numTestsPerEvictionRun, long minEvictableIdleTimeMillis, boolean testWhileIdle) {
        this(factory, maxActive, whenExhaustedAction, maxWait, maxIdle, maxTotal, minIdle, testOnBorrow, testOnReturn, timeBetweenEvictionRunsMillis, numTestsPerEvictionRun, minEvictableIdleTimeMillis, testWhileIdle, GenericKeyedObjectPool.DEFAULT_LIFO);
    }

    /**
     * Create a new GenericKeyedObjectPoolFactory.
     *
     * @param factory the KeyedPoolableObjectFactory to used by created pools.
     * @param maxActive the maximum number of objects that can be borrowed from pools at one time.
     * @param whenExhaustedAction the action to take when the pool is exhausted.
     * @param maxWait the maximum amount of time to wait for an idle object when the pool is exhausted.
     * @param maxIdle the maximum number of idle objects in the pools.
     * @param maxTotal the maximum number of objects that can exists at one time.
     * @param minIdle the minimum number of idle objects to have in the pool at any one time.
     * @param testOnBorrow whether to validate objects before they are returned by borrowObject.
     * @param testOnReturn whether to validate objects after they are returned to returnObject.
     * @param timeBetweenEvictionRunsMillis the number of milliseconds to sleep between examining idle objects for eviction.
     * @param numTestsPerEvictionRun the number of idle objects to examine per run of the evictor.
     * @param minEvictableIdleTimeMillis the minimum number of milliseconds an object can sit idle in the pool before it is eligible for eviction.
     * @param testWhileIdle whether to validate objects in the idle object eviction thread.
     * @param lifo whether or not objects are returned in last-in-first-out order from the idle object pool.
     * @since Pool 1.4
     * @see GenericKeyedObjectPool#GenericKeyedObjectPool(KeyedPoolableObjectFactory, int, byte, long, int, int, int, boolean, boolean, long, int, long, boolean, boolean)
     */
    public GenericKeyedObjectPoolFactory(KeyedPoolableObjectFactory factory, int maxActive, byte whenExhaustedAction, long maxWait, int maxIdle, int maxTotal, int minIdle, boolean testOnBorrow, boolean testOnReturn, long timeBetweenEvictionRunsMillis, int numTestsPerEvictionRun, long minEvictableIdleTimeMillis, boolean testWhileIdle, boolean lifo) {
        _maxIdle = maxIdle;
        _maxActive = maxActive;
        _maxTotal = maxTotal;
        _minIdle = minIdle;
        _maxWait = maxWait;
        _whenExhaustedAction = whenExhaustedAction;
        _testOnBorrow = testOnBorrow;
        _testOnReturn = testOnReturn;
        _testWhileIdle = testWhileIdle;
        _timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
        _numTestsPerEvictionRun = numTestsPerEvictionRun;
        _minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
        _factory = factory;
        _lifo = lifo;
    }

    public KeyedObjectPool createPool() {
        return new GenericKeyedObjectPool(_factory,_maxActive,_whenExhaustedAction,_maxWait,_maxIdle,_maxTotal,_minIdle,_testOnBorrow,_testOnReturn,_timeBetweenEvictionRunsMillis,_numTestsPerEvictionRun,_minEvictableIdleTimeMillis,_testWhileIdle,_lifo);
    }

    //--- protected attributes ---------------------------------------

    protected int _maxIdle = GenericKeyedObjectPool.DEFAULT_MAX_IDLE;
    protected int _maxActive = GenericKeyedObjectPool.DEFAULT_MAX_ACTIVE;
    protected int _maxTotal = GenericKeyedObjectPool.DEFAULT_MAX_TOTAL;
    protected int _minIdle = GenericKeyedObjectPool.DEFAULT_MIN_IDLE;
    protected long _maxWait = GenericKeyedObjectPool.DEFAULT_MAX_WAIT;
    protected byte _whenExhaustedAction = GenericKeyedObjectPool.DEFAULT_WHEN_EXHAUSTED_ACTION;
    protected boolean _testOnBorrow = GenericKeyedObjectPool.DEFAULT_TEST_ON_BORROW;
    protected boolean _testOnReturn = GenericKeyedObjectPool.DEFAULT_TEST_ON_RETURN;
    protected boolean _testWhileIdle = GenericKeyedObjectPool.DEFAULT_TEST_WHILE_IDLE;
    protected long _timeBetweenEvictionRunsMillis = GenericKeyedObjectPool.DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS;
    protected int _numTestsPerEvictionRun =  GenericKeyedObjectPool.DEFAULT_NUM_TESTS_PER_EVICTION_RUN;
    protected long _minEvictableIdleTimeMillis = GenericKeyedObjectPool.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS;
    protected KeyedPoolableObjectFactory _factory = null;
    protected boolean _lifo = GenericKeyedObjectPool.DEFAULT_LIFO;

}
