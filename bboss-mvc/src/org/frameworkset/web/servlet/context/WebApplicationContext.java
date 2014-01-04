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
package org.frameworkset.web.servlet.context;

import java.net.MalformedURLException;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.BeanDestroyHook;
import org.frameworkset.spi.DefaultApplicationContext;
import org.frameworkset.spi.assemble.AssembleUtil;
import org.frameworkset.spi.assemble.BeanInf;
import org.frameworkset.spi.assemble.ServiceProviderManager;
import org.frameworkset.spi.assemble.callback.AssembleCallback;
import org.frameworkset.spi.assemble.callback.WebDocbaseAssembleCallback;
import org.frameworkset.spi.support.MessageSource;
import org.frameworkset.util.io.Resource;
import org.frameworkset.util.io.ResourcePatternResolver;
import org.frameworkset.web.servlet.DispatchServlet;
import org.frameworkset.web.servlet.i18n.WebMessageSourceUtil;
import org.frameworkset.web.servlet.support.ServletContextResource;
import org.frameworkset.web.servlet.support.WebApplicationContextUtils;
import org.frameworkset.web.ui.ThemeSource;
import org.frameworkset.web.ui.context.Theme;
import org.frameworkset.web.ui.context.UiApplicationContextUtils;
import org.frameworkset.web.util.WebUtils;

import com.frameworkset.spi.assemble.BeanInstanceException;



/**
 * <p>Title: WebApplicationContext.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-24
 * @author biaoping.yin
 * @version 1.0
 */
public class WebApplicationContext extends DefaultApplicationContext implements ThemeSource {
	private static final Logger logger = Logger.getLogger(WebApplicationContext.class);
	/**
	 * Context attribute to bind root WebApplicationContext to on successful startup.
	 * <p>Note: If the startup of the root context fails, this attribute can contain
	 * an exception or error as value. Use WebApplicationContextUtils for convenient
	 * lookup of the root WebApplicationContext.
	 * @see WebApplicationContextUtils#getWebApplicationContext
	 * @see WebApplicationContextUtils#getRequiredWebApplicationContext
	 */
	public static final String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.class.getName() + ".ROOT";


	/**
	 * Scope identifier for request scope: "request".
	 * Supported in addition to the standard scopes "singleton" and "prototype".
	 */
	public static final String SCOPE_REQUEST = "request";

	/**
	 * Scope identifier for session scope: "session".
	 * Supported in addition to the standard scopes "singleton" and "prototype".
	 */
	public static final String SCOPE_SESSION = "session";

	/**
	 * Scope identifier for global session scope: "globalSession".
	 * Supported in addition to the standard scopes "singleton" and "prototype".
	 */
	public static final String SCOPE_GLOBAL_SESSION = "globalSession";
	protected WebApplicationContext(String configfile) {
		super(configfile);
//		this.onRefresh();
		
	}
	
	
	
	public WebApplicationContext(String webprex, String docbase,
			String configfile) {
		super( webprex,  docbase,
				 configfile);
//		this.onRefresh();
	}



	private ServletContext servletContext;

	private ThemeSource themeSource;


	
	


	/**
	 * Set the ServletContext that this WebApplicationContext runs in.
	 */
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public ServletContext getServletContext() {
		return this.servletContext;
	}


	

	/**
	 * This implementation supports file paths beneath the root of the ServletContext.
	 * @see ServletContextResource
	 */
	protected Resource getResourceByPath(String path) {
		return new ServletContextResource(this.servletContext, path);
	}

	/**
	 * This implementation supports pattern matching in unexpanded WARs too.
	 * @see ServletContextResourcePatternResolver
	 */
	protected ResourcePatternResolver getResourcePatternResolver() {
		return new ServletContextResourcePatternResolver(this);
	}

	/**
	 * Initialize the theme capability.
	 */
	protected void onRefresh() {
		this.themeSource = UiApplicationContextUtils.initThemeSource(this);
	}

