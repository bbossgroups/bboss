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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.frameworkset.spi.support.LocaleContext;
import org.frameworkset.spi.support.MessageSource;
import org.frameworkset.spi.support.SimpleLocaleContext;
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
	
	public static final String LOCALE_CONTEXT_REQUEST_ATTRIBUTE =
			"org.frameworkset.web.servlet.tags.LocalContext";
	
	
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
		LocaleResolver localeResolver = (LocaleResolver) request.getAttribute(DispatchServlet.LOCALE_RESOLVER_ATTRIBUTE);
		if(localeResolver == null)	
		{
			localeResolver = DispatchServlet.getLocaleResolver(DispatchServlet.webApplicationContext);
			request.setAttribute(DispatchServlet.LOCALE_RESOLVER_ATTRIBUTE,localeResolver);
		}
			
		return localeResolver;
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
//		LocaleResolver localeResolver = getLocaleResolver(request);
//		if (localeResolver != null) {
//			return localeResolver.resolveLocale(request);
//		}
//		else {
//			return request.getLocale();
//		}
		return getRequestContextLocal(request);
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
	public static String getLocaleCode(HttpServletRequest request) {
//		LocaleResolver localeResolver = getLocaleResolver(request);
//		if (localeResolver != null) {
//			return localeResolver.resolveLocaleCode(request);
//		}
//		else {
//			return String.valueOf(request.getLocale());
//		}
		Locale local = getLocalContext(request).getLocale();
		return local.toString();
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
	
	public static Locale getRequestContextLocal(HttpServletRequest request)
	{
		LocaleContext lc = getLocalContext( request);
		return lc.getLocale();
		
		
	}
	/**
	 * 根据code从mvc的国际化配置文件中获取对应语言的代码值
	 * @param code
	 * @param request
	 * @return
	 */
	public static String getI18nMessage(String code,HttpServletRequest request)
	{
		return getI18nMessage(code,(String )null,request);
		
		
	}
	/**
	 * 根据code从mvc的国际化配置文件中获取对应语言的代码值,如果代码值为空，则返回defaultMessage
	 * @param code
	 * @param defaultMessage
	 * @param request
	 * @return
	 */
	public static String getI18nMessage(String code,String defaultMessage,HttpServletRequest request)
	{
		return getI18nMessage(code,(Object[])null,defaultMessage,request);
		
		
	}
	
	/**
	 * 根据code从mvc的国际化配置文件中获取对应语言的代码值,如果代码值为空，则返回defaultMessage
	 * @param code
	 * @param defaultMessage
	 * @param request
	 * @return
	 */
	public static String getI18nMessage(String code,String defaultMessage)
	{
		return getI18nMessage(code,(Object[])null,defaultMessage,null);
		
		
	}
	
	/**
	 * 根据code从mvc的国际化配置文件中获取对应语言的代码值,如果代码值为空，则返回defaultMessage
	 * @param code
	 * @param defaultMessage
	 * @param request
	 * @return
	 */
	public static String getI18nMessage(String code)
	{
		return getI18nMessage(code,(Object[])null,(String)null,null);
		
		
	}
	/**
	 * 根据code从mvc的国际化配置文件中获取对应语言的代码值,并且将数组args中的每个元素替换到代码值中位置占位符，例如{0}会用数组的第一个元素替换
	 * @param code
	 * @param args
	 * @param request
	 * @return
	 */
	public static String getI18nMessage(String code,Object[] args,HttpServletRequest request)
	{
		return getI18nMessage(code,args,(String)null,request);
		
		
	}
	public static String getI18nMessage(String code,Object[] args)
	{
		return getI18nMessage( code, args,(String )null);
	}
	public static String getI18nMessage(String code,Object[] args,String defaultMessage)
	{
		return  getI18nMessage( code,args, defaultMessage,null);
	}
	/**
	 * 根据code从mvc的国际化配置文件中获取对应语言的代码值,如果代码值为空，则返回defaultMessage,并且将数组args中的每个元素替换到代码值中位置占位符，例如{0}会用数组的第一个元素替换
	 * @param code
	 * @param args
	 * @param defaultMessage
	 * @param request
	 * @return
	 */
	public static String getI18nMessage(String code,Object[] args,String defaultMessage,HttpServletRequest request)
	{
		if(request != null)
		{
			Locale locale = RequestContextUtils.getRequestContextLocal(request);
			MessageSource messageSource = WebApplicationContextUtils.getWebApplicationContext();
			if(messageSource != null)
				return  messageSource.getMessage(code, args, defaultMessage, locale);
			else
			{
				if(defaultMessage != null)
					return defaultMessage;
				else
					return code;
				
			}
		}
		else
		{
			
			Locale locale = DispatchServlet.getLocaleResolver().resolveLocale(null);
			MessageSource messageSource = WebApplicationContextUtils.getWebApplicationContext();
			if(messageSource != null)
				return  messageSource.getMessage(code, args, defaultMessage, locale);
			else
			{
				if(defaultMessage != null)
					return defaultMessage;
				else
					return code;
				
			}
		}
		
		
		
	}
	private static LocaleContext defaultLocalContext =  new SimpleLocaleContext(Locale.getDefault(),Locale.getDefault().toString());
	public static LocaleContext getLocalContext(HttpServletRequest request)
	{
		if(request != null)
		{
			
		
			LocaleContext lc = (LocaleContext)request.getAttribute(RequestContextUtils.LOCALE_CONTEXT_REQUEST_ATTRIBUTE);
			if(lc == null)
			{
				LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
				Locale locale = null;
				if (localeResolver != null) {
					// Try LocaleResolver (we're within a DispatcherServlet request).
					locale = localeResolver.resolveLocale(request);
				}
				else {
					// No LocaleResolver available -> try fallback.
					locale =  request.getLocale();
				}
				String localeName = locale.toString();
				lc = new SimpleLocaleContext(locale,localeName);
				request.setAttribute(RequestContextUtils.LOCALE_CONTEXT_REQUEST_ATTRIBUTE,lc);
			}
			return lc;
		}
		else
		{
			return defaultLocalContext;
		}
		
	}
	public static String getRequestContextLocalName(HttpServletRequest request)
	{
		LocaleContext lc = getLocalContext( request);
		return lc.getLocaleName();
		
		
	}
	
	
	
	public static void setLocale(HttpServletRequest request,
			HttpServletResponse response, String locale) {
		RequestContextUtils.getLocaleResolver(request).setLocale(request, response, locale);
		
	}

	public static void setLocale(HttpServletRequest request,
			HttpServletResponse response, Locale locale) {
		RequestContextUtils.getLocaleResolver(request).setLocale(request, response, locale);
		
	}
	


}
