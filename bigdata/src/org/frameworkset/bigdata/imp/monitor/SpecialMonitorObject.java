package org.frameworkset.bigdata.imp.monitor;

import java.util.List;
import java.util.Map;

/**
 * 监控界面需要用到的展示对象
 * @author yinbp
 *
 */
public class SpecialMonitorObject  implements java.io.Serializable{
	/**
	 * Map<hostname,JobStatic>
	 */
	private Map <String,JobStatic> jobstaticsIdxByHost ;//作业分布服务器情况一览表	
	private String jobName;
	private List<String> allJobNames;
	private String jobconfig;
	
	private String jobdef;
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
	private boolean canrun;
	 
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public List<String> getAllJobNames() {
		return allJobNames;
	}
	public void setAllJobNames(List<String> allJobNames) {
		this.allJobNames = allJobNames;
	}
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

}
