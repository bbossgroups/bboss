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
import java.nio.charset.Charset;
import java.sql.Blob;

import org.frameworkset.http.FileBlob;
import org.frameworkset.http.MediaType;
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
	private MediaType responseMediaType;
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
	private boolean eval = false;
	private String evalDataType(Class responseClass)
	{
		if(String.class.isAssignableFrom(responseClass))
		{
			eval = true;
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
		else if(Enum.class.isAssignableFrom(responseClass))
		{			
			eval = true;
			return ValueConstants.datatype_string;
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
			datatype = this.evalDataType(method.getReturnType());
	
		charset =AnnotationUtils.converDefaultValue( body.charset());
		this.responseMediaType = convertMediaType();
	}
	private MediaType convertMediaType()
	{
		MediaType temp = null;		 
		
		String type =  datatype();
		String charset =  charset();
		if(type == null)
		{
			if(charset != null)
			{
				temp = new MediaType("text","html",Charset.forName(charset));
			}
			else
			{
				temp = new MediaType("text","html",Charset.forName("UTF-8"));
			}
		}
		else if(type.equals("json"))
		{
			if(charset != null)
			{
				temp = new MediaType("application","json",Charset.forName(charset));
			}
			else
				temp = new MediaType("application","json",Charset.forName("UTF-8"));
		}
		else if(type.equals("jsonp"))
		{
			if(charset != null)
			{
				temp = new MediaType("application","jsonp",Charset.forName(charset));
			}
			else
				temp = new MediaType("application","jsonp",Charset.forName("UTF-8"));
		}
		
		else if(type.equals("xml"))
		{
			if(charset != null)
			{
				temp = new MediaType("application","xml",Charset.forName(charset));
			}
			else
				temp = new MediaType("application","xml",Charset.forName("UTF-8"));
		}
		else if(type.equals("javascript"))
		{
			if(charset != null)
			{
				temp = new MediaType("application","javascript",Charset.forName(charset));
			}
			else
				temp = new MediaType("application","javascript",Charset.forName("UTF-8"));
		}
			//javascript
		return temp;
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
	public MediaType getResponseMediaType() {
		return responseMediaType;
	}
	public boolean isEval() {
		return eval;
	}

//	public boolean isEval() {
//		return eval;
//	}
}
