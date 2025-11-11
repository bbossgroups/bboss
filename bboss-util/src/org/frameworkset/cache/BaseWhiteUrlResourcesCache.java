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

import org.frameworkset.util.AntPathMatcher;
import org.frameworkset.util.PathMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用资源缓存接口
 * @author biaoping.yin
 * @Date 2025/11/11
 */
public abstract class BaseWhiteUrlResourcesCache extends BaseResourcesCache<String>{
    private List<String> whiteUrlLists;

    private PathMatcher pathMatcher = new AntPathMatcher();
    private Object lock = new Object();

    public BaseWhiteUrlResourcesCache( ) {
        this( 5000L);
    }

    public BaseWhiteUrlResourcesCache(long scanInterval ) {
        super("WhiteUrlListsCache", scanInterval);
    }

    public BaseWhiteUrlResourcesCache(String name,long scanInterval ) {
        super(name, scanInterval);
    }

    /**
     * 初始化
     */
    @Override
    public void init() {
        
    }

    /**
     * 加载资源
     */
    @Override
    public void loadResources(ResourceCacheEntity<String> resourceCacheEntity) {
        String urls = resourceCacheEntity.getResource();
        
        if(urls == null || urls.trim().length() == 0){
            synchronized (lock) {
                this.whiteUrlLists = new ArrayList<>();
            }
            return;
        }
        String[] urlsArray = urls.split(",");
        List whiteUrlLists = new ArrayList();
        for(String url:urlsArray){
            whiteUrlLists.add(url.trim());
        }
        synchronized (lock) {
            this.whiteUrlLists = whiteUrlLists;
        }
    }
    public boolean isWhiteUrl(String url){
        List<String> whiteUrlLists = null;
        synchronized ( lock) {
            whiteUrlLists = this.whiteUrlLists;
        }
        if(whiteUrlLists == null)
            return false;
        for(String whiteUrl:whiteUrlLists){
            if(pathMatcher.match(whiteUrl,url)){
                return true;
            }
        }
        return false;
    }

    /**
     * 销毁
     */
    @Override
    public void destroy() {
        whiteUrlLists = null;
    }

 

 

}
