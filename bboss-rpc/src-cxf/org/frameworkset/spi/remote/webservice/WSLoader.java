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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

import javax.servlet.ServletConfig;
import javax.xml.ws.Endpoint;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.jaxws.spi.ProviderImpl;
import org.apache.log4j.Logger;
import org.frameworkset.spi.ApplicationContext;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.assemble.Pro;
import org.frameworkset.spi.assemble.ProList;
import org.frameworkset.spi.assemble.ServiceProviderManager;




/**
 * <p>Title: WSLoader.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2011-4-23 下午05:04:15
 * @author biaoping.yin
 * @version 1.0
 */
public class WSLoader {
	private static final Logger logger = Logger.getLogger(WSLoader.class);
	
	 
	private static boolean loadDefaultWebServiceOk = false;
	private static boolean loadModuleWebServiceOk = false;
	private static boolean loadMvcWebServiceOk = false;
	 public static String wsconfigFile = "org/frameworkset/spi/ws/webserivce-modules.xml";
	 public static String managerProviderconfigFile = "manager-provider.xml";
	 public static boolean webservice_enable()	 
     {
		 if(WSLoader.fileexist(managerProviderconfigFile))
		 {
	     	try {
	     		
					return  ApplicationContext.getApplicationContext().getBooleanProperty("rpc.webservice.enable",true);
				} catch (Exception e) {
					return true;
				}
	     	
	     	
		 }
		 else
			 return true;
     }
	 
	 public static Bus loadBusNoConfig(ServletConfig servletConfig)
     
