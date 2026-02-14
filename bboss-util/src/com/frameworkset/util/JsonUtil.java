package com.frameworkset.util;
/**
 * Copyright 2026 bboss
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import org.frameworkset.json.JacksonObjectMapperWrapper;
import org.frameworkset.json.JsonTypeReference;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * json序列化反序列化工具类
 * @author biaoping.yin
 * @Date 2026/2/13
 */
public class JsonUtil {

    public static JacksonObjectMapperWrapper getJacksonObjectMapper(){
        return SimpleStringUtil.getJacksonObjectMapper();
    }

    public static <T> T json2Object(String jsonString,Class<T> toclass,boolean ALLOW_SINGLE_QUOTES) {
        // TODO Auto-generated method stub

//		String jsonString = "[{'from_date':'2001-09-21','to_date':'2011-04-02','company':'人寿保险','department':'xxx','position':'主管' },{'from_date':'0002-12-01','to_date':'2011-04-02', 'company':'人寿保险','department':'xxx','position':'主管' }]";
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
//		try {
//			T value = mapper.readValue(jsonString, toclass);
//			return value;
//			
//			
//		} catch (Exception e) {
//			throw new IllegalArgumentException(jsonString,e);
//		}
        return SimpleStringUtil.json2Object(jsonString,toclass,ALLOW_SINGLE_QUOTES);



    }
    public static <T> T json2ObjectWithType(String jsonString, JsonTypeReference<T> ref) {
        return SimpleStringUtil.json2ObjectWithType(jsonString,ref);


    }
    public static <T> List<T> json2ListObject(String jsonString, final Class<T> beanType) {
         
        return SimpleStringUtil.json2ListObject(jsonString,beanType);


    }
    public static <T> Set<T> json2LSetObject(String jsonString, final Class<T> beanType) {
        return SimpleStringUtil.json2LSetObject(jsonString,beanType);


    }

    public static <T> T[] json2LArrayObject(String jsonString, final Class<T> beanType) {
        return SimpleStringUtil.json2LArrayObject(jsonString,beanType);


    }
    public static ObjectMapper getObjectMapper(){
        return SimpleStringUtil.getObjectMapper();
    }
    public static <K,T> Map<K,T> json2LHashObject(String jsonString, final Class<K> keyType, final Class<T> beanType) {
        return SimpleStringUtil.json2LHashObject(jsonString,keyType,beanType);


    }

    public static <T> List<T> json2ListObject(InputStream jsonString, final Class<T> beanType) {
        return SimpleStringUtil.json2ListObject(jsonString,beanType);


    }

    public static <D,T> D json2TypeObject(InputStream jsonString,final Class<D> containType,final Class<T> beanType) {
        return SimpleStringUtil.json2TypeObject(jsonString,containType,beanType);
    }

    public static <D,T> D json2TypeObject(String jsonString,final Class<D> containType,final Class<T> beanType) {
        return SimpleStringUtil.json2TypeObject(jsonString,containType,beanType);
    }
    public static <T> Set<T> json2LSetObject(InputStream jsonString,final Class<T> beanType) {
        return SimpleStringUtil.json2LSetObject(jsonString,beanType);


    }
    public static <K,T> Map<K,T> json2LHashObject(InputStream jsonString, final Class<K> keyType, final Class<T> beanType) {
        return SimpleStringUtil.json2LHashObject(jsonString,keyType,beanType);


    }
    public static <T> T[] json2LArrayObject(InputStream jsonString, final Class<T> beanType) {
        return SimpleStringUtil.json2LArrayObject(jsonString,beanType);


    }
    public static <T> T json2ObjectWithType(InputStream json,JsonTypeReference<T> ref) {
        return SimpleStringUtil.json2ObjectWithType(json,ref);


    }
    public static <T> T  json2ObjectWithType(String jsonString,JsonTypeReference<T> ref,boolean ALLOW_SINGLE_QUOTES) {
        // TODO Auto-generated method stub

//		String jsonString = "[{'from_date':'2001-09-21','to_date':'2011-04-02','company':'人寿保险','department':'xxx','position':'主管' },{'from_date':'0002-12-01','to_date':'2011-04-02', 'company':'人寿保险','department':'xxx','position':'主管' }]";
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
//		try {
//			T value = mapper.readValue(jsonString, ref);
//			return value;
//			
//			
//		} catch (Exception e) {
//			throw new IllegalArgumentException(jsonString,e);
//		}
        return SimpleStringUtil.json2ObjectWithType(jsonString, ref, ALLOW_SINGLE_QUOTES);


    }

