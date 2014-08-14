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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.frameworkset.log.DefaultBaseLogger;
import org.frameworkset.remote.EventRemoteService;
import org.frameworkset.remote.EventUtils;
import org.frameworkset.spi.ApplicationContext;
import org.frameworkset.spi.BaseSPIManager;
import org.frameworkset.spi.ClientProxyContext;
import org.frameworkset.spi.security.SecurityManager;
import org.frameworkset.thread.DelayThread;
import org.frameworkset.thread.RejectCallback;
import org.frameworkset.thread.ThreadPoolManagerFactory;
import org.frameworkset.thread.ThreadPoolManagerFactory.InnerThreadPoolExecutor;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;

/**
 * 事件处理抽象类
 * 
 * @author biaoping.yin
 * @version 1.0
 */
public class EventHandle extends RejectCallback implements Notifiable {
	private static final Logger log = Logger.getLogger(EventHandle.class);
	private static  DefaultBaseLogger baselog = new DefaultBaseLogger (log);
	
	private static EventHandle instance;
	
	public static final String  EVENT_SERVICE = "event.serivce";
	/**
	 * 存放系统中所有的本地和远程事件都感兴趣的监听器,当某个事件被触发时，事件管理框架将向list中的所有监听器发送消息 List<Listener>
	 */
	private static List list = new ArrayList();

	/**
	 * 存放系统中所有本地事件都感兴趣的监听器,当某个事件被触发时，事件管理框架将向list中的所有监听器发送消息 List<Listener>
	 */
	private static List locallist = new ArrayList();
	/**
	 * 将监听器所监听的事件类型对监听器建立索引，当某个类型的本地或远程事件被激发时， 事件管理框架除了给所有的事件感兴趣的监听器发
	 * 送消息时，还会给对该类型事件感兴趣的监听器发送消息
	 * 需要注意的是，如果某个监听器即对某个特定类型的事件感兴趣又对其他所有的事件感兴趣，那么将该监听器 按照对所有事件感兴趣的监听器处理 Map<EventType,List<Listener>>
	 */
	private static Map listenersIndexbyType = new HashMap();

	/**
	 * 将监听器所监听的事件类型对监听器建立索引，当某个类型的本地事件被激发时， 事件管理框架除了给所有的事件感兴趣的监听器发
	 * 送消息时，还会给对该类型事件感兴趣的监听器发送消息
	 * 需要注意的是，如果某个监听器即对某个特定类型的事件感兴趣又对其他所有的事件感兴趣，那么将该监听器 按照对所有事件感兴趣的监听器处理 Map<EventType,List<Listener>>
	 */
	private static Map localListenersIndexbyType = new HashMap();
	
	/**
	 * 监听所有类型远程事件的监听器列表
	 */
	private static List remoteListeners = new ArrayList();
	/**
	 * 监听特定类型远程事件的监听器列表
	 */
	private static Map remoteListenersIndexbyType = new HashMap();
	
	private static String event_user = BaseSPIManager.getProperty("event.user","admin");
	private static String event_password = BaseSPIManager.getProperty("event.password","123456");

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



	/**
	 * Description:
	 * 
	 * @param listener
	 * @see com.chinacreator.security.authorization.ACLNotifiable#addListener(com.chinacreator.security.authorization.ACLListener)
	 */
	public void addListener(Listener listener) {
		addListener(listener, true);
	}

	/**
	 * Description:
	 * 
	 * @param listener
	 * @see com.frameworkset.platform.security.authorization.ACLNotifiable#addListener(com.chinacreator.security.authorization.ACLListener)
	 */
	public void addListener(Listener listener, boolean remote) {
		

		if (remote) {
			this.addListener(listener,Listener.LOCAL_REMOTE);
//			if (!this.containListener(list, listener)) {
//				list.add(listener);
//			}
		} else {
			this.addListener(listener,Listener.LOCAL);
//			if (!this.containListener(locallist, listener)) {
//				locallist.add(listener);
//			}
		}

	}

