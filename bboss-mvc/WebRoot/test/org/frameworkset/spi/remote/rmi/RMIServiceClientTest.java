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

package org.frameworkset.spi.remote.rmi;

import java.rmi.RemoteException;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>Title: RMIServiceClientTest.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2011-5-18 下午05:30:15
 * @author biaoping.yin
 * @version 1.0
 */
public class RMIServiceClientTest {
	BaseApplicationContext context ;
	@Before
	public void init()
	{
		context = DefaultApplicationContext.getApplicationContext("org/frameworkset/spi/remote/rmi/rmi-client.xml");
	}
	@Test 
	public void test() throws RemoteException
	{
		RMIServiceTestInf test = (RMIServiceTestInf)context.getBeanObject("rmi_service_client_test");
		System.out.println(test.sayHello("多多"));
	}

}
