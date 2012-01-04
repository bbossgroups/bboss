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
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.frameworkset.soa.annotation.ExcludeField;
import org.frameworkset.util.asm.AsmUtil;


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
	private static final Logger log = Logger.getLogger(AsmUtil.class);
	public static class PropertieDescription
	{
		private Class propertyType;		
		private Method writeMethod;
		private Method readMethod;
		private Field field;
		private String name;
		
		private boolean canwrite = false;
		private boolean canread = false;
		private boolean canseriable = true;
		
		private boolean oldAccessible = false;
		public PropertieDescription(Class propertyType,Field field, Method writeMethod,Method readMethod,
				String name)
		{
			super();
			this.propertyType = propertyType;
			this.writeMethod = writeMethod;
			this.name = name;
			this.field = field;
			
			this.readMethod = readMethod;
			if(this.field != null)
				oldAccessible = this.field.isAccessible();
			if((writeMethod == null || this.readMethod == null)
					&& this.field != null && !this.field.isAccessible())
			{
				 int mode = this.field.getModifiers();
				 if( !Modifier.isFinal(mode) 
							&& !Modifier.isStatic(mode)
							)
				 {
					this.field.setAccessible(true);
					canwrite = true;
					canread = true;
				 }
			}
			
			if(!canread && readMethod != null)
				this.canread = true;
			
			if(!canwrite && writeMethod != null)
				this.canwrite = true;
			if(this.field != null )
			{
				 int mode = this.field.getModifiers();
				 if( Modifier.isFinal(mode) 
							|| Modifier.isStatic(mode) 
							|| Modifier.isTransient(mode) 
							|| findAnnotation(ExcludeField.class) != null)
				 {
					 canseriable = false;
				 }
			}
			
			if(canseriable && (readMethod == null || writeMethod == null) && this.field == null)
			{
				canseriable = false;
			}
			
				
		}
		
		public <T extends Annotation> T findAnnotation(Class<T> type)
		{
			if(this.field != null)
				return (T)this.field.getAnnotation(type);
			return null;
		}
		
		public Annotation[] findAnnotations()
		{
			if(this.field != null)
				return this.field.getAnnotations();
			return null;
		}
		
		public boolean canread()
		{
			return canread;
		}
		
		public boolean canwrite()
		{
			return canwrite;
		}
		
		public boolean canseriable()
		{
			return canseriable;
		}
		
		
		
		
		public Object getValue(Object po) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
		{
			if(po == null)
				return null;
			if(this.readMethod != null)
				return this.readMethod.invoke(po);
			else if(this.field != null)
				return this.field.get(po);
			throw new IllegalAccessException("Get value for property["+this.name+"] failed:get Method or field not exist");
			
		}
		public void setValue(Object po,Object value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
		{
			if(po == null)
				return ;
			if(this.writeMethod != null)
			{
				this.writeMethod.invoke(po,value);
				
			}
			else if(this.field != null)
			{
				this.field.set(po,value);
			}
			else
			{
				throw new IllegalAccessException("Set value for property["+this.name+"] failed: set Method or field not exist.");
			}
			
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

		public Field getField() {
			return field;
		}

		public void setWriteMethod(Method writeMethod) {
			this.writeMethod = writeMethod;
		}

		public void setReadMethod(Method readMethod) {
			this.readMethod = readMethod;
		}
	}
	public static class ClassInfo
	{
		
	    private volatile transient Field[] declaredFields;
	    
//	    private volatile transient Map<String ,PropertieDescription> propertyDescriptors;
	    private volatile transient List<PropertieDescription> propertyDescriptors;

	    private volatile transient Method[] declaredMethods;

	    private Class clazz;
	    
	    private  ClassInfo(Class clazz){
	    	this.clazz = clazz;
	    	this.init();
	    }
	    
	    private static final Field[] NULL = new Field[0];
	    private static final Method[] NULL_M = new Method[0];

		private static final List<PropertieDescription>	NULL_P	= new ArrayList<PropertieDescription>();
	 
//	    private Object prodescLock = new Object();
	    public Method[] getDeclaredMethods()
		{
	    	
	    	if(declaredMethods == NULL_M)
    				return null;
    		return declaredMethods;
    		
		
		}
	    
	    private void init()
	    {
//	    	if(declaredFields == null)
	    	{
//	    		synchronized(prodescLock)
    			{
    				if(declaredFields == null)
    	    		{
    					Field[] retfs = null;
		    			try
		    			{		    				
		    				retfs = getRecursiveDeclaredFileds();
		    				
		    			}
		    			catch(Exception e)
		    			{
		    				log.error(e);
//		    				declaredFields =NULL;
		    			}
		    			List<PropertieDescription> retpropertyDescriptors = null;
		    			try
    	    			{		    				
		    				retpropertyDescriptors = initBeaninfo(retfs);	 
		    				
    	    			}
    	    			catch(Exception e)
    	    			{
    	    				retpropertyDescriptors = NULL_P;
    	    			}
		    			Method[] retmethods = null;
		    			try
		    			{		    				
		    				retmethods = getRecursiveDeclaredMehtods();		    			
		    			}
		    			catch(Exception e)
		    			{
		    				log.error(e);
		    			}
		    			this.propertyDescriptors = retpropertyDescriptors;
		    			if(retmethods == null)
	    					declaredMethods = NULL_M;
	    				else
	    					declaredMethods = retmethods;
		    			if(retfs == null)
	    					declaredFields = NULL;
	    				else
	    					declaredFields = retfs;
		    			
    	    		}
    			}
	    	}
	    }
	    public Field[] getDeclaredFields()
	    {
//	    	init();
    		if(declaredFields == NULL)
    			return null;
    		return declaredFields;    			
	    }
	    
	    public List<PropertieDescription> getPropertyDescriptors()
	    {
	    	return propertyDescriptors ;
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
	    
	    private Field getDeclaredField(Field[] declaredFields,String name)
	    {
//	    	    Field[] declaredFields = this.getDeclaredFields();
	    	    if(declaredFields == null)
	    	    	return null;
	    	    for(Field f:declaredFields)
	    	    {
	    	    	if(f.getName().equals(name))
	    	    		return f;
	    	    }
	    	    return null;
	    	
	    		
	    }
	    
	    
	    
	    private List<Field> copyFields(Field[] declaredFields)
	    {
	    	if(declaredFields == null || declaredFields.length == 0)
	    		return null;
	    	List<Field> copys = new ArrayList<Field>(declaredFields.length);
	    	for(int i =0;i < declaredFields.length; i++)
	    	{
	    		copys.add(declaredFields[i]);
	    	}
	    	return copys;
	    }
	    /**
	     * 如果包含名称为name的字段，由于该字段在BeanInfo中已经存在，则将该字段从fileds副本中移除，以便将
	     * 最后剩下的字段生成get/set方法
	     * @param name
	     * @param fields
	     * @return
	     */
	    private Field containFieldAndRemove(String name,List<Field> fields)
		{
	    	
			for(int i = 0; i < fields.size(); i ++)
			{
				Field p = fields.get(i);
				if(p.getName().equals(name))
				{
					fields.remove(i);
					i --;
					return p;
				}
			}
			return null;
		}
	    
	    public Class getClazz()
	    {
	    	return this.clazz;
	    }
	    private List<PropertieDescription> initBeaninfo(Field[] declaredFields)
	    {
	    	List<PropertieDescription> propertyDescriptors = null;
	    	
	    	BeanInfo beanInfo = null;
			try
			{
				beanInfo = Introspector.getBeanInfo(this.clazz);
				
				PropertyDescriptor[] attributes = beanInfo.getPropertyDescriptors();
//				List<PropertieDescription> asm = new ArrayList<PropertieDescription>();
				if(attributes == null || attributes.length == 0)
				{
					if(declaredFields == null || declaredFields.length == 0)
					{
						propertyDescriptors = NULL_P;
					}
					else
					{
						propertyDescriptors = new ArrayList<PropertieDescription>(declaredFields.length);
						for(Field f:declaredFields)
						{
							propertyDescriptors.add(buildPropertieDescription( f));
						}
//						if(asm.size() > 0)
//						{
//							this.clazz = AsmUtil.addGETSETMethodForClass(asm, this.clazz);
//						}
					}
					return propertyDescriptors;
				}
				else
				{
					List<Field> copyFields = copyFields(declaredFields);
					propertyDescriptors = new ArrayList<PropertieDescription>();
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
						
						propertyDescriptors.add( buildPropertieDescription(declaredFields, copyFields,attr));
					}
					
					if(copyFields.size() > 0)
					{
						for(Field f:copyFields)
						{
							propertyDescriptors.add(  buildPropertieDescription( f));
						}
					}
//					if(asm.size() > 0)
//					{
//						this.clazz = AsmUtil.addGETSETMethodForClass(asm, this.clazz);
//					}
				}
				
				
				
			}
			catch (Exception e)
			{
				propertyDescriptors = NULL_P;
				log.error("Init Beaninfo[" + clazz.getName() + "] failed:",e);
			}
			return propertyDescriptors;
			
	    }
	    
	    private PropertieDescription buildPropertieDescription(Field[] declaredFields,List<Field> copeFields,PropertyDescriptor attr )
	    {
	    	Method wm = attr.getWriteMethod();
	    	Method rm = attr.getReadMethod();
	    	Field field = this.getDeclaredField(declaredFields,attr.getName());
	    	
	    	this.containFieldAndRemove(attr.getName(), copeFields) ;    	
	    	PropertieDescription pd = new PropertieDescription(attr.getPropertyType(),
	    			                    field,wm,
	    								rm,attr.getName());
//	    	if(field != null && (wm == null || rm == null))
//	    		asm.add(pd);
	    	
	    	return pd;
	    }
	    
	    private PropertieDescription buildPropertieDescription(Field field )
	    {
//	    	Method wm = null;
//	    	Method rm = null;
	    	PropertieDescription pd = new PropertieDescription(field.getType(),
	    									field,null,
	    									null,field.getName());
//	    	asm.add(pd);
	    	return pd;
	    }
		public PropertieDescription getPropertyDescriptor(String name)
		{
//			this.init();
			if(propertyDescriptors != NULL_P)
			{
				for(int i = 0; i < this.propertyDescriptors.size(); i ++)
				{
					PropertieDescription p = this.propertyDescriptors.get(i);
					if(p.getName().equals(name))
						return p;
				}
    			return null;
			}
    		else
    			return null;
    		
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

	
	public static Method[] getDeclaredMethods(Class target)	
	{
		ClassInfo  csinfo = getClassInfo(target);
		return csinfo.getDeclaredMethods();
	}
	
}
