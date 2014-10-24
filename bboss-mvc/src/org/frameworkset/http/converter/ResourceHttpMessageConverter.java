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
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;

import org.frameworkset.http.HttpHeaders;
import org.frameworkset.http.HttpInputMessage;
import org.frameworkset.http.HttpOutputMessage;
import org.frameworkset.http.MediaType;
import org.frameworkset.util.ClassUtils;
import org.frameworkset.util.FileCopyUtils;
import org.frameworkset.util.annotations.ValueConstants;
import org.frameworkset.util.io.ByteArrayResource;
import org.frameworkset.util.io.ClassPathResource;
import org.frameworkset.util.io.Resource;

import com.frameworkset.util.StringUtil;

/**
 * Implementation of {@link HttpMessageConverter} that can read and write {@link Resource Resources}.
 *
 * <p>By default, this converter can read all media types. The Java Activation Framework (JAF) - if available - is used
 * to determine the {@code Content-Type} of written resources. If JAF is not available, {@code application/octet-stream}
 * is used.
 *
 * @author Arjen Poutsma
 * @since 3.0.2
 */
public class ResourceHttpMessageConverter implements HttpMessageConverter<Resource> {

	private static final boolean jafPresent =
			ClassUtils.isPresent("javax.activation.FileTypeMap", ResourceHttpMessageConverter.class.getClassLoader());

	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return Resource.class.isAssignableFrom(clazz);
	}

	public boolean canWrite( Class<?> clazz, MediaType mediaType) {
		return Resource.class.isAssignableFrom(clazz);
	}

	public List<MediaType> getSupportedMediaTypes() {
		return Collections.singletonList(MediaType.ALL);
	}

	public Resource read(Class<? extends Resource> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {

		byte[] body = FileCopyUtils.copyToByteArray(inputMessage.getBody());
		return new ByteArrayResource(body);
	}
	@Override
	public boolean isdefault() {
		// TODO Auto-generated method stub
		return false;
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
	public void write(Resource resource, MediaType contentType, HttpOutputMessage outputMessage,HttpInputMessage inputMessage )
			throws IOException, HttpMessageNotWritableException {

		HttpHeaders headers = outputMessage.getHeaders();
		if (contentType == null || contentType.isWildcardType() || contentType.isWildcardSubtype()) {
			contentType = getContentType(resource);
		}
//		if(!usecustomMediaTypeByMethod && this.responsecontenteype != null)
//		{
//			contentType = this.responsecontenteype;
//		}
		if (contentType != null) {
			headers.setContentType(contentType);
		}
		Long contentLength = getContentLength(resource, contentType);
		if (contentLength != null) {
			headers.setContentLength(contentLength);
		}
		FileCopyUtils.copy(resource.getInputStream(), outputMessage.getBody());
		outputMessage.getBody().flush();
	}

	private MediaType getContentType(Resource resource) {
		if (jafPresent) {
			return ActivationMediaTypeFactory.getMediaType(resource);
		}
		else {
			return MediaType.APPLICATION_OCTET_STREAM;
		}
	}

	protected Long getContentLength(Resource resource, MediaType contentType) throws IOException {
		return resource.contentLength();
	}


	/**
	 * Inner class to avoid hard-coded JAF dependency.
	 */
	private static class ActivationMediaTypeFactory {

		private static final FileTypeMap fileTypeMap;

		static {
			fileTypeMap = loadFileTypeMapFromContextSupportModule();
		}

		private static FileTypeMap loadFileTypeMapFromContextSupportModule() {
			// see if we can find the extended mime.types from the context-support module
			Resource mappingLocation = new ClassPathResource("org/frameworkset/web/servlet/mime.types");
			if (mappingLocation.exists()) {
				InputStream inputStream = null;
				try {
					inputStream = mappingLocation.getInputStream();
					return new MimetypesFileTypeMap(inputStream);
				}
				catch (IOException ex) {
					// ignore
				}
				finally {
					if (inputStream != null) {
						try {
							inputStream.close();
						}
						catch (IOException ex) {
							// ignore
						}
					}
				}
			}
			return FileTypeMap.getDefaultFileTypeMap();
		}

		public static MediaType getMediaType(Resource resource) {
			String mediaType = fileTypeMap.getContentType(resource.getFilename());
			return (StringUtil.hasText(mediaType) ? MediaType.parseMediaType(mediaType) : null);
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

	@Override
	public boolean canWrite(String dataype) {
		// TODO Auto-generated method stub
		return false;
	}

	
	
}
