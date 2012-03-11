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

import org.frameworkset.util.CollectionUtils;
import org.frameworkset.web.servlet.handler.HandlerMeta;

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

	private HandlerInterceptor[] interceptors;

	private List<HandlerInterceptor> interceptorList;


	/**
	 * Create a new HandlerExecutionChain.
	 * @param handler the handler object to execute
	 */
	public HandlerExecutionChain(HandlerMeta handler) {
		this(handler, null);
	}

	/**
	 * Create a new HandlerExecutionChain.
	 * @param handler the handler object to execute
	 * @param interceptors the array of interceptors to apply
	 * (in the given order) before the handler itself executes
	 */
	public HandlerExecutionChain(Object handler, HandlerInterceptor[] interceptors) {
		if (handler instanceof HandlerExecutionChain) {
			HandlerExecutionChain originalChain = (HandlerExecutionChain) handler;
			this.handler = originalChain.getHandler();
			this.interceptorList = new ArrayList();
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
	

}
