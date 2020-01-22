package org.frameworkset.util.concurrent;
/**
 * Copyright 2008 biaoping.yin
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2018/9/4 15:42
 * @author biaoping.yin
 * @version 1.0
 */
public class ThreadPoolFactory {
	public static ExecutorService buildThreadPool(final String threadName,String rejectMessage ,int threadCount,int threadQueue,
												  long blockedWaitTimeout,int warnMultsRejects){
//		ExecutorService executor = Executors.newFixedThreadPool(this.getThreadCount(), new ThreadFactory() {
//			@Override
//			public Thread newThread(Runnable r) {
//				return new DBESThread(r);
//			}
//		});

		return buildThreadPool( threadName,  rejectMessage ,  threadCount,  threadQueue,  blockedWaitTimeout,  warnMultsRejects,false,null);
	}

	public static ExecutorService buildThreadPool(final String threadName,String rejectMessage ,int threadCount,int threadQueue,
												  long blockedWaitTimeout,int warnMultsRejects,boolean preStartAllCoreThreads,final Boolean daemon){

		return buildThreadPool( threadName,  rejectMessage ,  threadCount,  threadQueue,  0L,
		  blockedWaitTimeout,  warnMultsRejects,  preStartAllCoreThreads, daemon);
	}

	public static ExecutorService buildThreadPool(final String threadName,String rejectMessage ,int threadCount,int threadQueue,long keepAliveTime,
												  long blockedWaitTimeout,int warnMultsRejects,boolean preStartAllCoreThreads,final Boolean daemon){
//		ExecutorService executor = Executors.newFixedThreadPool(this.getThreadCount(), new ThreadFactory() {
//			@Override
//			public Thread newThread(Runnable r) {
//				return new DBESThread(r);
//			}
//		});

		ThreadPoolExecutor blockedExecutor = new ThreadPoolExecutor(threadCount, threadCount,
				keepAliveTime, TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(threadQueue),
				new ThreadFactory() {
					private AtomicInteger threadCount = new AtomicInteger(0);

					@Override
					public Thread newThread(Runnable r) {
						int num = threadCount.incrementAndGet();
						return new WorkThread(r,threadName,num,daemon);
					}
				},new BlockedRejectedExecutionHandler(rejectMessage,  blockedWaitTimeout,warnMultsRejects));
		if(preStartAllCoreThreads)
			blockedExecutor.prestartAllCoreThreads();
		return blockedExecutor;
	}

	public static ExecutorService buildThreadPool(final String threadName,String rejectMessage ,int threadCount,int threadQueue,
												  long blockedWaitTimeout,int warnMultsRejects,boolean preStartAllCoreThreads){
		return buildThreadPool( threadName,  rejectMessage ,  threadCount,  threadQueue,  blockedWaitTimeout,  warnMultsRejects,preStartAllCoreThreads,null);
	}

	public static ExecutorService buildThreadPool(final String threadName,String rejectMessage ,int threadCount,
												  int threadQueue,long blockedWaitTimeout){


		return buildThreadPool(threadName,rejectMessage , threadCount,threadQueue,blockedWaitTimeout,1000);
	}
}
