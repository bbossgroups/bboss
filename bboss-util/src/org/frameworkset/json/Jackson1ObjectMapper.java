package org.frameworkset.json;

import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;
import org.frameworkset.util.annotations.DateFormateMeta;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Type;
public class Jackson1ObjectMapper implements JacksonObjectMapper {
	ObjectMapper mapper = null;
	private String dateFormat;

	private String locale;

	private String timeZone;
	private boolean disableTimestamp = false;
	boolean failedOnUnknownProperties = false;

	@Override
	public String getDateFormat() {
		return dateFormat;
	}
	public com.fasterxml.jackson.databind.JavaType getJavaType(Class containerType, Class ... beanClass){
		return null;
	}
	public com.fasterxml.jackson.databind.ObjectMapper getObjectMapper(){
		return null;
	}


	public com.fasterxml.jackson.databind.JavaType getJavaMapType(Class containerType, Class keyClass, Class valueClass){
		return null;
	}
	@Override
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	@Override
	public String getLocale() {
		return locale;
	}

	@Override
	public void setLocale(String locale) {
		this.locale = locale;
	}

	@Override
	public String getTimeZone() {
		return timeZone;
	}

	@Override
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	@Override
	public boolean isDisableTimestamp() {
		return disableTimestamp;
	}

	@Override
	public void setDisableTimestamp(boolean disableTimestamp) {
		this.disableTimestamp = disableTimestamp;
	}

	public boolean isFailedOnUnknownProperties() {
		return failedOnUnknownProperties;
	}

	@Override
	public void setFailedOnUnknownProperties(boolean failedOnUnknownProperties) {
		this.failedOnUnknownProperties = failedOnUnknownProperties;
	}

	@Override
	public void init() {
		if(dateFormat != null && !dateFormat.equals("")) {
			DateFormateMeta dateFormateMeta = DateFormateMeta.buildDateFormateMeta(this.dateFormat, this.locale, this.timeZone);
			this.mapper.setDateFormat(dateFormateMeta.toDateFormat());


		}
		if(this.disableTimestamp){
			mapper.disable(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS);
		}
		this.mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,failedOnUnknownProperties);
	}

	public Jackson1ObjectMapper(){
		mapper = new ObjectMapper();
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
	}
	 /* (non-Javadoc)
		 * @see org.frameworkset.json.JacksonObjectMapper#json2Object(java.lang.String, java.lang.Class)
		 */
		@Override
		public  <T> T json2Object(String jsonString,Class<T> toclass) {
				// TODO Auto-generated method stub
				return json2Object(jsonString,toclass,true);
				
			
			}
		    
		 /* (non-Javadoc)
		 * @see org.frameworkset.json.JacksonObjectMapper#json2Object(java.lang.String, java.lang.Class, boolean)
		 */
		@Override
		public  <T> T json2Object(String jsonString,Class<T> toclass,boolean ALLOW_SINGLE_QUOTES) {
				// TODO Auto-generated method stub

//				String jsonString = "[{'from_date':'2001-09-21','to_date':'2011-04-02','company':'人寿保险','department':'xxx','position':'主管' },{'from_date':'0002-12-01','to_date':'2011-04-02', 'company':'人寿保险','department':'xxx','position':'主管' }]";
//				ObjectMapper mapper = new ObjectMapper();
//				mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
				try {
					T value = mapper.readValue(jsonString, toclass);
					return value;
					
					
				} catch (Exception e) {
					throw new IllegalArgumentException(jsonString,e);
				}
				
				
			
			}
		
		/* (non-Javadoc)
		 * @see org.frameworkset.json.JacksonObjectMapper#json2Object(java.lang.String, java.lang.Class, boolean)
		 */
		@Override
		public  <T> T json2Object(InputStream jsonString,Class<T> toclass,boolean ALLOW_SINGLE_QUOTES) {
				// TODO Auto-generated method stub

//				String jsonString = "[{'from_date':'2001-09-21','to_date':'2011-04-02','company':'人寿保险','department':'xxx','position':'主管' },{'from_date':'0002-12-01','to_date':'2011-04-02', 'company':'人寿保险','department':'xxx','position':'主管' }]";
//				ObjectMapper mapper = new ObjectMapper();
//				mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
				try {
					T value = mapper.readValue(jsonString, toclass);
					return value;
					
					
				} catch (Exception e) {
					throw new IllegalArgumentException("",e);
				}
				
				
			
			}
