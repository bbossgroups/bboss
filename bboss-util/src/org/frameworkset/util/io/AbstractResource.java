/*
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

package org.frameworkset.util.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.frameworkset.util.ResourceUtils;





/**
 * <p>Title: AbstractResource.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-9-24 下午05:33:52
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class AbstractResource implements Resource {
	protected volatile long savesize;
	private static Logger log = Logger.getLogger(AbstractResource.class);
	

	/**
	 * This implementation checks whether a File can be opened,
	 * falling back to whether an InputStream can be opened.
	 * This will cover both directories and content resources.
	 */
	public boolean exists() {
		// Try file existence: can we find the file in the file system?
		try {
			return getFile().exists();
		}
		catch (IOException ex) {
			// Fall back to stream existence: can we open the stream?
			try {
				InputStream is = getInputStream();
				is.close();
				return true;
			}
			catch (Throwable isEx) {
				log.error("",isEx);
				return false;
			}
		}
	}

	/**
	 * This implementation always returns <code>true</code>.
	 */
	public boolean isReadable() {
		return true;
	}

	/**
	 * This implementation always returns <code>false</code>.
	 */
	public boolean isOpen() {
		return false;
	}

	/**
	 * This implementation throws a FileNotFoundException, assuming
	 * that the resource cannot be resolved to a URL.
	 */
	public URL getURL() throws IOException {
		throw new FileNotFoundException(getDescription() + " cannot be resolved to URL");
	}

	/**
	 * This implementation builds a URI based on the URL returned
	 * by {@link #getURL()}.
	 */
	public URI getURI() throws IOException {
		URL url = getURL();
		try {
			return ResourceUtils.toURI(url);
		}
		catch (URISyntaxException ex) {
			throw new NestedIOException("Invalid URI [" + url + "]", ex);
		}
	}

	/**
	 * This implementation throws a FileNotFoundException, assuming
	 * that the resource cannot be resolved to an absolute file path.
	 */
	public File getFile() throws IOException {
		throw new FileNotFoundException(getDescription() + " cannot be resolved to absolute file path");
	}

	/**
	 * This implementation checks the timestamp of the underlying File,
	 * if available.
	 * @see #getFileForLastModifiedCheck()
	 */
	public long lastModified() throws IOException {
		long lastModified = getFileForLastModifiedCheck().lastModified();
		if (lastModified == 0L) {
			throw new FileNotFoundException(getDescription() +
					" cannot be resolved in the file system for resolving its last-modified timestamp");
		}
		return lastModified;
	}
	/**
	 * This implementation checks the timestamp of the underlying File,
	 * if available.
	 * @see #getFile()
	 */
	public long contentLength() throws IOException {
		return getFile().length();
	}
	/**
	 * Determine the File to use for timestamp checking.
	 * <p>The default implementation delegates to {@link #getFile()}.
	 * @return the File to use for timestamp checking (never <code>null</code>)
	 * @throws IOException if the resource cannot be resolved as absolute
	 * file path, i.e. if the resource is not available in a file system
	 */
	protected File getFileForLastModifiedCheck() throws IOException {
		return getFile();
	}

	/**
	 * This implementation throws a FileNotFoundException, assuming
	 * that relative resources cannot be created for this resource.
	 */
	public Resource createRelative(String relativePath) throws IOException {
		throw new FileNotFoundException("Cannot create a relative resource for " + getDescription());
	}

	/**
	 * This implementation always throws IllegalStateException,
	 * assuming that the resource does not carry a filename.
	 */
	public String getFilename() throws IllegalStateException {
		throw new IllegalStateException(getDescription() + " does not carry a filename");
	}


	/**
	 * This implementation returns the description of this resource.
	 * @see #getDescription()
	 */
	public String toString() {
		return getDescription();
	}

	/**
	 * This implementation compares description strings.
	 * @see #getDescription()
	 */
	public boolean equals(Object obj) {
		return (obj == this ||
		    (obj instanceof Resource && ((Resource) obj).getDescription().equals(getDescription())));
	}

	/**
	 * This implementation returns the description's hash code.
	 * @see #getDescription()
	 */
	public int hashCode() {
		return getDescription().hashCode();
	}
	
	public void release()
	{
		
	}
	
	public  void savetofile(File destinctionFile,ResourceHandleListener<AbstractResource> listener) throws IOException
	{
		InputStream stFileInputStream = null;

        FileOutputStream stFileOutputStream = null;
        OutputStream dufferOput = null;
        try
        {
//            makeFile(destinctionFile);
        	if(listener != null)
        	{
        		try {
        			listener.startEvent(this,destinctionFile);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
        	}
            stFileInputStream = this.getInputStream();
            if(stFileInputStream == null)
            	return;

            stFileOutputStream = new FileOutputStream(destinctionFile);
            dufferOput = new BufferedOutputStream(stFileOutputStream);
            int arraySize = 1024;
            byte buffer[] = new byte[arraySize];
            int bytesRead;
            while ((bytesRead = stFileInputStream.read(buffer)) != -1)
            {
            	
            	dufferOput.write(buffer, 0, bytesRead);
                this.savesize = savesize + bytesRead;
                if(listener != null)
            	{
                	try {
                		listener.handleDataEvent(this,destinctionFile);
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
            	}
            }
            dufferOput.flush();

        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
        	if(listener != null)
        	{
        		try {
					listener.endEvent(this,destinctionFile);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
            if (stFileInputStream != null)
                try
                {
                    stFileInputStream.close();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            if(dufferOput != null)
            {
            	try {
					dufferOput.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            if (stFileOutputStream != null)
                try
                {
                    stFileOutputStream.close();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
	}
	public  void savetofile(File destinctionFile) throws IOException
    {

		savetofile(destinctionFile,null);

    }

	public long getSavesize() {
		return savesize;
	}

}
