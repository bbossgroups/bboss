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
 * <p>DefaultAccessControl.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2005-2013 </p>
 * 
 * @Date 2013年10月26日
 * @author biaoping.yin
 * @version 1.0
 */
public class DefaultAccessControl implements AccessControlInf {

	public DefaultAccessControl() {
		// TODO Auto-generated constructor stub
	}

	public boolean checkAccess(HttpServletRequest request,
			HttpServletResponse response, JspWriter out, boolean protect) {
		return true;
		
	}

	public String getCurrentSystemID() {
		// TODO Auto-generated method stub
		return "";
	}

	public String getUserID() {
		// TODO Auto-generated method stub
		return "";
	}

	public boolean checkPermission(String resid, String action, String restype) {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isAdmin() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isOrganizationManager(String orgId) {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isSubOrgManager(String orgId) {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isGrantedRole(String string) {
		// TODO Auto-generated method stub
		return true;
	}

	public String getUserName() {
		// TODO Auto-generated method stub
		return "";
	}

	public String getUserAccount() {
		// TODO Auto-generated method stub
		return "";
	}

	public String getUserAttribute(String variableName) {
		// TODO Auto-generated method stub
		return "";
	}

	public String getChargeOrgId() {
		// TODO Auto-generated method stub
		return "";
	}

	public boolean allowIfNoRequiredRoles(String resourceType) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean checkAccess(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean checkAccess(HttpServletRequest request,
			HttpServletResponse response, boolean protect) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean checkAdminAccess(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean checkManagerAccess(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean checkPermission(String resourceID, String action,
			String resourceType, boolean redirect, String redirectPath) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean checkURLPermission(String uri) {
		// TODO Auto-generated method stub
		return false;
	}

	public HttpServletRequest getRequest() {
		// TODO Auto-generated method stub
		return null;
	}

	public HttpSession getSession() {
		// TODO Auto-generated method stub
		return null;
	}

	public PageContext getPageContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isGuest() {
		// TODO Auto-generated method stub
		return false;
	}

}
