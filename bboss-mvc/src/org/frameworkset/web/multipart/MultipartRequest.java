package org.frameworkset.web.multipart;

import java.util.Iterator;
import java.util.Map;

public interface MultipartRequest {
	/**
	 * Return an {@link java.util.Iterator} of String objects containing the
	 * parameter names of the multipart files contained in this request. These
	 * are the field names of the form (like with normal parameters), not the
	 * original file names.
	 * @return the names of the files
	 */
	Iterator<String> getFileNames();

	/**
	 * Return the contents plus description of an uploaded file in this request,
	 * or <code>null</code> if it does not exist.
	 * @param name a String specifying the parameter name of the multipart file
	 * @return the uploaded content in the form of a {MultipartFile} object
	 */
	MultipartFile getFile(String name);
	MultipartFile[] getFiles(String name);
	
	/**
	 * 获取第一个file input元素对应的附件数组
	 * @return
	 */
	MultipartFile[] getFirstFieldFiles();

	/**
	 * Return a {@link java.util.Map} of the multipart files contained in this request.
	 * @return a map containing the parameter names as keys, and the
	 * {@link MultipartFile} objects as values
	 * @see MultipartFile
	 */
	Map<String,MultipartFile[]> getFileMap();

}
