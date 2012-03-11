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
package org.frameworkset.web.demo;

/**
 * <p>Title: UserController.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2011-1-5
 * @author biaoping.yin
 * @version 1.0
 */

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.frameworkset.util.annotations.Attribute;
import org.frameworkset.util.annotations.HandlerMapping;
import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.RequestParam;
import org.frameworkset.web.servlet.ModelMap;
import org.frameworkset.web.servlet.handler.annotations.Controller;

import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;

@Controller
public class UserController {
	UserService userService;
	
	private String updateUserErrorView;
	private String updateUserSuccessView;
	private String addUserSuccessView;
	private String deleteUserSuccessView;
	private String showUserView;
	private String showUserListView;
	/**
	 * http://localhost:8080/bboss-mvc/updateUser.htm
	 * @param userTemp
	 * @param model
	 * @return
	 * @throws UserManagerException
	 */
	@HandlerMapping(value = "/updateUser.htm")
	public String updateUser(User userTemp, ModelMap model) throws UserManagerException {
		try
		{
			if(model.hasErrors())
			{
				model.addAttribute("user", userTemp);
				return updateUserErrorView;
			}
			else
			{
				userService.updateUser(userTemp);
				model.addAttribute("userId", userTemp.getUserId());
				return updateUserSuccessView;
			}
		}
		catch(UserManagerException e)
		{
			model.addAttribute("user", userTemp);
			throw e;
		}
	}
	
	/**
	 * http://localhost:8080/bboss-mvc/addUser.htm
	 * @param userTemp
	 * @param model
	 * @return
	 * @throws UserManagerException
	 */
	@HandlerMapping(value = "/addUser.htm")
	public String addUser(User userTemp, ModelMap model) throws UserManagerException {
		try
		{
			userService.addUser(userTemp);
			model.addAttribute("userId", userTemp.getUserId());
			return addUserSuccessView;
		}
		catch(UserManagerException e)
		{
			model.addAttribute("user", userTemp);
			throw e;
		}
	}
	
	/**
	 * http://localhost:8080/bboss-mvc/deleteUser.htm
	 * @param userId
	 * @param model
	 * @return
	 * @throws UserManagerException
	 */
	@HandlerMapping(value = "/deleteUser.htm")
	public String deleteUser(@RequestParam(name = "userId") int userId,ModelMap model) throws UserManagerException {
		try
		{
			userService.deleteUser(userId);
			model.addAttribute("userId",userId);
			return deleteUserSuccessView;
		}
		catch(UserManagerException e)
		{
			throw e;
		}
	}
	
	
	/**
	 * http://localhost:8080/bboss-mvc/deleteUser.htm
	 * @param userId
	 * @param model
	 * @return
	 * @throws UserManagerException
	 */
	@HandlerMapping(value = "/deleteAllUser.htm")
	public String deleteAllUser(ModelMap model) throws UserManagerException {
		try
		{
			userService.deleteAllUser();
			model.addAttribute("delete","ok");
			return deleteUserSuccessView;
		}
		
		catch (SQLException e)
		{
			throw new UserManagerException(e);
		}
	}

	/**
	 * http://localhost:8080/bboss-mvc/userinfo.htm
	 * @param userId
	 * @param model
	 * @return
	 * @throws UserManagerException
	 */
	@HandlerMapping(value = "/userinfo.htm")
	public String queryuser(@RequestParam(name = "userId")
						    @Attribute(name = "userId",required=true,defaultvalue="2") int userId,
			ModelMap model) throws UserManagerException {
		if(model.hasErrors())
			return showUserView;
		User userTemp = userService.getUser(userId);
		if(userTemp == null)
			model.getErrors().rejectValueWithErrorArgs("message", "user.not.exist",new Object[]{userId});
		model.addAttribute("user", userTemp);
		return showUserView;
	}
	/**
	 * http://localhost:8080/bboss-mvc/pagerqueryuser.htm
	 */
	@HandlerMapping(value = "/pagerqueryuser.htm")
	public String pagerqueryuser(@PagerParam(name=PagerParam.SORT ) String sortKey,
							@PagerParam(name=PagerParam.DESC,defaultvalue="true") boolean desc,
							@PagerParam(name=PagerParam.OFFSET) long offset,
							@PagerParam(name=PagerParam.PAGE_SIZE,defaultvalue="2") int pagesize,
							@RequestParam(name = "userName") String username ,
							ModelMap model) throws UserManagerException {
		if(model.hasErrors())
			return showUserListView;
//		if(username==null||username.equals(""))return showUserListView;
		ListInfo userTemp = userService.getUsersNullRowHandler(username,offset, pagesize);
		if(userTemp == null)
			model.getErrors().rejectValueWithErrorArgs("message", "user.not.exist",new Object[]{username});
		model.addAttribute("users", userTemp);
		return showUserListView;
	}
	
	@HandlerMapping(value = "/queryuser.htm")
	public String queryuserbyrequest(HttpServletRequest request,ModelMap model) throws UserManagerException {
		String userId = StringUtil.getParameter(request, "userId", "2");
		int userId_ = Integer.parseInt(userId);
		User userTemp = userService.getUser(userId_);
		if(userTemp == null)
		{
			model.getErrors().rejectValueWithErrorArgs("message", "user.not.exist",new Object[]{userId});
			model.getErrors().reject("user.not.exception");
		}
		model.addAttribute("user", userTemp);
		return showUserView;
}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public String userManagerExceptionHandler(HttpServletRequest request,HttpServletResponse response,
											PageContext pageContext,
											UserManagerException e,
											ModelMap model)
	{
		model.addAttribute("error_message", e.getMessage());
		return showUserView;
	}

	/**
	 * @return the updateUserErrorView
	 */
	public String getUpdateUserErrorView() {
		return updateUserErrorView;
	}

	/**
	 * @param updateUserErrorView the updateUserErrorView to set
	 */
	public void setUpdateUserErrorView(String updateUserErrorView) {
		this.updateUserErrorView = updateUserErrorView;
	}

	/**
	 * @return the updateUserSuccessView
	 */
	public String getUpdateUserSuccessView() {
		return updateUserSuccessView;
	}

	/**
	 * @param updateUserSuccessView the updateUserSuccessView to set
	 */
	public void setUpdateUserSuccessView(String updateUserSuccessView) {
		this.updateUserSuccessView = updateUserSuccessView;
	}

	/**
	 * @return the addUserSuccessView
	 */
	public String getAddUserSuccessView() {
		return addUserSuccessView;
	}

	/**
	 * @param addUserSuccessView the addUserSuccessView to set
	 */
	public void setAddUserSuccessView(String addUserSuccessView) {
		this.addUserSuccessView = addUserSuccessView;
	}

	/**
	 * @return the deleteUserSuccessView
	 */
	public String getDeleteUserSuccessView() {
		return deleteUserSuccessView;
	}

	/**
	 * @param deleteUserSuccessView the deleteUserSuccessView to set
	 */
	public void setDeleteUserSuccessView(String deleteUserSuccessView) {
		this.deleteUserSuccessView = deleteUserSuccessView;
	}

	/**
	 * @return the showUserView
	 */
	public String getShowUserView() {
		return showUserView;
	}

	/**
	 * @param showUserView the showUserView to set
	 */
	public void setShowUserView(String showUserView) {
		this.showUserView = showUserView;
	}

	/**
	 * @param showUserListView the showUserListView to set
	 */
	public void setShowUserListView(String showUserListView) {
		this.showUserListView = showUserListView;
	}
}
