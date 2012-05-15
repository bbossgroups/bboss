package org.frameworkset.web.multipart.commons;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadBase.FileUploadIOException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.util.LimitedInputStream;
import org.apache.commons.fileupload.util.Streams;
import org.apache.log4j.Logger;
import org.frameworkset.util.io.Resource;
import org.frameworkset.web.multipart.MaxUploadSizeExceededException;
import org.frameworkset.web.multipart.MultipartException;
import org.frameworkset.web.multipart.MultipartFile;
import org.frameworkset.web.servlet.mvc.RequestMap;
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
	protected MultipartParsingResult parseFileItems(List fileItems, String[] encoding,HttpServletRequest request) {
		Map multipartFiles = new HashMap();
		Map multipartParameters = new HashMap();
		String oldEncoding = encoding[1];
		String newEncoding = encoding[0];
		
		
        String method = request.getMethod();
        String agent = request.getHeader("User-Agent");        
        boolean isie = agent.contains("MSIE ");
		boolean isutf8 = newEncoding.toLowerCase().equals("utf-8");
        boolean isget = method !=null && method.equals("GET");

		// Extract multipart files and multipart parameters.
		for (Iterator it = fileItems.iterator(); it.hasNext();) {
			FileItem fileItem = (FileItem) it.next();
			if (fileItem.isFormField()) {
				String value = null;
				if (encoding != null) {
					try {
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
			                
			            	value = fileItem.getString(oldEncoding);
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
	
	private InputStream getOctetStream(long requestSize,InputStream input) throws SizeLimitExceededException
	{
		
		long sizeMax = this.getFileUpload().getSizeMax();
		if (sizeMax >= 0) {
           
            if (requestSize == -1) {
                input = new LimitedInputStream(input, sizeMax) {
                    protected void raiseError(long pSizeMax, long pCount)
                            throws IOException {
                        FileUploadException ex =
                            new SizeLimitExceededException(
                                "the request was rejected because"
                                + " its size (" + pCount
                                + ") exceeds the configured maximum"
                                + " (" + pSizeMax + ")",
                                pCount, pSizeMax);
                        throw new FileUploadIOException(ex);
                    }
                };
            } else {
                if (sizeMax >= 0 && requestSize > sizeMax) {
                    throw new SizeLimitExceededException(
                            "the request was rejected because its size ("
                            + requestSize
                            + ") exceeds the configured maximum ("
                            + sizeMax + ")",
                            requestSize, sizeMax);
                }
            }
        }
		return input;
	}
	/**
	 * html 5,firefox application/octet-stream upload parse
	 * 
	 * text/html,application/xhtml+xml,application/xml;q=0.9,;q=0.8
		Accept-Encoding	gzip, deflate
		Accept-Language	zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3
		Cache-Control	no-cache
		Connection	keep-alive
		Content-Disposition	attachment; name="filedata"; filename="bluegreen_tab_bg_left.gif"
		Content-Length	870
		Content-Type	application/octet-stream
		Cookie	CNZZDATA2111753=cnzz_eid=60843127-1336722603-http%253A%252F%252Fwww.google.com.hk%252Furl%253Fsa%253Dt%2526rct%253Dj%2526q%253Dxheditor%2526source%253Dweb%2526cd%253D1%2526ved%253D0CF8QFjAA%2526url%253Dhttp%25253A%25252F%25252Fxheditor.com%25252F%2526ei%253DpcSsT5DdFoaYiAeuhZmKCQ%2526usg%253DAFQjCNFWHUR0AgI4Lt7sEW8bN7Lz7jLtbw%2526cad%253Drjt&ntime=1337046438&cnzz_a=2&retime=1337046850765&sin=none&ltime=1337046850765&rtime=2
		Host	xheditor.com
		Pragma	no-cache
		Referer	http://xheditor.com/demos/demo08.html
		User-Agent	Mozilla/5.0 (Windows NT 5.1; rv:11.0) Gecko/20100101 Firefox/11.0
	 * Parse the given List of octet Files into a Bboss MultipartParsingResult,
	 * containing Bboss MultipartFile instances and a Map of multipart parameter.
	 * @param fileItems the Commons FileIterms to parse
	 * @param encoding the encoding to use for form fields
	 * @return the Bboss MultipartParsingResult
	 * 
	 */
	protected MultipartParsingResult parseOctetFileItems(HttpServletRequest request) {
		Map multipartFiles = new HashMap();
		try {  
            String dispoString = request.getHeader("Content-Disposition");  
            int iFindStart = dispoString.indexOf(" filename=\"") + 11;  
            int iFindEnd = dispoString.indexOf("\"", iFindStart);  
            String sFileName = dispoString.substring(iFindStart, iFindEnd);
            iFindStart = dispoString.indexOf(" name=\"") + 7;  
            iFindEnd = dispoString.indexOf("\"", iFindStart);  
            String sFieldName = dispoString.substring(iFindStart, iFindEnd);
            
            String contentType = request.getContentType();
            int i = request.getContentLength();
            OctetstreamMultipartFile file = new OctetstreamMultipartFile(sFieldName,contentType,i,this.getFileItemFactory().getRepository(),this.getFileItemFactory().getSizeThreshold(),sFileName);
            Streams.copy(getOctetStream(i,request.getInputStream()), file.getOutputStream(),
                    true);
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
    		if (logger.isDebugEnabled()) {
    			logger.debug("Found multipart file [" + file.getName() + "] of size " + file.getSize() +
    					" bytes with original filename [" + file.getOriginalFilename() + "], stored " +
    					file.getStorageDescription());

    		}
		}
		catch (FileUploadBase.SizeLimitExceededException ex) {
			throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), ex);
		}
//		catch (FileUploadException ex) {
//			throw new MultipartException("Could not parse multipart servlet request", ex);
//		} 
		catch (IOException e) {
			throw new MultipartException("Could not parse multipart servlet request", e);
		} 

				  	
		return new MultipartParsingResult(multipartFiles, new RequestMap(request));
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
					
					MultipartFile file = file_;
					if (logger.isDebugEnabled()) {
						logger.debug("Cleaning up multipart file [" + file.getName() + "] with original filename [" +
								file.getOriginalFilename() + "], stored " + file.getStorageDescription());
					}
					file.destroy();
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
