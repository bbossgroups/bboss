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

package org.frameworkset.http.converter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.frameworkset.http.HttpInputMessage;
import org.frameworkset.http.HttpOutputMessage;
import org.frameworkset.http.MediaType;
import org.frameworkset.util.FileCopyUtils;
import org.frameworkset.util.annotations.ValueConstants;

/**
 * Implementation of {@link HttpMessageConverter} that can read and write strings.
 *
 * <p>By default, this converter supports all media types (<code>&#42;&#47;&#42;</code>), and writes with a {@code
 * Content-Type} of {@code text/plain}. This can be overridden by setting the {@link
 * #setSupportedMediaTypes(java.util.List) supportedMediaTypes} property.
 *
 * @author Arjen Poutsma
 * @since 3.0
 */
public class StringHttpMessageConverter extends AbstractHttpMessageConverter<Object> {
	
	

	public static  Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	private final List<Charset> availableCharsets;

	private boolean writeAcceptCharset = true;

	public StringHttpMessageConverter() {
		super(new MediaType("text", "plain", DEFAULT_CHARSET),new MediaType("text", "html"), new MediaType("text", "javascript"));
		this.availableCharsets = new ArrayList<Charset>(Charset.availableCharsets().values());
	}

	/**
	 * Indicates whether the {@code Accept-Charset} should be written to any outgoing request.
	 * <p>Default is {@code true}.
	 */
	public void setWriteAcceptCharset(boolean writeAcceptCharset) {
		this.writeAcceptCharset = writeAcceptCharset;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return String.class.equals(clazz);
	}

	@Override
	protected String readInternal(Class clazz, HttpInputMessage inputMessage) throws IOException {
		MediaType contentType = inputMessage.getHeaders().getContentType();
		Charset charset = contentType.getCharSet() != null ? contentType.getCharSet() : DEFAULT_CHARSET;
		return FileCopyUtils.copyToString(new InputStreamReader(inputMessage.getBody(), charset));
	}

	@Override
	protected Long getContentLength(Object data, MediaType contentType) {
		String s = null;
		if(data instanceof String)
		{
			s = (String)data;
		}
		else
			s = String.valueOf(data);
		if (contentType != null && contentType.getCharSet() != null) {
			Charset charset = contentType.getCharSet();
			try {
				return (long) s.getBytes(charset.name()).length;
			}
			catch (UnsupportedEncodingException ex) {
				// should not occur
				throw new InternalError(ex.getMessage());
			}
		}
		else {
			return null;
		}
	}

	@Override
	protected void writeInternal(Object data, HttpOutputMessage outputMessage,HttpInputMessage inputMessage) throws IOException {
		if (writeAcceptCharset) {
			outputMessage.getHeaders().setAcceptCharset(getAcceptedCharsets());
		}
		String s = null;
		if(data instanceof String)
		{
			s = (String)data;
		}
		else
			s = String.valueOf(data);
		MediaType contentType = outputMessage.getHeaders().getContentType();
		Charset charset = contentType.getCharSet() != null ? contentType.getCharSet() : DEFAULT_CHARSET;
		FileCopyUtils.copy(s, new OutputStreamWriter(outputMessage.getBody(), charset));
	}

	/**
	 * Return the list of supported {@link Charset}.
	 *
	 * <p>By default, returns {@link Charset#availableCharsets()}. Can be overridden in subclasses.
	 *
	 * @return the list of accepted charsets
	 */
	protected List<Charset> getAcceptedCharsets() {
		return this.availableCharsets;
	}
	
	public void setResponseCharset(String charset) {
		super.responsecontenteype = new MediaType("text","html",Charset.forName(charset));
	}
	public boolean isdefault()
	{
		return true;
	}
	
	/**
	 * 获取用户请求报文对应的数据类型：String,json
	 * @return
	 */
	public String getRequetBodyDataType()
	{
		return ValueConstants.datatype_string;
	}
	public String getResponseBodyDataType()
	{
		return ValueConstants.datatype_string;
	}
	
	protected boolean canWrite(MediaType mediaType) {
		if (mediaType == null || MediaType.ALL.equals(mediaType)) {
			return false;
		}
		for (MediaType supportedMediaType : getSupportedMediaTypes()) {
			if (supportedMediaType.isCompatibleWith(mediaType)) {
				return true;
			}
		}
		return false;
	}
	
	
	public boolean canWrite(String datatype) {
		// TODO Auto-generated method stub
		if(datatype == null)
			return false;
		
		if(datatype.equals(ValueConstants.datatype_string))
				return true;
		else
			return false;
	}
}
