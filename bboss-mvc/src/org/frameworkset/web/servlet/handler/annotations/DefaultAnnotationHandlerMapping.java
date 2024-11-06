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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.frameworkset.util.ClassUtil;
import org.frameworkset.util.annotations.AnnotationUtils;
import org.frameworkset.util.annotations.HandlerMapping;
import org.frameworkset.util.annotations.HttpMethod;
import org.frameworkset.web.HttpRequestMethodNotSupportedException;
import org.frameworkset.web.servlet.handler.AbstractDetectingUrlHandlerMapping;
import org.frameworkset.web.servlet.handler.HandlerUtils;

import com.frameworkset.util.StringUtil;

/**
 * <p>Title: DefaultAnnotationHandlerMapping.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-23
 * @author biaoping.yin
 * @version 1.0
 */
public class DefaultAnnotationHandlerMapping  extends AbstractDetectingUrlHandlerMapping {

	private boolean useDefaultSuffixPattern = true;

	private Map<Class, HandlerMapping> cachedMappings = new HashMap<Class, HandlerMapping>();

	
	/**
	 * Set whether to register paths using the default suffix pattern as well:
	 * i.e. whether "/users" should be registered as "/users.*" too.
	 * <p>Default is "true". Turn this convention off if you intend to interpret
	 * your <code>@HandlerMapping</code> paths strictly.
	 * <p>Note that paths which include a ".xxx" suffix already will not be
	 * transformed using the default suffix pattern in any case.
	 */
	public void setUseDefaultSuffixPattern(boolean useDefaultSuffixPattern) {
		this.useDefaultSuffixPattern = useDefaultSuffixPattern;
	}


	protected String[] determineUrlsForHandler(String beanName) {
		return HandlerUtils.determineUrlsForHandler(getApplicationContext(), beanName, cachedMappings);
	}
	
	


	/**
	 * Validate the given annotated handler against the current request.
	 * @see #validateMapping
	 */
	protected void validateHandler(Object handler, HttpServletRequest request) throws Exception {
		HandlerMapping mapping = this.cachedMappings.get(ClassUtil.getClassInfo(handler.getClass()).getClazz());
		if (mapping == null) {
			mapping = AnnotationUtils.findAnnotation(ClassUtil.getClassInfo(handler.getClass()).getClazz(), HandlerMapping.class);
		}
		if (mapping != null) {
			validateMapping(mapping, request);
		}
	}

	/**
	 * Validate the given type-level mapping metadata against the current request,
	 * checking HTTP request method and parameter conditions.
	 * @param mapping the mapping metadata to validate
	 * @param request current HTTP request
	 * @throws Exception if validation failed
	 */
	protected void validateMapping(HandlerMapping mapping, HttpServletRequest request) throws Exception {
		HttpMethod[] mappedMethods = mapping.method();
		if (!ServletAnnotationMappingUtils.checkRequestMethod(mappedMethods, request)) {
			String[] supportedMethods = new String[mappedMethods.length];
			for (int i = 0; i < mappedMethods.length; i++) {
				supportedMethods[i] = mappedMethods[i].name();
			}
			throw new HttpRequestMethodNotSupportedException(request.getMethod(), supportedMethods);
		}

		String[] mappedParams = mapping.params();
		if (!ServletAnnotationMappingUtils.checkParameters(mappedParams, request)) {
			throw new ServletException("Parameter conditions {" +
					StringUtil.arrayToDelimitedString(mappedParams, ", ") +
					"} not met for request parameters: " + request.getParameterMap());
		}
	}


	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
		if(cachedMappings != null)
		{
			cachedMappings.clear();
		}
		
	}

}
