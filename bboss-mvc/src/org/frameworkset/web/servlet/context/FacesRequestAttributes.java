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

import java.lang.reflect.Method;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.frameworkset.util.Assert;
import org.frameworkset.util.ClassUtils;
import org.frameworkset.util.ReflectionUtils;
import org.frameworkset.web.util.WebUtils;

import com.frameworkset.util.StringUtil;

/**
 * <p>Title: FacesRequestAttributes.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-1
 * @author biaoping.yin
 * @version 1.0
 */
public class FacesRequestAttributes implements RequestAttributes {

	private static final boolean portletApiPresent =
			ClassUtils.isPresent("javax.portlet.PortletSession", FacesRequestAttributes.class.getClassLoader());

	/**
	 * We'll create a lot of these objects, so we don't want a new logger every time.
	 */
	private static final Log logger = LogFactory.getLog(FacesRequestAttributes.class);

	private final FacesContext facesContext;


	/**
	 * Create a new FacesRequestAttributes adapter for the given FacesContext.
	 * @param facesContext the current FacesContext
	 * @see javax.faces.context.FacesContext#getCurrentInstance()
	 */
	public FacesRequestAttributes(FacesContext facesContext) {
		Assert.notNull(facesContext, "FacesContext must not be null");
		this.facesContext = facesContext;
	}


	/**
	 * Return the JSF FacesContext that this adapter operates on.
	 */
	protected final FacesContext getFacesContext() {
		return this.facesContext;
	}

	/**
	 * Return the JSF ExternalContext that this adapter operates on.
	 * @see javax.faces.context.FacesContext#getExternalContext()
	 */
	protected final ExternalContext getExternalContext() {
		return getFacesContext().getExternalContext();
	}

	/**
	 * Return the JSF attribute Map for the specified scope
	 * @param scope constant indicating request or session scope
	 * @return the Map representation of the attributes in the specified scope
	 * @see #SCOPE_REQUEST
	 * @see #SCOPE_SESSION
	 */
	protected Map<String, Object> getAttributeMap(int scope) {
		if (scope == SCOPE_REQUEST) {
			return getExternalContext().getRequestMap();
		}
		else {
			return getExternalContext().getSessionMap();
		}
	}


	@Override
	public Object getAttribute(String name, int scope) {
		if (scope == SCOPE_GLOBAL_SESSION && portletApiPresent) {
			return PortletSessionAccessor.getAttribute(name, getExternalContext());
		}
		else {
			return getAttributeMap(scope).get(name);
		}
	}

	@Override
	public void setAttribute(String name, Object value, int scope) {
		if (scope == SCOPE_GLOBAL_SESSION && portletApiPresent) {
			PortletSessionAccessor.setAttribute(name, value, getExternalContext());
		}
		else {
			getAttributeMap(scope).put(name, value);
		}
	}

	@Override
	public void removeAttribute(String name, int scope) {
		if (scope == SCOPE_GLOBAL_SESSION && portletApiPresent) {
			PortletSessionAccessor.removeAttribute(name, getExternalContext());
		}
		else {
			getAttributeMap(scope).remove(name);
		}
	}

	@Override
	public String[] getAttributeNames(int scope) {
		if (scope == SCOPE_GLOBAL_SESSION && portletApiPresent) {
			return PortletSessionAccessor.getAttributeNames(getExternalContext());
		}
		else {
			return StringUtil.toStringArray(getAttributeMap(scope).keySet());
		}
	}

	@Override
	public void registerDestructionCallback(String name, Runnable callback, int scope) {
		if (logger.isWarnEnabled()) {
			logger.warn("Could not register destruction callback [" + callback + "] for attribute '" + name +
					"' because FacesRequestAttributes does not support such callbacks");
		}
	}

