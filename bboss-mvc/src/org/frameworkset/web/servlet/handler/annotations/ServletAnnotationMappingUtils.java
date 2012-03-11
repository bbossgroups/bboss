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

import javax.servlet.http.HttpServletRequest;

import org.frameworkset.util.ObjectUtils;
import org.frameworkset.util.annotations.HttpMethod;
import org.frameworkset.web.util.WebUtils;

/**
 * <p>Title: ServletAnnotationMappingUtils.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-24
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class ServletAnnotationMappingUtils {
	
	/**
	 * Check whether the given request matches the specified request methods.
	 * @param methods the HTTP request methods to check against
	 * @param request the current HTTP request to check
	 */
	public static boolean checkRequestMethod(HttpMethod[] methods, HttpServletRequest request) {
		if (!ObjectUtils.isEmpty(methods)) {
			boolean match = false;
			for (HttpMethod method : methods) {
				if (method.name().equals(request.getMethod())) {
					match = true;
				}
			}
			if (!match) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Check whether the given request matches the specified parameter conditions.
	 * @param params the parameter conditions, following
	 * {@link org.frameworkset.web.servlet.handler.annotations.HandlerMapping#params()}
	 * @param request the current HTTP request to check
	 */
	public static boolean checkParameters(String[] params, HttpServletRequest request) {
		if (!ObjectUtils.isEmpty(params)) {
			for (String param : params) {
				int separator = param.indexOf('=');
				if (separator == -1) {
					if (param.startsWith("!")) {
						if (WebUtils.hasSubmitParameter(request, param.substring(1))) {
							return false;
						}
					}
					else if (!WebUtils.hasSubmitParameter(request, param)) {
						return false;
					}
				}
				else {
					String key = param.substring(0, separator);
					String value = param.substring(separator + 1);
					if (!value.equals(request.getParameter(key))) {
						return false;
					}
				}
			}
		}
		return true;
	}

}
