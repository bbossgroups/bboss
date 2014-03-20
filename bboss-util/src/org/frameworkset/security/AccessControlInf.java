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
package org.frameworkset.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

/**
 * <p>AccessControlInf.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2005-2013 </p>
 * 
 * @Date 2013年10月26日
 * @author biaoping.yin
 * @version 1.0
 */
public interface AccessControlInf {

	boolean checkAccess(HttpServletRequest request, HttpServletResponse response,
			JspWriter out,boolean protect);

	String getCurrentSystemID();

	String getUserID();



	boolean isAdmin();

	boolean isOrganizationManager(String orgId);

	boolean isSubOrgManager(String orgId);

	boolean isGrantedRole(String string);

	String getUserName();

	String getUserAccount();

	String getUserAttribute(String variableName);

	String getChargeOrgId();
	
	public boolean allowIfNoRequiredRoles(String resourceType);
	public boolean checkAccess(HttpServletRequest request,
			HttpServletResponse response) ;
	public boolean checkAccess(HttpServletRequest request,
			HttpServletResponse response, boolean protect) ;
	

	
	
	/**
	 * 检测当前登陆用户是否是管理员
	 */
	public boolean checkAdminAccess(HttpServletRequest request,
			HttpServletResponse response);
	/**
	 * 检测当前登陆用户是否是管理员或者拥有超级管理员角色
	 */
	public boolean checkManagerAccess(HttpServletRequest request,
			HttpServletResponse response) ;
	/**
	 * 检测当前系统用户是否拥有访问资源的权限
	 * 
	 * @param resourceID
	 * @param action
	 * @param resourceType
	 * @return
	 */
	public boolean checkPermission(String resourceID, String action,
			String resourceType);
	/**
	 * 检测当前系统用户是否拥有访问资源的权限，如果没有则跳转到权限提示页面 否则允许用户访问当前资源
	 * 根据条件redirect决定是否跳转，true表示跳转，false表示不跳转
	 * 
	 * @param resourceID
	 * @param action
	 * @param resourceType
	 * @param redirect
	 * @return
	 */
	public boolean checkPermission(String resourceID, String action,
			String resourceType, boolean redirect, String redirectPath);
	/**
	 * 检测当前系统用户是否拥有访问资源的权限
	 * 
	 * @param resourceID
	 * @param action
	 * @param resourceType
	 * @return
	 */
	public boolean checkURLPermission(String uri) ;
	
	public HttpServletRequest getRequest();
	public HttpSession getSession();
	public PageContext getPageContext() ;
	public boolean isGuest();
}
