/*
 *  Copyright 2008-2010 biaoping.yin
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
package org.frameworkset.spi;

import org.frameworkset.util.ResourceUtils;

import java.net.URL;

/**
 * <p>Title: SOAFileApplicationContext.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2011-5-13 下午05:23:29
 * @author biaoping.yin
 * @version 1.0
 */
public class SOAFileApplicationContext extends SOAApplicationContext{
	public static final String default_charset = "UTF-8"; 
	public SOAFileApplicationContext(String baseDir,String file) {
		this(baseDir,file,default_charset);
		
		
	}

	public SOAFileApplicationContext(String baseDir,String file,boolean needInitProvider) {
		this(baseDir,file,default_charset,needInitProvider);


	}


	public SOAFileApplicationContext(URL file, String path)
	{
		this( (String)null,file,  path);
	}
	
	public SOAFileApplicationContext(String baseDir,URL file, String path)
	{
		super( baseDir,file,  path);
	}

	public SOAFileApplicationContext(String baseDir,URL file, String path,boolean needInitProvider)
	{
		super( baseDir,file,  path,needInitProvider);
	}


	public SOAFileApplicationContext(String baseDir,String file,String charset) {
		this(baseDir, ResourceUtils.getFileURL(baseDir,file),file);
//		try {
//			File file_ = ValueObjectUtil.getClassPathFile(file);
//			if(file_ != null)
//				this.configfile = file_.getAbsolutePath();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		this.configfile = file;
		
	}

	public SOAFileApplicationContext(String baseDir,String file,String charset,boolean needInitProvider) {
		this(baseDir, ResourceUtils.getFileURL(baseDir,file),file,  needInitProvider);
//		try {
//			File file_ = ValueObjectUtil.getClassPathFile(file);
//			if(file_ != null)
//				this.configfile = file_.getAbsolutePath();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		this.configfile = file;

	}

}
