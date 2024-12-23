/*
 * Copyright 2006 The Apache Software Foundation.
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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class consists exclusively of static methods that operate on or return keyedPool related interfaces.
 *
 * @author Sandy McArthur
 * @version $Revision: 385296 $ $Date: 2006-03-12 10:28:08 -0500 (Sun, 12 Mar 2006) $
 * @since Pool 1.3
 */
public final class PoolUtils {

    /**
     * Timer used to periodically check pools idle object count.
     * Because a {@link Timer} creates a {@link Thread} this is lazily instantiated.
     */
    private static Timer MIN_IDLE_TIMER;

    /**
     * PoolUtils instances should NOT be constructed in standard programming.
     * Instead, the class should be used procedurally: PoolUtils.adapt(aPool);.
     * This constructor is public to permit tools that require a JavaBean instance to operate.
     */
    public PoolUtils() {
    }

    /**
     * Adapt a <code>KeyedPoolableObjectFactory</code> instance to work where a <code>PoolableObjectFactory</code> is
     * needed. This method is the equivalent of calling
     * {@link #adapt(KeyedPoolableObjectFactory, Object) PoolUtils.adapt(aKeyedPoolableObjectFactory, new Object())}.
     *
     * @param keyedFactory the {@link KeyedPoolableObjectFactory} to delegate to.
     * @return a {@link PoolableObjectFactory} that delegates to <code>keyedFactory</code> with an internal key.
     * @throws IllegalArgumentException when <code>keyedFactory</code> is <code>null</code>.
     * @see #adapt(KeyedPoolableObjectFactory, Object)
     * @since Pool 1.3
     */
    public static PoolableObjectFactory adapt(final KeyedPoolableObjectFactory keyedFactory) throws IllegalArgumentException {
        return adapt(keyedFactory, new Object());
    }

    /**
     * Adapt a <code>KeyedPoolableObjectFactory</code> instance to work where a <code>PoolableObjectFactory</code> is
     * needed using the specified <code>key</code> when delegating.
     *
     * @param keyedFactory the {@link KeyedPoolableObjectFactory} to delegate to.
     * @param key the key to use when delegating.
     * @return a {@link PoolableObjectFactory} that delegates to <code>keyedFactory</code> with the specified key.
     * @throws IllegalArgumentException when <code>keyedFactory</code> or <code>key</code> is <code>null</code>.
     * @see #adapt(KeyedPoolableObjectFactory)
     * @since Pool 1.3
     */
    public static PoolableObjectFactory adapt(final KeyedPoolableObjectFactory keyedFactory, final Object key) throws IllegalArgumentException {
        return new PoolableObjectFactoryAdaptor(keyedFactory, key);
    }

    /**
     * Adapt a <code>PoolableObjectFactory</code> instance to work where a <code>KeyedPoolableObjectFactory</code> is
     * needed. The key is ignored.
     *
     * @param factory the {@link PoolableObjectFactory} to delegate to.
     * @return a {@link KeyedPoolableObjectFactory} that delegates to <code>factory</code> ignoring the key.
     * @throws IllegalArgumentException when <code>factory</code> is <code>null</code>.
     * @since Pool 1.3
     */
    public static KeyedPoolableObjectFactory adapt(final PoolableObjectFactory factory) throws IllegalArgumentException {
        return new KeyedPoolableObjectFactoryAdaptor(factory);
    }

    /**
     * Adapt a <code>KeyedObjectPool</code> instance to work where an <code>ObjectPool</code> is needed. This is the
     * equivalent of calling {@link #adapt(KeyedObjectPool, Object) PoolUtils.adapt(aKeyedObjectPool, new Object())}.
     *
     * @param keyedPool the {@link KeyedObjectPool} to delegate to.
     * @return an {@link ObjectPool} that delegates to <code>keyedPool</code> with an internal key.
     * @throws IllegalArgumentException when <code>keyedPool</code> is <code>null</code>.
     * @see #adapt(KeyedObjectPool, Object)
     * @since Pool 1.3
     */
    public static ObjectPool adapt(final KeyedObjectPool keyedPool) throws IllegalArgumentException {
        return adapt(keyedPool, new Object());
    }

    /**
     * Adapt a <code>KeyedObjectPool</code> instance to work where an <code>ObjectPool</code> is needed using the
     * specified <code>key</code> when delegating.
     *
     * @param keyedPool the {@link KeyedObjectPool} to delegate to.
     * @param key the key to use when delegating.
     * @return an {@link ObjectPool} that delegates to <code>keyedPool</code> with the specified key.
     * @throws IllegalArgumentException when <code>keyedPool</code> or <code>key</code> is <code>null</code>.
     * @see #adapt(KeyedObjectPool)
     * @since Pool 1.3
     */
    public static ObjectPool adapt(final KeyedObjectPool keyedPool, final Object key) throws IllegalArgumentException {
        return new ObjectPoolAdaptor(keyedPool, key);
    }

