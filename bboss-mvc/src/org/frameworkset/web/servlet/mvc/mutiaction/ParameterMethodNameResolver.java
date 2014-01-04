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
package org.frameworkset.web.servlet.mvc.mutiaction;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.frameworkset.util.Assert;
import org.frameworkset.web.servlet.mvc.MethodNameResolver;
import org.frameworkset.web.servlet.mvc.NoSuchRequestHandlingMethodException;
import org.frameworkset.web.util.WebUtils;

import com.frameworkset.util.StringUtil;

/**
 * <p>Title: ParameterMethodNameResolver.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-17
 * @author biaoping.yin
 * @version 1.0
 */
public class ParameterMethodNameResolver  implements MethodNameResolver {

	/**
	 * Default name for the parameter whose value identifies the method to invoke:
	 * "action".
	 */
	public static final String DEFAULT_PARAM_NAME = "action";


	protected final static Logger logger = Logger.getLogger(ParameterMethodNameResolver.class);

	private String paramName = DEFAULT_PARAM_NAME;

	private String[] methodParamNames;

	private Properties logicalMappings;

	private String defaultMethodName;


	/**
	 * Set the name of the parameter whose <i>value</i> identifies the name of
	 * the method to invoke. Default is "action".
	 * <p>Alternatively, specify parameter names where the very existence of each
	 * parameter means that a method of the same name should be invoked, via
	 * the "methodParamNames" property.
	 * @see #setMethodParamNames
	 */
	public void setParamName(String paramName) {
		if (paramName != null) {
			Assert.hasText(paramName, "'paramName' must not be empty");
		}
		this.paramName = paramName;
	}

	/**
	 * Set a String array of parameter names, where the <i>very existence of a
	 * parameter</i> in the list (with value ignored) means that a method of the
	 * same name should be invoked. This target method name may then be optionally
	 * further mapped via the {@link #logicalMappings} property, in which case it
	 * can be considered a logical name only.
	 * @see #setParamName
	 */
	public void setMethodParamNames(String[] methodParamNames) {
		this.methodParamNames = methodParamNames;
	}

	/**
	 * Specifies a set of optional logical method name mappings. For both resolution
	 * strategies, the method name initially comes in from the view layer. If that needs
	 * to be treated as a 'logical' method name, and mapped to a 'real' method name, then
	 * a name/value pair for that purpose should be added to this Properties instance.
	 * Any method name not found in this mapping will be considered to already be the
	 * real method name.
	 * <p>Note that in the case of no match, where the {@link #defaultMethodName} property
	 * is used if available, that method name is considered to already be the real method
	 * name, and is not run through the logical mapping.
	 * @param logicalMappings a Properties object mapping logical method names to real
	 * method names
	 */
	public void setLogicalMappings(Properties logicalMappings) {
		this.logicalMappings = logicalMappings;
	}

	/**
	 * Set the name of the default handler method that should be
	 * used when no parameter was found in the request
	 */
	public void setDefaultMethodName(String defaultMethodName) {
		if (defaultMethodName != null) {
			Assert.hasText(defaultMethodName, "'defaultMethodName' must not be empty");
		}
		this.defaultMethodName = defaultMethodName;
	}


	public String getHandlerMethodName(HttpServletRequest request) throws NoSuchRequestHandlingMethodException {
		String methodName = null;

		// Check parameter names where the very existence of each parameter
		// means that a method of the same name should be invoked, if any.
		if (this.methodParamNames != null) {
			for (int i = 0; i < this.methodParamNames.length; ++i) {
				String candidate = this.methodParamNames[i];
				if (WebUtils.hasSubmitParameter(request, candidate)) {
					methodName = candidate;
					if (logger.isDebugEnabled()) {
						logger.debug("Determined handler method '" + methodName +
								"' based on existence of explicit request parameter of same name");
					}
					break;
				}
			}
		}

		// Check parameter whose value identifies the method to invoke, if any.
		if (methodName == null && this.paramName != null) {
			methodName = request.getParameter(this.paramName);
			if (methodName != null) {
				if (logger.isDebugEnabled()) {
					logger.debug("Determined handler method '" + methodName +
							"' based on value of request parameter '" + this.paramName + "'");
				}
			}
		}

		if (methodName != null && this.logicalMappings != null) {
			// Resolve logical name into real method name, if appropriate.
			String originalName = methodName;
			methodName = this.logicalMappings.getProperty(methodName, methodName);
			if (logger.isDebugEnabled()) {
				logger.debug("Resolved method name '" + originalName + "' to handler method '" + methodName + "'");
			}
		}

		if (methodName != null && !StringUtil.hasText(methodName)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Method name '" + methodName + "' is empty: treating it as no method name found");
			}
			methodName = null;
		}

		if (methodName == null) {
			if (this.defaultMethodName != null) {
				// No specific method resolved: use default method.
				methodName = this.defaultMethodName;
				if (logger.isDebugEnabled()) {
					logger.debug("Falling back to default handler method '" + this.defaultMethodName + "'");
				}
			}
			else {
				// If resolution failed completely, throw an exception.
				throw new NoSuchRequestHandlingMethodException(request);
			}
		}

		return methodName;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
