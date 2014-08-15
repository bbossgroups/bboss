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

package org.frameworkset.spi.assemble;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p>Title: JobMethod.java</p> 
 * <p>Description: 作业方法执行对象</p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2011-1-22 下午07:36:24
 * @author biaoping.yin
 * @version 1.0
 */
public class MethodInvoker {
	private boolean  isClassMethod = false; 
	private Object instance;
	private Object[] argments;
	private Method method;
	private Pro providerManagerInfo;
	public boolean isClassMethod()
	{
		return this.isClassMethod;
	}
	public MethodInvoker(boolean isClassMethod, Object instance, Object[] argments,
			Method method,Pro providerManagerInfo) {
		super();
		this.isClassMethod = isClassMethod;
		this.instance = instance;
		this.argments = argments;
		this.method = method;
		this.providerManagerInfo = providerManagerInfo;
	}
	
	public void invoker() throws IllegalArgumentException, 
								IllegalAccessException, InvocationTargetException
	{
		this.method.invoke(instance, argments);
	}
	public Pro getProviderManagerInfo() {
		return providerManagerInfo;
	}

}
