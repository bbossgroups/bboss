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
package org.frameworkset.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.frameworkset.http.converter.HttpMessageConverter;
import org.frameworkset.web.servlet.handler.HandlerMeta;
import org.frameworkset.web.servlet.mvc.LastModified;

/**
 * <p>Title: HandlerAdapter.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-24
 * @author biaoping.yin
 * @version 1.0
 */
public interface HandlerAdapter {
	/**
	 * Given a handler instance, return whether or not this HandlerAdapter can
	 * support it. Typical HandlerAdapters will base the decision on the handler
	 * type. HandlerAdapters will usually only support one handler type each.
	 * <p>A typical implementation:
	 * <p><code>
	 * return (handler instanceof MyHandler);
	 * </code>
	 * @param handler handler object to check
	 * @return whether or not this object can use the given handler
	 */
	boolean supports(HandlerMeta handler); 
	
	/**
	 * Use the given handler to handle this request.
	 * The workflow that is required may vary widely.
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @param handler handler to use. This object must have previously been passed
	 * to the <code>supports</code> method of this interface, which must have
	 * returned <code>true</code>.
	 * @throws Exception in case of errors
	 * @return ModelAndView object with the name of the view and the required
	 * model data, or <code>null</code> if the request has been handled directly
	 */
	ModelAndView handle(HttpServletRequest request, HttpServletResponse response, PageContext pageContext,HandlerMeta handler) throws Exception;

	/**
	 * Same contract as for HttpServlet's <code>getLastModified</code> method.
	 * Can simply return -1 if there's no support in the handler class.
	 * @param request current HTTP request
	 * @param handler handler to use
	 * @return the lastModified value for the given handler
	 * @see javax.servlet.http.HttpServlet#getLastModified
	 * @see LastModified#getLastModified
	 */
	long getLastModified(HttpServletRequest request, HandlerMeta handler);
	
	

	public void setMessageConverters(HttpMessageConverter[] messageConverters);
	
	public boolean containMessageConverters();

	void destroy();
	


}
