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

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.jgroups.Address;

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
	
	private static boolean remoteevent_enabled ; 
	
	private static EventRPCDispatcher eventRPCDispatcher;
	private static boolean inited ;
	private static Map<String,String> protocols = null;
	private static String defaultProtocol = "udp";
	private static String protocol = null;
	public static void init()
	{
		if(inited)
			return;
		synchronized(EventUtils.class)
		{
			if(inited)
				return;
			
			try {
				BaseApplicationContext eventcontext = DefaultApplicationContext.getApplicationContext("eventconf.xml");
				protocols = eventcontext.getTBeanObject("jgroup_protocols", Map.class);
				protocol = (String)eventcontext.getExtendAttribute("jgroup_protocols", "use");
				remoteevent_enabled =eventcontext.getBooleanProperty("remoteevent.enabled",true); 
				if(remoteevent_enabled)				
					eventRPCDispatcher =eventcontext.getTBeanObject("eventRPCDispatcher", EventRPCDispatcher.class);
				
			} catch (Throwable e) {
				log.error("init event RPC Dispatcher failed:", e);
			}
			inited = true;
		}
		
	}
	public static String getProtocolConfigFile()
	{
		
		if(protocols != null)
		{
			
			return protocols.get(protocol);
		}
		else
		{
			BaseApplicationContext eventcontext = DefaultApplicationContext.getApplicationContext("eventconf.xml");
			protocols = eventcontext.getTBeanObject("jgroup_protocols", Map.class);
			protocol = (String)eventcontext.getExtendAttribute("jgroup_protocols", "use");
			return protocols.get(protocol);
		}
	}
	public static boolean remoteevent_enabled()
    {
		init();
        return remoteevent_enabled;
    }

	public static boolean containSelf( List<Address> addresses)
	{
		init();		
		
		if(eventRPCDispatcher != null)
			return eventRPCDispatcher.containSelf(addresses);
		return false;
	}
	
	public static List<Address> removeSelf( List<Address> addresses)
	{
		init();		
		
		if(eventRPCDispatcher != null)
			return eventRPCDispatcher.removeSelf(addresses);
		return addresses;
	}
	public static EventRPCDispatcher getEventRPCDispatcher() {
		init();		
		return eventRPCDispatcher;
	}
	
	public static List<Address> getRPCAddresses() {
		init();		
		
		if(eventRPCDispatcher != null)
			return eventRPCDispatcher.getAddresses();
		return null;
	}

	public static String getDefaultProtocol() {
		return defaultProtocol;
	}
}
