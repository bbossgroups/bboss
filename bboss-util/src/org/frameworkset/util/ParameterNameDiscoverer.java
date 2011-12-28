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
package org.frameworkset.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * <p>Title: ParameterNameDiscoverer.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-13
 * @author biaoping.yin
 * @version 1.0
 */
public interface ParameterNameDiscoverer {
	/**
	 * Return parameter names for this method,
	 * or <code>null</code> if they cannot be determined.
	 * @param method method to find parameter names for
	 * @return an array of parameter names if the names can be resolved,
	 * or <code>null</code> if they cannot
	 */
	String[] getParameterNames(Method method);
	
	/**
	 * Return parameter names for this constructor,
	 * or <code>null</code> if they cannot be determined.
	 * @param ctor constructor to find parameter names for
	 * @return an array of parameter names if the names can be resolved,
	 * or <code>null</code> if they cannot
	 */
	String[] getParameterNames(Constructor ctor);

}
