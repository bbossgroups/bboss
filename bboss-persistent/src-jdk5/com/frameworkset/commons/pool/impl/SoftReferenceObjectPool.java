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

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.frameworkset.commons.pool.BaseObjectPool;
import com.frameworkset.commons.pool.ObjectPool;
import com.frameworkset.commons.pool.PoolableObjectFactory;

/**
 * A {@link java.lang.ref.SoftReference SoftReference} based
 * {@link ObjectPool}.
 *
 * @author Rodney Waldhoff
 * @version $Revision: 383290 $ $Date: 2006-03-05 02:00:15 -0500 (Sun, 05 Mar 2006) $
 */
public class SoftReferenceObjectPool extends BaseObjectPool implements ObjectPool {
    public SoftReferenceObjectPool() {
        _pool = new ArrayList();
        _factory = null;
    }

    public SoftReferenceObjectPool(PoolableObjectFactory factory) {
        _pool = new ArrayList();
        _factory = factory;
    }

    public SoftReferenceObjectPool(PoolableObjectFactory factory, int initSize) throws Exception {
        _pool = new ArrayList();
        _factory = factory;
        if(null != _factory) {
            for(int i=0;i<initSize;i++) {
                Object obj = _factory.makeObject();
                _factory.passivateObject(obj);
                _pool.add(new SoftReference(obj));
            }
        }
    }

    public synchronized Object borrowObject() throws Exception {        
        assertOpen();
        Object obj = null;
        while(null == obj) {
            if(_pool.isEmpty()) {
                if(null == _factory) {
                    throw new NoSuchElementException();
                } else {
                    obj = _factory.makeObject();
                }
            } else {
                SoftReference ref = (SoftReference)(_pool.remove(_pool.size() - 1));
                obj = ref.get();
            }
            if(null != _factory && null != obj) {
                _factory.activateObject(obj);
            }
            if (null != _factory && null != obj && !_factory.validateObject(obj)) {
                _factory.destroyObject(obj);
                obj = null;
            }
        }
        _numActive++;
        return obj;
    }

    public synchronized void returnObject(Object obj) throws Exception {
        assertOpen();
        boolean success = true;
        if(!(_factory.validateObject(obj))) {
            success = false;
        } else {
            try {
                _factory.passivateObject(obj);
            } catch(Exception e) {
                success = false;
            }
        }

        boolean shouldDestroy = !success;
        _numActive--;
        if(success) {
            _pool.add(new SoftReference(obj));
        }
        notifyAll(); // _numActive has changed

        if(shouldDestroy) {
            try {
                _factory.destroyObject(obj);
            } catch(Exception e) {
                // ignored
            }
        }

    }

    public synchronized void invalidateObject(Object obj) throws Exception {
        assertOpen();
        _numActive--;
        _factory.destroyObject(obj);
        notifyAll(); // _numActive has changed
    }

    /**
     * Create an object, and place it into the pool.
     * addObject() is useful for "pre-loading" a pool with idle objects.
     */
    public synchronized void addObject() throws Exception {
        assertOpen();
        Object obj = _factory.makeObject();
        _numActive++;   // A little slimy - must do this because returnObject decrements it.
        returnObject(obj);
    }

    /** Returns an approximation not less than the of the number of idle instances in the pool. */
    public synchronized int getNumIdle() {
        assertOpen();
        return _pool.size();
    }

    public synchronized int getNumActive() {
        assertOpen();
        return _numActive;
    }

    public synchronized void clear() {
        assertOpen();
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
    }

    public synchronized void close() throws Exception {
        clear();
        _pool = null;
        _factory = null;
        super.close();
    }

    public synchronized void setFactory(PoolableObjectFactory factory) throws IllegalStateException {
        assertOpen();
        if(0 < getNumActive()) {
            throw new IllegalStateException("Objects are already active");
        } else {
            clear();
            _factory = factory;
        }
    }

    /** My pool. */
    private List _pool = null;

    /** My {@link PoolableObjectFactory}. */
    private PoolableObjectFactory _factory = null;

    /** Number of active objects. */
    private int _numActive = 0;
}
