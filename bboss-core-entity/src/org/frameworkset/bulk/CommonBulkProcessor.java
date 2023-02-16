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

import org.frameworkset.util.concurrent.ThreadPoolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/12/4 14:13
 * @author biaoping.yin
 * @version 1.0
 */
public class CommonBulkProcessor {
	private Logger logger = LoggerFactory.getLogger(CommonBulkProcessor.class);
	private CommonBulkCommand bulkCommand ;
	private long lastAppendDataTime = -1;
	private Lock r = new ReentrantLock();
	private Lock w = new ReentrantLock();
	public CommonBulkConfig getBulkConfig() {
		return bulkConfig;
	}

	private CommonBulkCommand buildBulkCommand(){

		return new CommonBulkCommand(this);
	}

	private CommonBulkConfig bulkConfig;
	private Flush flush;
	private ExecutorService executor ;

	public BulkAction getBulkAction() {
		return bulkAction;
	}

	private BulkAction bulkAction;

	private boolean inited;
	/**
	 * 0 正常
	 * 1 stop;
	 */
	private int status;
	private  void stop(){
		this.status = 1;
		synchronized (flush) {
			flush.notify();
		}
	}

	public CommonBulkProcessor(CommonBulkConfig bulkConfig){
		this.bulkConfig = bulkConfig;
	}

	public synchronized void increamentTotalsize(int totalSize){
		try {
			r.lock();
			this.totalSize = this.totalSize + totalSize;
		}
		finally {
			r.unlock();
		}

	}
	public void init(){
		if(inited)
			return;
		this.inited = true;
//		clientInterface = ElasticSearchHelper.getRestClientUtil(bulkConfig.getElasticsearch());
		this.bulkAction = bulkConfig.getBulkAction();
		executor = ThreadPoolFactory.buildThreadPool(bulkConfig.getBulkProcessorName(),bulkConfig.getBulkRejectMessage(),
				this.bulkConfig.getWorkThreads(),this.bulkConfig.getWorkThreadQueue(),
				this.bulkConfig.getBlockedWaitTimeout()
				,this.bulkConfig.getWarnMultsRejects());
		this.bulkCommand = buildBulkCommand();

//		dataQueue =  new ArrayBlockingQueue<BulkData>(bulkConfig.getBulkQueue());
		if(bulkConfig.getFlushInterval() > 0) {
			flush = new Flush(bulkConfig.getBulkProcessorName() + "-flush-thread");
			flush.start();
		}
//		ShutdownUtil.addShutdownHook(new Runnable() {
//			@Override
//			public void run() {
//				shutDown();
//			}
//		});
	}

	private boolean touchBatchSize(){

		if(this.bulkCommand != null && this.bulkCommand.getBulkDataSize() >= bulkConfig.getBulkSizes()){
			return true;
		}
		else{
			return false;
		}
	}

	public long getLastAppendDataTime(){
		return lastAppendDataTime;
	}
	private void appendBulkData(CommonBulkData bulkData){
		try {
			w.lock();
			if(bulkCommand == null){
				return;
			}
//			lastAppendDataTime = System.currentTimeMillis();
//			this.bulkCommand.addBulkData(bulkData);
//			if(this.touchBatchSize()){
//				this.execute(true);
//			}

			_appendBulkData( bulkData);


		}
		finally {
			w.unlock();
		}
	}

	private void _appendBulkData(CommonBulkData bulkData){
		lastAppendDataTime = System.currentTimeMillis();
		this.bulkCommand.addBulkData(bulkData);
		if(this.touchBatchSize()){
			this.execute(true);
		}
	}

	private void forceFlush(long flushInterval){
		try {
			w.lock();
			if(bulkCommand == null){
				return;
			}

			long interval = System.currentTimeMillis() - lastAppendDataTime;
			if (interval > flushInterval && bulkCommand.getBulkDataSize() > 0) {
				execute(true);
			}

		}
		finally {
			w.unlock();
		}
	}

