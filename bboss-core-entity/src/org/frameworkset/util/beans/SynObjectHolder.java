package org.frameworkset.util.beans;
/**
 * Copyright 2023 bboss
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * <p>Description: 用来保持特定类型的对象，方便在不同的对象内部共享和修改对象</p>
 * <p></p>
 * <p>Copyright (c) 2026</p>
 * @Date 2026/3/22
 * @author biaoping.yin
 * @version 1.0
 */
public class SynObjectHolder<T> {
    private T object;

    public synchronized T getObject() {
        return object;
    }

    public synchronized void setObject(T object) {
        this.object = object;
    }

    public synchronized void setObject(T object,Condition<T> condition) {
        if(condition.isTrue(this.object,object)) {
            this.object = object;
        }
    }
}
