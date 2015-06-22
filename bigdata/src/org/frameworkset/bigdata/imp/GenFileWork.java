package org.frameworkset.bigdata.imp;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.frameworkset.util.SimpleStringUtil;



public class GenFileWork  implements Runnable{
	 private static Logger log = Logger.getLogger(GenFileWork.class);
	BlockingQueue<FileSegment> queue;
	CyclicBarrier barrier;
	java.util.concurrent.atomic.AtomicInteger count; 
	BlockingQueue<FileSegment> upqueue;
	GenFileHelper genFileHelper;
	public GenFileWork(GenFileHelper genFileHelper,AtomicInteger count, BlockingQueue<FileSegment> queue,BlockingQueue<FileSegment> upqueue ,CyclicBarrier barrier)
	{
		this.queue = queue;
		this.barrier = barrier;
		this.count = count;
		this.upqueue = upqueue;
		this.genFileHelper = genFileHelper;
	}
	@Override
	public void run() {
		int i = -1; 
		while(true)
		{
			
			try {			
				FileSegment segment = queue.poll(genFileHelper.getGenquequetimewait(),TimeUnit.SECONDS);
				if(segment == null)
				{
					if(count.get() == 0)
						break;
					 
				}
				else
				{
					count.decrementAndGet();
					
					log.info("Process segment: "+segment);
					WriteDataTask dataTask = new WriteDataTask(genFileHelper,upqueue,segment);
					try {
						segment.genstarttimestamp =  System.currentTimeMillis();
						segment.taskStatus.setStatus(0);
						segment.taskStatus.setTaskInfo(segment.toString());
						dataTask.run();
						if(segment.taskStatus.getStatus() != 2)
							segment.taskStatus.setStatus(1);
						segment.taskStatus.setTaskInfo(segment.toString());
					} catch (Exception e) {
						segment.taskStatus.setStatus(2);
						segment.taskStatus.setTaskInfo(segment.toString());
						segment.taskStatus.setErrorInfo(SimpleStringUtil.exceptionToString(e));
					}
					
					log.info("Process segment ok:"+segment);
					if(count.get() == 0)
						break;
				}
				 
				
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}  
//			try {
//				barrier.await();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				break;
//			} catch (BrokenBarrierException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				break;
//			}
		}
		
	}
}
