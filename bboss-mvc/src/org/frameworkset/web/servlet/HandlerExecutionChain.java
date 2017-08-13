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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.util.CollectionUtils;
import org.frameworkset.util.ObjectUtils;
import org.frameworkset.web.servlet.handler.HandlerMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: HandlerExecutionChain.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-23
 * @author biaoping.yin
 * @version 1.0
 */
public class HandlerExecutionChain {
	private final HandlerMeta handler;
	private static Logger logger = LoggerFactory.getLogger(HandlerExecutionChain.class);
	private HandlerInterceptor[] interceptors;

	private List<HandlerInterceptor> interceptorList;
	private int interceptorIndex = -1;


	/**
	 * Create a new HandlerExecutionChain.
	 * @param handler the handler object to execute
	 */
	public HandlerExecutionChain(HandlerMeta handler) {
		this(handler, (HandlerInterceptor[])null);
	}

	/**
	 * Create a new HandlerExecutionChain.
	 * @param handler the handler object to execute
	 * @param interceptors the array of interceptors to apply
	 * (in the given order) before the handler itself executes
	 */
	public HandlerExecutionChain(Object handler, HandlerInterceptor... interceptors) {
		if (handler instanceof HandlerExecutionChain) {
			HandlerExecutionChain originalChain = (HandlerExecutionChain) handler;
			this.handler = originalChain.getHandler();
			this.interceptorList = new ArrayList<HandlerInterceptor>();
			CollectionUtils.mergeArrayIntoCollection(originalChain.getInterceptors(), this.interceptorList);
			CollectionUtils.mergeArrayIntoCollection(interceptors, this.interceptorList);
		}
		else {
			this.handler = (HandlerMeta)handler;
			this.interceptors = interceptors;
		}
	}


	/**
	 * Return the handler object to execute.
	 * @return the handler object
	 */
	public HandlerMeta getHandler() {
		return this.handler;
	}

	public void addInterceptor(HandlerInterceptor interceptor) {
		initInterceptorList();
		this.interceptorList.add(interceptor);
	}

	public void addInterceptors(HandlerInterceptor[] interceptors) {
		if (interceptors != null) {
			initInterceptorList();
			for (int i = 0; i < interceptors.length; i++) {
				this.interceptorList.add(interceptors[i]);
			}
		}
	}

	private void initInterceptorList() {
		if (this.interceptorList == null) {
			this.interceptorList = new ArrayList();
		}
		if (this.interceptors != null) {
			for (int i = 0; i < this.interceptors.length; i++) {
				this.interceptorList.add(this.interceptors[i]);
			}
			this.interceptors = null;
		}
	}

	/**
	 * Return the array of interceptors to apply (in the given order).
	 * @return the array of HandlerInterceptors instances (may be <code>null</code>)
	 */
	public HandlerInterceptor[] getInterceptors() {
		if (this.interceptors == null && this.interceptorList != null) {
			this.interceptors = (HandlerInterceptor[])
					this.interceptorList.toArray(new HandlerInterceptor[this.interceptorList.size()]);
		}
		return this.interceptors;
	}

	/**
	 * Delegates to the handler's <code>toString()</code>.
	 */
	public String toString() {
		return String.valueOf(handler);
	}

	public void addInterceptors(
			List<HandlerInterceptor> gloabelHandlerInterceptors)
	{
		this.initInterceptorList();
		this.interceptorList.addAll(gloabelHandlerInterceptors);
		
	}
	
	/**
	 * Apply preHandle methods of registered interceptors.
	 * @return {@code true} if the execution chain should proceed with the
	 * next interceptor or the handler itself. Else, DispatcherServlet assumes
	 * that this interceptor has already dealt with the response itself.
	 */
	boolean applyPreHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HandlerInterceptor[] interceptors = getInterceptors();
		if (!ObjectUtils.isEmpty(interceptors)) {
			for (int i = 0; i < interceptors.length; i++) {
				HandlerInterceptor interceptor = interceptors[i];
				if (!interceptor.preHandle(request, response, this.handler)) {
					triggerAfterCompletion(request, response, null);
					return false;
				}
				this.interceptorIndex = i;
			}
		}
		return true;
	}
	
	/**
	 * Apply postHandle methods of registered interceptors.
	 */
	void applyPostHandle(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) throws Exception {
		HandlerInterceptor[] interceptors = getInterceptors();
		if (!ObjectUtils.isEmpty(interceptors)) {
			for (int i = interceptors.length - 1; i >= 0; i--) {
				HandlerInterceptor interceptor = interceptors[i];
				interceptor.postHandle(request, response, this.handler, mv);
			}
		}
	}
	/**
	 * Trigger afterCompletion callbacks on the mapped HandlerInterceptors.
	 * Will just invoke afterCompletion for all interceptors whose preHandle invocation
	 * has successfully completed and returned true.
	 */
	void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response, Exception ex)
			throws Exception {

		HandlerInterceptor[] interceptors = getInterceptors();
		if (!ObjectUtils.isEmpty(interceptors)) {
			for (int i = this.interceptorIndex; i >= 0; i--) {
				HandlerInterceptor interceptor = interceptors[i];
				try {
					interceptor.afterCompletion(request, response, this.handler, ex);
				}
				catch (Throwable ex2) {
					logger.error("HandlerInterceptor.afterCompletion threw exception", ex2);
				}
			}
		}
	}
	
	
	/**
	 * Apply afterConcurrentHandlerStarted callback on mapped AsyncHandlerInterceptors.
	 */
	void applyAfterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response) {
		HandlerInterceptor[] interceptors = getInterceptors();
		if (!ObjectUtils.isEmpty(interceptors)) {
			for (int i = interceptors.length - 1; i >= 0; i--) {
				if (interceptors[i] instanceof AsyncHandlerInterceptor) {
					try {
						AsyncHandlerInterceptor asyncInterceptor = (AsyncHandlerInterceptor) interceptors[i];
						asyncInterceptor.afterConcurrentHandlingStarted(request, response, this.handler);
					}
					catch (Throwable ex) {
						logger.error("Interceptor [" + interceptors[i] + "] failed in afterConcurrentHandlingStarted", ex);
					}
				}
			}
		}
	}
	

}
