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


import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;

import org.apache.log4j.Logger;
import org.frameworkset.spi.support.LocaleContext;
import org.frameworkset.spi.support.MessageSource;
import org.frameworkset.spi.support.MessageSourceResolvable;
import org.frameworkset.spi.support.NoSuchMessageException;
import org.frameworkset.spi.support.validate.Errors;
import org.frameworkset.spi.support.validate.EscapedErrors;
import org.frameworkset.util.Assert;
import org.frameworkset.util.ClassUtils;
import org.frameworkset.web.servlet.HandlerMapping;
import org.frameworkset.web.servlet.context.WebApplicationContext;
import org.frameworkset.web.servlet.theme.AbstractThemeResolver;
import org.frameworkset.web.servlet.view.InternalResourceView;
import org.frameworkset.web.ui.ThemeSource;
import org.frameworkset.web.ui.context.ResourceBundleThemeSource;
import org.frameworkset.web.ui.context.Theme;
import org.frameworkset.web.util.UrlPathHelper;
import org.frameworkset.web.util.WebUtils;

import com.frameworkset.util.HtmlUtils;


/**
 * <p>Title: RequestContext.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-28
 * @author biaoping.yin
 * @version 1.0
 */
public class RequestContext {
	private static final Logger logger = Logger.getLogger(RequestContext.class);
	/**
	 * Default theme name used if the RequestContext cannot find a ThemeResolver.
	 * Only applies to non-DispatcherServlet requests.
	 * <p>Same as AbstractThemeResolver's default, but not linked in here to
	 * avoid package interdependencies.
	 * @see AbstractThemeResolver#ORIGINAL_DEFAULT_THEME_NAME
	 */
	public static final String DEFAULT_THEME_NAME = "theme-classic";
	

	/**
	 * Request attribute to hold the current web application context for RequestContext usage.
	 * By default, the DispatcherServlet's context (or the root context as fallback) is exposed.
	 */
	public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = RequestContext.class.getName() + ".CONTEXT";
	/**
	 * Log category to use when no mapped handler is found for a request.
	 */
	public static final String BIND_ERROR_MESSAGES_KEY = "org.frameworkset.web.servlet.BIND_ERROR_MESSAGES_KEY";


	protected static final boolean jstlPresent = ClassUtils.isPresent(
			"javax.servlet.jsp.jstl.core.Config", JspAwareRequestContext.class.getClassLoader());


	private HttpServletRequest request;
	private HttpServletResponse response;

	private Map model;

	private WebApplicationContext webApplicationContext;

	private Locale locale;
	private String localeName;

	private Theme theme;

	private Boolean defaultHtmlEscape;

	private UrlPathHelper urlPathHelper;

	private Map errorsMap;


	/**
	 * Create a new RequestContext for the given request,
	 * using the request attributes for Errors retrieval.
	 * <p>This only works with InternalResourceViews, as Errors instances
	 * are part of the model and not normally exposed as request attributes.
	 * It will typically be used within JSPs or custom tags.
	 * <p><b>Will only work within a DispatcherServlet request.</b> Pass in a
	 * ServletContext to be able to fallback to the root WebApplicationContext.
	 * @param request current HTTP request
	 * @see DispatcherServlet
	 * @see #RequestContext(javax.servlet.http.HttpServletRequest, javax.servlet.ServletContext)
	 */
	public RequestContext(HttpServletRequest request) {
		initContext(request, null,null, null);
	}

	/**
	 * Create a new RequestContext for the given request,
	 * using the request attributes for Errors retrieval.
	 * <p>This only works with InternalResourceViews, as Errors instances
	 * are part of the model and not normally exposed as request attributes.
	 * It will typically be used within JSPs or custom tags.
	 * <p>If a ServletContext is specified, the RequestContext will also
	 * work with the root WebApplicationContext (outside a DispatcherServlet).
	 * @param request current HTTP request
	 * @param servletContext the servlet context of the web application
	 * (can be <code>null</code>; necessary for fallback to root WebApplicationContext)
	 * @see WebApplicationContext
	 * @see org.frameworkset.web.servlet.DispatchServlet
	 */
	public RequestContext(HttpServletRequest request, ServletContext servletContext) {
		initContext(request,null, servletContext, null);
	}

