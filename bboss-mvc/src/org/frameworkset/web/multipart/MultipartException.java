package org.frameworkset.web.multipart;

import org.frameworkset.util.beans.NestedRuntimeException;




public class MultipartException  extends NestedRuntimeException {

	/**
	 * Constructor for MultipartException.
	 * @param msg the detail message
	 */
	public MultipartException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for MultipartException.
	 * @param msg the detail message
	 * @param cause the root cause from the multipart parsing API in use
	 */
	public MultipartException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
