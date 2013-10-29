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

package org.frameworkset.spi.interceptor;

import java.lang.reflect.Method;

import com.frameworkset.proxy.Interceptor;

/**
 * <p>Title: DumyInterceptor.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2012-11-28 下午5:51:27
 * @author biaoping.yin
 * @version 1.0
 */
public class DumyInterceptor implements Interceptor {

	public DumyInterceptor() {
		// TODO Auto-generated constructor stub
	}

	public void before(Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub

	}

	public void after(Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub

	}

	public void afterThrowing(Method method, Object[] args, Throwable throwable)
			throws Throwable {
		// TODO Auto-generated method stub

	}

	public void afterFinally(Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub

	}

}
