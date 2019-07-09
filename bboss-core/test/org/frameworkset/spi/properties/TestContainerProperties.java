package org.frameworkset.spi.properties;
/**
 * Copyright 2008 biaoping.yin
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

import org.frameworkset.spi.assemble.PropertiesContainer;
import org.junit.Test;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/7/8 12:49
 * @author biaoping.yin
 * @version 1.0
 */
public class TestContainerProperties {
	@Test
	public void test(){
		PropertiesContainer propertiesContainer = new PropertiesContainer();
		propertiesContainer.addConfigPropertiesFile("org/frameworkset/spi/properties/application.properties");
		String elasticUser = propertiesContainer.getProperty("elasticUser");
		String elasticPassword = propertiesContainer.getProperty("elasticPassword");
		System.out.println();
	}

	@Test
	public void testSysEnv(){
		PropertiesContainer propertiesContainer = new PropertiesContainer();
		propertiesContainer.addConfigPropertiesFile("org/frameworkset/spi/properties/applicationenv.properties");
		String elasticUser = propertiesContainer.getProperty("elasticEnvUser");
		String elasticPassword = propertiesContainer.getProperty("elasticSysProPassword");
		System.out.println("elasticUser:"+elasticUser);
		System.out.println("elasticPassword:"+elasticPassword);
	}
}