	public Theme getTheme(String themeName) {
		return this.themeSource.getTheme(themeName);
	}
	
//	
//	/**
//	 * 加载bboss-mvc配置文件，包括相关view，theme，resource，controler等等的配置，configfile配置文件及其
//	 * 导入的其他子配置文件公用一个组件管理容器，该容器中对应的所有组件和属性和其他组件管理容器中的组件和属性
//	 * 互相隔离，不存在跨容器依赖关系。
//	 * 
//	 * @return
//	 */
//	public static WebApplicationContext getWebApplicationContext(String configfile) {
//		if (configfile == null || configfile.equals("")) {
//			logger.debug("configfile is null or empty.default Config File["
//					+ ServiceProviderManager.defaultConfigFile
//					+ "] will be used. ");
//			configfile = ServiceProviderManager.defaultConfigFile;
//		}
//		WebApplicationContext instance = (WebApplicationContext)applicationContexts.get(configfile);
//		if (instance != null)
//			return instance;
//		synchronized (lock) {
//			instance = (WebApplicationContext)applicationContexts.get(configfile);
//			if (instance != null)
//				return instance;
//			instance = new WebApplicationContext(configfile);
//			ApplicationContext.addShutdownHook(new BeanDestroyHook(instance));
//			applicationContexts.put(configfile, instance);
//			return instance;
//
//		}
//	}
	
