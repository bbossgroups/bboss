package org.frameworkset.http.converter.json;

import java.io.IOException;
import java.util.List;

import org.frameworkset.http.HttpHeaders;
import org.frameworkset.http.HttpInputMessage;
import org.frameworkset.http.HttpOutputMessage;
import org.frameworkset.http.MediaType;
import org.frameworkset.http.converter.AbstractHttpMessageConverter;
import org.frameworkset.http.converter.HttpMessageNotReadableException;
import org.frameworkset.http.converter.HttpMessageNotWritableException;

public class MappingJacksonHttpMessageConverter extends AbstractHttpMessageConverter<Object> implements JsonConvertInf{
	private AbstractHttpMessageConverter<Object> convert;
	
	public static final String JSONPCALLBACK_PARAM_NAME = "jsonp_callback";
	public MappingJacksonHttpMessageConverter()
	{
		String jacson2Class = "org.frameworkset.http.converter.json.MappingJackson2HttpMessageConverter";
		try {			
			
			
				convert = (AbstractHttpMessageConverter<Object>) Class.forName(jacson2Class).newInstance();
			
		} catch (ClassNotFoundException e) {
			
		} catch (InstantiationException e) {
			
		} catch (IllegalAccessException e) {
			
		}
		 catch (NoClassDefFoundError e) {
			
		}
		
		catch (Exception e) {
			
		}
		if(convert  == null)
		{
			jacson2Class = "org.frameworkset.http.converter.json.MappingJackson1HttpMessageConverter";
			try {
				
				
				
				convert = (AbstractHttpMessageConverter) Class.forName(jacson2Class).newInstance();
				
			} catch (ClassNotFoundException e) {
				
			} catch (InstantiationException e) {
				
			} catch (IllegalAccessException e) {
				
			}
			 catch (NoClassDefFoundError e) {
					
				}
			catch (Exception e) {
				
			}
		}
	}
	@Override
	public boolean canWrite(String dataype) {
		// TODO Auto-generated method stub
		return convert.canWrite(dataype);
	}

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return convert.supports(clazz);
	}

	@Override
	public Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		// TODO Auto-generated method stub
		return convert.readInternal(clazz, inputMessage);
	}

	@Override
	public void writeInternal(Object t, HttpOutputMessage outputMessage, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotWritableException {
		convert.writeInternal(t, outputMessage, inputMessage);
	}
	@Override
	public void setResponseCharset(String charset) {
		// TODO Auto-generated method stub
		convert.setResponseCharset(charset);
	}
	@Override
	public String getRequetBodyDataType() {
		// TODO Auto-generated method stub
		return convert.getRequetBodyDataType();
	}
	@Override
	public String getResponseBodyDataType() {
		// TODO Auto-generated method stub
		return convert.getResponseBodyDataType();
	}
	@Override
	public boolean canRead(String datatype) {
		// TODO Auto-generated method stub
		return convert.canRead(datatype);
	}
	@Override
	public void setSupportedMediaTypes(List<MediaType> supportedMediaTypes) {
		// TODO Auto-generated method stub
		convert.setSupportedMediaTypes(supportedMediaTypes);
	}
	@Override
	public List<MediaType> getSupportedMediaTypes() {
		// TODO Auto-generated method stub
		return convert.getSupportedMediaTypes();
	}
	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		// TODO Auto-generated method stub
		return convert.canRead(clazz, mediaType);
	}
	@Override
	public boolean canRead(MediaType mediaType) {
		// TODO Auto-generated method stub
		return convert.canRead(mediaType);
	}
	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		// TODO Auto-generated method stub
		return convert.canWrite(clazz, mediaType);
	}
	@Override
	public boolean canWrite(MediaType mediaType) {
		// TODO Auto-generated method stub
		return convert.canWrite(mediaType);
	}
	@Override
	public void addDefaultHeaders(HttpHeaders headers, Object t, MediaType contentType) throws IOException {
		// TODO Auto-generated method stub
		convert.addDefaultHeaders(headers, t, contentType);
	}
	@Override
	public MediaType getDefaultContentType(Object t) throws IOException {
		// TODO Auto-generated method stub
		return convert.getDefaultContentType(t);
	}
	@Override
	public Long getContentLength(Object t, MediaType contentType) throws IOException {
		// TODO Auto-generated method stub
		return convert.getContentLength(t, contentType);
	}
	@Override
	public boolean isdefault() {
		// TODO Auto-generated method stub
		return convert.isdefault();
	}
	@Override
	public MediaType getDefaultAcceptedMediaType() {
		// TODO Auto-generated method stub
		return convert.getDefaultAcceptedMediaType();
	}
	
	 

	public void setJsonpCallback(String jsonpCallback) {
		((JsonConvertInf)convert).setJsonpCallback(jsonpCallback);
	}
	
	/**
	 * Indicate whether the JSON output by this view should be prefixed with ")]}', ". Default is false.
	 * <p>Prefixing the JSON string in this manner is used to help prevent JSON Hijacking.
	 * The prefix renders the string syntactically invalid as a script so that it cannot be hijacked.
	 * This prefix should be stripped before parsing the string as JSON.
	 * @see #setJsonPrefix
	 */
	public void setPrefixJson(boolean prefixJson) {
		
		((JsonConvertInf)convert).setPrefixJson(prefixJson);
	}
	public void setObjectMapper(Object objectMapper) {
		((JsonConvertInf)convert).setObjectMapper( objectMapper);
	}

}
