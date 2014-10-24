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
import java.nio.charset.Charset;
import java.util.List;

import org.frameworkset.http.HttpInputMessage;
import org.frameworkset.http.HttpOutputMessage;
import org.frameworkset.http.MediaType;

/**
 * <p>Title: HttpMessageConverter.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-11-23
 * @author biaoping.yin
 * @version 1.0
 */
public interface HttpMessageConverter<T> {
	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	public static final MediaType[] jsonmediatypes = new MediaType[] {new MediaType("application", "json", DEFAULT_CHARSET),new MediaType("application", "jsonp", DEFAULT_CHARSET)}; 
    public static final MediaType[] xmlmediatypes = new MediaType[] {new MediaType("text","xml", DEFAULT_CHARSET),new MediaType("application","xml", DEFAULT_CHARSET), new MediaType("application", "*+xml", DEFAULT_CHARSET)};
	/**
	 * Indicates whether the given class can be read by this converter.
	 * @param clazz the class to test for readability
	 * @param mediaType the media type to read, can be {@code null} if not specified.
	 * Typically the value of a {@code Content-Type} header.
	 * @return {@code true} if readable; {@code false} otherwise
	 */
	boolean canRead(Class<?> clazz, MediaType mediaType);
	
	/**
	 * Indicates whether the given class can be read by this converter.
	 * @param clazz the class to test for readability
	 * @param mediaType the media type to read, can be {@code null} if not specified.
	 * Typically the value of a {@code Content-Type} header.
	 * @return {@code true} if readable; {@code false} otherwise
	 */
	boolean canRead(String datatype);

	/**
	 * Indicates whether the given class can be written by this converter.
	 * @param clazz the class to test for writability
	 * @param mediaType the media type to write, can be {@code null} if not specified.
	 * Typically the value of an {@code Accept} header.
	 * @return {@code true} if writable; {@code false} otherwise
	 */
	boolean canWrite(Class<?> clazz, MediaType mediaType);

	/**
	 * Return the list of {@link MediaType} objects supported by this converter.
	 * @return the list of supported media types
	 */
	List<MediaType> getSupportedMediaTypes();

	/**
	 * Read an object of the given type form the given input message, and returns it.
	 * @param clazz the type of object to return. This type must have previously been passed to the
	 * {@link #canRead canRead} method of this interface, which must have returned {@code true}.
	 * @param inputMessage the HTTP input message to read from
	 * @return the converted object
	 * @throws IOException in case of I/O errors
	 * @throws HttpMessageNotReadableException in case of conversion errors
	 */
	T read(Class<? extends T> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException;

	/**
	 * Write an given object to the given output message.
	 * @param t the object to write to the output message. The type of this object must have previously been
	 * passed to the {@link #canWrite canWrite} method of this interface, which must have returned {@code true}.
	 * @param contentType the content type to use when writing. May be {@code null} to indicate that the
	 * default content type of the converter must be used. If not {@code null}, this media type must have
	 * previously been passed to the {@link #canWrite canWrite} method of this interface, which must have
	 * returned {@code true}.
	 * @param usecustomMediaTypeByMethod 识别contentType是不是由控制器方法单独指定，如果是则为true,否则为false，如果没有使用
	 * @param outputMessage the message to write to
	 * @throws IOException in case of I/O errors
	 * @throws HttpMessageNotWritableException in case of conversion errors
	 */
	void write(T t, MediaType contentType, HttpOutputMessage outputMessage,HttpInputMessage inputMessage )
			throws IOException, HttpMessageNotWritableException;
	boolean isdefault();
	public MediaType getDefaultAcceptedMediaType();
	/**
	 * 获取用户请求报文对应的数据类型：String,json
	 * @return
	 */
	public String getRequetBodyDataType();

	boolean canWrite(String dataype);
	
	
	/**
	 * 获取用户响应报文对应的数据类型：String,json,xml
	 * @return
	 */
//	public String getResponseBodyDataType();


}
