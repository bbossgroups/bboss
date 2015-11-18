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

import java.io.InputStream;
import java.net.URL;

import org.apache.log4j.Logger;
import org.frameworkset.spi.assemble.ServiceProviderManager;
import org.frameworkset.spi.assemble.callback.AssembleCallback;

/**
 * <p>Title: DefaultApplicationContext.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2011-5-11 上午11:25:51
 * @author biaoping.yin
 * @version 1.0
 */
public class DefaultApplicationContext extends BaseApplicationContext {
	private static Logger log = Logger.getLogger(DefaultApplicationContext.class);
	/**
	 * 获取指定根配置文件上下文bean组件管理容器，配置文件从参数configfile对应配置文件开始
	 * 不同的上下文件环境容器互相隔离，组件间不存在依赖关系，属性也不存在任何引用关系。
	 * 
	 * @return
	 */
	public static DefaultApplicationContext getApplicationContext(String configfile) {
		if (configfile == null || configfile.equals("")) {
			log.debug("configfile is null or empty.default Config File["
					+ ServiceProviderManager.defaultConfigFile
					+ "] will be used. ");
			configfile = ServiceProviderManager.defaultConfigFile;
		}
		DefaultApplicationContext instance = (DefaultApplicationContext)applicationContexts.get(configfile);
		if (instance != null)
		{
			instance.initApplicationContext();
			return instance;
		}
		synchronized (lock) {
			instance = (DefaultApplicationContext)applicationContexts.get(configfile);
			if (instance != null)
				return instance;
			instance = new DefaultApplicationContext(configfile);
			BaseApplicationContext.addShutdownHook(new BeanDestroyHook(instance));
			applicationContexts.put(configfile, instance);
			

		}
		instance.initApplicationContext();
		return instance;
	}
	
	
	public static DefaultApplicationContext getApplicationContext(URL configfile) {
		if (configfile == null || configfile.equals("")) {
			throw new IllegalArgumentException("configfile is null");
		}
		String conf = configfile.getFile();
		DefaultApplicationContext instance = (DefaultApplicationContext)applicationContexts.get(conf);
		if (instance != null)
		{
			instance.initApplicationContext();
			return instance;
		}
		synchronized (lock) {
			instance = (DefaultApplicationContext)applicationContexts.get(conf);
			if (instance != null)
				return instance;
			instance = new DefaultApplicationContext(configfile);
			BaseApplicationContext.addShutdownHook(new BeanDestroyHook(instance));
			applicationContexts.put(conf, instance);
			

		}
		instance.initApplicationContext();
		return instance;
	}
	
	protected DefaultApplicationContext(String configfile)
	{
		super(configfile);
	}
	protected DefaultApplicationContext(String configfile, boolean isfile,boolean init)
	{
		super((String)configfile,isfile,init);
	}
	protected DefaultApplicationContext(String configfile, boolean isfile,String charset,boolean init)
	{
		super((String)configfile,isfile,charset, init);
	}
	protected DefaultApplicationContext(String docbaseType, String docbase,
			String configfile)
	{
		super( docbaseType,  docbase,
				 configfile);
	}
	protected DefaultApplicationContext(String docbaseType, String docbase,
			String configfile,boolean isfile)
	{
		super( docbaseType,  docbase,
				 configfile, isfile,true);
	}
	public DefaultApplicationContext(URL file, String path)
	{

		super((URL)file,  path);
	}

	public DefaultApplicationContext(InputStream instream, boolean isfile,boolean init) {
		super((InputStream)instream,isfile,init);
	}
	
	/**
	 * 可以用于外部文件加载，类似于构造函数
	 * public DefaultApplicationContext(String configfile)
	 * @param configfile
	 */
	public DefaultApplicationContext(URL configfile) {
		super(AssembleCallback.classpathprex, "", 
				configfile, true);
	}


//	public ServiceID buildServiceID(Map<String, ServiceID> serviceids,
//			String serviceid, int serviceType, String providertype,
//			BaseApplicationContext applicationcontext) {
//		return ServiceIDUtil.buildServiceID(serviceids, serviceid, serviceType,
//				providertype, applicationcontext);
//
//	}
//
//
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
