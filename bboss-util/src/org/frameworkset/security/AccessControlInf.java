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
import javax.servlet.jsp.JspWriter;

/**
 * <p>AccessControlInf.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2005-2013 </p>
 * 
 * @Date 2013Äê10ÔÂ26ÈÕ
 * @author biaoping.yin
 * @version 1.0
 */
public interface AccessControlInf {

	boolean checkAccess(HttpServletRequest request, HttpServletResponse response,
			JspWriter out,boolean protect);

	String getCurrentSystemID();

	String getUserID();

	boolean checkPermission(String resid, String action, String restype);

	boolean isAdmin();

	boolean isOrganizationManager(String orgId);

	boolean isSubOrgManager(String orgId);

	boolean isGrantedRole(String string);

	String getUserName();

	String getUserAccount();

	String getUserAttribute(String variableName);

	String getChargeOrgId();

}
