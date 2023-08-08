package org.frameworkset.http.converter.json;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.frameworkset.http.HttpInputMessage;
import org.frameworkset.http.HttpOutputMessage;
import org.frameworkset.http.MediaType;
import org.frameworkset.http.ServerHttpRequest;
import org.frameworkset.http.converter.AbstractGenericHttpMessageConverter;
import org.frameworkset.http.converter.GenericHttpMessageConverter;
import org.frameworkset.http.converter.HttpMessageNotReadableException;
import org.frameworkset.http.converter.HttpMessageNotWritableException;
import org.frameworkset.util.Assert;
import org.frameworkset.util.ClassUtils;
import org.frameworkset.util.TypeUtils;
import org.frameworkset.util.annotations.ValueConstants;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractJackson2HttpMessageConverter extends AbstractGenericHttpMessageConverter<Object> 
		implements GenericHttpMessageConverter<Object>,JsonConvertInf {
	private String jsonpCallback = ServerHttpRequest.JSONPCALLBACK_PARAM_NAME;
	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	// Check for Jackson 2.3's overloaded canDeserialize/canSerialize variants
	// with cause reference
	private static final boolean jackson23Available = ClassUtils.hasMethod(ObjectMapper.class, "canDeserialize",
			JavaType.class, AtomicReference.class);

	// Check for Jackson 2.6+ for support of generic type aware serialization of
	// polymorphic collections
	private static final boolean jackson26Available = ClassUtils.hasMethod(ObjectMapper.class,
			"setDefaultPrettyPrinter", PrettyPrinter.class);

	protected ObjectMapper objectMapper;

	private Boolean prettyPrint;

	protected AbstractJackson2HttpMessageConverter(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	protected AbstractJackson2HttpMessageConverter(ObjectMapper objectMapper, MediaType supportedMediaType) {
		super(supportedMediaType);
		this.objectMapper = objectMapper;
	}

	protected AbstractJackson2HttpMessageConverter(ObjectMapper objectMapper, MediaType... supportedMediaTypes) {
		super(supportedMediaTypes);
		this.objectMapper = objectMapper;
	}

	/**
	 * Set the {@code ObjectMapper} for this view. If not set, a default
	 * {@link ObjectMapper#ObjectMapper() ObjectMapper} is used.
	 * <p>
	 * Setting a custom-configured {@code ObjectMapper} is one way to take
	 * further control of the JSON serialization process. For example, an
	 * extended {@link com.fasterxml.jackson.databind.ser.SerializerFactory} can
	 * be configured that provides custom serializers for specific types. The
	 * other option for refining the serialization process is to use Jackson's
	 * provided annotations on the types to be serialized, in which case a
	 * custom-configured ObjectMapper is unnecessary.
	 */
	public void setObjectMapper(Object objectMapper) {
		Assert.notNull(objectMapper, "ObjectMapper must not be null");
		this.objectMapper = (ObjectMapper)objectMapper;
		configurePrettyPrint();
	}

	/**
	 * Return the underlying {@code ObjectMapper} for this view.
	 */
	public ObjectMapper getObjectMapper() {
		return this.objectMapper;
	}

	/**
	 * Whether to use the {@link DefaultPrettyPrinter} when writing JSON. This
	 * is a shortcut for setting up an {@code ObjectMapper} as follows:
	 * 
	 * <pre class="code">
	 * ObjectMapper mapper = new ObjectMapper();
	 * mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
	 * converter.setObjectMapper(mapper);
	 * </pre>
	 */
	public void setPrettyPrint(boolean prettyPrint) {
		this.prettyPrint = prettyPrint;
		configurePrettyPrint();
	}

	private void configurePrettyPrint() {
		if (this.prettyPrint != null) {
			this.objectMapper.configure(SerializationFeature.INDENT_OUTPUT, this.prettyPrint);
		}
	}

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return canRead(clazz, null, mediaType);
	}

	@Override
	public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
		JavaType javaType = getJavaType(type, contextClass);
		if (!jackson23Available || !logger.isWarnEnabled()) {
			return (this.objectMapper.canDeserialize(javaType) && canRead(mediaType));
		}
		AtomicReference<Throwable> causeRef = new AtomicReference<Throwable>();
		if (this.objectMapper.canDeserialize(javaType, causeRef) && canRead(mediaType)) {
			return true;
		}
		Throwable cause = causeRef.get();
		if (cause != null) {
			String msg = "Failed to evaluate deserialization for type " + javaType;
			if (logger.isDebugEnabled()) {
				logger.warn(msg, cause);
			} else {
				logger.warn(msg + ": " + cause);
			}
		}
		return false;
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		if (!jackson23Available || !logger.isWarnEnabled()) {
			return (this.objectMapper.canSerialize(clazz) && canWrite(mediaType));
		}
		AtomicReference<Throwable> causeRef = new AtomicReference<Throwable>();
		if (this.objectMapper.canSerialize(clazz, causeRef) && canWrite(mediaType)) {
			return true;
		}
		Throwable cause = causeRef.get();
		if (cause != null) {
			String msg = "Failed to evaluate serialization for type [" + clazz + "]";
			if (logger.isDebugEnabled()) {
				logger.warn(msg, cause);
			} else {
				logger.warn(msg + ": " + cause);
			}
		}
		return false;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		// should not be called, since we override canRead/Write instead
		throw new UnsupportedOperationException();
	}
	
	

	@Override
	public Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {

		JavaType javaType = getJavaType(clazz, null);
		return readJavaType(javaType, inputMessage);
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
            javaType =   getJavaType(clazz, null);
        else
            javaType = objectMapper.getTypeFactory().constructParametricType(clazz, elementTypes);
        return readJavaType(javaType, inputMessage);
    }

	@Override
	public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {

		JavaType javaType = getJavaType(type, contextClass);
		return readJavaType(javaType, inputMessage);
	}

	@SuppressWarnings("deprecation")
	private Object readJavaType(JavaType javaType, HttpInputMessage inputMessage) {
		try {
			if (inputMessage instanceof MappingJacksonInputMessage) {
				Class<?> deserializationView = ((MappingJacksonInputMessage) inputMessage).getDeserializationView();
				if (deserializationView != null) {
					return this.objectMapper.readerWithView(deserializationView).withType(javaType)
							.readValue(inputMessage.getBody());
				}
			}
			InputStream im = inputMessage.getBody();

			try {
				return this.objectMapper.readValue(im, javaType);
			}
			catch (MismatchedInputException mismatchedInputException){
				String msg = mismatchedInputException.getMessage();
				if(msg != null && msg.startsWith("No content to map due to end-of-input")){
					return null;
				}
				throw mismatchedInputException;
			}
			catch(Exception e) {
				throw e;
			}
//			int av = 0;
//			try {
//				av = im.available();
//			}catch (Exception e){
//
//			}
//			if(av > 0) {
//				return this.objectMapper.readValue(im, javaType);
//			}
//			else {
//				return null;
//			}

		} catch (IOException ex) {
			throw new HttpMessageNotReadableException("Could not read document: " + ex.getMessage(), ex);
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage,
			HttpInputMessage inputMessage) throws IOException, HttpMessageNotWritableException {

		JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders().getContentType());
		JsonGenerator generator = this.objectMapper.getFactory().createGenerator(outputMessage.getBody(), encoding);
		try {
			HttpServletRequest request = inputMessage.getServletRequest();
			String callback = request.getParameter(jsonpCallback);
			writePrefix(generator, object, callback);

			Class<?> serializationView = null;
			FilterProvider filters = null;
			Object value = object;
			JavaType javaType = null;
			if (object instanceof MappingJacksonValue) {
				MappingJacksonValue container = (MappingJacksonValue) object;
				value = container.getValue();
				serializationView = container.getSerializationView();
				filters = container.getFilters();
			}
			if (jackson26Available && type != null && value != null && TypeUtils.isAssignable(type, value.getClass())) {
				javaType = getJavaType(type, null);
			}
			ObjectWriter objectWriter;
			if (serializationView != null) {
				objectWriter = this.objectMapper.writerWithView(serializationView);
			} else if (filters != null) {
				objectWriter = this.objectMapper.writer(filters);
			} else {
				objectWriter = this.objectMapper.writer();
			}
			if (javaType != null && javaType.isContainerType()) {
				objectWriter = objectWriter.withType(javaType);
			}
			objectWriter.writeValue(generator, value);

			writeSuffix(generator, object,callback);
			generator.flush();

		} catch (JsonProcessingException ex) {
			throw new HttpMessageNotWritableException("Could not write content: " + ex.getMessage(), ex);
		}
	}

	/**
	 * Write a prefix before the main content.
	 * 
	 * @param generator
	 *            the generator to use for writing content.
	 * @param object
	 *            the object to write to the output message.
	 */
	protected void writePrefix(JsonGenerator generator, Object object,String callback) throws IOException {
	}

	/**
	 * Write a suffix after the main content.
	 * 
	 * @param generator
	 *            the generator to use for writing content.
	 * @param object
	 *            the object to write to the output message.
	 */
	protected void writeSuffix(JsonGenerator generator, Object object,String callback) throws IOException {
	}

	/**
	 * Return the Jackson {@link JavaType} for the specified type and context
	 * class.
	 * <p>
	 * The default implementation returns
	 * {@code typeFactory.constructType(type, contextClass)}, but this can be
	 * overridden in subclasses, to allow for custom generic collection
	 * handling. For instance:
	 * 
	 * <pre class="code">
	 * protected JavaType getJavaType(Type type) {
	 * 	if (type instanceof Class && List.class.isAssignableFrom((Class) type)) {
	 * 		return TypeFactory.collectionType(ArrayList.class, MyBean.class);
	 * 	} else {
	 * 		return super.getJavaType(type);
	 * 	}
	 * }
	 * </pre>
	 * 
	 * @param type
	 *            the generic type to return the Jackson JavaType for
	 * @param contextClass
	 *            a context class for the target type, for example a class in
	 *            which the target type appears in a method signature (can be
	 *            {@code null})
	 * @return the Jackson JavaType
	 */
	protected JavaType getJavaType(Type type, Class<?> contextClass) {
		TypeFactory tf = this.objectMapper.getTypeFactory();
		// Conditional call because Jackson 2.7 does not support null
		// contextClass anymore
		// TypeVariable resolution will not work with Jackson 2.7, see SPR-13853
		// for more details
		return (contextClass != null ? tf.constructType(type, contextClass) : tf.constructType(type));
	}

	/**
	 * Determine the JSON encoding to use for the given content type.
	 * 
	 * @param contentType
	 *            the media type as requested by the caller
	 * @return the JSON encoding to use (never {@code null})
	 */
	protected JsonEncoding getJsonEncoding(MediaType contentType) {
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

	@Override
	public MediaType getDefaultContentType(Object object) throws IOException {
		if (object instanceof MappingJacksonValue) {
			object = ((MappingJacksonValue) object).getValue();
		}
		return super.getDefaultContentType(object);
	}

	@Override
	public Long getContentLength(Object object, MediaType contentType) throws IOException {
		if (object instanceof MappingJacksonValue) {
			object = ((MappingJacksonValue) object).getValue();
		}
		return super.getContentLength(object, contentType);
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
 
	public void setJsonpCallback(String jsonpCallback) {
		this.jsonpCallback = jsonpCallback;
	}

}
