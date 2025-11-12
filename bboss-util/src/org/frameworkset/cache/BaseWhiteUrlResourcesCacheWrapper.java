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
public class BaseWhiteUrlResourcesCacheWrapper extends ResourcesCacheWrapper<String>{
    private static Logger logger = org.slf4j.LoggerFactory.getLogger(BaseWhiteUrlResourcesCacheWrapper.class);
    private BaseWhiteUrlResourcesCache baseWhiteUrlResourcesCache;

    public BaseWhiteUrlResourcesCacheWrapper(BaseWhiteUrlResourcesCache resourcesCache){
        super(resourcesCache);
        this.baseWhiteUrlResourcesCache = resourcesCache;
    }
    public boolean isWhiteUrl(String url){
        return baseWhiteUrlResourcesCache.isWhiteUrl(url);
    }
 
}
