package org.frameworkset.json;

import com.fasterxml.jackson.databind.JavaType;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

public class JacksonObjectMapperWrapper implements JacksonObjectMapper {
	private String dateFormat;

	private String locale;

	private String timeZone;
	private boolean disableTimestamp = false;
	boolean failedOnUnknownProperties = false;

	public boolean isFailedOnUnknownProperties() {
		return failedOnUnknownProperties;
	}

	@Override
	public void setFailedOnUnknownProperties(boolean failedOnUnknownProperties) {
		this.failedOnUnknownProperties = failedOnUnknownProperties;
		this.jacksonObjectMapper.setFailedOnUnknownProperties(failedOnUnknownProperties);
	}

	@Override
	public void init() {
		if(jacksonObjectMapper != null)
			this.jacksonObjectMapper.init();
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
		this.jacksonObjectMapper.setDateFormat(dateFormat);
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
		this.jacksonObjectMapper.setLocale(locale);
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
		this.jacksonObjectMapper.setTimeZone(timeZone);
	}

	public boolean isDisableTimestamp() {
		return disableTimestamp;
	}

	public void setDisableTimestamp(boolean disableTimestamp) {
		this.disableTimestamp = disableTimestamp;
		this.jacksonObjectMapper.setDisableTimestamp(disableTimestamp);
	}

	public JacksonObjectMapperWrapper()
	{
		 
		try {
			 
			jacksonObjectMapper = (JacksonObjectMapper) Class.forName("org.frameworkset.json.Jackson2ObjectMapper").newInstance();
		} catch (ClassNotFoundException e) {

		} catch (InstantiationException e) {

		} catch (IllegalAccessException e) {

		} catch (NoClassDefFoundError e) {

		}catch (Exception e) {

		}
		if(jacksonObjectMapper == null)
		{
			try {
				
				jacksonObjectMapper = (JacksonObjectMapper) Class.forName("org.frameworkset.json.Jackson1ObjectMapper").newInstance();
			} catch (ClassNotFoundException e) {

			} catch (InstantiationException e) {

			} catch (IllegalAccessException e) {

			} catch (NoClassDefFoundError e) {

			}catch (Exception e) {

			}
		}
	}

	private JacksonObjectMapper jacksonObjectMapper;
	 /* (non-Javadoc)
	 * @see org.frameworkset.json.JacksonObjectMapper#json2Object(java.lang.String, java.lang.Class)
	 */
	@Override
	public  <T> T json2Object(String jsonString,Class<T> toclass) {
			// TODO Auto-generated method stub
			return jacksonObjectMapper.json2Object(jsonString,toclass,true);
			
		
		}
	    
	 /* (non-Javadoc)
	 * @see org.frameworkset.json.JacksonObjectMapper#json2Object(java.lang.String, java.lang.Class, boolean)
	 */
	@Override
	public  <T> T json2Object(String jsonString,Class<T> toclass,boolean ALLOW_SINGLE_QUOTES) {
			return jacksonObjectMapper.json2Object(jsonString,toclass,ALLOW_SINGLE_QUOTES);
			
			
		
		}
	public JavaType getJavaType(Class containerType, Class ... beanClass){
		return jacksonObjectMapper.getJavaType(containerType, beanClass);
	}
	public JavaType getJavaMapType(Class containerType, Class keyClass,Class valueClass){
		JavaType javaType = jacksonObjectMapper.getJavaType(containerType, keyClass,valueClass);
		return javaType;
	}
	public com.fasterxml.jackson.databind.ObjectMapper getObjectMapper(){
		return jacksonObjectMapper.getObjectMapper();
	}
	/* (non-Javadoc)
	 * @see org.frameworkset.json.JacksonObjectMapper#json2Object(java.lang.String, java.lang.Class, boolean)
	 */
	@Override
	public  <T> T json2Object(InputStream jsonString,Class<T> toclass,boolean ALLOW_SINGLE_QUOTES) {
			return jacksonObjectMapper.json2Object(jsonString,toclass,ALLOW_SINGLE_QUOTES);
			
			
		
		}
	  public    <T> T json2ObjectWithType(String jsonString,final JsonTypeReference<T> ref) {
			return json2ObjectWithType(jsonString,ref,true);
			
		
		}
	  
	  public   <T> T json2ObjectWithType(String jsonString,final JsonTypeReference<T> ref,boolean ALLOW_SINGLE_QUOTES) {
			// TODO Auto-generated method stub

//			String jsonString = "[{'from_date':'2001-09-21','to_date':'2011-04-02','company':'人寿保险','department':'xxx','position':'主管' },{'from_date':'0002-12-01','to_date':'2011-04-02', 'company':'人寿保险','department':'xxx','position':'主管' }]";
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
		  return jacksonObjectMapper.json2ObjectWithType( jsonString,ref,ALLOW_SINGLE_QUOTES);
			
		
		}
	  
	  
	  public    <T> T json2ObjectWithType(InputStream jsonString,final JsonTypeReference<T> ref) {
			return json2ObjectWithType(jsonString,ref,true);
			
		
		}
	  
