package org.frameworkset.spi.assemble;
/**
 * Copyright 2020 bboss
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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Description: 获取默认属性配置容器</p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 * @Date 2020/8/1 21:15
 * @author biaoping.yin
 * @version 1.0
 */
public class PropertiesUtil {
	private static Map<String,PropertiesContainer> propertiesContainerMap = new LinkedHashMap<>();
	public static PropertiesContainer getPropertiesContainer(){

		return getPropertiesContainer("application.properties");
	}

	public static PropertiesContainer getPropertiesContainer(String propertiesFile){
		PropertiesContainer propertiesContainer = propertiesContainerMap.get(propertiesFile);
		if(propertiesContainer != null){
			return propertiesContainer;
		}
		synchronized (PropertiesUtil.class) {
			propertiesContainer = propertiesContainerMap.get(propertiesFile);
			if(propertiesContainer != null){
				return propertiesContainer;
			}

			propertiesContainer = new PropertiesContainer();
			propertiesContainer.addConfigPropertiesFile(propertiesFile);
			propertiesContainerMap.put(propertiesFile,propertiesContainer);
		}
		return propertiesContainer;
	}

}
