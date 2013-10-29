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

package org.frameworkset.util.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Title: HandleMethod.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-10-18 下午02:10:51
 * @author biaoping.yin
 * @version 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HandlerMapping {
	/**
	 * The primary mapping expressed by this annotation.
	 * <p>In a Servlet environment: the path mapping URIs (e.g. "/myPath.do").
	 * Ant-style path patterns are also supported (e.g. "/myPath/*.do").
	 * At the method level, relative paths (e.g. "edit.do") are supported
	 * within the primary mapping expressed at the type level.
	 * <p>In a Portlet environment: the mapped portlet modes
	 * (i.e. "EDIT", "VIEW", "HELP" or any custom modes).
	 * <p><b>Supported at the type level as well as at the method level!</b>
	 * When used at the type level, all method-level mappings inherit
	 * this primary mapping, narrowing it for a specific handler method.
	 * <p>In case of Servlet-based handler methods, the method names are
	 * taken into account for narrowing if no path was specified explicitly,
	 * according to the specified
	 * {@link MethodNameResolver}
	 * (by default an
	 * {@link InternalPathMethodNameResolver}).
	 * Note that this only applies in case of ambiguous annotation mappings
	 * that do not specify a path mapping explicitly. In other words,
	 * the method name is only used for narrowing among a set of matching
	 * methods; it does not constitute a primary path mapping itself.
	 * <p>If you have a single default method (without explicit path mapping),
	 * then all requests without a more specific mapped method found will
	 * be dispatched to it. If you have multiple such default methods, then
	 * the method name will be taken into account for choosing between them.
	 */
	String[] value() default {};

	/**
	 * The HTTP request methods to map to, narrowing the primary mapping:
	 * GET, POST, HEAD, OPTIONS, PUT, DELETE, TRACE.
	 * <p><b>Supported at the type level as well as at the method level!</b>
	 * When used at the type level, all method-level mappings inherit
	 * this HTTP method restriction (i.e. the type-level restriction
	 * gets checked before the handler method is even resolved).
	 * <p><b>Currently only supported in Servlet environments!</b>
	 * To be supported for Portlet 2.0 resource requests in Bboss 3.0 as well.
	 */
	HttpMethod[] method() default {};

//	/**
//	 * The parameters of the mapped request, narrowing the primary mapping.
//	 * <p>Same format for any environment: a sequence of "myParam=myValue" style
//	 * expressions, with a request only mapped if each such parameter is found
//	 * to have the given value. "myParam" style expressions are also supported,
//	 * with such parameters having to be present in the request (allowed to have
//	 * any value). Finally, "!myParam" style expressions indicate that the
//	 * specified parameter is <i>not</i> supposed to be present in the request.
//	 * <p><b>Supported at the type level as well as at the method level!</b>
//	 * When used at the type level, all method-level mappings inherit
//	 * this parameter restriction (i.e. the type-level restriction
//	 * gets checked before the handler method is even resolved).
//	 * <p>In a Servlet environment, parameter mappings are considered as restrictions
//	 * that are enforced at the type level. The primary path mapping (i.e. the
//	 * specified URI value) still has to uniquely identify the target handler, with
//	 * parameter mappings simply expressing preconditions for invoking the handler.
//	 * <p>In a Portlet environment, parameters are taken into account as mapping
//	 * differentiators, i.e. the primary portlet mode mapping plus the parameter
//	 * conditions uniquely identify the target handler. Different handlers may be
//	 * mapped onto the same portlet mode, as long as their parameter mappings differ.
//	 */
	String[] params() default {};
	
	
}
