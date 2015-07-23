package org.frameworkset.bigdata.imp.monitor;

import java.util.List;
import java.util.Map;

/**
 * 监控界面需要用到的展示对象
 * @author yinbp
 *
 */
public class SpecialMonitorObject  implements java.io.Serializable{
	private String jobstaticid ;
	/**
	 * Map<hostname,JobStatic>
	 */
	private Map <String,JobStatic> jobstaticsIdxByHost ;//作业分布服务器情况一览表	
	private String jobName;
//	private List<String> allJobNames;
	private String jobconfig;
	String  failedTaskNos;
	private String undotaskNos;
	String  successTaskNos;
	private String jobdef;
	private int totaltasks;
	
	private int unruntasks;
	private int completetasks;
	private int failtasks;
	private int runtasks;
	private int waittasks;
	
	private List<String> allDataNodes;
	private String adminNode;
	/**
	 * 抽取成功记录数
	 */
	private long successrecords;
	
	/**
	 * 抽取成功记录数
	 */
	private long failerecords;
	/**
	 * -1:未开始
	 * 0:执行：正在执行
	 * 1:结束：执行完毕
	 * 2:结束：执行失败
	 * 3:结束：部分成功，部分异常
	 * 4:挂起： 部分未开始，其他要么完成要么失败，类似于挂起
	 * 5:结束：强制停止
	 */
	private int status = -1;
	private long startid;
	private long endid;
	private boolean canrun;
	 
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
//	public List<String> getAllJobNames() {
//		return allJobNames;
//	}
//	public void setAllJobNames(List<String> allJobNames) {
//		this.allJobNames = allJobNames;
//	}
	public Map<String, JobStatic> getJobstaticsIdxByHost() {
		return jobstaticsIdxByHost;
	}
	public void setJobstaticsIdxByHost(Map<String, JobStatic> jobstaticsIdxByHost) {
		this.jobstaticsIdxByHost = jobstaticsIdxByHost;
	}
	public String getJobconfig() {
		return jobconfig;
	}
	public void setJobconfig(String jobconfig) {
		this.jobconfig = jobconfig;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public boolean isCanrun() {
		return canrun;
	}
	public void setCanrun(boolean canrun) {
		this.canrun = canrun;
	}
	public String getJobdef() {
		return jobdef;
	}
	public void setJobdef(String jobdef) {
		this.jobdef = jobdef;
	}
	public String getSuccessTaskNos() {
		return successTaskNos;
	}
	public void setSuccessTaskNos(String successTaskNos) {
		this.successTaskNos = successTaskNos;
	}
	public String getFailedTaskNos() {
		return failedTaskNos;
	}
	public void setFailedTaskNos(String failedTaskNos) {
		this.failedTaskNos = failedTaskNos;
	}
	public int getTotaltasks() {
		return totaltasks;
	}
	public void setTotaltasks(int totaltasks) {
		this.totaltasks = totaltasks;
	}
	public long getSuccessrecords() {
		return successrecords;
	}
	public void setSuccessrecords(long successrecords) {
		this.successrecords = successrecords;
	}
	public long getFailerecords() {
		return failerecords;
	}
	public void setFailerecords(long failerecords) {
		this.failerecords = failerecords;
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
	public int getRuntasks() {
		return runtasks;
	}
	public void setRuntasks(int runtasks) {
		this.runtasks = runtasks;
	}
	public int getWaittasks() {
		return waittasks;
	}
	public void setWaittasks(int waittasks) {
		this.waittasks = waittasks;
	}
	public String getUndotaskNos() {
		return undotaskNos;
	}
	public void setUndotaskNos(String undotaskNos) {
		this.undotaskNos = undotaskNos;
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
	public List<String> getAllDataNodes() {
		return allDataNodes;
	}
	public void setAllDataNodes(List<String> allDataNodes) {
		this.allDataNodes = allDataNodes;
	}
	public String getAdminNode() {
		return adminNode;
	}
	public void setAdminNode(String adminNode) {
		this.adminNode = adminNode;
	}

}
