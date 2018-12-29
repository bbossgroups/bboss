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
import java.util.concurrent.ConcurrentHashMap;

public final class MissingStaticCache<K,V> {

    private final int size;
    public final int DEFAULT_SIZE = 1000;

    private final Map<K,V> eden;
    private int misses;
    private int missesMax;
    private boolean stopCache;


    public MissingStaticCache() {
        this.size = DEFAULT_SIZE;
        missesMax = size * 2;
        this.eden = new ConcurrentHashMap<K,V>(size);
    }
    public MissingStaticCache(int size) {
        this.size = size;
        this.missesMax = size * 2;
        this.eden = new ConcurrentHashMap<K,V>(size);
    }

    /**
	 * 用于判断缓存命中失败次数是否超过最大允许次数
     * @return
	 */
    private void shouldCache(){
        if( misses >= missesMax) {
            stopCache = true;
            this.clear();
        }
    }

    /**
     * 判断是否需要停止缓存数据
     * @return
     */
    public boolean stopCache(){
        return this.stopCache;
    }

    public void clear(){
        this.eden.clear();
    }

    public int increamentMissing(){
        misses++;
        shouldCache();
        return misses;
    }

    public V get(K k) {
        V v = this.eden.get(k);
        return v;
    }

    public void put(K k, V v) {
        this.eden.put(k, v);
    }


    public int edenSize(){
        return this.eden.size();
    }
}