	/**
	 * Create a new RequestContext for the given request,
	 * using the given model attributes for Errors retrieval.
	 * <p>This works with all View implementations.
	 * It will typically be used by View implementations.
	 * <p><b>Will only work within a DispatcherServlet request.</b> Pass in a
	 * ServletContext to be able to fallback to the root WebApplicationContext.
	 * @param request current HTTP request
	 * @param model the model attributes for the current view
	 * (can be <code>null</code>, using the request attributes for Errors retrieval)
	 * @see org.frameworkset.web.servlet.DispatchServlet
	 * @see #RequestContext(javax.servlet.http.HttpServletRequest, javax.servlet.ServletContext, Map)
	 */
	public RequestContext(HttpServletRequest request, Map model) {
		initContext(request, null,null, model);
	}

	/**
	 * Create a new RequestContext for the given request,
	 * using the given model attributes for Errors retrieval.
	 * <p>This works with all View implementations.
	 * It will typically be used by View implementations.
	 * <p>If a ServletContext is specified, the RequestContext will also
	 * work with a root WebApplicationContext (outside a DispatcherServlet).
	 * @param request current HTTP request
	 * @param servletContext the servlet context of the web application
	 * (can be <code>null</code>; necessary for fallback to root WebApplicationContext)
	 * @param model the model attributes for the current view
	 * (can be <code>null</code>, using the request attributes for Errors retrieval)
	 * @see WebApplicationContext
	 * @see org.frameworkset.web.servlet.DispatchServlet
	 */
	public RequestContext(HttpServletRequest request, ServletContext servletContext, Map model) {
		initContext(request, null,servletContext, model);
	}
	
	/**
	 * Create a new RequestContext for the given request,
	 * using the given model attributes for Errors retrieval.
	 * <p>This works with all View implementations.
	 * It will typically be used by View implementations.
	 * <p>If a ServletContext is specified, the RequestContext will also
	 * work with a root WebApplicationContext (outside a DispatcherServlet).
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @param servletContext the servlet context of the web application
	 * (can be <code>null</code>; necessary for fallback to root WebApplicationContext)
	 * @param model the model attributes for the current view
	 * (can be <code>null</code>, using the request attributes for Errors retrieval)
	 * @see WebApplicationContext
	 * @see DispatcherServlet
	 */
	public RequestContext(HttpServletRequest request, HttpServletResponse response,
			ServletContext servletContext, Map<String, Object> model) {

		initContext(request, response, servletContext, model);
	}

	/**
	 * Default constructor for subclasses.
	 */
	protected RequestContext() {
	}


