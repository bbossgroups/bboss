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
public interface ResourcesCache<T> {
    /**
     * 初始化
     */
    void init();
    /**
     * 加载资源
     */
    void loadResources(ResourceCacheEntity<T> resourceCacheEntity);
    
    /**
     * 销毁
     */
    void destroy();
    
    /**
     * 获取缓存名称
     * @return
     */
    String getName();
    
    /**
     * 扫描间隔时间
     * @return
     */
    long getScanInterval();

    ResourceCacheEntity<T> getResourceCacheEntity(long oldLastModifiedTime);
    

}