    /**
     * Adapt an <code>ObjectPool</code> to work where an <code>KeyedObjectPool</code> is needed.
     * The key is ignored.
     *
     * @param pool the {@link ObjectPool} to delegate to.
     * @return a {@link KeyedObjectPool} that delegates to <code>keyedPool</code> ignoring the key.
     * @throws IllegalArgumentException when <code>keyedPool</code> is <code>null</code>.
     * @since Pool 1.3
     */
    public static KeyedObjectPool adapt(final ObjectPool pool) throws IllegalArgumentException {
        return new KeyedObjectPoolAdaptor(pool);
    }

    /**
     * Wraps an <code>ObjectPool</code> and dynamically checks the type of objects borrowed and returned to the keyedPool.
     * If an object is passed to the keyedPool that isn't of type <code>type</code> a {@link ClassCastException} will be thrown.
     *
     * @param pool the keyedPool to enforce type safety on
     * @return an <code>ObjectPool</code> that will only allow objects of <code>type</code>
     * @since Pool 1.3
     */
    public static ObjectPool checkedPool(final ObjectPool pool, final Class type) {
        if (pool == null) {
            throw new IllegalArgumentException("pool must not be null.");
        }
        if (type == null) {
            throw new IllegalArgumentException("type must not be null.");
        }
        return new CheckedObjectPool(pool, type);
    }

    /**
     * Wraps an <code>KeyedObjectPool</code> and dynamically checks the type of objects borrowed and returned to the keyedPool.
     * If an object is passed to the keyedPool that isn't of type <code>type</code> a {@link ClassCastException} will be thrown.
     *
     * @param keyedPool the keyedPool to enforce type safety on
     * @return an <code>KeyedObjectPool</code> that will only allow objects of <code>type</code>
     * @since Pool 1.3
     */
    public static KeyedObjectPool checkedPool(final KeyedObjectPool keyedPool, final Class type) {
        if (keyedPool == null) {
            throw new IllegalArgumentException("keyedPool must not be null.");
        }
        if (type == null) {
            throw new IllegalArgumentException("type must not be null.");
        }
        return new CheckedKeyedObjectPool(keyedPool, type);
    }

    /**
     * Periodically check the idle object count for the keyedPool. At most one idle object will be added per period.
     * If there is an exception when calling {@link ObjectPool#addObject()} then no more checks will be performed.
     *
     * @param pool the keyedPool to check periodically.
     * @param minIdle if the {@link ObjectPool#getNumIdle()} is less than this then add an idle object.
     * @param period the frequency to check the number of idle objects in a keyedPool, see
     *      {@link Timer#schedule(TimerTask, long, long)}.
     * @return the {@link TimerTask} that will periodically check the pools idle object count.
     * @throws IllegalArgumentException when <code>keyedPool</code> is <code>null</code> or
     *      when <code>minIdle</code> is negative or when <code>period</code> isn't
     *      valid for {@link Timer#schedule(TimerTask, long, long)}.
     * @since Pool 1.3
     */
    public static TimerTask checkMinIdle(final ObjectPool pool, final int minIdle, final long period) throws IllegalArgumentException {
        if (pool == null) {
            throw new IllegalArgumentException("keyedPool must not be null.");
        }
        if (minIdle < 0) {
            throw new IllegalArgumentException("minIdle must be non-negative.");
        }
        final TimerTask task = new ObjectPoolMinIdleTimerTask(pool, minIdle);
        getMinIdleTimer().schedule(task, 0L, period);
        return task;
    }

