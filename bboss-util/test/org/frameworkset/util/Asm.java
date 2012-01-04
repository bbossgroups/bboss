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
package org.frameworkset.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.frameworkset.util.ClassUtil.ClassInfo;
import org.frameworkset.util.ClassUtil.PropertieDescription;
import org.junit.Test;


/**
 * <p>
 * Title: Asm.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2008
 * </p>
 * 
 * @Date 2011-12-30
 * @author biaoping.yin
 * @version 1.0
 */
public class Asm 
{
	@Test
	public void testaddGETSETMethodForClass() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		Class clazz = Foo.class ;
		Field title = clazz.getDeclaredField("age");
		if(!title.isAccessible())
			title.setAccessible(true);
		
		Foo ins = new Foo();
		System.out.println(title.get(ins));
		ClassInfo classInfo = ClassUtil.getClassInfo(clazz);
		
	
		PropertieDescription p = classInfo.getPropertyDescriptor("age");
		Class clazz1 =  classInfo.getClazz();
		Method[] methods = classInfo.getDeclaredMethods();
		Object ins1 = null;
		try {
			ins1 = clazz1.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(clazz == clazz1);
		
	}
	public static class Foo
	{
		private final static String age = "duoduo";
		public String execute(String name,String country)
		{
			System.out.println("helloword:" + name);
			return "helloword:" + name;
		}
		public String getAge() {
			return age;
		}
	}
}
