package org.frameworkset.bigdata.imp.monitor;

import java.util.Map;

/**
 * 数据节点对应的作业数据
 * @author yinbp
 *
 */
public class HostJobs  implements java.io.Serializable{
	private Map<String,JobStatic> jobs;
	/**
	 * 监控数据对应的数据节点时间戳，监控根据对比时间戳来确认其返回的数据是否过时
	 */
	private long datanodeTimestamp ;
	public Map<String, JobStatic> getJobs() {
		return jobs;
	}
	public void setJobs(Map<String, JobStatic> jobs) {
		this.jobs = jobs;
	}
	public long getDatanodeTimestamp() {
		return datanodeTimestamp;
	}
	public void setDatanodeTimestamp(long datanodeTimestamp) {
		this.datanodeTimestamp = datanodeTimestamp;
	}
}
