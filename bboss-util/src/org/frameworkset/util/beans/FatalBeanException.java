package org.frameworkset.util.beans;

public class FatalBeanException  extends BeansException {

	/**
	 * Create a new FatalBeanException with the specified message.
	 * @param msg the detail message
	 */
	public FatalBeanException(String msg) {
		super(msg);
	}

	/**
	 * Create a new FatalBeanException with the specified message
	 * and root cause.
	 * @param msg the detail message
	 * @param cause the root cause
	 */
	public FatalBeanException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
