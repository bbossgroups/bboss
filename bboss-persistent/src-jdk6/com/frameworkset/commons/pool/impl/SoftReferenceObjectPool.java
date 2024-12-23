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

import java.lang.ref.SoftReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.frameworkset.commons.pool.BaseObjectPool;
import com.frameworkset.commons.pool.ObjectPool;
import com.frameworkset.commons.pool.PoolableObjectFactory;
import com.frameworkset.commons.pool.PoolUtils;

/**
 * A {@link java.lang.ref.SoftReference SoftReference} based
 * {@link ObjectPool}.
 *
 * @author Rodney Waldhoff
 * @author Sandy McArthur
 * @version $Revision: 777748 $ $Date: 2009-05-22 20:00:44 -0400 (Fri, 22 May 2009) $
 * @since Pool 1.0
 */
public class SoftReferenceObjectPool extends BaseObjectPool implements ObjectPool {
    /**
     * Create a <code>SoftReferenceObjectPool</code> without a factory.
     * {@link #setFactory(PoolableObjectFactory) setFactory} should be called
     * before any attempts to use the pool are made.
     * Generally speaking you should prefer the {@link #SoftReferenceObjectPool(PoolableObjectFactory)} constructor.
     *
     * @see #SoftReferenceObjectPool(PoolableObjectFactory)
     */
    public SoftReferenceObjectPool() {
        _pool = new ArrayList();
        _factory = null;
    }

    /**
     * Create a <code>SoftReferenceObjectPool</code> with the specified factory.
     *
     * @param factory object factory to use.
     */
    public SoftReferenceObjectPool(PoolableObjectFactory factory) {
        _pool = new ArrayList();
        _factory = factory;
    }

    /**
     * Create a <code>SoftReferenceObjectPool</code> with the specified factory and initial idle object count.
     *
     * @param factory object factory to use.
     * @param initSize initial size to attempt to prefill the pool.
     * @throws Exception when there is a problem prefilling the pool.
     * @throws IllegalArgumentException when <code>factory</code> is <code>null</code>.
     * @deprecated because this is a SoftReference pool, prefilled idle obejects may be garbage collected before they are used.
     *      To be removed in Pool 3.0.
     */
    public SoftReferenceObjectPool(PoolableObjectFactory factory, int initSize) throws Exception, IllegalArgumentException {
        if (factory == null) {
            throw new IllegalArgumentException("factory required to prefill the pool.");
        }
        _pool = new ArrayList(initSize);
        _factory = factory;
        PoolUtils.prefill(this, initSize);
    }

    public synchronized Object borrowObject() throws Exception {
        assertOpen();
        Object obj = null;
        boolean newlyCreated = false;
        while(null == obj) {
            if(_pool.isEmpty()) {
                if(null == _factory) {
                    throw new NoSuchElementException();
                } else {
                    newlyCreated = true;
                    obj = _factory.makeObject();
                }
            } else {
                SoftReference ref = (SoftReference)(_pool.remove(_pool.size() - 1));
                obj = ref.get();
                ref.clear(); // prevent this ref from being enqueued with refQueue.
            }
            if (null != _factory && null != obj) {
                try {
                    _factory.activateObject(obj);
                    if (!_factory.validateObject(obj)) {
                        throw new Exception("ValidateObject failed");
                    }
                } catch (Throwable t) {
                    try {
                        _factory.destroyObject(obj);
                    } catch (Throwable t2) {
                        // swallowed
                    } finally {
                        obj = null;
                    }
                    if (newlyCreated) {
                        throw new NoSuchElementException(
                            "Could not create a validated object, cause: " +
                            t.getMessage());
                    }
                }
            }
        }
        _numActive++;
        return obj;
    }

