package org.frameworkset.bigdata.imp;

import java.io.Serializable;
import java.util.Map;

public class ReassignTask implements Serializable {
	private String jobname;
	private String reassigntaskNode;
	private String reassigntaskJobname;
	private Map<String, Integer> hostTaskInfos;
	private boolean adminasdatanode;
	private String adminnode;
	private String jobstaticid;
	public ReassignTask() {
		// TODO Auto-generated constructor stub
	}
	public String getJobname() {
		return jobname;
	}
	public void setJobname(String jobname) {
		this.jobname = jobname;
	}
	public String getReassigntaskNode() {
		return reassigntaskNode;
	}
	public void setReassigntaskNode(String reassigntaskNode) {
		this.reassigntaskNode = reassigntaskNode;
	}
	
	
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("jobname=").append(jobname).append(",")
		.append("reassigntaskNode=").append(reassigntaskNode).append(",").append("otherTaskInfos=").append(hostTaskInfos.toString());
		return builder.toString();
	}
	public String getReassigntaskJobname() {
		return reassigntaskJobname;
	}
	public void setReassigntaskJobname(String reassigntaskJobname) {
		this.reassigntaskJobname = reassigntaskJobname;
	}
	public Map<String, Integer> getHostTaskInfos() {
		return hostTaskInfos;
	}
	public void setHostTaskInfos(Map<String, Integer> hostTaskInfos) {
		this.hostTaskInfos = hostTaskInfos;
	}
	public boolean isAdminasdatanode() {
		return adminasdatanode;
	}
	public void setAdminasdatanode(boolean adminasdatanode) {
		this.adminasdatanode = adminasdatanode;
	}
	public String getAdminnode() {
		return adminnode;
	}
	public void setAdminnode(String adminnode) {
		this.adminnode = adminnode;
	}
	public String getJobstaticid() {
		return jobstaticid;
	}
	public void setJobstaticid(String jobstaticid) {
		this.jobstaticid = jobstaticid;
	}

}
