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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.frameworkset.netty.NettyRPCServer;
import org.frameworkset.spi.assemble.ServiceProviderManager;
import org.frameworkset.spi.cglib.CGLibUtil;
import org.frameworkset.spi.cglib.RemoteCGLibProxy;
import org.frameworkset.spi.remote.Header;
import org.frameworkset.spi.remote.Headers;
import org.frameworkset.spi.remote.JGroupHelper;
import org.frameworkset.spi.remote.RPCAddress;
import org.frameworkset.spi.remote.RPCHelper;
import org.frameworkset.spi.remote.RemoteServiceID;
import org.frameworkset.spi.remote.Target;
import org.frameworkset.spi.remote.Util;
import org.frameworkset.spi.remote.context.RequestContext;
import org.frameworkset.spi.remote.jms.JMSServer;
import org.frameworkset.spi.remote.mina.server.MinaRPCServer;
import org.frameworkset.spi.remote.rmi.RMIServer;
import org.frameworkset.spi.security.SecurityContext;
import org.frameworkset.spi.security.SecurityManager;




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
	 * 缓存客户端代理组件类：
	 * key String类型，对应容器根配置文件
	 * value  Map<String,Object>类型，key表示组件url地址，Object代表客户端代理组件
	 */
	private static Map<String,Map<String,Object>> clientbeans = new HashMap<String,Map<String,Object>>();
	private static Object findObjectFromcache(String context,String name)
	{
		Map<String,Object> objects = clientbeans.get(context);
		if(objects == null)
			return null;
		Object val = objects.get(name);
		return val;
	}
	private static void cacheObject(String context,String name,Object value)
	{
		Map<String,Object> objects = clientbeans.get(context);
		if(objects == null)
		{
			objects = new HashMap<String,Object>();
			clientbeans.put(context, objects);
		}
		
		objects.put(name,value);		
	}
	/**
	 * 获取DefaultApplicationContext类型容器中的服务组件调用代理
	 * @param <T> 泛型类型
	 * @param context 容器标识，一般是容器初始化的配置文件路径
	 * @param name  服务组件访问地址
	 * @param type  组件接口类型，使用泛型来实现接口的自动类型转换
	 * @return 服务组件调用代理
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getSimpleClientBean(String context,String name,Class<T> type)
	{
//    	T value = (T)findObjectFromcache( context,name);
//		if(value != null)
//			return value;
//		synchronized(ClientProxyContext.class)
//		{
//			
//			value = (T)findObjectFromcache( context,name);
//			if(value != null)
//				return value;
//			RemoteServiceID serviceID = buildServiceID(name,context,BaseApplicationContext.container_type_simple);
//			serviceID.setInfType(type);
//			RemoteCallContext ccontext = new RemoteCallContextImpl(context,BaseApplicationContext.container_type_simple);
//			buildClientCallContext(serviceID, ccontext,false);
//			value =  CGLibUtil.getBeanInstance(type, new RemoteCGLibProxy(serviceID,ccontext));
//			cacheObject(context,name,value);
//			
//		}
//		return value;
		return _getClientBean(context,name,type,BaseApplicationContext.container_type_simple);
		
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T _getClientBean(String context,String name,Class<T> type,int containerType)
	{
    	T value = (T)findObjectFromcache( context,name);
		if(value != null)
			return value;
		synchronized(ClientProxyContext.class)
		{
			
			value = (T)findObjectFromcache( context,name);
			if(value != null)
				return value;
			RemoteServiceID serviceID = buildServiceID(name,context,containerType);
			serviceID.setInfType(type);
			RemoteCallContext ccontext = new RemoteCallContextImpl(context,containerType);
			buildClientCallContext(serviceID, ccontext,false);
			value =  CGLibUtil.getBeanInstance(type, new RemoteCGLibProxy(serviceID,ccontext));
			cacheObject(context,name,value);
			
		}
		return value;
		
	}

	/**
	 * 构建特定组件管理容器远程请求调用上下文中参数头信息和安全上下文信息
	 * 
	 * @fixed biaoping.yin 2010-10-11
	 * @param params
	 * @param context
	 * @param applicationContext
	 * @return
	 */
	public static RemoteCallContext buildClientCallContext(RemoteServiceID remoteServiceID,
			RemoteCallContext context,boolean fromrest) {
		String params = null;
		Headers headers = null;
		SecurityContext securityContext = null;
		String user = null;
		String password = null;
		boolean hasparams = false;
//		Map<String,Header> contextHeaders = null;
		Map<String,Header> contextHeaders = fromrest && RequestContext.getRequestContext(false) != null ?RequestContext.getRequestContext(false).getHeaders():null;
		do
		{
			params = remoteServiceID.getUrlParams();
			if(params == null || params.equals(""))
			{
				remoteServiceID = remoteServiceID.getRestfulServiceID();
				continue;
			}
			else
			{
				if(!hasparams)
					hasparams = true;
				remoteServiceID = remoteServiceID.getRestfulServiceID();
			}
			StringTokenizer tokenizer = new StringTokenizer(params, "&", false);
			
			/**
			 * 协议中包含的属性参数，可以用来做路由条件
			 */
			
			while (tokenizer.hasMoreTokens()) {
	
				String parameter = tokenizer.nextToken();
	
				int idex = parameter.indexOf("=");
				if (idex <= 0) {
					throw new SPIException("非法的服务参数串[" + params + "]");
				}
				StringTokenizer ptokenizer = new StringTokenizer(parameter, "=",
						false);
				String name = ptokenizer.nextToken();
				String value = ptokenizer.nextToken();
				Header header = new Header(name, value);
				if (name.equals(SecurityManager.USER_ACCOUNT_KEY)) {
					user = value;
	
				} else if (name.equals(SecurityManager.USER_PASSWORD_KEY)) {
					password = value;
	
				} else {
					if (headers == null)
						headers = new Headers();
					headers.put(header.getName(), header);
				}
			}
		}while(remoteServiceID != null);
		if(!hasparams)
		{
			if(contextHeaders != null&& contextHeaders.size() > 0)
			{
				headers = new Headers();
				headers.putAll(contextHeaders);
				context.setHeaders(headers);
			}
			
			return context;
		}
		else
		{
			if (securityContext == null)
				securityContext = new SecurityContext(user, password);
			context.setSecutiryContext(securityContext);
			if(contextHeaders != null && contextHeaders.size() > 0)
			{
				if(headers == null)
					headers = new Headers();
				headers.putAll(contextHeaders);
				
			}
			context.setHeaders(headers);
			return context;
		}
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
//		RemoteServiceID serviceID = buildServiceID(name,context,BaseApplicationContext.container_type_application);
//		serviceID.setInfType(type);
//		RemoteCallContext ccontext = new RemoteCallContextImpl(context,BaseApplicationContext.container_type_application);
//		buildClientCallContext(serviceID, ccontext,false);
//		return CGLibUtil.getBeanInstance(type, new RemoteCGLibProxy(serviceID,ccontext));
		return _getClientBean(context,name,type,BaseApplicationContext.container_type_application);
	}
	
	/**
	  * 获取ApplicationContext类型容器中的服务组件调用代理
	 * @param <T> 泛型类型
	 * @param context 容器标识，一般是容器初始化的配置文件路径
	 * @param name  服务组件访问地址
	 * @param type  组件接口类型，使用泛型来实现接口的自动类型转换
	 * @return 服务组件调用代理
	 */
	public static <T> T getApplicationClientBean(String context,String name,Class<T> type,int containertype)
	{
//		RemoteServiceID serviceID = buildServiceID(name,context,containertype);
//		serviceID.setInfType(type);
//		RemoteCallContext ccontext = new RemoteCallContextImpl(context,containertype);
//		buildClientCallContext(serviceID, ccontext,false);
//		return CGLibUtil.getBeanInstance(type, new RemoteCGLibProxy(serviceID,ccontext));
		return _getClientBean(context,name,type,containertype);
	}
	
	
	public static Object getRestClientBean(RemoteServiceID restid)
	{
		RemoteServiceID serviceID = copyServiceID( restid);
		
		RemoteCallContext ccontext = new RemoteCallContextImpl(restid.getApplicationContext(),restid.getContainerType());
		buildClientCallContext(serviceID, ccontext,true);
		if(serviceID.getRestfulServiceID() != null && !serviceID.getRestfulServiceID().isRestStyle())
			return CGLibUtil.getBeanInstance(restid.getInfType(), new RemoteCGLibProxy(serviceID.getRestfulServiceID(),ccontext));
		else
			return CGLibUtil.getBeanInstance(restid.getInfType(), new RemoteCGLibProxy(serviceID,ccontext));
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
//		RemoteServiceID serviceID = buildServiceID(name,BaseApplicationContext.mvccontainer_identifier,BaseApplicationContext.container_type_mvc);
//		serviceID.setInfType(type);
//		RemoteCallContext ccontext = new RemoteCallContextImpl(BaseApplicationContext.mvccontainer_identifier,BaseApplicationContext.container_type_mvc);
//		buildClientCallContext(serviceID, ccontext,false);
//		return CGLibUtil.getBeanInstance(type, new RemoteCGLibProxy(serviceID,ccontext));
		return _getClientBean(BaseApplicationContext.mvccontainer_identifier,name,type,BaseApplicationContext.container_type_mvc);
	}
	
	public static RemoteServiceID buildServiceID(String serviceid,String applicationcontext,int containerType) {
		return RPCHelper.buildClientServiceID(serviceid, applicationcontext		, containerType		 );

	}
	
	public static RemoteServiceID copyServiceID(RemoteServiceID restid) {
		return RPCHelper.buildClientServiceIDFromRestID(restid	 );

	}
	

	/**
	 * 根据远程调用目标ip地址和端口获取对应服务器中返回的结果 适用于mina和jgroup协议
	 * 其他协议应该通过特定协议的接口和带协议的getRPCResult方法获取 对应服务器的接口
	 * 
	 * @param ip
	 * @param port
	 * @param values
	 * @return
	 * @throws Throwable
	 */
	public static Object getRPCResult(String ip, String port, Object values)
			throws Throwable {

		return RPCHelper.getRPCResult(ip, port, values);
	}

	/**
	 * 根据url地址获取对应的结果值
	 * 
	 * @param url
	 *            对应的远程地址url
	 * @param values
	 *            多播调用返回的结果集
	 * @param protocol
	 *            rpc对应的远程协议，
	 * @see org.frameworkset.remote.Target.BROADCAST_TYPE_MUTICAST
	 *      org.frameworkset.remote.Target.BROADCAST_TYPE_UNICAST
	 *      org.frameworkset.remote.Target.BROADCAST_TYPE_JRGOUP
	 *      org.frameworkset.remote.Target.BROADCAST_TYPE_MINA
	 *      org.frameworkset.remote.Target.BROADCAST_TYPE_JMS
	 *      org.frameworkset.remote.Target.BROADCAST_TYPE_RMI
	 *      org.frameworkset.remote.Target.BROADCAST_TYPE_EJB
	 *      org.frameworkset.remote.Target.BROADCAST_TYPE_CORBA
	 *      org.frameworkset.remote.Target.BROADCAST_TYPE_WEBSERVICE
	 * @return
	 * @throws Throwable
	 */
	public static Object getRPCResult(String url, Object values, String protocol)
			throws Throwable {
		return RPCHelper.getRPCResult(url, values, protocol);

	}

	public static Object getRPCResult(int index, Object values)
			throws Throwable {
		return RPCHelper.getRPCResult(index, values);

	}

	public static int getRPCResultSize(Object values) {
		return RPCHelper.getRPCResultSize(values);
	}


	/**
	 * 获取当前所有连通的节点清单
	 */
	@SuppressWarnings("unchecked")
	public static List<RPCAddress> getAllNodes() {
		// {
		// List<Address> nodes = JGroupHelper.getJGroupHelper().getAppservers();
		// List<RPCAddress> ret = new ArrayList<RPCAddress>();
		// for (IpAddress ipaddr : nodes)
		// {
		// RPCAddress rpcaddr = new RPCAddress(ipaddr.getIpAddress(),
		// ipaddr.getPort());
		// ret.add(rpcaddr);
		// }

		// return ret;
		return RPCHelper.getRPCHelper().getAllNodes();
	}

	public static List<RPCAddress> getAllNodes(String protocol) {
		// {
		// List<Address> nodes = JGroupHelper.getJGroupHelper().getAppservers();
		// List<RPCAddress> ret = new ArrayList<RPCAddress>();
		// for (IpAddress ipaddr : nodes)
		// {
		// RPCAddress rpcaddr = new RPCAddress(ipaddr.getIpAddress(),
		// ipaddr.getPort());
		// ret.add(rpcaddr);
		// }

		// return ret;
		return RPCHelper.getRPCHelper().getAllNodes(protocol);
	}

	

	/**
	 * 校验地址是否是合法可连通的地址
	 * 
	 * @param address
	 * @return
	 */
	public static boolean validateAddress(RPCAddress address) {
		// Vector servers = getAppservers();
		// for(int i = 0; i < servers.size() ; i ++)
		// {
		// IpAddress ipAddress = (IpAddress)servers.get(0);
		// if(ipAddress.equals(address))
		// return true;
		//                  
		// }
		// return false;
		// return JGroupHelper.getJGroupHelper().validateAddress(address);
		return RPCHelper.getRPCHelper().validateAddress(address);
	}

	/**
	 * 判断系统是否启用了集群功能
	 * 
	 * @return
	 */
	public static boolean clusterstarted() {
		return JGroupHelper.getJGroupHelper().clusterstarted()
				|| MinaRPCServer.getMinaRPCServer().started()
				|| JMSServer.getJMSServer().started();
	}

	/**
	 * 判断系统是否启用了集群功能
	 * 
	 * @return
	 */
	public static boolean clusterstarted(String protocol) {

		if (protocol == null) {
			protocol = Util.default_protocol;
		}
		if (protocol.equals(Target.BROADCAST_TYPE_JRGOUP))
			return JGroupHelper.getJGroupHelper().clusterstarted();
		else if (protocol.equals(Target.BROADCAST_TYPE_MINA)) {
			return MinaRPCServer.getMinaRPCServer().started();
		} else if (protocol.equals(Target.BROADCAST_TYPE_NETTY)) {
			return NettyRPCServer.getNettyRPCServer().started();
		} else if (protocol.equals(Target.BROADCAST_TYPE_JMS)) {
			return JMSServer.getJMSServer().started();

		} else if (protocol.equals(Target.BROADCAST_TYPE_RMI)) {
			return RMIServer.getRMIServer().started();

		} else {
			throw new java.lang.IllegalArgumentException("未支持协议：" + protocol);
		}

	}

	public static String getClusterName() {
		return JGroupHelper.getJGroupHelper().getClusterName();
	}

	/**
	 * 获取webservice服务调用结果
	 * 
	 * @param url
	 * @param ret
	 * @return
	 * @throws Throwable
	 */
	public static Object getWSRPCResult(String url, Object ret)
			throws Throwable {
		// TODO Auto-generated method stub
		return getRPCResult(url, ret, Target.BROADCAST_TYPE_WEBSERVICE);
	}

	/**
	 * 获取webservice服务调用结果
	 * 
	 * @param url
	 * @param ret
	 * @return
	 * @throws Throwable
	 */
	public static Object getJMSRPCResult(String url, Object ret)
			throws Throwable {
		// TODO Auto-generated method stub
		return getRPCResult(url, ret, Target.BROADCAST_TYPE_JMS);
	}

	/**
	 * 获取mina服务调用结果
	 * 
	 * @param url
	 * @param ret
	 * @return
	 * @throws Throwable
	 */
	public static Object getMinaRPCResult(String url, Object ret)
			throws Throwable {
		// TODO Auto-generated method stub
		return getRPCResult(url, ret, Target.BROADCAST_TYPE_MINA);
	}

	/**
	 * 获取mina服务调用结果
	 * 
	 * @param url
	 * @param ret
	 * @return
	 * @throws Throwable
	 */
	public static Object getNettyRPCResult(String url, Object ret)
			throws Throwable {
		// TODO Auto-generated method stub

		return getRPCResult(url, ret, Target.BROADCAST_TYPE_NETTY);
	}

	/**
	 * 获取mina服务调用结果
	 * 
	 * @param url
	 * @param ret
	 * @return
	 * @throws Throwable
	 */
	public static Object getRMIRPCResult(String url, Object ret)
			throws Throwable {
		// TODO Auto-generated method stub

		return getRPCResult(url, ret, Target.BROADCAST_TYPE_RMI);
	}

	/**
	 * 获取mina服务调用结果
	 * 
	 * @param url
	 * @param ret
	 * @return
	 * @throws Throwable
	 */
	public static Object getHttpRPCResult(String url, Object ret)
			throws Throwable {
		// TODO Auto-generated method stub

		return getRPCResult(url, ret, Target.BROADCAST_TYPE_HTTP);
	}

	/**
	 * 获取webservice服务调用结果
	 * 
	 * @param url
	 * @param ret
	 * @return
	 * @throws Throwable
	 */
	public static Object getJGroupRPCResult(String url, Object ret)
			throws Throwable {
		// TODO Auto-generated method stub
		return getRPCResult(url, ret, Target.BROADCAST_TYPE_JRGOUP);
	}
