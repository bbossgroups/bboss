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
package org.frameworkset.web.servlet.handler.annotations;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.frameworkset.http.converter.HttpMessageConverter;
import org.frameworkset.util.AntPathMatcher;
import org.frameworkset.util.Assert;
import org.frameworkset.util.ParameterNameDiscoverer;
import org.frameworkset.util.PathMatcher;
import org.frameworkset.util.annotations.SessionAttributes;
import org.frameworkset.web.servlet.HandlerAdapter;
import org.frameworkset.web.servlet.HandlerExecutionChain;
import org.frameworkset.web.servlet.ModelAndView;
import org.frameworkset.web.servlet.handler.HandlerMeta;
import org.frameworkset.web.servlet.handler.HandlerUtils;
import org.frameworkset.web.servlet.handler.HandlerUtils.ServletHandlerMethodResolver;
import org.frameworkset.web.servlet.mvc.MethodNameResolver;
import org.frameworkset.web.servlet.mvc.mutiaction.InternalPathMethodNameResolver;
import org.frameworkset.web.servlet.support.WebArgumentResolver;
import org.frameworkset.web.servlet.support.WebContentGenerator;
import org.frameworkset.web.servlet.support.session.DefaultSessionAttributeStore;
import org.frameworkset.web.servlet.support.session.SessionAttributeStore;
import org.frameworkset.web.util.UrlPathHelper;
import org.frameworkset.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: AnnotationMethodHandlerAdapter.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-23
 * @author biaoping.yin
 * @version 1.0
 */
public class AnnotationMethodHandlerAdapter  extends WebContentGenerator implements HandlerAdapter{

	/**
	 * Log category to use when no mapped handler is found for a request.
	 * @see #pageNotFoundLogger
	 */
	public static final String PAGE_NOT_FOUND_LOG_CATEGORY = "org.frameworkset.web.servlet.PageNotFound";


	/**
	 * Additional logger to use when no mapped handler is found for a request.
	 * @see #PAGE_NOT_FOUND_LOG_CATEGORY
	 */
	protected final static Logger pageNotFoundLogger = LoggerFactory.getLogger(PAGE_NOT_FOUND_LOG_CATEGORY);
//	protected static final Log pageNotFoundLogger = LogFactory.getLog(PAGE_NOT_FOUND_LOG_CATEGORY);

	private UrlPathHelper urlPathHelper = new UrlPathHelper();

	private PathMatcher pathMatcher = new AntPathMatcher();
	
	protected HttpMessageConverter[] messageConverters;

	private MethodNameResolver methodNameResolver = new InternalPathMethodNameResolver();

//	private WebBindingInitializer webBindingInitializer;

	private SessionAttributeStore sessionAttributeStore = new DefaultSessionAttributeStore();

	private int cacheSecondsForSessionAttributeHandlers = 0;

	private boolean synchronizeOnSession = false;

//	private ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
	private ParameterNameDiscoverer parameterNameDiscoverer = null;
	private WebArgumentResolver[] customArgumentResolvers;

	private  Map<Class<?>, ServletHandlerMethodResolver> methodResolverCache =
			new ConcurrentHashMap<Class<?>, ServletHandlerMethodResolver>();


	public AnnotationMethodHandlerAdapter() {
		// no restriction of HTTP methods by default
		super(false);
		setAlwaysUseFullPath(true) ;
	}


	/**
	 * Set if URL lookup should always use the full path within the current servlet
	 * context. Else, the path within the current servlet mapping is used if applicable
	 * (that is, in the case of a ".../*" servlet mapping in web.xml).
	 * <p>Default is "false".
	 * @see org.frameworkset.web.util.UrlPathHelper#setAlwaysUseFullPath
	 */
	public void setAlwaysUseFullPath(boolean alwaysUseFullPath) {
		this.urlPathHelper.setAlwaysUseFullPath(alwaysUseFullPath);
	}

	/**
	 * Set if context path and request URI should be URL-decoded. Both are returned
	 * <i>undecoded</i> by the Servlet API, in contrast to the servlet path.
	 * <p>Uses either the request encoding or the default encoding according
	 * to the Servlet spec (ISO-8859-1).
	 * @see org.frameworkset.web.util.UrlPathHelper#setUrlDecode
	 */
	public void setUrlDecode(boolean urlDecode) {
		this.urlPathHelper.setUrlDecode(urlDecode);
	}

