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
package com.frameworkset.common.tag.pager;
import java.util.Map;

import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.util.SQLUtil.DBHashtable;
import com.frameworkset.util.ValueObjectUtil;
/**
 * 封装调用业务方法产生的集合中的值对象的Class句柄和public fields  
 * @author biaoping.yin
 * 2005-3-25
 * version 1.0
 */



public class ClassData 
{	
	private static final Logger log = Logger.getLogger(ClassData.class);
	
	/**
	 * 值对象
	 */
	private Object valueObject;
	private Map data = null;
	private Object mapkey = null;
	
	private boolean toUpercase = true;
	/**
//	 * 值对象定义的方法
//	 */
//	private Method[] methods;
//	private Field[] fields;
	public ClassData()
	{
	}
	
	
	public ClassData(Map data)
	{
		this.data = data;
		if(data instanceof DBHashtable)
		{
			
		}
		else
		{
			this.toUpercase = false;
		}
	}
	public ClassData(Map data,boolean toUpercase)
	{
	    this.data = data;
	    this.toUpercase = toUpercase;
	}
	/**
	 * 构建器，以一个值对象和Field数组作为参数，构建体内对私有的valueObject对象、fields
	 * 数组进行初始化
	 * @param valueObject - 值对象,用来初始化valueObjce变量
	 * @param fields - 值对象中外界可以访问到的所有属性
	 */
//	public ClassData(Object valueObject, 
//					 Method[] methods, 
//					 Field[] fields)
//	{
//		this.setValueObject(valueObject);
////		this.methods = methods;
////		this.fields = fields;
//	}
	
	public ClassData(Object valueObject)
	{
	    
		this.setValueObject(valueObject);
//		this.methods = valueObject.getClass().getMethods();
//		this.fields = valueObject.getClass().getFields();		
	}
	
	public ClassData(Object valueObject,Object mapkey,boolean toUpercase)
	{
	    
		this.setValueObject(valueObject);
		this.mapkey = mapkey;
		this.toUpercase = toUpercase;
//		this.methods = valueObject.getClass().getMethods();
//		this.fields = valueObject.getClass().getFields();		
	}
	/**
	 * Access method for the valueObject property.
	 * 
	 * @return   the current value of the valueObject property
	 */
	public Object getValueObject()
	{
		if(this.data != null)
		{
			
			return data;
		}
		return valueObject;
	}
	
	
	/**
	 * Sets the value of the valueObject property.
	 * 
	 * @param aValueObject the new value of the valueObject property
	 */
	private void setValueObject(Object aValueObject)
	{
		if(aValueObject == null)
			return;
		
		if((aValueObject instanceof DBHashtable))
		{
			this.data = (Map)aValueObject;
		}
		else if((aValueObject instanceof Map))
		{
			this.data = (Map)aValueObject;
			toUpercase = false;
		}
		else
		{
			valueObject = aValueObject;
		}
	}
	
	/**
	 * 从methods中查询给定字段的getter Method对象,如果找到则返回该对象，否则返回null或者抛出异常
	 * 
	 * @return Method
	 */
//	private Method seekMethod(String fieldName)
//	{
////		if (methods == null)
////			return null;
////		for (int i = 0; i < this.methods.length; i++)
////		{
////			if (methods[i]
////				.getName()
////				.equals(ValueObjectUtil.getMethodName(fieldName)))
////				return methods[i];
////		}
//		return null;
//	}
	/**
	 * 从fields中查询给定名称的Field对象,如果找到则返回该对象，否则返回null或者抛出异常
	 * 
	 * @return Field
	 */
//	private Field seekField(String fieldName)
//	{
////		if (fields == null)
////			return null;
////		for (int i = 0; i < this.fields.length; i++)
////		{
////			if (fields[i].getName().equals(fieldName))
////				return fields[i];
////		}
//		return null;
//	}
	

	/**
	 * 返回给定属性fieldName的值，
	 * 返回值得类型为Object
	 * @param fieldName - 属性名称
	 * @return Object
	 */
	public Object getValue(String fieldName)
	{
		if (fieldName == null)
			return null;
		/**
		 * 如果封装数据的值对象为Hashtable时直接从hashtable中获取数据，
		 * 否则通过值对象获取相应字段的值
		 */
		if(data != null)
		{
			if(this.toUpercase)
				return data.get(fieldName.toUpperCase());
			else
				return data.get(fieldName); 
		}
		else
		{
			
		    return ValueObjectUtil.getValue(valueObject,fieldName);
		}	
	}
	
    /**
     * @return Returns the data.
     */
    public Map getData() {
        return data;
    }
    /**
     * @param data The data to set.
     */
    public void setData(Map data) {
        this.data = data;
    }


	
	public Object getMapkey()
	{
	
		return mapkey;
	}


	
	
}
