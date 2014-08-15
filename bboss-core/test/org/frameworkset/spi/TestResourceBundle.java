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

package org.frameworkset.spi;

import java.io.IOException;
import java.util.Locale;

import org.frameworkset.util.io.Resource;
import org.junit.Test;

/**
 * <p>Title: TestResourceBundle.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-10-19 10:32:18
 * @author biaoping.yin
 * @version 1.0
 */
public class TestResourceBundle {
	@Test
	public void test()
	{
		ApplicationContext context = ApplicationContext.getApplicationContext();
		System.out.println(context.getMessage("probe.jsp.generic.no", (Object[])null, (Locale)null));
		System.out.println(context.getMessage("probe.jsp.generic.no"));
	}
	
	@Test
	public void testspecial()
	{
		ApplicationContext context = ApplicationContext.getApplicationContext("org/frameworkset/spi/support/messages.xml");
		System.out.println(context.getMessage("Trans.Log.TransformationIsToAllocateStep", new Object[]{"a","b"}, (Locale)null));
		System.out.println(context.getMessage("Trans.Log.TransformationIsToAllocateStep", new Object[]{"a","b"}, Locale.SIMPLIFIED_CHINESE));
		System.out.println(context.getMessage("StepLoader.RuntimeError.UnableToInstantiateClass.TRANS0006",Locale.US));
		Resource resource = context.getResource("org/frameworkset/spi/support/messages");
		System.out.println(resource);
	}
	
	@Test
	public void testspecialsss()
	{
		DefaultApplicationContext context = DefaultApplicationContext.getApplicationContext("org/frameworkset/spi/support/messages.xml");
		System.out.println(context.getMessage("Trans.Log.TransformationIsToAllocateStep", new Object[]{"a","b"}, (Locale)null));
		System.out.println(context.getMessage("Trans.Log.TransformationIsToAllocateStep", new Object[]{"a","b"}, Locale.SIMPLIFIED_CHINESE));
		System.out.println(context.getMessage("StepLoader.RuntimeError.UnableToInstantiateClass.TRANS0006",Locale.US));
		Resource resource = context.getResource("org/frameworkset/spi/support/messages");
		System.out.println(resource);
	}
	
	@Test
	public void testResource()
	{
		ApplicationContext context = ApplicationContext.getApplicationContext();
		System.out.println(context.getResource("messages"));
	}
	
	@Test
	public void testPatternResources() throws IOException
	{
		ApplicationContext context = ApplicationContext.getApplicationContext();
		System.out.println(context.getResources("messages*"));
	}
	
	@Test
	public void testResources() throws IOException
	{
		ApplicationContext context = ApplicationContext.getApplicationContext();
		System.out.println(context.getResources("messages"));
	}

}
