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

import java.util.HashMap;
import java.util.Map;

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
//		HttpRequestProxy.startHttpPools("application.properties");
		Map configs = new HashMap();
//		configs.put("http.poolNames","report");
//		configs.put("report.http.health","/health");//health监控检查地址必须配置，否则将不会启动健康检查机制
//		configs.put("report.http.discoverService","org.frameworkset.http.client.DemoHttpHostDiscover");
		configs.put("http.health","/health");//health监控检查地址必须配置，否则将不会启动健康检查机制
		configs.put("http.discoverService","org.frameworkset.http.client.DemoHttpHostDiscover");
		HttpRequestProxy.startHttpPools(configs);
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
