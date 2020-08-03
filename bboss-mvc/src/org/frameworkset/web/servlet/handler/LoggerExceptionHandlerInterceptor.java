package org.frameworkset.web.servlet.handler;
/**
 * Copyright 2020 bboss
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.frameworkset.web.servlet.HandlerInterceptor;
import org.frameworkset.web.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 * @Date 2020/8/3 11:51
 * @author biaoping.yin
 * @version 1.0
 */
public class LoggerExceptionHandlerInterceptor implements HandlerInterceptor {
	private Logger logger = LoggerFactory.getLogger(LoggerExceptionHandlerInterceptor.class);
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, HandlerMeta handlerMeta) throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, HandlerMeta handlerMeta, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, HandlerMeta handlerMeta, Exception ex) throws Exception {
		if(ex != null && logger.isErrorEnabled()){
			StringBuilder builder = new StringBuilder();
			builder.append("Request[").append(request.getRequestURI()).append("] handler[").append(handlerMeta.toString()).append("] failed!");
			logger.error(builder.toString(),ex);
		}
	}
}
