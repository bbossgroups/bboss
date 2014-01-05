package org.frameworkset.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.frameworkset.spi.assemble.Pro;
import org.frameworkset.spi.assemble.ProList;
import org.frameworkset.spi.assemble.ProMap;
import org.quartz.Calendar;
import org.quartz.JobDetail;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerListener;
import org.quartz.TriggerListener;
import org.quartz.impl.SchedulerRepository;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 
 * <p>Title: TaskService.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-3-26 下午02:04:47
 * @author biaoping.yin,gao.tang
 * @version 1.0
 */
public class TaskService implements Service {
	private static Logger log = Logger.getLogger(ScheduleService.class);

	
	private Scheduler scheduler = null;
	private static ScheduleRepository scheduleRepository = new ScheduleRepository();
	private boolean exposeSchedulerInRepository = false;
//	private static TaskService taskService = null;
	

	private BaseApplicationContext taskContext;

	private boolean started = false;
	private String taskconfig;

	private Map<String, ScheduleServiceInfo> schedulerServiceIndex = new HashMap<String, ScheduleServiceInfo>();

	private TaskService(String taskconfig) {
		this.taskconfig = taskconfig == null?ScheduleRepository.taskconfig:taskconfig;
	}
	static 
	{
		BaseApplicationContext.addShutdownHook(new ShutdownThread(),1000);
	}
	static class ShutdownThread extends Thread {
//		TaskService taskService;

		public ShutdownThread() {
//			this.taskService = taskService;
		}

		public void run() {
//			if (taskService != null)
//				taskService.stopService();
			if(scheduleRepository != null)
			{
				scheduleRepository.stopTaskServices();
				scheduleRepository = null;
			}
		}
	}

	static Object lock = new Object();

	public static TaskService getTaskService() {
//		if (taskService != null)
//			return taskService;
//		synchronized (lock) {
//			if (taskService != null)
//				return taskService;
//			taskService = new TaskService();
//		}
//		return taskService;configfile
		return getTaskService(null);
	}
	public static TaskService getTaskService(String configfile) {
//		if (taskService != null)
//			return taskService;
//		synchronized (lock) {
//			if (taskService != null)
//				return taskService;
//			taskService = new TaskService();
//		}
//		return taskService;
		if(configfile == null || configfile.trim().equals(""))
			configfile = scheduleRepository.taskconfig;
		TaskService taskService = scheduleRepository.getTaskService(configfile);
		if(taskService != null)
			return taskService;
		synchronized(lock)
		{
			taskService = scheduleRepository.getTaskService(configfile);
			if(taskService != null)
				return taskService;
			taskService = new TaskService(configfile);
			scheduleRepository.addTaskService(configfile, taskService);
			
		}
		return taskService;
	}

	private List<ScheduleServiceInfo> getScheduleServiceInfos(Pro taskconfig) {
		ProList<Pro> pl = taskconfig.getList();
		if(pl == null || pl.size() <= 0)
		    return null;
		List<ScheduleServiceInfo> scheduleServiceInfos = new ArrayList<ScheduleServiceInfo>();
		for (Pro pro : pl) {
			ScheduleServiceInfo serviceInfo = getScheduleServiceInfo(pro);
			scheduleServiceInfos.add(serviceInfo);
			this.schedulerServiceIndex.put(serviceInfo.getId(), serviceInfo);
		}
		return scheduleServiceInfos;
	}

	private ScheduleServiceInfo getScheduleServiceInfo(Pro pro) {
		// private String name;
		// private String id;
		// private String clazz;
		// private boolean used = true;
		//		
		// /**
		// * Map<String,SchedulejobInfo>
		// */
		// private Map jobsbyIds = new HashMap();
		// /**
		// * List<SchedulejobInfo>
		// */
		// private List jobs = new ArrayList();
		ScheduleServiceInfo scheduleServiceInfo = new ScheduleServiceInfo();
		scheduleServiceInfo.setName(pro.getName());
		scheduleServiceInfo.setId(pro.getStringExtendAttribute("taskid"));
		scheduleServiceInfo.setClazz(pro.getClazz());
		scheduleServiceInfo.setUsed(pro.getBooleanExtendAttribute("used"));

		setSchedulejobInfos(pro, scheduleServiceInfo);

		return scheduleServiceInfo;
	}

