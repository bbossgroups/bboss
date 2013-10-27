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
package org.frameworkset.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * Title: CharacterEncodingFilter.java
 * </p>
 * <p>
 * Description:
 * 
 *     <filter>  
        <filter-name>encodingFilter</filter-name>  
        <filter-class>  
            org.frameworkset.web.CharacterEncodingFilter</filter-class>  
        <init-param>  
            <param-name>encoding</param-name>  
            <param-value>UTF-8</param-value>  
        </init-param>  
        <init-param>  
            <param-name>forceEncoding</param-name>  
            <param-value>true</param-value>  
        </init-param>  
    </filter>  
      <filter-mapping>  
        <filter-name>encodingFilter</filter-name>  
        <url-pattern>/*</url-pattern>  
    </filter-mapping>  
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2008
 * </p>
 * 
 * @Date 2011-4-26
 * @author biaoping.yin
 * @version 1.0
 */
public class CharacterEncodingFilter implements Filter {
	/* The FilterConfig of this filter */
	private FilterConfig filterConfig;

	private String encoding;

	private boolean forceEncoding = false;

	/**
	 * Set the encoding to use for requests. This encoding will be passed into a
	 * {@link javax.servlet.http.HttpServletRequest#setCharacterEncoding} call.
	 * <p>
	 * Whether this encoding will override existing request encodings (and
	 * whether it will be applied as default response encoding as well) depends
	 * on the {@link #setForceEncoding "forceEncoding"} flag.
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * Set whether the configured {@link #setEncoding encoding} of this filter
	 * is supposed to override existing request and response encodings.
	 * <p>
	 * Default is "false", i.e. do not modify the encoding if
	 * {@link javax.servlet.http.HttpServletRequest#getCharacterEncoding()}
	 * returns a non-null value. Switch this to "true" to enforce the specified
	 * encoding in any case, applying it as default response encoding as well.
	 * <p>
	 * Note that the response encoding will only be set on Servlet 2.4+
	 * containers, since Servlet 2.3 did not provide a facility for setting a
	 * default response encoding.
	 */
	public void setForceEncoding(boolean forceEncoding) {
		this.forceEncoding = forceEncoding;
	}

	/**
	 * Suffix that gets appended to the filter name for the "already filtered"
	 * request attribute.
	 * 
	 * @see #getAlreadyFilteredAttributeName
	 */
	public static final String ALREADY_FILTERED_SUFFIX = ".FILTERED";

	/**
	 * This <code>doFilter</code> implementation stores a request attribute for
	 * "already filtered", proceeding without filtering again if the attribute
	 * is already there.
	 * 
	 * @see #getAlreadyFilteredAttributeName
	 * @see #shouldNotFilter
	 * @see #doFilterInternal
	 */
	public final void doFilter(ServletRequest request,
			ServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (!(request instanceof HttpServletRequest)
				|| !(response instanceof HttpServletResponse)) {
			throw new ServletException(
					"OncePerRequestFilter just supports HTTP requests");
		}
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String alreadyFilteredAttributeName = getAlreadyFilteredAttributeName();
		if (request.getAttribute(alreadyFilteredAttributeName) != null
				|| shouldNotFilter(httpRequest)) {
			// Proceed without invoking this filter...
			filterChain.doFilter(request, response);
		} else {
			// Do invoke this filter...
			request.setAttribute(alreadyFilteredAttributeName, Boolean.TRUE);
			try {
				doFilterInternal(httpRequest, httpResponse, filterChain);
			} finally {
				// Remove the "already filtered" request attribute for this
				// request.
				request.removeAttribute(alreadyFilteredAttributeName);
			}
		}
	}

	/**
	 * Return the name of the request attribute that identifies that a request
	 * is already filtered.
	 * <p>
	 * Default implementation takes the configured name of the concrete filter
	 * instance and appends ".FILTERED". If the filter is not fully initialized,
	 * it falls back to its class name.
	 * 
	 * @see #getFilterName
	 * @see #ALREADY_FILTERED_SUFFIX
	 */
	protected String getAlreadyFilteredAttributeName() {
		String name = getFilterName();
		if (name == null) {
			name = getClass().getName();
		}
		return name + ALREADY_FILTERED_SUFFIX;
	}

	/**
	 * Can be overridden in subclasses for custom filtering control, returning
	 * <code>true</code> to avoid filtering of the given request.
	 * <p>
	 * The default implementation always returns <code>false</code>.
	 * 
	 * @param request
	 *            current HTTP request
	 * @return whether the given request should <i>not</i> be filtered
	 * @throws ServletException
	 *             in case of errors
	 */
	protected boolean shouldNotFilter(HttpServletRequest request)
			throws ServletException {
		return false;
	}

	/**
	 * Same contract as for <code>doFilter</code>, but guaranteed to be just
	 * invoked once per request. Provides HttpServletRequest and
	 * HttpServletResponse arguments instead of the default ServletRequest and
	 * ServletResponse ones.
	 */
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String old = request.getCharacterEncoding();
		if (this.encoding != null
				&& (this.forceEncoding || old == null)) {
			request.setCharacterEncoding(this.encoding);
			if (this.forceEncoding) {
				response.setCharacterEncoding(this.encoding);
			}
		}
		filterChain.doFilter(request, response);
	}

	public void destroy() {
		// TODO Auto-generated method stub

	}
	public static String defaultEncoding =  "UTF-8"; 
	public void init(FilterConfig arg0) throws ServletException {
		this.filterConfig = arg0;
		this.encoding = filterConfig.getInitParameter("encoding");
		if(this.encoding == null) this.encoding = defaultEncoding;
		this.forceEncoding = Boolean.parseBoolean(filterConfig.getInitParameter("forceEncoding"));

	}

	/**
	 * Make the name of this filter available to subclasses. Analogous to
	 * GenericServlet's <code>getServletName()</code>.
	 * <p>
	 * Takes the FilterConfig's filter name by default. If initialized as bean
	 * in a Spring application context, it falls back to the bean name as
	 * defined in the bean factory.
	 * 
	 * @return the filter name, or <code>null</code> if none available
	 * @see javax.servlet.GenericServlet#getServletName()
	 * @see javax.servlet.FilterConfig#getFilterName()
	 * @see #setBeanName
	 */
	protected final String getFilterName() {
		return (this.filterConfig != null ? this.filterConfig.getFilterName()
				: null);
	}

	/**
	 * Make the ServletContext of this filter available to subclasses. Analogous
	 * to GenericServlet's <code>getServletContext()</code>.
	 * <p>
	 * Takes the FilterConfig's ServletContext by default. If initialized as
	 * bean in a Spring application context, it falls back to the ServletContext
	 * that the bean factory runs in.
	 * 
	 * @return the ServletContext instance, or <code>null</code> if none
	 *         available
	 * @see javax.servlet.GenericServlet#getServletContext()
	 * @see javax.servlet.FilterConfig#getServletContext()
	 * @see #setServletContext
	 */
	protected final ServletContext getServletContext() {
		return (this.filterConfig != null ? this.filterConfig
				.getServletContext() : null);
	}

}
