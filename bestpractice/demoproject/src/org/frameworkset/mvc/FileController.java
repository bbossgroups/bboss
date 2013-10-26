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
package org.frameworkset.mvc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.multipart.IgnoreFieldNameMultipartFile;
import org.frameworkset.web.multipart.MultipartFile;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.util.StringUtil;

/**
 * <p> FileController.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2012-5-14 ÏÂÎç3:25:35
 * @author biaoping.yin
 * @version 1.0
 */
public class FileController {
	
	public static String getWorkFoldPath()
	{
		return org.frameworkset.web.servlet.support.WebApplicationContextUtils.getWebApplicationContext().getProperty("file.workfolder");
	}
	private String filedomain;
	private String rootPath;
	public String fileupload()
	{
		return "path:fileupload";
	}
	
	public String foldertree()
	{
		return "path:foldertree";
	}
	public String filelist(String uri,ModelMap model) throws Exception
	{
		if(uri == null)
			uri = "";
		List files = getDirectoryResource(uri);
		model.addAttribute("files", files);
		return "path:filelist";
	}
	public @ResponseBody String mkdir(String workfolder,String uri)
	{
		
		File file = new File(rootPath,workfolder);
		boolean s = false;
		if(!file.exists())
		{
			
			file.mkdirs();
		}
		File d = new File(file.getAbsolutePath(),uri);
		if(!d.exists())
		{
			
			s = d.mkdirs();
		}
		return s?"success":"fail";
			
	}
	public List getDirectoryResource(String uri)
			throws Exception {
		
		List fileResources = new ArrayList();
		File[] subFiles = FileUtil.getSubFiles(rootPath, uri);
		for (int i = 0; subFiles != null && i < subFiles.length; i++) {
			FileResource fr = new FileResource();
			String theURI = "";
			if (uri != null && uri.trim().length() != 0) {
				uri = uri.replace('\\', '/');
				if (!uri.endsWith("/")) {
					theURI = uri + "/";
				} else {
					theURI = uri;
				}
				if (theURI.trim().equals("/")) {
					theURI = "";
				}
			}
			fr.setUri(filedomain + theURI + subFiles[i].getName());			
			fr.setName(subFiles[i].getName());
			fr.setSize(subFiles[i].length());
			fr.setModifyTime(new Date(subFiles[i].lastModified()));
			fileResources.add(fr);
		}
		return fileResources;
	}
	
	public String uptable()
	{
		return "path:uptable";
	}
	public @ResponseBody String uploadFiles(IgnoreFieldNameMultipartFile[] filedata,boolean overide ,String workfolder)
	{
		File file = new File(rootPath,workfolder);
		StringBuffer msg = new StringBuffer(); 
		for(int i =0; filedata != null && i < filedata.length; i ++)
		{
			MultipartFile file_ = filedata[i];
			File f = new File(file.getAbsolutePath(),file_.getOriginalFilename());
			if(f.exists() && !overide)
			{
				continue;
			}
			else
			{
				try {
					file_.transferTo(f);
				} catch (Exception e) {
					msg.append(StringUtil.formatBRException(e));
				}
			}
		}
		
		if(msg.length() == 0)
			return "success";
		else
			return msg.toString();
	}
	
	
	public @ResponseBody String upload(IgnoreFieldNameMultipartFile[] filedata,String testparam) throws IllegalStateException, IOException
	{
		if(filedata != null)
		{
			filedata[0].transferTo(new File("d:/tst.png"));
		}
		return "{\"err\":\"\",\"msg\":\"tst.png\"}";
	}
	
	public @ResponseBody String uploadwithbean(FileBean filedata,String testparam) throws IllegalStateException, IOException
	{
		if(filedata != null)
		{
			filedata.getFile().transferTo(new File("d:/tst.png"));
		}
		return "{\"err\":\"\",\"msg\":\"tst.png\"}";
	}
	
	public static void main(String[] args)
	{
		String dispoString = "attachment; name=\"filedata\"; filename=\"TempPlanDiagram.png\"";
		 int iFindStart = dispoString.indexOf(" filename=\"") + 11;  
         int iFindEnd = dispoString.indexOf("\"", iFindStart);  
         String sFileName = dispoString.substring(iFindStart, iFindEnd);
         iFindStart = dispoString.indexOf(" name=\"") + 7;  
         iFindEnd = dispoString.indexOf("\"", iFindStart);  
         String sFieldName = dispoString.substring(iFindStart, iFindEnd);
	}

}