	/**
	 * Initialize this context with the given request,
	 * using the given model attributes for Errors retrieval.
	 * <p>Delegates to <code>getFallbackLocale</code> and <code>getFallbackTheme</code>
	 * for determining the fallback locale and theme, respectively, if no LocaleResolver
	 * and/or ThemeResolver can be found in the request.
	 * @param request current HTTP request
	 * @param servletContext the servlet context of the web application
	 * (can be <code>null</code>; necessary for fallback to root WebApplicationContext)
	 * @param model the model attributes for the current view
	 * (can be <code>null</code>, using the request attributes for Errors retrieval)
	 * @see #getFallbackLocale
	 * @see #getFallbackTheme
	 * @see org.frameworkset.web.servlet.DispatchServlet#LOCALE_RESOLVER_ATTRIBUTE
	 * @see org.frameworkset.web.servlet.DispatchServlet#THEME_RESOLVER_ATTRIBUTE
	 */
	protected void initContext(HttpServletRequest request,HttpServletResponse response, ServletContext servletContext, Map model) {
		this.request = request;
		this.model = model;
		this.response = response;

		// Fetch WebApplicationContext, either from DispatcherServlet or the root context.
		// ServletContext needs to be specified to be able to fall back to the root context!
		this.webApplicationContext =
				(WebApplicationContext) request.getAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		if (this.webApplicationContext == null) {
			this.webApplicationContext = RequestContextUtils.getWebApplicationContext(request, servletContext);
		}

		// Determine locale to use for this RequestContext.
//		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
//		if(localeResolver == null)
//			
//				localeResolver = DispatchServlet.getLocaleResolver(webApplicationContext);
//			
//		if (localeResolver != null) {
//			// Try LocaleResolver (we're within a DispatcherServlet request).
//			this.locale = localeResolver.resolveLocale(request);
//		}
//		else {
//			// No LocaleResolver available -> try fallback.
//			this.locale = getFallbackLocale();
//		}
//		localeName = this.locale.toString();
		LocaleContext lc = RequestContextUtils.getLocalContext(request);
		this.locale = lc.getLocale();
		this.localeName = lc.getLocaleName();
		
		
		// Determine default HTML escape setting from the "defaultHtmlEscape"
		// context-param in web.xml, if any.
		this.defaultHtmlEscape = WebUtils.getDefaultHtmlEscape(this.webApplicationContext.getServletContext());

		this.urlPathHelper = new UrlPathHelper();
	}
	
	
	/**
	 * Initialize this context with the given request,
	 * using the given model attributes for Errors retrieval.
	 * <p>Delegates to <code>getFallbackLocale</code> and <code>getFallbackTheme</code>
	 * for determining the fallback locale and theme, respectively, if no LocaleResolver
	 * and/or ThemeResolver can be found in the request.
	 * @param request current HTTP request
	 * @param servletContext the servlet context of the web application
	 * (can be <code>null</code>; necessary for fallback to root WebApplicationContext)
	 * @param model the model attributes for the current view
	 * (can be <code>null</code>, using the request attributes for Errors retrieval)
	 * @see #getFallbackLocale
	 * @see #getFallbackTheme
	 * @see org.frameworkset.web.servlet.DispatchServlet#LOCALE_RESOLVER_ATTRIBUTE
	 * @see org.frameworkset.web.servlet.DispatchServlet#THEME_RESOLVER_ATTRIBUTE
	 */
	protected void initContext(HttpServletRequest request, ServletContext servletContext, Map model) {
		initContext( request, null, servletContext,  model);
	}

	/**
	 * Determine the fallback locale for this context.
	 * <p>The default implementation checks for a JSTL locale attribute
	 * in request, session or application scope; if not found,
	 * returns the <code>HttpServletRequest.getLocale()</code>.
	 * @return the fallback locale (never <code>null</code>)
	 * @see javax.servlet.http.HttpServletRequest#getLocale()
	 */
	protected Locale getFallbackLocale() {
//		if (jstlPresent) {
//			Locale locale = JstlLocaleResolver.getJstlLocale(getRequest(), getServletContext());
//			if (locale != null) {
//				return locale;
//			}
//		}
		return getRequest().getLocale();
	}

	/**
	 * Determine the fallback theme for this context.
	 * <p>The default implementation returns the default theme (with name "theme").
	 * @return the fallback theme (never <code>null</code>)
	 */
	protected Theme getFallbackTheme() {
		ThemeSource themeSource = RequestContextUtils.getThemeSource(getRequest());
		if (themeSource == null) {
			themeSource = new ResourceBundleThemeSource();
		}
		Theme theme = themeSource.getTheme(DEFAULT_THEME_NAME);
		if (theme == null) {
			throw new IllegalStateException("No theme defined and no fallback theme found");
		}
		return theme;
	}


	/**
	 * Return the underlying HttpServletRequest.
	 * Only intended for cooperating classes in this package.
	 */
	protected final HttpServletRequest getRequest() {
		return this.request;
	}

	/**
	 * Return the underlying ServletContext.
	 * Only intended for cooperating classes in this package.
	 */
	protected final ServletContext getServletContext() {
		return this.webApplicationContext.getServletContext();
	}

	/**
	 * Return the current WebApplicationContext.
	 */
	public final WebApplicationContext getWebApplicationContext() {
		return this.webApplicationContext;
	}

	/**
	 * Return the current WebApplicationContext as MessageSource.
	 */
	public final MessageSource getMessageSource() {
		return this.webApplicationContext;
	}

	/**
	 * Return the current locale.
	 */
	public final Locale getLocale() {
		return this.locale;
	}