    public synchronized void returnObject(Object obj) throws Exception {
        boolean success = !isClosed();
        if (_factory != null) {
            if(!_factory.validateObject(obj)) {
                success = false;
            } else {
                try {
                    _factory.passivateObject(obj);
                } catch(Exception e) {
                    success = false;
                }
            }
        }

        boolean shouldDestroy = !success;
        _numActive--;
        if(success) {
            _pool.add(new SoftReference(obj, refQueue));
        }
        notifyAll(); // _numActive has changed

        if (shouldDestroy && _factory != null) {
            try {
                _factory.destroyObject(obj);
            } catch(Exception e) {
                // ignored
            }
        }
    }

    public synchronized void invalidateObject(Object obj) throws Exception {
        _numActive--;
        if (_factory != null) {
            _factory.destroyObject(obj);
        }
        notifyAll(); // _numActive has changed
    }

    /**
     * Create an object, and place it into the pool.
     * addObject() is useful for "pre-loading" a pool with idle objects.
     */
    public synchronized void addObject() throws Exception {
        assertOpen();
        if (_factory == null) {
            throw new IllegalStateException("Cannot add objects without a factory.");
        }
        Object obj = _factory.makeObject();

        boolean success = true;
        if(!_factory.validateObject(obj)) {
            success = false;
        } else {
            _factory.passivateObject(obj);
        }

        boolean shouldDestroy = !success;
        if(success) {
            _pool.add(new SoftReference(obj, refQueue));
            notifyAll(); // _numActive has changed
        }

        if(shouldDestroy) {
            try {
                _factory.destroyObject(obj);
            } catch(Exception e) {
                // ignored
            }
        }
    }

    /** Returns an approximation not less than the of the number of idle instances in the pool. */
    public synchronized int getNumIdle() {
        pruneClearedReferences();
        return _pool.size();
    }

    /**
     * Return the number of instances currently borrowed from this pool.
     *
     * @return the number of instances currently borrowed from this pool
     */
    public synchronized int getNumActive() {
        return _numActive;
    }

    /**
     * Clears any objects sitting idle in the pool.
     */
    public synchronized void clear() {
        if(null != _factory) {
            Iterator iter = _pool.iterator();
            while(iter.hasNext()) {
                try {
                    Object obj = ((SoftReference)iter.next()).get();
                    if(null != obj) {
                        _factory.destroyObject(obj);
                    }
                } catch(Exception e) {
                    // ignore error, keep destroying the rest
                }
            }
        }
        _pool.clear();
        pruneClearedReferences();
    }

    public void close() throws Exception {
        super.close();
        clear();
    }

    /**
     * Sets the {@link PoolableObjectFactory factory} this pool uses
     * to create new instances. Trying to change
     * the <code>factory</code> while there are borrowed objects will
     * throw an {@link IllegalStateException}.
     *
     * @param factory the {@link PoolableObjectFactory} used to create new instances.
     * @throws IllegalStateException when the factory cannot be set at this time
     */
    public synchronized void setFactory(PoolableObjectFactory factory) throws IllegalStateException {
        assertOpen();
        if(0 < getNumActive()) {
            throw new IllegalStateException("Objects are already active");
        } else {
            clear();
            _factory = factory;
        }
    }

    /**
     * If any idle objects were garbage collected, remove their
     * {@link Reference} wrappers from the idle object pool.
     */
    private void pruneClearedReferences() {
        Reference ref;
        while ((ref = refQueue.poll()) != null) {
            try {
                _pool.remove(ref);
            } catch (UnsupportedOperationException uoe) {
                // ignored
            }
        }
    }

    /** My pool. */
    private List _pool = null;

    /** My {@link PoolableObjectFactory}. */
    private PoolableObjectFactory _factory = null;

    /**
     * Queue of broken references that might be able to be removed from <code>_pool</code>.
     * This is used to help {@link #getNumIdle()} be more accurate with minimial
     * performance overhead.
     */
    private final ReferenceQueue refQueue = new ReferenceQueue();

    /** Number of active objects. */
    private int _numActive = 0;
}
