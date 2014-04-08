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

import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p> AbstractHessionHandler.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2013-2-21 下午4:41:23
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class AbstractHessionHandler {
	private Object service;
	private Class serviceInterface; 
	public AbstractHessionHandler() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Check whether the service reference has been set.
	 * @see #setService
	 */
	protected void checkService() throws IllegalArgumentException {
		if (getService() == null) {
			throw new IllegalArgumentException("Property 'service' is required");
		}
	}

	/**
	 * Check whether a service reference has been set,
	 * and whether it matches the specified service.
	 * @see #setServiceInterface
	 * @see #setService
	 */
	protected void checkServiceInterface() throws IllegalArgumentException {
		Class serviceInterface = getServiceInterface();
		Object service = getService();
		if (serviceInterface == null) {
			throw new IllegalArgumentException("Property 'serviceInterface' is required");
		}
		if (service instanceof String) {
			throw new IllegalArgumentException("Service [" + service + "] is a String " +
					"rather than an actual service reference: Have you accidentally specified " +
					"the service bean name as value instead of as reference?");
		}
		if (!serviceInterface.isInstance(service)) {
			throw new IllegalArgumentException("Service interface [" + serviceInterface.getName() +
					"] needs to be implemented by service [" + service + "] of class [" +
					service.getClass().getName() + "]");
		}
	}
	public Object getService() {
		return service;
	}
	public void setService(Object service) {
		this.service = service;
	}
	public Class getServiceInterface() {
		return serviceInterface;
	}
	public void setServiceInterface(Class serviceInterface) {
		this.serviceInterface = serviceInterface;
	}
	public void invoke(HttpServletRequest req,HttpServletResponse response) throws Throwable
	{	

//	    String serviceId = req.getPathInfo();
//	    String objectId = req.getParameter("id");
//	    if (objectId == null)
//	      objectId = req.getParameter("ejbid");

//	    ServiceContext.begin(req, serviceId, objectId);

	    try {
	    	 InputStream is = req.getInputStream();
	         OutputStream os = response.getOutputStream();
	    	invoke(is, os);
	    } catch (RuntimeException e) {
	      throw e;
	    } catch (Throwable e) {
	      throw new ServletException(e);
	    } finally {
//	      ServiceContext.end();
	    }
	}
	protected abstract void invoke(InputStream inputStream, OutputStream outputStream) throws Throwable;
	protected abstract void afterPropertiesSet();

}
