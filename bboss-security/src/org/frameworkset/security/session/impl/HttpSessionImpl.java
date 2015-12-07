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
import javax.servlet.http.HttpSessionContext;

import org.frameworkset.security.session.Session;

/**
 * <p>Title: HttpSessionImpl.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2014年4月15日
 * @author biaoping.yin
 * @version 3.8.0
 */
public class HttpSessionImpl implements HttpSession {
	private Session session = null;
	private ServletContext servletContext;
	private String contextpath;
	public HttpSessionImpl(Session session,ServletContext servletContext,String contextpath)
	{
		this.session = session;
		this.servletContext = servletContext;
		this.contextpath = contextpath;
	}
	@Override
	public Object getAttribute(String attribute) {
		// TODO Auto-generated method stub
		return session.getAttribute(this,attribute,contextpath);
	}

	@Override
	public Enumeration getAttributeNames() {
		// TODO Auto-generated method stub
		return this.session.getAttributeNames(this,contextpath);
	}

	@Override
	public long getCreationTime() {
		// TODO Auto-generated method stub
		return this.session.getCreationTime();
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return this.session.getId();
	}

	
	public void touch(String lastAccessedUrl) {
		session.touch(this, lastAccessedUrl,contextpath);

	}
	
	

	@Override
	public long getLastAccessedTime() {
		// TODO Auto-generated method stub
		return this.session.getLastAccessedTime();
	}

	
	public int getMaxInactiveInterval() {
		
		return (int)session.getMaxInactiveInterval();
	}

	@Override
	public Object getValue(String attribute) {
		
		return this.session.getValue(this,attribute,contextpath);
	}

	@Override
	public String[] getValueNames() {
		// TODO Auto-generated method stub
		return session.getValueNames(this,contextpath);
	}

	@Override
	public void invalidate() {
		this.session.invalidate(this,contextpath);

	}

	@Override
	public boolean isNew() {
		
		return session.isNew();
	}

	@Override
	public void putValue(String attribute, Object value) {
		session.putValue(this,attribute, value,contextpath);

	}

	@Override
	public void removeAttribute(String attribute) {
		session.removeAttribute(this,attribute,contextpath);

	}

	@Override
	public void removeValue(String attribute) {
		session.removeValue(this,attribute,contextpath);

	}

	@Override
	public void setAttribute(String attribute, Object value) {
		session.setAttribute(this,attribute, value,contextpath);

	}
//
//	@Override
//	public void setMaxInactiveInterval(long maxInactiveInterval) {
//		session.setMaxInactiveInterval((int)maxInactiveInterval);
//
//	}

	
	
	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return this.servletContext;
	}

	
	public HttpSessionContext getSessionContext() {
		return new SimpleHttpSessionContext();
	}

	@Override
	public void setMaxInactiveInterval(int maxInactiveInterval) {
		this.session.setMaxInactiveInterval(this,maxInactiveInterval,true,  contextpath);
		
	}
	
	public Session getInnerSession()
	{
		return this.session;
	}

}
