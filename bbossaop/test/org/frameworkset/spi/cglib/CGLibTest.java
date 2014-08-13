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

package org.frameworkset.spi.cglib;

import java.util.List;

import org.frameworkset.spi.ApplicationContext;
import org.frameworkset.spi.ClientProxyContext;
import org.frameworkset.util.ClassUtil;
import org.frameworkset.util.ClassUtil.ClassInfo;
import org.junit.Test;

/**
 * <p>Title: CGLibTest.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-6-21 上午10:30:57
 * @author biaoping.yin
 * @version 1.0
 */
public class CGLibTest {
	static ApplicationContext context_provider = ApplicationContext.getApplicationContext("org/frameworkset/spi/cglib/service-bean-assemble.xml");
	@Test
	public void test()
	{
		//远程调用
		CGLibService service = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/cglib/service-bean-assemble.xml","(rmi::172.16.17.216:1099)/cglibbean",CGLibService.class);
		System.out.println(service.sayhello("多多"));
	}
	
	@Test
	public void testCGlib()
	{
		//远程调用
		CGLibService service = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/cglib/service-bean-assemble.xml","(rmi::172.16.17.216:1099)/cglibbean",CGLibService.class);
		ClassInfo into = ClassUtil.getClassInfo(service.getClass());
		List<Class> classes = into.getSuperClasses();
		System.out.println(service instanceof CGLibService);
		System.out.println(service.getClass().getName());
	}
	
	
	
	
	@Test
	public void localtest()
	{
		//本地调用
		CGLibService service = (CGLibService)context_provider.getBeanObject("cglibbean");
		System.out.println(service.sayhello("多多"));
	}
	
	

}
