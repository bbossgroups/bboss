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
package org.frameworkset.http.converter.wordpdf;

import java.io.IOException;
import java.util.List;

import org.frameworkset.http.HttpInputMessage;
import org.frameworkset.http.HttpOutputMessage;
import org.frameworkset.http.MediaType;
import org.frameworkset.http.converter.HttpMessageConverter;
import org.frameworkset.http.converter.HttpMessageNotReadableException;
import org.frameworkset.http.converter.HttpMessageNotWritableException;
import org.frameworkset.util.annotations.ValueConstants;



/**
 * <p> Word2PDFMessageConverter.java</p>
 * <p> Description: 
 * 1.word转pdf，并提供浏览和下载功能
 * 2.word模板结合数据，再转pdf，并提供浏览和下载功能
 * 3.word转flash格式文件
 * </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2012-9-17 上午8:29:53
 * @author biaoping.yin
 * @version 1.0
 */
public class Word2PDFMessageConverter <T> implements HttpMessageConverter<T>{
	/**
	 * 指定全局临时文件目录
	 */
	private String tempDir;
	private String flashpaperWorkDir;
	private boolean openoffice = false;
	private long waittimes = -1;

	public String getFlashpaperWorkDir() {
		return flashpaperWorkDir;
	}

	public void setFlashpaperWorkDir(String flashpaperWorkDir) {
		this.flashpaperWorkDir = flashpaperWorkDir;
	}

	public long getWaittimes() {
		return waittimes;
	}

	public void setWaittimes(long waittimes) {
		this.waittimes = waittimes;
	}

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		// TODO Auto-generated method stub
		return WordResponse.class.isAssignableFrom(clazz);
	}

	@Override
	public boolean canWrite( Class<?> clazz, MediaType mediaType) {
		// TODO Auto-generated method stub
		return WordResponse.class.isAssignableFrom(clazz);
	}

	@Override
	public List<MediaType> getSupportedMediaTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T read(Class<? extends T> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void write(T t, MediaType contentType,
			HttpOutputMessage outputMessage, HttpInputMessage inputMessage,
			boolean usecustomMediaTypeByMethod) throws IOException,
			HttpMessageNotWritableException {
		// TODO Auto-generated method stub
		if(t instanceof WordResponse)
		{
			WordResponse tmp = (WordResponse)t ;
			tmp.setConvter(this);
			if(tmp.getTempdir() == null)
			{
				if(this.tempDir != null)
				{
					tmp.setTempdir(this.tempDir);
				}
			}
			if(tmp.getFlashpaperWorkDir() == null)
			{
				if(this.flashpaperWorkDir != null)
				{
					tmp.setFlashpaperWorkDir(flashpaperWorkDir);
				}
			}
			
			if(tmp.getWaittimes() == -1)
			{
				if(this.getWaittimes() != -1)
				{
					tmp.setWaittimes(getWaittimes());
				}
			}
			try {
				tmp._resonse(outputMessage, inputMessage);
			} catch (IOException e) {
				throw e;
			}
			 catch (Exception e) {
					throw new IOException(e);
			}
			
		}
	}

	public String getTempDir() {
		return tempDir;
	}

	public void setTempDir(String tempDir) {
		this.tempDir = tempDir;
	}

	public boolean isOpenoffice() {
		return openoffice;
	}

	public void setOpenoffice(boolean openoffice) {
		this.openoffice = openoffice;
	}

	@Override
	public boolean isdefault() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public MediaType getDefaultAcceptedMediaType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequetBodyDataType() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean canRead(String datatype)
	{
		return false;
	}
	public String getResponseBodyDataType()
	{
		return ValueConstants.datatype_word;
	}

	@Override
	public boolean canWrite(String datatype) {
		// TODO Auto-generated method stub
		if(datatype == null)
			return false;
		if(datatype.equals(ValueConstants.datatype_word))
		{
			return true;
		}
		else
			return false;
			
	}
}
