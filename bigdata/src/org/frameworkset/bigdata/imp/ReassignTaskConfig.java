package org.frameworkset.bigdata.imp;

import java.util.List;
import java.util.Map;

import org.frameworkset.bigdata.imp.TaskInfo;

public class ReassignTaskConfig implements java.io.Serializable{
	private String jobname;
	private Map<String,List<TaskInfo>> tasks;
	public ReassignTaskConfig() {
		// TODO Auto-generated constructor stub
	}
	public String getJobname() {
		return jobname;
	}
	public void setJobname(String jobname) {
		this.jobname = jobname;
	}
	public Map<String, List<TaskInfo>> getTasks() {
		return tasks;
	}
	public void setTasks(Map<String, List<TaskInfo>> tasks) {
		this.tasks = tasks;
	}

}
