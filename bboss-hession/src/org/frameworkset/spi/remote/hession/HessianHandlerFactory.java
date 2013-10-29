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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.spi.ApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.frameworkset.web.servlet.support.WebApplicationContextUtils;

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

	private Map<String,HessianHanderContainer> containers ;
	public HessianHandlerFactory() {
		containers = new HashMap<String,HessianHanderContainer>();
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
			hessiancontainer = containers.get("bboss.hessian.mvc");
			if(hessiancontainer == null)
			{
				synchronized(containers)
				{
					hessiancontainer = containers.get("bboss.hessian.mvc");
					if(hessiancontainer == null)
					{
						hessiancontainer = new HessianHanderContainer(WebApplicationContextUtils.getWebApplicationContext());
						containers.put("bboss.hessian.mvc",hessiancontainer);
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
