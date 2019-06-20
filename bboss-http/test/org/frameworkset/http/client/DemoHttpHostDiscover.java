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

import org.frameworkset.spi.remote.http.HttpHost;
import org.frameworkset.spi.remote.http.proxy.HttpHostDiscover;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/6/19 21:38
 * @author biaoping.yin
 * @version 1.0
 */
public class DemoHttpHostDiscover extends HttpHostDiscover {
	private int count = 0;
	@Override
	protected List<HttpHost> discover() {

		List<HttpHost> hosts = new ArrayList<HttpHost>();
		HttpHost host = new HttpHost("192.168.137.1:808");
		hosts.add(host);
		if(count != 2) {
			host = new HttpHost("192.168.137.1:809");
			hosts.add(host);
		}
		else{
			System.out.println("aa");
		}
		host = new HttpHost("192.168.137.1:810");
		hosts.add(host);
		count ++;
		return hosts;
	}
}
