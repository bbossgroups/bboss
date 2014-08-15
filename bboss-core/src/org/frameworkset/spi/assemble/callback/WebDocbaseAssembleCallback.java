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

package org.frameworkset.spi.assemble.callback;

import com.frameworkset.util.SimpleStringUtil;

/**
 * <p>Title: WebDocbaseAssembleCallback.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-10-4 ÉÏÎç10:53:01
 * @author biaoping.yin
 * @version 1.0
 */
public class WebDocbaseAssembleCallback implements AssembleCallback {
	
	private String webdocbase;
	private String docbaseType = AssembleCallback.webprex;
	public WebDocbaseAssembleCallback(String webdocbase)
	{
		this.webdocbase = webdocbase;
		if(this.webdocbase != null)
			this.webdocbase = this.webdocbase.replace('\\', '/');
	}
	public String getDocbasePath(String path) {
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>webdocbase:" + webdocbase);
		return SimpleStringUtil.getRealPath(webdocbase, path);
	}

	public String getDocbaseType() {
		
		return docbaseType;
	}
	public String getRootPath() {
		// TODO Auto-generated method stub
		return webdocbase;
	}
	
	
	

}
