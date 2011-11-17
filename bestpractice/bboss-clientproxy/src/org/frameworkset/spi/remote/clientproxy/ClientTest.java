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

package org.frameworkset.spi.remote.clientproxy;

import org.frameworkset.spi.ClientProxyContext;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>Title: ClientTest.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2011-10-20 下午01:12:19
 * @author biaoping.yin
 * @version 1.0
 */
public class ClientTest {
	ClientInf mvcinf ;
	WSService WSService; 
	ClientInf defaultinf ; 
	ClientInf simpleinf;
	@Before
	public void init()
	{
		//获取mvc容器中组件的远程服务调用接口，mvc容器由服务端mvc框架自动初始化
		mvcinf = ClientProxyContext.getWebMVCClientBean(
				"(http::localhost:8080/bboss-mvc/http.rpc)" +
				"/client.proxy.demo?user=admin&password=123456",
				ClientInf.class);
		
		//获取ApplicationContext类型容器中组件的远程服务调用接口
		//ApplicationContext容器必须是以下方式创建
//		ApplicationContext context = ApplicationContext.getApplicationContext("org/frameworkset/web/ws/testwsmodule.xml");
		WSService = ClientProxyContext.getSimpleClientBean("org/frameworkset/web/ws/testwsmodule.xml", 
				"(http::localhost:8080/bboss-mvc/http.rpc)" +
				"/mysfirstwsservice?user=admin&password=123456", 
				WSService.class);
		
		//获取服务器端默认容器中组件的远程服务调用接口
		//服务器端默认容器manager-provider.xml必须是以下方式创建
//		ApplicationContext context = ApplicationContext.getApplicationContext();
		//以下是传统的远程服务获取方式，必须要求本地有相应的接口和组件实现以及配置文件，新的api已经消除了这种限制
//		context.getTBeanObject("(http::localhost:8080/bboss-mvc/http.rpc)" +
//				"/client.proxy.simpledemo?user=admin&password=123456",  ClientInf.class);
		defaultinf = ClientProxyContext.getApplicationClientBean( "(http::localhost:8080/bboss-mvc/http.rpc)" +
				"/client.proxy.simpledemo?user=admin&password=123456", ClientInf.class);
		
		//获取客户端调用代理接口
		//服务器端容器org/frameworkset/spi/ws/webserivce-modules.xml必须是以下方式创建
//		DefaultApplicationContext context = DefaultApplicationContext.getApplicationContext("org/frameworkset/spi/ws/webserivce-modules.xml");
		simpleinf = ClientProxyContext.getSimpleClientBean("org/frameworkset/spi/ws/webserivce-modules.xml",//容器标识
		                                                            "(http::localhost:8080/bboss-mvc/http.rpc)/client.proxy.simpledemo?user=admin&password=123456",//服务组件地址 
		                                                            ClientInf.class);//服务接口
		TestInf TestInf = ClientProxyContext.getWebMVCClientBean(
		                                				"(http::localhost:8080/bboss-mvc/http.rpc)" +
		                                				"//dateconvert/*.html?user=admin&password=123456",
		                                				TestInf.class);
		
		//环境预热
		mvcinf.helloworld("aaaa，多多");
		WSService.sayHello("aaaa，多多");
		simpleinf.helloworld("aaaa，多多");
		defaultinf.helloworld("aaaa，多多");
		String test = TestInf.dateconvert();
		System.out.println();
	}
	@Test
	public void testA()
	{
		
	}
	@Test
	public void testMvcClient()
	{
		long s = System.currentTimeMillis();
		//进行远程方法调用,并输出调用结果
		String re = (mvcinf.helloworld("aaaa，多多"));
		long e = System.currentTimeMillis();
		System.out.println("testMvcClient:" +re + "," + (e -s));
		
		
	}
	
	
	@Test
	public void testWSServiceClient()
	{
		
		long s = System.currentTimeMillis();
		//进行远程方法调用,并输出调用结果
		String re = WSService.sayHello("aaaa，多多");
		long e = System.currentTimeMillis();
		System.out.println("testWSServiceClient:" +re + "," + (e -s));
	}
	
	@Test
	public void testDefaultApplicationClient()
	{
		long s = System.currentTimeMillis();
		//进行远程方法调用,并输出调用结果
		String re = (defaultinf.helloworld("aaaa，多多"));
		long e = System.currentTimeMillis();
		System.out.println("testDefaultApplicationClient:" +re + "," + (e -s));
	}
	
	
	@Test
	public void testSimpleClient()
	{
		long s = System.currentTimeMillis();
		//进行远程方法调用,并输出调用结果
		String re = (simpleinf.helloworld("aaaa，多多"));
		long e = System.currentTimeMillis();
		System.out.println("testSimpleClient:" +re + "," + (e -s));
		
	}


}
