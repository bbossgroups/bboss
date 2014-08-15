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
package org.frameworkset.spi.assemble;

import org.frameworkset.spi.assemble.callback.AssembleCallback;

/**
 * 
 * 
 * <p>Title: ManagerImport.java</p>
 *
 * <p>Description: 導入外部管理服務文件</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>bboss workgroup</p>
 * @Date Aug 13, 2008 3:29:41 PM
 * @author biaoping.yin,尹标平
 * @version 1.0
 */
public class ManagerImport {
	private String file;
	private String docbase;
	
	private boolean webbased = false;
	private boolean classpathbased = true;
	
	private String realPath ;
	
	public boolean isWebBase()
	{
		return this.webbased;
	}
	
	public boolean isClasspathBase()
	{
		return this.classpathbased;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getDocbase() {
		return docbase;
	}
	public void setWebbased(boolean webbased)
	{
		if(webbased)
		{
			this.webbased = true;
			this.classpathbased = false;
		}
		else
		{
			this.webbased = false;
			this.classpathbased = true;
		}
	}
	public void setDocbase(String docbase) {
		if(docbase == null || docbase.trim().length() == 0)
			return;
		if(docbase.startsWith(AssembleCallback.classpathprex))
		{
			this.classpathbased = true;
			this.webbased = false;
			this.docbase = docbase.substring(AssembleCallback.classpathprex.length());
		}
		else if(docbase.startsWith(AssembleCallback.webprex))
		{
			this.classpathbased = false;
			this.webbased = true;
			this.docbase = docbase.substring(AssembleCallback.webprex.length());
		}
		else
			this.docbase = docbase;
	}
	
	public String getRealPath()
	{
//		if(docbase != null && !docbase.equals(""))
//			return StringUtil.applyRelativePath(this.docbase, file);
		return this.realPath == null ? file:this.realPath;
	}

	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}
	
	
	
}