	/**
	 * Return the current theme (never <code>null</code>).
	 * Resolved lazily for more efficiency when theme support is not used.
	 */
	public final Theme getTheme() {
		if (this.theme == null) {
			// Lazily determine theme to use for this RequestContext.
			this.theme = RequestContextUtils.getTheme(this.request);
			if (this.theme == null) {
				// No ThemeResolver and ThemeSource available -> try fallback.
				this.theme = getFallbackTheme();
			}
		}
		return this.theme;
	}


	/**
	 * (De)activate default HTML escaping for messages and errors, for the scope
	 * of this RequestContext. The default is the application-wide setting
	 * (the "defaultHtmlEscape" context-param in web.xml).
	 * @see WebUtils#isDefaultHtmlEscape
	 */
	public void setDefaultHtmlEscape(boolean defaultHtmlEscape) {
		this.defaultHtmlEscape = Boolean.valueOf(defaultHtmlEscape);
	}

	/**
	 * Is default HTML escaping active?
	 * Falls back to <code>false</code> in case of no explicit default given.
	 */
	public boolean isDefaultHtmlEscape() {
		return (this.defaultHtmlEscape != null && this.defaultHtmlEscape.booleanValue());
	}

	/**
	 * Return the default HTML escape setting, differentiating
	 * between no default specified and an explicit value.
	 * @return whether default HTML escaping is enabled (null = no explicit default)
	 */
	public Boolean getDefaultHtmlEscape() {
		return this.defaultHtmlEscape;
	}