	private void setSchedulejobInfos(Pro pro,
			ScheduleServiceInfo scheduleServiceInfo) {
		ProList<Pro> pm = pro.getList();
//		Set<Map.Entry<String, Pro>> set = pm.entrySet();
//		Iterator<Map.Entry<String, Pro>> it = set.iterator();
//		Map<String, SchedulejobInfo> jobsbyIds = new HashMap<String, SchedulejobInfo>();
		if(pm != null && pm.size() > 0){
			for (Pro jobPro : pm) {
				SchedulejobInfo jobinfo = getSchedulejobInfo(jobPro);
				scheduleServiceInfo.add(jobinfo);
			}
		}
	}

	private SchedulejobInfo getSchedulejobInfo(Pro jobPro) {
		// private ScheduleServiceInfo parent;
		//		
		// private String name;
		// private String id;
		// private String clazz;
		// private boolean used = true;
		// private String cronb_time ;
		// /**
		// * Map<String,String>
		// */
		// private Map parameters = new HashMap();
		SchedulejobInfo jobinfo = new SchedulejobInfo();
		jobinfo.setName(jobPro.getName());
		jobinfo.setId(jobPro.getStringExtendAttribute("jobid"));
		jobinfo.setClazz(jobPro.getStringExtendAttribute("action"));
		jobinfo.setUsed(jobPro.getBooleanExtendAttribute("used"));
		jobinfo.setBeanName(jobPro.getStringExtendAttribute("bean-name"));
		jobinfo.setBeanClass(jobPro.getStringExtendAttribute("bean-class"));
		jobinfo.setMethod(jobPro.getStringExtendAttribute("method"));
		jobinfo.setMethodConstruction(jobPro.getConstruction());
		jobinfo.setShouldRecover(jobPro.getBooleanExtendAttribute("shouldRecover",false));
		jobinfo.setCronb_time(jobPro.getStringExtendAttribute("cronb_time"));
		jobinfo.setJobPro(jobPro);
		setParameters(jobPro, jobinfo);
		return jobinfo;
	}

