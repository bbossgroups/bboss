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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.Tag;

import com.frameworkset.common.tag.pager.DataInfo;
import com.frameworkset.platform.cms.driver.jsp.CMSServletRequest;

/**
 * <p>DefaultCMSCallBack.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2005-2013 </p>
 * 
 * @Date 2013年10月26日
 * @author biaoping.yin
 * @version 1.0
 */
public class DefaultCMSCallBack implements CMSCallBack {
	private CMSServletRequest cmsrequest;
	public DefaultCMSCallBack(CMSServletRequest cmsrequest) {
		this.cmsrequest = cmsrequest;
	}

	@Override
	public String initContext(int maxPateItems) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initContextData(long offset, int newPageCount, long totalsize) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DataInfo getCMSDataInfo(Tag tag,HttpServletRequest request,String sortKey, boolean desc, long offset,
			int maxPageItems, boolean listMode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getCMSCellData() {
		// TODO Auto-generated method stub
		return null;
	}

}
