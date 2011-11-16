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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>ClassUtil.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2011-9-6
 * @author biaoping.yin
 * @version 1.0
 */
public class ClassUtil
{
	public static class PropertieDescription
	{
		private Class propertyType;		
		private Method writeMethod;
		private Method readMethod;
		private String name;
		public PropertieDescription(Class propertyType, Method writeMethod,Method readMethod,
				String name)
		{
			super();
			this.propertyType = propertyType;
			this.writeMethod = writeMethod;
			this.name = name;
			this.readMethod = readMethod;
		}
		
		public Class getPropertyType(){
			return propertyType;
		}
		public Method getWriteMethod(){
			return this.writeMethod;
		}
		public String getName(){
			return this.name;
		}

		
		public Method getReadMethod()
		{
		
			return readMethod;
		}
	}
	public static class ClassInfo
	{
		
	    private volatile transient Field[] declaredFields;
	    
	    private volatile transient Map<String ,PropertieDescription> propertyDescriptors;
	    private volatile transient SoftReference publicFields;
	    private volatile transient Method[] declaredMethods;
	    private volatile transient SoftReference publicMethods;
	    private volatile transient SoftReference declaredConstructors;
	    private volatile transient SoftReference publicConstructors;
	    // Intermediate results for getFields and getMethods
	    private volatile transient SoftReference declaredPublicFields;
	    private volatile transient SoftReference declaredPublicMethods;
	    private Class clazz;
	    
	    private  ClassInfo(Class clazz){
	    	this.clazz = clazz;
	    }
	    
	    private static final Field[] NULL = new Field[0];
	    private static final Method[] NULL_M = new Method[0];

		private static final Map<String, PropertieDescription>	NULL_P	= new HashMap<String, PropertieDescription>(0);
	    private Object declaredFieldsLock = new Object();
	    private Object declaredMethodsLock = new Object();
	    private Object prodescLock = new Object();
	    public Method[] getDeclaredMethods()
		{
	    	if(declaredMethods != null)
    		{
    			if(declaredMethods == NULL_M)
    				return null;
    			return declaredMethods;
    		}
    		else
    		{
    			synchronized(declaredMethodsLock)
    			{
    				if(declaredFields != null)
    	    		{
    					if(declaredMethods == NULL_M)
    	    				return null;
    	    			return declaredMethods;
    	    		}
	    			try
	    			{		    				
	    				Method[] ret = getRecursiveDeclaredMehtods();
	    				if(ret == null)
	    					declaredMethods = NULL_M;
	    				else
	    					declaredMethods = ret;
	    			}
	    			catch(Exception e)
	    			{
	    				declaredMethods =NULL_M;
	    			}
    			}
    			
    		}
    		if(declaredMethods == NULL_M)
				return null;
			
    		return declaredMethods ;
		
		}
	    public Field[] getDeclaredFields()
	    {
	    	    
	    		if(declaredFields != null)
	    		{
	    			if(declaredFields == NULL)
	    				return null;
	    			return declaredFields;
	    		}
	    		else
	    		{
	    			synchronized(declaredFieldsLock)
	    			{
	    				if(declaredFields != null)
	    	    		{
	    					if(declaredFields == NULL)
	    	    				return null;
	    	    			return declaredFields;
	    	    		}
		    			try
		    			{		    				
		    				Field[] ret = getRecursiveDeclaredFileds();
		    				if(ret == null)
		    					declaredFields = NULL;
		    				else
		    					declaredFields = ret;
		    			}
		    			catch(Exception e)
		    			{
		    				declaredFields =NULL;
		    			}
	    			}
	    			
	    		}
	    		if(declaredFields == NULL)
    				return null;
    			
	    		return declaredFields ;
	    	
	    		
	    }
	    
	    private Method[] getRecursiveDeclaredMehtods()
	    {
	    	Method[] methods = null;
	    	List<Method> lfs = new ArrayList<Method>();
	    	Class clazz_super = clazz;
	    	do
	    	{
		    	try
		    	{
		    		methods = clazz_super.getDeclaredMethods();	
		    		if(methods != null && methods.length > 0)
		    		{
		    			for(Method f:methods)
		    			{
			    			lfs.add(f);
			    		}
		    		}
			    		
		    		clazz_super = clazz_super.getSuperclass();
		    		if(clazz_super == null)
		    		{
		    			break;
		    		}
		    		
		    	}
		    	catch(Exception e)
		    	{
		    		clazz_super = clazz_super.getSuperclass();
		    		if(clazz_super == null)
		    			break;
		    	}
	    	}
	    	while(true);
	    	if(lfs.size() > 0)
	    	{
	    		methods = new Method[lfs.size()];
		    	for(int i = 0; i < lfs.size(); i ++)
		    	{
		    		methods[i] = lfs.get(i);
		    	}
	    	}
	    	return methods;
	    	
	    }
	    
	    
	    private Field[] getRecursiveDeclaredFileds()
	    {
	    	Field[] fields = null;
	    	List<Field> lfs = new ArrayList<Field>();
	    	Class clazz_super = clazz;
	    	do
	    	{
		    	try
		    	{
		    		fields = clazz_super.getDeclaredFields();
		    		if(fields != null && fields.length > 0)
		    		{
		    			for(Field f:fields)
			    		{
			    			lfs.add(f);
			    		}
		    		}
		    		clazz_super = clazz_super.getSuperclass();
		    		if(clazz_super == null)
		    		{
		    			break;
		    		}
		    		
		    	}
		    	catch(Exception e)
		    	{
		    		clazz_super = clazz_super.getSuperclass();
		    		if(clazz_super == null)
		    			break;
		    	}
	    	}
	    	while(true);
	    	if(lfs.size() > 0)
	    	{
	    		fields = new Field[lfs.size()];
		    	for(int i = 0; i < lfs.size(); i ++)
		    	{
		    		fields[i] = lfs.get(i);
		    	}
	    	}
	    	return fields;
	    	
	    }
	    
