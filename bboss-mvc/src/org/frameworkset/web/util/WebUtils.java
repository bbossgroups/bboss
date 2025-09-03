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
package org.frameworkset.web.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.frameworkset.http.HttpRequest;
import org.frameworkset.spi.ApplicationContextAware;
import org.frameworkset.spi.BeanClassLoaderAware;
import org.frameworkset.spi.BeanInfoAware;
import org.frameworkset.spi.BeanNameAware;
import org.frameworkset.spi.ResourceLoaderAware;
import org.frameworkset.spi.assemble.BeanAccembleHelper;
import org.frameworkset.spi.assemble.BeanInf;
import org.frameworkset.spi.assemble.Pro;
import org.frameworkset.spi.support.ApplicationObjectSupport;
import org.frameworkset.spi.support.MessageSourceAware;
import org.frameworkset.util.Assert;
import org.frameworkset.util.CollectionUtils;
import org.frameworkset.web.servlet.HandlerMapping;
import org.frameworkset.web.servlet.context.ServletContextAware;
import org.frameworkset.web.servlet.context.WebApplicationContext;
import org.frameworkset.web.servlet.mvc.WebRequest;
import org.frameworkset.web.servlet.support.WebApplicationObjectSupport;

import com.frameworkset.spi.assemble.BeanInstanceException;
import com.frameworkset.util.StringUtil;


