package org.frameworkset.util;

import org.frameworkset.util.ClassUtil.ClassInfo;
import org.frameworkset.util.beans.BeanWrapper;

/**
 * {@link ConfigurablePropertyAccessor} implementation that directly accesses
 * instance fields. Allows for direct binding to fields instead of going through
 * JavaBean setters.
 *
 * <p>As of Spring 4.2, the vast majority of the {@link BeanWrapper} features have
 * been merged to {@link AbstractPropertyAccessor}, which means that property
 * traversal as well as collections and map access is now supported here as well.
 *
 * <p>A DirectFieldAccessor's default for the "extractOldValueForEditor" setting
 * is "true", since a field can always be read without side effects.
 *
 * @author Juergen Hoeller
 * @author Stephane Nicoll
 * @since 2.0
 * @see #setExtractOldValueForEditor
 * @see BeanWrapper
 
 */
public class DirectFieldAccessor {
	/** The wrapped object */
	private Object object;
	private ClassInfo classInfo;
	public DirectFieldAccessor(Object object)
	{
		this.object = object;
		classInfo = ClassUtil.getClassInfo(object.getClass());
		
	} 
	public Object getPropertyValue(String propertyName)   {
		return classInfo.getPropertyValue(object, propertyName);
	}
	
	public void setPropertyValue(String propertyName,Object value)   {
		classInfo.setPropertyValue(object, propertyName,value);
	}

}
