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

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/12/7 9:44
 * @author biaoping.yin
 * @version 1.0
 */
public class CommonBulkProcessorBuilder {
	private CommonBulkConfig bulkConfig;
	public CommonBulkProcessorBuilder(){
		bulkConfig = new CommonBulkConfig();
	}
	public CommonBulkProcessorBuilder setBulkRetryHandler(CommonBulkRetryHandler bulkRetryHandler) {
		this.bulkConfig.setBulkRetryHandler( bulkRetryHandler);
		return this;
	}


	public CommonBulkProcessorBuilder setRetryTimes(int retryTimes) {
		this.bulkConfig.setRetryTimes( retryTimes);
		return this;
	}


	public CommonBulkProcessorBuilder setBulkAction(BulkAction bulkAction) {
		this.bulkConfig.setBulkAction( bulkAction);
		return this;
	}


	public CommonBulkProcessorBuilder setRetryInterval(long retryInterval) {
		this.bulkConfig.setRetryInterval( retryInterval);
		return this;
	}


	public CommonBulkProcessorBuilder setBulkSizes(int bulkSizes) {
		bulkConfig.setBulkSizes(bulkSizes);
		return this;
	}


	public CommonBulkProcessorBuilder setBulkFailRetry(int bulkFailRetry) {
		bulkConfig.setBulkFailRetry(bulkFailRetry);
		return this;
	}


	public CommonBulkProcessorBuilder setFlushInterval(long flushInterval) {
		this.bulkConfig.setFlushInterval(flushInterval);
		return this;
	}
	public CommonBulkProcessorBuilder setWorkThreadQueue(int workThreadQueue) {
		this.bulkConfig.setWorkThreadQueue( workThreadQueue);
		return this;
	}

//	public BulkProcessorBuilder setBulkQueue(int bulkQueue) {
//		this.bulkConfig.setBulkQueue(bulkQueue);
//		return this;
//	}


	public CommonBulkProcessorBuilder setWorkThreads(int workThreads) {
		this.bulkConfig.setWorkThreads(workThreads);
		return this;
	}
	public CommonBulkProcessorBuilder addBulkInterceptor(CommonBulkInterceptor bulkInterceptor){
		this.bulkConfig.addBulkInterceptor(bulkInterceptor);
		return this;
	}
	public CommonBulkProcessorBuilder setBlockedWaitTimeout(long blockedWaitTimeout) {
		bulkConfig.setBlockedWaitTimeout(blockedWaitTimeout);
		return this;
	}


	public CommonBulkProcessorBuilder setWarnMultsRejects(int warnMultsRejects) {
		this.bulkConfig.setWarnMultsRejects( warnMultsRejects);
		return this;
	}
	public CommonBulkProcessor build(){
		if(bulkConfig == null || bulkConfig.getBulkAction() == null){
			throw new CommonBulkProcessorException("build BulkProcessor failed:bulkConfig is null or BulkAction is not setted.");
		}
		CommonBulkProcessor bulkProcessor = new CommonBulkProcessor(this.bulkConfig);
		bulkProcessor.init();
		return bulkProcessor;
	}


//	public BulkProcessorBuilder setPollTimeOut(long pollTimeOut) {
//		this.bulkConfig.setPollTimeOut( pollTimeOut);
//		return this;
//	}
	public CommonBulkProcessorBuilder setBulkRejectMessage(String bulkRejectMessage) {
		if(bulkRejectMessage == null)
			bulkRejectMessage = "Reject bulk processor";
		this.bulkConfig.setBulkRejectMessage( bulkRejectMessage);
		return this;
	}
	public CommonBulkProcessorBuilder setBulkProcessorName(String bulkProcessorName) {
		if(bulkProcessorName == null)
			bulkProcessorName = "BulkProcessor";
		this.bulkConfig.setBulkProcessorName( bulkProcessorName);
		return this;
	}


 
}