	private void setParameters(Pro jobPro, SchedulejobInfo jobinfo) {
		ProMap parameters = jobPro.getMap();
		if(parameters == null || parameters.size() <= 0)
		    return;
		Set<Map.Entry<String, Pro>> set = parameters.entrySet();
		Iterator<Map.Entry<String, Pro>> it = set.iterator();
		Map<String, SchedulejobInfo> jobsbyIds = new HashMap<String, SchedulejobInfo>();
		while (it.hasNext()) {
			Map.Entry<String, Pro> entry = it.next();
			String name = entry.getKey();
			Pro parameter = entry.getValue();

			jobinfo.addParameter(name, parameter.getTrueValue());
		}
	}
	private void buildCalender(Pro pro) throws Exception
	{
		Calendar calendar = null;
		Object value = pro.getObject();
		String name = pro.getName();
		if(value instanceof String)
		{
			calendar = CalendarBuilderUtil.calendarBuilder((String)value);
		}
		else if(value instanceof BaseCalendarBuilder)
		{
			name = ((BaseCalendarBuilder)value).getCalendarName();
			calendar = ((BaseCalendarBuilder)value).buildCalendar();
		}
		scheduler.addCalendar(name,calendar,false,false);
	}
	public synchronized void startService() {

		if (started)
			return;
		taskContext = DefaultApplicationContext.getApplicationContext(taskconfig);
		Pro taskconfig = taskContext.getProBean("taskconfig");
		
		if (!taskconfig.getBooleanExtendAttribute("enable")) {
			log.debug("Scheduler not enable.");
			return;
		}
		try {
			ProMap quartz = taskContext.getMapProperty("quartz.config");
			
			if(quartz == null || quartz.size() == 0)
			{
				SchedulerFactory factory = new StdSchedulerFactory();
				scheduler = createScheduler(factory,null);
			}
			else
			{
				SchedulerFactory factory = new StdSchedulerFactory();
				this.initSchedulerFactory(factory, quartz);
				scheduler = createScheduler(factory,quartz.getString("org.quartz.scheduler.instanceName"));
			}
			
			ProMap calender = taskContext.getMapProperty("quartz.config.calendar");
			if(calender != null && calender.size() >0)
			{
				Iterator iterator = calender.entrySet().iterator();
				while(iterator.hasNext())
				{
					Map.Entry pro = (Map.Entry)iterator.next();
					try {
						buildCalender((Pro)pro.getValue());
					} catch (Exception e) {
						log.error("加载日历失败!",e);
					}
				}
			}
			ProList schedulerlistener = taskContext.getListProperty("quartz.config.schedulerlistener");
			if(schedulerlistener != null && schedulerlistener.size() >0)
			{
				Iterator iterator = schedulerlistener.iterator();
				while(iterator.hasNext())
				{
					Pro pro = (Pro)iterator.next();
					try {
						scheduler.addSchedulerListener((SchedulerListener)pro.getObject());
					} catch (Exception e) {
						log.error("加载SchedulerListener失败!",e);
					}
				}
			}
			
			
			ProList globaljoblistener = taskContext.getListProperty("quartz.config.globaljoblistener");
			if(globaljoblistener != null && globaljoblistener.size() >0)
			{
				Iterator iterator = globaljoblistener.iterator();
				while(iterator.hasNext())
				{
					Pro pro = (Pro)iterator.next();
					try {
						scheduler.addGlobalJobListener((JobListener)pro.getObject());
					} catch (Exception e) {
						log.error("加载GlobalJobListener失败!",e);
					}
				}
			}
			
			
			ProList joblistener = taskContext.getListProperty("quartz.config.joblistener");
			if(joblistener != null && joblistener.size() >0)
			{
				Iterator iterator = joblistener.iterator();
				while(iterator.hasNext())
				{
					Pro pro = (Pro)iterator.next();
					try {
						scheduler.addJobListener((JobListener)pro.getObject());
					} catch (Exception e) {
						log.error("加载JobListener失败!",e);
					}
				}
			}
			
			ProList globaltriggerlistener = taskContext.getListProperty("quartz.config.globaltriggerlistener");
			if(globaltriggerlistener != null && globaltriggerlistener.size() >0)
			{
				Iterator iterator = globaltriggerlistener.iterator();
				while(iterator.hasNext())
				{
					Pro pro = (Pro)iterator.next();
					try {
						scheduler.addGlobalTriggerListener((TriggerListener)pro.getObject());
					} catch (Exception e) {
						log.error("加载GlobalTriggerListener失败!",e);
					}
				}
			}
			ProList triggerlistener = taskContext.getListProperty("quartz.config.triggerlistener");
			if(triggerlistener != null && triggerlistener.size() >0)
			{
				Iterator iterator = triggerlistener.iterator();
				while(iterator.hasNext())
				{
					Pro pro = (Pro)iterator.next();
					try {
						scheduler.addTriggerListener((TriggerListener)pro.getObject());
					} catch (Exception e) {
						log.error("加载TriggerListener失败!",e);
					}
				}
			}
			

			List<ScheduleServiceInfo> scheduleServices = this
					.getScheduleServiceInfos(taskconfig);
			for (int i = 0; scheduleServices != null && i < scheduleServices.size(); i++) {
				ScheduleServiceInfo scheduleServiceInfo = scheduleServices.get(i);
				if (!scheduleServiceInfo.isUsed())
					continue;
	//			String clazz = scheduleServiceInfo.getClazz();
				try {
	//				ScheduleService instance = (ScheduleService) Class.forName(
	//						clazz).newInstance();
	//				instance.init(scheduleServiceInfo);
					ScheduleService instance = scheduleServiceInfo.getScheduleService(this);
					instance.startService(scheduler);
					instance.startupConfigedService(scheduler);
				} catch (Exception e) {
					log.error("Scheduler failed:" + e.getMessage() + ",scheduleServiceInfo : "
							+ scheduleServiceInfo,e);
					continue;
				}
	
			}
			scheduler.start();
			started = true;
			log.debug("Scheduler started.");

		} catch (Exception ex) {
			log.error("Scheduler failed:" + ex.getMessage(),ex);
			return;
		}
		
		

		if (scheduler == null) {
			log.debug("没有启动计划执行引擎，启动任务服务失败!");
			return;

		}
		started = true;

	}
	
