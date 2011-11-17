/**
 * Copyright 2008 biaoping.yin Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.frameworkset.upload.controller;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.upload.service.FileBean;
import org.frameworkset.upload.service.UploadService;
import org.frameworkset.util.annotations.RequestParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.multipart.MultipartFile;
import org.frameworkset.web.multipart.MultipartHttpServletRequest;
import org.frameworkset.web.servlet.ModelMap;

/**
 * CREATE TABLE FILETABLE ( FILENAME VARCHAR(100), FILECONTENT BLOB(2147483647),
 * FILEID VARCHAR(100), FILESIZE BIGINT )
 * 
 * <p>
 * XMLRequestController.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2009
 * </p>
 * 
 * @Date 2011-6-22
 * @author biaoping.yin
 * @version 1.0
 */
public class UploadController
{

	private UploadService	uploadService;

	public String main(ModelMap model) throws Exception
	{

		try
		{
			model.addAttribute("files", uploadService.queryfiles());
			model.addAttribute("clobfiles", uploadService.queryclobfiles());
		}
		catch (Exception e)
		{
			throw e;
		}
		return "path:main";
	}

	/**
	 * @param request
	 * @param model
	 * @param idNum
	 * @param type
	 * @param des
	 * @param byid
	 * @return
	 */
	public String uploadFile(MultipartHttpServletRequest request)
	{
		Iterator<String> fileNames = request.getFileNames();
		// 根据服务器的文件保存地址和原文件名创建目录文件全路径		
		try
		{
			while (fileNames.hasNext())
			{
				String name = fileNames.next();
				MultipartFile[] files = request.getFiles(name);				 
//				file.transferTo(dest)
				for(MultipartFile file:files)
				{
					String filename = file.getOriginalFilename();
					if (filename != null && filename.trim().length() > 0)
					{
						uploadService.uploadFile(file.getInputStream(), file
								.getSize(), filename);
					}
				}
			}			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return "path:ok";
	}
	
	
	/**
	 * @param request
	 * @param model
	 * @param idNum
	 * @param type
	 * @param des
	 * @param byid
	 * @return
	 */
	public String uploadFileWithMultipartFile(@RequestParam(name="upload1")  MultipartFile file)
	{

		
		// 根据服务器的文件保存地址和原文件名创建目录文件全路径
		
		try
		{
			String filename = file.getOriginalFilename();
			if (filename != null && filename.trim().length() > 0)
			{
				uploadService.uploadFile(file.getInputStream(), file
						.getSize(), filename);

			}			
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return "path:ok";
	}
	
	public @ResponseBody File uploaddownFileWithMultipartFile( MultipartFile file) throws IllegalStateException, IOException
	{

		File f = new File("d:/" + file.getOriginalFilename());
		file.transferTo(f);
		return f;
		// 根据服务器的文件保存地址和原文件名创建目录文件全路径
		
//		try
//		{
//			String filename = file.getOriginalFilename();
//			if (filename != null && filename.trim().length() > 0)
//			{
//				uploadService.uploadFile(file.getInputStream(), file
//						.getSize(), filename);
//
//			}			
//			
//		}
//		catch (Exception ex)
//		{
//			ex.printStackTrace();
//		}
//		return "path:ok";
	}
	
	/**
	 * @param request
	 * @param model
	 * @param idNum
	 * @param type
	 * @param des
	 * @param byid
	 * @return
	 */
	public String uploadFileWithListBean(List<FileBean> files)
	{			
		
		return "path:ok";
	}
	
	/**
	 * @param request
	 * @param model
	 * @param idNum
	 * @param type
	 * @param des
	 * @param byid
	 * @return
	 */
	public String uploadFileWithMultipartFiles(@RequestParam(name="upload1")  MultipartFile[] files)
	{		
		try
		{ 
			
			for(MultipartFile file:files)
			{
				String filename = file.getOriginalFilename();
//				file.transferTo(new File("d:/"+ filename));
				
				if (filename != null && filename.trim().length() > 0)
				{
					uploadService.uploadFile(file.getInputStream(), file
							.getSize(), filename);
	
				}
			}			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return "path:ok";
	}
	
	/**
	 * @param request
	 * @param model
	 * @param idNum
	 * @param type
	 * @param des
	 * @param byid
	 * @return
	 */
	public @ResponseBody(charset="GBK") String uploadFileWithMultipartFilesJson(@RequestParam(name="upload1")  MultipartFile[] files,HttpServletResponse response)
	{		
		try
		{ 

			for(MultipartFile file:files)
			{
				String filename = file.getOriginalFilename();
//				file.transferTo(new File("d:/"+ filename));
				
				if (filename != null && filename.trim().length() > 0)
				{
					uploadService.uploadFile(file.getInputStream(), file
							.getSize(), filename);
	
				}
			}			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return "你好";
	}
	
	/**
	 * @param request
	 * @param model
	 * @param idNum
	 * @param type
	 * @param des
	 * @param byid
	 * @return
	 */
	public String uploadFileWithFileBean(FileBean file)
	{	
		try
		{
			
			//对FileBean对象中的附件进行处理。。。。
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return "path:ok";
	}

	public String deletefiles() throws Exception
	{

		uploadService.deletefiles();
		return "path:ok";
	}

	public String queryfiles() throws Exception
	{

		
		return "path:ok";
	}

	/**
	 * 直接将blob对应的文件内容以相应的文件名响应到客户端，需要提供request和response对象
	 * 这个方法比较特殊，因为derby数据库的blob字段必须在statement有效范围内才能使用，所以采用了空行处理器，来进行处理
	 * 查询数据库的操作也只好放在控制器中处理
	 * @param fileid
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void downloadFileFromBlob(
			@RequestParam(name = "fileid") String fileid,
			 HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		uploadService.downloadFileFromBlob(fileid, request, response);

	}
	
	/**
	 * 直接将blob对应的文件内容以相应的文件名响应到客户端，需要提供request和response对象
	 * 这个方法比较特殊，因为derby数据库的blob字段必须在statement有效范围内才能使用，所以采用了空行处理器，来进行处理
	 * 查询数据库的操作也只好放在控制器中处理
	 * @param fileid
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void downloadFileFromClob(
			@RequestParam(name = "fileid") String fileid,
			 HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{

		uploadService.downloadFileFromClob(fileid, request, response);

	}

	/**
	 * 将数据库中存储的文件内容转储到应用服务器文件目录中，然后将转储的文件下载，无需提供response和request对象
	 * 
	 * @param fileid
	 * @return
	 * @throws Exception
	 */
	public @ResponseBody File downloadFileFromFile(@RequestParam(name = "fileid") String fileid)
			throws Exception
	{

		return uploadService.getDownloadFile(fileid);
	}
	public @ResponseBody File downloadFileFromClobFile(@RequestParam(name = "fileid") String fileid)
	throws Exception
	{
	
		return uploadService.getDownloadClobFile(fileid);
	}
	

	public UploadService getUploadService()
	{

		return uploadService;
	}

	public void setUploadService(UploadService uploadService)
	{

		this.uploadService = uploadService;
	}

}
