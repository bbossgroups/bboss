/**
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.  
 */
package org.frameworkset.web.multipart.commons;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.output.DeferredFileOutputStream;
import org.apache.log4j.Logger;
import org.frameworkset.web.multipart.IgnoreFieldNameMultipartFile;

import com.frameworkset.util.FileUtil;



/**
 * <p> OctetstreamMultipartFile.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2012-5-14 下午5:49:16
 * @author biaoping.yin
 * @version 1.0
 */
public class OctetstreamMultipartFile  implements IgnoreFieldNameMultipartFile, Serializable {

	protected final static Logger logger = Logger.getLogger(CommonsMultipartFile.class);

//	private final File fileItem;

	private final long size;
	private String filedName;
	private String origineFileName;
	private String contentType;
		
    /**
     * Output stream for this item.
     */
    private transient DeferredFileOutputStream dfos;

    /**
     * The temporary file to use.
     */
    private transient File tempFile;

	/**
	 * Create an instance wrapping the given FileItem.
	 * @param fileItem the FileItem to wrap
	 * @throws IOException 
	 */
	public OctetstreamMultipartFile(String filedName,String contentType,int size,File repository,int sizeThreshold,String origineFileName) throws IOException {
		this.repository = repository;
		this.sizeThreshold = sizeThreshold; 
		this.size = size;
		this.filedName = filedName;
		this.origineFileName = origineFileName;
//		getOutputStream();
//		this.fileItem = this.dfos.getFile();
	}
	   /**
     * Removes the file contents from the temporary storage.
     */
    protected void finalize() {
        File outputFile = dfos.getFile();

        if (outputFile != null && outputFile.exists()) {
            outputFile.delete();
        }
    }


	/**
	 * Return the underlying <code>org.apache.commons.fileupload.FileItem</code>
	 * instance. There is hardly any need to access this.
	 */
	public final File getFileItem() {
		return this.tempFile;
	}


	public String getName() {
		return filedName;
	}

	public String getOriginalFilename() {
		String filename = origineFileName;
		if (filename == null) {
			// Should never happen.
			return "";
		}
		// check for Unix-style path
		int pos = filename.lastIndexOf("/");
		if (pos == -1) {
			// check for Windows-style path
			pos = filename.lastIndexOf("\\");
		}
		if (pos != -1)  {
			// any sort of path separator found
			return filename.substring(pos + 1);
		}
		else {
			// plain name
			return filename;
		}
	}

	public String getContentType() {
		return contentType;
	}

	public boolean isEmpty() {
		return (this.size == 0);
	}

	public long getSize() {
		return this.size;
	}
	 /**
     * Counter used in unique identifier generation.
     */
    private static int counter = 0;

	 /**
     * Returns an identifier that is unique within the class loader used to
     * load this class, but does not have random-like apearance.
     *
     * @return A String with the non-random looking instance identifier.
     */
    private static String getUniqueId() {
        final int limit = 100000000;
        int current;
        synchronized (DiskFileItem.class) {
            current = counter++;
        }
        String id = Integer.toString(current);

        // If you manage to get more than 100 million of ids, you'll
        // start getting ids longer than 8 characters.
        if (current < limit) {
            id = ("00000000" + id).substring(id.length());
        }
        return id;
    }
    private File repository;

	private int sizeThreshold;
    /**
     * UID used in unique file name generation.
     */
    private static final String UID =
            new java.rmi.server.UID().toString()
                .replace(':', '_').replace('-', '_');
    /**
     * Creates and returns a {@link java.io.File File} representing a uniquely
     * named temporary file in the configured repository path. The lifetime of
     * the file is tied to the lifetime of the <code>FileItem</code> instance;
     * the file will be deleted when the instance is garbage collected.
     *
     * @return The {@link java.io.File File} to be used for temporary storage.
     */
    protected File getTempFile() {
        if (tempFile == null) {
            File tempDir = repository;
            if (tempDir == null) {
                tempDir = new File(System.getProperty("java.io.tmpdir"));
            }

            String tempFileName =
                "upload_" + UID + "_" + getUniqueId() + ".tmp";

            tempFile = new File(tempDir, tempFileName);
        }
        return tempFile;
    }
    
