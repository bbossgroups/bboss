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
import org.frameworkset.security.session.SessionEvent;
import org.frameworkset.security.session.SessionListener;
import org.frameworkset.security.session.SessionStore;
import org.frameworkset.security.session.domain.CrossDomain;

import com.frameworkset.util.StringUtil;


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
	private Object sessionstore;
	private CrossDomain crossDomain;
	private boolean startLifeScan = false;
	/**
	 * session监听器,多个用,号分隔
	 */
	private String sessionlisteners;
	private boolean httpOnly;
	private boolean secure;
	private String domain;
	/**
	 * 应用编码，如果没有指定appcode值默认为应用上下文
	 * appcode的作用：当所有的应用上下文为/时，用来区分后台统计的会话信息
	 */
	private String appcode;
	private long cookieLiveTime;
	private SessionStore sessionStore;
	private SessionMonitor	sessionMonitor;
	private List<SessionListener> sessionListeners;
	
	/**
	 * session超时检测时间间隔，默认为-1，不检测
	 * 如果需要检测，那么只要令牌持续时间超过sessionscaninterval
	 * 对应的时间将会被清除
	 */
	private long sessionscaninterval = 5*60000;
	private boolean usewebsession = false;
	public SessionManager()
	{
		
	}
	public SessionManager(long sessionTimeout, Object sessionStore,
			String cookiename, boolean httponly,
			long cookieLiveTime,String listeners) {
		this.sessionTimeout = sessionTimeout;
		this.sessionStore = SessionStoreFactory.getSessionStore(sessionStore,this);
		if(this.sessionStore == null)
			this.usewebsession = true;
		this.cookiename = cookiename;
		this.httpOnly = httponly;
		this.cookieLiveTime = cookieLiveTime;
		if(!StringUtil.isEmpty(listeners))
		{
			String[] temp = listeners.trim().split("\\,");
			initSessionListeners(temp);
		}
		if(!usewebsession && startLifeScan)
		{
			 log.debug("Session life scan monitor start.");
			sessionMonitor = new SessionMonitor();
			sessionMonitor.start();
		}
	}
	
	public void init()
	{
		this.sessionStore = SessionStoreFactory.getSessionStore(sessionstore,this);
		if(this.sessionStore == null)
			this.usewebsession = true;
		if(!StringUtil.isEmpty(this.sessionlisteners))
		{
			String[] temp = sessionlisteners.trim().split("\\,");
			initSessionListeners(temp);
		}
		if(!usewebsession&&startLifeScan)
		{
			 log.debug("Session life scan monitor start.");
			sessionMonitor = new SessionMonitor();
			sessionMonitor.start();
		}
	}
	public boolean usewebsession()
	{
		return this.usewebsession;
	}
	private void initSessionListeners(String[] listeners)
	{
		sessionListeners = new ArrayList<SessionListener>();
		for(int i = 0; listeners != null && i < listeners.length; i ++)
		{
			try {
				SessionListener l = (SessionListener)Class.forName(listeners[i].trim()).newInstance();
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

	public void destroy() {
		if(sessionStore != null)
			this.sessionStore.destory();
		if(this.sessionMonitor != null)
		{
			this.sessionMonitor.killdown();
		}
		
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
				log.debug("过期会话清理开始....");
				try {
					sessionStore.livecheck();
				} catch (Exception e1) {
					log.debug("过期会话扫描异常：",e1);
				}
				log.debug("过期会话清理结束.");
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

	public long getSessionTimeout() {
		return sessionTimeout;
	}
	public String getCookiename() {
		return cookiename;
	}
	public boolean isHttpOnly() {
		return httpOnly;
	}
	public long getCookieLiveTime() {
		return cookieLiveTime;
	}
	public List<SessionListener> getSessionListeners() {
		return sessionListeners;
	}
	public long getSessionscaninterval() {
		return sessionscaninterval;
	}
	public boolean haveSessionListener()
	{
		return this.sessionListeners != null && this.sessionListeners.size() > 0;
	}
	public void dispatchEvent(SessionEventImpl sessionEvent) {
		for(int i = 0; sessionListeners != null && i < this.sessionListeners.size(); i ++)
		{
			
				switch(sessionEvent.getEventType())
				{
					case SessionEvent.EventType_create:
						try {
							sessionListeners.get(i).createSession(sessionEvent);
						} catch (Exception e) {
							log.error("",e);
						}
						break;
					case SessionEvent.EventType_destroy:
						try {
							sessionListeners.get(i).destroySession(sessionEvent);
						} catch (Exception e) {
							log.error("",e);
						}
						break;
					case SessionEvent.EventType_addAttibute:
						try {
							sessionListeners.get(i).addAttribute(sessionEvent);
						} catch (Exception e) {
							log.error("",e);
						}
						break;
					case SessionEvent.EventType_removeAttibute:
						try {
							sessionListeners.get(i).removeAttribute(sessionEvent);
						} catch (Exception e) {
							log.error("",e);
						}
						break;
					default:
						throw new RuntimeException("未知的事务类型："+sessionEvent.getEventType());
				}
			
		}
		
	}
	
	
	public String getSessionlisteners() {
		return sessionlisteners;
	}
	public void setSessionlisteners(String sessionlisteners) {
		this.sessionlisteners = sessionlisteners;
	}
	public void setSessionTimeout(long sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}
	public void setCookiename(String cookiename) {
		this.cookiename = cookiename;
	}
	public void setSessionstore(Object sessionstore) {
		this.sessionstore = sessionstore;
	}
	public void setHttpOnly(boolean httpOnly) {
		this.httpOnly = httpOnly;
	}
	public void setCookieLiveTime(long cookieLiveTime) {
		this.cookieLiveTime = cookieLiveTime;
	}
	public void setSessionscaninterval(long sessionscaninterval) {
		this.sessionscaninterval = sessionscaninterval;
	}
	public boolean isSecure() {
		return secure;
	}
	public void setSecure(boolean secure) {
		this.secure = secure;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getAppcode() {
		return appcode;
	}
	public void setAppcode(String appcode) {
		this.appcode = appcode;
	}
	public CrossDomain getCrossDomain() {
		return crossDomain;
	}
	public void setCrossDomain(CrossDomain crossDomain) {
		this.crossDomain = crossDomain;
	}
	public boolean isStartLifeScan() {
		return startLifeScan;
	}
	public void setStartLifeScan(boolean startLifeScan) {
		this.startLifeScan = startLifeScan;
	}
}
