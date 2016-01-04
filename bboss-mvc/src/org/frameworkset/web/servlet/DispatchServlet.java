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

import java.io.IOException;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;
import org.frameworkset.http.converter.HttpMessageConverter;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.assemble.Pro;
import org.frameworkset.spi.assemble.ProList;
import org.frameworkset.spi.event.IocLifeCycleEventListener;
import org.frameworkset.spi.io.PropertiesLoaderUtils;
import org.frameworkset.spi.support.LocaleContext;
import org.frameworkset.spi.support.LocaleContextHolder;
import org.frameworkset.spi.support.SimpleLocaleContext;
import org.frameworkset.util.ClassUtils;
import org.frameworkset.util.DataFormatUtil;
import org.frameworkset.util.beans.BeansException;
import org.frameworkset.util.io.ClassPathResource;
import org.frameworkset.web.HttpRequestMethodNotSupportedException;
import org.frameworkset.web.multipart.MultipartException;
import org.frameworkset.web.multipart.MultipartHttpServletRequest;
import org.frameworkset.web.multipart.MultipartResolver;
import org.frameworkset.web.servlet.context.RequestAttributes;
import org.frameworkset.web.servlet.context.RequestContextHolder;
import org.frameworkset.web.servlet.context.ServletRequestAttributes;
import org.frameworkset.web.servlet.context.WebApplicationContext;
import org.frameworkset.web.servlet.handler.AbstractUrlHandlerMapping;
import org.frameworkset.web.servlet.handler.HandlerMappingsTable;
import org.frameworkset.web.servlet.handler.HandlerMeta;
import org.frameworkset.web.servlet.handler.HandlerUtils;
import org.frameworkset.web.servlet.handler.PathURLNotSetException;
import org.frameworkset.web.servlet.handler.annotations.AnnotationMethodHandlerAdapter;
import org.frameworkset.web.servlet.handler.annotations.DefaultAnnotationHandlerMapping;
import org.frameworkset.web.servlet.i18n.DefaultLocaleResolver;
import org.frameworkset.web.servlet.mvc.HttpRequestHandlerAdapter;
import org.frameworkset.web.servlet.mvc.SimpleControllerHandlerAdapter;
import org.frameworkset.web.servlet.support.RequestContext;
import org.frameworkset.web.servlet.support.RequestContextUtils;
import org.frameworkset.web.servlet.support.RequestMethodHttpServletRequest;
import org.frameworkset.web.servlet.support.WebApplicationContextUtils;
import org.frameworkset.web.servlet.view.AbstractUrlBasedView;
import org.frameworkset.web.servlet.view.UrlBasedViewResolver;
import org.frameworkset.web.servlet.view.View;
import org.frameworkset.web.servlet.view.ViewResolver;
import org.frameworkset.web.ui.ThemeSource;
import org.frameworkset.web.util.UrlPathHelper;
import org.frameworkset.web.util.WebUtils;

import com.frameworkset.spi.assemble.BeanInstanceException;
import com.frameworkset.util.StringUtil;



/**
 * <p>Title: DispatchServlet.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-22
 * @author biaoping.yin
 * @version 1.0
 */
public class DispatchServlet extends HttpServlet {
	private static  Properties defaultStrategies;
	
	private String iocLifeCycleEventListeners;
	private String iocLifeCycleEventListenerParams;
	private List<IocLifeCycleEventListener> iocLifeCycleEventListenerList ;
	
	public static Properties getDefaultStrategies()
	{
		return defaultStrategies;
	}
	
	
	
	public static void destory()
	{
		DispatchServlet.defaultStrategies = null;
		DispatchServlet.localeResolver = null;
		DispatchServlet.viewResolvers = null;
		DispatchServlet.webApplicationContext = null;
		
				
	}
	
	/**
	 * Name of the class path resource (relative to the DispatcherServlet class)
	 * that defines DispatcherServlet's default strategy names.
	 */
	private static final String DEFAULT_STRATEGIES_PATH = "DispatcherServlet.properties";
	private static Logger logger = Logger.getLogger(DispatchServlet.class);
	static {
		// Load default strategy implementations from properties file.
		// This is currently strictly internal and not meant to be customized
		// by application developers.
		try {
//			
			
			ClassPathResource resource = new ClassPathResource(DEFAULT_STRATEGIES_PATH, DispatchServlet.class);
			defaultStrategies = PropertiesLoaderUtils.loadProperties(resource);
		}
		catch (IOException ex) {
			throw new IllegalStateException("Could not load 'DispatcherServlet.properties': " + ex.getMessage());
		}
	}
	
	/** Should we dispatch an HTTP OPTIONS request to {@link #doService}? */
	private boolean dispatchOptionsRequest = false;

	/** Should we dispatch an HTTP TRACE request to {@link #doService}? */
	private boolean dispatchTraceRequest = false;
	
	/**
	 * Well-known name for the MultipartResolver object in the bean factory for this namespace.
	 */
	public static final String MULTIPART_RESOLVER_BEAN_NAME = "multipartResolver";

	/**
	 * Well-known name for the LocaleResolver object in the bean factory for this namespace.
	 */
	public static final String LOCALE_RESOLVER_BEAN_NAME = "localeResolver";

	/**
	 * Well-known name for the ThemeResolver object in the bean factory for this namespace.
	 */
	public static final String THEME_RESOLVER_BEAN_NAME = "themeResolver";

	/**
	 * Well-known name for the HandlerMapping object in the bean factory for this namespace.
	 * Only used when "detectAllHandlerMappings" is turned off.
	 * @see #setDetectAllHandlerMappings
	 */
	public static final String HANDLER_MAPPING_BEAN_NAME = "handlerMapping";

	/**
	 * Well-known name for the HandlerAdapter object in the bean factory for this namespace.
	 * Only used when "detectAllHandlerAdapters" is turned off.
	 * @see #setDetectAllHandlerAdapters
	 */
//	public static final String HANDLER_ADAPTER_BEAN_NAME = "handlerAdapter";

	/**
	 * Well-known name for the HandlerExceptionResolver object in the bean factory for this
	 * namespace. Only used when "detectAllHandlerExceptionResolvers" is turned off.
	 * @see #setDetectAllHandlerExceptionResolvers
	 */
	public static final String HANDLER_EXCEPTION_RESOLVER_BEAN_NAME = "handlerExceptionResolver";

	/**
	 * Well-known name for the RequestToViewNameTranslator object in the bean factory for
	 * this namespace.
	 */
	public static final String REQUEST_TO_VIEW_NAME_TRANSLATOR_BEAN_NAME = "viewNameTranslator";

	/**
	 * Well-known name for the ViewResolver object in the bean factory for this namespace.
	 * Only used when "detectAllViewResolvers" is turned off.
	 * @see #setDetectAllViewResolvers
	 */
	public static final String VIEW_RESOLVER_BEAN_NAME = "viewResolver";

	/**
	 * Request attribute to hold the currently chosen HandlerExecutionChain.
	 * Only used for internal optimizations.
	 */
	public static final String HANDLER_EXECUTION_CHAIN_ATTRIBUTE = DispatchServlet.class.getName() + ".HANDLER";

	/**
	 * Request attribute to hold the current web application context.
	 * Otherwise only the global web app context is obtainable by tags etc.
	 * @see RequestContextUtils#getWebApplicationContext
	 */
	public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = DispatchServlet.class.getName() + ".CONTEXT";

	/**
	 * Request attribute to hold the current LocaleResolver, retrievable by views.
	 * @see RequestContextUtils#getLocaleResolver
	 */
	public static final String LOCALE_RESOLVER_ATTRIBUTE = DispatchServlet.class.getName() + ".LOCALE_RESOLVER";

	/**
	 * Request attribute to hold the current ThemeResolver, retrievable by views.
	 * @see RequestContextUtils#getThemeResolver
	 */
	public static final String THEME_RESOLVER_ATTRIBUTE = DispatchServlet.class.getName() + ".THEME_RESOLVER";

	/**
	 * Request attribute to hold the current ThemeSource, retrievable by views.
	 * @see RequestContextUtils#getThemeSource
	 */
	public static final String THEME_SOURCE_ATTRIBUTE = DispatchServlet.class.getName() + ".THEME_SOURCE";


	/**
	 * Log category to use when no mapped handler is found for a request.
	 */
	public static final String PAGE_NOT_FOUND_LOG_CATEGORY = "org.frameworkset.web.servlet.PageNotFound";
	
	
	/**
	 * Log category to use when no mapped handler is found for a request.
	 */
	public static final String messageConverters_KEY = "org.frameworkset.web.servlet.messageConverters_KEY";
	
	




