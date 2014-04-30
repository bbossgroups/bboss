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
package org.frameworkset.security.session.impl;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.frameworkset.web.token.DTokenValidateFailedException;
import org.frameworkset.web.token.TokenFilter;
import org.frameworkset.web.token.TokenHelper;
import org.frameworkset.web.token.TokenService;

/**
 * <p>Title: SessionHelper.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2014年4月30日
 * @author biaoping.yin
 * @version 3.8.0
 */
public class SessionHelper {
	private static SessionManager sessionManager;
	
	static {
		BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("sessionconf.xml");
		sessionManager = context.getTBeanObject("sessionManager", SessionManager.class);
	}
	

	public static void destroy() {
		sessionManager = null;
		
	}


	public static SessionManager getSessionManager() {
		return sessionManager;
	}
}
