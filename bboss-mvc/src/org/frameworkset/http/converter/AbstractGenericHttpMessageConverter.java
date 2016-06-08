package org.frameworkset.http.converter;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;

import javax.servlet.http.HttpServletResponse;

import org.frameworkset.http.HttpHeaders;
import org.frameworkset.http.HttpInputMessage;
import org.frameworkset.http.HttpOutputMessage;
import org.frameworkset.http.MediaType;
import org.frameworkset.http.StreamingHttpOutputMessage;
import org.frameworkset.http.converter.AbstractHttpMessageConverter;
import org.frameworkset.http.converter.HttpMessageNotWritableException;

public abstract class AbstractGenericHttpMessageConverter<T> extends AbstractHttpMessageConverter<T>
		implements GenericHttpMessageConverter<T> {

	/**
	 * Construct an {@code AbstractGenericHttpMessageConverter} with no
	 * supported media types.
	 * 
	 * @see #setSupportedMediaTypes
	 */
	protected AbstractGenericHttpMessageConverter() {
	}

	/**
	 * Construct an {@code AbstractGenericHttpMessageConverter} with one
	 * supported media type.
	 * 
	 * @param supportedMediaType
	 *            the supported media type
	 */
	protected AbstractGenericHttpMessageConverter(MediaType supportedMediaType) {
		super(supportedMediaType);
	}

	/**
	 * Construct an {@code AbstractGenericHttpMessageConverter} with multiple
	 * supported media type.
	 * 
	 * @param supportedMediaTypes
	 *            the supported media types
	 */
	protected AbstractGenericHttpMessageConverter(MediaType... supportedMediaTypes) {
		super(supportedMediaTypes);
	}

	@Override
	public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
		return canRead(contextClass, mediaType);
	}

	@Override
	public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
		return canWrite(clazz, mediaType);
	}

	/**
	 * This implementation sets the default headers by calling {@link #addDefaultHeaders},
	 * and then calls {@link #writeInternal}.
	 */
	public final void write(final T t, final Type type, MediaType contentType, final HttpOutputMessage outputMessage,final HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotWritableException {

		final HttpHeaders headers = outputMessage.getHeaders();
		addDefaultHeaders(headers, t, contentType);

		if (outputMessage instanceof StreamingHttpOutputMessage) {
			StreamingHttpOutputMessage streamingOutputMessage = (StreamingHttpOutputMessage) outputMessage;
			streamingOutputMessage.setBody(new StreamingHttpOutputMessage.Body() {
				@Override
				public void writeTo(final OutputStream outputStream) throws IOException {
					writeInternal(t, type, new HttpOutputMessage() {
						@Override
						public OutputStream getBody() throws IOException {
							return outputStream;
						}
						@Override
						public HttpHeaders getHeaders() {
							return headers;
						}
						@Override
						public HttpServletResponse getResponse() {
							// TODO Auto-generated method stub
							return outputMessage.getResponse();
						}
					},inputMessage);
				}
			});
		}
		else {
			writeInternal(t, type, outputMessage,inputMessage);
			outputMessage.getBody().flush();
		}
	}

	@Override
	public void writeInternal(T t,  HttpOutputMessage outputMessage,HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotWritableException {

		writeInternal(t, null, outputMessage,inputMessage);
	}

	/**
	 * Abstract template method that writes the actual body. Invoked from
	 * {@link #write}.
	 * 
	 * @param t
	 *            the object to write to the output message
	 * @param type
	 *            the type of object to write, can be {@code null} if not
	 *            specified.
	 * @param outputMessage
	 *            the HTTP output message to write to
	 * @throws IOException
	 *             in case of I/O errors
	 * @throws HttpMessageNotWritableException
	 *             in case of conversion errors
	 */
	protected abstract void writeInternal(T t, Type type, HttpOutputMessage outputMessage,HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotWritableException;

}
