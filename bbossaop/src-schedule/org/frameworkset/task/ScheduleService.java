package org.frameworkset.task;

import java.io.Serializable;
import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;
import org.frameworkset.spi.assemble.BeanAccembleHelper;
import org.frameworkset.spi.assemble.MethodInvoker;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

/**
 * 
 * <p>Title: ScheduleService.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-3-26 下午02:04:21
 * @author biaoping.yin,gao.tang
 * @version 1.0
 */
public abstract class ScheduleService implements Serializable{	
	private static Logger log = Logger.getLogger(ScheduleService.class);
	protected ScheduleServiceInfo scheduleServiceInfo;
	protected TaskService taskservice;
	
	public void init(ScheduleServiceInfo scheduleServiceInfo)
	{
		this.scheduleServiceInfo = scheduleServiceInfo;
	}
	
	/**
	 * 装载任务项的
	 * @param scheduler
	 * @throws ScheduleServiceException
	 */
	public abstract void startService(Scheduler scheduler) throws ScheduleServiceException;
	private MethodInvoker buildJobMethod(SchedulejobInfo jobInfo)
	{
		MethodInvoker jobMethod = null; 
		
		if(jobInfo.getBeanName() != null && !jobInfo.getBeanName().equals(""))
		{
//			Pro providerManagerInfo = jobInfo.getJobPro().getApplicationContext().getProBean(jobInfo.getBeanName());
			return BeanAccembleHelper.creatorMethodInvokerByBean(jobInfo.getJobPro(), jobInfo.getBeanName(), jobInfo.getMethod());
			
			
		}
		else
		{
			return BeanAccembleHelper.creatorMethodInvokerByClass(jobInfo.getJobPro(), jobInfo.getBeanClass(), jobInfo.getMethod());
		}
		
	}
	protected void installMethodInvokerJob(Scheduler scheduler,SchedulejobInfo jobInfo) throws InstantiationException, IllegalAccessException,
	ClassNotFoundException, ParseException, SchedulerException
	{
		log.debug("启动作业组["+(jobInfo.getParent() !=null?jobInfo.getParent().getId():"")+"]中的方法作业["+jobInfo.getId()+"]开始。");
		JobDetail jobDetail = new JobDetail(jobInfo.getId(),
                //Scheduler.DEFAULT_GROUP,
				scheduleServiceInfo.getId(),
				MethodInvokerJob.class);
		JobDataMap map = new JobDataMap();
		
		map.put("JobMethod",buildJobMethod(jobInfo));
//		map.put("parameters",jobInfo.getParameters());
		jobDetail.setJobDataMap(map);
		jobDetail.setRequestsRecovery(jobInfo.isShouldRecover());
		//jobDetail.se
		CronTrigger trigger = new CronTrigger(jobInfo.getId(), scheduleServiceInfo.getId());
		
		trigger.setCronExpression(jobInfo.getCronb_time());
		scheduler.scheduleJob(jobDetail,trigger);
		log.debug("启动作业组["+(jobInfo.getParent() !=null?jobInfo.getParent().getId():"")+"]中的方法作业["+jobInfo.getId()+"]完毕。");
	}
	protected void installExecuteJob(Scheduler scheduler,SchedulejobInfo jobInfo) throws InstantiationException, IllegalAccessException,
																		ClassNotFoundException, ParseException, SchedulerException
	{
		log.debug("启动作业组["+(jobInfo.getParent() !=null?jobInfo.getParent().getId():"")+"]中的作业["+jobInfo.getId()+"]开始。");
		JobDetail jobDetail = new JobDetail(jobInfo.getId(),
                //Scheduler.DEFAULT_GROUP,
				scheduleServiceInfo.getId(),
                ExecuteJOB.class);
		JobDataMap map = new JobDataMap();
		Execute instance = (Execute)Class.forName(jobInfo.getClazz() ).newInstance();
		map.put("action",instance);
		map.put("parameters",jobInfo.getParameters());
		jobDetail.setJobDataMap(map);
		jobDetail.setRequestsRecovery(jobInfo.isShouldRecover());
		//jobDetail.se
		CronTrigger trigger = new CronTrigger(jobInfo.getId(), scheduleServiceInfo.getId());
		
		trigger.setCronExpression(jobInfo.getCronb_time());
		scheduler.scheduleJob(jobDetail,trigger);
		log.debug("启动作业组["+(jobInfo.getParent() !=null?jobInfo.getParent().getId():"")+"]中的作业["+jobInfo.getId()+"]完毕。");
	}
	
	public void startupConfigedService(Scheduler scheduler)
	{
		List list  = this.scheduleServiceInfo.getJobs();
		for(int i = 0; list != null && i < list.size(); i ++)
		{			
			try {
//				JobDetail jobDetail = new JobDetail(jobInfo.getId(),
//	                    //Scheduler.DEFAULT_GROUP,
//						scheduleServiceInfo.getId(),
//	                    ExecuteJOB.class);
//				JobDataMap map = new JobDataMap();
//				Execute instance = (Execute)Class.forName(jobInfo.getClazz() ).newInstance();
//				map.put("action",instance);
//				map.put("parameters",jobInfo.getParameters());
//				jobDetail.setJobDataMap(map);
//				jobDetail.setRequestsRecovery(jobInfo.isShouldRecover());
//				//jobDetail.se
//				CronTrigger trigger = new CronTrigger(jobInfo.getId(), scheduleServiceInfo.getId());
//				
//				trigger.setCronExpression(jobInfo.getCronb_time());
//				scheduler.scheduleJob(jobDetail,trigger);
				SchedulejobInfo jobInfo = (SchedulejobInfo)list.get(i);
				if(!jobInfo.isUsed())
					continue;
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
				e.printStackTrace();
				continue;
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
			
			catch (ParseException ex1) {
	            ex1.printStackTrace();
	            continue;
	        } catch (SchedulerException ex) {
	           ex.printStackTrace();
	           continue;
	        }
		} 
	}
	
	public  abstract void startExecuteJob(Scheduler scheduler,SchedulejobInfo jobInfo);
	public  abstract void updateJob(Scheduler scheduler,SchedulejobInfo jobInfo);
	public  abstract void updateTriger(Scheduler scheduler,SchedulejobInfo jobInfo);
    public  abstract void updateJobAndTriger(Scheduler scheduler, SchedulejobInfo jobInfo);
	public boolean isExist(Scheduler scheduler,String groupid,String jobid)
	{
		try {
			Object obj = scheduler.getJobDetail(jobid,groupid);
			if(obj != null)
				return true;
			else
				return false;
		} catch (SchedulerException e) {
			e.printStackTrace();			
			return false;
		}
		
	}

	public SchedulejobInfo getSchedulejobInfoByID(String jobname)
	{		
		return this.scheduleServiceInfo.getSchedulejobInfoByID(jobname);
	}

	
	public TaskService getTaskservice()
	{
	
		return taskservice;
	}

	
	public void setTaskservice(TaskService taskservice)
	{
	
		this.taskservice = taskservice;
	}
}