	private void forceExecute(){
		try {
			w.lock();

			if (bulkCommand !=null && bulkCommand.getBulkDataSize() > 0) {
				execute(false);
			}

		}
		finally {
			w.unlock();
		}
	}
	/**
	 * @param data
	 */
	public void insertData(Object data){

			assertShutdown();
			CommonBulkData bulkData = new CommonBulkData();
			bulkData.setData(data);
			bulkData.setType(CommonBulkData.INSERT);
			appendBulkData( bulkData);
	}


	/**
	 * ES 1.x,2.x,5.x,6.x,7.x,+
	 * @param data
	 */
	public void updateData(Object data){
//		try {
			assertShutdown();
			CommonBulkData bulkData = new CommonBulkData();
		bulkData.setData(data);
		bulkData.setType(CommonBulkData.UPDATE);
//			this.dataQueue.put(bulkData);
			appendBulkData( bulkData);
//		} catch (InterruptedException e) {
//			logger.info("InterruptedException");
//		}
	}




	/**
	 * ES 1.x,2.x,5.x,6.x,7.x,+
	 * @param data 待删除的文档_id
	 */
	public void deleteData(Object data){
//		try {
			assertShutdown();
			CommonBulkData bulkData = new CommonBulkData();
		bulkData.setData(data);
		bulkData.setType(CommonBulkData.DELETE);
			appendBulkData( bulkData);
//			bulkCommand.addBulkData(bulkData);
//			this.dataQueue.put(bulkData);
//		} catch (InterruptedException e) {
//			logger.info("InterruptedException");
//		}
	}



	private void execute(boolean initBuilCommand){
		executor.submit(bulkCommand);
		if(initBuilCommand)
			bulkCommand = this.buildBulkCommand();
		else{
			bulkCommand = null;
		}
	}


	/**
	 * ES 1.x,2.x,5.x,6.x,7.x,+
	 * @param datas
	 */
	public void insertDatas(List<?> datas){
		if(datas == null || datas.size() == 0)
			return;
		assertShutdown();
		try {
			w.lock();
			if(bulkCommand == null){
				return;
			}
			for(Object data:datas) {
				CommonBulkData bulkData = new CommonBulkData();
				bulkData.setData(data);
				bulkData.setType(CommonBulkData.INSERT);
				_appendBulkData( bulkData);
			}

		}
		finally {
			w.unlock();
		}

	}



	/**
	 * ES 1.x,2.x,5.x,6.x,7.x,+
	 * @param datas
	 */
	public void updateDatas( List<?> datas){
		if(datas == null || datas.size() == 0)
			return;
		assertShutdown();

		try {
			w.lock();
			if(bulkCommand == null){
				return;
			}
			for(Object data:datas) {
				CommonBulkData bulkData = new CommonBulkData();
				bulkData.setData(data);
				bulkData.setType(CommonBulkData.UPDATE);
				_appendBulkData( bulkData);
			}

		}
		finally {
			w.unlock();
		}
	}


	public boolean isShutdown(){
		return this.status == 1;
	}

	public void assertShutdown(){
		if(isShutdown())
			throw new CommonBulkProcessorException("Bulk processor is Shutdown.");
	}
	/**
	 * ES 1.x,2.x,5.x,6.x,7.x,+
	 * @param datas 待删除的文档_id集合
	 */
	public void deleteDatas(List<?> datas){
		if(datas == null || datas.size() == 0){
			return ;
		}
		assertShutdown();
		try {
			w.lock();
			if(bulkCommand == null){
				return;
			}
			for(Object data :datas) {
				CommonBulkData bulkData = new CommonBulkData();
				bulkData.setData(data);
				bulkData.setType(CommonBulkData.DELETE);
				_appendBulkData( bulkData);
			}
		}
		finally {
			w.unlock();
		}

	}



	public long getFailedSize() {
		return failedSize;
	}


	private long totalSize = 0;
	private long failedSize = 0;
	public long getTotalSize() {
		return totalSize;
	}
	public   void increamentFailedSize(int failedSize){
		try {
			r.lock();
			this.failedSize = this.failedSize + failedSize;
		}
		finally {
			r.unlock();
		}
	}

	public int getBulkSizes() {
		return bulkConfig.getBulkSizes();
	}

