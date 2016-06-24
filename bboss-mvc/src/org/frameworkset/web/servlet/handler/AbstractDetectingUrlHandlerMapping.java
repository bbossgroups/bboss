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
package org.frameworkset.web.servlet.handler;



import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.frameworkset.util.ObjectUtils;
import org.frameworkset.util.beans.BeansException;

import com.frameworkset.spi.assemble.BeanInstanceException;

/**
 * <p>Title: AbstractDetectingUrlHandlerMapping.java</p> 
 * <p>Description: bboss-mvc中无需在启动时加载所有的url处理器</p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-24
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class AbstractDetectingUrlHandlerMapping extends AbstractUrlHandlerMapping{
	private boolean detectHandlersInAncestorContexts = false;
	private static Logger logger = Logger.getLogger(AbstractUrlHandlerMapping.class);
	
	


	/**
	 * Set whether to detect handler beans in ancestor ApplicationContexts.
	 * <p>Default is "false": Only handler beans in the current ApplicationContext
	 * will be detected, i.e. only in the context that this HandlerMapping itself
	 * is defined in (typically the current DispatcherServlet's context).
	 * <p>Switch this flag on to detect handler beans in ancestor contexts
	 * (typically the Bboss root WebApplicationContext) as well.
	 */
	public void setDetectHandlersInAncestorContexts(boolean detectHandlersInAncestorContexts) {
		this.detectHandlersInAncestorContexts = detectHandlersInAncestorContexts;
	}

	/**
	 * Calls the {@link #detectHandlers()} method in addition to the
	 * superclass's initialization.
	 */
	protected void initApplicationContext() throws RuntimeException {
		super.initApplicationContext();
		try {
			detectHandlers();
		} catch (Exception e) {
			throw new BeanInstanceException(e);
		}
	}

	
//	/**
//	 * Register all handlers found in the current ApplicationContext.
//	 * <p>The actual URL determination for a handler is up to the concrete
//	 * {@link #determineUrlsForHandler(String)} implementation. A bean for
//	 * which no such URLs could be determined is simply not considered a handler.
//	 * @throws BeansException if the handler couldn't be registered
//	 * @see #determineUrlsForHandler(String)
//	 */
//	protected Object detectHandlers() throws Exception {
//		if (logger.isDebugEnabled()) {
//			logger.debug("Looking for URL mappings in application context: " + getApplicationContext());
//		}
//		Set<String> beanNames = this.getApplicationContext().getPropertyKeys();
//		if(beanNames == null || beanNames.size() == 0)
//			return null;
//		// Take any bean name that we can determine URLs for.
//		Iterator<String> beanNamesItr = beanNames.iterator();
//		while(beanNamesItr.hasNext()) {
//			String beanName = beanNamesItr.next();
//			String[] urls = determineUrlsForHandler(beanName);
//			if(!ObjectUtils.isEmpty(urls) && StringUtil.containKey(urls, urlPath))
//			{
//			
//				// URL paths found: Let's consider it a handler.
//				return registerHandler(urls, beanName);
//				
//			}
//			
//		}
//		return null;
//	}
	
	/**
	 * Register all handlers found in the current ApplicationContext.
	 * <p>The actual URL determination for a handler is up to the concrete
	 * {@link #determineUrlsForHandler(String)} implementation. A bean for
	 * which no such URLs could be determined is simply not considered a handler.
	 * @throws BeansException if the handler couldn't be registered
	 * @see #determineUrlsForHandler(String)
	 */
	protected void detectHandlers() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("Looking for URL mappings in application context: " + getApplicationContext());
		}
		Set<String> beanNames = this.getApplicationContext().getPropertyKeys();
		if(beanNames == null || beanNames.size() == 0)
			return ;
		// Take any bean name that we can determine URLs for.
		Iterator<String> beanNamesItr = beanNames.iterator();
		while(beanNamesItr.hasNext()) {
			String beanName = beanNamesItr.next();
			try
			{
				String[] urls = determineUrlsForHandler(beanName);
				if (!ObjectUtils.isEmpty(urls)) {
					// URL paths found: Let's consider it a handler.
					HandlerMeta meta = new HandlerMeta();
					meta.setHandler(beanName);
					meta.setPathNames(getApplicationContext().getProBean(beanName).getMvcpaths());
					registerHandler(urls, meta);
				}
				else {
					
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
	
	


	/**
	 * Determine the URLs for the given handler bean.
	 * @param beanName the name of the candidate bean
	 * @return the URLs determined for the bean,
	 * or <code>null</code> or an empty array if none
	 */
	protected abstract String[] determineUrlsForHandler(String beanName);

}