	@Override
	public Object resolveReference(String key) {
		if (REFERENCE_REQUEST.equals(key)) {
			return getExternalContext().getRequest();
		}
		else if (REFERENCE_SESSION.equals(key)) {
			return getExternalContext().getSession(true);
		}
		else if ("application".equals(key)) {
			return getExternalContext().getContext();
		}
		else if ("requestScope".equals(key)) {
			return getExternalContext().getRequestMap();
		}
		else if ("sessionScope".equals(key)) {
			return getExternalContext().getSessionMap();
		}
		else if ("applicationScope".equals(key)) {
			return getExternalContext().getApplicationMap();
		}
		else if ("facesContext".equals(key)) {
			return getFacesContext();
		}
		else if ("cookie".equals(key)) {
			return getExternalContext().getRequestCookieMap();
		}
		else if ("header".equals(key)) {
			return getExternalContext().getRequestHeaderMap();
		}
		else if ("headerValues".equals(key)) {
			return getExternalContext().getRequestHeaderValuesMap();
		}
		else if ("param".equals(key)) {
			return getExternalContext().getRequestParameterMap();
		}
		else if ("paramValues".equals(key)) {
			return getExternalContext().getRequestParameterValuesMap();
		}
		else if ("initParam".equals(key)) {
			return getExternalContext().getInitParameterMap();
		}
		else if ("view".equals(key)) {
			return getFacesContext().getViewRoot();
		}
		else if ("viewScope".equals(key)) {
			return getFacesContext().getViewRoot().getViewMap();
		}
		else if ("flash".equals(key)) {
			return getExternalContext().getFlash();
		}
		else if ("resource".equals(key)) {
			return getFacesContext().getApplication().getResourceHandler();
		}
		else {
			return null;
		}
	}

	@Override
	public String getSessionId() {
		Object session = getExternalContext().getSession(true);
		try {
			// Both HttpSession and PortletSession have a getId() method.
			Method getIdMethod = session.getClass().getMethod("getId", new Class<?>[0]);
			return ReflectionUtils.invokeMethod(getIdMethod, session).toString();
		}
		catch (NoSuchMethodException ex) {
			throw new IllegalStateException("Session object [" + session + "] does not have a getId() method");
		}
	}

	@Override
	public Object getSessionMutex() {
		// Enforce presence of a session first to allow listeners
		// to create the mutex attribute, if any.
		Object session = getExternalContext().getSession(true);
		Object mutex = getExternalContext().getSessionMap().get(WebUtils.SESSION_MUTEX_ATTRIBUTE);
		if (mutex == null) {
			mutex = session;
		}
		return mutex;
	}


	/**
	 * Inner class to avoid hard-coded Portlet API dependency.
 	 */
	private static class PortletSessionAccessor {

		public static Object getAttribute(String name, ExternalContext externalContext) {
			Object session = externalContext.getSession(false);
//			if (session instanceof PortletSession) {
//				return ((PortletSession) session).getAttribute(name, PortletSession.APPLICATION_SCOPE);
//			}
//			else 
			if (session != null) {
				return externalContext.getSessionMap().get(name);
			}
			else {
				return null;
			}
		}

		public static void setAttribute(String name, Object value, ExternalContext externalContext) {
			Object session = externalContext.getSession(true);
//			if (session instanceof PortletSession) {
//				((PortletSession) session).setAttribute(name, value, PortletSession.APPLICATION_SCOPE);
//			}
//			else 
			{
				externalContext.getSessionMap().put(name, value);
			}
		}

		public static void removeAttribute(String name, ExternalContext externalContext) {
			Object session = externalContext.getSession(false);
//			if (session instanceof PortletSession) {
//				((PortletSession) session).removeAttribute(name, PortletSession.APPLICATION_SCOPE);
//			}
//			else 
			if (session != null) {
				externalContext.getSessionMap().remove(name);
			}
		}

		public static String[] getAttributeNames(ExternalContext externalContext) {
			Object session = externalContext.getSession(false);
//			if (session instanceof PortletSession) {
//				return StringUtil.toStringArray(
//						((PortletSession) session).getAttributeNames(PortletSession.APPLICATION_SCOPE));
//			}
//			else 
			if (session != null) {
				return StringUtil.toStringArray(externalContext.getSessionMap().keySet());
			}
			else {
				return new String[0];
			}
		}
	}

}
