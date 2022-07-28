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
import org.frameworkset.util.shutdown.ShutdownUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static Logger log = LoggerFactory.getLogger(ApplicationContext.class);

	
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
		try {
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
				ShutdownUtil.addShutdownHook(new BeanDestroyHook(instance));
				applicationContexts.put(configfile, instance);
				

			}
			instance.initApplicationContext();
			return instance;
		} 
		catch(java.lang.ClassCastException e){
			
			throw new RuntimeException("版本冲突，5.0.2.3以后的版本不建议使用ApplicationContext.getApplicationContext(),但是仍然需要使用旧版默认容器对象，则必须修改jar包bboss-core-xxx.jar/aop.properites文件中的defaultApplicationContext属性值设置为BaseSPIManager1，"
					+ "\r\n如果需要使用新的版本，升级策略：\r\n请将defaultApplicationContext属性值设置为=BaseSPIManager2并且将代码中所有调用ApplicationContext.getApplicationContext()方法的地方调整为BaseSPIManager2.getDefaultApplicationContext()\r\n"
					+ "所有使用用BaseSPIManager类的地方改为BaseSPIManager2类.",e);
		}
		catch(java.lang.RuntimeException e){
			throw e;
		}
		
		
	}
	

	// private static Map<String, ServiceID> serviceids = new
	// java.util.WeakHashMap<String, ServiceID>();

//	public ServiceID buildServiceID(Map<String, ServiceID> serviceids,
//			String serviceid, int serviceType, String providertype,
//			BaseApplicationContext applicationcontext) {
//		return RPCHelper.buildServiceID(serviceids, serviceid, serviceType,
//				providertype, applicationcontext);
//
//	}
//
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
//
//	public ServiceID buildServiceID(Map<String, ServiceID> serviceids,
//			String serviceid, int serviceType,
//			BaseApplicationContext applicationcontext) {
//
//		return buildServiceID(serviceids, serviceid, serviceType, null,
//				applicationcontext);
//
//	}
//
//	public ServiceID buildBeanServiceID(Map<String, ServiceID> serviceids,
//			String serviceid, BaseApplicationContext applicationcontext) {
//
//		return buildServiceID(serviceids, serviceid,
//				ServiceID.PROPERTY_BEAN_SERVICE, null, applicationcontext);
//
//	}

	
	

	

	


}