	    public Method getDeclaredMethod(String name)
		{

	    	Method[] ret = getDeclaredMethods();
    	    if(ret == null)
    	    	return null;
    	    for(Method f:ret)
    	    {
    	    	if(f.getName().equals(name))
    	    		return f;
    	    }
    	    return null;
			
		}
	    public Field getDeclaredField(String name)
	    {
	    	    Field[] ret = this.getDeclaredFields();
	    	    if(ret == null)
	    	    	return null;
	    	    for(Field f:ret)
	    	    {
	    	    	if(f.getName().equals(name))
	    	    		return f;
	    	    }
	    	    return null;
	    	
	    		
	    }
	    private Map<String ,PropertieDescription> initBeaninfo()
	    {
	    	Map<String ,PropertieDescription> propertyDescriptors = null;
	    	BeanInfo beanInfo = null;
			try
			{
				beanInfo = Introspector.getBeanInfo(this.clazz);
				
				PropertyDescriptor[] attributes = beanInfo.getPropertyDescriptors();
				if(attributes == null || attributes.length == 0)
				{
					propertyDescriptors = NULL_P;
					return propertyDescriptors;
				}
				propertyDescriptors = new HashMap<String,PropertieDescription>(attributes.length-1);
				for(int i = 0;  i < attributes.length; i ++)
				{		
					PropertyDescriptor attr = attributes[i];
					if(attr.getName().equals("class"))
					{
						if(i == 0 && attributes.length == 1)
						{
							propertyDescriptors = NULL_P;
							break;
						}
						else
						{
							continue;
						}
					}
				
					propertyDescriptors.put(attr.getName(), new PropertieDescription(attr.getPropertyType(),attr.getWriteMethod(),attr.getReadMethod(),attr.getName()));
				}
				
			}
			catch (IntrospectionException e)
			{
				propertyDescriptors = NULL_P;
				e.printStackTrace();
			}
			return propertyDescriptors;
			
	    }
		public PropertieDescription getPropertyDescriptor(String name)
		{
			
			if(this.propertyDescriptors != null)
    		{
    			if(propertyDescriptors != NULL_P)
    				return propertyDescriptors.get(name);
    			else
    				return null;
    		}
    		else
    		{
    			synchronized(prodescLock)
    			{
    				if(propertyDescriptors == null)
    	    		{
    					try
    	    			{		    				
    	    				propertyDescriptors = initBeaninfo();	    				
    	    			}
    	    			catch(Exception e)
    	    			{
    	    				propertyDescriptors =NULL_P;
    	    			}
    	    			
    	    		}
	    			
    			}
    			
    		}
    		if(propertyDescriptors == NULL_P)
				return null;
			
    		return propertyDescriptors.get(name);
		}

		
		
	}
	
	private static  Map<Class,ClassInfo> classInfos = new HashMap<Class,ClassInfo>();
	private static Object lock = new Object();
	public static Field[] getDeclaredFields(Class clazz) throws SecurityException {
		ClassInfo classinfo = getClassInfo(clazz);
		return classinfo.getDeclaredFields();
       
    }
	
	
	public static Field getDeclaredField(Class clazz,String name) throws SecurityException {
		ClassInfo classinfo = getClassInfo(clazz);
		return classinfo.getDeclaredField(name);
       
    }
	
	public static PropertieDescription getPropertyDescriptor(Class clazz,String name)
	{
		ClassInfo classinfo = getClassInfo(clazz);
		return classinfo.getPropertyDescriptor(name);
	}
	
	public static ClassInfo  getClassInfo(Class clazz)
	{
		ClassInfo classinfo = classInfos.get(clazz);
		if(classinfo != null)
			return classinfo;
		synchronized(lock)
		{
			classinfo = classInfos.get(clazz);
			if(classinfo == null)
			{
				classinfo = new ClassInfo(clazz);
				classInfos.put(clazz, classinfo);
			}
		}
		return classinfo;
	}


	public static Method getDeclaredMethod(Class clazz,String name)
	{
		ClassInfo  csinfo = getClassInfo(clazz);
		if(csinfo == null)
			return null;
		// TODO Auto-generated method stub
		return csinfo.getDeclaredMethod(name);
	}

	
	
	
}
