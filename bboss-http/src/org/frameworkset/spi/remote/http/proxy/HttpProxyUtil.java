package org.frameworkset.spi.remote.http.proxy;
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

import org.frameworkset.spi.remote.http.ClientConfiguration;
import org.frameworkset.spi.remote.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * <p>Description: 从外部服务注册中心监听服务地址变更后调用本组件handleDiscoverHosts方法刷新负载组件地址清单</p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/6/20 12:47
 * @author biaoping.yin
 * @version 1.0
 */
public class HttpProxyUtil {
	private static Logger logger = LoggerFactory.getLogger(HttpProxyUtil.class);

	/**
	 *
	 * @param poolName
	 * @param hosts
	 */
	public static void handleDiscoverHosts(String poolName, List<HttpHost> hosts){
		if(poolName == null)
			poolName = "default";
		ClientConfiguration clientConfiguration = ClientConfiguration.getClientConfiguration(poolName);
		if (clientConfiguration != null){
			HttpHostDiscover httpHostDiscover = null;
			HttpServiceHosts httpServiceHosts = clientConfiguration.getHttpServiceHosts();
			if(httpServiceHosts != null) {
				httpHostDiscover = httpServiceHosts.getHostDiscover();
				if (httpHostDiscover == null) {
					if (logger.isInfoEnabled()) {//Registry default HttpHostDiscover
						logger.info("Registry default HttpHostDiscover to httppool[{}]", poolName);
					}
					synchronized (HttpProxyUtil.class) {
						httpHostDiscover = httpServiceHosts.getHostDiscover();
						if (httpHostDiscover == null) {
							httpHostDiscover = new DefaultHttpHostDiscover();
							httpHostDiscover.setHttpServiceHosts(httpServiceHosts);
							httpServiceHosts.setHostDiscover(httpHostDiscover);
						}
					}
				}
				if (httpHostDiscover != null) {
					httpHostDiscover.handleDiscoverHosts(hosts);
				}
			}
		}

	}
//	public static HttpHostDiscover getHttpHostDiscover(String poolName){
//		ClientConfiguration clientConfiguration = ClientConfiguration.getClientConfiguration(poolName);
//		if (clientConfiguration != null){
//			HttpServiceHosts httpServiceHosts = clientConfiguration.getHttpServiceHosts();
//			return httpServiceHosts != null?httpServiceHosts.getHostDiscover():null;
//		}
//		return null;
//	}

}
