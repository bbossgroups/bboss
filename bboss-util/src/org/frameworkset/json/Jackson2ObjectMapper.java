package org.frameworkset.json;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
public class Jackson2ObjectMapper implements JacksonObjectMapper {
	ObjectMapper mapper = new ObjectMapper();
 

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

//			String jsonString = "[{'from_date':'2001-09-21','to_date':'2011-04-02','company':'人寿保险','department':'xxx','position':'主管' },{'from_date':'0002-12-01','to_date':'2011-04-02', 'company':'人寿保险','department':'xxx','position':'主管' }]";
			
//			mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
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

//			String jsonString = "[{'from_date':'2001-09-21','to_date':'2011-04-02','company':'人寿保险','department':'xxx','position':'主管' },{'from_date':'0002-12-01','to_date':'2011-04-02', 'company':'人寿保险','department':'xxx','position':'主管' }]";
			
//			mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
			try {
				T value = mapper.readValue(jsonString, toclass);
				return value;
				
				
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
		ObjectMapper mapper = new ObjectMapper();
		TypeReference<?> ref_ = new TypeReference<Object>(){
			@Override
			public Type getType() {
				// TODO Auto-generated method stub
				return ref.getType();
			}

			 
		};  
		try {
			T value = mapper.readValue(jsonString, ref_);
			return value;
			
			
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
	public   <T> T json2ObjectWithType(String jsonString,final JsonTypeReference<T> ref,boolean ALLOW_SINGLE_QUOTES) {
		// TODO Auto-generated method stub

//		String jsonString = "[{'from_date':'2001-09-21','to_date':'2011-04-02','company':'人寿保险','department':'xxx','position':'主管' },{'from_date':'0002-12-01','to_date':'2011-04-02', 'company':'人寿保险','department':'xxx','position':'主管' }]";
		ObjectMapper mapper = new ObjectMapper();
		TypeReference<?> ref_ = new TypeReference<Object>(){
			@Override
			public Type getType() {
				// TODO Auto-generated method stub
				return ref.getType();
			}

			 
		};  
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
//	    	ObjectMapper mapper = new ObjectMapper();
//			mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
			
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
//	    	ObjectMapper mapper = new ObjectMapper();
//			mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
			try {
				mapper.writeValue(writer,object);
				
				
				
				
			} catch (Exception e) {
				throw new IllegalArgumentException("错误的json序列化操作",e);
			}
			
			
		
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
//	    	ObjectMapper mapper = new ObjectMapper();
//			mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
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
//	    	ObjectMapper mapper = new ObjectMapper();
//			mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
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
//	    	ObjectMapper mapper = new ObjectMapper();
//			mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
			try {
				return mapper.writeValueAsBytes(object);
				
				
				
				
			} catch (Exception e) {
				throw new IllegalArgumentException("错误的json序列化操作",e);
			}
			
			
		
		}



}
