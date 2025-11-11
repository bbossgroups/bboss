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

import static java.lang.Thread.sleep;

/**
 * @author biaoping.yin
 * @Date 2025/11/11
 */
public class TestCache {
    private static Logger logger = org.slf4j.LoggerFactory.getLogger(TestCache.class);
    public static void main(String[] args) {
        ResourcesCacheTest resourcesCache = new ResourcesCacheTest();
        ResourcesCacheWrapper resourcesCacheWrapper = new ResourcesCacheWrapper(resourcesCache);
        resourcesCacheWrapper.init();

        int max = 5;
        int i = 0;
        do {
            
            logger.info("/metrics/indext.html is white url:" + resourcesCache.isWhiteUrl("/metrics/indext.html"));
            logger.info("cancelOrder.api is white url:" + resourcesCache.isWhiteUrl("cancelOrder.api"));
            logger.info("cancelOrder is white url:" + resourcesCache.isWhiteUrl("cancelOrder"));
            i ++;
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        while (i < max);
        resourcesCacheWrapper.destroy();
    }

}
