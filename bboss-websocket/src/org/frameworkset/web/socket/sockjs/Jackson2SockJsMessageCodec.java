package org.frameworkset.web.socket.sockjs;

import java.io.IOException;
import java.io.InputStream;

import org.frameworkset.http.converter.json.Jackson2ObjectMapperBuilder;
import org.frameworkset.util.Assert;

import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Jackson2SockJsMessageCodec extends AbstractSockJsMessageCodec {

	private final ObjectMapper objectMapper;


	public Jackson2SockJsMessageCodec() {
		this.objectMapper = Jackson2ObjectMapperBuilder.json().build();
	}

	public Jackson2SockJsMessageCodec(ObjectMapper objectMapper) {
		Assert.notNull(objectMapper, "ObjectMapper must not be null");
		this.objectMapper = objectMapper;
	}


	@Override
	public String[] decode(String content) throws IOException {
		return this.objectMapper.readValue(content, String[].class);
	}

	@Override
	public String[] decodeInputStream(InputStream content) throws IOException {
		return this.objectMapper.readValue(content, String[].class);
	}

	@Override
	protected char[] applyJsonQuoting(String content) {
		return JsonStringEncoder.getInstance().quoteAsString(content);
	}

}
