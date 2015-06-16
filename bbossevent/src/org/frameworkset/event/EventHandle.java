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

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;
import org.frameworkset.remote.EventUtils;
import org.jgroups.Address;

/**
 * 事件处理抽象类
 * 
 * @author biaoping.yin
 * @version 1.0
 */
public class EventHandle  implements Notifiable {
	private static final Logger log = Logger.getLogger(EventHandle.class);
	
	static ThreadPoolExecutor sendexecutor = new java.util.concurrent.ThreadPoolExecutor(50,Integer.MAX_VALUE, 0, NANOSECONDS,
            new java.util.concurrent.ArrayBlockingQueue<Runnable>(10)
            {

				@Override
				public boolean offer(Runnable e) {
					
					try {
						super.put(e);
						return true;
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					return false;
				}
		
            });
	static ThreadPoolExecutor receiveexecutor = new java.util.concurrent.ThreadPoolExecutor(50,Integer.MAX_VALUE, 0, NANOSECONDS,
            new java.util.concurrent.ArrayBlockingQueue<Runnable>(10)
            {

				@Override
				public boolean offer(Runnable e) {
					
					try {
						super.put(e);
						return true;
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					return false;
				}
		
            });
	private static EventHandle instance;
	
	public static final String  EVENT_SERVICE = "event.serivce";
	/**
	 * 存放系统中所有的本地和远程事件都感兴趣的监听器,当某个事件被触发时，事件管理框架将向list中的所有监听器发送消息 List<Listener>
	 */
	private static List list = new ArrayList();

//	/**
//	 * 存放系统中所有本地事件都感兴趣的监听器,当某个事件被触发时，事件管理框架将向list中的所有监听器发送消息 List<Listener>
//	 */
//	private static List locallist = new ArrayList();
	/**
	 * 将监听器所监听的事件类型对监听器建立索引，当某个类型的本地或远程事件被激发时， 事件管理框架除了给所有的事件感兴趣的监听器发
	 * 送消息时，还会给对该类型事件感兴趣的监听器发送消息
	 * 需要注意的是，如果某个监听器即对某个特定类型的事件感兴趣又对其他所有的事件感兴趣，那么将该监听器 按照对所有事件感兴趣的监听器处理 Map<EventType,List<Listener>>
	 */
	private static Map listenersIndexbyType = new HashMap();

//	/**
//	 * 将监听器所监听的事件类型对监听器建立索引，当某个类型的本地事件被激发时， 事件管理框架除了给所有的事件感兴趣的监听器发
//	 * 送消息时，还会给对该类型事件感兴趣的监听器发送消息
//	 * 需要注意的是，如果某个监听器即对某个特定类型的事件感兴趣又对其他所有的事件感兴趣，那么将该监听器 按照对所有事件感兴趣的监听器处理 Map<EventType,List<Listener>>
//	 */
//	private static Map localListenersIndexbyType = new HashMap();
//	
//	/**
//	 * 监听所有类型远程事件的监听器列表
//	 */
//	private static List remoteListeners = new ArrayList();
//	/**
//	 * 监听特定类型远程事件的监听器列表
//	 */
//	private static Map remoteListenersIndexbyType = new HashMap();
	
 
//	/**
//	 * 事件缓冲器，通过缓冲中事件数据的变化情况来在集群环境中广播事件 Map<fqn,Event>
//	 * 
//	 */
//	private static AopMapContainer treeMap = null;
	
//	public static final String EVENT_CLUSTER = JBOSSCacheContainer.clusterName
//			+ ".EventRpcDispatcherGroup";
//	boolean rpcinited = false;
	
	

	protected EventHandle() {
		
	}
	
	public static EventHandle getInstance()
	{
		
		if(instance == null)
		{
			EventHandle temp = new EventHandle();
			instance = temp;
		}
		return instance;
	}
	
	public static void sendEvent(Event event)
	{
		getInstance().change(event);
	}



	/**
	 * Description:
	 * 
	 * @param listener
	 * @see .authorization.ACLNotifiable#addListener(.authorization.ACLListener)
	 */
	public void addListener(Listener listener) {
		if(!this.containListener(list, listener))
			list.add(listener);
	}

	/**
	 * Description:
	 * 
	 * @param listener
	/**
	 * @see addListener(Listener listener )
	 */
	@Deprecated 
	public void addListener(Listener listener, boolean remote) {
		

//		if (remote) {
//			this.addListener(listener,Listener.LOCAL_REMOTE);
////			if (!this.containListener(list, listener)) {
////				list.add(listener);
////			}
//		} else 
		{
			this.addListener(listener );
//			if (!this.containListener(locallist, listener)) {
//				locallist.add(listener);
//			}
		}

	}

	public void addListener(Listener listener, EventType eventtype) {
		if(eventtype == null)
		{
			this.addListener(listener);
		}
		else
		{
			List type = new ArrayList();
			type.add(eventtype);
			this.addListener(listener, type);
		}
		
	}
	/**
	 * Description:注册监听器，被注册的监听器只对eventtypes中包含的事件类型的远程事件和本地事件感兴趣
	 * 
	 * @param listener
	 * @param List
	 *            <ResourceChangeEventType> 监听器需要监听的消息类型
	 * @see .authorization.ACLNotifiable#addListener(.authorization.ACLListener)
	 */
	public void addListener(Listener listener, List eventtypes) {

		if (eventtypes == null || eventtypes.size() == 0) {
			this.addListener(listener);
		}
		else
		{
			 
				for (int i = 0; i < eventtypes.size(); i++) {
					EventType eventType = (EventType) eventtypes.get(i);
					String key = eventType.toString();
					if (this.containEventType(listenersIndexbyType, key)) {
						List listeners = (List) listenersIndexbyType
								.get(key);
						if(listeners == null)
						{
							listeners = new ArrayList();
							listenersIndexbyType.put(key, listeners);
						}
						if (containListener(listeners, listener)) {
							continue;

						}
						listeners.add(listener);
					} else {
						List listeners = new ArrayList();
						listeners.add(listener);
						listenersIndexbyType.put(key,
								listeners);

					}
				}
			 
		}
	}

	/**
	 * 注册监听器，被注册的监听器只对eventtypes中包含的事件类型感兴趣，对其它类型的事件不敢兴趣
	 * 根据remote的值决定监听器是否监听远程事件和本地事件，remote=true时监听远程和本地事件，false时 只监听本地事件。
	 * @see addListener(Listener listener, List eventtypes)
	 */
	@Deprecated 
	public void addListener(Listener listener, List eventtypes, boolean remote) {
		 
			this.addListener(listener, eventtypes );
		 	
	}
	

	
	/**
	 *@see addListener(Listener listener, List eventtypes)
	 */
	@Deprecated 
	public void addListener(Listener listener, List eventtypes, int listenerType) {
		addListener(listener, eventtypes);
				
		
	}



	/**
	 * @see addListener(Listener listener )
	 */
	@Deprecated 
	public void addListener(Listener listener, int listenerType) {
		 addListener( listener ) ;
//		if (listenerType == Listener.LOCAL_REMOTE) {
//			
//			if (!this.containListener(list, listener)) {
//				list.add(listener);
//			}
//		} else if (listenerType == Listener.LOCAL){			
//			if (!this.containListener(locallist, listener)) {
//				locallist.add(listener);
//			}
//		}
//		else if (listenerType == Listener.REMOTE){			
//			if (!this.containListener(remoteListeners, listener)) {
//				remoteListeners.add(listener);
//			}
//		}
//		else //其他类型的监听器，按照LOCAL_REMOTE类型处理
//		{
//			log.warn("addListener:注册不支持的监听器类型[" + listenerType + ",listener=" + listener + "] on line 375");
//			if (!this.containListener(list, listener)) {
//				list.add(listener);
//			}
//		}
		
	}

	/**
	 * 判断监听器是否在列表中存在
	 */
	private boolean containListener(List listeners, Listener listener) {
		for (int i = 0; i < listeners.size(); i++) {
			Listener temp = (Listener) listeners.get(i);
			if (temp == listener) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断事件类型是否已经建立索引
	 * 
	 * @param eventType
	 * @return
	 */
	private boolean containEventType(Map indexs, String key) {
		return indexs.containsKey(key);
	}

	/**
	 * Description:
	 * 
	 * @param source
	 * @see .authorization.ACLNotifiable#change(java.lang.Object)
	 */
	public void change(Event event) {
		change(event, true);

	}

	/**
	 * 向对本地事件和远程事件感兴趣的监听器广播事件
	 * 
	 * @param event
	 */
	private static void _handleCommon(Event event) {
		for (int i = 0; list != null && i < list.size(); i++) {
			Listener listener = (Listener) list.get(i);
			try {
				listener.handle(event);
			} catch (Exception e) {
				log.error(event,e);
			}
		}

		EventType eventType = event.getType();
		List listeners = (List) listenersIndexbyType.get(eventType.toString());
		for (int i = 0; listeners != null && i < listeners.size(); i++) {
			Listener listener = (Listener) listeners.get(i);
			try {
				listener.handle(event);
			} catch (Exception e) {
				log.error(event,e);
			}
		}

	}

	/**
	 * 公共事件处理方法
	 * 
	 * @param event
	 */
	private static void handleEvent(Event event) {
		if(event.getEventTarget() != null)
		{
			 List<Address> addresses = EventUtils.removeSelf(event.getEventTarget().getBroadcastAddresses());//如果包含本地地址则需要剔除本地地址，本地事件单独进行处理
			 if(addresses.size() != event.getEventTarget().getBroadcastAddresses().size())//单独处理本地地址
				{
					try {
						_handleCommon (event);//如果没有启用集群，那么向本地远程事件类型监听器发送消息
					} catch (Exception e) {
						log.error("",e);
					}
				}
				try{
//						EventRemoteService eventRemoteService = ClientProxyContext.getApplicationClientBean(getEVENT_SERVICEName(event.getEventTarget()),EventRemoteService.class);
//						 
//					    eventRemoteService.remoteEventChange( event);	
					EventUtils.getEventRPCDispatcher().callRemote( addresses, event);
				}catch(Exception e){
//					_handleCommon(event);//如果集群环境下，正常情况下，消息会被通用监听器接收，但是如果集群处理失败，那么需要直接将事件发给自己
					log.error("",e);
				}
			
				
		}
		else
		{
			if(event.isLocal() )
			{
				try {
					_handleCommon(event);
				} catch (Exception e) {
					log.error("",e);
					
				}
				
				
			}				
			else if(event.isRemoteLocal())
			{
				
				if (!EventUtils.remoteevent_enabled()) {
					
					try {
						_handleCommon(event);
					} catch (Exception e) {
						log.error("",e);
						
					}
					
				}
				else
				{		
//					try {
//						_handleCommon(event);//如果没有启用集群，那么向本地远程事件类型监听器发送消息
//					} catch (Exception e) {
//						log.error("",e);
//					}
					
					try{
						EventUtils.getEventRPCDispatcher().callRemote( false, event);
//						EventRemoteService eventRemoteService = ClientProxyContext.getApplicationClientBean(getEVENT_SERVICEName(null),EventRemoteService.class);
//						 
//					    eventRemoteService.remoteEventChange( event);
					}catch(Exception e){
						log.error("",e);
//						_handleCommon(event);//如果集群环境下，正常情况下，消息会被通用监听器接收，但是如果集群处理失败，那么需要直接将事件发给自己
						return;
						
					}
					
					
					
						
//					
				}
			} 
			
			else if(event.isRemote()) //不能发送给本地远程事件监听器和本地事件监听器
			{
				if (!EventUtils.remoteevent_enabled()) {
					log.debug("系统被设置为禁用远程事件,忽略远程事件的处理：target=" + event.getEventTarget() + ",event source:" + event.getSource() + ",event type:" + event.getType());
				}
				else
				{
					try{
						EventUtils.getEventRPCDispatcher().callRemote(true,  event);
					}catch(Exception e){
						
						log.error("",e);
//						_handleRemote(event);
						return;
						
					}
						
						
					
				}
			}
		}
		
		

	}
	 

	

	/**
	 * 如果系统允许集群或者多实例间缓冲数据并且集群已经正常启动则首先在集群中广播
	 */
	public void change(Event event, boolean synchronizable) {
	
		event.setSynchronize(synchronizable);
		_change(event);

	}
	
	/**
	 * 本地事件处理
	 * 
	 * @param event
	 */
	private void _change(final Event event) {

		if (!event.isSynchronize()) {
			 
			
			sendexecutor.execute(new Runnable(){

				public void run() {
					handleEvent(event);
					
				}
				
			});
		   
		} else {
			handleEvent(event);
		}

	}

	public void change(String eventsource, EventType eventType) {

		change(eventsource, eventType, true);
	}

	public void change(String eventsource, EventType eventType,
			boolean synchronizable) {
		Event event = new EventImpl(eventsource, eventType);
		change(event, synchronizable);
	}

	

	

	// public static void main(String[] a)
	// {
	// //// org.jboss.mx.util.PropertyAccess d;
	// // EventHandle handle = new EventHandle ();
	// //
	// // Event e = new EventImpl("",ACLEventType.ORGUNIT_INFO_DELETE);
	// // handle.change(e);
	// //// treeMap.put(e.toString(), e);
	// //// System.out.println("adte:" + treeMap.get(e.toString()));
	// // //treeMap.remove(e.toString());
	// // T t = new T();
	// // t.run();
	// // B b = new B();
	// // b.run();
	// // testSerialzableObject();
	//		
	//		
	// }

//	static class T extends Thread {
//		public void run() {
//			EventHandle handle = new EventHandle();
//			try {
//				handle.init();
//			} catch (ChannelException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			System.out.println();
//			// Event e = new EventImpl("",ACLEventType.ORGUNIT_INFO_DELETE);
//			// handle.change(e);
//		}
//	}
//
//	static class B extends Thread {
//		public void run() {
//			while (true) {
//				try {
//					sleep(10000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			// EventHandle handle = new EventHandle ();
//
//			// Event e = new EventImpl("",ACLEventType.ORGUNIT_INFO_DELETE);
//			// handle.change(e);
//		}
//	}
	

	private static boolean stopped = false;
	
	
	public static void shutdown()
	{
		if(stopped)
			return;
		stopped = true;
		
		try {
			if(sendexecutor != null)
			{
				sendexecutor.shutdown();
				sendexecutor = null;
				
			}
		} catch (Exception e) {
			log.info("shutdown event send executor thread pool error:",e);
		}
		
		try {
			if(receiveexecutor != null)
			{
				receiveexecutor.shutdown();
				receiveexecutor = null;
				
			}
		} catch (Exception e) {
			log.info("shutdown event receive executor thread pool error:",e);
		}
		//inited = false;
		 
		instance = null;
		list = null;
		listenersIndexbyType = null;
		
		
		
		
	}

	public static Object remotechange(final Event event) {
		
		receiveexecutor.execute(new Runnable(){

			public void run() {
				_handleCommon(event);
				
			}
			
		});
		
		return 1;
	}



	


}
