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

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.frameworkset.security.session.Session;
import org.frameworkset.security.session.SessionBasicInfo;
import org.frameworkset.security.session.SessionEvent;
import org.frameworkset.security.session.SessionStore;
import org.frameworkset.security.session.statics.SessionConfig;

/**
 * <p>Title: DelegateSessionStrore.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2014年5月6日
 * @author biaoping.yin
 * @version 3.8.0
 */
public class DelegateSessionStore implements SessionStore {
	private SessionStore sessionStore;
	public DelegateSessionStore(SessionStore sessionStore)
	{
		this.sessionStore = sessionStore;
	}
	@Override
	public void destory() {
		sessionStore.destory();

	}

	@Override
	public void livecheck() {
		sessionStore.livecheck();

	}
	public HttpSession createHttpSession(ServletContext servletContext,SessionBasicInfo sessionBasicInfo,String contextpath)
	{
		Session session = createSession(sessionBasicInfo);
		HttpSession httpsession = new HttpSessionImpl(session,servletContext,contextpath);
		if(SessionHelper.haveSessionListener())
		{
			SessionHelper.dispatchEvent(new SessionEventImpl(httpsession,SessionEvent.EventType_create));
		}
		return httpsession;
	}
	@Override
	public Session createSession(SessionBasicInfo sessionBasicInfo) {
		// TODO Auto-generated method stub
		Session session = sessionStore.createSession( sessionBasicInfo);
		if(session == null)
			return null;
		session._setSessionStore(this);
		session.putNewStatus();
		
		return session;
	}

	@Override
	public Object getAttribute(String appKey,String contextpath, String sessionID, String attribute) {
		// TODO Auto-generated method stub
		String _attribute = SessionHelper.wraperAttributeName(appKey,contextpath,  attribute);
		return sessionStore.getAttribute(appKey, contextpath, sessionID, _attribute);
	}

	@Override
	public Enumeration getAttributeNames(String appKey,String contextpath, String sessionID) {
		// TODO Auto-generated method stub
		return this.sessionStore.getAttributeNames(appKey, contextpath, sessionID);
	}

	@Override
	public void updateLastAccessedTime(String appKey, String sessionID,
			long lastAccessedTime,String lastAccessedUrl) {
		this.sessionStore.updateLastAccessedTime(appKey, sessionID, lastAccessedTime, lastAccessedUrl);

	}

	@Override
	public long getLastAccessedTime(String appKey, String sessionID) {
		// TODO Auto-generated method stub
		return this.sessionStore.getLastAccessedTime(appKey, sessionID);
	}

	@Override
	public String[] getValueNames(String appKey,String contextpath, String sessionID) {
		// TODO Auto-generated method stub
		return this.sessionStore.getValueNames(appKey, contextpath, sessionID);
	}

	@Override
	public void invalidate(HttpSession session,String appKey,String contextpath, String sessionID) {
		if(SessionHelper.haveSessionListener())
		{
			SessionHelper.dispatchEvent(new SessionEventImpl(session,SessionEvent.EventType_destroy));
		}
		this.sessionStore.invalidate(session,appKey, contextpath, sessionID);
		
		
		
		
	}

	@Override
	public boolean isNew(String appKey, String sessionID) {
		// TODO Auto-generated method stub
		return this.sessionStore.isNew(appKey, sessionID);
	}

	@Override
	public void removeAttribute(HttpSession session,String appKey,String contextpath, String sessionID,
			String attribute) {
		if(SessionHelper.haveSessionListener())
		{
			SessionHelper.dispatchEvent(new SessionEventImpl(session,SessionEvent.EventType_removeAttibute)
										.setAttributeName(attribute));
		}
		String _attribute = SessionHelper.wraperAttributeName(appKey,contextpath,  attribute);
		this.sessionStore.removeAttribute(  session,appKey, contextpath, sessionID, _attribute);		
		
		
		
	}

	@Override
	public void addAttribute(HttpSession session,String appKey,String contextpath, String sessionID, String attribute,
			Object value) {
		
		value = SessionHelper.serial(value);
		String _attribute = SessionHelper.wraperAttributeName(appKey,contextpath,  attribute);
		 this.sessionStore.addAttribute(  session,appKey, contextpath, sessionID, _attribute, value);
		 
		
		if(SessionHelper.haveSessionListener())
		{
			SessionHelper.dispatchEvent(new SessionEventImpl(session,SessionEvent.EventType_addAttibute)
										.setAttributeName(attribute));
		}
		
	}

	@Override
	public void setSessionManager(SessionManager sessionManager) {
		this.sessionStore.setSessionManager(sessionManager);
		

	}

	@Override
	public Session getSession(String appKey,String contextpath, String sessionid) {
		// TODO Auto-generated method stub
		Session session = this.sessionStore.getSession(appKey, contextpath, sessionid);
		if(session != null)
			session._setSessionStore(this);
		return session;
	}
	@Override
	public void setMaxInactiveInterval(HttpSession session, String appKey, String id, long maxInactiveInterval,
			String contextpath) {
		sessionStore.setMaxInactiveInterval(session, appKey, id, maxInactiveInterval, contextpath);
		
	}
	@Override
	public void saveSessionConfig(SessionConfig config) {
		sessionStore.saveSessionConfig(config);
		
	}
	@Override
	public SessionConfig getSessionConfig(String appkey) {
		// TODO Auto-generated method stub
		return sessionStore.getSessionConfig(appkey);
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.sessionStore.getName();
	}
	
	

}