	/**
	 * Set the UrlPathHelper to use for resolution of lookup paths.
	 * <p>Use this to override the default UrlPathHelper with a custom subclass,
	 * or to share common UrlPathHelper settings across multiple HandlerMappings
	 * and HandlerAdapters.
	 */
	public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
		Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
		this.urlPathHelper = urlPathHelper;
	}

	/**
	 * Set the PathMatcher implementation to use for matching URL paths
	 * against registered URL patterns. Default is AntPathMatcher.
	 */
	public void setPathMatcher(PathMatcher pathMatcher) {
		Assert.notNull(pathMatcher, "PathMatcher must not be null");
		this.pathMatcher = pathMatcher;
	}

	/**
	 * Set the MethodNameResolver to use for resolving default handler methods
	 * (carrying an empty <code>@HandlerMapping</code> annotation).
	 * <p>Will only kick in when the handler method cannot be resolved uniquely
	 * through the annotation metadata already.
	 */
	public void setMethodNameResolver(MethodNameResolver methodNameResolver) {
		this.methodNameResolver = methodNameResolver;
	}

//	/**
//	 * Specify a WebBindingInitializer which will apply pre-configured
//	 * configuration to every DataBinder that this controller uses.
//	 */
//	public void setWebBindingInitializer(WebBindingInitializer webBindingInitializer) {
//		this.webBindingInitializer = webBindingInitializer;
//	}

	/**
	 * Specify the strategy to store session attributes with.
	 * <p>Default is {@link org.frameworkset.web.servlet.support.session.DefaultSessionAttributeStore},
	 * storing session attributes in the HttpSession, using the same
	 * attribute name as in the model.
	 */
	public void setSessionAttributeStore(SessionAttributeStore sessionAttributeStore) {
		Assert.notNull(sessionAttributeStore, "SessionAttributeStore must not be null");
		this.sessionAttributeStore = sessionAttributeStore;
	}

	/**
	 * Cache content produced by <code>@SessionAttributes</code> annotated handlers
	 * for the given number of seconds. Default is 0, preventing caching completely.
	 * <p>In contrast to the "cacheSeconds" property which will apply to all general
	 * handlers (but not to <code>@SessionAttributes</code> annotated handlers), this
	 * setting will apply to <code>@SessionAttributes</code> annotated handlers only.
	 * @see #setCacheSeconds

	 */
	public void setCacheSecondsForSessionAttributeHandlers(int cacheSecondsForSessionAttributeHandlers) {
		this.cacheSecondsForSessionAttributeHandlers = cacheSecondsForSessionAttributeHandlers;
	}

	/**
	 * Set if controller execution should be synchronized on the session,
	 * to serialize parallel invocations from the same client.
	 * <p>More specifically, the execution of each handler method will get
	 * synchronized if this flag is "true". The best available session mutex
	 * will be used for the synchronization; ideally, this will be a mutex
	 * exposed by HttpSessionMutexListener.
	 * <p>The session mutex is guaranteed to be the same object during
	 * the entire lifetime of the session, available under the key defined
	 * by the <code>SESSION_MUTEX_ATTRIBUTE</code> constant. It serves as a
	 * safe reference to synchronize on for locking on the current session.
	 * <p>In many cases, the HttpSession reference itself is a safe mutex
	 * as well, since it will always be the same object reference for the
	 * same active logical session. However, this is not guaranteed across
	 * different servlet containers; the only 100% safe way is a session mutex.


	 */
	public void setSynchronizeOnSession(boolean synchronizeOnSession) {
		this.synchronizeOnSession = synchronizeOnSession;
	}

	/**
	 * Set the ParameterNameDiscoverer to use for resolving method parameter
	 * names if needed (e.g. for default attribute names).
	 */
	public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer) {
		this.parameterNameDiscoverer = parameterNameDiscoverer;
	}

	/**
	 * Set a custom ArgumentResolvers to use for special method parameter types.
	 * Such a custom ArgumentResolver will kick in first, having a chance to
	 * resolve an argument value before the standard argument handling kicks in.
	 */
	public void setCustomArgumentResolver(WebArgumentResolver argumentResolver) {
		this.customArgumentResolvers = new WebArgumentResolver[] {argumentResolver};
	}

	/**
	 * Set one or more custom ArgumentResolvers to use for special method
	 * parameter types. Any such custom ArgumentResolver will kick in first,
	 * having a chance to resolve an argument value before the standard
	 * argument handling kicks in.
	 */
	public void setCustomArgumentResolvers(WebArgumentResolver[] argumentResolvers) {
		this.customArgumentResolvers = argumentResolvers;
	}


	public boolean supports(HandlerMeta handler) {
		return !handler.isWebsocket() && getMethodResolver(handler,handler.getHandlerClass()).hasHandlerMethods();
	}
	
	
	
 

	@Override
	public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, PageContext pageContext, HandlerExecutionChain mappedHandler)
			throws Exception {

		if (mappedHandler.getHandler().getHandlerClass().getAnnotation(SessionAttributes.class) != null) {
			// Always prevent caching in case of session attribute management.
			checkAndPrepare(request, response, this.cacheSecondsForSessionAttributeHandlers, true,mappedHandler.getHandler());
			// Prepare cached set of session attributes names.
		}
		else {
			// Uses configured default cacheSeconds setting.
			checkAndPrepare(request, response, true,mappedHandler.getHandler());
		}

		// Execute invokeHandlerMethod in synchronized block if required.
		if (this.synchronizeOnSession) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				Object mutex = WebUtils.getSessionMutex(session);
				synchronized (mutex) {
					return invokeHandlerMethod(request, response, pageContext,mappedHandler);
				}
			}
		}

		return invokeHandlerMethod(request, response, pageContext,mappedHandler);
	}

	protected ModelAndView invokeHandlerMethod(
			HttpServletRequest request, HttpServletResponse response,  PageContext pageContext,HandlerExecutionChain mappedHandler) throws Exception {


		ServletHandlerMethodResolver methodResolver = getMethodResolver(mappedHandler.getHandler(),mappedHandler.getHandler().getHandlerClass());
		return HandlerUtils.invokeHandlerMethod(request, response, pageContext, mappedHandler,
				methodResolver,messageConverters);
	}
	
	/**
	 * Build a HandlerMethodResolver for the given handler type.
	 */
	public  ServletHandlerMethodResolver getMethodResolver(HandlerMeta handler,Class handlerClass) {
//		Class handlerClass = ClassUtils.getUserClass(handler);
		
		ServletHandlerMethodResolver resolver = (org.frameworkset.web.servlet.handler.HandlerUtils.ServletHandlerMethodResolver) methodResolverCache.get(handlerClass);
		if (resolver == null) {
			resolver = new ServletHandlerMethodResolver(handler,handlerClass, urlPathHelper, pathMatcher, methodNameResolver);
			methodResolverCache.put(handlerClass, resolver);
		}
		return resolver;
	}
	
