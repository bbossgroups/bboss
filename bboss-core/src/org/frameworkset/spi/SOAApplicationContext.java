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
package org.frameworkset.spi;

import java.io.InputStream;
import java.net.URL;

import org.apache.log4j.Logger;
import org.frameworkset.spi.assemble.Pro;
import org.frameworkset.spi.assemble.ServiceProviderManager;

/**
 * <p>Title: SOAApplicationContext.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2011-5-9 下午06:12:52
 * @author biaoping.yin
 * @version 1.0
 */
public class SOAApplicationContext extends DefaultApplicationContext {
	private static Logger log = Logger.getLogger(SOAApplicationContext.class);
	private String charset;
	protected boolean serial; 
	
	public SOAApplicationContext(String soacontent) {
		super((String)soacontent,false,true);
	}
	
	public SOAApplicationContext(String soacontent,boolean init) {
		super((String)soacontent,false,init);
	}
	public SOAApplicationContext(String soacontent,String charset) {
		
		super((String)soacontent,false,charset,true);
	}
	
	public SOAApplicationContext(String soacontent,String charset,boolean init) {
		
		super((String)soacontent,false,charset,init);
	}
	
	
	
	
	
	public SOAApplicationContext(URL file, String path)
	{
		super((URL)file,  path);
		
	}
	
	public SOAApplicationContext(InputStream instream)
	{
		super((InputStream)instream,  false,true);
		
	}
	
	public SOAApplicationContext(InputStream instream,boolean init)
	{
		super((InputStream)instream,  false, init);
		
	}





	/**
	 * 本方法，对于SOAApplicationContext容器是不适用的，呵呵
	 * 获取指定根配置文件上下文bean组件管理容器，配置文件从参数configfile对应配置文件开始
	 * 不同的上下文件环境容器互相隔离，组件间不存在依赖关系，属性也不存在任何引用关系。
	 * 
	 * @return
	 * 
	 */
	public static SOAApplicationContext getApplicationContext(String configfile) {
		if (configfile == null || configfile.equals("")) {
			log.debug("configfile is null or empty.default Config File["
					+ ServiceProviderManager.defaultConfigFile
					+ "] will be used. ");
			configfile = ServiceProviderManager.defaultConfigFile;
		}
		SOAApplicationContext instance = (SOAApplicationContext)applicationContexts.get(configfile);
		if (instance != null)
		{
			instance.initApplicationContext();
			return instance;
		}
		synchronized (lock) {
			instance = (SOAApplicationContext)applicationContexts.get(configfile);
			if (instance != null)
				return instance;
			instance = new SOAApplicationContext(configfile);
			BaseApplicationContext.addShutdownHook(new BeanDestroyHook(instance));
			applicationContexts.put(configfile, instance);
			

		}
		instance.initApplicationContext();
		return instance;
	}
	public boolean isSerial() {
		return serial;
	}

	public void setSerial(boolean serial) {
		this.serial = serial;
	}
	
	 public void init()
    {
    	try
    	{
	    	providerManager = new ServiceProviderManager(this);
	    	providerManager.setSerial(serial);
	    	if(this.instream == null)
	    	{
	    		providerManager.init(docbaseType, docbase, configfile);
	    		
	    	}
	    	else
	    	{
	    		providerManager.init(docbaseType, docbase, instream);
	    		
	    	}
			
    	}
    	finally
    	{
    		this.docbaseType = null;
			this.docbase = null;
			this.configfile = null;
			this.instream = null;
    	}
		
		
    }

	
	/**
	 * bean组件工厂方法， 如果serviceID不为空，则serviceID是根据getBeanObject(Context context,
	 * String name,Object defaultValue)方法的name生成的
	 * 否则需要根据providerManagerInfo的name或者refid来生成serviceID
	 * 
	 * @param context
	 * @param providerManagerInfo
	 * @param defaultValue
	 * @param serviceID
	 * @return
	 */
	public Object getBeanObject(CallContext context, Pro providerManagerInfo,
			Object defaultValue) {
		if (providerManagerInfo == null)
			throw new SPIException("bean对象为空。");
//		String key = providerManagerInfo.getName();
		if (providerManagerInfo.isRefereced()) {
			Object retvalue = providerManagerInfo.getTrueValue(context,
					defaultValue);
			return retvalue;
		}

		Object finalsynProvider = this.providerManager.getBeanObject(context,
				providerManagerInfo, defaultValue);
		return finalsynProvider;
		
	}
	
	/**
	 * bean工厂方法
	 * 
	 * @param context
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Object getBeanObject(CallContext context, String name, Object defaultValue,
			boolean fromprovider) {
		// 分析服务参数
//		int idx = name.indexOf("?");

//		String _name = name;
		if (context == null)
			context = new LocalCallContextImpl(this);
//		if (idx > 0) {
//			String params = name.substring(idx + 1);
//			context = buildCallContext(params, context);
//			// name = name.substring(0,idx);
//		}

		
		// new ServiceID(name,GroupRequest.GET_ALL,0,ServiceID.result_rsplist,
		// ServiceID.PROPERTY_BEAN_SERVICE);
		Pro providerManagerInfo = this.providerManager
				.getPropertyBean(name);
		return getBeanObject(context, providerManagerInfo, defaultValue);
		
	}
	
	public Object proxyObject(Pro providerManagerInfo,Object refvalue,String refid)
    {
//    	if (providerManagerInfo.enableTransaction()
//				|| providerManagerInfo.enableAsyncCall()
//				|| providerManagerInfo.usedCustomInterceptor()) {
//    		if (refid != null && providerManagerInfo.isSinglable()) {
//				Object provider = servicProviders.get(refid);
//				if (provider != null)
//					return provider;
//				synchronized (servicProviders) {
//					provider = servicProviders.get(refid);
//					if (provider != null)
//						return provider;
//					provider = createInf( providerManagerInfo,
//							refvalue);
//					servicProviders.put(refid, provider);
//				}
//				return provider;
//			} else {
//				refvalue = createInf( providerManagerInfo,
//						refvalue);
//				return refvalue;
//			}
//		} else 
		
		return refvalue;
		
    }
	
	


}