//		  public   <T> T json2Object(String jsonString,TypeReference<T> ref) {
//				return json2Object(jsonString,ref,true);
//				
//			
//			}
//		  
		@Override
		public <T> T json2ObjectWithType(InputStream jsonString, final JsonTypeReference<T> ref,
				boolean ALLOW_SINGLE_QUOTES) {
			ObjectMapper mapper = new ObjectMapper();
			
			
			TypeReference<?> ref_ = new TypeReference<Object>(){
				@Override
				public Type getType() {
					// TODO Auto-generated method stub
					return ref.getType();
				}
				
			};  
					//(TypeReference)ref.getTypeReference();
			mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
			try {
				T value = mapper.readValue(jsonString, ref_);
				return value;
				
				
			} catch (Exception e) {
				throw new IllegalArgumentException(e);
			}
		}
		  public  <T> T json2ObjectWithType(String jsonString,final JsonTypeReference<T> ref,boolean ALLOW_SINGLE_QUOTES) {
				// TODO Auto-generated method stub
	
//				String jsonString = "[{'from_date':'2001-09-21','to_date':'2011-04-02','company':'人寿保险','department':'xxx','position':'主管' },{'from_date':'0002-12-01','to_date':'2011-04-02', 'company':'人寿保险','department':'xxx','position':'主管' }]";
				ObjectMapper mapper = new ObjectMapper();
				
				
				TypeReference<?> ref_ = new TypeReference<Object>(){
					@Override
					public Type getType() {
						// TODO Auto-generated method stub
						return ref.getType();
					}
					
				};  
						//(TypeReference)ref.getTypeReference();
				mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
				try {
					T value = mapper.readValue(jsonString, ref_);
					return value;
					
					
				} catch (Exception e) {
					throw new IllegalArgumentException(jsonString,e);
				}
				
			
			}
		  
		  /* (non-Javadoc)
		 * @see org.frameworkset.json.JacksonObjectMapper#object2json(java.lang.Object)
		 */
		@Override
		public   String object2json(Object object) {
		    	return object2json(object,true) ;
				
				
			
			}
		  
		  /* (non-Javadoc)
		 * @see org.frameworkset.json.JacksonObjectMapper#object2json(java.lang.Object, boolean)
		 */
		@Override
		public   String object2json(Object object,boolean ALLOW_SINGLE_QUOTES) {
//		    	ObjectMapper mapper = new ObjectMapper();
//				mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
				try {
					String value = mapper.writeValueAsString(object);
					
					return value;
					
					
				} catch (Exception e) {
					throw new IllegalArgumentException("错误的json序列化操作",e);
				}
				
				
			
			}
		  
		  /* (non-Javadoc)
		 * @see org.frameworkset.json.JacksonObjectMapper#object2json(java.lang.Object, java.io.File)
		 */
		@Override
		public   void object2json(Object object,File writer) {
		    	object2json(object,writer,true) ;
			}
		  
		  /* (non-Javadoc)
		 * @see org.frameworkset.json.JacksonObjectMapper#object2json(java.lang.Object, java.io.File, boolean)
		 */
		@Override
		public   void object2json(Object object,File writer,boolean ALLOW_SINGLE_QUOTES) {
//		    	ObjectMapper mapper = new ObjectMapper();
//				mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
				try {
					mapper.writeValue(writer,object);
					
					
					
					
				} catch (Exception e) {
					throw new IllegalArgumentException("错误的json序列化操作",e);
				}
				
				
			
			}
    @Override
    public  void object2jsonDisableCloseAndFlush(Object object,OutputStream writer) {
        throw new RuntimeException("not support object2jsonDisableCloseAndFlush");
    }
    @Override
    public   void object2jsonDisableCloseAndFlush(Object object,OutputStream writer,boolean ALLOW_SINGLE_QUOTES) {
        throw new RuntimeException("not support object2jsonDisableCloseAndFlush");
    }
    /* (non-Javadoc)
		 * @see org.frameworkset.json.JacksonObjectMapper#object2json(java.lang.Object, java.io.OutputStream)
		 */
		@Override
		public  void object2json(Object object,OutputStream writer) {
		    	object2json(object,writer,true) ;
			}
		  
		  /* (non-Javadoc)
		 * @see org.frameworkset.json.JacksonObjectMapper#object2json(java.lang.Object, java.io.OutputStream, boolean)
		 */
		@Override
		public   void object2json(Object object,OutputStream writer,boolean ALLOW_SINGLE_QUOTES) {
//		    	ObjectMapper mapper = new ObjectMapper();
//				mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
				try {
					mapper.writeValue(writer,object);
					
					
					
					
				} catch (Exception e) {
					throw new IllegalArgumentException("错误的json序列化操作",e);
				}
				
				
			
			}
		  
		  /* (non-Javadoc)
		 * @see org.frameworkset.json.JacksonObjectMapper#object2json(java.lang.Object, java.io.Writer)
		 */
		@Override
		public   void object2json(Object object,Writer writer) {
		    	object2json(object,writer,true) ;
			}
		  
		  /* (non-Javadoc)
		 * @see org.frameworkset.json.JacksonObjectMapper#object2json(java.lang.Object, java.io.Writer, boolean)
		 */
		@Override
		public   void object2json(Object object,Writer writer,boolean ALLOW_SINGLE_QUOTES) {
//		    	ObjectMapper mapper = new ObjectMapper();
//				mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
				try {
					mapper.writeValue(writer,object);
					
					
					
					
				} catch (Exception e) {
					throw new IllegalArgumentException("错误的json序列化操作",e);
				}
				
				
			
			}
		  
		  /* (non-Javadoc)
		 * @see org.frameworkset.json.JacksonObjectMapper#object2jsonAsbyte(java.lang.Object)
		 */
		@Override
		public   byte[] object2jsonAsbyte(Object object) {
		    	return object2jsonAsbyte(object,true) ;
			}
		  
		  
		  /* (non-Javadoc)
		 * @see org.frameworkset.json.JacksonObjectMapper#object2jsonAsbyte(java.lang.Object, boolean)
		 */
		@Override
		public   byte[] object2jsonAsbyte(Object object,boolean ALLOW_SINGLE_QUOTES) {
//		    	ObjectMapper mapper = new ObjectMapper();
//				mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
				try {
					return mapper.writeValueAsBytes(object);
					
					
					
					
				} catch (Exception e) {
					throw new IllegalArgumentException("错误的json序列化操作",e);
				}
				
				
			
			}

		

}
