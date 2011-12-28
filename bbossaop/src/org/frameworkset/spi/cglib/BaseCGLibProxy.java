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
import org.frameworkset.spi.remote.ServiceID;

/**
 * <p>Title: BaseCGLibProxy.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-6-19 ÏÂÎç09:20:31
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class BaseCGLibProxy  implements MethodInterceptor{

	protected BaseTXManager providerManagerInfo;
	protected ServiceID serviceID;
	protected CallContext callcontext;
	protected Object delegate;

	public BaseCGLibProxy(Object delegate,BaseTXManager providerManagerInfo,
			ServiceID serviceID, CallContext callcontext) {
		super();
		this.providerManagerInfo = providerManagerInfo;
		this.serviceID = serviceID;
		this.callcontext = callcontext;
		this.delegate = delegate;
	}

}
