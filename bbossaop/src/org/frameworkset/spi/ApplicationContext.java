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

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.frameworkset.netty.NettyRPCServer;
import org.frameworkset.spi.assemble.ServiceProviderManager;
import org.frameworkset.spi.remote.JGroupHelper;
import org.frameworkset.spi.remote.RPCAddress;
import org.frameworkset.spi.remote.RPCHelper;
import org.frameworkset.spi.remote.ServiceID;
import org.frameworkset.spi.remote.Target;
import org.frameworkset.spi.remote.Util;
import org.frameworkset.spi.remote.jms.JMSServer;
import org.frameworkset.spi.remote.mina.server.MinaRPCServer;
import org.frameworkset.spi.remote.rmi.RMIServer;

/**
 * <p>
 * Title: ApplicationContext.java
 * </p>
 * <p>
 * Description: ApplicationContext组件是对抽象组件BaseApplicationContext的扩展，主要功能
 * 特性是除了拥有基本的aop和ioc、全局属性管理的功能外，还具备支持远程服务组件调用的功能
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2010-3-21 下午08:46:12
 * @author biaoping.yin
 * @version 1.0
 */
public class ApplicationContext extends BaseApplicationContext {

	private static Logger log = Logger.getLogger(ApplicationContext.class);

	
	protected ApplicationContext(String configfile)
	{
		super(configfile);
	}
	protected ApplicationContext(String configfile, boolean isfile)
	{
		super(configfile,isfile,true);
	}
	protected ApplicationContext(String docbaseType, String docbase,
			String configfile)
	{
		super( docbaseType,  docbase,
				 configfile);
	}
	protected ApplicationContext(String docbaseType, String docbase,
			String configfile,boolean isfile)
	{
		super( docbaseType,  docbase,
				 configfile, isfile,true);
	}
	

	/**
	 * 获取默认上下文的bean组件管理容器，配置文件从manager-provider.xml文件开始
	 * 
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return getApplicationContext(null);
	}
	
	/**
	 * 获取指定根配置文件上下文bean组件管理容器，配置文件从参数configfile对应配置文件开始
	 * 不同的上下文件环境容器互相隔离，组件间不存在依赖关系，属性也不存在任何引用关系。
	 * 
	 * @return
	 */
	public static ApplicationContext getApplicationContext(String configfile) {
		if (configfile == null || configfile.equals("")) {
			log.debug("configfile is null or empty.default Config File["
					+ ServiceProviderManager.defaultConfigFile
					+ "] will be used. ");
			configfile = ServiceProviderManager.defaultConfigFile;
		}
		ApplicationContext instance = (ApplicationContext)applicationContexts.get(configfile);
		if (instance != null)
		{
			instance.initApplicationContext();
			return instance;
		}
		synchronized (lock) {
			instance = (ApplicationContext)applicationContexts.get(configfile);
			if (instance != null)
				return instance;
			instance = new ApplicationContext(configfile);
			BaseApplicationContext.addShutdownHook(new BeanDestroyHook(instance));
			applicationContexts.put(configfile, instance);
			

		}
		instance.initApplicationContext();
		return instance;
	}
	

	// private static Map<String, ServiceID> serviceids = new
	// java.util.WeakHashMap<String, ServiceID>();

	public ServiceID buildServiceID(Map<String, ServiceID> serviceids,
			String serviceid, int serviceType, String providertype,
			BaseApplicationContext applicationcontext) {
		return RPCHelper.buildServiceID(serviceids, serviceid, serviceType,
				providertype, applicationcontext);

	}

	public static ServiceID buildServiceID(String serviceid, int serviceType,
			BaseApplicationContext applicationcontext) {

		// // SoftReference<ServiceID> reference;
		//        
		//        
		// long timeout = getRPCRequestTimeout();
		// ServiceID serviceID = new ServiceID(serviceid, null,
		// GroupRequest.GET_ALL, timeout, ServiceID.result_rsplist,
		// serviceType);
		//           

		return RPCHelper.buildServiceID(serviceid, serviceType,
				applicationcontext);

	}

	public ServiceID buildServiceID(Map<String, ServiceID> serviceids,
			String serviceid, int serviceType,
			BaseApplicationContext applicationcontext) {

		return buildServiceID(serviceids, serviceid, serviceType, null,
				applicationcontext);

	}

	public ServiceID buildBeanServiceID(Map<String, ServiceID> serviceids,
			String serviceid, BaseApplicationContext applicationcontext) {

		return buildServiceID(serviceids, serviceid,
				ServiceID.PROPERTY_BEAN_SERVICE, null, applicationcontext);

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


}
