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

package org.frameworkset.spi.async;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.frameworkset.spi.ApplicationContext;
import org.frameworkset.spi.assemble.ProMap;
import org.frameworkset.spi.async.annotation.Result;
import org.frameworkset.util.TimeUtil;

/**
 * <p>
 * Title: AsyncCall.java
 * </p>
 * <p>
 * Description: 执行服务的异步调用
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2011-4-21 上午11:06:42
 * @author biaoping.yin
 * @version 1.0
 */
public class AsyncCall {
	private static Logger log = Logger.getLogger(AsyncCall.class);
	// static java.util.concurrent.ThreadPoolExecutor executor = null;
	private java.util.concurrent.ThreadPoolExecutor callexecutor = null;
	private java.util.concurrent.ThreadPoolExecutor callbackexecutor = null;
	private Object lock = new Object();
	private boolean started = false;
	private CallHandler callHandler;
	private CallBackHandler callBackHandler;
	/**
	 * 请求调用堆栈
	 */
	private LinkedBlockingQueue<CallService> callblockqueue = new java.util.concurrent.LinkedBlockingQueue<CallService>(
			ApplicationContext.getApplicationContext().getIntProperty(
					"component.asynccall.block.size", 200));
	/**
	 * 请求响应回调处理堆栈
	 */
	private LinkedBlockingQueue<CallService> callbackblockqueue = new java.util.concurrent.LinkedBlockingQueue<CallService>(
			ApplicationContext.getApplicationContext().getIntProperty(
					"component.asynccallback.block.size", 200));

	public void start() {
		if (this.started)
			return;
		synchronized (lock) {
			if (this.started)
				return;
			initCallExecutor();
			initCallBackExecutor();
			callHandler = new CallHandler();
			new Thread(this.callHandler, "callHandler").start();
			callBackHandler = new CallBackHandler();
			new Thread(this.callBackHandler, "callBackHandler").start();
			this.started = true;
		}
	}

	public void stop() {
		synchronized (lock) {
			if (!this.started)
				return;
			stopCallExecutor();
			stopCallBackExecutor();
			started = false;
		}
	}

	private void stopCallExecutor() {
		if (this.callexecutor != null)
			this.callexecutor.shutdown();

	}

	private void stopCallBackExecutor() {
		if (this.callbackexecutor != null)
			this.callbackexecutor.shutdown();
	}

	private void initCallExecutor() {
		ProMap proMap = ApplicationContext.getApplicationContext()
				.getMapProperty("component.asynccall.threadpool");
		if (proMap == null || proMap.size() == 0) {
			/**
			 * public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
			 * long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable>
			 * workQueue, RejectedExecutionHandler handler)
			 */
			callexecutor = new java.util.concurrent.ThreadPoolExecutor(5, 20,
					40, TimeUnit.SECONDS,
					new java.util.concurrent.LinkedBlockingQueue<Runnable>(
							ApplicationContext.getApplicationContext()
									.getIntProperty(
											"component.asynccall.block.size",
											200) / 2), new CallerRunsPolicy());
		} else {
			String timeUnit = proMap.getString("timeUnit", "TimeUnit.SECONDS");
			TimeUnit timeUnit_ = TimeUtil.getTimeUnitByName(timeUnit, TimeUnit.SECONDS);

			callexecutor = new java.util.concurrent.ThreadPoolExecutor(proMap
					.getInt("corePoolSize", 5), proMap.getInt(
					"maximumPoolSize", 20), proMap.getInt("keepAliveTime", 40),
					timeUnit_,
					new java.util.concurrent.LinkedBlockingQueue<Runnable>(
							ApplicationContext.getApplicationContext()
									.getIntProperty(
											"component.asynccall.block.size",
											200) / 2), new CallerRunsPolicy());
		}
	}

	private void initCallBackExecutor() {
		ProMap proMap = ApplicationContext.getApplicationContext()
				.getMapProperty("component.asynccallback.threadpool");
		if (proMap == null || proMap.size() == 0) {
			/**
			 * public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
			 * long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable>
			 * workQueue, RejectedExecutionHandler handler)
			 */
			callbackexecutor = new java.util.concurrent.ThreadPoolExecutor(
					5,
					20,
					40,
					TimeUnit.SECONDS,
					new java.util.concurrent.LinkedBlockingQueue<Runnable>(
							ApplicationContext
									.getApplicationContext()
									.getIntProperty(
											"component.asynccallback.block.size",
											200) / 2), new CallerRunsPolicy());
		} else {
			String timeUnit = proMap.getString("timeUnit", "TimeUnit.SECONDS");
			TimeUnit timeUnit_ = TimeUtil.getTimeUnitByName(timeUnit, TimeUnit.SECONDS);

			callbackexecutor = new java.util.concurrent.ThreadPoolExecutor(
					proMap.getInt("corePoolSize", 5),
					proMap.getInt("maximumPoolSize", 20),
					proMap.getInt("keepAliveTime", 40),
					timeUnit_,
					new java.util.concurrent.LinkedBlockingQueue<Runnable>(
							ApplicationContext
									.getApplicationContext()
									.getIntProperty(
											"component.asynccallback.block.size",
											200) / 2), new CallerRunsPolicy());
		}
	}

