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

/**
 * <p>Title: SessionImpl.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2014年4月15日
 * @author biaoping.yin
 * @version 3.8.0
 */
public class SessionImpl implements Session {
	private String sessionID;
	private String sessionCookieName;
	private long creationTime;
	private long lastAccessedTime;
	private long maxInactiveInterval;
	@Override
	public Object getAttribute(String attribute) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration getAttributeNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getCreationTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getLastAccessedTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getMaxInactiveInterval() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getValue(String attribute) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getValueNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void invalidate() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void putValue(String attribute, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeAttribute(String attribute) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeValue(String attribute) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAttribute(String attribute, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMaxInactiveInterval(long maxInactiveInterval) {
		// TODO Auto-generated method stub

	}

	@Override
	public void touch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getReferip() {
		// TODO Auto-generated method stub
		return null;
	}

}
