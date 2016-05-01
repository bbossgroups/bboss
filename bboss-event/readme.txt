directory:
src--source code
test--test source code
lib--bboss-event framework depends jars
dist--bboss-event release package.

---------------------------------
bbossevent关联工程：
---------------------------------
bbossevent<-cms_baseline [bboss-event.jar]

bbossevent->bboss-aop [bboss-aop.jar,bboss-camel.jar,bboss-mina.jar,bboss-jms.jar,bboss-ws.jar]
bbossevent->bboss-util [frameworkset-util.jar]

bbossevent->bboss-persistent [frameworkset-pool.jar] 因为aop框架中的事务注解依赖了持久层框架


------------------------------------------------------------------
update function list in bbossgroups-2.0-rc2 bbossgroups-2.0-rc1 since bbossgroups-2.0-rc
------------------------------------------------------------------
2010-10-09
------------------------------------------------------------------
o 修改执行事件监听器handle方法，修改远程事件处理逻辑
------------------------------------------------------------------
2010-09-26
------------------------------------------------------------------
o 修改执行事件监听器handle方法，增加对handle方法抛出的异常的处理
------------------------------------------------------------------
------------------------------------------------------------------
2010-09-13
------------------------------------------------------------------
o 修改测试用例，保持与aop框架的最新版本api同步（主要是jgroups的api发生了变化）
------------------------------------------------------------------
2010-08-30
------------------------------------------------------------------
o 修改异步事件处理，增加事件队列机制，防止事件堆积造成线程池处理忙的问题

/bbossevent/resources/event-service-assemble.xml增加以下属性配置event.block.queue.size
<property name="event.block.queue.size" value="200"/>
线程池event.threadpool的失败等待处理被设置为：
<property name="waitFailHandler" value="org.frameworkset.event.EventHandle$WaiterFailedHandler"/>

update function list:
------------------------------
 1.0.2 - 2010-03-16
------------------------------
o 修改Event，Listener,EventImpl三个类，支持java泛型
------------------------------
 1.0.2 - 2010-02-21
------------------------------
o 升级jgroup 2.8.0 GA
o 替换log4j.jar 为log4j-1.2.14.jar
o 升级aop框架：安全、认证、加密
o 添加frameworkset-pool.jar
o 移除jg-magic-map.xml文件，该文件已经打包到jgroups.jar中
------------------------------
 1.0.2 - 2010-03-07
------------------------------
o 将线程池移入bboss aop框架中
o 远程事件EventTarget地址支持以下4种方法：
1.默认rpc协议eventTarget（使用于mina和jgroup协议）
EventTarget defualtprotocoltarget = new EventTarget("192.168.11.102",1186);
2.指定rpc协议的eventTarget（使用于mina和jgroup协议）
EventTarget target = new EventTarget("jgroup","192.168.11.102",1186);
3.指定目标物理url的地址协议(适用于mina，jgroup，jms，webservice协议)
EventTarget target = new EventTarget("jgroup::192.168.11.102:1186");
4.指定群发地址列表
EventTarget target = new EventTarget("jgroup::192.168.11.102:1186;192.168.11.102:1187");

使用方法：
    EventTarget target = new EventTarget("jgroup","192.168.11.102",1186);
		Event event = new EventImpl("hello world type2 with target[" + target +"].",
								ExampleEventType.type2withtarget,
								target,
								Event.REMOTE);

		EventHandle.getInstance().change(event);
		
		EventTarget defualtprotocoltarget = new EventTarget("192.168.11.102",1186);
		event = new EventImpl("hello world type2 with default target[" + defualtprotocoltarget +"].",
								ExampleEventType.type2withtarget,
								defualtprotocoltarget,
								Event.REMOTE);

		EventHandle.getInstance().change(event);
		
		defualtprotocoltarget = new EventTarget("jgroup::192.168.11.102:1186");
		event = new EventImpl("hello world type2 with jgroup target[" + defualtprotocoltarget +"].",
								ExampleEventType.type2withtarget,
								defualtprotocoltarget,
								Event.REMOTE);

		EventHandle.getInstance().change(event);
		
		defualtprotocoltarget = new EventTarget("jgroup::192.168.11.102:1186;192.168.11.102:1187");
		event = new EventImpl("hello world type2 with jgroups target[" + defualtprotocoltarget +"].",
								ExampleEventType.type2withtarget,
								defualtprotocoltarget,
								Event.REMOTE);

		EventHandle.getInstance().change(event);

------------------------------
 1.0.2 - 2010-01-07
------------------------------
o 添加ant构建脚本和属性文件：build.xml,build.properties

------------------------------
 1.0.2 - 2009-06-01
------------------------------
o 添加ant构建脚本和属性文件：build.xml,build.properties
o 重写java.util.concurrent 线程池功能：
/bbossevent/src/org/frameworkset/thread/ThreadPoolExecutor.java
/bbossevent/src/org/frameworkset/thread/RejectedExecutionHandler.java
/bbossevent/src/manager-provider.xml
org/frameworkset/thread/ThreadPoolManagerFactory$InnerThreadPoolExecutor类增加方法：
public boolean busy(RejectCallback rejectcallback, BaseLogger log)
参数说明：
rejectcallback--回调方法，用来判别线程已经忙等待了多少次，以便计算和统计每次的等待时间
log-参数，日志记录接口，通过log可以将每次等待的信息输出的前端控制台

