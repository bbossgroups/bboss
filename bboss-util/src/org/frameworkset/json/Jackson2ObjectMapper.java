package org.frameworkset.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.frameworkset.util.BeanUtils;
import org.frameworkset.util.ClassUtils;
import org.frameworkset.util.annotations.DateFormateMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES;

public class Jackson2ObjectMapper implements JacksonObjectMapper {
    private static final Logger logger = LoggerFactory.getLogger(Jackson2ObjectMapper.class);

    static ClassLoader moduleClassLoader = Jackson2ObjectMapper.class.getClassLoader();
	protected ObjectMapper mapper = null;
	protected ObjectMapper ALLOW_SINGLE_QUOTES_mapper = null;
    protected ObjectMapper ALLOW_SINGLE_QUOTES_disableCloseAndFlushMapper = null;
	protected ObjectMapper NOT_ALLOW_SINGLE_QUOTES_mapper = null;
    protected ObjectMapper NOT_ALLOW_SINGLE_QUOTES_disableCloseAndFlushMapper = null;

	private String dateFormat;

	private String locale;

	private String timeZone;
	private boolean disableTimestamp = false;
	boolean failedOnUnknownProperties = false;
	@Override
	public String getDateFormat() {
		return dateFormat;
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

	public Jackson2ObjectMapper(){
		mapper = new ObjectMapper();
		//反序列化时，属性不存在时忽略属性
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        registerWellKnownModulesIfAvailable(mapper);
		ALLOW_SINGLE_QUOTES_mapper = new ObjectMapper();
		//反序列化时，属性不存在时忽略属性
		ALLOW_SINGLE_QUOTES_mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
		ALLOW_SINGLE_QUOTES_mapper.configure(ALLOW_SINGLE_QUOTES, true);
        registerWellKnownModulesIfAvailable(ALLOW_SINGLE_QUOTES_mapper);
		NOT_ALLOW_SINGLE_QUOTES_mapper = new ObjectMapper();
		//反序列化时，属性不存在时忽略属性
		NOT_ALLOW_SINGLE_QUOTES_mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        NOT_ALLOW_SINGLE_QUOTES_mapper.configure(ALLOW_SINGLE_QUOTES, false);
        registerWellKnownModulesIfAvailable(NOT_ALLOW_SINGLE_QUOTES_mapper);

        this.NOT_ALLOW_SINGLE_QUOTES_disableCloseAndFlushMapper = new ObjectMapper();
        //反序列化时，属性不存在时忽略属性
        NOT_ALLOW_SINGLE_QUOTES_disableCloseAndFlushMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        NOT_ALLOW_SINGLE_QUOTES_disableCloseAndFlushMapper.configure(ALLOW_SINGLE_QUOTES, false);
        NOT_ALLOW_SINGLE_QUOTES_disableCloseAndFlushMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        NOT_ALLOW_SINGLE_QUOTES_disableCloseAndFlushMapper.configure(JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM, false);
        registerWellKnownModulesIfAvailable(NOT_ALLOW_SINGLE_QUOTES_disableCloseAndFlushMapper);

        this.ALLOW_SINGLE_QUOTES_disableCloseAndFlushMapper = new ObjectMapper();
        //反序列化时，属性不存在时忽略属性
        ALLOW_SINGLE_QUOTES_disableCloseAndFlushMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        ALLOW_SINGLE_QUOTES_disableCloseAndFlushMapper.configure(ALLOW_SINGLE_QUOTES, false);
        ALLOW_SINGLE_QUOTES_disableCloseAndFlushMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        ALLOW_SINGLE_QUOTES_disableCloseAndFlushMapper.configure(JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM, false);
        registerWellKnownModulesIfAvailable(ALLOW_SINGLE_QUOTES_disableCloseAndFlushMapper);
	}


    public static void registerWellKnownModulesIfAvailable(ObjectMapper objectMapper) {
        // Java 7 java.nio.file.Path class present?
        if (ClassUtils.isPresent("java.nio.file.Path", moduleClassLoader)) {
            try {
                Class<? extends com.fasterxml.jackson.databind.Module> jdk7Module = (Class<? extends com.fasterxml.jackson.databind.Module>)
                        ClassUtils.forName("com.fasterxml.jackson.datatype.jdk7.Jdk7Module", moduleClassLoader);
                objectMapper.registerModule(BeanUtils.instantiate(jdk7Module));
            }
            catch (ClassNotFoundException ex) {
                // jackson-datatype-jdk7 not available
            }
        }

        // Java 8 java.util.Optional class present?
        if (ClassUtils.isPresent("java.util.Optional", moduleClassLoader)) {
            try {
                Class<? extends com.fasterxml.jackson.databind.Module> jdk8Module = (Class<? extends com.fasterxml.jackson.databind.Module>)
                        ClassUtils.forName("com.fasterxml.jackson.datatype.jdk8.Jdk8Module", moduleClassLoader);
                objectMapper.registerModule(BeanUtils.instantiate(jdk8Module));
            }
            catch (ClassNotFoundException ex) {
                // jackson-datatype-jdk8 not available
            }
        }

        try {
            Class<?> javaTimeModuleRegistClass = (Class<?>)
                    ClassUtils.forName("org.frameworkset.json.JavaTimeModuleRegist");
            Method javaTimeModuleRegist = javaTimeModuleRegistClass.getMethod("javaTimeModuleRegist", ObjectMapper.class);
            if (javaTimeModuleRegist != null) {
                javaTimeModuleRegist.invoke(null, objectMapper);
            }
        }
        catch (Exception e){
        }

        catch (Throwable e){
        }


        // Joda-Time present?
        if (ClassUtils.isPresent("org.joda.time.LocalDate", moduleClassLoader)) {
            try {
                Class<? extends com.fasterxml.jackson.databind.Module> jodaModule = (Class<? extends com.fasterxml.jackson.databind.Module>)
                        ClassUtils.forName("com.fasterxml.jackson.datatype.joda.JodaModule", moduleClassLoader);
                objectMapper.registerModule(BeanUtils.instantiate(jodaModule));
            }
            catch (ClassNotFoundException ex) {
                // jackson-datatype-joda not available
            }
        }

    }

	 /* (non-Javadoc)
	 * @see org.frameworkset.json.JacksonObjectMapper#json2Object(java.lang.String, java.lang.Class)
	 */
	@Override
	public  <T> T json2Object(String jsonString,Class<T> toclass) {
			// TODO Auto-generated method stub
			return json2Object(jsonString,toclass,false);
			
		
		}


	public JavaType getJavaType(Class containerType,Class ... beanClass){
		JavaType javaType = mapper.getTypeFactory().constructParametricType(containerType, beanClass);
		return javaType;
	}

	public JavaType getJavaMapType(Class containerType, Class keyClass,Class valueClass){
		JavaType javaType = mapper.getTypeFactory().constructMapType(containerType, keyClass,valueClass);
		return javaType;
	}

	public ObjectMapper getObjectMapper(){

		return mapper;
	}
	
 
	    
	 /* (non-Javadoc)
	 * @see org.frameworkset.json.JacksonObjectMapper#json2Object(java.lang.String, java.lang.Class, boolean)
	 */
	@Override
	public  <T> T json2Object(String jsonString,Class<T> toclass,boolean ALLOW_SINGLE_QUOTES) {
			// TODO Auto-generated method stub

//			String jsonString = "[{'from_date':'2001-09-21','to_date':'2011-04-02','company':'人寿保险','department':'xxx','position':'主管' },{'from_date':'0002-12-01','to_date':'2011-04-02', 'company':'人寿保险','department':'xxx','position':'主管' }]";
			
//			mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
			try {
				if(ALLOW_SINGLE_QUOTES) {
					T value = ALLOW_SINGLE_QUOTES_mapper.readValue(jsonString, toclass);
					return value;
				}
				else{
					T value = NOT_ALLOW_SINGLE_QUOTES_mapper.readValue(jsonString, toclass);
					return value;
				}
				
				
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

//			String jsonString = "[{'from_date':'2001-09-21','to_date':'2011-04-02','company':'人寿保险','department':'xxx','position':'主管' },{'from_date':'0002-12-01','to_date':'2011-04-02', 'company':'人寿保险','department':'xxx','position':'主管' }]";
			
//			mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
			try {

				if(ALLOW_SINGLE_QUOTES) {
					T value = ALLOW_SINGLE_QUOTES_mapper.readValue(jsonString, toclass);
					return value;
				}
				else{
					T value = NOT_ALLOW_SINGLE_QUOTES_mapper.readValue(jsonString, toclass);
					return value;
				}
				
			} catch (Exception e) {
				throw new IllegalArgumentException("",e);
			}
			
			
		
		}
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
	@Override
	public <T> T json2ObjectWithType(InputStream jsonString, final JsonTypeReference<T> ref, boolean ALLOW_SINGLE_QUOTES) {
		// TODO Auto-generated method stub
		TypeReference<T> ref_ = new TypeReference<T>(){
			@Override
			public Type getType() {
				// TODO Auto-generated method stub
				return ref.getType();
			}

			 
		};  
		try {


			if(ALLOW_SINGLE_QUOTES) {
				T value = ALLOW_SINGLE_QUOTES_mapper.readValue(jsonString, ref_);
				return value;
			}
			else{
				T value = NOT_ALLOW_SINGLE_QUOTES_mapper.readValue(jsonString, ref_);
				return value;
			}
			
			
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
	public   <T> T json2ObjectWithType(String jsonString,final JsonTypeReference<T> ref,boolean ALLOW_SINGLE_QUOTES) {
		// TODO Auto-generated method stub

//		String jsonString = "[{'from_date':'2001-09-21','to_date':'2011-04-02','company':'人寿保险','department':'xxx','position':'主管' },{'from_date':'0002-12-01','to_date':'2011-04-02', 'company':'人寿保险','department':'xxx','position':'主管' }]";
		TypeReference<T> ref_ = new TypeReference<T>(){
			@Override
			public Type getType() {
				// TODO Auto-generated method stub
				return ref.getType();
			}

			 
		};  
		try {

			if(ALLOW_SINGLE_QUOTES) {
				T value = ALLOW_SINGLE_QUOTES_mapper.readValue(jsonString, ref_);
				return value;
			}
			else{
				T value = NOT_ALLOW_SINGLE_QUOTES_mapper.readValue(jsonString, ref_);
				return value;
			}
			
			
		} catch (Exception e) {
			throw new IllegalArgumentException(jsonString,e);
		}
		
		
	
	}
	  
	  /* (non-Javadoc)
	 * @see org.frameworkset.json.JacksonObjectMapper#object2json(java.lang.Object)
	 */
	@Override
	public   String object2json(Object object) {
	    	return object2json(object,false) ;
			
			
		
		}
	  
	  /* (non-Javadoc)
	 * @see org.frameworkset.json.JacksonObjectMapper#object2json(java.lang.Object, boolean)
	 */
	@Override
	public   String object2json(Object object,boolean ALLOW_SINGLE_QUOTES) {
//	    	ObjectMapper mapper = new ObjectMapper();
//			mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
			
			try {

				if(ALLOW_SINGLE_QUOTES) {
					String value = ALLOW_SINGLE_QUOTES_mapper.writeValueAsString(object);

					return value;
				}
				else{
					String value = NOT_ALLOW_SINGLE_QUOTES_mapper.writeValueAsString(object);

					return value;
				}
				
			} catch (Exception e) {
				throw new IllegalArgumentException("错误的json序列化操作",e);
			}
			
			
		
		}
	  
	  /* (non-Javadoc)
	 * @see org.frameworkset.json.JacksonObjectMapper#object2json(java.lang.Object, java.io.File)
	 */
	@Override
	public   void object2json(Object object,File writer) {
	    	object2json(object,writer,false) ;
		}
	  
	  /* (non-Javadoc)
	 * @see org.frameworkset.json.JacksonObjectMapper#object2json(java.lang.Object, java.io.File, boolean)
	 */
	@Override
	public   void object2json(Object object,File writer,boolean ALLOW_SINGLE_QUOTES) {
//	    	ObjectMapper mapper = new ObjectMapper();
//			mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
			try {


				if(ALLOW_SINGLE_QUOTES) {
					ALLOW_SINGLE_QUOTES_mapper.writeValue(writer,object);
				}
				else{
					NOT_ALLOW_SINGLE_QUOTES_mapper.writeValue(writer,object);
				}
				
				
			} catch (Exception e) {
				throw new IllegalArgumentException("错误的json序列化操作",e);
			}
			
			
		
		}
	  
	  /* (non-Javadoc)
	 * @see org.frameworkset.json.JacksonObjectMapper#object2json(java.lang.Object, java.io.OutputStream)
	 */
	@Override
	public  void object2json(Object object,OutputStream writer) {
	    	object2json(object,writer,false) ;
		}
	  
	  /* (non-Javadoc)
	 * @see org.frameworkset.json.JacksonObjectMapper#object2json(java.lang.Object, java.io.OutputStream, boolean)
	 */
	@Override
	public   void object2json(Object object,OutputStream writer,boolean ALLOW_SINGLE_QUOTES) {
//	    	ObjectMapper mapper = new ObjectMapper();
//			mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
			try {
				if(ALLOW_SINGLE_QUOTES) {
					ALLOW_SINGLE_QUOTES_mapper.writeValue(writer,object);
				}
				else{
					NOT_ALLOW_SINGLE_QUOTES_mapper.writeValue(writer,object);
				}
				
				
				
			} catch (Exception e) {
				throw new IllegalArgumentException("错误的json序列化操作",e);
			}
			
			
		
		}
    /* (non-Javadoc)
     * @see org.frameworkset.json.JacksonObjectMapper#object2json(java.lang.Object, java.io.OutputStream)
     */
    @Override
    public  void object2jsonDisableCloseAndFlush(Object object,OutputStream writer) {
        object2jsonDisableCloseAndFlush(object,writer,false) ;
    }
    @Override
    public   void object2jsonDisableCloseAndFlush(Object object,OutputStream writer,boolean ALLOW_SINGLE_QUOTES) {
//	    	ObjectMapper mapper = new ObjectMapper();
//			mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
        try {
            if(ALLOW_SINGLE_QUOTES) {
                ALLOW_SINGLE_QUOTES_disableCloseAndFlushMapper.writeValue(writer,object);
            }
            else{
                NOT_ALLOW_SINGLE_QUOTES_disableCloseAndFlushMapper.writeValue(writer,object);
            }



        } catch (Exception e) {
            throw new IllegalArgumentException("错误的json序列化操作",e);
        }



    }
	  
	  /* (non-Javadoc)
	 * @see org.frameworkset.json.JacksonObjectMapper#object2json(java.lang.Object, java.io.Writer)
	 */
	@Override
	public   void object2json(Object object,Writer writer) {
	    	object2json(object,writer,false) ;
		}
	  
	  /* (non-Javadoc)
	 * @see org.frameworkset.json.JacksonObjectMapper#object2json(java.lang.Object, java.io.Writer, boolean)
	 */
	@Override
	public   void object2json(Object object,Writer writer,boolean ALLOW_SINGLE_QUOTES) {
//	    	ObjectMapper mapper = new ObjectMapper();
//			mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
			try {

				if(ALLOW_SINGLE_QUOTES) {
					ALLOW_SINGLE_QUOTES_mapper.writeValue(writer,object);
				}
				else{
					NOT_ALLOW_SINGLE_QUOTES_mapper.writeValue(writer,object);
				}
				
				
			} catch (Exception e) {
				throw new IllegalArgumentException("错误的json序列化操作",e);
			}
			
			
		
		}
	  
	  /* (non-Javadoc)
	 * @see org.frameworkset.json.JacksonObjectMapper#object2jsonAsbyte(java.lang.Object)
	 */
	@Override
	public   byte[] object2jsonAsbyte(Object object) {
	    	return object2jsonAsbyte(object,false) ;
		}
	  
	  
	  /* (non-Javadoc)
	 * @see org.frameworkset.json.JacksonObjectMapper#object2jsonAsbyte(java.lang.Object, boolean)
	 */
	@Override
	public   byte[] object2jsonAsbyte(Object object,boolean ALLOW_SINGLE_QUOTES) {
//	    	ObjectMapper mapper = new ObjectMapper();
//			mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
			try {
				if(ALLOW_SINGLE_QUOTES)
					return ALLOW_SINGLE_QUOTES_mapper.writeValueAsBytes(object);
				else
					return NOT_ALLOW_SINGLE_QUOTES_mapper.writeValueAsBytes(object);
				
				
				
				
			} catch (Exception e) {
				throw new IllegalArgumentException("错误的json序列化操作",e);
			}
			
			
		
		}
	@Override
	public void init() {
		if(dateFormat != null && !dateFormat.equals("")) {
			DateFormateMeta dateFormateMeta = DateFormateMeta.buildDateFormateMeta(this.dateFormat, this.locale, this.timeZone);
			this.mapper.setDateFormat(dateFormateMeta.toDateFormat());
			this.ALLOW_SINGLE_QUOTES_mapper.setDateFormat(dateFormateMeta.toDateFormat());
			this.NOT_ALLOW_SINGLE_QUOTES_mapper.setDateFormat(dateFormateMeta.toDateFormat());
		}
		if(this.disableTimestamp){
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			ALLOW_SINGLE_QUOTES_mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			NOT_ALLOW_SINGLE_QUOTES_mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		}
		this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,failedOnUnknownProperties);
		ALLOW_SINGLE_QUOTES_mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,failedOnUnknownProperties);
		NOT_ALLOW_SINGLE_QUOTES_mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,failedOnUnknownProperties);
	}


}
