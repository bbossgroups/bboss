/*
 *  Copyright 2008 biaoping.yin
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

package org.frameworkset.spi.remote.context;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.frameworkset.spi.assemble.SynchronizedMethod;
import org.frameworkset.spi.remote.Header;
import org.frameworkset.spi.remote.ServiceID;
import org.frameworkset.spi.security.SecurityContext;
import org.frameworkset.spi.security.SecurityException;

/**
 * <p>
 * Title: RequestContext.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2010-7-25 下午01:42:23
 * @author biaoping.yin
 * @version 1.0
 */
public class RequestContext {
	public RequestContext(SecurityContext securityContext, boolean setcontext) {
		super();
		this.securityContext = securityContext;
		if (securityContext == null)
			this.securityContext = new SecurityContext();
		this.securityContext.setRequest(this);
		if (setcontext)
			setRequestContext(this);
	}

	SecurityContext securityContext;
	private static final ThreadLocal<RequestContext> requestLocal = new ThreadLocal<RequestContext>();

	void setRequestContext(RequestContext context) {
		requestLocal.set(context);
	}

	public static void destoryRequestContext() {
		requestLocal.set(null);
	}

	SecurityContext getSecurityContext_() {
		return this.securityContext;
	}

	public static SecurityContext getSecurityContext() {
		RequestContext context = getRequestContext();
		return context != null ? context.getSecurityContext_() : null;
	}

	public static RequestContext getRequestContext() {
		return getRequestContext(true);
	}
	
	public static RequestContext getRequestContext(boolean create) {
		RequestContext context = requestLocal.get();
		if (context == null && create)
			context = new RequestContext(new SecurityContext(), false);
		return context;
	}

	/**
	 * 执行远程调用的准备功能，做认证和鉴权
	 * 
	 * @param method_call
	 * @throws Throwable
	 */
	public void preMethodCall(ServiceID id, String method, Class[] types,
			Map<String, Header> headers) throws Throwable {
		try {
			// SecurityContext securityContext =
			// method_call.getSecurityContext();
			initPermissionInfo(id, method, types);
			if (SecurityContext.getSecurityManager().checkUser(securityContext)) {
				// initPermissionInfo( id, method, types);
				if (SecurityContext.getSecurityManager().checkPermission(
						securityContext)) {
					if (securityContext == null)
						securityContext = new SecurityContext();
					// SecurityContext.setSecurityContext(securityContext);
				} else {
					throw new SecurityException("权限检测失败,当前用户无法执行服务操作："
							+ securityContext);
				}
			} else {

				throw new SecurityException("认证失败,请检查用户凭证信息是否正确："
						+ securityContext);
			}
			if (headers != null && headers.size() > 0) {
//				Iterator<String> keys = headers.keySet().iterator();
//				while (keys.hasNext()) {
//					String key = keys.next();
//					Header head = headers.get(key);
//					this.addParameters(key, head.getValue());
//				}
				callparameters.putAll(headers);
				// this.addParameters(headers);
			}
			
		} catch (SecurityException e) {
			throw e;
		}
		catch (Exception e) {
			throw new SecurityException("" + securityContext,e);
		}
	}
	public Map<String,Header> getHeaders()
	{
		return this.callparameters;
	}
	private void initPermissionInfo(ServiceID id, String method, Class[] types) {
		// SecurityContext securityContext = method_call.getSecurityContext();

		// ServiceID id = (ServiceID)method_call.getArgs()[0];
		// if(securityContext == null)
		// return;
		setServiceid(id.getService());
		// String method = (String)method_call.getArgs()[1];
		// Class[] types = (Class[])method_call.getArgs()[3];
		setServiceid(id.getService());
		setMethodop(SynchronizedMethod.buildMethodUUID(method, types));

	}
	static class UtilMap extends HashMap<String,Header>
	{
	    public int getInt(String key)
	    {            
	    	Object header = this.getObject(key);
	    	
	        if(header == null )
	            return 0;
	        int value_ = Integer.parseInt((String)header);
	        return value_;
	    }
	    
