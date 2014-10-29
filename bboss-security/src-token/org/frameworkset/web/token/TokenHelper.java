/*
 *  Copyright 2008 bbossgroups
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
package org.frameworkset.web.token;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;

/**
 * <p>Title: TokenHelper.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2014年4月18日
 * @author biaoping.yin
 * @version 3.8.0
 */
public class TokenHelper {
	private static TokenFilter tokenFilter;
	private static TokenService tokenService;
	static void setTokenFilter(TokenFilter tokenFilter)
	{
		TokenHelper.tokenFilter = tokenFilter;
	}
	static {
		BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("tokenconf.xml");
		tokenService = context.getTBeanObject("token.TokenService", TokenService.class);
	}
	public static TokenService getTokenService() {
		return tokenService;
	}
	
	public static boolean isEnableToken()
	{
		return tokenService.isEnableToken();
	}
	
	public static void doDTokencheck(ServletRequest request,
			ServletResponse response) throws IOException, DTokenValidateFailedException
	{
		tokenFilter.doDTokencheck(request, response);
	}
	
	public static void doTicketcheck(ServletRequest request,
			ServletResponse response) throws IOException, DTokenValidateFailedException
	{
		tokenFilter.doTicketcheck(request, response);
	}

	public static void destroy() {
		tokenService = null;
		tokenFilter = null;
		
	}
}
