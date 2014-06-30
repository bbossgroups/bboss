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

import java.util.UUID;

import org.frameworkset.security.session.Session;
import org.frameworkset.security.session.SessionBasicInfo;
import org.frameworkset.security.session.SessionStore;

/**
 * <p>Title: BaseSessionStore.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2014年4月15日
 * @author biaoping.yin
 * @version 3.8.0
 */
public abstract class BaseSessionStore implements SessionStore {
	
	protected SessionManager sessionManager;
	protected String randomToken()
	{
		String token = UUID.randomUUID().toString();
		return token;
	}
	@Override
	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public SessionManager getSessionManager() {
		return sessionManager;
	}
	@Override
	public void destory() {
		// TODO Auto-generated method stub

	}
	public abstract Session createSession(SessionBasicInfo sessionBasicInfo);
	public long getSessionTimeout() {
		return sessionManager.getSessionTimeout();
	}
	
	public String getCookiename() {
		return sessionManager.getCookiename();
	}
	
	public boolean isHttpOnly() {
		return sessionManager.isHttpOnly();
	}
	
	public boolean isSecure() {
		return sessionManager.isSecure();
	}
	public String getDomain() {
		return sessionManager.getDomain();
	}
	public long getCookieLiveTime() {
		return sessionManager.getCookieLiveTime();
	}
	

}
