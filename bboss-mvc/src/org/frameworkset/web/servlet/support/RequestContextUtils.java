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
package org.frameworkset.web.servlet.support;

import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.frameworkset.web.servlet.DispatchServlet;
import org.frameworkset.web.servlet.LocaleResolver;
import org.frameworkset.web.servlet.ThemeResolver;
import org.frameworkset.web.servlet.context.WebApplicationContext;
import org.frameworkset.web.ui.ThemeSource;
import org.frameworkset.web.ui.context.Theme;


/**
 * <p>Title: RequestContextUtils.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-24
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class RequestContextUtils {
	/** {@link javax.servlet.jsp.PageContext} attribute for page-level
	 * {@link RequestContext} instance.
	 * */
	public static final String REQUEST_CONTEXT_PAGE_ATTRIBUTE =
			"org.frameworkset.web.servlet.tags.REQUEST_CONTEXT";
	/**
	 * Look for the WebApplicationContext associated with the DispatchServlet
	 * that has initiated request processing.
	 * @param request current HTTP request
	 * @return the request-specific web application context
	 * @throws IllegalStateException if no servlet-specific context has been found
	 */
	public static WebApplicationContext getWebApplicationContext(ServletRequest request)
	    throws IllegalStateException {

		return getWebApplicationContext(request, null);
	}

	/**
	 * Look for the WebApplicationContext associated with the DispatchServlet
	 * that has initiated request processing, and for the global context if none
	 * was found associated with the current request. This method is useful to
	 * allow components outside the framework, such as JSP tag handlers,
	 * to access the most specific application context available.
	 * @param request current HTTP request
	 * @param servletContext current servlet context
	 * @return the request-specific WebApplicationContext, or the global one
	 * if no request-specific context has been found
	 * @throws IllegalStateException if neither a servlet-specific nor a
	 * global context has been found
	 */
	public static WebApplicationContext getWebApplicationContext(
			ServletRequest request, ServletContext servletContext) throws IllegalStateException {

		WebApplicationContext webApplicationContext = (WebApplicationContext) request.getAttribute(
				DispatchServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		if (webApplicationContext == null) {
			if (servletContext == null) {
				throw new IllegalStateException("No WebApplicationContext found: not in a DispatchServlet request?");
			}
			webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			if (webApplicationContext == null) {
				throw new IllegalStateException("No WebApplicationContext found: no ContextLoaderListener registered?");
			}
		}
		return webApplicationContext;
	}

	/**
	 * Return the LocaleResolver that has been bound to the request by the
	 * DispatchServlet.
	 * @param request current HTTP request
	 * @return the current LocaleResolver, or <code>null</code> if not found
	 */
	public static LocaleResolver getLocaleResolver(HttpServletRequest request) {
		return (LocaleResolver) request.getAttribute(DispatchServlet.LOCALE_RESOLVER_ATTRIBUTE);
	}

	/**
	 * Retrieves the current locale from the given request,
	 * using the LocaleResolver bound to the request by the DispatchServlet
	 * (if available), falling back to the request's accept-header Locale.
	 * @param request current HTTP request
	 * @return the current locale, either from the LocaleResolver or from
	 * the plain request
	 * @see #getLocaleResolver
	 * @see javax.servlet.http.HttpServletRequest#getLocale()
	 */
	public static Locale getLocale(HttpServletRequest request) {
		LocaleResolver localeResolver = getLocaleResolver(request);
		if (localeResolver != null) {
			return localeResolver.resolveLocale(request);
		}
		else {
			return request.getLocale();
		}
	}

	/**
	 * Return the ThemeResolver that has been bound to the request by the
	 * DispatchServlet.
	 * @param request current HTTP request
	 * @return the current ThemeResolver, or <code>null</code> if not found
	 */
	public static ThemeResolver getThemeResolver(HttpServletRequest request) {
		return (ThemeResolver) request.getAttribute(DispatchServlet.THEME_RESOLVER_ATTRIBUTE);
	}

	/**
	 * Return the ThemeSource that has been bound to the request by the
	 * DispatchServlet.
	 * @param request current HTTP request
	 * @return the current ThemeSource
	 */
	public static ThemeSource getThemeSource(HttpServletRequest request) {
		return (ThemeSource) request.getAttribute(DispatchServlet.THEME_SOURCE_ATTRIBUTE);
	}

	/**
	 * Retrieves the current theme from the given request, using the ThemeResolver
	 * and ThemeSource bound to the request by the DispatchServlet.
	 * @param request current HTTP request
	 * @return the current theme, or <code>null</code> if not found
	 * @see #getThemeResolver
	 */
	public static Theme getTheme(HttpServletRequest request) {
		ThemeResolver themeResolver = getThemeResolver(request);
		ThemeSource themeSource = getThemeSource(request);
		if (themeResolver != null && themeSource != null) {
			String themeName = themeResolver.resolveThemeName(request);
			return themeSource.getTheme(themeName);
		}
		else {
			return null;
		}
	}
	
	public static Locale getRequestContextLocal(PageContext pageContext)
	{
		RequestContext requestContext = (RequestContext) pageContext.getAttribute(REQUEST_CONTEXT_PAGE_ATTRIBUTE);
		
		if (requestContext == null) {
			requestContext = new JspAwareRequestContext(pageContext);
			pageContext.setAttribute(REQUEST_CONTEXT_PAGE_ATTRIBUTE, requestContext);
		}
		return requestContext.getLocale();
		
		
	}
	public static String getRequestContextLocalName(PageContext pageContext)
	{
		RequestContext requestContext = (RequestContext) pageContext.getAttribute(REQUEST_CONTEXT_PAGE_ATTRIBUTE);
		
		if (requestContext == null) {
			requestContext = new JspAwareRequestContext(pageContext);
			pageContext.setAttribute(REQUEST_CONTEXT_PAGE_ATTRIBUTE, requestContext);
		}
		return requestContext.getLocaleName();
		
		
	}
	
	public static RequestContext getRequestContext(PageContext pageContext)
	{
		RequestContext requestContext = (RequestContext) pageContext.getAttribute(REQUEST_CONTEXT_PAGE_ATTRIBUTE);
		
		if (requestContext == null) {
			requestContext = new JspAwareRequestContext(pageContext);
			pageContext.setAttribute(REQUEST_CONTEXT_PAGE_ATTRIBUTE, requestContext);
		}
		return requestContext;
		
		
	}

}
