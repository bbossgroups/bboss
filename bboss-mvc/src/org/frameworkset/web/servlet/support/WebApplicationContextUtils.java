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

import javax.servlet.ServletContext;
import javax.servlet.jsp.PageContext;

import org.frameworkset.util.Assert;
import org.frameworkset.web.servlet.DispatchServlet;
import org.frameworkset.web.servlet.context.WebApplicationContext;

/**
 * <p>
 * Title: WebApplicationContextUtils.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2008
 * </p>
 * 
 * @Date 2010-9-24
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class WebApplicationContextUtils {
	/**
	 * Find the root WebApplicationContext for this web application, which is
	 * typically loaded via

	 * <p>
	 * Will rethrow an exception that happened on root context startup, to
	 * differentiate between a failed context startup and no context at all.
	 * 
	 * @param sc
	 *            ServletContext to find the web application context for
	 * @return the root WebApplicationContext for this web app
	 * @throws IllegalStateException
	 *             if the root WebApplicationContext could not be found
	 * @see WebApplicationContext#ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE
	 */
	public static WebApplicationContext getRequiredWebApplicationContext(
			ServletContext sc) throws IllegalStateException {

		WebApplicationContext wac = getWebApplicationContext(sc);
		if (wac == null) {
			throw new IllegalStateException(
					"No WebApplicationContext found: no ContextLoaderListener registered?");
		}
		return wac;
	}

	public static WebApplicationContext getRequiredWebApplicationContext(
			PageContext pageContext) throws IllegalStateException {

		WebApplicationContext wac = getWebApplicationContext(pageContext
				.getServletContext());
		if (wac == null) {
			throw new IllegalStateException(
					"No WebApplicationContext found: no ContextLoaderListener registered?");
		}
		return wac;
	}

	/**
	 * Find the root WebApplicationContext for this web application, which is
	 * typically loaded via
	
	 * <p>
	 * Will rethrow an exception that happened on root context startup, to
	 * differentiate between a failed context startup and no context at all.
	 * 
	 * @param sc
	 *            ServletContext to find the web application context for
	 * @return the root WebApplicationContext for this web app, or
	 *         <code>null</code> if none
	 * @see WebApplicationContext#ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE
	 */
	public static WebApplicationContext getWebApplicationContext(
			ServletContext sc) {
		return getWebApplicationContext(sc,
				WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
	}

	/**
	 * Find a custom WebApplicationContext for this web application.
	 * 
	 * @param sc
	 *            ServletContext to find the web application context for
	 * @param attrName
	 *            the name of the ServletContext attribute to look for
	 * @return the desired WebApplicationContext for this web app, or
	 *         <code>null</code> if none
	 */
	public static WebApplicationContext getWebApplicationContext(
			ServletContext sc, String attrName) {
		Assert.notNull(sc, "ServletContext must not be null");
		Object attr = sc.getAttribute(attrName);
		if (attr == null) {
			return DispatchServlet.webApplicationContext;
		}
		if (attr instanceof RuntimeException) {
			throw (RuntimeException) attr;
		}
		if (attr instanceof Error) {
			throw (Error) attr;
		}
		if (attr instanceof Exception) {
			IllegalStateException ex = new IllegalStateException();
			ex.initCause((Exception) attr);
			throw ex;
		}
		if (!(attr instanceof WebApplicationContext)) {
			throw new IllegalStateException(
					"Context attribute is not of type WebApplicationContext: "
							+ attr);
		}
		return (WebApplicationContext) attr;
	}

	/**
	 * Find a custom WebApplicationContext for this web application.
	 * 
	 * @param sc
	 *            ServletContext to find the web application context for
	 * @param attrName
	 *            the name of the ServletContext attribute to look for
	 * @return the desired WebApplicationContext for this web app, or
	 *         <code>null</code> if none
	 */
	public static WebApplicationContext getWebApplicationContext() {

		return DispatchServlet.webApplicationContext;

	}

	// /**
	// * Register web-specific scopes with the given BeanFactory,
	// * as used by the WebApplicationContext.
	// * @param beanFactory the BeanFactory to configure
	// */
	// public static void
	// registerWebApplicationScopes(ConfigurableListableBeanFactory beanFactory)
	// {
	// beanFactory.registerScope(WebApplicationContext.SCOPE_REQUEST, new
	// RequestScope());
	// beanFactory.registerScope(WebApplicationContext.SCOPE_SESSION, new
	// SessionScope(false));
	// beanFactory.registerScope(WebApplicationContext.SCOPE_GLOBAL_SESSION, new
	// SessionScope(true));
	//
	// beanFactory.registerResolvableDependency(ServletRequest.class, new
	// ObjectFactory() {
	// public Object getObject() {
	// RequestAttributes requestAttr =
	// RequestContextHolder.currentRequestAttributes();
	// if (!(requestAttr instanceof ServletRequestAttributes)) {
	// throw new
	// IllegalStateException("Current request is not a servlet request");
	// }
	// return ((ServletRequestAttributes) requestAttr).getRequest();
	// }
	// });
	// beanFactory.registerResolvableDependency(HttpSession.class, new
	// ObjectFactory() {
	// public Object getObject() {
	// RequestAttributes requestAttr =
	// RequestContextHolder.currentRequestAttributes();
	// if (!(requestAttr instanceof ServletRequestAttributes)) {
	// throw new
	// IllegalStateException("Current request is not a servlet request");
	// }
	// return ((ServletRequestAttributes)
	// requestAttr).getRequest().getSession();
	// }
	// });
	// }

}
