package org.frameworkset.bigdata.imp;


public class TaskInfo  implements java.io.Serializable{
	long startoffset;
	long endoffset;
	long pagesize;
	String filename;
	
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
	

}
