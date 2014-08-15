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

package org.frameworkset.spi.remote.webservice;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.ws.Endpoint;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.cxf.jaxws.spi.ProviderImpl;
import org.apache.log4j.Logger;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.assemble.Pro;

import com.frameworkset.util.SimpleStringUtil;


/**
 * <p>Title: WebServicePublisherUtil.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2011-5-11 下午06:17:20
 * @author biaoping.yin
 * @version 1.0
 */
public final class WebServicePublisherUtil {
	/**
	 * 
	 */
	private static Map<String,Pro> tracks = new HashMap<String,Pro>();
	private static final Logger logger = Logger.getLogger(WebServicePublisherUtil.class);
	public static void loaderContextWebServices(BaseApplicationContext context,ClassLoader classLoader)
	{
		if(context == null)
			return;
		
		if (logger.isDebugEnabled()) {
			logger.debug("Looking up and Load webservices in application context: " + context.getConfigfile());
		}
		Set<String> beanNames = context.getPropertyKeys();
		if(beanNames == null || beanNames.size() == 0)
			return ;
		// Take any bean name that we can determine URLs for.
		Iterator<String> beanNamesItr = beanNames.iterator();
		while(beanNamesItr.hasNext()) {
			String beanName = beanNamesItr.next();
			try
			{
				Pro wspro = context.getProBean(beanName);
				if(!wspro.isWSService())
					continue;
				else {
					registerWS( wspro, classLoader);
				}
			}
			catch(Exception e)
			{
//				if (logger.) 
				{
					logger.error("Detect Handler bean name '" + beanName + "' failed: " + e.getMessage(),e);
				}
			}
			
		}
		
	}
	
	public static boolean hasPublished(String servicePort)
	{
		return  tracks.containsKey(servicePort);
	}
	
	public static Pro getPublishedWSInfo(String servicePort)
	{
		return  tracks.get(servicePort);
	}
	
	public static void tracePublishedWSInfo(String servicePort,Pro wsinfo)
	{
		tracks.put(servicePort,wsinfo);
	}
	
	public static String convertServicePort(String ServicePort,ClassLoader classLoader)
	{
		if(!isWeblogicServer( classLoader) )
		{
			
			if(ServicePort.startsWith("/"))
				return ServicePort;
			else if(ServicePort.startsWith("http://") || ServicePort.startsWith("https://"))
			{
				return ServicePort;
			}
			return "/"+ServicePort;
		}
		else
		{
			org.frameworkset.spi.BaseApplicationContext context = org.frameworkset.spi.DefaultApplicationContext
			.getApplicationContext("org/frameworkset/spi/ws/webserivce-modules.xml");
			String ws_base_contextpath = context.getProperty("ws.base.contextpath");
			if(ws_base_contextpath != null)
			{
				if(ServicePort.startsWith(ws_base_contextpath))
					return ServicePort;
				else if(ServicePort.startsWith("http://") || ServicePort.startsWith("https://"))
				{
					return ServicePort;
				}
				return SimpleStringUtil.getRealPath(ws_base_contextpath, ServicePort);
			}
			else
			{
				if(ServicePort.startsWith("/"))
					return ServicePort;
				else if(ServicePort.startsWith("http://") || ServicePort.startsWith("https://"))
				{
					return ServicePort;
				}
				return "/"+ServicePort;
			}
//			return "/"+ServicePort;
		}
	}
	
	private static boolean isWeblogicServer(ClassLoader classLoader)
	{
		
//			try {
//				if(classLoader.getClass().getName().startsWith("weblogic."))
//					return true;
//				return false;
////				weblogic.utils.classloaders.ChangeAwareClassLoader classLoader_ = (weblogic.utils.classloaders.ChangeAwareClassLoader) classLoader;
////				return true;
//			} catch (Exception e) {
//				return false;
//			}
		return false;
		
	}
	private static void registerWS(Pro wspro,ClassLoader classLoader) {
		try
        { 
            String servicePort = wspro.getWSAttribute("ws:servicePort");
            if(servicePort == null || servicePort.trim().equals(""))
                throw new java.lang.IllegalArgumentException("web service [name="+wspro.getName() + ",servicePort="+servicePort+"] config error: must config servicePort attribute for web service ["+wspro.getName() + "] in " + wspro.getConfigFile()  );
            if(tracks.containsKey(servicePort))
            {
            	logger.warn("Webservice ["+wspro.getName() + "] in "+wspro.getConfigFile() 
            			+ " has been registed in "+ tracks.get(servicePort).getConfigFile() + ",ignored this regist action.");
            	return;
            }
            Object webservice = wspro.getBeanObject();
            String mtom = wspro.getWSAttribute("ws:mtom");

            /**
             * tomcat容器下这段是没有问题的，但是在weblogic下就会有问题，所有改为以下方式发布服务：
             * ProviderImpl providerimpl = new ProviderImpl();
             * Endpoint ep = providerimpl.createAndPublishEndpoint(WebServicePublisherUtil.convertServicePort( servicePort_, classLoader), webservice);
             */
//            //            Endpoint ep = Endpoint.publish("/" + servicePort, webservice);
//          Endpoint ep = Endpoint.publish(convertServicePort(servicePort, classLoader), webservice);
            ProviderImpl providerimpl = new ProviderImpl();
            Endpoint ep = providerimpl.createAndPublishEndpoint(convertServicePort(servicePort, classLoader), webservice);
            SOAPBinding binding = (SOAPBinding) ep.getBinding();
            if(mtom != null && mtom.equalsIgnoreCase("true"))
                binding.setMTOMEnabled(true);
            tracks.put(servicePort, wspro);
            logger.debug("Webservice [name="+wspro.getName() + ",servicePort="+servicePort+"]  in "+wspro.getConfigFile() 
        			+ " registed successed.");
        }
        catch(Exception e)
        {        	
           logger.warn(e.getMessage(),e);
        }
		
	}

}
