/*
 *  Copyright 2008-2010 biaoping.yin
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
package org.frameworkset.web.ws;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.frameworkset.spi.remote.webservice.JaxWsProxyFactory;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>Title: WSClient.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2011-4-28 下午01:59:19
 * @author biaoping.yin
 * @version 1.0
 */
public class WSClient {
	BaseApplicationContext context ; 
	org.frameworkset.web.ws.WSService wsservice;
	@Before
	public void init()
	{
		context = DefaultApplicationContext.getApplicationContext("org/frameworkset/web/ws/wsclient.xml"); 
		org.apache.cxf.jaxws.JaxWsProxyFactoryBean factory = new org.apache.cxf.jaxws.JaxWsProxyFactoryBean();
		factory.setAddress("http://localhost:8080/bboss-mvc/cxfservices/mysfirstwsservicePort");
		factory.setServiceClass(org.frameworkset.web.ws.WSService.class);
		wsservice =  (WSService)factory.create();
		wsservice.sayHello("多多");
	}
	@Test
	public void testA()
	{
		org.frameworkset.web.ws.WSService wsservice =  (WSService)context.getBeanObject("WSServiceClient");
		System.out.println(wsservice.sayHello("多多"));
	}
	@Test
	public void testcode()
	{
		long s = System.currentTimeMillis();
		String re = wsservice.sayHello("多多");
		long e = System.currentTimeMillis();
		System.out.println("testMvcClient:" +re + "," + (e -s));
	}
	
	@Test
	public void testmyserivce()
	{
		Myservice ddd = context.getTBeanObject("myservicessss",Myservice.class);
		System.out.println(ddd.test("duoduo"));
	}
	@Test
	public void testBbossProxy()
	{
		org.frameworkset.web.ws.WSService wsservice = JaxWsProxyFactory.getWSClient("http://localhost:8080/" +
				"bboss-mvc/cxfservices/mysfirstwsservicePort",WSService.class);
		System.out.println(wsservice.sayHello("多多"));
	}
	
	
	
	
	                                                                                                                          

}
