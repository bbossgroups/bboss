package org.frameworkset.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * <p>Title: ScheduleServiceInfo.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-3-26 下午02:04:36
 * @author biaoping.yin,gao.tang
 * @version 1.0
 */
public class ScheduleServiceInfo implements java.io.Serializable {
	private String name;
	private String id;
	private String clazz;
	private boolean used = true;
	private ScheduleService instance;
	
	/**
	 * Map<String,SchedulejobInfo>
	 */
	private Map jobsbyIds = new HashMap();
	/**
	 * List<SchedulejobInfo>
	 */
	private List jobs = new ArrayList();
	public String getClazz() {
		return clazz;
	}
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isUsed() {
		return used;
	}
	public void setUsed(boolean used) {
		this.used = used;
	}
	
	public void add(SchedulejobInfo schedulejobInfo)
	{
		schedulejobInfo.setParent(this);
		this.jobs.add(schedulejobInfo);
		this.jobsbyIds.put(schedulejobInfo.getId(), schedulejobInfo);
	}
//	
	/**
	 * 
	 * @return List<SchedulejobInfo>
	 */
	public List getJobs()
	{
		return this.jobs;
	}
	
	public SchedulejobInfo getSchedulejobInfoByID(String id)
	{
		return (SchedulejobInfo)this.jobsbyIds.get(id);
	}
	
	public String toString(){
		StringBuilder returnVal = new StringBuilder();
		returnVal.append("[name=").append(name).append(",")
			.append("id=").append(id).append(",")
			.append("clazz=").append(clazz).append("]");
		return returnVal.toString();
	} 
	private Object lock = new Object();
	public ScheduleService getScheduleService(TaskService taskService)
	{
		if(instance != null)
			return instance;
		synchronized(lock)
		{
			if(instance!= null)
				return instance;
			
			try
			{
				instance = (ScheduleService) Class.forName(clazz).newInstance();
				instance.init(this);
				instance.setTaskservice(taskService);
			}
			catch (InstantiationException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (ClassNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return instance;
	}
	

}
