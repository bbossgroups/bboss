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
package org.frameworkset.web.servlet.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.frameworkset.http.converter.HttpMessageConverter;
import org.frameworkset.web.HttpRequestHandler;
import org.frameworkset.web.servlet.HandlerAdapter;
import org.frameworkset.web.servlet.ModelAndView;
import org.frameworkset.web.servlet.handler.HandlerMeta;

/**
 * <p>Title: HttpRequestHandlerAdapter.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-7
 * @author biaoping.yin
 * @version 1.0
 */
public class HttpRequestHandlerAdapter  implements HandlerAdapter {

	public boolean supports(HandlerMeta handler) {
		return (handler.getHandler() instanceof HttpRequestHandler);
	}

	public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, PageContext pageContext,HandlerMeta handler)
			throws Exception {

		((HttpRequestHandler) handler.getHandler()).handleRequest(request, response,pageContext);
		return null;
	}

	public long getLastModified(HttpServletRequest request, HandlerMeta handler) {
		if (handler.getHandler() instanceof LastModified) {
			return ((LastModified) handler.getHandler()).getLastModified(request);
		}
		return -1L;
	}
	private HttpMessageConverter[] messageConverters;
	public void setMessageConverters(HttpMessageConverter[] messageConverters) {
		// TODO Auto-generated method stub
		this.messageConverters = messageConverters;
	}
	
	public boolean containMessageConverters() {
		
		return this.messageConverters != null && this.messageConverters.length > 0;
	}

	@Override
	public void destroy() {
		messageConverters = null;
		
	}

}
