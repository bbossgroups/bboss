/*
 *  Copyright 2008 bbossgroups
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
package org.frameworkset.security.session.impl;

import org.frameworkset.spi.BaseApplicationContext;


/**
 * <p>Title: SessionManagerFactory.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2014年4月15日
 * @author biaoping.yin
 * @version 3.8.0
 */
public class SessionManagerFactory {
	private static SessionManager sessionManager;
	static 
	{
		BaseApplicationContext.addShutdownHook(new Runnable(){

			@Override
			public void run() {
				destory();
			}
			
		});
	}
	
	static void destory()
	{
		if(sessionManager != null)
		{
			sessionManager.destory();
			sessionManager = null;
		}
	}
	
	public static SessionManager getSessionManager()
	{
		if(sessionManager == null)
		{
			throw new RuntimeException("SessionManager not build corrected using getSessionManager(String sessionstore,long sessionTimeout,String cookiename,boolean httpOnly,long cookieLiveTime) or getSessionManager(String sessionstore,long sessionTimeout)");
		}
		return sessionManager;
	}
	
	public static SessionManager getSessionManager(String sessionstore,long sessionTimeout)
	{
		if(sessionManager == null)
		{
			synchronized(SessionManager.class)
			{
				if(sessionManager == null)
					sessionManager = new SessionManager(sessionTimeout,sessionstore,SessionManager.default_cookiename,SessionManager.default_httpOnly, SessionManager.default_cookieLiveTime);
			}
		}
		return sessionManager;
	}
	
	public static SessionManager getSessionManager(String sessionstore,long sessionTimeout,String cookiename,boolean httpOnly,long cookieLiveTime)
	{
		if(sessionManager == null)
		{
			synchronized(SessionManager.class)
			{
				if(sessionManager == null)
					sessionManager = new SessionManager(sessionTimeout,sessionstore,cookiename,httpOnly, cookieLiveTime);
			}
		}
		return sessionManager;
	}
}