	/**
	 * Load and/or apply Quartz properties to the given SchedulerFactory.
	 * @param schedulerFactory the SchedulerFactory to initialize
	 */
	private void initSchedulerFactory(SchedulerFactory schedulerFactory,ProMap quartz)
			throws SchedulerException, IOException {

//		if (!(schedulerFactory instanceof StdSchedulerFactory)) {
//			if (this.configLocation != null || this.quartzProperties != null ||
//					this.taskExecutor != null || this.dataSource != null) {
//				throw new IllegalArgumentException(
//						"StdSchedulerFactory required for applying Quartz properties: " + schedulerFactory);
//			}
//			// Otherwise assume that no initialization is necessary...
//			return;
//		}
//
//		Properties mergedProps = new Properties();
//		
//
//		if (this.resourceLoader != null) {
//			mergedProps.setProperty(StdSchedulerFactory.PROP_SCHED_CLASS_LOAD_HELPER_CLASS,
//					ResourceLoaderClassLoadHelper.class.getName());
//		}
//
//		if (this.taskExecutor != null) {
//			mergedProps.setProperty(StdSchedulerFactory.PROP_THREAD_POOL_CLASS,
//					LocalTaskExecutorThreadPool.class.getName());
//		}
//		else {
//			// Set necessary default properties here, as Quartz will not apply
//			// its default configuration when explicitly given properties.
//			mergedProps.setProperty(StdSchedulerFactory.PROP_THREAD_POOL_CLASS, SimpleThreadPool.class.getName());
//			mergedProps.setProperty(PROP_THREAD_COUNT, Integer.toString(DEFAULT_THREAD_COUNT));
//		}
//
//		if (this.configLocation != null) {
//			if (logger.isInfoEnabled()) {
//				logger.info("Loading Quartz config from [" + this.configLocation + "]");
//			}
//			PropertiesLoaderUtils.fillProperties(mergedProps, this.configLocation);
//		}
//
//		CollectionUtils.mergePropertiesIntoMap(this.quartzProperties, mergedProps);
//
//		if (this.dataSource != null) {
//			mergedProps.put(StdSchedulerFactory.PROP_JOB_STORE_CLASS, LocalDataSourceJobStore.class.getName());
//		}
//
//		// Make sure to set the scheduler name as configured in the Spring configuration.
//		if (this.schedulerName != null) {
//			mergedProps.put(StdSchedulerFactory.PROP_SCHED_INSTANCE_NAME, this.schedulerName);
//		}
		Properties mergedProps = new Properties();
		Set<String> keys = quartz.keySet();
		if(keys.size() <= 0)
		{
			return ;
		}
		Iterator<String> keys_ = keys.iterator();
		while(keys_.hasNext())
		{
			String key = keys_.next();
			String value = quartz.getString(key);
			if(value != null && !quartz.equals(""))
				mergedProps.put(key, value);
		}
		if(mergedProps.size() > 0)
			((StdSchedulerFactory) schedulerFactory).initialize(mergedProps);
	}
	
