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

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.web.servlet.i18n.AcceptHeaderLocaleResolver;

/**
 * <p>Title: LocaleResolver.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-24
 * @author biaoping.yin
 * @see AcceptHeaderLocaleResolver
 * @see RequestContext#getLocale
 * @version 1.0
 */
public interface LocaleResolver {
	/**
	   * Resolve the current locale via the given request.
	   * Should return a default locale as fallback in any case.
	   * @param request the request to resolve the locale for
	   * @return the current locale (never <code>null</code>)
	   */
		Locale resolveLocale(HttpServletRequest request);
		
		String resolveLocaleCode(HttpServletRequest request);

	  /**
	   * Set the current locale to the given one.
	   * @param request the request to be used for locale modification
	   * @param response the response to be used for locale modification
	   * @param locale the new locale, or <code>null</code> to clear the locale
		 * @throws UnsupportedOperationException if the LocaleResolver implementation
		 * does not support dynamic changing of the theme
	   */
		void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale);
		

		  /**
		   * Set the current locale to the given one.
		   * @param request the request to be used for locale modification
		   * @param response the response to be used for locale modification
		   * @param locale the new locale, or <code>null</code> to clear the locale
			 * @throws UnsupportedOperationException if the LocaleResolver implementation
			 * does not support dynamic changing of the theme
		   */
			void setLocale(HttpServletRequest request, HttpServletResponse response, String locale);

}
