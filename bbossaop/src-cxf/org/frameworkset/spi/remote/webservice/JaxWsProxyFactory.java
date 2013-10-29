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

package org.frameworkset.spi.remote.webservice;



/**
 * <p>Title: JaxWsProxyFactory.java</p> 
 * <p>Description: ws 代理工厂，用来webservice 客户端调用组件
 * 使用实例:
 * org.frameworkset.web.ws.WSService wsservice = JaxWsProxyFactory.getWSClient("http://localhost:8080/bboss-mvc/cxfservices/mysfirstwsservicePort",
 *                                                org.frameworkset.web.ws.WSService.class);
 * </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2011-10-22 下午12:21:22
 * @author biaoping.yin
 * @version 1.0
 */
public class JaxWsProxyFactory
{
	/**
	 * 根据服务访问地址和服务对应的接口动态生成服务的客户端调用实例
	 * @param <T> 服务接口类型
	 * @param wsurl 服务访问地址
	 * @param wsinf 服务接口
	 * @return 客户端调用实例
	 */
	public static  <T> T getWSClient(String wsurl,Class<T> wsinf)
	{
		
		org.apache.cxf.jaxws.JaxWsProxyFactoryBean factory = new org.apache.cxf.jaxws.JaxWsProxyFactoryBean();
		factory.setAddress(wsurl);
		factory.setServiceClass(wsinf);
		T wsservice =  (T)factory.create();
		return wsservice;
	}
}
