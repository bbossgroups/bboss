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

import org.apache.log4j.Logger;
import org.frameworkset.http.converter.HttpMessageConverter;
import org.frameworkset.util.AntPathMatcher;
import org.frameworkset.util.Assert;
import org.frameworkset.util.ParameterNameDiscoverer;
import org.frameworkset.util.PathMatcher;
import org.frameworkset.util.annotations.SessionAttributes;
import org.frameworkset.web.servlet.HandlerAdapter;
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

/**
 * <p>Title: AnnotationMethodHandlerAdapter.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-23
 * @author biaoping.yin
 * @version 1.0
 */
public class AnnotationMethodHandlerAdapter  extends WebContentGenerator implements HandlerAdapter {

	/**
	 * Log category to use when no mapped handler is found for a request.
	 * @see #pageNotFoundLogger
	 */
	public static final String PAGE_NOT_FOUND_LOG_CATEGORY = "org.frameworkset.web.servlet.PageNotFound";


	/**
	 * Additional logger to use when no mapped handler is found for a request.
	 * @see #PAGE_NOT_FOUND_LOG_CATEGORY
	 */
	protected final static Logger pageNotFoundLogger = Logger.getLogger(PAGE_NOT_FOUND_LOG_CATEGORY);
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
	 * @see org.frameworkset.web.util.AntPathMatcher
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
	 * <p>Default is a {@link org.frameworkset.spi.support.LocalVariableTableParameterNameDiscoverer}.
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
		return getMethodResolver(handler.getHandlerClass()).hasHandlerMethods();
	}
	
	
	
	public boolean hasHandlerMethods(Class handler) {
		return getMethodResolver(handler).hasHandlerMethods();
	}

	public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, PageContext pageContext,HandlerMeta handler)
			throws Exception {

		if (handler.getHandlerClass().getAnnotation(SessionAttributes.class) != null) {
			// Always prevent caching in case of session attribute management.
			checkAndPrepare(request, response, this.cacheSecondsForSessionAttributeHandlers, true);
			// Prepare cached set of session attributes names.
		}
		else {
			// Uses configured default cacheSeconds setting.
			checkAndPrepare(request, response, true);
		}

		// Execute invokeHandlerMethod in synchronized block if required.
		if (this.synchronizeOnSession) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				Object mutex = WebUtils.getSessionMutex(session);
				synchronized (mutex) {
					return invokeHandlerMethod(request, response, pageContext,handler);
				}
			}
		}

		return invokeHandlerMethod(request, response, pageContext,handler);
	}

	protected ModelAndView invokeHandlerMethod(
			HttpServletRequest request, HttpServletResponse response,  PageContext pageContext,HandlerMeta handler) throws Exception {

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
		ServletHandlerMethodResolver methodResolver = getMethodResolver(handler.getHandlerClass());
		return HandlerUtils.invokeHandlerMethod(request, response, pageContext, handler, 
				methodResolver,messageConverters);
	}
	
	/**
	 * Build a HandlerMethodResolver for the given handler type.
	 */
	public  ServletHandlerMethodResolver getMethodResolver(Class handlerClass) {
//		Class handlerClass = ClassUtils.getUserClass(handler);
		
		ServletHandlerMethodResolver resolver = (org.frameworkset.web.servlet.handler.HandlerUtils.ServletHandlerMethodResolver) methodResolverCache.get(handlerClass);
		if (resolver == null) {
			resolver = new ServletHandlerMethodResolver(handlerClass, urlPathHelper, pathMatcher, methodNameResolver);
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


//	/**
//	 * Handle the case where no request handler method was found.
//	 * <p>The default implementation logs a warning and sends an HTTP 404 error.
//	 * Alternatively, a fallback view could be chosen, or the
//	 * NoSuchRequestHandlingMethodException could be rethrown as-is.
//	 * @param ex the NoSuchRequestHandlingMethodException to be handled
//	 * @param request current HTTP request
//	 * @param response current HTTP response
//	 * @return a ModelAndView to render, or <code>null</code> if handled directly
//	 * @throws Exception an Exception that should be thrown as result of the servlet request
//	 */
//	protected ModelAndView handleNoSuchRequestHandlingMethod(
//			NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//
//		pageNotFoundLogger.warn(ex.getMessage());
//		response.sendError(HttpServletResponse.SC_NOT_FOUND);
//		return null;
//	}

//	/**
//	 * Template method for creating a new ServletRequestDataBinder instance.
//	 * <p>The default implementation creates a standard ServletRequestDataBinder.
//	 * This can be overridden for custom ServletRequestDataBinder subclasses.
//	 * @param request current HTTP request
//	 * @param target the target object to bind onto (or <code>null</code>
//	 * if the binder is just used to convert a plain parameter value)
//	 * @param objectName the objectName of the target object
//	 * @return the ServletRequestDataBinder instance to use
//	 * @throws Exception in case of invalid state or arguments
//	 * @see ServletRequestDataBinder#bind(javax.servlet.ServletRequest)
//	 * @see ServletRequestDataBinder#convertIfNecessary(Object, Class, MethodParameter)
//	 */
//	protected ServletRequestDataBinder createBinder(
//			HttpServletRequest request, Object target, String objectName) throws Exception {
//
//		return new ServletRequestDataBinder(target, objectName);
//	}
//
//	/**
//	 * Build a HandlerMethodResolver for the given handler type.
//	 */
//	private ServletHandlerMethodResolver getMethodResolver(Class handlerClass) {
////		Class handlerClass = ClassUtils.getUserClass(handler);
//		
//		ServletHandlerMethodResolver resolver = this.methodResolverCache.get(handlerClass);
//		if (resolver == null) {
//			resolver = new ServletHandlerMethodResolver(handlerClass);
//			this.methodResolverCache.put(handlerClass, resolver);
//		}
//		return resolver;
//	}
//
//
//	private class ServletHandlerMethodResolver extends HandlerMethodResolver {
//
//		public ServletHandlerMethodResolver(Class<?> handlerType) {
//			super(handlerType);
//		}
//		
//		
//		public MethodData resolveHandlerMethod(HttpServletRequest request) throws ServletException {
//			String lookupPath = urlPathHelper.getLookupPathForRequest(request);
//			Map<HandlerMappingInfo, MethodInfo> targetHandlerMethods = new LinkedHashMap<HandlerMappingInfo, MethodInfo>();
//			Map<HandlerMappingInfo, String> targetPathMatches = new LinkedHashMap<HandlerMappingInfo, String>();
//			
//			
//			
//			String resolvedMethodName = methodNameResolver.getHandlerMethodName(request);
//			
//			for (MethodInfo handlerMethod : getHandlerMethods()) {
//				HandlerMappingInfo mappingInfo = new HandlerMappingInfo();
//				HandlerMapping mapping = handlerMethod.getMethodMapping();
//				if(mapping == null)
//				{
//					if(resolvedMethodName.equals(handlerMethod.getMethod().getName()))
//					{
//						String path_ = (String)request.getAttribute(org.frameworkset.web.servlet.HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
//						Map pathdatas = AnnotationUtils.resolvePathDatas(handlerMethod, path_);
//						
//						MethodData methodData = new MethodData(handlerMethod,pathdatas);
//					
//						return methodData;
//					}
//					continue;
//				}
//				mappingInfo.paths = handlerMethod.getPathPattern();
//				if (!hasTypeLevelMapping() || !Arrays.equals(mapping.method(), getTypeLevelMapping().method())) {
//					mappingInfo.methods = mapping.method();
//				}
//				if (!hasTypeLevelMapping() || !Arrays.equals(mapping.params(), getTypeLevelMapping().params())) {
//					mappingInfo.params = mapping.params();
//				}
//				boolean match = false;
//				if (handlerMethod.getPathPattern() != null && handlerMethod.getPathPattern().length > 0) {
//					for (String mappedPath : handlerMethod.getPathPattern()) {
//						if (isPathMatch(mappedPath, lookupPath)) {
//							if (checkParameters(mappingInfo, request)) {
//								match = true;
//								targetPathMatches.put(mappingInfo, mappedPath);
//							}
//							else {
//								break;
//							}
//						}
//					}
//				}
//				else {
//					// No paths specified: parameter match sufficient.
//					match = checkParameters(mappingInfo, request);
////					if (match && mappingInfo.methods.length == 0 && mappingInfo.params.length == 0 &&
////							resolvedMethodName != null && !resolvedMethodName.equals(handlerMethod.getMethod().getName())) {
////						match = false;
////					}
//				}
//				if (match) {
//					MethodInfo oldMappedMethod = targetHandlerMethods.put(mappingInfo, handlerMethod);
//					if(oldMappedMethod != null)
//						throw new IllegalStateException("Ambiguous handler methods mapped for HTTP path '" +
//								lookupPath + "': {" + oldMappedMethod + ", " + handlerMethod +
//								"}. If you intend to handle the same path in multiple methods, then factor " +
//								"them out into a dedicated handler class with that path mapped at the type level!");
////					if (oldMappedMethod != null && oldMappedMethod != handlerMethod.getMethod()) {
////						if (methodNameResolver != null && mappingInfo.paths.length == 0) {
////							if (!oldMappedMethod.getName().equals(handlerMethod.getMethod().getName())) {
////								if (resolvedMethodName == null) {
////									resolvedMethodName = methodNameResolver.getHandlerMethodName(request);
////								}
////								if (!resolvedMethodName.equals(oldMappedMethod.getName())) {
////									oldMappedMethod = null;
////								}
////								if (!resolvedMethodName.equals(handlerMethod.getMethod().getName())) {
////									if (oldMappedMethod != null) {
////										targetHandlerMethods.put(mappingInfo, oldMappedMethod);
////										oldMappedMethod = null;
////									}
////									else {
////										targetHandlerMethods.remove(mappingInfo);
////									}
////								}
////							}
////						}
////						if (oldMappedMethod != null) {
////							throw new IllegalStateException("Ambiguous handler methods mapped for HTTP path '" +
////									lookupPath + "': {" + oldMappedMethod + ", " + handlerMethod +
////									"}. If you intend to handle the same path in multiple methods, then factor " +
////									"them out into a dedicated handler class with that path mapped at the type level!");
////						}
////					}
//				}
//			}
//			if (targetHandlerMethods.size() == 1) {
//				MethodInfo handlerMethod = targetHandlerMethods.values().iterator().next();
//				String path_ = (String)request.getAttribute(org.frameworkset.web.servlet.HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
//				Map pathdatas = AnnotationUtils.resolvePathDatas(handlerMethod, path_);
//				
//				MethodData methodData = new MethodData(handlerMethod,pathdatas);
//			
//				return methodData;
////				return targetHandlerMethods.values().iterator().next();
//			}
//			else if (!targetHandlerMethods.isEmpty()) {
//				HandlerMappingInfo bestMappingMatch = null;
//				String bestPathMatch = null;
//				for (HandlerMappingInfo mapping : targetHandlerMethods.keySet()) {
//					String mappedPath = targetPathMatches.get(mapping);
//					if (bestMappingMatch == null) {
//						bestMappingMatch = mapping;
//						bestPathMatch = mappedPath;
//					}
//					else {
//						if (isBetterPathMatch(mappedPath, bestPathMatch, lookupPath) ||
//								(!isBetterPathMatch(bestPathMatch, mappedPath, lookupPath) &&
//										(isBetterMethodMatch(mapping, bestMappingMatch) ||
//										(!isBetterMethodMatch(bestMappingMatch, mapping) &&
//												isBetterParamMatch(mapping, bestMappingMatch))))) {
//							bestMappingMatch = mapping;
//							bestPathMatch = mappedPath;
//						}
//					}
//				}
//				MethodInfo handlerMethod = targetHandlerMethods.get(bestMappingMatch);
//				String path_ = (String)request.getAttribute(org.frameworkset.web.servlet.HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
//				Map pathdatas = AnnotationUtils.resolvePathDatas(handlerMethod, path_);
//				
//				MethodData methodData = new MethodData(handlerMethod,pathdatas);
//			
//				return methodData;
////				return targetHandlerMethods.get(bestMappingMatch);
//			}
//			else {
//				throw new NoSuchRequestHandlingMethodException(lookupPath, request.getMethod(), request.getParameterMap());
//			}
//		}
//
//		private boolean isPathMatch(String mappedPath, String lookupPath) {
//			if (mappedPath.equals(lookupPath) || pathMatcher.match(mappedPath, lookupPath)) {
//				return true;
//			}
//			boolean hasSuffix = (mappedPath.indexOf('.') != -1);
//			if (!hasSuffix && pathMatcher.match(mappedPath + ".*", lookupPath)) {
//				return true;
//			}
//			return (!mappedPath.startsWith("/") &&
//					(lookupPath.endsWith(mappedPath) || pathMatcher.match("/**/" + mappedPath, lookupPath) ||
//							(!hasSuffix && pathMatcher.match("/**/" + mappedPath + ".*", lookupPath))));
//		}
//
//		private boolean checkParameters(HandlerMappingInfo mapping, HttpServletRequest request) {
//			return ServletAnnotationMappingUtils.checkRequestMethod(mapping.methods, request) &&
//					ServletAnnotationMappingUtils.checkParameters(mapping.params, request);
//		}
//
//		private boolean isBetterPathMatch(String mappedPath, String mappedPathToCompare, String lookupPath) {
//			return (mappedPath != null &&
//					(mappedPathToCompare == null || mappedPathToCompare.length() < mappedPath.length() ||
//							(mappedPath.equals(lookupPath) && !mappedPathToCompare.equals(lookupPath))));
//		}
//
//		private boolean isBetterMethodMatch(HandlerMappingInfo mapping, HandlerMappingInfo mappingToCompare) {
//			return (mappingToCompare.methods.length == 0 && mapping.methods.length > 0);
//		}
//
//		private boolean isBetterParamMatch(HandlerMappingInfo mapping, HandlerMappingInfo mappingToCompare) {
//			return (mappingToCompare.params.length < mapping.params.length);
//		}
//	}
//
//
//	private class ServletHandlerMethodInvoker extends HandlerMethodInvoker {
//
//		private boolean responseArgumentUsed = false;
//
//		public ServletHandlerMethodInvoker(HandlerMethodResolver resolver) {
//			super(resolver, webBindingInitializer, sessionAttributeStore,
//					parameterNameDiscoverer, customArgumentResolvers);
//		}
//
//		@Override
//		protected void raiseMissingParameterException(String paramName, Class paramType) throws Exception {
//			throw new MissingServletRequestParameterException(paramName, paramType.getName());
//		}
//
//		@Override
//		protected void raiseSessionRequiredException(String message) throws Exception {
//			throw new HttpSessionRequiredException(message);
//		}
//
//		@Override
//		protected WebDataBinder createBinder(NativeWebRequest webRequest, Object target, String objectName)
//				throws Exception {
//
//			return AnnotationMethodHandlerAdapter.this.createBinder(
//					(HttpServletRequest) webRequest.getNativeRequest(), target, objectName);
//		}
//
//		@Override
//		protected void doBind(NativeWebRequest webRequest, WebDataBinder binder, boolean failOnErrors)
//				throws Exception {
//
//			ServletRequestDataBinder servletBinder = (ServletRequestDataBinder) binder;
//			servletBinder.bind((ServletRequest) webRequest.getNativeRequest());
//			if (failOnErrors) {
//				servletBinder.closeNoCatch();
//			}
//		}
//
//		@Override
//		protected Object resolveStandardArgument(Class parameterType, NativeWebRequest webRequest)
//				throws Exception {
//
//			HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
//			HttpServletResponse response = (HttpServletResponse) webRequest.getNativeResponse();
//
//			if (ServletRequest.class.isAssignableFrom(parameterType)) {
//				return request;
//			}
//			else if (ServletResponse.class.isAssignableFrom(parameterType)) {
//				this.responseArgumentUsed = true;
//				return response;
//			}
//			else if (HttpSession.class.isAssignableFrom(parameterType)) {
//				return request.getSession();
//			}
//			else if (Principal.class.isAssignableFrom(parameterType)) {
//				return request.getUserPrincipal();
//			}
//			else if (Locale.class.equals(parameterType)) {
//				return RequestContextUtils.getLocale(request);
//			}
//			else if (InputStream.class.isAssignableFrom(parameterType)) {
//				return request.getInputStream();
//			}
//			else if (Reader.class.isAssignableFrom(parameterType)) {
//				return request.getReader();
//			}
//			else if (OutputStream.class.isAssignableFrom(parameterType)) {
//				this.responseArgumentUsed = true;
//				return response.getOutputStream();
//			}
//			else if (Writer.class.isAssignableFrom(parameterType)) {
//				this.responseArgumentUsed = true;
//				return response.getWriter();
//			}
//			return super.resolveStandardArgument(parameterType, webRequest);
//		}
//
//		@SuppressWarnings("unchecked")
//		public ModelAndView getModelAndView(MethodInfo handlerMethod, Class handlerType, Object returnValue,
//				ModelMap implicitModel, ServletWebRequest webRequest) {
//
//			if (returnValue instanceof ModelAndView) {
//				ModelAndView mav = (ModelAndView) returnValue;
//				mav.getModelMap().mergeAttributes(implicitModel);
//				return mav;
//			}
//			else if (returnValue instanceof ModelMap) {
//				return new ModelAndView().addAllObjects(implicitModel).addAllObjects(((ModelMap) returnValue));
//			}
//			else if (returnValue instanceof Map) {
//				return new ModelAndView().addAllObjects(implicitModel).addAllObjects((Map) returnValue);
//			}
//			else if (returnValue instanceof View) {
//				return new ModelAndView((View) returnValue).addAllObjects(implicitModel);
//			}
//			else if (returnValue instanceof String) {
//				return new ModelAndView((String) returnValue).addAllObjects(implicitModel);
//			}
//			else if (returnValue == null) {
//				// Either returned null or was 'void' return.
//				if (this.responseArgumentUsed || webRequest.isNotModified()) {
//					return null;
//				}
//				else {
//					// Assuming view name translation...
//					return new ModelAndView().addAllObjects(implicitModel);
//				}
//			}
//			else if (!BeanUtils.isSimpleProperty(returnValue.getClass())) {
//				// Assume a single model attribute...
//				ModelAttribute attr = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), ModelAttribute.class);
//				String attrName = (attr != null ? attr.name() : "");
//				ModelAndView mav = new ModelAndView().addAllObjects(implicitModel);
//				if ("".equals(attrName)) {
//					Class resolvedType = GenericTypeResolver.resolveReturnType(handlerMethod.getMethod(), handlerType);
//					attrName = Conventions.getVariableNameForReturnType(handlerMethod.getMethod(), resolvedType, returnValue);
//				}
//				return mav.addObject(attrName, returnValue);
//			}
//			else {
//				throw new IllegalArgumentException("Invalid handler method return value: " + returnValue);
//			}
//		}
//	}
//
//
//	private static class HandlerMappingInfo {
//
//		public String[] paths = new String[0];
//
//		public RequestMethod[] methods = new RequestMethod[0];
//
//		public String[] params = new String[0];
//
//		public boolean equals(Object obj) {
//			HandlerMappingInfo other = (HandlerMappingInfo) obj;
//			return (Arrays.equals(this.paths, other.paths) && Arrays.equals(this.methods, other.methods) &&
//					Arrays.equals(this.params, other.params));
//		}
//
//		public int hashCode() {
//			return (Arrays.hashCode(this.paths) * 29 + Arrays.hashCode(this.methods) * 31 +
//					Arrays.hashCode(this.params));
//		}
//	}
//
//
//	public Set<MethodInfo> getHandlerMethods(Class<?> handlerType) {
//		
//		return this.getMethodResolver(handlerType).getHandlerMethods();
//	}
	
	


}
