package org.frameworkset.bigdata.imp;


public class TaskInfo  implements java.io.Serializable{
	long startoffset;
	long endoffset;
	long pagesize;
	String filename;
	/**
	 * 如果任务被重新分配，则忽略处理
	 */
	transient boolean reassigned;
	/**
	 * 任务号
	 */
	String taskNo;
	
	public long getStartoffset() {
		return startoffset;
	}
	public void setStartoffset(long startoffset) {
		this.startoffset = startoffset;
	}
	public long getEndoffset() {
		return endoffset;
	}
	public void setEndoffset(long endoffset) {
		this.endoffset = endoffset;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public long getPagesize() {
		return pagesize;
	}
	public void setPagesize(long pagesize) {
		this.pagesize = pagesize;
	}
	public String toString()
	 {
		
		 StringBuilder builder = new StringBuilder();
		 builder.append("filename="+filename).append(",")
			.append("pagesize=").append(pagesize).append(",")
			.append("startoffset=").append(startoffset).append(",")
			.append("endoffset=").append(endoffset);
		 return builder.toString();
	 }
	public String getTaskNo() {
		return taskNo;
	}
	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}
	public boolean isReassigned() {
		return reassigned;
	}
	public void setReassigned(boolean reassigned) {
		this.reassigned = reassigned;
	}
	

}
