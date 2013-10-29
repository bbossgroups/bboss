package org.frameworkset.web.multipart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.frameworkset.web.servlet.support.RequestMethodHttpServletRequest;

public abstract class AbstractMultipartHttpServletRequest extends RequestMethodHttpServletRequest
    implements MultipartHttpServletRequest {

	private Map<String,MultipartFile[]> multipartFiles;


	/**
	 * Wrap the given HttpServletRequest in a MultipartHttpServletRequest.
	 * @param request the request to wrap
	 */
	protected AbstractMultipartHttpServletRequest(HttpServletRequest request) {
		super(request);
	}


	public Iterator<String> getFileNames() {
		return getMultipartFiles().keySet().iterator();
	}

	public MultipartFile getFile(String name) {
		MultipartFile[] files = getMultipartFiles().get(name);
		if(files == null || files.length == 0)
			return null;
		return files[0];
//		return (MultipartFile) getMultipartFiles().get(name);
	}
	
	public MultipartFile[] getFiles(String name) {
		MultipartFile[] files = getMultipartFiles().get(name);
//		if(files == null || files.length == 0)
//			return null;
		return files;
//		return (MultipartFile) getMultipartFiles().get(name);
	}

	public Map<String,MultipartFile[]> getFileMap() {
		return getMultipartFiles();
	}


	/**
	 * Set a Map with parameter names as keys and MultipartFile objects as values.
	 * To be invoked by subclasses on initialization.
	 */
	protected final void setMultipartFiles(Map<String,MultipartFile[]> multipartFiles) {
		this.multipartFiles = Collections.unmodifiableMap(multipartFiles);
	}

	/**
	 * Obtain the MultipartFile Map for retrieval,
	 * lazily initializing it if necessary.
	 * @see #initializeMultipart()
	 */
	protected Map<String,MultipartFile[]> getMultipartFiles() {
		if (this.multipartFiles == null) {
			initializeMultipart();
		}
		return this.multipartFiles;
	}

	/**
	 * Lazily initialize the multipart request, if possible.
	 * Only called if not already eagerly initialized.
	 */
	protected void initializeMultipart() {
		throw new IllegalStateException("Multipart request not initialized");
	}
	
	@Override
	/**
	 * 返回界面上所有文件
	 */
	public MultipartFile[] getFirstFieldFiles() {
		if(this.multipartFiles == null)
		{
			return null;
		}
		Iterator<Entry<String, MultipartFile[]>> entries = this.multipartFiles.entrySet().iterator();
		List<MultipartFile> ret = new ArrayList<MultipartFile>(multipartFiles.size());
		int i = 0;
		while(entries.hasNext())
		{
			Entry<String, MultipartFile[]> entry = entries.next();
			MultipartFile[] values = entry.getValue();
			if(values != null && values.length > 0)
			{
				for(MultipartFile value:values)
				{
					if(value.getSize() == 0)
						continue;
					i ++;
					ret.add(value);
				}
				
			}
		}
		
		return ret.toArray(new MultipartFile[i]);
//		Iterator<String> filenames = getFileNames();
//		if(filenames == null)
//			return null;
//		while(filenames.hasNext())
//		{
//			MultipartFile[] values = getFiles(filenames.next());
//			
//			if(values != null && values.length > 0)
//			{
//				return values; 
//				
//			}
//		}
//		
//		
//		return null;
	}

}