    /**
     * Returns an {@link java.io.OutputStream OutputStream} that can
     * be used for storing the contents of the file.
     *
     * @return An {@link java.io.OutputStream OutputStream} that can be used
     *         for storing the contensts of the file.
     *
     * @throws IOException if an error occurs.
     */
    public OutputStream getOutputStream()
        throws IOException {
        if (dfos == null) {
            File outputFile = getTempFile();
            dfos = new DeferredFileOutputStream(sizeThreshold, outputFile);
        }
        return dfos;
    }
	public byte[] getBytes() {
		if (!isAvailable()) {
			throw new IllegalStateException("File has been moved - cannot be read again");
		}
		if(cachedContent != null)
			return cachedContent;
		 byte[] fileData = new byte[(int) getSize()];
	        FileInputStream fis = null;
	    	
	        try {
	            fis = new FileInputStream(dfos.getFile());
	            fis.read(fileData);
	        } catch (IOException e) {
	            fileData = null;
	        } finally {
	            if (fis != null) {
	                try {
	                    fis.close();
	                } catch (IOException e) {
	                    // ignore
	                }
	            }
	        }

	        return fileData;
	}

//	public InputStream getInputStream() throws IOException {
//		if (!isAvailable()) {
//			throw new IllegalStateException("File has been moved - cannot be read again");
//		}
//		InputStream inputStream = this.fileItem != null && this.fileItem.exists()?new java.io.FileInputStream(this.fileItem):null;
//		return (inputStream != null ? inputStream : new ByteArrayInputStream(new byte[0]));
//	}
	
	 /**
     * Cached contents of the file.
     */
    private byte[] cachedContent;
    
    /**
     * Provides a hint as to whether or not the file contents will be read
     * from memory.
     *
     * @return <code>true</code> if the file contents will be read
     *         from memory; <code>false</code> otherwise.
     */
    public boolean isInMemory() {
        if (cachedContent != null) {
            return true;
        }
        return dfos.isInMemory();
    }

    /**
     * Returns an {@link java.io.InputStream InputStream} that can be
     * used to retrieve the contents of the file.
     *
     * @return An {@link java.io.InputStream InputStream} that can be
     *         used to retrieve the contents of the file.
     *
     * @throws IOException if an error occurs.
     */
    public InputStream getInputStream()
        throws IOException {
    	if (!isAvailable()) {
			throw new IllegalStateException("File has been moved - cannot be read again");
		}
        if (!isInMemory()) {
            return new FileInputStream(dfos.getFile());
        }

        if (cachedContent == null) {
            cachedContent = dfos.getData();
        }
        return new ByteArrayInputStream(cachedContent);
    }

	public void transferTo(File dest) throws IOException, IllegalStateException {
		if (!isAvailable()) {
			throw new IllegalStateException("File has already been moved - cannot be transferred again");
		}

		if (dest.exists() && !dest.delete()) {
			throw new IOException(
					"Destination file [" + dest.getAbsolutePath() + "] already exists and could not be deleted");
		}

		try {
			FileUtil.fileCopy(this.tempFile, dest);
			this.tempFile.delete();
			 {
				
				logger.debug("Multipart file '" + getName() + "' with original filename [" +
						getOriginalFilename() + "], stored " + getStorageDescription() + ": " +
						 "move to [" + dest.getAbsolutePath() + "]");
			}
		}
		catch (Exception ex) {
			logger.error("Could not transfer to file", ex);
//			throw new IllegalStateException(ex.getMessage());
			throw new IOException("Could not transfer to file: " + ex.getMessage());
		}
//		catch (IOException ex) {
//			throw ex;
//		}
//		catch (Exception ex) {
//			logger.error("Could not transfer to file", ex);
//			throw new IOException("Could not transfer to file: " + ex.getMessage());
//		}
	}

	/**
	 * Determine whether the multipart content is still available.
	 * If a temporary file has been moved, the content is no longer available.
	 */
	protected boolean isAvailable() {
		
		// Check whether current file size is different than original one.
		return this.tempFile.exists() && (this.tempFile.length() == this.size);
	}

	/**
	 * Return a description for the storage location of the multipart content.
	 * Tries to be as specific as possible: mentions the file location in case
	 * of a temporary file.
	 */
	public String getStorageDescription() {
		{
			return "on disk";
		}
	}
	@Override
	public void destroy() {
		this.cachedContent = null;
		 if (this.tempFile != null && this.tempFile.exists()) {
			 this.tempFile.delete();
	        }
		
	}

}
