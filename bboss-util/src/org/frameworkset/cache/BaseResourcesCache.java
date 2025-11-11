package org.frameworkset.cache;
/**
 * Copyright 2025 bboss
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
 * 通用资源缓存接口
 * @author biaoping.yin
 * @Date 2025/11/11
 */
public abstract class BaseResourcesCache<T> implements ResourcesCache<T>{
    protected String name;
    /**
     * 资源扫描间隔时间
     */
    protected long scanInterval;
    
    public BaseResourcesCache(String name,long scanInterval){
        this.name = name;
        this.scanInterval = scanInterval;
    }

    @Override
    public long getScanInterval() {
        return scanInterval;
    }

    @Override
    public String getName() {
        return name;
    }
}
