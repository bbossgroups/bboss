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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2018/8/29 21:39
 * @author biaoping.yin
 * @version 1.0
 */
public class BlockedRejectedExecutionHandler implements RejectedExecutionHandler {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private AtomicInteger rejectCounts = new AtomicInteger();
	private long blockedWaitTimeout;
	private int warnMultsRejects = 1000;
	private String message;
	public BlockedRejectedExecutionHandler(String message, long blockedWaitTimeout){
		this(message,blockedWaitTimeout,1000);
	}
	public BlockedRejectedExecutionHandler(String message, long blockedWaitTimeout,int warnMultsRejects){
		this.blockedWaitTimeout = blockedWaitTimeout;
		this.message = message;
		this.warnMultsRejects = warnMultsRejects;
	}
	/**
	 * Always log per 1000 mults rejects.
	 * @param r
	 * @param executor
	 */
	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		int counts = rejectCounts.incrementAndGet();
		if(logger.isWarnEnabled()) {
			int t = counts % warnMultsRejects;
			if (t == 0) {
					logger.warn(new StringBuilder().append("Task[")
							.append(message).append("] blocked ")
							.append(counts).append(" times.").toString());
			}
		}
//
		try {
			if(blockedWaitTimeout <= 0L) {
				executor.getQueue().put(r);
			}
			else {
				boolean result = executor.getQueue().offer(r, this.blockedWaitTimeout, TimeUnit.MILLISECONDS);
				if(!result){
					throw new RejectedExecutionException(new StringBuilder().append("Task[").append(message)
							.append("] rejected: wait timeout after ")
							.append(blockedWaitTimeout).append(" MILLISECONDS.").toString());
				}
			}
		} catch (InterruptedException e1) {
			throw new RejectedExecutionException(e1);
		}
	}
}