	 {
	          	
	         Bus bus = BusFactory.getDefaultBus(true);
	         return bus;
	     
	//     ResourceManager resourceManager = (ResourceManager)bus.getExtension(org.apache.cxf.resource.ResourceManager.class);
	//     resourceManager.addResourceResolver(new ServletContextResourceResolver(servletConfig.getServletContext()));
	//     replaceDestinationFactory();
	//     controller = createServletController(servletConfig);
	 }
	 /**
	  * for mvc
	  * @param classLoader
	  * @param config
	  */
	 public static void publishAllWebService(ClassLoader classLoader,ServletConfig config)
	 {		 
		 boolean webservice_enable =  WSLoader.webservice_enable();
		 if(webservice_enable)
	        {        	
	    //        super.loadBus(servletConfig);        
			    WSLoader.loadBusNoConfig(config);
	            // You could add the endpoint publish codes here
//	        	org.apache.cxf.transport.servlet.AbstractCXFServlet.LOG.info("LOAD_BUS_WITHOUT_APPLICATION_CONTEXT");
	    //        Bus bus = getBus();
	    //        BusFactory.setDefaultBus(bus); 
	        	
	        	WSLoader.loadAllWebService(classLoader);
//	            ProList webservices = WSUtil.webservices;
//	            if(webservices != null)
//	            {
//	                for(int i = 0;i < webservices.size(); i ++)
//	                {
//	                    try
//	                    {
//	                        Pro pro = webservices.getPro(i);
//	                        Object webservice = pro.getBeanObject();
//	                        String servicePort = pro.getStringExtendAttribute("servicePort");
//	                        if(servicePort == null || servicePort.trim().equals(""))
//	                            throw new java.lang.IllegalArgumentException("web service ["+pro.getName() + "] config error: must config servicePort attribute for web service ["+pro.getName() + "]"  );
//	                        String mtom = pro.getStringExtendAttribute("mtom");
//	                        Endpoint ep = Endpoint.publish("/" + servicePort, webservice);
//	                        SOAPBinding binding = (SOAPBinding) ep.getBinding();
//	                        if(mtom != null && mtom.equalsIgnoreCase("true"))
//	                            binding.setMTOMEnabled(true);
//	                    }
//	                    catch(Exception e)
//	                    {
//	                        e.printStackTrace();
//	                    }
//	                    
//	                }
//	            }
	        }
	        else
	        {
	            System.out.println("CXF not started,rpc.webservice.enable = false. Please check config file [org/frameworkset/spi/manager-rpc-service.xml] to enable you cxf webservice.");
	        }
	 }
	public static void loadAllWebService(ClassLoader classLoader)
	{
		
		try {
			if(!WSLoader.loadDefaultWebServiceOk)
			{
				synchronized(WSLoader.class)
				{
					if(!WSLoader.loadDefaultWebServiceOk)
					{
						WSLoader.loadDefaultWebServiceOk = true;
						loadDefaultWebService( classLoader);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		try {
			if(!WSLoader.loadModuleWebServiceOk)
			{
				synchronized(WSLoader.class)
				{
					if(!WSLoader.loadModuleWebServiceOk)
					{
						WSLoader.loadModuleWebServiceOk = true;
						loadModulesWebService( classLoader);
					}
				}
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		try {
			if(!WSLoader.loadMvcWebServiceOk)
			{
				synchronized(WSLoader.class)
				{
					if(!WSLoader.loadMvcWebServiceOk)
					{
						WSLoader.loadMvcWebServiceOk = true;
						loadMvcWebService( classLoader);
					}
				}
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	
		
	}
	private static void loadDefaultWebService(ClassLoader classLoader)
	{
		 ProList webservices =  ApplicationContext.getApplicationContext().getListProperty("cxf.webservices.config")   ;
         if(webservices != null)
         {
             for(int i = 0;i < webservices.size(); i ++)
             {
                 try
                 {
                     Pro pro = webservices.getPro(i);
                     
                     String servicePort_ = pro.getStringExtendAttribute("servicePort");
                     
                     if(servicePort_ == null || servicePort_.equals(""))
                     {
                    	 logger.warn("Webservice ["+pro.getName() + "] in "+pro.getConfigFile() 
                      			+ " has not defined servicePort,please check this definition,ignored this regist action.. ");
                    	 continue;
                     }
                     String servicePort = servicePort_.startsWith("ws:") ?servicePort_:"ws:" + servicePort_;
                     if(WebServicePublisherUtil.hasPublished(servicePort))
                     {
                     	logger.warn("Webservice ["+pro.getName() + "] in "+pro.getConfigFile() 
                     			+ " has been registed in "+ WebServicePublisherUtil.getPublishedWSInfo(servicePort).getConfigFile() + ",ignored this regist action.");
                     	return;
                     }
                     
                     Object webservice = pro.getBeanObject();
                     String mtom = pro.getStringExtendAttribute("mtom");
                     /**
                      * tomcat容器下这段是没有问题的，但是在weblogic下就会有问题，所有改为以下方式发布服务：
                      * ProviderImpl providerimpl = new ProviderImpl();
                      * Endpoint ep = providerimpl.createAndPublishEndpoint(WebServicePublisherUtil.convertServicePort( servicePort_, classLoader), webservice);
                      */
//                     Endpoint ep = Endpoint.publish(WebServicePublisherUtil.convertServicePort( servicePort_, classLoader), webservice);
                     ProviderImpl providerimpl = new ProviderImpl();
                     Endpoint ep = providerimpl.createAndPublishEndpoint(WebServicePublisherUtil.convertServicePort( servicePort_, classLoader), webservice);
                     SOAPBinding binding = (SOAPBinding) ep.getBinding();
                     if(mtom != null && mtom.equalsIgnoreCase("true"))
                         binding.setMTOMEnabled(true);
                     WebServicePublisherUtil.tracePublishedWSInfo(servicePort, pro);
                 }
                 catch(Exception e)
                 {
                    logger.warn(e.getMessage(),e);
                 }
                 
             }
         }
	}
	
	
	
	/**
	 * 需要确保mvc分派器在webservice服务引擎之前启动，否则获取不到任何在mvc框架中配置的webservice服务
	 */
	private static void loadMvcWebService(ClassLoader classLoader)
	{
		
		try {
//			org.frameworkset.spi.BaseApplicationContext context = org.frameworkset.spi.DefaultApplicationContext
//					.getApplicationContext("org/frameworkset/spi/ws/webserivce-modules.xml");
			Class clas = Class.forName("org.frameworkset.web.servlet.support.WebApplicationContextUtils");
			Method m = clas.getMethod("getWebApplicationContext", null);
			org.frameworkset.spi.BaseApplicationContext context = (BaseApplicationContext)m.invoke(null, null);
			if(context == null)
			{
				WSLoader.loadMvcWebServiceOk = false;
				return;
			}
			WebServicePublisherUtil.loaderContextWebServices(context,classLoader);
		} catch (Exception e) {
			logger.warn(e.getMessage(),e);
		}
	}
	  private static ClassLoader getTCL() throws IllegalAccessException, InvocationTargetException {
	        Method method = null;
	        try {
	            method = (java.lang.Thread.class).getMethod("getContextClassLoader", null);
	        } catch (NoSuchMethodException e) {
	            return null;
	        }
	        return (ClassLoader)method.invoke(Thread.currentThread(), null);
	    }
	public static boolean fileexist(String configFile)
	{/////
		
//		String configFile = "org/frameworkset/spi/ws/webserivce-modules.xml";
		 URL confURL = ServiceProviderManager.class.getClassLoader().getResource(configFile);
         if (confURL == null)
             confURL = ServiceProviderManager.class.getClassLoader().getResource("/" + configFile);

         try {
			if (confURL == null)
			     confURL = getTCL().getResource(configFile);
			 if (confURL == null)
			     confURL = getTCL().getResource("/" + configFile);
			 if (confURL == null)
			     confURL = ClassLoader.getSystemResource(configFile);
			 if (confURL == null)
			     confURL = ClassLoader.getSystemResource("/" + configFile);
		} catch (Exception e) {
			return false;
		}

         if (confURL == null) 
        	 return false;
         return true;
	}
	private static void loadModulesWebService(ClassLoader classLoader)
	{
		
		if(!fileexist(wsconfigFile))
			return;
		org.frameworkset.spi.BaseApplicationContext context = org.frameworkset.spi.DefaultApplicationContext
		.getApplicationContext(wsconfigFile);
		String[] cxf_webservices_modules = context.getStringArray("cxf.webservices.modules");
		if(cxf_webservices_modules == null || cxf_webservices_modules.length == 0)
			return ;
		org.frameworkset.spi.BaseApplicationContext context_ = null;
		/**
		 * 预加载所有ws模块应用上下文
		 */
		for(String t:cxf_webservices_modules)
		{
			context_ = org.frameworkset.spi.DefaultApplicationContext.getApplicationContext(t);
//			loaderContextWebServices(context_);
		}
		/**
		 * 提取每个模块上下文中的webservice服务并加载之
		 */
		for(String t:cxf_webservices_modules)
		{
			context_ = org.frameworkset.spi.DefaultApplicationContext.getApplicationContext(t);
			WebServicePublisherUtil.loaderContextWebServices(context_,classLoader);
		}
		
	}
	
	
	
	
	
}
