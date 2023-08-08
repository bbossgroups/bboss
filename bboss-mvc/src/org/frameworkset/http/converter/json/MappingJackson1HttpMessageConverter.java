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

package org.frameworkset.http.converter.json;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.frameworkset.http.HttpInputMessage;
import org.frameworkset.http.HttpOutputMessage;
import org.frameworkset.http.MediaType;
import org.frameworkset.http.ServerHttpRequest;
import org.frameworkset.http.converter.AbstractHttpMessageConverter;
import org.frameworkset.http.converter.HttpMessageNotReadableException;
import org.frameworkset.http.converter.HttpMessageNotWritableException;
import org.frameworkset.spi.InitializingBean;
import org.frameworkset.util.Assert;
import org.frameworkset.util.annotations.DateFormateMeta;
import org.frameworkset.util.annotations.ValueConstants;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Implementation of {@link  }
 * that can read and write JSON using <a href="http://jackson.codehaus.org/">Jackson's</a> {@link ObjectMapper}.
 *
 * <p>This converter can be used to bind to typed beans, or untyped {@link java.util.HashMap HashMap} instances.
 *
 * <p>By default, this converter supports {@code application/json}. This can be overridden by setting the
 * {@link #setSupportedMediaTypes(List) supportedMediaTypes} property.
 *
 * @author Arjen Poutsma
 * @since 3.0
 * @see org.frameworkset.web.servlet.view.json.MappingJacksonJsonView
 */
public class MappingJackson1HttpMessageConverter extends AbstractHttpMessageConverter<Object>  implements JsonConvertInf,InitializingBean{


	private String jsonpCallback = ServerHttpRequest.JSONPCALLBACK_PARAM_NAME;
	private ObjectMapper objectMapper = null;
	private String dateFormat;

	private String locale;

	private String timeZone;
	private boolean disableTimestamp = false;
	private boolean prefixJson = false;



	/**
	 * Construct a new {@code BindingJacksonHttpMessageConverter}.
	 */
	public MappingJackson1HttpMessageConverter() {
		super(jsonmediatypes);
		objectMapper = new ObjectMapper();
		this.objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
	}
	public void setFailedOnUnknownProperties(boolean failedOnUnknownProperties) {
		this.objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,failedOnUnknownProperties);
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public boolean isDisableTimestamp() {
		return disableTimestamp;
	}

	public void setDisableTimestamp(boolean disableTimestamp) {
		this.disableTimestamp = disableTimestamp;
	}

	/**
	 * Sets the {@code ObjectMapper} for this view. If not set, a default
	 * {@link ObjectMapper#ObjectMapper() ObjectMapper} is used.
	 * <p>Setting a custom-configured {@code ObjectMapper} is one way to take further control of the JSON serialization
	 * process. For example, an extended {@link org.codehaus.jackson.map.SerializerFactory} can be configured that provides
	 * custom serializers for specific types. The other option for refining the serialization process is to use Jackson's
	 * provided annotations on the types to be serialized, in which case a custom-configured ObjectMapper is unnecessary.
	 */
	public void setObjectMapper(Object objectMapper) {
		Assert.notNull(objectMapper, "'objectMapper' must not be null");
		this.objectMapper = (ObjectMapper)objectMapper;
	}

	public void init() {
		if(dateFormat != null && !dateFormat.equals("")) {
			DateFormateMeta dateFormateMeta = DateFormateMeta.buildDateFormateMeta(this.dateFormat, this.locale, this.timeZone);
			this.objectMapper.setDateFormat(dateFormateMeta.toDateFormat());


		}
		if(this.disableTimestamp){
			objectMapper.disable(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS);
		}
	}

	public boolean canWrite(MediaType mediaType) {
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

	/**
	 * Indicates whether the JSON output by this view should be prefixed with "{} &&". Default is false.
	 * <p> Prefixing the JSON string in this manner is used to help prevent JSON Hijacking. The prefix renders the string
	 * syntactically invalid as a script so that it cannot be hijacked. This prefix does not affect the evaluation of JSON,
	 * but if JSON validation is performed on the string, the prefix would need to be ignored.
	 */
	public void setPrefixJson(boolean prefixJson) {
		this.prefixJson = prefixJson;
	}

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		JavaType javaType = getJavaType(clazz);
		return this.objectMapper.canDeserialize(javaType) && canRead(mediaType);
	}

	/**
	 * Returns the Jackson {@link JavaType} for the specific class.
	 *
	 * <p>Default implementation returns {@link TypeFactory#type(java.lang.reflect.Type)}, but this can be overridden
	 * in subclasses, to allow for custom generic collection handling. For instance:
	 * <pre class="code">
	 * protected JavaType getJavaType(Class&lt;?&gt; clazz) {
	 *   if (List.class.isAssignableFrom(clazz)) {
	 *     return TypeFactory.collectionType(ArrayList.class, MyBean.class);
	 *   } else {
	 *     return super.getJavaType(clazz);
	 *   }
	 * }
	 * </pre>
	 *
	 * @param clazz the class to return the java type for
	 * @return the java type
	 */
	protected JavaType getJavaType(Class<?> clazz) {
		return objectMapper.getTypeFactory().type(clazz);
	}

	@Override
	public boolean canWrite( Class<?> clazz, MediaType mediaType) {
		return  (this.objectMapper.canSerialize(clazz) && this.canWrite(mediaType));
	}

	@Override
	public boolean supports(Class<?> clazz) {
		// should not be called, since we override canRead/Write instead
		throw new UnsupportedOperationException();
	}

	@Override
	public Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		JavaType javaType = getJavaType(clazz);
		try {
			return this.objectMapper.readValue(inputMessage.getBody(), javaType);
		}
		catch (JsonParseException ex) {
			throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
		}
	}

    /**
     * Abstract template method that reads the actual object. Invoked from {@link #read}.
     * @param clazz the type of object to return
     * @param inputMessage the HTTP input message to read from
     * @return the converted object
     * @throws IOException in case of I/O errors
     * @throws HttpMessageNotReadableException in case of conversion errors
     */
    @Override
    public Object readInternal(Class<? > clazz,Class[] elementTypes, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException{
        JavaType javaType = null;
        if(elementTypes == null || elementTypes.length == 0)
            javaType =   getJavaType(clazz);
        else
            javaType = objectMapper.getTypeFactory().constructParametricType(clazz, elementTypes);
        return this.objectMapper.readValue(inputMessage.getBody(), javaType);
    }

	@Override
	public void writeInternal(Object o, HttpOutputMessage outputMessage,HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotWritableException {

		MediaType contenttype = outputMessage.getHeaders().getContentType();
		JsonEncoding encoding = getEncoding(contenttype);
		JsonGenerator jsonGenerator =
				this.objectMapper.getJsonFactory().createJsonGenerator(outputMessage.getBody(), encoding);
		try {
			if(!contenttype.isCompatibleWith(this.jsonmediatypes[1]))
			{
				if (this.prefixJson) {
					jsonGenerator.writeRaw("{} && ");
				}
				this.objectMapper.writeValue(jsonGenerator, o);
			}
			else
			{
				HttpServletRequest request = inputMessage.getServletRequest();
				String callback = request.getParameter(jsonpCallback);
				if(callback == null || callback.equals(""))
				{
					logger.warn("jsonp responsed warn:callback function is not post by client request,direct to reponse json datas.");
					if (this.prefixJson) {
						jsonGenerator.writeRaw("{} && ");
					}
					this.objectMapper.writeValue(jsonGenerator, o);
				}
				else
				{
					jsonGenerator.writeRaw(callback);
					jsonGenerator.writeRaw("(");
					if (this.prefixJson) {
						jsonGenerator.writeRaw("{} && ");
					}
					this.objectMapper.writeValue(jsonGenerator, o);
					jsonGenerator.writeRaw(")");
					jsonGenerator.flush();
				}
				
			}
		}
		catch (JsonGenerationException ex) {
			throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
		}
	}

	private JsonEncoding getEncoding(MediaType contentType) {
		if (contentType != null && contentType.getCharSet() != null) {
			Charset charset = contentType.getCharSet();
			for (JsonEncoding encoding : JsonEncoding.values()) {
				if (charset.name().equals(encoding.getJavaName())) {
					return encoding;
				}
			}
		}
		return JsonEncoding.UTF8;
	}
	
	/**
	 * 获取用户请求报文对应的数据类型：String,json
	 * @return
	 */
	public String getRequetBodyDataType()
	{
		return ValueConstants.datatype_json;
	}
	/**
	 * 获取用户请求报文对应的数据类型：String,json
	 * @return
	 */
	public String getResponseBodyDataType()
	{
		return ValueConstants.datatype_json;
	}

	
	public boolean canWrite(String datatype) {
		// TODO Auto-generated method stub
		if(datatype == null)
			return false;
		
		if(datatype.equals(ValueConstants.datatype_json))
				return true;
		else if(datatype.equals(ValueConstants.datatype_jsonp))
				return true;
		else
			return false;
	}

	public String getJsonpCallback() {
		return jsonpCallback;
	}

	public void setJsonpCallback(String jsonpCallback) {
		this.jsonpCallback = jsonpCallback;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.init();
	}
}
