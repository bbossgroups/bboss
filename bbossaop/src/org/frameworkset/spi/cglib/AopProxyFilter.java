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

package org.frameworkset.spi.cglib;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.sf.cglib.proxy.CallbackFilter;

/**
 * <p>Title: AopProxyFilter.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-7-19 下午03:46:17
 * @author biaoping.yin
 * @version 1.0
 */
public class AopProxyFilter implements CallbackFilter {

	public int accept(Method arg0) {
		int modifier = arg0.getModifiers();
		
		if(Modifier.isProtected(modifier))
			return 1;
		else
			return 0;
	}

}