	public boolean started() {
		return this.started;
	}

	public void putCallService(CallService callService)
			throws InterruptedException {
		callblockqueue.put(callService);
	}

	public void putCallBackService(CallService callService)
			throws InterruptedException {
		this.callbackblockqueue.put(callService);
	}

	public Object runCallService(CallService task) throws Exception {
		if (!this.started())
			throw new AsyncCallException("异步调用服务已经被停止，拒绝接受新的异步服务调用");
		if (task != null) {
			try {
				/**
				 * 如果需要返回结果，那么检测是否回调方式，如果不是回调方式，那么采用阻塞主线程模式，如果是采用异步调用方式
				 * 如果不需要返回结果，那么直接采用异步调用方式
				 */
				if (task.getAsyncMethod().getAsyncResultMode() == Result.YES) {
					if (task.getAsyncMethod().getAsyncCallback() == null) {
						if (task.getAsyncMethod().getAsyncTimeout() > 0) {
							FutureTask f = new FutureTask(task);
							callexecutor.execute(f);
							return f.get(task.getAsyncMethod()
									.getAsyncTimeout(), TimeUnit.MICROSECONDS);
						} else // 这种不设置超时直接阻塞的方式没有实际意义，与同步调用一致
						{
							FutureTask f = new FutureTask(task);
							callexecutor.execute(f);
							return f.get();
						}
					} else {//有回调函数则异步处理,交给工作队列异步处理
						this.putCallBackService(task);
					}
				} else {//不需要返回值则异步处理，交给工作队列异步处理，防止大并发访问资源被耗尽
					this.putCallService(task);
				}
			} catch (Exception e) {
				throw e;
			}

			return null;
		}
		else 
			throw new AsyncCallException("异步服务调用失败：服务为null");
	}

	
	/**
	 * 不需要返回结果的模式，也没有回调函数，可以指定超时时间，如果没有指定则run方法中同步条用
	 * 否则异步调用，超时时报异常记录日志(这种超时没有特别的意义，只是告诉系统说明服务之星超过摸个时间了)
	 * <p>Title: AsyncCall.java</p> 
	 * <p>Description: </p>
	 * <p>bboss workgroup</p>
	 * <p>Copyright (c) 2007</p>
	 * @Date 2011-4-21 下午03:58:56
	 * @author biaoping.yin
	 * @version 1.0
	 */
	public static class AsynRunnable implements Runnable
	{
		
		CallService task; 
		AsynRunnable(CallService task)
		{
			this.task = task;
		}

		public void run() {
			try {	
				if(task.getAsyncMethod().getAsyncTimeout() <= 0)
				{
					task.call();
				}
				else
				{
					FutureTask t = new FutureTask(task);
					new Thread(t).start();
					t.get(task.getAsyncMethod().getAsyncTimeout(), TimeUnit.MICROSECONDS);
				}
			} catch (Exception e) {				
				log.error(e.getMessage(),e);
			}
		}
	}
	
	public static class AsynCallbackRunnable implements Runnable
	{
		CallService task; 
		AsynCallbackRunnable(CallService task)
		{
			this.task = task;
		}

		public void run() {
			try {
				
				if(task.getAsyncMethod().getAsyncTimeout() <= 0)
				{
					final Object ret = task.call();
					/**
					 * 对调用结果采用异步方式进行回调处理
					 */
						
						new Thread(new Runnable(){
							public void run()
							{
								task.getCallBackService().getCallBack().handleResult(ret);
							}
						}).start();
					
				}
				else
				{
					FutureTask t = new FutureTask(task);
					new Thread(t).start();
					final Object ret = t.get(task.getAsyncMethod().getAsyncTimeout(), TimeUnit.MICROSECONDS);
					/**
					 * 对调用结果采用异步方式进行回调处理
					 */
					new Thread(new Runnable(){
						public void run()
						{
							task.getCallBackService().getCallBack().handleResult(ret);
						}
					}).start();
				}
			} 
			catch (InvocationTargetException e) {			
				
				final Throwable throwable = e.getTargetException();
				new Thread(new Runnable(){
					public void run()
					{
						task.getCallBackService().getCallBack().handleError(throwable);
					}
				}).start();
			}
			catch (Exception e) {			
				
				final Throwable throwable = e;
				new Thread(new Runnable(){
					public void run()
					{
						task.getCallBackService().getCallBack().handleError(throwable);
					}
				}).start();
			}
			
		}
	}
	class CallHandler implements Runnable {

		public void run() {
			CallService callService = null;
			while (true) {

//				if (!AsyncCall.this.started())
//					break;
				try {
					
						callService = callblockqueue.take();
						callexecutor.execute(new AsynRunnable(callService));
					
					
				} catch (Exception e) {
					log.error(e.getMessage(),e);
					continue;
				}

			}

		}

	}

	class CallBackHandler implements Runnable {

		public void run() {
			CallService callService = null;
			while (true) {
//				if (!AsyncCall.this.started())
//					break;
				try {
					callService = callbackblockqueue.take();
					callbackexecutor.execute(new AsynCallbackRunnable(callService));
					
				} catch (Exception e) {
					log.error(e.getMessage(),e);
					continue;
				}

			}
		}

	}

}
