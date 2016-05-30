package org.frameworkset.http.converter.json;

import java.io.IOException;

import org.frameworkset.http.HttpInputMessage;
import org.frameworkset.http.HttpOutputMessage;
import org.frameworkset.http.converter.AbstractHttpMessageConverter;
import org.frameworkset.http.converter.HttpMessageNotReadableException;
import org.frameworkset.http.converter.HttpMessageNotWritableException;

public class MappingJacksonDelegateHttpMessageConverter extends AbstractHttpMessageConverter<Object>{
	private AbstractHttpMessageConverter<Object> convert;
	public MappingJacksonDelegateHttpMessageConverter()
	{
		String jacson2Class = "org.frameworkset.http.converter.json.MappingJackson2HttpMessageConverter";
		try {
			Class clazz = Class.forName(jacson2Class);
			if(clazz != null					)
			{
				convert = (AbstractHttpMessageConverter<Object>) clazz.newInstance();
			}
		} catch (ClassNotFoundException e) {
			
		} catch (InstantiationException e) {
			
		} catch (IllegalAccessException e) {
			
		}
		if(convert  == null)
		{
			jacson2Class = "org.frameworkset.http.converter.json.MappingJacksonHttpMessageConverter";
			try {
				Class clazz = Class.forName(jacson2Class);
				if(clazz != null					)
				{
					convert = (AbstractHttpMessageConverter) clazz.newInstance();
				}
			} catch (ClassNotFoundException e) {
				
			} catch (InstantiationException e) {
				
			} catch (IllegalAccessException e) {
				
			}
		}
	}
	@Override
	public boolean canWrite(String dataype) {
		// TODO Auto-generated method stub
		return convert.canRead(dataype);
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

}