	/**
	 * Additional logger to use when no mapped handler is found for a request.
	 */
	protected static final Logger pageNotFoundLogger = Logger.getLogger(PAGE_NOT_FOUND_LOG_CATEGORY);
	
	/** List of ViewResolvers used by this servlet */
	private static List viewResolvers;
	
	
	
	
	/** List of HandlerMappings used by this servlet */
	private HandlerMappingsTable handlerMappings;
	
	
	/** List of HandlerAdapters used by this servlet */
//	private List<HandlerAdapter> handlerAdapters;
	
	org.frameworkset.web.servlet.handler.annotations.AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter;
	org.frameworkset.web.servlet.mvc.SimpleControllerHandlerAdapter simpleControllerHandlerAdapter;	
	org.frameworkset.web.servlet.mvc.HttpRequestHandlerAdapter httpRequestHandlerAdapter; 
	
	/** List of HandlerAdapters used by this servlet */
	private List<HandlerInterceptor> gloabelHandlerInterceptors;
	
 
	private  HttpMessageConverter[] messageConverters;	

	
	/** Expose LocaleContext and RequestAttributes as inheritable for child threads? */
	private static boolean threadContextInheritable = false;
	
	/** MultipartResolver used by this servlet */
	private MultipartResolver multipartResolver;
	
	/** RequestToViewNameTranslator used by this servlet */
	private RequestToViewNameTranslator viewNameTranslator;
	
	/** List of HandlerExceptionResolvers used by this servlet */
	private List handlerExceptionResolvers;
	
	/** WebApplicationContext for this servlet */
	public  static WebApplicationContext webApplicationContext;
	private static String messagesources = null;
	private static String useCodeAsDefaultMessage = "true";
	public static String getMessagesources()
	{
		return messagesources;
	}
	
	/**
	 * Set whether this servlet should dispatch an HTTP OPTIONS request to
	 * the {@link #doService} method.
	 * <p>Default is "false", applying {@link javax.servlet.http.HttpServlet}'s
	 * default behavior (i.e. enumerating all standard HTTP request methods
	 * as a response to the OPTIONS request).
	 * <p>Turn this flag on if you prefer OPTIONS requests to go through the
	 * regular dispatching chain, just like other HTTP requests. This usually
	 * means that your controllers will receive those requests; make sure
	 * that those endpoints are actually able to handle an OPTIONS request.
	 * <p>Note that HttpServlet's default OPTIONS processing will be applied
	 * in any case. Your controllers are simply available to override the
	 * default headers and optionally generate a response body.
	 */
	public void setDispatchOptionsRequest(boolean dispatchOptionsRequest) {
		this.dispatchOptionsRequest = dispatchOptionsRequest;
	}

	/**
	 * Set whether this servlet should dispatch an HTTP TRACE request to
	 * the {@link #doService} method.
	 * <p>Default is "false", applying {@link javax.servlet.http.HttpServlet}'s
	 * default behavior (i.e. reflecting the message received back to the client).
	 * <p>Turn this flag on if you prefer TRACE requests to go through the
	 * regular dispatching chain, just like other HTTP requests. This usually
	 * means that your controllers will receive those requests; make sure
	 * that those endpoints are actually able to handle a TRACE request.
	 * <p>Note that HttpServlet's default TRACE processing will be applied
	 * in any case. Your controllers are simply available to override the
	 * default headers and the default body, calling <code>response.reset()</code>
	 * if necessary.
	 */
	public void setDispatchTraceRequest(boolean dispatchTraceRequest) {
		this.dispatchTraceRequest = dispatchTraceRequest;
	}


