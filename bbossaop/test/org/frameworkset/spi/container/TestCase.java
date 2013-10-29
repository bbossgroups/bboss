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

import org.frameworkset.spi.ApplicationContext;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>Title: TestCase.java</p> 
 * <p>Description: 数组注入方式</p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2011-4-11 下午01:47:29
 * @author biaoping.yin
 * @version 1.0
 */
public class TestCase {
	private ApplicationContext context ;
	@Before
	public void init()
	{
		context = ApplicationContext.getApplicationContext("org/frameworkset/spi/container/arraybean-test.xml");
	}
	@Test
	public void test()
	{
		ArrayBeanTest test = (ArrayBeanTest)context.getBeanObject("ArrayBeanTest");
		System.out.println(test);
	}
	
	

}
