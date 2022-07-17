package org.frameworkset.task;

import com.frameworkset.util.StringUtil;
import com.frameworkset.util.ValueObjectUtil;
import org.frameworkset.spi.assemble.BeanAccembleHelper;
import org.frameworkset.spi.assemble.MethodInvoker;
import org.quartz.*;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.impl.triggers.AbstractTrigger;
import org.quartz.impl.triggers.CalendarIntervalTriggerImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.quartz.JobKey.jobKey;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.impl.matchers.KeyMatcher.keyEquals;

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
	private static Logger log = LoggerFactory.getLogger(ScheduleService.class);
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
		if(jobInfo.getBean() != null){
			return BeanAccembleHelper.creatorMethodInvokerByBean(jobInfo.getBean(), jobInfo.getMethod());
		}
		else if(jobInfo.getBeanName() != null && !jobInfo.getBeanName().equals(""))
		{
//			Pro providerManagerInfo = jobInfo.getJobPro().getApplicationContext().getProBean(jobInfo.getBeanName());
			return BeanAccembleHelper.creatorMethodInvokerByBean(jobInfo.getJobPro(), jobInfo.getBeanName(), jobInfo.getMethod());
			
			
		}

		else
		{
			return BeanAccembleHelper.creatorMethodInvokerByClass(jobInfo.getJobPro(), jobInfo.getBeanClass(), jobInfo.getMethod());
		}
		
	}
	protected void installExecuteJob(Scheduler scheduler,SchedulejobInfo jobInfo) throws Exception
	{
		log.info("启动作业组["+(jobInfo.getParent() !=null?jobInfo.getParent().getId():"")+"]中的作业["+jobInfo.getId()+"]开始。");
//		JobDetail jobDetail = new JobDetail(jobInfo.getId(),
//				//Scheduler.DEFAULT_GROUP,
//				scheduleServiceInfo.getId(),
//				ExecuteJOB.class);
		JobDetail jobDetail = null;
		JobBuilder jobBuilder = JobBuilder.newJob();
		jobBuilder.withIdentity(jobInfo.getId(),
				//Scheduler.DEFAULT_GROUP,
				scheduleServiceInfo.getId()).ofType(ExecuteJOB.class);
		String joblistenername = jobInfo.getJobPro().getStringExtendAttribute("joblistenername");
		if(!StringUtil.isEmpty(joblistenername) )
		{
			String[] joblistenernames = joblistenername.split("\\,");
			ListenerManager listenerManager = scheduler.getListenerManager();
			for(int i = 0; i < joblistenernames.length; i ++)
			{
				JobListener l = (JobListener)Class.forName(joblistenernames[i].trim()).newInstance();

				listenerManager.addJobListener(l,keyEquals(jobKey(jobInfo.getId(),scheduleServiceInfo.getId())));
//				jobDetail.addJobListener(joblistenernames[i]);
			}
//			for(int i = 0; i < joblistenernames.length; i ++)
//			{
//				jobDetail.addJobListener(joblistenernames[i]);
//			}

		}
		JobDataMap map = new JobDataMap();
		Execute instance = (Execute)Class.forName(jobInfo.getClazz() ).newInstance();
		map.put("action",instance);
		map.put("parameters",jobInfo.getParameters());
		jobBuilder.setJobData(map);
//		jobDetail.setJobDataMap(map);
		jobBuilder.requestRecovery(jobInfo.isShouldRecover());
//		jobDetail.setRequestsRecovery(jobInfo.isShouldRecover());
		//jobDetail.se


//		String volatility = jobInfo.getJobPro().getStringExtendAttribute("volatility");
//		if(!StringUtil.isEmpty(volatility))
//		{
//			jobDetail.setVolatility(Boolean.parseBoolean(volatility));
//		}
		String description = jobInfo.getJobPro().getStringExtendAttribute("description");
		if(!StringUtil.isEmpty(description))
		{
//			jobDetail.setDescription(description);
			jobBuilder.withDescription(description);
		}

		String durability = jobInfo.getJobPro().getStringExtendAttribute("durability");
		if(!StringUtil.isEmpty(description))
		{
//			jobDetail.setDurability(Boolean.parseBoolean(durability));
			jobBuilder.storeDurably(Boolean.parseBoolean(durability));
		}


		Trigger trigger = this.buildTrigger(scheduler,jobInfo);
		jobDetail = jobBuilder.build();
		scheduler.scheduleJob(jobDetail,trigger);
		log.info("启动作业组["+(jobInfo.getParent() !=null?jobInfo.getParent().getId():"")+"]中的作业["+jobInfo.getId()+"]完毕。");
	}

	protected void installMethodInvokerJob(Scheduler scheduler,SchedulejobInfo jobInfo) throws Exception
	{
		log.info("启动作业组["+(jobInfo.getParent() !=null?jobInfo.getParent().getId():"")+"]中的方法作业["+jobInfo.getId()+"]开始。");
//		JobDetail jobDetail = new JobDetail(jobInfo.getId(),
//                //Scheduler.DEFAULT_GROUP,
//				scheduleServiceInfo.getId(),
//				MethodInvokerJob.class);
		JobDetail jobDetail = null;
		JobBuilder jobBuilder = JobBuilder.newJob();
		jobBuilder.withIdentity(jobInfo.getId(),
				//Scheduler.DEFAULT_GROUP,
				scheduleServiceInfo.getId()).ofType(MethodInvokerJob.class);



		JobDataMap map = new JobDataMap();
		
		map.put("JobMethod",buildJobMethod(jobInfo));
		jobBuilder.setJobData(map);
		jobBuilder.requestRecovery(jobInfo.isShouldRecover());
		String volatility = jobInfo.getJobPro().getStringExtendAttribute("volatility");
//		if(!StringUtil.isEmpty(volatility))
//		{
//			jobDetail.setVolatility(Boolean.parseBoolean(volatility));
//		}
		String description = jobInfo.getJobPro().getStringExtendAttribute("description");
		if(!StringUtil.isEmpty(description))
		{
//			jobDetail.setDescription(description);
			jobBuilder.withDescription(description);
		}
		String durability = jobInfo.getJobPro().getStringExtendAttribute("durability");
		if(!StringUtil.isEmpty(description))
		{
			jobBuilder.storeDurably(Boolean.parseBoolean(durability));
//			jobDetail.setDurability(Boolean.parseBoolean(durability));
		}
		jobDetail = jobBuilder.build();
		Trigger trigger = this.buildTrigger(scheduler,jobInfo);
		String joblistenername = jobInfo.getJobPro().getStringExtendAttribute("joblistenername");
		if(!StringUtil.isEmpty(joblistenername) )
		{
			String[] joblistenernames = joblistenername.split("\\,");
			ListenerManager listenerManager = scheduler.getListenerManager();
			for(int i = 0; i < joblistenernames.length; i ++)
			{
				JobListener l = (JobListener)Class.forName(joblistenernames[i].trim()).newInstance();

				listenerManager.addJobListener(l,keyEquals(jobKey(jobInfo.getId(),scheduleServiceInfo.getId())));
//				jobDetail.addJobListener(joblistenernames[i]);
			}

		}

		scheduler.scheduleJob(jobDetail,trigger);
		log.info("启动作业组["+(jobInfo.getParent() !=null?jobInfo.getParent().getId():"")+"]中的方法作业["+jobInfo.getId()+"]完毕。");
	}
	protected IntervalUnit getIntervalUnit(String intervalUnit)
	{
		
		IntervalUnit temp = IntervalUnit.valueOf(intervalUnit);
		if(temp == null)
			return IntervalUnit.DAY;
		else
			return temp;
	}
	protected Trigger buildTrigger(Scheduler scheduler,SchedulejobInfo jobInfo) throws Exception
	{
		String triggertype = jobInfo.getJobPro().getStringExtendAttribute("trigger", "cron");
		Trigger rtrigger = null;
		String calendar = jobInfo.getJobPro().getStringExtendAttribute("calendar");


		if(triggertype.equals("cron"))
		{

//			CronTrigger trigger = new CronTrigger(jobInfo.getId(), scheduleServiceInfo.getId());
			CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(jobInfo.getCronb_time());
			String timeZone = jobInfo.getJobPro().getStringExtendAttribute("timeZone");

			if(!StringUtil.isEmpty(timeZone) )
			{
				cronScheduleBuilder.inTimeZone(TimeZone.getTimeZone(timeZone));
			}
			Trigger trigger = null;
			if(!StringUtil.isEmpty(calendar))
			{
				trigger = newTrigger()
						.withIdentity(jobInfo.getId(), scheduleServiceInfo.getId())
						.withSchedule(cronScheduleBuilder).modifiedByCalendar(calendar)
						.build();
			}
			else
			{
				trigger = newTrigger()
						.withIdentity(jobInfo.getId(), scheduleServiceInfo.getId())
						.withSchedule(cronScheduleBuilder)
						.build();
			}
			rtrigger = trigger; 
		}
		else if(triggertype.equals("simple"))
		{
			String s_startTime = jobInfo.getJobPro().getStringExtendAttribute("startTime");
			Date startTime = null;
			if(!StringUtil.isEmpty(s_startTime) )
			{
				SimpleDateFormat format = ValueObjectUtil.getDefaultDateFormat();
				startTime = format.parse(s_startTime);
			}
			else
				startTime = new Date();
			
				
			String s_endTime = jobInfo.getJobPro().getStringExtendAttribute("endTime");
	        Date endTime = null; 
	        if(!StringUtil.isEmpty(s_endTime) )
			{
				SimpleDateFormat format = ValueObjectUtil.getDefaultDateFormat();
				endTime = format.parse(s_endTime);
			}
	        String s_repeatCount = jobInfo.getJobPro().getStringExtendAttribute("repeatCount");
	        int repeatCount = -1; 
	        if(!StringUtil.isEmpty(s_repeatCount) )
			{
	        	repeatCount = Integer.parseInt(s_repeatCount);
			}
	        String s_repeatInterval = jobInfo.getJobPro().getStringExtendAttribute("repeatInterval");
	        long repeatInterval = 0;
	        if(!StringUtil.isEmpty(s_repeatInterval) )
			{
	        	repeatInterval = Long.parseLong(s_repeatInterval);
			}
			SimpleTriggerImpl simpletrigger = new SimpleTriggerImpl(jobInfo.getId(), scheduleServiceInfo.getId());
			simpletrigger.setStartTime(startTime);
			simpletrigger.setEndTime(endTime);
			simpletrigger.setRepeatCount(repeatCount);
			simpletrigger.setRepeatInterval(repeatInterval);
			if(!StringUtil.isEmpty(calendar))
				simpletrigger.setCalendarName(calendar);

//			SimpleTrigger simpletrigger = new SimpleTrigger(jobInfo.getId(), scheduleServiceInfo.getId(),  startTime,
//		             endTime,  repeatCount,  repeatInterval);
			rtrigger = simpletrigger;
		}
		else if(triggertype.equals("DateInterval"))
		{
			String s_startTime = jobInfo.getJobPro().getStringExtendAttribute("startTime");
			Date startTime = null;
			if(!StringUtil.isEmpty(s_startTime) )
			{
				SimpleDateFormat format = ValueObjectUtil.getDefaultDateFormat();
				startTime = format.parse(s_startTime);
			}
			else
				startTime = new Date();
				
			String s_endTime = jobInfo.getJobPro().getStringExtendAttribute("endTime");
	        Date endTime = null; 
	        if(!StringUtil.isEmpty(s_endTime) )
			{
				SimpleDateFormat format = ValueObjectUtil.getDefaultDateFormat();
				endTime = format.parse(s_endTime);
			}
			IntervalUnit intervalUnit = null;
			String s_intervalUnit = jobInfo.getJobPro().getStringExtendAttribute("intervalUnit");
	        
	        if(!StringUtil.isEmpty(s_intervalUnit) )
			{
	        	intervalUnit = getIntervalUnit(s_intervalUnit);
			}
			else
				intervalUnit = IntervalUnit.SECOND;
	        
			String s_repeatInterval = jobInfo.getJobPro().getStringExtendAttribute("repeatInterval");
	        int repeatInterval = 0;
	        if(!StringUtil.isEmpty(s_repeatInterval) )
			{
	        	repeatInterval = Integer.parseInt(s_repeatInterval);
			}
//			CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withInterval(repeatInterval,intervalUnit);
			CalendarIntervalTriggerImpl dateIntervalTrigger = new CalendarIntervalTriggerImpl(jobInfo.getId(), scheduleServiceInfo.getId(), startTime,
		            endTime,  intervalUnit,   repeatInterval);
			if(!StringUtil.isEmpty(calendar))
				dateIntervalTrigger.setCalendarName(calendar);
			rtrigger = dateIntervalTrigger;
		}
		else if(triggertype.equals("NthIncludedDay"))
		{
			NthIncludedDayTrigger NthIncludedDayTrigger = new NthIncludedDayTrigger(jobInfo.getId(), scheduleServiceInfo.getId());
			String s_startTime = jobInfo.getJobPro().getStringExtendAttribute("startTime");
			Date startTime = null;
			if(!StringUtil.isEmpty(s_startTime) )
			{
				SimpleDateFormat format = ValueObjectUtil.getDefaultDateFormat();
				startTime = format.parse(s_startTime);
			}
			else
				startTime = new Date();
			NthIncludedDayTrigger.setStartTime(startTime);
			
				
			String s_endTime = jobInfo.getJobPro().getStringExtendAttribute("endTime");
	        Date endTime = null; 
	        if(!StringUtil.isEmpty(s_endTime) )
			{
				SimpleDateFormat format = ValueObjectUtil.getDefaultDateFormat();
				endTime = format.parse(s_endTime);
			}
	        NthIncludedDayTrigger.setEndTime(endTime);
	        String fireAtTime = jobInfo.getJobPro().getStringExtendAttribute("fireAtTime");
			//HH:MM[:SS]
			if(!StringUtil.isEmpty(fireAtTime) )
			{
				NthIncludedDayTrigger.setFireAtTime(fireAtTime);
			}
			
			String s_intervalType = jobInfo.getJobPro().getStringExtendAttribute("intervalType");
				
			if(!StringUtil.isEmpty(s_intervalType) )
			{
				if(s_intervalType.equals("MONTHLY"))
					NthIncludedDayTrigger.setIntervalType(NthIncludedDayTrigger.INTERVAL_TYPE_MONTHLY);
				else if(s_intervalType.equals("WEEKLY"))
					NthIncludedDayTrigger.setIntervalType(NthIncludedDayTrigger.INTERVAL_TYPE_WEEKLY);
				else if(s_intervalType.equals("YEARLY"))
					NthIncludedDayTrigger.setIntervalType(NthIncludedDayTrigger.INTERVAL_TYPE_YEARLY);
			}
			String s_N = jobInfo.getJobPro().getStringExtendAttribute("N");
				
			if(!StringUtil.isEmpty(s_N) )
			{
				NthIncludedDayTrigger.setN(Integer.parseInt(s_N));
			}
			String timeZone = jobInfo.getJobPro().getStringExtendAttribute("timeZone");
			
			if(!StringUtil.isEmpty(timeZone) )
			{
				NthIncludedDayTrigger.setTimeZone(TimeZone.getTimeZone(timeZone));
			}
			if(!StringUtil.isEmpty(calendar))
				NthIncludedDayTrigger.setCalendarName(calendar);
			rtrigger = NthIncludedDayTrigger;
//			setNextFireCutoffInterval(int)
		}
		else if(triggertype.equals("builder"))
		{
			String triggerbuilder_bean = jobInfo.getJobPro().getStringExtendAttribute("triggerbuilder-bean");
			String triggerbuilder_class = jobInfo.getJobPro().getStringExtendAttribute("triggerbuilder-class");
			if(!StringUtil.isEmpty(triggerbuilder_bean))
			{
				TriggerBuilder rtrigger_builder = jobInfo.getJobPro().getApplicationContext().getTBeanObject(triggerbuilder_bean,TriggerBuilder.class);
				rtrigger = rtrigger_builder.builder(jobInfo);
				if(!StringUtil.isEmpty(calendar) && rtrigger instanceof AbstractTrigger)
					((AbstractTrigger)rtrigger).setCalendarName(calendar);
			}
			else if(!StringUtil.isEmpty(triggerbuilder_class))
			{
				try {
					TriggerBuilder rtrigger_builder = (TriggerBuilder) Class.forName(triggerbuilder_class).newInstance();
					rtrigger = rtrigger_builder.builder(jobInfo);
				} catch (Exception e) {
					
					throw e;
				}
			}
		}
			
		String triggerlistenername = jobInfo.getJobPro().getStringExtendAttribute("triggerlistenername");
		if(!StringUtil.isEmpty(triggerlistenername) )
		{
			String[] triggerlistenernames = triggerlistenername.split("\\,");
			ListenerManager listenerManager = scheduler.getListenerManager();
			for(int i = 0; i < triggerlistenernames.length; i ++)
			{
				TriggerListener l = (TriggerListener)Class.forName(triggerlistenernames[i].trim()).newInstance();
				listenerManager.addTriggerListener(l,keyEquals(TriggerKey.triggerKey(jobInfo.getId(),scheduleServiceInfo.getId())));
//				rtrigger.addTriggerListener(triggerlistenernames[i]);
			}
			
		}

		return rtrigger;
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
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage(),e);
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
			Object obj = scheduler.getJobDetail(new JobKey(jobid,groupid));
			if(obj != null)
				return true;
			else
				return false;
		} catch (SchedulerException e) {
			log.error(e.getMessage(),e);	
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
