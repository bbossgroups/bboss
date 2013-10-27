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
package org.frameworkset.upload.dao.impl;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.upload.dao.UpLoadDao;
import org.frameworkset.web.multipart.MultipartFile;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.SQLParams;
import com.frameworkset.common.poolman.handle.FieldRowHandler;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.util.StringUtil;



/**
 * <p>ConfigSQLUploadDaoImpl.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2011-6-17
 * @author biaoping.yin
 * @version 1.0
 */
public class ConfigSQLUploadDaoImpl implements UpLoadDao
{
	private ConfigSQLExecutor executor ;
	/**
	 * 上传附件
	 * @param inputStream
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public boolean uploadFile(InputStream inputStream,long size, String filename) throws Exception {
		boolean result = true;
		
		try {
			
			SQLParams sqlparams = new SQLParams();
			sqlparams.addSQLParam("filename", filename, SQLParams.STRING);
			sqlparams.addSQLParam("FILECONTENT", inputStream, size,SQLParams.BLOBFILE);
			sqlparams.addSQLParam("FILEID", UUID.randomUUID().toString(),SQLParams.STRING);
			sqlparams.addSQLParam("FILESIZE", size,SQLParams.LONG);
			executor.insertBean("uploadFile", sqlparams);			
			
		} catch (Exception ex) {
			ex.printStackTrace();
			result = false;
			throw new Exception("上传附件关联临控指令布控信息附件失败：" + ex);
		} finally {
			if(inputStream != null){
				inputStream.close();
			}
		}
		return result;
	}
	
	public File getDownloadFile(String fileid) throws Exception
	{
		try
		{
			return executor.queryTField(
											File.class,
											new FieldRowHandler<File>() {

												@Override
												public File handleField(
														Record record)
														throws Exception
												{

													// 定义文件对象
													File f = new File(
															"d:/",
															record
																	.getString("filename"));
													// 如果文件已经存在则直接返回f
													if (f.exists())
														return f;
													// 将blob中的文件内容存储到文件中
													record
															.getFile(
																		"filecontent",
																		f);
													return f;
												}
											},
											"getDownloadFile",
											fileid);
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	@Override
	public void deletefiles() throws Exception
	{

		executor.delete("deletefiles");			
	}

	@Override
	public List<HashMap> queryfiles() throws Exception
	{

		return executor.queryList(HashMap.class, "queryfiles");
		
	}

	@Override
	public void downloadFileFromBlob(String fileid, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception
	{

		try
		{
			executor.queryByNullRowHandler(new NullRowHandler() {
				@Override
				public void handleRow(Record record) throws Exception
				{

					StringUtil.sendFile(request, response, record
							.getString("filename"), record
							.getBlob("filecontent"));
				}
			}, "downloadFileFromBlob", fileid);
		}
		catch (Exception e)
		{
			throw e;
		}
		
	}

	
	public ConfigSQLExecutor getExecutor()
	{
	
		return executor;
	}

	
	public void setExecutor(ConfigSQLExecutor executor)
	{
	
		this.executor = executor;
	}

	@Override
	public void downloadFileFromClob(String fileid, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception
	{

		try
		{
			executor.queryByNullRowHandler(new NullRowHandler() {
				@Override
				public void handleRow(Record record) throws Exception
				{

					StringUtil.sendFile(request, response, record
							.getString("filename"), record
							.getClob("filecontent"));
				}
			}, "getDownloadClobFile", fileid);
		}
		catch (Exception e)
		{
			throw e;
		}
		
	}

	@Override
	public File getDownloadClobFile(String fileid) throws Exception
	{

		try
		{
			return executor.queryTField(
											File.class,
											new FieldRowHandler<File>() {

												@Override
												public File handleField(
														Record record)
														throws Exception
												{

													// 定义文件对象
													File f = new File("d:/",record.getString("filename"));
													// 如果文件已经存在则直接返回f
													if (f.exists())
														return f;
													// 将blob中的文件内容存储到文件中
													record.getFile("filecontent",f);
													return f;
												}
											},
											"getDownloadClobFile",
											fileid);
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	@Override
	public List<HashMap> queryclobfiles() throws Exception
	{

		return executor.queryList(HashMap.class, "queryclobfiles");
	}

	@Override
	public void uploadClobFile(MultipartFile file) throws Exception
	{

		try {
			
			SQLParams sqlparams = new SQLParams();
			sqlparams.addSQLParam("filename", file.getOriginalFilename(), SQLParams.STRING);
			sqlparams.addSQLParam("FILECONTENT", file,SQLParams.CLOBFILE);
			sqlparams.addSQLParam("FILEID", UUID.randomUUID().toString(),SQLParams.STRING);
			sqlparams.addSQLParam("FILESIZE", file.getSize(),SQLParams.LONG);
			executor.insertBean("uploadClobFile", sqlparams);			
			
		} catch (Exception ex) {
		
			
			throw new Exception("上传附件失败：" + ex);
		} 
		
	}

	

}
