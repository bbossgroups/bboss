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

import java.net.URL;

import org.frameworkset.util.ResourceUtils;

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
	public SOAFileApplicationContext(String file) {
		this(file,default_charset);
		
		
	}
	
	
	
	
	public SOAFileApplicationContext(URL file, String path)
	{
		super( file,  path);
	}
	public SOAFileApplicationContext(String file,String charset) {
		this( ResourceUtils.getFileURL(file),file);
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
