/**
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
package org.frameworkset.spi.remote.hession;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.frameworkset.spi.ApplicationContext;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;


/**
 * <p> HessianHandlerFactory.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2013-2-21 下午5:26:17
 * @author biaoping.yin
 * @version 1.0
 */
public class HessianHandlerFactory {
	private static Logger log = Logger.getLogger(HessianHandlerFactory.class);
	private Map<String,HessianHanderContainer> containers ;
	private Method getWebApplicationContext;
	public HessianHandlerFactory() {
		containers = new HashMap<String,HessianHanderContainer>();
		try {
			Class WebApplicationContextUtils = Class.forName("org.frameworkset.web.servlet.support.WebApplicationContextUtils");
			getWebApplicationContext = WebApplicationContextUtils.getMethod("getWebApplicationContext");
			 
		} catch (ClassNotFoundException e) {
			log.debug("init mvc hessian container failed:class org.frameworkset.web.servlet.support.WebApplicationContextUtils not founded .check bboss-mvc.jar in your classpath.",e);
		} catch (NoSuchMethodException e) {
			log.debug("init mvc hessian container failed:NoSuchMethodException getWebApplicationContext in class org.frameworkset.web.servlet.support.WebApplicationContextUtils not founded .",e);
		} catch (SecurityException e) {
			log.debug("init mvc hessian container with SecurityException in class  org.frameworkset.web.servlet.support.WebApplicationContextUtils .",e);
		}
		catch (RuntimeException e) {
			log.debug("init mvc hessian container with RuntimeException in class  org.frameworkset.web.servlet.support.WebApplicationContextUtils .",e);
		}
	 catch (Exception e) {
			log.debug("init mvc hessian container with SecurityException in class  org.frameworkset.web.servlet.support.WebApplicationContextUtils .",e);
		}
		catch (Throwable e) {
			log.debug("init mvc hessian container with SecurityException in class  org.frameworkset.web.servlet.support.WebApplicationContextUtils .",e);
		}
	}
	private BaseApplicationContext getMVCBaseApplicationContext()
	{
		try {
			if(getWebApplicationContext != null)
				return (BaseApplicationContext)getWebApplicationContext.invoke(null, null);
		} catch (SecurityException e) {
			log.debug("init mvc hessian container with SecurityException in class  org.frameworkset.web.servlet.support.WebApplicationContextUtils .",e);
		} catch (IllegalAccessException e) {
			log.debug("IllegalAccessException while Invoke getWebApplicationContext method of class  org.frameworkset.web.servlet.support.WebApplicationContextUtils failed.",e);
		} catch (IllegalArgumentException e) {
			log.debug("IllegalArgumentException while Invoke getWebApplicationContext method of class  org.frameworkset.web.servlet.support.WebApplicationContextUtils failed.",e);
		} catch (InvocationTargetException e) {
			log.debug("InvocationTargetException while Invoke getWebApplicationContext method of class  org.frameworkset.web.servlet.support.WebApplicationContextUtils failed.",e);
		}
		return null;
	}
	/**
	 * http://localhost/hession?container=xx.xx.xx&service=ss
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	public AbstractHessionHandler getHessionHandler(HttpServletRequest request,HttpServletResponse response) throws Throwable
	{
		String container=request.getParameter("container");
		String containtype=request.getParameter("containertype");
		String service=request.getParameter("service");
		
		if(service == null || "".equals(service))
		{
			throw new Exception("lookup hession service failed:serviceid is null");
		}
		
		HessianHanderContainer hessiancontainer = null;
		if(container == null || "".equals(container))
		{
			container="bboss.hessian.mvc";
			containtype = "mvc";
			
			
		}
		else
		{
			if(containtype == null || containtype.equals(""))
				containtype = "simple";
			
		}
		
		if("bboss.hessian.mvc".equals(container) || "mvc".equals(containtype)){
			if(getWebApplicationContext != null)
			{
				hessiancontainer = containers.get("bboss.hessian.mvc");
				if(hessiancontainer == null)
				{
					synchronized(containers)
					{
						hessiancontainer = containers.get("bboss.hessian.mvc");
						if(hessiancontainer == null)
						{
							BaseApplicationContext mvccontainer = getMVCBaseApplicationContext();
							if(mvccontainer != null)							
							{
								hessiancontainer = new HessianHanderContainer(mvccontainer);
								containers.put("bboss.hessian.mvc",hessiancontainer);
							}
						}
					}
					
				}
			}
		}
		else if("simple".equals(containtype)){
			hessiancontainer = containers.get(container);
			if(hessiancontainer == null)
			{
				synchronized(containers)
				{
					hessiancontainer = containers.get(container);
					if(hessiancontainer == null)
					{
						hessiancontainer = new HessianHanderContainer(DefaultApplicationContext.getApplicationContext(container));
						containers.put(container,hessiancontainer);
					}
				}
				
			}
		}
		else {
			hessiancontainer = containers.get(container);
			if(hessiancontainer == null)
			{
				synchronized(containers)
				{
					hessiancontainer = containers.get(container);
					if(hessiancontainer == null)
					{
						hessiancontainer = new HessianHanderContainer(ApplicationContext.getApplicationContext(container));
						containers.put(container,hessiancontainer);
					}
				}
				
			}
		}
		
		if(hessiancontainer  == null )
		{
			throw new Exception("lookup hession service["+service+"] failed:hessian handler container[container="+container+",containtype=" +containtype + "] is null");
		}
		return hessiancontainer.getHessionHandler(service);
	}

}