	/**
	 * 加载bboss-mvc配置文件，包括相关view，theme，resource，controler等等的配置，configfile配置文件及其
	 * 导入的其他子配置文件公用一个组件管理容器，该容器中对应的所有组件和属性和其他组件管理容器中的组件和属性
	 * 互相隔离，不存在跨容器依赖关系。
	 * 
	 * @return
	 */
	public static WebApplicationContext getWebApplicationContext(ServletContext servletContext,String configfile) {
		if (configfile == null || configfile.equals("")) {
			logger.debug("configfile is null or empty.default Config File["
					+ ServiceProviderManager.defaultConfigFile
					+ "] will be used. ");
			configfile = ServiceProviderManager.defaultConfigFile;
		}
		
		
		WebApplicationContext instance = (WebApplicationContext)applicationContexts.get(configfile);
		if (instance != null)
		{
			instance.initApplicationContext(servletContext);
			return instance;
		}
//		try {
//			System.setErr(new PrintStream(new File("d:/wserr.log")));
//			System.setOut(new PrintStream(new File("d:/wsout.log")));
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		synchronized (lock) {
			instance = (WebApplicationContext)applicationContexts.get(configfile);
			if (instance == null)
			{
				
				String docbase = servletContext.getRealPath("");
				
				
				if(docbase == null)
				{
					try {
//						System.out.print("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$servletContext.getResource(/).getFile()："+servletContext.getResource("/").getFile());
						System.out.print("servletContext.getRealPath(/)："+servletContext.getRealPath("/"));
//						System.out.print("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$servletContext.getResource(/).getPath()："+servletContext.getResource("/").getPath());
						docbase = servletContext.getResource("/").getPath();
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
//				System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%docbase:" + docbase);
				AssembleUtil.registAssembleCallback(new WebDocbaseAssembleCallback(docbase));
				instance = new WebApplicationContext(AssembleCallback.webprex,"",configfile);
				
				
				BaseApplicationContext.addShutdownHook(new BeanDestroyHook(instance));
				applicationContexts.put(BaseApplicationContext.mvccontainer_identifier, instance);
				applicationContexts.put(configfile, instance);
			}
			

		}
		instance.initApplicationContext(servletContext);
		return instance;
		
		
		
		
	}
	
	protected void initApplicationContext(ServletContext servletContext)
	{
		if(applicationContextInited)
			return;
		synchronized(initlock)
		{
			if(applicationContextInited)
				return;
			setServletContext(servletContext);
			onRefresh();
			super.initApplicationContext();
			
			applicationContextInited = true;
		}
	}
	
	/**
	 * Initialize the MessageSource. Use parent's if none defined in this
	 * context.
	 */
	protected void initMessageSource() {


		String messageSource_basename = DispatchServlet.getMessagesources();
		
		String 	messageSource_useCodeAsDefaultMessage = DispatchServlet.getUseCodeAsDefaultMessage();
		
		try
		{

			MessageSource ms = WebMessageSourceUtil.getMessageSource(messageSource_basename, servletContext,Boolean.parseBoolean(messageSource_useCodeAsDefaultMessage)) ;
					
			this.initBean(ms, "org.frameworkset.spi.support.HotDeployResourceBundleMessageSource");
			
			this.messageSource = ms;
			if (logger.isDebugEnabled()) {
				logger.debug("Using MessageSource [" + this.messageSource + "]");
			}
		}
		catch(Exception e)
		{
			logger.error("Using MessageSource [org.frameworkset.spi.support.HotDeployResourceBundleMessageSource] failed:",e);
			
		}
		
		
	}
	
//	/**
//	 * Initialize the MessageSource. Use parent's if none defined in this
//	 * context.
//	 */
//	protected void initMessageSource() {
//
//		String messageClass = DispatchServlet.getDefaultStrategies().getProperty("messageSource","org.frameworkset.spi.support.HotDeployResourceBundleMessageSource");
////		String messageSource_basename = DispatchServlet.getDefaultStrategies().getProperty("messageSource.basename","/WEB-INF/messages");
//		String messageSource_basename = DispatchServlet.getMessagesources();
//		
////		String 	messageSource_cacheSeconds = DispatchServlet.getDefaultStrategies().getProperty("messageSource.cacheSeconds","-1");
//		String 	messageSource_useCodeAsDefaultMessage = DispatchServlet.getUseCodeAsDefaultMessage();
//		
//		Class  message = null;
//		try
//		{
//			message = Class.forName(messageClass);
//			HotDeployResourceBundleMessageSource ms = (HotDeployResourceBundleMessageSource) message.newInstance();
//			ms.setBasename(messageSource_basename);
//			ms.setResourceLoader(new ServletContextResourceLoader(this.servletContext));
////			ms.setCacheSeconds(Integer.parseInt(messageSource_cacheSeconds));
//			ms.setUseCodeAsDefaultMessage(Boolean.parseBoolean(messageSource_useCodeAsDefaultMessage));
//			this.initBean(ms, "org.frameworkset.spi.support.HotDeployResourceBundleMessageSource");
//			
//			this.messageSource = ms;
//			if (logger.isDebugEnabled()) {
//				logger.debug("Using MessageSource [" + this.messageSource + "]");
//			}
//		}
//		catch(Exception e)
//		{
//			logger.error("Using MessageSource [" + messageClass + "] failed:",e);
//			try {
//				message = Class.forName("org.frameworkset.spi.support.HotDeployResourceBundleMessageSource");
//				HotDeployResourceBundleMessageSource ms = (HotDeployResourceBundleMessageSource) message.newInstance();
//				ms.setBasename(messageSource_basename);
//				ms.setResourceLoader(new ServletContextResourceLoader(this.servletContext));
////				ms.setCacheSeconds(Integer.parseInt(messageSource_cacheSeconds));
//				ms.setUseCodeAsDefaultMessage(Boolean.parseBoolean(messageSource_useCodeAsDefaultMessage));
//				this.initBean(ms, "org.frameworkset.spi.support.HotDeployResourceBundleMessageSource");
//				
//				this.messageSource = ms;
//				if (logger.isDebugEnabled()) {
//					logger.debug("Using MessageSource [" + this.messageSource + "]");
//				}
//			} catch (Exception e1) {
////				if (logger.isDebugEnabled()) {
//					logger.error("Using MessageSource [" + messageClass + "] failed:",e1);
////				}
//			}
//		}
//		
//		 
//		
////		else {
////			// Use empty MessageSource to be able to accept getMessage calls.
////			DelegatingMessageSource dms = new DelegatingMessageSource();
////			dms.setParentMessageSource(getInternalParentMessageSource());
////			this.messageSource = dms;
////			// beanFactory.registerSingleton(MESSAGE_SOURCE_BEAN_NAME,
////			// this.messageSource);
////			if (log.isDebugEnabled()) {
////				log.debug("Unable to locate MessageSource with name '"
////						+ MESSAGE_SOURCE_BEAN_NAME + "': using default ["
////						+ this.messageSource + "]");
////			}
////		}
//		
//		
//	}
	
	public Object createBean(Class clazz) throws BeanInstanceException{
		return createBean(clazz, null);
	}
	
	public Object createBean(Class clazz, BeanInf providerManagerInfo) throws BeanInstanceException{
		try {
			Object ret = clazz.newInstance();
			return initBean(ret,providerManagerInfo);
		} catch (BeanInstanceException e) {
			throw e;
		} catch (InstantiationException e) {
			throw new BeanInstanceException(e);
		} catch (IllegalAccessException e) {
			throw new BeanInstanceException(e);
		}
		 catch (Exception e) {
				throw new BeanInstanceException(e);
		}
	}
	
	public Object initBean(Object bean , BeanInf providerManagerInfo) throws BeanInstanceException{
		try {
			
			return WebUtils.initBean(bean,  providerManagerInfo, this);
		} catch (BeanInstanceException e) {
			throw e;
		}
		 catch (Exception e) {
				throw new BeanInstanceException(e);
		}
		
	}
	
	public Object initBean(Object bean , String beanName) throws BeanInstanceException{
		try {
			
			return WebUtils.initBean(bean,  beanName, this);
		} catch (BeanInstanceException e) {
			throw e;
		}
		 catch (Exception e) {
				throw new BeanInstanceException(e);
		}
		
	}
	
	
	
	
	
	
	
}
