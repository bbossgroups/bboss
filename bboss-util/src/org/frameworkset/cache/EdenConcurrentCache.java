/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.frameworkset.cache;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class EdenConcurrentCache<K,V> {

    private final int size;
    public final int DEFAULT_SIZE = 1000;

    private final Map<K,V> eden;


    private Lock cacheLock = new ReentrantLock();

    private final Map<K,V> longterm;
    public EdenConcurrentCache() {
        this.size = DEFAULT_SIZE;
        this.eden = new ConcurrentHashMap<K,V>(size);
        this.longterm = new WeakHashMap<K,V>(size);
    }
    public EdenConcurrentCache(int size) {
        this.size = size;
        this.eden = new ConcurrentHashMap<K,V>(size);
        this.longterm = new WeakHashMap<K,V>(size);
    }



    public V get(K k) {
        V v = this.eden.get(k);
        if (v == null) {
            try{
                cacheLock.lock();
                v = this.longterm.get(k);
            }
            finally {
                cacheLock.unlock();
            }
            if (v != null) {
                this.eden.put(k, v);
            }

        }
        return v;
    }

    public int getMaxSize(){
        return this.size;
    }

    /**
     * put data with key and value
     * if read max data size then return true ,either return false.
     * @param k
     * @param v
     * @return
     */
    public boolean put(K k, V v) {
        boolean outOfSize = false;
        if (this.eden.size() >= size) {
            try{
                cacheLock.lock();
                this.longterm.putAll(this.eden);
            }
            finally {
                cacheLock.unlock();
            }
            outOfSize = true;
            this.eden.clear();
        }
        this.eden.put(k, v);
        return outOfSize;
    }
    public int longtermSize(){
        return this.longterm.size();
    }

    public int edenSize(){
        return this.eden.size();
    }

    public void clear(){
        this.eden.clear();
        try {
            cacheLock.lock();
            this.longterm.clear();
        }
        finally {
            cacheLock.unlock();
        }
    }
}
