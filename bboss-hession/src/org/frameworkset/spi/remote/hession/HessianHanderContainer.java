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
package org.frameworkset.spi.remote.hession;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.assemble.Pro;

import com.caucho.hessian.io.SerializerFactory;
import com.caucho.services.server.GenericService;

/**
 * <p> HessianHanderContainer.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2013-2-21 下午5:25:30
 * @author biaoping.yin
 * @version 1.0
 */
public class HessianHanderContainer {

	private Map<String,AbstractHessionHandler> hessionHandlers;
	private BaseApplicationContext context;
	private static final Logger log = Logger.getLogger(HessianHanderContainer.class);
	private Map<String,Pro> hessionPortServices = new HashMap<String,Pro>();
	public HessianHanderContainer(BaseApplicationContext context) {
		hessionHandlers = new HashMap<String,AbstractHessionHandler>();
		this.context = context;
		initHessionPortServices();
	}
	public AbstractHessionHandler getHessionHandler(String service) throws Exception
	{
		AbstractHessionHandler handler = this.hessionHandlers.get(service);
		if(handler != null)
			return handler;
		synchronized(hessionHandlers)
		{
			handler = this.hessionHandlers.get(service);
			if(handler != null)
				return handler;
			handler = parserHessionHandler(service) ;
			this.hessionHandlers.put(service, handler);
		}
		if(handler == null)
			 throw new 	java.lang.IllegalArgumentException("Service["+service+"] not found in "+this.context.getConfigfile());
		return handler;
			
	}
	private void initHessionPortServices()
	{
		if(context != null)
		{
			
			Set<String> beanNames = context.getPropertyKeys();
			if(beanNames == null || beanNames.size() == 0)
				return ;
			// Take any bean name that we can determine URLs for.
			Iterator<String> beanNamesItr = beanNames.iterator();
			Pro pro = null;
			while(beanNamesItr.hasNext()) {
				String beanName = beanNamesItr.next();
				try
				{
					pro = context.getProBean(beanName);
					if(pro == null)
						continue;
					String servicePort = pro.getStringExtendAttribute("hessian:servicePort");
					if(servicePort != null)
						this.hessionPortServices.put(servicePort, pro);
				}
				catch(Exception e)
				{
//					if (logger.) 
					{
						log.debug("Detect Hession servicePort Handler from '" + beanName + "' failed: " + e.getMessage(),e);
					}
				}
				
			}
		}
	}
	private Pro _searchWithHessionPort(String service)
	{
//		if(context != null)
//		{
//			
//			Set<String> beanNames = context.getPropertyKeys();
//			if(beanNames == null || beanNames.size() == 0)
//				return null;
//			// Take any bean name that we can determine URLs for.
//			Iterator<String> beanNamesItr = beanNames.iterator();
//			Pro pro = null;
//			while(beanNamesItr.hasNext()) {
//				String beanName = beanNamesItr.next();
//				try
//				{
//					pro = context.getProBean(beanName);
//					if(pro == null)
//						continue;
//					String servicePort = pro.getStringExtendAttribute("hessian:servicePort");
//				}
//				catch(Exception e)
//				{
////					if (logger.) 
//					{
//						logger.error("Detect Handler bean name '" + beanName + "' failed: " + e.getMessage(),e);
//					}
//				}
//				
//			}
//		}
		return hessionPortServices.get(service);
	}
	/**
	 * hessian:api
	 * hessian:serializable xml|bin
	 * hessian:debug default false used by serializable="bin".
	 * hessian:sendCollectionType used by serializable="bin". default true Set whether to send the Java collection type for each serialized collection.
	 * hessian:serializerFactory used by serializable="bin".default com.caucho.hessian.io.SerializerFactory 
	 * hessian:servicePort 指定服务的唯一标识，一般无需指定hessian:servicePort属性，hessian服务的标识直接为组件的名称name或者id，但是如果在mvc控制器模式下的组件名称不是很友好可以
	 * 使用hessian:servicePort来指定一个简单的hessian服务名称
	 * @param service
	 * @return
	 * @throws Exception 
	 */
	private AbstractHessionHandler parserHessionHandler(String service) throws Exception
	{
		Pro pro = this.context.getProBean(service);
		if(pro == null)//检索hessian:servicePort值为service的组件元数据
		{
			
			pro = _searchWithHessionPort(service);
			if(pro == null)
				throw new Exception("Parser Hession Handler failed:hession service["+service+"] not found in hessian container[ "+context.getConfigfile()+"]");
			service = pro.getName();
			
		}
		String api = pro.getStringExtendAttribute("hessian:api");
		String serialtype = pro.getStringExtendAttribute("hessian:serializable");
		if(serialtype == null || "".equals(serialtype))
			serialtype = "bin";
		Object _service = context.getBeanObject(service);
		Class serviceapi = null;
		AbstractHessionHandler handler = null;
		if(api == null || "".equals(api))
		{
			Class serviceClass = _service.getClass();
			serviceapi = this.findRemoteAPI(serviceClass);
			
		}
		else
		{
			serviceapi = Class.forName(api);			
		}
		if(serialtype.equals("bin"))
		{
			HessionHandler handler_ = new HessionHandler();
			handler_.setService(_service);
			handler_.setServiceInterface(serviceapi);
			boolean debug = pro.getBooleanExtendAttribute("hessian:debug",false);
			handler_.setDebug(debug);
			boolean sendCollectionType = pro.getBooleanExtendAttribute("hessian:sendCollectionType",true);
			handler_.setSendCollectionType(sendCollectionType);
			
			String serializerFactory = pro.getStringExtendAttribute("hessian:serializerFactory");
			if(serializerFactory == null || "".equals(serializerFactory))
			{
				
			}
			else
			{
				SerializerFactory _serializerFactory = (SerializerFactory)Class.forName(serializerFactory).newInstance();
				handler_.setSerializerFactory(_serializerFactory);
			}
			
			handler = handler_;
			
		}
		else if(serialtype.equals("xml"))
		{
			handler = new BurlapHandler();
			handler.setService(_service);
			handler.setServiceInterface(serviceapi);
		}
		handler.afterPropertiesSet();
		return handler;
	}
	private Class findRemoteAPI(Class implClass)
	  {
	    if (implClass == null || implClass.equals(GenericService.class))
	      return null;
	    
	    Class []interfaces = implClass.getInterfaces();

	    if (interfaces.length == 1)
	      return interfaces[0];

	    return findRemoteAPI(implClass.getSuperclass());
	  }

}