//	protected ModelAndView invokeHandlerMethod(
//			HttpServletRequest request, HttpServletResponse response,  PageContext pageContext,Object handler) throws Exception {
//
//		try {
//			ServletHandlerMethodResolver methodResolver = getMethodResolver(handler.getClass());
//			MethodData handlerMethod = methodResolver.resolveHandlerMethod(request);
//			ServletHandlerMethodInvoker methodInvoker = new ServletHandlerMethodInvoker(methodResolver);
//			ServletWebRequest webRequest = new ServletWebRequest(request, response);
//			ModelMap implicitModel = new ModelMap();
//			
//			Object result = methodInvoker.invokeHandlerMethod(handlerMethod, handler, request,  response, pageContext,implicitModel);
//			ModelAndView mav =
//					methodInvoker.getModelAndView(handlerMethod.getMethodInfo(), handler.getClass(), result, implicitModel, webRequest);
////			methodInvoker.updateModelAttributes(
////					handler, (mav != null ? mav.getModel() : null), implicitModel, webRequest);
//			return mav;
//		}
//		catch (NoSuchRequestHandlingMethodException ex) {
//			return handleNoSuchRequestHandlingMethod(ex, request, response);
//		}
//	}

	public long getLastModified(HttpServletRequest request, HandlerMeta handler) {
		return -1;
	}


	


	public void setMessageConverters(HttpMessageConverter[] messageConverters) {
		this.messageConverters = messageConverters;
	}


	public boolean containMessageConverters() {
		
		return this.messageConverters != null && this.messageConverters.length > 0;
	}


	@Override
	public void destroy() {
		if(methodNameResolver != null)
		{
			this.methodNameResolver.destroy();
			methodNameResolver = null;
		}
		this.messageConverters = null;
		if(methodResolverCache != null)
		{
			Iterator<Entry<Class<?>, ServletHandlerMethodResolver>> it = this.methodResolverCache.entrySet().iterator();
			while(it.hasNext())
			{
				Entry<Class<?>, ServletHandlerMethodResolver> entry = it.next();
				entry.getValue().destroy();
			}
			this.methodResolverCache.clear();
			methodResolverCache = null;
		}
				
		
	}


	
	


}