    /**
     * Periodically check the idle object count for the key in the keyedPool. At most one idle object will be added per period.
     * If there is an exception when calling {@link KeyedObjectPool#addObject(Object)} then no more checks for that key
     * will be performed.
     *
     * @param keyedPool the keyedPool to check periodically.
     * @param key the key to check the idle count of.
     * @param minIdle if the {@link KeyedObjectPool#getNumIdle(Object)} is less than this then add an idle object.
     * @param period the frequency to check the number of idle objects in a keyedPool, see
     *      {@link Timer#schedule(TimerTask, long, long)}.
     * @return the {@link TimerTask} that will periodically check the pools idle object count.
     * @throws IllegalArgumentException when <code>keyedPool</code>, <code>key</code> is <code>null</code> or
     *      when <code>minIdle</code> is negative or when <code>period</code> isn't
     *      valid for {@link Timer#schedule(TimerTask, long, long)}.
     * @since Pool 1.3
     */
    public static TimerTask checkMinIdle(final KeyedObjectPool keyedPool, final Object key, final int minIdle, final long period) throws IllegalArgumentException {
        if (keyedPool == null) {
            throw new IllegalArgumentException("keyedPool must not be null.");
        }
        if (key == null) {
            throw new IllegalArgumentException("key must not be null.");
        }
        if (minIdle < 0) {
            throw new IllegalArgumentException("minIdle must be non-negative.");
        }
        final TimerTask task = new KeyedObjectPoolMinIdleTimerTask(keyedPool, key, minIdle);
        getMinIdleTimer().schedule(task, 0L, period);
        return task;
    }

    /**
     * Periodically check the idle object count for each key in the <code>Collection</code> <code>keys</code> in the keyedPool.
     * At most one idle object will be added per period.
     *
     * @param keyedPool the keyedPool to check periodically.
     * @param keys a collection of keys to check the idle object count.
     * @param minIdle if the {@link KeyedObjectPool#getNumIdle(Object)} is less than this then add an idle object.
     * @param period the frequency to check the number of idle objects in a keyedPool, see
     *      {@link Timer#schedule(TimerTask, long, long)}.
     * @return a {@link Map} of key and {@link TimerTask} pairs that will periodically check the pools idle object count.
     * @throws IllegalArgumentException when <code>keyedPool</code>, <code>keys</code>, or any of the values in the
     *      collection is <code>null</code> or when <code>minIdle</code> is negative or when <code>period</code> isn't
     *      valid for {@link Timer#schedule(TimerTask, long, long)}.
     * @see #checkMinIdle(KeyedObjectPool, Object, int, long)
     * @since Pool 1.3
     */
    public static Map checkMinIdle(final KeyedObjectPool keyedPool, final Collection keys, final int minIdle, final long period) throws IllegalArgumentException {
        if (keys == null) {
            throw new IllegalArgumentException("keys must not be null.");
        }
        final Map tasks = new HashMap(keys.size());
        final Iterator iter = keys.iterator();
        while (iter.hasNext()) {
            final Object key = iter.next();
            final TimerTask task = checkMinIdle(keyedPool, key, minIdle, period);
            tasks.put(key, task);
        }
        return tasks;
    }

    /**
     * Call <code>addObject()</code> on <code>keyedPool</code> <code>count</code> number of times.
     *
     * @param pool the keyedPool to prefill.
     * @param count the number of idle objects to add.
     * @throws Exception when {@link ObjectPool#addObject()} fails.
     * @throws IllegalArgumentException when <code>keyedPool</code> is <code>null</code>.
     * @since Pool 1.3
     */
    public static void prefill(final ObjectPool pool, final int count) throws Exception, IllegalArgumentException {
        if (pool == null) {
            throw new IllegalArgumentException("keyedPool must not be null.");
        }
        for (int i = 0; i < count; i++) {
            pool.addObject();
        }
    }

    /**
     * Call <code>addObject(Object)</code> on <code>keyedPool</code> with <code>key</code> <code>count</code>
     * number of times.
     *
     * @param keyedPool the keyedPool to prefill.
     * @param key the key to add objects for.
     * @param count the number of idle objects to add for <code>key</code>.
     * @throws Exception when {@link KeyedObjectPool#addObject(Object)} fails.
     * @throws IllegalArgumentException when <code>keyedPool</code> or <code>key</code> is <code>null</code>.
     * @since Pool 1.3
     */
    public static void prefill(final KeyedObjectPool keyedPool, final Object key, final int count) throws Exception, IllegalArgumentException {
        if (keyedPool == null) {
            throw new IllegalArgumentException("keyedPool must not be null.");
        }
        if (key == null) {
            throw new IllegalArgumentException("key must not be null.");
        }
        for (int i = 0; i < count; i++) {
            keyedPool.addObject(key);
        }
    }

