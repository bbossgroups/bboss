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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.frameworkset.security.session.Session;

import com.frameworkset.util.StringUtil;

/**
 * <p>Title: SessionHttpServletRequestWrapper.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2014年4月30日
 * @author biaoping.yin
 * @version 3.8.0
 */
public class SessionHttpServletRequestWrapper extends HttpServletRequestWrapper {
	private String sessionid;
	private HttpSessionImpl session;
	private HttpServletResponse response;
	private ServletContext servletContext;	
	public SessionHttpServletRequestWrapper(HttpServletRequest request,HttpServletResponse response,ServletContext servletContext) {
		super(request);
		sessionid = StringUtil.getCookieValue((HttpServletRequest)request, SessionHelper.getSessionManager().getCookiename());
		this.servletContext = servletContext;
		this.response = response;
	}

	@Override
	public HttpSession getSession() {
		
		 return getSession(true);
	}

	@Override
	public HttpSession getSession(boolean create) {
		if( SessionHelper.getSessionManager().usewebsession())
		{
			// TODO Auto-generated method stub
			return super.getSession();
		}
		if(sessionid == null)
		{
			if(create)
			{
//				int cookielivetime = (int)SessionHelper.getSessionManager().getSessionTimeout();
//				if(cookielivetime <= 0)
//				{
//					cookielivetime = Integer.MAX_VALUE;
//				}
				int cookielivetime = -1;
				Session session = SessionHelper.createSession(this.getContextPath().replace("/", ""),StringUtil.getClientIP(this),this.getRequestURI());				
				sessionid = session.getId();
				this.session = new HttpSessionImpl(session,servletContext);
				StringUtil.addCookieValue(this, response, SessionHelper.getSessionManager().getCookiename(), sessionid, cookielivetime);
				return this.session;
			}
			else
			{
				return null;
			}
		}
		else if(session != null)
		{
			return session;
		}
		else
		{
			Session session = SessionHelper.getSession(this.getContextPath().replace("/", ""),sessionid);
			if(session == null)//session不存在，创建新的session
			{				
				if(create)
				{
//					int cookielivetime = (int)SessionHelper.getSessionManager().getSessionTimeout();
//					if(cookielivetime <= 0)
//					{
//						cookielivetime = Integer.MAX_VALUE;
//					}
					int cookielivetime = -1;
					session = SessionHelper.createSession(this.getContextPath().replace("/", ""),StringUtil.getClientIP(this),this.getRequestURI());
					
					sessionid = session.getId();
					this.session =  new HttpSessionImpl(session,servletContext);
					StringUtil.addCookieValue(this, response, SessionHelper.getSessionManager().getCookiename(), sessionid, cookielivetime);
				}
			}
			else
			{
				this.session =  new HttpSessionImpl(session,servletContext);
			}
			return this.session;
		}
		
		
	}

	public void touch() {
		if( SessionHelper.getSessionManager().usewebsession())
			return;
		if(this.sessionid != null )
		{
			if(session == null)
			{
				Session session_ = SessionHelper.getSession(this.getContextPath().replace("/", ""), sessionid);
				if(session_ == null || !session_.isValidate())
					return;
				this.session =  new HttpSessionImpl(session_,servletContext);
			}
			if(session != null && !session.isNew() )
			{
				session.touch(this.getRequestURI());
			}
		}
		
	}

}
