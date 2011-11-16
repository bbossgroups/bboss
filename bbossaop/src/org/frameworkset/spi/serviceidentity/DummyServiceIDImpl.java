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

package org.frameworkset.spi.serviceidentity;

import org.frameworkset.spi.BaseApplicationContext;


/**
 * <p>Title: DummyServiceIDImpl.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2011-5-11 ÉÏÎç11:24:01
 * @author biaoping.yin
 * @version 1.0
 */
public class DummyServiceIDImpl extends BaseServiceIDImpl {

	public DummyServiceIDImpl()
    {
    	super();
    }
	public DummyServiceIDImpl(String serviceID, int resultMode, int waittime,
			int resultType, int beanType,
			BaseApplicationContext applicationcontext) {
		super(serviceID, resultMode, waittime, resultType, beanType, applicationcontext);
		this.service = serviceID;
		// TODO Auto-generated constructor stub
	}
	public DummyServiceIDImpl(String serviceID, int resultMode, long timeout,
			int resultType, int beanType,
			BaseApplicationContext applicationcontext) {
		super(serviceID, resultMode, timeout, resultType, beanType, applicationcontext);
		this.service = serviceID;
		// TODO Auto-generated constructor stub
	}
	public DummyServiceIDImpl(String serviceID, String providerID,
			int resultMode, long timeout, int resultType, int beanType,
			BaseApplicationContext applicationcontext) {
		super(serviceID, providerID, resultMode, timeout, resultType, beanType,
				applicationcontext);
		this.service = serviceID;
		// TODO Auto-generated constructor stub
	}
	@Override
	public String getNextRestfulServiceAddress() {
		
		return null;
	}

	

}
