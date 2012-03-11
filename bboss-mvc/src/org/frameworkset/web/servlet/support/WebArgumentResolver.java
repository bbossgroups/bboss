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

import org.frameworkset.util.MethodParameter;
import org.frameworkset.web.servlet.mvc.NativeWebRequest;

/**
 * <p>Title: WebArgumentResolver.java</p> 
 * <p>Description:  SPI for resolving custom arguments for a specific handler method parameter.
 * Typically implemented to detect sppecial parameter types, resolving
 * well-known argument values for them.
 *
 * <p>A typical implementation could look like as follows:
 *
 * <pre class="code">
 * public class MySpecialArgumentResolver implements ArgumentResolver {
 *
 *   public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) {
 *     if (methodParameter.getParameterType().equals(MySpecialArg.class)) {
 *       return new MySpecialArg("myValue");
 *     }
 *     return UNRESOLVED;
 *   }
 * }</pre>
 *</p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-24
 * @author biaoping.yin
 * @version 1.0
 */
public interface WebArgumentResolver {
	
	/**
	 * Marker to be returned when the resolver does not know how to
	 * handle the given method parameter.
	 */
	Object UNRESOLVED = new Object();


	/**
	 * Resolve an argument for the given handler method parameter within the given web request.
	 * @param methodParameter the handler method parameter to resolve
	 * @param webRequest the current web request, allowing access to the native request as well
	 * @return the argument value, or <code>UNRESOLVED</code> if not resolvable
	 * @throws Exception in case of resolution failure
	 */
	Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception;


}
