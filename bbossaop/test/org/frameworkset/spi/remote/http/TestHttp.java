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

package org.frameworkset.spi.remote.http;

import org.frameworkset.spi.ClientProxyContext;
import org.frameworkset.spi.remote.RPCHelper;
import org.frameworkset.spi.remote.RPCTestInf;
import org.frameworkset.spi.remote.TestBase;
import org.frameworkset.spi.remote.health.RPCValidator;
import org.junit.Test;

/**
 * <p>Title: TestHttp.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-9-12 下午09:17:45
 * @author biaoping.yin
 * @version 1.0
 */
public class TestHttp extends TestBase{
	 
	@Test
	public void testFromHttpServer()
	{
		RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("(http::172.16.7.108:8080)/rpc.test?user=admin&password=123456&server_uuid=app1",RPCTestInf.class);
//		RPCTestInf testInf = (RPCTestInf)BaseSPIManager.getBeanObject("rpc.test");
		 System.out.println("testInf.getCount() = "+testInf.getCount());
		long start = System.currentTimeMillis();
		
		for(int i = 0; i < 1; i ++)
		    System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
		
		long end = System.currentTimeMillis();
		System.out.println("消耗时间：" + (end - start)  + "毫秒");
		
	}
	
	@Test
	public void testAuthsuccessHttpServer()
	{
		RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("(http::172.16.7.108:8080)/rpc.test?user=admin&password=123456&server_uuid=app1",RPCTestInf.class);
//		RPCTestInf testInf = (RPCTestInf)BaseSPIManager.getBeanObject("rpc.test");
		 System.out.println("testInf.getCount() = "+testInf.getCount());
		long start = System.currentTimeMillis();
		
		for(int i = 0; i < 10; i ++)
		    System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
		
		long end = System.currentTimeMillis();
		System.out.println("消耗时间：" + (end - start)  + "毫秒");
		
	}
	
	@Test
	public void testAuthfailedHttpServer()
	{
		try {
			RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("(http::172.16.7.108:8080)/rpc.test?user=admin&password=1234576&server_uuid=app1",RPCTestInf.class);
			//		RPCTestInf testInf = (RPCTestInf)BaseSPIManager.getBeanObject("rpc.test");
			System.out.println("testInf.getCount() = " + testInf.getCount());
			long start = System.currentTimeMillis();
			for (int i = 0; i < 1; i++)
				System.out.println("testInf.getCount():" + i + " = "
						+ testInf.getCount());
			long end = System.currentTimeMillis();
			System.out.println("消耗时间：" + (end - start) + "毫秒");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	@Test
	public void testFromMutiHttpServers()
	{
		RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("(http::172.16.7.108:8080;172.16.7.108:8081)/rpc.test?user=admin&password=123456&server_uuid=app1",RPCTestInf.class);
//		RPCTestInf testInf = (RPCTestInf)BaseSPIManager.getBeanObject("rpc.test");
		 System.out.println("testInf.getCount() = "+testInf.getCount());
		long start = System.currentTimeMillis();
		
		for(int i = 0; i < 1; i ++)
		{
			Object ret = testInf.getCount();
		    try {
				System.out.println("testInf.getCount():" + i + " = "+RPCHelper.getRPCResult("172.16.7.108:8080", ret,"http"));
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    try {
				System.out.println("testInf.getCount():" + i + " = "+RPCHelper.getRPCResult("172.16.7.108:8081", ret,"http"));
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		long end = System.currentTimeMillis();
		System.out.println("消耗时间：" + (end - start)  + "毫秒");
		
	}
	@Test
	public void testFromHttpsServer()
	{
		RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("(https::172.16.7.108:8080)/rpc.test?user=admin&password=123456&server_uuid=app1",RPCTestInf.class);
//		RPCTestInf testInf = (RPCTestInf)BaseSPIManager.getBeanObject("rpc.test");
		  System.out.println("testInf.getCount() = "+testInf.getCount());
		long start = System.currentTimeMillis();
		
		for(int i = 0; i < 1; i ++)
		    System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
		
		long end = System.currentTimeMillis();
		System.out.println("消耗时间：" + (end - start) / 1000 + "秒");
		
	}
	
	
	@Test
	public void testParameterFromHttpsServer()
	{
		RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("(https::172.16.7.108:8080)/rpc.test?user=admin&password=123456&server_uuid=app1",RPCTestInf.class);
//		RPCTestInf testInf = (RPCTestInf)BaseSPIManager.getBeanObject("rpc.test");
		 System.out.println("testInf.getString(): = "+testInf.getParameter());
			
		long start = System.currentTimeMillis();
		
		for(int i = 0; i < 1; i ++)
		    System.out.println("testInf.getString():" + i + " = "+testInf.getParameter());
		
		long end = System.currentTimeMillis();
		System.out.println("消耗时间：" + (end - start)  + "豪秒");
		
	}
	@Test
	public void testFromAppServer()
	{
		RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("(http::172.16.7.108:8080/WebRoot/http.rpc)/rpc.test?user=admin&password=123456&server_uuid=app1",RPCTestInf.class);
//		RPCTestInf testInf = (RPCTestInf)BaseSPIManager.getBeanObject("rpc.test");
		 System.out.println("testInf.getCount():= "+testInf.getCount());
		long start = System.currentTimeMillis();
		
		for(int i = 0; i < 1; i ++)
		    System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
		
		long end = System.currentTimeMillis();
		System.out.println("消耗时间：" + (end - start)  + "毫秒");
		
	}
	
	public void testFromApphttpsServer()
	{
		RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("(https::172.16.7.108:8080/monitor/http.hc)/rpc.test?user=admin&password=123456&server_uuid=app1",RPCTestInf.class);
//		RPCTestInf testInf = (RPCTestInf)BaseSPIManager.getBeanObject("rpc.test");
		long start = System.currentTimeMillis();
		
		for(int i = 0; i < 1; i ++)
		    System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
		
		long end = System.currentTimeMillis();
		System.out.println("消耗时间：" + (end - start) / 1000 + "秒");
		
	}
	
	@Test
	public void validateAuth()
	{
		for(int i = 0;  i < 100; i ++)
		{
			RPCValidator.validator("http","172.16.7.108","807","admin","123456");
		}
		
	}
	@Test
	public void validateAsynAuth()
	{
		new Thread(){
			public void run()
			{
				for(int i = 0;  i < 100; i ++)
				{
					
					RPCValidator.validator("http","172.16.7.108","807","admin","123456");
				}
			}
		}.start();
		
		new Thread(){
			public void run()
			{
				for(int i = 0;  i < 100; i ++)
				{
					
					RPCValidator.validator("http","172.16.7.108","807","admin","123456");
				}
			}
		}.start();
		
		new Thread(){
			public void run()
			{
				for(int i = 0;  i < 100; i ++)
				{
					
					RPCValidator.validator("http","172.16.7.108","807","admin","123456");
				}
			}
		}.start();
		
		new Thread(){
			public void run()
			{
				for(int i = 0;  i < 100; i ++)
				{
					
					RPCValidator.validator("http","172.16.7.108","807","admin","123456");
				}
			}
		}.start();
		System.out.println();
		
	}
	
	
	@Test
	public void validate()
	{
		for(int i = 0;  i < 100; i ++)
		{
			RPCValidator.validator("http","172.16.7.108","807");
		}
	}

}
