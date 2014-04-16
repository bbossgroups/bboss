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

import javax.servlet.http.HttpSession;

import org.frameworkset.security.session.Session;
import org.frameworkset.security.session.SessionStore;

/**
 * <p>Title: HttpSessionStore.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2014年4月15日
 * @author biaoping.yin
 * @version 3.8.0
 */
public class HttpSessionStore implements SessionStore {
	
	@Override
	public void destory() {		
		
	}
	
	public Session createSession(Object source)
	{
		if(source == null)
			return null;
		HttpSessionImpl session = new HttpSessionImpl();
		session.setSession((HttpSession)source);
		return session; 
	}

	@Override
	public void livecheck() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getAttribute(String appKey,String sessionID, String attribute) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration getAttributeNames(String appKey,String sessionID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateLastAccessedTime(String appKey,String sessionID, long lastAccessedTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getLastAccessedTime(String appKey,String sessionID) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String[] getValueNames(String appKey,String sessionID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void invalidate(String appKey,String sessionID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isNew(String appKey,String sessionID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeAttribute(String appKey,String sessionID, String attribute) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAttribute(String appKey,String sessionID, String attribute, Object value) {
		// TODO Auto-generated method stub
		
	}

}