	/**
	class Worker implements Runnable{

		@Override
		public void run() {
			long pollStartTime = System.currentTimeMillis();
			List<BulkData> batchBulkDatas = null;
			BulkCommand bulkCommand = null;

			int dataSize = 0;
			do {
				try {

					BulkData bulkData = dataQueue.poll(bulkConfig.getPollTimeOut(), TimeUnit.MILLISECONDS);
					if (bulkData != null) {
						pollStartTime = System.currentTimeMillis();
						if(batchBulkDatas == null){
							batchBulkDatas = new ArrayList<BulkData>();
						}
						dataSize ++;//实际记录大小，因为bulkData为collection时，数据大小应该以collection大小之和
						totalSize ++;
						batchBulkDatas.add(bulkData);
						if(dataSize >= bulkConfig.getBulkSizes()){
							bulkCommand = new BulkCommand(batchBulkDatas,BulkProcessor.this);
							batchBulkDatas = null;
							dataSize = 0;
							executor.submit(bulkCommand);
						}
					}
					else{
						boolean forceFlush = false;
						if (bulkConfig.getFlushInterval() > 0) {
							long interval = System.currentTimeMillis() - pollStartTime;
							if (interval > bulkConfig.getFlushInterval()) {
								forceFlush = true;
							}
						}
						if (isShutdown()) {
							forceFlush = true;
						}

						// force flush
						if(forceFlush && dataSize > 0){
							bulkCommand = new BulkCommand(batchBulkDatas,BulkProcessor.this);
							batchBulkDatas = null;
							dataSize = 0;
							pollStartTime = System.currentTimeMillis();
							executor.submit(bulkCommand);
						}
						if(!isShutdown())
							continue;
						else {
							if(logger.isInfoEnabled()){
								logger.info("BulkProcessor process total {} data requests.",totalSize);
							}
							queueEmpty = true;
							synchronized (BulkProcessor.this){
								BulkProcessor.this.notifyAll();
							}

							break;
						}
					}


				} catch (InterruptedException e) {
					break;
				}
			}while(true);
		}
	}
*/
	class Flush extends Thread{
		public Flush(String name){
			super(name);
		}

		@Override
		public void run() {
			long flushInterval = bulkConfig.getFlushInterval();

			while(true) {

				try {
					synchronized (this) {
						wait(flushInterval);
					}
					forceFlush(flushInterval);
					if(!isShutdown()) {
						continue;
					}
					else
					{

						break;
					}



				} catch (InterruptedException e) {
					break;
				}
			}
		}
	}

	/**
	 * 调用shutDown停止方法后，BulkProcessor不会接收新的请求，但是会处理完所有已经进入bulk队列的数据
	 */
	public void shutDown(){
		if(logger.isInfoEnabled())
			logger.info("ShutDown BulkProcessor[{}] begin.....",this.bulkConfig.getBulkProcessorName());
		stop();
//		try{
//			if(flush != null){
//				flush.interrupt();
//			}
//		}
//		catch (Exception e){
//
//		}
		try {
			this.forceExecute();
		}
		catch (Throwable e){

		}
		if(executor != null){
			try {
				if(logger.isInfoEnabled())
					logger.info("ShutDown BulkProcessor["+this.bulkConfig.getBulkProcessorName()+"] thread executor pool  begin......");
				executor.shutdown();
//				if(logger.isInfoEnabled()){
//					logger.info("BulkProcessor process total success records {} failed records {}.",totalSize,failedSize);
//				}
				if(logger.isInfoEnabled())
					logger.info("ShutDown BulkProcessor["+this.bulkConfig.getBulkProcessorName()+"] thread executor pool complete.");
			}
			catch(Exception e){
				if(logger.isErrorEnabled())
					logger.error("ShutDown BulkProcessor["+this.bulkConfig.getBulkProcessorName()+"] thread executor pool failed:",e);
			}
		}
		if(logger.isInfoEnabled())
			logger.info("ShutDown BulkProcessor[{}] complete.",this.bulkConfig.getBulkProcessorName());

	}

}
