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
package com.frameworkset.platform.cms.driver.callback;

import javax.servlet.jsp.tagext.Tag;

import org.apache.log4j.Logger;

import com.frameworkset.platform.cms.driver.jsp.CMSServletRequest;

/**
 * <p>CMSCallBackUtil.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2005-2013 </p>
 * 
 * @Date 2013年10月26日
 * @author biaoping.yin
 * @version 1.0
 */
public class CMSCallBackUtil {
	static CMSCallBackFactory cmsCallBackFactory;
	private static Logger log = Logger.getLogger(CMSCallBackUtil.class);
	static {
		try {
			cmsCallBackFactory = (CMSCallBackFactory) Class.forName("com.frameworkset.platform.cms.driver.callback.PlatformCMSCallBackFactory").newInstance();
		} catch (Exception e) {
			log.warn("Use DefaultCMSCallBackFactory");
			cmsCallBackFactory = new DefaultCMSCallBackFactory();
		}
	}
	public CMSCallBackUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static CMSCallBack getCMSCallBack(CMSServletRequest cmsrequest)
	{
		return cmsCallBackFactory.getCMSCallBack(cmsrequest);
	}
	
	public static CMSCallBack getCMSCallBack()
	{
		return cmsCallBackFactory.getCMSCallBack();
	}

	public static boolean isCMSListTag( Tag tag) {
		// TODO Auto-generated method stub
		return cmsCallBackFactory.isCMSListTag(  tag);
	}

}