	/**
	 * Create the Scheduler instance for the given factory and scheduler name.
	 * Called by {@link #afterPropertiesSet}.
	 * <p>The default implementation invokes SchedulerFactory's <code>getScheduler</code>
	 * method. Can be overridden for custom Scheduler creation.
	 * @param schedulerFactory the factory to create the Scheduler with
	 * @param schedulerName the name of the scheduler to create
	 * @return the Scheduler instance
	 * @throws SchedulerException if thrown by Quartz methods
	 * @see #afterPropertiesSet
	 * @see org.quartz.SchedulerFactory#getScheduler
	 */
	protected Scheduler createScheduler(SchedulerFactory schedulerFactory, String schedulerName)
			throws SchedulerException {

//		// Override thread context ClassLoader to work around naive Quartz ClassLoadHelper loading.
//		Thread currentThread = Thread.currentThread();
//		ClassLoader threadContextClassLoader = currentThread.getContextClassLoader();
//		boolean overrideClassLoader = (this.resourceLoader != null &&
//				!this.resourceLoader.getClassLoader().equals(threadContextClassLoader));
//		if (overrideClassLoader) {
//			currentThread.setContextClassLoader(this.resourceLoader.getClassLoader());
//		}
		if(schedulerName == null)
			return schedulerFactory.getScheduler();
		try {
			SchedulerRepository repository = SchedulerRepository.getInstance();
			synchronized (repository) {
				Scheduler existingScheduler = (schedulerName != null ? repository.lookup(schedulerName) : null);
				Scheduler newScheduler = schedulerFactory.getScheduler();
				if (newScheduler == existingScheduler) {
					throw new IllegalStateException("Active Scheduler of name '" + schedulerName + "' already registered " +
							"in Quartz SchedulerRepository. Cannot create a new bboss-managed Scheduler of the same name!");
				}
				if (!this.exposeSchedulerInRepository) {
					// Need to remove it in this case, since Quartz shares the Scheduler instance by default!
					SchedulerRepository.getInstance().remove(newScheduler.getSchedulerName());
				}
				return newScheduler;
			}
		}
		finally {
//			if (overrideClassLoader) {
//				// Reset original thread context ClassLoader.
//				currentThread.setContextClassLoader(threadContextClassLoader);
//			}
		}
	}

	public void startExecuteJob(String groupid, String jobname)
	{
		ScheduleServiceInfo scheduleServiceInfo = schedulerServiceIndex
		.get(groupid);
		if(scheduleServiceInfo == null)
		{
			log.debug("作业组" + groupid + "不存在，忽略作业"+jobname+"启动.");
			return ;
		}
		SchedulejobInfo jobInfo = scheduleServiceInfo.getScheduleService(this).getSchedulejobInfoByID(jobname);
		if(jobInfo == null)
		{
			log.debug("作业组" + groupid + "中不存在对应的作业，忽略作业"+jobname+"启动.");
			return ;
		}
//		if(jobInfo == null)
//			jobInfo = scheduleServiceInfo.getScheduleService().getSchedulejobInfoByID(jobname);
		if(jobInfo != null)
			startExecuteJob( groupid, jobInfo);
		
		
	}
	/**
	 * 启动一个任务
	 * 
	 * @param jobInfo
	 */
	public void startExecuteJob(String groupid, SchedulejobInfo jobInfo) {
		if (!started)
			startService();
		if (scheduler == null) {
			log.debug("没有启动计划执行引擎服务，启动任务服务失败!");
			return;

		}
		ScheduleServiceInfo scheduleServiceInfo = schedulerServiceIndex
				.get(groupid);
		if (scheduleServiceInfo != null && scheduleServiceInfo.isUsed()) {
			jobInfo.setParent(scheduleServiceInfo);			
//			String clazz = scheduleServiceInfo.getClazz();
			try {
//				ScheduleService instance = (ScheduleService) Class.forName(
//						clazz).newInstance();
//				instance.init(scheduleServiceInfo);
				scheduleServiceInfo.getScheduleService(this).startExecuteJob(scheduler, jobInfo);
			}  catch (Exception e) {

				log.error(e.getMessage(),e);

			}
		}
	}
	/**
	 * 更新指定作业组的任务
	 * @param groupid
	 * @param jobname
	 */
	public void updateExecuteJob(String groupid, String jobname) {
		ScheduleServiceInfo scheduleServiceInfo = schedulerServiceIndex
		.get(groupid);
		if(scheduleServiceInfo == null)
		{
			log.debug("作业组" + groupid + "不存在，忽略作业"+jobname+"更新操作.");
			return ;
		}
		SchedulejobInfo jobInfo = scheduleServiceInfo.getScheduleService(this).getSchedulejobInfoByID(jobname);
		if(jobInfo == null)
		{
			log.debug("作业组" + groupid + "中不存在对应的作业，忽略作业"+jobname+"更新操作.");
			return ;
		}
		updateExecuteJob( groupid,  jobInfo);
	}
	
