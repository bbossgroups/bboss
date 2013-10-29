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

import org.apache.log4j.Logger;

/**
 * <p>SecurityUtil.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2005-2013 </p>
 * 
 * @Date 2013年10月26日
 * @author biaoping.yin
 * @version 1.0
 */
public class SecurityUtil {
	private static Logger log = Logger.getLogger(SecurityUtil.class);
	private static AccessControlFactory accessControlFactory;
	static
	{
		try {
			accessControlFactory = (AccessControlFactory) Class.forName("com.frameworkset.platform.security.PlatformAccessControlFactoryImpl").newInstance();
		} catch (Exception e) {
			log.warn("Use DefaultAccessControlFactoryImpl");
			accessControlFactory = new DefaultAccessControlFactoryImpl();
		}
	}

	public SecurityUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static AccessControlInf getAccessControl(HttpServletRequest request,HttpServletResponse response,javax.servlet.jsp.JspWriter out)
	{
		return accessControlFactory.getAccessControl( request, response, out);
	}

}
