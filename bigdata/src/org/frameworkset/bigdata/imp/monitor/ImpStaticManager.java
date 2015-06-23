package org.frameworkset.bigdata.imp.monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.frameworkset.event.Event;
import org.frameworkset.event.EventHandle;
import org.frameworkset.event.EventImpl;
import org.frameworkset.event.EventTarget;
import org.frameworkset.event.EventType;
import org.frameworkset.event.Listener;
import org.frameworkset.remote.EventUtils;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.jgroups.Address;
import org.frameworkset.bigdata.imp.ExecutorJob;
import org.frameworkset.bigdata.imp.HDFSUploadData;
import org.frameworkset.bigdata.imp.TaskConfig;
import org.frameworkset.bigdata.imp.TaskInfo;
import org.frameworkset.bigdata.util.DBHelper;

/**
 * 监控统计
 * @author yinbp
 */

public class ImpStaticManager implements Listener<Object>{
	private static Logger log = Logger.getLogger(ImpStaticManager.class);
	private HashMap<String,JobStatic> localjobstatics = new HashMap<String,JobStatic>();
	private boolean adminasdatanode;
	private Object localjobstaticsLock = new Object();
	private Object adminJobsLock = new Object();
	/**
	 * Map<hostaddress,HostJobs:datanodetimestamp jobname JobStatic>
	 */
	private HashMap<String,HostJobs> monitorAlljobstaticsIdxByHost = new HashMap<String,HostJobs>();//所有容器正在执行的作业
	/**
	 * Map<jobname,Map<hostname,JobStatic>>
	 */
	private HashMap<String,Map<String,JobStatic>> monitorAlljobstaticsIdxByJob = new HashMap<String,Map<String,JobStatic>>();//作业分布服务器情况一览表
	
	/**
	 * Map<jobname,Status(long)>
	 
	 * -1:未开始
	 * 0:正在执行
	 * 1:执行完毕
	 * 2:执行异常
	 */
	private HashMap<String,Integer> monitorAlljobstatusIdxByJob = new HashMap<String,Integer>();//作业分布服务器情况一览表

	@Override
	public void handle(Event<Object> e) {
		final Event<Object> e_ = e;
		new Thread(new Runnable(){
			public void run()
			{
				
				handlecommand(e_);
			}
		}).start();
		
	}
	
	public JobStatic addJobStatic(ExecutorJob job)
	{
		synchronized(localjobstaticsLock)
		{
			JobStatic jobStatic = new JobStatic();
			jobStatic.setConfig(job.getConfig().toString());
			jobStatic.setStatus(0);
			jobStatic.setStartTime(System.currentTimeMillis());
			localjobstatics.put(job.getConfig().getJobname(), jobStatic);
			return jobStatic;
		}
	}
	
