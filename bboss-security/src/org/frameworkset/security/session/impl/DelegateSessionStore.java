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

import org.frameworkset.security.session.Session;
import org.frameworkset.security.session.SessionEvent;
import org.frameworkset.security.session.SessionStore;

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

	@Override
	public Session createSession(String appKey, String referip) {
		// TODO Auto-generated method stub
		Session session = sessionStore.createSession(appKey, referip);
		SessionHelper.dispatchEvent(new SessionEventImpl(session,SessionEvent.EventType_create));
		return session;
	}

	@Override
	public Object getAttribute(String appKey, String sessionID, String attribute) {
		// TODO Auto-generated method stub
		return sessionStore.getAttribute(appKey, sessionID, attribute);
	}

	@Override
	public Enumeration getAttributeNames(String appKey, String sessionID) {
		// TODO Auto-generated method stub
		return this.sessionStore.getAttributeNames(appKey, sessionID);
	}

	@Override
	public void updateLastAccessedTime(String appKey, String sessionID,
			long lastAccessedTime) {
		this.sessionStore.updateLastAccessedTime(appKey, sessionID, lastAccessedTime);

	}

	@Override
	public long getLastAccessedTime(String appKey, String sessionID) {
		// TODO Auto-generated method stub
		return this.sessionStore.getLastAccessedTime(appKey, sessionID);
	}

	@Override
	public String[] getValueNames(String appKey, String sessionID) {
		// TODO Auto-generated method stub
		return this.sessionStore.getValueNames(appKey, sessionID);
	}

	@Override
	public void invalidate(String appKey, String sessionID) {
		this.sessionStore.invalidate(appKey, sessionID);

	}

	@Override
	public boolean isNew(String appKey, String sessionID) {
		// TODO Auto-generated method stub
		return this.sessionStore.isNew(appKey, sessionID);
	}

	@Override
	public void removeAttribute(String appKey, String sessionID,
			String attribute) {
		this.sessionStore.removeAttribute(appKey, sessionID, attribute);

	}

	@Override
	public void addAttribute(String appKey, String sessionID, String attribute,
			Object value) {
		this.sessionStore.addAttribute(appKey, sessionID, attribute, value);

	}

	@Override
	public void setSessionManager(SessionManager sessionManager) {
		this.sessionStore.setSessionManager(sessionManager);
		

	}

	@Override
	public Session getSession(String appKey, String sessionid) {
		// TODO Auto-generated method stub
		return this.sessionStore.getSession(appKey, sessionid);
	}

}