    /**
     * Call <code>addObject(Object)</code> on <code>keyedPool</code> with each key in <code>keys</code> for
     * <code>count</code> number of times. This has the same effect as calling
     * {@link #prefill(KeyedObjectPool, Object, int)} for each key in the <code>keys</code> collection.
     *
     * @param keyedPool the keyedPool to prefill.
     * @param keys {@link Collection} of keys to add objects for.
     * @param count the number of idle objects to add for each <code>key</code>.
     * @throws Exception when {@link KeyedObjectPool#addObject(Object)} fails.
     * @throws IllegalArgumentException when <code>keyedPool</code>, <code>keys</code>, or
     *      any value in <code>keys</code> is <code>null</code>.
     * @see #prefill(KeyedObjectPool, Object, int)
     * @since Pool 1.3
     */
    public static void prefill(final KeyedObjectPool keyedPool, final Collection keys, final int count) throws Exception, IllegalArgumentException {
        if (keys == null) {
            throw new IllegalArgumentException("keys must not be null.");
        }
        final Iterator iter = keys.iterator();
        while (iter.hasNext()) {
            prefill(keyedPool, iter.next(), count);
        }
    }

    /**
     * Returns a synchronized (thread-safe) ObjectPool backed by the specified ObjectPool.
     *
     * @param pool the ObjectPool to be "wrapped" in a synchronized ObjectPool.
     * @return a synchronized view of the specified ObjectPool.
     * @since Pool 1.3
     */
    public static ObjectPool synchronizedPool(final ObjectPool pool) {
        return new SynchronizedObjectPool(pool);
    }

    /**
     * Returns a synchronized (thread-safe) KeyedObjectPool backed by the specified KeyedObjectPool.
     * 
     * @param keyedPool the KeyedObjectPool to be "wrapped" in a synchronized KeyedObjectPool.
     * @return a synchronized view of the specified KeyedObjectPool.
     * @since Pool 1.3
     */
    public static KeyedObjectPool synchronizedPool(final KeyedObjectPool keyedPool) {
        return new SynchronizedKeyedObjectPool(keyedPool);
    }

    /**
     * Returns a synchronized (thread-safe) PoolableObjectFactory backed by the specified PoolableObjectFactory.
     *
     * @param factory the PoolableObjectFactory to be "wrapped" in a synchronized PoolableObjectFactory.
     * @return a synchronized view of the specified PoolableObjectFactory.
     * @since Pool 1.3
     */
    public static PoolableObjectFactory synchronizedPoolableFactory(final PoolableObjectFactory factory) {
        return new SynchronizedPoolableObjectFactory(factory);
    }

    /**
     * Returns a synchronized (thread-safe) KeyedPoolableObjectFactory backed by the specified KeyedPoolableObjectFactory.
     *
     * @param keyedFactory the KeyedPoolableObjectFactory to be "wrapped" in a synchronized KeyedPoolableObjectFactory.
     * @return a synchronized view of the specified KeyedPoolableObjectFactory.
     * @since Pool 1.3
     */
    public static KeyedPoolableObjectFactory synchronizedPoolableFactory(final KeyedPoolableObjectFactory keyedFactory) {
        return new SynchronizedKeyedPoolableObjectFactory(keyedFactory);
    }

    /**
     * Get the <code>Timer</code> for checking keyedPool's idle count. Lazily create the {@link Timer} as needed.
     *
     * @return the {@link Timer} for checking keyedPool's idle count.
     * @since Pool 1.3
     */
    private static synchronized Timer getMinIdleTimer() {
        if (MIN_IDLE_TIMER == null) {
            MIN_IDLE_TIMER = new Timer(true);
        }
        return MIN_IDLE_TIMER;
    }

    private static class PoolableObjectFactoryAdaptor implements PoolableObjectFactory {
        private final Object key;
        private final KeyedPoolableObjectFactory keyedFactory;

        PoolableObjectFactoryAdaptor(final KeyedPoolableObjectFactory keyedFactory, final Object key) throws IllegalArgumentException {
            if (keyedFactory == null) {
                throw new IllegalArgumentException("keyedFactory must not be null.");
            }
            if (key == null) {
                throw new IllegalArgumentException("key must not be null.");
            }
            this.keyedFactory = keyedFactory;
            this.key = key;
        }

        public Object makeObject() throws Exception {
            return keyedFactory.makeObject(key);
        }

        public void destroyObject(final Object obj) throws Exception {
            keyedFactory.destroyObject(key, obj);
        }

        public boolean validateObject(final Object obj) {
            return keyedFactory.validateObject(key, obj);
        }

        public void activateObject(final Object obj) throws Exception {
            keyedFactory.activateObject(key, obj);
        }

