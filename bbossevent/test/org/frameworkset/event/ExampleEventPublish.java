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

import java.util.Vector;

import org.frameworkset.spi.remote.JGroupHelper;
import org.junit.Test;

import bboss.org.jgroups.Address;

/**
 * 
 * 
 * <p>Title: TestEventHandle.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *

 * @Date Sep 21, 2008 9:28:05 PM
 * @author biaoping.yin
 * @version 1.0
 */
public class ExampleEventPublish {
	public static void publishEventtype1()
	{
		/**
		 * 构建事件消息【hello world type2.】，指定了事件的类型为ExampleEventType.type2
		 * 默认的事件消息广播途径为Event.REMOTELOCAL,你可以指定自己的事件广播途径
		 *  消息只在本地传播，消息被发送给所有对ExampleEventType.type2类型消息感兴趣的本地消息监听器和本地远程事件监听器
		 * 	Event event = new EventImpl("hello world type2.",ExampleEventType.type2,Event.LOCAL);
		 *  消息在网络上传播，消息被发送给所有对ExampleEventType.type2类型消息感兴趣的远程消息监听器和本地远程事件监听器，同时也会直接发送给本地监听器
		 *  Event event = new EventImpl("hello world type2.",ExampleEventType.type2,Event.REMOTELOCAL);
		 *  消息在网络上传播，消息被发送给所有对ExampleEventType.type2类型消息感兴趣的远程消息监听器和本地远程事件监听器
		 *  Event event = new EventImpl("hello world type2.",ExampleEventType.type2,Event.REMOTE);
		 */
		Event event = new EventImpl("hello world type1.",ExampleEventType.type1);
		
		/**
		 * 事件以同步方式传播
		 */
		
		EventHandle.getInstance().change(event);
		
	}
	
	public static void publishAsynEventtype1()
	{
		/**
		 * 构建事件消息【hello world type2.】，指定了事件的类型为ExampleEventType.type2
		 * 默认的事件消息广播途径为Event.REMOTELOCAL,你可以指定自己的事件广播途径
		 *  消息只在本地传播，消息被发送给所有对ExampleEventType.type2类型消息感兴趣的本地消息监听器和本地远程事件监听器
		 * 	Event event = new EventImpl("hello world type2.",ExampleEventType.type2,Event.LOCAL);
		 *  消息在网络上传播，消息被发送给所有对ExampleEventType.type2类型消息感兴趣的远程消息监听器和本地远程事件监听器，同时也会直接发送给本地监听器
		 *  Event event = new EventImpl("hello world type2.",ExampleEventType.type2,Event.REMOTELOCAL);
		 *  消息在网络上传播，消息被发送给所有对ExampleEventType.type2类型消息感兴趣的远程消息监听器和本地远程事件监听器
		 *  Event event = new EventImpl("hello world type2.",ExampleEventType.type2,Event.REMOTE);
		 */
		Event event = new EventImpl("hello world type1.",ExampleEventType.type1);
		
		/**
		 * 事件以同步方式传播
		 */
		
		EventHandle.getInstance().change(event,false);
		
	}
	
	public static void publishEventtype2()
	{
		/**
		 * 构建事件消息【hello world type2.】，指定了事件的类型为ExampleEventType.type2
		 * 默认的事件消息广播途径为Event.REMOTELOCAL,你可以指定自己的事件广播途径
		 *  消息只在本地传播，消息被发送给所有对ExampleEventType.type2类型消息感兴趣的本地消息监听器和本地远程事件监听器
		 * 	Event event = new EventImpl("hello world type2.",ExampleEventType.type2,Event.LOCAL);
		 *  消息在网络上传播，消息被发送给所有对ExampleEventType.type2类型消息感兴趣的远程消息监听器和本地远程事件监听器，同时也会直接发送给本地监听器
		 *  Event event = new EventImpl("hello world type2.",ExampleEventType.type2,Event.REMOTELOCAL);
		 *  消息在网络上传播，消息被发送给所有对ExampleEventType.type2类型消息感兴趣的远程消息监听器和本地远程事件监听器
		 *  Event event = new EventImpl("hello world type2.",ExampleEventType.type2,Event.REMOTE);
		 */
	
		Event event = new EventImpl("hello world type2.",ExampleEventType.type2);
		/**
		 * 消息以异步方式传递
		 */
		
		EventHandle.getInstance().change(event,false);
		
	}
	
