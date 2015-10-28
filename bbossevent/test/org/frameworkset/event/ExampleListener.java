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
package org.frameworkset.event;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * <p>Title: ExampleListener.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *

 * @Date Sep 21, 2008 6:03:33 PM
 * @author biaoping.yin
 * @version 1.0
 */
public class ExampleListener implements Listener<String>{
	/**
	 * 注册监听器,监听ExampleEventType.type1和ExampleEventType.type2两种类型的事件
	 * 事件消息可以是本地事件，可以是远程本地消息，也可以是远程消息
	 */
	public void init()
	{
		List eventtypes = new ArrayList();
		eventtypes.add(ExampleEventType.type1);
		eventtypes.add(ExampleEventType.type2);
		eventtypes.add(ExampleEventType.type2withtarget);
		//监听器监听的事件消息可以是本地事件，可以是远程本地消息，也可以是远程消息
		//如果不指定eventtypes则监听所有类型的事件消息
		NotifiableFactory.getNotifiable().addListener(this, eventtypes);
		/**
		 * 只监听本地消息和本地发布的本地远程消息
		 * NotifiableFactory.getNotifiable().addListener(this, eventtypes,Listener.LOCAL);
		 * 只监听本地消息和从远程消息广播器发布的远程消息和远程本地消息
		 * NotifiableFactory.getNotifiable().addListener(this, eventtypes,Listener.LOCAL_REMOTE);
		 * 只监听从远程消息广播器发布的远程消息和远程本地消息
		 * NotifiableFactory.getNotifiable().addListener(this, eventtypes,Listener.REMOTE);
		 */
		
	}
	/**
	 * 处理监听到的消息
	 */
	public void handle(Event<String> e) {
		
		if(e == null)
			return;
		if(e.getType().equals(ExampleEventType.type1))
		{
			System.out.println("Receive event type is " + e.getType());
			
		}
		else if(e.getType().equals(ExampleEventType.type2))
		{
			System.out.println("Receive event type is " + e.getType());
		}
		else if(e.getType().equals(ExampleEventType.type2withtarget))
		{
			System.out.println("Receive event type is " + e.getType() + " from "  + e.getEventTarget());
		}
		else
		{
			System.out.println("Unknown event type :" + e.getType());
		}
		System.out.println("the event source :" + e.getSource());
		String oj = e.getSource();
		
	}

}
