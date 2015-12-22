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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.frameworkset.security.session.Session;
import org.frameworkset.security.session.SessionBasicInfo;
import org.frameworkset.security.session.domain.CrossDomain;
import org.frameworkset.security.session.statics.AttributeInfo;
import org.frameworkset.security.session.statics.NullSessionStaticManagerImpl;
import org.frameworkset.security.session.statics.SessionConfig;
import org.frameworkset.security.session.statics.SessionStaticManager;
import org.frameworkset.soa.ObjectSerializable;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;

import com.frameworkset.util.SimpleStringUtil;
import com.frameworkset.util.StringUtil;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

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
	private static boolean inited = false;
	public static SessionConfig getSessionConfig(String appcode)
	{
		return sessionManager.getSessionConfig(appcode,true);
	}
	public static SessionConfig getSessionConfig(String appcode,boolean serialattributes)
	{
		return sessionManager.getSessionConfig(appcode, serialattributes);
	}
	public static void init(String contextpath){
		if(inited)
			return ;
		synchronized(SessionHelper.class)
		{
			if(inited)
				return ;
			try
			{
				BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("sessionconf.xml");
				SessionManager sessionManager = context.getTBeanObject("sessionManager", SessionManager.class);
				SessionStaticManager sessionStaticManager = null;
				if(!sessionManager.usewebsession())
					sessionStaticManager = context.getTBeanObject("sessionStaticManager", SessionStaticManager.class);
				else
					sessionStaticManager = new NullSessionStaticManagerImpl();
				sessionManager.initSessionConfig(contextpath);
				SessionHelper.sessionManager = sessionManager;
				SessionHelper.sessionStaticManager = sessionStaticManager;
			}
			finally
			{
				inited = true;
			}
		}
		
	}
	
	public static Object convertValue(String value,AttributeInfo attributeInfo)
	{
		if(attributeInfo.getType().equals("String"))
			return value;
		else if(attributeInfo.getType().equals("int"))
			return Integer.parseInt(value);
		else if(attributeInfo.getType().equals("long"))
			return Long.parseLong(value);
		else if(attributeInfo.getType().equals("double"))
			return Double.parseDouble(value);
		else if(attributeInfo.getType().equals("float"))
			return Float.parseFloat(value);
		else if(attributeInfo.getType().equals("boolean"))
			return Boolean.parseBoolean(value);
		return value;
	}
	
	public static Map<String,AttributeInfo> parserExtendAttributes(HttpServletRequest request,SessionConfig sessionConfig  )
	{
		AttributeInfo[] monitorAttributeArray = sessionConfig.getExtendAttributeInfos();
		if(monitorAttributeArray == null || monitorAttributeArray.length == 0)
		{
			return null;
		}
		Map<String,AttributeInfo>  datas = new HashMap<String,AttributeInfo>();
		for(AttributeInfo attributeInfo : monitorAttributeArray)
		{
			String value = request.getParameter(attributeInfo.getName());
			if(value != null )
			{
				
				if(value.trim().equals("")  )
				{
					if(attributeInfo.isEnableEmptyValue())
					{
						String enableEmptyValue = request.getParameter(attributeInfo.getName()+"_enableEmptyValue");
						if(enableEmptyValue !=  null)
						{
							try {
								attributeInfo = attributeInfo.clone();
								attributeInfo.setValue("");
								datas.put(attributeInfo.getName(), attributeInfo);
							} catch (CloneNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					
				}
				else
				{
					try {
						attributeInfo = attributeInfo.clone();
						attributeInfo.setValue(SessionHelper. convertValue(  value,  attributeInfo));
						datas.put(attributeInfo.getName(), attributeInfo);
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		}
		return datas;
	}
	
	public static void evalqueryfields(AttributeInfo[] monitorAttributeArray, Map keys)
	{
		 
		if(monitorAttributeArray != null && monitorAttributeArray.length > 0)
		{
			for(AttributeInfo attr:monitorAttributeArray)
			{
				keys.put(attr.getName(), 1);
				 
				
			}
			
			
		}
		
		
		
	}
	
	public static void buildExtendFieldQueryCondition(Map<String, AttributeInfo> monitorAttributeArray,  BasicDBObject query)
	{
		 
		if(monitorAttributeArray != null && monitorAttributeArray.size() > 0)
		{
			
			for(Entry<String, AttributeInfo> Entry:monitorAttributeArray.entrySet())
			{
				AttributeInfo  attr = Entry.getValue();
				if(attr.getType().equals("String"))
				{
					
					if(!attr.isLike())
					{
						if (!StringUtil.isEmpty((String)attr.getValue())) {
							Object value = serial(attr.getValue());
							query.append(attr.getName(), value);
						}
						else if(attr.isEnableEmptyValue())
						{
							BasicDBList values = new BasicDBList();
							values.add(new BasicDBObject(attr.getName(), serial("")));
							values.add(new BasicDBObject(attr.getName(), null));
							query.append("$or", values);
						}
						

						
					}
					else
					{
						if (!StringUtil.isEmpty((String)attr.getValue())) {
							Object value = attr.getValue();
							Pattern hosts = Pattern.compile("^<ps><p n=\"_dflt_\" s:t=\"String\"><\\!\\[CDATA\\[" + value + ".*$",
									Pattern.CASE_INSENSITIVE);
							query.append(attr.getName(), new BasicDBObject("$regex",hosts));
						}
						else if(attr.isEnableEmptyValue())
						{
							 
							//values.add(null);
						
							BasicDBList values = new BasicDBList();
							values.add(new BasicDBObject(attr.getName(), serial("")));
							values.add(new BasicDBObject(attr.getName(), null));
							query.append("$or", values);
							
							
						}
					}
				}
				else 
				{
					Object value = serial(attr.getValue());
					query.append(attr.getName(), value);
				}
				
			}
			
			
		}
		
		
		
	}
	public static List<AttributeInfo> evalqueryfiledsValue(AttributeInfo[] monitorAttributeArray, DBObject dbobject)  
	{
		List<AttributeInfo> extendAttrs = null;
		 
		if(monitorAttributeArray != null && monitorAttributeArray.length > 0)				
		{
			extendAttrs = new ArrayList<AttributeInfo>();
			AttributeInfo attrvalue = null;
			for(AttributeInfo attributeInfo:monitorAttributeArray)
			{
				try {
					attrvalue = attributeInfo.clone();
					String value = (String)dbobject.get(attrvalue.getName());
					attrvalue.setValue(unserial(  value));
					extendAttrs.add(attrvalue);
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		return extendAttrs;
		
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
	
	public static HttpSession createSession(ServletContext servletContext,SessionBasicInfo sessionBasicInfo,String contextpath)
	{
		HttpSession session = sessionManager.getSessionStore().createHttpSession(   servletContext,  sessionBasicInfo,  contextpath);
		
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
		 return getAppKeyFromRequest(request);
		
	}
	
	public static String getAppKeyFromRequest(HttpServletRequest request)
	{
//		String appcode = getSessionManager().getAppcode();
//		if(appcode != null)
//		{
//			return appcode;
//		}
		
		if(request != null)
		{
			String appKey = request.getContextPath().replace("/", "");
			if(appKey.equals(""))
				appKey = "ROOT";
			return appKey;
		}
		return null;
		
	}
	
	public static String getAppKeyFromServletContext(ServletContext context)
	{
//		String appcode = getSessionManager().getAppcode();
//		if(appcode != null)
//		{
//			return appcode;
//		}
		
		if(context != null)
		{
			
			String appKey = context.getContextPath().replace("/", "");
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
	/**
	 * 
	 * @param monitorAttributes
	 * @return
	 */
	public static  AttributeInfo[] getExtendAttributeInfos(String monitorAttributes)
	{
		
		 
		AttributeInfo[] monitorAttributeArray = SimpleStringUtil.json2Object(monitorAttributes,AttributeInfo[].class);
//		 AttributeInfo[] monitorAttributeArray = null;
//		if(!StringUtil.isEmpty(monitorAttributes))
//		{
//			String[] monitorAttributeArray_ = monitorAttributes.split(",");
//			monitorAttributeArray = new AttributeInfo[monitorAttributeArray_.length];
//			AttributeInfo attributeInfo = null;
//			for(int i = 0; i < monitorAttributeArray_.length; i ++)
//			{
//				String attr = monitorAttributeArray_[i];
//				attributeInfo = new AttributeInfo();
//				String attrinfo[] = attr.split(":");
//				if(attrinfo.length > 2)
//				{
//					attributeInfo.setName(attrinfo[0]);
//					attributeInfo.setType(attrinfo[1]);
//					attributeInfo.setCname(attrinfo[2]);
//				}
//				else if(attrinfo.length > 1)
//				{
//					attributeInfo.setName(attrinfo[0]);
//					attributeInfo.setType(attrinfo[1]);
//				}
//				else
//				{
//					attributeInfo.setName(attrinfo[0]);
//					attributeInfo.setType("String");
//				}
//				monitorAttributeArray[i]=attributeInfo;
//					
//				
//			}
//			
//		}
		return monitorAttributeArray;
	}
}