	public TaskStatus addJobTaskStatic(JobStatic jobStatic,TaskInfo taskInfo)
	{	
		synchronized(localjobstaticsLock)
		{
			return jobStatic.addJobTaskStatic( taskInfo);
		}
		
		
	}
	private Map<String,JobStatic> cloneStaticData()
	{
		synchronized(localjobstaticsLock)
		{
			Map<String,JobStatic> ret = (Map<String, JobStatic>) this.localjobstatics.clone();
			return ret;
		}
	}
	void handlecommand(Event<Object> e_)
	{
		
		EventType command = e_.getType();
		Address eventsourceaddress = e_.getSourceAddress();
		String sourceaddress = eventsourceaddress.toString();
		
		if(command.equals(HDFSUploadData.hdfs_upload_monitor_request_commond))//处理监控请求，将数据节点作业监控数据返回到监控中心
		{
			log.info("处理监控请求，将数据节点作业监控数据返回到监控中心:"+sourceaddress);
			Map<String,JobStatic> alljobstatics = cloneStaticData();
			HostJobs hostjobstatics = new HostJobs();
			hostjobstatics.setDatanodeTimestamp(System.currentTimeMillis());
			hostjobstatics.setJobs(alljobstatics);
			if(alljobstatics != null && alljobstatics.size() > 0)
			{
				EventTarget target = new EventTarget(eventsourceaddress);
				Event event = new EventImpl(hostjobstatics,HDFSUploadData.hdfs_upload_monitor_response_commond,target);
				EventHandle.sendEvent(event, false);
			}
		}
		else if(command.equals(HDFSUploadData.hdfs_upload_monitor_response_commond))//将响应的监控数据添加到服务端的监控队列中
		{
			/**
			 * Map<hostaddress,Map<jobname,JobStatic>>
			 */
			HostJobs alljobstatics  = (HostJobs)e_.getSource();
			if(alljobstatics.getJobs() == null || alljobstatics.getJobs().size() == 0)
			{
				return;
			}
			log.info("处理监控数据，将数据节点["+sourceaddress+"]作业监控数据添加监控中心监控数据中。");
			synchronized(this.adminJobsLock)
			{
				HostJobs oldalljobstatics  = monitorAlljobstaticsIdxByHost.get(sourceaddress);
				if(oldalljobstatics == null || oldalljobstatics.getDatanodeTimestamp() < alljobstatics.getDatanodeTimestamp())//数据节点数据没有或者最新返回的数据比之前的数据新则更新监控中心的数据
				{
					monitorAlljobstaticsIdxByHost.put(sourceaddress, alljobstatics);
					Iterator<Entry<String, JobStatic>> jobs = alljobstatics.getJobs().entrySet().iterator();
					while(jobs.hasNext())
					{
						Entry<String, JobStatic> entry = jobs.next();
						String jobname = entry.getKey();
						JobStatic  job = entry.getValue();
						Map<String,JobStatic> jobsIdxByHost = monitorAlljobstaticsIdxByJob.get(jobname);
						if(jobsIdxByHost == null)
						{
							jobsIdxByHost = new HashMap<String,JobStatic>();
							jobsIdxByHost.put(sourceaddress, job);
							monitorAlljobstaticsIdxByJob.put(jobname, jobsIdxByHost);
						}
						else
						{
							jobsIdxByHost.put(sourceaddress, job);
						}
					}
				}
			}
			
		}
		
		
	}

	public Map<String, JobStatic> getLocaljobstatics() {
		return cloneStaticData();
	}

	public Map<String, Map<String, JobStatic>> getMonitorAlljobstaticsIdxByHost() {
		synchronized(adminJobsLock)
		{
			return (Map<String, Map<String, JobStatic>>) monitorAlljobstaticsIdxByHost.clone();
		}
	}

	public  Map<String, Map<String, JobStatic>> getMonitorAlljobstaticsIdxByJob(String jobName) {
		synchronized(adminJobsLock)
		{
			return (Map<String, Map<String, JobStatic>>) monitorAlljobstaticsIdxByJob.clone();
		}
	}
	
