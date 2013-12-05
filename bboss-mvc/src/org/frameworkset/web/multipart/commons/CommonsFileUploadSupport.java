package org.frameworkset.web.multipart.commons;

import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

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
        boolean isie = agent != null ?agent.contains("MSIE "):false;
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
//	public static class FileUploadException extends Exception {
//	    /**
//	     * Serial version UID, being used, if the exception
//	     * is serialized.
//	     */
//	    private static final long serialVersionUID = 8881893724388807504L;
//	    /**
//	     * The exceptions cause. We overwrite the cause of
//	     * the super class, which isn't available in Java 1.3.
//	     */
//	    private final Throwable cause;
//
//	    /**
//	     * Constructs a new <code>FileUploadException</code> without message.
//	     */
//	    public FileUploadException() {
//	        this(null, null);
//	    }
//
//	    /**
//	     * Constructs a new <code>FileUploadException</code> with specified detail
//	     * message.
//	     *
//	     * @param msg the error message.
//	     */
//	    public FileUploadException(final String msg) {
//	        this(msg, null);
//	    }
//
//	    /**
//	     * Creates a new <code>FileUploadException</code> with the given
//	     * detail message and cause.
//	     * @param msg The exceptions detail message.
//	     * @param cause The exceptions cause.
//	     */
//	    public FileUploadException(String msg, Throwable cause) {
//	        super(msg);
//	        this.cause = cause;
//	    }
//
//	    /**
//	     * Prints this throwable and its backtrace to the specified print stream.
//	     *
//	     * @param stream <code>PrintStream</code> to use for output
//	     */
//	    public void printStackTrace(PrintStream stream) {
//	        super.printStackTrace(stream);
//	        if (cause != null) {
//	            stream.println("Caused by:");
//	            cause.printStackTrace(stream);
//	        }
//	    }
//
//	    /**
//	     * Prints this throwable and its backtrace to the specified
//	     * print writer.
//	     *
//	     * @param writer <code>PrintWriter</code> to use for output
//	     */
//	    public void printStackTrace(PrintWriter writer) {
//	        super.printStackTrace(writer);
//	        if (cause != null) {
//	            writer.println("Caused by:");
//	            cause.printStackTrace(writer);
//	        }
//	    }
//	}
	 /**
     * This exception is thrown for hiding an inner
     * {@link FileUploadException} in an {@link IOException}.
     */
    public static class FileUploadIOException extends IOException {
        /** The exceptions UID, for serializing an instance.
         */
        private static final long serialVersionUID = -7047616958165584154L;
        /** The exceptions cause; we overwrite the parent
         * classes field, which is available since Java
         * 1.4 only.
         */
        private final FileUploadException cause;

        /**
         * Creates a <code>FileUploadIOException</code> with the
         * given cause.
         * @param pCause The exceptions cause, if any, or null.
         */
        public FileUploadIOException(FileUploadException pCause) {
            // We're not doing super(pCause) cause of 1.3 compatibility.
            cause = pCause;
        }

        /**
         * Returns the exceptions cause.
         * @return The exceptions cause, if any, or null.
         */
        public Throwable getCause() {
            return cause;
        }
    }
	
    /**
     * Interface of an object, which may be closed.
     */
    public static interface Closeable {
        /**
         * Closes the object.
         * @throws IOException An I/O error occurred.
         */
        void close() throws IOException;

        /**
         * Returns, whether the object is already closed.
         * @return True, if the object is closed, otherwise false.
         * @throws IOException An I/O error occurred.
         */
        boolean isClosed() throws IOException;
    }

    /**
     * An input stream, which limits its data size. This stream is
     * used, if the content length is unknown.
     */
    public static abstract class LimitedInputStream
            extends FilterInputStream implements Closeable {
        /**
         * The maximum size of an item, in bytes.
         */
        private long sizeMax;
        /**
         * The current number of bytes.
         */
        private long count;
        /**
         * Whether this stream is already closed.
         */
        private boolean closed;

        /**
         * Creates a new instance.
         * @param pIn The input stream, which shall be limited.
         * @param pSizeMax The limit; no more than this number of bytes
         *   shall be returned by the source stream.
         */
        public LimitedInputStream(InputStream pIn, long pSizeMax) {
            super(pIn);
            sizeMax = pSizeMax;
        }

        /**
         * Called to indicate, that the input streams limit has
         * been exceeded.
         * @param pSizeMax The input streams limit, in bytes.
         * @param pCount The actual number of bytes.
         * @throws IOException The called method is expected
         *   to raise an IOException.
         */
        protected abstract void raiseError(long pSizeMax, long pCount)
                throws IOException;

        /** Called to check, whether the input streams
         * limit is reached.
         * @throws IOException The given limit is exceeded.
         */
        private void checkLimit() throws IOException {
            if (count > sizeMax) {
                raiseError(sizeMax, count);
            }
        }

        /**
         * Reads the next byte of data from this input stream. The value
         * byte is returned as an <code>int</code> in the range
         * <code>0</code> to <code>255</code>. If no byte is available
         * because the end of the stream has been reached, the value
         * <code>-1</code> is returned. This method blocks until input data
         * is available, the end of the stream is detected, or an exception
         * is thrown.
         * <p>
         * This method
         * simply performs <code>in.read()</code> and returns the result.
         *
         * @return     the next byte of data, or <code>-1</code> if the end of the
         *             stream is reached.
         * @exception  IOException  if an I/O error occurs.
         * @see        java.io.FilterInputStream#in
         */
        public int read() throws IOException {
            int res = super.read();
            if (res != -1) {
                count++;
                checkLimit();
            }
            return res;
        }

        /**
         * Reads up to <code>len</code> bytes of data from this input stream
         * into an array of bytes. If <code>len</code> is not zero, the method
         * blocks until some input is available; otherwise, no
         * bytes are read and <code>0</code> is returned.
         * <p>
         * This method simply performs <code>in.read(b, off, len)</code>
         * and returns the result.
         *
         * @param      b     the buffer into which the data is read.
         * @param      off   The start offset in the destination array
         *                   <code>b</code>.
         * @param      len   the maximum number of bytes read.
         * @return     the total number of bytes read into the buffer, or
         *             <code>-1</code> if there is no more data because the end of
         *             the stream has been reached.
         * @exception  NullPointerException If <code>b</code> is <code>null</code>.
         * @exception  IndexOutOfBoundsException If <code>off</code> is negative,
         * <code>len</code> is negative, or <code>len</code> is greater than
         * <code>b.length - off</code>
         * @exception  IOException  if an I/O error occurs.
         * @see        java.io.FilterInputStream#in
         */
        public int read(byte[] b, int off, int len) throws IOException {
            int res = super.read(b, off, len);
            if (res > 0) {
                count += res;
                checkLimit();
            }
            return res;
        }

        /**
         * Returns, whether this stream is already closed.
         * @return True, if the stream is closed, otherwise false.
         * @throws IOException An I/O error occurred.
         */
        public boolean isClosed() throws IOException {
            return closed;
        }

        /**
         * Closes this input stream and releases any system resources
         * associated with the stream.
         * This
         * method simply performs <code>in.close()</code>.
         *
         * @exception  IOException  if an I/O error occurs.
         * @see        java.io.FilterInputStream#in
         */
        public void close() throws IOException {
            closed = true;
            super.close();
        }
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
	
	/** Utility class for working with streams.
	 */
	public static final class Streams {
	    /**
	     * Private constructor, to prevent instantiation.
	     * This class has only static methods.
	     */
	    private Streams() {
	        // Does nothing
	    }

	    /**
	     * Default buffer size for use in
	     * {@link #copy(InputStream, OutputStream, boolean)}.
	     */
	    private static final int DEFAULT_BUFFER_SIZE = 8192;

	    /**
	     * Copies the contents of the given {@link InputStream}
	     * to the given {@link OutputStream}. Shortcut for
	     * <pre>
	     *   copy(pInputStream, pOutputStream, new byte[8192]);
	     * </pre>
	     * @param pInputStream The input stream, which is being read.
	     * It is guaranteed, that {@link InputStream#close()} is called
	     * on the stream.
	     * @param pOutputStream The output stream, to which data should
	     * be written. May be null, in which case the input streams
	     * contents are simply discarded.
	     * @param pClose True guarantees, that {@link OutputStream#close()}
	     * is called on the stream. False indicates, that only
	     * {@link OutputStream#flush()} should be called finally.
	     *
	     * @return Number of bytes, which have been copied.
	     * @throws IOException An I/O error occurred.
	     */
	    public static long copy(InputStream pInputStream,
	            OutputStream pOutputStream, boolean pClose)
	            throws IOException {
	        return copy(pInputStream, pOutputStream, pClose,
	                new byte[DEFAULT_BUFFER_SIZE]);
	    }

	    /**
	     * Copies the contents of the given {@link InputStream}
	     * to the given {@link OutputStream}.
	     * @param pIn The input stream, which is being read.
	     *   It is guaranteed, that {@link InputStream#close()} is called
	     *   on the stream.
	     * @param pOut The output stream, to which data should
	     *   be written. May be null, in which case the input streams
	     *   contents are simply discarded.
	     * @param pClose True guarantees, that {@link OutputStream#close()}
	     *   is called on the stream. False indicates, that only
	     *   {@link OutputStream#flush()} should be called finally.
	     * @param pBuffer Temporary buffer, which is to be used for
	     *   copying data.
	     * @return Number of bytes, which have been copied.
	     * @throws IOException An I/O error occurred.
	     */
	    public static long copy(InputStream pIn,
	            OutputStream pOut, boolean pClose,
	            byte[] pBuffer)
	    throws IOException {
	        OutputStream out = pOut;
	        InputStream in = pIn;
	        try {
	            long total = 0;
	            for (;;) {
	                int res = in.read(pBuffer);
	                if (res == -1) {
	                    break;
	                }
	                if (res > 0) {
	                    total += res;
	                    if (out != null) {
	                        out.write(pBuffer, 0, res);
	                    }
	                }
	            }
	            if (out != null) {
	                if (pClose) {
	                    out.close();
	                } else {
	                    out.flush();
	                }
	                out = null;
	            }
	            in.close();
	            in = null;
	            return total;
	        } finally {
	            if (in != null) {
	                try {
	                    in.close();
	                } catch (Throwable t) {
	                    /* Ignore me */
	                }
	            }
	            if (pClose  &&  out != null) {
	                try {
	                    out.close();
	                } catch (Throwable t) {
	                    /* Ignore me */
	                }
	            }
	        }
	    }

	    /**
	     * This convenience method allows to read a
	     * {@link org.apache.commons.fileupload.FileItemStream}'s
	     * content into a string. The platform's default character encoding
	     * is used for converting bytes into characters.
	     * @param pStream The input stream to read.
	     * @see #asString(InputStream, String)
	     * @return The streams contents, as a string.
	     * @throws IOException An I/O error occurred.
	     */
	    public static String asString(InputStream pStream) throws IOException {
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        copy(pStream, baos, true);
	        return baos.toString();
	    }

	    /**
	     * This convenience method allows to read a
	     * {@link org.apache.commons.fileupload.FileItemStream}'s
	     * content into a string, using the given character encoding.
	     * @param pStream The input stream to read.
	     * @param pEncoding The character encoding, typically "UTF-8".
	     * @see #asString(InputStream)
	     * @return The streams contents, as a string.
	     * @throws IOException An I/O error occurred.
	     */
	    public static String asString(InputStream pStream, String pEncoding)
	            throws IOException {
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        copy(pStream, baos, true);
	        return baos.toString(pEncoding);
	    }
	}

}
