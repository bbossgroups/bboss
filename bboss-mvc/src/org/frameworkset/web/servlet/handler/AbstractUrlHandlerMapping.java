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
package org.frameworkset.web.servlet.handler;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.frameworkset.util.AntPathMatcher;
import org.frameworkset.util.Assert;
import org.frameworkset.util.PathMatcher;
import org.frameworkset.util.beans.BeansException;
import org.frameworkset.web.servlet.HandlerExecutionChain;
import org.frameworkset.web.servlet.HandlerMapping;
import org.frameworkset.web.util.UrlPathHelper;

/**
 * <p>Title: AbstractUrlHandlerMapping.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-24
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class AbstractUrlHandlerMapping extends AbstractHandlerMapping{
	
	protected UrlPathHelper urlPathHelper = new UrlPathHelper();
	

	protected PathMatcher pathMatcher = new AntPathMatcher();
	private static Logger logger = Logger.getLogger(AbstractUrlHandlerMapping.class);
	public void destroy()
	{
		super.destroy();
		if(handlerMap != null)
		{
			handlerMap.clear();handlerMap = null;
		}
		this.rootHandler = null;
		this.urlPathHelper = null;
		this.pathMatcher = null;
	}
	/**
	 * 
	 */
	private HandlerUrlMappingRegisterTable<String,HandlerMeta> handlerMap = new HandlerUrlMappingRegisterTable<String,HandlerMeta>();

	private HandlerMeta rootHandler;

	private boolean lazyInitHandlers;
	
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
	
	public void setHandlerMap(HandlerUrlMappingRegisterTable handlerMap) {
		this.handlerMap = handlerMap;
		
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
	 * and MethodNameResolvers.
	 * @see org.frameworkset.web.servlet.mvc.mutiaction.AbstractUrlMethodNameResolver#setUrlPathHelper
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
	 * Return the PathMatcher implementation to use for matching URL paths
	 * against registered URL patterns.
	 */
	public PathMatcher getPathMatcher() {
		return this.pathMatcher;
	}

	/**
	 * Set the root handler for this handler mapping, that is,
	 * the handler to be registered for the root path ("/").
	 * <p>Default is <code>null</code>, indicating no root handler.
	 */
	public void setRootHandler(HandlerMeta rootHandler) {
		this.rootHandler = rootHandler;
	}

	/**
	 * Return the root handler for this handler mapping (registered for "/"),
	 * or <code>null</code> if none.
	 */
	public HandlerMeta getRootHandler() {
		return this.rootHandler;
	}

	/**
	 * Set whether to lazily initialize handlers. Only applicable to
	 * singleton handlers, as prototypes are always lazily initialized.
	 * Default is "false", as eager initialization allows for more efficiency
	 * through referencing the controller objects directly.
	 * <p>If you want to allow your controllers to be lazily initialized,
	 * make them "lazy-init" and set this flag to true. Just making them
	 * "lazy-init" will not work, as they are initialized through the
	 * references from the handler mapping in this case.
	 */
	public void setLazyInitHandlers(boolean lazyInitHandlers) {
		this.lazyInitHandlers = lazyInitHandlers;
	}


	/**
	 * Look up a handler for the URL path of the given request.
	 * @param request current HTTP request
	 * @return the handler instance, or <code>null</code> if none found
	 */
	protected HandlerExecutionChain getHandlerInternal(HttpServletRequest request) throws Exception {
		String lookupPath = this.urlPathHelper.getLookupPathForRequest(request);
		HandlerExecutionChain handler = lookupHandler(lookupPath, request);
		if (handler == null) {
			// We need to care for the default handler directly, since we need to
			// expose the PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE for it as well.
			HandlerMeta rawHandler = null;
			if ("/".equals(lookupPath)) {
				rawHandler = getRootHandler();
			}
			if (rawHandler == null) {
				rawHandler = getDefaultHandler();
			}
			if (rawHandler != null) {
				validateHandler(rawHandler, request);
				handler = buildPathExposingHandler(rawHandler, lookupPath,lookupPath,request.getRequestURI());
			}
		}
		if (handler != null ) {
			logger.debug("Mapping [" + lookupPath + "] to handler '" + handler.getHandler().getHandlerName() + "'");
		}
		else if (handler == null) {
			logger.debug("No handler mapping found for [" + lookupPath + "]");
		}
		return handler;
	}

	/**
	 * Look up a handler instance for the given URL path.
	 * <p>Supports direct matches, e.g. a registered "/test" matches "/test",
	 * and various Ant-style pattern matches, e.g. a registered "/t*" matches
	 * both "/test" and "/team". For details, see the AntPathMatcher class.
	 * <p>Looks for the most exact pattern, where most exact is defined as
	 * the longest path pattern.
	 * @param urlPath URL the bean is mapped to
	 * @param request current HTTP request (to expose the path within the mapping to)
	 * @return the associated handler instance, or <code>null</code> if not found
	 * @see #exposePathWithinMapping
	 * @see org.frameworkset.web.util.AntPathMatcher
	 */
	protected HandlerExecutionChain lookupHandler(String urlPath, HttpServletRequest request) throws Exception {
		// Direct match?
		HandlerMeta handler = this.handlerMap.get(urlPath);
		if (handler != null) {
			validateHandler(handler, request);
			return buildPathExposingHandler(handler, urlPath,urlPath,request.getRequestURI());
		}
		// Pattern match?
		String bestPathMatch = null;
		for (Iterator it = this.handlerMap.keySet().iterator(); it.hasNext();) {
			String registeredPath = (String) it.next();
			if (getPathMatcher().match(registeredPath, urlPath) &&
					(bestPathMatch == null || bestPathMatch.length() < registeredPath.length())) {
				bestPathMatch = registeredPath;
			}
		}
		if (bestPathMatch != null) {
			handler = this.handlerMap.get(bestPathMatch);
			validateHandler(handler, request);
			String pathWithinMapping = getPathMatcher().extractPathWithinPattern(bestPathMatch, urlPath);
			return buildPathExposingHandler(handler, pathWithinMapping,urlPath,request.getRequestURI());
		}
		// No handler found...
		return null;
	}

	/**
	 * Validate the given handler against the current request.
	 * <p>The default implementation is empty. Can be overridden in subclasses,
	 * for example to enforce specific preconditions expressed in URL mappings.
	 * @param handler the handler object to validate
	 * @param request current HTTP request
	 * @throws Exception if validation failed
	 */
	protected void validateHandler(HandlerMeta handler, HttpServletRequest request) throws Exception {
	}

	/**
	 * Build a handler object for the given raw handler, exposing the actual
	 * handler as well as the {@link #PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE}
	 * before executing the handler.
	 * <p>The default implementation builds a {@link HandlerExecutionChain}
	 * with a special interceptor that exposes the path attribute.
	 * @param rawHandler the raw handler to expose
	 * @param pathWithinMapping the path to expose before executing the handler
	 * @return the final handler object
	 */
	protected HandlerExecutionChain buildPathExposingHandler(HandlerMeta rawHandler, String pathWithinMapping,String mappingpath,String requesturi) {
		// Bean name or resolved handler?
		HandlerMeta ret = rawHandler;
		if (rawHandler.getHandler() instanceof String) {
			String handlerName = (String) rawHandler.getHandler();
			ret = new HandlerMeta();
			ret.setPathNames(rawHandler.getPathNames());
			ret.setHandler(getApplicationContext().getBeanObject(handlerName));
		}
		HandlerExecutionChain chain = new HandlerExecutionChain(ret);
		chain.addInterceptor(new PathExposingHandlerInterceptor(mappingpath,pathWithinMapping,requesturi));
		return chain;
	}

	/**
	 * Expose the path within the current mapping as request attribute.
	 * @param pathWithinMapping the path within the current mapping
	 * @param request the request to expose the path to
	 * @see #PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE
	 */
	protected void exposePathWithinMapping(String pathWithinMapping, HttpServletRequest request) {
		request.setAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, pathWithinMapping);
	}
	
	/**
	 * Expose the path  the current mapping as request attribute.
	 * @param mappingPath the path within the current mapping
	 * @param request the request to expose the path to
	 * @see #HANDLER_MAPPING_PATH_ATTRIBUTE
	 */
	protected void exposeHandleMappingPath(String mappingPath, HttpServletRequest request) {
		request.setAttribute(HandlerMapping.HANDLER_MAPPING_PATH_ATTRIBUTE, mappingPath);
	}
	
	/**
	 * Expose the path  the current mapping as request attribute.
	 * @param mappingPath the path within the current mapping
	 * @param request the request to expose the path to
	 * @see #HANDLER_MAPPING_PATH_ATTRIBUTE
	 */
	public static void exposeAttribute(String key,Object value, HttpServletRequest request) {
		request.setAttribute(key, value);
	}
	
	/**
	 * Expose the path  the current mapping as request attribute.
	 * @param mappingPath the path within the current mapping
	 * @param request the request to expose the path to
	 * @see #HANDLER_MAPPING_PATH_ATTRIBUTE
	 */
	public static void removeAttribute(String key,HttpServletRequest request) {
		request.removeAttribute(key);
	}



	/**
	 * Register the specified handler for the given URL paths.
	 * @param urlPaths the URLs that the bean should be mapped to
	 * @param beanName the name of the handler bean
	 * @throws BeansException if the handler couldn't be registered
	 * @throws IllegalStateException if there is a conflicting handler registered
	 */
	protected HandlerMeta registerHandler(String[] urlPaths, HandlerMeta meta) throws Exception, IllegalStateException {
		Assert.notNull(urlPaths, "URL path array must not be null");
		HandlerMeta ret = null;
		for (int j = 0; j < urlPaths.length; j++) {
			if(j == 0)
				ret = registerHandler(urlPaths[j], meta);
			else
				registerHandler(urlPaths[j], meta);
		}
		return ret;
	}

	/**
	 * Register the specified handler for the given URL path.
	 * @param urlPath the URL the bean should be mapped to
	 * @param handler the handler instance or handler bean name String
	 * (a bean name will automatically be resolved into the corresponding handler bean)
	 * @throws BeansException if the handler couldn't be registered
	 * @throws IllegalStateException if there is a conflicting handler registered
	 */
	protected HandlerMeta registerHandler(String urlPath, HandlerMeta handler) throws Exception, IllegalStateException {
		Assert.notNull(urlPath, "URL path must not be null");
		Assert.notNull(handler, "Handler object must not be null");
		HandlerMeta resolvedHandler = handler;

		// Eagerly resolve handler if referencing singleton via name.
		if (!this.lazyInitHandlers && handler.getHandler() instanceof String) {
			String handlerName = (String) handler.getHandler();
			
			if (getApplicationContext().isSingleton(handlerName)) {
				resolvedHandler.setHandler(getApplicationContext().getBeanObject(handlerName));
			}
		}

		HandlerMeta mappedHandler = this.handlerMap.get(urlPath);
		if (mappedHandler != null) {
			if (mappedHandler.getHandler() != resolvedHandler.getHandler()) {
				throw new IllegalStateException(
						"Cannot map handler [" + handler.getHandlerName() + "] to URL path [" + urlPath +
						"]: There is already handler [" + resolvedHandler.getHandlerName() + "] mapped.");
			}
		}
		else {
			if (urlPath.equals("/")) {
				if (logger.isDebugEnabled()) {
					logger.debug("Root mapping to handler [" + resolvedHandler.getHandlerName() + "]");
				}
				setRootHandler(resolvedHandler);
			}
			else if (urlPath.equals("/*")) {
				if (logger.isDebugEnabled()) {
					logger.debug("Default mapping to handler [" + resolvedHandler.getHandlerName() + "]");
				}
				setDefaultHandler(resolvedHandler);
			}
			else {
				this.handlerMap.put(urlPath, resolvedHandler);
				if (logger.isDebugEnabled()) {
					logger.debug("Mapped URL path [" + urlPath + "] onto handler [" + resolvedHandler.getHandlerName() + "]");
				}
			}
		}
		return resolvedHandler;
	}


