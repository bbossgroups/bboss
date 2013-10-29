package org.frameworkset.task;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;

/**
 * 
 * 缺省的任务调度器
 * <p>Title: DefaultScheduleService.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-3-26 下午02:03:35
 * @author biaoping.yin,gao.tang
 * @version 1.0
 */

public class DefaultScheduleService extends ScheduleService implements Serializable{
	private static Logger log = Logger.getLogger(DefaultScheduleService.class);
	public void startService(Scheduler scheduler)
			throws ScheduleServiceException {
		

	}

	public void startExecuteJob(Scheduler scheduler, SchedulejobInfo jobInfo) {
		if(!jobInfo.isUsed())
			return ;
		try
		{
			if(!jobInfo.isMethodInvokerJob())
			{
				installExecuteJob(scheduler,jobInfo);
			}
			else
			{
				installMethodInvokerJob(scheduler,jobInfo);
			}
		} 
		 catch (Exception ex) {
	    	log.error(ex.getMessage(),ex);
	    }

	}

	public void updateJob(Scheduler scheduler, SchedulejobInfo jobInfo) {
		// TODO Auto-generated method stub

	}

	public void updateTriger(Scheduler scheduler, SchedulejobInfo jobInfo) {
		

	}

	public void updateJobAndTriger(Scheduler scheduler, SchedulejobInfo jobInfo) {
		this.taskservice.deleteJob(jobInfo.getId(),jobInfo.getParent().getId());
		startExecuteJob( scheduler, jobInfo) ;
	}

}