	/**
	 * Set the UrlPathHelper to use for context path and request URI decoding.
	 * Can be used to pass a shared UrlPathHelper instance in.
	 * <p>A default UrlPathHelper is always available.
	 */
	public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
		Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
		this.urlPathHelper = urlPathHelper;
	}

	/**
	 * Return the UrlPathHelper used for context path and request URI decoding.
	 * Can be used to configure the current UrlPathHelper.
	 * <p>A default UrlPathHelper is always available.
	 */
	public UrlPathHelper getUrlPathHelper() {
		return this.urlPathHelper;
	}


	/**
	 * Return the context path of the original request,
	 * that is, the path that indicates the current web application.
	 * This is useful for building links to other resources within the application.
	 * <p>Delegates to the UrlPathHelper for decoding.
	 * @see javax.servlet.http.HttpServletRequest#getContextPath
	 * @see #getUrlPathHelper
	 */
	public String getContextPath() {
		return this.urlPathHelper.getOriginatingContextPath(this.request);
	}

	/**
	 * Return the request URI of the original request, that is, the invoked URL
	 * without parameters. This is particularly useful as HTML form action target,
	 * possibly in combination with the original query string.
	 * <p><b>Note this implementation will correctly resolve to the URI of any
	 * originating root request in the presence of a forwarded request. However, this
	 * can only work when the Servlet 2.4 'forward' request attributes are present.
	 * For use in a Servlet 2.3 environment, you can rely on
	 * {@link InternalResourceView}
	 * to add these prior to dispatching the request.</b>
	 * <p>Delegates to the UrlPathHelper for decoding.
	 * @see #getQueryString
	 * @see org.frameworkset.web.util.UrlPathHelper#getOriginatingRequestUri
	 * @see #getUrlPathHelper
	 */
	public String getRequestUri() {
		return this.urlPathHelper.getOriginatingRequestUri(this.request);
	}

	/**
	 * Return the query string of the current request, that is, the part after
	 * the request path. This is particularly useful for building an HTML form
	 * action target in combination with the original request URI.
	 * <p><b>Note this implementation will correctly resolve to the query string of any
	 * originating root request in the presence of a forwarded request. However, this
	 * can only work when the Servlet 2.4 'forward' request attributes are present.
	 * For use in a Servlet 2.3 environment, you can rely on
	 * {@link InternalResourceView}
	 * to add these prior to dispatching the request.</b>
	 * <p>Delegates to the UrlPathHelper for decoding.
	 * @see #getRequestUri
	 * @see org.frameworkset.web.util.UrlPathHelper#getOriginatingQueryString
	 * @see #getUrlPathHelper
	 */
	public String getQueryString() {
		return this.urlPathHelper.getOriginatingQueryString(this.request);
	}


	/**
	 * Retrieve the message for the given code, using the "defaultHtmlEscape" setting.
	 * @param code code of the message
	 * @param defaultMessage String to return if the lookup fails
	 * @return the message
	 */
	public String getMessage(String code, String defaultMessage) {
		return getMessage(code, null, defaultMessage, isDefaultHtmlEscape());
	}

	/**
	 * Retrieve the message for the given code, using the "defaultHtmlEscape" setting.
	 * @param code code of the message
	 * @param args arguments for the message, or <code>null</code> if none
	 * @param defaultMessage String to return if the lookup fails
	 * @return the message
	 */
	public String getMessage(String code, Object[] args, String defaultMessage) {
		return getMessage(code, args, defaultMessage, isDefaultHtmlEscape());
	}

	/**
	 * Retrieve the message for the given code, using the "defaultHtmlEscape" setting.
	 * @param code code of the message
	 * @param args arguments for the message as a List, or <code>null</code> if none
	 * @param defaultMessage String to return if the lookup fails
	 * @return the message
	 */
	public String getMessage(String code, List args, String defaultMessage) {
		return getMessage(code, (args != null ? args.toArray() : null), defaultMessage, isDefaultHtmlEscape());
	}

	/**
	 * Retrieve the message for the given code.
	 * @param code code of the message
	 * @param args arguments for the message, or <code>null</code> if none
	 * @param defaultMessage String to return if the lookup fails
	 * @param htmlEscape HTML escape the message?
	 * @return the message
	 */
	public String getMessage(String code, Object[] args, String defaultMessage, boolean htmlEscape) {
		String msg = this.webApplicationContext.getMessage(code, args, defaultMessage, this.locale);
		return (htmlEscape ? HtmlUtils.htmlEscape(msg) : msg);
	}

	/**
	 * Retrieve the message for the given code, using the "defaultHtmlEscape" setting.
	 * @param code code of the message
	 * @return the message
	 * @throws NoSuchMessageException if not found
	 */
	public String getMessage(String code) throws NoSuchMessageException {
		return getMessage(code, null, isDefaultHtmlEscape());
	}

	/**
	 * Retrieve the message for the given code, using the "defaultHtmlEscape" setting.
	 * @param code code of the message
	 * @param args arguments for the message, or <code>null</code> if none
	 * @return the message
	 * @throws NoSuchMessageException if not found
	 */
	public String getMessage(String code, Object[] args) throws NoSuchMessageException {
		return getMessage(code, args, isDefaultHtmlEscape());
	}

	/**
	 * Retrieve the message for the given code, using the "defaultHtmlEscape" setting.
	 * @param code code of the message
	 * @param args arguments for the message as a List, or <code>null</code> if none
	 * @return the message
	 * @throws NoSuchMessageException if not found
	 */
	public String getMessage(String code, List args) throws NoSuchMessageException {
		return getMessage(code, (args != null ? args.toArray() : null), isDefaultHtmlEscape());
	}

	/**
	 * Retrieve the message for the given code.
	 * @param code code of the message
	 * @param args arguments for the message, or <code>null</code> if none
	 * @param htmlEscape HTML escape the message?
	 * @return the message
	 * @throws NoSuchMessageException if not found
	 */
	public String getMessage(String code, Object[] args, boolean htmlEscape) throws NoSuchMessageException {
		String msg = this.webApplicationContext.getMessage(code, args, this.locale);
		return (htmlEscape ? HtmlUtils.htmlEscape(msg) : msg);
	}

	/**
	 * Retrieve the given MessageSourceResolvable (e.g. an ObjectError instance),
	 * using the "defaultHtmlEscape" setting.
	 * @param resolvable the MessageSourceResolvable
	 * @return the message
	 * @throws NoSuchMessageException if not found
	 */
	public String getMessage(MessageSourceResolvable resolvable) throws NoSuchMessageException {
		return getMessage(resolvable, isDefaultHtmlEscape());
	}

	/**
	 * Retrieve the given MessageSourceResolvable (e.g. an ObjectError instance).
	 * @param resolvable the MessageSourceResolvable
	 * @param htmlEscape HTML escape the message?
	 * @return the message
	 * @throws NoSuchMessageException if not found
	 */
	public String getMessage(MessageSourceResolvable resolvable, boolean htmlEscape) throws NoSuchMessageException {
		String msg = this.webApplicationContext.getMessage(resolvable, this.locale);
		return (htmlEscape ? HtmlUtils.htmlEscape(msg) : msg);
	}


	/**
	 * Retrieve the theme message for the given code.
	 * <p>Note that theme messages are never HTML-escaped, as they typically
	 * denote theme-specific resource paths and not client-visible messages.
	 * @param code code of the message
	 * @param defaultMessage String to return if the lookup fails
	 * @return the message
	 */
	public String getThemeMessage(String code, String defaultMessage) {
		return getTheme().getMessageSource().getMessage(code, null, defaultMessage, this.locale);
	}

	/**
	 * Retrieve the theme message for the given code.
	 * <p>Note that theme messages are never HTML-escaped, as they typically
	 * denote theme-specific resource paths and not client-visible messages.
	 * @param code code of the message
	 * @param args arguments for the message, or <code>null</code> if none
	 * @param defaultMessage String to return if the lookup fails
	 * @return the message
	 */
	public String getThemeMessage(String code, Object[] args, String defaultMessage) {
		return getTheme().getMessageSource().getMessage(code, args, defaultMessage, this.locale);
	}

	/**
	 * Retrieve the theme message for the given code.
	 * <p>Note that theme messages are never HTML-escaped, as they typically
	 * denote theme-specific resource paths and not client-visible messages.
	 * @param code code of the message
	 * @param args arguments for the message as a List, or <code>null</code> if none
	 * @param defaultMessage String to return if the lookup fails
	 * @return the message
	 */
	public String getThemeMessage(String code, List args, String defaultMessage) {
		return getTheme().getMessageSource().getMessage(
				code, (args != null ? args.toArray() : null), defaultMessage, this.locale);
	}

	/**
	 * Retrieve the theme message for the given code.
	 * <p>Note that theme messages are never HTML-escaped, as they typically
	 * denote theme-specific resource paths and not client-visible messages.
	 * @param code code of the message
	 * @return the message
	 * @throws NoSuchMessageException if not found
	 */
	public String getThemeMessage(String code) throws NoSuchMessageException {
		return getTheme().getMessageSource().getMessage(code, null, this.locale);
	}

	/**
	 * Retrieve the theme message for the given code.
	 * <p>Note that theme messages are never HTML-escaped, as they typically
	 * denote theme-specific resource paths and not client-visible messages.
	 * @param code code of the message
	 * @param args arguments for the message, or <code>null</code> if none
	 * @return the message
	 * @throws NoSuchMessageException if not found
	 */
	public String getThemeMessage(String code, Object[] args) throws NoSuchMessageException {
		return getTheme().getMessageSource().getMessage(code, args, this.locale);
	}

	/**
	 * Retrieve the theme message for the given code.
	 * <p>Note that theme messages are never HTML-escaped, as they typically
	 * denote theme-specific resource paths and not client-visible messages.
	 * @param code code of the message
	 * @param args arguments for the message as a List, or <code>null</code> if none
	 * @return the message
	 * @throws NoSuchMessageException if not found
	 */
	public String getThemeMessage(String code, List args) throws NoSuchMessageException {
		return getTheme().getMessageSource().getMessage(
				code, (args != null ? args.toArray() : null), this.locale);
	}

	/**
	 * Retrieve the given MessageSourceResolvable in the current theme.
	 * <p>Note that theme messages are never HTML-escaped, as they typically
	 * denote theme-specific resource paths and not client-visible messages.
	 * @param resolvable the MessageSourceResolvable
	 * @return the message
	 * @throws NoSuchMessageException if not found
	 */
	public String getThemeMessage(MessageSourceResolvable resolvable) throws NoSuchMessageException {
		return getTheme().getMessageSource().getMessage(resolvable, this.locale);
	}


	/**
	 * Retrieve the Errors instance for the given bind object,
	 * using the "defaultHtmlEscape" setting.
	 * @param name name of the bind object
	 * @return the Errors instance, or <code>null</code> if not found
	 */
	public Errors getErrors(String name) {
		return getErrors(name, isDefaultHtmlEscape());
	}

	/**
	 * Retrieve the Errors instance for the given bind object.
	 * @param name name of the bind object
	 * @param htmlEscape create an Errors instance with automatic HTML escaping?
	 * @return the Errors instance, or <code>null</code> if not found
	 */
	public Errors getErrors(String name, boolean htmlEscape) {
		if (this.errorsMap == null) {
			this.errorsMap = new HashMap();
		}
		Errors errors = (Errors) this.errorsMap.get(name);
		boolean put = false;
		if (errors == null) {
			errors = (Errors)this.request.getAttribute(RequestContext.BIND_ERROR_MESSAGES_KEY);
//			errors = (Errors) getModelObject(BindingResult.MODEL_KEY_PREFIX + name);
//			// Check old BindException prefix for backwards compatibility.
//			if (errors == null) {
//				errors = (Errors) getModelObject(BindException.ERROR_KEY_PREFIX + name);
//			}
//			if (errors instanceof BindException) {
//				errors = ((BindException) errors).getBindingResult();
//			}
			if (errors == null) {
				return null;
			}
			put = true;
		}
		if (htmlEscape && !(errors instanceof EscapedErrors)) {
			errors = new EscapedErrors(errors);
			put = true;
		}
		else if (!htmlEscape && errors instanceof EscapedErrors) {
			errors = ((EscapedErrors) errors).getSource();
			put = true;
		}
		if (put) {
			this.errorsMap.put(name, errors);
		}
		return errors;
	}

	/**
	 * Retrieve the model object for the given model name,
	 * either from the model or from the request attributes.
	 * @param modelName the name of the model object
	 * @return the model object
	 */
	protected Object getModelObject(String modelName) {
		if (this.model != null) {
			return this.model.get(modelName);
		}
		else {
			return this.request.getAttribute(modelName);
		}
	}

