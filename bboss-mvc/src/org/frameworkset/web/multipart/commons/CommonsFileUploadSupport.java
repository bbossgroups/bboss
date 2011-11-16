package org.frameworkset.web.multipart.commons;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.log4j.Logger;
import org.frameworkset.util.io.Resource;
import org.frameworkset.web.multipart.MultipartFile;
import org.frameworkset.web.util.WebUtils;

import com.frameworkset.util.StringUtil;

public abstract class CommonsFileUploadSupport {
	/**
	 * 默认的参数编码规则
	 */
	private String encoding;

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	protected final static Logger logger = Logger.getLogger(CommonsFileUploadSupport.class);

	private final DiskFileItemFactory fileItemFactory;

	private final FileUpload fileUpload;

	private boolean uploadTempDirSpecified = false;


	/**
	 * Instantiate a new CommonsFileUploadSupport with its
	 * corresponding FileItemFactory and FileUpload instances.
	 * @see #newFileItemFactory
	 * @see #newFileUpload
	 */
	public CommonsFileUploadSupport() {
		this.fileItemFactory = newFileItemFactory();
		this.fileUpload = newFileUpload(getFileItemFactory());
	}


	/**
	 * Return the underlying <code>org.apache.commons.fileupload.disk.DiskFileItemFactory</code>
	 * instance. There is hardly any need to access this.
	 * @return the underlying DiskFileItemFactory instance
	 */
	public DiskFileItemFactory getFileItemFactory() {
		return this.fileItemFactory;
	}

	/**
	 * Return the underlying <code>org.apache.commons.fileupload.FileUpload</code>
	 * instance. There is hardly any need to access this.
	 * @return the underlying FileUpload instance
	 */
	public FileUpload getFileUpload() {
		return this.fileUpload;
	}

	/**
	 * Set the maximum allowed size (in bytes) before uploads are refused.
	 * -1 indicates no limit (the default).
	 * @param maxUploadSize the maximum upload size allowed
	 * @see org.apache.commons.fileupload.FileUploadBase#setSizeMax
	 */
	public void setMaxUploadSize(long maxUploadSize) {
		this.fileUpload.setSizeMax(maxUploadSize);
	}

	/**
	 * Set the maximum allowed size (in bytes) before uploads are written to disk.
	 * Uploaded files will still be received past this amount, but they will not be
	 * stored in memory. Default is 10240, according to Commons FileUpload.
	 * @param maxInMemorySize the maximum in memory size allowed
	 * @see org.apache.commons.fileupload.disk.DiskFileItemFactory#setSizeThreshold
	 */
	public void setMaxInMemorySize(int maxInMemorySize) {
		this.fileItemFactory.setSizeThreshold(maxInMemorySize);
	}

	/**
	 * Set the default character encoding to use for parsing requests,
	 * to be applied to headers of individual parts and to form fields.
	 * Default is ISO-8859-1, according to the Servlet spec.
	 * <p>If the request specifies a character encoding itself, the request
	 * encoding will override this setting. This also allows for generically
	 * overriding the character encoding in a filter that invokes the
	 * <code>ServletRequest.setCharacterEncoding</code> method.
	 * @param defaultEncoding the character encoding to use
	 * @see javax.servlet.ServletRequest#getCharacterEncoding
	 * @see javax.servlet.ServletRequest#setCharacterEncoding
	 * @see WebUtils#DEFAULT_CHARACTER_ENCODING
	 * @see org.apache.commons.fileupload.FileUploadBase#setHeaderEncoding
	 */
	public void setDefaultEncoding(String defaultEncoding) {
		this.fileUpload.setHeaderEncoding(defaultEncoding);
	}

	protected String getDefaultEncoding() {
		String encoding = getFileUpload().getHeaderEncoding();
		if (encoding == null) {
			if(this.encoding != null)
				return this.encoding;
			encoding = WebUtils.DEFAULT_CHARACTER_ENCODING;
		}
		return encoding;
	}