	private void mergeconfigTabletasks(List<String> names)
	{
		try {
			List<String> dbjobs = DBHelper.getAllJobNames();
			if(dbjobs != null)
			{
				for(String job:dbjobs)
				{
					if(!names.contains(job))
						names.add(job);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public SpecialMonitorObject getSpecialMonitorObject(String jobName)
	{
		
		SpecialMonitorObject job = new SpecialMonitorObject();
		
		synchronized(adminJobsLock)
		{
			
			List<String> names =this.getConfigTasks();
			
			if(monitorAlljobstaticsIdxByJob.size() > 0)
			{
				
				mergeconfigtasks(names,monitorAlljobstaticsIdxByJob.keySet());
			}
			mergeconfigTabletasks(names);
			if(names.size() == 0)
				return null;
			if(jobName == null)
			{
				jobName =  names.get(0);
				
			}
			job.setJobName(jobName);
			
			job.setAllJobNames(names);
			Map<String, JobStatic> jobstatic =  monitorAlljobstaticsIdxByJob.get(jobName);
			if(jobstatic != null)
			{
				jobstatic = (Map<String, JobStatic>)((HashMap<String, JobStatic>)jobstatic).clone();
				job.setJobstaticsIdxByHost(jobstatic);
				job.setStatus( checkstatus(jobstatic));
				
			}
			if(job.getStatus() == -1 || job.getStatus() == 1 || job.getStatus() == 2 || job.getStatus() == 3  )
				job.setCanrun(true);
			TaskConfig config = HDFSUploadData.buildTaskConfig(jobName);
			if(config != null)
			{
				job.setJobdef(config.getJobdef());
				job.setJobconfig(	config.toString());
			}
			
		}
		
		
		return job;
	}
	
	private int checkstatus(Map<String, JobStatic> jobstatic)
	{
		if(jobstatic == null || jobstatic.size() == 0)
			return -1;
		else
		{
			Set<Entry<String, JobStatic>> set = jobstatic.entrySet();
			int status = -1;
			/**
			 * -1:未开始
			 * 0:执行：正在执行
			 * 1:结束：执行完毕
			 * 2:结束：执行失败
			 * 3:结束：部分成功，部分异常
			 * 4:挂起： 部分未开始，其他要么完成要么失败，类似于挂起
			 */
			boolean unrun = false;
			boolean isrun = false;
			boolean success = false;
			boolean failed = false;
			for(Entry<String, JobStatic> entry:set)
			{
				switch(entry.getValue().getStatus())
				{
					case -1:
						unrun = true;break;
					case 0:
						isrun = true;break;
					case 1:
						success = true;break;
					case 2:
						failed = true;break;
					default:
						break;
						
				}
			}
			if(isrun)
				return 0;
			else if(unrun)
			{
				 if(!success && !failed)
					 return -1;
				 else
				 {
					 return 4;
				 }
			}
			else if(success && !failed)
				return 1;
			else if(!success && failed)
				return 2;
			
			else //if(success || failed)
				return 3;
			
				
				
		}
	}
	
	private void mergeconfigtasks(List<String> names, Set<String> keySet) {
		
		for(String config:keySet)
		{
			if(!names.contains(config))
				names.add(config);
		}
		
	}

	private void mergeconfigtasks(List<String> names) {
		List<String> configTasks = getConfigTasks();
		
		
			for(String config:configTasks)
			{
				if(!names.contains(config))
					names.add(config);
			}
		
		
	}

	public List<String> getConfigTasks()
	{
		BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("tasks.xml");
		List<String> tasks = new ArrayList<String>();
		
		tasks.addAll(context.getPropertyKeys());
		return tasks;
	}
	
	/**
	 * 同步所有数据节点的监控数据到监控中心
	 */
	public void synJobStatus()
	{	  
		Event event = new EventImpl("",HDFSUploadData.hdfs_upload_monitor_request_commond);
		EventHandle.sendEvent(event, false);
	}
	
	
	public List<Address> getAllDataNode()
	{
		if(this.adminasdatanode)
			return EventUtils.getRPCAddresses();
		else
			return EventUtils.removeSelf(EventUtils.getRPCAddresses());
	}
	
	public List<String> getAllDataNodeString()
	{
		List<Address> ads = this.getAllDataNode();
		if(ads == null)
			return new ArrayList<String>();
		List<String> ads_str = new ArrayList<String>(ads.size());
		for(Address ad:ads)
		{
			ads_str.add(ad.toString());
		}
		return ads_str;
		
	}
	public String getLocalNode()
	{
		Address locaAddress = EventUtils.getLocalAddress();
		if(locaAddress!=null)
			return locaAddress.toString();
		else
			return null;
		
	}
	 
	public boolean isAdminasdatanode() {
		return adminasdatanode;
	}

	public void setAdminasdatanode(boolean adminasdatanode) {
		this.adminasdatanode = adminasdatanode;
	}

}