//	public static ServiceID buildServiceID(String serviceid, int serviceType,
//			BaseApplicationContext applicationcontext) {
//
//		// // SoftReference<ServiceID> reference;
//		//        
//		//        
//		// long timeout = getRPCRequestTimeout();
//		// ServiceID serviceID = new ServiceID(serviceid, null,
//		// GroupRequest.GET_ALL, timeout, ServiceID.result_rsplist,
//		// serviceType);
//		//           
//
//		return RPCHelper.buildServiceID(serviceid, serviceType,
//				applicationcontext);
//
//	}
	
	
	
    
    

	/**
	 * 构建特定组件管理容器远程请求调用上下文中参数头信息和安全上下文信息
	 * 
	 * @fixed biaoping.yin 2010-10-11
	 * @param params
	 * @param context
	 * @param applicationContext
	 * @return
	 */
	public static RemoteCallContext buildCallContext(String params,
			RemoteCallContext context, BaseApplicationContext applicationContext) {
		if (context == null) {
			RemoteCallContext ccontext = new RemoteCallContextImpl(applicationContext);
		}
		StringTokenizer tokenizer = new StringTokenizer(params, "&", false);

		/**
		 * 协议中包含的属性参数，可以用来做路由条件
		 */
		Headers headers = null;
		SecurityContext securityContext = null;
		String user = null;
		String password = null;
		while (tokenizer.hasMoreTokens()) {

			String parameter = tokenizer.nextToken();

			int idex = parameter.indexOf("=");
			if (idex <= 0) {
				throw new SPIException("非法的服务参数串[" + params + "]");
			}
			StringTokenizer ptokenizer = new StringTokenizer(parameter, "=",
					false);
			String name = ptokenizer.nextToken();
			String value = ptokenizer.nextToken();
			Header header = new Header(name, value);
			if (name.equals(SecurityManager.USER_ACCOUNT_KEY)) {
				user = value;

			} else if (name.equals(SecurityManager.USER_PASSWORD_KEY)) {
				password = value;

			} else {
				if (headers == null)
					headers = new Headers();
				headers.put(header.getName(), header);
			}
		}
		if (securityContext == null)
			securityContext = new SecurityContext(user, password);
		context.setSecutiryContext(securityContext);
		context.setHeaders(headers);
		return context;
	}


}
