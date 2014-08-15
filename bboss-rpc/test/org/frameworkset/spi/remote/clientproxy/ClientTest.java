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

package org.frameworkset.spi.remote.clientproxy;

import org.frameworkset.spi.ClientProxyContext;
import org.junit.Test;

/**
 * <p>Title: ClientTest.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2011-10-20 下午01:12:19
 * @author biaoping.yin
 * @version 1.0
 */
public class ClientTest {
	@Test
	public void testMvcClient()
	{
		ClientInf inf = ClientProxyContext.getWebMVCClientBean("(http::172.16.25.108:8080/bboss-mvc/http.rpc)/client.proxy.demo?user=admin&password=123456", ClientInf.class);
		System.out.println(inf.helloworld("aaaa"));
	}

}
