
package org.frameworkset.web.multipart;

/**
 * 
 * <p>Title: MaxUploadSizeExceededException.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008-2010</p>
 * @Date 2010-10-2
 * @author biaoping.yin
 * @version 1.0
 */
public class MaxUploadSizeExceededException  extends MultipartException {

	private final long maxUploadSize;


	/**
	 * Constructor for MaxUploadSizeExceededException.
	 * @param maxUploadSize the maximum upload size allowed
	 */
	public MaxUploadSizeExceededException(long maxUploadSize) {
		this(maxUploadSize, null);
	}

	/**
	 * Constructor for MaxUploadSizeExceededException.
	 * @param maxUploadSize the maximum upload size allowed
	 * @param ex root cause from multipart parsing API in use
	 */
	public MaxUploadSizeExceededException(long maxUploadSize, Throwable ex) {
		super("Maximum upload size of " + maxUploadSize + " bytes exceeded", ex);
		this.maxUploadSize = maxUploadSize;
	}


	/**
	 * Return the maximum upload size allowed.
	 */
	public long getMaxUploadSize() {
		return maxUploadSize;
	}

}
