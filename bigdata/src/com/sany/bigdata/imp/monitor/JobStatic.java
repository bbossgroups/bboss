package com.sany.bigdata.imp.monitor;

import java.util.ArrayList;
import java.util.List;

import com.sany.bigdata.imp.TaskInfo;

public class JobStatic implements java.io.Serializable,java.lang.Cloneable{
	/**
	 * -1:未开始
	 * 0:正在执行
	 * 1:执行完毕
	 * 2:执行异常 
	 */
	private int status = - 1;
	private long startTime;
	private long endTime;
	private String config;
	private String errormsg;
	private List<TaskStatus> runtasksInfos;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	 

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	 

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	public List<TaskStatus> getRuntasksInfos() {
		return runtasksInfos;
	}

	public TaskStatus addJobTaskStatic(TaskInfo taskInfo)
	{
		if(runtasksInfos == null)
		{
			runtasksInfos = new ArrayList<TaskStatus>();
		}
		TaskStatus ts = new TaskStatus();
		ts.setStatus(0);
		ts.setTaskInfo(taskInfo.toString());
		runtasksInfos.add(ts);
		return ts;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public void setRuntasksInfos(List<TaskStatus> runtasksInfos) {
		this.runtasksInfos = runtasksInfos;
	}
	
	
}
