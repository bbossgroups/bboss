package org.frameworkset.task;

import java.io.Serializable;
import java.text.ParseException;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

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
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			log.error(e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			log.error(e);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		
		catch (ParseException ex1) {
			log.error(ex1);
	    } catch (SchedulerException ex) {
	    	log.error(ex);
	    }

	}

	public void updateJob(Scheduler scheduler, SchedulejobInfo jobInfo) {
		// TODO Auto-generated method stub

	}

	public void updateTriger(Scheduler scheduler, SchedulejobInfo jobInfo) {
		// TODO Auto-generated method stub

	}

	public void updateJobAndTriger(Scheduler scheduler, SchedulejobInfo jobInfo) {
		this.taskservice.deleteJob(jobInfo.getId(),jobInfo.getParent().getId());
		startExecuteJob( scheduler, jobInfo) ;
	}

}
