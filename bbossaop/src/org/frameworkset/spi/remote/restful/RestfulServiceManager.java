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

package org.frameworkset.spi.remote.restful;

import org.frameworkset.spi.ApplicationContext;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.BaseSPIManager;
import org.frameworkset.spi.remote.ServiceID;

/**
 * <p>Title: RestfulServiceManager.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-3-7 ÏÂÎç10:32:33
 * @author biaoping.yin
 * @version 1.0
 */
public class RestfulServiceManager {
	private static RestfulServiceManager instance;
	private RestfulServiceConvertor restfulServiceConvertor;
	private RestfulServiceManager()
	{
		
	}
	private void init(){
		restfulServiceConvertor = (RestfulServiceConvertor)BaseSPIManager.getBeanObject("rpc.restful.convertor");
	}
	public static RestfulServiceManager getRestfulServiceManager()
	{
		if(instance != null)
			return instance;
		synchronized(RestfulServiceManager.class)
		{
			if(instance != null)
				return instance;
			RestfulServiceManager temp = new RestfulServiceManager();
			temp.init();
			instance = temp;
			
		}
		return instance;
	}
	
	public ServiceID convert(ServiceID restserviceid,BaseApplicationContext applicationcontext)
	{
		String restfuluddi = restserviceid.getFistRestNode();
		String service = restserviceid.getService();
		String serviceid = this.restfulServiceConvertor.convert(restfuluddi, service);
		ServiceID ret = ApplicationContext.buildServiceID(serviceid, ServiceID.PROPERTY_BEAN_SERVICE,applicationcontext);
		return ret;
	}

}
