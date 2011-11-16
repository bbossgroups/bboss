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

import org.frameworkset.spi.assemble.ServiceProviderManager;
import org.frameworkset.spi.cglib.CGLibUtil;
import org.frameworkset.spi.cglib.RemoteCGLibProxy;
import org.frameworkset.spi.remote.RPCHelper;
import org.frameworkset.spi.remote.ServiceID;




/**
 * <p>Title: ClientProxyContext.java</p> 
 * <p>Description: 远程服务组件动态调用代理工厂，可根据服务地址，服务接口，服务对应的容器标识
 * 生成服务的客户端调用代理组件，然后通过代理组件来实现远程服务调用，使用实例如下：
 * 
 * @Test
	public void testMvcClient()
	{
		//获取mvc容器中组件的远程服务调用接口，mvc容器由服务端mvc框架自动初始化
		ClientInf inf = ClientProxyContext.getWebMVCClientBean(
				"(http::172.16.25.108:8080/bboss-mvc/http.rpc)" +
				"/client.proxy.demo?user=admin&password=123456",
				ClientInf.class);
		//进行远程方法调用,并输出调用结果
		System.out.println(inf.helloworld("aaaa，多多"));
		
		
	}
	
	
	@Test
	public void testApplicationClient()
	{
		//获取ApplicationContext类型容器中组件的远程服务调用接口
		WSService inf = ClientProxyContext.getApplicationClientBean("org/frameworkset/web/ws/testwsmodule.xml", 
				"(http::172.16.25.108:8080/bboss-mvc/http.rpc)" +
				"/mysfirstwsservice?user=admin&password=123456", 
				WSService.class);
		//ApplicationContext容器必须是以下方式创建
//		ApplicationContext context = ApplicationContext.getApplicationContext("org/frameworkset/web/ws/testwsmodule.xml");
		//进行远程方法调用,并输出调用结果
		System.out.println(inf.sayHello("aaaa，多多"));
	}
	
	@Test
	public void testDefaultApplicationClient()
	{
		//获取服务器端默认容器中组件的远程服务调用接口
		ClientInf inf = ClientProxyContext.getApplicationClientBean( "(http::172.16.25.108:8080/bboss-mvc/http.rpc)" +
				"/client.proxy.simpledemo?user=admin&password=123456", ClientInf.class);
		//进行远程方法调用,并输出调用结果
		System.out.println(inf.helloworld("aaaa，多多"));
		//服务器端默认容器manager-provider.xml必须是以下方式创建
//		ApplicationContext context = ApplicationContext.getApplicationContext();
		//以下是传统的远程服务获取方式，必须要求本地有相应的接口和组件实现以及配置文件，新的api已经消除了这种限制
//		context.getTBeanObject("(http::172.16.25.108:8080/bboss-mvc/http.rpc)" +
//				"/client.proxy.simpledemo?user=admin&password=123456",  ClientInf.class);
	}
	
	
	@Test
	public void testSimpleClient()
	{
		//获取客户端调用代理接口
		ClientInf inf = ClientProxyContext.getSimpleClientBean("org/frameworkset/spi/ws/webserivce-modules.xml",//容器标识
		                                                            "(http::172.16.25.108:8080/bboss-mvc/http.rpc)/client.proxy.simpledemo?user=admin&password=123456",//服务组件地址 
		                                                            ClientInf.class);//服务接口
		//进行远程方法调用,并输出调用结果
		System.out.println(inf.helloworld("aaaa，多多"));
		
		//服务器端容器org/frameworkset/spi/ws/webserivce-modules.xml必须是以下方式创建
//		DefaultApplicationContext context = DefaultApplicationContext.getApplicationContext("org/frameworkset/spi/ws/webserivce-modules.xml");
	}
 * </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2011-10-19 下午09:43:26
 * @author biaoping.yin
 * @version 1.0
 */
public class ClientProxyContext
{
	/**
	 * 获取DefaultApplicationContext类型容器中的服务组件调用代理
	 * @param <T> 泛型类型
	 * @param context 容器标识，一般是容器初始化的配置文件路径
	 * @param name  服务组件访问地址
	 * @param type  组件接口类型，使用泛型来实现接口的自动类型转换
	 * @return 服务组件调用代理
	 */
	public static <T> T getSimpleClientBean(String context,String name,Class<T> type)
	{
		ServiceID serviceID = buildServiceID(name,context,BaseApplicationContext.container_type_simple);
		CallContext ccontext = new CallContext(context,BaseApplicationContext.container_type_simple);
		BaseApplicationContext.buildClientCallContext(serviceID.getUrlParams(), ccontext);
		return CGLibUtil.getBeanInstance(type, new RemoteCGLibProxy(serviceID,ccontext));
		
	}
	
	/**
	  * 获取服务端默认容器中的服务组件调用代理
	 * @param <T> 泛型类型	 
	 * @param name  服务组件访问地址
	 * @param type  组件接口类型，使用泛型来实现接口的自动类型转换
	 * @return 服务组件调用代理
	 */
	public static <T> T getApplicationClientBean(String name,Class<T> type)
	{
		return getApplicationClientBean(ServiceProviderManager.defaultConfigFile,name,type);
	}
	
	/**
	  * 获取ApplicationContext类型容器中的服务组件调用代理
	 * @param <T> 泛型类型
	 * @param context 容器标识，一般是容器初始化的配置文件路径
	 * @param name  服务组件访问地址
	 * @param type  组件接口类型，使用泛型来实现接口的自动类型转换
	 * @return 服务组件调用代理
	 */
	public static <T> T getApplicationClientBean(String context,String name,Class<T> type)
	{
		ServiceID serviceID = buildServiceID(name,context,BaseApplicationContext.container_type_application);
		CallContext ccontext = new CallContext(context,BaseApplicationContext.container_type_application);
		BaseApplicationContext.buildClientCallContext(serviceID.getUrlParams(), ccontext);
		return CGLibUtil.getBeanInstance(type, new RemoteCGLibProxy(serviceID,ccontext));
	}
	
	/**
	  * 获取MVC容器中的服务组件调用代理
	 * @param <T>	泛型类型 
	 * @param name  服务组件访问地址
	 * @param type  组件接口类型，使用泛型来实现接口的自动类型转换
	 * @return 服务组件调用代理
	 */
	public static <T> T getWebMVCClientBean(String name,Class<T> type)
	{
		ServiceID serviceID = buildServiceID(name,BaseApplicationContext.mvccontainer_identifier,BaseApplicationContext.container_type_mvc);
		CallContext ccontext = new CallContext(BaseApplicationContext.mvccontainer_identifier,BaseApplicationContext.container_type_mvc);
		BaseApplicationContext.buildClientCallContext(serviceID.getUrlParams(), ccontext);
		return CGLibUtil.getBeanInstance(type, new RemoteCGLibProxy(serviceID,ccontext));
	}
	
	public static ServiceID buildServiceID(String serviceid,String applicationcontext,int containerType) {
		return RPCHelper.buildClientServiceID(serviceid, applicationcontext		, containerType		 );

	}

}