        public void passivateObject(final Object obj) throws Exception {
            keyedFactory.passivateObject(key, obj);
        }

        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("PoolableObjectFactoryAdaptor");
            sb.append("{key=").append(key);
            sb.append(", keyedFactory=").append(keyedFactory);
            sb.append('}');
            return sb.toString();
        }
    }

    private static class KeyedPoolableObjectFactoryAdaptor implements KeyedPoolableObjectFactory {
        private final PoolableObjectFactory factory;

        KeyedPoolableObjectFactoryAdaptor(final PoolableObjectFactory factory) throws IllegalArgumentException {
            if (factory == null) {
                throw new IllegalArgumentException("factory must not be null.");
            }
            this.factory = factory;
        }

        public Object makeObject(final Object key) throws Exception {
            return factory.makeObject();
        }

        public void destroyObject(final Object key, final Object obj) throws Exception {
            factory.destroyObject(obj);
        }

        public boolean validateObject(final Object key, final Object obj) {
            return factory.validateObject(obj);
        }

        public void activateObject(final Object key, final Object obj) throws Exception {
            factory.activateObject(obj);
        }

        public void passivateObject(final Object key, final Object obj) throws Exception {
            factory.passivateObject(obj);
        }

        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("KeyedPoolableObjectFactoryAdaptor");
            sb.append("{factory=").append(factory);
            sb.append('}');
            return sb.toString();
        }
    }

    private static class ObjectPoolAdaptor implements ObjectPool {
        private final Object key;
        private final KeyedObjectPool keyedPool;

        ObjectPoolAdaptor(final KeyedObjectPool keyedPool, final Object key) throws IllegalArgumentException {
            if (keyedPool == null) {
                throw new IllegalArgumentException("keyedPool must not be null.");
            }
            if (key == null) {
                throw new IllegalArgumentException("key must not be null.");
            }
            this.keyedPool = keyedPool;
            this.key = key;
        }

        public Object borrowObject() throws Exception, NoSuchElementException, IllegalStateException {
            return keyedPool.borrowObject(key);
        }

        public void returnObject(final Object obj) throws Exception {
            keyedPool.returnObject(key, obj);
        }

        public void invalidateObject(final Object obj) throws Exception {
            keyedPool.invalidateObject(key, obj);
        }

        public void addObject() throws Exception, IllegalStateException {
            keyedPool.addObject(key);
        }

        public int getNumIdle() throws UnsupportedOperationException {
            return keyedPool.getNumIdle(key);
        }

        public int getNumActive() throws UnsupportedOperationException {
            return keyedPool.getNumActive(key);
        }

        public void clear() throws Exception, UnsupportedOperationException {
            keyedPool.clear();
        }

        public void close() throws Exception {
            keyedPool.close();
        }

        public void setFactory(final PoolableObjectFactory factory) throws IllegalStateException, UnsupportedOperationException {
            keyedPool.setFactory(adapt(factory));
        }

        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("ObjectPoolAdaptor");
            sb.append("{key=").append(key);
            sb.append(", keyedPool=").append(keyedPool);
            sb.append('}');
            return sb.toString();
        }
    }

    private static class KeyedObjectPoolAdaptor implements KeyedObjectPool {
        private final ObjectPool pool;

        KeyedObjectPoolAdaptor(final ObjectPool pool) throws IllegalArgumentException {
            if (pool == null) {
                throw new IllegalArgumentException("keyedPool must not be null.");
            }
            this.pool = pool;
        }

        public Object borrowObject(final Object key) throws Exception, NoSuchElementException, IllegalStateException {
            return pool.borrowObject();
        }

        public void returnObject(final Object key, final Object obj) throws Exception {
            pool.returnObject(obj);
        }

        public void invalidateObject(final Object key, final Object obj) throws Exception {
            pool.invalidateObject(obj);
        }

        public void addObject(final Object key) throws Exception, IllegalStateException {
            pool.addObject();
        }

        public int getNumIdle(final Object key) throws UnsupportedOperationException {
            return pool.getNumIdle();
        }

        public int getNumActive(final Object key) throws UnsupportedOperationException {
            return pool.getNumActive();
        }

        public int getNumIdle() throws UnsupportedOperationException {
            return pool.getNumIdle();
        }

        public int getNumActive() throws UnsupportedOperationException {
            return pool.getNumActive();
        }

        public void clear() throws Exception, UnsupportedOperationException {
            pool.clear();
        }

        public void clear(final Object key) throws Exception, UnsupportedOperationException {
            pool.clear();
        }

        public void close() throws Exception {
            pool.close();
        }

        public void setFactory(final KeyedPoolableObjectFactory factory) throws IllegalStateException, UnsupportedOperationException {
            pool.setFactory(adapt(factory));
        }

        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("KeyedObjectPoolAdaptor");
            sb.append("{keyedPool=").append(pool);
            sb.append('}');
            return sb.toString();
        }
    }

    private static class CheckedObjectPool implements ObjectPool {
        private final Class type;
        private final ObjectPool pool;

        CheckedObjectPool(final ObjectPool pool, final Class type) {
            if (pool == null) {
                throw new IllegalArgumentException("pool must not be null.");
            }
            if (type == null) {
                throw new IllegalArgumentException("type must not be null.");
            }
            this.pool = pool;
            this.type = type;
        }

        public Object borrowObject() throws Exception, NoSuchElementException, IllegalStateException {
            final Object obj = pool.borrowObject();
            if (type.isInstance(obj)) {
                return obj;
            } else {
                throw new ClassCastException("Borrowed object is not of type: " + type.getName() + " was: " + obj);
            }
        }

        public void returnObject(final Object obj) throws Exception {
            if (type.isInstance(obj)) {
                pool.returnObject(obj);
            } else {
                throw new ClassCastException("Returned object is not of type: " + type.getName() + " was: " + obj);
            }
        }

        public void invalidateObject(final Object obj) throws Exception {
            if (type.isInstance(obj)) {
                pool.invalidateObject(obj);
            } else {
                throw new ClassCastException("Invalidated object is not of type: " + type.getName() + " was: " + obj);
            }
        }

        public void addObject() throws Exception, IllegalStateException, UnsupportedOperationException {
            pool.addObject();
        }

        public int getNumIdle() throws UnsupportedOperationException {
            return pool.getNumIdle();
        }

        public int getNumActive() throws UnsupportedOperationException {
            return pool.getNumActive();
        }

        public void clear() throws Exception, UnsupportedOperationException {
            pool.clear();
        }

        public void close() throws Exception {
            pool.close();
        }

        public void setFactory(final PoolableObjectFactory factory) throws IllegalStateException, UnsupportedOperationException {
            pool.setFactory(factory);
        }

        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("CheckedObjectPool");
            sb.append("{type=").append(type);
            sb.append(", keyedPool=").append(pool);
            sb.append('}');
            return sb.toString();
        }
    }

    private static class CheckedKeyedObjectPool implements KeyedObjectPool {
        private final Class type;
        private final KeyedObjectPool keyedPool;

        CheckedKeyedObjectPool(final KeyedObjectPool keyedPool, final Class type) {
            if (keyedPool == null) {
                throw new IllegalArgumentException("keyedPool must not be null.");
            }
            if (type == null) {
                throw new IllegalArgumentException("type must not be null.");
            }
            this.keyedPool = keyedPool;
            this.type = type;
        }

        public Object borrowObject(final Object key) throws Exception, NoSuchElementException, IllegalStateException {
            Object obj = keyedPool.borrowObject(key);
            if (type.isInstance(obj)) {
                return obj;
            } else {
                throw new ClassCastException("Borrowed object for key: " + key + " is not of type: " + type.getName() + " was: " + obj);
            }
        }

        public void returnObject(final Object key, final Object obj) throws Exception {
            if (type.isInstance(obj)) {
                keyedPool.returnObject(key, obj);
            } else {
                throw new ClassCastException("Returned object for key: " + key + " is not of type: " + type.getName() + " was: " + obj);
            }
        }

        public void invalidateObject(final Object key, final Object obj) throws Exception {
            if (type.isInstance(obj)) {
                keyedPool.invalidateObject(key, obj);
            } else {
                throw new ClassCastException("Invalidated object for key: " + key + " is not of type: " + type.getName() + " was: " + obj);
            }
        }

        public void addObject(final Object key) throws Exception, IllegalStateException, UnsupportedOperationException {
            keyedPool.addObject(key);
        }

        public int getNumIdle(final Object key) throws UnsupportedOperationException {
            return keyedPool.getNumIdle(key);
        }

        public int getNumActive(final Object key) throws UnsupportedOperationException {
            return keyedPool.getNumActive(key);
        }

        public int getNumIdle() throws UnsupportedOperationException {
            return keyedPool.getNumIdle();
        }

        public int getNumActive() throws UnsupportedOperationException {
            return keyedPool.getNumActive();
        }

        public void clear() throws Exception, UnsupportedOperationException {
            keyedPool.clear();
        }

        public void clear(final Object key) throws Exception, UnsupportedOperationException {
            keyedPool.clear(key);
        }

        public void close() throws Exception {
            keyedPool.close();
        }

        public void setFactory(final KeyedPoolableObjectFactory factory) throws IllegalStateException, UnsupportedOperationException {
            keyedPool.setFactory(factory);
        }

        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("CheckedKeyedObjectPool");
            sb.append("{type=").append(type);
            sb.append(", keyedPool=").append(keyedPool);
            sb.append('}');
            return sb.toString();
        }
    }

    private static class ObjectPoolMinIdleTimerTask extends TimerTask {
        private final int minIdle;
        private final ObjectPool pool;

        ObjectPoolMinIdleTimerTask(final ObjectPool pool, final int minIdle) throws IllegalArgumentException {
            if (pool == null) {
                throw new IllegalArgumentException("poll must not be null.");
            }
            this.pool = pool;
            this.minIdle = minIdle;
        }

        public void run() {
            boolean success = false;
            try {
                if (pool.getNumIdle() < minIdle) {
                    pool.addObject();
                }
                success = true;

            } catch (Exception e) {
                cancel();

            } finally {
                // detect other types of Throwable and cancel this Timer
                if (!success) {
                    cancel();
                }
            }
        }

        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("ObjectPoolMinIdleTimerTask");
            sb.append("{minIdle=").append(minIdle);
            sb.append(", keyedPool=").append(pool);
            sb.append('}');
            return sb.toString();
        }
    }

    private static class KeyedObjectPoolMinIdleTimerTask extends TimerTask {
        private final int minIdle;
        private final Object key;
        private final KeyedObjectPool pool;

        KeyedObjectPoolMinIdleTimerTask(final KeyedObjectPool pool, final Object key, final int minIdle) throws IllegalArgumentException {
            if (pool == null) {
                throw new IllegalArgumentException("keyedPool must not be null.");
            }
            this.pool = pool;
            this.key = key;
            this.minIdle = minIdle;
        }

        public void run() {
            boolean success = false;
            try {
                if (pool.getNumIdle(key) < minIdle) {
                    pool.addObject(key);
                }
                success = true;

            } catch (Exception e) {
                cancel();

            } finally {
                // detect other types of Throwable and cancel this Timer
                if (!success) {
                    cancel();
                }
            }
        }

        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("KeyedObjectPoolMinIdleTimerTask");
            sb.append("{minIdle=").append(minIdle);
            sb.append(", key=").append(key);
            sb.append(", keyedPool=").append(pool);
            sb.append('}');
            return sb.toString();
        }
    }

    private static class SynchronizedObjectPool implements ObjectPool {
        private final Object lock;
        private final ObjectPool pool;

        SynchronizedObjectPool(final ObjectPool pool) throws IllegalArgumentException {
            if (pool == null) {
                throw new IllegalArgumentException("keyedPool must not be null.");
            }
            this.pool = pool;
            lock = new Object();
        }

        public Object borrowObject() throws Exception, NoSuchElementException, IllegalStateException {
            synchronized (lock) {
                return pool.borrowObject();
            }
        }

        public void returnObject(final Object obj) throws Exception {
            synchronized (lock) {
                pool.returnObject(obj);
            }
        }

        public void invalidateObject(final Object obj) throws Exception {
            synchronized (lock) {
                pool.invalidateObject(obj);
            }
        }

        public void addObject() throws Exception, IllegalStateException, UnsupportedOperationException {
            synchronized (lock) {
                pool.addObject();
            }
        }

        public int getNumIdle() throws UnsupportedOperationException {
            synchronized (lock) {
                return pool.getNumIdle();
            }
        }

        public int getNumActive() throws UnsupportedOperationException {
            synchronized (lock) {
                return pool.getNumActive();
            }
        }

        public void clear() throws Exception, UnsupportedOperationException {
            synchronized (lock) {
                pool.clear();
            }
        }

        public void close() throws Exception {
            synchronized (lock) {
                pool.close();
            }
        }

        public void setFactory(final PoolableObjectFactory factory) throws IllegalStateException, UnsupportedOperationException {
            synchronized (lock) {
                pool.setFactory(factory);
            }
        }

        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("SynchronizedObjectPool");
            sb.append("{keyedPool=").append(pool);
            sb.append('}');
            return sb.toString();
        }
    }

    private static class SynchronizedKeyedObjectPool implements KeyedObjectPool {
        private final Object lock;
        private final KeyedObjectPool keyedPool;

        SynchronizedKeyedObjectPool(final KeyedObjectPool keyedPool) throws IllegalArgumentException {
            if (keyedPool == null) {
                throw new IllegalArgumentException("keyedPool must not be null.");
            }
            this.keyedPool = keyedPool;
            lock = new Object();
        }

        public Object borrowObject(final Object key) throws Exception, NoSuchElementException, IllegalStateException {
            synchronized (lock) {
                return keyedPool.borrowObject(key);
            }
        }

        public void returnObject(final Object key, final Object obj) throws Exception {
            synchronized (lock) {
                keyedPool.returnObject(key, obj);
            }
        }

        public void invalidateObject(final Object key, final Object obj) throws Exception {
            synchronized (lock) {
                keyedPool.invalidateObject(key, obj);
            }
        }

        public void addObject(final Object key) throws Exception, IllegalStateException, UnsupportedOperationException {
            synchronized (lock) {
                keyedPool.addObject(key);
            }
        }

        public int getNumIdle(final Object key) throws UnsupportedOperationException {
            synchronized (lock) {
                return keyedPool.getNumIdle(key);
            }
        }

        public int getNumActive(final Object key) throws UnsupportedOperationException {
            synchronized (lock) {
                return keyedPool.getNumActive(key);
            }
        }

        public int getNumIdle() throws UnsupportedOperationException {
            synchronized (lock) {
                return keyedPool.getNumIdle();
            }
        }

        public int getNumActive() throws UnsupportedOperationException {
            synchronized (lock) {
                return keyedPool.getNumActive();
            }
        }

        public void clear() throws Exception, UnsupportedOperationException {
            synchronized (lock) {
                keyedPool.clear();
            }
        }

        public void clear(final Object key) throws Exception, UnsupportedOperationException {
            synchronized (lock) {
                keyedPool.clear(key);
            }
        }

        public void close() throws Exception {
            synchronized (lock) {
                keyedPool.close();
            }
        }

        public void setFactory(final KeyedPoolableObjectFactory factory) throws IllegalStateException, UnsupportedOperationException {
            synchronized (lock) {
                keyedPool.setFactory(factory);
            }
        }

        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("SynchronizedKeyedObjectPool");
            sb.append("{keyedPool=").append(keyedPool);
            sb.append('}');
            return sb.toString();
        }
    }

    private static class SynchronizedPoolableObjectFactory implements PoolableObjectFactory {
        private final Object lock;
        private final PoolableObjectFactory factory;

        SynchronizedPoolableObjectFactory(final PoolableObjectFactory factory) throws IllegalArgumentException {
            if (factory == null) {
                throw new IllegalArgumentException("factory must not be null.");
            }
            this.factory = factory;
            lock = new Object();
        }

        public Object makeObject() throws Exception {
            synchronized (lock) {
                return factory.makeObject();
            }
        }

        public void destroyObject(final Object obj) throws Exception {
            synchronized (lock) {
                factory.destroyObject(obj);
            }
        }

        public boolean validateObject(final Object obj) {
            synchronized (lock) {
                return factory.validateObject(obj);
            }
        }

        public void activateObject(final Object obj) throws Exception {
            synchronized (lock) {
                factory.activateObject(obj);
            }
        }

        public void passivateObject(final Object obj) throws Exception {
            synchronized (lock) {
                factory.passivateObject(obj);
            }
        }

        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("SynchronizedPoolableObjectFactory");
            sb.append("{factory=").append(factory);
            sb.append('}');
            return sb.toString();
        }
    }

    private static class SynchronizedKeyedPoolableObjectFactory implements KeyedPoolableObjectFactory {
        private final Object lock;
        private final KeyedPoolableObjectFactory keyedFactory;

        SynchronizedKeyedPoolableObjectFactory(final KeyedPoolableObjectFactory keyedFactory) throws IllegalArgumentException {
            if (keyedFactory == null) {
                throw new IllegalArgumentException("keyedFactory must not be null.");
            }
            this.keyedFactory = keyedFactory;
            lock = new Object();
        }

        public Object makeObject(final Object key) throws Exception {
            synchronized (lock) {
                return keyedFactory.makeObject(key);
            }
        }

        public void destroyObject(final Object key, final Object obj) throws Exception {
            synchronized (lock) {
                keyedFactory.destroyObject(key, obj);
            }
        }

        public boolean validateObject(final Object key, final Object obj) {
            synchronized (lock) {
                return keyedFactory.validateObject(key, obj);
            }
        }

        public void activateObject(final Object key, final Object obj) throws Exception {
            synchronized (lock) {
                keyedFactory.activateObject(key, obj);
            }
        }

        public void passivateObject(final Object key, final Object obj) throws Exception {
            synchronized (lock) {
                keyedFactory.passivateObject(key, obj);
            }
        }

        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("SynchronizedKeyedPoolableObjectFactory");
            sb.append("{keyedFactory=").append(keyedFactory);
            sb.append('}');
            return sb.toString();
        }
    }
}