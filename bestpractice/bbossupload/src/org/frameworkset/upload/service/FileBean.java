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
package org.frameworkset.upload.service;

import org.frameworkset.util.annotations.RequestParam;
import org.frameworkset.web.multipart.MultipartFile;


/**
 * <p>FileBean.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2011-7-22
 * @author biaoping.yin
 * @version 1.0
 */
public class FileBean
{
	private MultipartFile upload1;
	private String upload1des;
//	@RequestParam(name="upload1")
//	private MultipartFile[] uploads;
//	@RequestParam(name="upload1")
//	private MultipartFile anupload;

	
	public MultipartFile getUpload1()
	{
	
		return upload1;
	}

	
	public void setUpload1(MultipartFile upload1)
	{
	
		this.upload1 = upload1;
	}


	
	public String getUpload1des()
	{
	
		return upload1des;
	}


	
	public void setUpload1des(String upload1des)
	{
	
		this.upload1des = upload1des;
	}


	
//	public MultipartFile[] getUploads()
//	{
//	
//		return uploads;
//	}


	
//	public void setUploads(MultipartFile[] uploads)
//	{
//	
//		this.uploads = uploads;
//	}
//
//
//	
//	public MultipartFile getAnupload()
//	{
//	
//		return anupload;
//	}


//	
//	public void setAnupload(MultipartFile anupload)
//	{
//	
//		this.anupload = anupload;
//	}


	
}
