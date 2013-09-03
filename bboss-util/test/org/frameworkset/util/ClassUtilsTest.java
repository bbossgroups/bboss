/**
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.  
 */
package org.frameworkset.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.frameworkset.util.ClassUtil.PropertieDescription;


/**
 * <p>ClassUtilsTest.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2011-5-26
 * @author biaoping.yin
 * @version 1.0
 */
public class ClassUtilsTest
{
	public static class Test extends ParentTest
	{
		private String field;
		private String parentfield;
		
		public String getField()
		{
		
			return field;
		}

		
		public void setField(String field)
		{
		
			this.field = field;
		}


//		public int getParentfield() {
//			return parentfield;
//		}


		public void setParentfield(String parentfield) {
			this.parentfield = parentfield;
		}
	}
	public void test(Map<String,Test> datas)
	{
		
	}
	
	public static class ParentTest
	{
		private int parentfield;
		private static String staticparentfield;
		private static final String fparentfield = "dd";
		/**
		 * don't named isbooleanfield.
		 */
		private boolean blooenfield = false;
		private boolean blooenfield2 = false;
		
		public String getNotExistField()
		{
			return "";
		}
		public void setNotExistField(String notExistField)
		{
//			return "";
		}
		
		public int getParentfield()
		{
		
			return parentfield;
		}

		
//		public void setParentfield(String parentfield)
//		{
//		
//			this.parentfield = parentfield;
//		}
		public boolean isBlooenfield() {
			return blooenfield;
		}
		public void setBlooenfield(boolean blooenfield) {
			this.blooenfield = blooenfield;
		}
		public boolean getBlooenfield2() {
			return blooenfield2;
		}
		public void setBlooenfield2(boolean blooenfield2) {
			this.blooenfield2 = blooenfield2;
		}
		
	}
	@org.junit.Test
	public void testFieldGet()
	{
		Field parentfield = ClassUtil.getDeclaredField(Test.class, "parentfield");
		Field fields[] = ClassUtil.getDeclaredFields(Test.class);
		
		System.out.println();
	}
	
	@org.junit.Test
	public void testFieldnameGen()
	{
		String parentfield = ClassUtil.genJavaName("aa_bb_cc");
		
		parentfield = ClassUtil.genJavaName("_bb_cc");
		parentfield = ClassUtil.genJavaName("_bb");
		parentfield = ClassUtil.genJavaName("BB");
		System.out.println();
	}
	
	@org.junit.Test
	public void testProGet()
	{
		PropertieDescription parentfield = ClassUtil.getPropertyDescriptor(Test.class, "parentfield");
		PropertieDescription blooenfield = ClassUtil.getPropertyDescriptor(Test.class, "blooenfield");
		PropertieDescription blooenfield2 = ClassUtil.getPropertyDescriptor(Test.class, "blooenfield2");
		Test t = new Test();
		t.setParentfield("aaa");
		
		
		System.out.println(t.getParentfield());
	}
	
	public static void main(String[] args)
	{
		try
		{
			Method me = ClassUtilsTest.class.getMethod("test", Map.class);
			
			Class c = new HashMap<String,Test>().getClass();
//			java.lang.reflect.
//			TypeVariable[] aa = c.getTypeParameters();
//			Class s = aa[0].getClass();
//			TypeVariable zzz = aa[0];
//			System.out.println(zzz.getName());
//			
//			System.out.println(aa[0].getGenericDeclaration());
//			Map tt = GenericTypeResolver.getRawType(genericType, typeVariableMap)etTypeVariableMap(c);
			System.out.println();
		}
		catch (SecurityException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (NoSuchMethodException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public  static class  Foo  { 
	     public   static   void  main(String[] args)  throws  Exception  { 
	        Method[] methods  =  Foo. class .getDeclaredMethods(); 
	         for  (Method method : methods)  { 
	            System.out.println( " returnType: " ); 
	            Type returnType  =  method.getGenericReturnType(); 
	             if  (returnType  instanceof  ParameterizedType) { 
	                Type[] types  =  ((ParameterizedType)returnType).getActualTypeArguments(); 
	                 for (Type type:types) { 
	                    System.out.println(type); 
	                } 
	            } 
	            System.out.println( " paramTypeType: " ); 
	            Type[] paramTypeList  =  method.getGenericParameterTypes(); 
	             for  (Type paramType : paramTypeList)  { 
	                 if  (paramType  instanceof  ParameterizedType) { 
	                    Type[] types  =  ((ParameterizedType)paramType).getActualTypeArguments(); 
	                     for (Type type:types) { 
	                        System.out.println(type); 
	                    } 
	                } 
	            } 
	        } 
	    } 

	     public     List <String>  test3(List<Integer>  list)  { 
	         return   null ; 
	    } 

	     private     Map < String, Double >  test4(Map < String, Test >  map)  { 
	         return   null ; 
	    } 

	} 


}
