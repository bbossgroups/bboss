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
package org.frameworkset.web.servlet.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.frameworkset.web.servlet.HandlerExecutionChain;
import org.frameworkset.web.servlet.HandlerInterceptor;
import org.frameworkset.web.servlet.HandlerMapping;
import org.frameworkset.web.servlet.support.WebApplicationObjectSupport;

/**
 * <p>Title: AbstractHandlerMapping.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-24
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class AbstractHandlerMapping  extends WebApplicationObjectSupport implements HandlerMapping{
	
	private HandlerMeta defaultHandler;

	private  List interceptors = new ArrayList();

	private HandlerInterceptor[] adaptedInterceptors;
	
	
	public void destroy()
	{
		this.defaultHandler = null;
		this.adaptedInterceptors = null;
		this.interceptors = null;
	}

	
	/**
	 * Set the default handler for this handler mapping.
	 * This handler will be returned if no specific mapping was found.
	 * <p>Default is <code>null</code>, indicating no default handler.
	 */
	public void setDefaultHandler(HandlerMeta defaultHandler) {
		this.defaultHandler = defaultHandler;
	}

	/**
	 * Return the default handler for this handler mapping,
	 * or <code>null</code> if none.
	 */
	public HandlerMeta getDefaultHandler() {
		return this.defaultHandler;
	}

	/**
	 * Set the interceptors to apply for all handlers mapped by this handler mapping.
	 * <p>Supported interceptor types are HandlerInterceptor and WebRequestInterceptor.
	 * @param interceptors array of handler interceptors, or <code>null</code> if none
	 * @see #adaptInterceptor
	 * @see HandlerInterceptor
	 * @see WebRequestInterceptor
	 */
	public void setInterceptors(Object[] interceptors) {
		this.interceptors.addAll(Arrays.asList(interceptors));
	}


	/**
	 * Initializes the interceptors.
	 * @see #extendInterceptors(java.util.List)
	 * @see #initInterceptors()
	 */
	protected void initApplicationContext() {
		extendInterceptors(this.interceptors);
		initInterceptors();
	}


	/**
	 * Extension hook that subclasses can override to register additional interceptors,
	 * given the configured interceptors (see {@link #setInterceptors}).
	 * <p>Will be invoked before {@link #initInterceptors()} adapts the specified
	 * interceptors into {@link HandlerInterceptor} instances.
	 * <p>The default implementation is empty.
	 * @param interceptors the configured interceptor List (never <code>null</code>),
	 * allowing to add further interceptors before as well as after the existing
	 * interceptors
	 */
	protected void extendInterceptors(List interceptors) {
	}

	/**
	 * Initialize the specified interceptors, adapting them where necessary.
	 * @see #setInterceptors
	 * @see #adaptInterceptor
	 */
	protected void initInterceptors() {
		if (!this.interceptors.isEmpty()) {
			this.adaptedInterceptors = new HandlerInterceptor[this.interceptors.size()];
			for (int i = 0; i < this.interceptors.size(); i++) {
				Object interceptor = this.interceptors.get(i);
				if (interceptor == null) {
					throw new IllegalArgumentException("Entry number " + i + " in interceptors array is null");
				}
				this.adaptedInterceptors[i] = adaptInterceptor(interceptor);
			}
		}
	}

	/**
	 * Adapt the given interceptor object to the HandlerInterceptor interface.
	 * <p>Supported interceptor types are HandlerInterceptor and WebRequestInterceptor.
	 * Each given WebRequestInterceptor will be wrapped in a WebRequestHandlerInterceptorAdapter.
	 * Can be overridden in subclasses.
	 * @param interceptor the specified interceptor object
	 * @return the interceptor wrapped as HandlerInterceptor
	 * @see HandlerInterceptor
	 * @see WebRequestInterceptor
	 * @see WebRequestHandlerInterceptorAdapter
	 */
	protected HandlerInterceptor adaptInterceptor(Object interceptor) {
		if (interceptor instanceof HandlerInterceptor) {
			return (HandlerInterceptor) interceptor;
		}
//		else if (interceptor instanceof WebRequestInterceptor) {
//			return new WebRequestHandlerInterceptorAdapter((WebRequestInterceptor) interceptor);
//		}
		else {
			throw new IllegalArgumentException("Interceptor type not supported: " + interceptor.getClass().getName());
		}
	}

	/**
	 * Return the adapted interceptors as HandlerInterceptor array.
	 * @return the array of HandlerInterceptors, or <code>null</code> if none
	 */
	protected final HandlerInterceptor[] getAdaptedInterceptors() {
		return this.adaptedInterceptors;
	}