	/**
	 * Override HttpServlet's <code>getLastModified</code> method to evaluate
	 * the Last-Modified value of the mapped handler.
	 */
	protected long getLastModified(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			String requestUri = new UrlPathHelper().getRequestUri(request);
			logger.debug("DispatcherServlet with name '" + getServletName() +
					"' determining Last-Modified value for [" + requestUri + "]");
		}
		try {
			if(request instanceof RequestMethodHttpServletRequest)
			{
				
			}
			else
			{
				request = new RequestMethodHttpServletRequest(request);
			}
			HandlerExecutionChain mappedHandler = getHandler(request, true);
			if (mappedHandler == null || mappedHandler.getHandler() == null || mappedHandler.getHandler().getHandler() == null) {
				// Ignore -> will reappear on doService.
				logger.debug("No handler found in getLastModified");
				return -1;
			}

			HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());
			long lastModified = ha.getLastModified(request, mappedHandler.getHandler());
			if (logger.isInfoEnabled()) {
				String requestUri = new UrlPathHelper().getRequestUri(request);
				logger.debug("Last-Modified value for [" + requestUri + "] is: " + lastModified);
			}
			return lastModified;
		}
		catch (Exception ex) {
			// Ignore -> will reappear on doService.
			logger.error("Exception thrown in getLastModified", ex);
			return -1;
		}
	}
	
	
	/**
	 * Return the HandlerExecutionChain for this request.
	 * Try all handler mappings in order.
	 * @param request current HTTP request
	 * @param cache whether to cache the HandlerExecutionChain in a request attribute
	 * @return the HandlerExceutionChain, or <code>null</code> if no handler could be found
	 */
	protected HandlerExecutionChain getHandler(HttpServletRequest request, boolean cache) throws Exception {
		HandlerExecutionChain handler =
				(HandlerExecutionChain) request.getAttribute(HANDLER_EXECUTION_CHAIN_ATTRIBUTE);
		if (handler != null) {
			if (!cache) {
				request.removeAttribute(HANDLER_EXECUTION_CHAIN_ATTRIBUTE);
			}
			return handler;
		}
		handler = this.handlerMappings.getHandler(request, this.getServletName());
		if (handler != null) {
			if (cache) {
				request.setAttribute(HANDLER_EXECUTION_CHAIN_ATTRIBUTE, handler);
			}
			return handler;
		}
//		Iterator it = this.handlerMappings.iterator();
//		while (it.hasNext()) {
//			HandlerMapping hm = (HandlerMapping) it.next();
//			if (logger.isTraceEnabled()) {
//				logger.trace("Testing handler map [" + hm  + "] in DispatcherServlet with name '" +
//						getServletName() + "'");
//			}
//			handler = hm.getHandler(request);
//			if (handler != null) {
//				if (cache) {
//					request.setAttribute(HANDLER_EXECUTION_CHAIN_ATTRIBUTE, handler);
//				}
//				return handler;
//			}
//		}
		return null;
	}
	
	/**
	 * Return the HandlerAdapter for this handler object.
	 * @param handler the handler object to find an adapter for
	 * @throws ServletException if no HandlerAdapter can be found for the handler.
	 * This is a fatal error.
	 */
	protected HandlerAdapter getHandlerAdapter(HandlerMeta handler) throws ServletException {
//		Iterator it = this.handlerAdapters.iterator();
//		while (it.hasNext()) {
//			HandlerAdapter ha = (HandlerAdapter) it.next();
//		
//			if (ha.supports(handler)) {
//				return ha;
//			}
//		}
		if(this.annotationMethodHandlerAdapter.supports(handler))
			return this.annotationMethodHandlerAdapter;
		else if(this.simpleControllerHandlerAdapter.supports(handler))
			return this.simpleControllerHandlerAdapter;
		else if(this.httpRequestHandlerAdapter.supports(handler))
			return simpleControllerHandlerAdapter;
		throw new ServletException("No adapter for handler [" + handler.getHandlerName()  +
				"]: Does your handler implement a supported interface like Controller?");
	}
	
	/**
	 * Process this request, publishing an event regardless of the outcome.
	 * <p>The actual event handling is performed by the abstract
	 * {@link #doService} template method.
	 */
	protected final void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		long startTime = System.currentTimeMillis();
		Throwable failureCause = null;

		try {
			doService(request, response);
		}
		catch (ServletException ex) {
			failureCause = ex;
			throw ex;
		}
		catch (IOException ex) {
			failureCause = ex;
			throw ex;
		}
		catch (Throwable ex) {
			failureCause = ex;
			throw new NestedServletException("Request processing failed", ex);
		}

		finally {
			if (failureCause != null) {
//				this.logger.debug("Could not complete request", failureCause);
			}
			else {
				this.logger.debug("Successfully completed request");
			}
//			if (this.publishEvents) {
//				// Whether or not we succeeded, publish an event.
//				long processingTime = System.currentTimeMillis() - startTime;
//				this.webApplicationContext.publishEvent(
//						new ServletRequestHandledEvent(this,
//								request.getRequestURI(), request.getRemoteAddr(),
//								request.getMethod(), getServletConfig().getServletName(),
//								WebUtils.getSessionId(request), getUsernameForRequest(request),
//								processingTime, failureCause));
//			}
		}
	}
	
	/**
	 * No handler found -> set appropriate HTTP response status.
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @throws Exception if preparing the response failed
	 */
	protected void noHandlerFound(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		if (pageNotFoundLogger.isWarnEnabled()) 
		{
			String requestUri = new UrlPathHelper().getRequestUri(request);
			pageNotFoundLogger.warn("No mapping found for HTTP request with URI [" +
					requestUri + "] in DispatcherServlet with name '" + getServletName() + "'");
		}
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
	}
	
	private String contextAttribute = "contextConfigLocation";
	private boolean publishContext;
	private boolean cleanupAfterInclude;
		/** LocaleResolver used by this servlet */
	private static LocaleResolver localeResolver;
	
	/** ThemeResolver used by this servlet */
	private ThemeResolver themeResolver;
	private String getContextAttributeName()
	{
		return contextAttribute;
	}
	/**
	 * Retrieve a <code>WebApplicationContext</code> from the <code>ServletContext</code>
	 * attribute with the {@link #setContextAttribute configured name}. The
	 * <code>WebApplicationContext</code> must have already been loaded and stored in the
	 * <code>ServletContext</code> before this servlet gets initialized (or invoked).
	 * <p>Subclasses may override this method to provide a different
	 * <code>WebApplicationContext</code> retrieval strategy.
	 * @return the WebApplicationContext for this servlet, or <code>null</code> if not found
	 * @see #getContextAttribute()
	 */
	protected WebApplicationContext findWebApplicationContext() {
		String attrName = getContextAttributeName();
		if (attrName == null) {
			return null;
		}
		WebApplicationContext wac =
				WebApplicationContextUtils.getWebApplicationContext(getServletContext(), attrName);
//		if (wac == null) {
//			throw new IllegalStateException("No WebApplicationContext found: initializer not registered?");
//		}
		return wac;
	}
	/**
	 * Overridden method of {@link HttpServletBean}, invoked after any bean properties
	 * have been set. Creates this servlet's WebApplicationContext.
	 * @throws Exception 
	 */
	protected final void initServletBean(ServletConfig config) throws Exception {
		getServletContext().log("Initializing Bboss MVC FrameworkServlet '" + getServletName() + "'");
//		if (this.logger.isInfoEnabled()) {
			this.logger.debug("FrameworkServlet '" + getServletName() + "': initialization started");
//		}
		long startTime = System.currentTimeMillis();

		try {
			initWebApplicationContext( config);
//			initFrameworkServlet();
		}
//		catch (ServletException ex) {
//			this.logger.error("Context initialization failed", ex);
//			throw ex;
//		}
		catch (Exception ex) {
			this.logger.error("Context initialization failed", ex);
			throw ex;
		}

//		if (this.logger.isInfoEnabled()) {
			long elapsedTime = System.currentTimeMillis() - startTime;
			this.logger.debug("FrameworkServlet '" + getServletName() + "': initialization completed in " +
					elapsedTime + " ms");
//		}
	}
	
	protected final void publishWebService(ServletConfig config)
	{
		String wsloadclass = "org.frameworkset.spi.remote.webservice.WSLoader";
		Method publishAllWebService = null;
		try {
			
			Class clazz = Class.forName(wsloadclass);
			publishAllWebService = clazz.getMethod("publishAllWebService", ClassLoader.class,ServletConfig.class);
			
			
//			WSLoader.publishAllWebService(this.getClass().getClassLoader(),config);
			
		} catch (Throwable e) {
			logger.debug(" Not found "+wsloadclass + " or "+e.getMessage()+" in classpath,Ignore publish mvc webservices.");
		}
		
		try {
			if(publishAllWebService != null)
			{
				logger.debug("Publish MVC webservice start.");
				publishAllWebService.invoke(null, this.getClass().getClassLoader(),config);
				logger.debug("Publish MVC webservice finished.");
			}
		} catch (Exception e) {
			logger.debug("Publish mvc webservices failed:",e);
		} 
		
	}
	
	/**
	 * Initialize and publish the WebApplicationContext for this servlet.
	 * <p>Delegates to {@link #createWebApplicationContext} for actual creation
	 * of the context. Can be overridden in subclasses.
	 * @return the WebApplicationContext instance
	 * @throws BeansException if the context couldn't be initialized
	 * @see #setContextClass
	 * @see #setContextConfigLocation
	 */
	protected WebApplicationContext initWebApplicationContext(ServletConfig config) {
		WebApplicationContext wac = findWebApplicationContext();
		if (wac == null) {
//			// No fixed context defined for this servlet - create a local one.
//			WebApplicationContext parent =
//					WebApplicationContextUtils.getWebApplicationContext(getServletContext());
			wac = createWebApplicationContext( config);
		}

//		if (!this.refreshEventReceived) {
//			// Apparently not a ConfigurableApplicationContext with refresh support:
//			// triggering initial onRefresh manually here.
//			onRefresh(wac);
//		}

		if (this.publishContext) {
			// Publish the context as a servlet context attribute.
			String attrName = getServletContextAttributeName();
			getServletContext().setAttribute(attrName, wac);
			if (this.logger.isDebugEnabled()) {
				this.logger.debug("Published WebApplicationContext of servlet '" + getServletName() +
						"' as ServletContext attribute with name [" + attrName + "]");
			}
		}

		return wac;
	}
	/**
	 * Prefix for the ServletContext attribute for the WebApplicationContext.
	 * The completion is the servlet name.
	 */
	public static final String SERVLET_CONTEXT_PREFIX = DispatchServlet.class.getName() + ".CONTEXT.";
	/**
	 * Return the ServletContext attribute name for this servlet's WebApplicationContext.
	 * <p>The default implementation returns
	 * <code>SERVLET_CONTEXT_PREFIX + servlet name</code>.
	 * @see #SERVLET_CONTEXT_PREFIX
	 * @see #getServletName
	 */
	public String getServletContextAttributeName() {
		return SERVLET_CONTEXT_PREFIX + getServletName();
	}

	
	/**
	 * Instantiate the WebApplicationContext for this servlet, either a default
	 * 
	 * or a {@link #setContextClass custom context class}, if set.
	 * <p>This implementation expects custom contexts to implement the

	 * interface. Can be overridden in subclasses.
	 * <p>Do not forget to register this servlet instance as application listener on the
	 * created context (for triggering its {@link #onRefresh callback}, and to call

	 * before returning the context instance.
	 * @param parent the parent ApplicationContext to use, or <code>null</code> if none
	 * @return the WebApplicationContext for this servlet
	 * @throws BeansException if the context couldn't be initialized

	 */
	protected WebApplicationContext createWebApplicationContext(ServletConfig config)
	{
		String contextConfigLocation = config.getInitParameter("contextConfigLocation");
		//start event
		
		webApplicationContext = WebApplicationContext.getWebApplicationContext(config.getServletContext(),contextConfigLocation);
		
		return webApplicationContext;
	}
	
	/**
	 * Exposes the DispatcherServlet-specific request attributes and
	 * delegates to {@link #doDispatch} for the actual dispatching.
	 */
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (logger.isDebugEnabled()) {
			String requestUri = new UrlPathHelper().getRequestUri(request);
			logger.debug("DispatcherServlet with name '" + getServletName() +
					"' processing request for [" + requestUri + "]");
		}

		// Keep a snapshot of the request attributes in case of an include,
		// to be able to restore the original attributes after the include.
		Map attributesSnapshot = null;
		if (WebUtils.isIncludeRequest(request)) {
			logger.debug("Taking snapshot of request attributes before include");
			attributesSnapshot = new HashMap();
			Enumeration attrNames = request.getAttributeNames();
			while (attrNames.hasMoreElements()) {
				String attrName = (String) attrNames.nextElement();
				if (this.cleanupAfterInclude || attrName.startsWith("org.frameworkset.web.servlet")) {
					attributesSnapshot.put(attrName, request.getAttribute(attrName));
				}
			}
		}

		// Make framework objects available to handlers and view objects.
		request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, getWebApplicationContext());
		request.setAttribute(LOCALE_RESOLVER_ATTRIBUTE, this.localeResolver);
		request.setAttribute(THEME_RESOLVER_ATTRIBUTE, this.themeResolver);
		request.setAttribute(THEME_SOURCE_ATTRIBUTE, getThemeSource());

		try {
			DataFormatUtil.initDateformatThreadLocal();
			doDispatch(request, response);
		}
		finally {
			DataFormatUtil.releaseDateformatThreadLocal();
			// Restore the original attribute snapshot, in case of an include.
			if (attributesSnapshot != null) {
				restoreAttributesAfterInclude(request, attributesSnapshot);
			}
		}
	}
	
	/**
	 * Restore the request attributes after an include.
	 * @param request current HTTP request
	 * @param attributesSnapshot the snapshot of the request attributes
	 * before the include
	 */
	private void restoreAttributesAfterInclude(HttpServletRequest request, Map attributesSnapshot) {
		logger.debug("Restoring snapshot of request attributes after include");

		// Need to copy into separate Collection here, to avoid side effects
		// on the Enumeration when removing attributes.
		Set attrsToCheck = new HashSet();
		Enumeration attrNames = request.getAttributeNames();
		while (attrNames.hasMoreElements()) {
			String attrName = (String) attrNames.nextElement();
			if (this.cleanupAfterInclude || attrName.startsWith("org.frameworkset.web.servlet")) {
				attrsToCheck.add(attrName);
			}
		}

		// Iterate over the attributes to check, restoring the original value
		// or removing the attribute, respectively, if appropriate.
		for (Iterator it = attrsToCheck.iterator(); it.hasNext();) {
			String attrName = (String) it.next();
			Object attrValue = attributesSnapshot.get(attrName);
			if (attrValue != null) {
				if (logger.isDebugEnabled()) {
					logger.debug("Restoring original value of attribute [" + attrName + "] after include");
				}
				request.setAttribute(attrName, attrValue);
			}
			else {
				if (logger.isDebugEnabled()) {
					logger.debug("Removing attribute [" + attrName + "] after include");
				}
				request.removeAttribute(attrName);
			}
		}
	}
	/**
	 * Return this servlet's ThemeSource, if any; else return <code>null</code>.
	 * <p>Default is to return the WebApplicationContext as ThemeSource,
	 * provided that it implements the ThemeSource interface.
	 * @return the ThemeSource, if any
	 * @see #getWebApplicationContext()
	 */
	public final ThemeSource getThemeSource() {
		if (getWebApplicationContext() instanceof ThemeSource) {
			return (ThemeSource) getWebApplicationContext();
		}
		else {
			return null;
		}
	}
	
	/**
	 * 设置语言环境
	 * @param request
	 */
	public static void setLocaleContext(final HttpServletRequest request)
	{
		LocaleContextHolder.setLocaleContext(buildLocaleContext(request), threadContextInheritable);
	}
	/**
	 * Process the actual dispatching to the handler.
	 * <p>The handler will be obtained by applying the servlet's HandlerMappings in order.
	 * The HandlerAdapter will be obtained by querying the servlet's installed
	 * HandlerAdapters to find the first that supports the handler class.
	 * <p>All HTTP methods are handled by this method. It's up to HandlerAdapters or
	 * handlers themselves to decide which methods are acceptable.
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @throws Exception in case of any kind of processing failure
	 */
	protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request = new RequestMethodHttpServletRequest(request);
		HttpServletRequest processedRequest = request;
		HandlerExecutionChain mappedHandler = null;
		int interceptorIndex = -1;

		// Expose current LocaleResolver and request as LocaleContext.
		LocaleContext previousLocaleContext = null;

		// Expose current RequestAttributes to current thread.
		RequestAttributes previousRequestAttributes = null;
		ServletRequestAttributes requestAttributes = null;

		if (logger.isTraceEnabled()) {
			logger.trace("Bound request context to thread: " + request);
		}
		PageContext pageContext = null;
		JspFactory fac= null;
		try {
			
			previousLocaleContext = LocaleContextHolder.getLocaleContext();
//			LocaleContextHolder.setLocaleContext(buildLocaleContext(request), this.threadContextInheritable);
			setLocaleContext(  request);

			// Expose current RequestAttributes to current thread.
			previousRequestAttributes = RequestContextHolder.getRequestAttributes();
			fac=JspFactory.getDefaultFactory();
			pageContext=fac.getPageContext(this, request,response, null, false, JspWriter.DEFAULT_BUFFER <= 0?8192:JspWriter.DEFAULT_BUFFER, true); 
			requestAttributes = new ServletRequestAttributes(request, response,pageContext);
			RequestContextHolder.setRequestAttributes(requestAttributes, this.threadContextInheritable);
			ModelAndView mv = null;
			boolean errorView = false;

			try {
				processedRequest = checkMultipart(request);

				// Determine handler for the current request.
				mappedHandler = getHandler(processedRequest, false);
				if (mappedHandler == null 
						|| mappedHandler.getHandler() == null 
						|| mappedHandler.getHandler().getHandler() == null) {
					noHandlerFound(processedRequest, response);
					return;
				}

				// Apply preHandle methods of registered interceptors.
				mappedHandler.addInterceptors(this.gloabelHandlerInterceptors);
				HandlerInterceptor[] interceptors = mappedHandler.getInterceptors();
				if (interceptors != null) {
					for (int i = 0; i < interceptors.length; i++) {
						HandlerInterceptor interceptor = interceptors[i];
						if (!interceptor.preHandle(processedRequest, response, mappedHandler.getHandler())) {
							triggerAfterCompletion(mappedHandler, interceptorIndex, processedRequest, response, null);
							return;
						}
						interceptorIndex = i;
					}
				}

				// Actually invoke the handler.
				HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());
				
//				if(this.messageConverters != null && messageConverters.length > 0)
//				{
//					try
//					{
//						request.setAttribute(messageConverters_KEY, this.messageConverters);
//						mv = ha.handle(processedRequest,response,  pageContext,mappedHandler.getHandler());
//					}
//					finally
//					{
//						request.removeAttribute(messageConverters_KEY);
//					}
//					
//				}
//				else
				{
					mv = ha.handle(processedRequest,response,  pageContext,mappedHandler.getHandler());
				}

				// Do we need view name translation?
				if (mv != null && !mv.hasView()) {
					mv.setViewName(getDefaultViewName(request));
				}

				// Apply postHandle methods of registered interceptors.
				if (interceptors != null) {
					for (int i = interceptors.length - 1; i >= 0; i--) {
						HandlerInterceptor interceptor = interceptors[i];
						interceptor.postHandle(processedRequest, response, mappedHandler.getHandler(), mv);
					}
				}
			}
			catch (ModelAndViewDefiningException ex) {
				logger.debug("ModelAndViewDefiningException encountered", ex);
				mv = ex.getModelAndView();
			}
			catch (Exception ex) {
				HandlerMeta handler = (mappedHandler != null ? mappedHandler.getHandler() : null);
				mv = processHandlerException(processedRequest, response, handler, ex);
				errorView = (mv != null);
			}

			// Did the handler return a view to render?
			if (mv != null && !mv.wasCleared()) {
				render(mv, processedRequest, response);
				if (errorView) {
					WebUtils.clearErrorRequestAttributes(request);
				}
			}
			else {
				if (logger.isDebugEnabled()) {
					logger.debug("Null ModelAndView returned to DispatcherServlet with name '" +
							getServletName() + "': assuming HandlerAdapter completed request handling");
				}
			}

			// Trigger after-completion for successful outcome.
			triggerAfterCompletion(mappedHandler, interceptorIndex, processedRequest, response, null);
		}

		catch (Exception ex) {
			// Trigger after-completion for thrown exception.
			triggerAfterCompletion(mappedHandler, interceptorIndex, processedRequest, response, ex);
			throw ex;
		}
		catch (Error err) {
			ServletException ex = new NestedServletException("Handler processing failed", err);
			// Trigger after-completion for thrown exception.
			triggerAfterCompletion(mappedHandler, interceptorIndex, processedRequest, response, ex);
			throw ex;
		}
		catch (Throwable err) {
			ServletException ex = new NestedServletException("Handler processing failed", err);
			// Trigger after-completion for thrown exception.
			triggerAfterCompletion(mappedHandler, interceptorIndex, processedRequest, response, ex);
			throw ex;
		}

		finally {
			// Clean up any resources used by a multipart request.
			try {
				if (processedRequest != request) {
					cleanupMultipart(processedRequest);
				}
			} catch (Exception e) {
				
			}

			// Reset thread-bound context.
			RequestContextHolder.setRequestAttributes(previousRequestAttributes, this.threadContextInheritable);
			LocaleContextHolder.setLocaleContext(previousLocaleContext, this.threadContextInheritable);

			// Clear request attributes.
			requestAttributes.requestCompleted();
			if (logger.isTraceEnabled()) {
				logger.trace("Cleared thread-bound request context: " + request);
			}
			if(fac != null && pageContext != null)
			{
				fac.releasePageContext(pageContext);
			}
		}
	}
	
	/**
	 * Clean up any resources used by the given multipart request (if any).
	 * @param request current HTTP request
	 * @see MultipartResolver#cleanupMultipart
	 */
	protected void cleanupMultipart(HttpServletRequest request) {
		if (request instanceof MultipartHttpServletRequest) {
			this.multipartResolver.cleanupMultipart((MultipartHttpServletRequest) request);
		}
	}
	/**
	 * @FIXME
	 * Determine an error ModelAndView via the registered HandlerExceptionResolvers.
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @param handler the executed handler, or <code>null</code> if none chosen at the time of
	 * the exception (for example, if multipart resolution failed)
	 * @param ex the exception that got thrown during handler execution
	 * @return a corresponding ModelAndView to forward to
	 * @throws Exception if no error ModelAndView found
	 */
	protected ModelAndView processHandlerException(
			HttpServletRequest request, HttpServletResponse response, HandlerMeta handlerMeta, Exception ex)
			throws Exception {

		// Check registerer HandlerExceptionResolvers...
		ModelAndView exMv = null;
		if(handlerExceptionResolvers != null)
		{
			for (Iterator it = this.handlerExceptionResolvers.iterator(); exMv == null && it.hasNext();) {
				HandlerExceptionResolver resolver = (HandlerExceptionResolver) it.next();
				exMv = resolver.resolveException(request, response, handlerMeta, ex);
			}
		}
		if (exMv != null) {
			try
			{
				if(exMv.getView() != null && exMv.getView() instanceof AbstractUrlBasedView)
				{
					//
					AbstractUrlBasedView view = (AbstractUrlBasedView) exMv.getView();
					String url = view.getUrl();
					if(UrlBasedViewResolver.isPathVariable(url))
					{
						url = handlerMeta.getUrlPath(url,null,handlerMeta.getHandler(),request);
						view.setUrl(url);
					}
				}
				if(exMv != null && UrlBasedViewResolver.isPathVariable(exMv.getViewName()))
				{
					exMv.setViewName(handlerMeta.getUrlPath(exMv.getViewName(),null,handlerMeta.getHandler(),request));
				}
			}
			
			catch(PathURLNotSetException ex1)
			{
				return HandlerUtils.handleNoSuchRequestHandlingMethod(ex1, request, response);
			}
//			if (logger.isDebugEnabled()) 
			{
				logger.debug("Handler execution resulted in exception - forwarding to resolved error view: " + exMv, ex);
			}
			WebUtils.exposeErrorRequestAttributes(request, ex, getServletName());
			return exMv;
		}

		// Send default responses for well-known exceptions, if possible.
		if (ex instanceof HttpRequestMethodNotSupportedException && !response.isCommitted()) {
			String[] supportedMethods = ((HttpRequestMethodNotSupportedException) ex).getSupportedMethods();
			if (supportedMethods != null) {
				response.setHeader("Allow", StringUtil.arrayToDelimitedString(supportedMethods, ", "));
			}
			response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, ex.getMessage());
			return null;
		}

		throw ex;
	}
	/**
	 * Translate the supplied request into a default view name.
	 * @param request current HTTP servlet request
	 * @return the view name (or <code>null</code> if no default found)
	 * @throws Exception if view name translation failed
	 */
	protected String getDefaultViewName(HttpServletRequest request) throws Exception {
		return this.viewNameTranslator.getViewName(request);
	}
	
	/**
	 * Trigger afterCompletion callbacks on the mapped HandlerInterceptors.
	 * Will just invoke afterCompletion for all interceptors whose preHandle
	 * invocation has successfully completed and returned true.
	 * @param mappedHandler the mapped HandlerExecutionChain
	 * @param interceptorIndex index of last interceptor that successfully completed
	 * @param ex Exception thrown on handler execution, or <code>null</code> if none
	 * @see HandlerInterceptor#afterCompletion
	 */
	private void triggerAfterCompletion(
			HandlerExecutionChain mappedHandler, int interceptorIndex,
			HttpServletRequest request, HttpServletResponse response, Exception ex)
			throws Exception {

		// Apply afterCompletion methods of registered interceptors.
		if (mappedHandler != null) {
			HandlerInterceptor[] interceptors = mappedHandler.getInterceptors();
			if (interceptors != null) {
				for (int i = interceptorIndex; i >= 0; i--) {
					HandlerInterceptor interceptor = interceptors[i];
					try {
						interceptor.afterCompletion(request, response, mappedHandler.getHandler(), ex);
					}
					catch (Throwable ex2) {
						logger.error("HandlerInterceptor.afterCompletion threw exception", ex2);
					}
				}
			}
		}
	}

	/**
	 * Convert the request into a multipart request, and make multipart resolver available.
	 * If no multipart resolver is set, simply use the existing request.
	 * @param request current HTTP request
	 * @return the processed request (multipart wrapper if necessary)
	 * @see MultipartResolver#resolveMultipart
	 */
	protected HttpServletRequest checkMultipart(HttpServletRequest request) throws MultipartException {
		if (this.multipartResolver != null && this.multipartResolver.isMultipart(request)) {
			if (request instanceof MultipartHttpServletRequest) {
				logger.debug("Request is already a MultipartHttpServletRequest - if not in a forward, " +
						"this typically results from an additional MultipartFilter in web.xml");
			}
			else {
				return this.multipartResolver.resolveMultipart(request);
			}
		}
		// If not returned before: return original request.
		return request;
	}
	/**
	 * Build a LocaleContext for the given request, exposing the request's
	 * primary locale as current locale.
	 * <p>The default implementation uses the dispatcher's LocaleResolver
	 * to obtain the current locale, which might change during a request.
	 * @param request current HTTP request
	 * @return the corresponding LocaleContext
	 */
	protected static LocaleContext buildLocaleContext(final HttpServletRequest request) {
		Locale locale = localeResolver.resolveLocale(request);
		return new SimpleLocaleContext(locale);
	}
	/**
	 * Return this servlet's WebApplicationContext.
	 */
	public final WebApplicationContext getWebApplicationContext() {
		return this.webApplicationContext;
	}
	/**
	 * Render the given ModelAndView. This is the last stage in handling a request.
	 * It may involve resolving the view by name.
	 * @param mv the ModelAndView to render
	 * @param request current HTTP servlet request
	 * @param response current HTTP servlet response
	 * @throws Exception if there's a problem rendering the view
	 */
	protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// Determine locale for request and apply it to the response.
