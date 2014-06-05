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
package org.frameworkset.http;

import java.io.File;
import java.io.InputStream;
import java.sql.Blob;


/**
 * <p>FileBlob.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2011-6-17
 * @author biaoping.yin
 * @version 1.0
 */
public class FileBlob
{
	private int rendtype = DOWNLOAD;
	public int getRendtype() {
		return rendtype;
	}
	public void setRendtype(int rendtype) {
		this.rendtype = rendtype;
	}

	public static int BROWSER = 0;
	public static int DOWNLOAD = 1;
	/**
	 * 0:file
	 * 1:blob
	 * 2:inputstream
	 */
	private int dataType = 0;
	public boolean isBlob()
	{
		return dataType == 1;
	}
	public FileBlob(String fileName, Blob data)
	{

		super();
		this.fileName = fileName;
		this.data = data;
		dataType = 1;
	}
	
	public FileBlob(String fileName, InputStream data)
	{

		super();
		this.fileName = fileName;
		this.data = data;
		dataType = 2;
	}
	public FileBlob( File data,int rendtype)
	{

		super();
		this.fileName = data.getName();
		this.data = data;
		this.rendtype = rendtype;
		dataType = 0;
	}
	
	
	public FileBlob( String file)
	{

		this(file,DOWNLOAD);
	}
	public FileBlob( File data)
	{

		this(data,DOWNLOAD);
	}
	
	
	public FileBlob( String file,int rendtype)
	{

		super();
		File data = new File(file);
		this.fileName = data.getName();
		this.data = data;
		this.rendtype = rendtype;
	}

	private String fileName;
	private Object data;
	
	public String getFileName()
	{
	
		return fileName;
	}
	public boolean isdownload()
	{
		return this.rendtype == this.DOWNLOAD;
	}
	public boolean isFile()
	{
		return dataType == 0;
	}
	
	public boolean isStream()
	{
		return dataType == 2;
	}
	
	public Blob getData()
	{
	
		return (Blob)data;
	}
	
	public File getFileData()
	{
	
		return (File)data;
	}
	
	public InputStream getInputStream()
	{
	
		return (InputStream)data;
	}
	
	

}
