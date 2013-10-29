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

package org.frameworkset.spi.asyn;

import org.frameworkset.spi.ApplicationContext;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>Title: TestRun.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2011-4-21 下午06:46:37
 * @author biaoping.yin
 * @version 1.0
 */
public class TestRun {
	private ApplicationContext context ;
	@Before
	public void init()
	{
		context = ApplicationContext.getApplicationContext("org/frameworkset/spi/asyn/asyn.xml");
	}
	@Test
	public void testAsync()
	{
		AsynbeanTest test = (AsynbeanTest)context.getBeanObject("asyn.AsynbeanTest");
		for(int i = 0; i < 10 ; i++)
			test.testHelloworld("Async call.");
		System.out.println("runned.");
	}
	
	
	@Test
	public void testTimeout5000WithResult()
	{
		AsynbeanTest test = (AsynbeanTest)context.getBeanObject("asyn.AsynbeanTest");
		System.out.println(test.testHelloworld1("Async call Timeout 5000ms with Result."));
		System.out.println("runned.");
	}
	
	@Test
	public void testTimeout5000NoResult()
	{
		AsynbeanTest test = (AsynbeanTest)context.getBeanObject("asyn.AsynbeanTest");
		System.out.println(test.testHelloworld0("Async call Timeout 5000ms No Result."));
		System.out.println("runned.");
	}
	
	@Test
	public void testTimeout5000WithCallBackService()
	{
		AsynbeanTest test = (AsynbeanTest)context.getBeanObject("asyn.AsynbeanTest");
		System.out.println(test.testHelloworld2("Async call Timeout 5000 With CallBackService."));
		System.out.println("runned.");
	}
	@Test
	public void testWithCallBackService()
	{
		AsynbeanTest test = (AsynbeanTest)context.getBeanObject("asyn.AsynbeanTest");
		System.out.println(test.testHelloworld4("Async call With CallBackService."));
		System.out.println("runned.");
	}
	
	@Test
	public void testWithCallBackServiceException()
	{
		AsynbeanTest test = (AsynbeanTest)context.getBeanObject("asyn.AsynbeanTest");
		try {
			System.out.println(test.testHelloworldException("Async call With CallBackService."));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("runned.");
	}
	
	@Test
	public void testResult()
	{
		AsynbeanTest test = (AsynbeanTest)context.getBeanObject("asyn.AsynbeanTest");
		System.out.println(test.testHelloworld3("call With"));
		System.out.println("runned.");
	}
	
	
	

}