//	/**
//	 * Look up a handler for the given request, falling back to the default
//	 * handler if no specific one is found.
//	 * @param request current HTTP request
//	 * @return the corresponding handler instance, or the default handler
//	 * @see #getHandlerInternal
//	 */
//	public final HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
//		Object handler = getHandlerInternal(request);
//		if (handler == null) {
//			handler = getDefaultHandler();
//		}
//		if (handler == null) {
//			return null;
//		}
//		// Bean name or resolved handler?
//		if (handler instanceof String) {
//			String handlerName = (String) handler;
//			handler = getApplicationContext().getBeanObject(handlerName);
//		}
//		return getHandlerExecutionChain(handler, request);
//	}
	/**
	 * Look up a handler for the given request, falling back to the default
	 * handler if no specific one is found.
	 * @param request current HTTP request
	 * @return the corresponding handler instance, or the default handler
	 * @see #getHandlerInternal
	 */
	public final HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
		Object handler = getHandlerInternal(request);
		if (handler == null) {
			handler = getDefaultHandler();
		}
		if (handler == null) {
			return null;
		}
		// Bean name or resolved handler?
		if (handler instanceof HandlerMeta ) {
			HandlerMeta temp = ((HandlerMeta)handler);
			if(temp.getHandler() instanceof String)
			{
				String handlerName = (String)temp.getHandler();
				HandlerMeta meta = new HandlerMeta();
				meta.setPathNames(temp.getPathNames());
				meta.setHandler(getApplicationContext().getBeanObject(handlerName));
				handler = meta;
			}
		}
		return getHandlerExecutionChain(handler, request);
	}

	/**
	 * Look up a handler for the given request, returning <code>null</code> if no
	 * specific one is found. This method is called by {@link #getHandler};
	 * a <code>null</code> return value will lead to the default handler, if one is set.
	 * <p>Note: This method may also return a pre-built {@link HandlerExecutionChain},
	 * combining a handler object with dynamically determined interceptors.
	 * Statically specified interceptors will get merged into such an existing chain.
	 * @param request current HTTP request
	 * @return the corresponding handler instance, or <code>null</code> if none found
	 * @throws Exception if there is an internal error
	 */
	protected abstract HandlerExecutionChain getHandlerInternal(HttpServletRequest request) throws Exception;

	/**
	 * Build a HandlerExecutionChain for the given handler, including applicable interceptors.
	 * <p>The default implementation simply builds a standard HandlerExecutionChain with
	 * the given handler and this handler mapping's common interceptors. Subclasses may
	 * override this in order to extend/rearrange the list of interceptors.
	 * <p><b>NOTE:</b> The passed-in handler object may be a raw handler or a pre-built
	 * HandlerExecutionChain. This method should handle those two cases explicitly,
	 * either building a new HandlerExecutionChain or extending the existing chain.
	 * <p>For simply adding an interceptor, consider calling <code>super.getHandlerExecutionChain</code>
	 * and invoking {@link HandlerExecutionChain#addInterceptor} on the returned chain object.
	 * @param handler the resolved handler instance (never <code>null</code>)
	 * @param request current HTTP request
	 * @return the HandlerExecutionChain (never <code>null</code>)
	 * @see #getAdaptedInterceptors()
	 */
	protected HandlerExecutionChain getHandlerExecutionChain(Object handler, HttpServletRequest request) {
		if (handler instanceof HandlerExecutionChain) {
			HandlerExecutionChain chain = (HandlerExecutionChain) handler;
			chain.addInterceptors(getAdaptedInterceptors());
			return chain;
		}
		else {
			return new HandlerExecutionChain(handler, getAdaptedInterceptors());
		}
	}



}
