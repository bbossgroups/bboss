package org.frameworkset.spi;
/**
 * Copyright 2022 bboss
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
 * <p>Description: 用于保持当前应用进程根ioc容器对象</p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 * @Date 2022/5/20
 * @author biaoping.yin
 * @version 1.0
 */
public class IOCContainerHolder {
	private static BaseApplicationContext applicationContext;

	/**
	 * 初始化容器对象
	 * @param applicationContext
	 */
	public static void setApplicationContext(BaseApplicationContext applicationContext){
		IOCContainerHolder.applicationContext = applicationContext;
	}

	public static BaseApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 从容器中获取对象
	 * @param beanName
	 * @param type
	 * @param <T>
	 * @return
	 */
	public static  <T> T getTBeanObject(String beanName,Class<T> type) {
		return applicationContext.getTBeanObject(beanName,type);
	}

}