	/**
	 * Description:注册监听器，被注册的监听器只对eventtypes中包含的事件类型的远程事件和本地事件感兴趣
	 * 
	 * @param listener
	 * @param List
	 *            <ResourceChangeEventType> 监听器需要监听的消息类型
	 * @see com.chinacreator.security.authorization.ACLNotifiable#addListener(com.chinacreator.security.authorization.ACLListener)
	 */
	public void addListener(Listener listener, List eventtypes) {

		addListener(listener, eventtypes, true);
	}

	/**
	 * 注册监听器，被注册的监听器只对eventtypes中包含的事件类型感兴趣，对其它类型的事件不敢兴趣
	 * 根据remote的值决定监听器是否监听远程事件和本地事件，remote=true时监听远程和本地事件，false时 只监听本地事件。
	 */
	public void addListener(Listener listener, List eventtypes, boolean remote) {
		if(remote)
		{
			this.addListener(listener, eventtypes, Listener.LOCAL_REMOTE);
		}
		else
		{
			this.addListener(listener, eventtypes, Listener.LOCAL);
		}		
	}
	
	
	public void addListener(Listener listener, List eventtypes, int listenerType) {
		
		/**
		 * 判断监听器是否已经在对所有消息都感兴趣的监听器列表中，已经存在就返回，否则 继续执行后续的工作
		 */
		if(listenerType == Listener.LOCAL)
		{
			if(containListener(locallist, listener))
				return;
		}
		else if(listenerType == Listener.LOCAL_REMOTE)
		{
			if(containListener(list, listener))
				return;
		}
		else if(listenerType == Listener.REMOTE)
		{
			if(containListener(remoteListeners, listener))
				return;
		}
		else //其它类型按LOCAL_REMOTE类型处理
		{
			if(containListener(list, listener))
				return;
		}
		if (eventtypes == null || eventtypes.size() == 0) {
			this.addListener(listener, listenerType);
		} else {
			if (listenerType == Listener.LOCAL_REMOTE) {
				for (int i = 0; i < eventtypes.size(); i++) {
					EventType eventType = (EventType) eventtypes.get(i);
					if (this.containEventType(listenersIndexbyType, eventType)) {
						List listeners = (List) listenersIndexbyType
								.get(eventType.toString());
						if(listeners == null)
						{
							listeners = new ArrayList();
							listenersIndexbyType.put(eventType.toString(), listeners);
						}
						if (containListener(listeners, listener)) {
							continue;

						}
						listeners.add(listener);
					} else {
						List listeners = new ArrayList();
						listeners.add(listener);
						listenersIndexbyType.put(eventType.toString(),
								listeners);

					}
				}
			} 
			else if (listenerType == Listener.LOCAL) 
			{
				for (int i = 0; i < eventtypes.size(); i++) {
					EventType eventType = (EventType) eventtypes.get(i);
					if (this.containEventType(localListenersIndexbyType,
							eventType)) {
						List listeners = (List) localListenersIndexbyType
								.get(eventType.toString());
						if(listeners == null)
						{
							listeners = new ArrayList();
							localListenersIndexbyType.put(eventType.toString(), listeners);
						}
						if (containListener(listeners, listener)) {
							continue;

						}
						listeners.add(listener);
					} else {
						List listeners = new ArrayList();
						listeners.add(listener);
						localListenersIndexbyType.put(eventType.toString(),
								listeners);

					}
				}
			}
			
			else if (listenerType == Listener.REMOTE) 
			{
				for (int i = 0; i < eventtypes.size(); i++) {
					EventType eventType = (EventType) eventtypes.get(i);
					if (this.containEventType(remoteListenersIndexbyType,
							eventType)) {
						List listeners = (List) remoteListenersIndexbyType
								.get(eventType.toString());
						if(listeners == null)
						{
							listeners = new ArrayList();
							remoteListenersIndexbyType.put(eventType.toString(), listeners);
						}
						if (containListener(listeners, listener)) {
							continue;

						}
						listeners.add(listener);
					} else {
						List listeners = new ArrayList();
						listeners.add(listener);
						remoteListenersIndexbyType.put(eventType.toString(),
								listeners);

					}
				}
			}
			else  //其他类型的监听器，按照LOCAL_REMOTE类型处理
			{
				log.warn("addListener:注册不支持的监听器类型[" + listenerType + ",listener=" + listener + "] on line 332");
				for (int i = 0; i < eventtypes.size(); i++) {
					EventType eventType = (EventType) eventtypes.get(i);
					if (this.containEventType(listenersIndexbyType, eventType)) {
						List listeners = (List) listenersIndexbyType
								.get(eventType.toString());
						if(listeners == null)
						{
							listeners = new ArrayList();
							listenersIndexbyType.put(eventType.toString(), listeners);
						}
						if (containListener(listeners, listener)) {
							continue;

						}
						listeners.add(listener);
					} else {
						List listeners = new ArrayList();
						listeners.add(listener);
						listenersIndexbyType.put(eventType.toString(),
								listeners);

					}
				}
			}
		}
	}



