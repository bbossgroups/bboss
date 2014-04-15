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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.frameworkset.security.session.SessionListener;
import org.frameworkset.security.session.SessionStore;


/**
 * <p>Title: SessionManager.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2014年4月15日
 * @author biaoping.yin
 * @version 3.8.0
 */
public class SessionManager {
	private static Logger log = Logger.getLogger(SessionManager.class);
	public static final String default_cookiename = "B_SESSIONID";
	public static final boolean default_httpOnly = true;
	public static final long default_cookieLiveTime = -1l;
	private long sessionTimeout;
	private String cookiename;
	private boolean httpOnly;
	private long cookieLiveTime;
	private String sessionStore_;
	private SessionStore sessionStore;
	private SessionMonitor	sessionMonitor;
	private List<SessionListener> sessionListeners;
	/**
	 * 令牌超时检测时间间隔，默认为-1，不检测
	 * 如果需要检测，那么只要令牌持续时间超过tokendualtime
	 * 对应的时间将会被清除
	 */
	private long sessionscaninterval = 1800000;
	public SessionManager(long sessionTimeout, String sessionStore,
			String cookiename, boolean httponly,
			long cookieLiveTime,String[] listeners) {
		this.sessionTimeout = sessionTimeout;
		this.sessionStore_ = sessionStore;
		this.sessionStore = SessionStoreFactory.getTokenStore(sessionStore_);
		this.cookiename = cookiename;
		this.httpOnly = httponly;
		this.cookieLiveTime = cookieLiveTime;
		initSessionListeners(listeners);
		sessionMonitor = new SessionMonitor();
		sessionMonitor.start();
	}
	private void initSessionListeners(String[] listeners)
	{
		sessionListeners = new ArrayList<SessionListener>();
		for(int i = 0; listeners != null && i < listeners.length; i ++)
		{
			try {
				SessionListener l = (SessionListener)Class.forName(listeners[i]).newInstance();
				sessionListeners.add(l);
			} catch (InstantiationException e) {
				throw new SessionManagerException(e);
			} catch (IllegalAccessException e) {
				throw new SessionManagerException(e);
			} catch (ClassNotFoundException e) {
				throw new SessionManagerException(e);
			}
		}
	}

	public SessionStore getSessionStore()
	{
		return sessionStore;
	}

	public void destory() {
		this.sessionStore.destory();
		
	}
	
	class SessionMonitor extends Thread
	{
		public SessionMonitor()
		{
			super("Session Scan Thread.");
		}
		private boolean killdown = false;
		public void start()
		{
			super.start();
		}
		public void killdown() {
			killdown = true;
			synchronized(this)
			{
				this.notifyAll();
			}
			
		}
		@Override
		public void run() {
			while(!killdown)
			{
				
//				check();
				log.debug("过期令牌清理开始....");
				try {
					sessionStore.livecheck();
				} catch (Exception e1) {
					log.debug("过期令牌扫描异常：",e1);
				}
				log.debug("过期令牌清理结束.");
				synchronized(this)
				{
					try {
						
						this.wait(sessionscaninterval);
					} catch (InterruptedException e) {
						break;
					}
				}
				if(killdown)
					break;
			}
		}
		public boolean isKilldown() {
			return killdown;
		}
		
	}

}
