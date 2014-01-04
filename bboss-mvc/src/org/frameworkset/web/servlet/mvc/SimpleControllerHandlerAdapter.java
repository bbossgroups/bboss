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
import org.frameworkset.web.servlet.Controller;
import org.frameworkset.web.servlet.DispatchServlet;
import org.frameworkset.web.servlet.HandlerAdapter;
import org.frameworkset.web.servlet.ModelAndView;
import org.frameworkset.web.servlet.handler.HandlerMeta;
import org.frameworkset.web.servlet.handler.HandlerUtils;
import org.frameworkset.web.servlet.handler.PathURLNotSetException;
import org.frameworkset.web.servlet.view.AbstractUrlBasedView;
import org.frameworkset.web.servlet.view.UrlBasedViewResolver;

/**
 * @FIXME
 * <p>Title: SimpleControllerHandlerAdapter.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-24
 * @author biaoping.yin
 * @version 1.0
 */
public class SimpleControllerHandlerAdapter  implements HandlerAdapter{
	private HttpMessageConverter[] messageConverters;
	private static final String mname = "handleRequest";
	public boolean supports(HandlerMeta handler) {
		return (handler.getHandler() instanceof Controller);
	}
	
	public ModelAndView handle(HttpServletRequest request, HttpServletResponse response,PageContext pageContext, HandlerMeta handlerMeta)
			throws Exception {
		if(this.messageConverters != null && messageConverters.length > 0)
		{
			try
			{
				request.setAttribute(DispatchServlet.messageConverters_KEY, this.messageConverters);
				ModelAndView mav = ((Controller) handlerMeta.getHandler()).handleRequest(request, response,pageContext);
				if(mav != null )
				{
					if(mav.getView() != null && mav.getView() instanceof AbstractUrlBasedView)
					{
						//处理path:类型路径
						AbstractUrlBasedView view = (AbstractUrlBasedView) mav.getView();
						String url = view.getUrl();
						if(UrlBasedViewResolver.isPathVariable(url))
						{
							url = handlerMeta.getUrlPath(url,mname,handlerMeta.getHandler(),request);
							view.setUrl(url);
						}
					}
					else if(UrlBasedViewResolver.isPathVariable(mav.getViewName()))
					{
						mav.setViewName(handlerMeta.getUrlPath(mav.getViewName(),mname,handlerMeta.getHandler(),request));
					}
				}
				return mav;
			}
			catch(PathURLNotSetException ex)
			{
				return HandlerUtils.handleNoSuchRequestHandlingMethod(ex, request, response);
			}
			finally
			{
				request.removeAttribute(DispatchServlet.messageConverters_KEY);
			}			
		}
		else
		{
			try
			{
				ModelAndView mav =  ((Controller) handlerMeta.getHandler()).handleRequest(request, response,pageContext);
				if(mav != null && UrlBasedViewResolver.isPathVariable(mav.getViewName()))
				{
					mav.setViewName(handlerMeta.getUrlPath(mav.getViewName(),mname,handlerMeta.getHandler(),request));
				}
				return mav;
			}
			catch(PathURLNotSetException ex)
			{
				return HandlerUtils.handleNoSuchRequestHandlingMethod(ex, request, response);
			}
		}
		
	}

	public long getLastModified(HttpServletRequest request, HandlerMeta handler) {
		if (handler.getHandler() instanceof LastModified) {
			return ((LastModified) handler.getHandler()).getLastModified(request);
		}
		return -1L;
	}

	public void setMessageConverters(HttpMessageConverter[] messageConverters) {
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
