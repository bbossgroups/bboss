/*
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
package org.frameworkset.spi.annotations;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.frameworkset.spi.mvc.ListBean;
import org.frameworkset.spi.mvc.UserBean;
import org.frameworkset.util.LocalVariableTableParameterNameDiscoverer;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.handler.HandlerUrlMappingRegisterTable;
import org.junit.Test;

/**
 * <p>Title: TestAnnotation.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-16
 * @author biaoping.yin
 * @version 1.0
 */
public class TestAnnotation {
//	public void test(String value,
//			@RequestParam(name="name"/**,editor=""*/)  String name,
//			@RequestParam(name="name"/**,editor=""*/)  String id)
//	{
//		System.out.println("name:"+name);
//		System.out.println("value:"+value);
//	}
	
	public static void main(String args[])
	{
//		try {
//			Annotation[][] ans = TestAnnotation.class.getMethod("test", String.class,String.class,String.class).getParameterAnnotations();
//			System.out.println(ans.length);
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NoSuchMethodException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		try {
//			BeanInfo beanInfo = Introspector.getBeanInfo(TestAnnotation.class);
//			PropertyDescriptor[] attributes = beanInfo.getPropertyDescriptors();
 			Method me = TestAnnotation.class.getMethod("get", null);
			ResponseBody by = me.getAnnotation(ResponseBody.class);
			Object[][] aaa = me.getParameterAnnotations();
			System.out.println(by + "" + aaa);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private HandlerUrlMappingRegisterTable handlerMap = new HandlerUrlMappingRegisterTable();
	public void setHandlerMap(HandlerUrlMappingRegisterTable handlerMap) {
		this.handlerMap = handlerMap;
	}
	
	public @ResponseBody String get()
	{
		return "";
	}
	public List<UserBean> list = new ArrayList<UserBean>();
	
	
	@Test
	public void testListType() throws SecurityException, NoSuchMethodException, NoSuchFieldException
	{
		Method m = Test1.class.getMethod("test", String.class,List.class);
		Type[] types = m.getGenericParameterTypes();
		
		Type zzz = ((ParameterizedType)types[1]).getActualTypeArguments()[0];
		String name = ((Class)zzz).getName();
		System.out.println(((ParameterizedType)types[1]).getActualTypeArguments()[0]);
		
//		System.out.println(name.substring("class ".length()));
		m = Test1.class.getMethod("test1", String.class,List.class);
		types = m.getGenericParameterTypes();
		if(types[1] instanceof ParameterizedType)
		{
			System.out.println((ParameterizedType)types[1]);
			name = ((ParameterizedType)types[1]).getActualTypeArguments()[0].toString();
	//		System.out.println(((ParameterizedType)types[1]).getActualTypeArguments()[0]);
			System.out.println(name.substring("class ".length()));
		}
		else
		{
			System.out.println(types[1]);
		}
		/**
		 * 
		 */
		
		LocalVariableTableParameterNameDiscoverer test = new LocalVariableTableParameterNameDiscoverer();
		String[] names = test.getParameterNames(m);
		System.out.println(names);
//		ArrayList<UserBean> list = new ArrayList<UserBean>();
////		
//		ParameterizedType pt = (ParameterizedType) list.getClass().getGenericSuperclass();  
//		System.out.println(pt.getActualTypeArguments().length);  
//	    System.out.println(pt.getActualTypeArguments()[0]);
//	    Field f = this.getClass().getDeclaredField("list");	
//	    System.out.println(f.getGenericType());
//	    System.out.println(list.getClass().getGenericSuperclass());
//	    System.out.println(list.getClass().getsGenericInterfaces()[0]);
	    
	    
		
	}
	@Test
	public void testp()
	{

		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(ListBean.class);
		} catch (Exception e) {
			
		}
		PropertyDescriptor[] attributes = beanInfo.getPropertyDescriptors();
		for(PropertyDescriptor property:attributes)
		{
			System.out.println(property.getReadMethod());
		}
	}
	
	public class Test1
	{
		public void test(String test,List<UserBean> list )
		{
			
		}
		
		public void test1(String test,List list )
		{
			
		}
	}


}
