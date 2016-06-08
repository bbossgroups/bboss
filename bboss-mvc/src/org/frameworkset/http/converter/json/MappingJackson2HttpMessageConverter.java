package org.frameworkset.http.converter.json;

import java.io.IOException;

import org.frameworkset.http.MediaType;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MappingJackson2HttpMessageConverter  extends AbstractJackson2HttpMessageConverter {

	private String jsonPrefix;


	/**
	 * Construct a new {@link MappingJackson2HttpMessageConverter} using default configuration
	 * provided by {@link Jackson2ObjectMapperBuilder}.
	 */
	public MappingJackson2HttpMessageConverter() {
		this(Jackson2ObjectMapperBuilder.json().build());
	}

	/**
	 * Construct a new {@link MappingJackson2HttpMessageConverter} with a custom {@link ObjectMapper}.
	 * You can use {@link Jackson2ObjectMapperBuilder} to build it easily.
	 * @see Jackson2ObjectMapperBuilder#json()
	 */
	public MappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
		super(objectMapper, MediaType.APPLICATION_JSON_UTF8,
				new MediaType("application", "*+json", DEFAULT_CHARSET));
	}

	/**
	 * Specify a custom prefix to use for this view's JSON output.
	 * Default is none.
	 * @see #setPrefixJson
	 */
	public void setJsonPrefix(String jsonPrefix) {
		this.jsonPrefix = jsonPrefix;
	}

	/**
	 * Indicate whether the JSON output by this view should be prefixed with ")]}', ". Default is false.
	 * <p>Prefixing the JSON string in this manner is used to help prevent JSON Hijacking.
	 * The prefix renders the string syntactically invalid as a script so that it cannot be hijacked.
	 * This prefix should be stripped before parsing the string as JSON.
	 * @see #setJsonPrefix
	 */
	public void setPrefixJson(boolean prefixJson) {
		this.jsonPrefix = (prefixJson ? ")]}', " : null);
	}


	@Override
	protected void writePrefix(JsonGenerator generator, Object object,String callback) throws IOException {
		if (this.jsonPrefix != null) {
			generator.writeRaw(this.jsonPrefix);
		}
		String jsonpFunction = null;
		if(callback == null)
			jsonpFunction = (object instanceof MappingJacksonValue ? ((MappingJacksonValue) object).getJsonpFunction() : null);
		else 
			jsonpFunction = callback;
		if (jsonpFunction != null) {
			generator.writeRaw("/**/");
			generator.writeRaw(jsonpFunction + "(");
		}
	}

	@Override
	protected void writeSuffix(JsonGenerator generator, Object object,String callback) throws IOException {
		String jsonpFunction = null;
		if(callback == null)
			jsonpFunction = (object instanceof MappingJacksonValue ? ((MappingJacksonValue) object).getJsonpFunction() : null);
		else 
			jsonpFunction = callback;
		if (jsonpFunction != null) {
			generator.writeRaw(");");
		}
	}
	
}
