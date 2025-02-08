package org.frameworkset.spi.assemble;/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import java.util.Map;

public interface GetProperties {
	void reset();
	String getExternalProperty(String property);
	String getSystemEnvProperty(String property);
	String getExternalProperty(String property,String defaultValue);

    /**
     * 根据属性名称前缀获取属性集
     * @param namespace
     * @param propertyPrex 属性名称前缀
     * @param truncated 返回的key是否截取掉前缀
     * @return
     */
    default Map<String,Object> getExternalProperties(String namespace,String propertyPrex,boolean truncated){
        return null;
    }
	Object getExternalObjectProperty(String property);
	Object getExternalObjectProperty(String property,Object defaultValue);
	boolean getExternalBooleanProperty(String property,boolean defaultValue);
//	String getProperty(String property);
	String getExternalPropertyWithNS(String namespace,String property);
	String getExternalPropertyWithNS(String namespace,String property,String defaultValue);
	Object getExternalObjectPropertyWithNS(String namespace,String property);
	Object getExternalObjectPropertyWithNS(String namespace,String property,Object defaultValue);
//	boolean getBooleanProperty(String property,boolean defaultValue);
//	Map getAllProperties();
    Map getAllExternalProperties();
}