	/**
	 * Set the temporary directory where uploaded files get stored.
	 * Default is the servlet container's temporary directory for the web application.
	 * @see WebUtils#TEMP_DIR_CONTEXT_ATTRIBUTE
	 */
	public void setUploadTempDir(Resource uploadTempDir) throws IOException {
		if (!uploadTempDir.exists() && !uploadTempDir.getFile().mkdirs()) {
			throw new IllegalArgumentException("Given uploadTempDir [" + uploadTempDir + "] could not be created");
		}
		this.fileItemFactory.setRepository(uploadTempDir.getFile());
		this.uploadTempDirSpecified = true;
	}

	protected boolean isUploadTempDirSpecified() {
		return this.uploadTempDirSpecified;
	}


	/**
	 * Factory method for a Commons DiskFileItemFactory instance.
	 * <p>Default implementation returns a standard DiskFileItemFactory.
	 * Can be overridden to use a custom subclass, e.g. for testing purposes.
	 * @return the new DiskFileItemFactory instance
	 */
	protected DiskFileItemFactory newFileItemFactory() {
		return new DiskFileItemFactory();
	}

	/**
	 * Factory method for a Commons FileUpload instance.
	 * <p><b>To be implemented by subclasses.</b>
	 * @param fileItemFactory the Commons FileItemFactory to build upon
	 * @return the Commons FileUpload instance
	 */
	protected abstract FileUpload newFileUpload(FileItemFactory fileItemFactory);


	/**
	 * Determine an appropriate FileUpload instance for the given encoding.
	 * <p>Default implementation returns the shared FileUpload instance
	 * if the encoding matches, else creates a new FileUpload instance
	 * with the same configuration other than the desired encoding.
	 * @param encoding the character encoding to use
	 * @return an appropriate FileUpload instance.
	 */
	protected FileUpload prepareFileUpload(String encoding) {
		FileUpload fileUpload = getFileUpload();
		FileUpload actualFileUpload = fileUpload;

		// Use new temporary FileUpload instance if the request specifies
		// its own encoding that does not match the default encoding.
		if (encoding != null && !encoding.equals(fileUpload.getHeaderEncoding())) {
			actualFileUpload = newFileUpload(getFileItemFactory());
			actualFileUpload.setSizeMax(fileUpload.getSizeMax());
			actualFileUpload.setHeaderEncoding(encoding);
		}

		return actualFileUpload;
	}
	private boolean isIOS88591(String endcoding) {
        endcoding = endcoding.toLowerCase();
        return endcoding.startsWith("iso") && (endcoding.indexOf("8859") != -1) && endcoding.endsWith("1");
    }
	
	
	/**
	 * Parse the given List of Commons FileItems into a Bboss MultipartParsingResult,
	 * containing Bboss MultipartFile instances and a Map of multipart parameter.
	 * @param fileItems the Commons FileIterms to parse
	 * @param encoding the encoding to use for form fields
	 * @return the Bboss MultipartParsingResult
	 * @see CommonsMultipartFile#CommonsMultipartFile(org.apache.commons.fileupload.FileItem)
	 */
	protected MultipartParsingResult parseFileItems(List fileItems, String[] encoding) {
		Map multipartFiles = new HashMap();
		Map multipartParameters = new HashMap();
		String oldEncoding = encoding[1];
		String newEncoding = encoding[0];

		// Extract multipart files and multipart parameters.
		for (Iterator it = fileItems.iterator(); it.hasNext();) {
			FileItem fileItem = (FileItem) it.next();
			if (fileItem.isFormField()) {
				String value = null;
				if (encoding != null) {
					try {
//						value = fileItem.getString(encoding);
						
						
						if ( (oldEncoding == null || isIOS88591(oldEncoding)) ) {
							if(newEncoding != null)
							{
								value = fileItem.getString(newEncoding);
							}
							else
							{
								value = fileItem.getString();
							}
			            }
			            else 
			            {
			                
			            	value = fileItem.getString(oldEncoding);;
			            }
						
					}
					catch (UnsupportedEncodingException ex) {
//						if (logger.isWarnEnabled()) 
						{
							logger.warn("Could not decode multipart item '" + fileItem.getFieldName() +
									"' with encoding '" + encoding + "': using platform default");
						}
						value = fileItem.getString();
					}
				}
				else {
					value = fileItem.getString();
				}
				String[] curParam = (String[]) multipartParameters.get(fileItem.getFieldName());
				if (curParam == null) {
					// simple form field
					multipartParameters.put(fileItem.getFieldName(), new String[] { value });
				}
				else {
					// array of simple form fields
					String[] newParam = StringUtil.addStringToArray(curParam, value);
					multipartParameters.put(fileItem.getFieldName(), newParam);
				}
			}
			else {
				// multipart file field
				CommonsMultipartFile file = new CommonsMultipartFile(fileItem);
				MultipartFile[] multipartFiles_ = (MultipartFile[])multipartFiles.get(file.getName());
				if(multipartFiles_ == null)
				{
					multipartFiles_ = new MultipartFile[]{file};
					multipartFiles.put(file.getName(),multipartFiles_);
				}
				else
				{
					MultipartFile[] newParam = addFileToArrayFiles( multipartFiles_, file);
					multipartFiles.put(file.getName(),newParam);
				}
//				if (multipartFiles.put(file.getName(), file) != null) {
//					throw new MultipartException(
//							"Multiple files for field name [" + file.getName() + "] found - not supported by MultipartResolver");
//				}
				if (logger.isDebugEnabled()) {
					logger.debug("Found multipart file [" + file.getName() + "] of size " + file.getSize() +
							" bytes with original filename [" + file.getOriginalFilename() + "], stored " +
							file.getStorageDescription());
				}
			}
		}
		return new MultipartParsingResult(multipartFiles, multipartParameters);
	}
	
