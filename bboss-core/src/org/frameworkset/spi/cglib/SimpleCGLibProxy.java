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

import net.sf.cglib.proxy.MethodProxy;

import org.frameworkset.spi.CallContext;
import org.frameworkset.spi.assemble.BaseTXManager;
import org.frameworkset.spi.assemble.Pro;

/**
 * <p>Title: SimpleCGLibProxy.java</p> 
 * <p>Description: 主要适用于引用组件和内部组件的aop
 * 拦截和处理</p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2012-1-3 上午11:11:42
 * @author biaoping.yin
 * @version 1.0
 */
public class SimpleCGLibProxy  extends BaseCGLibProxy{

	

	public SimpleCGLibProxy(Object delegate,BaseTXManager providerManagerInfo) {
		super(delegate,providerManagerInfo,
				 (CallContext)null); 
	}

	public Object intercept(Object delegate, Method method, Object[] args,
			MethodProxy proxy) throws Throwable {
		return CGLibUtil.invoke(this.delegate, method, args, proxy,  (Pro)providerManagerInfo);
	}

}
