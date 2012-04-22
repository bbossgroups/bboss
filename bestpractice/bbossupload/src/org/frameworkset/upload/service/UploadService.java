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

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.upload.dao.UpLoadDao;
import org.frameworkset.web.multipart.MultipartFile;


/**
 * <p>UploadService.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2011-6-17
 * @author biaoping.yin
 * @version 1.0
 */
public class UploadService
{
	private UpLoadDao uploadDao;
	public UpLoadDao getUploadDao() {
		return uploadDao;
	}
	public void setUploadDao(UpLoadDao uploadDao) {
		this.uploadDao = uploadDao;
	}
	public void deletefiles() throws Exception
	{

		uploadDao.deletefiles();
		
	}
	public List<HashMap> queryfiles() throws Exception
	{

		// TODO Auto-generated method stub
		return uploadDao.queryfiles();
	}
	public void uploadFile(InputStream inputStream, long size, String filename) throws Exception
	{

		uploadDao.uploadFile(inputStream, size, filename);
		
	}
	
	
	
	public void uploadClobFile(MultipartFile file) throws Exception
	{

		uploadDao.uploadClobFile(file);
		
	}
	
	public File getDownloadFile(String fileid) throws Exception
	{
		return uploadDao.getDownloadFile(fileid);
	}
	
	public void downloadFileFromBlob(String fileid,  HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		uploadDao.downloadFileFromBlob(fileid, request, response);
	}
	
	public void downloadFileFromClob(String fileid,  HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		uploadDao.downloadFileFromClob(fileid, request, response);
	}
	public  List<HashMap> queryclobfiles() throws Exception
	{

		// TODO Auto-generated method stub
		return uploadDao.queryclobfiles();
	}
	public File getDownloadClobFile(String fileid) throws Exception
	{

		return uploadDao.getDownloadClobFile(fileid);
	}
}
