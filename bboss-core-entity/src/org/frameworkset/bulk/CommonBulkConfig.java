package org.frameworkset.bulk;
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

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/12/4 14:16
 * @author biaoping.yin
 * @version 1.0
 */
public class CommonBulkConfig {
	private List<CommonBulkInterceptor> bulkInterceptors;
	private long blockedWaitTimeout;
	private int warnMultsRejects;
	private String bulkProcessorName = "BulkProcessor";
	private String bulkRejectMessage = "Reject bulk processor";

	/**
	 * 失败重试机制
	 */
	private CommonBulkRetryHandler bulkRetryHandler;
	private int retryTimes = 3;
	private long retryInterval = 0L;
	/**
	 * 记录数达到bulkRecords指定的条数时执行一次bulk操作
	 */
	private int bulkSizes;

	/**
	 * 失败重试次数
	 */
	private int bulkFailRetry;
	/**
	 * 强制bulk操作时间，单位毫秒，如果自上次bulk操作flushInterval毫秒后，数据量没有满足
	 */
	private long flushInterval;
//	private int bulkQueue = 10000;
	private int workThreads = 20;
	private int workThreadQueue = 100;

	private BulkAction bulkAction;

	public int getBulkSizes() {
		return bulkSizes;
	}

	public CommonBulkConfig setBulkSizes(int bulkSizes) {
		this.bulkSizes = bulkSizes;
		return this;
	}

	public int getBulkFailRetry() {
		return bulkFailRetry;
	}

	public List<CommonBulkInterceptor> getBulkInterceptors() {
		return bulkInterceptors;
	}

	public CommonBulkConfig setBulkFailRetry(int bulkFailRetry) {
		this.bulkFailRetry = bulkFailRetry;
		return this;
	}

	public long getFlushInterval() {
		return flushInterval;
	}

	public CommonBulkConfig setFlushInterval(long flushInterval) {
		this.flushInterval = flushInterval;
		return this;
	}
//
//	public int getBulkQueue() {
//		return bulkQueue;
//	}
//
//	public BulkConfig setBulkQueue(int bulkQueue) {
//		this.bulkQueue = bulkQueue;
//		return this;
//	}

	public int getWorkThreads() {
		return workThreads;
	}

	public CommonBulkConfig setWorkThreads(int workThreads) {
		this.workThreads = workThreads;
		return this;
	}
	public CommonBulkConfig addBulkInterceptor(CommonBulkInterceptor bulkInterceptor){
		if(bulkInterceptors == null){
			bulkInterceptors = new ArrayList<CommonBulkInterceptor>();
		}
		bulkInterceptors.add(bulkInterceptor);
		return this;
	}

	public int getWorkThreadQueue() {
		return workThreadQueue;
	}

	public CommonBulkConfig setWorkThreadQueue(int workThreadQueue) {
		this.workThreadQueue = workThreadQueue;
		return this;
	}

	public long getBlockedWaitTimeout() {
		return blockedWaitTimeout;
	}

	public CommonBulkConfig setBlockedWaitTimeout(long blockedWaitTimeout) {
		this.blockedWaitTimeout = blockedWaitTimeout;
		return this;
	}

	public int getWarnMultsRejects() {
		return warnMultsRejects;
	}

	public CommonBulkConfig setWarnMultsRejects(int warnMultsRejects) {
		this.warnMultsRejects = warnMultsRejects;
		return this;
	}

	public String getBulkProcessorName() {
		return bulkProcessorName;
	}

	public CommonBulkConfig setBulkProcessorName(String bulkProcessorName) {
		this.bulkProcessorName = bulkProcessorName;
		return this;
	}

	public String getBulkRejectMessage() {
		return bulkRejectMessage;
	}

	public CommonBulkConfig setBulkRejectMessage(String bulkRejectMessage) {
		this.bulkRejectMessage = bulkRejectMessage;
		return this;
	}
//
//	public long getPollTimeOut() {
//		return pollTimeOut;
//	}
//
//	public BulkConfig setPollTimeOut(long pollTimeOut) {
//		this.pollTimeOut = pollTimeOut;
//		return this;
//	}







	public CommonBulkRetryHandler getBulkRetryHandler() {
		return bulkRetryHandler;
	}

	public void setBulkRetryHandler(CommonBulkRetryHandler bulkRetryHandler) {
		this.bulkRetryHandler = bulkRetryHandler;
	}

	public int getRetryTimes() {
		return retryTimes;
	}

	public void setRetryTimes(int retryTimes) {
		this.retryTimes = retryTimes;
	}

	public long getRetryInterval() {
		return retryInterval;
	}

	public void setRetryInterval(long retryInterval) {
		this.retryInterval = retryInterval;
	}


	public BulkAction getBulkAction() {
		return bulkAction;
	}

	public void setBulkAction(BulkAction bulkAction) {
		this.bulkAction = bulkAction;
	}
}
