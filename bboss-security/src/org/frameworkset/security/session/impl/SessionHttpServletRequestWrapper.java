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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.frameworkset.security.session.Session;
import org.frameworkset.security.session.SessionBasicInfo;
import org.frameworkset.security.session.domain.App;
import org.frameworkset.security.session.domain.CrossDomain;

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

				String appkey = SessionHelper.getAppKey(this);
				SessionBasicInfo sessionBasicInfo = new SessionBasicInfo();
				sessionBasicInfo.setAppKey(appkey);
				sessionBasicInfo.setReferip(StringUtil.getClientIP(this));
				sessionBasicInfo.setRequesturi(this.getRequestURI());
				
				Session session = SessionHelper.createSession(sessionBasicInfo);				
				sessionid = session.getId();
				this.session = new HttpSessionImpl(session,servletContext,this.getContextPath());

				writeCookies( );
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
			String appkey =  SessionHelper.getAppKey(this);

			Session session = SessionHelper.getSession(appkey,this.getContextPath(),sessionid);
			if(session == null)//session不存在，创建新的session
			{				
				if(create)
				{

				
					
					SessionBasicInfo sessionBasicInfo = new SessionBasicInfo();
					sessionBasicInfo.setAppKey(appkey);
					sessionBasicInfo.setReferip(StringUtil.getClientIP(this));
					sessionBasicInfo.setRequesturi(this.getRequestURI());
					
					session = SessionHelper.createSession(sessionBasicInfo);
					sessionid = session.getId();
					this.session =  new HttpSessionImpl(session,servletContext,this.getContextPath());

					writeCookies( );
				}
			}
			else
			{
				this.session =  new HttpSessionImpl(session,servletContext,this.getContextPath());
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
				String appkey =  SessionHelper.getAppKey(this);
				Session session_ = SessionHelper.getSession(appkey,this.getContextPath(), sessionid);
				if(session_ == null || !session_.isValidate())
				{
					this.sessionid = null;
					return;
				}
				this.session =  new HttpSessionImpl(session_,servletContext,this.getContextPath());
			}
			if(session != null && !session.isNew() )
			{
				session.touch(this.getRequestURI());
			}
		}
		
	}
	private static Object dummy = new Object();
	private void writeCookies( )
	{
		int cookielivetime = -1;
		CrossDomain crossDomain = SessionHelper.getSessionManager().getCrossDomain() ;
		if(crossDomain == null)
		{
			boolean secure = SessionHelper.getSessionManager().isSecure();
			if(!this.isSecure())
				secure = false;
			StringUtil.addCookieValue(this, response, SessionHelper.getSessionManager().getCookiename(), sessionid, cookielivetime,SessionHelper.getSessionManager().isHttpOnly(),
					secure,SessionHelper.getSessionManager().getDomain());
		}
		else
		{
			List<App> apps = crossDomain.getDomainApps();
			if(crossDomain.get_paths() != null)
			{
				boolean secure = SessionHelper.getSessionManager().isSecure();
				if(!this.isSecure())
					secure = false;
				for(String path:crossDomain.get_paths())
				{
					StringUtil.addCookieValue(this, path,
												response, 
												SessionHelper.getSessionManager().getCookiename(), 
												sessionid, cookielivetime,
												SessionHelper.getSessionManager().isHttpOnly(),								
												secure,
												crossDomain.getRootDomain());
				}
			}
			else
			{
				boolean secure = SessionHelper.getSessionManager().isSecure();
				if(!this.isSecure())
					secure = false;
				Map<String,Object> setted = new HashMap<String,Object>();
				for(App app:apps)
				{
					if(app.getPath() == null)
					{
						StringUtil.addCookieValue(this, response, SessionHelper.getSessionManager().getCookiename(), sessionid, cookielivetime,SessionHelper.getSessionManager().isHttpOnly(),
								secure,crossDomain.getRootDomain());
					}
					else
					{
						if(!setted.containsKey(app.getPath()))
						{
							StringUtil.addCookieValue(this, app.getPath(),response, SessionHelper.getSessionManager().getCookiename(), sessionid, cookielivetime,SessionHelper.getSessionManager().isHttpOnly(),								
									secure,crossDomain.getRootDomain());
							setted.put(app.getPath(), dummy);
						}
						else
						{
							
						}
						
						
					}
				}
				setted = null;
			}
			
		}
	}

	@Override
	public String getRequestedSessionId() {
		if( SessionHelper.getSessionManager().usewebsession())
		{
			return super.getRequestedSessionId();
		}
		if(this.sessionid != null)
			return sessionid;
		HttpSession session = this.getSession(false);
		if(session == null)
			return null;
		else
			return session.getId();
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		if( SessionHelper.getSessionManager().usewebsession())
		{
			return super.isRequestedSessionIdFromCookie();
		}
		return true;
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		if( SessionHelper.getSessionManager().usewebsession())
		{
			return super.isRequestedSessionIdFromURL();
		}
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {
		if( SessionHelper.getSessionManager().usewebsession())
		{
			return super.isRequestedSessionIdFromUrl();
		}
		return false;
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		if( SessionHelper.getSessionManager().usewebsession())
		{
			return super.isRequestedSessionIdValid();
		}
		HttpSessionImpl session = (HttpSessionImpl)this.getSession(false);
		if(session == null)
			return false;
		else
			return session.getInnerSession().isValidate();
	}

}
