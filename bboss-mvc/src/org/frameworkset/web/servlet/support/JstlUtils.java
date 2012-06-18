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

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;

import org.frameworkset.spi.support.MessageSource;
import org.frameworkset.spi.support.MessageSourceResourceBundle;
import org.frameworkset.spi.support.ResourceBundleMessageSource;

/**
 * <p>Title: JstlUtils.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-28
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class JstlUtils {
	/**
	 * Checks JSTL's "javax.servlet.jsp.jstl.fmt.localizationContext"
	 * context-param and creates a corresponding child message source,
	 * with the provided -defined MessageSource as parent.
	 * @param servletContext the ServletContext we're running in
	 * (to check JSTL-related context-params in <code>web.xml</code>)
	 * @param messageSource the MessageSource to expose, typically
	 * the ApplicationContext of the current DispatcherServlet
	 * @return the MessageSource to expose to JSTL; first checking the
	 * JSTL-defined bundle, then the -defined MessageSource
	 * @see org.frameworkset.spi.ApplicationContext
	 */
	public static MessageSource getJstlAwareMessageSource(
			ServletContext servletContext, MessageSource messageSource) {

		if (servletContext != null) {
			String jstlInitParam = servletContext.getInitParameter(Config.FMT_LOCALIZATION_CONTEXT);
			if (jstlInitParam != null) {
				// Create a ResourceBundleMessageSource for the specified resource bundle
				// basename in the JSTL context-param in web.xml, wiring it with the given
				// -defined MessageSource as parent.
				ResourceBundleMessageSource jstlBundleWrapper = new ResourceBundleMessageSource();
				jstlBundleWrapper.setBasename(jstlInitParam);
				jstlBundleWrapper.setParentMessageSource(messageSource);
				jstlBundleWrapper.setUseCodeAsDefaultMessage(true);
				return jstlBundleWrapper;
			}
		}
		return messageSource;
	}

	/**
	 * Exposes JSTL-specific request attributes specifying locale
	 * and resource bundle for JSTL's formatting and message tags,
	 * using 's locale and MessageSource.
	 * @param request the current HTTP request
	 * @param messageSource the MessageSource to expose,
	 * typically the current ApplicationContext (may be <code>null</code>)
	 * @see #exposeLocalizationContext(RequestContext)
	 */
	public static void exposeLocalizationContext(HttpServletRequest request, MessageSource messageSource) {
		Locale jstlLocale = RequestContextUtils.getRequestContextLocal(request);
		Config.set(request, Config.FMT_LOCALE, jstlLocale);
		if (messageSource != null) {
			LocalizationContext jstlContext = new BBossLocalizationContext(messageSource, request);
			Config.set(request, Config.FMT_LOCALIZATION_CONTEXT, jstlContext);
		}
	}

	/**
	 * Exposes JSTL-specific request attributes specifying locale
	 * and resource bundle for JSTL's formatting and message tags,
	 * using 's locale and MessageSource.
	 * @param requestContext the context for the current HTTP request,
	 * including the ApplicationContext to expose as MessageSource
	 */
	public static void exposeLocalizationContext(RequestContext requestContext) {
		Config.set(requestContext.getRequest(), Config.FMT_LOCALE, requestContext.getLocale());
		MessageSource messageSource = getJstlAwareMessageSource(
				requestContext.getServletContext(), requestContext.getMessageSource());
		LocalizationContext jstlContext = new BBossLocalizationContext(messageSource, requestContext.getRequest());
		Config.set(requestContext.getRequest(), Config.FMT_LOCALIZATION_CONTEXT, jstlContext);
	}


	/**
	 * -specific LocalizationContext adapter that merges session-scoped
	 * JSTL LocalizationContext/Locale attributes with the local Bboss request context.
	 */
	private static class BBossLocalizationContext extends LocalizationContext {

		private final MessageSource messageSource;

		private final HttpServletRequest request;

		public BBossLocalizationContext(MessageSource messageSource, HttpServletRequest request) {
			this.messageSource = messageSource;
			this.request = request;
		}

		public ResourceBundle getResourceBundle() {
			HttpSession session = this.request.getSession(false);
			if (session != null) {
				Object lcObject = Config.get(session, Config.FMT_LOCALIZATION_CONTEXT);
				if (lcObject instanceof LocalizationContext) {
					ResourceBundle lcBundle = ((LocalizationContext) lcObject).getResourceBundle();
					return new MessageSourceResourceBundle(this.messageSource, getLocale(), lcBundle);
				}
			}
			return new MessageSourceResourceBundle(this.messageSource, getLocale());
		}

		public Locale getLocale() {
			HttpSession session = this.request.getSession(false);
			if (session != null) {
				Object localeObject = Config.get(session, Config.FMT_LOCALE);
				if (localeObject instanceof Locale) {
					return (Locale) localeObject;
				}
			}
			return RequestContextUtils.getRequestContextLocal(this.request);
		}
	};

}
