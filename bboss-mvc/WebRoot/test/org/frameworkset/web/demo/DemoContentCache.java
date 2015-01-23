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
package org.frameworkset.web.demo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.frameworkset.spi.DisposableBean;

import com.frameworkset.util.DaemonThread;
import com.frameworkset.util.FileUtil;
import com.frameworkset.util.ResourceInitial;
import com.frameworkset.util.StringUtil;


/**
 * <p>DemoContentCach.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2011-6-25
 * @author biaoping.yin
 * @version 1.0
 */
public class DemoContentCache implements org.frameworkset.spi.InitializingBean,DisposableBean
{
	private Map<String,String> democontentCache = new HashMap<String,String>();	
	private String getFileContent_(String path,String charset,boolean convertHtmlTag)
	{
		String content = "";
		try
		{
			if(convertHtmlTag)
				content = StringUtil.HTMLNoBREncode(FileUtil.getFileContent(path,charset));
			else
				content = FileUtil.getFileContent(path,charset);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}
	
	private Object lock = new Object();
	public String getFileContent(String path,String charset,boolean convertHtmlTag)
	{
		
		String key = path + "|" + charset + "|" + convertHtmlTag;
		String content = democontentCache.get(key);
		if(content != null)
			return content;
		else
		{
			synchronized(lock)
			{
				content = democontentCache.get(key);
				if(content != null)
					return content;
				content = getFileContent_(path,charset,convertHtmlTag);
				democontentCache.put(key, content);
				damon.addFile(path, new ResourceCacheRefresh(key ));
			}			
		}
		
		return content;
	}
	private void removecache(String cachekey)
	{
		synchronized(lock)
		{
			this.democontentCache.remove(cachekey);
		}
	}
	private DaemonThread damon = null; 
	private long refreshInterval= 10000;
	public static final String cacheobjectkey = "org.frameworkset.web.demo.DemoContentCache";
	
	 
	
	 class ResourceCacheRefresh implements ResourceInitial
	{
		private String cachekey;
		public ResourceCacheRefresh(String cachekey)
		{
			this.cachekey =cachekey;
		}
		public void reinit() {
			removecache(cachekey);
		}
		
	}



	public void afterPropertiesSet() throws Exception
	{
		damon = new DaemonThread(refreshInterval,"DEMO Refresh Monitor Worker"); 
		damon.start();
		
	}
	
	public long getRefreshInterval()
	{
	
		return refreshInterval;
	}
	
	public void setRefreshInterval(long refreshInterval)
	{
	
		this.refreshInterval = refreshInterval;
	}
	@Override
	public void destroy() throws Exception {
		if(damon != null)
		{
			damon.stopped();
		}
		
	}
	
	
}