    public static <T> T  json2ObjectWithType(InputStream json,JsonTypeReference<T> ref,boolean ALLOW_SINGLE_QUOTES) {
        // TODO Auto-generated method stub

//		String jsonString = "[{'from_date':'2001-09-21','to_date':'2011-04-02','company':'人寿保险','department':'xxx','position':'主管' },{'from_date':'0002-12-01','to_date':'2011-04-02', 'company':'人寿保险','department':'xxx','position':'主管' }]";
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
//		try {
//			T value = mapper.readValue(jsonString, ref);
//			return value;
//			
//			
//		} catch (Exception e) {
//			throw new IllegalArgumentException(jsonString,e);
//		}
        return SimpleStringUtil.json2ObjectWithType(json, ref, ALLOW_SINGLE_QUOTES);


    }

    public static <T> T json2Object(String jsonString,Class<T> toclass) {
        // TODO Auto-generated method stub
        return SimpleStringUtil.json2Object(jsonString,toclass);


    }
    public static <T> T json2Object(InputStream jsonString,Class<T> toclass) {
        // TODO Auto-generated method stub
        return SimpleStringUtil.json2Object(jsonString,toclass);


    }

    public static String object2json(Object object,boolean ALLOW_SINGLE_QUOTES) {

        return SimpleStringUtil.object2json(  object,  ALLOW_SINGLE_QUOTES);


    }

    public static String object2json(Object object) {
        return object2json(object,true) ;

    }

    public static String object2jsonPretty(Object object,boolean ALLOW_SINGLE_QUOTES) {

        return SimpleStringUtil.object2jsonPretty(  object,  ALLOW_SINGLE_QUOTES);


    }

    public static String object2jsonPretty(Object object) {
        return SimpleStringUtil.object2jsonPretty(object) ;

    }


    public static void object2json(Object object, Writer writer, boolean ALLOW_SINGLE_QUOTES) {
//    	ObjectMapper mapper = new ObjectMapper();
//		mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
//		try {
//			mapper.writeValue(writer,object);
//			
//			
//			
//			
//		} catch (Exception e) {
//			throw new IllegalArgumentException("错误的json序列化操作",e);
//		}

        SimpleStringUtil.object2json(object,writer,ALLOW_SINGLE_QUOTES);

    }

    public static void object2json(Object object,Writer writer) {
        SimpleStringUtil.object2json(object,writer) ;
    }
    public static void object2json(Object object,StringBuilder builder) {
        SimpleStringUtil.object2json(object,builder) ;
    }

    public static void object2json(Object object, OutputStream writer, boolean ALLOW_SINGLE_QUOTES) {
//    	ObjectMapper mapper = new ObjectMapper();
//		mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
//		try {
//			mapper.writeValue(writer,object);
//			
//			
//			
//			
//		} catch (Exception e) {
//			throw new IllegalArgumentException("错误的json序列化操作",e);
//		}
        SimpleStringUtil.object2json(object,writer,ALLOW_SINGLE_QUOTES);


    }

    public static void object2json(Object object,OutputStream writer) {
        SimpleStringUtil.object2json(object,writer) ;
    }


    public static void object2jsonDisableCloseAndFlush(Object object,OutputStream writer) {
        SimpleStringUtil.object2jsonDisableCloseAndFlush(object,writer) ;
    }

    public  static void object2jsonDisableCloseAndFlush(Object object,OutputStream writer,boolean ALLOW_SINGLE_QUOTES) {
        SimpleStringUtil.object2jsonDisableCloseAndFlush(  object,  writer,  ALLOW_SINGLE_QUOTES);
    }

    public static void object2json(Object object, File writer, boolean ALLOW_SINGLE_QUOTES) {
//    	ObjectMapper mapper = new ObjectMapper();
//		mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
//		try {
//			mapper.writeValue(writer,object);
//			
//			
//			
//			
//		} catch (Exception e) {
//			throw new IllegalArgumentException("错误的json序列化操作",e);
//		}
        SimpleStringUtil.object2json(object,writer,ALLOW_SINGLE_QUOTES);


    }

    public static void object2json(Object object,File writer) {
        SimpleStringUtil.object2json(object,writer) ;
    }

    public static byte[] object2jsonAsbyte(Object object,boolean ALLOW_SINGLE_QUOTES) {
//    	ObjectMapper mapper = new ObjectMapper();
//		mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
//		try {
//			return mapper.writeValueAsBytes(object);
//			
//			
//			
//			
//		} catch (Exception e) {
//			throw new IllegalArgumentException("错误的json序列化操作",e);
//		}

        return SimpleStringUtil.object2jsonAsbyte(  object,  ALLOW_SINGLE_QUOTES);

    }
    

    public static byte[] object2jsonAsbyte(Object object) {
        return SimpleStringUtil.object2jsonAsbyte(object) ;
    }

}
