package org.frameworkset.http.converter.json;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.frameworkset.http.HttpHeaders;
import org.frameworkset.http.HttpInputMessage;

public class MappingJacksonInputMessage  implements HttpInputMessage {

	private final InputStream body;

	private final HttpHeaders headers;

	private Class<?> deserializationView;


	public MappingJacksonInputMessage(InputStream body, HttpHeaders headers) {
		this.body = body;
		this.headers = headers;
	}

	public MappingJacksonInputMessage(InputStream body, HttpHeaders headers, Class<?> deserializationView) {
		this(body, headers);
		this.deserializationView = deserializationView;
	}


	@Override
	public InputStream getBody() throws IOException {
		return this.body;
	}

	@Override
	public HttpHeaders getHeaders() {
		return this.headers;
	}

	public void setDeserializationView(Class<?> deserializationView) {
		this.deserializationView = deserializationView;
	}

	public Class<?> getDeserializationView() {
		return this.deserializationView;
	}

	@Override
	public HttpServletRequest getServletRequest() {
		// TODO Auto-generated method stub
		return null;
	}

}
