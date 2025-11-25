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
    private List<String> permissionWhiteUrlLists;
    /**
     * 认证白名单和鉴权白名单url分隔符
     */
    private String paSplit = "------";

    private List<String> authenticationWhiteUrlLists;
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
     * 设置认证白名单和鉴权白名单url分隔符
     * @param paSplit
     * @return
     */
    public BaseWhiteUrlResourcesCache setPaSplit(String paSplit) {
        this.paSplit = paSplit;
        return this;
    }

    /**
     * 初始化
     */
    @Override
    public void init() {
        
    }

    /**
     * 加载资源
     * @param resourceCacheEntity
     */
    @Override
    public void loadResources(ResourceCacheEntity<String> resourceCacheEntity) {
        String urls = resourceCacheEntity.getResource();
        
        if(urls == null || urls.trim().length() == 0){
            synchronized (lock) {
                this.permissionWhiteUrlLists = new ArrayList<>();
                this.authenticationWhiteUrlLists = new ArrayList<>();
            }
            return;
        }
        int paSplitIdex = urls.indexOf(paSplit);
       
        //如果没有区分免认证和免鉴权的url，则默认所有url都为免认证和免鉴权
        if(paSplitIdex > 0){
            String authenticationUrls = null;
            String permissionUrls = null;

            String[] tmp = urls.split(paSplit);
            authenticationUrls = tmp[0];
            permissionUrls = tmp[1];
            List<String> authenticationWhiteUrlLists = new ArrayList<>();
            buildWhiteUrlLists(authenticationUrls,authenticationWhiteUrlLists);
            List<String> permissionWhiteUrlLists = new ArrayList<>();
            if(authenticationWhiteUrlLists != null && authenticationWhiteUrlLists.size() > 0){
                permissionWhiteUrlLists.addAll(authenticationWhiteUrlLists);
            }

            buildWhiteUrlLists(permissionUrls,permissionWhiteUrlLists);
            synchronized (lock) {
                this.permissionWhiteUrlLists = permissionWhiteUrlLists;
                this.authenticationWhiteUrlLists = authenticationWhiteUrlLists;
            }
        }
        else{
            List<String> authenticationWhiteUrlLists = new ArrayList<>();
            buildWhiteUrlLists(urls,authenticationWhiteUrlLists);
            synchronized (lock) {
                this.permissionWhiteUrlLists = authenticationWhiteUrlLists;
                this.authenticationWhiteUrlLists = authenticationWhiteUrlLists;
            }
        }
       
    }
    
 

    public static void buildWhiteUrlLists(String urls,List<String> whiteUrlLists) {
        String[] urlsArray = urls.split(",");
        for (String url : urlsArray) {
            url = url.trim();
            String[] urlArray = url.split("\n");
            for(String tmp:urlArray) {
                tmp = tmp.trim();
                if (!whiteUrlLists.contains(tmp))
                    whiteUrlLists.add(tmp);
            }
        }
    }
    public boolean isPermissionWhiteUrl(String url){
        List<String> whiteUrlLists = null;
        synchronized ( lock) {
            whiteUrlLists = this.permissionWhiteUrlLists;
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

    public boolean isAuthenticationWhiteUrls(String url){
        List<String> whiteUrlLists = null;
        synchronized ( lock) {
            whiteUrlLists = this.authenticationWhiteUrlLists;
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
        permissionWhiteUrlLists = null;
        authenticationWhiteUrlLists = null;
    }

 

 

}
