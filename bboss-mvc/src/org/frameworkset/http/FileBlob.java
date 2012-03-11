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
	public FileBlob(String fileName, Blob data)
	{

		super();
		this.fileName = fileName;
		this.data = data;
	}

	private String fileName;
	private Blob data;
	
	public String getFileName()
	{
	
		return fileName;
	}
	
	
	
	public Blob getData()
	{
	
		return data;
	}
	
	

}