	    public long getLong(String key)
	    {            
	        Object value = this.getObject(key);
	        if(value == null)
	            return 0;
	        long value_ = Integer.parseInt(value.toString());
	        return value_;
//	        return value.getLong();
	    }
	    
	    public int getInt(String key,int defaultValue)
	    {
	        Object value = this.getObject(key);
	        if(value == null)
	            return defaultValue;
	        int value_ = Integer.parseInt(value.toString());
	        return value_;
	    }
	    
	    
	    public long getLong(String key,long defaultValue)
	    {
	        Object value = this.getObject(key);
	        if(value == null)
	            return defaultValue;
	        long value_ = Long.parseLong(value.toString());
	        return value_;
	    }
	    
	    
	    
	    public boolean getBoolean(String key)
	    {
	        Object value = this.getObject(key);
	        if(value == null)
	            return false;
	        boolean value_ = Boolean.parseBoolean(value.toString());
	        return value_;
	    }
	    public boolean getBoolean(String key,boolean defaultValue)
	    {
	        Object value = this.getObject(key);
	        if(value == null)
	            return defaultValue;
	        boolean value_ =  Boolean.parseBoolean(value.toString());
	        return value_;
	    }
	    
	    public String getString(String key)
	    {
	        Object value = this.getObject(key);
	        if(value == null)
	            return null;
	        
	        return value.toString();
	    }
	    public String getString(String key,String defaultValue)
	    {
	        Object value = this.getObject(key);
	        
	        if(value == null)
	            return defaultValue;
	        
	        return value.toString();
	    }
	    
	    
	    
	    
	    
	    
	    public Object getObject(String key)
	    {
	        Header value = this.get(key);
	        if(value != null)
	        	return value.getValue();
	        return value;
	    }
	   
	    
	    public Object getObject(String key,Object defaultValue)
	    {
	        Object value = getObject( key);
	        
	        if(value == null)
	            return defaultValue;
	        
	        return value;
	    }
	}
	/**
	 * 访问的方法标识，用来进行权限控制
	 */
	private String methodop;
	/**
	 * 访问的服务标识，用来进行权限控制
	 */
	private String serviceid;

	/**
	 * 应用执行环境设置的上下文信息
	 */
	private UtilMap callparameters = new UtilMap();

//	public void addParameters(Object key, Object value) {
//		this.callparameters.put(key, value);
//	}
//
//	public void addParameters(Map parameters) {
//		this.callparameters.putAll(parameters);
//	}

	public Object getParameter(Object key) {
		return this.callparameters.get(key);
	}

	public int getIntParameter(String key) {

		return callparameters.getInt(key);
	}

	public long getLongParameter(String key) {
		return callparameters.getLong(key);
	}

	public int getIntParameter(String key, int defaultValue) {
		return callparameters.getInt(key, defaultValue);
	}

	public long getLongParameter(String key, long defaultValue) {
		return callparameters.getLong(key, defaultValue);
	}

	public boolean getBooleanParameter(String key) {
		return callparameters.getBoolean(key);
	}

	public boolean getBooleanParameter(String key, boolean defaultValue) {
		return callparameters.getBoolean(key, defaultValue);
	}

	public String getStringParameter(String key) {
		return callparameters.getString(key);
	}

	public String getStringParameter(String key, String defaultValue) {
		return callparameters.getString(key, defaultValue);
	}

	public Object getObjectParameter(String key) {
		return callparameters.getObject(key);
	}

	public Object getObjectParameter(String key, Object defaultValue) {
		return callparameters.getObject(key);
	}

	public String getServiceid() {
		return serviceid;
	}

	public void setServiceid(String serviceid) {
		this.serviceid = serviceid;
	}

	public String getMethodop() {

		return methodop;
	}

	public void setMethodop(String methodop) {

		this.methodop = methodop;
	}



}
