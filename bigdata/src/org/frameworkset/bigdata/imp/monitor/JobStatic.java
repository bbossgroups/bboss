package org.frameworkset.bigdata.imp.monitor;

import java.util.ArrayList;
import java.util.List;

import org.frameworkset.bigdata.imp.TaskInfo;

public class JobStatic implements java.io.Serializable,java.lang.Cloneable{
	
	private String jobstaticid ;
	/**
	 * -1:未开始
	 * 0:正在执行
	 * 1:执行完毕
	 * 2:执行异常
	 * 3:强制终止
	 */
	
	private int status = - 1;
	private long startTime;
	private long endTime;
	private String config;
	private String errormsg;
	private List<TaskStatus> runtasksInfos;
	private int totaltasks;
	private int unruntasks;
	private int completetasks;
	private int failtasks;
	private int runtasks;
	private int waittasks;
	private String jobname;
	public long startid;
	public long endid;
	/**
	 * 抽取成功记录数
	 */
	private long successrecords;
	
	/**
	 * 抽取成功记录数
	 */
	private long failerecords;
	private int currentposition;
	private boolean onejob;
	public boolean canstop()
	{
		return this.status == 0 || this.status == -1;
	}
	

	/**
	 * 报错成功作业号，以逗号分隔
	 */
	private List<String> undotaskNos;
	/**
	 * 报错成功作业号，以逗号分隔
	 */
	private String completetaskNos;
	/**
	 * 保存失败作业号，以逗号分隔
	 */
	private String failedtaskNos;
	public void incrementfailtasks()
	{
		this.failtasks ++;
	}
	
	public void incrementcompletetasks()
	{
		this.completetasks ++;
	}
	
	public void incrementunruntasks()
	{
		this.unruntasks ++;
	}
	
	public void eval()
	{
		/**
		 * 重置状态
		 */
		 this.failtasks= 0; 
		 this.completetasks =0;
		 this.unruntasks = 0;
		 runtasks = 0;
		 waittasks = 0;
		 this.successrecords = 0;
		 failerecords = 0;
		 this.completetaskNos = null;
		 this.failedtaskNos = null;
		if(runtasksInfos != null && runtasksInfos.size() > 0)
		{
			StringBuilder success = new StringBuilder();
			StringBuilder failed = new StringBuilder();
			this.unruntasks = this.totaltasks - runtasksInfos.size();
			for(TaskStatus status :runtasksInfos)
			{
				switch(status.getStatus())
				{
					/**
					 * <pg:equal value="-1">未开始</pg:equal>
												<pg:equal value="0">正在运行</pg:equal>
												<pg:equal value="1">执行完毕</pg:equal>
												<pg:equal value="2">执行异常</pg:equal>
												<pg:equal value="3">排队等候</pg:equal>
					 */
					case -1:
						this.unruntasks ++;break;
					case 0:
						this.runtasks ++;break;
					case 1:
						this.completetasks ++;
						if(success.length() > 0)
							success.append(",");
						success.append(status.getTaskNo());
						
						break;	
					case 2:
						this.failtasks ++;
						if(failed.length() > 0)
							failed.append(",");
						failed.append(status.getTaskNo());
						break;
					case 3:
						this.waittasks ++;break;	
				}
				this.successrecords = successrecords+status.getHandlerows();
				this.failerecords = failerecords + status.getErrorrows(); 
			}
			
			
			this.completetaskNos = success.toString();
			this.failedtaskNos = failed.toString();
		}
		else
		{
			this.unruntasks = this.totaltasks;
		}
		waittasks = this.totaltasks - this.failtasks - this.completetasks - this.unruntasks-runtasks;
		
	}
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	public boolean isforceStop()
	{
		return this.status == 3;
	}
	
	public boolean stopped()
	{
		return this.status == 1 || this.status == 2 || this.status == 3;
	}

	 

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	 

	@Override
	public Object clone() throws CloneNotSupportedException {
		JobStatic ret = (JobStatic)super.clone();
		ret.runtasksInfos = ImpStaticManager.cloneRuntasksInfosa(this.runtasksInfos);
		
		ret.undotaskNos = ImpStaticManager.cloneListString(this.undotaskNos);
		return ret;
	}

	public List<TaskStatus> getRuntasksInfos() {
		return runtasksInfos;
	}

	public TaskStatus addJobTaskStatic(TaskInfo taskInfo,int queuepostion)
	{
		if(runtasksInfos == null)
		{
			runtasksInfos = new ArrayList<TaskStatus>();
		}
		TaskStatus ts = new TaskStatus();
		ts.setQueuepostion(queuepostion);
		ts.setStatus(0);
		ts.setTaskNo(taskInfo.getTaskNo());
		ts.setTaskInfo(taskInfo.toString());
		runtasksInfos.add(ts);
		this.currentposition = queuepostion;
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

	public int getTotaltasks() {
		return totaltasks;
	}

	public void setTotaltasks(int totaltasks) {
		this.totaltasks = totaltasks;
	}

	public int getUnruntasks() {
		return unruntasks;
	}

	public void setUnruntasks(int unruntasks) {
		this.unruntasks = unruntasks;
	}

	public int getCompletetasks() {
		return completetasks;
	}

	public void setCompletetasks(int completetasks) {
		this.completetasks = completetasks;
	}

	public int getFailtasks() {
		return failtasks;
	}

	public void setFailtasks(int failtasks) {
		this.failtasks = failtasks;
	}

	public int getWaittasks() {
		return waittasks;
	}

	public void setWaittasks(int waittasks) {
		this.waittasks = waittasks;
	}

	public int getRuntasks() {
		return runtasks;
	}

	public void setRuntasks(int runtasks) {
		this.runtasks = runtasks;
	}

	public String getJobname() {
		// TODO Auto-generated method stub
		return jobname;
	}

	public void setJobname(String jobname) {
		this.jobname = jobname;
	}

	public String getCompletetaskNos() {
		return completetaskNos;
	}

	public void setCompletetaskNos(String completetaskNos) {
		this.completetaskNos = completetaskNos;
	}

	public String getFailedtaskNos() {
		return failedtaskNos;
	}

	public int getCurrentposition() {
		return currentposition;
	}

	public void setCurrentposition(int currentposition) {
		this.currentposition = currentposition;
	}

	public long getSuccessrecords() {
		return successrecords;
	}

	public long getFailerecords() {
		return failerecords;
	}

	public List<String> getUndotaskNos() {
		return undotaskNos;
	}
	
	public void completeTask(String taskNo)
	{
		if(undotaskNos != null)
		{
			undotaskNos.remove(taskNo);
		}
	}

	public void setUndotaskNos(List<String> undotaskNos) {
		this.undotaskNos = undotaskNos;
	}

	public boolean isOnejob() {
		return onejob;
	}

	public void setOnejob(boolean onejob) {
		this.onejob = onejob;
	}

	public void incrementtotaltasks() {
		this.totaltasks ++;
		
	}

	public long getStartid() {
		return startid;
	}

	public void setStartid(long startid) {
		this.startid = startid;
	}

	public long getEndid() {
		return endid;
	}

	public void setEndid(long endid) {
		this.endid = endid;
	}

	public String getJobstaticid() {
		return jobstaticid;
	}

	public void setJobstaticid(String jobstaticid) {
		this.jobstaticid = jobstaticid;
	}
	
	
}