o 事件框架中的线程池完善
  改进线程池功能，完善任务拒绝策略中的WaitPolicy，增加delaytime，maxWait,waitFailHandler属性，以便实现等待时间递增策略，并提供最大等待次数
  如果超过最大等待次数并且配置了waitFailHandler属性直接将任务发给waitFailHandler处理器，否则丢弃任务。
 
 可以方便地配置多个线程池，详细信息可以参考src/event-service-assemble.xml中的配置：
 
 <!-- 
			最大等待5次，每次延时长10%
		 -->
		<property name="event.threadpool">
			<map>
				<property name="corePoolSize" value="5"/>
				<property name="maximumPoolSize" value="10"/>
				<!-- 
				TimeUnit.SECONDS
				TimeUnit.MICROSECONDS
				TimeUnit.MILLISECONDS
				TimeUnit.NANOSECONDS
				时间单位适用于以下参数：
				keepAliveTime
				waitTime
				delayTime（当delayTime为整数时间而不是百分比时有效）
				 -->
				<property name="timeUnit" value="TimeUnit.SECONDS"/>
				<property name="keepAliveTime" value="40"/>		
				<!-- 
				/**
		             
		             * LinkedBlockingQueue
		             * PriorityBlockingQueue
		             * ArrayBlockingQueue
		             * SynchronousQueue
		             */
				 -->
				<property name="blockingQueueType" value="ArrayBlockingQueue"/>		
				<property name="blockingQueue" value="10"/>
				
				<!-- 
				RejectedExecutionHandler
				必须实现java.util.concurrent.RejectedExecutionHandler接口
				目前系统提供以下缺省实现：
				org.frameworkset.thread.WaitPolicy  循环等待event.threadpool.waitTime指定的时间，单位为秒
				java.util.concurrent.ThreadPoolExecutor$DiscardPolicy 直接丢弃任务，不抛出异常
				java.util.concurrent.ThreadPoolExecutor$AbortPolicy 直接丢弃任务，抛出异常RejectedExecutionException
				java.util.concurrent.ThreadPoolExecutor$CallerRunsPolicy 直接运行
				java.util.concurrent.ThreadPoolExecutor$DiscardOldestPolicy 放入队列，将最老的任务删除
				 -->
				<property name="rejectedExecutionHandler" value="org.frameworkset.thread.WaitPolicy"/>
				<!-- 
					以下参数只有在配置的org.frameworkset.thread.WaitPolicy策略时才需要配置
				 -->
				<property name="waitTime" value="1"/>
				<property name="delayTime" value="10%"/>
				<property name="maxWaits" value="5"/>
				<property name="waitFailHandler" value="org.frameworkset.thread.TestThread$WaitFailHandlerTest"/>
			</map>
		</property> 
		
		具体使用方法如下：
		 
		 ThreadPoolExecutor executer = ThreadPoolManagerFactory.getThreadPoolExecutor("event.threadpool");//获取实例
		 如果对应的"event.threadpool"没有被配置，那么将使用默认的线程池参数配置：
		 	defaultPoolparams.put("corePoolSize", "5");
		    defaultPoolparams.put("maximumPoolSize", "10");
		    defaultPoolparams.put("keepAliveTime", "30");
		    defaultPoolparams.put("timeUnit", "TimeUnit.SECONDS");
		    defaultPoolparams.put("blockingQueue", "10");
		    defaultPoolparams.put("blockingQueueType", "ArrayBlockingQueue");	    
		    defaultPoolparams.put("rejectedExecutionHandler", "org.frameworkset.thread.WaitPolicy");
		    
		    defaultPoolparams.put("waitTime","1");
		    defaultPoolparams.put("delaytime","10%");
		    defaultPoolparams.put("maxWaits","10");
		 
        for(int i = 0; i < 50 ; i ++)
            executer.execute(new WaitRun());//通过线程池执行任务
           
        任务定义如下：
        public static class WaitRun extends DelayThread
    {

        public void run()
        {
            //do something.

        }

    }
    waitFailHandler 处理定义类
      public static class WaitFailHandlerTest implements WaitFailHandler<WaitRun>
    {

        public void failhandler(WaitRun r)
        {
            System.out.println("r:" + r);
            
        }
        
    }
	
	
	
  
o 事件管理框架远程事件无法发送的问题
原因是：

/bbossaop/src/org/frameworkset/remote/Target.java类没有序列化导致其子类
/bbossevent/src/org/frameworkset/event/EventTarget.java在发送时无法识别父类target中的对象

解决办法升级bboss-aop框架

o 升级frameworkset-pool.jar 
将com.frameworkset.common.poolman.sql.PrimaryKeyCache中的全局变量
  Map id_tables的初始化方式改为并发map，以免出现多线程安全性问题。
  
  public PrimaryKeyCache(String dbname) {
		this.dbname = dbname;
		id_tables = new java.util.concurrent.ConcurrentHashMap();
	}
	


------------------------------
 1.0.1 - 2009-05-17
------------------------------
o 标准的事件管理框架
o 支持远程事件
  支持点对点和多点远程事件广播和接收功能
  可静态指定事件广播的网络节点集，也可以缺省向集群中的所有节点发送事件消息
o 支持同步和异步两种事件发送机制
  

First version of bboss-event-1.0.1 released.

[http://blog.csdn.net/yin_bp]
[http://www.openbboss.com]