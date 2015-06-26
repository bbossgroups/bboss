package org.frameworkset.bigdata.imp;

import java.io.Serializable;

public class StopDS implements Serializable {

	public StopDS() {
		// TODO Auto-generated constructor stub
	}
	private String jobname;
	private String stopdbnames;
	public String getJobname() {
		return jobname;
	}
	public void setJobname(String jobname) {
		this.jobname = jobname;
	}
	public String getStopdbnames() {
		return stopdbnames;
	}
	public void setStopdbnames(String stopdbnames) {
		this.stopdbnames = stopdbnames;
	}
	
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("jobname=").append(this.jobname)
		.append(",stopdbnames=").append(this.stopdbnames);
		return builder.toString();
	}

}
