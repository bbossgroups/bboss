/**
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
package org.frameworkset.web.servlet.i18n;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.frameworkset.web.servlet.DispatchServlet;

import com.frameworkset.util.SimpleStringUtil;

/**
 * <p> SessionLocalResolver.java</p>
 * <p> Description: 从session中获取国家区域对象,如果session中设置了国别Locale对象
 * 则从session中获取local对象，如果没有则返回request中包含的Locale对象
 * </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2012-5-8 下午5:45:55
 * @author biaoping.yin
 * @version 1.0
 */
public class SessionLocalResolver extends AbstractLocaleResolver {
	 
	public static final String SESSION_LOCAL_KEY = "org.frameworkset.web.servlet.i18n.SESSION_LOCAL_KEY";
	private String sessionlocalkey = SESSION_LOCAL_KEY;
	public Locale resolveLocale(HttpServletRequest request) {
		if(request == null)
			return defaultLocal;
		HttpSession session = request.getSession(false);
		Locale local = session != null?(Locale)session.getAttribute(sessionlocalkey):null;
		return (local == null ?defaultLocal:local);
	}

	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
//		throw new UnsupportedOperationException(
//				"Cannot change HTTP accept header - use a different locale resolution strategy");
		
		HttpSession session = request.getSession();
		if(locale == null)
			locale =defaultLocal;
		session.setAttribute(sessionlocalkey,locale);
		DispatchServlet.setLocaleContext(request);
	}
	@Override
	public void setLocale(HttpServletRequest request,
			HttpServletResponse response, String locale) {
		if(locale != null)
			setLocale(request, response, SimpleStringUtil.getLocale(locale));
		else
			setLocale(request, response, request.getLocale());
		DispatchServlet.setLocaleContext(request);

	}
	public String getSessionlocalkey() {
		return sessionlocalkey != null?sessionlocalkey:SESSION_LOCAL_KEY;
	}
	

	public void setSessionlocalkey(String sessionlocalkey) {
		this.sessionlocalkey = sessionlocalkey != null?sessionlocalkey:SESSION_LOCAL_KEY;
	}

}
