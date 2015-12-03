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
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.frameworkset.spi.assemble.Pro;

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
	private static TokenServiceInf tokenService;
	private static final Logger log = Logger.getLogger(TokenHelper.class);
	static void setTokenFilter(TokenFilter tokenFilter)
	{
		init();
		TokenHelper.tokenFilter = tokenFilter;
	}
	static boolean inited;
	static void init(){
		if(inited) 
			return;
		synchronized(TokenHelper.class)
		{
			if(inited) 
				return;
			try {
				BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("tokenconf.xml");
				Pro pro = context.getProBean("token.TokenService");
				if(pro != null)
				{
					List<Pro> refs = pro.getReferences();
					if(refs == null || refs.size() == 0)
					{
						tokenService = new DummyTokenService();
					}
					else
					{
						boolean enabled = false;
						for(Pro p:refs)
						{
							if(p.getName() != null && p.getName().equals("enableToken"))
							{
								Object value = p.getValue();
								if(value != null && value.toString().equals("true"))
									enabled = true; 
								break;
							}
						}
						if(enabled)
						{
							tokenService = context.getTBeanObject("token.TokenService", TokenService.class);
						}
						else
						{
							tokenService = new DummyTokenService();
						}
					}
				}
				else
				{
					tokenService = new DummyTokenService();
				}
			}
			catch (RuntimeException e) {
				tokenService = new DummyTokenService();
				log.warn("",e);
			}
			catch (Exception e) {
				tokenService = new DummyTokenService();
				log.warn("",e);
			}
			catch (Throwable e) {
				tokenService = new DummyTokenService();
				log.warn("",e);
			}
			inited = true;
		}
	}
	public static TokenServiceInf getTokenService() {
		init();
		return tokenService;
	}
	
	public static boolean isEnableToken()
	{
		init();
		return tokenService.isEnableToken();
	}
	
	public static void doDTokencheck(ServletRequest request,
			ServletResponse response) throws IOException, DTokenValidateFailedException
	{
		init();
		tokenFilter.doDTokencheck(request, response);
	}
	
	public static void doTicketcheck(ServletRequest request,
			ServletResponse response) throws IOException, DTokenValidateFailedException
	{
		init();
		tokenFilter.doTicketcheck(request, response);
	}

	public static void destroy() {
		tokenService = null;
		tokenFilter = null;
		inited = false;
		
	}
}
