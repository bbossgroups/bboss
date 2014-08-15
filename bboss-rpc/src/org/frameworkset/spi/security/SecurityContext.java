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
package org.frameworkset.spi.security;


import org.frameworkset.spi.BaseSPIManager;
import org.frameworkset.spi.remote.context.RequestContext;

/**
 * 
 * <p>Title: SecurityContext.java</p> 
 * <p>Description: 远程服务调用安全上下文信息</p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date Apr 28, 2009 10:57:05 PM
 * @author biaoping.yin
 * @version 1.0
 */
public class SecurityContext implements java.io.Serializable{
	private RequestContext request;
	/**
	 * 用户账号
	 */
	private String user;
	public void setUser(String user)
    {
        this.user = user;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
	 * 用户密码
	 */
	private String password;
	
	
	public SecurityContext(String user, String password) {
		super();
		this.user = user;
		this.password = password;
//		this.user_roles = user_roles;
	}
	public SecurityContext()
	{
		
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

//	public String[] getUser_roles() {
//		return user_roles;
//	}
	private static SecurityManager securityManager;
	public static SecurityManager getSecurityManager()
	{
	    if(securityManager != null)
	        return securityManager;
	    synchronized(SecurityContext.class)
	    {
	        if(securityManager != null)
	            return securityManager;
	        return securityManager = (SecurityManager)BaseSPIManager.getBeanObject("system.securityManager",new DummySecurityManager());
	    }
	}
	
	public String toString()
	{
	    StringBuffer ret = new StringBuffer().append("SecurityContext[useraccount=" )
	                             .append(user)
	                             .append(",password=")
	                             .append(password).append("]");
	    if(this.request != null){
	                             ret.append(",serviceid=")
	                             .append(request.getServiceid())
	                             .append(",method=" )
	                             .append(request.getMethodop()).append("]");
	    }
	    return ret.toString();
	}
	
//	private static final ThreadLocal<SecurityContext> securityLocal = new ThreadLocal<SecurityContext>();
	
//	public static void setSecurityContext(SecurityContext context)
//	{   
//	    securityLocal.set(context);
//	}
//	
//	public static void destorySecurityContext()
//    {
//        securityLocal.set(null);
//    }
	
	public static SecurityContext getSecurityContext()
	{
		
	    return RequestContext.getSecurityContext();
	}

	public RequestContext getRequest() {
		return request;
	}

	public void setRequest(RequestContext request) {
		this.request = request;
	}
	
	public String getServiceid()
    {
		if(this.request != null)
			return request.getServiceid();
		else
			return null;
    }

    

    public String getMethodop()
	{
	
    	if(this.request != null)
			return request.getMethodop();
		else
			return null;
	}
	
	
	
	

    

   

}
