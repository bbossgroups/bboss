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

public class Insterceptor1 implements Interceptor {

	public void after(Method method, Object[] args) throws Throwable {
		System.out.println("Insterceptor1.after(" + method.getName() + ", Object[] args)");

	}

	public void afterFinally(Method method, Object[] args) throws Throwable {
		System.out.println("Insterceptor1.afterFinally(" + method.getName() + ", Object[] args)");
	}

	public void afterThrowing(Method method, Object[] args, Throwable throwable)
			throws Throwable {
		System.out.println("Insterceptor1.afterThrowing(" + method.getName() + ", Object[] args, Throwable throwable)");

	}

	public void before(Method method, Object[] args) throws Throwable {
		System.out.println("Insterceptor1.before(" + method.getName() + ", Object[] args)");
	}

	public void afterThrowing(Method arg0, Object[] arg1) throws Throwable {
		// TODO Auto-generated method stub
		
	}

}