//	/**
//	 * Create a BindStatus for the given bind object,
//	 * using the "defaultHtmlEscape" setting.
//	 * @param path the bean and property path for which values and errors
//	 * will be resolved (e.g. "person.age")
//	 * @return the new BindStatus instance
//	 * @throws IllegalStateException if no corresponding Errors object found
//	 */
//	public BindStatus getBindStatus(String path) throws IllegalStateException {
//		return new BindStatus(this, path, isDefaultHtmlEscape());
//	}

//	/**
//	 * Create a BindStatus for the given bind object,
//	 * using the "defaultHtmlEscape" setting.
//	 * @param path the bean and property path for which values and errors
//	 * will be resolved (e.g. "person.age")
//	 * @param htmlEscape create a BindStatus with automatic HTML escaping?
//	 * @return the new BindStatus instance
//	 * @throws IllegalStateException if no corresponding Errors object found
//	 */
//	public BindStatus getBindStatus(String path, boolean htmlEscape) throws IllegalStateException {
//		return new BindStatus(this, path, htmlEscape);
//	}
	
	/**
	 * Return a context-aware URl for the given relative URL.
	 * @param relativeUrl the relative URL part
	 * @return a URL that points back to the server with an absolute path
	 * (also URL-encoded accordingly)
	 */
	public String getContextUrl(String relativeUrl) {
		String url = getContextPath() + relativeUrl;
		if (this.response != null) {
			url = this.response.encodeURL(url);
		}
		return url;
	}


	/**
	 * Inner class that isolates the JSTL dependency.
	 * Just called to resolve the fallback locale if the JSTL API is present.
	 */
	private static class JstlLocaleResolver {

		public static Locale getJstlLocale(HttpServletRequest request, ServletContext servletContext) {
			Object localeObject = Config.get(request, Config.FMT_LOCALE);
			if (localeObject == null) {
				HttpSession session = request.getSession(false);
				if (session != null) {
					localeObject = Config.get(session, Config.FMT_LOCALE);
				}
				if (localeObject == null && servletContext != null) {
					localeObject = Config.get(servletContext, Config.FMT_LOCALE);
				}
			}
			return (localeObject instanceof Locale ? (Locale) localeObject : null);
		}
	}
	public static boolean isPagerMehtod(HttpServletRequest request)
	{
		return (Boolean)request.getAttribute(HandlerMapping.PAGER_METHOD_FLAG_ATTRIBUTE);
	}
	public static String getHandlerMappingPath(HttpServletRequest request)
	{
		return (String) request
		.getAttribute(org.frameworkset.web.servlet.HandlerMapping.HANDLER_MAPPING_PATH_ATTRIBUTE);
	}
	
	public static String getPathWithinHandlerMappingPath(HttpServletRequest request)
	{
//		String urlpath = (String) request
//		.getAttribute(org.frameworkset.web.servlet.HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
//		 if(urlpath != null && urlpath.startsWith("/") )
//		 {
//			 urlpath = request.getContextPath() + urlpath;
//		 }
//		 return urlpath;
		return (String) request
		.getAttribute(org.frameworkset.web.servlet.HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
	}
	
	public static String getWithinHandlerMappingPathIfUseContextPath(HttpServletRequest request)
	{
		String urlpath = (String) getPathWithinHandlerMappingPath(request);
		 if(urlpath != null && urlpath.startsWith("/") )
		 {
			 urlpath = request.getContextPath() + urlpath;
		 }
		 return urlpath;
//		return (String) request
//		.getAttribute(org.frameworkset.web.servlet.HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
	}
	public static final String COOKIE_PREFIX = "pager.";
	public static String getPagerSizeCookieID(HttpServletRequest request,String paramNamePrefix)
	{
		String baseUri = RequestContext.getHandlerMappingPath(request);
		String cookieid = paramNamePrefix == null ?
				COOKIE_PREFIX + baseUri :
					COOKIE_PREFIX + baseUri + "|" +paramNamePrefix;
		return cookieid;
	}
	
	public static int getPagerSize(HttpServletRequest request,Object defauleValue,String cookieid)
	{
//		
//		String cookieid = paramNamePrefix == null ?
//							PagerDataSet.COOKIE_PREFIX + baseUri :
//								PagerDataSet.COOKIE_PREFIX + baseUri + "|" +paramNamePrefix;
//		int default_ = HandlerMapping.DEFAULT_PAGE_SIZE;
//		if(defauleValue == null || defauleValue.equals(ValueConstants.DEFAULT_NONE) )
//			default_ = HandlerMapping.DEFAULT_PAGE_SIZE;
//		else {
//			try {
//				default_ = Integer.parseInt(String.valueOf(defauleValue));
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//		}
		int default_ = getCustomPageSize( defauleValue);
		int defaultSize =  consumeCookie(cookieid,default_,request,null);
		
		return defaultSize;
	}
	 public static  Cookie[] getPageCookies(HttpServletRequest request) {
//			HttpServletRequest request = this.getHttpServletRequest();
//	        HttpSession session = request.getSession(false);
			Cookie[] cookies = request.getCookies();
			if (null == cookies) {
				cookies = new Cookie[0];
			}
			return cookies;
		}
	 public static  boolean isPagerCookie(final Cookie cookie) {
			return 0 == cookie.getName().indexOf(COOKIE_PREFIX)	;
		}
	 
	 public static  boolean isCookieForThisPagerTag(final Cookie cookie,String cookieid,String pageId) {
			
			if(pageId != null)
				return cookie.getName().equals(cookieid);
			else
			{
				return cookie.getName().equals(cookieid);
			}
		}
	 public static int consumeCookie(String cookieid,int defaultsize,HttpServletRequest request,String pageId) {
			
			Cookie[] cookies = getPageCookies(request);
			Cookie cookie;
			
			for (int i = 0; i < cookies.length; i++) {
				cookie = cookies[i];
				if (isPagerCookie(cookie)) {				
					if (isCookieForThisPagerTag(cookie,cookieid,pageId)) {
							try {
								return Integer.parseInt(cookie.getValue());
							} catch (Exception e) {
								return defaultsize;
							}
						
					}
				}
			}
			return defaultsize;
		}
	
	public static int getCustomPageSize(Object defauleValue)
	{
		int default_ = HandlerMapping.DEFAULT_PAGE_SIZE;
		if(defauleValue == null  )
			default_ = HandlerMapping.DEFAULT_PAGE_SIZE;
		else {
			try {
				default_ = Integer.parseInt(String.valueOf(defauleValue));
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return default_;
	}

	public String getLocaleName() {
		return localeName;
	}

}
