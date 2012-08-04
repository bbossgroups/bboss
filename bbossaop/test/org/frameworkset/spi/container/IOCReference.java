/*
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

package org.frameworkset.spi.container;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.frameworkset.spi.remote.restful.RestfulServiceConvertor;

/**
 * <p>Title: IOCReference.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2012-8-4 下午2:17:18
 * @author biaoping.yin
 * @version 1.0
 */
public class IOCReference {

	public void test()
	{
		//初始化ioc容器对象，在该容器对象中定义了三个外部ioc容器组件的引用组件
		BaseApplicationContext context = DefaultApplicationContext
						.getApplicationContext("org/frameworkset/spi/container/iocreference.xml");
		//跨ioc组件实例获取方法示例
		RestfulServiceConvertor convertor = context.getTBeanObject("test_refbean_from_outnerapplicationcontext",RestfulServiceConvertor.class);
		RestfulServiceConvertor convertor1 = context.getTBeanObject("test_refbean_from_mvcapplicationcontext",RestfulServiceConvertor.class);
		RestfulServiceConvertor convertor2 = context.getTBeanObject("test_refbean_from_outnerdefaultapplicationcontext",RestfulServiceConvertor.class);
	}
}
