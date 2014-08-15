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

package org.frameworkset.spi.support;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.util.io.ResourceLoader;

/**
 * <p>Title: MessageSourceUtil.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2012-6-4 11:56:43
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class MessageSourceUtil {
	protected static  Map<String,HotDeployResourceBundleMessageSource> messageSources = new HashMap<String,HotDeployResourceBundleMessageSource>();
	static
	{
		BaseApplicationContext.addShutdownHook(new Runnable(){

			public void run() {
				HotDeployResourceBundleMessageSource.stopmonitor();	
				if(messageSources != null)
				{
					Iterator<Entry<String, HotDeployResourceBundleMessageSource>> it = messageSources.entrySet().iterator();
					while(it.hasNext())
					{
						Entry<String, HotDeployResourceBundleMessageSource> entry = it.next();
						entry.getValue().destroy();
					}
					messageSources.clear();
					messageSources = null;
				}
			}});
	}
	public static MessageSource getMessageSource(String basenames)
	{
		return getMessageSource(basenames,true);
	}
	protected static MessageSource getMessageSource_(String basenames,ResourceLoader resourceLoader,boolean useCodeAsDefaultMessage,String cachekey)
	{
		HotDeployResourceBundleMessageSource messageSource = messageSources.get(cachekey);
		if(messageSource != null)
		{
			return messageSource;
		}
		else
		{
			synchronized(messageSources)
			{
				messageSource = messageSources.get(cachekey);
				if(messageSource != null)
				{
					return messageSource;
				}
				messageSource = new HotDeployResourceBundleMessageSource();
				messageSource.setBasename(basenames);
				if(resourceLoader != null)
				{
					messageSource.setResourceLoader(resourceLoader);
				}
				messageSource.setUseCodeAsDefaultMessage(useCodeAsDefaultMessage);
				messageSource.setChangemonitor(true);
				messageSources.put(cachekey, messageSource);
				return messageSource;
			}
		}
	}
	public static MessageSource getMessageSource(String basenames,boolean useCodeAsDefaultMessage)
	{
//		HotDeployResourceBundleMessageSource messageSource = messageSources.get(basenames);
//		if(messageSource != null)
//		{
//			return messageSource;
//		}
//		else
//		{
//			synchronized(messageSources)
//			{
//				messageSource = messageSources.get(basenames);
//				if(messageSource != null)
//				{
//					return messageSource;
//				}
//				messageSource = new HotDeployResourceBundleMessageSource();
//				messageSource.setBasename(basenames);
//				messageSource.setUseCodeAsDefaultMessage(useCodeAsDefaultMessage);
//				messageSource.setChangemonitor(true);
//				messageSources.put(basenames, messageSource);
//				return messageSource;
//			}
//		}
		return getMessageSource_(basenames,(ResourceLoader) null,useCodeAsDefaultMessage,basenames);
		
	}
	public static MessageSource getMessageSource(String[] basenames)
	{
		return getMessageSource(basenames,true);
	}
	public static MessageSource getMessageSource(String[] basenames,boolean useCodeAsDefaultMessage)
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
		return getMessageSource(ret.toString(),useCodeAsDefaultMessage);
	}

}