	  public   <T> T json2ObjectWithType(InputStream jsonString,final JsonTypeReference<T> ref,boolean ALLOW_SINGLE_QUOTES) {
			// TODO Auto-generated method stub

//			String jsonString = "[{'from_date':'2001-09-21','to_date':'2011-04-02','company':'人寿保险','department':'xxx','position':'主管' },{'from_date':'0002-12-01','to_date':'2011-04-02', 'company':'人寿保险','department':'xxx','position':'主管' }]";
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
		  return jacksonObjectMapper.json2ObjectWithType( jsonString,ref,ALLOW_SINGLE_QUOTES);
			
		
		}
	  
	  /* (non-Javadoc)
	 * @see org.frameworkset.json.JacksonObjectMapper#object2json(java.lang.Object)
	 */
	@Override
	public   String object2json(Object object) {
	    	return jacksonObjectMapper.object2json(object,true) ;
			
			
		
		}

    /**
     * 转换为格式化json串
     * @see org.frameworkset.json.JacksonObjectMapper#object2json(java.lang.Object, boolean)
     */
    @Override
    public   String object2jsonPretty(Object object) {
        return object2jsonPretty(object,false);
    }
    /**
     * 转换为格式化json串
     * @see org.frameworkset.json.JacksonObjectMapper#object2json(java.lang.Object, boolean)
     */
    @Override
    public   String object2jsonPretty(Object object,boolean ALLOW_SINGLE_QUOTES) {
        return jacksonObjectMapper.object2jsonPretty(  object,  ALLOW_SINGLE_QUOTES);

    }
	  
	  /* (non-Javadoc)
	 * @see org.frameworkset.json.JacksonObjectMapper#object2json(java.lang.Object, boolean)
	 */
	@Override
	public   String object2json(Object object,boolean ALLOW_SINGLE_QUOTES) {
		return jacksonObjectMapper.object2json(  object,  ALLOW_SINGLE_QUOTES);
			
			
		
		}
	  
	  /* (non-Javadoc)
	 * @see org.frameworkset.json.JacksonObjectMapper#object2json(java.lang.Object, java.io.File)
	 */
	@Override
	public   void object2json(Object object,File writer) {
		jacksonObjectMapper.object2json(object,writer,true) ;
		}
	  
	  /* (non-Javadoc)
	 * @see org.frameworkset.json.JacksonObjectMapper#object2json(java.lang.Object, java.io.File, boolean)
	 */
	@Override
	public   void object2json(Object object,File writer,boolean ALLOW_SINGLE_QUOTES) {
		jacksonObjectMapper.object2json(  object,  writer,  ALLOW_SINGLE_QUOTES); 
	}
	  
	  /* (non-Javadoc)
	 * @see org.frameworkset.json.JacksonObjectMapper#object2json(java.lang.Object, java.io.OutputStream)
	 */
	@Override
	public  void object2json(Object object,OutputStream writer) {
		jacksonObjectMapper.object2json(object,writer,true) ;
	}
    @Override
    public  void object2jsonDisableCloseAndFlush(Object object,OutputStream writer) {
        jacksonObjectMapper.object2jsonDisableCloseAndFlush(object,writer) ;
    }
    @Override
    public   void object2jsonDisableCloseAndFlush(Object object,OutputStream writer,boolean ALLOW_SINGLE_QUOTES) {
        jacksonObjectMapper.object2jsonDisableCloseAndFlush(  object,  writer,  ALLOW_SINGLE_QUOTES);
    }  
	  /* (non-Javadoc)
	 * @see org.frameworkset.json.JacksonObjectMapper#object2json(java.lang.Object, java.io.OutputStream, boolean)
	 */
	@Override
	public   void object2json(Object object,OutputStream writer,boolean ALLOW_SINGLE_QUOTES) {
		jacksonObjectMapper.object2json(  object,  writer,  ALLOW_SINGLE_QUOTES);
			
			
		
		}
	  
	  /* (non-Javadoc)
	 * @see org.frameworkset.json.JacksonObjectMapper#object2json(java.lang.Object, java.io.Writer)
	 */
	@Override
	public   void object2json(Object object,Writer writer) {
		jacksonObjectMapper.object2json(object,writer,true) ;
		}
	  
	  /* (non-Javadoc)
	 * @see org.frameworkset.json.JacksonObjectMapper#object2json(java.lang.Object, java.io.Writer, boolean)
	 */
	@Override
	public   void object2json(Object object,Writer writer,boolean ALLOW_SINGLE_QUOTES) {
		jacksonObjectMapper.object2json(  object,  writer,  ALLOW_SINGLE_QUOTES) ;
			
			
		
		}
	  
	  /* (non-Javadoc)
	 * @see org.frameworkset.json.JacksonObjectMapper#object2jsonAsbyte(java.lang.Object)
	 */
	@Override
	public   byte[] object2jsonAsbyte(Object object) {
	    	return jacksonObjectMapper.object2jsonAsbyte(object,true) ;
		}
	  
	  
	  /* (non-Javadoc)
	 * @see org.frameworkset.json.JacksonObjectMapper#object2jsonAsbyte(java.lang.Object, boolean)
	 */
	@Override
	public   byte[] object2jsonAsbyte(Object object,boolean ALLOW_SINGLE_QUOTES) {
		return jacksonObjectMapper.object2jsonAsbyte(  object,  ALLOW_SINGLE_QUOTES);
	}

}
