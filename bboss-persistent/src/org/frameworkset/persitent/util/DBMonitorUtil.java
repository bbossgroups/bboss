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

package org.frameworkset.persitent.util;

import org.frameworkset.spi.ApplicationContext;

import com.frameworkset.common.poolman.monitor.PoolMonitorService;

/**
 * <p>Title: DBMonitorUtil.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-5-28 下午06:08:31
 * @author biaoping.yin
 * @version 1.0
 */
public class DBMonitorUtil {
	public static final String monitorService = "pool.monitor.service";
	public static final ApplicationContext context = ApplicationContext.getApplicationContext("com/frameworkset/common/poolman/monitor/poolmonitor-service.xml");
	public static PoolMonitorService getPoolMonitorService()
	{
		
		return (PoolMonitorService)context.getBeanObject(monitorService);
	}
	
	
	public static PoolMonitorService getPoolMonitorService(String url)
	{
		
		return (PoolMonitorService)context.getBeanObject(url);
	}
	
	
	

}
