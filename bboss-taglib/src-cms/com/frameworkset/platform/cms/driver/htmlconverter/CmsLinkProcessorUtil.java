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
package com.frameworkset.platform.cms.driver.htmlconverter;

import org.apache.log4j.Logger;

import com.frameworkset.platform.cms.driver.jsp.ContextInf;

/**
 * <p>CmsLinkProcessorUtil.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2005-2013 </p>
 * 
 * @Date 2013年10月26日
 * @author biaoping.yin
 * @version 1.0
 */
public class CmsLinkProcessorUtil {
	private static CmsLinkProcessorFactory cmsLinkProcessorFactory;
	private static Logger log = Logger.getLogger(CmsLinkProcessorUtil.class);
	static {
		try {
			cmsLinkProcessorFactory = (CmsLinkProcessorFactory) Class.forName("com.frameworkset.platform.cms.driver.htmlconverter.PlatformCmsLinkProcessorFactory").newInstance();
		} catch (Exception e) {
			log.warn("Use DefaultCmsLinkProcessorFactory");
			cmsLinkProcessorFactory = new DefaultCmsLinkProcessorFactory();
		}
		
	}
	public CmsLinkProcessorUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static CmsLinkProcessorInf getCmsLinkProcessor(ContextInf context,int m_mode, String encode)
	{
		return cmsLinkProcessorFactory.getCmsLinkProcessor(context, m_mode, encode);
	}

}
