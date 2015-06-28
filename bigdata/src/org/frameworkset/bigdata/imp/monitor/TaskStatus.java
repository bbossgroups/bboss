package org.frameworkset.bigdata.imp.monitor;

public class TaskStatus implements java.io.Serializable,java.lang.Cloneable{
	private String taskInfo;
	private int queuepostion;
	/**
	 * -1：未执行任务
	 * 0:正在执行
	 * 1:执行完毕
	 * 2:执行异常
	 * 3:排队等候
	 */
	private int status =-1;
	private String errorInfo;
	private long handlerows;
	/**
	 * 任务号
	 */
	private String taskNo;
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
	public String getTaskNo() {
		return taskNo;
	}
	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	public int getQueuepostion() {
		return queuepostion;
	}
	public void setQueuepostion(int queuepostion) {
		this.queuepostion = queuepostion;
	}

}
