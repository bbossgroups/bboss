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
package org.frameworkset.spi.mvc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.util.annotations.Attribute;
import org.frameworkset.util.annotations.AttributeScope;
import org.frameworkset.util.annotations.DataBind;
import org.frameworkset.util.annotations.ModelAttribute;
import org.frameworkset.util.annotations.RequestParam;
import org.frameworkset.web.multipart.MultipartFile;
import org.frameworkset.web.multipart.MultipartHttpServletRequest;
import org.frameworkset.web.servlet.ModelAndView;
import org.frameworkset.web.servlet.mvc.MultiActionController;

/**
 * <p>Title: DemoMutiActionController.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-16
 * @author biaoping.yin
 * @version 1.0
 */
public class DemoMutiActionController extends MultiActionController {
	private String queryUsersViewName = "userList";
	private String viewName = "queryUsersViewName";
//	private String uploadSuccess = "/jsp/uploadsuccess.jsp";
	private String uploadSuccess = "uploadsuccess";
	/**
	 * http:/localhost:8080/bboss-mvc/demo/queryUsers.htm
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView queryUsers(HttpServletRequest request,HttpServletResponse response)
	{
		List<UserBean> users = new ArrayList<UserBean>();
		UserBean user = new UserBean();
		for(int i = 0; i<100; i ++)
		{
			user = new UserBean();
			user.setId("" + i);
			user.setUserName("duoduo");
			user.setUserPassword((123456 + i) + "" );
			users.add(user);
		}
		return new ModelAndView(getQueryUsersViewName(),"userList",users);
	}
	
	/**
	 * http:/localhost:8080/bboss-mvc/demo/genrateImg.htm
	 * @param request
	 * @param response
	 */
	public void genrateImg(HttpServletRequest request,HttpServletResponse response)
	{
		List<UserBean> users = new ArrayList<UserBean>();
		UserBean user = new UserBean();
		for(int i = 0; i<100; i ++)
		{
			user = new UserBean();
			user.setId("" + i);
			user.setUserName("duoduo");
			user.setUserPassword((123456 + i) + "" );
			users.add(user);
		}
//		response.getOutputStream();
//		return new ModelAndView(getQueryUsersViewName(),"userList",users);
	}
	
	/**
	 * http:/localhost:8080/bboss-mvc/demo/queryPagineUsers.htm
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView queryPagineUsers(HttpServletRequest request,HttpServletResponse response)
	{
		
		return new ModelAndView("pagineUserList");
	}

	/**
	 * //http:/localhost:8080/bboss-mvc/demo/deleteUser.htm
	 * @param request
	 * @param response
	 * @return
	 */
	public String deleteUser(HttpServletRequest request,HttpServletResponse response)
	{
		return getQueryUsersViewName();
	}
	 //http:/localhost:8080/bboss-mvc/demo/updateUser.htm
	 
	public String updateUser(@RequestParam(name="name") String name,HttpServletRequest request,HttpServletResponse response)
	{
		System.out.println("name param:"+name);
		return getQueryUsersViewName();
	}
	
	/**
	 * //http:/localhost:8080/bboss-mvc/demo/updateUserFromRequestParam.htm?name=duoduo
	 * @param name
	 * @param request
	 * @param response
	 * @return
	 */
	public String updateUserFromRequestParam(@RequestParam(name="name") String name,HttpServletRequest request,HttpServletResponse response)
	{
		System.out.println("name param:"+name);
		return getQueryUsersViewName();
	}
	
	
	public String updateUserFromModelMapParam(@ModelAttribute(name="name") String name,HttpServletRequest request,HttpServletResponse response)
	{
		System.out.println("name param:"+name);
		return getQueryUsersViewName();
	}
	
	public String updateUserFromSessionParam(@Attribute(name="name",scope=AttributeScope.REQUEST_ATTRIBUTE) String name,HttpServletRequest request,HttpServletResponse response)
	{
		System.out.println("name param:"+name);
		return getQueryUsersViewName();
	}
	
	public ModelAndView addUser(@DataBind UserBean user,HttpServletRequest request,HttpServletResponse response)
	{
		System.out.println("name param:"+user.getUserName());//http:/localhost:8080/bboss-mvc/demo/addUser.htm?id=1&userName=duoduo&userPassword=123456
		return new ModelAndView(getQueryUsersViewName(),"user",user);
	}
	
	public ModelAndView uploadFiles(MultipartHttpServletRequest request,HttpServletResponse response,
			@RequestParam(name="submit") String submit)
	{
		Iterator<String> fileNames = request.getFileNames();
		List<FileBean> files = new ArrayList<FileBean>();
		File dir = new File("d:/mutifiles/");
		if(!dir.exists())
			dir.mkdirs();
		while(fileNames.hasNext())
		{
			String name = fileNames.next();
			MultipartFile file = request.getFile(name);
			
			try {
				file.transferTo(new File("d:/mutifiles/" + file.getOriginalFilename()));
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			FileBean filebean = new FileBean();
			filebean.setFileName(file.getOriginalFilename());
			filebean.setFilePath("d:/mutifiles/" + file.getOriginalFilename());
			filebean.setFileSize(file.getSize());
			files.add(filebean);
		}
//		return new ModelAndView(new RedirectView(uploadSuccess,true),"uploadfiles",files);
		return new ModelAndView(uploadSuccess,"uploadfiles",files);
	}
	//http:/localhost:8080/bboss-mvc/demo/uploadjsp.htm?id=1&userName=duoduo&userPassword=123456
	public ModelAndView uploadjsp(HttpServletRequest request,HttpServletResponse response)
	{
		
		return new ModelAndView("fileupload");
	}
	public String getQueryUsersViewName() {
		return queryUsersViewName;
	}
//	public void setQueryUsersViewName(String queryUsersViewName) {
//		this.queryUsersViewName = queryUsersViewName;
//	}
//
//	public String getViewName() {
//		return viewName;
//	}
//
//	public void setViewName(String viewName) {
//		this.viewName = viewName;
//	}
}
