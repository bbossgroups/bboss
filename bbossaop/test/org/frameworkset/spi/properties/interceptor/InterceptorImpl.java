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

package org.frameworkset.spi.properties.interceptor;

import java.lang.reflect.Method;

import com.frameworkset.proxy.Interceptor;


/**
 * <p>Title: InterceptorImpl.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-1-28 下午09:53:25
 * @author biaoping.yin
 * @version 1.0
 */
public class InterceptorImpl implements Interceptor {
	private A a;
	public void after(Method method, Object[] args) throws Throwable {
		System.out.println("Insterceptor.after(" + method.getName() + ", Object[] args)=" + args[0]);

	}

	public void afterFinally(Method method, Object[] args) throws Throwable {
		System.out.println("Insterceptor.afterFinally(" + method.getName() + ", Object[] args)=" + args[0]);
	}

	public void afterThrowing(Method method, Object[] args, Throwable throwable)
			throws Throwable {
		System.out.println("Insterceptor.afterThrowing(" + method.getName() + ", Object[] args, Throwable throwable)=" + args[0]);

	}

	public void before(Method method, Object[] args) throws Throwable {
		System.out.println("Insterceptor.before(" + method.getName() + ", Object[] args)=" + args[0]);


	}
}