	public void addListener(Listener listener, int listenerType) {
		if (listenerType == Listener.LOCAL_REMOTE) {
			
			if (!this.containListener(list, listener)) {
				list.add(listener);
			}
		} else if (listenerType == Listener.LOCAL){			
			if (!this.containListener(locallist, listener)) {
				locallist.add(listener);
			}
		}
		else if (listenerType == Listener.REMOTE){			
			if (!this.containListener(remoteListeners, listener)) {
				remoteListeners.add(listener);
			}
		}
		else //其他类型的监听器，按照LOCAL_REMOTE类型处理
		{
			log.warn("addListener:注册不支持的监听器类型[" + listenerType + ",listener=" + listener + "] on line 375");
			if (!this.containListener(list, listener)) {
				list.add(listener);
			}
		}
		
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
	private boolean containEventType(Map indexs, EventType eventType) {
		return indexs.containsKey(eventType.toString());
	}

	/**
	 * Description:
	 * 
	 * @param source
	 * @see com.chinacreator.security.authorization.ACLNotifiable#change(java.lang.Object)
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
		if(event.isLocal() )
		{
			if(event.getEventTarget() != null)
				log.warn("本地事件不能指定事件发送远程目标：target=" + event.getEventTarget() + ",event source:" + event.getSource() + ",event type:" + event.getType());
			try {
				_handleCommon(event);
			} catch (Exception e) {
				log.error(e);
				e.printStackTrace();
			}
			try {
				_handleLocal(event);
			} catch (Exception e) {
				log.error("",e);
			}
			return;
		}
		    
			
		else if(event.isRemoteLocal())
		{
			try {
				_handleLocal(event);//首先发送给本地事件类型监听器
			} catch (Exception e) {
				log.error("",e);
			}
			if (!EventUtils.remoteevent_enabled()) {
				
				try {
					_handleCommon(event);//如果没有启用集群，那么向本地远程事件类型监听器发送消息
				} catch (Exception e) {
					log.error("",e);
				}
				if(event.getEventTarget() != null)
					log.warn("远程事件被禁用，忽略发送远程事件：target=" + event.getEventTarget() + ",event source:" + event.getSource() + ",event type:" + event.getType());
				
			}
			else
			{				
				if(EventUtils.cluster_enable() && event.getEventTarget() == null)
				{
					
					try{
						EventRemoteService eventRemoteService = ClientProxyContext.getApplicationClientBean(getEVENT_SERVICEName(null),EventRemoteService.class);
						UUIDGenerator uuid_gen = UUIDGenerator.getInstance();
						UUID uuid = uuid_gen.generateRandomBasedUUID();
						String key = uuid.toString();
					    eventRemoteService.remoteEventChange(key,event);
					}catch(Exception e){
						log.error("",e);
						_handleCommon(event);//如果集群环境下，正常情况下，消息会被通用监听器接收，但是如果集群处理失败，那么需要直接将事件发给自己
						return;
						
					}
				}
				else if(event.getEventTarget() != null)
				{	
					try{
						EventRemoteService eventRemoteService = ClientProxyContext.getApplicationClientBean(getEVENT_SERVICEName(event.getEventTarget()),EventRemoteService.class);
						UUIDGenerator uuid_gen = UUIDGenerator.getInstance();
						UUID uuid = uuid_gen.generateRandomBasedUUID();
						String key = uuid.toString();
					    eventRemoteService.remoteEventChange(key,event);					    
					}catch(Exception e){
						log.error("",e);
					}
					try {
						_handleCommon(event);//本地可能会重复接收到同一个事件，因为如果指定的目标地址包含了本地地址
					} catch (Exception e) {
						log.error("",e);
					}
					
										
				}
					
				else 
				{
					try {
						_handleCommon(event);
					} catch (Exception e) {
						log.error("",e);
					}
				}
			}
		} 
		
		else if(event.isRemote()) //不能发送给本地远程事件监听器和本地事件监听器
		{
			if (!EventUtils.remoteevent_enabled()) {
				log.debug("系统被设置为禁用远程事件,忽略远程事件的处理：target=" + event.getEventTarget() + ",event source:" + event.getSource() + ",event type:" + event.getType());
			}
			else
			{
				if(EventUtils.cluster_enable() || event.getEventTarget() != null)
				{
				    EventRemoteService eventRemoteService = ClientProxyContext.getApplicationClientBean(getEVENT_SERVICEName(event.getEventTarget()),EventRemoteService.class);
					UUIDGenerator uuid_gen = UUIDGenerator.getInstance();
					UUID uuid = uuid_gen.generateRandomBasedUUID();
					String key = uuid.toString();
		
					try{
					    eventRemoteService.remoteEventChange(key,event);
					}catch(Exception e){
						
						log.error("",e);
//						_handleRemote(event);
						return;
						
					}
				}
					
				
			}
		}
		

	}
	private static String static_networks = BaseSPIManager.getProperty("event.static-networks","all");
	public static String getEVENT_SERVICEName(EventTarget target)
	{
		String user = target ==null || target.getUserAccount() == null || target.getUserAccount().equals("")?event_user:target.getUserAccount();
		String password =  target ==null || target.getUserPassword() == null || target.getUserPassword().equals("")?event_password:target.getUserPassword();
		  
			
		StringBuffer st = new StringBuffer();
	    if(target == null)
	    {
	         st.append("(").append(static_networks).append(")/").append(EVENT_SERVICE);
	    }
	    else
	    {
	         st.append("(").append(target.getStringTargets()).append(")/" ).append( EVENT_SERVICE);
	    }
	    if(user != null)
	    {
	    	st.append("?").append(SecurityManager.USER_ACCOUNT_KEY).append("=").append(user).append("&");
	    	st.append(SecurityManager.USER_PASSWORD_KEY).append("=").append(password != null?password:"");
	    }
	    
	    return st.toString();
	}

	/**
	 * 向只对本地事件感兴趣的监听器，广播本地事件
	 * 
	 * @param event
	 */
	private static void _handleLocal(Event event) {
		for (int i = 0; locallist != null && i < locallist.size(); i++) {
			Listener listener = (Listener) locallist.get(i);
			try {
				listener.handle(event);
			} catch (Exception e) {
				log.error(event,e);
			}
		}

		EventType eventType = event.getType();
		List listeners = (List) localListenersIndexbyType.get(eventType.toString());
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
	 * 向只对远程事件感兴趣的监听器，广播本地事件
	 * 
	 * @param event
	 */
	private static void _handleRemote(Event event) {
		for (int i = 0; EventHandle.remoteListeners != null && i < EventHandle.remoteListeners.size(); i++) {
			Listener listener = (Listener) locallist.get(i);
			try {
				listener.handle(event);
			} catch (Exception e) {
				log.error(event,e);
			}
		}

		EventType eventType = event.getType();
		List listeners = (List) EventHandle.remoteListenersIndexbyType.get(eventType.toString());
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
	 * 如果系统允许集群或者多实例间缓冲数据并且集群已经正常启动则首先在集群中广播
	 */
	public void change(Event event, boolean synchronizable) {
	
		event.setSynchronized(synchronizable);
		_change(event);

	}
	static InnerThreadPoolExecutor executor ;
	/**
	 * 本地事件处理
	 * 
	 * @param event
	 */
	private void _change(Event event) {

		if (!event.isSynchronized()) {
			init();
			executor.busy(this, baselog);
			commandsQueue.add(event);
		   
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

	/**
	 * 定义线程，异步处理所有的事件，已实时更新缓冲区的权限信息
	 * 
	 * @author biaoping.yin
	 * @version 1.0
	 */
	static class HandleThread extends DelayThread {
		// Iterator listeners;

		Event source;

		HandleThread(Event source) {
			this.source = source;
//			if (source != null) {
//				Thread t = new Thread(this);
//				t.start();
//			}
		}

		/**
		 * Description:线程主体
		 * 
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			if(source != null)
				handleEvent(source);
		}

                public void setReject()
                {
                    // TODO Auto-generated method stub
                    
                }
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
	/**
	 * 外部掉用的远程处理方法，所有的远程事件通过外部方法来处理
	 * @param eventfqn
	 * @return
	 * @throws Exception
	 */
	public static int remotechange(String eventfqn,Event event) throws Exception {
		log.info("remote fqn:" + eventfqn);

		if (event == null) {
			log.info("Remote event handle ignore event=null.");
			return 0;
		}
		
		log.info("Remote event target :" + event.getEventTarget());
		log.info("Remote event type:" + event.getType());
		try {
//			if(event.isRemoteLocal())
//			{
//				_handleCommon(event);
//			}
//			else if(event.isRemote())
			{
				try
				{
					_handleRemote(event);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				try
				{
					_handleCommon(event);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	private static int maxSerials = ApplicationContext.getApplicationContext().getIntProperty("event.block.queue.size",200);
	private static LinkedBlockingQueue<Event> commandsQueue = new LinkedBlockingQueue<Event>(maxSerials);
	public static class WaiterFailedHandler implements org.frameworkset.thread.WaitFailHandler<HandleThread>
    {
        public void failhandler(HandleThread run)
        {
        	commandsQueue.add(run.source);
        }

    }
	private static Thread evntHanderWorker;
	private static boolean stopped = false;
	private static boolean inited = false;
	
	
	static synchronized void init()
	{
		if(inited )
			return;
		inited = true;
		executor = (InnerThreadPoolExecutor)ThreadPoolManagerFactory.getThreadPoolExecutor("event.threadpool");
		evntHanderWorker = new Thread(new Runnable(){
			public void run() {
				
				while(true)
				{
					try {
						Event<?> event = commandsQueue.take();
						if(stopped)
							break;
						executor.execute(new HandleThread(event));
					} catch (InterruptedException e) {
//						e.printStackTrace();
						if(stopped)
							break;
						log.error(e);
						continue;
					}
					
				}
				 
			}},"Event Handler Worker");
		evntHanderWorker.start();
	}
	
	public static void shutdown()
	{
		if(stopped)
			return;
		stopped = true;
		try {
			if(evntHanderWorker != null)
			{
				synchronized(evntHanderWorker)
				{
					
					evntHanderWorker.interrupt();
					evntHanderWorker = null;
					
				}
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			if(executor != null)
			{
				executor.shutdown();
				executor = null;
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//inited = false;
		baselog = null;
		try {
			if(commandsQueue != null)
			{
				commandsQueue.clear();
				commandsQueue = null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		instance = null;
		list = null;
		listenersIndexbyType = null;
		locallist = null;
		localListenersIndexbyType = null;
		remoteListeners = null;
		remoteListeners = null;
		
		
		
	}



	


}
