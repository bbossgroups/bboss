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
 * @Date 2011-5-11 11:24:01
 * @author biaoping.yin
 * @version 1.0
 */
public class DummyServiceIDImpl extends BaseServiceIDImpl {

	public DummyServiceIDImpl()
    {
    	super();
    }
	public DummyServiceIDImpl(String serviceID, String providertype, int beanType,
			BaseApplicationContext applicationcontext) {
//		super(serviceID,  beanType, applicationcontext);
		 super( serviceID, providertype,applicationcontext, beanType);
		// TODO Auto-generated constructor stub
	}

	

	

}
