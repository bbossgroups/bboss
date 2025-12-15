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

import org.slf4j.Logger;

/**
 * @author biaoping.yin
 * @Date 2025/11/11
 */
public class ResourcesCacheWrapper<T> implements ResourcesCache<T> ,Runnable{
    private static Logger logger = org.slf4j.LoggerFactory.getLogger(ResourcesCacheWrapper.class);
    private ResourcesCache resourcesCache;
    private boolean stopped;
    private Thread refreshThread;
    private volatile long lastModifiedTime = -1L;
    public ResourcesCacheWrapper(ResourcesCache resourcesCache){
        this.resourcesCache = resourcesCache;
    }


    @Override
    public void init() {
        resourcesCache.init();
        refreshThread = new Thread(this);
        refreshThread.setDaemon(true);
        refreshThread.setName("ResourcesCacheWrapper refreshThread[" + this.getName()+"]");
        //初始化一次性处理
        ResourceCacheEntity<T> resourceCacheEntity = resourcesCache.getResourceCacheEntity(-1L);
        if(resourceCacheEntity != null) {
            this.lastModifiedTime = resourceCacheEntity.getLastModifiedTime();
            resourcesCache.loadResources(  resourceCacheEntity);
        }
        
        
        refreshThread.start();
    }

    @Override
    public void loadResources(ResourceCacheEntity<T> resourceCacheEntity) {
        resourcesCache.loadResources( resourceCacheEntity);
    }

    /**
     * 销毁
     */
    @Override
    public void destroy() {
        if(stopped)
            return;
        
        synchronized (refreshThread) {
            if(stopped)
                return;
            stopped = true;
            refreshThread.notifyAll();
        }
        try {
            refreshThread.join();
        } catch (InterruptedException e) {
            logger.warn("Interrupted while waiting for refresh thread to finish", e);
        }
        resourcesCache.destroy();
        logger.info("ResourcesCacheWrapper[{}] destroyed successfully", this.getName());
    }

    /**
     * 获取缓存名称
     *
     * @return
     */
    @Override
    public String getName() {
        return resourcesCache.getName();
    }

    /**
     * 扫描间隔时间
     *
     * @return
     */
    @Override
    public long getScanInterval() {
        return resourcesCache.getScanInterval();
    }
 

    @Override
    public ResourceCacheEntity<T> getResourceCacheEntity(long oldLastModifiedTime){
        return resourcesCache.getResourceCacheEntity( oldLastModifiedTime);
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        while (!stopped){ 
            if(stopped){
                break;
            }
            synchronized (refreshThread) {
                try {
                    refreshThread.wait(getScanInterval());
                    if(stopped){
                        break;
                    }
                    long start = System.currentTimeMillis();
                    if(logger.isDebugEnabled()) {
                        logger.debug("ResourcesCacheWrapper[{}] refresh resources start:", this.getName());
                    }
                    ResourceCacheEntity<T> resourceCacheEntity = resourcesCache.getResourceCacheEntity(lastModifiedTime);
                    long lastModifiedTime = resourceCacheEntity.getLastModifiedTime();
                    if(lastModifiedTime > this.lastModifiedTime){
                        
                        try {
                            if(logger.isInfoEnabled()) {
                                logger.info("ResourcesCache[{}] loadResources(resourceCacheEntity) begin.", this.getName());
                            }
                            resourcesCache.loadResources(resourceCacheEntity);
                            if(logger.isInfoEnabled()) {
                                logger.info("ResourcesCache[{}] loadResources(resourceCacheEntity) complete.", this.getName());
                            }
                        }
                        catch (Exception e){
                            logger.warn("load resources error:",e);
                        }
                        
                    }
                    this.lastModifiedTime = lastModifiedTime;
                    if(logger.isDebugEnabled()) {
                        logger.debug("ResourcesCacheWrapper[{}] refresh resources complete:{}ms", this.getName(), System.currentTimeMillis() - start);
                    }
                    
                } catch (InterruptedException e) {
                   break;
                }
            }            
        }
        logger.info("ResourcesCacheWrapper refresh thread stopped for cache: {}", this.getName());
    }
}
