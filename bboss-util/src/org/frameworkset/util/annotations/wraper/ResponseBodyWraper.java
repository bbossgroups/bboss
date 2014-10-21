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
package org.frameworkset.util.annotations.wraper;

import java.io.File;
import java.lang.reflect.Method;
import java.sql.Blob;

import org.frameworkset.http.FileBlob;
import org.frameworkset.util.annotations.AnnotationUtils;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.util.annotations.ValueConstants;
import org.frameworkset.util.io.Resource;

/**
 * <p>ResponseBodyWraper.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2005-2013 </p>
 * 
 * @Date 2013年10月3日
 * @author biaoping.yin
 * @version 1.0
 */
public class ResponseBodyWraper {
	/**
	 * com.sun.syndication.feed.rss.Channel
	 */
	private static Class rsschannel = null;
	/**
	 * com.sun.syndication.feed.atom.Feed
	 */
	private static Class atomFeed = null;
	
	/**
	 *  org.frameworkset.http.converter.wordpdf.WordResponse
	 */
	private static Class wordResponse = null;
	static 
	{
		try
		{
			wordResponse = Class.forName("org.frameworkset.http.converter.wordpdf.WordResponse");
		}
		catch(Throwable e)
		{
			
		}
		
		try
		{
			rsschannel = Class.forName("com.sun.syndication.feed.rss.Channel");
		}
		catch(Throwable e)
		{
			
		}
		try
		{
			atomFeed = Class.forName("com.sun.syndication.feed.atom.Feed");
		}
		catch(Throwable e)
		{
			
		}
	}
	
	/**
	 * 指定响应的数据类型
	 * @return
	 */
	private String datatype;
	private String evalDataType(Class responseClass)
	{
		if(String.class.isAssignableFrom(responseClass))
		{
			return ValueConstants.datatype_string;
		}
		else if(
				File.class.isAssignableFrom(responseClass)||
				FileBlob.class.isAssignableFrom(responseClass)
				||Blob.class.isAssignableFrom(responseClass)
				||Resource.class.isAssignableFrom(responseClass) )
		{
			return ValueConstants.datatype_file;
		}
		else if(byte[].class.isAssignableFrom(responseClass))
		{
			return ValueConstants.datatype_bytearray; 
		}
		else if(rsschannel != null && rsschannel.isAssignableFrom(responseClass))
		{
			return ValueConstants.datatype_rss;
		}
		else if(atomFeed != null && atomFeed.isAssignableFrom(responseClass))
		{
			return ValueConstants.datatype_atom;
		}
		else if(wordResponse != null && wordResponse.isAssignableFrom(responseClass))
		{
			return ValueConstants.datatype_word;
		}
		else
		{
			return ValueConstants.datatype_json;
		}
			
	}
	/**
	 * 指定响应的数据编码字符集
	 * @return
	 */
	private String charset;
	public ResponseBodyWraper(ResponseBody body,Method method) {
		datatype = AnnotationUtils.converDefaultValue(body.datatype());
		if(datatype == null)
			datatype = this.evalDataType(method.getClass());
		charset =AnnotationUtils.converDefaultValue( body.charset());
	}
	
	/**
	 * 指定响应的数据类型
	 * @return
	 */
	public String datatype(){
		return this.datatype;
	}
	/**
	 * 指定响应的数据编码字符集
	 * @return
	 */
	public String charset(){
		return this.charset;
	}
}
