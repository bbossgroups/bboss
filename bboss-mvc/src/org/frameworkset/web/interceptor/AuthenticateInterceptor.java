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
package org.frameworkset.web.interceptor;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.frameworkset.util.ObjectUtils;
import org.frameworkset.web.servlet.HandlerInterceptor;
import org.frameworkset.web.servlet.ModelAndView;
import org.frameworkset.web.servlet.handler.HandlerMeta;
import org.frameworkset.web.servlet.mvc.RequestMap;
import org.frameworkset.web.util.UrlPathHelper;
import org.frameworkset.web.util.WebUtils;

import com.frameworkset.util.BeanUtils;


/**
 * <p>
 * FirstInterceptor.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2009
 * </p>
 * 
 * @Date 2011-5-31
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class AuthenticateInterceptor extends AuthenticateFilter implements HandlerInterceptor {
	private static Logger logger = Logger.getLogger(AuthenticateInterceptor.class);

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, HandlerMeta handlerMeta, Exception ex)
			throws Exception {

		// TODO Auto-generated method stub

	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, HandlerMeta handlerMeta,
			ModelAndView modelAndView) throws Exception {

		// TODO Auto-generated method stub

	}
	
	public boolean _preHandle(HttpServletRequest request,
			HttpServletResponse response, HandlerMeta handlerMeta)
			throws Exception {
		String requesturipath = UrlPathHelper.getPathWithinApplication(request);
		//做控制逻辑检测，如果检测失败，则执行下述逻辑，否则执行正常的控制器方法		
		if(needCheck(requesturipath) )
		{			
			boolean checkresult = check(request,
					response, handlerMeta);
			if(!checkresult)
			{
				request.setAttribute(accesscontrol_check_result, accesscontrol_check_result_fail);
				if(!response.isCommitted())
				{
					String dispatcherPath = prepareForRendering(request, response,requesturipath);
					StringBuffer targetUrl = new StringBuffer();
					if (!this.isforward() && !this.isinclude && this.contextRelative && dispatcherPath.startsWith("/")) {
						targetUrl.append(request.getContextPath());
					}
					targetUrl.append(dispatcherPath);
					if (this.exposeModelAttributes) {
						String enc = this.encodingScheme;
						if (enc == null) {
							enc = request.getCharacterEncoding();
						}
						if (enc == null) {
							enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
						}
						appendQueryProperties(targetUrl, request, new RequestMap(request),
								enc);
					}
					sendRedirect(request, response, targetUrl.toString(), http10Compatible,this.isforward(),this.isinclude);
				}
				return false;
			}
			else
			{
				request.setAttribute(accesscontrol_check_result, accesscontrol_check_result_ok);
				return true;
			}
		}
		else
		{
			request.setAttribute(accesscontrol_check_result, accesscontrol_check_result_ok);
			return true;
		}	
		
	}
	
	

	/**
	 * Append query properties to the redirect URL. Stringifies, URL-encodes and
	 * formats model attributes as query properties.
	 * 
	 * @param targetUrl
	 *            the StringBuffer to append the properties to
	 * @param model
	 *            Map that contains model attributes
	 * @param encodingScheme
	 *            the encoding scheme to use
	 * @throws UnsupportedEncodingException
	 *             if string encoding failed
	 * @see #queryProperties
	 */
	protected void appendQueryProperties(StringBuffer targetUrl,
			HttpServletRequest request, Map model, String encodingScheme)
			throws UnsupportedEncodingException {

		// Extract anchor fragment, if any.
		String fragment = null;
		int anchorIndex = targetUrl.indexOf("#");
		if (anchorIndex > -1) {
			fragment = targetUrl.substring(anchorIndex);
			targetUrl.delete(anchorIndex, targetUrl.length());
		}
		boolean first = (this.getRedirecturl().indexOf('?') < 0);
		if(!isforward() && !isinclude)
		{
			// If there aren't already some parameters, we need a "?".
			
			Iterator entries = queryProperties(model).entrySet().iterator();
			while (entries.hasNext()) {
				Map.Entry entry = (Map.Entry) entries.next();
				String key = entry.getKey().toString();
				Object rawValue = entry.getValue();
				Iterator valueIter = null;
				if (rawValue != null && rawValue.getClass().isArray()) {
					valueIter = Arrays.asList(ObjectUtils.toObjectArray(rawValue))
							.iterator();
				} else if (rawValue instanceof Collection) {
					valueIter = ((Collection) rawValue).iterator();
				} else {
					valueIter = Collections.singleton(rawValue).iterator();
				}
				while (valueIter.hasNext()) {
					Object value = valueIter.next();
					if (first) {
						targetUrl.append('?');
						first = false;
					} else {
						targetUrl.append('&');
					}
					String encodedKey = urlEncode(key, encodingScheme);
					String encodedValue = (value != null ? urlEncode(value
							.toString(), encodingScheme) : "");
					targetUrl.append(encodedKey).append('=').append(encodedValue);
				}
			}
		}
		exposeForwardRequestAttributes(first, targetUrl, request,
				encodingScheme);
		// javax.servlet.forward.request_uri=%2Fbboss-mvc%2Findex.htm&javax.servlet.forward.context_path=%2Fbboss-mvc&javax.servlet.forward.servlet_path=%2Findex.htm
		// Append anchor fragment, if any, to end of URL.
		if (fragment != null) {
			targetUrl.append(fragment);
		}
	}

	/**
	 * Determine name-value pairs for query strings, which will be stringified,
	 * URL-encoded and formatted by {@link #appendQueryProperties}.
	 * <p>
	 * This implementation filters the model through checking
	 * {@link #isEligibleProperty(String, Object)} for each element, by default
	 * accepting Strings, primitives and primitive wrappers only.
	 * 
	 * @param model
	 *            the original model Map
	 * @return the filtered Map of eligible query properties
	 * @see #isEligibleProperty(String, Object)
	 */
	protected Map queryProperties(Map model) {
		Map result = new LinkedHashMap();
		for (Iterator it = model.entrySet().iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = entry.getKey().toString();
			Object value = entry.getValue();
			if (isEligibleProperty(key, value)) {
				result.put(key, value);
			}
		}
		return result;
	}

	/**
	 * Determine whether the given model element should be exposed as a query
	 * property.
	 * <p>
	 * The default implementation considers Strings and primitives as eligible,
	 * and also arrays and Collections/Iterables with corresponding elements.
	 * This can be overridden in subclasses.
	 * 
	 * @param key
	 *            the key of the model element
	 * @param value
	 *            the value of the model element
	 * @return whether the element is eligible as query property
	 */
	protected boolean isEligibleProperty(String key, Object value) {
		if (value == null) {
			return false;
		}
		if (isEligibleValue(value)) {
			return true;
		}

		if (value.getClass().isArray()) {
			int length = Array.getLength(value);
			if (length == 0) {
				return false;
			}
			for (int i = 0; i < length; i++) {
				Object element = Array.get(value, i);
				if (!isEligibleValue(element)) {
					return false;
				}
			}
			return true;
		}

		if (value instanceof Collection) {
			Collection coll = (Collection) value;
			if (coll.isEmpty()) {
				return false;
			}
			for (Iterator it = coll.iterator(); it.hasNext();) {
				Object element = it.next();
				if (!isEligibleValue(element)) {
					return false;
				}
			}
			return true;
		}

		return false;
	}

	/**
	 * Determine whether the given model element value is eligible for exposure.
	 * <p>
	 * The default implementation considers primitives, Strings, Numbers, Dates,
	 * URIs, URLs and Locale objects as eligible. This can be overridden in
	 * subclasses.
	 * 
	 * @param value
	 *            the model element value
	 * @return whether the element value is eligible
	 * @see BeanUtils#isSimpleValueType
	 */
	protected boolean isEligibleValue(Object value) {
		return (value != null && BeanUtils.isSimpleValueType(value.getClass()));
	}

	/**
	 * URL-encode the given input String with the given encoding scheme.
	 * <p>
	 * The default implementation uses
	 * <code>URLEncoder.encode(input, enc)</code>.
	 * 
	 * @param input
	 *            the unencoded input String
	 * @param encodingScheme
	 *            the encoding scheme
	 * @return the encoded output String
	 * @throws UnsupportedEncodingException
	 *             if thrown by the JDK URLEncoder
	 * @see java.net.URLEncoder#encode(String, String)
	 * @see java.net.URLEncoder#encode(String)
	 */
	protected String urlEncode(String input, String encodingScheme)
			throws UnsupportedEncodingException {
		return (input != null ? URLEncoder.encode(input, encodingScheme) : null);
	}

	/**
	 * Expose the current request URI and paths as
	 * {@link javax.servlet.http.HttpServletRequest} attributes under the keys
	 * defined in the Servlet 2.4 specification, for containers that implement
	 * 2.3 or an earlier version of the Servlet API:
	 * <code>javax.servlet.forward.request_uri</code>,
	 * <code>javax.servlet.forward.context_path</code>,
	 * <code>javax.servlet.forward.servlet_path</code>,
	 * <code>javax.servlet.forward.path_info</code>,
	 * <code>javax.servlet.forward.query_string</code>.
	 * <p>
	 * Does not override values if already present, to not cause conflicts with
	 * the attributes exposed by Servlet 2.4+ containers themselves.
	 * 
	 * @param request
	 *            current servlet request
	 * @throws UnsupportedEncodingException
	 */
	public void exposeForwardRequestAttributes(boolean first,
			StringBuffer targetUrl, HttpServletRequest request,
			String encodingScheme) throws UnsupportedEncodingException {


		if (request.getRequestURI() != null) {
			if (first)
			{
				targetUrl.append("?");
				first = false;
			}
			else
				targetUrl.append("&");
			
			String encodedValue = urlEncode(request.getRequestURI(),
					encodingScheme);
			targetUrl.append(WebUtils.FORWARD_REQUEST_URI_ATTRIBUTE)
					.append("=").append(encodedValue);

		}
		if (request.getContextPath() != null) {
			if (first)
			{
				targetUrl.append("?");
				first = false;
			}
			else
			{
				targetUrl.append("&");
			}
			
			String encodedValue = urlEncode(request.getContextPath(),
					encodingScheme);
			targetUrl.append("&").append(
					WebUtils.FORWARD_CONTEXT_PATH_ATTRIBUTE).append("=")
					.append(encodedValue);
		}
		if (request.getServletPath() != null) {
			if (first){
				targetUrl.append("?");
				first = false;
			}
			else
			{
				targetUrl.append("&");
			}
			String encodedValue = urlEncode(request.getServletPath(),
					encodingScheme);
			targetUrl.append("&").append(
					WebUtils.FORWARD_SERVLET_PATH_ATTRIBUTE).append("=")
					.append(encodedValue);
		}
		if (request.getPathInfo() != null) {
			if (first)
			{
				targetUrl.append("?");
				first = false;
			}
			else
			{
				targetUrl.append("&");
			}
			String encodedValue = urlEncode(request.getPathInfo(),
					encodingScheme);
			targetUrl.append("&").append(WebUtils.FORWARD_PATH_INFO_ATTRIBUTE)
					.append("=").append(encodedValue);
		}
		if (request.getQueryString() != null) {
			if (first){
				targetUrl.append("?");
				first = false;
			}
			else
			{
				targetUrl.append("&");
			}
			String encodedValue = urlEncode(request.getQueryString(),
					encodingScheme);
			targetUrl.append("&").append(
					WebUtils.FORWARD_QUERY_STRING_ATTRIBUTE).append("=")
					.append(encodedValue);
		}

	}

	// /**
	// * Determine name-value pairs for query strings, which will be
	// stringified,
	// * URL-encoded and formatted by {@link #appendQueryProperties}.
	// * <p>This implementation filters the model through checking
	// * {@link #isEligibleProperty(String, Object)} for each element,
	// * by default accepting Strings, primitives and primitive wrappers only.
	// * @param model the original model Map
	// * @return the filtered Map of eligible query properties
	// * @see #isEligibleProperty(String, Object)
	// */
	// protected Map queryProperties(Map model) {
	// Map result = new LinkedHashMap();
	// for (Iterator it = model.entrySet().iterator(); it.hasNext();) {
	// Map.Entry entry = (Map.Entry) it.next();
	// String key = entry.getKey().toString();
	// Object value = entry.getValue();
	// if (isEligibleProperty(key, value)) {
	// result.put(key, value);
	// }
	// }
	// return result;
	// }


}
