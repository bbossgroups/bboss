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
package org.frameworkset.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;


/**
 * <p>BigFile.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2011-7-23
 * @author biaoping.yin
 * @version 1.0
 */
public interface BigFile
{
	
	/**
	 * Return the original filename in the client's filesystem.
	 * <p>This may contain path information depending on the browser used,
	 * but it typically will not with any other than Opera.
	 * @return the original filename, or the empty String if no file
	 * has been chosen in the multipart form
	 */
	String getOriginalFilename();

	/**
	 * Return the content type of the file.
	 * @return the content type, or <code>null</code> if not defined
	 * (or no file has been chosen in the multipart form)
	 */
	String getContentType();

	/**
	 * Return whether the uploaded file is empty, that is, either no file has
	 * been chosen in the multipart form or the chosen file has no content.
	 */
	boolean isEmpty();

	/**
	 * Return the size of the file in bytes.
	 * @return the size of the file, or 0 if empty
	 */
	long getSize();

	/**
	 * Return the contents of the file as an array of bytes.
	 * @return the contents of the file as bytes, or an empty byte array if empty
	 * @throws IOException in case of access errors (if the temporary store fails)
	 */
	byte[] getBytes() throws IOException;

	/**
	 * Return an InputStream to read the contents of the file from.
	 * The user is responsible for closing the stream.
	 * @return the contents of the file as stream, or an empty stream if empty
	 * @throws IOException in case of access errors (if the temporary store fails)
	 */
	InputStream getInputStream() throws IOException;

	/**
	 * Transfer the received file to the given destination file.
	 * <p>This may either move the file in the filesystem, copy the file in the
	 * filesystem, or save memory-held contents to the destination file.
	 * If the destination file already exists, it will be deleted first.
	 * <p>If the file has been moved in the filesystem, this operation cannot
	 * be invoked again. Therefore, call this method just once to be able to
	 * work with any storage mechanism.
	 * @param dest the destination file
	 * @throws IOException in case of reading or writing errors
	 * @throws IllegalStateException if the file has already been moved
	 * in the filesystem and is not available anymore for another transfer
	 */
	void transferTo(File dest) throws IOException, IllegalStateException;

}
