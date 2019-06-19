package org.frameworkset.http.client;
/**
 * Copyright 2008 biaoping.yin
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.frameworkset.spi.remote.http.HttpRequestProxy;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/6/19 15:25
 * @author biaoping.yin
 * @version 1.0
 */
public class HttpRequestProxyTest {
	@Before
	public void startPool(){
		HttpRequestProxy.startHttpPools("application.properties");
	}
	@Test
	public void testGet(){
		String data = HttpRequestProxy.httpGetforString("/testBBossIndexCrud");
		System.out.println(data);
		do {
			try {
				data = HttpRequestProxy.httpGetforString("/testBBossIndexCrud");
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(3000l);
			} catch (Exception e) {
				break;
			}
			try {
				data = HttpRequestProxy.httpGetforString("/testBBossIndexCrud");
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				data = HttpRequestProxy.httpGetforString("/testBBossIndexCrud");
			} catch (Exception e) {
				e.printStackTrace();
			}
//			break;
		}
		while(true);
	}
}