	/**
	 * 更新默认组的作业
	 * @param jobname
	 */
	public void updateExecuteJob(String jobname) {
		updateExecuteJob("default", jobname);
	}

	/**
	 * 更新一个任务项
	 * 
	 * @param jobInfo
	 */
	public void updateExecuteJob(String groupid, SchedulejobInfo jobInfo) {
		// Schedular sch = (Schedular) jobInfo;
		if (!started)
			startService();
		if (scheduler == null) {
			log.debug("没有启动计划执行引擎，启动任务服务失败!");
			return;

		}
		ScheduleServiceInfo scheduleServiceInfo = schedulerServiceIndex
				.get(groupid);
		if (scheduleServiceInfo != null && scheduleServiceInfo.isUsed()) {
			jobInfo.setParent(scheduleServiceInfo);
//			String clazz = scheduleServiceInfo.getClazz();
			try {
//				ScheduleService instance = (ScheduleService) Class.forName(
//						clazz).newInstance();
//				instance.init(scheduleServiceInfo);
				// scheduler.deleteJob(sch.getSchedularID()+"",groupid);
				scheduleServiceInfo.getScheduleService(this).updateJobAndTriger(scheduler, jobInfo);
			}  catch (Exception e) {

				log.error(e.getMessage(),e);

			}
			// catch (SchedulerException e)
			// {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		}
	}

	public void restartService() {
		this.stopService();

		this.startService();

	}

	/**
	 * 恢复所有触发器
	 * 
	 */
	public void resumeAll() {
		if (scheduler == null) {
			log.debug("没有启动计划执行引擎，启动任务服务失败!");
			return;

		}
		try {
			scheduler.resumeAll();
			
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String[] getJobGroupNames(){
	    try
        {
            return scheduler.getJobGroupNames();
        }
        catch (SchedulerException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
	}
	
	public String[] getJobNames(String jgroupId){
	    try
        {
            return scheduler.getJobNames(jgroupId);
        }
        catch (SchedulerException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
	}
	
	public List getCurrentlyExecutingJobs(){
	    try
        {
            return scheduler.getCurrentlyExecutingJobs();
        }
        catch (SchedulerException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
	}

	/**
	 * 恢复一个任务
	 * 
	 * @param name
	 * @param groupid
	 */
	public void resumeJob(String name, String groupid) {
		if (scheduler == null) {
			log.debug("没有启动计划执行引擎，启动任务服务失败!");
			return;

		}

		ScheduleServiceInfo scheduleServiceInfo = schedulerServiceIndex
				.get(groupid);
		if (scheduleServiceInfo != null && scheduleServiceInfo.isUsed()) {
			try {
				scheduler.resumeJob(name, groupid);
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 暂停所有触发器
	 * 
	 */
	public void pauseAll() {
		if (scheduler == null) {
			log.debug("没有启动计划执行引擎，启动任务服务失败!");
			return;

		}
		try {
			scheduler.pauseAll();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 删除一个任务
	 * 
	 * @param jobname
	 * @param groupid
	 */
	public void deleteJob(String jobname, String groupid) {

		if (scheduler == null) {
			log.debug("没有启动计划执行引擎，启动任务服务失败!");
			return;

		}
		// schedulerServiceIndex fIXME schedulerServiceIndex 这个map没有清除
		ScheduleServiceInfo scheduleServiceInfo = schedulerServiceIndex.get(groupid);
		if (scheduleServiceInfo != null && scheduleServiceInfo.isUsed()) {
			try {
				// scheduler.unscheduleJob(jobname, groupid);
				scheduler.deleteJob(jobname, groupid);
//				schedulerServiceIndex.remove(groupid);
			} catch (SchedulerException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 暂停一个任务
	 * 
	 * @param jobname
	 * @param groupid
	 */
	public void pauseJob(String jobname, String groupid) {

		if (scheduler == null) {
			log.debug("没有启动计划执行引擎，启动任务服务失败!");
			return;

		}
		ScheduleServiceInfo scheduleServiceInfo = schedulerServiceIndex
				.get(groupid);
		if (scheduleServiceInfo != null && scheduleServiceInfo.isUsed()) {
			try {
				scheduler.pauseJob(jobname, groupid);
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 取消一个触发器
	 * 
	 * @param triggerName
	 * @param triggerGroup
	 */
	public void unscheduleJob(String triggerName, String triggerGroup) {
		if (scheduler == null) {
			log.debug("没有启动计划执行引擎服务，启动任务服务失败!");
			return;

		}
		ScheduleServiceInfo scheduleServiceInfo = schedulerServiceIndex
				.get(triggerGroup);
		if (scheduleServiceInfo != null && scheduleServiceInfo.isUsed()) {
			try {
				scheduler.unscheduleJob(triggerName, triggerGroup);
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 终止正在执行的触发器
	 * 
	 */
	public void standbyScheduler() {
		if (scheduler == null) {
			log.debug("没有启动计划执行引擎服务，启动任务服务失败!");
			return;

		}
		try {
			scheduler.standby();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 立刻执行一次触发器
	 * 
	 * @param jobName
	 * @param groupName
	 */
	public void triggerJob(String jobName, String groupName) {
		if (scheduler == null) {
			log.debug("没有启动计划执行引擎服务，启动任务服务失败!");
			return;

		}
		ScheduleServiceInfo scheduleServiceInfo = schedulerServiceIndex
				.get(groupName);
		if (scheduleServiceInfo != null && scheduleServiceInfo.isUsed()) {
			try {
				scheduler.triggerJob(jobName, groupName);
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void stopService() {
		stopService(true);
	}
	public void stopService(boolean forceshutdown) {
		if (!started) {
			return;
		}
		try {
			
			this.scheduler.shutdown(forceshutdown);
			Thread.sleep(4000);
			schedulerServiceIndex.clear();
			
			started = false;
			scheduler = null;
			scheduleRepository = null;
			schedulerServiceIndex = null;
			taskContext = null;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 按照传过来的参数立刻执行一次触发器
	 * 
	 * @param jobName
	 * @param groupName
	 * @param parameters
	 */
	public void triggerJob(String jobName, String groupName, Map parameters) {
		if (scheduler == null) {
			log.debug("没有启动计划执行引擎服务，启动任务服务失败!");
			return;

		}
		ScheduleServiceInfo scheduleServiceInfo = schedulerServiceIndex
				.get(groupName);
		if (scheduleServiceInfo != null && scheduleServiceInfo.isUsed()) {
			try {
				JobDetail jobdetail = scheduler
						.getJobDetail(jobName, groupName);
				Map parameters_ = (Map) jobdetail.getJobDataMap().get(
						"parameters");
				Set entrySet = parameters.entrySet();

				for (Iterator it = entrySet.iterator(); it.hasNext();) {
					Map.Entry entry = (Map.Entry) it.next();
					parameters_.put(entry.getKey(), entry.getValue());

				}
				scheduler.triggerJob(jobName, groupName, jobdetail
						.getJobDataMap());

			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		}
	}

}
