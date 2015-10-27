package org.frameworkset.cache;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.frameworkset.util.DaemonThread;
import com.frameworkset.util.FileUtil;
import com.frameworkset.util.ResourceInitial;
import com.frameworkset.util.StringUtil;
import com.frameworkset.util.UUIDResource;

public class FileContentCache  
{
	private static Logger log = Logger.getLogger(FileContentCache.class);
	private Map<String,String> democontentCache = new HashMap<String,String>();	
	public static final int HTMLNoBREncode  = 0;
	public static final int HTMLEncode  = 1;
	public static final int PLAINEncode  = 2;
	public static final int HTMLEncodej  = 3;
	private String _getFileContent(String path,String charset,int encodeType)
	{
		String content = "";
		try
		{
			if(encodeType == HTMLNoBREncode)
				content = StringUtil.HTMLNoBREncode(FileUtil.getFileContent(path,charset));
			else if(encodeType == HTMLEncode)
				content = StringUtil.HTMLEncode(FileUtil.getFileContent(path,charset));
			else  if(encodeType == HTMLEncodej)
				content = StringUtil.HTMLEncodej(FileUtil.getFileContent(path,charset));
			else				
				content = FileUtil.getFileContent(path,charset);
		}
		catch (IOException e)
		{
			log.error("Get File Content Error:path="+path+",charset="+charset+",convertHtmlTag="+encodeType,e);
		}
		return content;
	}
	
	private Object lock = new Object();
	public String getFileContent(String path,String charset,int encodeType)
	{
		
		String key = path + "|" + charset + "|" + encodeType;
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
				content = _getFileContent(path,charset, encodeType);
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
	private long refreshInterval= 5000;
	
	 
	
	 class ResourceCacheRefresh implements ResourceInitial,UUIDResource
	{
		private String cachekey;
		public ResourceCacheRefresh(String cachekey)
		{
			this.cachekey =cachekey;
		}
		public void reinit() {
			removecache(cachekey);
		}
		@Override
		public String getUUID() {
			
			return cachekey;
		}
		
	}



	public void start() throws Exception
	{
		damon = new DaemonThread(refreshInterval,"FileContentCache Refresh Monitor Worker"); 
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
	 
	public void destroy() throws Exception {
		if(damon != null)
		{
			damon.stopped();
		}
		
	}

}
