package org.frameworkset.http.converter.json;

import com.fasterxml.jackson.databind.ser.FilterProvider;

public class MappingJacksonValue {

	private Object value;

	private Class<?> serializationView;

	private FilterProvider filters;

	private String jsonpFunction;


	/**
	 * Create a new instance wrapping the given POJO to be serialized.
	 * @param value the Object to be serialized
	 */
	public MappingJacksonValue(Object value) {
		this.value = value;
	}


	/**
	 * Modify the POJO to serialize.
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Return the POJO that needs to be serialized.
	 */
	public Object getValue() {
		return this.value;
	}

	/**
	 * Set the serialization view to serialize the POJO with.
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writerWithView(Class)
	 * @see com.fasterxml.jackson.annotation.JsonView
	 */
	public void setSerializationView(Class<?> serializationView) {
		this.serializationView = serializationView;
	}

	/**
	 * Return the serialization view to use.
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writerWithView(Class)
	 * @see com.fasterxml.jackson.annotation.JsonView
	 */
	public Class<?> getSerializationView() {
		return this.serializationView;
	}

	/**
	 * Set the Jackson filter provider to serialize the POJO with.
	 * @since 4.2
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writer(FilterProvider)
	 * @see com.fasterxml.jackson.annotation.JsonFilter
	 * @see Jackson2ObjectMapperBuilder#filters(FilterProvider)
	 */
	public void setFilters(FilterProvider filters) {
		this.filters = filters;
	}

	/**
	 * Return the Jackson filter provider to use.
	 * @since 4.2
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writer(FilterProvider)
	 * @see com.fasterxml.jackson.annotation.JsonFilter
	 */
	public FilterProvider getFilters() {
		return this.filters;
	}

	/**
	 * Set the name of the JSONP function name.
	 */
	public void setJsonpFunction(String functionName) {
		this.jsonpFunction = functionName;
	}

	/**
	 * Return the configured JSONP function name.
	 */
	public String getJsonpFunction() {
		return this.jsonpFunction;
	}

}
