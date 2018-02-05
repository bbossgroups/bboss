package org.frameworkset.http.converter.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.frameworkset.http.MediaType;
import org.frameworkset.spi.InitializingBean;
import org.frameworkset.util.annotations.DateFormateMeta;

import java.io.IOException;

public class MappingJackson2HttpMessageConverter  extends AbstractJackson2HttpMessageConverter implements InitializingBean {

	private String jsonPrefix;
	private String dateFormat;

	private String locale;

	private String timeZone;
	private boolean disableTimestamp = false;
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
	 * Construct a new {@link MappingJackson2HttpMessageConverter} using default configuration
	 * provided by {@link Jackson2ObjectMapperBuilder}.
	 */
	public MappingJackson2HttpMessageConverter() {
		this(Jackson2ObjectMapperBuilder.json()
								.build()
								.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false));
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

	public void setFailedOnUnknownProperties(boolean failedOnUnknownProperties) {
		this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,failedOnUnknownProperties);
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

	public void init() {
		if(dateFormat != null && !dateFormat.equals("")) {
			DateFormateMeta dateFormateMeta = DateFormateMeta.buildDateFormateMeta(this.dateFormat, this.locale, this.timeZone);
			this.objectMapper.setDateFormat(dateFormateMeta.toDateFormat());


		}
		if(this.disableTimestamp){
			objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.init();
	}
}
