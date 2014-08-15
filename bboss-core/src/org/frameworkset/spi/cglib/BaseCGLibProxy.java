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

import net.sf.cglib.proxy.MethodInterceptor;

import org.frameworkset.spi.CallContext;
import org.frameworkset.spi.assemble.BaseTXManager;

/**
 * <p>Title: BaseCGLibProxy.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-6-19 下午09:20:31
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class BaseCGLibProxy  implements MethodInterceptor{

	protected BaseTXManager providerManagerInfo;
	protected CallContext callcontext;
	protected Object delegate;
	public BaseCGLibProxy(Object delegate)
	{
		this.delegate = delegate;
	}
	public BaseCGLibProxy(Object delegate,BaseTXManager providerManagerInfo, CallContext callcontext) {
		super();
		this.providerManagerInfo = providerManagerInfo;
		
		this.callcontext = callcontext;
		this.delegate = delegate;
	}

}
