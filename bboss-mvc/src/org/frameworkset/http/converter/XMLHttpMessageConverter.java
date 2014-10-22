package org.frameworkset.http.converter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.frameworkset.http.HttpHeaders;
import org.frameworkset.http.HttpInputMessage;
import org.frameworkset.http.HttpOutputMessage;
import org.frameworkset.http.MediaType;
import org.frameworkset.util.Assert;
import org.frameworkset.util.ClassUtils;
import org.frameworkset.util.FileCopyUtils;
import org.frameworkset.util.annotations.ValueConstants;

import com.thoughtworks.xstream.XStream;

public class XMLHttpMessageConverter  extends AbstractHttpMessageConverter<Object> {
	
	private final ConcurrentMap<Class, JAXBContext> jaxbContexts = new ConcurrentHashMap<Class, JAXBContext>();

	public static  Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	private final List<Charset> availableCharsets;

	private boolean writeAcceptCharset = true;

	public XMLHttpMessageConverter() {
		super(xmlmediatypes);
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
		return true;
	}
	

	protected Object readFromSource(Class<?> clazz, HttpHeaders headers, Source source) throws IOException {
		try {
			Unmarshaller unmarshaller = createUnmarshaller(clazz);
			if (clazz.isAnnotationPresent(XmlRootElement.class)) {
				return unmarshaller.unmarshal(source);
			}
			else {
				JAXBElement jaxbElement = unmarshaller.unmarshal(source, clazz);
				return jaxbElement.getValue();
			}
		}
		catch (UnmarshalException ex) {
			throw new HttpMessageNotReadableException("Could not unmarshal to [" + clazz + "]: " + ex.getMessage(), ex);

		}
		catch (JAXBException ex) {
			throw new HttpMessageConversionException("Could not instantiate JAXBContext: " + ex.getMessage(), ex);
		}
	}

//	@Override
//	protected String readInternal(Class clazz, HttpInputMessage inputMessage) throws IOException {
//		MediaType contentType = inputMessage.getHeaders().getContentType();
//		Charset charset = contentType.getCharSet() != null ? contentType.getCharSet() : DEFAULT_CHARSET;
//		return FileCopyUtils.copyToString(new InputStreamReader(inputMessage.getBody(), charset));
//	}
	
