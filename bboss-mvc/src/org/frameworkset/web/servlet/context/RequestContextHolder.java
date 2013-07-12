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
package org.frameworkset.web.servlet.context;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.frameworkset.spi.support.NamedInheritableThreadLocal;
import org.frameworkset.spi.support.NamedThreadLocal;
import org.frameworkset.util.ClassUtils;

/**
 * <p>Title: RequestContextHolder.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-1
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class RequestContextHolder {
	
	private static final boolean jsfPresent =
		ClassUtils.isPresent("javax.faces.context.FacesContext", RequestContextHolder.class.getClassLoader());

private static final ThreadLocal requestAttributesHolder = new NamedThreadLocal("Request attributes");

private static final ThreadLocal inheritableRequestAttributesHolder =
		new NamedInheritableThreadLocal("Request context");


/**
 * Reset the RequestAttributes for the current thread.
 */
public static void resetRequestAttributes() {
	requestAttributesHolder.set(null);
	inheritableRequestAttributesHolder.set(null);
}

/**
 * Bind the given RequestAttributes to the current thread,
 * <i>not</i> exposing it as inheritable for child threads.
 * @param attributes the RequestAttributes to expose
 * @see #setRequestAttributes(RequestAttributes, boolean)
 */
public static void setRequestAttributes(RequestAttributes attributes) {
	setRequestAttributes(attributes, false);
}

/**
 * Bind the given RequestAttributes to the current thread.
 * @param attributes the RequestAttributes to expose
 * @param inheritable whether to expose the RequestAttributes as inheritable
 * for child threads (using an {@link java.lang.InheritableThreadLocal})
 */
public static void setRequestAttributes(RequestAttributes attributes, boolean inheritable) {
	if (inheritable) {
		inheritableRequestAttributesHolder.set(attributes);
		requestAttributesHolder.set(null);
	}
	else {
		requestAttributesHolder.set(attributes);
		inheritableRequestAttributesHolder.set(null);
	}
}

/**
 * Return the RequestAttributes currently bound to the thread.
 * @return the RequestAttributes currently bound to the thread,
 * or <code>null</code> if none bound
 */
public static RequestAttributes getRequestAttributes() {
	RequestAttributes attributes = (RequestAttributes) requestAttributesHolder.get();
	if (attributes == null) {
		attributes = (RequestAttributes) inheritableRequestAttributesHolder.get();
	}
	return attributes;
}

/**
 * Return the RequestContainer currently bound to the thread.
 * @return the RequestContainer currently bound to the thread,
 * or <code>null</code> if none bound
 */
public static RequestContainer getRequestContainer() {
	RequestContainer attributes = (RequestContainer) requestAttributesHolder.get();
	if (attributes == null) {
		attributes = (RequestContainer) inheritableRequestAttributesHolder.get();
	}
	return attributes;
}


public static HttpServletResponse getResponse()
{
	RequestContainer requestContainer = getRequestContainer();
	return requestContainer != null?requestContainer.getResponse():null;
}
public static PageContext getPageContext()
{
	RequestContainer requestContainer = getRequestContainer();
	return requestContainer != null?requestContainer.getPageContext():null;
}
public static HttpServletRequest getRequest() 
{
	RequestContainer requestContainer = getRequestContainer();
	return requestContainer != null?requestContainer.getRequest():null;
}
public static HttpSession getSession(boolean create) 
{
	RequestContainer requestContainer = getRequestContainer();
	return requestContainer != null?requestContainer.getSession(create):null;
}

public static HttpSession getSession() 
{
	RequestContainer requestContainer = getRequestContainer();
	return requestContainer != null?requestContainer.getSession():null;
}

/**
 * Return the RequestAttributes currently bound to the thread.
 * <p>Exposes the previously bound RequestAttributes instance, if any.
 * Falls back to the current JSF FacesContext, if any.
 * @return the RequestAttributes currently bound to the thread
 * @throws IllegalStateException if no RequestAttributes object
 * is bound to the current thread
 * @see #setRequestAttributes
 * @see ServletRequestAttributes
 * @see FacesRequestAttributes
 * @see javax.faces.context.FacesContext#getCurrentInstance()
 */
public static RequestAttributes currentRequestAttributes() throws IllegalStateException {
	RequestAttributes attributes = getRequestAttributes();
	if (attributes == null) {
		if (jsfPresent) {
			attributes = FacesRequestAttributesFactory.getFacesRequestAttributes();
		}
		if (attributes == null) {
			throw new IllegalStateException("No thread-bound request found: " +
					"Are you referring to request attributes outside of an actual web request, " +
					"or processing a request outside of the originally receiving thread? " +
					"If you are actually operating within a web request and still receive this message, " +
					"your code is probably running outside of DispatcherServlet/DispatcherPortlet: " +
					"In this case, use RequestContextListener or RequestContextFilter to expose the current request.");
		}
	}
	return attributes;
}


/**
 * Inner class to avoid hard-coded JSF dependency.
	 */
private static class FacesRequestAttributesFactory {

	public static RequestAttributes getFacesRequestAttributes() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		return (facesContext != null ? new FacesRequestAttributes(facesContext) : null);
	}
}

}
