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
package org.frameworkset.http.converter;

import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.util.List;

import org.frameworkset.http.FileBlob;
import org.frameworkset.http.HttpInputMessage;
import org.frameworkset.http.HttpOutputMessage;
import org.frameworkset.http.MediaType;
import org.frameworkset.util.annotations.ValueConstants;
import org.frameworkset.util.io.ByteArrayResource;
import org.frameworkset.util.io.ClassPathResource;
import org.frameworkset.util.io.FileSystemResource;
import org.frameworkset.util.io.Resource;
import org.frameworkset.util.io.UrlResource;
import org.frameworkset.web.servlet.support.ServletContextResource;

import com.frameworkset.util.StringUtil;


/**
 * <p>
 * FileMessageConvertor.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2009
 * </p>
 * 
 * @Date 2011-6-17
 * @author biaoping.yin
 * @version 1.0
 */
public class FileMessageConvertor<T> implements HttpMessageConverter<T>
{
	@Override
	public boolean isdefault() {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean canRead(Class<?> clazz, MediaType mediaType)
	{

		// TODO Auto-generated method stub
		return false;
	}

	public boolean canWrite(Class<?> clazz, MediaType mediaType)
	{

		// TODO Auto-generated method stub
		return File.class.isAssignableFrom(clazz) || Blob.class.isAssignableFrom(clazz) || FileBlob.class.isAssignableFrom(clazz)
				|| ClassPathResource.class.isAssignableFrom(clazz)
				|| ServletContextResource.class.isAssignableFrom(clazz)
				|| FileSystemResource.class.isAssignableFrom(clazz)
				|| UrlResource.class.isAssignableFrom(clazz)
				|| ByteArrayResource.class.isAssignableFrom(clazz);
	}

	public List<MediaType> getSupportedMediaTypes()
	{

		// TODO Auto-generated method stub
		return null;
	}

	public T read(Class<? extends T> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException
	{

		// TODO Auto-generated method stub
		return null;
	}

	public void write(T t, MediaType contentType,
			HttpOutputMessage outputMessage,HttpInputMessage inputMessage ) throws IOException,
			HttpMessageNotWritableException
	{

		if(t instanceof File)
		{
			try
			{
				StringUtil.sendFile(inputMessage.getServletRequest(), outputMessage.getResponse(), (File)t);
			}
			catch (Exception e)
			{
				throw new HttpMessageNotWritableException(((File)t).getAbsolutePath(),e);
			}
		}
		else if(t instanceof Resource)
		{
			try {
				StringUtil.sendFile_(inputMessage.getServletRequest(), outputMessage.getResponse(), (Resource)t);
			} catch (Exception e)
			{
				throw new HttpMessageNotWritableException(((Resource)t).toString(),e);
			}
		}		
		else
		{
			FileBlob fb = (FileBlob)t;
			try
			{
				if(fb.isdownload())
				{
					if(fb.isBlob())
						StringUtil.sendFile(inputMessage.getServletRequest(), outputMessage.getResponse(), fb.getFileName(),fb.getData());
					else if(fb.isStream())
					{
						StringUtil.sendFile(inputMessage.getServletRequest(), outputMessage.getResponse(), fb.getFileName(),fb.getInputStream());
					}
					else
						StringUtil.sendFile(inputMessage.getServletRequest(), outputMessage.getResponse(), fb.getFileData());
				}
				else
				{
//					if(!fb.isFile())
//						StringUtil.showFile(inputMessage.getServletRequest(), outputMessage.getResponse(), fb.getFileName(),fb.getData());
//					else
//						StringUtil.showFile(inputMessage.getServletRequest(), outputMessage.getResponse(), fb.getFileData());
					
					if(fb.isBlob())
						StringUtil.showFile(inputMessage.getServletRequest(), outputMessage.getResponse(), fb.getFileName(),fb.getData());
					else if(fb.isStream())
					{
						StringUtil.showFile(inputMessage.getServletRequest(), outputMessage.getResponse(), fb.getFileName(),fb.getInputStream());
					}
					else if(fb.isFile())
					{
						StringUtil.showFile(inputMessage.getServletRequest(), outputMessage.getResponse(), fb.getFileData());
					}
					else
						StringUtil.showFile(inputMessage.getServletRequest(), outputMessage.getResponse(), fb.getFileName(),fb.getData());
				}
			}
			catch (Exception e)
			{
				throw new HttpMessageNotWritableException(fb.getFileName(),e);
			}
		}

	}
	protected MediaType defaultAcceptedMediaType;
	public MediaType getDefaultAcceptedMediaType()
	{
		if(defaultAcceptedMediaType != null)
			return defaultAcceptedMediaType;
		synchronized(this){
			return defaultAcceptedMediaType = this.getSupportedMediaTypes().get(0);
		}
	}
	/**
	 * 获取用户请求报文对应的数据类型：String,json
	 * @return
	 */
	public String getRequetBodyDataType()
	{
		return null;
	}
	public boolean canRead(String datatype)
	{
		return false;
	}
	public String getResponseBodyDataType()
	{
		return null;
	}

	 
	public boolean canWrite(String datatype) {
		// TODO Auto-generated method stub
		if(datatype == null)
			return false;
		if(datatype.equals(ValueConstants.datatype_file))
			return true;
		else
			return false;
	}
}