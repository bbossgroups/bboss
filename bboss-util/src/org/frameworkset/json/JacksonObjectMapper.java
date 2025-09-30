package org.frameworkset.json;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

public interface JacksonObjectMapper {

	public ObjectMapper getObjectMapper();
	public JavaType getJavaType(Class containerType, Class ... beanClass);
	public JavaType getJavaMapType(Class containerType, Class keyClass,Class valueClass);
	<T> T json2Object(String jsonString, Class<T> toclass);

	<T> T json2Object(String jsonString, Class<T> toclass, boolean ALLOW_SINGLE_QUOTES);
	
	<T> T json2Object(InputStream jsonString, Class<T> toclass, boolean ALLOW_SINGLE_QUOTES);
	//	  public   <T> T json2Object(String jsonString,TypeReference<T> ref) {
	//			return json2Object(jsonString,ref,true);
	//			
	//		
	//		}
	//	  
	//	  public   <T> T json2Object(String jsonString,TypeReference<T> ref,boolean ALLOW_SINGLE_QUOTES) {
	//			// TODO Auto-generated method stub
	//
	////			String jsonString = "[{'from_date':'2001-09-21','to_date':'2011-04-02','company':'人寿保险','department':'xxx','position':'主管' },{'from_date':'0002-12-01','to_date':'2011-04-02', 'company':'人寿保险','department':'xxx','position':'主管' }]";
	//			ObjectMapper mapper = new ObjectMapper();
	//			mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
	//			try {
	//				T value = mapper.readValue(jsonString, ref);
	//				return value;
	//				
	//				
	//			} catch (Exception e) {
	//				throw new IllegalArgumentException(jsonString,e);
	//			}
	//			
	//		
	//		}

	String object2json(Object object);

	String object2json(Object object, boolean ALLOW_SINGLE_QUOTES);

	void object2json(Object object, File writer);

	void object2json(Object object, File writer, boolean ALLOW_SINGLE_QUOTES);

	void object2json(Object object, OutputStream writer);

	void object2json(Object object, OutputStream writer, boolean ALLOW_SINGLE_QUOTES);

    void object2jsonDisableCloseAndFlush(Object object,OutputStream writer) ;
    void object2jsonDisableCloseAndFlush(Object object,OutputStream writer,boolean ALLOW_SINGLE_QUOTES);

	void object2json(Object object, Writer writer);

	void object2json(Object object, Writer writer, boolean ALLOW_SINGLE_QUOTES);

	byte[] object2jsonAsbyte(Object object);

	byte[] object2jsonAsbyte(Object object, boolean ALLOW_SINGLE_QUOTES);

	<T> T json2ObjectWithType(String jsonString, JsonTypeReference<T> ref, boolean aLLOW_SINGLE_QUOTES);
	
	<T> T json2ObjectWithType(InputStream jsonString, JsonTypeReference<T> ref, boolean aLLOW_SINGLE_QUOTES);
	public String getDateFormat();

	public void setDateFormat(String dateFormat) ;

	public String getLocale() ;

	public void setLocale(String locale) ;

	public String getTimeZone();

	public void setTimeZone(String timeZone);

	public boolean isDisableTimestamp() ;

	public void setDisableTimestamp(boolean disableTimestamp) ;

	void setFailedOnUnknownProperties(boolean failedOnUnknownProperties);
	void init();
}