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
package org.frameworkset.web.servlet.i18n;

import javax.servlet.ServletContext;

import org.frameworkset.spi.support.MessageSource;
import org.frameworkset.spi.support.MessageSourceUtil;
import org.frameworkset.web.servlet.support.ServletContextResourceLoader;
/**
 * <p>Title: HierarchicalMessageSource.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2012-6-4
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class WebMessageSourceUtil extends MessageSourceUtil {
	public static MessageSource getMessageSource(String basenames,ServletContext servletContext)
	{
		return getMessageSource(basenames,servletContext,true);
	}
	/**
	 * web资源加载
	 * @param basenames
	 * @param servletContext
	 * @return
	 */
	public static MessageSource getMessageSource(String basenames,ServletContext servletContext,boolean useCodeAsDefaultMessage)
	{
		String key = null;
//		if(basenames.startsWith("web::"))
//		{
//			key = basenames;
//			basenames = basenames.substring("web::".length());
//		}
//		else
		{
			key = "web::" + basenames;
		}
		return getMessageSource_( basenames,new ServletContextResourceLoader(servletContext), useCodeAsDefaultMessage,key);
//		HotDeployResourceBundleMessageSource messageSource = messageSources.get(key);
//		
//		if(messageSource != null)
//		{
//			return messageSource;
//		}
//		else
//		{
//			synchronized(messageSources)
//			{
//				messageSource = messageSources.get(key);
//				if(messageSource != null)
//				{
//					return messageSource;
//				}
//				messageSource = new HotDeployResourceBundleMessageSource();
//				messageSource.setBasename(basenames);
//				messageSource.setResourceLoader(new ServletContextResourceLoader(servletContext));
//				messageSource.setUseCodeAsDefaultMessage(useCodeAsDefaultMessage);
//				messageSource.setChangemonitor(true);
//				messageSources.put(key, messageSource);
//				return messageSource;
//			}
//		}
		
	}
	public static MessageSource getMessageSource(String[] basenames,ServletContext servletContext)
	{
		return getMessageSource(basenames, servletContext,true);
	}
	/**
	 * 
	 * @param basenames
	 * @param servletContext
	 * @return
	 */
	public static MessageSource getMessageSource(String[] basenames,ServletContext servletContext,boolean useCodeAsDefaultMessage)
	{
		if(basenames == null || basenames.length == 0)
			return null;
		StringBuffer ret = new StringBuffer();
		for(int i = 0; i < basenames.length; i ++)
		{
			String name = basenames[i];
			if(i > 0)
				ret.append(",");
			ret.append(name);			
			
		}
		return getMessageSource(ret.toString(), servletContext);
	}
	
//	public static void main(String[] args)
//	{
//		System.out.println("web::xxx".substring("web::".length()));
//	}

}
