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
package com.frameworkset.commons.pool2;

/**
 * This interface may be implemented by an object pool to enable clients
 * (primarily those clients that wrap pools to provide pools with extended
 * features) to provide additional information to the pool relating to object
 * using allowing more informed decisions and reporting to be made regarding
 * abandoned objects.
 *
 * @param <T>   The type of object provided by the pool.
 *
 * @since 2.0
 */
public interface UsageTracking<T> {

    /**
     * This method is called every time a pooled object to enable the pool to
     * better track borrowed objects.
     *
     * @param pooledObject  The object that is being used
     */
    void use(T pooledObject);
}