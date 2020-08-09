package org.frameworkset.spi.assemble.plugin;/*
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

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.assemble.GetProperties;
import org.frameworkset.spi.assemble.PropertiesContainer;

import java.util.Map;

public interface PropertiesFilePlugin {
	/**
	 * 返回多个属性配置文件，对应的路径格式为（多个用逗号分隔）：
	 * conf/elasticsearch.properties,application.properties,config/application.properties
	 * 上述的文件都是在classpath下面即可，如果需要指定绝对路径，格式为：
	 * file:d:/aaa.properties,file:d:/elasticsearch.properties,config/application.properties
	 *
	 * 说明：带file:前缀表示后面的路径为绝对路径
	 *
	 * @return
	 * @param applicationContext
	 */
	public String getFiles(BaseApplicationContext applicationContext,Map<String,String> extendsAttributes, PropertiesContainer propertiesContainer);

	/**
	 * 直接获取配置属性集合
	 * @param applicationContext
	 * @return
	 */
	public Map getConfigProperties(BaseApplicationContext applicationContext,Map<String,String> extendsAttributes, PropertiesContainer propertiesContainer);
	/**
	 * 0: 外部自定义配置文件
	 * 1：外部自定义属性
	 * -1:采用默认配置文件
	 */
	public int getInitType(BaseApplicationContext applicationContext,Map<String,String> extendsAttributes, PropertiesContainer propertiesContainer);

	public void restore(BaseApplicationContext applicationContext,Map<String,String> extendsAttributes, PropertiesContainer propertiesContainer);
	public static final int INIT_TYPE_OUTCONFIGFILE = 0;
	public static final int INIT_TYPE_OUTMAP = 1;
	public static final int INIT_TYPE_DEFAULT = -1;


	void afterLoaded(GetProperties applicationContext, PropertiesContainer propertiesContainer);
}