//	/**
//	 * Return the registered handlers as an unmodifiable Map, with the registered path
//	 * as key and the handler object (or handler bean name in case of a lazy-init handler)
//	 * as value.
//	 * @see #getDefaultHandler()
//	 */
//	public final Map getHandlerMap() {
//		return Collections.unmodifiableMap(this.handlerMap);
//	}


	/**
	 * Special interceptor for exposing the
	 * {@link AbstractUrlHandlerMapping#PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE} attribute.
	 * @link AbstractUrlHandlerMapping#exposePathWithinMapping
	 */
	private class PathExposingHandlerInterceptor extends HandlerInterceptorAdapter {

		private final String pathWithinMapping;
		private final String mappingpath;
		private final String requesturi;

		public PathExposingHandlerInterceptor(String mappingpath,String pathWithinMapping,String requesturi) {
			this.pathWithinMapping = pathWithinMapping;
			this.mappingpath = mappingpath;
			this.requesturi = requesturi;
		}

		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, HandlerMeta handler) {
			exposePathWithinMapping(this.pathWithinMapping, request);
			exposeHandleMappingPath(this.mappingpath, request);
			exposeAttribute(HANDLER_REQUESTURI_ATTRIBUTE,this.requesturi, request) ;
			return true;
		}
	}

}
