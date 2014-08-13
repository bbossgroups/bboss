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

package org.frameworkset.spi.beans.factory;

import org.frameworkset.spi.ApplicationContext;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>Title: TestCase.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2011-1-14 11:16:36
 * @author biaoping.yin
 * @version 1.0
 */
public class TestCase {
	ApplicationContext context ;
	@Before
	public void initContext()
	{
		context = ApplicationContext.getApplicationContext("org/frameworkset/spi/beans/factory/factorybean.xml");
		
	}
	@Test
	public void runCase()
	{
		TestBean bean =  (TestBean) context.getBeanObject("testbeanCreateNoArgs");
		TestBean bean1 =  (TestBean) context.getBeanObject("testbeanCreateByArgs");
		TestBean bean2 =  (TestBean) context.getBeanObject("staticTestbeanCreateByArgs");
		TestBean bean3 =  (TestBean) context.getBeanObject("staticTestbeanCreateNoArgs");
	}
	
	@Test
	public void runExceptionCase()
	{
		try {
			TestBean bean = (TestBean) context
					.getBeanObject("testbeanCreateNoArgsThrowException");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			TestBean bean1 = (TestBean) context
					.getBeanObject("testbeanCreateByArgsThrowException");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			TestBean bean2 = (TestBean) context
					.getBeanObject("staticTestbeanCreateByArgsThrowException");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			TestBean bean3 = (TestBean) context
					.getBeanObject("staticTestbeanCreateNoArgsThrowException");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
	}
	@Test
	public void test()
	{
		TestBean bean3 = context.getTBeanObject("testbeanCreateByArgsUseFactoryClassButNoStatic",TestBean.class);
		System.out.println();
	}

}
