package org.frameworkset.util.beans;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;



public class BeanWrapper{
	private Object wrappedInstance ;
	private BeanInfo beanInfo ;
	public BeanWrapper(Object target) {
		this.setWrappedInstance(target);
	}


	/**
	 * Change the wrapped JavaBean object.
	 * @param obj the bean instance to wrap	
	 * in favor of recreating a BeanWrapper per target instance
	 */
	public void setWrappedInstance(Object obj)
	{
		this.wrappedInstance = obj;
		try {
			this.beanInfo = this.wrappedInstance ==null?null:Introspector.getBeanInfo(this.wrappedInstance.getClass());
		} catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	/**
	 * Return the bean instance wrapped by this object, if any.
	 * @return the bean instance, or <code>null</code> if none set
	 */
	public Object getWrappedInstance()
	{
		return this.wrappedInstance;
	}

	/**
	 * Return the type of the wrapped JavaBean object.
	 * @return the type of the wrapped bean instance,
	 * or <code>null</code> if no wrapped object has been set
	 */
	public Class getWrappedClass()
	{
		return wrappedInstance == null?null:wrappedInstance.getClass();
	}

	/**
	 * Obtain the PropertyDescriptors for the wrapped object
	 * (as determined by standard JavaBeans introspection).
	 * @return the PropertyDescriptors for the wrapped object
	 */
	public PropertyDescriptor[] getPropertyDescriptors()
	{
		return this.beanInfo ==null ? null:this.beanInfo.getPropertyDescriptors();
	}

	/**
	 * Obtain the property descriptor for a specific property
	 * of the wrapped object.
	 * @param propertyName the property to obtain the descriptor for
	 * (may be a nested path, but no indexed/mapped property)
	 * @return the property descriptor for the specified property
	 * @throws InvalidPropertyException if there is no such property
	 */
	public PropertyDescriptor getPropertyDescriptor(String propertyName) throws BeansException
	{
		PropertyDescriptor[] pds = this.getPropertyDescriptors();
		if(pds == null)
			return null;
		for(PropertyDescriptor pd:pds)
		{
			if(pd.getName().equals(propertyName))
				return pd;
		}
		return null;
	}


	public Class getPropertyType(String fixedField) {
		PropertyDescriptor pd = getPropertyDescriptor(fixedField);
		return pd == null?null:pd.getPropertyType();
	}


	public Object getPropertyValue(String field) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		PropertyDescriptor pd = getPropertyDescriptor(field);
		return pd == null?null:pd.getReadMethod().invoke(wrappedInstance, null);
	}


}