	private MultipartFile[] addFileToArrayFiles(MultipartFile[] multipartFiles_,MultipartFile file)
	{
		
		MultipartFile[] newArr = new MultipartFile[multipartFiles_.length + 1];
		System.arraycopy(multipartFiles_, 0, newArr, 0, multipartFiles_.length);
		newArr[multipartFiles_.length] = file;
		return newArr;
	}

	/**
	 * Cleanup the Bboss MultipartFiles created during multipart parsing,
	 * potentially holding temporary data on disk.
	 * <p>Deletes the underlying Commons FileItem instances.
	 * @param multipartFiles Collection of MultipartFile instances
	 * @see org.apache.commons.fileupload.FileItem#delete()
	 */
	protected void cleanupFileItems(Collection multipartFiles) {
		for (Iterator it = multipartFiles.iterator(); it.hasNext();) {
			MultipartFile[] files = (MultipartFile[]) it.next();
			if(files != null)
			{
				for(MultipartFile file_:files)
				{
					CommonsMultipartFile file = (CommonsMultipartFile)file_;
					if (logger.isDebugEnabled()) {
						logger.debug("Cleaning up multipart file [" + file.getName() + "] with original filename [" +
								file.getOriginalFilename() + "], stored " + file.getStorageDescription());
					}
					file.getFileItem().delete();
				}
			}
		}
	}


	/**
	 * Holder for a Map of Bboss MultipartFiles and a Map of
	 * multipart parameters.
	 */
	protected static class MultipartParsingResult {

		private final Map multipartFiles;

		private final Map multipartParameters;

		/**
		 * Create a new MultipartParsingResult.
		 * @param multipartFiles Map of field name to MultipartFile instance
		 * @param multipartParameters Map of field name to form field String value
		 */
		public MultipartParsingResult(Map multipartFiles, Map multipartParameters) {
			this.multipartFiles = multipartFiles;
			this.multipartParameters = multipartParameters;
		}

		/**
		 * Return the multipart files as Map of field name to MultipartFile instance.
		 */
		public Map getMultipartFiles() {
			return this.multipartFiles;
		}

		/**
		 * Return the multipart parameters as Map of field name to form field String value.
		 */
		public Map getMultipartParameters() {
			return this.multipartParameters;
		}
	}

}
