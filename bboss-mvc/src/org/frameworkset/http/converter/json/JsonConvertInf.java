package org.frameworkset.http.converter.json;

import org.frameworkset.spi.InitializingBean;

public interface JsonConvertInf extends InitializingBean {
	public void setJsonpCallback(String jsonpCallback);
	/**
	 * Indicate whether the JSON output by this view should be prefixed with ")]}', ". Default is false.
	 * <p>Prefixing the JSON string in this manner is used to help prevent JSON Hijacking.
	 * The prefix renders the string syntactically invalid as a script so that it cannot be hijacked.
	 * This prefix should be stripped before parsing the string as JSON.
	 * @see #setPrefixJson
	 */
	public void setPrefixJson(boolean prefixJson) ;
	/**
	 * @param objectMapper
	 */
	public void setObjectMapper(Object objectMapper);
	public String getDateFormat();

	public void setDateFormat(String dateFormat) ;

	public String getLocale() ;

	public void setLocale(String locale) ;

	public String getTimeZone();

	public void setTimeZone(String timeZone);

	public boolean isDisableTimestamp() ;

	public void setDisableTimestamp(boolean disableTimestamp) ;

	void setFailedOnUnknownProperties(boolean failedOnUnknownProperties);
}
