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

import javax.servlet.http.HttpServletRequest;

import org.frameworkset.security.session.Session;
import org.frameworkset.security.session.SessionBasicInfo;
import org.frameworkset.security.session.domain.CrossDomain;
import org.frameworkset.security.session.statics.NullSessionStaticManagerImpl;
import org.frameworkset.security.session.statics.SessionStaticManager;
import org.frameworkset.soa.ObjectSerializable;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;

/**
 * <p>Title: SessionHelper.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2014年4月30日
 * @author biaoping.yin
 * @version 3.8.0
 */
public class SessionHelper {
	private static SessionManager sessionManager;
	private static SessionStaticManager sessionStaticManager;
	
	static {
		BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("sessionconf.xml");
		sessionManager = context.getTBeanObject("sessionManager", SessionManager.class);
		if(!sessionManager.usewebsession())
			sessionStaticManager = context.getTBeanObject("sessionStaticManager", SessionStaticManager.class);
		else
			sessionStaticManager = new NullSessionStaticManagerImpl();
	}
	

	public static void destroy() {
		sessionManager = null;
		
	}


	public static SessionManager getSessionManager() {
		return sessionManager;
	}
	
	public static SessionStaticManager getSessionStaticManager()
	{
		return sessionStaticManager;
	}
	
	public static Session createSession(SessionBasicInfo sessionBasicInfo)
	{
		Session session = sessionManager.getSessionStore().createSession( sessionBasicInfo);
		
		return session;
	}
	public static void dispatchEvent(SessionEventImpl sessionEvent) 
	{
		sessionManager.dispatchEvent(sessionEvent);
	}
	
	public static boolean haveSessionListener() 
	{
		return sessionManager.haveSessionListener();
	}

	public static Session getSession(String appkey,String contextPath, String sessionid) {
		// TODO Auto-generated method stub
		return sessionManager.getSessionStore().getSession(appkey,contextPath, sessionid);
	}
	

	public static Object serial(Object value)
	{
		if(value != null)
		{
			try {
				value = ObjectSerializable.toXML(value);
//				value = new String(((String)value).getBytes(Charset.defaultCharset()),"UTF-8");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return value;
	}
	
	public static Object unserial(String value)
	{
		if(value == null)
			return null;
		return ObjectSerializable.toBean(value, Object.class);
	}
	
	public static String wraperAttributeName(String appkey,String contextpath, String attribute)
	{
		CrossDomain crossDomain = sessionManager.getCrossDomain();
		if(crossDomain == null)
			return attribute;
		return crossDomain.wraperAttributeName(appkey, contextpath, attribute);
	}
	
	public static String dewraperAttributeName(String appkey,String contextpath, String attribute)
	{
		CrossDomain crossDomain = sessionManager.getCrossDomain();
		if(crossDomain == null)
			return attribute;
		return crossDomain.dewraperAttributeName(appkey, contextpath, attribute);
	}
	public static String getAppKey(HttpServletRequest request)
	{
		String appcode = getSessionManager().getAppcode();
		if(appcode != null)
		{
			return appcode;
		}
		if(request != null)
		{
			String appKey = request.getContextPath().replace("/", "");
			if(appKey.equals(""))
				appKey = "ROOT";
			return appKey;
		}
		return null;
		
	}
	
	public static boolean hasMonitorPermission(String app, HttpServletRequest request)
	{
		return getSessionStaticManager().hasMonitorPermission(app, request);
	}
	
	public static boolean hasDeleteAppPermission(String app, HttpServletRequest request)
	{
		return getSessionStaticManager().hasMonitorPermission(app, request);
	}
	
	public static boolean deleteApp(String app) throws Exception
	{
		return getSessionStaticManager().deleteApp(app);
	}
	
	public static boolean isMonitorAll() throws Exception
	{
		return getSessionStaticManager().isMonitorAll();
	}
}
