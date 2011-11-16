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
package org.frameworkset.remote;

import org.apache.log4j.Logger;
import org.frameworkset.spi.BaseSPIManager;
import org.frameworkset.spi.remote.JGroupHelper;

/**
 * 
 * <p>Title: EventUtils.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright (c) 2009</p>
 *
 * <p>bboss workgroup</p>
 * @Date May 17, 2009
 * @author biaoping.yin
 * @version 1.0
 */
public class EventUtils {

	private static final Logger log = Logger.getLogger(EventUtils.class);
	private static boolean remoteevent_enabled = BaseSPIManager.getBooleanProperty("remoteevent.enabled",true); 
	
	public static boolean cluster_enable()
	{
	    return JGroupHelper.getJGroupHelper().clusterstarted();
	}
	
	public static boolean remoteevent_enabled()
        {
            return remoteevent_enabled;
        }
}
