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

import javax.xml.ws.Endpoint;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.cxf.jaxws.spi.ProviderImpl;
import org.apache.log4j.Logger;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.assemble.Pro;
import org.frameworkset.spi.assemble.ProList;




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
	
	
	
	public void loadAllWebService(ClassLoader classLoader)
	{
		try {
			loadDefaultWebService( classLoader);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		try {
			loadModulesWebService( classLoader);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		try {
			loadMvcWebService( classLoader);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
	}
	private  void loadDefaultWebService(ClassLoader classLoader)
	{
		 ProList webservices = WSUtil.webservices;
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
                    logger.warn(e);
                 }
                 
             }
         }
	}
	
	
	
	/**
	 * 需要确保mvc分派器在webservice服务引擎之前启动，否则获取不到任何在mvc框架中配置的webservice服务
	 */
	private  void loadMvcWebService(ClassLoader classLoader)
	{
		
		try {
			org.frameworkset.spi.BaseApplicationContext context = org.frameworkset.spi.DefaultApplicationContext
					.getApplicationContext("org/frameworkset/spi/ws/webserivce-modules.xml");
			WebServicePublisherUtil.loaderContextWebServices((BaseApplicationContext) context
					.getBeanObject("webapplicationcontext"),classLoader);
		} catch (Exception e) {
			logger.warn(e);
		}
	}
	
	
	private  void loadModulesWebService(ClassLoader classLoader)
	{
		org.frameworkset.spi.BaseApplicationContext context = org.frameworkset.spi.DefaultApplicationContext
		.getApplicationContext("org/frameworkset/spi/ws/webserivce-modules.xml");
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