	@Override
	public final Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException {
		if(String.class.isAssignableFrom(clazz))
		{
			MediaType contentType = inputMessage.getHeaders().getContentType();
			Charset charset = contentType.getCharSet() != null ? contentType.getCharSet() : DEFAULT_CHARSET;
			return FileCopyUtils.copyToString(new InputStreamReader(inputMessage.getBody(), charset));
		}
		else 
		{
			if (clazz.isAnnotationPresent(XmlRootElement.class)) {
				return readFromSource(clazz, inputMessage.getHeaders(), new StreamSource(inputMessage.getBody()));
			}
			else
			{
				XStream xStream = new XStream();  
				MediaType contentType = inputMessage.getHeaders().getContentType();
				Charset charset = contentType.getCharSet() != null ? contentType.getCharSet() : DEFAULT_CHARSET;
				return xStream.fromXML(new InputStreamReader(inputMessage.getBody(), charset));
			}
			
		}
	}
	protected boolean canWrite(MediaType mediaType) {
		if (mediaType == null || MediaType.ALL.equals(mediaType)) {
			return false;
		}
		for (MediaType supportedMediaType : getSupportedMediaTypes()) {
			if (supportedMediaType.includes(mediaType)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns true if any of the {@linkplain #setSupportedMediaTypes(List) supported media types}
	 * include the given media type.
	 * @param mediaType the media type to read, can be {@code null} if not specified. Typically the value of a
	 *                  {@code Content-Type} header.
	 * @return true if the supported media types include the media type, or if the media type is {@code null}
	 */
	protected boolean canRead(MediaType mediaType) {
		if (mediaType == null) {
			return false;
		}
		for (MediaType supportedMediaType : getSupportedMediaTypes()) {
			if (supportedMediaType.includes(mediaType)) {
				return true;
			}
		}
		return false;
	}
	private void convertToXML(Object s,HttpOutputMessage outputMessage) throws IOException
	{
		if(s == null)
			;
		else if(s instanceof String)
		{
			MediaType contentType = outputMessage.getHeaders().getContentType();
			Charset charset = contentType.getCharSet() != null ? contentType.getCharSet() : DEFAULT_CHARSET;
			FileCopyUtils.copy((String)s, new OutputStreamWriter(outputMessage.getBody(), charset));
		}
		else 
		{
			try {
				Class clazz = ClassUtils.getUserClass(s);
				if (clazz.isAnnotationPresent(XmlRootElement.class)) {			
					Marshaller marshaller = createMarshaller(clazz);
					try {
						setCharset(outputMessage.getHeaders().getContentType(), marshaller);
						marshaller.marshal(s, new StreamResult(outputMessage.getBody()));
					} catch (MarshalException ex) {
						throw new HttpMessageNotWritableException("Could not marshal [" + s + "]: " + ex.getMessage(), ex);
					}
					catch (JAXBException ex) {
						throw new HttpMessageConversionException("Could not instantiate JAXBContext: " + ex.getMessage(), ex);
					}
				
				}
				
				else
				{
					XStream xStream = new XStream();  
					MediaType contentType = outputMessage.getHeaders().getContentType();
					Charset charset = contentType.getCharSet() != null ? contentType.getCharSet() : DEFAULT_CHARSET;
					xStream.toXML(s,new OutputStreamWriter(outputMessage.getBody(), charset));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	protected Long getContentLength(Object s, MediaType contentType) {
		if(s instanceof String)
		{
			if (contentType != null && contentType.getCharSet() != null) {
				Charset charset = contentType.getCharSet();
				try {
					return (long) ((String)s).getBytes(charset.name()).length;
				}
				catch (UnsupportedEncodingException ex) {
					// should not occur
					throw new InternalError(ex.getMessage());
				}
			}
			else
				return null;
		}
		else {
			return null;
		}
	}
	
	@Override
	protected void writeInternal(Object s, HttpOutputMessage outputMessage,HttpInputMessage inputMessage) throws IOException {
		if (writeAcceptCharset) {
			outputMessage.getHeaders().setAcceptCharset(getAcceptedCharsets());
		}
		convertToXML(s,outputMessage);
//		if(str != null)
//		{
//			MediaType contentType = outputMessage.getHeaders().getContentType();
//			Charset charset = contentType.getCharSet() != null ? contentType.getCharSet() : DEFAULT_CHARSET;
//			FileCopyUtils.copy(str, new OutputStreamWriter(outputMessage.getBody(), charset));
//		}
		
//		FileCopyUtils.copy(s, new OutputStreamWriter(outputMessage.getBody(), charset));
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
		super.responsecontenteype = new MediaType("application","xml",Charset.forName(charset));
	}
	
	/**
	 * Creates a new {@link Marshaller} for the given class.
	 *
	 * @param clazz the class to create the marshaller for
	 * @return the {@code Marshaller}
	 * @throws HttpMessageConversionException in case of JAXB errors
	 */
	protected final Marshaller createMarshaller(Class clazz) {
		try {
			JAXBContext jaxbContext = getJaxbContext(clazz);
			return jaxbContext.createMarshaller();
		}
		catch (JAXBException ex) {
			throw new HttpMessageConversionException(
					"Could not create Marshaller for class [" + clazz + "]: " + ex.getMessage(), ex);
		}
	}

	/**
	 * Creates a new {@link Unmarshaller} for the given class.
	 *
	 * @param clazz the class to create the unmarshaller for
	 * @return the {@code Unmarshaller}
	 * @throws HttpMessageConversionException in case of JAXB errors
	 */
	protected final Unmarshaller createUnmarshaller(Class clazz) throws JAXBException {
		try {
			JAXBContext jaxbContext = getJaxbContext(clazz);
			return jaxbContext.createUnmarshaller();
		}
		catch (JAXBException ex) {
			throw new HttpMessageConversionException(
					"Could not create Unmarshaller for class [" + clazz + "]: " + ex.getMessage(), ex);
		}
	}

	/**
	 * Returns a {@link JAXBContext} for the given class.
	 *
	 * @param clazz the class to return the context for
	 * @return the {@code JAXBContext}
	 * @throws HttpMessageConversionException in case of JAXB errors
	 */
	protected final JAXBContext getJaxbContext(Class clazz) {
		Assert.notNull(clazz, "'clazz' must not be null");
		JAXBContext jaxbContext = jaxbContexts.get(clazz);
		if (jaxbContext == null) {
			try {
				jaxbContext = JAXBContext.newInstance(clazz);
				jaxbContexts.putIfAbsent(clazz, jaxbContext);
			}
			catch (JAXBException ex) {
				throw new HttpMessageConversionException(
						"Could not instantiate JAXBContext for class [" + clazz + "]: " + ex.getMessage(), ex);
			}
		}
		return jaxbContext;
	}
	
	
	

	protected boolean writeToResult(Object o, HttpHeaders headers, Result result) throws IOException {
		try {
			Class clazz = ClassUtils.getUserClass(o);
			if (clazz.isAnnotationPresent(XmlRootElement.class)) {			
				Marshaller marshaller = createMarshaller(clazz);
				setCharset(headers.getContentType(), marshaller);
				marshaller.marshal(o, result);
				return true;
			}
			else
			{
				
				return false;
			}
		}
		catch (MarshalException ex) {
			throw new HttpMessageNotWritableException("Could not marshal [" + o + "]: " + ex.getMessage(), ex);
		}
		catch (JAXBException ex) {
			throw new HttpMessageConversionException("Could not instantiate JAXBContext: " + ex.getMessage(), ex);
		}
	}
	
	private void setCharset(MediaType contentType, Marshaller marshaller) throws PropertyException {
		if (contentType != null && contentType.getCharSet() != null) {
			marshaller.setProperty(Marshaller.JAXB_ENCODING, contentType.getCharSet().name());
		}
	}

	@Override
	public String getRequetBodyDataType() {
		
		return ValueConstants.datatype_xml;
	}
	/**
	 * 获取用户响应报文对应的数据类型：String,json,xml
	 * @return
	 */
	public String getResponseBodyDataType()
	{
		return ValueConstants.datatype_xml;
	}
	

	public boolean canWrite(String datatype) {
		// TODO Auto-generated method stub
		if(datatype == null)
			return false;
		
		if(datatype.equals(ValueConstants.datatype_xml))
				return true;
		else
			return false;
	}
}