	public static void publishEventtype2Withtarget()
	{
		/**
		 * 构建事件消息【hello world type2.】，指定了事件的类型为ExampleEventType.type2withtarget
		 * 默认的事件消息广播途径为Event.REMOTELOCAL,你可以指定自己的事件广播途径
	
		 *  消息在网络上传播，消息被发送给所有对ExampleEventType.type2类型消息感兴趣的远程消息监听器和本地远程事件监听器，同时也会直接发送给本地监听器
		 *  Event event = new EventImpl("hello world type2.",ExampleEventType.type2,Event.REMOTELOCAL);
		 *  消息在网络上传播，消息被发送给所有对ExampleEventType.type2类型消息感兴趣的远程消息监听器和本地远程事件监听器
		 *  Event event = new EventImpl("hello world type2.",ExampleEventType.type2,Event.REMOTE);
		 */
		

		EventTarget defualtprotocoltarget = null;
		Event event = null;
		Vector<Address> addresses = JGroupHelper.getJGroupHelper().getAppservers();
		if(addresses.size() > 0)//往一个节点发送数据
		{
			defualtprotocoltarget = new EventTarget("jgroup::" + addresses.get(0));
			
			defualtprotocoltarget.setUserAccount("admin");
			defualtprotocoltarget.setUserPassword("123456");
		    event = new EventImpl("hello world type2 with jgroup target[" + defualtprotocoltarget +"].",
									ExampleEventType.type2withtarget,
									defualtprotocoltarget,
									Event.REMOTE);
	
			EventHandle.getInstance().change(event);
		}
		if(addresses.size() > 2)//往前两个节点发送数据
		{
			defualtprotocoltarget = new EventTarget("jgroup::" + addresses.get(0) + ";" + addresses.get(1) );
			event = new EventImpl("hello world type2 with jgroups target[" + defualtprotocoltarget +"].",
									ExampleEventType.type2withtarget,
									defualtprotocoltarget,
									Event.REMOTE);

			EventHandle.getInstance().change(event);
		}
		
		//往所有节点广播消息
		{
			defualtprotocoltarget = new EventTarget("jgroup::all" );
			event = new EventImpl("hello world type2 with jgroups target[" + defualtprotocoltarget +"].",
									ExampleEventType.type2withtarget,
									defualtprotocoltarget);
	
			EventHandle.getInstance().change(event);
		}
	}
	
	@Test
	public static void publishFileEvent()
	{
		/**
		 * 构建事件消息【hello world type2.】，指定了事件的类型为ExampleEventType.type2withtarget
		 * 默认的事件消息广播途径为Event.REMOTELOCAL,你可以指定自己的事件广播途径
	
		 *  消息在网络上传播，消息被发送给所有对ExampleEventType.type2类型消息感兴趣的远程消息监听器和本地远程事件监听器，同时也会直接发送给本地监听器
		 *  Event event = new EventImpl("hello world type2.",ExampleEventType.type2,Event.REMOTELOCAL);
		 *  消息在网络上传播，消息被发送给所有对ExampleEventType.type2类型消息感兴趣的远程消息监听器和本地远程事件监听器
		 *  Event event = new EventImpl("hello world type2.",ExampleEventType.type2,Event.REMOTE);
		 */
		

		EventTarget
		defualtprotocoltarget = null;
		Event event = null;
//		Vector<Address> addresses = JGroupHelper.getJGroupHelper().getAppservers();
		
		
		defualtprotocoltarget = new EventTarget("netty","172.16.7.108",12347);
		event = new EventImpl("hello world type2 with jgroups target[" + defualtprotocoltarget +"].",
								ExampleEventType.type2withtarget,
								defualtprotocoltarget,
								Event.REMOTE);

		EventHandle.getInstance().change(event,true);
		
		
	}
	
	public static void main(String[] args)
	{


		publishFileEvent();
	}


	
	

}
