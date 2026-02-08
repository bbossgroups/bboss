package org.frameworkset.web.interceptor;
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

import com.frameworkset.util.SimpleStringUtil;
import org.frameworkset.web.HttpRequestMethodNotSupportedException;
import org.frameworkset.web.servlet.HandlerInterceptor;
import org.frameworkset.web.servlet.ModelAndView;
import org.frameworkset.web.servlet.handler.HandlerMeta;
import org.frameworkset.web.servlet.handler.LoggerExceptionHandlerInterceptor;
import org.frameworkset.web.token.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 * @Date 2020/8/3 11:51
 * @author biaoping.yin
 * @version 1.0
 */
public class Logger2ndJsonExceptionHandlerInterceptor implements HandlerInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(LoggerExceptionHandlerInterceptor.class);
	private boolean jsonRedirect ;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, HandlerMeta handlerMeta) throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, HandlerMeta handlerMeta, ModelAndView modelAndView) throws Exception {

	}
    public static void sendFailedJson(HttpServletRequest request,
                                         HttpServletResponse response,   Exception ex) throws IOException {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setCode(AuthResponse.code_405);
         
        HttpSession session = request.getSession(false);
        if (session != null)
            authResponse.setSessionId(session.getId());
        if (ex instanceof HttpRequestMethodNotSupportedException  ) {

            authResponse.setMessage(ex.getMessage());
			 
		}
        else{
            authResponse.setMessage(SimpleStringUtil.exceptionToString(ex));
        }
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(SimpleStringUtil.object2json(authResponse));
        writer.flush();
        
//        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED,SimpleStringUtil.object2json(authResponse));  
    }
 
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, HandlerMeta handlerMeta, Exception ex) throws Exception {
		if(ex != null && logger.isErrorEnabled()){
			StringBuilder builder = new StringBuilder();
			builder.append("Request[").append(request.getRequestURI()).append("@").append(handlerMeta.toString()).append("] failed!");
			logger.error(builder.toString(),ex);
            if ( jsonRedirect && !response.isCommitted()){
                sendFailedJson(request,response, ex);
            }
		}
	}
 


}
