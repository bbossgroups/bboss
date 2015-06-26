package org.frameworkset.bigdata.imp;

import java.io.Serializable;
import java.util.Map;

public class ReassignTask implements Serializable {
	private String jobname;
	private String reassigntaskNode;
	private Map<String, Integer> otherTaskInfos;
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
	public void setOtherTaskInfos(Map<String, Integer> taskinfos) {
		this.otherTaskInfos =  taskinfos;
		
	}
	public Map<String, Integer> getOtherTaskInfos() {
		return otherTaskInfos;
	}

}
