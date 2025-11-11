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
public class ResourcesCacheTest extends BaseWhiteUrlResourcesCache{
    
    public ResourcesCacheTest( ) {
        super("WhiteUrlListsCache", 5000L);
    }


    public ResourceCacheEntity<String> getResourceCacheEntity(long oldLastModifiedTime){
        ResourceCacheEntity<String> resourceCacheEntity = new ResourceCacheEntity<String>();
        resourceCacheEntity.setResource(getWhiteUrls());
        resourceCacheEntity.setLastModifiedTime(getLastModifiedTime());
        return resourceCacheEntity;
    }
    public String getWhiteUrls() {
        String urls = "/**/*.html,\n" +
                "\t\t\t\tgetChannels.api,\n" +
                "\t\t\t\t/province.json,\n" +
                "\t\t\t\t/findOrderDetailData.api,\n" +
                "\t\t\t\tsaveOutOrderHandle.api,\n" +
                "\t\t\t\tfindOutSpeechList.api,\n" +
                "\t\t\t\tfindOutboundOrderRecordlList.api,\n" +
                "\t\t\t\tqueryCustomAnswer.api,\n" +
                "\t\t\t\tfindQuestionListByCallType.api,\n" +
                "\t\t\t\tsaveCustomAnswer.api,\n" +
                "\t\t\t\tcancelOrder.api";
        return urls;
    }


    public long getLastModifiedTime() {
        return System.currentTimeMillis();
    }
}
