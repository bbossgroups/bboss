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

package org.frameworkset.spi.properties.abc;

import org.frameworkset.spi.ApplicationContext;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>Title: TestCase.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2011-5-6 下午10:55:27
 * @author biaoping.yin
 * @version 1.0
 */
public class TestCase {
	
	BaseApplicationContext context ;
	BaseApplicationContext context1 ;
	@Before
	public void init()
	{
		context = ApplicationContext.getApplicationContext("org/frameworkset/spi/properties/abc/mutilreference.xml");
		context1 = DefaultApplicationContext.getApplicationContext("org/frameworkset/spi/properties/abc/test.xml");
	}
	@Test
	public void test()
	{
		A a = (A)context.getBeanObject("a");
		System.out.println();
		B b = (B)context.getBeanObject("b");
	}
	@Test 
	public void test1()
	{
		context1.getBeanObject("test");
	}

}
