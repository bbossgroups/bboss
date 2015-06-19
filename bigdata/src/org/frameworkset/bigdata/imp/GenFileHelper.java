package org.frameworkset.bigdata.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

public class GenFileHelper {
	private static Logger log = Logger.getLogger(GenFileHelper.class); 
	List<Thread> genthreads;
	List<Thread> upthreads;
	AtomicInteger genfilecount;
	AtomicInteger upfilecount;
	ExecutorJob job;
	TaskConfig config;
	public boolean genlocalfile()
	{
		return this.config.isGenlocalfile();
	}
	public GenFileHelper(ExecutorJob job) {
		this.job = job;
		this.config = this.job.config;
	}
	public int getGenquequetimewait() {
		return config.getGenquequetimewait();
	}
	 
	public int getUploadqueuetimewait() {
		return config.getUploadqueuetimewait();
	}
	public void countdownupfilecount() {
		this.upfilecount.decrementAndGet();
	}

	public void countdowngenfilecount() {
		this.genfilecount.decrementAndGet();
	}

	public void join() {
		for (Thread thread : genthreads)
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(config.isGenlocalfile())
		{
			for (Thread thread : upthreads)
				try {
					thread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	public void run(TaskConfig config) {
//		CyclicBarrier barrier = new CyclicBarrier(config.getGeneworkthreads());
		genfilecount = new AtomicInteger(config.getTasks().length);

		genthreads = new ArrayList<Thread>(config.getGeneworkthreads());
		for (int i = 0; i < config.getGeneworkthreads(); i++) {
			GenFileWork gw = new GenFileWork(this, genfilecount,
					job.getGenfileQueues(), job.getUpfileQueues(), null);
			log.info("start task handle thread["+config.filebasename+"-"+i+"]");
			Thread thread = new Thread(gw,config.filebasename+"-"+i);
			genthreads.add(thread);
			thread.start();
		}

		if(config.isGenlocalfile())
		{	
			upfilecount = new AtomicInteger(config.getTasks().length);
	
	//		CyclicBarrier upbarrier = new CyclicBarrier(works);
			upthreads = new ArrayList<Thread>(config.getUploadeworkthreads());
			for (int i = 0; i < config.getUploadeworkthreads(); i++) {
				Thread thread = new Thread(new UploadWorker(this,
						job.getFileSystem(), upfilecount, job.getUpfileQueues(),
						null));
				upthreads.add(thread);
				thread.start();
			}
		}

	}

}