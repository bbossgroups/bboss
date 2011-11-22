/**
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
package org.frameworkset.web.demo;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;


/**
 * <p>DemoUtil.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2011-6-25
 * @author biaoping.yin
 * @version 1.0
 */
public class DemoUtil
{
	private static DemoContentCache cachebean ;
	public static DemoContentCache getDemoContentCache()
	{
		if(cachebean == null)
		{
			BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("org/frameworkset/web/demo/bboss-demo.xml");
			cachebean = context.getTBeanObject(DemoContentCache.cacheobjectkey,DemoContentCache.class);
			
		}
		return cachebean;
	}
}
