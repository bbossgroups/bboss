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

package org.frameworkset.spi.properties.loopinject;

import org.frameworkset.spi.ApplicationContext;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.junit.Before;
import org.junit.Test;


/**
 * <p>Title: TestloopInject.java</p> 
 * <p>Description: bbossgroups 3.5以前的版本不支持属性循环依赖注入，
 * 3.5以后的版本提供了很好的支持,并且支持局部循环引用
 * 局部循环引用的名称格式定义为：
 * name1->name12->name121
 * 
 * 对于构造函数的依赖注入处理
 * 对于工厂依赖注入处理
 * 对于从引用开始的依赖注入处理（这个必须支持循环引用，具体需要进一步改进）
 * </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-1-17 下午08:41:46
 * @author biaoping.yin
 * @version 1.0
 */
public class TestloopInject
{
	BaseApplicationContext context ;
	@Before
	public void init()
	{
		context = DefaultApplicationContext.getApplicationContext("org/frameworkset/spi/properties/loopinject/loopinject.xml");
	}
	@Test
	public void injectloop()
	{
		A a = (A)context.getBeanObject("loop.a");
		
		System.out.println(a);
	}
	
	@Test
	public void selfinjectloop()
	{
		BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("org/frameworkset/spi/properties/loopinject/test.xml"); 
		Test1 a = (Test1)context.getBeanObject("test1");
		System.out.println(a);
	}
	
	@Test
	public void selfcontaininjectloop()
	{
		BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("org/frameworkset/spi/properties/loopinject/testcontainref.xml"); 
		Test1 a = (Test1)context.getBeanObject("test1");
		System.out.println(a);
	}
	@Test
	public void refinjectloop()
	{
		BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("org/frameworkset/spi/properties/loopinject/test.xml"); 
		Test1 a = (Test1)context.getBeanObject("test1");
		Test2 test6 = (Test2)context.getBeanObject("test6");
		Test2 test7 = (Test2)context.getBeanObject("test7");
		Test2 test8 = (Test2)context.getBeanObject("test8");
		
		System.out.println(a);
	}
}
