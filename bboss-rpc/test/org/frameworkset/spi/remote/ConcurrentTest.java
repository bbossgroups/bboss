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

package org.frameworkset.spi.remote;

import org.frameworkset.spi.ApplicationContext;
import org.junit.Test;

/**
 * <p>Title: ConcurrentTest.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-7-22 下午02:44:46
 * @author biaoping.yin
 * @version 1.0
 */
public class ConcurrentTest {
	static ApplicationContext context = ApplicationContext.getApplicationContext("org/frameworkset/spi/remote/manager-rpc-test.xml");
	@Test
	public void test()
	{
		TestT a = new TestT();
		a.start();
		RuntimeException s;
		TestB b = new TestB();
		b.start();
//		TestO o = new TestO();
//		o.start();
	}
	
	public static void main(String[] args)
	{
		ConcurrentTest test = new ConcurrentTest();
		test.test();
	}
	static class TestT extends Thread
	{
		public void run()
		{
			while(true)
			{
				RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(rmi::172.16.17.216:1099)/rpc.test");
				byte[] bytes = testInf.getBytes("多多");
				System.out.println(bytes);
				try {
					sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	static class TestB extends Thread
	{
		public void run()
		{
			while(true)
			{
				RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(rmi::172.16.17.216:1099)/rpc.test");
				String bytes = testInf.getString("多多");
				System.out.println(bytes);
				try {
					sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	static class TestO extends Thread
	{
		public void run()
		{
			while(true)
			{
				RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(rmi::172.16.17.216:1099)/rpc.test");
				O bytes = testInf.getO();
				System.out.println(bytes);
				try {
					sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	static class O implements java.io.Serializable
	{
		public O(String name)
		{
			this.name = name;
		}
		String name;
		
	}
}
