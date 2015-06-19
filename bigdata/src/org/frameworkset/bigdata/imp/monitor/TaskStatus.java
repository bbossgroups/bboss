package org.frameworkset.bigdata.imp.monitor;

public class TaskStatus implements java.io.Serializable,java.lang.Cloneable{
	private String taskInfo;
	/**
	 * 0:正在执行
	 * 1:执行完毕
	 * 2:执行异常
	 * 3:排队等候
	 */
	private int status =-1;
	private String errorInfo;
	private long handlerows;
	private long errorrows;
	public String getTaskInfo() {
		return taskInfo;
	}
	public void setTaskInfo(String taskInfo) {
		this.taskInfo = taskInfo;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	public long getHandlerows() {
		return handlerows;
	}
	public void setHandlerows(long handlerows) {
		this.handlerows = handlerows;
	}
	public long getErrorrows() {
		return errorrows;
	}
	public void setErrorrows(long errorrows) {
		this.errorrows = errorrows;
	}

}