/**
 * <p>Title: WebUtils.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-22
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class WebUtils {
	/**
	 * Return an appropriate request object of the specified type, if available,
	 * unwrapping the given request as far as necessary.
	 * @param request the servlet request to introspect
	 * @param requiredType the desired type of request object
	 * @return the matching request object, or {@code null} if none
	 * of that type is available
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getNativeRequest(ServletRequest request, Class<T> requiredType) {
		if (requiredType != null) {
			if (requiredType.isInstance(request)) {
				return (T) request;
			}
			else if (request instanceof ServletRequestWrapper) {
				return getNativeRequest(((ServletRequestWrapper) request).getRequest(), requiredType);
			}
		}
		return null;
	}

	/**
	 * Return an appropriate response object of the specified type, if available,
	 * unwrapping the given response as far as necessary.
	 * @param response the servlet response to introspect
	 * @param requiredType the desired type of response object
	 * @return the matching response object, or {@code null} if none
	 * of that type is available
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getNativeResponse(ServletResponse response, Class<T> requiredType) {
		if (requiredType != null) {
			if (requiredType.isInstance(response)) {
				return (T) response;
			}
			else if (response instanceof ServletResponseWrapper) {
				return getNativeResponse(((ServletResponseWrapper) response).getResponse(), requiredType);
			}
		}
		return null;
	}

	/**
	 * Standard Servlet 2.3+ spec request attributes for include URI and paths.
	 * <p>If included via a RequestDispatcher, the current resource will see the
	 * originating request. Its own URI and paths are exposed as request attributes.
	 */
	public static final String INCLUDE_REQUEST_URI_ATTRIBUTE = "javax.servlet.include.request_uri";
	public static final String INCLUDE_CONTEXT_PATH_ATTRIBUTE = "javax.servlet.include.context_path";
	public static final String INCLUDE_SERVLET_PATH_ATTRIBUTE = "javax.servlet.include.servlet_path";
	public static final String INCLUDE_PATH_INFO_ATTRIBUTE = "javax.servlet.include.path_info";
	public static final String INCLUDE_QUERY_STRING_ATTRIBUTE = "javax.servlet.include.query_string";

	/**
	 * Standard Servlet 2.4+ spec request attributes for forward URI and paths.
	 * <p>If forwarded to via a RequestDispatcher, the current resource will see its
	 * own URI and paths. The originating URI and paths are exposed as request attributes.
	 */
	public static final String FORWARD_REQUEST_URI_ATTRIBUTE = "javax.servlet.forward.request_uri";
	public static final String FORWARD_CONTEXT_PATH_ATTRIBUTE = "javax.servlet.forward.context_path";
	public static final String FORWARD_SERVLET_PATH_ATTRIBUTE = "javax.servlet.forward.servlet_path";
	public static final String FORWARD_PATH_INFO_ATTRIBUTE = "javax.servlet.forward.path_info";
	public static final String FORWARD_QUERY_STRING_ATTRIBUTE = "javax.servlet.forward.query_string";

	/**
	 * Standard Servlet 2.3+ spec request attributes for error pages.
	 * <p>To be exposed to JSPs that are marked as error pages, when forwarding
	 * to them directly rather than through the servlet container's error page
	 * resolution mechanism.
	 */
	public static final String ERROR_STATUS_CODE_ATTRIBUTE = "javax.servlet.error.status_code";
	public static final String ERROR_EXCEPTION_TYPE_ATTRIBUTE = "javax.servlet.error.exception_type";
	public static final String ERROR_MESSAGE_ATTRIBUTE = "javax.servlet.error.message";
	public static final String ERROR_EXCEPTION_ATTRIBUTE = "javax.servlet.error.exception";
	public static final String ERROR_REQUEST_URI_ATTRIBUTE = "javax.servlet.error.request_uri";
	public static final String ERROR_SERVLET_NAME_ATTRIBUTE = "javax.servlet.error.servlet_name";


	/**
	 * Prefix of the charset clause in a content type String: ";charset="
	 */
	public static final String CONTENT_TYPE_CHARSET_PREFIX = ";charset=";

	/**
	 * Default character encoding to use when <code>request.getCharacterEncoding</code>
	 * returns <code>null</code>, according to the Servlet spec.
	 * @see ServletRequest#getCharacterEncoding
	 */
	public static final String DEFAULT_CHARACTER_ENCODING = "ISO-8859-1";

	/**
	 * Standard Servlet spec context attribute that specifies a temporary
	 * directory for the current web application, of type <code>java.io.File</code>.
	 */
	public static final String TEMP_DIR_CONTEXT_ATTRIBUTE = "javax.servlet.context.tempdir";

	/**
	 * HTML escape parameter at the servlet context level
	 * (i.e. a context-param in <code>web.xml</code>): "defaultHtmlEscape".
	 */
	public static final String HTML_ESCAPE_CONTEXT_PARAM = "defaultHtmlEscape";

	/**
	 * Web app root key parameter at the servlet context level
	 * (i.e. a context-param in <code>web.xml</code>): "webAppRootKey".
	 */
	public static final String WEB_APP_ROOT_KEY_PARAM = "webAppRootKey";

	/** Default web app root key: "webapp.root" */
	public static final String DEFAULT_WEB_APP_ROOT_KEY = "webapp.root";

	/** Name suffixes in case of image buttons */
	public static final String[] SUBMIT_IMAGE_SUFFIXES = {".x", ".y"};

	/** Key for the mutex session attribute */
	public static final String SESSION_MUTEX_ATTRIBUTE = WebUtils.class.getName() + ".MUTEX";


	/**
	 * Set a system property to the web application root directory.
	 * The key of the system property can be defined with the "webAppRootKey"
	 * context-param in <code>web.xml</code>. Default is "webapp.root".
	 * <p>Can be used for tools that support substition with <code>System.getProperty</code>
	 * values, like log4j's "${key}" syntax within log file locations.
	 * @param servletContext the servlet context of the web application
	 * @throws IllegalStateException if the system property is already set,
	 * or if the WAR file is not expanded
	 * @see #WEB_APP_ROOT_KEY_PARAM
	 * @see #DEFAULT_WEB_APP_ROOT_KEY
	 * @see WebAppRootListener
	 * @see Log4jWebConfigurer
	 */
	public static void setWebAppRootSystemProperty(ServletContext servletContext) throws IllegalStateException {
		Assert.notNull(servletContext, "ServletContext must not be null");
		String root = servletContext.getRealPath("/");
		if (root == null) {
			throw new IllegalStateException(
			    "Cannot set web app root system property when WAR file is not expanded");
		}
		String param = servletContext.getInitParameter(WEB_APP_ROOT_KEY_PARAM);
		String key = (param != null ? param : DEFAULT_WEB_APP_ROOT_KEY);
		String oldValue = System.getProperty(key);
		if (oldValue != null && !StringUtil.pathEquals(oldValue, root)) {
			throw new IllegalStateException(
			    "Web app root system property already set to different value: '" +
			    key + "' = [" + oldValue + "] instead of [" + root + "] - " +
			    "Choose unique values for the 'webAppRootKey' context-param in your web.xml files!");
		}
		System.setProperty(key, root);
		servletContext.log("Set web app root system property: '" + key + "' = [" + root + "]");
	}

	/**
	 * Remove the system property that points to the web app root directory.
	 * To be called on shutdown of the web application.
	 * @param servletContext the servlet context of the web application
	 * @see #setWebAppRootSystemProperty
	 */
	public static void removeWebAppRootSystemProperty(ServletContext servletContext) {
		Assert.notNull(servletContext, "ServletContext must not be null");
		String param = servletContext.getInitParameter(WEB_APP_ROOT_KEY_PARAM);
		String key = (param != null ? param : DEFAULT_WEB_APP_ROOT_KEY);
		System.getProperties().remove(key);
	}

	/**
	 * Return whether default HTML escaping is enabled for the web application,
	 * i.e. the value of the "defaultHtmlEscape" context-param in <code>web.xml</code>
	 * (if any). Falls back to <code>false</code> in case of no explicit default given.
	 * @param servletContext the servlet context of the web application
	 * @return whether default HTML escaping is enabled (default is false)
	 */
	public static boolean isDefaultHtmlEscape(ServletContext servletContext) {
		if (servletContext == null) {
			return false;
		}
		String param = servletContext.getInitParameter(HTML_ESCAPE_CONTEXT_PARAM);
		return Boolean.valueOf(param).booleanValue();
	}

	/**
	 * Return whether default HTML escaping is enabled for the web application,
	 * i.e. the value of the "defaultHtmlEscape" context-param in <code>web.xml</code>
	 * (if any).
	 * <p>This method differentiates between no param specified at all and
	 * an actual boolean value specified, allowing to have a context-specific
	 * default in case of no setting at the global level.
	 * @param servletContext the servlet context of the web application
	 * @return whether default HTML escaping is enabled (null = no explicit default)
	 */
	public static Boolean getDefaultHtmlEscape(ServletContext servletContext) {
		if (servletContext == null) {
			return null;
		}
		Assert.notNull(servletContext, "ServletContext must not be null");
		String param = servletContext.getInitParameter(HTML_ESCAPE_CONTEXT_PARAM);
		return (StringUtil.hasText(param)? Boolean.valueOf(param) : null);
	}

	/**
	 * Return the temporary directory for the current web application,
	 * as provided by the servlet container.
	 * @param servletContext the servlet context of the web application
	 * @return the File representing the temporary directory
	 */
	public static File getTempDir(ServletContext servletContext) {
		Assert.notNull(servletContext, "ServletContext must not be null");
		return (File) servletContext.getAttribute(TEMP_DIR_CONTEXT_ATTRIBUTE);
	}

	/**
	 * Return the real path of the given path within the web application,
	 * as provided by the servlet container.
	 * <p>Prepends a slash if the path does not already start with a slash,
	 * and throws a FileNotFoundException if the path cannot be resolved to
	 * a resource (in contrast to ServletContext's <code>getRealPath</code>,
	 * which returns null).
	 * @param servletContext the servlet context of the web application
	 * @param path the path within the web application
	 * @return the corresponding real path
	 * @throws FileNotFoundException if the path cannot be resolved to a resource
	 * @see javax.servlet.ServletContext#getRealPath
	 */
	public static String getRealPath(ServletContext servletContext, String path) throws FileNotFoundException {
		Assert.notNull(servletContext, "ServletContext must not be null");
		// Interpret location as relative to the web application root directory.
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		String realPath = servletContext.getRealPath(path);
		if (realPath == null) {
			throw new FileNotFoundException(
					"ServletContext resource [" + path + "] cannot be resolved to absolute file path - " +
					"web application archive not expanded?");
		}
		return realPath;
	}


	/**
	 * Determine the session id of the given request, if any.
	 * @param request current HTTP request
	 * @return the session id, or <code>null</code> if none
	 */
	public static String getSessionId(HttpServletRequest request) {
		Assert.notNull(request, "Request must not be null");
		HttpSession session = request.getSession(false);
		return (session != null ? session.getId() : null);
	}

	/**
	 * Check the given request for a session attribute of the given name.
	 * Returns null if there is no session or if the session has no such attribute.
	 * Does not create a new session if none has existed before!
	 * @param request current HTTP request
	 * @param name the name of the session attribute
	 * @return the value of the session attribute, or <code>null</code> if not found
	 */
	public static Object getSessionAttribute(HttpServletRequest request, String name) {
		Assert.notNull(request, "Request must not be null");
		HttpSession session = request.getSession(false);
		return (session != null ? session.getAttribute(name) : null);
	}

	/**
	 * Check the given request for a session attribute of the given name.
	 * Throws an exception if there is no session or if the session has no such
	 * attribute. Does not create a new session if none has existed before!
	 * @param request current HTTP request
	 * @param name the name of the session attribute
	 * @return the value of the session attribute, or <code>null</code> if not found
	 * @throws IllegalStateException if the session attribute could not be found
	 */
	public static Object getRequiredSessionAttribute(HttpServletRequest request, String name)
	    throws IllegalStateException {

		Object attr = getSessionAttribute(request, name);
		if (attr == null) {
			throw new IllegalStateException("No session attribute '" + name + "' found");
		}
		return attr;
	}

	/**
	 * Set the session attribute with the given name to the given value.
	 * Removes the session attribute if value is null, if a session existed at all.
	 * Does not create a new session if not necessary!
	 * @param request current HTTP request
	 * @param name the name of the session attribute
	 * @param value the value of the session attribute
	 */
	public static void setSessionAttribute(HttpServletRequest request, String name, Object value) {
		Assert.notNull(request, "Request must not be null");
		if (value != null) {
			request.getSession().setAttribute(name, value);
		}
		else {
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.removeAttribute(name);
			}
		}
	}

	/**
	 * Get the specified session attribute, creating and setting a new attribute if
	 * no existing found. The given class needs to have a public no-arg constructor.
	 * Useful for on-demand state objects in a web tier, like shopping carts.
	 * @param session current HTTP session
	 * @param name the name of the session attribute
	 * @param clazz the class to instantiate for a new attribute
	 * @return the value of the session attribute, newly created if not found
	 * @throws IllegalArgumentException if the session attribute could not be instantiated
	 */
	public static Object getOrCreateSessionAttribute(HttpSession session, String name, Class clazz)
			throws IllegalArgumentException {

		Assert.notNull(session, "Session must not be null");
		Object sessionObject = session.getAttribute(name);
		if (sessionObject == null) {
			try {
				sessionObject = clazz.newInstance();
			}
			catch (InstantiationException ex) {
				throw new IllegalArgumentException(
				    "Could not instantiate class [" + clazz.getName() +
				    "] for session attribute '" + name + "': " + ex.getMessage());
			}
			catch (IllegalAccessException ex) {
				throw new IllegalArgumentException(
				    "Could not access default constructor of class [" + clazz.getName() +
				    "] for session attribute '" + name + "': " + ex.getMessage());
			}
			session.setAttribute(name, sessionObject);
		}
		return sessionObject;
	}

	/**
	 * Return the best available mutex for the given session:
	 * that is, an object to synchronize on for the given session.
	 * <p>Returns the session mutex attribute if available; usually,
	 * this means that the HttpSessionMutexListener needs to be defined
	 * in <code>web.xml</code>. Falls back to the HttpSession itself
	 * if no mutex attribute found.
	 * <p>The session mutex is guaranteed to be the same object during
	 * the entire lifetime of the session, available under the key defined
	 * by the <code>SESSION_MUTEX_ATTRIBUTE</code> constant. It serves as a
	 * safe reference to synchronize on for locking on the current session.
	 * <p>In many cases, the HttpSession reference itself is a safe mutex
	 * as well, since it will always be the same object reference for the
	 * same active logical session. However, this is not guaranteed across
	 * different servlet containers; the only 100% safe way is a session mutex.
	 * @param session the HttpSession to find a mutex for
	 * @return the mutex object (never <code>null</code>)
	 * @see #SESSION_MUTEX_ATTRIBUTE
	 * @see HttpSessionMutexListener
	 */
	public static Object getSessionMutex(HttpSession session) {
		Assert.notNull(session, "Session must not be null");
		Object mutex = session.getAttribute(SESSION_MUTEX_ATTRIBUTE);
		if (mutex == null) {
			mutex = session;
		}
		return mutex;
	}


	/**
	 * Determine whether the given request is an include request,
	 * that is, not a top-level HTTP request coming in from the outside.
	 * <p>Checks the presence of the "javax.servlet.include.request_uri"
	 * request attribute. Could check any request attribute that is only
	 * present in an include request.
	 * @param request current servlet request
	 * @return whether the given request is an include request
	 */
	public static boolean isIncludeRequest(ServletRequest request) {
		return (request.getAttribute(INCLUDE_REQUEST_URI_ATTRIBUTE) != null);
	}

	/**
	 * Expose the current request URI and paths as {@link javax.servlet.http.HttpServletRequest}
	 * attributes under the keys defined in the Servlet 2.4 specification,
	 * for containers that implement 2.3 or an earlier version of the Servlet API:
	 * <code>javax.servlet.forward.request_uri</code>,
	 * <code>javax.servlet.forward.context_path</code>,
	 * <code>javax.servlet.forward.servlet_path</code>,
	 * <code>javax.servlet.forward.path_info</code>,
	 * <code>javax.servlet.forward.query_string</code>.
	 * <p>Does not override values if already present, to not cause conflicts
	 * with the attributes exposed by Servlet 2.4+ containers themselves.
	 * @param request current servlet request
	 */
	public static void exposeForwardRequestAttributes(HttpServletRequest request) {
		exposeRequestAttributeIfNotPresent(request, FORWARD_REQUEST_URI_ATTRIBUTE, request.getRequestURI());
		exposeRequestAttributeIfNotPresent(request, FORWARD_CONTEXT_PATH_ATTRIBUTE, request.getContextPath());
		exposeRequestAttributeIfNotPresent(request, FORWARD_SERVLET_PATH_ATTRIBUTE, request.getServletPath());
		exposeRequestAttributeIfNotPresent(request, FORWARD_PATH_INFO_ATTRIBUTE, request.getPathInfo());
		exposeRequestAttributeIfNotPresent(request, FORWARD_QUERY_STRING_ATTRIBUTE, request.getQueryString());
	}

	/**
	 * Expose the Servlet spec's error attributes as {@link javax.servlet.http.HttpServletRequest}
	 * attributes under the keys defined in the Servlet 2.3 specification, for error pages that
	 * are rendered directly rather than through the Servlet container's error page resolution.
	 * <code>javax.servlet.error.status_code</code>,
	 * <code>javax.servlet.error.exception_type</code>,
	 * <code>javax.servlet.error.message</code>,
	 * <code>javax.servlet.error.exception</code>,
	 * <code>javax.servlet.error.request_uri</code>,
	 * <code>javax.servlet.error.servlet_name</code>.
	 * <p>Does not override values if already present, to respect attribute values
	 * that have been exposed explicitly before.
	 * <p>Exposes status code 200 by default. Set the "javax.servlet.error.status_code"
	 * attribute explicitly (before or after) in order to expose a different status code.
	 * @param request current servlet request
	 * @param ex the exception encountered
	 * @param servletName the name of the offending servlet
	 */
	public static void exposeErrorRequestAttributes(HttpServletRequest request, Throwable ex, String servletName) {
		exposeRequestAttributeIfNotPresent(request, ERROR_STATUS_CODE_ATTRIBUTE, HttpServletResponse.SC_OK);
		exposeRequestAttributeIfNotPresent(request, ERROR_EXCEPTION_TYPE_ATTRIBUTE, ex.getClass());
		exposeRequestAttributeIfNotPresent(request, ERROR_MESSAGE_ATTRIBUTE, ex.getMessage());
		exposeRequestAttributeIfNotPresent(request, ERROR_EXCEPTION_ATTRIBUTE, ex);
		exposeRequestAttributeIfNotPresent(request, ERROR_REQUEST_URI_ATTRIBUTE, request.getRequestURI());
		exposeRequestAttributeIfNotPresent(request, ERROR_SERVLET_NAME_ATTRIBUTE, servletName);
	}

	/**
	 * Expose the specified request attribute if not already present.
	 * @param request current servlet request
	 * @param name the name of the attribute
	 * @param value the suggested value of the attribute
	 */
	private static void exposeRequestAttributeIfNotPresent(ServletRequest request, String name, Object value) {
		if (request.getAttribute(name) == null) {
			request.setAttribute(name, value);
		}
	}

	/**
	 * Expose the given Map as request attributes, using the keys as attribute names
	 * and the values as corresponding attribute values. Keys need to be Strings.
	 * @param request current HTTP request
	 * @param attributes the attributes Map
	 */
	public static void exposeRequestAttributes(ServletRequest request, Map attributes) {
		Assert.notNull(request, "Request must not be null");
		Iterator it = attributes.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			if (!(entry.getKey() instanceof String)) {
				throw new IllegalArgumentException(
						"Invalid key [" + entry.getKey() + "] in attributes Map - only Strings allowed as attribute keys");
			}
			request.setAttribute((String) entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Retrieve the first cookie with the given name. Note that multiple
	 * cookies can have the same name but different paths or domains.
	 * @param request current servlet request
	 * @param name cookie name
	 * @return the first cookie with the given name, or <code>null</code> if none is found
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Assert.notNull(request, "Request must not be null");
		Cookie cookies[] = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (name.equals(cookies[i].getName())) {
					return cookies[i];
				}
			}
		}
		return null;
	}


	/**
	 * Check if a specific input type="submit" parameter was sent in the request,
	 * either via a button (directly with name) or via an image (name + ".x" or
	 * name + ".y").
	 * @param request current HTTP request
	 * @param name name of the parameter
	 * @return if the parameter was sent
	 * @see #SUBMIT_IMAGE_SUFFIXES
	 */
	public static boolean hasSubmitParameter(ServletRequest request, String name) {
		Assert.notNull(request, "Request must not be null");
		if (request.getParameter(name) != null) {
			return true;
		}
		for (int i = 0; i < SUBMIT_IMAGE_SUFFIXES.length; i++) {
			String suffix = SUBMIT_IMAGE_SUFFIXES[i];
			if (request.getParameter(name + suffix) != null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Obtain a named parameter from the given request parameters.
	 * <p>See {@link #findParameterValue(java.util.Map, String)}
	 * for a description of the lookup algorithm.
	 * @param request current HTTP request
	 * @param name the <i>logical</i> name of the request parameter
	 * @return the value of the parameter, or <code>null</code>
	 * if the parameter does not exist in given request
	 */
	public static String findParameterValue(ServletRequest request, String name) {
		return findParameterValue(request.getParameterMap(), name);
	}

	/**
	 * Obtain a named parameter from the given request parameters.
	 * <p>This method will try to obtain a parameter value using the
	 * following algorithm:
	 * <ol>
	 * <li>Try to get the parameter value using just the given <i>logical</i> name.
	 * This handles parameters of the form <tt>logicalName = value</tt>. For normal
	 * parameters, e.g. submitted using a hidden HTML form field, this will return
	 * the requested value.</li>
	 * <li>Try to obtain the parameter value from the parameter name, where the
	 * parameter name in the request is of the form <tt>logicalName_value = xyz</tt>
	 * with "_" being the configured delimiter. This deals with parameter values
	 * submitted using an HTML form submit button.</li>
	 * <li>If the value obtained in the previous step has a ".x" or ".y" suffix,
	 * remove that. This handles cases where the value was submitted using an
	 * HTML form image button. In this case the parameter in the request would
	 * actually be of the form <tt>logicalName_value.x = 123</tt>. </li>
	 * </ol>
	 * @param parameters the available parameter map
	 * @param name the <i>logical</i> name of the request parameter
	 * @return the value of the parameter, or <code>null</code>
	 * if the parameter does not exist in given request
	 */
	public static String findParameterValue(Map parameters, String name) {
		// First try to get it as a normal name=value parameter
		String value = (String) parameters.get(name);
		if (value != null) {
			return value;
		}
		// If no value yet, try to get it as a name_value=xyz parameter
		String prefix = name + "_";
		Iterator paramNames = parameters.keySet().iterator();
		while (paramNames.hasNext()) {
			String paramName = (String) paramNames.next();
			if (paramName.startsWith(prefix)) {
				// Support images buttons, which would submit parameters as name_value.x=123
				for (int i = 0; i < SUBMIT_IMAGE_SUFFIXES.length; i++) {
					String suffix = SUBMIT_IMAGE_SUFFIXES[i];
					if (paramName.endsWith(suffix)) {
						return paramName.substring(prefix.length(), paramName.length() - suffix.length());
					}
				}
				return paramName.substring(prefix.length());
			}
		}
		// We couldn't find the parameter value...
		return null;
	}
	
	public static String getHANDLER_REQUESTURI(HttpServletRequest request)
	{
		return (String)request.getAttribute(HandlerMapping.HANDLER_REQUESTURI_ATTRIBUTE);
	}
	
	public static String getHANDLER_Mappingpath(HttpServletRequest request)
	{
		return (String)request.getAttribute(HandlerMapping.HANDLER_MAPPING_PATH_ATTRIBUTE);
	}

	/**
	 * Return a map containing all parameters with the given prefix.
	 * Maps single values to String and multiple values to String array.
	 * <p>For example, with a prefix of "_", "_param1" and
	 * "_param2" result in a Map with "param1" and "param2" as keys.
	 * @param request HTTP request in which to look for parameters
	 * @param prefix the beginning of parameter names
	 * (if this is null or the empty string, all parameters will match)
	 * @return map containing request parameters <b>without the prefix</b>,
	 * containing either a String or a String array as values
	 * @see javax.servlet.ServletRequest#getParameterNames
	 * @see javax.servlet.ServletRequest#getParameterValues
	 * @see javax.servlet.ServletRequest#getParameterMap
	 */
	public static Map getParametersStartingWith(ServletRequest request, String prefix) {
		Assert.notNull(request, "Request must not be null");
		Enumeration paramNames = request.getParameterNames();
		Map params = new TreeMap();
		boolean hasprefix = false;
		if (prefix != null && !prefix .equals( "")) {
			
			hasprefix = true;
		}
		
		while (paramNames != null && paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			if (hasprefix && paramName.startsWith(prefix)) {
				String unprefixed = paramName.substring(prefix.length());
				String[] values = request.getParameterValues(paramName);
				if (values == null || values.length == 0) {
					// Do nothing, no values found at all.
				}
				else if (values.length > 1) {
					params.put(unprefixed, values);
				}
				else {
					params.put(unprefixed, values[0]);
				}
			}
		}
		return params;
	}
	
	
	/**
	 * Return a map containing all parameters with the given prefix.
	 * Maps single values to String and multiple values to String array.
	 * <p>For example, with a prefix of "_", "_param1" and
	 * "_param2" result in a Map with "param1" and "param2" as keys.
	 * @param request HTTP request in which to look for parameters
	 * @param prefix the beginning of parameter names
	 * (if this is null or the empty string, all parameters will match)
	 * @return map containing request parameters <b>without the prefix</b>,
	 * containing either a String or a String array as values
	 * @see javax.servlet.ServletRequest#getParameterNames
	 * @see javax.servlet.ServletRequest#getParameterValues
	 * @see javax.servlet.ServletRequest#getParameterMap
	 */
	public static Map getParametersStartingWith(WebRequest request, String prefix) {
		Assert.notNull(request, "Request must not be null");
		Iterator paramNames = null;
		try{
			paramNames = request.getParameterMap().keySet().iterator();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		Map params = new TreeMap();
		boolean hasprefix = false;
		if (prefix != null && !prefix .equals( "")) {
			
			hasprefix = true;
		}
		while (paramNames != null && paramNames.hasNext()) {
			String paramName = (String) paramNames.next();
			if (hasprefix && paramName.startsWith(prefix)) {
				String unprefixed = paramName.substring(prefix.length());
				String[] values = request.getParameterValues(paramName);
				if (values == null || values.length == 0) {
					// Do nothing, no values found at all.
				}
				else if (values.length > 1) {
					params.put(unprefixed, values);
				}
				else {
					params.put(unprefixed, values[0]);
				}
			}
		}
		return params;
	}

	/**
	 * Return the target page specified in the request.
	 * @param request current servlet request
	 * @param paramPrefix the parameter prefix to check for
	 * (e.g. "_target" for parameters like "_target1" or "_target2")
	 * @param currentPage the current page, to be returned as fallback
	 * if no target page specified
	 * @return the page specified in the request, or current page if not found
	 */
	public static int getTargetPage(ServletRequest request, String paramPrefix, int currentPage) {
		Enumeration paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			if (paramName.startsWith(paramPrefix)) {
				for (int i = 0; i < WebUtils.SUBMIT_IMAGE_SUFFIXES.length; i++) {
					String suffix = WebUtils.SUBMIT_IMAGE_SUFFIXES[i];
					if (paramName.endsWith(suffix)) {
						paramName = paramName.substring(0, paramName.length() - suffix.length());
					}
				}
				return Integer.parseInt(paramName.substring(paramPrefix.length()));
			}
		}
		return currentPage;
	}


//	/**
//	 * Extract the URL filename from the given request URL path.
//	 * Correctly resolves nested paths such as "/products/view.html" as well.
//	 * @param urlPath the request URL path (e.g. "/index.html")
//	 * @return the extracted URI filename (e.g. "index")
//	 */
//	public static String extractFilenameFromUrlPath(String urlPath) {
//		int begin = urlPath.lastIndexOf('/') + 1;
//		int end = urlPath.indexOf(';');
//		if (end == -1) {
//			end = urlPath.indexOf('?');
//			if (end == -1) {
//				end = urlPath.length();
//			}
//		}
//		String filename = urlPath.substring(begin, end);
//		int dotIndex = filename.lastIndexOf('.');
//		if (dotIndex != -1) {
//			filename = filename.substring(0, dotIndex);
//		}
//		return filename;
//	}
	
	
	/**
	 * Clear the Servlet spec's error attributes as {@link javax.servlet.http.HttpServletRequest}
	 * attributes under the keys defined in the Servlet 2.3 specification:
	 * <code>javax.servlet.error.status_code</code>,
	 * <code>javax.servlet.error.exception_type</code>,
	 * <code>javax.servlet.error.message</code>,
	 * <code>javax.servlet.error.exception</code>,
	 * <code>javax.servlet.error.request_uri</code>,
	 * <code>javax.servlet.error.servlet_name</code>.
	 * @param request current servlet request
	 */
	public static void clearErrorRequestAttributes(HttpServletRequest request) {
		request.removeAttribute(ERROR_STATUS_CODE_ATTRIBUTE);
		request.removeAttribute(ERROR_EXCEPTION_TYPE_ATTRIBUTE);
		request.removeAttribute(ERROR_MESSAGE_ATTRIBUTE);
		request.removeAttribute(ERROR_EXCEPTION_ATTRIBUTE);
		request.removeAttribute(ERROR_REQUEST_URI_ATTRIBUTE);
		request.removeAttribute(ERROR_SERVLET_NAME_ATTRIBUTE);
	}
	
	public static Object initBean(Object bean, BeanInf providerManagerInfo,WebApplicationContext context) throws BeanInstanceException
	{
		if(bean instanceof WebApplicationObjectSupport)
		{
			((WebApplicationObjectSupport)bean).setApplicationContext(context);
			
		}
		else if(bean instanceof ApplicationObjectSupport)
		{
			((ApplicationObjectSupport)bean).setApplicationContext(context);
			
		}
		else if (bean instanceof ApplicationContextAware) {
			((ApplicationContextAware) bean).setApplicationContext(context);
		}
		if(bean instanceof ServletContextAware)
		{
			((ServletContextAware)bean).setServletContext(context.getServletContext());
		}
		if(bean instanceof BeanClassLoaderAware)
		{
			((BeanClassLoaderAware)bean).setBeanClassLoader(context.getClassLoader());
		}
		if(bean instanceof BeanNameAware)
		{
			if(providerManagerInfo != null)
				((BeanNameAware) bean).setBeanName(providerManagerInfo.getName());
		}
		if(bean instanceof BeanInfoAware)
		{
			if(providerManagerInfo != null && providerManagerInfo instanceof Pro)
				((BeanInfoAware) bean).setBeaninfo((Pro)providerManagerInfo);
		}
		if (bean instanceof MessageSourceAware) {
			((MessageSourceAware) bean).setMessageSource(context);
		}
		if (bean instanceof ResourceLoaderAware) {
			((ResourceLoaderAware) bean).setResourceLoader(context);
		}
		BeanAccembleHelper.afterPropertiesSet(bean,  providerManagerInfo,context);
		BeanAccembleHelper.registDestroy(bean,  providerManagerInfo,context) ;
		return bean;
	}
	
	public static Object initBean(Object bean, String beanName,WebApplicationContext context) throws BeanInstanceException
	{
		if(bean instanceof WebApplicationObjectSupport)
		{
			((WebApplicationObjectSupport)bean).setApplicationContext(context);
			
		}
		else if(bean instanceof ApplicationObjectSupport)
		{
			((ApplicationObjectSupport)bean).setApplicationContext(context);
			
		}
		else if (bean instanceof ApplicationContextAware) {
			((ApplicationContextAware) bean).setApplicationContext(context);
		}
		if(bean instanceof ServletContextAware)
		{
			((ServletContextAware)bean).setServletContext(context.getServletContext());
		}
		if(bean instanceof BeanNameAware)
		{
			((BeanNameAware) bean).setBeanName(beanName);
		}
		if (bean instanceof MessageSourceAware) {
			((MessageSourceAware) bean).setMessageSource(context);
		}
		if (bean instanceof ResourceLoaderAware) {
			((ResourceLoaderAware) bean).setResourceLoader(context);
		}
		
		
		
		
		
		
		
		BeanAccembleHelper.afterPropertiesSet(bean,  null,context);
		BeanAccembleHelper.registDestroy(bean,  null,context) ;
		return bean;
	}
	
	
	/**
	 * Extract the URL filename from the given request URL path.
	 * Correctly resolves nested paths such as "/products/view.html" as well.
	 * @param urlPath the request URL path (e.g. "/index.html")
	 * @return the extracted URI filename (e.g. "index")
	 */
	public static String extractFilenameFromUrlPath(String urlPath) {
		String filename = extractFullFilenameFromUrlPath(urlPath);
		int dotIndex = filename.lastIndexOf('.');
		if (dotIndex != -1) {
			filename = filename.substring(0, dotIndex);
		}
		return filename;
	}
	/**
	 * Extract the full URL filename (including file extension) from the given request URL path.
	 * Correctly resolves nested paths such as "/products/view.html" as well.
	 * @param urlPath the request URL path (e.g. "/products/index.html")
	 * @return the extracted URI filename (e.g. "index.html")
	 */
	public static String extractFullFilenameFromUrlPath(String urlPath) {
		int end = urlPath.indexOf(';');
		if (end == -1) {
			end = urlPath.indexOf('?');
			if (end == -1) {
				end = urlPath.length();
			}
		}
		int begin = urlPath.lastIndexOf('/', end) + 1;
		return urlPath.substring(begin, end);
	}
	/**
	 * Check if the request is a same-origin one, based on {@code Origin}, {@code Host},
	 * {@code Forwarded} and {@code X-Forwarded-Host} headers.
	 * @return {@code true} if the request is a same-origin one, {@code false} in case
	 * of cross-origin request.
	 * @since 4.2
	 */
	public static boolean isSameOrigin(HttpRequest request) {
		String origin = request.getHeaders().getOrigin();
		if (origin == null) {
			return true;
		}
		UriComponents actualUrl = UriComponentsBuilder.fromHttpRequest(request).build();
		UriComponents originUrl = UriComponentsBuilder.fromOriginHeader(origin).build();
		return (actualUrl.getHost().equals(originUrl.getHost()) && getPort(actualUrl) == getPort(originUrl));
	}
	private static int getPort(UriComponents component) {
		int port = component.getPort();
		if (port == -1) {
			if ("http".equals(component.getScheme()) || "ws".equals(component.getScheme())) {
				port = 80;
			}
			else if ("https".equals(component.getScheme()) || "wss".equals(component.getScheme())) {
				port = 443;
			}
		}
		return port;
	}
	/**
	 * Check the given request origin against a list of allowed origins.
	 * A list containing "*" means that all origins are allowed.
	 * An empty list means only same origin is allowed.
	 * @return {@code true} if the request origin is valid, {@code false} otherwise
	 * @since 4.1.5
	 * @see <a href="https://tools.ietf.org/html/rfc6454">RFC 6454: The Web Origin Concept</a>
	 */
	public static boolean isValidOrigin(HttpRequest request, Collection<String> allowedOrigins) {
		Assert.notNull(request, "Request must not be null");
		Assert.notNull(allowedOrigins, "Allowed origins must not be null");

		String origin = request.getHeaders().getOrigin();
		if (origin == null || allowedOrigins.contains("*")) {
			return true;
		}
		else if (CollectionUtils.isEmpty(allowedOrigins)) {
			return isSameOrigin(request);
		}
		else {
			return allowedOrigins.contains(origin);
		}
	}
}