//		Locale locale = this.localeResolver.resolveLocale(request);
		Locale locale = RequestContextUtils.getRequestContextLocal(request);
		response.setLocale(locale);

		View view = null;

		if (mv.isReference()) {
			// We need to resolve the view name.
			view = resolveViewName(mv.getViewName(), mv.getModelInternal(), locale, request);
			if (view == null) {
				throw new ServletException("Could not resolve view with name '" + mv.getViewName() +
						"' in servlet with name '" + getServletName() + "'");
			}
		}
		else {
			// No need to lookup: the ModelAndView object contains the actual View object.
			view = mv.getView();
			if (view == null) {
				throw new ServletException("ModelAndView [" + mv + "] neither contains a view name nor a " +
						"View object in servlet with name '" + getServletName() + "'");
			}
		}

		// Delegate to the View object for rendering.
		if (logger.isDebugEnabled()) {
			logger.debug("Rendering view [" + view + "] in DispatcherServlet with name '" + getServletName() + "'");
		}
		
		try
		{
			
			if(mv.hasErrors())
				request.setAttribute(RequestContext.BIND_ERROR_MESSAGES_KEY, mv.getErrors());
			view.render(mv.getModelInternal(), request, response);
		}
		finally
		{
			request.removeAttribute(RequestContext.BIND_ERROR_MESSAGES_KEY);
		}
	}
	
	/**
	 * Resolve the given view name into a View object (to be rendered).
	 * <p>Default implementations asks all ViewResolvers of this dispatcher.
	 * Can be overridden for custom resolution strategies, potentially based
	 * on specific model attributes or request parameters.
	 * @param viewName the name of the view to resolve
	 * @param model the model to be passed to the view
	 * @param locale the current locale
	 * @param request current HTTP servlet request
	 * @return the View object, or <code>null</code> if none found
	 * @throws Exception if the view cannot be resolved
	 * (typically in case of problems creating an actual View object)
	 * @see ViewResolver#resolveViewName
	 */
	protected View resolveViewName(String viewName, Map model, Locale locale, HttpServletRequest request)
			throws Exception {

		for (Iterator it = this.viewResolvers.iterator(); it.hasNext();) {
			ViewResolver viewResolver = (ViewResolver) it.next();
			View view = viewResolver.resolveViewName(viewName, locale);
			if (view != null) {
				return view;
			}
		}
		return null;
	}
	protected void initMessagesources(ServletConfig config)
	{
		messagesources = config.getInitParameter("messagesources");
		if(messagesources == null)
		{
			messagesources = DispatchServlet.getDefaultStrategies().getProperty("messageSource.basename","/WEB-INF/messages");
		}
		useCodeAsDefaultMessage = config.getInitParameter("useCodeAsDefaultMessage");
		if(useCodeAsDefaultMessage == null)
		{
			useCodeAsDefaultMessage = DispatchServlet.getDefaultStrategies().getProperty("messageSource.useCodeAsDefaultMessage","true");
		}
		
		
	}
	private Map<String,String> parserIocLifeCycleEventListenerParams(String iocLifeCycleEventListenerParams)
	{
		Map<String,String> paramMap = new HashMap<String,String>();
		if(iocLifeCycleEventListenerParams != null && iocLifeCycleEventListenerParams.trim().length() > 0)
		{
			
			String[] params = iocLifeCycleEventListenerParams.split("\\|");
			for(String param :params)
			{
				String[] pv = param.split("=");
				paramMap.put(pv[0], pv[1]);
			}
			
		}
		return paramMap;
	}
	protected void initIocLifeCycleEventListeners(ServletConfig config)
	{
		iocLifeCycleEventListeners = config.getInitParameter("iocLifeCycleEventListeners");
		if(StringUtil.isNotEmpty(iocLifeCycleEventListeners ))
		{
			
			iocLifeCycleEventListenerParams = config.getInitParameter("iocLifeCycleEventListenerParams");
			Map<String,String> params = parserIocLifeCycleEventListenerParams(iocLifeCycleEventListenerParams);
			String[]  iocLifeCycleEventListeners_ = iocLifeCycleEventListeners.split(",");
			this.iocLifeCycleEventListenerList = new ArrayList<IocLifeCycleEventListener>();
			for(String iocLifeCycleEventListener:iocLifeCycleEventListeners_)
			{
				
				try {
					IocLifeCycleEventListener l = (IocLifeCycleEventListener)Class.forName(iocLifeCycleEventListener).newInstance();
					if(params != null && params.size() > 0)
						l.init(params);
					iocLifeCycleEventListenerList.add(l);
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
	/**
	 * Initialize the strategy objects that this servlet uses.
	 * <p>May be overridden in subclasses in order to initialize
	 * further strategy objects.
	 * @throws Exception 
	 */
	protected void init_(ServletConfig config) throws Exception {
//		loadCustomJars(config);
		initMessagesources(config);
		this.initIocLifeCycleEventListeners( config);
		for(int i = 0; this.iocLifeCycleEventListenerList != null && i < this.iocLifeCycleEventListenerList.size(); i ++)
		{
			IocLifeCycleEventListener l = this.iocLifeCycleEventListenerList.get(i);
			try {
				l.beforestart();
			} catch (Exception e) {
				logger.warn("before start WebApplicationContext:",e);
			}
		}
		try {
			this.initServletBean(config);
			publishWebService(config);
//		WebApplicationContext context = this.initWebApplicationContext( config);
			initMessageConverters(this.webApplicationContext);
			initMultipartResolver(this.webApplicationContext);
			initLocaleResolver(this.webApplicationContext);
			initThemeResolver(this.webApplicationContext);
			initHandlerMappings(this.webApplicationContext);
			initHandlerAdapters(this.webApplicationContext);
			initHandlerExceptionResolvers(this.webApplicationContext);
			initRequestToViewNameTranslator(this.webApplicationContext);
			initViewResolvers(this.webApplicationContext);
			initGloabelHandlerInterceptors(this.webApplicationContext);
		} catch (Exception e1) {
			logger.warn("Init WebApplicationContext:",e1);
		}
		for(int i = 0; this.iocLifeCycleEventListenerList != null && i < this.iocLifeCycleEventListenerList.size(); i ++)
		{
			IocLifeCycleEventListener l = this.iocLifeCycleEventListenerList.get(i);
			try {
				l.afterstart(webApplicationContext);
			} catch (Exception e) {
				logger.warn("After start WebApplicationContext:",e);
			}
		}
	}
	
	private void initMessageConverters(
			WebApplicationContext webApplicationContext) {
		ProList<Pro> list = webApplicationContext.getListProperty("httpMessageConverters");
		if(list != null && list.size() > 0)
		{
			messageConverters = new HttpMessageConverter[list.size()];
			for(int i = 0; i < list.size(); i ++)
			{
				Pro t = list.get(i);
				try {
					this.messageConverters[i] = (HttpMessageConverter) t
							.getBean();
				} catch (Exception e) {
					System.out.println("load messageConvert failed:" + t.getClazz());
					logger.error("load messageConvert failed:" + t.getClazz(), e);
				}
			}
		}
		
	}

//	private void loadCustomJars(ServletConfig config)
//	{
//		String docbase = this.getServletContext().getRealPath("");
//		String customJarsLocation = config.getInitParameter("customJarsLocation");
//		logger.debug("load custom Jars from Location:\r\n" + customJarsLocation);
//		CustomClassLoader classLoader = (CustomClassLoader)getDefaultStrategy(null, CustomClassLoader.class);
//		classLoader.initClassLoader(this.getClass().getClassLoader());
//		CustomJarsLauncher launcher = new CustomJarsLauncher();
//		launcher.loadFromRepository(classLoader, docbase, customJarsLocation);
//	}
	
	/**
	 * Initialize the ViewResolvers used by this class.
	 * <p>If no ViewResolver beans are defined in the BeanFactory
	 * for this namespace, we default to InternalResourceViewResolver.
	 */
	private void initViewResolvers(BaseApplicationContext context) {
		this.viewResolvers = null;

//		if (this.detectAllViewResolvers) {
//			// Find all ViewResolvers in the ApplicationContext,
//			// including ancestor contexts.
//			Map matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(
//					context, ViewResolver.class, true, false);
//			if (!matchingBeans.isEmpty()) {
//				this.viewResolvers = new ArrayList(matchingBeans.values());
//				// We keep ViewResolvers in sorted order.
//				Collections.sort(this.viewResolvers, new OrderComparator());
//			}
//		}
//		else 
		{
			try {
				Object vr = context.getBeanObject(VIEW_RESOLVER_BEAN_NAME);
				this.viewResolvers = Collections.singletonList(vr);
			}
			catch (Exception ex) {
				// Ignore, we'll add a default ViewResolver later.
			}
		}

		// Ensure we have at least one ViewResolver, by registering
		// a default ViewResolver if no other resolvers are found.
		if (this.viewResolvers == null) {
			this.viewResolvers = getDefaultStrategies(context, ViewResolver.class);
			if (logger.isDebugEnabled()) {
				logger.debug("No ViewResolvers found in servlet '" + getServletName() + "': using default");
			}
		}
	}
	
	/**
	 * Initialize the RequestToViewNameTranslator used by this servlet instance. If no
	 * implementation is configured then we default to DefaultRequestToViewNameTranslator.
	 */
	private void initRequestToViewNameTranslator(BaseApplicationContext context) {
		try {
			this.viewNameTranslator = (RequestToViewNameTranslator) context.getBeanObject(
					REQUEST_TO_VIEW_NAME_TRANSLATOR_BEAN_NAME);
			if (logger.isDebugEnabled()) {
				logger.debug("Using RequestToViewNameTranslator [" + this.viewNameTranslator + "]");
			}
		}
		catch (Exception ex) {
			// We need to use the default.
			try {
				this.viewNameTranslator =
						(RequestToViewNameTranslator) getDefaultStrategy(context, RequestToViewNameTranslator.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Unable to locate RequestToViewNameTranslator with name '" +
						REQUEST_TO_VIEW_NAME_TRANSLATOR_BEAN_NAME +
						"': using default [" + this.viewNameTranslator.getClass().getCanonicalName() + "]");
			}
		}
	}
	/**
	 * Initialize the HandlerExceptionResolver used by this class.
	 * <p>If no bean is defined with the given name in the BeanFactory
	 * for this namespace, we default to no exception resolver.
	 */
	private void initHandlerExceptionResolvers(BaseApplicationContext context) {
		this.handlerExceptionResolvers = null;

//		if (this.detectAllHandlerExceptionResolvers) {
//			// Find all HandlerExceptionResolvers in the ApplicationContext,
//			// including ancestor contexts.
//			Map matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(
//					context, HandlerExceptionResolver.class, true, false);
//			if (!matchingBeans.isEmpty()) {
//				this.handlerExceptionResolvers = new ArrayList(matchingBeans.values());
//				// We keep HandlerExceptionResolvers in sorted order.
//				Collections.sort(this.handlerExceptionResolvers, new OrderComparator());
//			}
//		}
//		else 
		{
			try {
				Object her = context.getBeanObject(
						HANDLER_EXCEPTION_RESOLVER_BEAN_NAME);
				this.handlerExceptionResolvers = Collections.singletonList(her);
			}
			catch (Exception ex) {
				// Ignore, no HandlerExceptionResolver is fine too.
			}
		}

		// Just for consistency, check for default HandlerExceptionResolvers...
		// There aren't any in usual scenarios.
		if (this.handlerExceptionResolvers == null) {
			this.handlerExceptionResolvers = getDefaultStrategies(context, HandlerExceptionResolver.class);
			if (logger.isDebugEnabled()) {
				logger.debug("No HandlerExceptionResolvers found in servlet '" + getServletName() + "': using default");
			}
		}
		if(this.handlerExceptionResolvers != null && this.handlerExceptionResolvers.size() == 0)
			this.handlerExceptionResolvers = null;
	}
	
	private void initGloabelHandlerInterceptors(BaseApplicationContext context)
	{
		try
		{
			this.gloabelHandlerInterceptors = (List<HandlerInterceptor>)context.
					getBeanObject("org.frameworkset.web.servlet.gloabel.HandlerInterceptors");
		}
		catch (Exception e)
		{
			this.gloabelHandlerInterceptors = new ArrayList<HandlerInterceptor>();
		}
	}
	/**
	 * Initialize the HandlerAdapters used by this class.
	 * <p>If no HandlerAdapter beans are defined in the BeanFactory
	 * for this namespace, we default to SimpleControllerHandlerAdapter.
	 */
	private void initHandlerAdapters(BaseApplicationContext context) {
//		this.handlerAdapters = null;
//
////		if (this.detectAllHandlerAdapters) {
////			// Find all HandlerAdapters in the ApplicationContext,
////			// including ancestor contexts.
////			Map matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(
////					context, HandlerAdapter.class, true, false);
////			if (!matchingBeans.isEmpty()) {
////				this.handlerAdapters = new ArrayList(matchingBeans.values());
////				// We keep HandlerAdapters in sorted order.
////				Collections.sort(this.handlerAdapters, new OrderComparator());
////			}
////		}
////		else 
////		{
////			try {
////				Object ha = context.getBeanObject(HANDLER_ADAPTER_BEAN_NAME);
////				List temp = Collections.singletonList(ha);
////				this.handlerAdapters = (List<HandlerAdapter>)temp;
////			}
////			catch (Exception ex) {
////				// Ignore, we'll add a default HandlerAdapter later.
////			}
////		}
//
//		// Ensure we have at least some HandlerAdapters, by registering
//		// default HandlerAdapters if no other adapters are found.
//		if (this.handlerAdapters == null) {
//			this.handlerAdapters = getDefaultStrategies(context, HandlerAdapter.class);
//			if (logger.isDebugEnabled()) {
//				logger.debug("No HandlerAdapters found in servlet '" + getServletName() + "': using default");
//			}
//		}
//		if(handlerAdapters != null)
//		{
//			for(HandlerAdapter adapter :handlerAdapters)
//			{
//				if(!adapter.containMessageConverters())
//					adapter.setMessageConverters(messageConverters);
//			}
//		}
		
		this.annotationMethodHandlerAdapter = createDefaultStrategy(context, AnnotationMethodHandlerAdapter.class);
		if(!annotationMethodHandlerAdapter.containMessageConverters())
			annotationMethodHandlerAdapter.setMessageConverters(messageConverters);
		
		this.simpleControllerHandlerAdapter = createDefaultStrategy(context, SimpleControllerHandlerAdapter.class);
		if(!simpleControllerHandlerAdapter.containMessageConverters())
			simpleControllerHandlerAdapter.setMessageConverters(messageConverters);
		
		this.httpRequestHandlerAdapter = createDefaultStrategy(context, HttpRequestHandlerAdapter.class);
		if(!httpRequestHandlerAdapter.containMessageConverters())
			httpRequestHandlerAdapter.setMessageConverters(messageConverters);
		
	}
	
	/**
	 * Initialize the HandlerMappings used by this class.
	 * <p>If no HandlerMapping beans are defined in the BeanFactory
	 * for this namespace, we default to BeanNameUrlHandlerMapping.
	 */
	private void initHandlerMappings(BaseApplicationContext context) {
		this.handlerMappings = null;
//		this.handlerMappings = (HandlerMappingsTable)context.getBeanObject(HANDLER_MAPPING_BEAN_NAME);
//		if (this.handlerMappings == null) {
//			List handlerMappings_ = getDefaultStrategies(context, HandlerMapping.class);
//			handlerMappings = (HandlerMappingsTable) context.createBean(HandlerMappingsTable.class);
//			initHandlerMappings(handlerMappings_);
//			handlerMappings.setHandlerMappings(handlerMappings_);
//			if (logger.isDebugEnabled()) {
//				logger.debug("No HandlerMappings found in servlet '" + getServletName() + "': using default");
//			}
//		}
		handlerMappings = (HandlerMappingsTable) context.createBean(HandlerMappingsTable.class);
		 DefaultAnnotationHandlerMapping handlerMapping = (DefaultAnnotationHandlerMapping) context.createBean(DefaultAnnotationHandlerMapping.class);
//		 @SuppressWarnings("unchecked")
//		HandlerUrlMappingRegisterTable<String,HandlerMeta> registTable = (HandlerUrlMappingRegisterTable<String,HandlerMeta>) context.createBean(HandlerUrlMappingRegisterTable.class);
//		 handlerMapping.setHandlerMap(registTable);
		 _initHandlerMappings(handlerMapping);
		 handlerMappings.setHandlerMapping(handlerMapping);
////			Map matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(
////					context, HandlerMapping.class, true, false);
////			if (!matchingBeans.isEmpty()) {
////				this.handlerMappings = new ArrayList(matchingBeans.values());
////				// We keep HandlerMappings in sorted order.
////				Collections.sort(this.handlerMappings, new OrderComparator());
////			}
//		
////		else 
//		{
//			try {
//				Object hm = context.getBeanObject(HANDLER_MAPPING_BEAN_NAME);
//				this.handlerMappings = Collections.singletonList(hm);
//			}
//			catch (Exception ex) {
//				// Ignore, we'll add a default HandlerMapping later.
//			}
//		}
//
//		// Ensure we have at least one HandlerMapping, by registering
//		// a default HandlerMapping if no other mappings are found.
//		if (this.handlerMappings == null) {
//			this.handlerMappings = getDefaultStrategies(context, HandlerMapping.class);
//			initHandlerMappings();
//			if (logger.isDebugEnabled()) {
//				logger.debug("No HandlerMappings found in servlet '" + getServletName() + "': using default");
//			}
//		}
	}
	private void initHandlerMappings(List handlerMappings_)
	{
		if(handlerMappings_ != null && handlerMappings_.size() > 0)
		{
				for(int i = 0; i < handlerMappings_.size(); i ++)
				{
					Object handler = handlerMappings_.get(i);
					if(handler instanceof AbstractUrlHandlerMapping)
					{
						((AbstractUrlHandlerMapping)handler).setAlwaysUseFullPath(true);
					}
				}
		}
	}
	private void _initHandlerMappings(Object handler)
	{
		 
			if(handler instanceof AbstractUrlHandlerMapping)
			{
				((AbstractUrlHandlerMapping)handler).setAlwaysUseFullPath(true);
			}
				 
		 
	}
	
	/**
	 * Initialize the ThemeResolver used by this class.
	 * <p>If no bean is defined with the given name in the BeanFactory
	 * for this namespace, we default to a FixedThemeResolver.
	 */
	private void initThemeResolver(BaseApplicationContext context) {
		try {
			this.themeResolver = (ThemeResolver)
					context.getBeanObject(THEME_RESOLVER_BEAN_NAME);
			if (logger.isDebugEnabled()) {
				logger.debug("Using ThemeResolver [" + this.themeResolver + "]");
			}
		}
		catch (Exception ex) {
			// We need to use the default.
			try {
				this.themeResolver = (ThemeResolver) getDefaultStrategy(context, ThemeResolver.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Unable to locate ThemeResolver with name '" + THEME_RESOLVER_BEAN_NAME +
						"': using default [" + this.themeResolver + "]");
			}
		}
	}
	public static LocaleResolver getLocaleResolver(WebApplicationContext context)
	{
		if(localeResolver != null)
		{
			return localeResolver;
		}
		else
		{
			initLocaleResolver(context);
			return localeResolver;
		}
	}
	
	
	public static LocaleResolver getLocaleResolver()
	{
		if(localeResolver != null)
		{
			return localeResolver;
		}
		else
		{
			initLocaleResolver(DispatchServlet.webApplicationContext);
			return localeResolver;
		}
	}
	
	/**
	 * Initialize the LocaleResolver used by this class.
	 * <p>If no bean is defined with the given name in the BeanFactory
	 * for this namespace, we default to AcceptHeaderLocaleResolver.
	 */
	public synchronized static void initLocaleResolver(WebApplicationContext context) {
		if(localeResolver == null)
		{
			try {
				localeResolver = (LocaleResolver)
						context.getBeanObject(LOCALE_RESOLVER_BEAN_NAME);
				if (logger.isDebugEnabled()) {
					logger.debug("Using LocaleResolver [" + localeResolver + "]");
				}
			}
			catch (Exception ex) {
				// We need to use the default.
				try {
					localeResolver = (LocaleResolver) getDefaultStrategy(context, LocaleResolver.class);
				} catch (Exception e) {
					localeResolver = new DefaultLocaleResolver();
					e.printStackTrace();
				}
				if (logger.isDebugEnabled()) {
					logger.debug("Unable to locate LocaleResolver with name '" + LOCALE_RESOLVER_BEAN_NAME +
							"': using default [" + localeResolver + "]");
				}
			}
		}
	}
	
	/**
	 * Return the default strategy object for the given strategy interface.
	 * <p>The default implementation delegates to {@link #getDefaultStrategies},
	 * expecting a single object in the list.
	 * @param context the current WebApplicationContext
	 * @param strategyInterface the strategy interface
	 * @return the corresponding strategy object
	 * @throws BeansException if initialization failed
	 * @see #getDefaultStrategies
	 */
	public static Object getDefaultStrategy(BaseApplicationContext context, Class strategyInterface) throws BeanInstanceException {
		List strategies = getDefaultStrategies(context, strategyInterface);
		if (strategies.size() <= 0) {
			throw new BeanInstanceException(
					"DispatcherServlet needs exactly 1 strategy for interface [" + strategyInterface.getName() + "]");
		}
		return strategies.get(0);
	}
	
	/**
	 * Create a List of default strategy objects for the given strategy interface.
	 * <p>The default implementation uses the "DispatcherServlet.properties" file
	 * (in the same package as the DispatcherServlet class) to determine the class names.
	 * It instantiates the strategy objects through the context's BeanFactory.
	 * @param context the current WebApplicationContext
	 * @param strategyInterface the strategy interface
	 * @return the List of corresponding strategy objects
	 * @throws BeansException if initialization failed
	 */
	public static List getDefaultStrategies(BaseApplicationContext context, Class strategyInterface) throws BeanInstanceException {
		String key = strategyInterface.getName();
		List strategies = null;
		String value = defaultStrategies.getProperty(key);
		if (value != null) {
			String[] classNames = StringUtil.commaDelimitedListToStringArray(value);
			strategies = new ArrayList(classNames.length);
			for (int i = 0; i < classNames.length; i++) {
				String className = classNames[i];
//				if (JdkVersion.getMajorJavaVersion() < JdkVersion.JAVA_15 && className.indexOf("Annotation") != -1) {
//					// Skip Java 5 specific strategies when running on JDK 1.4...
//					continue;
//				}
				try {
					Class clazz = ClassUtils.forName(className, DispatchServlet.class.getClassLoader());
					Object strategy = createDefaultStrategy(context, clazz);
					strategies.add(strategy);
				}
				catch (ClassNotFoundException ex) {
					throw new BeanInstanceException(
							"Could not find DispatcherServlet's default strategy class [" + className +
							"] for interface [" + key + "]", ex);
				}
				catch (LinkageError err) {
					throw new BeanInstanceException(
							"Error loading DispatcherServlet's default strategy class [" + className +
							"] for interface [" + key + "]: problem with class file or dependent class", err);
				}
			}
		}
		else {
			strategies = Collections.EMPTY_LIST;
		}
		return strategies;
	}
	
	/**
	 * Create a default strategy.
	 * <p>The default implementation uses

	 * @param context the current WebApplicationContext
	 * @param clazz the strategy implementation class to instantiate
	 * @throws BeansException if initialization failed
	 * @return the fully configured strategy instance

	 */
	protected static <T> T createDefaultStrategy(BaseApplicationContext context, Class<T> clazz) throws BeanInstanceException {
		
		if(context != null)
			return (T)context.createBean(clazz);
		else
		{
			try {
				return clazz.newInstance();
			} catch (InstantiationException e) {
				throw new BeanInstanceException(e);
			} catch (IllegalAccessException e) {
				throw new BeanInstanceException(e);
			}
			 catch (Exception e) {
					throw new BeanInstanceException(e);
				}
		}
	}
	
	/**
	 * Initialize the MultipartResolver used by this class.
	 * <p>If no bean is defined with the given name in the BeanFactory
	 * for this namespace, no multipart handling is provided.
	 */
	private void initMultipartResolver(WebApplicationContext context) {
		try {
			this.multipartResolver = (MultipartResolver)
					context.getBeanObject(MULTIPART_RESOLVER_BEAN_NAME);
			if (logger.isDebugEnabled()) {
				logger.debug("Using MultipartResolver [" + this.multipartResolver + "]");
			}
		}
		catch (Exception ex) {
			// Default is no multipart resolver.
			this.multipartResolver = null;
			if (logger.isDebugEnabled()) {
				logger.debug("Unable to locate MultipartResolver with name '"	+ MULTIPART_RESOLVER_BEAN_NAME +
						"': no multipart request handling provided");
			}
		}
	}


	@Override
	public void init(ServletConfig config) throws ServletException {
		
		super.init(config);
		try {
			init_( config);
		}
		catch(ServletException e)
		{
			throw e;
		} catch (Exception e) {
			
			throw new NestedServletException("",e);
		}
	}
	
	
	/**
	 * Delegate GET requests to processRequest/doService.
	 * <p>Will also be invoked by HttpServlet's default implementation of <code>doHead</code>,
	 * with a <code>NoBodyResponse</code> that just captures the content length.
	 * @see #doService
	 * @see #doHead
	 */
	protected final void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {

		processRequest(request, response);
	}

	/**
	 * Delegate POST requests to {@link #processRequest}.
	 * @see #doService
	 */
	protected final void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {

		processRequest(request, response);
	}

	/**
	 * Delegate PUT requests to {@link #processRequest}.
	 * @see #doService
	 */
	protected final void doPut(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {

		processRequest(request, response);
	}

	/**
	 * Delegate DELETE requests to {@link #processRequest}.
	 * @see #doService
	 */
	protected final void doDelete(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {

		processRequest(request, response);
	}

	/**
	 * Delegate OPTIONS requests to {@link #processRequest}, if desired.
	 * <p>Applies HttpServlet's standard OPTIONS processing first.
	 * @see #doService
	 */
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doOptions(request, response);
		if (this.dispatchOptionsRequest) {
			processRequest(request, response);
		}
	}

	/**
	 * Delegate TRACE requests to {@link #processRequest}, if desired.
	 * <p>Applies HttpServlet's standard TRACE processing first.
	 * @see #doService
	 */
	protected void doTrace(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doTrace(request, response);
		if (this.dispatchTraceRequest) {
			processRequest(request, response);
		}
	}


//	

	/**
	 * Determine the username for the given request.
	 * <p>The default implementation takes the name of the UserPrincipal, if any.
	 * Can be overridden in subclasses.
	 * @param request current HTTP request
	 * @return the username, or <code>null</code> if none found
	 * @see javax.servlet.http.HttpServletRequest#getUserPrincipal()
	 */
	protected String getUsernameForRequest(HttpServletRequest request) {
		Principal userPrincipal = request.getUserPrincipal();
		return (userPrincipal != null ? userPrincipal.getName() : null);
	}

	public static String getUseCodeAsDefaultMessage() {
		return useCodeAsDefaultMessage;
	}



	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
//		if(this.handlerAdapters != null)
//		{
//			for(int i = 0 ; i < this.handlerAdapters.size() ;i ++)
//			{
//				HandlerAdapter ha = this.handlerAdapters.get(i);
//				ha.destroy();
//			}
//			this.handlerAdapters.clear();
//			this.handlerAdapters = null;
//		}
		if(annotationMethodHandlerAdapter != null)
			this.annotationMethodHandlerAdapter.destroy();
		
		if(simpleControllerHandlerAdapter != null)
			this.simpleControllerHandlerAdapter.destroy();
		 
		if(httpRequestHandlerAdapter != null)
			this.httpRequestHandlerAdapter.destroy();
		 
	}
}
