/**
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
package org.frameworkset.spi.remote.hession.client;

import java.net.MalformedURLException;

import org.frameworkset.spi.DefaultApplicationContext;
import org.frameworkset.spi.remote.hession.server.ServiceInf;
import org.junit.Test;

import com.caucho.hessian.client.HessianProxyFactory;

/**
 * <p> ClientTest.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2013-2-22 上午11:14:18
 * @author biaoping.yin
 * @version 1.0
 */
public class ClientTest {

	public ClientTest() {
		// TODO Auto-generated constructor stub
	}
	@Test
	public void testFromIOC()
	{
		DefaultApplicationContext context = DefaultApplicationContext.getApplicationContext("org/frameworkset/spi/remote/hession/client/hessian-client.xml");
			//获取客户端组件实例
		ServiceInf basic = context.getTBeanObject("clientservice", ServiceInf.class);
		System.out.println(basic.hello("duoduo"));
	}
	
	@Test
	public void testHessian() throws MalformedURLException
	{
		String url = "http://localhost:8080/SanyPDP/hessian?container=org/frameworkset/spi/remote/hession/server/hessian-service.xml&service=basicservice";//指定容器标识和容器类型及服务标识
		
		 HessianProxyFactory factory = new HessianProxyFactory();
	    ServiceInf basic = (ServiceInf) factory.create(org.frameworkset.spi.remote.hession.server.ServiceInf.class, url);
	    System.out.println("Hello: " + basic.hello("John"));
			
	}
	
	@Test
	public void testSimpleHessian() throws MalformedURLException
	{
		String url = "http://localhost:8080/SanyPDP/hessian?service=basicservice";//指定容器标识和容器类型及服务标识
		
		 HessianProxyFactory factory = new HessianProxyFactory();
	    ServiceInf basic = (ServiceInf) factory.create(org.frameworkset.spi.remote.hession.server.ServiceInf.class, url);
	    System.out.println("Hello: " + basic.hello("John"));
	    
			
	}
	@Test
	public void testGreatWall() throws MalformedURLException
	{
		String url = "http://gwall.sany.com.cn/sany-greatwall-web/helloservice";
		 HessianProxyFactory factory = new HessianProxyFactory();
		 GreatWallHello basic = (GreatWallHello) factory.create(org.frameworkset.spi.remote.hession.client.GreatWallHello.class, url);
		 System.out.println(basic.greet("greatwall"));
	}

}
