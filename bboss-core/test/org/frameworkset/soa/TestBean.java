/*
 *  Copyright 2008-2010 biaoping.yin
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
package org.frameworkset.soa;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

import org.frameworkset.util.ClassUtil;
import org.frameworkset.util.ClassUtil.ClassInfo;

/**
 * <p>Title: TestBean.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2011-5-10 ÉÏÎç10:17:29
 * @author biaoping.yin
 * @version 1.0
 */
public class TestBean {
	private transient String field = "aaaa";
	
	public static void main(String[] args) throws IntrospectionException
	{
		ClassInfo beanInfo_ = ClassUtil.getClassInfo(TestBean.class);
		Field[] fields = beanInfo_.getDeclaredFields();
		for(int i = 0; i < fields.length; i ++)
		{
			Field f = fields[i];
			Class ptype = f.getType();
			String name = f.getName();
			try {
				Object value = f.get(new TestBean());
				System.out.println();
				
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	

}
